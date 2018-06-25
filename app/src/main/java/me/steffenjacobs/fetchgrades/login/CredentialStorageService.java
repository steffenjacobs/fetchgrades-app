package me.steffenjacobs.fetchgrades.login;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class CredentialStorageService {

    private static final String CREDENTIALS = "CREDENTIALS";

    public void saveCredentials(Context context, String username, String password) {
        SharedPreferences.Editor editor = context.getSharedPreferences(CREDENTIALS, MODE_PRIVATE).edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.apply();
    }

    public String getUsername(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(CREDENTIALS, MODE_PRIVATE);
        return prefs.getString("username", null);
    }

    public String getPassword(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(CREDENTIALS, MODE_PRIVATE);
        return prefs.getString("password", null);
    }
}
