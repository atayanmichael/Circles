package com.circles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

public class BeautifulCirclesDrawView extends View {

    ArrayList<Circle> circles;


    public BeautifulCirclesDrawView(Context context) {
        super(context);
        init();
    }

    public BeautifulCirclesDrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BeautifulCirclesDrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        circles = new ArrayList<>();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < circles.size(); ++i) {
            Circle circle = circles.get(i);
            canvas.drawCircle(circle.x, circle.y, circle.radius, circle.paint);
        }
    }

    private boolean currentPointIsBusy = false;
    private int currentIndex = -1;
    private boolean currentCircleMoved = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int actionMasked = event.getActionMasked();
        float x = event.getX();
        float y = event.getY();
        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN:
                currentCircleMoved = false;
                currentPointIsBusy = false;
                for (int i = 0; i < circles.size(); ++i)
                    if (circles.get(i).inRange(x, y)) {
                        currentIndex = i;
                        currentPointIsBusy = true;
                        break;
                    }
                if (!currentPointIsBusy)
                    circles.add(new Circle(x, y));
                invalidate();
                return true;
            case MotionEvent.ACTION_MOVE:
                if (currentPointIsBusy)
                    if (currentIndex != -1) {
                        currentCircleMoved = true;
                        circles.get(currentIndex).x = x;
                        circles.get(currentIndex).y = y;
                        invalidate();
                    }
                return true;
            case MotionEvent.ACTION_UP:
                if (!currentCircleMoved && currentPointIsBusy)
                    circles.remove(currentIndex);
                invalidate();
                return true;
            default:
                break;

        }
        return super.onTouchEvent(event);
    }

    private class Circle {
        private float x;
        private float y;
        private float radius;
        private Paint paint;

        public Circle(float x, float y) {
            this.x = x;
            this.y = y;
            Random random = new Random();
            radius = 50 + random.nextInt(50);
            paint = new Paint();
            //set a beautiful color: alpha: 80-159, red: 20-69, green: 100-149, blue: 50-149
            paint.setColor(Color.argb(80 + random.nextInt(80), 20 + random.nextInt(50), 100 + random.nextInt(50), 50 + random.nextInt(100)));
        }

        public boolean inRange(float x, float y) {
            if ((this.x - x) * (this.x - x) + (this.y - y) * (this.y - y) < radius * radius)
                return true;
            return false;
        }
    }
}
