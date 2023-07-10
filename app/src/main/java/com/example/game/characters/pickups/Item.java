package com.example.game.characters.pickups;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.game.core.GameView;
import com.example.game.species.Body;
import com.example.game.species.Circle;
import com.example.game.species.Shape;
import com.example.game.ui.FuncButton;
import com.example.game.ui.JoyStick;
import com.example.game.util.Vec;

public abstract class Item {
    private int mass;
    private float elasticity;
    private float l;
    private float maxHealth;
    protected Shape body;
    protected JoyStick attackInput;

    protected Bitmap image;

    public Item(Item i) {
        this.body = new Circle(0, 0, i.getBody().getR());

        this.l = i.getL();

        this.mass = i.getMass();
        this.elasticity = i.getElasticity();

        this.maxHealth = i.getMaxHealth();
    }

    public Item(Shape body, int mass, float elasticity, float l, float maxHealth) {
        this.body = body;

        this.l = l;

        this.mass = mass;
        this.elasticity = elasticity;

        this.maxHealth = maxHealth;
    }

    public Bitmap getImage() {
        return image;
    }

    public Bitmap getGroundImage() {
        return null;
    }

    public int getId() {
        return 0;
    }

    public void addAmmo(int ammo) {}

    public void nullCountToCoolDown() {}

    public float getMaxHealth() {
        return maxHealth;
    }

    public float getL() {
        return l;
    }

    public void countCoolDown() {}

    public Body getNextBullet(Vec pos, float z, Vec dir) {
        return null;
    }
    public Body getNextBullet(Vec pos, float z, Vec dir, Vec target, float targetZ) {
        return null;
    }

    public Body[] getNextBulletArray(Vec pos, float z, Vec dir) {
        return null;
    }
    public Body[] getNextBulletArray(Vec pos, float z, Vec dir, Vec target, float targetZ) {
        return null;
    }

    public int getMass() {
        return mass;
    }

    public float getElasticity() {
        return elasticity;
    }

    public Shape getBody() {
        return body;
    }

    public JoyStick getAttackInput() {
        return attackInput;
    }

    public FuncButton getExtraButton() {return null;}

    public abstract void draw(Canvas c, Paint p, float x1, float y1, float x2, float y2);

    public void drawDir(Canvas c, Paint p, Vec plPosV, float plX, float plY) {
        p.setColor(Color.GRAY);
        p.setStrokeWidth(5);
        c.drawLine((plPosV.x + plX) * GameView.HEIGHT_MULTIP, (plPosV.y + plY) * GameView.HEIGHT_MULTIP,
                (plPosV.x + attackInput.getXDir() * 500 * GameView.HEIGHT_MULTIP + plX) * GameView.HEIGHT_MULTIP,
                (plPosV.y + attackInput.getYDir() * 500 * GameView.HEIGHT_MULTIP + plY) * GameView.HEIGHT_MULTIP, p);
    }
}