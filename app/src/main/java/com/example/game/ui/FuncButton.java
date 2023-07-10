package com.example.game.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class FuncButton {
    protected float centerX, centerY, radius;
    protected float x2, y2;
    protected boolean press;
    protected boolean active;

    protected Bitmap image;

    public FuncButton(float centerX, float centerY, float radius) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
    }

    public FuncButton(float x1, float y1, float x2, float y2) {
        centerX = x1;
        centerY = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public void setImage(Bitmap image) {
        if (!(this.image == image)) this.image = Bitmap.createScaledBitmap(image, (int)(radius * 2), (int)(radius * 2), false);
    }

    public boolean getPress() {
        return press;
    }

    public void setPress(boolean press) {
        this.press = press;
    }

    public boolean onClick(float x, float y) {
        if (x2 == 0) return (x - centerX) * (x - centerX) + (y - centerY) * (y - centerY) <= radius*radius && active;
        else return x >= centerX && x <= x2 && y >= centerY && y <= y2 && active;
    }

    public void click(float x, float y) {
        press = onClick(x, y);
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void draw(Canvas c, Paint p) {
        if (active) {
            if (x2 == 0) c.drawBitmap(image, centerX - radius, centerY - radius, p);
            else c.drawRect(centerX, centerY, x2, y2, p);
        }
    }
}
