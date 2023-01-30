package com.example.game.buildlogic;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.example.game.core.GameView;
import com.example.game.ui.JoyStick;
import com.example.game.util.Collisions;

public class Parallelepiped extends Primitive {
    protected float w,h;

    //Moving
    protected float selfSpeedX, selfSpeedY;
    protected float accSpeedX, accSpeedY;
    protected boolean contained;
    protected boolean canMoveJsX, canMoveJsY;
    protected boolean speeded;
    protected float accelerationGround;
    protected float accelerationX, accelerationY;
    protected boolean aXStarted, aYStarted;
    protected int timeX, timeY, countX, countY;
    protected float prevSpeedX, prevSpeedY;
    protected boolean staticXY;

    private boolean harmonicMotion;
    private float harmCenterSpeedX, harmCenterSpeedY, harmCenterSpeedZ;
    private boolean pushing;


    protected float jumpSpeed;
    protected float floorLevel;
    protected boolean jumped;
    protected boolean staticZ;
    protected float accelerationZ;

    public Parallelepiped(float x, float y, float z, float w, float h, float l, boolean staticZ,
                          boolean staticXY, boolean harmonicMotion, float harmCenterSpeedX, float harmCenterSpeedY, float harmCenterSpeedZ,
                          float accelerationX, float accelerationY, float accelerationZ, float startSpeedX, float startSpeedY, float startSpeedZ, boolean pushing, boolean isTrigger) {
        super(x, y, z, l, isTrigger);
        this.w = w;
        this.h = h;

        accelerationGround = 0.8f;

        speeded = true;

        this.staticZ = staticZ;
        this.staticXY = staticXY;

        this.pushing = pushing;

        this.harmonicMotion = harmonicMotion;

        this.harmCenterSpeedX = harmCenterSpeedX;
        this.harmCenterSpeedY = harmCenterSpeedY;

        this.harmCenterSpeedZ = harmCenterSpeedZ;

        accSpeedX = startSpeedX;
        accSpeedY = startSpeedY;
        jumpSpeed = startSpeedZ;

        this.accelerationX = accelerationX;
        this.accelerationY = accelerationY;

        this.accelerationZ = accelerationZ;
    }

    public Parallelepiped(float x, float y, float z, float w, float h, float l, boolean staticZ,
                          boolean staticXY, boolean pushing, boolean isTrigger) {
        super(x, y, z, l, isTrigger);
        this.w = w;
        this.h = h;

        this.staticZ = staticZ;
        this.staticXY = staticXY;

        this.pushing = pushing;

        accelerationZ = -0.75f;

        canMoveJsY = canMoveJsX = true;
    }

    public float[] getRectCoords() {
        return new float[] {getX(),getY(),getX()+w,getY()+h};
    }
    public float[] getRectCoordsAbove() {
        return new float[] {getX(), getY() - (getZ()+getL()), getX()+w, getY() - (getZ()+getL())+h+getL()};
    }

    //Moving
    public boolean getJumped() {
        return jumped;
    }
    public void setJumped(boolean jumped) {
        this.jumped = jumped;
    }

    public float getFloorLevel() {
        return floorLevel;
    }
    public void setFloorLevel(float floorLevel) {
        this.floorLevel = floorLevel;
    }

    public float getJumpSpeed() {
        return jumpSpeed;
    }
    public void setJumpSpeed(float jumpSpeed) {
        this.jumpSpeed = jumpSpeed;
    }

    public boolean getStaticZ() {
        return staticZ;
    }

    public boolean getHarmonicMotion() {
        return harmonicMotion;
    }

    public float getAccelerationZ() {
        return accelerationZ;
    }

    public void jump() {
        if (jumped && !harmonicMotion) {
            z += jumpSpeed;
            jumpSpeed += accelerationZ;
            if (z <= floorLevel) {
                jumped = false;
                jumpSpeed = 0;
                z = floorLevel;
            }
        }
        if (harmonicMotion) {
            if (!staticZ) {
                z += jumpSpeed;
                jumpSpeed += accelerationZ;
                if (Math.abs(jumpSpeed) >= harmCenterSpeedZ) {
                    jumpSpeed = jumpSpeed > 0 ? harmCenterSpeedZ : -harmCenterSpeedZ;
                    accelerationZ = -accelerationZ;
                }
            }
        }
    }

    public boolean getPushing() {
        return pushing;
    }

    public boolean getSpeeded() {
        return speeded;
    }
    public void setSpeeded(boolean speeded) {
        this.speeded = speeded;
    }

    public float getAccSpeedX() {
        return accSpeedX;
    }
    public void setAccSpeedX(float speed) {
        accSpeedX = speed;
    }

    public float getAccSpeedY() {
        return accSpeedY;
    }
    public void setAccSpeedY(float speed) {
        accSpeedY = speed;
    }

    public float getSpeedX() {
        return accSpeedX + selfSpeedX;
    }

    public float getSpeedY() {
        return accSpeedY + selfSpeedY;
    }

    public void setNewSpeedX(float newSp) {
        accelerationX = -newSp / 30;
        accSpeedX = newSp;
        speeded = true;
        aXStarted = true;
    }

    public void setNewSpeedX(float newSp, float a) {
        accelerationX = a;
        accSpeedX = newSp;
        speeded = true;
        aXStarted = true;
    }

    public void setNewSpeedY(float newSp) {
        accelerationY = -newSp / 30;
        accSpeedY = newSp;
        speeded = true;
        aYStarted = true;
    }

    public void setNewSpeedY(float newSp, float a) {
        accelerationY = a;
        accSpeedY = newSp;
        speeded = true;
        aYStarted = true;
    }

