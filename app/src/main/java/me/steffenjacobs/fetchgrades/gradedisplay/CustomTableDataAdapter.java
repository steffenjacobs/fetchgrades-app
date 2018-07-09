package me.steffenjacobs.fetchgrades.gradedisplay;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;
import de.codecrafters.tableview.toolkit.TableDataRowBackgroundProviders;
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
                renderedView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
                renderedView.setPadding(10, 10, 0, 10);
                break;
            case 1:
                renderedView = new TextView(this.getContext());
                renderedView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                renderedView.setText(module.getEcts() + "");
                renderedView.setTypeface(null, Typeface.BOLD);
                renderedView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
                renderedView.setGravity(Gravity.CENTER);
                renderedView.setPadding(10, 10, 0, 10);
                break;
            case 2:
                renderedView = new TextView(this.getContext());
                renderedView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                renderedView.setText(module.getGrade() + "");
                renderedView.setTypeface(null, Typeface.BOLD);
                renderedView.setGravity(Gravity.CENTER);
                renderedView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
                renderedView.setPadding(10, 10, 0, 10);
                break;
            case 3:
                renderedView = new TextView(this.getContext());
                renderedView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                renderedView.setText(module.isPassed() ? "✔" : "✘");
                renderedView.setTypeface(null, Typeface.BOLD);
                if (module.isPassed()) {
                    renderedView.setText("✔");
                    renderedView.setTextColor(Color.rgb(0x1e, 0x66, 0x45));
                } else {
                    renderedView.setText("✘");
                    renderedView.setTextColor(Color.rgb(0x66, 0x1d, 0x1d));
                }
                renderedView.setGravity(Gravity.CENTER);
                renderedView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
                renderedView.setPadding(10, 10, 0, 10);
        }

        return renderedView;
    }
}
