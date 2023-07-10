package com.example.game.species;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.game.MainActivity;
import com.example.game.characters.Enemy;
import com.example.game.characters.pickups.MachineGun;
import com.example.game.characters.pickups.Melee;
import com.example.game.characters.pickups.Pistol;
import com.example.game.characters.pickups.ShotGun;
import com.example.game.characters.pickups.ThrowableWeapon;
import com.example.game.core.GameView;
import com.example.game.util.Vec;

public class Portal extends Body {
    private Bitmap image;
    private float maxWidth, curWidth;

    public Portal(float x, float y, float z, float lifeTime) {
        super(x, y, z, 250, 1, 100000, 0, lifeTime);

        int rand = (int)(Math.random() * MainActivity.portals.length);
        image = Bitmap.createScaledBitmap(MainActivity.portals[rand], (int)(250 * GameView.HEIGHT_MULTIP), (int)(250 * GameView.HEIGHT_MULTIP), false);

        maxWidth = l / 2;
        curWidth = 0;

        velV = new Vec(0.00001f, 0.00001f);
    }

    public Portal(float x, float y, float z, float curTime, float lifeTime) {
        super(x, y, z, 250, 1, 100000, 0, lifeTime);

        int rand = (int)(Math.random() * MainActivity.portals.length);
        image = Bitmap.createScaledBitmap(MainActivity.portals[rand], (int)(250 * GameView.HEIGHT_MULTIP), (int)(250 * GameView.HEIGHT_MULTIP), false);

        health = curTime;
        maxWidth = l / 2;
        if (curTime > maxHealth - 2000) curWidth = maxWidth * ((maxHealth - health) / 2000);
        else if (curTime < 2000) curWidth = maxWidth * (health / 2000);
        else curWidth = maxWidth;

        velV = new Vec(0.00001f, 0.00001f);
    }

    @Override
    public float[] getRawRectangle(float plX, float plY) {
        return new float[] {
                (posV.x - image.getWidth() + plX) * GameView.HEIGHT_MULTIP,
                (posV.y - image.getHeight() + plY - z) * GameView.HEIGHT_MULTIP,
                (posV.x + image.getWidth() + plX) * GameView.HEIGHT_MULTIP,
                (posV.y + image.getHeight() + plY - z) * GameView.HEIGHT_MULTIP
        };
    }
    
