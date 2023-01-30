package com.example.game.characters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.game.R;
import com.example.game.buildlogic.Parallelepiped;
import com.example.game.buildlogic.Primitive;
import com.example.game.core.GameView;
import com.example.game.ui.JoyStick;

public class Player extends Parallelepiped {
    private Bitmap image;

    private float plJumpForce, speedMode;

    public void setImageBitmap(Context context) {
        image = BitmapFactory.decodeResource(context.getResources(), R.drawable.character);
        image = Bitmap.createScaledBitmap(image, (int)(170 * GameView.HEIGHT_MULTIP), (int)(260 * GameView.HEIGHT_MULTIP), false);
    }

    public Player(float x, float y, float z, float side, int l) {
        super(x, y, z, side, side, l, false, false, true, false);

        viewX = (float)GameView.ORIG_WIDTH / 2 - side / 2;
        viewY = (float)GameView.ORIG_HEIGHT / 2 - side / 2;

        //SPEED MODES 4 - LOW, 8 - MIDDLE, 15 - HIGH
        speedMode = 8;

        plJumpForce = 30;
    }

    public void changeSpeed(JoyStick js) {
        selfSpeedX = js.getXDir() * speedMode;

        selfSpeedY = js.getYDir() * speedMode;
    }

    public void setJumpSpeed() {
        jumpSpeed = plJumpForce;
    }
    public float getSpeedMode() {
        return speedMode;
    }

    public void draw(Canvas c, Paint p, boolean freeCam, float[] point) {
        p.setColor(Color.GREEN);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(5);

        if (!freeCam) {
            c.drawRect(viewX * GameView.HEIGHT_MULTIP, viewY * GameView.HEIGHT_MULTIP, (viewX + w) * GameView.HEIGHT_MULTIP,
                    (viewY + h) * GameView.HEIGHT_MULTIP, p);
            c.drawBitmap(image, (getViewX() - 35) * GameView.HEIGHT_MULTIP, (getViewY() - 150) * GameView.HEIGHT_MULTIP, p);

        } else if (freeCam) {
            c.drawRect((x - point[0]) * GameView.HEIGHT_MULTIP, (y - point[1] - z) * GameView.HEIGHT_MULTIP,
                    (x - point[0] + w) * GameView.HEIGHT_MULTIP, (y - point[1] + h - z) * GameView.HEIGHT_MULTIP, p);
            c.drawBitmap(image, (getX() - point[0] - 35) * GameView.HEIGHT_MULTIP, (getY() - getZ() - point[1] - 150) * GameView.HEIGHT_MULTIP, p);
        }
    }
}
