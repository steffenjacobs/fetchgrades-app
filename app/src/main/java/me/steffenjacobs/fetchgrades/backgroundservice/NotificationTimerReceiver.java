package me.steffenjacobs.fetchgrades.backgroundservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import me.steffenjacobs.fetchgrades.login.SettingsStorageService;

public class NotificationTimerReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && context != null) {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
                //called on reboot
                NotificationScheduler.setReminder(context, new SettingsStorageService(context).getBackgroundServiceInterval());
                return;
            }
        }

        NotificationScheduler.onReceive(context);
    }
}
