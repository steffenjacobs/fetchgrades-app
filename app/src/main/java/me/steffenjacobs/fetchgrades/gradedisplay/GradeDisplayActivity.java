package me.steffenjacobs.fetchgrades.gradedisplay;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import me.steffenjacobs.fetchgrades.R;
import me.steffenjacobs.fetchgrades.backgroundservice.BackgroundService;
import me.steffenjacobs.fetchgrades.gradefetcher.GradeCalculator;
import me.steffenjacobs.fetchgrades.gradefetcher.Module;

public class GradeDisplayActivity extends AppCompatActivity {


    public static final long INTERVAL_MILLIS = 60000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_display);
        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        final Bundle b = getIntent().getExtras();
        String username = b.getString("username");
        String password = b.getString("password");

        BackgroundService bgService = new BackgroundService(this, username, password);
        List<Module> modules = bgService.getModules();

        LinearLayout rootLayout = (LinearLayout) findViewById(R.id.rootLayout);
        rootLayout.addView(ReducedTableGenerator.getFullTableView(this, modules));

        TextView textView = new TextView(this);

        double avg_grade = GradeCalculator.calculateAverage(modules);
        DecimalFormat decim = new DecimalFormat("0.00");
        avg_grade = Double.parseDouble(decim.format(avg_grade));


        textView.setText("Average: " +  avg_grade);



        textView.setTypeface(null, Typeface.BOLD);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(0, 30, 0, 0);
        rootLayout.addView(textView);

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
            bgService.updateStorage(modules);
        }

        bgService.enableNotifications(INTERVAL_MILLIS);
    }
}
