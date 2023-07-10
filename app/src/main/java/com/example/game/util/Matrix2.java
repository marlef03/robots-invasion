package com.example.game.util;

public class Matrix2 {
    public int rows, columns;
    public float[][] data;

    public Matrix2(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;

        data = new float[rows][columns];
    }

    public void rotate(double angle) {
        data[0][0] = (float)Math.cos(angle);
        data[0][1] = -(float)Math.sin(angle);
        data[1][0] = (float)Math.sin(angle);
        data[1][1] = (float)Math.cos(angle);
    }

    public Vec multyVec(Vec v) {
        Vec res = new Vec();
        res.x = data[0][0] * v.x + data[0][1] * v.y;
        res.y = data[1][0] * v.x + data[1][1] * v.y;

        return res;
    }
}