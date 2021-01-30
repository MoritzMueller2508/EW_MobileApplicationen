package com.dhbw.tinf19ai.CoroniReisen;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

public class CustomMarker extends MarkerView {

    //initialize values and objects

    private TextView tvContent;


    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public CustomMarker(Context context, int layoutResource) {
        super(context, layoutResource);
        tvContent = (TextView) findViewById(R.id.tvContent);

    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        tvContent.setText("" + e.getY());
        super.refreshContent(e, highlight);

    }

    @Override
    public MPPointF getOffsetForDrawingAtPoint(float posX, float posY) {
        return super.getOffsetForDrawingAtPoint(posX/2, posY);
    }
}
