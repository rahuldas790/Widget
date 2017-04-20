package rahulkumardas.floatingwidget;

import android.view.View;

/**
 * Created by Rahul Kumar Das on 03-03-2017.
 */

public class SubItems {
    public int x;
    public int y;
    public int width;
    public int height;

    public float alpha;

    public View view;

    public SubItems(View view, int width, int height) {
        this.view = view;
        this.width = width;
        this.height = height;
        alpha = view.getAlpha();
        x = 0;
        y = 0;
    }
}