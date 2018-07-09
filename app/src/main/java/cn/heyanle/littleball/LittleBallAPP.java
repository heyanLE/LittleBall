package cn.heyanle.littleball;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import java.io.PrintWriter;
import java.io.StringWriter;

import cn.heyanle.littleball.activities.NulActivity;
import cn.heyanle.littleball.utils.HeLog;

public class LittleBallAPP extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //CrashReport.initCrashReport(getApplicationContext(), "e8e4fd469c", C.IS_DEBUG);
        new CrashHandler(this);
    }
}

class CrashHandler implements Thread.UncaughtExceptionHandler {

    private Context context;

    public CrashHandler(Context context) {
        this.context = context;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {

        StringWriter writer = new StringWriter();
        try {
            PrintWriter printWriter = new PrintWriter(writer);
            throwable.printStackTrace(printWriter);
            Throwable cause = throwable.getCause();
            while (cause != null) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        HeLog.e("ThreadCrash", writer.toString(), this);


        Intent intent = new Intent();
        intent.setClass(context, NulActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(NulActivity.NUL_KEY, writer.toString());
        context.startActivity(intent);

        android.os.Process.killProcess(android.os.Process.myPid());

    }
}
