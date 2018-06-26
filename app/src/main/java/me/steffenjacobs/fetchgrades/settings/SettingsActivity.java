package me.steffenjacobs.fetchgrades.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import me.steffenjacobs.fetchgrades.R;
import me.steffenjacobs.fetchgrades.gradedisplay.GradeDisplayActivity;
import me.steffenjacobs.fetchgrades.login.SettingsStorageService;
import me.steffenjacobs.fetchgrades.login.LoginActivity;

public class SettingsActivity extends AppCompatActivity {

    private SettingsStorageService settingsStorageService;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                goToGradeOverview(false);
                break;
            default:
                break;
        }

        return true;
    }

    private void goToGradeOverview(boolean changed) {
        Intent intent = new Intent(this, GradeDisplayActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle b = new Bundle();
        b.putBoolean("settings-changed", changed);
        final Bundle initial = getIntent().getExtras();
        b.putString("username", initial.getString("username"));
        b.putString("password", initial.getString("password"));
        intent.putExtras(b);
        startActivity(intent);
    }

    private void goToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void persistSettings() {
        final Switch backgroundSwitch = (Switch) findViewById(R.id.switchBackground);
        settingsStorageService.saveBackgroundServiceEnabled(backgroundSwitch.isChecked());
        final TextView textBackgroundInterval = (TextView) findViewById(R.id.textBackgroundInterval);
        settingsStorageService.saveBackgroundServiceInterval(Long.parseLong(textBackgroundInterval.getText().toString()));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        settingsStorageService = new SettingsStorageService(this);
        setupActionBar();

        Button okButton = (Button) findViewById(R.id.buttonOk);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                persistSettings();
                goToGradeOverview(true);
            }
        });

        Button clearButton = (Button) findViewById(R.id.buttonClearCredentials);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsStorageService.clearCredentials();
                goToLogin();
            }
        });

        final TextView textBackgroundInterval = (TextView) findViewById(R.id.textBackgroundInterval);
        textBackgroundInterval.setText("" + settingsStorageService.getBackgroundServiceInterval());

        final Switch backgroundSwitch = (Switch) findViewById(R.id.switchBackground);
        backgroundSwitch.setChecked(settingsStorageService.isBackgroundServiceEnabled());
        backgroundSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textBackgroundInterval.setEnabled(backgroundSwitch.isChecked());
            }
        });

        textBackgroundInterval.setEnabled(backgroundSwitch.isChecked());
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
