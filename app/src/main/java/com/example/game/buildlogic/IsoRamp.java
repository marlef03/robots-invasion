package com.example.game.buildlogic;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.example.game.core.GameView;
import com.example.game.util.Collisions;

public class IsoRamp extends Primitive {
    private float leftSide,rightSide;
    private float leftSideX, leftSideY, rightSideX, rightSideY;

    private float constL;
    private int siding;
    private float coeffX, coeffY;

    public IsoRamp(float x, float y, float z, float l, float leftSide, float rightSide, int siding) {
        super(x, y, z, l, false);
        this.leftSide = leftSide;
        this.rightSide = rightSide;

        constL = l;
        this.siding = siding;

        leftSideX = leftSide * (float)Math.cos(Math.PI / 6);
        leftSideY = leftSide * (float)Math.sin(Math.PI / 6);
        rightSideX = rightSide * (float)Math.cos(Math.PI / 6);
        rightSideY = rightSide * (float)Math.sin(Math.PI / 6);

        switch (siding) {
            case 0: case 2:
                coeffX = l / (2 * leftSideX);
                break;
            case 1: case 3:
                coeffX = l / (2 * rightSideX);
        }
    }

    public Pointc[] getPoints() {
        return new Pointc[] {
                new Pointc(getX() + rightSideX, getY()),
                new Pointc(getX() + leftSideX + rightSideX, getY() + leftSideY),
                new Pointc(getX() + leftSideX, getY() + rightSideY + leftSideY),
                new Pointc(getX(), getY() + rightSideY)
        };
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

        return Collisions.polyRectIntersect(getPoints(), plRectCoords[0], plRectCoords[1], plRectCoords[2], plRectCoords[3], false);
    }

