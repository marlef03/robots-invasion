package com.example.game.characters.pickups;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import com.example.game.MainActivity;
import com.example.game.core.GameView;
import com.example.game.species.Body;
import com.example.game.species.Bomb;
import com.example.game.species.Circle;
import com.example.game.species.ContactBomb;
import com.example.game.species.StickyBomb;
import com.example.game.ui.FuncButton;
import com.example.game.ui.JoyStick;
import com.example.game.util.Vec;

import java.util.ArrayList;

public class ThrowableWeapon extends Item {
    private float bulletVel, bulletH, bulletL;
    private int bulletR;
    private int lifeTime;
    private int coolDown, countToCoolDown;

    private float damage;

    private int ammo, maxStickies;
    private boolean sticky;
    private boolean instantExplosion;

    private float bulletJumpVel;

    private ArrayList<Body> stickies;

    private FuncButton detonateButton;

    public ThrowableWeapon(int ammo, boolean sticky, boolean instantExplosion) {
        super(new Circle(0, 0, 40), 10, 1, 30, 0);

        if (sticky) image = MainActivity.stickybomb;
        else if (instantExplosion) image = MainActivity.contactbomb;
        else image = MainActivity.bomb;

        bulletVel = 2.5f;
        bulletJumpVel = 1.7f;
        bulletH   = 40;
        bulletL   = 20;
        bulletR   = 40;

        damage = 0.08f;

        lifeTime = 1500;
        coolDown = 500;

        this.ammo = ammo;
        this.sticky = sticky;
        this.instantExplosion = instantExplosion;
        maxStickies = 10;

        attackInput = new JoyStick(240 * GameView.HEIGHT_MULTIP, GameView.HEIGHT - 240 * GameView.HEIGHT_MULTIP, 200 * GameView.HEIGHT_MULTIP, 70 * GameView.HEIGHT_MULTIP);
        attackInput.setStickImage(Bitmap.createScaledBitmap(MainActivity.attackjoystickstick, (int)(140 * GameView.HEIGHT_MULTIP), (int)(140 * GameView.HEIGHT_MULTIP), false));

        detonateButton = new FuncButton(600 * GameView.HEIGHT_MULTIP, GameView.HEIGHT - 400 * GameView.HEIGHT_MULTIP, 80 * GameView.HEIGHT_MULTIP);
        detonateButton.setImage(MainActivity.detonatebutton);
    }

    public int getAmmo() {
        return ammo;
    }

    public boolean getSticky() {
        return sticky;
    }

    public boolean getStickiesStatus() {
        return !sticky || stickies == null;
    }

    public boolean getInstantExplosion() {
        return instantExplosion;
    }

    public void deleteSticky(StickyBomb bomb) {
        stickies.remove(bomb);
        if (stickies.isEmpty()) clean();
    }

    public void addSticky(StickyBomb bmb) {
        if (stickies == null) {
            stickies = new ArrayList<>();
            detonateButton.setActive(true);
        }
        stickies.add(bmb);
    }

    @Override
    public Bitmap getGroundImage() {
        return MainActivity.bombGround;
    }

    @Override
    public FuncButton getExtraButton() {
        return detonateButton;
    }

    @Override
    public int getId() {
        return 3;
    }

    @Override
    public void addAmmo(int ammo) {
        this.ammo += ammo;
    }

    @Override
    public void nullCountToCoolDown() {
        countToCoolDown = 0;
        ammo++;
    }

    public void countCoolDown() {
        if (countToCoolDown == coolDown) countToCoolDown = 0;
        if (countToCoolDown != 0) countToCoolDown++;
    }

    public Body toMuchStickies() {
        if (stickies != null && stickies.size() > maxStickies) {
            Body toDel = stickies.get(0);
            stickies.remove(0);
            return toDel;
        }
        return null;
    }

    public ArrayList<Body> detonate() {
        return stickies;
    }

    public void clean() {
        stickies = null;
        detonateButton.setActive(false);
    }

