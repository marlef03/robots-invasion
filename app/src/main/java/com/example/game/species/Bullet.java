package com.example.game.species;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

import com.example.game.MainActivity;
import com.example.game.core.GameView;
import com.example.game.util.Vec;

public class Bullet extends Body {
    Bitmap[] images;

    public Bullet(float x, float y, float z, float l, int r, float mass, float elasticity, float endTime) {
        super(x, y, z, l, r, mass, elasticity, endTime);
    }

    public Bullet(float x1, float y1, float x2, float y2, float z, float l, int w, float mass, float elasticity, float endTime) {
        super(x1, y1, x2, y2, z, l, w, mass, elasticity, endTime);
    }

    public void setBitmap() {
        if (shape instanceof Circle) {
            images = new Bitmap[]{MainActivity.pistolbullet};
            Vec dir = velV.unit();
            float angle;
            if (dir.y > 0) {
                angle = (float) Math.toDegrees(Math.acos(dir.x));
            } else {
                angle = (float) Math.toDegrees(2 * Math.PI - Math.acos(dir.x));
            }

            images[0] = Bitmap.createScaledBitmap(images[0], (int) (6 * shape.getR() * GameView.HEIGHT_MULTIP), (int) (3 * shape.getR() * GameView.HEIGHT_MULTIP), false);
            Matrix matrix = new Matrix();
            matrix.postRotate(angle);
            images[0] = Bitmap.createBitmap(images[0], 0, 0, images[0].getWidth(), images[0].getHeight(), matrix, true);
        }
        else {
            images = MainActivity.meleeWave;
            Vec dir = velV.unit();
            for (int i = 0; i < images.length; i++) {
                images[i] = Bitmap.createScaledBitmap(images[i], (int) (l * GameView.HEIGHT_MULTIP), (int) (l * GameView.HEIGHT_MULTIP), false);

                if (dir.x < 0) {
                    Matrix matrix = new Matrix();
                    matrix.postScale(-1.0f, 1.0f);
                    images[i] = Bitmap.createBitmap(images[i], 0, 0, images[i].getWidth(), images[i].getHeight(), matrix, true);
                }
            }
        }
    }

    public void jump() {
        if (jumped) {
            z += jumpVel;
            jumpVel += jumpAcc;
            if (z <= 0) {
                z = 0;
                health = 0;
            }
        }
    }

    public void count() {
        changeHealth(-1);
    }

    @Override
    public void draw(Canvas c, Paint p, float plX, float plY) {
        if (images != null) {
            if (images.length == 1)
                c.drawBitmap(images[0], (posV.x - (float) images[0].getWidth() / 2 + plX) * GameView.HEIGHT_MULTIP,
                        (posV.y - z - (float) images[0].getHeight() / 2 + plY) * GameView.HEIGHT_MULTIP, p);
            else
                c.drawBitmap(images[(int)((1 - health / maxHealth) * 4)], (posV.x - (float) images[0].getWidth() / 2 + plX) * GameView.HEIGHT_MULTIP,
                        (posV.y - z - (float) images[0].getHeight() / 2 + plY) * GameView.HEIGHT_MULTIP, p);
        }
    }
}
