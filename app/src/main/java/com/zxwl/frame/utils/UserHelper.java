package com.zxwl.frame.utils;


import com.zxwl.frame.bean.UserInfo;
import com.zxwl.frame.utils.sharedpreferences.PreferencesHelper;


public class UserHelper {

    public static void saveUser(UserInfo user) {
        PreferencesHelper.saveData(user);
    }

    public static UserInfo getSavedUser() {
        return PreferencesHelper.getData(UserInfo.class);
    }

    public static String getUserId() {
        return getSavedUser().id;
    }

    public static void clearUserInfo(Class user) {
        PreferencesHelper.remove(user);
    }
}
