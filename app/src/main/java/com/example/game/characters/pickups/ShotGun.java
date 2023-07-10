package com.example.game.characters.pickups;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.game.MainActivity;
import com.example.game.core.GameView;
import com.example.game.species.Bullet;
import com.example.game.species.Rectangle;
import com.example.game.ui.JoyStick;
import com.example.game.util.Vec;

public class ShotGun extends Item {
    private float bulletVel, bulletH, bulletL;
    private int bulletR;
    private int lifeTime;
    private int coolDown, reloadCoolDown, shotCoolDown, countToCoolDown;

    private int ammo, curAmmo, ammoReload;
    private float damage;

    private float sweep;

    public ShotGun(int ammo, int curAmmo, int ammoReload) {
        super(new Rectangle(0, 0, 0, 120, 90), 20, 1, 50, 0);

        image = MainActivity.shotgun;

        bulletVel = 1.6f;
        bulletH   = 50;
        bulletL   = 20;
        bulletR   = 10;

        damage = 1;

        lifeTime = 500;
        coolDown = shotCoolDown = 900;
        reloadCoolDown = 1800;

        sweep = 0.2f;

        this.ammo = ammo;
        this.curAmmo = curAmmo;
        this.ammoReload = ammoReload;

        attackInput = new JoyStick(240 * GameView.HEIGHT_MULTIP, GameView.HEIGHT - 240 * GameView.HEIGHT_MULTIP, 200 * GameView.HEIGHT_MULTIP, 70 * GameView.HEIGHT_MULTIP);
        attackInput.setStickImage(Bitmap.createScaledBitmap(MainActivity.attackjoystickstick, (int)(140 * GameView.HEIGHT_MULTIP), (int)(140 * GameView.HEIGHT_MULTIP), false));
    }

    public int getAmmo() {
        return ammo;
    }

    public int getCurrentAmmo() {
        return curAmmo;
    }

    public int getAmmoReload() {
        return ammoReload;
    }

    public void countCoolDown() {
        if (countToCoolDown == coolDown) {
            countToCoolDown = 0;
            if (coolDown == reloadCoolDown) {
                coolDown = shotCoolDown;
                if (ammo - ammoReload >= 0) {
                    ammo -= ammoReload;
                    curAmmo = ammoReload;
                }
                else {
                    curAmmo = ammo;
                    ammo = 0;
                }
            }
        }
        if (countToCoolDown != 0) countToCoolDown++;
    }

    @Override
    public void nullCountToCoolDown() {
        countToCoolDown = 0;
        curAmmo += 5;
    }

    @Override
    public int getId() {
        return 1;
    }

    @Override
    public void addAmmo(int ammo) {
        this.ammo += ammo;
    }

    @Override
    public Bullet[] getNextBulletArray(Vec pos, float z, Vec dir) {
        if (countToCoolDown != 0 || curAmmo == 0) return null;
        countToCoolDown = 1;

        curAmmo -= 5;
        if (curAmmo <= 0) {
            curAmmo = 0;
            coolDown = reloadCoolDown;
        }

        Bullet[] bullets = new Bullet[5];

        for (int i = 0; i < 5; i++) {
            Vec tempDir = dir.add(dir.normal().multy(-sweep * 2 + sweep * i)).unit();
            Vec tempPos = pos.add(tempDir.multy(bulletR + 10));

            bullets[i] = new Bullet(tempPos.x, tempPos.y, z + bulletH, bulletL, bulletR, 10, 10, lifeTime);
            bullets[i].setVelV(new Vec(tempDir.x, tempDir.y).multy(bulletVel));
            bullets[i].setFriction(0);
            bullets[i].setJumpAcc(0);
            bullets[i].setDamage(damage);
            bullets[i].setBitmap();
        }

        return bullets;
    }

    @Override
    public Bullet[] getNextBulletArray(Vec pos, float z, Vec dir, Vec point, float targetZ) {
        if (countToCoolDown != 0 || curAmmo == 0) return null;
        countToCoolDown = 1;

        curAmmo -= 5;
        if (curAmmo <= 0) {
            curAmmo = 0;
            coolDown = reloadCoolDown;
        }

        Bullet[] bullets = new Bullet[5];

        float dist = point.subtr(pos).mag();

        for (int i = 0; i < 5; i++) {
            Vec tempDir = dir.add(dir.normal().multy(-sweep * 2 + sweep * i)).unit();
            Vec tempPos = pos.add(tempDir.multy(bulletR + 10));

            bullets[i] = new Bullet(tempPos.x, tempPos.y, z + bulletH, bulletL, bulletR, 10, 10, lifeTime);
            bullets[i].setVelV(new Vec(tempDir.x, tempDir.y).multy(bulletVel));
            bullets[i].setFriction(0);
            bullets[i].setJumpAcc(0);
            bullets[i].setJumpVel(dist != 0 ? bulletVel * (targetZ - z) / dist : 0);
            bullets[i].setJumped(true);
            bullets[i].setDamage(damage);
            bullets[i].setBitmap();
        }

        return bullets;
    }

    @Override
    public void draw(Canvas c, Paint p, float x1, float y1, float x2, float y2) {
        c.drawBitmap(Bitmap.createScaledBitmap(image, (int)(x2 - x1 - 20), (int)(y2 - y1 - 20), false), x1 + 10, y1 + 10, p);

        p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.FILL);
        p.setTextSize(40 * GameView.HEIGHT_MULTIP);
        p.setAntiAlias(true);
        p.setTypeface(MainActivity.font);

        c.drawText(String.valueOf(ammo), x2 - p.measureText(String.valueOf(ammo)) * GameView.HEIGHT_MULTIP, y2 - 15 * GameView.HEIGHT_MULTIP, p);
        c.drawText(String.valueOf(curAmmo), x2 - p.measureText(String.valueOf(curAmmo)) * GameView.HEIGHT_MULTIP, y2 - 50 * GameView.HEIGHT_MULTIP, p);

        p.setAntiAlias(false);
    }
}
