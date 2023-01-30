package com.example.game.buildlogic;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.game.core.GameView;
import com.example.game.util.Collisions;

public class Ramp extends Primitive {
    private float w, h, constL;
    private int siding;

    private float coeff;

    public float[] getRectCoords() {
        return new float[] {getX(),getY(),getX()+w,getY()+h};
    }

    public Ramp(float x, float y, float z, float w, float h, float l, int siding) {
        super(x, y, z, l, false);

        this.w = w;
        this.h = h;
        constL = l;

        switch (siding) {
            case 0: case 2:
                coeff = l / h;
                break;
            case 1: case 3:
                coeff = l / w;
        }

        this.siding = siding;
    }

    @Override
    public boolean checkY(float plX, float plY) {
        return false;
    }

    @Override
    public boolean checkWallCol(float[] plRectCoords, float plZ, float plL) {
        if ((plZ >= getL() + getZ())) {
            return false;
        }
        if (plZ + plL < getZ()) return false;

        return (Collisions.rectIntersect(plRectCoords, getRectCoords()));
    }

    public boolean checkLineCol(float[] plRectCoords, float plZ) {
        float dif;
        switch (siding) {
            case 0:
                if (plRectCoords[3] > getY() && plRectCoords[3] < getY() + h) {
                    dif = plRectCoords[3] - getY();
                    setL(dif * coeff);
                } else if (plRectCoords[3] <= getY()) setL(0);
                else if (plRectCoords[3] >= getY() + h) setL(constL);

                return Collisions.lineLineIntersect(plRectCoords[1], plZ, plRectCoords[3], plZ, getY(), getZ(), getY() + h, getZ() + constL);
            case 1:
                if (plRectCoords[2] > getX() && plRectCoords[2] < getX() + w) {
                    dif = plRectCoords[2] - getX();
                    setL(dif * coeff);
                } else if (plRectCoords[2] <= getX()) setL(0);
                else if (plRectCoords[2] >= getX() + w) setL(constL);

                return Collisions.lineLineIntersect(plRectCoords[0], plZ, plRectCoords[2], plZ, getX(), getZ(), getX() + w, getZ() + constL);
            case 2:
                if (plRectCoords[1] < getY() + h && plRectCoords[1] > getY()) {
                    dif = (getY() + h) - plRectCoords[1];
                    setL(dif * coeff);
                } else if (plRectCoords[1] >= getY() + h) setL(0);
                else if (plRectCoords[1] <= getY()) setL(constL);

                return Collisions.lineLineIntersect(plRectCoords[1], plZ, plRectCoords[3], plZ, getY(), getZ() + constL, getY() + h, getZ());
            case 3:
                if (plRectCoords[0] < getX() + w && plRectCoords[0] > getX()) {
                    dif = (getX() + w) - plRectCoords[0];
                    setL(dif * coeff);
                } else if (plRectCoords[0] >= getX() + w) setL(0);
                else if (plRectCoords[0] <= getX()) setL(constL);

                return Collisions.lineLineIntersect(plRectCoords[0], plZ, plRectCoords[2], plZ, getX(), getZ() + constL, getX() + w, getZ());
        }
        return false;
    }

    @Override
    public boolean checkTopCol(float[] plRectCoords, float plZ) {
        return (Collisions.rectIntersect(plRectCoords, getRectCoords()));
    }

