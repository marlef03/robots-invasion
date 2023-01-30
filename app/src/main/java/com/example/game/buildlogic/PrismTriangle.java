package com.example.game.buildlogic;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import com.example.game.core.GameView;
import com.example.game.util.Collisions;

public class PrismTriangle extends Primitive {
    private float xSide, ySide;
    private int siding;

    public PrismTriangle(float x, float y, float z, float l, float length, int siding) {
        super(x, y, z, l, false);

        this.xSide = (float)((int)(length * Math.cos(Math.PI / 6)));
        this.ySide = (float)((int)(length * Math.sin(Math.PI / 6)));
        this.siding = siding;
    }

    public float[] getTrianCoords() {
        return new float[] {getX(), getY(), xSide, ySide};
    }

    @Override
    public boolean checkY(float plX, float plY) {
        if (siding == 0 || siding == 1)
            return plY >= getY() + ySide;
        else if (siding == 2 || siding == 3)
            return plY >= getY() - ySide;
        return false;
    }

    @Override
    public boolean checkWallCol(float[] plRectCoords, float plZ, float plL) {
        if ((plZ >= getL() + getZ()) || plZ + plL < getZ()) return false;

        switch (siding) {
            case 0: case 2:
                return Collisions.lineRectIntersect(getX(), getY() + ySide, getX() + xSide, getY(),
                        plRectCoords[0], plRectCoords[1], plRectCoords[2], plRectCoords[3]);
            case 1: case 3:
                return Collisions.lineRectIntersect(getX(), getY(), getX() + xSide, getY() + ySide,
                        plRectCoords[0], plRectCoords[1], plRectCoords[2], plRectCoords[3]);
        }
        return false;
    }

    @Override
    public boolean checkTopCol(float[] plRectCoords, float plZ) {
        return false;
    }

    @Override
    public void drawWall(Canvas c, Paint p, float plX, float plY) {
        //OUTLINE
        p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(10);

        switch (siding) {
            case 0: case 2:
                c.drawLine((getX() + plX) * GameView.HEIGHT_MULTIP, (getY() + plY + ySide - getZ()) * GameView.HEIGHT_MULTIP,
                        (getX() + plX + xSide) * GameView.HEIGHT_MULTIP, (getY() + plY - getZ()) * GameView.HEIGHT_MULTIP, p);

                c.drawLine((getX() + plX) * GameView.HEIGHT_MULTIP, (getY() + plY + ySide - getL() - getZ()) * GameView.HEIGHT_MULTIP,
                        (getX() + plX) * GameView.HEIGHT_MULTIP, (getY() + plY + ySide - getZ()) * GameView.HEIGHT_MULTIP, p);

                c.drawLine((getX() + plX + xSide) * GameView.HEIGHT_MULTIP, (getY()  + plY- getL() - getZ()) * GameView.HEIGHT_MULTIP,
                        (getX() + plX + xSide) * GameView.HEIGHT_MULTIP, (getY() + plY - getZ()) * GameView.HEIGHT_MULTIP, p);

                c.drawLine((getX() + plX) * GameView.HEIGHT_MULTIP, (getY() + plY + ySide - getL() - getZ()) * GameView.HEIGHT_MULTIP,
                        (getX() + plX + xSide) * GameView.HEIGHT_MULTIP, (getY() + plY - getL() - getZ()) * GameView.HEIGHT_MULTIP, p);
                break;
            case 1: case 3:
                c.drawLine((getX() + plX) * GameView.HEIGHT_MULTIP, (getY() + plY - getZ()) * GameView.HEIGHT_MULTIP,
                        (getX() + plX + xSide) * GameView.HEIGHT_MULTIP, (getY() + plY + ySide - getZ()) * GameView.HEIGHT_MULTIP, p);

                c.drawLine((getX() + plX) * GameView.HEIGHT_MULTIP, (getY() + plY - getL() - getZ()) * GameView.HEIGHT_MULTIP,
                        (getX() + plX) * GameView.HEIGHT_MULTIP, (getY() + plY - getZ()) * GameView.HEIGHT_MULTIP, p);

                c.drawLine((getX() + plX + xSide) * GameView.HEIGHT_MULTIP, (getY() + plY + ySide - getL() - getZ()) * GameView.HEIGHT_MULTIP,
                        (getX() + plX + xSide) * GameView.HEIGHT_MULTIP, (getY() + plY + ySide - getZ()) * GameView.HEIGHT_MULTIP, p);

                c.drawLine((getX() + plX) * GameView.HEIGHT_MULTIP, (getY() + plY - getL() - getZ()) * GameView.HEIGHT_MULTIP,
                        (getX() + plX + xSide) * GameView.HEIGHT_MULTIP, (getY() + plY + ySide - getL() - getZ()) * GameView.HEIGHT_MULTIP, p);

        }
    }

    @Override
    public void drawRoof(Canvas c, Paint p, float plX, float plY) {

    }
}
