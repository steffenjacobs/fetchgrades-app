package me.steffenjacobs.fetchgrades.gradedisplay;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
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
import de.codecrafters.tableview.toolkit.TableDataRowBackgroundProviders;
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
                refreshTable(bgService.getModules());
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

    private void refreshTable(List<Module> list){
        TableView<String[]> tableView = (TableView<String[]>) findViewById(R.id.tableView);
        CustomTableDataAdapter customTableDataAdapter = new CustomTableDataAdapter(this, list);
        tableView.setDataAdapter(customTableDataAdapter);
        CustomHeaderDataAdapter customHeaderDataAdapter = new CustomHeaderDataAdapter(this);
        tableView.setHeaderAdapter( customHeaderDataAdapter);


     /*   int colorEvenRows = getResources().getColor(R.color.colorAccent);
        int colorOddRows = getResources().getColor(R.color.colorPrimaryDark);
        tableView.setDataRowBackgroundProvider(TableDataRowBackgroundProviders.alternatingRowColors(colorEvenRows, colorOddRows));
*/


        TableColumnWeightModel columnModel = new TableColumnWeightModel(4);
        columnModel.setColumnWeight(0, 3);
        columnModel.setColumnWeight(1, 1);
        columnModel.setColumnWeight(2, 1);
        columnModel.setColumnWeight(3, 1);
        tableView.setColumnModel(columnModel);
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
        refreshTable(list);


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