    @Override
    public void drawWall(Canvas c, Paint p, float plX, float plY) {
        //FILL
        p.setColor(Color.DKGRAY);
        p.setStyle(Paint.Style.FILL);

        switch (siding) {
            case 0:
                c.drawRect((getX() + plX) * GameView.HEIGHT_MULTIP, (getY() + plY + h - constL - getZ()) * GameView.HEIGHT_MULTIP,
                        (getX() + plX + w) * GameView.HEIGHT_MULTIP, (getY() + plY + h - getZ()) * GameView.HEIGHT_MULTIP, p);
                break;
//            case 1:
//                c.drawLine(getX(), getY() + h - getZ() + plZ, getX() + w, getY() + h - getZ() + plZ, p);
//
//                c.drawLine(getX() + w, getY() + h - getZ() + plZ, getX() + w, getY() + h - constL - getZ() + plZ, p);
//
//                c.drawLine(getX() + w, getY() + h - constL - getZ() + plZ, getX(), getY() + h - getZ() + plZ, p);
//                break;
//            case 3:
//                c.drawLine(getX(), getY() + h - getZ() + plZ, getX() + w, getY() + h - getZ() + plZ, p);
//
//                c.drawLine(getX() + w, getY() + h - getZ() + plZ, getX(), getY() + h - constL - getZ() + plZ, p);
//
//                c.drawLine(getX(), getY() + h - getZ() + plZ, getX(), getY() + h - constL - getZ() + plZ, p);
        }

        //OUTLINE
        p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(10);

        switch (siding) {
            case 0:
                c.drawRect((getX() + plX) * GameView.HEIGHT_MULTIP, (getY() + plY + h - constL - getZ()) * GameView.HEIGHT_MULTIP,
                        (getX() + plX + w) * GameView.HEIGHT_MULTIP, (getY() + plY + h - getZ()) * GameView.HEIGHT_MULTIP, p);
                break;
            case 1:
                c.drawLine((getX() + plX) * GameView.HEIGHT_MULTIP, (getY() + plY + h - getZ()) * GameView.HEIGHT_MULTIP,
                        (getX() + plX + w) * GameView.HEIGHT_MULTIP, (getY() + plY + h - getZ()) * GameView.HEIGHT_MULTIP, p);

                c.drawLine((getX() + plX + w) * GameView.HEIGHT_MULTIP, (getY() + plY + h - getZ()) * GameView.HEIGHT_MULTIP,
                        (getX() + plX + w) * GameView.HEIGHT_MULTIP, (getY() + plY + h - constL - getZ()) * GameView.HEIGHT_MULTIP, p);

                c.drawLine((getX() + plX + w) * GameView.HEIGHT_MULTIP, (getY() + plY + h - constL - getZ()) * GameView.HEIGHT_MULTIP,
                        (getX() + plX) * GameView.HEIGHT_MULTIP, (getY() + plY + h - getZ()) * GameView.HEIGHT_MULTIP, p);
                break;
            case 3:
                c.drawLine((getX() + plX) * GameView.HEIGHT_MULTIP, (getY() + plY + h - getZ()) * GameView.HEIGHT_MULTIP,
                        (getX() + plX + w) * GameView.HEIGHT_MULTIP, (getY() + plY + h - getZ()) * GameView.HEIGHT_MULTIP, p);

                c.drawLine((getX() + plX + w) * GameView.HEIGHT_MULTIP, (getY() + plY + h - getZ()) * GameView.HEIGHT_MULTIP,
                        (getX() + plX) * GameView.HEIGHT_MULTIP, (getY() + plY + h - constL - getZ()) * GameView.HEIGHT_MULTIP, p);

                c.drawLine((getX() + plX) * GameView.HEIGHT_MULTIP, (getY() + plY + h - getZ()) * GameView.HEIGHT_MULTIP,
                        (getX() + plX) * GameView.HEIGHT_MULTIP, (getY() + plY + h - constL - getZ()) * GameView.HEIGHT_MULTIP, p);
        }
    }

