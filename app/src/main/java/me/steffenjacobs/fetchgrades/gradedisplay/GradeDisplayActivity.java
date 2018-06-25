package me.steffenjacobs.fetchgrades.gradedisplay;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import me.steffenjacobs.fetchgrades.R;
import me.steffenjacobs.fetchgrades.gradefetcher.FetchGrades;
import me.steffenjacobs.fetchgrades.gradefetcher.GradeCalculator;
import me.steffenjacobs.fetchgrades.gradefetcher.Module;

public class GradeDisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_display);
       // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        final Bundle b = getIntent().getExtras();
        String username = b.getString("username");
        String password = b.getString("password");

        FetchGrades grades = new FetchGrades(username, password);
        try {
            List<Module> modules = grades.fetchGrades();
            System.out.println("Average: " + GradeCalculator.calculateAverage(modules));


            LinearLayout rootLayout = (LinearLayout) findViewById(R.id.rootLayout);
            rootLayout.addView(ReducedTableGenerator.getFullTableView(this, modules));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
