package com.example.game.species;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.game.core.GameView;
import com.example.game.util.Matrix2;
import com.example.game.util.Vec;

import java.util.Arrays;
import java.util.Comparator;

public class Rectangle extends Shape {
    private Vec refDir;
    private int width;

    private Matrix2 rotMat;

    public Rectangle(float x1, float y1, float x2, float y2, int w) {
        vertex = new Vec[4];
        width = w;

        vertex[0] = new Vec(x1, y1);
        vertex[1] = new Vec(x2, y2);

        length = vertex[1].subtr(vertex[0]).mag();
        dirV = vertex[1].subtr(vertex[0]).unit();
        refDir = vertex[1].subtr(vertex[0]).unit();

        vertex[2] = vertex[1].add(dirV.normal().multy(w));
        vertex[3] = vertex[2].add(dirV.multy(-length));

        posV = vertex[0].add(dirV.multy(length / 2)).add(dirV.normal().multy((float)w / 2));


        rotMat = new Matrix2(2, 2);
        angle = 0;

        getVertices(0);
    }

    @Override
    public float getMaxLength() {
        return (float)Math.sqrt(width * width + length * length) / 2;
    }

    @Override
    public void getVertices(double angle) {
        rotMat.rotate(angle);
        dirV = rotMat.multyVec(refDir);
        vertex[0].set(posV.add(dirV.multy(-length / 2)).add(dirV.normal().multy((float)width / 2)));
        vertex[1].set(posV.add(dirV.multy(-length / 2)).add(dirV.normal().multy(-(float)width / 2)));
        vertex[2].set(posV.add(dirV.multy(length / 2)).add(dirV.normal().multy(-(float)width / 2)));
        vertex[3].set(posV.add(dirV.multy(length / 2)).add(dirV.normal().multy((float)width / 2)));
    }

    @Override
    public void draw(Canvas c, Paint p, float plX, float plY) {
        p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(5);

        c.drawLine((vertex[0].x + plX) * GameView.HEIGHT_MULTIP, (vertex[0].y + plY) * GameView.HEIGHT_MULTIP,
                (vertex[1].x + plX) * GameView.HEIGHT_MULTIP, (vertex[1].y + plY) * GameView.HEIGHT_MULTIP, p);

        c.drawLine((vertex[1].x + plX) * GameView.HEIGHT_MULTIP, (vertex[1].y + plY) * GameView.HEIGHT_MULTIP,
                (vertex[2].x + plX) * GameView.HEIGHT_MULTIP, (vertex[2].y + plY) * GameView.HEIGHT_MULTIP, p);

        c.drawLine((vertex[2].x + plX) * GameView.HEIGHT_MULTIP, (vertex[2].y + plY) * GameView.HEIGHT_MULTIP,
                (vertex[3].x + plX) * GameView.HEIGHT_MULTIP, (vertex[3].y + plY) * GameView.HEIGHT_MULTIP, p);

        c.drawLine((vertex[3].x + plX) * GameView.HEIGHT_MULTIP, (vertex[3].y + plY) * GameView.HEIGHT_MULTIP,
                (vertex[0].x + plX) * GameView.HEIGHT_MULTIP, (vertex[0].y + plY) * GameView.HEIGHT_MULTIP, p);
    }

    @Override
    public void draw(Canvas c, Paint p, float plX, float plY, float l) {
        p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(5);

        c.drawLine((vertex[0].x + plX) * GameView.HEIGHT_MULTIP, (vertex[0].y + plY) * GameView.HEIGHT_MULTIP,
                (vertex[1].x + plX) * GameView.HEIGHT_MULTIP, (vertex[1].y + plY) * GameView.HEIGHT_MULTIP, p);

        c.drawLine((vertex[1].x + plX) * GameView.HEIGHT_MULTIP, (vertex[1].y + plY) * GameView.HEIGHT_MULTIP,
                (vertex[2].x + plX) * GameView.HEIGHT_MULTIP, (vertex[2].y + plY) * GameView.HEIGHT_MULTIP, p);

        c.drawLine((vertex[2].x + plX) * GameView.HEIGHT_MULTIP, (vertex[2].y + plY) * GameView.HEIGHT_MULTIP,
                (vertex[3].x + plX) * GameView.HEIGHT_MULTIP, (vertex[3].y + plY) * GameView.HEIGHT_MULTIP, p);

        c.drawLine((vertex[3].x + plX) * GameView.HEIGHT_MULTIP, (vertex[3].y + plY) * GameView.HEIGHT_MULTIP,
                (vertex[0].x + plX) * GameView.HEIGHT_MULTIP, (vertex[0].y + plY) * GameView.HEIGHT_MULTIP, p);


        c.drawLine((vertex[0].x + plX) * GameView.HEIGHT_MULTIP, (vertex[0].y + plY) * GameView.HEIGHT_MULTIP,
                (vertex[0].x + plX) * GameView.HEIGHT_MULTIP, (vertex[0].y + plY - l) * GameView.HEIGHT_MULTIP, p);

        c.drawLine((vertex[1].x + plX) * GameView.HEIGHT_MULTIP, (vertex[1].y + plY) * GameView.HEIGHT_MULTIP,
                (vertex[1].x + plX) * GameView.HEIGHT_MULTIP, (vertex[1].y + plY - l) * GameView.HEIGHT_MULTIP, p);

        c.drawLine((vertex[2].x + plX) * GameView.HEIGHT_MULTIP, (vertex[2].y + plY) * GameView.HEIGHT_MULTIP,
                (vertex[2].x + plX) * GameView.HEIGHT_MULTIP, (vertex[2].y + plY - l) * GameView.HEIGHT_MULTIP, p);

        c.drawLine((vertex[3].x + plX) * GameView.HEIGHT_MULTIP, (vertex[3].y + plY) * GameView.HEIGHT_MULTIP,
                (vertex[3].x + plX) * GameView.HEIGHT_MULTIP, (vertex[3].y + plY - l) * GameView.HEIGHT_MULTIP, p);


        c.drawLine((vertex[0].x + plX) * GameView.HEIGHT_MULTIP, (vertex[0].y + plY - l) * GameView.HEIGHT_MULTIP,
                (vertex[1].x + plX) * GameView.HEIGHT_MULTIP, (vertex[1].y + plY - l) * GameView.HEIGHT_MULTIP, p);

        c.drawLine((vertex[1].x + plX) * GameView.HEIGHT_MULTIP, (vertex[1].y + plY - l) * GameView.HEIGHT_MULTIP,
                (vertex[2].x + plX) * GameView.HEIGHT_MULTIP, (vertex[2].y + plY - l) * GameView.HEIGHT_MULTIP, p);

        c.drawLine((vertex[2].x + plX) * GameView.HEIGHT_MULTIP, (vertex[2].y + plY - l) * GameView.HEIGHT_MULTIP,
                (vertex[3].x + plX) * GameView.HEIGHT_MULTIP, (vertex[3].y + plY - l) * GameView.HEIGHT_MULTIP, p);

        c.drawLine((vertex[3].x + plX) * GameView.HEIGHT_MULTIP, (vertex[3].y + plY - l) * GameView.HEIGHT_MULTIP,
                (vertex[0].x + plX) * GameView.HEIGHT_MULTIP, (vertex[0].y + plY - l) * GameView.HEIGHT_MULTIP, p);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
