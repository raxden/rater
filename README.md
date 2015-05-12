Android Rater
==========

Android Rater is a library that prompts the user to rate the application if application has been launched 7 times and it has been 3 days since the first launch by default.

## Usage

### build.gradle

```
compile 'com.raxdenstudios:androidcommons:2.0.+'
compile 'com.raxdenstudios:androidrater:2.0.+'
```

### Activity or Fragment

```
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        RaterHelper.getInstance().init(context);
        RaterHelper.getInstance().showRaterDialogIfNecessary(context, new RaterHelper.AppRaterCallbacks() {
        
          public void onDialogClickRate() {}
          public void onDialogClickRemindLater() {}
          public void onDialogClickDontShowAgain() {}
          
        });
    }
```
