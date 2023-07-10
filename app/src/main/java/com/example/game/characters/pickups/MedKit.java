package com.example.game.characters.pickups;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.game.MainActivity;
import com.example.game.core.GameView;
import com.example.game.species.Circle;
import com.example.game.ui.FuncButton;

public class MedKit extends Item {
    private int ammo, maxAmmo;
    private float amount;

    private FuncButton useButton;

    public MedKit(MedKit m) {
        super(m);

        image = m.image;

        ammo = 1;
        maxAmmo = 10;
        amount = 40;

        useButton = new FuncButton(600 * GameView.HEIGHT_MULTIP, GameView.HEIGHT - 400 * GameView.HEIGHT_MULTIP, 80 * GameView.HEIGHT_MULTIP);
        useButton.setImage(MainActivity.healbutton);
        useButton.setActive(true);
    }

    public MedKit() {
        super(new Circle(0, 0, 40), 5, 1, 30, 0);

        image = MainActivity.medkit;

        ammo = 1;
        maxAmmo = 10;
        amount = 40;

        useButton = new FuncButton(600 * GameView.HEIGHT_MULTIP, GameView.HEIGHT - 400 * GameView.HEIGHT_MULTIP, 80 * GameView.HEIGHT_MULTIP);
        useButton.setImage(MainActivity.healbutton);
        useButton.setActive(true);
    }

    @Override
    public FuncButton getExtraButton() {
        return useButton;
    }

    public float getAmount() {
        return amount;
    }

    public int getAmmo() {
        return ammo;
    }

    public boolean isMaxAmmo() {
        return ammo < maxAmmo;
    }

    @Override
    public void addAmmo(int ammo) {
        this.ammo += ammo;
    }

    @Override
    public void draw(Canvas c, Paint p, float x1, float y1, float x2, float y2) {
        c.drawBitmap(Bitmap.createScaledBitmap(image, (int)(x2 - x1 - 20), (int)(y2 - y1 - 20), false), x1 + 10, y1 + 10, p);

        p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.FILL);
        p.setTextSize(40 * GameView.HEIGHT_MULTIP);
        p.setAntiAlias(true);
        p.setTypeface(MainActivity.font);

        c.drawText(String.valueOf(ammo), x2 - p.measureText(String.valueOf(ammo)) * GameView.HEIGHT_MULTIP, y2 - 15 * GameView.HEIGHT_MULTIP, p);

        p.setAntiAlias(false);
    }
}
