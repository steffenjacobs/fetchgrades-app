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

import me.steffenjacobs.fetchgrades.web.Module;

public class FullTableGenerator {

    public static View getFullTableView(Context context, List<Module> modules) {
        String[] column = {"Exam ID", "Semester", "Round", "Exam Date",
                "Exam Title", "Examiner", "Exam Type", "Grade", "ECTS", "Status", "Attempt"};

        ScrollView sv = new ScrollView(context);
        TableLayout tableLayout = createTableLayout(column, modules, context);
        HorizontalScrollView hsv = new HorizontalScrollView(context);

        hsv.addView(tableLayout);
        sv.addView(hsv);
        return sv;
    }

    private static TextView createTextView(String content, Context context) {
        TextView textView = new TextView(context);
        textView.setBackgroundColor(Color.WHITE);
        textView.setGravity(Gravity.CENTER);
        textView.setText(content);
        textView.setPadding(10, 3, 10, 3);
        return textView;
    }

    private static TableLayout createTableLayout(String[] columnHeaders, List<Module> modules, Context context) {

        //create table layout
        TableLayout tableLayout = new TableLayout(context);
        tableLayout.setBackgroundColor(Color.WHITE);
        tableLayout.setStretchAllColumns(true);

        //create table parameters
        TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams();
        TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams();
        tableRowParams.setMargins(1, 1, 1, 1);
        tableRowParams.weight = 1;

        //create header
        TableRow headersRow = new TableRow(context);
        headersRow.setBackgroundColor(Color.BLACK);
        for (String s : columnHeaders) {
            TextView textView = createTextView(s, context);
            textView.setTypeface(null, Typeface.BOLD);
            headersRow.addView(textView, tableRowParams);
        }
        tableLayout.addView(headersRow, tableLayoutParams);

        //create table
        for (int i = 0; i < modules.size(); i++) {
            Module m = modules.get(i);
            TableRow tableRow = new TableRow(context);
            tableRow.setBackgroundColor(Color.BLACK);

            tableRow.addView(createTextView("" + m.getExamNumber(), context), tableRowParams);
            tableRow.addView(createTextView(m.getSemester(), context), tableRowParams);
            tableRow.addView(createTextView("" + m.getRound(), context), tableRowParams);
            tableRow.addView(createTextView(Module.DATE_FORMAT.format(m.getExamDate()), context), tableRowParams);
            tableRow.addView(createTextView(m.getModuleName(), context), tableRowParams);
            tableRow.addView(createTextView(m.getExaminer(), context), tableRowParams);
            tableRow.addView(createTextView(m.getExamType(), context), tableRowParams);
            tableRow.addView(createTextView("" + m.getGrade(), context), tableRowParams);
            tableRow.addView(createTextView("" + m.getEcts(), context), tableRowParams);
            tableRow.addView(createTextView(m.isPassed() ? "passed" : "failed", context), tableRowParams);
            tableRow.addView(createTextView("" + m.getAttempt(), context), tableRowParams);

            tableLayout.addView(tableRow, tableLayoutParams);
        }

        return tableLayout;
    }
}
