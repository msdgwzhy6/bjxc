package com.zxwl.frame;

import android.app.Activity;

import java.util.Stack;

/**
 * Copyright 2015 蓝色互动. All rights reserved.
 * author：hw
 * data:2017/4/11 08:14
 * ClassName: ${Class_Name}
 */

public final class AppManager {
    private static Stack<Activity> activityStack;

    private static AppManager instance;

    private AppManager() {

    }

    /**
     * 获得AppManager的实例
     *
     * @return
     */
    public static AppManager getInstance() {
        if (null == instance) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * 添加activity到栈中
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 获得最后一个压栈的activity，就是最上面的一个activity
     *
     * @return
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的activity
     *
     * @param activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的activity
     *
     * @param cls
     */
    public void finishActivity(Class<?> cls) {
        Activity activity = null;
        for (Activity a : activityStack) {
            if (a.getClass().equals(cls)) {
                activity = a;
                break;
            }
        }
        if (activity != null)
            finishActivity(activity);
    }

    /**
     * 获得指定类名的activity
     *
     * @param cls
     * @return
     */
    public Activity getActivity(Class<?> cls) {
        /*
         * for (Activity activity : activityStack) { if
		 * (activity.getClass().equals(cls)) { return activity; } }
		 */
        // 应该由栈顶往下去遍历拿最近的activity去比较
        if (null != activityStack && activityStack.size() != 0) {
            int size = activityStack.size();
            for (int i = size - 1; i >= 0; i--) {
                Activity activity = activityStack.get(i);
                if (activity.getClass().equals(cls)) {
                    return activity;
                }
            }
        }
        return null;
    }

    /**
     * 结束所有的activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出程序
     */
    public void appExit() {
        try {
            finishAllActivity();
            //System.exit(0);
        } catch (Exception e) {

        }
    }

    /**
     * 获取activity 栈
     */
    public Stack<Activity> getStack() {
        return activityStack;
    }

}
