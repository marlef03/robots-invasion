package com.example.game.characters.pickups;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.game.MainActivity;
import com.example.game.species.Circle;

public class Money extends Item {
    private int amount;

    public Money(int amount) {
        super(new Circle(0, 0, 50), 5, 1, 30, 0);

        image = MainActivity.money;

        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public void draw(Canvas c, Paint p, float x1, float y1, float x2, float y2) {

    }
}
