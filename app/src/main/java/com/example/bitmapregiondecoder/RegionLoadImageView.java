package com.example.bitmapregiondecoder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;
import java.io.IOException;
import java.io.InputStream;

public class RegionLoadImageView extends AppCompatImageView {
    private BitmapRegionDecoder mRegionDecoder;
    private BitmapFactory.Options mOptions = new BitmapFactory.Options();
    private Bitmap mBitmap;
    private Matrix mMatrix = new Matrix();
    private InputStream inputStream;
    private int rectLeft, rectTop, rectRight, rectBottom;
    private int measuredWidht, measuredHeight;
    private float dx, dy, downX, downY;

    public RegionLoadImageView(Context context) {
        super(context);
    }

    public RegionLoadImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RegionLoadImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    public void setImageRes(int imageRes) {
        inputStream = getResources().openRawResource(imageRes);
        setRegionDecoder(inputStream);
    }

    /**
     * 设置区域解码器
     */
    @RequiresApi(api = Build.VERSION_CODES.S)
    public void setRegionDecoder(InputStream inputStream) {
        try {
            mRegionDecoder = BitmapRegionDecoder.newInstance(inputStream, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measuredWidht = getMeasuredWidth();
        measuredHeight = getMeasuredHeight();
        updateRegion(0, 0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                dx = downX - event.getRawX();
                dy = downY - event.getRawY();
                updateRegion((int)dx, (int)dy);
                break;
        }
        return true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        try {
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mOptions.inBitmap = mBitmap;
        Rect rect = new Rect(rectLeft, rectTop, rectRight, rectBottom);
        mMatrix.setScale(1, 1);
        mBitmap = mRegionDecoder.decodeRegion(rect, mOptions);
        canvas.drawBitmap(mBitmap, mMatrix, null);
    }

    public void updateRegion(int x, int y) {
        rectLeft = x;
        rectTop = y;
        rectRight = x + measuredWidht;
        rectBottom = y + measuredHeight;
        invalidate();
    }
}
