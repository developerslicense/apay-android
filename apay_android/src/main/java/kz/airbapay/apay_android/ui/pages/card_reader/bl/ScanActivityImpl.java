package kz.airbapay.apay_android.ui.pages.card_reader.bl;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;

import java.util.List;

import kz.airbapay.apay_android.R;

public class ScanActivityImpl extends ScanBaseActivity {

	public static final String RESULT_CARD_NUMBER = "cardNumber";
	public static final String RESULT_EXPIRY_MONTH = "expiryMonth";
	public static final String RESULT_EXPIRY_YEAR = "expiryYear";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.irdcs_activity_scan_card);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
				requestPermissions(new String[]{Manifest.permission.CAMERA}, 110);
			} else {
				mIsPermissionCheckDone = true;
			}
		} else {
			// no permission checks
			mIsPermissionCheckDone = true;
		}

		setViewIds(R.id.cardRectangle, R.id.shadedBackground, R.id.texture,
				R.id.cardNumber, R.id.expiry);
	}

	@Override
	protected void onCardScanned(String numberResult, String month, String year) {
		Intent intent = new Intent();
		intent.putExtra(RESULT_CARD_NUMBER, numberResult);
		intent.putExtra(RESULT_EXPIRY_MONTH, month);
		intent.putExtra(RESULT_EXPIRY_YEAR, year);
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	public void onPrediction(final String number, final Expiry expiry, final Bitmap bitmap,
							 final List<DetectedBox> digitBoxes, final DetectedBox expiryBox) {

		super.onPrediction(number, expiry, bitmap, digitBoxes, expiryBox);
	}

}
