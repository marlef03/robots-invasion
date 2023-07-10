package com.example.game.species;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.example.game.MainActivity;
import com.example.game.core.GameView;
import com.example.game.util.ComplexImage;

public class Shop extends Body {
    private Circle trigger;

    public Shop(float x1, float y1, float x2, float y2, float z, float l, int w) {
        super(x1, y1, x2, y2, z, l, w, 0, 0.4f, 0);

        imageControl = new ComplexImage(MainActivity.shop, 158, 226);

        trigger = new Circle(posV.x, posV.y, shape.getMaxLength() + 150);
    }

    public Circle getTrigger() {
        return trigger;
    }

    @Override
    public void draw(Canvas c, Paint p, float plX, float plY) {
        imageControl.drawSpecifiedImages(c, p, posV.x - 382, posV.y - z - l - 263, plX, plY);
    }
}
