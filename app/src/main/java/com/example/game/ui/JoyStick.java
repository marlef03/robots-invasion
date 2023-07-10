package com.example.game.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.example.game.MainActivity;
import com.example.game.characters.Player;
import com.example.game.core.GameView;

public class JoyStick {
    private float jX, jY;
    private float radiusJoy;
    protected float centerX, centerY;
    protected float radius;
    protected boolean tr;
    protected float xDir, yDir;

    protected float distToCenter;

    protected boolean hold, click;

    protected Bitmap stickImage;

    public JoyStick(float x, float y, float radius, float radJ) {
        centerX = x;
        centerY = y;
        this.radius = radius;

        radiusJoy = radJ;
        jX = centerX;
        jY = centerY;
    }

    public float getXDir() {
        return xDir;
    }

    public float getYDir() {
        return yDir;
    }

    public float getDistToCenter() {
        return distToCenter;
    }

    public float getRadius() {
        return radius;
    }

    public boolean onClick(float x, float y) {
        return (x - centerX) * (x - centerX) + (y - centerY) * (y - centerY) <= radius*radius;
    }

    public boolean getHold() {
        return hold;
    }

    public void setHold(boolean hold) {
        this.hold = hold;
    }

    public boolean getClick() {
        return click;
    }

    public void setClick(boolean click) {
        this.click = click;
    }

    public void reset() {
        yDir = xDir = distToCenter = 0;

        tr = true;
    }

    public void setStickImage(Bitmap image) {
        this.stickImage = Bitmap.createScaledBitmap(image, (int)(radiusJoy * 2), (int)(radiusJoy * 2), false);
    }

    public void calc(float x, float y) {
        tr = false;

        distToCenter = (float) Math.sqrt(Math.pow((x - centerX), 2) + Math.pow((y - centerY), 2));

        xDir = (x - centerX) / distToCenter;
        yDir = (y - centerY) / distToCenter;

        jX = x;
        jY = y;
    }

    public void translate() {
        if (tr) {
            jX += (centerX - jX) / (9 * GameView.HEIGHT_MULTIP);
            jY += (centerY - jY) / (9 * GameView.HEIGHT_MULTIP);
            if ((int)jX == 0 && (int)jY == 0 ) {
                tr = false;
                jX = centerX;
                jY = centerY;
            }
        }
    }

    public void draw(Canvas c, Paint p) {
        p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(10 * GameView.HEIGHT_MULTIP);
        c.drawCircle(centerX, centerY, radius, p);

        p.setColor(Color.BLUE);
        p.setStyle(Paint.Style.FILL);
        c.drawCircle(jX, jY, radiusJoy, p);

        if (stickImage != null) c.drawBitmap(stickImage, jX - radiusJoy, jY - radiusJoy, p);
    }
}