    public Body spawn(int wave) {
        int progress = (wave - 1) / 5;
        double var = Math.random();

        switch (progress) {
            case 0:
                return new Enemy(posV.x, posV.y, z, 70, 50, 5, 0.5f,
                        0.4f, 10 + wave, 1000, 3000000,
                        true, true, false, false, new Melee());
            case 1:
                if (var > 0.5)
                return new Enemy(posV.x, posV.y, z, 70, 50, 5, 0.5f,
                        0.4f, 10 + wave, 1000, 3000000,
                        true, true, wave > 7, false, new Pistol(100, 50, 50));
                else return new Enemy(posV.x, posV.y, z, 70, 50, 5, 0.5f,
                        0.4f, 10 + wave, 1000, 3000000,
                        true, true, false, false, new Melee());
            case 2:
                if (var > 0.5)
                return new Enemy(posV.x, posV.y, z, 70, 50, 5, 0.5f,
                        0.4f, 10, 1000, 3000000,
                        true, true, Math.random() > 0.2, false, new ShotGun(100, 25, 25));
                else if (var < 0.2f) return new Enemy(posV.x, posV.y, z, 70, 50, 5, 0.5f,
                        0.4f, 10 + wave, 1000, 3000000,
                        true, true, false, false, new Melee());
                else new Enemy(posV.x, posV.y, z, 70, 50, 5, 0.5f,
                            0.4f, 10 + wave, 1000, 3000000,
                            true, true, Math.random() > 0.2, false, new Pistol(100, 50, 50));
            case 3:
                if (var > 0.8)
                return new Enemy(posV.x, posV.y, z, 70, 50, 5, 0.5f,
                        0.4f, 10 + wave, 500, 3000000,
                        true, true, Math.random() > 0.2, false, new MachineGun(100, 100, 100));
                else if (var > 0.5)
                    return new Enemy(posV.x, posV.y, z, 70, 50, 5, 0.5f,
                            0.4f, 10 + wave, 2000, 3000000,
                            true, true, Math.random() > 0.2, false, new ShotGun(100, 25, 25));
                else if (var < 0.2f) return new Enemy(posV.x, posV.y, z, 70, 50, 5, 0.5f,
                        0.4f, 10 + wave, 1000, 3000000,
                        true, true, false, false, new Melee());
                else new Enemy(posV.x, posV.y, z, 70, 50, 5, 0.5f,
                            0.4f, 10 + wave, 1000, 3000000,
                            true, true, Math.random() > 0.2, false, new Pistol(100, 50, 50));
            case 4:
                if (var > 0.8)
                return new Enemy(posV.x, posV.y, z, 70, 50, 5, 0.5f,
                        0.4f, 10 + wave, 2000, 3000000,
                        true, true, Math.random() > 0.2, false, new ThrowableWeapon(20, false,
                        Math.random() > 0.1f));
                else if (var > 0.7)
                    return new Enemy(posV.x, posV.y, z, 70, 50, 5, 0.5f,
                            0.4f, 10 + wave, 500, 3000000,
                            true, true, Math.random() > 0.2, false, new MachineGun(100, 100, 100));
                else if (var > 0.3)
                    return new Enemy(posV.x, posV.y, z, 70, 50, 5, 0.5f,
                            0.4f, 10 + wave, 2000, 3000000,
                            true, true, Math.random() > 0.2, false, new ShotGun(100, 25, 25));
                else if (var < 0.2f) return new Enemy(posV.x, posV.y, z, 70, 50, 5, 0.5f,
                        0.4f, 10 + wave, 1000, 3000000,
                        true, true, false, false, new Melee());
                else new Enemy(posV.x, posV.y, z, 70, 50, 5, 0.5f,
                            0.4f, 10 + wave, 1000, 3000000,
                            true, true, Math.random() > 0.2, false, new Pistol(100, 50, 50));
            default:
                if (var > 0.8)
                    return new Enemy(posV.x, posV.y, z, 70, 50, 5, 0.5f,
                            0.4f, 10 + wave, 2000, 3000000,
                            true, true, Math.random() > 0.2, false, new ThrowableWeapon(20, false,
                            Math.random() > 0.1f));
                else if (var > 0.7)
                    return new Enemy(posV.x, posV.y, z, 70, 50, 5, 0.5f,
                            0.4f, 10 + wave, 500, 3000000,
                            true, true, Math.random() > 0.2, false, new MachineGun(100, 100, 100));
                else if (var > 0.3)
                    return new Enemy(posV.x, posV.y, z, 70, 50, 5, 0.5f,
                            0.4f, 10 + wave, 2000, 3000000,
                            true, true, Math.random() > 0.2, false, new ShotGun(100, 25, 25));
                else if (var < 0.2f) return new Enemy(posV.x, posV.y, z, 70, 50, 5, 0.5f,
                        0.4f, 10 + wave, 1000, 3000000,
                        true, true, false, false, new Melee());
                else new Enemy(posV.x, posV.y, z, 70, 50, 5, 0.5f,
                            0.4f, 10 + wave, 1000, 3000000,
                            true, true, Math.random() > 0.2, false, new Pistol(100, 50, 50));
        }
        return null;
    }

    public boolean clock() {
        return health % 2500 == 0;
    }

    public void count() {
        health--;
        if (health > maxHealth - 2000) {
            curWidth += (maxWidth - curWidth) / 60;
        }
        if (health < 2000) {
            curWidth -= curWidth / 30;
        }
    }

    @Override
    public void jump() {}

    @Override
    public void draw(Canvas c, Paint p, float plX, float plY) {
        if ((int)(2 * curWidth * GameView.HEIGHT_MULTIP) > 0) c.drawBitmap(Bitmap.createScaledBitmap(image, (int)(2 * curWidth * GameView.HEIGHT_MULTIP), image.getHeight(), false),
                (posV.x + plX - curWidth) * GameView.HEIGHT_MULTIP, (posV.y + plY - z - l) * GameView.HEIGHT_MULTIP, p);
    }
}
