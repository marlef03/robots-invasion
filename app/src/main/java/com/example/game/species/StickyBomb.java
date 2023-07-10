package com.example.game.species;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.game.MainActivity;
import com.example.game.characters.pickups.ThrowableWeapon;
import com.example.game.core.GameView;
import com.example.game.util.Vec;

public class StickyBomb extends Body {
    private Body parent;
    private Vec distToParent;
    private ThrowableWeapon weapon;

    private boolean once;

    private Bitmap image;

    public StickyBomb(float x, float y, float z, float l, int r, float mass, float elasticity, ThrowableWeapon wep) {
        super(x, y, z, l, r, mass, elasticity, 1);
        weapon = wep;

        once = true;

        image = Bitmap.createScaledBitmap(MainActivity.stickybomb, r * 2, r * 2, false);
    }

    @Override
    public void move() {
        super.move();

        if (parent != null) {
            float x1 = (float)(distToParent.x * Math.cos(parent.getAngleVel()) - distToParent.y * Math.sin(parent.getAngleVel()));
            float y1 = (float)(distToParent.y * Math.cos(parent.getAngleVel()) + distToParent.x * Math.sin(parent.getAngleVel()));

            distToParent.set(x1, y1);

            posV = parent.posV.add(distToParent);

            z += parent.jumpVel;
        }
    }

    public void playSound(float dist) {
        if (once && parent != null) {
            MainActivity.playStickyBomb(dist);
            once = false;
        }
    }

    @Override
    public Explosion getExplosion() {
        Explosion e = new Explosion(posV.x, posV.y, z - 300, 600, 200, 800, 0.3f);
        e.setDamage(damage);
        return e;
    }

    public void delFromWeapon() {
        weapon.deleteSticky(this);
    }

    public Body getParent() {
        return parent;
    }

    public void setParent(Body parent) {
        this.parent = parent;

        distToParent = posV.subtr(parent.posV);


        invMass = 0;
        jumpAcc = 0;
        jumpVel = 0;
        jumped = false;
    }

    @Override
    public void draw(Canvas c, Paint p, float plX, float plY) {
        c.drawBitmap(image, (posV.x - shape.getR() + plX) * GameView.HEIGHT_MULTIP, (posV.y - shape.getR() + plY - z - l) * GameView.HEIGHT_MULTIP, p);
    }
}
