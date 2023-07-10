package com.example.game.species;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.game.core.GameView;
import com.example.game.util.ComplexImage;
import com.example.game.util.Vec;

public class Body {
    protected float health, maxHealth;
    protected float damage = 1;

    protected float z, l;
    protected float prevZ;
    protected boolean zCol;

    protected Vec accV, velV, posV;
    protected float jumpVel, jumpAcc = -0.005f;
    protected float floorLevel;
    protected boolean jumped;
    protected float bounceCoeff = 2.5f;
    protected float invMass;
    protected float invInertia;
    protected float elasticity;

    protected float friction = 0.001f, angleFriction = 0.002f;
    protected float velForce;

    protected double angleVel;
    protected double angle;

    protected Shape shape;
    protected ComplexImage imageControl;

    public Body(float z, float l, float mass, float elasticity, float maxHealth) {
        accV = new Vec();
        velV = new Vec();
        angleVel = 0;

        this.z = z;
        this.l = l;

        invMass = mass == 0 ? 0 : 1 / mass;
        this.elasticity = elasticity;

        this.maxHealth = maxHealth;
        health = maxHealth;

        prevZ = z;
    }

    public Body(float x, float y, float z, float l, float r, float mass, float elasticity, float maxHealth) {
        accV = new Vec();
        velV = new Vec();
        angleVel = 0;

        this.z = z;
        this.l = l;

        posV = new Vec(x, y);

        invMass = mass == 0 ? 0 : 1 / mass;
        this.elasticity = elasticity;

        shape = new Circle(x, y, r);

        this.maxHealth = maxHealth;
        health = maxHealth;

        prevZ = z;
    }

    public Body(float x1, float y1, float x2, float y2, float z, float l, int w, float mass, float elasticity, float maxHealth) {
        accV = new Vec();
        velV = new Vec();
        angleVel = 0;

        this.z = z;
        this.l = l;

        invMass = mass == 0 ? 0 : 1 / mass;
        this.elasticity = elasticity;

        shape = new Rectangle(x1, y1, x2, y2, w);

        float inertia = mass * (w * w + shape.getLength() * shape.getLength());
        invInertia = mass == 0 ? 0 : 1 / inertia;

        posV = shape.getPosV();

        this.maxHealth = maxHealth;
        health = maxHealth;

        prevZ = z;
    }

    public void setZCol(boolean zCol) {
        this.zCol = zCol;
    }

    public boolean getZCol() {
        return zCol;
    }

    public void setImageControl(Bitmap[][] images, float width, float height) {
        imageControl = new ComplexImage(images, width, height);
    }

    public ComplexImage getImageControl() {
        return imageControl;
    }

    public float[] getRawRectangle(float plX, float plY) {
        if (shape instanceof Circle)
            return new float[] {
                    (posV.x - shape.getR() + plX) * GameView.HEIGHT_MULTIP,
                    (posV.y - shape.getR() + plY - z - l) * GameView.HEIGHT_MULTIP,
                    (posV.x + shape.getR() + plX) * GameView.HEIGHT_MULTIP,
                    (posV.y + shape.getR() + plY - z) * GameView.HEIGHT_MULTIP
            };
        if (shape instanceof Rectangle) {
            return new float[] {
                    (posV.x - shape.getMaxLength() + plX) * GameView.HEIGHT_MULTIP,
                    (posV.y - shape.getMaxLength() + plY - z - l) * GameView.HEIGHT_MULTIP,
                    (posV.x + shape.getMaxLength() + plX) * GameView.HEIGHT_MULTIP,
                    (posV.y + shape.getMaxLength() + plY - z) * GameView.HEIGHT_MULTIP
            };
        }

        return null;
    }

