package com.example.game.characters;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.game.MainActivity;
import com.example.game.characters.pickups.Item;
import com.example.game.characters.pickups.ItemHolder;
import com.example.game.characters.pickups.MachineGun;
import com.example.game.characters.pickups.Melee;
import com.example.game.characters.pickups.Pistol;
import com.example.game.characters.pickups.ShotGun;
import com.example.game.characters.pickups.ThrowableWeapon;
import com.example.game.core.GameView;
import com.example.game.species.Body;
import com.example.game.util.ImageManager;
import com.example.game.util.Vec;

public class Player extends Body {
    private Vec lastDir;
    private int stepsStream = -1;
    private int money;

    private ImageManager imageManager;

    private float jumpForce;

    private ItemHolder itemHolder;

    public Player(float x, float y, float z) {
        super(x, y, z, 100, 50, 15, 0.4f, 100);

        bounceCoeff = 10;
        friction = 0.002f;
        velForce = 0.6f;
        jumpForce = 2f;

        lastDir = new Vec(1, 0);

        itemHolder = new ItemHolder((float)GameView.WIDTH / 2 - 430 * GameView.HEIGHT_MULTIP, GameView.HEIGHT - 200 * GameView.HEIGHT_MULTIP,
                (float)GameView.WIDTH / 2 + 430 * GameView.HEIGHT_MULTIP, GameView.HEIGHT - 30 * GameView.HEIGHT_MULTIP, 5);

        imageManager = new ImageManager();
        imageManager.setWalkFrames(MainActivity.characterWalkFrames);
        imageManager.setPistolWalkFrames(MainActivity.characterPistolWalkFrames);
        imageManager.setMachineGunWalkFrames(MainActivity.characterMachineGunWalkFrames);
        imageManager.setShotGunWalkFrames(MainActivity.characterShotGunWalkFrames);
        imageManager.setMeleeWalkFrames(MainActivity.characterMeleeWalkFrames);
        imageManager.setBombWalkFrames(MainActivity.characterBombWalkFrames);
        imageManager.setContactBombWalkFrames(MainActivity.characterContactBombWalkFrames);
        imageManager.setStickyBombWalkFrames(MainActivity.characterStickyBombWalkFrames);
        imageManager.setMedKitWalkFrames(MainActivity.characterMedKitWalkFrames);
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getMoney() {
        return money;
    }

    public void changeMoney(int amount) {
        money += amount;
    }

    public float getJumpForce() {
        return jumpForce;
    }

    public ItemHolder getItemHolder() {
        return itemHolder;
    }

    public void setLastDir(Vec dir) {
        lastDir = dir;
    }

    @Override
    public void move() {
        super.move();

        if (accV.x != 0 && accV.y != 0) {
            lastDir = accV.unit();
            if (!jumped && stepsStream == -1) stepsStream = MainActivity.playSteps();
        }
        else if (stepsStream != -1) {
            MainActivity.stopSteps(stepsStream);
            stepsStream = -1;
        }

        shape.setDirV(lastDir);
    }

    @Override
    public void changeSpeed(float xDir, float yDir) {
        accV.x = xDir * velForce;

        accV.y = yDir * velForce;

        if (accV.x != 0 && accV.y != 0) {
            if (velV.mag() >= 0 && velV.mag() <= velForce) {
                velV.x = xDir * velForce;

                velV.y = yDir * velForce;
            }
        }
    }

    @Override
    public void draw(Canvas c, Paint p, float plX, float plY) {
        Bitmap im = Bitmap.createScaledBitmap(imageManager.getCurrentStatusFrame(lastDir, accV.mag() != 0, itemHolder.getCur()),
                (int)((l + shape.getR() * 2) * GameView.HEIGHT_MULTIP), (int)((l + shape.getR() * 2) * GameView.HEIGHT_MULTIP), false);

        c.drawBitmap(im, (posV.x + plX - (shape.getR() + l / 2)) * GameView.HEIGHT_MULTIP, (posV.y + plY - shape.getR() - z - l) * GameView.HEIGHT_MULTIP, p);
    }
}
