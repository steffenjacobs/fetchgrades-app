package me.steffenjacobs.fetchgrades.gradedisplay;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;
import me.steffenjacobs.fetchgrades.web.Module;

public class CustomTableDataAdapter extends TableDataAdapter {

    public CustomTableDataAdapter(Context context, List data) {
        super(context, data);
    }


    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        Module module = (Module) getRowData(rowIndex);
        TextView renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = new TextView(this.getContext());
                renderedView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                renderedView.setText(module.getModuleName());
                renderedView.setTypeface(null, Typeface.BOLD);
                renderedView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);
                renderedView.setPadding(10, 10, 0, 10);
                break;
            case 1:
                renderedView = new TextView(this.getContext());
                renderedView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                renderedView.setText(module.getEcts() + "");
                renderedView.setTypeface(null, Typeface.BOLD);
                renderedView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);
                renderedView.setGravity(Gravity.CENTER);
                renderedView.setPadding(10, 10, 0, 10);
                break;
            case 2:
                renderedView = new TextView(this.getContext());
                renderedView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                renderedView.setText(module.getGrade() + "");
                renderedView.setTypeface(null, Typeface.BOLD);
                renderedView.setGravity(Gravity.CENTER);
                renderedView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);
                renderedView.setPadding(10, 10, 0, 10);
                break;
        }

        return renderedView;
    }
}
