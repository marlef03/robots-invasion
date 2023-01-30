package com.example.game.buildlogic;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class Primitive {
    protected float x, y, z, l;
    protected float viewX, viewY;
    protected float[] alphaStatus;

    protected final boolean isTrigger;
    protected boolean active;

    public boolean getTrigger() {
        return isTrigger;
    }

    public void setActive(boolean b) {
        active = b;
    }

    public float[] getAlphaStatus() {
        return alphaStatus;
    }

    public void setAlphaStatus(float[] alphaStatus) {
        this.alphaStatus = alphaStatus;
    }

    public void setL(float l) {
        this.l = l;
    }
    public float getL() {
        return l;
    }
    public void setZ(float vz) {
        z = vz;
    }
    public float getZ() {
        return z;
    }
    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }
    public void setX(float vx) {
        x = vx;
    }
    public void setY(float vy) {
        y = vy;
    }
    public float getViewX() {
        return viewX;
    }
    public float getViewY() {
        return viewY;
    }
    public Primitive(float x, float y, float z, float l, boolean isTrigger) {
        this.x = viewX = x;
        this.y = viewY = y;
        this.z = z;
        this.l = l;

        this.isTrigger = isTrigger;

        alphaStatus = new float[] {0,0};
    }

    public abstract boolean checkY(float plX, float plY);

    public abstract boolean checkWallCol(float[] plRectCoords, float plZ, float plL);
    public abstract boolean checkTopCol(float[] plRectCoords, float plZ);
    public boolean checkUnderCol(float plZ, float plL) {
        return (plZ + plL >= z) && (plZ + plL < z + l) && (plZ != z);
    }

    public abstract void drawWall(Canvas c, Paint p, float plX, float plY);
    public abstract void drawRoof(Canvas c, Paint p, float plX, float plY);
}
