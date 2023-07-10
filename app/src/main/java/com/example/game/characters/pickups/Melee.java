package com.example.game.characters.pickups;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.game.MainActivity;
import com.example.game.core.GameView;
import com.example.game.species.Body;
import com.example.game.species.Bullet;
import com.example.game.species.Rectangle;
import com.example.game.ui.JoyStick;
import com.example.game.util.Vec;

public class Melee extends Item {
    private float bulletVel, bulletH, bulletL;
    private int lifeTime;
    private int coolDown, countToCoolDown;

    private float damage;

    private int width, height;

    public Melee() {
        super(new Rectangle(0, 0, 0, 150, 50), 10, 1, 20, 0);

        image = MainActivity.melee;

        bulletVel = 0.8f;
        bulletH   = 20;
        bulletL   = 80;

        damage = 0.8f;

        lifeTime = 160;
        coolDown = 100;

        width = 50;
        height = 10;

        attackInput = new JoyStick(240 * GameView.HEIGHT_MULTIP, GameView.HEIGHT - 240 * GameView.HEIGHT_MULTIP, 200 * GameView.HEIGHT_MULTIP, 70 * GameView.HEIGHT_MULTIP);
        attackInput.setStickImage(Bitmap.createScaledBitmap(MainActivity.attackjoystickstick, (int)(140 * GameView.HEIGHT_MULTIP), (int)(140 * GameView.HEIGHT_MULTIP), false));
    }

    public void countCoolDown() {
        if (countToCoolDown == coolDown) countToCoolDown = 0;
        if (countToCoolDown != 0) countToCoolDown++;
    }

    @Override
    public Bitmap getGroundImage() {
        return MainActivity.meleeGround;
    }

    @Override
    public void nullCountToCoolDown() {
        countToCoolDown = 0;
    }

    @Override
    public Body getNextBullet(Vec pos, float z, Vec dir) {
        if (countToCoolDown != 0) return null;
        countToCoolDown = 1;

        pos = pos.add(dir.multy(height));
        Vec firstP = pos.add(dir.normal().multy(width).add(dir.normal().multy(height)));
        Vec secondP = pos.add(dir.normal().multy(-width).add(dir.normal().multy(-height)));

        Bullet bullet = new Bullet(firstP.x, firstP.y, secondP.x, secondP.y, z + bulletH, bulletL, height, 10, 10, lifeTime);
        bullet.setVelV(new Vec(dir.x, dir.y).multy(bulletVel));
        bullet.setFriction(0);
        bullet.setJumpAcc(0);
        bullet.setDamage(damage);
        bullet.setBitmap();

        return bullet;
    }

    @Override
    public void drawDir(Canvas c, Paint p, Vec plPosV, float plX, float plY) {
        p.setColor(Color.GRAY);
        p.setStrokeWidth(5);
        c.drawLine((plPosV.x + plX) * GameView.HEIGHT_MULTIP, (plPosV.y + plY) * GameView.HEIGHT_MULTIP,
                (plPosV.x + attackInput.getXDir() * 100 * GameView.HEIGHT_MULTIP + plX) * GameView.HEIGHT_MULTIP,
                (plPosV.y + attackInput.getYDir() * 100 * GameView.HEIGHT_MULTIP + plY) * GameView.HEIGHT_MULTIP, p);
    }

    @Override
    public void draw(Canvas c, Paint p, float x1, float y1, float x2, float y2) {
        c.drawBitmap(Bitmap.createScaledBitmap(image, (int)(x2 - x1 - 20), (int)(y2 - y1 - 20), false), x1 + 10, y1 + 10, p);
    }
}
