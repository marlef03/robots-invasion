package com.example.game.species;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.game.MainActivity;
import com.example.game.core.GameView;

public class ContactBomb extends Bomb {
    Bitmap image;

    public ContactBomb(float x, float y, float z, float l, int r, float mass, float elasticity, float endTime) {
        super(x, y, z, l, r, mass, elasticity, endTime);

        image = Bitmap.createScaledBitmap(MainActivity.contactbomb, 2 * r, 2 * r, false);
    }

    @Override
    public void jump() {
        prevZ = z;

        if (jumped) {
            z += jumpVel;
            jumpVel += jumpAcc;
            if (z <= 0) {
                z = 0;
                health = 0;
            }
        }
    }

    @Override
    public void draw(Canvas c, Paint p, float plX, float plY) {
        c.drawBitmap(image, (posV.x - shape.getR() + plX) * GameView.HEIGHT_MULTIP, (posV.y - shape.getR() + plY - z - l) * GameView.HEIGHT_MULTIP, p);
    }
}
