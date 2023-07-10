package com.example.game.characters.pickups;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.game.MainActivity;
import com.example.game.core.GameView;
import com.example.game.species.Body;
import com.example.game.species.Bullet;
import com.example.game.species.Circle;
import com.example.game.species.Rectangle;
import com.example.game.ui.JoyStick;
import com.example.game.util.Vec;

public class Pistol extends Item {
    private float bulletVel, bulletH, bulletL;
    private int bulletR;
    private int lifeTime;
    private int coolDown, reloadCoolDown, shotCoolDown, countToCoolDown;

    private int ammo, curAmmo, ammoReload;
    private float damage;

    public Pistol(int ammo, int curAmmo, int ammoReload) {
        super(new Rectangle(0, 0, 0, 100, 100), 10, 1, 20, 0);

        image = MainActivity.pistol;

        bulletVel = 2;
        bulletH   = 60;
        bulletL   = 20;
        bulletR   = 20;

        damage = 1;

        lifeTime = 600;
        coolDown = shotCoolDown = 300;
        reloadCoolDown = 1200;

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

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public void addAmmo(int ammo) {
        this.ammo += ammo;
    }

    @Override
    public void nullCountToCoolDown() {
        countToCoolDown = 0;
        curAmmo++;
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
    public Body getNextBullet(Vec pos, float z, Vec dir) {
        if (countToCoolDown != 0 || curAmmo == 0) return null;
        countToCoolDown = 1;

        curAmmo--;
        if (curAmmo <= 0) {
            curAmmo = 0;
            coolDown = reloadCoolDown;
        }

        pos = pos.add(dir.multy(bulletR + 10));

        Bullet bullet = new Bullet(pos.x, pos.y, z + bulletH, bulletL, bulletR, 1, 10, lifeTime);
        bullet.setVelV(new Vec(dir.x, dir.y).multy(bulletVel));
        bullet.setFriction(0);
        bullet.setJumpAcc(0);
        bullet.setDamage(damage);
        bullet.setBitmap();

        return bullet;
    }

    @Override
    public Body getNextBullet(Vec pos, float z, Vec dir, Vec point, float targetZ) {
        if (countToCoolDown != 0 || curAmmo == 0) return null;
        countToCoolDown = 1;

        curAmmo--;
        if (curAmmo <= 0) {
            curAmmo = 0;
            coolDown = reloadCoolDown;
        }

        pos = pos.add(dir.multy(bulletR + 10));

        float dist = point.subtr(pos).mag();

        Bullet bullet = new Bullet(pos.x, pos.y, z + bulletH, bulletL, bulletR, 1, 10, lifeTime);
        bullet.setVelV(new Vec(dir.x, dir.y).multy(bulletVel));
        bullet.setFriction(0);
        bullet.setJumpAcc(0);
        bullet.setJumpVel(dist != 0 ? bulletVel * (targetZ - z) / dist : 0);
        bullet.setJumped(true);
        bullet.setDamage(damage);
        bullet.setBitmap();

        return bullet;
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
