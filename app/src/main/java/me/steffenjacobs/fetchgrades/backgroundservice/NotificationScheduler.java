package me.steffenjacobs.fetchgrades.backgroundservice;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import java.util.List;

import me.steffenjacobs.fetchgrades.gradedisplay.GradeDisplayActivity;
import me.steffenjacobs.fetchgrades.gradefetcher.Module;

import static android.content.Context.ALARM_SERVICE;

public class NotificationScheduler {
    public static final String TAG = "NotificationScheduler";
    private static BackgroundService backgroundService;
    private static NotificationService notificationService = new NotificationService();

    public static void setBackgroundService(BackgroundService bgService) {
        backgroundService = bgService;
    }

    public static void setReminder(Context context, long intervalSeconds) {
        // cancel already scheduled reminders
        cancelReminder(context);

        // Enable broadcast receiver
        ComponentName receiver = new ComponentName(context, NotificationTimerReceiver.class);
        context.getPackageManager().setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        Intent intent1 = new Intent(context, NotificationTimerReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0, intervalSeconds, pendingIntent);

    }

    public static void cancelReminder(Context context) {
        //disable broadcast receiver
        ComponentName receiver = new ComponentName(context, NotificationTimerReceiver.class);
        context.getPackageManager().setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

        //Remove pending intents
        Intent intent1 = new Intent(context, NotificationTimerReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.cancel(pendingIntent);
        pendingIntent.cancel();
    }

    public static void onReceive(Context context) {
        if (backgroundService != null) {
            backgroundService.refresh();
            if (backgroundService.hasNewGrades()) {
                List<Module> modules = backgroundService.getNewGrades();
                for (Module m : modules) {
                    notificationService.showNotification(context, GradeDisplayActivity.class,
                            backgroundService.generateNewGradeMessage(m), "");
                }
            } else {
                notificationService.showNotification(context, GradeDisplayActivity.class,
                        "No new grades.", "Sorry." + System.currentTimeMillis());
            }
        }
    }
}
