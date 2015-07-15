package com.raxdenstudios.rater;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import com.raxdenstudios.commons.util.Utils;

/**
 * <p>RaterHelper is a helper class that prompts the user to rate the application if application has
 * been launched 7 times and it has been 3 days since the first launch by default</p>
 * <p>Here is an example:</p>
 * <pre class="prettyprint">
 *
 *  RaterHelper.getInstance().init(context);
 *  RaterHelper.getInstance().showRaterDialogIfNecessary(context, new RaterHelper.AppRaterCallbacks() {
 *
 *  public void onDialogClickRate() {}
 *
 *  public void onDialogClickRemindLater() {}
 *
 *  public void onDialogClickDontShowAgain() {}
 *
 *  });
 *
 * </pre>
 */
public class RaterHelper {

    private static final String TAG = RaterHelper.class.getSimpleName();

    private static final String DONT_SHOW_AGAIN = "appRater.dontShowAgain";
    private static final String LAUNCH_COUNT = "appRater.launchCount";
    private static final String DATE_FIRST_LAUNCH = "appRater.firstLaunch";
    private static final int DAYS_UNTIL_PROMPT = 3;
    private static final int LAUNCHES_UNTIL_PROMPT = 7;

    private AppRaterConfiguration mConfig;
    private Object o = new Object();

    public interface AppRaterCallbacks {
        void onDialogClickRate();
        void onDialogClickRemindLater();
        void onDialogClickDontShowAgain();
    }

    public static class AppRaterConfiguration {

        public String appName;
        public String appPackageName;
        public String dialogTitle;
        public String dialogMessage;
        public String dialogButtonRate;
        public String dialogButtonRemindLater;
        public String dialogButtonDontShowAgain;
        public int daysUntilPrompt;
        public int launchesUntilPrompt;

        public AppRaterConfiguration(String appName, String packageName) {
            this.appName = appName;
            this.appPackageName = packageName;
            this.daysUntilPrompt = DAYS_UNTIL_PROMPT;
            this.launchesUntilPrompt = LAUNCHES_UNTIL_PROMPT;
        }
    }

    public static class DefaultAppRaterConfiguration extends AppRaterConfiguration {

        public DefaultAppRaterConfiguration(Context context, String appName, String packageName) {
            super(appName, packageName);
            this.dialogTitle = String.format(context.getString(R.string.app__rate_title), appName);
            this.dialogMessage = String.format(context.getString(R.string.app__rate_message), appName);
            this.dialogButtonRate = context.getString(R.string.app__rate_button_rate);
            this.dialogButtonRemindLater = context.getString(R.string.app__rate_button_reminder_later);
            this.dialogButtonDontShowAgain = context.getString(R.string.app__rate_button_dont_show_again);
        }
    }

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

    /**
     * Initialize RaterHelper with a default configuration.
     * @param context
     */
    public void init(Context context) {
        init(new DefaultAppRaterConfiguration(context, Utils.getApplicationName(context), Utils.getPackageName(context)));
    }

    /**
     * Initialize RaterHelper with a custom configuration.
     * @param configuration
     */
    public void init(AppRaterConfiguration configuration) {
        mConfig = configuration;
    }

    /**
     * Show dialog if application has been launched 7 times and it has been 3 days since the
     * first launch.
     * @param context
     * @param callbacks
     */
    public void showRaterDialogIfNecessary(Context context, AppRaterCallbacks callbacks) {
        synchronized (o) {

            if (mConfig == null) {
                init(context);
            }

            if (isDontShowAgain(context)) return;

            incrementLaunchCounter(context);

            Long dateFirstLaunch = getFirstLaunch(context);
            if (dateFirstLaunch == 0) {
                setFirstLaunch(context, System.currentTimeMillis());
            }

            // Wait at least n days before opening
            if (getLaunchCounter(context) >= mConfig.launchesUntilPrompt) {
                if (System.currentTimeMillis() >= getFirstLaunch(context) + (mConfig.daysUntilPrompt * 24 * 60 * 60 * 1000)) {
                    showRaterDialog(context, callbacks);
                }
            }
        }
    }

    /**
     * Show rater dialog.
     * @param context
     * @param callbacks
     */
    public void showRaterDialog(final Context context, final AppRaterCallbacks callbacks) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(mConfig.dialogTitle);
        builder.setMessage(mConfig.dialogMessage);
        builder.setPositiveButton(mConfig.dialogButtonRate, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
                handleRaterOption(context);
                if (callbacks != null) callbacks.onDialogClickRate();
            }
        });
        builder.setNeutralButton(mConfig.dialogButtonRemindLater, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
                handleRemindLaterOption(context);
                if (callbacks != null) callbacks.onDialogClickRemindLater();
            }
        });
        builder.setNegativeButton(mConfig.dialogButtonDontShowAgain, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
                handleDontShowAgainOption(context);
                if (callbacks != null) callbacks.onDialogClickDontShowAgain();
            }
        });
        builder.show();
    }

    /**
     * Handle "rater" option selected by user.
     * @param context
     */
    public void handleRaterOption(Context context) {
        setDontShowAgain(context, true);
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + mConfig.appPackageName)));
    }

    /**
     * Handle "remind later" option selected by user.
     * @param context
     */
    public void handleRemindLaterOption(Context context) {

    }

    /**
     * Handle "dont show again" option selected by user.
     * @param context
     */
    public void handleDontShowAgainOption(Context context) {
        setDontShowAgain(context, true);
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

    private void incrementLaunchCounter(Context context) {
        synchronized (o) {
            setLaunchCounter(context, getLaunchCounter(context) + 1);
        }
    }

}
