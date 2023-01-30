package com.example.game.core;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.example.game.characters.Player;
import com.example.game.ui.environment.MapContainer;
import com.example.game.ui.JoyStick;
import com.example.game.ui.JumpButton;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private MyThread drawTr = null;

    public static int WIDTH = Resources.getSystem().getDisplayMetrics().widthPixels,
            HEIGHT = Resources.getSystem().getDisplayMetrics().heightPixels;
    public static final int ORIG_WIDTH = 1920;
    public static final int ORIG_HEIGHT = 1080;
    public static final float WIDTH_MULTIP = (float) WIDTH / (float) ORIG_WIDTH;
    public static final float HEIGHT_MULTIP = (float) HEIGHT / (float) ORIG_HEIGHT;

    private Paint paint = new Paint();

    private JoyStick js;

    private JumpButton jumpBut;

    private Player pl;

    private MapContainer mainMap;

    public GameView(Context context) {
        super(context);
        init();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        getHolder().addCallback(this);

        setFocusable(true);

        paint.setFlags(Paint.SUBPIXEL_TEXT_FLAG);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);

        js = new JoyStick(WIDTH - 300 * HEIGHT_MULTIP, HEIGHT - 300 * HEIGHT_MULTIP, 210 * HEIGHT_MULTIP, 90 * HEIGHT_MULTIP);
        jumpBut = new JumpButton(170 * HEIGHT_MULTIP, HEIGHT - 200 * HEIGHT_MULTIP, 110 * HEIGHT_MULTIP);

        pl = new Player(10, 10, 0, 120, 100);
        pl.setImageBitmap(getContext());

        mainMap = new MapContainer(0, 0, 20000, 20000, pl.getViewX(), pl.getViewY(), false);
        //mainMap.cam.setPoint(0, 0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction() & event.getActionMasked()) {
            case (MotionEvent.ACTION_DOWN):
            case (MotionEvent.ACTION_POINTER_DOWN):
                int ie = event.getPointerCount() - 1;
                if (js.onClick(event.getX(ie), event.getY(ie))) {
                    js.calc(event.getX(ie), event.getY(ie));
                    js.setId(event.getPointerId(ie));
                }

                jumpBut.press(event.getX(ie), event.getY(ie), pl);
                break;

            case (MotionEvent.ACTION_MOVE):
                boolean b = false;
                for (int i=0;i<event.getPointerCount();i++) {
                    if (js.getId() == event.getPointerId(i)) {
                        b = true;
                        if (js.onClick(event.getX(i), event.getY(i))) {
                            js.calc(event.getX(i), event.getY(i));
                        } else {
                            js.reset();
                        }
                    }
                }
                if (!b) {
                    js.reset();
                }
                break;
            case (MotionEvent.ACTION_POINTER_UP):
                break;
            case (MotionEvent.ACTION_UP):
                js.reset();
                break;

        }

        return true;
    }

    public void update() {
        //Log.d("Moder", String.valueOf(HEIGHT_MULTIP));

        //GAME LOGIC BEFORE ALL MOVING



        mainMap.allMoving(pl);

        pl.changeSpeed(js);

        js.translate();
    }

    @Override
    public void draw(Canvas c) {
        super.draw(c);

        //Environment(Under player)
        mainMap.drawUnder(c, paint, pl.getZ(), pl.getJumpSpeed(), pl.getX(), pl.getY());

        //Player
        pl.draw(c, paint, mainMap.cam.getFreeCam(), mainMap.cam.getPoint());

        //Environment(Above player)
        mainMap.drawAbove(c, paint, pl.getZ(), pl.getJumpSpeed(), pl.getX(), pl.getY());

        //UI
        js.draw(c, paint);
        jumpBut.draw(c, paint);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        drawTr = new MyThread(getHolder(), this);
        drawTr.setRunning(true);
        drawTr.start();

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        boolean retry = true;
        drawTr.setRunning(false);
        while (retry) {
            try {
                drawTr.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }
}