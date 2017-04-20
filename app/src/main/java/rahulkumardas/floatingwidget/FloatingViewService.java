package rahulkumardas.floatingwidget;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rahul Kumar Das on 27-02-2017.
 */

public class FloatingViewService extends Service {
    private WindowManager mWindowManager;
    private View mFloatingView;
    private CountDownTimer timer;
    private ImageView iv1, iv2, iv3, iv4, iv5, iv6;
    private int width = 0;
    private int height = 100;
    private boolean left = true;
    private boolean showing = false;


    private int radius;
    private final int VISIBLE_DUR = 10000;
    private float startAngle, endAngle;
    private List<SubItems> subActionItems;

    public FloatingViewService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Inflate the floating view layout we created
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null);
        //The root element of the collapsed view layout
        final View collapsedView = mFloatingView.findViewById(R.id.collapse_view);

        //The root element of the side stick view layout
        final View expandedView = mFloatingView.findViewById(R.id.rest);
        final ImageView semiCircle = (ImageView) mFloatingView.findViewById(R.id.semi_circle);

        //Add the view to the window.
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        //Specify the view position
        params.gravity = Gravity.TOP | Gravity.LEFT;        //Initially view will be added to top-left corner
        params.x = 0;
        params.y = 100;

        //Add the view to the window
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingView, params);
        iv1 = new ImageView(this);
        iv2 = new ImageView(this);
        iv3 = new ImageView(this);
        iv4 = new ImageView(this);
        iv5 = new ImageView(this);
        iv6 = new ImageView(this);

        iv1.setImageResource(R.drawable.ic_home);
        iv1.setBackgroundResource(R.drawable.white_circle);
        iv2.setImageResource(R.drawable.ic_maps);
        iv2.setBackgroundResource(R.drawable.white_circle);
        iv3.setImageResource(R.drawable.ic_settings);
        iv3.setBackgroundResource(R.drawable.white_circle);
        iv4.setImageResource(R.drawable.ic_maps);
        iv4.setBackgroundResource(R.drawable.white_circle);
        iv5.setImageResource(R.drawable.ic_settings);
        iv5.setBackgroundResource(R.drawable.white_circle);


        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dialogIntent = new Intent(FloatingViewService.this, HomeActivity.class);
                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(dialogIntent);
                collapsedView.setVisibility(View.GONE);
                expandedView.setVisibility(View.VISIBLE);
                showing = false;
                removeView();
            }
        });
        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dialogIntent = new Intent(FloatingViewService.this, MapsActivity.class);
                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(dialogIntent);
                collapsedView.setVisibility(View.GONE);
                expandedView.setVisibility(View.VISIBLE);
                showing = false;
                removeView();
            }
        });
        iv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dialogIntent = new Intent(FloatingViewService.this, MapsActivity.class);
                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(dialogIntent);
                collapsedView.setVisibility(View.GONE);
                expandedView.setVisibility(View.VISIBLE);
                showing = false;
                removeView();
            }
        });
        iv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dialogIntent = new Intent(FloatingViewService.this, MainActivity.class);
                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(dialogIntent);
                collapsedView.setVisibility(View.GONE);
                expandedView.setVisibility(View.VISIBLE);
                showing = false;
                removeView();
            }
        });

        iv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dialogIntent = new Intent(FloatingViewService.this, MainActivity.class);
                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(dialogIntent);
                collapsedView.setVisibility(View.GONE);
                expandedView.setVisibility(View.VISIBLE);
                showing = false;
                removeView();
            }
        });

        iv6.setImageResource(R.drawable.ic_red_cross);
        iv6.setBackgroundResource(R.drawable.alpha_red_background);

        // get the width and height of the screen
        final WindowManager window = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = window.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
        radius = width / 6;
        startAngle = -90;
        endAngle = 90;
        setSubItems();

        // set the cross button
        final WindowManager.LayoutParams params2 = new WindowManager.LayoutParams(
                width / 6,
                width / 6,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        //Specify the view position
        params2.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;        //Initially view will be added to top-left corner
//        params2.x = width / 2 - 5 * iv6.getWidth();
        params2.y = (height - height / 5);


        // run a counter to set alpha of the floating button
        timer = new CountDownTimer(VISIBLE_DUR, 10) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {

                // remove the full circle and the shortcuts automatically
                try {
                    collapsedView.setVisibility(View.GONE);
                    expandedView.setVisibility(View.VISIBLE);
                    removeView();
                } catch (Exception e) {
                }

            }
        }.start();

        //Drag and move floating view using user's touch action.
        mFloatingView.findViewById(R.id.root_container).setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                collapsedView.setVisibility(View.VISIBLE);
                expandedView.setVisibility(View.GONE);

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //remember the initial position.
                        initialX = params.x;
                        initialY = params.y;

                        //get the touch location
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        // add the delete view
                        try {
                            mWindowManager.addView(iv6, params2);
                        } catch (Exception e) {
                            Log.i("Rahul", "View already added");
                        }
                        try {
                            removeView();
                        } catch (Exception e) {
                            Log.i("Rahul", "View is not attached to the window manager");
                        }

                        //Calculate the X and Y coordinates of the view.
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);

                        //Update the layout with new X & Y coordinate
                        mWindowManager.updateViewLayout(mFloatingView, params);


                        if (params.x > (width / 2 - iv6.getWidth()) && params.x < (width / 2 + iv6.getWidth()) && params.y > (height - height / 5) && params.y < ((height - height / 5) + iv6.getHeight())) {

                            stopSelf();
                            window.removeViewImmediate(iv6);
                        }
                        return true;
                    case MotionEvent.ACTION_UP:

                        // auto adjust height if below or above certain height
                        if (params.y > (height - 150))
                            params.y = height - 150;
                        else if (params.y < 150)
                            params.y = 150;

                        try {
                            mWindowManager.removeView(iv6);
                        } catch (Exception e) {

                        }
                        int Xdiff = (int) (event.getRawX() - initialTouchX);
                        int Ydiff = (int) (event.getRawY() - initialTouchY);

                        if (params.x > mFloatingView.getWidth() && params.x < width - mFloatingView.getWidth()) {

                            //if the widget is in between the screen rather than the sides, do not add the buttons to the window manager
                            //just move the widget to side of the screen and leave
                            if (params.x > width / 2) {
                                params.x = width;
                                semiCircle.setImageResource(R.drawable.flipped);
                                left = false;
                            } else {
                                params.x = 0;
                                semiCircle.setImageResource(R.drawable.cropped);
                                left = true;
                            }


                            // set the radius of the semicircle according to the side
                            if (params.x == 0) {
                                startAngle = -90;
                                endAngle = 90;
                            } else {
                                startAngle = 90;
                                endAngle = 270;
                            }
                            mWindowManager.updateViewLayout(mFloatingView, params);
                            break;
                        } else {
                            //if the widget is in closer to either of the sides
                            //move the widget to side of the screen and add the buttons
                            if (params.x > width / 2) {
                                params.x = width;
                                semiCircle.setImageResource(R.drawable.flipped);
                                left = false;
                            } else {
                                params.x = 0;
                                semiCircle.setImageResource(R.drawable.cropped);
                                left = true;
                            }


                            // set the radius of the semicircle according to the side
                            if (params.x == 0) {
                                startAngle = -90;
                                endAngle = 90;
                            } else {
                                startAngle = 90;
                                endAngle = 270;
                            }
                            timer.start();
                            mWindowManager.updateViewLayout(mFloatingView, params);
                        }

                        //The check for Xdiff <10 && YDiff< 10 because sometime elements moves a little while clicking.
                        //So that is click event.
                        if (Xdiff < 10 && Ydiff < 10 && !showing) {
                            // Get the center of the action view from the following function for efficiency
                            // populate destination x,y coordinates of Items
                            Point center = calculateItemPositions();

                            for (int i = 0; i < subActionItems.size(); i++) {
                                //Add the view to the window.
                                final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                                        ViewGroup.LayoutParams.WRAP_CONTENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT,
                                        WindowManager.LayoutParams.TYPE_PHONE,
                                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                                        PixelFormat.TRANSLUCENT);
                                params.gravity = Gravity.TOP | Gravity.LEFT;
                                params.x = (int) subActionItems.get(i).x;
                                params.y = (int) subActionItems.get(i).y;

                                //Add the view to the window
                                mWindowManager.addView(subActionItems.get(i).view, params);
//                            }
                                Log.i("Rahul", "Param " + i + " " + params.x + " " + params.y);
//                                    animationHandler.animateMenuOpening(center);
                            }

                            collapsedView.setVisibility(View.VISIBLE);
                            expandedView.setVisibility(View.GONE);
                            showing = true;
                            Log.i("Rahul", "ACTION_UP if part TRUE");
                        } else {
                            showing = false;
                            collapsedView.setVisibility(View.GONE);
                            expandedView.setVisibility(View.VISIBLE);
                            removeView();
                        }
                }
                return false;
            }
        });

    }

    private void setSubItems() {
        subActionItems = new ArrayList<>();
        SharedPreferences prefs = getSharedPreferences("buttons", MODE_PRIVATE);
        for (int i = 0; i < 5; i++) {
            boolean val = prefs.getBoolean(i + "", true);
            if (val && i == 0)
                subActionItems.add(new SubItems(iv1, 40, 40));
            if (val && i == 1)
                subActionItems.add(new SubItems(iv2, 40, 40));
            if (val && i == 2)
                subActionItems.add(new SubItems(iv3, 40, 40));
            if (val && i == 3)
                subActionItems.add(new SubItems(iv4, 40, 40));
            if (val && i == 4)
                subActionItems.add(new SubItems(iv5, 40, 40));

        }
    }

    /**
     * Calculates the desired positions of all items.
     *
     * @return getActionViewCenter()
     */
    private Point calculateItemPositions() {
        // Create an arc that starts from startAngle and ends at endAngle
        // in an area that is as large as 4*radius^2
        final Point center = getActionViewCenter();
        center.y -= mFloatingView.getHeight();
        center.y += mFloatingView.getHeight() / 8;
        if (!left) {
            center.x -= 2 * mFloatingView.getWidth() / 3;
        }
        Log.i("Rahul", "Center is  = " + center.x + " " + center.y);
        RectF area = new RectF(center.x - radius, center.y - radius, center.x + radius, center.y + radius);


        Path orbit = new Path();
        orbit.addArc(area, startAngle, endAngle - startAngle);

        PathMeasure measure = new PathMeasure(orbit, false);

        // Prevent overlapping when it is a full circle
        int divisor;
        if (Math.abs(endAngle - startAngle) >= 360 || subActionItems.size() <= 1) {
            divisor = subActionItems.size();
        } else {
            divisor = subActionItems.size() - 1;
        }

        // Measure this path, in order to find points that have the same distance between each other
        for (int i = 0; i < subActionItems.size(); i++) {
            float[] coords = new float[]{0f, 0f};
            measure.getPosTan((i) * measure.getLength() / divisor, coords, null);
            // get the x and y values of these points and set them to each of sub action items.
            subActionItems.get(i).x = (int) coords[0] - subActionItems.get(i).width / 2;
            subActionItems.get(i).y = (int) coords[1] - subActionItems.get(i).height / 2;

            Log.i("Rahul", "Coordinate of subItems is  = " + subActionItems.get(i).x + " " + subActionItems.get(i).y);
        }
        return center;
    }

    /**
     * Returns the center point of the main action view
     *
     * @return the action view center point
     */
    public Point getActionViewCenter() {
        Point point = getActionViewCoordinates();
        point.x += mFloatingView.getMeasuredWidth() / 2;
        point.y += mFloatingView.getMeasuredHeight() / 2;
        return point;
    }

    /**
     * Gets the coordinates of the main action view
     * This method should only be called after the main layout of the Activity is drawn,
     * such as when a user clicks the action button.
     *
     * @return a Point containing x and y coordinates of the top left corner of action view
     */
    private Point getActionViewCoordinates() {
        int[] coords = new int[2];
        // This method returns a x and y values that can be larger than the dimensions of the device screen.
        mFloatingView.getLocationOnScreen(coords);

        return new Point(coords[0], coords[1]);
    }


    /**
     * Removes the subAction shortcuts
     * This method should only be called when the shortcuts are visible,
     * such as when a user clicks the action button.
     */
    private void removeView() {
        for (int i = 0; i < subActionItems.size(); i++) {
//            showing = false;
            Log.i("Rahul", "REMOVE_VIEW part FALSE");
            try {
                mWindowManager.removeViewImmediate(subActionItems.get(i).view);
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null) mWindowManager.removeView(mFloatingView);
    }

}
