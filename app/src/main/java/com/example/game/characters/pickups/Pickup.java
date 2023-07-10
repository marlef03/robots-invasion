package com.example.game.characters.pickups;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

import com.example.game.MainActivity;
import com.example.game.core.GameView;
import com.example.game.species.Body;
import com.example.game.species.Circle;
import com.example.game.species.Rectangle;
import com.example.game.util.Vec;

public class Pickup extends Body {
    private Item item;
    private Circle pickTrigger;
    private Bitmap image;

    private int counter;

    public Pickup(float x, float y, float z, Item item) {
        super(z, item.getL(), item.getMass(), item.getElasticity(), item.getMaxHealth());

        jumped = true;
        shape = item.getBody();
        shape.setPosV(new Vec(x, y));
        shape.getVertices(0);

        if (shape instanceof Rectangle) {
            float inertia = item.getMass() * (((Rectangle) shape).getWidth() * ((Rectangle) shape).getWidth() + shape.getLength() * shape.getLength()) / 12;
            invInertia = item.getMass() == 0 ? 0 : 1 / inertia;
        }

        posV = shape.getPosV();

        this.item = item;

        pickTrigger = new Circle(x, y, item.getBody().getMaxLength() + 150);

        counter = 10000;

        if ((item instanceof ThrowableWeapon && !((ThrowableWeapon)item).getSticky() && !((ThrowableWeapon)item).getInstantExplosion()) || item instanceof Melee)
            image = Bitmap.createScaledBitmap(item.getGroundImage(), (int)(1.5 * shape.getMaxLength() * GameView.HEIGHT_MULTIP), (int)(1.5 * shape.getMaxLength() * GameView.HEIGHT_MULTIP),
                    false);
        else
            image = Bitmap.createScaledBitmap(item.getImage(), (int)(1.5 * shape.getMaxLength() * GameView.HEIGHT_MULTIP), (int)(1.5 * shape.getMaxLength() * GameView.HEIGHT_MULTIP),
                    false);
    }

    public Bitmap getRotatedBitmap() {
        float angle;
        if (shape.getDirV().y > 0) {
            angle = (float)Math.toDegrees(Math.acos(shape.getDirV().x));
        } else {
            angle = (float)Math.toDegrees(2 * Math.PI - Math.acos(shape.getDirV().x));
        }

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);
    }

    @Override
    public void move() {
        super.move();

        pickTrigger.setPosV(posV);
    }

    public void count() {
        counter--;
    }

    @Override
    public boolean isDestroyed() {
        return counter <= 0;
    }

    public Circle getPickTrigger() {
        return pickTrigger;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    @Override
    public void draw(Canvas c, Paint p, float plX, float plY) {
        int alpha = 255;
        if (counter < 3000) alpha = 128 + (int)(Math.sin((float)(3000 - counter) / 60) * 127);

        p.setAlpha(alpha);

        c.drawBitmap(getRotatedBitmap(), (posV.x + plX - shape.getMaxLength()) * GameView.HEIGHT_MULTIP, (posV.y + plY - z - shape.getMaxLength()) * GameView.HEIGHT_MULTIP, p);

        p.setAlpha(255);

        //super.draw(c, p, plX, plY);

        //pickTrigger.draw(c, p, plX, plY - z);
    }
}
