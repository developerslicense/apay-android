package kz.airbapay.apay_android.ui.pages.card_reader.bl;

import android.hardware.Camera;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

public class CameraThread extends Thread {

	private OnCameraOpenListener listener;

	public synchronized void startCamera(OnCameraOpenListener listener) {
		this.listener = listener;
		notify();
	}

	private synchronized OnCameraOpenListener waitForOpenRequest() {
		while (this.listener == null) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		OnCameraOpenListener listener = this.listener;
		this.listener = null;
		return listener;
	}

	@Override
	public void run() {
		while (true) {
			final OnCameraOpenListener listener = waitForOpenRequest();

			Camera camera = null;
			try {
				camera = Camera.open();
			} catch (Exception e) {
				Log.e("CameraThread", "failed to open Camera");
				e.printStackTrace();
			}

			final Camera resultCamera = camera;
			Handler handler = new Handler(Looper.getMainLooper());
			handler.post(() -> listener.onCameraOpen(resultCamera));
		}
	}
}
