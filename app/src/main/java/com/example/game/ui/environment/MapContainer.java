package com.example.game.ui.environment;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.example.game.buildlogic.*;
import com.example.game.characters.Player;
import com.example.game.core.GameView;
import com.example.game.ui.JoyStick;
import com.example.game.util.Collisions;

import java.util.ArrayList;
import java.util.List;

import kotlin.reflect.KParameter;

public class MapContainer {
    private float x, y, w, h;
    private float viewX, viewY;
    private List<Primitive> entities;
    private List<Parallelepiped> movings;

    private float plViewX, plViewY;

    public Camera cam;

    private float[] pointOfView;

    public float[] getRectCoords() {
        return new float[] {x,y,x+w,y+h};
    }

    public MapContainer(float x, float y, float w, float h, float plViewX, float plViewY, boolean freeCam) {
        this.x = this.viewX = x;
        this.y = this.viewY = y;
        this.w = w;
        this.h = h;
        this.plViewX = plViewX;
        this.plViewY = plViewY;

        pointOfView = new float[2];
        cam = new Camera();
        cam.setFreeCam(freeCam);

        entities = new ArrayList<>();

//        entities.add(new Parallelepiped(700, 200, 0, 200, 200, 100, false,
//                false, true, 10, 0, 0, 0.25f,
//                0, 0, 0, 0, 0, false, false));

        entities.add(new Parallelepiped(1000, 200, 200, 200, 200, 200, false, false, true, false));
        entities.add(new Parallelepiped(1000, 200, 0, 500, 500, 200, false, false, true, false));
        //((Parallelepiped) entities.get(0)).setJumpSpeed(100);
        //((Parallelepiped) entities.get(0)).setJumped(true);

        movings = new ArrayList<>();
    }

