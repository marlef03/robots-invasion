package com.example.game.characters.pickups;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.game.species.Circle;

public class Ammo extends Item {
    private int amount, id;

    public Ammo(int amount, int id) {
        super(new Circle(0, 0, 30), 5, 1, 40, 0);

        this.amount = amount;

        //IDs: Pistol = 0, ShotGun = 1, MachineGun = 2, ThrowableWeapon = 3
        this.id = id;


    }

    public int getAmount() {
        return amount;
    }

    public int getId() {
        return id;
    }

    @Override
    public void draw(Canvas c, Paint p, float x1, float y1, float x2, float y2) {}
}
