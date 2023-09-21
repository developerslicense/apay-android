package kz.airbapay.apay_android.ui.pages.card_reader.bl;

import android.graphics.Bitmap;

import java.util.List;

interface OnScanListener {

	void onPrediction(final String number, final Bitmap bitmap, final List<DetectedBox> digitBoxes);
	void onFatalError();

}
