package com.example.game.characters.pickups;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.game.MainActivity;
import com.example.game.core.GameView;
import com.example.game.ui.FuncButton;
import com.example.game.util.Vec;

public class ItemHolder {
    private Item[] items;
    private int itemCount;
    private float x1, y1, x2, y2;

    private float stepWidth;

    private int curPos;
    private boolean active;

    private Bitmap image;

    public ItemHolder(float x1, float y1, float x2, float y2, int size) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;

        items = new Item[size];

        stepWidth = (x2 - x1) / size;

        image = Bitmap.createScaledBitmap(MainActivity.itemholder, (int)stepWidth, (int)stepWidth, false);
    }

    public boolean onClick(float x, float y) {
        return x > x1 && x < x2 && y > y1 && y < y2;
    }

    public void functional(float x) {
        int pos = (int)((x - x1) / stepWidth);

        if ((curPos == pos && active) || items[pos] == null) {
            active = false;
            return;
        }
        else active = true;

        curPos = pos;
    }

    public boolean add(Item i) {
        if (itemCount >= items.length) return false;

        items[itemCount] = i;

        curPos = itemCount;

        itemCount++;

        return true;
    }

    public int getItemCount() {
        return itemCount;
    }

    public Item getBy(int index) {
        return items[index];
    }

    public Item getCur() {
        return active ? items[curPos] : null;
    }

    public void removeCur() {
        int pos = curPos + 1;
        int prevPos = curPos;

        items[prevPos] = null;

        while (pos < items.length) {
            items[prevPos] = items[pos];
            pos++;
            prevPos++;
        }
        items[items.length - 1] = null;

        active = false;

        itemCount--;
    }

    public FuncButton getExtraButton() {
        if (active) return items[curPos].getExtraButton();
        return null;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void draw(Canvas c, Paint p, Vec plPosV, float plZ, float plX, float plY) {
        p.setColor(Color.WHITE);
        p.setStrokeWidth(5);

        //c.drawRect(x1, y1, x2, y2, p);

        for (int i = 0; i < items.length; i++) {
            //if (i != 0) c.drawLine(x1 + stepWidth * i, y1, x1 + stepWidth * i, y2, p);

            c.drawBitmap(image, x1 + stepWidth * i, y1, p);

            if (items[i] != null)
                items[i].draw(c, p, x1 + stepWidth * i, y1, x1 + stepWidth * (i + 1), y2);
        }

        if (active) {
            p.setStyle(Paint.Style.STROKE);
            p.setColor(Color.GREEN);
            p.setStrokeWidth(4 * GameView.HEIGHT_MULTIP);
            c.drawRect(x1 + curPos * stepWidth, y1, x1 + (curPos + 1) * stepWidth, y2, p);

            if (items[curPos].getAttackInput() != null) {
                items[curPos].drawDir(c, p, plPosV, plX, plY - plZ);
            }
        }
    }
}
