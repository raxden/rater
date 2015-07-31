package com.raxdenstudios.rater;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.raxdenstudios.commons.util.Utils;

/**
 * Created by agomez on 31/07/2015.
 */
public class RaterManager {

    private static final String TAG = RaterManager.class.getSimpleName();

    private static final int DAYS_UNTIL_PROMPT = 3;
    private static final int LAUNCHES_UNTIL_PROMPT = 7;

    private Context mContext;
    private Configuration mConfiguration;


    public RaterManager(Context context) {
        this(context, new DefaultConfiguration(context, Utils.getApplicationName(context), Utils.getPackageName(context)));
    }

    public RaterManager(Context context, Configuration configuration) {
        mContext = context;
        mConfiguration = configuration;
    }

    /**
     * Show dialog if application has been launched 7 times and it has been 3 days since the
     * first launch.
     * @param context
     * @param callbacks
     */
    public void showRaterDialogIfNecessary(Context context, AppRaterCallbacks callbacks) {
        Log.d(TAG, "[showRaterDialogIfNecessary]");

        if (RaterHelper.getInstance().isDontShowAgain(context)) {
            Log.d(TAG, "[showRaterDialogIfNecessary] isDontShowAgain:true");
            return;
        }

        RaterHelper.getInstance().incrementLaunchCounter(context);
        long launchCounter = RaterHelper.getInstance().getLaunchCounter(context);
        Log.d(TAG, "[showRaterDialogIfNecessary] increment launch counter to "+launchCounter);

        Long dateFirstLaunch = RaterHelper.getInstance().getFirstLaunch(context);
        Long now = System.currentTimeMillis();

        if (dateFirstLaunch == 0) {
            Log.d(TAG, "[showRaterDialogIfNecessary] Is the first time, setting date of first launch...");
            dateFirstLaunch = now;
            RaterHelper.getInstance().setFirstLaunch(context, dateFirstLaunch);
        } else {
            Log.d(TAG, "[showRaterDialogIfNecessary] Date first launch "+(new java.util.Date(dateFirstLaunch).toString()));
        }

        Log.d(TAG, "launchCounter: "+launchCounter);
        Log.d(TAG, "launchesUntilPrompt: "+mConfiguration.launchesUntilPrompt);
        Log.d(TAG, "now: "+(new java.util.Date(now).toString()));
        Log.d(TAG, "dateFirstLaunch: "+dateFirstLaunch);
        Log.d(TAG, "daysUntilPrompt: "+mConfiguration.daysUntilPrompt);
        Log.d(TAG, "daysUntilPrompt Date: "+(new java.util.Date(mConfiguration.daysUntilPrompt * 24 * 60 * 60 * 1000).toString()));

        // Wait at least n days before opening
        if (launchCounter >= mConfiguration.launchesUntilPrompt) {
            if (now >= dateFirstLaunch + (mConfiguration.daysUntilPrompt * 24 * 60 * 60 * 1000)) {
                showRaterDialog(context, callbacks);
            }
        }
    }

    /**
     * Show rater dialog.
     * @param context
     * @param callbacks
     */
    public void showRaterDialog(final Context context, final AppRaterCallbacks callbacks) {
        Log.d(TAG, "[showRaterDialog]");

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(mConfiguration.dialogTitle);
        builder.setMessage(mConfiguration.dialogMessage);
        builder.setPositiveButton(mConfiguration.dialogButtonRate, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
                handleRaterOption();
                if (callbacks != null) callbacks.onDialogClickRate();
            }
        });
        builder.setNeutralButton(mConfiguration.dialogButtonRemindLater, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
                handleRemindLaterOption();
                if (callbacks != null) callbacks.onDialogClickRemindLater();
            }
        });
        builder.setNegativeButton(mConfiguration.dialogButtonDontShowAgain, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
                handleDontShowAgainOption();
                if (callbacks != null) callbacks.onDialogClickDontShowAgain();
            }
        });
        builder.show();
    }

    /**
     * Handle "rater" option selected by user.
     */
    private void handleRaterOption() {
        Log.d(TAG, "[handleRaterOption]");
        RaterHelper.getInstance().setDontShowAgain(mContext, true);
        mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + mConfiguration.appPackageName)));
    }

    /**
     * Handle "remind later" option selected by user.
     */
    private void handleRemindLaterOption() {
        Log.d(TAG, "[handleRemindLaterOption]");
    }

    /**
     * Handle "dont show again" option selected by user.
     */
    private void handleDontShowAgainOption() {
        Log.d(TAG, "[handleDontShowAgainOption]");
        RaterHelper.getInstance().setDontShowAgain(mContext, true);
    }

    public interface AppRaterCallbacks {
        void onDialogClickRate();
        void onDialogClickRemindLater();
        void onDialogClickDontShowAgain();
    }

    public static class Configuration {

        public String appName;
        public String appPackageName;
        public String dialogTitle;
        public String dialogMessage;
        public String dialogButtonRate;
        public String dialogButtonRemindLater;
        public String dialogButtonDontShowAgain;
        public int daysUntilPrompt;
        public int launchesUntilPrompt;

        public Configuration(String appName, String packageName) {
            this.appName = appName;
            this.appPackageName = packageName;
            this.daysUntilPrompt = DAYS_UNTIL_PROMPT;
            this.launchesUntilPrompt = LAUNCHES_UNTIL_PROMPT;
        }
    }

    public static class DefaultConfiguration extends Configuration {

        public DefaultConfiguration(Context context, String appName, String packageName) {
            super(appName, packageName);
            this.dialogTitle = String.format(context.getString(R.string.app__rate_title), appName);
            this.dialogMessage = String.format(context.getString(R.string.app__rate_message), appName);
            this.dialogButtonRate = context.getString(R.string.app__rate_button_rate);
            this.dialogButtonRemindLater = context.getString(R.string.app__rate_button_reminder_later);
            this.dialogButtonDontShowAgain = context.getString(R.string.app__rate_button_dont_show_again);
        }
    }
}
