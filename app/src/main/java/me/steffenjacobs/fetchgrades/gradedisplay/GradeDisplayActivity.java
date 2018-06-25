package me.steffenjacobs.fetchgrades.gradedisplay;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
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

        TextView textView = findViewById(R.id.textView);
        textView.setText("Hello World from Grade Display!");
        System.out.println("opened grade display");
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        final Bundle b = getIntent().getExtras();
        String username = b.getString("username");
        String password = b.getString("password");

        FetchGrades grades = new FetchGrades(username, password);
        try {
            List<Module> modules = grades.fetchGrades();
            System.out.println("Average: " + GradeCalculator.calculateAverage(modules));


            setContentView(ReducedTableGenerator.getFullTableView(this, modules));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
