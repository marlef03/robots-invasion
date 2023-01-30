package com.example.game.buildlogic;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

import com.example.game.core.GameView;
import com.example.game.util.Collisions;

public class Prism4 extends Primitive {
    private float leftSide,rightSide;
    private float leftSideX, leftSideY, rightSideX, rightSideY;

    public Prism4(float x, float y, float z, float l, float leftSide, float rightSide) {
        super(x, y, z, l, false);
        this.leftSide = leftSide;
        this.rightSide = rightSide;

        leftSideX = leftSide * (float)Math.cos(Math.PI / 6);
        leftSideY = leftSide * (float)Math.sin(Math.PI / 6);
        rightSideX = rightSide * (float)Math.cos(Math.PI / 6);
        rightSideY = rightSide * (float)Math.sin(Math.PI / 6);
    }

    public Pointc[] getPoints() {
        return new Pointc[] {
                new Pointc(x + rightSideX, y),
                new Pointc(x + leftSideX + rightSideX, y + leftSideY),
                new Pointc(x + leftSideX, y + rightSideY + leftSideY),
                new Pointc(x, y + rightSideY)
        };
    }

    @Override
    public boolean checkY(float plX, float plY) {
        return plX <= x + rightSideX ? plY >= y + rightSideY : plY >= y + leftSideY;
    }

    @Override
    public boolean checkWallCol(float[] plRectCoords, float plZ, float plL) {
        if ((plZ >= getL() + getZ())) {
            return false;
        }

        if (plZ + plL < getZ()) return false;
        return Collisions.polyRectIntersect(getPoints(), plRectCoords[0], plRectCoords[1], plRectCoords[2], plRectCoords[3], false);
    }

    @Override
    public boolean checkTopCol(float[] plRectCoords, float plZ) {
        return Collisions.polyRectIntersect(getPoints(), plRectCoords[0], plRectCoords[1], plRectCoords[2], plRectCoords[3], true);
    }

    @Override
    public void drawWall(Canvas c, Paint p, float plX, float plY) {
        //FILL
        p.setColor(Color.DKGRAY);
        p.setStyle(Paint.Style.FILL);

        //OUTLINE
        p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(10);

        c.drawLine((getPoints()[1].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[1].y + plY - z) * GameView.HEIGHT_MULTIP,
                (getPoints()[2].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[2].y + plY - z) * GameView.HEIGHT_MULTIP, p);

        c.drawLine((getPoints()[2].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[2].y + plY - z) * GameView.HEIGHT_MULTIP,
                (getPoints()[3].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[3].y + plY - z) * GameView.HEIGHT_MULTIP, p);


        c.drawLine((getPoints()[3].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[3].y + plY - l - z) * GameView.HEIGHT_MULTIP,
                (getPoints()[3].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[3].y + plY - z) * GameView.HEIGHT_MULTIP, p);

        c.drawLine((getPoints()[2].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[2].y + plY - l - z) * GameView.HEIGHT_MULTIP,
                (getPoints()[2].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[2].y + plY - z) * GameView.HEIGHT_MULTIP, p);

        c.drawLine((getPoints()[1].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[1].y + plY - l - z) * GameView.HEIGHT_MULTIP,
                (getPoints()[1].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[1].y + plY - z) * GameView.HEIGHT_MULTIP, p);
    }

    @Override
    public void drawRoof(Canvas c, Paint p, float plX, float plY) {
        //FILL
        p.setColor(Color.GRAY);
        p.setStyle(Paint.Style.FILL);

        //OUTLINE
        p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(10);

        c.drawLine((getPoints()[0].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[0].y + plY - l - z) * GameView.HEIGHT_MULTIP,
                (getPoints()[1].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[1].y + plY - l - z) * GameView.HEIGHT_MULTIP, p);

        c.drawLine((getPoints()[1].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[1].y + plY - l - z) * GameView.HEIGHT_MULTIP,
                (getPoints()[2].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[2].y + plY - l - z) * GameView.HEIGHT_MULTIP, p);

        c.drawLine((getPoints()[2].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[2].y + plY - l - z) * GameView.HEIGHT_MULTIP,
                (getPoints()[3].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[3].y + plY - l - z) * GameView.HEIGHT_MULTIP, p);

        c.drawLine((getPoints()[3].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[3].y + plY - l - z) * GameView.HEIGHT_MULTIP,
                (getPoints()[0].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[0].y + plY - l - z) * GameView.HEIGHT_MULTIP, p);
    }
}
