package com.example.game.buildlogic;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.game.util.Collisions;

public class Cylinder extends Primitive {
    private float horRad, verRad;

    public Cylinder(float x, float y, float z, float l, float radius) {
        super(x, y, z, l, false);
        this.horRad = radius;
        this.verRad = radius / 2;
    }

    public float getRadius() {
        return horRad;
    }
    public float getCoeff() {
        return horRad / verRad;
    }

    public float[] getCenter() {
        return new float[] {getX() + horRad, getY() + verRad};
    }

    public float[] getRectCoordsAbove() {
        return new float[] {getX(), getY() - (getZ()+getL()), getX()+horRad*2, getY() - (getZ()+getL())+verRad+getL()};
    }

    @Override
    public boolean checkY(float plX, float plY) {
        return plY >= getY() + verRad;
    }

    @Override
    public boolean checkWallCol(float[] plRectCoords, float plZ, float plL) {
        if ((plZ >= getL() + getZ())) {
            return false;
        }
        if (plZ + plL < getZ() + getL()) {
            if (Collisions.rectIntersect(plRectCoords, getRectCoordsAbove())) {
                float difX, difY;

                if (plRectCoords[2] - getRectCoordsAbove()[0] < 150) {
                    difX = plRectCoords[2] - getRectCoordsAbove()[0];
                } else if (getRectCoordsAbove()[2] - plRectCoords[0] < 150) {
                    difX = getRectCoordsAbove()[2] - plRectCoords[0];
                } else {
                    difX = 150;
                }

                if (plRectCoords[3] - getRectCoordsAbove()[1] < 150) {
                    difY = plRectCoords[3] - getRectCoordsAbove()[1];
                } else if (getRectCoordsAbove()[3] - plRectCoords[1] < 150) {
                    difY = getRectCoordsAbove()[3] - plRectCoords[1];
                } else {
                    difY = 150;
                }

                setAlphaStatus(new float[]{difX / 200, difY / 200});
            }
            else setAlphaStatus(new float[]{0, 0});
        } else setAlphaStatus(new float[]{0, 0});
        if (plZ + plL < getZ()) return false;
        return Collisions.ovalIntersect(plRectCoords, getCenter(), horRad, getCoeff());
    }

    @Override
    public boolean checkTopCol(float[] plRectCoords, float plZ) {
        return Collisions.ovalIntersect(plRectCoords, getCenter(), horRad, getCoeff());
    }

    @Override
    public void drawWall(Canvas c, Paint p, float plX, float plY) {

        //FILL
        p.setColor(Color.DKGRAY);
        p.setStyle(Paint.Style.FILL);
        p.setAlpha((int) (255 * (1 - getAlphaStatus()[0] * getAlphaStatus()[1])));

        c.drawRect(getViewX() + plX, (getViewY() + plY + verRad - getL() - getZ()), (getViewX() + plX + 2 * horRad),
                (getViewY() + plY + verRad - getZ()), p);

        c.drawArc(getViewX() + plX, getViewY() + plY - getZ() - 1, getViewX() + plX + 2 * horRad, getViewY() + plY + 2 * verRad - getZ(),
                0, 180, false, p);

        //OUTLINE
        p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(10);

        p.setAlpha((int)(255 * (1 - getAlphaStatus()[0] * getAlphaStatus()[1])));

        c.drawLine(getViewX() + plX, getViewY() + plY + verRad - getL() - getZ(), getViewX() + plX,
                getViewY() + plY + verRad - getZ(), p);
        c.drawLine(getViewX() + plX + 2 * horRad, getViewY() + plY + verRad - getL() - getZ(), getViewX() + plX + 2 * horRad,
                getViewY() + plY + verRad - getZ(), p);

        c.drawArc(getViewX() + plX, getViewY() + plY - getZ() - 1, getViewX() + plX + 2*horRad, getViewY() + plY + 2 * verRad - getZ(),
                0, 180, false, p);
    }

    @Override
    public void drawRoof(Canvas c, Paint p, float plX, float plY) {

        //FILL
        p.setColor(Color.GRAY);
        p.setStyle(Paint.Style.FILL);

        p.setAlpha((int) (255 * (1 - getAlphaStatus()[0] * getAlphaStatus()[1])));
        c.drawOval(getViewX() + plX, getViewY() + plY - getL() - getZ(), getViewX() + plX + horRad * 2,
                (getViewY() + plY - getL()) + verRad * 2 - getZ(), p);

        //OUTLINE
        p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(10);

        p.setAlpha((int)(255 * (1 - getAlphaStatus()[0] * getAlphaStatus()[1])));
        c.drawOval(getViewX() + plX, getViewY() + plY - getL() - getZ(),
                getViewX() + plX + horRad*2, (getViewY() + plY - getL()) + verRad*2 - getZ(), p);
    }
}
