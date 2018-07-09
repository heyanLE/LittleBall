package cn.heyanle.littleball.services;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.service.notification.NotificationListenerService;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tencent.bugly.crashreport.CrashReport;

import cn.heyanle.littleball.R;
import cn.heyanle.littleball.plugin.IntLittleBall;
import cn.heyanle.littleball.plugin.data.Face;
import cn.heyanle.littleball.pluginer.PluginLoader;
import cn.heyanle.littleball.pluginer.PluginManager;
import cn.heyanle.littleball.utils.HeLog;
import cn.heyanle.littleball.view.ArcView;

public class NotificationService extends NotificationListenerService implements IntLittleBall{


    PluginManager manager;

    private boolean nowDirection = true;
    private boolean isWindow = false;

    private boolean isTouch = false;


    //View相关对象
    CardView cardView;
    //FrameLayout frameLayout;
    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
    WindowManager windowManager = null;

    LinearLayout windowView;
    WindowManager.LayoutParams layoutParamsWindow = new WindowManager.LayoutParams();
    LinearLayout windowContentView;
    ArcView arcView;

    //View相关参数
    private float x = 0f;//相对于坐标系的绝对位置
    private float y = 0f;

    private float pX = 0.00f;
    private float pY = 0.00f;//相对于坐标轴的相对位置

    private float touchX = 0;
    private float touchY = 0;//点击坐标关于View的相对坐标

    private float xPixels = 0f;
    private float yPixels = 0f;//坐标系长度

    private float widthPixels = 0f;
    private float heightPixels = 0f;

    private int ballSize = 200;//View大小

    @Override
    public void onCreate() {
        super.onCreate();
        windowManager = (WindowManager) getApplication().getSystemService(Application.WINDOW_SERVICE);

        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        xPixels = dm.widthPixels;
        yPixels = dm.heightPixels;

        nowDirection = !(xPixels > yPixels);

        widthPixels = Math.min(xPixels,yPixels);
        heightPixels = Math.min(yPixels,xPixels);

        initView();

        showView();

        manager = new PluginLoader(this).loadInfo().loadPlugin(this);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        xPixels = dm.widthPixels;
        yPixels = dm.heightPixels;

        x = xPixels * pX;
        y = yPixels * pY;

        nowDirection = !(xPixels > yPixels);

        updateView();

        if (!nowDirection) arcView.setVisibility(View.GONE);
        else arcView.setVisibility(View.VISIBLE);


    }

    private void showView(){
        try{
            windowManager.addView(cardView,layoutParams);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void dismissView(){
        try {
            windowManager.removeView(cardView);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void showWindow(){
        try{
            if (!nowDirection) arcView.setVisibility(View.GONE);
            else arcView.setVisibility(View.VISIBLE);
            windowManager.addView(windowView,layoutParamsWindow);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void dismissWindow(){
        try {
            windowManager.removeView(windowView);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void updateView(){

        try {
            layoutParams.width = ballSize;
            layoutParams.height = ballSize;
            cardView.setRadius(ballSize/2);
            layoutParams.x = (int)x;
            layoutParams.y = (int)y;
            windowManager.updateViewLayout(cardView,layoutParams);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private void initView(){
        layoutParams.windowAnimations = android.R.style.Animation_Translucent;


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;


        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.width = ballSize;
        layoutParams.height = ballSize;
        layoutParams.gravity = Gravity.START | Gravity.TOP;

        cardView = new CardView(this);
        cardView.setRadius(ballSize/2);
        cardView.setElevation(20);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //CrashReport.testJavaCrash();

                showWindow();

            }
        });

        cardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        touchX = motionEvent.getX();
                        touchY = motionEvent.getY();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isTouch = true;
                            }
                        },1000);

                        break;
                    case MotionEvent.ACTION_MOVE:
                        int[] screenInt = new int[2];
                        int xxx = layoutParams.y;
                        cardView.getLocationOnScreen(screenInt);

                        int high = screenInt[1] - xxx;

                        x = (motionEvent.getRawX() - touchX);
                        y = (motionEvent.getRawY() - touchY - high);

                        HeLog.i("layoutParams.x",layoutParams.x + "",this);
                        HeLog.i("layoutParams.y",layoutParams.y + "",this);

                        updateView();

                        isTouch = true;

                        break;
                    case MotionEvent.ACTION_UP:

                        pX = layoutParams.x/xPixels;
                        pY = layoutParams.y/yPixels;

                        if (isTouch) {
                            isTouch = false;
                            return true;
                        }
                }
                return false;
            }
        });

        LayoutInflater inflater = LayoutInflater.from(getApplication());
        windowView = (LinearLayout) inflater.inflate(R.layout.service_main_windows,null);
        windowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissWindow();
            }
        });

        layoutParamsWindow.windowAnimations = R.style.WindowsAnimationVertical;


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            layoutParamsWindow.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            layoutParamsWindow.type = WindowManager.LayoutParams.TYPE_PHONE;

        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        layoutParamsWindow.format = PixelFormat.RGBA_8888;
        layoutParamsWindow.width = (int)widthPixels;
        layoutParamsWindow.height = (int)(widthPixels + 100);
        layoutParamsWindow.gravity = Gravity.CENTER | Gravity.BOTTOM;

        arcView = windowView.findViewById(R.id.service_main_arcView);
        arcView.getLayoutParams().height = 100;
        arcView.getLayoutParams().width = (int)widthPixels;

        windowContentView = windowView.findViewById(R.id.service_main_content);
        windowContentView.getLayoutParams().width = (int) widthPixels;
        windowContentView.getLayoutParams().height = (int) widthPixels;


        //contentView.setAlpha(alpha);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismissView();
    }

    @Override
    public void addFace(Face face) {

    }

    @Override
    public void clearAllFace() {

    }

    @Override
    public void clearNowShowFace() {

    }

    @Override
    public Context getContext() {
        return this;
    }
}
