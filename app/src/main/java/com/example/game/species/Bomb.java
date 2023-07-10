package com.example.game.species;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.game.MainActivity;
import com.example.game.core.GameView;

public class Bomb extends Body {
    private Bitmap[] animation;
    private int step;

    public Bomb(float x, float y, float z, float l, int r, float mass, float elasticity, float endTime) {
        super(x, y, z, l, r, mass, elasticity, endTime);

        animation = MainActivity.bombAnimation;
        for (int i = 0; i < animation.length; i++) {
            animation[i] = Bitmap.createScaledBitmap(animation[i], (int)(shape.getR() * 2 + l), (int)(shape.getR() * 2 + l), false);
        }

        step = 1500 / animation.length;
    }

    public void count() {
        changeHealth(-1);
    }

    @Override
    public void draw(Canvas c, Paint p, float plX, float plY) {
        c.drawBitmap(animation[(int)((1500 - health) / step) < animation.length ? (int)((1500 - health) / step) : animation.length - 1], (posV.x - shape.getR() + plX) * GameView.HEIGHT_MULTIP,
                (posV.y - shape.getR() + plY - z - l) * GameView.HEIGHT_MULTIP, p);
    }

}
