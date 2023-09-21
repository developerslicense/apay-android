package kz.airbapay.apay_android.ui.pages.card_reader;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import kz.airbapay.apay_android.R;
import kz.airbapay.apay_android.ui.pages.card_reader.bl.ScanBaseActivity;

public class ScanActivityImpl extends ScanBaseActivity {

	public static final String RESULT_CARD_NUMBER = "cardNumber";

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

		setViewIds(R.id.cardRectangle, R.id.shadedBackground, R.id.texture);
	}

	@Override
	protected void onCardScanned(String numberResult) {
		System.out.println("aaaaa " + numberResult);
		Intent intent = new Intent();
		intent.putExtra(RESULT_CARD_NUMBER, numberResult);
		setResult(RESULT_OK, intent);
		finish();
	}

}
