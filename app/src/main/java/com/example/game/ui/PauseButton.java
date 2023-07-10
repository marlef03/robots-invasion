package com.example.game.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.example.game.MainActivity;
import com.example.game.core.GameView;

public class PauseButton extends FuncButton {
    private float formX1, formY1, formX2, formY2;
    private boolean onPause;
    private ProgressButton sfxVolumeButton, musicVolumeButton;
    private FuncButton exit;

    private Bitmap pauseI, returnI;


    public PauseButton(float centerX, float centerY, float radius) {
        super(centerX, centerY, radius);

        formX1 = 450 * GameView.WIDTH_MULTIP;
        formY1 = 250 * GameView.HEIGHT_MULTIP;
        formX2 = GameView.WIDTH - 450 * GameView.WIDTH_MULTIP;
        formY2 = GameView.HEIGHT - 100 * GameView.HEIGHT_MULTIP;

        pauseI = Bitmap.createScaledBitmap(MainActivity.pausebutton, (int)radius * 2, (int)radius * 2, false);
        returnI = Bitmap.createScaledBitmap(MainActivity.returnbutton, (int)radius * 2, (int)radius * 2, false);

        sfxVolumeButton = new ProgressButton(600 * GameView.WIDTH_MULTIP, 450 * GameView.HEIGHT_MULTIP,
                GameView.WIDTH - 600 * GameView.WIDTH_MULTIP, 500 * GameView.HEIGHT_MULTIP);
        musicVolumeButton = new ProgressButton(600 * GameView.WIDTH_MULTIP, 700 * GameView.HEIGHT_MULTIP,
                GameView.WIDTH - 600 * GameView.WIDTH_MULTIP, 750 * GameView.HEIGHT_MULTIP);

        exit = new FuncButton(700 * GameView.WIDTH_MULTIP, 800 * GameView.HEIGHT_MULTIP,
                GameView.WIDTH - 700 * GameView.WIDTH_MULTIP, 900 * GameView.HEIGHT_MULTIP);
        exit.setActive(true);
    }

    public FuncButton getExitButton() {
        return exit;
    }

    public ProgressButton getSfxVolumeButton() {
        return sfxVolumeButton;
    }

    public ProgressButton getMusicVolumeButton() {
        return musicVolumeButton;
    }

    public void setVolume() {
        MainActivity.setVolume(sfxVolumeButton.getPos(), musicVolumeButton.getPos());
    }

    public void setOnPause(boolean onPause) {
        this.onPause = onPause;
        if (onPause) {
            MainActivity.pauseAllSounds();
            MainActivity.playPause();
        }
        else {
            MainActivity.resumeAllSounds();
        }
    }

    public boolean getOnPause() {
        return onPause;
    }

    @Override
    public void click(float x, float y) {
        super.click(x, y);
        if (press) setOnPause(!onPause);
        press = false;
    }

    @Override
    public void draw(Canvas c, Paint p) {
        if (onPause) {
            c.drawBitmap(returnI, centerX - radius, centerY - radius, p);

            p.setColor(Color.YELLOW);
            p.setStyle(Paint.Style.FILL);
            c.drawRect(formX1, formY1, formX2, formY2, p);

            sfxVolumeButton.draw(c, p);
            musicVolumeButton.draw(c, p);

            p.setColor(Color.BLACK);
            p.setStyle(Paint.Style.FILL);
            exit.draw(c, p);

            p.setColor(Color.WHITE);
            p.setStyle(Paint.Style.FILL);
            p.setTextSize(140 * GameView.HEIGHT_MULTIP);
            p.setAntiAlias(true);
            p.setTypeface(MainActivity.font);

            c.drawText("Пауза", (float)GameView.WIDTH / 2 - 200 * GameView.HEIGHT_MULTIP, 160 * GameView.HEIGHT_MULTIP, p);

            p.setColor(Color.BLACK);

            p.setTextSize(50 * GameView.HEIGHT_MULTIP);
            c.drawText("Громкость звуковых эффектов", (float)GameView.WIDTH / 2 - 420 * GameView.HEIGHT_MULTIP, 330 * GameView.HEIGHT_MULTIP, p);

            p.setTextSize(60 * GameView.HEIGHT_MULTIP);
            c.drawText("Громкость музыки", (float)GameView.WIDTH / 2 - 300 * GameView.HEIGHT_MULTIP, 600 * GameView.HEIGHT_MULTIP, p);

            p.setColor(Color.WHITE);
            p.setTextSize(50 * GameView.HEIGHT_MULTIP);
            c.drawText("Выйти в меню", (float)GameView.WIDTH / 2 - 180 * GameView.HEIGHT_MULTIP, 870 * GameView.HEIGHT_MULTIP, p);
        }
        else {
            c.drawBitmap(pauseI, centerX - radius, centerY - radius, p);
        }

        p.setAntiAlias(false);
    }
}
