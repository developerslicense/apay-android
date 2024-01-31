package kz.airbapay.apay_android.ui.pages.card_reader.bl.card_number_detection;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Looper;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.renderscript.Type;

import java.util.LinkedList;

import kz.airbapay.apay_android.ui.pages.card_reader.bl.camera.OnScanListener;
import kz.airbapay.apay_android.ui.pages.card_reader.bl.image.ImageConverterUtils;

public class MachineLearningThread implements Runnable {

	class RunArguments {

		private final byte[] mFrameBytes;
		private final OnScanListener mScanListener;
		private final Context mContext;
		private final int mWidth;
		private final int mHeight;
		private final int mSensorOrientation;
		private final float mRoiCenterYRatio;

		RunArguments(byte[] frameBytes,
					 int width,
					 int height,
					 int sensorOrientation,
					 OnScanListener scanListener,
					 Context context,
					 float roiCenterYRatio
		) {
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

		Bitmap bm = ImageConverterUtils.getBitmap(
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
