package me.steffenjacobs.fetchgrades.gradedisplay;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.List;

import me.steffenjacobs.fetchgrades.R;
import me.steffenjacobs.fetchgrades.backgroundservice.BackgroundService;
import me.steffenjacobs.fetchgrades.web.GradeCalculator;
import me.steffenjacobs.fetchgrades.web.Module;
import me.steffenjacobs.fetchgrades.login.LoginActivity;
import me.steffenjacobs.fetchgrades.login.SettingsStorageService;
import me.steffenjacobs.fetchgrades.settings.SettingsActivity;
import me.steffenjacobs.fetchgrades.util.AndroidUtil;

public class GradeDisplayActivity extends AppCompatActivity {
    private BackgroundService bgService;
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
            case R.id.action_refresh:
                bgService.refresh();
                renderView(bgService.getModules());
                Toast.makeText(this, getText(R.string.grade_refreshed), Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                final Bundle initial = getIntent().getExtras();
                Bundle b = new Bundle();
                b.putString("username", initial.getString("username"));
                b.putString("password", initial.getString("password"));
                intent.putExtras(b);
                startActivity(intent);
                break;
            case android.R.id.home:
                Intent intent2 = new Intent(this, LoginActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle b2 = new Bundle();
                b2.putBoolean("logout-redirect", true);
                intent2.putExtras(b2);
                startActivity(intent2);
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_display);
        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        settingsStorageService = new SettingsStorageService(this);
        setupActionBar();

        final Bundle b = getIntent().getExtras();
        final String username = b.getString("username");
        final String password = b.getString("password");

        AndroidUtil.allowNetworkOnMainThread();
        bgService = new BackgroundService(this, username, password);
        renderView(bgService.getModules());

        if (settingsStorageService.isBackgroundServiceEnabled()) {
            bgService.enableNotifications(settingsStorageService.getBackgroundServiceInterval());
        } else {
            bgService.disableNotifications();
        }
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void renderView(List<Module> modules) {
        LinearLayout rootLayout = (LinearLayout) findViewById(R.id.rootLayout);
        rootLayout.removeAllViews();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        rootLayout.addView(ReducedTableGenerator.getFullTableView(this, modules, metrics.widthPixels));

        TextView textView = new TextView(this);

        double avg_grade = GradeCalculator.calculateAverage(modules);
        DecimalFormat decim = new DecimalFormat("0.00");

        textView.setText(String.format(getString(R.string.grade_average), decim.format(avg_grade)));
        textView.setTypeface(null, Typeface.BOLD);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(0, 30, 0, 0);
        rootLayout.addView(textView);
        rootLayout.invalidate();

        if (bgService.hasNewGrades()) {
            String text = "New grades available!\n";
            for (Module m : bgService.getNewGrades()) {
                text += bgService.generateNewGradeMessage(m) + "\n";
            }
            TextView textView2 = new TextView(this);
            textView2.setText(text);
            textView2.setTypeface(null, Typeface.BOLD);
            textView2.setGravity(Gravity.CENTER);
            textView2.setPadding(0, 30, 0, 0);
            rootLayout.addView(textView2);
            bgService.updateModuleStorage(modules);
        }


    }
}
