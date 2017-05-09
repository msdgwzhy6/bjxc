package com.zxwl.frame.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.zxwl.frame.R;
import com.zxwl.frame.views.lockscreen.LockScreenService;
import com.zxwl.frame.views.lockscreen.SlideLockView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * if the screen is locked, this Activity will show.
 *
 * @author
 */
public class LockScreenActivity extends Activity implements Runnable{

    public static boolean isLocked = false;

    private boolean stopThread;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm ");
    private Handler handler;
    private TextView textView1, textView2, textView3;
    private Thread thread;
    public static String[] WEEK = {"星期天","星期一","星期二","星期三","星期四","星期五","星期六"};
    public static final int WEEKDAYS = 7;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); // 不要title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // 全屏显示
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// 强制为横屏.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        setContentView(R.layout.activity_lock_screen);
        isLocked = true;
        SlideLockView slideView = (SlideLockView) findViewById(R.id.slideLockView);
        slideView.setLockListener(new SlideLockView.OnLockListener() {
            @Override
            public void onOpenLockSuccess() {
                isLocked = false;
                virbate();
                startAvtivity();
                finish();
            }
        });

        startService(new Intent(this, LockScreenService.class));
        init();
    }

    private void init() {
        // TODO Auto-generated method stub
        textView1 = (TextView) findViewById(R.id.time1);
        textView2 = (TextView) findViewById(R.id.time2);
        textView3 = (TextView) findViewById(R.id.time3);

        handler = new Handler() {
            public void handleMessage(Message msg) {
                String times = (String) msg.obj;
                String[] rs1 = times.split(" ");
                String[] rs2 = rs1[1].split(":");
                Log.i("TAG","小时==="+rs2[0]);
                textView1.setText(rs2[0]);
                textView2.setText(rs2[1]);
                textView3.setText(rs1[0] + "\t\t" + rs1[2]);
            }
        };
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {
            while (!stopThread) {
                String str = sdf.format(new Date());
                Date parse = sdf.parse(str);
                String week = getWeek(parse);Log.i("TAG","week==="+week);
                String date = str + week;
                handler.sendMessage(handler.obtainMessage(100, date));
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected void onDestroy() {
        stopThread = true;
        super.onDestroy();
    }

    /**
     * 震动
     */

    private void virbate() {
        Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) { //监控/拦截/屏蔽返回键
            startAvtivity();
            //finish();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            startAvtivity();
            //finish();
        } else if (keyCode == KeyEvent.KEYCODE_HOME) {
            //由于Home键为系统键，此处不能捕获，需要重写onAttachedToWindow()
            startAvtivity();
            //finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void startAvtivity() {
        Intent intent = new Intent(this,ContactBookDialogActivity.class);
        startActivity(intent);
    }

    public static String getWeek(Date parse) {
        String Week = "星期";
        Calendar c = Calendar.getInstance();

        c.setTime(parse);
        int dayIndex = c.get(Calendar.DAY_OF_WEEK);
        if (dayIndex < 1 || dayIndex > WEEKDAYS) {
            return null;
        }

        return WEEK[dayIndex - 1];
    }

}