    public boolean checkLineCol(float[] plRectCoords, float plZ, float plL) {
        float dif, xC, xC2, coeffY2 = 0;
        switch (siding) {
            case 0:
                if (plRectCoords[3] > getY() && plRectCoords[3] < getY() + leftSideY + rightSideY) {
                    coeffY = (getY() + leftSideY + rightSideY - plRectCoords[3]) / (leftSideY + rightSideY);
                    coeffY2 = (getY() + leftSideY + rightSideY - plRectCoords[1]) / (leftSideY + rightSideY);
                }

                xC = (getX() - leftSideX) + coeffY * (leftSideX + rightSideX);
                xC2 = (getX() - leftSideX) + coeffY2 * (leftSideX + rightSideX);

                if (plRectCoords[2] > xC
                        && plRectCoords[2] < xC + 2 * leftSideX) {

                    dif = plRectCoords[2] - xC;
                    setL(dif * coeffX);
                } else if (plRectCoords[2] <= xC) setL(0);
                else if (plRectCoords[2] >= xC + 2 * leftSideX) setL(constL);

                return Collisions.lineLineIntersect(plRectCoords[0], plZ, plRectCoords[2], plZ, xC, getZ(),
                        xC + 2 * leftSideX, getZ() + constL)
                        || (l == constL && plZ <= z + l && plZ > z);
            case 1:
                if (plRectCoords[3] > getY() && plRectCoords[3] < getY() + leftSideY + rightSideY) {
                    coeffY = (getY() + leftSideY + rightSideY - plRectCoords[3]) / (leftSideY + rightSideY);
                    coeffY2 = (getY() + leftSideY + rightSideY - plRectCoords[1]) / (leftSideY + rightSideY);
                }

                xC = (getX() + leftSideX + 2 * rightSideX) - coeffY * (leftSideX + rightSideX);
                xC2 = (getX() + leftSideX + 2 * rightSideX) - coeffY2 * (leftSideX + rightSideX);

                if (plRectCoords[0] < xC
                        && plRectCoords[0] > xC - 2 * rightSideX) {

                    dif = xC - plRectCoords[0];
                    setL(dif * coeffX);
                } else if (plRectCoords[0] >= xC) setL(0);
                else if (plRectCoords[0] <= xC - 2 * rightSideX) setL(constL);

                return Collisions.lineLineIntersect(plRectCoords[0], plZ, plRectCoords[2], plZ, xC, getZ(),
                        xC - 2 * rightSideX, getZ() + constL)
                        || (l == constL && plZ <= z + l && plZ > z);
            case 2:
                if (plRectCoords[1] > getY() && plRectCoords[1] < getY() + leftSideY + rightSideY) {
                    coeffY = (plRectCoords[1] - getY()) / (leftSideY + rightSideY);
                    coeffY2 = (plRectCoords[3] - getY()) / (leftSideY + rightSideY);
                }

                xC = (getX() + rightSideX + 2 * leftSideX) - coeffY * (leftSideX + rightSideX);
                xC2 = (getX() + rightSideX + 2 * leftSideX) - coeffY2 * (leftSideX + rightSideX);

                if (plRectCoords[0] < xC
                        && plRectCoords[0] > xC - 2 * leftSideX) {

                    dif = xC - plRectCoords[0];
                    setL(dif * coeffX);
                } else if (plRectCoords[0] >= xC) setL(0);
                else if (l == constL && plZ <= z + l && plZ > z);

                return Collisions.lineLineIntersect(plRectCoords[0], plZ, plRectCoords[2], plZ, xC, getZ(),
                        xC - 2 * leftSideX, getZ() + constL)
                        || (l == constL && plZ + plL <= l && plZ > z);
            case 3:
                if (plRectCoords[1] > getY() && plRectCoords[1] < getY() + leftSideY + rightSideY) {
                    coeffY = (plRectCoords[1] - getY()) / (leftSideY + rightSideY);
                    coeffY2 = (plRectCoords[3] - getY()) / (leftSideY + rightSideY);
                }

                xC = (getX() - rightSideX) + coeffY * (leftSideX + rightSideX);
                xC2 = (getX() - rightSideX) + coeffY2 * (leftSideX + rightSideX);

                if (plRectCoords[2] > xC
                        && plRectCoords[2] < xC + 2 * rightSideX) {

                    dif = plRectCoords[2] - xC;
                    setL(dif * coeffX);
                } else if (plRectCoords[2] <= xC) setL(0);
                else if (plRectCoords[2] >= xC + 2 * rightSideX) setL(constL);

                return Collisions.lineLineIntersect(plRectCoords[0], plZ, plRectCoords[2], plZ, xC, getZ(),
                        xC + 2 * rightSideX, getZ() + constL)
                        || (l == constL && plZ <= z + l && plZ > z);
        }
        return false;
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

        c.drawLine((getPoints()[0].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[0].y + plY - getZ()) * GameView.HEIGHT_MULTIP,
                (getPoints()[1].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[1].y + plY - getZ()) * GameView.HEIGHT_MULTIP, p);

        c.drawLine((getPoints()[1].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[1].y + plY - getZ()) * GameView.HEIGHT_MULTIP,
                (getPoints()[2].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[2].y + plY - getZ()) * GameView.HEIGHT_MULTIP, p);

        c.drawLine((getPoints()[2].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[2].y + plY - getZ()) * GameView.HEIGHT_MULTIP,
                (getPoints()[3].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[3].y + plY - getZ()) * GameView.HEIGHT_MULTIP, p);

        c.drawLine((getPoints()[3].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[3].y + plY - getZ()) * GameView.HEIGHT_MULTIP,
                (getPoints()[0].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[0].y + plY - getZ()) * GameView.HEIGHT_MULTIP, p);

        switch (siding) {
            case 0:
                c.drawLine((getPoints()[2].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[2].y + plY - constL - getZ()) * GameView.HEIGHT_MULTIP,
                        (getPoints()[2].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[2].y + plY - getZ()) * GameView.HEIGHT_MULTIP, p);

                c.drawLine((getPoints()[1].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[1].y + plY - constL - getZ()) * GameView.HEIGHT_MULTIP,
                        (getPoints()[1].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[1].y + plY - getZ()) * GameView.HEIGHT_MULTIP, p);


                c.drawLine((getPoints()[0].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[0].y + plY - getZ()) * GameView.HEIGHT_MULTIP,
                        (getPoints()[1].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[1].y + plY - constL - getZ()) * GameView.HEIGHT_MULTIP, p);

                c.drawLine((getPoints()[3].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[3].y + plY - getZ()) * GameView.HEIGHT_MULTIP,
                        (getPoints()[2].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[2].y + plY - constL - getZ()) * GameView.HEIGHT_MULTIP, p);


                c.drawLine((getPoints()[1].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[1].y + plY - constL - getZ()) * GameView.HEIGHT_MULTIP,
                        (getPoints()[2].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[2].y + plY - constL - getZ()) * GameView.HEIGHT_MULTIP, p);
                break;

            case 1:
                c.drawLine((getPoints()[3].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[3].y + plY - constL - getZ()) * GameView.HEIGHT_MULTIP,
                        (getPoints()[3].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[3].y + plY - getZ()) * GameView.HEIGHT_MULTIP, p);

                c.drawLine((getPoints()[2].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[2].y + plY - constL - getZ()) * GameView.HEIGHT_MULTIP,
                        (getPoints()[2].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[2].y + plY - getZ()) * GameView.HEIGHT_MULTIP, p);


                c.drawLine((getPoints()[0].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[0].y + plY - getZ()) * GameView.HEIGHT_MULTIP,
                        (getPoints()[3].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[3].y + plY - constL - getZ()) * GameView.HEIGHT_MULTIP, p);

                c.drawLine((getPoints()[1].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[1].y + plY - getZ()) * GameView.HEIGHT_MULTIP,
                        (getPoints()[2].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[2].y + plY - constL - getZ()) * GameView.HEIGHT_MULTIP, p);


                c.drawLine((getPoints()[2].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[2].y + plY - constL - getZ()) * GameView.HEIGHT_MULTIP,
                        (getPoints()[3].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[3].y + plY - constL - getZ()) * GameView.HEIGHT_MULTIP, p);
                break;
            case 2:
                c.drawLine((getPoints()[0].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[0].y + plY - constL - getZ()) * GameView.HEIGHT_MULTIP,
                        (getPoints()[0].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[0].y + plY - getZ()) * GameView.HEIGHT_MULTIP, p);

                c.drawLine((getPoints()[3].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[3].y + plY - constL - getZ()) * GameView.HEIGHT_MULTIP,
                        (getPoints()[3].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[3].y + plY - getZ()) * GameView.HEIGHT_MULTIP, p);


                c.drawLine((getPoints()[1].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[1].y + plY - getZ()) * GameView.HEIGHT_MULTIP,
                        (getPoints()[0].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[0].y + plY - constL - getZ()) * GameView.HEIGHT_MULTIP, p);

                c.drawLine((getPoints()[2].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[2].y + plY - getZ()) * GameView.HEIGHT_MULTIP,
                        (getPoints()[3].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[3].y + plY - constL - getZ()) * GameView.HEIGHT_MULTIP, p);


                c.drawLine((getPoints()[3].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[3].y + plY - constL - getZ()) * GameView.HEIGHT_MULTIP,
                        (getPoints()[0].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[0].y + plY - constL - getZ()) * GameView.HEIGHT_MULTIP, p);
                break;
            case 3:
                c.drawLine((getPoints()[1].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[1].y + plY - constL - getZ()) * GameView.HEIGHT_MULTIP,
                        (getPoints()[1].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[1].y + plY - getZ()) * GameView.HEIGHT_MULTIP, p);

                c.drawLine((getPoints()[0].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[0].y + plY - constL - getZ()) * GameView.HEIGHT_MULTIP,
                        (getPoints()[0].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[0].y + plY - getZ()) * GameView.HEIGHT_MULTIP, p);


                c.drawLine((getPoints()[2].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[2].y + plY - getZ()) * GameView.HEIGHT_MULTIP,
                        (getPoints()[1].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[1].y + plY - constL - getZ()) * GameView.HEIGHT_MULTIP, p);

                c.drawLine((getPoints()[3].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[3].y + plY - getZ()) * GameView.HEIGHT_MULTIP,
                        (getPoints()[0].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[0].y + plY - constL - getZ()) * GameView.HEIGHT_MULTIP, p);


                c.drawLine((getPoints()[0].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[0].y + plY - constL - getZ()) * GameView.HEIGHT_MULTIP,
                        (getPoints()[1].x + plX) * GameView.HEIGHT_MULTIP, (getPoints()[1].y + plY - constL - getZ()) * GameView.HEIGHT_MULTIP, p);
        }
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


    }
}
