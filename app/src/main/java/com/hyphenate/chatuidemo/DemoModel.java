package com.hyphenate.chatuidemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by lzan13 on 2016/10/24.
 * Static config
 */
public class DemoModel {

    private static DemoModel instance;

    private static Context mContext;

    private DemoModel() {
    }

    public synchronized static DemoModel getInstance(Context context) {
        if (instance == null) {
            instance = new DemoModel();
        }
        mContext = context;
        return instance;
    }

    /**
     * Check notification bar notify switch
     */
    public boolean isNotification() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        return sharedPref.getBoolean("notification_switch", false);
    }

    /**
     * Check sound notify switch
     */
    public boolean isSoundNotification() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        return sharedPref.getBoolean("notification_sound_switch", false);
    }

    /**
     * Check vibrate notify switch
     */
    public boolean isVibrateNotification() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        return sharedPref.getBoolean("notification_vibrate_switch", false);
    }

    /**
     * Accept group invites automatically
     */
    public boolean isAcceptGroupInvitesAutomatically() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        return sharedPref.getBoolean("accept_group_invites_automatically", false);
    }

    /**
     * Adaptive video bitrate
     */
    public boolean isAdaptiveVideoBitrate() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        return sharedPref.getBoolean("adaptive_video_bitrate", false);
    }

}