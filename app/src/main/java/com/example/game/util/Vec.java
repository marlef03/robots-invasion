package com.example.game.util;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.annotation.NonNull;

public class Vec {
    public float x;
    public float y;

    public Vec() {
        x = 0;
        y = 0;
    }

    public Vec(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void set(Vec pos) {
        x = pos.x;
        y = pos.y;
    }

    public Vec add(Vec v) {
        return new Vec(x + v.x, y + v.y);
    }

    public Vec subtr(Vec v) {
        return new Vec(x - v.x, y - v.y);
    }

    public float mag() {
        return (float)Math.sqrt(x * x + y * y);
    }

    public Vec multy(float m) {
        return new Vec(x * m, y * m);
    }

    public Vec unit(){
        if(this.mag() == 0){
            return new Vec();
        } else {
            return new Vec(x / mag(), y / mag());
        }
    }

    public Vec normal() {
        return new Vec(-y, x).unit();
    }

    public static float dot(Vec a, Vec b) {
        return a.x * b.x + a.y * b.y;
    }

    public static float cross(Vec a, Vec b) {
        return a.x * b.y - a.y * b.x;
    }

    public void draw(Canvas c, Paint p, float startX, float startY, float length) {
        p.setColor(Color.MAGENTA);
        c.drawLine(startX, startY, startX + x * length, startY + y * length, p);
    }

    @NonNull
    @Override
    public String toString() {
        return "{" + x + "; " + y + "}";
    }
}
