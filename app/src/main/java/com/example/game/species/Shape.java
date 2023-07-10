package com.example.game.species;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.game.util.Vec;

public abstract class Shape {
    protected Vec[] vertex;
    protected Vec posV, dirV;
    protected double angle;
    protected float length;
    protected float r;

    public void draw(Canvas c, Paint p, float plX, float plY) {}
    public abstract void draw(Canvas c, Paint p, float plX, float plY, float l);

    public float getMaxLength() {
        return 0;
    }

    public Vec[] getVertex() {
        return vertex;
    }

    public void setVertex(Vec[] vertex) {
        this.vertex = vertex;
    }

    public Vec getPosV() {
        return posV;
    }

    public void setPosV(Vec posV) {
        this.posV = posV;
    }

    public Vec getDirV() {
        return dirV;
    }

    public void setDirV(Vec dirV) {
        this.dirV = dirV;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getR() {
        return r;
    }

    public void getVertices(double angle) {}
}
