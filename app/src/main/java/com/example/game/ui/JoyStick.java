package com.example.game.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.example.game.characters.Player;
import com.example.game.core.GameView;

public class JoyStick {
    private float centerX, centerY;
    private float jX, jY;

    private float radiusBit, radiusJoy;
    private float radius;

    private boolean tr;
    private int id;

    private float xDir, yDir;

    public float getXDir() {
        return xDir;
    }

    public float getYDir() {
        return yDir;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public JoyStick(float x, float y, float radius, float radJ) {
        centerX = x;
        centerY = y;
        radiusBit = radius;
        radiusJoy = radJ;
        jX = centerX;
        jY = centerY;
        this.radius = radiusBit;
    }

    public boolean onClick(float x, float y) {
        float exDir = x - centerX;
        float eyDir = y - centerY;
        float r = exDir*exDir + eyDir*eyDir;

        return r <= radius*radius;
    }

    public void calc(float x, float y) {
        tr = false;

        float r = (float)Math.sqrt(Math.pow((x - centerX), 2) + Math.pow((y - centerY), 2));

        xDir = (x - centerX) / r;
        yDir = (y - centerY) / r;

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

    public void reset() {
        yDir = xDir = 0;

        tr = true;
    }

    public void draw(Canvas c, Paint p) {
        p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(10);
        c.drawCircle(centerX, centerY, radiusBit, p);

        p.setColor(Color.BLUE);
        p.setStyle(Paint.Style.FILL);
        c.drawCircle(jX, jY, radiusJoy, p);
    }
}
