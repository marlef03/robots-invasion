package com.example.game.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.game.MainActivity;
import com.example.game.characters.pickups.*;
import com.example.game.core.GameView;

public class ItemCard extends FuncButton {
    FuncButton[] buttons;
    private Item item;
    private Bitmap image;
    private int cost, ammoCost;

    public ItemCard(float x1, float y1, float x2, float y2, Item item, int cost, int ammoCost) {
        super(x1, y1, x2, y2);
        this.item = item;

        active = true;

        this.cost = cost;
        this.ammoCost = ammoCost;

        if (!(item instanceof Melee || item instanceof MedKit)) {
            buttons = new FuncButton[2];

            buttons[0] = new FuncButton(x1 + 15, y1 + 15, x2 - 15, y2 - 5 - (y2 - y1) / 2);
            buttons[1] = new FuncButton(x1 + 15, y2 + 5 - (y2 - y1) / 2, x2 - 15, y2 - 15);
        }

        image = Bitmap.createScaledBitmap(item.getImage(), (int)(y2 - centerY - 30), (int)(y2 - centerY - 30), false);
    }

    @Override
    public void click(float x, float y) {
        press = super.onClick(x, y) && !press;

        if (buttons != null) {
            if (press) {
                buttons[0].setActive(true);
                buttons[1].setActive(true);
            } else {
                buttons[0].setActive(false);
                buttons[1].setActive(false);
            }
        }
    }

    public int getCost() {
        return cost;
    }

    public int getAmmoCost() {
        return ammoCost;
    }

    public Item getItem() {
        return item;
    }

    public void draw(Canvas c, Paint p) {
        p.setColor(Color.rgb(30, 30, 30));
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(10 * GameView.HEIGHT_MULTIP);

        super.draw(c, p);

        c.drawBitmap(image, centerX + 15, centerY + 15, p);

        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.GREEN);
        p.setTextSize(50 * (float)Math.sqrt(GameView.HEIGHT_MULTIP));
        p.setAntiAlias(true);
        p.setTypeface(MainActivity.font);

        if (ammoCost == 0) c.drawText("$" + cost, x2 - p.measureText("$" + cost) - 15 * (float)Math.sqrt(GameView.HEIGHT_MULTIP), y2 - 30 * (float)Math.sqrt(GameView.HEIGHT_MULTIP), p);
        else {
            c.drawText("$" + cost, x2 - p.measureText("$" + cost) - 15 * (float)Math.sqrt(GameView.HEIGHT_MULTIP), y2 - 80 * (float)Math.sqrt(GameView.HEIGHT_MULTIP), p);
            c.drawText("$" + ammoCost, x2 - p.measureText("$" + ammoCost) - 15 * (float)Math.sqrt(GameView.HEIGHT_MULTIP), y2 - 25 * (float)Math.sqrt(GameView.HEIGHT_MULTIP), p);
        }

        p.setColor(Color.WHITE);
        p.setTextSize(36 * (float)Math.sqrt(GameView.HEIGHT_MULTIP));
        if (item instanceof Melee)
            c.drawText("Бита", centerX + 10, centerY + 40 * (float)Math.sqrt(GameView.HEIGHT_MULTIP), p);
        else if (item instanceof Pistol)
            c.drawText("Пистолет", centerX + 10, centerY + 40 * (float)Math.sqrt(GameView.HEIGHT_MULTIP), p);
        else if (item instanceof ShotGun)
            c.drawText("Дробовик", centerX + 10, centerY + 40 * (float)Math.sqrt(GameView.HEIGHT_MULTIP), p);
        else if (item instanceof MachineGun)
            c.drawText("Пулемёт", centerX + 10, centerY + 40 * (float)Math.sqrt(GameView.HEIGHT_MULTIP), p);
        else if (item instanceof ThrowableWeapon && !((ThrowableWeapon) item).getSticky() && !((ThrowableWeapon) item).getInstantExplosion())
            c.drawText("Временные бомбы", centerX + 10, centerY + 40 * (float)Math.sqrt(GameView.HEIGHT_MULTIP), p);
        else if (item instanceof ThrowableWeapon && !((ThrowableWeapon) item).getSticky())
            c.drawText("Контактные бомбы", centerX + 10, centerY + 40 * (float)Math.sqrt(GameView.HEIGHT_MULTIP), p);
        else if (item instanceof ThrowableWeapon && ((ThrowableWeapon) item).getSticky())
            c.drawText("Бомбы-липучки", centerX + 10, centerY + 40 * (float)Math.sqrt(GameView.HEIGHT_MULTIP), p);
        else if (item instanceof MedKit)
            c.drawText("Аптечка", centerX + 10, centerY + 40 * (float)Math.sqrt(GameView.HEIGHT_MULTIP), p);

        if (press && buttons != null) {
            p.setColor(Color.YELLOW);
            p.setStyle(Paint.Style.FILL);

            buttons[0].draw(c, p);

            p.setColor(Color.GREEN);

            buttons[1].draw(c, p);

            p.setColor(Color.BLACK);
            p.setTextSize(70 * (float)Math.sqrt(GameView.HEIGHT_MULTIP));
            p.setAntiAlias(true);
            p.setTypeface(MainActivity.font);

            c.drawText("Оружие", centerX + 60 * (float)Math.sqrt(GameView.HEIGHT_MULTIP), centerY + 100 * (float)Math.sqrt(GameView.HEIGHT_MULTIP), p);
            c.drawText("Патроны", centerX + 40 * (float)Math.sqrt(GameView.HEIGHT_MULTIP), y2 - 50 * (float)Math.sqrt(GameView.HEIGHT_MULTIP), p);
        }

        p.setAntiAlias(false);
    }

}
