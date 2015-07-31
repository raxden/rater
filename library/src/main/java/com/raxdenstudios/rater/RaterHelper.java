package com.raxdenstudios.rater;

import android.content.Context;
import android.content.SharedPreferences;

public class RaterHelper {

    private static final String DONT_SHOW_AGAIN = "appRater.dontShowAgain";
    private static final String LAUNCH_COUNT = "appRater.launchCount";
    private static final String DATE_FIRST_LAUNCH = "appRater.firstLaunch";

    private Object o = new Object();

    private static RaterHelper INSTANCE = null;

    private RaterHelper() {}

    private synchronized static void createInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RaterHelper();
        }
    }

    public static RaterHelper getInstance() {
        if (INSTANCE == null) createInstance();
        return INSTANCE;
    }

    private SharedPreferences getRaterPreferences(Context context) {
        return context.getSharedPreferences(RaterHelper.class.getSimpleName(), 0);
    }

    public boolean isDontShowAgain(Context context) {
        synchronized (o) {
            return getRaterPreferences(context).getBoolean(DONT_SHOW_AGAIN, false);
        }
    }

    public void setDontShowAgain(Context context, boolean dontShowAgain) {
        synchronized (o) {
            SharedPreferences.Editor editor = getRaterPreferences(context).edit();
            if (editor != null) {
                editor.putBoolean(DONT_SHOW_AGAIN, true);
                editor.commit();
            }
        }
    }

    public long getLaunchCounter(Context context) {
        synchronized (o) {
            return getRaterPreferences(context).getLong(LAUNCH_COUNT, 0);
        }
    }

    public void setLaunchCounter(Context context, long launchCount) {
        synchronized (o) {
            SharedPreferences.Editor editor = getRaterPreferences(context).edit();
            if (editor != null) {
                editor.putLong(LAUNCH_COUNT, launchCount);
            }
        }
    }

    public void setFirstLaunch(Context context, long firstLaunch) {
        synchronized (o) {
            SharedPreferences.Editor editor = getRaterPreferences(context).edit();
            if (editor != null) {
                editor.putLong(DATE_FIRST_LAUNCH, firstLaunch);
            }
        }
    }

    public long getFirstLaunch(Context context) {
        synchronized (o) {
            return getRaterPreferences(context).getLong(DATE_FIRST_LAUNCH, 0);
        }
    }

    public void incrementLaunchCounter(Context context) {
        synchronized (o) {
            setLaunchCounter(context, getLaunchCounter(context) + 1);
        }
    }

}