    public boolean getCanMoveJsX() {
        return canMoveJsX;
    }
    public void setCanMoveJsX(boolean canMoveJsX) {
        this.canMoveJsX = canMoveJsX;
    }

    public boolean getCanMoveJsY() {
        return canMoveJsY;
    }
    public void setCanMoveJsY(boolean canMoveJsY) {
        this.canMoveJsY = canMoveJsY;
    }

    public boolean getStaticXY() {
        return staticXY;
    }

    public void swapXDirection() {
        accSpeedX = -accSpeedX;
        selfSpeedX = -selfSpeedX;
    }
    public void swapYDirection() {
        accSpeedY = -accSpeedY;
        selfSpeedY = -selfSpeedY;
    }

    public void moveX() {
        x += accSpeedX + selfSpeedX;
    }
    public void moveX(float dir) {
        x += dir;
    }

    public void moveY() {
        y += accSpeedY + selfSpeedY;
    }
    public void moveY(float dir) {
        y += dir;
    }

    public void setAccelerationX(float acceleration) {
        this.accelerationX = acceleration;
    }
    public float getAccelerationX() {
        return accelerationX;
    }

    public void setAccelerationY(float acceleration) {
        this.accelerationY = acceleration;
    }
    public float getAccelerationY() {
        return accelerationY;
    }

    public void setContained(boolean a) {
        contained = a;
    }
    public boolean getContained() {
        return contained;
    }

    public void speedUpdate() {
        if (!harmonicMotion) {
            if (aXStarted) {
                accSpeedX += accelerationX;

                if (accelerationX < 0) {
                    if (accSpeedX <= 0) {
                        accelerationX = 0;
                        accSpeedX = 0;
                        aXStarted = false;
                        speeded = false;
                    }
                } else if (accelerationX > 0) {
                    if (accSpeedX >= 0) {
                        accelerationX = 0;
                        accSpeedX = 0;
                        aXStarted = false;
                        speeded = false;
                    }
                }
            }
            if (aYStarted) {
                accSpeedY += accelerationY;

                if (accelerationY < 0) {
                    if (accSpeedY <= 0) {
                        accelerationY = 0;
                        accSpeedY = 0;
                        aYStarted = false;
                        speeded = false;
                    }
                } else if (accelerationY > 0) {
                    if (accSpeedY >= 0) {
                        accelerationY = 0;
                        accSpeedY = 0;
                        aYStarted = false;
                        speeded = false;
                    }
                }
            }
        }
        else {
            if (!staticXY) {
                accSpeedX += accelerationX;
                accSpeedY += accelerationY;
                if (Math.abs(accSpeedX) >= harmCenterSpeedX) {
                    accSpeedX = accSpeedX > 0 ? harmCenterSpeedX : -harmCenterSpeedX;
                    accelerationX = -accelerationX;
                }
                if (Math.abs(accSpeedY) >= harmCenterSpeedY) {
                    accSpeedY = accSpeedY > 0 ? harmCenterSpeedY : -harmCenterSpeedY;
                    accelerationY = -accelerationY;
                }
            }
        }
    }

    @Override
    public boolean checkY(float plX, float plY) {
        return plY >= getY()+h;
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

        if (plZ + plL <= getZ()) return false;
        return Collisions.rectIntersect(plRectCoords, getRectCoords());
    }


    @Override
    public boolean checkTopCol(float[] plRectCoords, float plZ) {
        return Collisions.rectIntersect(plRectCoords, getRectCoords());
    }

    @Override
    public void drawWall(Canvas c, Paint p, float plX, float plY) {
        //FILL
        if (!getTrigger()) {
            p.setColor(Color.DKGRAY);
            p.setStyle(Paint.Style.FILL);
            p.setAlpha((int) (255 * (1 - getAlphaStatus()[0] * getAlphaStatus()[1])));

            c.drawRect((x + plX) * GameView.HEIGHT_MULTIP, (y + plY - l + h - z) * GameView.HEIGHT_MULTIP,
                    (x + plX + w) * GameView.HEIGHT_MULTIP, (y + plY + h - z) * GameView.HEIGHT_MULTIP, p);
        }

        //OUTLINE
        p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(10);

        p.setAlpha((int)(255 * (1 - getAlphaStatus()[0] * getAlphaStatus()[1])));
        c.drawRect((x + plX) * GameView.HEIGHT_MULTIP, (y + plY - l + h - z) * GameView.HEIGHT_MULTIP,
                (x + plX + w) * GameView.HEIGHT_MULTIP, (y + plY + h - z) * GameView.HEIGHT_MULTIP, p);
    }

    @Override
    public void drawRoof(Canvas c, Paint p, float plX, float plY) {
        //FILL
        if (!getTrigger()) {
            p.setColor(Color.GRAY);
            p.setStyle(Paint.Style.FILL);

            p.setAlpha((int) (255 * (1 - getAlphaStatus()[0] * getAlphaStatus()[1])));
            c.drawRect((x + plX) * GameView.HEIGHT_MULTIP, (y + plY - l - z) * GameView.HEIGHT_MULTIP,
                    (x + plX + w) * GameView.HEIGHT_MULTIP, (y + plY - l + h - z) * GameView.HEIGHT_MULTIP, p);
        }

        //OUTLINE
        p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(10);

        p.setAlpha((int)(255 * (1 - getAlphaStatus()[0] * getAlphaStatus()[1])));
        c.drawRect((x + plX) * GameView.HEIGHT_MULTIP, (y + plY - l - z) * GameView.HEIGHT_MULTIP,
                (x + plX + w) * GameView.HEIGHT_MULTIP, (y + plY - l + h - z) * GameView.HEIGHT_MULTIP, p);
    }
}
