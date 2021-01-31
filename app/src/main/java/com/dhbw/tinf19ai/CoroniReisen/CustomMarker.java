package com.dhbw.tinf19ai.CoroniReisen;

/**
 * This class facilates the presentation of the data in the PieChart, allowing different functinalities, like highlighting an entry when tapped.
 */

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


    /**
     * Set Value of Entry, then refresh content
     *
     * @param e
     * @param highlight
     */
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        tvContent.setText("" + e.getY());
        super.refreshContent(e, highlight);

    }

    /**
     * Center data value
     *
     * @param posX
     * @param posY
     * @return
     */
    @Override
    public MPPointF getOffsetForDrawingAtPoint(float posX, float posY) {
        return super.getOffsetForDrawingAtPoint(posX/2, posY);
    }
}
