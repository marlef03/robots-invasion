package com.example.game.species;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.game.core.GameView;
import com.example.game.util.Vec;

public class Circle extends Shape {
    public Circle(float x, float y, float r) {
        vertex = new Vec[0];

        posV = new Vec(x, y);
        this.r = r;

        dirV = new Vec(1, 0);
    }

    public Circle(Vec pos, float r) {
        vertex = new Vec[0];

        posV = pos;
        this.r = r;
    }

    @Override
    public float getMaxLength() {
        return r;
    }

    public void draw(Canvas c, Paint p, float plX, float plY) {
        p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(5);

        c.drawCircle((posV.x + plX) * GameView.HEIGHT_MULTIP, (posV.y + plY) * GameView.HEIGHT_MULTIP, r * GameView.HEIGHT_MULTIP, p);
    }

    public void draw(Canvas c, Paint p, float plX, float plY, int alpha) {
        p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(5);

        p.setAlpha(alpha);

        c.drawCircle((posV.x + plX) * GameView.HEIGHT_MULTIP, (posV.y + plY) * GameView.HEIGHT_MULTIP, r * GameView.HEIGHT_MULTIP, p);

        p.setAlpha(255);
    }

    @Override
    public void draw(Canvas c, Paint p, float plX, float plY, float l) {
        p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(5);

        c.drawArc((posV.x - r + plX) * GameView.HEIGHT_MULTIP, (posV.y - r + plY) * GameView.HEIGHT_MULTIP,
                (posV.x + r + plX) * GameView.HEIGHT_MULTIP, (posV.y + r + plY) * GameView.HEIGHT_MULTIP, 0, 180, false, p);

        c.drawLine((posV.x + plX - r) * GameView.HEIGHT_MULTIP, (posV.y + plY) * GameView.HEIGHT_MULTIP,
                (posV.x + plX - r) * GameView.HEIGHT_MULTIP, (posV.y + plY - l) * GameView.HEIGHT_MULTIP, p);

        c.drawLine((posV.x + plX + r) * GameView.HEIGHT_MULTIP, (posV.y + plY) * GameView.HEIGHT_MULTIP,
                (posV.x + plX + r) * GameView.HEIGHT_MULTIP, (posV.y + plY - l) * GameView.HEIGHT_MULTIP, p);


        c.drawCircle((posV.x + plX) * GameView.HEIGHT_MULTIP, (posV.y + plY - l) * GameView.HEIGHT_MULTIP, r * GameView.HEIGHT_MULTIP, p);
    }
}