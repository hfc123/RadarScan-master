package com.cheerchip.radarscanlibs.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import com.cheerchip.radarscanlibs.R;


/**
 * Created by Mr_immortalZ on 2016/5/2.
 * email : mr_immortalz@qq.com
 */
public class RadarView extends View {
    private Paint mPaintLine;//画圆线需要用到的paint
    private Paint mPaintCircle;//画圆需要用到的paint
    private Paint mPaintScan;//画扫描需要用到的paint

    private int mWidth, mHeight;//整个图形的长度和宽度

    private Matrix matrix = new Matrix();//旋转需要的矩阵
    private int scanAngle;//扫描旋转的角度
    private Shader scanShader;//扫描渲染shader
    private Bitmap centerBitmap;//最中间icon

    //每个圆圈所占的比例
    private static float[] circleProportion = {1 / 13f, 2 / 13f, 3 / 13f, 4 / 13f, 5 / 13f, 6 / 13f};
    private int scanSpeed = 5;

    private int currentScanningCount;//当前扫描的次数
    private int currentScanningItem;//当前扫描显示的item
    private int maxScanItemCount;//最大扫描次数
    private boolean startScan = false;//只有设置了数据后才会开始扫描

    //不停的自己给自己发送消息重绘产生旋转的效果
    private Runnable run = new Runnable() {
        @Override
        public void run() {
            scanAngle = (scanAngle + scanSpeed) % 360;
            matrix.postRotate(scanSpeed, mWidth / 2, mHeight / 2);
            invalidate();
            postDelayed(run, 130);
        }
    };

    public RadarView(Context context) {
        this(context, null);
    }

    public RadarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        mPaintLine = new Paint();
        mPaintLine.setColor(getResources().getColor(R.color.line_color_blue));
        mPaintLine.setAntiAlias(true);
        mPaintLine.setStrokeWidth(1);
        mPaintLine.setStyle(Paint.Style.STROKE);

        mPaintCircle = new Paint();
        mPaintCircle.setColor(Color.WHITE);
        mPaintCircle.setAntiAlias(true);

        mPaintScan = new Paint();
        mPaintScan.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureSize(widthMeasureSpec), measureSize(widthMeasureSpec));
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mWidth = mHeight = Math.min(mWidth, mHeight);
        //中间的头像
        centerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.circle_photo);
        //设置扫描渲染的shader
        scanShader = new SweepGradient(mWidth / 2, mHeight / 2,
                new int[]{Color.TRANSPARENT, Color.parseColor("#84B5CA")}, null);
    }

    private int measureSize(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = 300;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawCircle(canvas);
        drawScan(canvas);
        drawCenterIcon(canvas);
    }

    /**
     * 绘制圆线圈
     *
     * @param canvas
     */
    private void drawCircle(Canvas canvas) {
        canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth * circleProportion[1], mPaintLine);     // 绘制小圆
        canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth * circleProportion[2], mPaintLine);   // 绘制中圆
        canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth * circleProportion[3], mPaintLine); // 绘制中大圆
        canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth * circleProportion[4], mPaintLine);  // 绘制大圆
        canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth * circleProportion[5], mPaintLine);  // 绘制大大圆
    }

    /**
     * 绘制扫描
     *
     * @param canvas
     */
    private void drawScan(Canvas canvas) {
        canvas.save();
        mPaintScan.setShader(scanShader);
        canvas.concat(matrix);
        canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth * circleProportion[4], mPaintScan);
        canvas.restore();
    }

    /**
     * 绘制最中间的图标
     *
     * @param canvas
     */
    private void drawCenterIcon(Canvas canvas) {
        canvas.drawBitmap(centerBitmap, null,
                new Rect((int) (mWidth / 2 - mWidth * circleProportion[0]), (int) (mHeight / 2 - mWidth * circleProportion[0]),
                        (int) (mWidth / 2 + mWidth * circleProportion[0]), (int) (mHeight / 2 + mWidth * circleProportion[0])), mPaintCircle);
    }
    //调用停止方法
    public void stopScan(){
        removeCallbacks(run);
    }
   //调用开始方法
    public void startScan(){
        post(run);
    }

    public void setMaxScanItemCount(int maxScanItemCount) {
        this.maxScanItemCount = maxScanItemCount;
    }

}
