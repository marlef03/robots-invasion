package com.example.game.core;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.example.game.MainActivity;
import com.example.game.characters.Player;
import com.example.game.characters.pickups.ItemHolder;
import com.example.game.ui.FuncButton;
import com.example.game.ui.JoyStick;
import com.example.game.ui.PauseButton;
import com.example.game.ui.ShopUI;
import com.example.game.ui.environment.MapContainer;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private MyThread drawTr = null;
    private UpdateThread updTr = null;

    public static int WIDTH = Resources.getSystem().getDisplayMetrics().widthPixels,
            HEIGHT = Resources.getSystem().getDisplayMetrics().heightPixels;
    public static final int ORIG_WIDTH = 1920;
    public static final int ORIG_HEIGHT = 1080;
    public static final float WIDTH_MULTIP = (float) WIDTH / (float) ORIG_WIDTH;
    public static final float HEIGHT_MULTIP = (float) HEIGHT / (float) ORIG_HEIGHT;

    private Paint paint = new Paint();

    private JoyStick[] js;

    private FuncButton[] buttons;

    private ItemHolder itemHolder;

    private MapContainer mainMap;

    private FuncButton[] menuButtons;
    private boolean loading;

    public GameView(Context context) {
        super(context);
        init();
    }

    private void init() {
        getHolder().addCallback(this);

        setFocusable(true);

        menuButtons = new FuncButton[] {
                new FuncButton((float)WIDTH / 2 - 250 * HEIGHT_MULTIP, (float)HEIGHT / 2,
                        (float)WIDTH / 2 + 250 * HEIGHT_MULTIP, (float)HEIGHT / 2 + 160 * HEIGHT_MULTIP),
                new FuncButton((float)WIDTH / 2 - 250 * HEIGHT_MULTIP, (float)HEIGHT / 2 + 200 * HEIGHT_MULTIP,
                        (float)WIDTH / 2 + 250 * HEIGHT_MULTIP, (float)HEIGHT / 2 + 380 * HEIGHT_MULTIP)
        };
        menuButtons[0].setActive(true);
        menuButtons[1].setActive(true);
    }

    private void restartMap() {
        MainActivity.writeFile("survival_map.txt",
                "0 0 3000 3000 0\n" +
                        "brn\n" +
                        "bre\n" +
                        "brs\n" +
                        "brw\n" +
                        "shop\n" +
                        "pl 1500 1500 50 100 0 0 0 1000\n" +
                        "pli 1 mel"
                , getContext());
    }

    private void mapInit() {
        loading = true;

        menuButtons[0].setActive(false);
        menuButtons[1].setActive(false);

        buttons = new FuncButton[6];

        buttons[0] = new FuncButton(250 * HEIGHT_MULTIP, 500 * HEIGHT_MULTIP, 100 * HEIGHT_MULTIP);
        buttons[0].setActive(true);
        buttons[0].setImage(MainActivity.jumpbutton);
        buttons[1] = new FuncButton(530 * HEIGHT_MULTIP, HEIGHT - 315 * HEIGHT_MULTIP, 90 * HEIGHT_MULTIP);

        buttons[4] = new PauseButton(WIDTH - 60 * HEIGHT_MULTIP, 60 * HEIGHT_MULTIP, 60 * HEIGHT_MULTIP);
        buttons[4].setActive(true);

        buttons[5] = new ShopUI(WIDTH - 60 * HEIGHT_MULTIP, HEIGHT - 60 * HEIGHT_MULTIP, 60 * HEIGHT_MULTIP);

        js = new JoyStick[2];
        js[0] = new JoyStick(WIDTH - 230 * HEIGHT_MULTIP, HEIGHT - 230 * HEIGHT_MULTIP, 200 * HEIGHT_MULTIP, 80 * HEIGHT_MULTIP);
        js[0].setStickImage(Bitmap.createScaledBitmap(MainActivity.movejoystickstick, (int)(160 * HEIGHT_MULTIP), (int)(160 * HEIGHT_MULTIP), false));

        mainMap = new MapContainer(MainActivity.readFile("survival_map.txt", getContext()));

        itemHolder = mainMap.pl.getItemHolder();

        loading = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int ie = event.getActionIndex();

        switch (event.getAction() & event.getActionMasked()) {
            case (MotionEvent.ACTION_DOWN):
            case (MotionEvent.ACTION_POINTER_DOWN):
                if (mainMap != null && !mainMap.gameOver) {
                    if (!((PauseButton) buttons[4]).getOnPause()) {
                        if (!buttons[5].getActive()) {
                            for (int i = 0; i < js.length; i++)
                                if (js[i] != null)
                                    if (js[i].onClick(event.getX(ie), event.getY(ie))) {
                                        js[i].calc(event.getX(ie), event.getY(ie));

                                        js[i].setHold(true);
                                    }

                            if (buttons[2] != null) {
                                if (buttons[2].onClick(event.getX(ie), event.getY(ie)))
                                    buttons[2].setPress(true);

                                if (buttons[3].onClick(event.getX(ie), event.getY(ie)))
                                    buttons[3].setPress(true);
                            }
                        }
                        else buttons[5].click(event.getX(ie), event.getY(ie));
                    }
                    else {
                        ((PauseButton) buttons[4]).getSfxVolumeButton().onClick(event.getX(ie), event.getY(ie));
                        ((PauseButton) buttons[4]).getMusicVolumeButton().onClick(event.getX(ie), event.getY(ie));
                        ((PauseButton) buttons[4]).setVolume();
                        if (((PauseButton) buttons[4]).getExitButton().onClick(event.getX(ie), event.getY(ie))) {
                            mainMap.gameOver = true;
                        }
                    }
                    buttons[4].click(event.getX(ie), event.getY(ie));
                }
                else {
                    if (menuButtons[0].onClick(event.getX(ie), event.getY(ie))) {
                        if (!MainActivity.fileExists("survival_map.txt", getContext())) restartMap();
                        mapInit();
                    }
                    else if (menuButtons[1].onClick(event.getX(ie), event.getY(ie))) {
                        restartMap();
                        mapInit();
                    }
                }
                break;

            case (MotionEvent.ACTION_MOVE):
                if (mainMap != null && !mainMap.gameOver) {
                    if (!((PauseButton) buttons[4]).getOnPause() && !buttons[5].getActive()) {
                        for (int j = 0; j < js.length; j++) {
                            if (js[j] != null) {
                                js[j].setHold(false);

                                for (int i = 0; i < event.getPointerCount(); i++)
                                    if (js[j].onClick(event.getX(i), event.getY(i))) {
                                        js[j].calc(event.getX(i), event.getY(i));

                                        js[j].setHold(true);
                                    }
                            }
                        }

                        if (buttons[2] != null) {
                            buttons[2].setPress(false);
                            buttons[3].setPress(false);
                        }

                        for (int i = 0; i < event.getPointerCount(); i++) {
                            if (buttons[2] != null) {
                                if (buttons[2].onClick(event.getX(i), event.getY(i))) {
                                    buttons[2].setPress(true);
                                }

                                if (buttons[3].onClick(event.getX(i), event.getY(i))) {
                                    buttons[3].setPress(true);
                                }
                            }
                        }
                    } else if (((PauseButton) buttons[4]).getOnPause()) {
                        for (int i = 0; i < event.getPointerCount(); i++) {
                            ((PauseButton) buttons[4]).getSfxVolumeButton().onClick(event.getX(i), event.getY(i));
                            ((PauseButton) buttons[4]).getMusicVolumeButton().onClick(event.getX(i), event.getY(i));
                        }
                        ((PauseButton) buttons[4]).setVolume();
                    }
                }

                break;

            case (MotionEvent.ACTION_UP):
            case (MotionEvent.ACTION_POINTER_UP):
                if (mainMap != null && !mainMap.gameOver) {
                    if (!((PauseButton) buttons[4]).getOnPause() && !buttons[5].getActive()) {
                        for (int i = 0; i < js.length; i++)
                            if (js[i] != null)
                                if (js[i].onClick(event.getX(ie), event.getY(ie))) {
                                    js[i].setHold(false);

                                    if (i == 1) js[i].setClick(true);
                                }

                        if (buttons[2] != null) {
                            if (buttons[2].onClick(event.getX(ie), event.getY(ie)))
                                buttons[2].setPress(false);

                            if (buttons[3].onClick(event.getX(ie), event.getY(ie)))
                                buttons[3].setPress(false);
                        }

                        if (buttons[0] != null)
                            buttons[0].click(event.getX(ie), event.getY(ie));

                        if (buttons[1] != null)
                            buttons[1].click(event.getX(ie), event.getY(ie));

                        if (itemHolder != null) {
                            if (itemHolder.onClick(event.getX(ie), event.getY(ie))) {
                                itemHolder.functional(event.getX(ie));
                            }

                            if (itemHolder.getExtraButton() != null)
                                itemHolder.getExtraButton().click(event.getX(ie), event.getY(ie));
                        }
                    }
                }
        }

        return true;
    }

    private void toMenu() {
        if (mainMap != null) {
            synchronized (mainMap.entities) {
                loading = true;

                if (mainMap.pl.getHealth() <= 0) restartMap();
                else MainActivity.writeFile("survival_map.txt", save(), getContext());

                mainMap.nullize();

                if (mainMap.nullized()) {
                    mainMap = null;
                }
                menuButtons[0].setActive(true);
                menuButtons[1].setActive(true);

                loading = false;

                MainActivity.resumeMusic();
            }
        }
    }

    public void setPause() {
        ((PauseButton) buttons[4]).setOnPause(true);
    }

    public String save() {
        return mainMap.serializeMap();
    }

    public void update() {
        if (mainMap != null) {
            if (!mainMap.gameOver) {
                if (!((PauseButton) buttons[4]).getOnPause()) {
                    synchronized (mainMap.entities) {
                        mainMap.inputHolder(js[0]);

                        mainMap.gameLogic(buttons, js);

                        if (!mainMap.gameOver) {
                            mainMap.allMoving();

                            if (itemHolder != null && itemHolder.getActive()) {
                                itemHolder.getCur().countCoolDown();

                                js[1] = itemHolder.getCur().getAttackInput();
                            } else js[1] = null;
                        }
                    }
                }
            }
            else toMenu();
        }
    }

    @Override
    public void draw(Canvas c) {
        if (!loading) {
            if (mainMap != null && !mainMap.gameOver) {
                synchronized (mainMap.entities) {
                    super.draw(c);

                    c.drawRGB(159, 155, 142);

                    if (mainMap != null) {
                        mainMap.draw(c, paint);

                        for (int i = 0; i < js.length; i++)
                            if (js[i] != null) {
                                if (!js[i].getClick() && !js[i].getHold()) js[i].reset();
                                js[i].translate();
                            }

                        for (int i = 0; i < js.length; i++)
                            if (js[i] != null) js[i].draw(c, paint);

                        for (int i = 0; i < buttons.length - 2; i++) {
                            if (buttons[i] != null) buttons[i].draw(c, paint);
                        }

                        if (itemHolder != null) {
                            itemHolder.draw(c, paint, mainMap.getPlayerPos(), mainMap.getPlayerZ(), -mainMap.cam.getScreenEdgePos().x, -mainMap.cam.getScreenEdgePos().y);

                            if (itemHolder.getExtraButton() != null)
                                itemHolder.getExtraButton().draw(c, paint);
                        }

                        if (buttons[5] != null) buttons[5].draw(c, paint);

                        if (((PauseButton) buttons[4]).getOnPause()) c.drawARGB(150, 0, 0, 0);

                        if (buttons[4] != null) buttons[4].draw(c, paint);
                    }
                }
            } else {
                c.drawBitmap(Bitmap.createScaledBitmap(MainActivity.menuScreen, (int)(ORIG_WIDTH * HEIGHT_MULTIP), HEIGHT, false), (float)WIDTH / 2 - ORIG_WIDTH * HEIGHT_MULTIP / 2,
                        0, paint);

                paint.setStyle(Paint.Style.FILL);
                paint.setColor(Color.WHITE);
                for (int i = 0; i < menuButtons.length; i++) {
                    menuButtons[i].draw(c, paint);
                }

                paint.setStyle(Paint.Style.FILL);
                paint.setColor(Color.BLACK);
                paint.setTextSize(50 * GameView.HEIGHT_MULTIP);
                paint.setAntiAlias(true);
                paint.setTypeface(MainActivity.font);

                c.drawText("Продолжить", (float) WIDTH / 2 - paint.measureText("Продолжить") / 2, (float) HEIGHT / 2 + 70 * GameView.HEIGHT_MULTIP, paint);
                c.drawText("игру", (float) WIDTH / 2 - paint.measureText("игру") / 2, (float) HEIGHT / 2 + 130 * GameView.HEIGHT_MULTIP, paint);

                c.drawText("Начать", (float) WIDTH / 2 - paint.measureText("Начать") / 2, (float) HEIGHT / 2 + 270 * GameView.HEIGHT_MULTIP, paint);
                c.drawText("новую игру", (float) WIDTH / 2 - paint.measureText("новую игру") / 2, (float) HEIGHT / 2 + 330 * GameView.HEIGHT_MULTIP, paint);
            }
        }
        else {
            c.drawBitmap(Bitmap.createScaledBitmap(MainActivity.loadingScreen, (int)(ORIG_WIDTH * HEIGHT_MULTIP), HEIGHT, false), (float)WIDTH / 2 - ORIG_WIDTH * HEIGHT_MULTIP / 2,
                    0, paint);

            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.BLACK);
            paint.setTextSize(80 * (float) Math.sqrt(HEIGHT_MULTIP));
            paint.setAntiAlias(true);
            paint.setTypeface(MainActivity.font);

            c.drawText("Загрузка...",WIDTH - paint.measureText("Загрузка...") - 50 * HEIGHT_MULTIP, (float) HEIGHT - 100 * HEIGHT_MULTIP, paint);
        }
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        if (updTr == null || !updTr.isAlive()) {
            updTr = new UpdateThread(this);
            updTr.setRunning(true);
            updTr.start();
        }
        if (drawTr == null || !drawTr.isAlive()) {
            drawTr = new MyThread(getHolder(), this);
            drawTr.setRunning(true);
            drawTr.start();
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        boolean retry = true;
        updTr.setRunning(false);
        while (retry) {
            try {
                updTr.join();
                retry = false;
            } catch (InterruptedException ignored) {
            }
        }

        retry = true;
        drawTr.setRunning(false);
        while (retry) {
            try {
                drawTr.join();
                retry = false;
            } catch (InterruptedException ignored) {
            }
        }
    }
}