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
        while (tRun) {
            Canvas c = surfHolder.lockCanvas();
            if (c != null) {
                surfV.draw(c);
                surfHolder.unlockCanvasAndPost(c);
            }
        }
    }
}