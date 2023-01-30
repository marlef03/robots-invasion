package com.example.game.core;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class MyThread extends Thread {

    private final SurfaceHolder surfHolder;
    private final GameView surfV;
    private boolean tRun = false;

    public MyThread(SurfaceHolder surfaceHolder, GameView surfaceView) {
        surfHolder = surfaceHolder;
        surfV = surfaceView;
    }

    public void setRunning(boolean b) {
        tRun = b;
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double nsPerTick = 1000000000D/60D;
        long lastTimer = System.currentTimeMillis();
        double delta = 0;

        while (tRun) {
            Canvas c = null;
            long now = System.nanoTime();
            delta += (now-lastTime)/nsPerTick;
            lastTime = now;
            boolean shouldRender = false;

            while (delta >= 1) {
                surfV.update();
                delta -= 1;
                shouldRender = true;
            }

            if (shouldRender) {
                c = surfHolder.lockCanvas();
                surfV.draw(c);
                surfHolder.unlockCanvasAndPost(c);
            }

            if (System.currentTimeMillis() - lastTimer >= 1000) {
                lastTimer += 1000;
            }
        }
    }
}