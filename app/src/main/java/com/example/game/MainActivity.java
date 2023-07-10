package com.example.game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.game.core.GameView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    public static Bitmap pistol, shotgun, machinegun, bomb, bombGround, contactbomb, stickybomb, melee, meleeGround, medkit, money, pistolbullet, pausebutton,
            returnbutton, itemholder, jumpbutton, pickupbutton, dropbutton, shopbutton, detonatebutton, healbutton, attackjoystickstick, movejoystickstick, menuScreen, loadingScreen;
    public static Bitmap[][] characterWalkFrames, characterPistolWalkFrames, characterMachineGunWalkFrames, characterShotGunWalkFrames, characterMeleeWalkFrames,
            characterBombWalkFrames, characterMedKitWalkFrames, characterContactBombWalkFrames, characterStickyBombWalkFrames, robotWalkFrames;
    public static Bitmap[][] floor, shop;
    public static Bitmap[][][] explosionFrames;
    public static Bitmap[] portals, bombAnimation, meleeWave;
    public static Bitmap[][] housingNorth, housingEast, housingSouth, housingWest;

    public static Typeface font;

    private GameView view;

    private static SoundPool sp;
    private static MediaPlayer mp;
    public static float sfxVolume = 1, musicVolume = 1;

    private static int explosionId, pistolShotId, shotGunShotId, meleeShotId, enemyJumperId, jumpId, landingId, stepsId, robostepsId, pauseId, moneygetId, moneyspentId,
            failureId, bombId, bombThrowId, stickyBombId, healId, newWaveId;
    private static boolean robostepsready;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        font = Typeface.createFromAsset(getAssets(),"fonts/main_font.ttf");

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sp = new SoundPool(100, AudioManager.STREAM_MUSIC, 0);
        mp = MediaPlayer.create(this, R.raw.regularmusic);

        explosionId = sp.load(this, R.raw.explosion, 1);
        pistolShotId = sp.load(this, R.raw.pistolshot, 1);
        shotGunShotId = sp.load(this, R.raw.shotgunshot, 1);
        meleeShotId = sp.load(this, R.raw.meleeshot, 1);
        enemyJumperId = sp.load(this, R.raw.enemyjumper, 1);
        jumpId = sp.load(this, R.raw.jump, 1);
        landingId = sp.load(this, R.raw.landing, 1);
        stepsId = sp.load(this, R.raw.steps, 1);
        robostepsId = sp.load(this, R.raw.robosteps, 1);
        robostepsready = false;
        pauseId = sp.load(this, R.raw.pause, 1);
        moneygetId = sp.load(this, R.raw.moneyget, 1);
        moneyspentId = sp.load(this, R.raw.moneyspent, 1);
        failureId = sp.load(this, R.raw.failure, 1);
        bombId = sp.load(this, R.raw.bomb, 1);
        bombThrowId = sp.load(this, R.raw.bombthrow, 1);
        stickyBombId = sp.load(this, R.raw.stickybomb, 1);
        healId = sp.load(this, R.raw.heal, 1);
        newWaveId = sp.load(this, R.raw.newwave, 1);

        sp.setOnLoadCompleteListener((soundPool, sound, complete) -> {
            if (sound == robostepsId && complete == 0) robostepsready = true;
        });

        pistol = BitmapFactory.decodeResource(getResources(), R.drawable.pistol_icon);
        shotgun = BitmapFactory.decodeResource(getResources(), R.drawable.sgun_icon);
        machinegun = BitmapFactory.decodeResource(getResources(), R.drawable.mgun_icon);
        bomb = BitmapFactory.decodeResource(getResources(), R.drawable.bomb_icon);
        contactbomb = BitmapFactory.decodeResource(getResources(), R.drawable.cbomb_icon);
        stickybomb = BitmapFactory.decodeResource(getResources(), R.drawable.sbomb_icon);
        melee = BitmapFactory.decodeResource(getResources(), R.drawable.melee_icon);
        medkit = BitmapFactory.decodeResource(getResources(), R.drawable.med_icon);
        money = BitmapFactory.decodeResource(getResources(), R.drawable.money);
        pistolbullet = BitmapFactory.decodeResource(getResources(), R.drawable.pistol_bullet);

        bombGround = BitmapFactory.decodeResource(getResources(), R.drawable.bomb_icon_ground);
        meleeGround = BitmapFactory.decodeResource(getResources(), R.drawable.melee_icon_ground);

        pausebutton = BitmapFactory.decodeResource(getResources(), R.drawable.pause_button);
        returnbutton = BitmapFactory.decodeResource(getResources(), R.drawable.return_button);
        itemholder = BitmapFactory.decodeResource(getResources(), R.drawable.selectbar);
        jumpbutton = BitmapFactory.decodeResource(getResources(), R.drawable.jumpbutton);
        attackjoystickstick = BitmapFactory.decodeResource(getResources(), R.drawable.attackjoystick_stick);
        movejoystickstick = BitmapFactory.decodeResource(getResources(), R.drawable.movejoystick_stick);
        pickupbutton = BitmapFactory.decodeResource(getResources(), R.drawable.pickupbutton);
        dropbutton = BitmapFactory.decodeResource(getResources(), R.drawable.dropbutton);
        shopbutton = BitmapFactory.decodeResource(getResources(), R.drawable.shopbutton);
        detonatebutton = BitmapFactory.decodeResource(getResources(), R.drawable.detonatebutton);
        healbutton = BitmapFactory.decodeResource(getResources(), R.drawable.healbutton);
        menuScreen = BitmapFactory.decodeResource(getResources(), R.drawable.menu_screen);
        loadingScreen = BitmapFactory.decodeResource(getResources(), R.drawable.loading_screen);

        Bitmap f = BitmapFactory.decodeResource(getResources(), R.drawable.floorj);
        floor = new Bitmap[15][15];
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                floor[i][j] = f;
            }
        }

        housingNorth = new Bitmap[][] {
                new Bitmap[] {
                        null,
                        null,
                        null,
                        null,
                        null
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_7),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_3)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_7),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_3)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_7),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_4)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_7),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_3)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_7),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_3)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_7),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_4)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_7),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_3)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_7),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_3)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_7),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_3)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_7),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_4)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_7),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_3)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_7),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_3)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_7),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_4)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_7),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_3)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_7),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_north_3)
                }
        };

        housingWest = new Bitmap[][] {
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_roof_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_roof_2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_roof_3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_roof_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_roof_3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_roof_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_roof_3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_roof_2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_roof_3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_roof_2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_9),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_6),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_6),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_6),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_6),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_6),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_6),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_6),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_6),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_6),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_6),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_6),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_6),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_6),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_6),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_6),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_6),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_6),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_6),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_6),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_11)
                }
        };

        housingEast = new Bitmap[][] {
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_10),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_8),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_8),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_8),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_8),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_8),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_8),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_8),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_8),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_8),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_8),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_8),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_8),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_8),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_8),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_8),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_8),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_8),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_8),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_8),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_12)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_roof_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_roof_2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_roof_3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_roof_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_roof_3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_roof_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_roof_3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_roof_2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_roof_3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_roof_2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile)
                }
        };

        housingSouth = new Bitmap[][] {
                new Bitmap[] {
                        null,
                        null,
                        null
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_5),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_roof_2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_5),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_5),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_5),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_roof_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_5),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_5),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_roof_3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_5),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_5),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_roof_2)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_5),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_5),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_5),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_roof_2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_5),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_5),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_roof_4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_5),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_ceiling_5),
                        BitmapFactory.decodeResource(getResources(), R.drawable.housing_roof_3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.roof_tile)
                }
        };

        shop = new Bitmap[][] {
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.shop_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.shop_6),
                        BitmapFactory.decodeResource(getResources(), R.drawable.shop_11),
                        BitmapFactory.decodeResource(getResources(), R.drawable.shop_16),
                        BitmapFactory.decodeResource(getResources(), R.drawable.shop_21)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.shop_2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.shop_7),
                        BitmapFactory.decodeResource(getResources(), R.drawable.shop_12),
                        BitmapFactory.decodeResource(getResources(), R.drawable.shop_17),
                        BitmapFactory.decodeResource(getResources(), R.drawable.shop_22)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.shop_3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.shop_8),
                        BitmapFactory.decodeResource(getResources(), R.drawable.shop_13),
                        BitmapFactory.decodeResource(getResources(), R.drawable.shop_18),
                        BitmapFactory.decodeResource(getResources(), R.drawable.shop_23)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.shop_4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.shop_9),
                        BitmapFactory.decodeResource(getResources(), R.drawable.shop_14),
                        BitmapFactory.decodeResource(getResources(), R.drawable.shop_19),
                        BitmapFactory.decodeResource(getResources(), R.drawable.shop_24)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.shop_5),
                        BitmapFactory.decodeResource(getResources(), R.drawable.shop_10),
                        BitmapFactory.decodeResource(getResources(), R.drawable.shop_15),
                        BitmapFactory.decodeResource(getResources(), R.drawable.shop_20),
                        BitmapFactory.decodeResource(getResources(), R.drawable.shop_25)
                }
        };

        bombAnimation = new Bitmap[] {
                BitmapFactory.decodeResource(getResources(), R.drawable.bomb_animation_1),
                BitmapFactory.decodeResource(getResources(), R.drawable.bomb_animation_2),
                BitmapFactory.decodeResource(getResources(), R.drawable.bomb_animation_3),
                BitmapFactory.decodeResource(getResources(), R.drawable.bomb_animation_4),
                BitmapFactory.decodeResource(getResources(), R.drawable.bomb_animation_5),
                BitmapFactory.decodeResource(getResources(), R.drawable.bomb_animation_6),
                BitmapFactory.decodeResource(getResources(), R.drawable.bomb_animation_7),
                BitmapFactory.decodeResource(getResources(), R.drawable.bomb_animation_8),
                BitmapFactory.decodeResource(getResources(), R.drawable.bomb_animation_9),
                BitmapFactory.decodeResource(getResources(), R.drawable.bomb_animation_10),
                BitmapFactory.decodeResource(getResources(), R.drawable.bomb_animation_11)
        };

        meleeWave = new Bitmap[] {
                BitmapFactory.decodeResource(getResources(), R.drawable.melee_wave_1),
                BitmapFactory.decodeResource(getResources(), R.drawable.melee_wave_2),
                BitmapFactory.decodeResource(getResources(), R.drawable.melee_wave_3),
                BitmapFactory.decodeResource(getResources(), R.drawable.melee_wave_4),
                BitmapFactory.decodeResource(getResources(), R.drawable.melee_wave_5)
        };

        portals = new Bitmap[] {
                BitmapFactory.decodeResource(getResources(), R.drawable.portal_blue),
                BitmapFactory.decodeResource(getResources(), R.drawable.portal_orange)
        };

        explosionFrames = new Bitmap[][][] {
                new Bitmap[][] {
                        new Bitmap[] {
                                null,
                                null,
                                null,
                                null,
                                null
                        },
                        new Bitmap[] {
                                null,
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_1_6),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_1_10),
                                null,
                                null
                        },
                        new Bitmap[] {
                                null,
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_1_7),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_1_11),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_1_15),
                                null
                        },
                        new Bitmap[] {
                                null,
                                null,
                                null,
                                null,
                                null
                        }
                },
                new Bitmap[][] {
                        new Bitmap[] {
                                null,
                                null,
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_2_9),
                                null,
                                null
                        },
                        new Bitmap[] {
                                null,
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_2_6),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_2_10),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_2_14),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_2_18)
                        },
                        new Bitmap[] {
                                null,
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_2_7),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_2_11),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_2_15),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_2_19)
                        },
                        new Bitmap[] {
                                null,
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_2_8),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_2_12),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_2_16),
                                null
                        }
                },
                new Bitmap[][] {
                        new Bitmap[] {
                                null,
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_3_5),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_3_9),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_3_13),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_3_17)
                        },
                        new Bitmap[] {
                                null,
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_3_6),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_3_10),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_3_14),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_3_18)
                        },
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_3_3),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_3_7),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_3_11),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_3_15),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_3_19)
                        },
                        new Bitmap[] {
                                null,
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_3_8),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_3_12),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_3_16),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_3_20)
                        }
                },
                new Bitmap[][] {
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_4_1),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_4_5),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_4_9),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_4_13),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_4_17)
                        },
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_4_2),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_4_6),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_4_10),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_4_14),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_4_18)
                        },
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_4_3),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_4_7),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_4_11),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_4_15),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_4_19)
                        },
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_4_4),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_4_8),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_4_12),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_4_16),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_4_20)
                        }
                },
                new Bitmap[][] {
                        new Bitmap[] {
                                null,
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_5_5),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_5_9),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_5_13),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_5_17)
                        },
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_5_2),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_5_6),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_5_10),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_5_14),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_5_18)
                        },
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_5_3),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_5_7),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_5_11),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_5_15),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_5_19)
                        },
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_5_4),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_5_8),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_5_12),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_5_16),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_5_20)
                        }
                },
                new Bitmap[][] {
                        new Bitmap[] {
                                null,
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_6_5),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_6_9),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_6_13),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_6_17)
                        },
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_6_2),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_6_6),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_6_10),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_6_14),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_6_18)
                        },
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_6_3),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_6_7),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_6_11),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_6_15),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_6_19)
                        },
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_6_4),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_6_8),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_6_12),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_6_16),
                                null
                        }
                },
                new Bitmap[][] {
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_7_1),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_7_5),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_7_9),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_7_13),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_7_17)
                        },
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_7_2),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_7_6),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_7_10),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_7_14),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_7_18)
                        },
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_7_3),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_7_7),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_7_11),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_7_15),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_7_19)
                        },
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_7_4),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_7_8),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_7_12),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_7_16),
                                null
                        }
                },
                new Bitmap[][] {
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_8_1),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_8_5),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_8_9),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_8_13),
                                null
                        },
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_8_2),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_8_6),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_8_10),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_8_14),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_8_18)
                        },
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_8_3),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_8_7),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_8_11),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_8_15),
                                null
                        },
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_8_4),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_8_8),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_8_12),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_8_16),
                                null
                        }
                },
                new Bitmap[][] {
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_9_1),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_9_5),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_9_9),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_9_13),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_9_17)
                        },
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_9_2),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_9_6),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_9_10),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_9_14),
                                null
                        },
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_9_3),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_9_7),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_9_11),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_9_15),
                                null
                        },
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_9_4),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_9_8),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_9_12),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_9_16),
                                null
                        }
                },
                new Bitmap[][] {
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_10_1),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_10_5),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_10_9),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_10_13),
                                null
                        },
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_10_2),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_10_6),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_10_10),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_10_14),
                                null
                        },
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_10_3),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_10_7),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_10_11),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_10_15),
                                null
                        },
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_10_4),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_10_8),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_10_12),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_10_16),
                                null
                        }
                },
                new Bitmap[][] {
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_11_1),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_11_5),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_11_9),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_11_13),
                                null
                        },
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_11_2),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_11_6),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_11_10),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_11_14),
                                null
                        },
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_11_3),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_11_7),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_11_11),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_11_15),
                                null
                        },
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_11_4),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_11_8),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_11_12),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_11_16),
                                null
                        }
                },
                new Bitmap[][] {
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_12_1),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_12_5),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_12_9),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_12_13),
                                null
                        },
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_12_2),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_12_6),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_12_10),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_12_14),
                                null
                        },
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_12_3),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_12_7),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_12_11),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_12_15),
                                null
                        },
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_12_4),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_12_8),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_12_12),
                                null,
                                null
                        }
                },
                new Bitmap[][] {
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_13_1),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_13_5),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_13_9),
                                null,
                                null
                        },
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_13_2),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_13_6),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_13_10),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_13_14),
                                null
                        },
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_13_3),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_13_7),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_13_11),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_13_15),
                                null
                        },
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_13_4),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_13_8),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_13_12),
                                null,
                                null
                        }
                },
                new Bitmap[][] {
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_14_1),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_14_5),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_14_9),
                                null,
                                null
                        },
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_14_2),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_14_6),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_14_10),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_14_14),
                                null
                        },
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_14_3),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_14_7),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_14_11),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_14_15),
                                null
                        },
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_14_4),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_14_8),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_14_12),
                                null,
                                null
                        }
                },
                new Bitmap[][] {
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_15_1),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_15_5),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_15_9),
                                null,
                                null
                        },
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_15_2),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_15_6),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_15_10),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_15_14),
                                null
                        },
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_15_3),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_15_7),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_15_11),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_15_15),
                                null
                        },
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_15_4),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_15_8),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_15_12),
                                null,
                                null
                        }
                },
                new Bitmap[][] {
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_16_1),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_16_5),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_16_9),
                                null,
                                null
                        },
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_16_2),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_16_6),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_16_10),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_16_14),
                                null
                        },
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_16_3),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_16_7),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_16_11),
                                null,
                                null
                        },
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_16_4),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_16_8),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_16_12),
                                null,
                                null
                        }
                },
                new Bitmap[][] {
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_17_1),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_17_5),
                                null,
                                null,
                                null
                        },
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_17_2),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_17_6),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_17_10),
                                null,
                                null
                        },
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_17_3),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_17_7),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_17_11),
                                null,
                                null
                        },
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_17_4),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_17_8),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_17_12),
                                null,
                                null
                        }
                },
                new Bitmap[][] {
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_18_1),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_18_5),
                                null,
                                null,
                                null
                        },
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_18_2),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_18_6),
                                null,
                                null,
                                null
                        },
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_18_3),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_18_7),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_18_11),
                                null,
                                null
                        },
                        new Bitmap[] {
                                null,
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_18_8),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_18_12),
                                null,
                                null
                        }
                },
                new Bitmap[][] {
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_19_1),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_19_5),
                                null,
                                null,
                                null
                        },
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_19_2),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_19_6),
                                null,
                                null,
                                null
                        },
                        new Bitmap[] {
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_19_3),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_19_7),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_19_11),
                                null,
                                null
                        },
                        new Bitmap[] {
                                null,
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_19_8),
                                BitmapFactory.decodeResource(getResources(), R.drawable.explosion_19_12),
                                null,
                                null
                        }
                },
        };

        characterWalkFrames = new Bitmap[][] {
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_idle_1)
                }
        };

        characterMedKitWalkFrames = new Bitmap[][] {
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_med_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_med_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_med_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_med_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_med_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_med_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_med_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_med_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_med_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_med_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_med_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_med_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_med_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_med_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_med_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_med_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_med_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_med_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_med_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_med_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_med_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_med_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_med_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_med_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_med_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_med_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_med_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_med_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_med_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_med_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_med_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_med_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_med_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_med_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_med_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_med_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_med_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_med_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_med_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_med_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_med_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_med_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_med_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_med_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_med_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_med_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_med_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_med_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_med_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_med_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_med_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_med_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_med_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_med_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_med_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_med_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_med_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_med_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_med_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_med_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_med_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_med_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_med_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_med_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_med_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_med_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_med_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_med_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_med_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_med_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_med_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_med_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_med_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_med_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_med_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_med_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_med_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_med_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_med_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_med_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_med_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_med_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_med_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_med_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_med_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_med_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_med_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_med_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_med_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_med_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_med_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_med_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_med_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_med_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_med_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_med_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_med_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_med_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_med_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_med_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_med_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_med_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_med_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_med_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_med_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_med_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_med_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_med_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_med_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_med_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_med_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_med_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_med_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_med_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_med_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_med_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_med_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_med_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_med_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_med_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_med_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_med_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_med_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_med_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_med_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_med_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_med_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_med_idle_1)
                }
        };

        characterBombWalkFrames = new Bitmap[][] {
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_bomb_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_bomb_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_bomb_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_bomb_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_bomb_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_bomb_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_bomb_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_bomb_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_bomb_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_bomb_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_bomb_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_bomb_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_bomb_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_bomb_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_bomb_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_bomb_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_bomb_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_bomb_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_bomb_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_bomb_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_bomb_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_bomb_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_bomb_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_bomb_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_bomb_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_bomb_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_bomb_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_bomb_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_bomb_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_bomb_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_bomb_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_bomb_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_bomb_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_bomb_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_bomb_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_bomb_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_bomb_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_bomb_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_bomb_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_bomb_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_bomb_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_bomb_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_bomb_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_bomb_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_bomb_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_bomb_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_bomb_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_bomb_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_bomb_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_bomb_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_bomb_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_bomb_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_bomb_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_bomb_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_bomb_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_bomb_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_bomb_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_bomb_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_bomb_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_bomb_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_bomb_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_bomb_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_bomb_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_bomb_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_bomb_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_bomb_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_bomb_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_bomb_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_bomb_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_bomb_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_bomb_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_bomb_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_bomb_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_bomb_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_bomb_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_bomb_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_bomb_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_bomb_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_bomb_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_bomb_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_bomb_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_bomb_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_bomb_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_bomb_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_bomb_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_bomb_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_bomb_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_bomb_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_bomb_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_bomb_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_bomb_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_bomb_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_bomb_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_bomb_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_bomb_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_bomb_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_bomb_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_bomb_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_bomb_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_bomb_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_bomb_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_bomb_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_bomb_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_bomb_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_bomb_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_bomb_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_bomb_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_bomb_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_bomb_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_bomb_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_bomb_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_bomb_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_bomb_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_bomb_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_bomb_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_bomb_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_bomb_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_bomb_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_bomb_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_bomb_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_bomb_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_bomb_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_bomb_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_bomb_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_bomb_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_bomb_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_bomb_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_bomb_idle_1)
                }
        };

        characterMeleeWalkFrames = new Bitmap[][] {
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_melee_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_melee_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_melee_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_melee_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_melee_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_melee_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_melee_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_melee_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_melee_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_melee_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_melee_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_melee_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_melee_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_melee_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_melee_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_melee_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_melee_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_melee_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_melee_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_melee_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_melee_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_melee_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_melee_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_melee_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_melee_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_melee_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_melee_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_melee_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_melee_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_melee_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_melee_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_melee_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_melee_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_melee_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_melee_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_melee_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_melee_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_melee_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_melee_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_melee_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_melee_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_melee_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_melee_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_melee_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_melee_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_melee_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_melee_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_melee_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_melee_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_melee_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_melee_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_melee_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_melee_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_melee_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_melee_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_melee_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_melee_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_melee_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_melee_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_melee_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_melee_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_melee_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_melee_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_melee_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_melee_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_melee_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_melee_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_melee_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_melee_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_melee_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_melee_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_melee_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_melee_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_melee_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_melee_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_melee_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_melee_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_melee_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_melee_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_melee_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_melee_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_melee_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_melee_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_melee_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_melee_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_melee_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_melee_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_melee_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_melee_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_melee_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_melee_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_melee_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_melee_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_melee_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_melee_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_melee_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_melee_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_melee_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_melee_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_melee_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_melee_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_melee_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_melee_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_melee_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_melee_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_melee_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_melee_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_melee_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_melee_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_melee_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_melee_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_melee_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_melee_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_melee_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_melee_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_melee_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_melee_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_melee_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_melee_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_melee_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_melee_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_melee_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_melee_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_melee_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_melee_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_melee_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_melee_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_melee_idle_1)
                }
        };

        characterShotGunWalkFrames = new Bitmap[][] {
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_sgun_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_sgun_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_sgun_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_sgun_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_sgun_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_sgun_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_sgun_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_sgun_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_sgun_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_sgun_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_sgun_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_sgun_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_sgun_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_sgun_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_sgun_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_sgun_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_sgun_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_sgun_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_sgun_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_sgun_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_sgun_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_sgun_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_sgun_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_sgun_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_sgun_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_sgun_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_sgun_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_sgun_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_sgun_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_sgun_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_sgun_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_sgun_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_sgun_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_sgun_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_sgun_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_sgun_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_sgun_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_sgun_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_sgun_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_sgun_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_sgun_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_sgun_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_sgun_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_sgun_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_sgun_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_sgun_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_sgun_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_sgun_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_sgun_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_sgun_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_sgun_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_sgun_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_sgun_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_sgun_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_sgun_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_sgun_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_sgun_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_sgun_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_sgun_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_sgun_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_sgun_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_sgun_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_sgun_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_sgun_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_sgun_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_sgun_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_sgun_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_sgun_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_sgun_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_sgun_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_sgun_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_sgun_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_sgun_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_sgun_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_sgun_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_sgun_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_sgun_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_sgun_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_sgun_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_sgun_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_sgun_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_sgun_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_sgun_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_sgun_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_sgun_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_sgun_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_sgun_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_sgun_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_sgun_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_sgun_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_sgun_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_sgun_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_sgun_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_sgun_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_sgun_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_sgun_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_sgun_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_sgun_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_sgun_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_sgun_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_sgun_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_sgun_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_sgun_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_sgun_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_sgun_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_sgun_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_sgun_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_sgun_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_sgun_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_sgun_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_sgun_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_sgun_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_sgun_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_sgun_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_sgun_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_sgun_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_sgun_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_sgun_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_sgun_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_sgun_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_sgun_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_sgun_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_sgun_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_sgun_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_sgun_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_sgun_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_sgun_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_sgun_idle_1)
                }
        };

        characterMachineGunWalkFrames = new Bitmap[][] {
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_mgun_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_mgun_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_mgun_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_mgun_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_mgun_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_mgun_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_mgun_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_mgun_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_mgun_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_mgun_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_mgun_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_mgun_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_mgun_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_mgun_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_mgun_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_mgun_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_mgun_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_mgun_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_mgun_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_mgun_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_mgun_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_mgun_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_mgun_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_mgun_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_mgun_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_mgun_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_mgun_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_mgun_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_mgun_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_mgun_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_mgun_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_mgun_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_mgun_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_mgun_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_mgun_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_mgun_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_mgun_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_mgun_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_mgun_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_mgun_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_mgun_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_mgun_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_mgun_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_mgun_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_mgun_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_mgun_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_mgun_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_mgun_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_mgun_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_mgun_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_mgun_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_mgun_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_mgun_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_mgun_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_mgun_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_mgun_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_mgun_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_mgun_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_mgun_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_mgun_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_mgun_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_mgun_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_mgun_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_mgun_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_mgun_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_mgun_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_mgun_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_mgun_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_mgun_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_mgun_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_mgun_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_mgun_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_mgun_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_mgun_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_mgun_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_mgun_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_mgun_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_mgun_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_mgun_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_mgun_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_mgun_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_mgun_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_mgun_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_mgun_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_mgun_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_mgun_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_mgun_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_mgun_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_mgun_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_mgun_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_mgun_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_mgun_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_mgun_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_mgun_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_mgun_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_mgun_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_mgun_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_mgun_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_mgun_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_mgun_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_mgun_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_mgun_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_mgun_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_mgun_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_mgun_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_mgun_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_mgun_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_mgun_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_mgun_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_mgun_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_mgun_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_mgun_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_mgun_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_mgun_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_mgun_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_mgun_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_mgun_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_mgun_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_mgun_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_mgun_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_mgun_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_mgun_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_mgun_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_mgun_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_mgun_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_mgun_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_mgun_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_mgun_idle_1)
                }
        };

        characterContactBombWalkFrames = new Bitmap[][] {
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_cbomb_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_cbomb_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_cbomb_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_cbomb_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_cbomb_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_cbomb_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_cbomb_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_cbomb_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_cbomb_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_cbomb_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_cbomb_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_cbomb_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_cbomb_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_cbomb_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_cbomb_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_cbomb_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_cbomb_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_cbomb_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_cbomb_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_cbomb_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_cbomb_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_cbomb_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_cbomb_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_cbomb_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_cbomb_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_cbomb_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_cbomb_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_cbomb_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_cbomb_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_cbomb_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_cbomb_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_cbomb_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_cbomb_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_cbomb_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_cbomb_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_cbomb_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_cbomb_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_cbomb_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_cbomb_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_cbomb_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_cbomb_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_cbomb_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_cbomb_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_cbomb_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_cbomb_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_cbomb_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_cbomb_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_cbomb_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_cbomb_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_cbomb_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_cbomb_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_cbomb_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_cbomb_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_cbomb_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_cbomb_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_cbomb_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_cbomb_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_cbomb_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_cbomb_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_cbomb_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_cbomb_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_cbomb_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_cbomb_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_cbomb_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_cbomb_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_cbomb_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_cbomb_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_cbomb_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_cbomb_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_cbomb_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_cbomb_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_cbomb_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_cbomb_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_cbomb_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_cbomb_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_cbomb_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_cbomb_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_cbomb_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_cbomb_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_cbomb_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_cbomb_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_cbomb_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_cbomb_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_cbomb_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_cbomb_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_cbomb_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_cbomb_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_cbomb_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_cbomb_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_cbomb_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_cbomb_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_cbomb_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_cbomb_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_cbomb_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_cbomb_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_cbomb_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_bomb_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_bomb_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_bomb_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_bomb_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_bomb_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_bomb_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_bomb_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_bomb_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_bomb_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_bomb_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_bomb_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_bomb_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_bomb_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_bomb_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_bomb_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_bomb_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_cbomb_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_cbomb_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_cbomb_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_cbomb_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_cbomb_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_cbomb_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_cbomb_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_cbomb_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_cbomb_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_cbomb_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_cbomb_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_cbomb_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_cbomb_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_cbomb_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_cbomb_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_cbomb_idle_1)
                }
        };

        characterStickyBombWalkFrames = new Bitmap[][] {
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_sbomb_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_sbomb_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_sbomb_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_sbomb_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_sbomb_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_sbomb_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_sbomb_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_sbomb_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_sbomb_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_sbomb_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_sbomb_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_sbomb_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_sbomb_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_sbomb_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_sbomb_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_sbomb_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_sbomb_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_sbomb_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_sbomb_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_sbomb_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_sbomb_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_sbomb_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_sbomb_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_sbomb_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_sbomb_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_sbomb_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_sbomb_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_sbomb_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_sbomb_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_sbomb_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_sbomb_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_sbomb_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_sbomb_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_sbomb_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_sbomb_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_sbomb_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_sbomb_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_sbomb_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_sbomb_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_sbomb_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_sbomb_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_sbomb_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_sbomb_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_sbomb_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_sbomb_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_sbomb_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_sbomb_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_sbomb_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_sbomb_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_sbomb_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_sbomb_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_sbomb_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_sbomb_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_sbomb_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_sbomb_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_sbomb_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_sbomb_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_sbomb_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_sbomb_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_sbomb_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_sbomb_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_sbomb_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_sbomb_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_sbomb_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_sbomb_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_sbomb_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_sbomb_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_sbomb_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_sbomb_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_sbomb_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_sbomb_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_sbomb_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_sbomb_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_sbomb_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_sbomb_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_sbomb_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_sbomb_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_sbomb_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_sbomb_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_sbomb_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_sbomb_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_sbomb_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_sbomb_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_sbomb_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_sbomb_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_sbomb_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_sbomb_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_sbomb_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_sbomb_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_sbomb_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_sbomb_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_sbomb_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_sbomb_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_sbomb_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_sbomb_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_sbomb_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_bomb_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_bomb_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_bomb_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_bomb_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_bomb_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_bomb_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_bomb_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_bomb_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_bomb_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_bomb_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_bomb_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_bomb_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_bomb_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_bomb_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_bomb_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_bomb_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_sbomb_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_sbomb_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_sbomb_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_sbomb_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_sbomb_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_sbomb_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_sbomb_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_sbomb_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_sbomb_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_sbomb_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_sbomb_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_sbomb_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_sbomb_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_sbomb_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_sbomb_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_sbomb_idle_1)
                }
        };

        characterPistolWalkFrames = new Bitmap[][] {
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_pistol_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_pistol_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_pistol_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_pistol_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_pistol_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_pistol_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_pistol_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_pistol_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_pistol_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_pistol_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_pistol_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_pistol_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_pistol_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_pistol_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_pistol_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_east_pistol_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_pistol_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_pistol_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_pistol_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_pistol_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_pistol_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_pistol_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_pistol_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_pistol_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_pistol_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_pistol_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_pistol_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_pistol_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_pistol_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_pistol_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_pistol_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southeast_pistol_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_pistol_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_pistol_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_pistol_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_pistol_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_pistol_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_pistol_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_pistol_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_pistol_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_pistol_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_pistol_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_pistol_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_pistol_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_pistol_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_pistol_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_pistol_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_south_pistol_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_pistol_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_pistol_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_pistol_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_pistol_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_pistol_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_pistol_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_pistol_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_pistol_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_pistol_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_pistol_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_pistol_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_pistol_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_pistol_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_pistol_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_pistol_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_southwest_pistol_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_pistol_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_pistol_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_pistol_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_pistol_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_pistol_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_pistol_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_pistol_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_pistol_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_pistol_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_pistol_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_pistol_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_pistol_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_pistol_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_pistol_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_pistol_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_west_pistol_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_pistol_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_pistol_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_pistol_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_pistol_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_pistol_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_pistol_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_pistol_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_pistol_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_pistol_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_pistol_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_pistol_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_pistol_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_pistol_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_pistol_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_pistol_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northwest_pistol_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_pistol_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_pistol_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_pistol_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_pistol_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_pistol_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_pistol_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_pistol_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_pistol_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_pistol_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_pistol_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_pistol_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_pistol_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_pistol_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_pistol_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_pistol_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_north_pistol_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_pistol_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_pistol_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_pistol_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_pistol_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_pistol_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_pistol_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_pistol_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_pistol_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_pistol_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_pistol_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_pistol_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_pistol_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_pistol_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_pistol_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_pistol_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.character_northeast_pistol_idle_1)
                }
        };

        robotWalkFrames = new Bitmap[][] {
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_east_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_east_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_east_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_east_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_east_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_east_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_east_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_east_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_east_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_east_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_east_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_east_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_east_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_east_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_east_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_east_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_southeast_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_southeast_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_southeast_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_southeast_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_southeast_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_southeast_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_southeast_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_southeast_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_southeast_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_southeast_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_southeast_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_southeast_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_southeast_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_southeast_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_southeast_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_southeast_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_south_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_south_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_south_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_south_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_south_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_south_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_south_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_south_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_south_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_south_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_south_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_south_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_south_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_south_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_south_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_south_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_southwest_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_southwest_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_southwest_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_southwest_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_southwest_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_southwest_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_southwest_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_southwest_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_southwest_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_southwest_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_southwest_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_southwest_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_southwest_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_southwest_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_southwest_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_southwest_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_west_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_west_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_west_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_west_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_west_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_west_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_west_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_west_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_west_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_west_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_west_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_west_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_west_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_west_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_west_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_west_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_northwest_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_northwest_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_northwest_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_northwest_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_northwest_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_northwest_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_northwest_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_northwest_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_northwest_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_northwest_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_northwest_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_northwest_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_northwest_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_northwest_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_northwest_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_northwest_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_north_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_north_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_north_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_north_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_north_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_north_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_north_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_north_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_north_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_north_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_north_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_north_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_north_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_north_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_north_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_north_idle_1)
                },
                new Bitmap[] {
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_northeast_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_northeast_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_northeast_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_northeast_walk_l4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_northeast_walk_l3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_northeast_walk_l2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_northeast_walk_l1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_northeast_idle_1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_northeast_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_northeast_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_northeast_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_northeast_walk_r4),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_northeast_walk_r3),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_northeast_walk_r2),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_northeast_walk_r1),
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_1_northeast_idle_1)
                }
        };

        mp.start();
        mp.setVolume(musicVolume, musicVolume);
        mp.setLooping(true);

        view = new GameView(this);
        setContentView(view);
    }

    @Override
    public void onPause() {
        super.onPause();
        view.setPause();

        writeFile("survival_map.txt", view.save(), this);
    }

    public static void writeFile(String fileName, String value, Context context) {
        File path = context.getApplicationContext().getFilesDir();
        try {
            FileOutputStream fos = new FileOutputStream(new File(path, fileName));
            fos.write(value.getBytes());
            fos.close();
        } catch (IOException ignored) {}
    }

    public static String readFile(String fileName, Context context) {
        File path = context.getApplicationContext().getFilesDir();
        File readingFile = new File(path, fileName);
        byte[] value = new byte[(int)readingFile.length()];
        try {
            FileInputStream fis = new FileInputStream(readingFile);
            fis.read(value);
            return new String(value);
        } catch (IOException ignored) {}
        return null;
    }

    public static boolean fileExists(String fileName, Context context) {
        File path = context.getApplicationContext().getFilesDir();
        File file = new File(path, fileName);
        return file.length() != 0;
    }

    public static void setVolume(float sfxPos, float musicPos) {
        sfxVolume = sfxPos;
        musicVolume = musicPos;
        mp.setVolume(musicVolume, musicVolume);
    }

    public static void pauseAllSounds() {
        sp.autoPause();
        mp.pause();
    }

    public static void pauseSfx() {
        sp.autoPause();
    }

    public static void resumeAllSounds() {
        sp.autoResume();
        mp.start();
    }

    public static void resumeMusic() {
        mp.start();
    }

    public static void playExplosion(float dist) {
        if (dist != 0) dist /= 300;
        if (dist < 1) dist = 1;

        sp.play(explosionId, sfxVolume / dist, sfxVolume / dist, 0, 0, 1);
    }

    public static void playPistolShot(float dist) {
        if (dist != 0) dist /= 300;
        if (dist < 1) dist = 1;

        sp.play(pistolShotId, sfxVolume / dist, sfxVolume / dist, 0, 0, 1);
    }

    public static void playShotGunShot(float dist) {
        if (dist != 0) dist /= 300;
        if (dist < 1) dist = 1;

        sp.play(shotGunShotId, sfxVolume / dist, sfxVolume / dist, 0, 0, 1);
    }

    public static void playMeleeShot(float dist) {
        if (dist != 0) dist /= 300;
        if (dist < 1) dist = 1;

        sp.play(meleeShotId, sfxVolume / dist, sfxVolume / dist, 0, 0, 1);
    }

    public static void playEnemyJumper(float dist) {
        if (dist != 0) dist /= 300;
        if (dist < 1) dist = 1;

        sp.play(enemyJumperId, sfxVolume / dist, sfxVolume / dist, 0, 0, 1);
    }

    public static void playJump() {
        sp.play(jumpId, sfxVolume, sfxVolume, 0, 0, 1);
    }

    public static void playLanding(float dist) {
        if (dist != 0) dist /= 300;
        if (dist < 1) dist = 1;

        sp.play(landingId, sfxVolume / dist, sfxVolume / dist, 0, 0, 1);
    }

    public static int playSteps() {
        return sp.play(stepsId, sfxVolume, sfxVolume, 0, -1, 1);
    }

    public static void stopSteps(int id) {
        sp.stop(id);
    }

    public static int playRoboSteps(float dist) {
        if (!robostepsready) return -1;

        if (dist != 0) dist /= 300;
        if (dist < 1) dist = 1;
        return sp.play(robostepsId, sfxVolume / dist, sfxVolume / dist, 0, -1, 1);
    }

    public static void changeRoboSteps(int id, float dist) {
        if (dist != 0) dist /= 300;
        if (dist < 1) dist = 1;
        sp.setVolume(id, sfxVolume / dist, sfxVolume / dist);
    }

    public static void stopRoboSteps(int id) {
        sp.stop(id);
    }

    public static void playPause() {
        sp.play(pauseId, sfxVolume, sfxVolume, 0, 0, 1);
    }

    public static void playMoneyGet() {
        sp.play(moneygetId, sfxVolume, sfxVolume, 0, 0, 1);
    }

    public static void playMoneySpent() {
        sp.play(moneyspentId, sfxVolume, sfxVolume, 0, 0, 1);
    }

    public static void playFailure() {
        sp.play(failureId, sfxVolume, sfxVolume, 0, 0, 1);
    }

    public static void playBomb(float dist) {
        if (dist != 0) dist /= 300;
        if (dist < 1) dist = 1;

        sp.play(bombId, sfxVolume / dist, sfxVolume / dist, 0, 0, 1);
    }

    public static void playBombThrow(float dist) {
        if (dist != 0) dist /= 300;
        if (dist < 1) dist = 1;

        sp.play(bombThrowId, sfxVolume / dist, sfxVolume / dist, 0, 0, 1);
    }

    public static void playStickyBomb(float dist) {
        if (dist != 0) dist /= 300;
        if (dist < 1) dist = 1;

        sp.play(stickyBombId, sfxVolume / dist, sfxVolume / dist, 0, 0, 1);
    }

    public static void playHeal() {
        sp.play(healId, sfxVolume, sfxVolume, 0, 0, 1);
    }

    public static void playNewWave() {
        sp.play(newWaveId, sfxVolume, sfxVolume, 0, 0, 1);
    }
}