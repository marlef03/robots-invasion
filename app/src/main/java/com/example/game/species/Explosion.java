package com.example.game.species;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.game.MainActivity;
import com.example.game.core.GameView;
import com.example.game.util.ComplexImage;

public class Explosion extends Body {
    private float force;
    private int step, counter;

    ComplexImage[] frames;

    public Explosion(float x, float y, float z, float l, float r, int endTime, float force) {
        super(x, y, z, l, r, 0, 0, endTime);

        frames = new ComplexImage[] {
                new ComplexImage(MainActivity.explosionFrames[0], 185 * GameView.HEIGHT_MULTIP, 216 * GameView.HEIGHT_MULTIP),
                new ComplexImage(MainActivity.explosionFrames[1], 185 * GameView.HEIGHT_MULTIP, 216 * GameView.HEIGHT_MULTIP),
                new ComplexImage(MainActivity.explosionFrames[2], 185 * GameView.HEIGHT_MULTIP, 216 * GameView.HEIGHT_MULTIP),
                new ComplexImage(MainActivity.explosionFrames[3], 185 * GameView.HEIGHT_MULTIP, 216 * GameView.HEIGHT_MULTIP),
                new ComplexImage(MainActivity.explosionFrames[4], 185 * GameView.HEIGHT_MULTIP, 216 * GameView.HEIGHT_MULTIP),
                new ComplexImage(MainActivity.explosionFrames[5], 185 * GameView.HEIGHT_MULTIP, 216 * GameView.HEIGHT_MULTIP),
                new ComplexImage(MainActivity.explosionFrames[6], 185 * GameView.HEIGHT_MULTIP, 216 * GameView.HEIGHT_MULTIP),
                new ComplexImage(MainActivity.explosionFrames[7], 185 * GameView.HEIGHT_MULTIP, 216 * GameView.HEIGHT_MULTIP),
                new ComplexImage(MainActivity.explosionFrames[8], 185 * GameView.HEIGHT_MULTIP, 216 * GameView.HEIGHT_MULTIP),
                new ComplexImage(MainActivity.explosionFrames[9], 185 * GameView.HEIGHT_MULTIP, 216 * GameView.HEIGHT_MULTIP),
                new ComplexImage(MainActivity.explosionFrames[10], 185 * GameView.HEIGHT_MULTIP, 216 * GameView.HEIGHT_MULTIP),
                new ComplexImage(MainActivity.explosionFrames[11], 185 * GameView.HEIGHT_MULTIP, 216 * GameView.HEIGHT_MULTIP),
                new ComplexImage(MainActivity.explosionFrames[12], 185 * GameView.HEIGHT_MULTIP, 216 * GameView.HEIGHT_MULTIP),
                new ComplexImage(MainActivity.explosionFrames[13], 185 * GameView.HEIGHT_MULTIP, 216 * GameView.HEIGHT_MULTIP),
                new ComplexImage(MainActivity.explosionFrames[14], 185 * GameView.HEIGHT_MULTIP, 216 * GameView.HEIGHT_MULTIP),
                new ComplexImage(MainActivity.explosionFrames[15], 185 * GameView.HEIGHT_MULTIP, 216 * GameView.HEIGHT_MULTIP),
                new ComplexImage(MainActivity.explosionFrames[16], 185 * GameView.HEIGHT_MULTIP, 216 * GameView.HEIGHT_MULTIP),
                new ComplexImage(MainActivity.explosionFrames[17], 185 * GameView.HEIGHT_MULTIP, 216 * GameView.HEIGHT_MULTIP),
                new ComplexImage(MainActivity.explosionFrames[18], 185 * GameView.HEIGHT_MULTIP, 216 * GameView.HEIGHT_MULTIP)
        };

        this.force = force;

        step = endTime / frames.length;

        counter = 0;
    }

    public Explosion(float x, float y, float z, float l, float r, float maxHealth, float health, float force) {
        super(x, y, z, l, r, 0, 0, health);

        this.force = force;

        int time = (int)maxHealth;

        step = time / frames.length;

        counter = (int)((maxHealth - health) / step);
    }

    public float getForce() {
        return force;
    }

    @Override
    public void move() {}

    @Override
    public void jump() {}

    public void count() {
        changeHealth(-1);
        force *= 0.998f;

        if (health % step == 0 && counter < frames.length - 1) counter++;
    }

    @Override
    public void draw(Canvas c, Paint p, float plX, float plY) {
        frames[counter].drawSpecifiedImages(c, p, posV.x - 350, posV.y - z - l, plX, plY);
    }
}
