package me.steffenjacobs.fetchgrades.gradedisplay;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.codecrafters.tableview.TableHeaderAdapter;
import me.steffenjacobs.fetchgrades.web.Module;

public class CustomHeaderDataAdapter extends TableHeaderAdapter {

    public CustomHeaderDataAdapter(Context context) {
        super(context);
    }

    @Override
    public View getHeaderView(int columnIndex, ViewGroup parentView) {
        TextView renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = new TextView(this.getContext());
                renderedView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                renderedView.setText("Module name");
                renderedView.setTypeface(null, Typeface.BOLD);
                renderedView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
                renderedView.setPadding(10, 10, 0, 10);
                break;
            case 1:
                renderedView = new TextView(this.getContext());
                renderedView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                renderedView.setText("ECTS");
                renderedView.setTypeface(null, Typeface.BOLD);
                renderedView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
                renderedView.setGravity(Gravity.CENTER);
                renderedView.setPadding(10, 10, 0, 10);
                break;
            case 2:
                renderedView = new TextView(this.getContext());
                renderedView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                renderedView.setText("Grade");
                renderedView.setTypeface(null, Typeface.BOLD);
                renderedView.setGravity(Gravity.CENTER);
                renderedView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
                renderedView.setPadding(10, 10, 0, 10);
                break;
            case 3:
                renderedView = new TextView(this.getContext());
                renderedView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                renderedView.setText( "Passed" );
                renderedView.setTypeface(null, Typeface.BOLD);
                renderedView.setGravity(Gravity.CENTER);
                renderedView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
                renderedView.setPadding(10, 10, 0, 10);
        }

        return renderedView;
    }
}
