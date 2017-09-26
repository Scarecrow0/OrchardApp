package com.scarecrow.root.orchardapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by root on 17-9-26.
 */

public class ViewDonateTree extends View {
    private Paint paint;
    private Bitmap canvas_bg = BitmapFactory.decodeResource(getResources(), R.drawable.donate_tree),
            apple_img = BitmapFactory.decodeResource(getResources(), R.drawable.donate_tree_apple);
    private List<Coordinate> mApplesPosition = new ArrayList<>();
    Random random = new Random();

    public ViewDonateTree(Context context, AttributeSet atts) {
        super(context, atts);
        paint = new Paint();
        paint.setColor(Color.RED);
        postInvalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(canvas_bg, 0, 0, paint);
        for (Coordinate c : mApplesPosition) {
            canvas.drawBitmap(apple_img, c.x, c.y, paint);
        }
    }


    public void spanApplesCoordinations(int apple_num) {
        for (int i = 0; i < apple_num; i++) {
            Coordinate c = new Coordinate(250 + random.nextInt(600),
                    370 + random.nextInt(800));
            while (!distantCheck(c)) {
                c = new Coordinate(250 + random.nextInt(600),
                        370 + random.nextInt(800));
            }
            mApplesPosition.add(c);
        }
        postInvalidate();


    }

    private boolean distantCheck(Coordinate c) {
        for (Coordinate ci : mApplesPosition) {
            if (EucuidDis(ci, c) < 60)
                return false;
        }
        return true;
    }

    private double EucuidDis(Coordinate c1, Coordinate c2) {
        return Math.pow((Math.pow((c1.x - c2.x), 2) + Math.pow((c1.y - c2.y), 2)), 0.5);
    }

    private class Coordinate {
        public float x, y;

        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
