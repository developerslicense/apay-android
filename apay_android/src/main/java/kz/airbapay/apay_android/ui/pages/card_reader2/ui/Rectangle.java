package kz.airbapay.apay_android.ui.pages.card_reader2.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import kz.airbapay.apay_android.ui.pages.card_reader2.models.Quad;

public class Rectangle extends View {
    private Paint paint = new Paint();
    private Quad corners;

    public Rectangle(Context context) {
        super(context);
    }
    public Rectangle(
            Context context,
            Quad corners
    ) {
        super(context);
        this.corners = corners;
    }
/*
    TOP_LEFT=PointF(308.266, 221.354),
    TOP_RIGHT=PointF(324.562, 190.12),
    BOTTOM_LEFT=PointF(310.982, 357.154)}
    BOTTOM_RIGHT=PointF(325.92, 343.574),
    */

    /*

        TOP_LEFT=PointF(100.0, 100.0),
        TOP_RIGHT=PointF(980.0, 100.0),
        BOTTOM_LEFT=PointF(100.0, 579.0)
        BOTTOM_RIGHT=PointF(980.0, 579.0),
      */

    @Override
    protected void onDraw(Canvas canvas) { // Override the onDraw() Method
        super.onDraw(canvas);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(2);

        //center
//        int x0 = canvas.getWidth() / 2;
//        int y0 = canvas.getHeight() / 2;
//        int dx = (int) (canvas.getWidth() / 2.5);
//        int dy = (int) (canvas.getHeight() / 2.9);
//        //draw guide box
//        canvas.drawRect(x0 - dx, y0 - dy, x0 + dx, y0 + dy, paint);

        canvas.drawRect(

                corners.getTopLeftCorner().x, corners.getTopRightCorner().y,
                corners.getBottomRightCorner().x, corners.getBottomLeftCorner().y,
                paint
        );
    }
}