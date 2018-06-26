package me.steffenjacobs.fetchgrades.login;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SettingsStorageService {

    private static final String CREDENTIALS = "CREDENTIALS";
    private static final String SETTINGS = "SETTINGS";
    private final Context context;

    public SettingsStorageService(Context context) {
        this.context = context;
    }

    public void saveCredentials(String username, String password) {
        SharedPreferences.Editor editor = context.getSharedPreferences(CREDENTIALS, MODE_PRIVATE).edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.apply();
    }

    public void clearCredentials() {
        SharedPreferences.Editor editor = context.getSharedPreferences(CREDENTIALS, MODE_PRIVATE).edit();
        editor.remove("username");
        editor.remove("password");
        editor.apply();
    }

    public String getUsername() {
        SharedPreferences prefs = context.getSharedPreferences(CREDENTIALS, MODE_PRIVATE);
        return prefs.getString("username", null);
    }

    public String getPassword() {
        SharedPreferences prefs = context.getSharedPreferences(CREDENTIALS, MODE_PRIVATE);
        return prefs.getString("password", null);
    }

    public void saveBackgroundServiceEnabled(boolean enabled) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SETTINGS, MODE_PRIVATE).edit();
        editor.putBoolean("backgroundservice-enabled", enabled);
        editor.apply();
    }

    public void saveBackgroundServiceInterval(long interval) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SETTINGS, MODE_PRIVATE).edit();
        editor.putLong("backgroundservice-interval", interval);
        editor.apply();
    }

    public boolean isBackgroundServiceEnabled() {
        SharedPreferences prefs = context.getSharedPreferences(SETTINGS, MODE_PRIVATE);
        return prefs.getBoolean("backgroundservice-enabled", true);
    }

    public long getBackgroundServiceInterval() {
        SharedPreferences prefs = context.getSharedPreferences(SETTINGS, MODE_PRIVATE);
        return prefs.getLong("backgroundservice-interval", 1800000);
    }
}
