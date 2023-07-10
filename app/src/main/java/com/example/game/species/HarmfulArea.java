package com.example.game.species;

public class HarmfulArea extends Body {
    private int coolDown, countToCoolDown;

    public HarmfulArea(float x, float y, float z, float l, float r, float damage, int coolDown) {
        super(x, y, z, l, r, 0, 0, 0);

        this.damage = damage;

        this.coolDown = coolDown;

    }

    public int getCoolDown() {
        return coolDown;
    }

    public boolean getReady() {
        return countToCoolDown == 0;
    }

    public void count() {
        if (coolDown == 0) return;
        countToCoolDown++;
        if (countToCoolDown == coolDown) {
            countToCoolDown = 0;
        }
    }
}
