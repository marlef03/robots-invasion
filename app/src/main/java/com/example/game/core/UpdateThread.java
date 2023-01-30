package com.example.game.core;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class UpdateThread extends Thread {
    private final GameView surfV;
    private boolean tRun = false;

    public UpdateThread(GameView surfaceView) {
        surfV = surfaceView;
    }

    public void setRunning(boolean b) {
        tRun = b;
    }

    @Override
    public void run() {
        // super.run();

        long lastTime = System.nanoTime();
        double amountOfTicks = 200.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();

        while (tRun) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            while (delta >= 1) {
                surfV.update();

                delta--;
            }

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
            }
        }
    }
}
