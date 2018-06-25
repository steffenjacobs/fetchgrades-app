package me.steffenjacobs.fetchgrades.gradedisplay;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

import me.steffenjacobs.fetchgrades.gradefetcher.Module;

public class ReducedTableGenerator {

    public static View getFullTableView(Context context, List<Module> modules) {
        String[] column = {"Exam Title", "Grade", "ECTS", "Passed"};

        ScrollView sv = new ScrollView(context);
        TableLayout tableLayout = createTableLayout(column, modules, context);
        HorizontalScrollView hsv = new HorizontalScrollView(context);

        hsv.addView(tableLayout);
        sv.addView(hsv);
        return sv;
    }

    private static TextView createTextView(String content, Context context) {
        return createTextView(content, context, Gravity.CENTER);
    }

    private static TextView createTextView(String content, Context context, int gravity) {
        TextView textView = new TextView(context);
        textView.setBackgroundColor(Color.WHITE);
        textView.setGravity(gravity);
        textView.setText(content);
        textView.setPadding(10, 3, 10, 3);
        return textView;
    }

    private static TableLayout createTableLayout(String[] columnHeaders, List<Module> modules, Context context) {
        // 1) Create a tableLayout and its params
        TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams();
        TableLayout tableLayout = new TableLayout(context);
        tableLayout.setStretchAllColumns(true);

        tableLayout.setBackgroundColor(Color.WHITE);

        // 2) create tableRow params
        TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams();
        tableRowParams.setMargins(1, 1, 1, 1);
        tableRowParams.weight = 1;

        TableRow headersRow = new TableRow(context);
        headersRow.setBackgroundColor(Color.BLACK);
        for (String s : columnHeaders) {
            TextView textView = createTextView(s, context);
            textView.setTypeface(null, Typeface.BOLD);
            headersRow.addView(textView, tableRowParams);
        }
        tableLayout.addView(headersRow, tableLayoutParams);

        for (int i = 0; i < modules.size(); i++) {
            Module m = modules.get(i);
            TableRow tableRow = new TableRow(context);
            tableRow.setBackgroundColor(Color.BLACK);
            tableRow.addView(createTextView(m.getModuleName(), context, Gravity.LEFT), tableRowParams);
            tableRow.addView(createTextView("" + m.getGrade(), context), tableRowParams);
            tableRow.addView(createTextView("" + m.getEcts(), context), tableRowParams);

            TextView status;
            if (m.isPassed()) {
                status = createTextView("✔", context);
                status.setTextColor(Color.rgb(0x1e, 0x66, 0x45));
            } else {
                status = createTextView("✘", context);
                status.setTextColor(Color.rgb(0x66, 0x1d, 0x1d));
            }
            tableRow.addView(status, tableRowParams);

            tableLayout.addView(tableRow, tableLayoutParams);
        }

        return tableLayout;
    }
}