    @Override
    public void drawRoof(Canvas c, Paint p, float plX, float plY) {
        //FILL
        p.setColor(Color.GRAY);
        p.setStyle(Paint.Style.FILL);

        switch (siding) {
            case 0:
                c.drawRect((getX() + plX) * GameView.HEIGHT_MULTIP, (getY() + plY - getZ()) * GameView.HEIGHT_MULTIP,
                        (getX() + plX + w) * GameView.HEIGHT_MULTIP, (getY() + plY + h - constL - getZ()) * GameView.HEIGHT_MULTIP, p);
                break;
//            case 1:
//                c.drawLine(getX(), getY() - getZ() + plZ, getX(), getY() + h - getZ() + plZ, p);
//
//                c.drawLine(getX(), getY() - getZ() + plZ, getX() + w, getY() - constL - getZ() + plZ, p);
//
//                c.drawLine(getX() + w, getY() - constL - getZ() + plZ, getX() + w, getY() + h - constL - getZ() + plZ, p);
//                break;
            case 2:
                c.drawRect((getX() + plX) * GameView.HEIGHT_MULTIP, (getY() + plY - constL - getZ()) * GameView.HEIGHT_MULTIP,
                        (getX() + plX + w) * GameView.HEIGHT_MULTIP, (getY() + plY + h - getZ()) * GameView.HEIGHT_MULTIP, p);
                break;
//            case 3:
//                c.drawLine(getX() + w, getY() - getZ() + plZ, getX() + w, getY() + h - getZ() + plZ, p);
//
//                c.drawLine(getX(), getY() - constL - getZ() + plZ, getX() + w, getY() - getZ() + plZ, p);
//
//                c.drawLine(getX(), getY() - constL - getZ() + plZ, getX(), getY() + h - constL - getZ() + plZ, p);
        }

        //OUTLINE
        p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(10);

        switch (siding) {
            case 0:
                if (getY() < getY() + h - getL())
                    c.drawRect((getX() + plX) * GameView.HEIGHT_MULTIP, (getY() + plY - getZ()) * GameView.HEIGHT_MULTIP,
                            (getX() + plX + w) * GameView.HEIGHT_MULTIP, (getY() + plY + h - constL - getZ()) * GameView.HEIGHT_MULTIP, p);
                break;
            case 1:
                c.drawLine((getX() + plX) * GameView.HEIGHT_MULTIP, (getY() + plY - getZ()) * GameView.HEIGHT_MULTIP,
                        (getX() + plX) * GameView.HEIGHT_MULTIP, (getY() + plY + h - getZ()) * GameView.HEIGHT_MULTIP, p);

                c.drawLine((getX() + plX) * GameView.HEIGHT_MULTIP, (getY() + plY - getZ()) * GameView.HEIGHT_MULTIP,
                        (getX() + plX + w) * GameView.HEIGHT_MULTIP, (getY() + plY - constL - getZ()) * GameView.HEIGHT_MULTIP, p);

                c.drawLine((getX() + plX + w) * GameView.HEIGHT_MULTIP, (getY() + plY - constL - getZ()) * GameView.HEIGHT_MULTIP,
                        (getX() + plX + w) * GameView.HEIGHT_MULTIP, (getY() + plY + h - constL - getZ()) * GameView.HEIGHT_MULTIP, p);
                break;
            case 2:
                c.drawRect((getX() + plX) * GameView.HEIGHT_MULTIP, (getY() + plY - constL - getZ()) * GameView.HEIGHT_MULTIP,
                        (getX() + plX + w) * GameView.HEIGHT_MULTIP, (getY() + plY + h - getZ()) * GameView.HEIGHT_MULTIP, p);
                break;
            case 3:
                c.drawLine((getX() + plX + w) * GameView.HEIGHT_MULTIP, (getY() + plY - getZ()) * GameView.HEIGHT_MULTIP,
                        (getX() + plX + w) * GameView.HEIGHT_MULTIP, (getY() + plY + h - getZ()) * GameView.HEIGHT_MULTIP, p);

                c.drawLine((getX() + plX) * GameView.HEIGHT_MULTIP, (getY() + plY - constL - getZ()) * GameView.HEIGHT_MULTIP,
                        (getX() + plX + w) * GameView.HEIGHT_MULTIP, (getY() + plY - getZ()) * GameView.HEIGHT_MULTIP, p);

                c.drawLine((getX() + plX) * GameView.HEIGHT_MULTIP, (getY() + plY - constL - getZ()) * GameView.HEIGHT_MULTIP,
                        (getX() + plX) * GameView.HEIGHT_MULTIP, (getY() + plY + h - constL - getZ()) * GameView.HEIGHT_MULTIP, p);
        }
    }
}
