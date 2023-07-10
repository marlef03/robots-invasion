package com.example.game.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.game.MainActivity;
import com.example.game.characters.pickups.Ammo;
import com.example.game.characters.pickups.Item;
import com.example.game.characters.pickups.MachineGun;
import com.example.game.characters.pickups.MedKit;
import com.example.game.characters.pickups.Melee;
import com.example.game.characters.pickups.Pistol;
import com.example.game.characters.pickups.ShotGun;
import com.example.game.characters.pickups.ThrowableWeapon;
import com.example.game.core.GameView;

public class ShopUI extends FuncButton {
    private float formX1, formY1, formX2, formY2;
    private ItemCard[][] assortment;
    private Item toPurchase;
    private int toPurchaseCost, purchaseStatus = -1;
    private int messageCount;

    private float width, height;

    public ShopUI(float centerX, float centerY, float radius) {
        super(centerX, centerY, radius);

        formX1 = 30 * GameView.WIDTH_MULTIP;
        formY1 = 30 * GameView.HEIGHT_MULTIP;
        formX2 = GameView.WIDTH - 30 * GameView.WIDTH_MULTIP;
        formY2 = GameView.HEIGHT - 30 * GameView.HEIGHT_MULTIP;

        width = (formX2 - formX1) / 4;
        height = (formY2 - formY1) / 3;

        assortment = new ItemCard[][] {
                new ItemCard[] {
                        new ItemCard(formX1, formY1, formX1 + width, formY1 + height, new Melee(), 200, 0),
                        new ItemCard(formX1 + width, formY1, formX1 + 2 * width, formY1 + height,
                                new Pistol(100, 20, 20), 1000, 200),
                        new ItemCard(formX1 + 2 * width, formY1, formX1 + 3 * width, formY1 + height,
                                new ShotGun(150, 25, 25), 2500, 300),
                        new ItemCard(formX1 + 3 * width, formY1, formX1 + 4 * width, formY1 + height,
                                new MachineGun(500, 100, 100), 5000, 500),
                },
                new ItemCard[] {
                        new ItemCard(formX1, formY1 + height, formX1 + width, formY1 + 2 * height,
                                new ThrowableWeapon(20, false, false), 5000, 500),
                        new ItemCard(formX1 + width, formY1 + height, formX1 + 2 * width, formY1 + 2 * height,
                                new ThrowableWeapon(20, false, true), 5000, 500),
                        new ItemCard(formX1 + 2 * width, formY1 + height, formX1 + 3 * width, formY1 + 2 * height,
                                new ThrowableWeapon(20, true, false), 5000, 500),
                        new ItemCard(formX1 + 3 * width, formY1 + height, formX1 + 4 * width, formY1 + 2 * height,
                                new MedKit(), 500, 0),
                }
        };

        setImage(MainActivity.returnbutton);
    }

    public int getToPurchaseCost() {
        return toPurchaseCost;
    }

    public Item getToPurchase() {
        return toPurchase;
    }

    public void resetToPurchase() {
        toPurchase = null;
        toPurchaseCost = 0;
    }

    public void purchaseStatus(int code) {
        if (code == 0) MainActivity.playMoneySpent();
        else MainActivity.playFailure();

        purchaseStatus = code;
        messageCount = 1;
    }

    @Override
    public void click(float x, float y) {
        super.click(x, y);
        if (!active) return;
        if (press) active = false;
        press = false;

        int desX = -1, desY = -1;

        for (int posY = 0; posY < assortment.length; posY++) {
            for (int posX = 0; posX < assortment[posY].length; posX++) {
                if (assortment[posY][posX] != null) {
                    if (assortment[posY][posX].buttons != null && assortment[posY][posX].getPress()) {
                        assortment[posY][posX].buttons[0].click(x, y);
                        assortment[posY][posX].buttons[1].click(x, y);

                        if (assortment[posY][posX].buttons[0].getPress()) {
                            toPurchase = assortment[posY][posX].getItem();

                            desX = posX;
                            desY = posY;
                        }
                        else if (assortment[posY][posX].buttons[1].getPress()) {
                            Item i = assortment[posY][posX].getItem();
                            if (i instanceof Pistol) toPurchase = new Ammo(100, 0);
                            else if (i instanceof ShotGun) toPurchase = new Ammo(100, 1);
                            else if (i instanceof MachineGun) toPurchase = new Ammo(250, 2);
                            else if (i instanceof ThrowableWeapon) toPurchase = new Ammo(25, 3);

                            desX = posX;
                            desY = posY;
                        }
                    }

                    assortment[posY][posX].click(x, y);

                    if (assortment[posY][posX].buttons == null && assortment[posY][posX].getPress()) {
                        toPurchase = assortment[posY][posX].getItem();

                        assortment[posY][posX].setPress(false);

                        desX = posX;
                        desY = posY;
                    }
                }
            }
        }

        if (toPurchase != null && desX > -1) {
            if (toPurchase instanceof Ammo) toPurchaseCost = assortment[desY][desX].getAmmoCost();
            else toPurchaseCost = assortment[desY][desX].getCost();
        }
    }

    @Override
    public void draw(Canvas c, Paint p) {
        if (active) {
            c.drawARGB(150, 0, 0, 0);

            p.setColor(Color.rgb(50, 50, 50));
            p.setStyle(Paint.Style.FILL);
            c.drawRect(formX1, formY1, formX2, formY2, p);

            for (int i = 0; i < assortment.length; i++) {
                for (int j = 0; j < assortment[i].length; j++) {
                    assortment[i][j].draw(c, p);
                }
            }

            c.drawBitmap(image, centerX - radius, centerY - radius, p);
        }

        if (messageCount > 0) {
            switch (purchaseStatus) {
                case 0:
                    p.setStyle(Paint.Style.FILL);
                    p.setColor(Color.GREEN);
                    p.setTextSize(40 * GameView.HEIGHT_MULTIP);
                    p.setAntiAlias(true);
                    p.setTypeface(MainActivity.font);

                    c.drawText("Успешная покупка!", (float)GameView.WIDTH / 2 - p.measureText("Успешная покупка!") / 2, 120, p);
                    break;
                case 1:
                    p.setStyle(Paint.Style.FILL);
                    p.setColor(Color.RED);
                    p.setTextSize(40 * GameView.HEIGHT_MULTIP);
                    p.setAntiAlias(true);
                    p.setTypeface(MainActivity.font);

                    c.drawText("Недостаточно средств для покупки!", (float)GameView.WIDTH / 2 - p.measureText("Недостаточно средств для покупки!") / 2, 120, p);
                    break;
                case 2:
                    p.setStyle(Paint.Style.FILL);
                    p.setColor(Color.RED);
                    p.setTextSize(40 * GameView.HEIGHT_MULTIP);
                    p.setAntiAlias(true);
                    p.setTypeface(MainActivity.font);

                    c.drawText("Недостаточно места в инвентаре!", (float)GameView.WIDTH / 2 - p.measureText("Недостаточно места в инвентаре!") / 2, 120, p);
                    break;
                case 3:
                    p.setStyle(Paint.Style.FILL);
                    p.setColor(Color.RED);
                    p.setTextSize(40 * GameView.HEIGHT_MULTIP);
                    p.setAntiAlias(true);
                    p.setTypeface(MainActivity.font);

                    c.drawText("Отсутствует оружие для приобретения патронов!",
                            (float)GameView.WIDTH / 2 - p.measureText("Отсутствует оружие для приобретения патронов!") / 2, 120, p);
                    break;
            }

            messageCount++;
            if (messageCount == 120) messageCount = 0;
        }

        p.setAntiAlias(false);
    }
}
