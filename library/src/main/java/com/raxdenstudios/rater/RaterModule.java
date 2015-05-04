package com.raxdenstudios.rater;

import android.content.Context;
import android.os.Bundle;

import com.raxdenstudios.app.module.ModuleImpl;

/**
 * Created by agomez on 04/05/2015.
 */
public class RaterModule extends ModuleImpl {

    private static final String TAG = RaterModule.class.getSimpleName();

    private RaterHelper.AppRaterCallbacks mCallbacks;

    public RaterModule(Context context, RaterHelper.AppRaterCallbacks callbacks) {
        RaterHelper.getInstance().init(context);
        mCallbacks = callbacks;
    }

    @Override
    public void onModuleCreate(Context context, Bundle savedInstanceState) {
        super.onModuleCreate(context, savedInstanceState);
        RaterHelper.getInstance().showRaterDialogIfNecessary(context, mCallbacks);
    }

}
