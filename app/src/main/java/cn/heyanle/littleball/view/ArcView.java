package cn.heyanle.littleball.view;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class ArcView extends View {

    private Paint mPaint = new Paint();


    private void initPaint() {
        mPaint.setColor(0xFF00BCD4);       //设置画笔颜色
        mPaint.setStyle(Paint.Style.FILL);  //设置画笔模式为填充
        mPaint.setStrokeWidth(10f);         //设置画笔宽度为10px
        mPaint.setAntiAlias(true);
    }

    public ArcView(Context context ){
        super(context);
        initPaint();
    }

    public ArcView(Context context, @Nullable AttributeSet attrs){
        super(context, attrs);
        initPaint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);

        float x = getWidth();
        float y = getHeight();

        canvas.drawCircle(x/2,y/2+x*x/(8*y),y/2+x*x/(8*y),mPaint);
        //canvas.drawRect(0, 0, x, y, mPaint);

    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        postInvalidate();
    }
}
