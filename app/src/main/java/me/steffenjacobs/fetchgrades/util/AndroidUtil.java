package me.steffenjacobs.fetchgrades.util;

import android.os.StrictMode;

public final class AndroidUtil {
    private AndroidUtil(){

    }

    public static void allowNetworkOnMainThread(){
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }
}
