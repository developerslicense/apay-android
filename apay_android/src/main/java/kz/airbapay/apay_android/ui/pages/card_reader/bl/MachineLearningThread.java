package kz.airbapay.apay_android.ui.pages.card_reader.bl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.renderscript.Type;

import java.util.LinkedList;

public class MachineLearningThread implements Runnable {

	class RunArguments {

		private final byte[] mFrameBytes;
		private final OnScanListener mScanListener;
		private final Context mContext;
		private final int mWidth;
		private final int mHeight;
		private final int mSensorOrientation;
		private final float mRoiCenterYRatio;

		RunArguments(byte[] frameBytes, int width, int height,
					 int sensorOrientation, OnScanListener scanListener, Context context,
					 float roiCenterYRatio) {
			mFrameBytes = frameBytes;
			mWidth = width;
			mHeight = height;
			mScanListener = scanListener;
			mContext = context;
			mSensorOrientation = sensorOrientation;
			mRoiCenterYRatio = roiCenterYRatio;
		}
	}

	private LinkedList<RunArguments> queue = new LinkedList<>();

	public MachineLearningThread() {
		super();
	}

	public synchronized void post(byte[] bytes, int width, int height, int sensorOrientation,
						   OnScanListener scanListener, Context context, float roiCenterYRatio) {
		RunArguments args = new RunArguments(bytes, width, height, sensorOrientation,
				scanListener, context, roiCenterYRatio);
		queue.push(args);
		notify();
	}

	// from https://stackoverflow.com/questions/43623817/android-yuv-nv12-to-rgb-conversion-with-renderscript
	// interestingly the question had the right algorithm for our format (yuv nv21)
	private Bitmap YUV_toRGB(byte[] yuvByteArray, int W, int H, Context ctx) {
		RenderScript rs = RenderScript.create(ctx);
		ScriptIntrinsicYuvToRGB yuvToRgbIntrinsic = ScriptIntrinsicYuvToRGB.create(rs,
				Element.U8_4(rs));

		Type.Builder yuvType = new Type.Builder(rs, Element.U8(rs)).setX(yuvByteArray.length);
		Allocation in = Allocation.createTyped(rs, yuvType.create(), Allocation.USAGE_SCRIPT);

		Type.Builder rgbaType = new Type.Builder(rs, Element.RGBA_8888(rs)).setX(W).setY(H);
		Allocation out = Allocation.createTyped(rs, rgbaType.create(), Allocation.USAGE_SCRIPT);

		in.copyFrom(yuvByteArray);

		yuvToRgbIntrinsic.setInput(in);
		yuvToRgbIntrinsic.forEach(out);
		Bitmap bmp = Bitmap.createBitmap(W, H, Bitmap.Config.ARGB_8888);
		out.copyTo(bmp);

		yuvToRgbIntrinsic.destroy();
		rs.destroy();
		in.destroy();
		out.destroy();
		return bmp;
	}

	private Bitmap getBitmap(byte[] bytes, int width, int height, int sensorOrientation,
							 float roiCenterYRatio, Context ctx) {
		final Bitmap bitmap = YUV_toRGB(bytes, width, height, ctx);

		sensorOrientation = sensorOrientation % 360;

		double h;
		double w;
		int x;
		int y;

		if (sensorOrientation == 0) {
			w = bitmap.getWidth();
			h = w * 302.0 / 480.0;
			x = 0;
			y = (int) Math.round(((double) bitmap.getHeight()) * roiCenterYRatio - h * 0.5);
		} else if (sensorOrientation == 90) {
			h = bitmap.getHeight();
			w = h * 302.0 / 480.0;
			y = 0;
			x = (int) Math.round(((double) bitmap.getWidth()) * roiCenterYRatio - w * 0.5);
		} else if (sensorOrientation == 180) {
			w = bitmap.getWidth();
			h = w * 302.0 / 480.0;
			x = 0;
			y = (int) Math.round(((double) bitmap.getHeight()) * (1.0 - roiCenterYRatio) - h * 0.5);
		} else {
			h = bitmap.getHeight();
			w = h * 302.0 / 480.0;
			x = (int) Math.round(((double) bitmap.getWidth()) * (1.0 - roiCenterYRatio) - w * 0.5);
			y = 0;
		}

		// make sure that our crop stays within the image
		if (x < 0) {
			x = 0;
		}
		if (y < 0) {
			y = 0;
		}
		if ((x + w) > bitmap.getWidth()) {
			x = bitmap.getWidth() - (int) w;
		}
		if ((y + h) > bitmap.getHeight()) {
			y = bitmap.getHeight() - (int) h;
		}

		Bitmap croppedBitmap = Bitmap.createBitmap(bitmap, x, y, (int) w, (int) h);

		Matrix matrix = new Matrix();
		matrix.postRotate(sensorOrientation);
		Bitmap bm = Bitmap.createBitmap(croppedBitmap, 0, 0, croppedBitmap.getWidth(), croppedBitmap.getHeight(),
				matrix, true);

		croppedBitmap.recycle();
		bitmap.recycle();

		return bm;
	}

	private synchronized RunArguments getNextImage() {
		while (queue.size() == 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return queue.pop();
	}

	private void runOcrModel(final Bitmap bitmap, final RunArguments args) {
		final OCR ocr = new OCR();
		final String number = ocr.predict(bitmap, args.mContext);
		final boolean hadUnrecoverableException = ocr.hadUnrecoverableException;

		Handler handler = new Handler(Looper.getMainLooper());
		handler.post(() -> {
			try {
				if (args.mScanListener != null) {
					if (hadUnrecoverableException) {
						args.mScanListener.onFatalError();
					} else {
						args.mScanListener.onPrediction(number, bitmap, ocr.digitBoxes);
					}
				}
			} catch (Error | Exception e) {
				// prevent callbacks from crashing the app, swallow it
				e.printStackTrace();
			}
		});
	}

	private void runModel() {
		final RunArguments args = getNextImage();

		Bitmap bm = getBitmap(
				args.mFrameBytes,
				args.mWidth,
				args.mHeight,
				args.mSensorOrientation,
				args.mRoiCenterYRatio,
				args.mContext
		);

		runOcrModel(bm, args);
	}

	@Override
	public void run() {
		while (true) {
			try {
				runModel();
			} catch (Error | Exception e) {
				// center field exception handling, make sure that the ml thread keeps running
				e.printStackTrace();
			}
		}
	}
}