    public void allMoving(Player pl) {
        movings.add((Parallelepiped)pl);

        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i) instanceof Parallelepiped)
                if (!((Parallelepiped) entities.get(i)).getStaticZ() || ((Parallelepiped) entities.get(i)).getSpeeded()) movings.add((Parallelepiped) entities.get(i));
        }

        entities.add(0, (Parallelepiped)pl);

        for (int ent = 0; ent < movings.size(); ent++) {
            boolean cantMoveX = false, cantMoveY = false, notMoveX = false, notMoveY = false, onJump = false;

            movings.get(ent).speedUpdate();


            movings.get(ent).moveX();

            if (leftSpace(movings.get(ent).getRectCoords())) {
                cantMoveX = true;
            }

            movings.get(ent).swapXDirection();

            movings.get(ent).moveX();

            movings.get(ent).swapXDirection();


            movings.get(ent).moveY();

            if (leftSpace(movings.get(ent).getRectCoords())) {
                cantMoveY = true;
            }

            movings.get(ent).swapYDirection();

            movings.get(ent).moveY();

            movings.get(ent).swapYDirection();


            for (int i = 0; i < entities.size(); i++) {
                if (movings.get(ent) == entities.get(i)) continue;

                float newSpeedX = movings.get(ent).getAccSpeedX(), newSpeedY = movings.get(ent).getAccSpeedY();
                float oldSpeedX = newSpeedX, oldSpeedY = newSpeedY;

                if (!cantMoveX) {
                    movings.get(ent).moveX();

                    if (entities.get(i) instanceof Parallelepiped) {
                        if (((Parallelepiped) entities.get(i)).checkWallCol(movings.get(ent).getRectCoords(), movings.get(ent).getZ(), movings.get(ent).getL())) {
                            if (entities.get(i).getTrigger()) entities.get(i).setActive(true);
                            else {
                                if (movings.get(ent).getPushing()) notMoveX = true;
                                entities.get(i).setActive(false);

                                if (!((Parallelepiped) entities.get(i)).getStaticXY()) {
                                    if (((Parallelepiped) entities.get(i)).getPushing()) {
                                        if (!movings.get(ent).getPushing()) {
                                            ((Parallelepiped) entities.get(i)).setNewSpeedX(movings.get(ent).getAccSpeedX());
                                            ((Parallelepiped) entities.get(i)).moveX(((Parallelepiped) entities.get(i)).getAccSpeedX());
                                        }
                                        else {
                                            if (Math.abs(movings.get(ent).getSpeedX()) > Math.abs(((Parallelepiped) entities.get(i)).getSpeedX())) {
                                                ((Parallelepiped) entities.get(i)).setNewSpeedX(movings.get(ent).getSpeedX());
                                            }
//                                            else if (Math.abs(movings.get(ent).getSpeedX()) < Math.abs(((Parallelepiped) entities.get(i)).getSpeedX())) {
//                                                newSpeedX = ((Parallelepiped) entities.get(i)).getSpeedX();
//                                            }
                                            else {
                                                newSpeedX = -((Parallelepiped) entities.get(i)).getSpeedX() / 10;
                                                ((Parallelepiped) entities.get(i)).setNewSpeedX(-((Parallelepiped) entities.get(i)).getSpeedX() / 10);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (entities.get(i) instanceof Cylinder) {
                        if (((Cylinder) entities.get(i)).checkWallCol(movings.get(ent).getRectCoords(), movings.get(ent).getZ(), movings.get(ent).getL())) {
                            if (entities.get(i).getTrigger()) entities.get(i).setActive(true);
                            else {
                                notMoveX = true;
                                entities.get(i).setActive(false);
                            }
                        }
                    }
                    if (entities.get(i) instanceof Prism4) {
                        if (((Prism4) entities.get(i)).checkWallCol(movings.get(ent).getRectCoords(), movings.get(ent).getZ(), movings.get(ent).getL())) {
                            notMoveX = true;
                        }
                    }
                    if (entities.get(i) instanceof PrismTriangle) {
                        if (((PrismTriangle) entities.get(i)).checkWallCol(movings.get(ent).getRectCoords(), movings.get(ent).getZ(), movings.get(ent).getL())) {
                            notMoveX = true;
                        }
                    }
                    if (entities.get(i) instanceof Ramp) {
                        if (((Ramp) entities.get(i)).checkWallCol(movings.get(ent).getRectCoords(), movings.get(ent).getZ(), movings.get(ent).getL())) {
                            notMoveX = true;
                        }
                    }
                    if (entities.get(i) instanceof IsoRamp) {
                        if (((IsoRamp) entities.get(i)).checkWallCol(movings.get(ent).getRectCoords(), movings.get(ent).getZ(), movings.get(ent).getL())) {
                            notMoveX = true;
                        }
                    }

                    movings.get(ent).swapXDirection();

                    movings.get(ent).moveX();

                    movings.get(ent).swapXDirection();

                    if (oldSpeedX != newSpeedX) movings.get(ent).setNewSpeedX(newSpeedX);
                }
                if (!cantMoveY) {
                    movings.get(ent).moveY();

                    if (entities.get(i) instanceof Parallelepiped) {
                        if (((Parallelepiped) entities.get(i)).checkWallCol(movings.get(ent).getRectCoords(), movings.get(ent).getZ(), movings.get(ent).getL())) {
                            if (entities.get(i).getTrigger()) entities.get(i).setActive(true);
                            else {
                                if (movings.get(ent).getPushing()) notMoveY = true;
                                entities.get(i).setActive(false);

                                if (!((Parallelepiped) entities.get(i)).getStaticXY()) {
                                    if (((Parallelepiped) entities.get(i)).getPushing()) {
                                        if (!((Parallelepiped) movings.get(ent)).getPushing()) {
                                            ((Parallelepiped) entities.get(i)).setNewSpeedY(movings.get(ent).getAccSpeedY());
                                            ((Parallelepiped) entities.get(i)).moveY(((Parallelepiped) entities.get(i)).getAccSpeedY());
                                        }
                                        else {
                                            if (Math.abs(movings.get(ent).getSpeedY()) > Math.abs(((Parallelepiped) entities.get(i)).getSpeedY())) {
                                                ((Parallelepiped) entities.get(i)).setNewSpeedY(movings.get(ent).getSpeedY());
                                            }
//                                            else if (Math.abs(movings.get(ent).getSpeedY()) < Math.abs(((Parallelepiped) entities.get(i)).getSpeedY())) {
//                                                newSpeedY = ((Parallelepiped) entities.get(i)).getSpeedY();
//                                            }
                                            else {
                                                newSpeedY = -((Parallelepiped) entities.get(i)).getSpeedY() / 10;
                                                ((Parallelepiped) entities.get(i)).setNewSpeedY(-((Parallelepiped) entities.get(i)).getSpeedY() / 10);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (entities.get(i) instanceof Cylinder) {
                        if (((Cylinder) entities.get(i)).checkWallCol(movings.get(ent).getRectCoords(), movings.get(ent).getZ(), movings.get(ent).getL())) {
                            if (entities.get(i).getTrigger()) entities.get(i).setActive(true);
                            else {
                                notMoveY = true;
                                entities.get(i).setActive(false);
                            }
                        }
                    }
                    if (entities.get(i) instanceof Prism4) {
                        if (((Prism4) entities.get(i)).checkWallCol(movings.get(ent).getRectCoords(), movings.get(ent).getZ(), movings.get(ent).getL())) {
                            notMoveY = true;
                        }
                    }
                    if (entities.get(i) instanceof PrismTriangle) {
                        if (((PrismTriangle) entities.get(i)).checkWallCol(movings.get(ent).getRectCoords(), movings.get(ent).getZ(), movings.get(ent).getL())) {
                            notMoveY = true;
                        }
                    }
                    if (entities.get(i) instanceof Ramp) {
                        if (((Ramp) entities.get(i)).checkWallCol(movings.get(ent).getRectCoords(), movings.get(ent).getZ(), movings.get(ent).getL())) {
                            notMoveY = true;
                        }
                    }
                    if (entities.get(i) instanceof IsoRamp) {
                        if (((IsoRamp) entities.get(i)).checkWallCol(movings.get(ent).getRectCoords(), movings.get(ent).getZ(), movings.get(ent).getL())) {
                            notMoveY = true;
                        }
                    }

                    movings.get(ent).swapYDirection();

                    movings.get(ent).moveY();

                    movings.get(ent).swapYDirection();

                    if (oldSpeedY != newSpeedY) movings.get(ent).setNewSpeedY(newSpeedY);
                }
                if (checkZCol(movings.get(ent), entities.get(i))) onJump = true;
            }
            if (onJump) movings.get(ent).setJumped(false);

            if (!notMoveX && !cantMoveX && !notMoveY && !cantMoveY) {
                movings.get(ent).moveX();
                movings.get(ent).moveY();
            } else if (!notMoveX && !cantMoveX) movings.get(ent).moveX();
            else if (!notMoveY && !cantMoveY) movings.get(ent).moveY();

            movings.get(ent).jump();
        }
        entities.remove(pl);
        movings.clear();
    }

    private boolean checkZCol(Parallelepiped pl, Primitive p) {
        if (pl.getStaticZ() || pl.getHarmonicMotion()) return false;

        boolean colTop = false;
        boolean colLine = false;
        if (p instanceof PrismTriangle) {
            return false;
        }
        if (p instanceof Parallelepiped) {
            colTop = ((Parallelepiped) p).checkTopCol(pl.getRectCoords(), pl.getZ());
        }
        if (p instanceof Cylinder) {
            colTop = ((Cylinder) p).checkTopCol(pl.getRectCoords(), pl.getZ());
        }
        if (p instanceof Prism4) {
            colTop = ((Prism4) p).checkTopCol(pl.getRectCoords(), pl.getZ());
        }
        if (p instanceof Ramp) {
            colTop = ((Ramp) p).checkTopCol(pl.getRectCoords(), pl.getZ());
            colLine = ((Ramp) p).checkLineCol(pl.getRectCoords(), pl.getZ());
        }
        if (p instanceof IsoRamp) {
            colTop = ((IsoRamp) p).checkTopCol(pl.getRectCoords(), pl.getZ());
            colLine = ((IsoRamp) p).checkLineCol(pl.getRectCoords(), pl.getZ(), pl.getL());
        }
        boolean colUnder = p.checkUnderCol(pl.getZ(), pl.getL());

        if (colTop) {
            if (pl.getJumpSpeed() < 0) {
                if (p.getL() + p.getZ() >= pl.getZ() && (pl.getZ() - (pl.getJumpSpeed() - pl.getAccelerationZ()) > p.getZ())) {
                    if (p instanceof Ramp || p instanceof IsoRamp) {
                        if (colLine) {
                            pl.setZ(p.getL() + p.getZ());
                            pl.setFloorLevel(p.getL() + p.getZ());
                            pl.setJumpSpeed(0);
                        }
                    } else {
                        pl.setZ(p.getL() + p.getZ());
                        pl.setFloorLevel(p.getL() + p.getZ());
                        pl.setJumpSpeed(0);
                    }
                }
            }
            if (pl.getJumpSpeed() == 0) {
                if (p instanceof Ramp || p instanceof IsoRamp) {
                    if (colLine) pl.setZ(p.getL() + p.getZ());
                }
                if (p.getZ() + p.getL() == pl.getZ()) {
                    if (p instanceof Parallelepiped) {
                        if (pl.getAccSpeedX() == 0) pl.setNewSpeedX((((Parallelepiped) p).getAccSpeedX() + ((Parallelepiped) p).getAccelerationX()));
                        if (pl.getAccSpeedY() == 0) pl.setNewSpeedY((((Parallelepiped) p).getAccSpeedY() + ((Parallelepiped) p).getAccelerationY()));

                        pl.setJumpSpeed(((Parallelepiped) p).getJumpSpeed());
                        pl.setJumped(true);
                        pl.jump();
                        pl.setJumped(false);
                        pl.setJumpSpeed(0);
                    }

                    return true;
                }
            }
        }
        if (pl.getZ() != 0) {
            pl.setJumped(true);
            pl.setFloorLevel(0);
        }
        if (pl.getJumpSpeed() > 0) {
            if (colTop && colUnder) {
                if (p instanceof Parallelepiped) {
                    float plSp = pl.getJumpSpeed();
                    pl.setJumpSpeed(-pl.getJumpSpeed() + ((Parallelepiped) p).getJumpSpeed() < 0 ? ((Parallelepiped) p).getJumpSpeed() + ((Parallelepiped) p).getAccelerationZ() : 0);
                    if (!pl.getStaticZ() && !pl.getHarmonicMotion()) {
                        ((Parallelepiped) p).setJumpSpeed(plSp);
                        ((Parallelepiped) p).setJumped(true);
                    }
                }
                else pl.setJumpSpeed(-pl.getJumpSpeed());
                pl.jump();
                pl.setJumpSpeed(0);
            }
        }
        return false;
    }

    private boolean leftSpace(float[] plRectCoords) {
        return Collisions.rectDisIntersect(plRectCoords, getRectCoords());
    }

    public void drawUnder(Canvas c, Paint p, float plZ, float jumpSpeed, float plX, float plY) {
        if (!cam.getFreeCam()) {
            pointOfView[0] = plViewX - plX;
            pointOfView[1] = plViewY - plY + plZ;
        } else {
            pointOfView[0] = -cam.getPoint()[0];
            pointOfView[1] = -cam.getPoint()[1];
        }

        p.setColor(Color.CYAN);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(15);

        c.drawRect((viewX + pointOfView[0]) * GameView.HEIGHT_MULTIP, (viewY + pointOfView[1]) * GameView.HEIGHT_MULTIP,
                (viewX + pointOfView[0] + w) * GameView.HEIGHT_MULTIP, (viewY + pointOfView[1] + h) * GameView.HEIGHT_MULTIP, p);

        for (int i=0;i<entities.size();i++) {
            if (entities.get(i).checkY(plX, plY) || plZ + Math.abs(jumpSpeed) >= entities.get(i).getZ() + entities.get(i).getL()) {
                entities.get(i).drawWall(c, p, pointOfView[0], pointOfView[1]);
            }

            if (entities.get(i).getZ() + entities.get(i).getL() <= plZ + Math.abs(jumpSpeed) || entities.get(i).checkY(plX, plY)) {
                entities.get(i).drawRoof(c, p, pointOfView[0], pointOfView[1]);
            }
        }
    }

    public void drawAbove(Canvas c, Paint p, float plZ, float jumpSpeed, float plX, float plY) {
        for (int i=0;i<entities.size();i++) {
            if (!entities.get(i).checkY(plX, plY) && plZ + Math.abs(jumpSpeed) < entities.get(i).getZ() + entities.get(i).getL()) {
                entities.get(i).drawWall(c, p, pointOfView[0], pointOfView[1]);
            }

            if (entities.get(i).getZ() + entities.get(i).getL() > plZ + Math.abs(jumpSpeed) && !entities.get(i).checkY(plX, plY)) {
                entities.get(i).drawRoof(c, p, pointOfView[0], pointOfView[1]);
            }
        }
    }
}
