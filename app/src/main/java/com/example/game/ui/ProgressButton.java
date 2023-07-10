package com.example.game.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.example.game.MainActivity;
import com.example.game.core.GameView;

public class ProgressButton extends FuncButton {
    private float x2, y2, pos;

    public ProgressButton(float x1, float y1, float x2, float y2) {
        super(x1, y1, 0);
        this.x2 = x2;
        this.y2 = y2;
        pos = 1;
    }

    @Override
    public boolean onClick(float x, float y) {
        if (x > centerX && y > centerY && x < x2 && y < y2) {
            pos = (x - centerX) / (x2 - centerX);
            return true;
        }
        return false;
    }

    public float getPos() {
        return pos;
    }

    @Override
    public void draw(Canvas c, Paint p) {
        p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(10);
        c.drawRect(centerX, centerY, x2, y2, p);

        float x = centerX + (x2 - centerX) * pos;
        c.drawLine(x, centerY, x, y2, p);

        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.FILL);
        p.setTextSize(50 * GameView.HEIGHT_MULTIP);
        p.setAntiAlias(true);
        p.setTypeface(MainActivity.font);

        c.drawText((int)(pos * 100) + "%", centerX + (x2 - centerX) / 2 - 50 * GameView.WIDTH_MULTIP, centerY + (y2 - centerY) / 2 - 50 * GameView.HEIGHT_MULTIP, p);
    }
}
