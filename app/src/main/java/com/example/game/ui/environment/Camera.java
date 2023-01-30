package com.example.game.ui.environment;

public class Camera {
    private float[] point;
    private boolean freeCam;

    public void setPoint(float pointX, float pointY) {
        this.point = new float[]{pointX, pointY};
    }

    public float[] getPoint() {
        return point;
    }

    public void setFreeCam(boolean a) {
        freeCam = a;
    }

    public boolean getFreeCam() {
        return freeCam;
    }
}