    public Explosion getExplosion() {
        Explosion e = new Explosion(posV.x, posV.y, z - 300, 600, 200, 800, 1f);
        e.setDamage(damage);
        return e;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getDamage() {
        return damage;
    }

    public void changeHealth(float num) {
        health += num;

        if (health > maxHealth) health = maxHealth;
        if (health < 0) health = 0;
    }

    public float getHealth() {
        return health;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public boolean isFullHealth() {
        return health == maxHealth;
    }

    public boolean isDestroyed() {
        return health <= 0 && maxHealth != 0;
    }

    public void changeSpeed(float xDir, float yDir) {}

    public float getBounceCoeff() {
        return bounceCoeff;
    }

    public float getPrevZ() {
        return prevZ;
    }

    public void jump() {
        prevZ = z;

        if (jumped) {
            z += jumpVel;
            jumpVel += jumpAcc;
            if (z <= floorLevel) {
                z = floorLevel;

                changeHealth(Math.abs(jumpVel) > 2.4 ? -damage * Math.abs(jumpVel) : 0);

                if (floorLevel == 0) {
                    jumpVel = -jumpVel / bounceCoeff;
                    if (jumpVel < 0.1f) {
                        jumpVel = 0;
                        jumped = false;
                    }
                }
                else {
                    jumped = false;
                    jumpVel = 0;
                }
            }
        }
    }

    public void move() {
        float velVMag = velV.mag();
        if (velVMag - friction >= 0) velV = velV.unit().multy(velVMag - friction);
        else velV = velV.multy(0);

        angleVel *= (1 - angleFriction);

        posV.set(posV.add(velV).x, posV.add(velV).y);
        shape.setPosV(posV);

        if (shape instanceof Rectangle) {
            angle += angleVel;
            shape.getVertices(angle);
        }

        if (shape instanceof Circle) {
            if (velV.x != 0 && velV.y != 0) shape.setDirV(velV.unit());
        }

        if (velV.mag() < 0.00000001f) {
            velV.x = 0;
            velV.y = 0;
        }
    }

    public void draw(Canvas c, Paint p, float plX, float plY) {
        if (imageControl != null) imageControl.drawSpecifiedImages(c, p, posV.x - shape.getLength() / 2, posV.y - (float)((Rectangle) shape).getWidth() / 2 - z, plX, plY);
    }

    public void drawHealthBars(Canvas c, Paint p, float plX, float plY) {
        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.FILL);
        c.drawRect((posV.x + plX - 70) * GameView.HEIGHT_MULTIP, (posV.y + plY - z - l - 45) * GameView.HEIGHT_MULTIP,
                (posV.x + plX + 70) * GameView.HEIGHT_MULTIP, (posV.y + plY - z - l - 30) * GameView.HEIGHT_MULTIP, p);

        if (maxHealth != 0) {
            p.setColor(Color.rgb(255 - (int) (health / maxHealth * 255), (int) (health / maxHealth * 255), 0));
            c.drawRect((posV.x + plX - 70) * GameView.HEIGHT_MULTIP, (posV.y + plY - z - l - 45) * GameView.HEIGHT_MULTIP,
                    (posV.x + plX - 70 + 140 * health / (maxHealth != 0 ? maxHealth : 1)) * GameView.HEIGHT_MULTIP, (posV.y + plY - z - l - 30) * GameView.HEIGHT_MULTIP, p);
        }
    }

    public float getJumpVel() {
        return jumpVel;
    }

    public void setJumpVel(float jumpVel) {
        this.jumpVel = jumpVel;
    }

    public void setJumpAcc(float jumpAcc) {
        this.jumpAcc = jumpAcc;
    }

    public void setFloorLevel(float floorLevel) {
        this.floorLevel = floorLevel;
    }

    public boolean getJumped() {
        return jumped;
    }

    public void setJumped(boolean jumped) {
        this.jumped = jumped;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getL() {
        return l;
    }

    public void setL(float l) {
        this.l = l;
    }

    public Vec getVelV() {
        return velV;
    }

    public void setVelV(Vec velV) {
        this.velV = velV;
    }

    public Vec getPosV() {
        return posV;
    }

    public void setPosV(Vec posV) {
        this.posV.x = posV.x;
        this.posV.y = posV.y;
    }

    public float getInvMass() {
        return invMass;
    }

    public float getInvInertia() {
        return invInertia;
    }

    public float getElasticity() {
        return elasticity;
    }

    public void setFriction(float friction) {
        this.friction = friction;
    }

    public float getVelForce() {
        return velForce;
    }

    public double getAngleVel() {
        return angleVel;
    }

    public void addAngleVel(double angle) {
        angleVel += angle;
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }
}
