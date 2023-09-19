package kz.airbapay.apay_android.ui.pages.card_reader.bl;

import android.graphics.Bitmap;

interface OnObjectListener {

	void onPrediction(final Bitmap bitmap, int imageWidth,
					  int imageHeight);

	void onObjectFatalError();

}
