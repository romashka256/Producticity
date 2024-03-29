package com.producticity.ui.customviews;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.producticity.R;
import com.producticity.utils.commonutils.ViewUtils;

public class DottedLine extends View {

    private Paint mPaint;
    private LinearGradient linearGradient;

    public DottedLine(Context context) {
        super(context);
        init();
    }

    public DottedLine(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DottedLine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public DottedLine(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        Resources res = getResources();

        mPaint = new Paint();

        mPaint.setColor(res.getColor(R.color.totalwhite));
        int size = ViewUtils.dpToPx(getResources(), 5);
        int gap = ViewUtils.dpToPx(getResources(), 20);
        mPaint.setStyle(Paint.Style.FILL);

        // To get actually round dots, we define a circle...
        Path path = new Path();
        path.addCircle(0, 0, size, Path.Direction.CW);
        // ...and use the path with the circle as our path effect
        mPaint.setPathEffect(new PathDashPathEffect(path, gap, 0, PathDashPathEffect.Style.ROTATE));

        // If we don't render in software mode, the dotted line becomes a solid line.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        linearGradient = new LinearGradient(0f, 0f, 0f, getHeight(), Color.TRANSPARENT, Color.WHITE, Shader.TileMode.MIRROR);
        mPaint.setShader(linearGradient);

        canvas.drawLine(getWidth() / 2, getHeight(), getWidth() / 2, 0, mPaint);
    }
}