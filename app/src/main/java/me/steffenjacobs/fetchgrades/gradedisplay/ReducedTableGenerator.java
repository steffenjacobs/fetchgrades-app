package me.steffenjacobs.fetchgrades.gradedisplay;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

import me.steffenjacobs.fetchgrades.gradefetcher.Module;

public class ReducedTableGenerator {

    public static View getFullTableView(Context context, List<Module> modules, int screenWidth) {
        String[] column = {"Exam Title", "Grade", "ECTS", "Passed"};

        ScrollView sv = new ScrollView(context);
        TableLayout tableLayout = createTableLayout(column, modules, context, screenWidth);
        HorizontalScrollView hsv = new HorizontalScrollView(context);

        hsv.addView(tableLayout);
        sv.addView(hsv);
        return sv;
    }

    private static RelativeLayoutTextFieldWrapper createTextView(String content, Context context) {
        return createTextView(content, context, Gravity.CENTER_HORIZONTAL);
    }

    static class RelativeLayoutTextFieldWrapper extends RelativeLayout {

        private final TextView txtView;


        public RelativeLayoutTextFieldWrapper(Context context, TextView view) {
            super(context);
            super.addView(view);
            this.txtView = view;
            txtView.setGravity(Gravity.CENTER);
        }

        public TextView getTextView() {
            return txtView;
        }
    }

    private static TextView createTextViewWithoutWrapper(String content, Context context, int gravity){
        TextView textView = new TextView(context);
        textView.setBackgroundColor(Color.WHITE);
        textView.setGravity(gravity);
        textView.setText(content);
        textView.setPadding(10, 3, 10, 3);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return textView;
    }

    private static RelativeLayoutTextFieldWrapper createTextView(String content, Context context, int gravity) {

        return new RelativeLayoutTextFieldWrapper(context, createTextViewWithoutWrapper(content, context, gravity));
    }

    private static TableLayout createTableLayout(String[] columnHeaders, List<Module> modules, Context context, int screenWidth) {

        // 1) Create a tableLayout and its params
        TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams();
        TableLayout tableLayout = new TableLayout(context);
        tableLayout.setStretchAllColumns(true);

        tableLayout.setBackgroundColor(Color.BLACK);

        // 2) create tableRow params
        TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams();
        tableRowParams.setMargins(1, 1, 1, 1);

        TableRow headersRow = new TableRow(context);
        for (String s : columnHeaders) {
            RelativeLayoutTextFieldWrapper textView = createTextView(s, context);
            textView.getTextView().setTypeface(null, Typeface.BOLD);
            headersRow.addView(textView, tableRowParams);
        }
        tableLayout.addView(headersRow, tableLayoutParams);

        for (int i = 0; i < modules.size(); i++) {
            Module m = modules.get(i);
            TableRow tableRow = new TableRow(context);
            //tableRow.setBackgroundColor(Color.BLACK);
            TextView moduleName = createTextViewWithoutWrapper(m.getModuleName(), context, Gravity.LEFT);
            FrameLayout al = new FrameLayout(context);
            al.addView(moduleName);
            moduleName.setWidth(screenWidth / 2);
            tableRow.addView(al, tableRowParams);

            RelativeLayoutTextFieldWrapper textGrade = createTextView("" + m.getGrade(), context);
            textGrade.getTextView().setWidth((int) (screenWidth * 0.16));
            tableRow.addView(textGrade, tableRowParams);

            RelativeLayoutTextFieldWrapper textEcts = createTextView("" + m.getEcts(), context);
            textEcts.getTextView().setWidth((int) (screenWidth * 0.17));
            tableRow.addView(textEcts, tableRowParams);

            RelativeLayoutTextFieldWrapper textPassed;
            if (m.isPassed()) {
                textPassed = createTextView("✔", context);
                textPassed.getTextView().setTextColor(Color.rgb(0x1e, 0x66, 0x45));
            } else {
                textPassed = createTextView("✘", context);
                textPassed.getTextView().setTextColor(Color.rgb(0x66, 0x1d, 0x1d));
            }
            tableRow.addView(textPassed, tableRowParams);

            tableLayout.addView(tableRow, tableLayoutParams);
        }

        return tableLayout;
    }
}