    @Override
    public Body getNextBullet(Vec pos, float z, Vec dir, Vec point, float targetZ) {
        if (countToCoolDown != 0 || ammo == 0) return null;
        countToCoolDown = 1;

        ammo--;

        Body bullet;
        float dist = point.subtr(pos).mag();

        if (instantExplosion) {
            bullet = new ContactBomb(pos.x, pos.y, z + bulletH, bulletL, bulletR, 2, 1, lifeTime);
        }
        else {
            bullet = new Bomb(pos.x, pos.y, z + bulletH, bulletL, bulletR, 2, 1, lifeTime);
        }

        bullet.setVelV(new Vec(dir.x, dir.y).multy(dist / 600));
        bullet.setFriction(bullet.getVelV().mag() / lifeTime);
        bullet.setJumpVel(bulletJumpVel * (1 + dist / 20000));
        bullet.setJumped(true);
        bullet.setDamage(damage);

        return bullet;
    }

    @Override
    public Body getNextBullet(Vec pos, float z, Vec dir) {
        if (countToCoolDown != 0 || ammo == 0) return null;
        countToCoolDown = 1;

        ammo--;
        pos = pos.unit().multy(pos.mag() + bulletR);

        Body bullet;

        if (sticky) {
            bullet = new StickyBomb(pos.x, pos.y, z + bulletH, bulletL, bulletR, 2, 1, this);
        }
        else if (instantExplosion) {
            bullet = new ContactBomb(pos.x, pos.y, z + bulletH, bulletL, bulletR, 2, 1, lifeTime);
        }
        else {
            bullet = new Bomb(pos.x, pos.y, z + bulletH, bulletL, bulletR, 2, 1, lifeTime);
        }

        bullet.setVelV(new Vec(dir.x, dir.y).multy(0.4f + bulletVel * (attackInput.getDistToCenter() / attackInput.getRadius())));
        bullet.setJumpVel(0.4f + bulletJumpVel * (1 - attackInput.getDistToCenter() / attackInput.getRadius()));
        bullet.setJumped(true);
        bullet.setDamage(damage);

        if (sticky) {
            if (stickies == null) {
                stickies = new ArrayList<>();
                detonateButton.setActive(true);
            }
            stickies.add(bullet);
        }

        return bullet;
    }

    @Override
    public void drawDir(Canvas c, Paint p, Vec plPosV, float plX, float plY) {
        p.setColor(Color.GRAY);
        p.setStrokeWidth(5);
        c.drawArc((plPosV.x + plX - (attackInput.getXDir() > 0 ? 0 : bulletVel * 400 * -attackInput.getXDir() * (attackInput.getDistToCenter() / attackInput.getRadius()))) * GameView.HEIGHT_MULTIP,
                (plPosV.y + plY - bulletJumpVel * 100 * (1 - attackInput.getDistToCenter() / attackInput.getRadius())) * GameView.HEIGHT_MULTIP,
                (plPosV.x + plX + (attackInput.getXDir() > 0 ? bulletVel * 400 * attackInput.getXDir() * (attackInput.getDistToCenter() / attackInput.getRadius()) : 0)) * GameView.HEIGHT_MULTIP,
                (plPosV.y + plY + bulletJumpVel * 100 * (1 - attackInput.getDistToCenter() / attackInput.getRadius())) * GameView.HEIGHT_MULTIP,
                attackInput.getXDir() > 0 ? 180 : 270, 90, false, p);
    }

    @Override
    public void draw(Canvas c, Paint p, float x1, float y1, float x2, float y2) {
        c.drawBitmap(Bitmap.createScaledBitmap(image, (int)(x2 - x1 - 20), (int)(y2 - y1 - 20), false), x1 + 10, y1 + 10, p);

        p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.FILL);
        p.setTextSize(40 * GameView.HEIGHT_MULTIP);
        p.setAntiAlias(true);
        p.setTypeface(MainActivity.font);

        c.drawText(String.valueOf(ammo), x2 - p.measureText(String.valueOf(ammo)), y2 - 15 * GameView.HEIGHT_MULTIP, p);

        if (stickies != null) {
            c.drawText(String.valueOf(stickies.size()), x2 - p.measureText(String.valueOf(stickies.size())), y2 - 50 * GameView.HEIGHT_MULTIP, p);
        }
        p.setAntiAlias(false);
    }
}
