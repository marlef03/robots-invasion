package com.example.game.util;

import android.graphics.Rect;

import com.example.game.buildlogic.Pointc;

public class Collisions {
    public static boolean rectIntersect(float[] r1, float[] r2) {
        return r1[0] < r2[2] && r1[2] > r2[0] &&
                r1[1] < r2[3] && r1[3] > r2[1];
    }

    public static boolean rectPointIntersect(float[] r1, float[] p) {
        return r1[0] < p[0] && r1[2] > p[0] &&
                r1[1] < p[1] && r1[3] > p[1];
    }

    public static boolean polyRectIntersect(Pointc[] vertices, float rx, float ry, float rw, float rh, boolean isInside) {
        boolean collision2 = false;

        int next = 0;
        for (int current=0; current<vertices.length; current++) {

            next = current+1;
            if (next == vertices.length) next = 0;

            Pointc pc = vertices[current];
            Pointc pn = vertices[next];

            boolean collision = lineRectIntersect(pc.x,pc.y,pn.x,pn.y, rx,ry,rw,rh);
            if (collision) return true;

            if (isInside) {
                if (((pc.y > ry && pn.y < ry) || (pc.y < ry && pn.y > ry)) &&
                        (rx < (pn.x - pc.x) * (ry - pc.y) / (pn.y - pc.y) + pc.x)) {
                    collision2 = !collision2;
                }
            }
        }
        return collision2;
    }

    public static boolean polyPointIntersect(Pointc[] vertices, float px, float py) {
        boolean collision = false;

        int next = 0;
        for (int current=0; current<vertices.length; current++) {

            next = current+1;
            if (next == vertices.length) next = 0;

            Pointc pc = vertices[current];
            Pointc pn = vertices[next];

            if (((pc.y > py && pn.y < py) || (pc.y < py && pn.y > py)) &&
                    (px < (pn.x-pc.x)*(py-pc.y) / (pn.y-pc.y)+pc.x)) {
                collision = !collision;
            }
        }
        return collision;
    }

    public static boolean lineRectIntersect(float x1, float y1, float x2, float y2, float rx, float ry, float rw, float rh) {
        boolean left =   lineLineIntersect(x1,y1,x2,y2, rx, ry, rx, rh);
        boolean right =  lineLineIntersect(x1,y1,x2,y2, rw, ry, rw, rh);
        boolean top =    lineLineIntersect(x1,y1,x2,y2, rx, ry, rw, ry);
        boolean bottom = lineLineIntersect(x1,y1,x2,y2, rx, rh, rw, rh);

        if (left || right || top || bottom) {
            return true;
        }
        return false;
    }


    public static boolean lineLineIntersect(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        float uA = ((x4-x3)*(y1-y3) - (y4-y3)*(x1-x3)) / ((y4-y3)*(x2-x1) - (x4-x3)*(y2-y1));
        float uB = ((x2-x1)*(y1-y3) - (y2-y1)*(x1-x3)) / ((y4-y3)*(x2-x1) - (x4-x3)*(y2-y1));

        if (uA >= 0 && uA <= 1 && uB >= 0 && uB <= 1) {
            return true;
        }
        return false;
    }

    public static boolean trianglePointIntersect(float[] trianCoords, int siding, float[] p) {
        float coeff = trianCoords[3] / trianCoords[2];
        switch (siding) {
            case 0:
                return (trianCoords[1] + trianCoords[3] - p[1] < (trianCoords[2] - (trianCoords[0] + trianCoords[2] - p[0])) * coeff
                        && p[0] < trianCoords[0] + trianCoords[2] && p[1] < trianCoords[1] + trianCoords[3]);
            case 1:
                return ((trianCoords[1] + trianCoords[3] - p[1]) < (trianCoords[2] - (p[0] - trianCoords[0])) * coeff
                    && p[0] > trianCoords[0] && p[1] < trianCoords[1] + trianCoords[3]);
            case 2:
                return (p[1] - trianCoords[1] < (trianCoords[2] - (p[0] - trianCoords[0])) * coeff && p[0] > trianCoords[0] && p[1] > trianCoords[1]);
            case 3:
                return (p[1] - trianCoords[1] < (trianCoords[2] - (trianCoords[0] + trianCoords[2] - p[0])) * coeff
                        && p[0] < trianCoords[0]+ trianCoords[2] && p[1] > trianCoords[1]);
        }
        return false;
    }

    public static boolean rectDisIntersect(float[] r1, float[] r2) {
        float[] r3 = new float[]{r1[0], r1[1], r1[0], r1[1]};
        if (!rectIntersect(r3, r2)) return true;
        r3 = new float[]{r1[0], r1[3], r1[0], r1[3]};
        if (!rectIntersect(r3, r2)) return true;
        r3 = new float[]{r1[2], r1[1], r1[2], r1[1]};
        if (!rectIntersect(r3, r2)) return true;
        r3 = new float[]{r1[2], r1[3], r1[2], r1[3]};
        return !rectIntersect(r3, r2);
    }

    public static boolean circleIntersect(float[] rect, float[] center, float radius) {
        radius *= radius;
        float xSide = center[0] - rect[0];
        float ySide = center[1] - rect[1];
        if (xSide * xSide + ySide * ySide < radius) return true;
        xSide = rect[2] - center[0];
        if (xSide * xSide + ySide * ySide < radius) return true;
        ySide = rect[3] - center[1];
        if (xSide * xSide + ySide * ySide < radius) return true;
        xSide = center[0] - rect[0];
        if (xSide * xSide + ySide * ySide < radius) return true;

        return false;
    }

    public static boolean circleDisIntersect(float[] rect, float[] center, float radius) {
        radius *= radius;
        float xSide = center[0] - rect[0];
        float ySide = center[1] - rect[1];
        if (xSide * xSide + ySide * ySide >= radius) return true;
        xSide = rect[2] - center[0];
        if (xSide * xSide + ySide * ySide >= radius) return true;
        ySide = rect[3] - center[1];
        if (xSide * xSide + ySide * ySide >= radius) return true;
        xSide = center[0] - rect[0];
        if (xSide * xSide + ySide * ySide >= radius) return true;

        return false;
    }

    public static boolean ovalIntersect(float[] rect, float[] center, float radius, float circleCoeff) {
        float plX = rect[0] - center[0];
        float plY = (rect[1] - center[1]) * circleCoeff;
        radius *= radius;

        if (plX * plX + plY * plY < radius) return true;

        plX = center[0] - rect[2];

        if (plX * plX + plY * plY < radius) return true;

        plY = (center[1] - rect[3]) * circleCoeff;

        if (plX * plX + plY * plY < radius) return true;

        plX = rect[0] - center[0];

        if (plX * plX + plY * plY < radius) return true;

        plX = (rect[0] + ((rect[2] - rect[0]) / 2) - center[0]);
        plY = (rect[1] - center[1]) * circleCoeff;

        if (plX * plX + plY * plY < radius) return true;

        plX = center[0] - rect[2];
        plY = (rect[1] + ((rect[3] - rect[1]) / 2) - center[1]) * circleCoeff;

        if (plX * plX + plY * plY < radius) return true;

        plX = (center[0] - (rect[2] - ((rect[2] - rect[0]) / 2)));
        plY = (center[1] - rect[3]) * circleCoeff;

        if (plX * plX + plY * plY < radius) return true;

        plX = rect[0] - center[0];
        plY = (center[1] - (rect[3] - (rect[3] - rect[1]) / 2)) * circleCoeff;

        if (plX * plX + plY * plY < radius) return true;
        return false;
    }
}
