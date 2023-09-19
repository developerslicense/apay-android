package kz.airbapay.apay_android.ui.pages.card_reader.bl;

import android.content.Context;
import android.util.AttributeSet;

public class OverlayNoCorners extends Overlay {

	public OverlayNoCorners(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.drawCorners = false;
	}

}
