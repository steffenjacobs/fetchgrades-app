package me.steffenjacobs.fetchgrades.gradedisplay;

import android.annotation.SuppressLint;
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

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_display);
        setupActionBar();

        final Bundle b = getIntent().getExtras();
        final String username = b.getString("username");
        final String password = b.getString("password");

        AndroidUtil.allowNetworkOnMainThread();
        bgService = new BackgroundService(this, username, password);
        List<Module> list = bgService.getModules();


        TableView<String[]> tableView = (TableView<String[]>) findViewById(R.id.tableView);
        CustomTableDataAdapter customTableDataAdapter = new CustomTableDataAdapter(this, list);
        tableView.setDataAdapter(customTableDataAdapter);
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(this, new String[]{"Module name", "ECTS", "Grade"} ));

        TableColumnWeightModel columnModel = new TableColumnWeightModel(3);
        columnModel.setColumnWeight(0, 3);
        columnModel.setColumnWeight(1, 1);
        columnModel.setColumnWeight(2, 1);
        tableView.setColumnModel(columnModel);

        /*
        LinearLayout rootLayout = (LinearLayout) findViewById(R.id.rootLayout);
        TextView textView = new TextView(this);

        double avg_grade = GradeCalculator.calculateAverage(list);
        DecimalFormat decim = new DecimalFormat("0.00");

        textView.setText(String.format(getString(R.string.grade_average), decim.format(avg_grade)));
        textView.setTypeface(null, Typeface.BOLD);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(0, 30, 0, 0);
        rootLayout.addView(textView); */
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
