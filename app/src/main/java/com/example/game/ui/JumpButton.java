package com.example.game.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.game.characters.Player;

public class JumpButton {
    private float centerX, centerY, radius;

    public JumpButton(float centerX, float centerY, float radius) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
    }

    public void press(float x, float y, Player pl) {
        if ((x - centerX) * (x - centerX) + (y - centerY) * (y - centerY) > radius*radius) {
            return;
        }
        if (!pl.getJumped()) {
            pl.setJumped(true);
            pl.setJumpSpeed();
        }
    }

    public void draw(Canvas c, Paint p) {
        p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(10);
        c.drawCircle(centerX, centerY, radius, p);
    }
}
