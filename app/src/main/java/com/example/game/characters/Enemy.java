package com.example.game.characters;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import com.example.game.MainActivity;
import com.example.game.characters.pickups.Ammo;
import com.example.game.characters.pickups.Item;
import com.example.game.characters.pickups.Melee;
import com.example.game.characters.pickups.Money;
import com.example.game.characters.pickups.Pickup;
import com.example.game.characters.pickups.ShotGun;
import com.example.game.characters.pickups.ThrowableWeapon;
import com.example.game.core.GameView;
import com.example.game.species.Body;
import com.example.game.util.ImageManager;
import com.example.game.util.Vec;

public class Enemy extends Body {
    private Item weapon;
    private Vec curDir;
    private Vec shootDir;
    private Vec targetPoint, shootPoint;

    private int stepStream;

    private float visibilityLength;

    private boolean movable, follower, flyable, jumper;
    private float flyableAcc = 0.001f, maxJumpVel = 1f, jumperVel, jumperVelConst = 2f, jumperDamage = 1f;

    private int coolDown, countToCoolDown;

    private ImageManager im;

    public Enemy(float x, float y, float z, float l, float rad, float mass, float elasticity, float velForce, float maxHealth, int coolDown, float visibilityLength,
                 boolean movable, boolean follower, boolean jumper, boolean flyable, Item weapon) {
        super(x, y, z, l, rad, mass, elasticity, maxHealth);

        this.velForce = velForce;

        this.weapon = weapon;
        this.coolDown = coolDown;

        curDir = new Vec(1, 0);

        this.visibilityLength = visibilityLength;

        this.movable = movable;
        this.follower = follower;
        this.jumper = jumper;
        this.flyable = flyable;

        stepStream = -1;

        im = new ImageManager();
        im.setWalkFrames(MainActivity.robotWalkFrames);
    }

    public float getVisibilityLength() {
        return visibilityLength;
    }

    public int getCoolDown() {
        return coolDown;
    }

    public boolean getMovable() {
        return movable;
    }

    public boolean getFollower() {
        return follower;
    }

    public boolean getFlyable() {
        return flyable;
    }

    public boolean getJumper() {
        return jumper;
    }

    public float getJumperDamage() {
        return jumperDamage;
    }

    public void setJumperAsVel() {
        jumpVel = jumperVel;
    }

    public Item getWeapon() {
        return weapon;
    }

    public void setNewTargetPoint(Vec target) {
        targetPoint = new Vec((float) Math.random() * (1000) + target.x - 500,
                (float) Math.random() * (1000) + target.y - 500);
    }

    @Override
    public void jump() {
        prevZ = z;

        if (jumped) {
            z += jumpVel;
            jumpVel += jumpAcc;
            if (z <= floorLevel) {
                z = floorLevel;

                changeHealth(Math.abs(jumpVel) > 2.4 ? -damage * Math.abs(jumpVel) : 0);

                if (!flyable && !jumper) {
                    if (floorLevel == 0) {
                        jumpVel = -jumpVel / bounceCoeff;
                        if (jumpVel < 0.1f) {
                            jumpVel = 0;
                            jumped = false;
                        }
                    } else {
                        jumped = false;
                        jumpVel = 0;
                    }
                }
                else if (flyable) jumpVel = -jumpVel;
                else {
                    jumpVel = jumperVel;
                }
            }
        }
    }

    public void setDirection(Vec target, float targetCenterZ) {
        shootPoint = target;

        if (target.subtr(posV).mag() > visibilityLength) {
            if (stepStream != -1) {
                MainActivity.stopRoboSteps(stepStream);
                stepStream = -1;
            }
            jumperVel = 0;
            return;
        }

        jumperVel = jumperVelConst;

        shootDir = target.subtr(posV).unit();

        curDir = shootDir;

        if (movable) {
            float dist = target.subtr(posV).mag();

            if (stepStream == -1) {
                stepStream = MainActivity.playRoboSteps(dist);
            }
            else {
                MainActivity.changeRoboSteps(stepStream, dist);
            }

            if (velV.mag() <= velForce) velV = curDir.multy(velForce);
            else velV = velV.subtr(velV.unit().multy(velForce));
        }
    }

    public boolean count() {
        countToCoolDown++;
        if (countToCoolDown == coolDown) {
            countToCoolDown = 0;
            return true;
        }
        return false;
    }

    public Body dropMoney() {
        return new Pickup(posV.x, posV.y, z, new Money(200));
    }

    public Body drop() {
        if (stepStream != -1) {
            MainActivity.stopRoboSteps(stepStream);
            stepStream = -1;
        }

        double chance = Math.random();
        if (chance >= 0.8) return new Pickup(posV.x, posV.y, z, weapon);
        return null;
    }

    public Body[] shoot(float targetCenterZ) {
        if (shootPoint.subtr(posV).mag() > visibilityLength) return new Body[1];

        if (weapon instanceof ShotGun) return weapon.getNextBulletArray(posV.add(shootDir.multy(shape.getMaxLength())), z, shootDir, shootPoint, targetCenterZ);
        else if (weapon instanceof Melee) return new Body[] {weapon.getNextBullet(posV.add(shootDir.multy(shape.getMaxLength())), z, shootDir)};
        else if (weapon instanceof ThrowableWeapon) return new Body[] {weapon.getNextBullet(posV, z + l, shootDir, shootPoint, targetCenterZ)};
        else return new Body[] {weapon.getNextBullet(posV.add(shootDir.multy(shape.getMaxLength())), z, shootDir, shootPoint, targetCenterZ)};
    }

    public void nullCountToCoolDown() {
        weapon.nullCountToCoolDown();

        countToCoolDown = 1;
    }

    @Override
    public void draw(Canvas c, Paint p, float plX, float plY) {
        Bitmap img = Bitmap.createScaledBitmap(im.getCurrentStatusFrame(curDir, velV.mag() != 0 && movable, null),
                (int)((l + shape.getR() * 2) * GameView.HEIGHT_MULTIP), (int)((l + shape.getR() * 2) * GameView.HEIGHT_MULTIP), false);

        c.drawBitmap(img, (posV.x + plX - (shape.getR() + l / 2)) * GameView.HEIGHT_MULTIP, (posV.y + plY - shape.getR() - z - l) * GameView.HEIGHT_MULTIP, p);

        drawHealthBars(c, p, plX, plY);
    }
}
