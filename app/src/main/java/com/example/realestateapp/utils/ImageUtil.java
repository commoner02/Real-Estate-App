package com.example.realestateapp.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class ImageUtil {

    public static Bitmap createPlaceholderBitmap() {
        int width = 200;
        int height = 200;
        Bitmap.Config config = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(Color.LTGRAY);
        canvas.drawRect(0, 0, width, height, paint);
        paint.setColor(Color.BLACK);
        paint.setTextSize(50);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("No Image", width / 2, height / 2, paint);
        return bitmap;
    }
}
