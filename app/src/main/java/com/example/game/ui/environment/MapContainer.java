package com.example.game.ui.environment;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.example.game.MainActivity;
import com.example.game.characters.Enemy;
import com.example.game.characters.Player;
import com.example.game.characters.pickups.*;
import com.example.game.core.GameView;
import com.example.game.species.*;
import com.example.game.ui.FuncButton;
import com.example.game.ui.JoyStick;
import com.example.game.ui.ShopUI;
import com.example.game.util.ComplexImage;
import com.example.game.util.Helper;
import com.example.game.util.Manifold;
import com.example.game.util.Vec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class MapContainer {
    private float x, y, w, h;

    public final ArrayList<Body> entities;

    private ArrayList<Manifold> manifolds;

    public Camera cam;

    public Player pl;

    private Pickup pickTo;

    private ComplexImage floorImage;

    private Shop shop;
    private int count, wave, robots;
    private boolean newWave = false;
    public boolean  gameOver = false;

    private boolean nullized;

    public MapContainer(String scenario) {
        String[] objects = scenario.split("\n");
        String[] mapParams = objects[0].split(" ");

        x = Float.parseFloat(mapParams[0]);
        y = Float.parseFloat(mapParams[1]);
        w = Float.parseFloat(mapParams[2]);
        h = Float.parseFloat(mapParams[3]);
        wave = Integer.parseInt(mapParams[4]);

        if (wave != 0) count = 1;

        floorImage = new ComplexImage(MainActivity.floor, 200, 200);

        cam = new Camera();

        manifolds = new ArrayList<>();
        entities = new ArrayList<>();

        ArrayList<StickyBomb> stickyBombs = new ArrayList<>();
        ArrayList<Integer> ids = new ArrayList<>();
        ThrowableWeapon tws = null;
        for (int i = 1; i < objects.length; i++) {
            String[] objParams = objects[i].split(" ");

            switch (objParams[0]) {
                case "pl":
                    pl = new Player(Float.parseFloat(objParams[1]), Float.parseFloat(objParams[2]), Float.parseFloat(objParams[3]));
                    pl.setHealth(Float.parseFloat(objParams[4]));
                    pl.setVelV(new Vec(Float.parseFloat(objParams[5]), Float.parseFloat(objParams[6])));
                    if (Float.parseFloat(objParams[7]) != 0) {
                        pl.setJumped(true);
                        pl.setJumpVel(Float.parseFloat(objParams[7]));
                    }
                    pl.setMoney(Integer.parseInt((objParams[8])));
                    entities.add(pl);
                    break;
                case "brn": {
                    Body b = new Body(3200, 0, -200, 0, 0, 10000, 1000, 0, 0.5f, 0);
                    b.setImageControl(MainActivity.housingNorth, 200, 200);
                    entities.add(b);
                    break;
                }
                case "bre": {
                    Body b = new Body(3000, -1000, 3600, -1000, 0, 10000, 20000, 0, 0.5f, 0);
                    b.setImageControl(MainActivity.housingEast, 200, 200);
                    entities.add(b);
                    break;
                }
                case "brs": {
                    Body b = new Body(-200, 3000, 3200, 3000, 0, 10000, 600, 0, 0.5f, 0);
                    b.setImageControl(MainActivity.housingSouth, 200, 200);
                    entities.add(b);
                    break;
                }
                case "brw": {
                    Body b = new Body(-600, -1000, 0, -1000, 0, 10000, 20000, 0, 0.5f, 0);
                    b.setImageControl(MainActivity.housingWest, 200, 200);
                    entities.add(b);
                    break;
                }
                case "exp":
                    entities.add(new Explosion(Float.parseFloat(objParams[1]), Float.parseFloat(objParams[2]),
                            Float.parseFloat(objParams[3]), Float.parseFloat(objParams[4]), Float.parseFloat(objParams[5]),
                            Float.parseFloat(objParams[6]), Float.parseFloat(objParams[7]), Float.parseFloat(objParams[8])));
                    break;
                case "bp": {
                    Bullet b = new Bullet(Float.parseFloat(objParams[1]), Float.parseFloat(objParams[2]), Float.parseFloat(objParams[3]), Float.parseFloat(objParams[4]),
                            Integer.parseInt(objParams[5]), Float.parseFloat(objParams[6]), Float.parseFloat(objParams[7]), Float.parseFloat(objParams[8]));
                    b.setVelV(new Vec(Float.parseFloat(objParams[9]), Float.parseFloat(objParams[10])));
                    entities.add(b);
                    break;
                }
                case "bm": {
                    Bullet b = new Bullet(Float.parseFloat(objParams[1]), Float.parseFloat(objParams[2]), Float.parseFloat(objParams[3]), Float.parseFloat(objParams[4]),
                            Float.parseFloat(objParams[5]), Float.parseFloat(objParams[6]), Integer.parseInt(objParams[7]), Float.parseFloat(objParams[8]), Float.parseFloat(objParams[9]),
                            Float.parseFloat(objParams[10]));
                    b.setVelV(new Vec(Float.parseFloat(objParams[11]), Float.parseFloat(objParams[12])));
                    entities.add(b);
                    break;
                }
                case "bmb": {
                    Bomb b = new Bomb(Float.parseFloat(objParams[1]), Float.parseFloat(objParams[2]), Float.parseFloat(objParams[3]), Float.parseFloat(objParams[4]),
                            Integer.parseInt(objParams[5]), Float.parseFloat(objParams[6]), Float.parseFloat(objParams[7]), Float.parseFloat(objParams[8]));
                    b.setVelV(new Vec(Float.parseFloat(objParams[9]), Float.parseFloat(objParams[10])));
                    if (Float.parseFloat(objParams[11]) != 0) {
                        b.setJumped(true);
                        b.setJumpVel(Float.parseFloat(objParams[11]));
                    }
                    entities.add(b);
                    break;
                }
                case "cbmb": {
                    ContactBomb b = new ContactBomb(Float.parseFloat(objParams[1]), Float.parseFloat(objParams[2]), Float.parseFloat(objParams[3]), Float.parseFloat(objParams[4]),
                            Integer.parseInt(objParams[5]), Float.parseFloat(objParams[6]), Float.parseFloat(objParams[7]), Float.parseFloat(objParams[8]));
                    b.setVelV(new Vec(Float.parseFloat(objParams[9]), Float.parseFloat(objParams[10])));
                    if (Float.parseFloat(objParams[11]) != 0) {
                        b.setJumped(true);
                        b.setJumpVel(Float.parseFloat(objParams[11]));
                    }
                    entities.add(b);
                    break;
                }
                case "sbmb":
                    StickyBomb sb = new StickyBomb(Float.parseFloat(objParams[1]), Float.parseFloat(objParams[2]), Float.parseFloat(objParams[3]), Float.parseFloat(objParams[4]),
                            Integer.parseInt(objParams[5]), Float.parseFloat(objParams[6]), Float.parseFloat(objParams[7]), null);
                    sb.setVelV(new Vec(Float.parseFloat(objParams[8]), Float.parseFloat(objParams[9])));
                    if (Float.parseFloat(objParams[10]) != 0) {
                        sb.setJumped(true);
                        sb.setJumpVel(Float.parseFloat(objParams[10]));
                    }
                    entities.add(sb);
                    stickyBombs.add(sb);
                    ids.add(Integer.parseInt(objParams[11]));
                    break;
                case "p":
                    switch (objParams[7]) {
                        case "p": {
                            Pickup p = new Pickup(Float.parseFloat(objParams[1]), Float.parseFloat(objParams[2]),
                                    Float.parseFloat(objParams[3]), new Pistol(Integer.parseInt(objParams[8]), Integer.parseInt(objParams[9]),
                                    Integer.parseInt(objParams[10])));
                            p.setVelV(new Vec(Float.parseFloat(objParams[4]), Float.parseFloat(objParams[5])));
                            if (Float.parseFloat(objParams[6]) != 0) {
                                p.setJumped(true);
                                p.setJumpVel(Float.parseFloat(objParams[6]));
                            }
                            entities.add(p);
                            break;
                        }
                        case "sg": {
                            Pickup p = new Pickup(Float.parseFloat(objParams[1]), Float.parseFloat(objParams[2]),
                                    Float.parseFloat(objParams[3]), new ShotGun(Integer.parseInt(objParams[8]), Integer.parseInt(objParams[9]),
                                    Integer.parseInt(objParams[10])));
                            p.setVelV(new Vec(Float.parseFloat(objParams[4]), Float.parseFloat(objParams[5])));
                            if (Float.parseFloat(objParams[6]) != 0) {
                                p.setJumped(true);
                                p.setJumpVel(Float.parseFloat(objParams[6]));
                            }
                            entities.add(p);
                            break;
                        }
                        case "mg": {
                            Pickup p = new Pickup(Float.parseFloat(objParams[1]), Float.parseFloat(objParams[2]),
                                    Float.parseFloat(objParams[3]), new MachineGun(Integer.parseInt(objParams[8]), Integer.parseInt(objParams[9]),
                                    Integer.parseInt(objParams[10])));
                            p.setVelV(new Vec(Float.parseFloat(objParams[4]), Float.parseFloat(objParams[5])));
                            if (Float.parseFloat(objParams[6]) != 0) {
                                p.setJumped(true);
                                p.setJumpVel(Float.parseFloat(objParams[6]));
                            }
                            entities.add(p);
                            break;
                        }
                        case "twi": {
                            Pickup p = new Pickup(Float.parseFloat(objParams[1]), Float.parseFloat(objParams[2]),
                                    Float.parseFloat(objParams[3]), new ThrowableWeapon(Integer.parseInt(objParams[8]), false, true));
                            p.setVelV(new Vec(Float.parseFloat(objParams[4]), Float.parseFloat(objParams[5])));
                            if (Float.parseFloat(objParams[6]) != 0) {
                                p.setJumped(true);
                                p.setJumpVel(Float.parseFloat(objParams[6]));
                            }
                            entities.add(p);
                            break;
                        }
                        case "tws": {
                            ThrowableWeapon wep = new ThrowableWeapon(Integer.parseInt(objParams[8]), true, false);
                            Pickup p = new Pickup(Float.parseFloat(objParams[1]), Float.parseFloat(objParams[2]),
                                    Float.parseFloat(objParams[3]), wep);
                            p.setVelV(new Vec(Float.parseFloat(objParams[4]), Float.parseFloat(objParams[5])));
                            if (Float.parseFloat(objParams[6]) != 0) {
                                p.setJumped(true);
                                p.setJumpVel(Float.parseFloat(objParams[6]));
                            }
                            entities.add(p);
                            tws = wep;
                            break;
                        }
                        case "tw": {
                            Pickup p = new Pickup(Float.parseFloat(objParams[1]), Float.parseFloat(objParams[2]),
                                    Float.parseFloat(objParams[3]), new ThrowableWeapon(Integer.parseInt(objParams[8]), false, false));
                            p.setVelV(new Vec(Float.parseFloat(objParams[4]), Float.parseFloat(objParams[5])));
                            if (Float.parseFloat(objParams[6]) != 0) {
                                p.setJumped(true);
                                p.setJumpVel(Float.parseFloat(objParams[6]));
                            }
                            entities.add(p);
                            break;
                        }
                        case "mel": {
                            Pickup p = new Pickup(Float.parseFloat(objParams[1]), Float.parseFloat(objParams[2]),
                                    Float.parseFloat(objParams[3]), new Melee());
                            p.setVelV(new Vec(Float.parseFloat(objParams[4]), Float.parseFloat(objParams[5])));
                            if (Float.parseFloat(objParams[6]) != 0) {
                                p.setJumped(true);
                                p.setJumpVel(Float.parseFloat(objParams[6]));
                            }
                            entities.add(p);
                            break;
                        }
                        case "med": {
                            Pickup p = new Pickup(Float.parseFloat(objParams[1]), Float.parseFloat(objParams[2]),
                                    Float.parseFloat(objParams[3]), new MedKit());
                            p.setVelV(new Vec(Float.parseFloat(objParams[4]), Float.parseFloat(objParams[5])));
                            if (Float.parseFloat(objParams[6]) != 0) {
                                p.setJumped(true);
                                p.setJumpVel(Float.parseFloat(objParams[6]));
                            }
                            entities.add(p);
                            break;
                        }
                        case "a": {
                            Pickup p = new Pickup(Float.parseFloat(objParams[1]), Float.parseFloat(objParams[2]),
                                    Float.parseFloat(objParams[3]), new Ammo(Integer.parseInt(objParams[8]), Integer.parseInt(objParams[9])));
                            p.setVelV(new Vec(Float.parseFloat(objParams[4]), Float.parseFloat(objParams[5])));
                            if (Float.parseFloat(objParams[6]) != 0) {
                                p.setJumped(true);
                                p.setJumpVel(Float.parseFloat(objParams[6]));
                            }
                            entities.add(p);
                        }
                        case "money": {
                            Pickup p = new Pickup(Float.parseFloat(objParams[1]), Float.parseFloat(objParams[2]),
                                    Float.parseFloat(objParams[3]), new Money(Integer.parseInt(objParams[8])));
                            p.setVelV(new Vec(Float.parseFloat(objParams[4]), Float.parseFloat(objParams[5])));
                            if (Float.parseFloat(objParams[6]) != 0) {
                                p.setJumped(true);
                                p.setJumpVel(Float.parseFloat(objParams[6]));
                            }
                            entities.add(p);
                        }
                    }
                    break;
                case "h":
                    entities.add(new HarmfulArea(Float.parseFloat(objParams[1]), Float.parseFloat(objParams[2]), Float.parseFloat(objParams[3]),
                            Float.parseFloat(objParams[4]), Float.parseFloat(objParams[5]), Float.parseFloat(objParams[6]), Integer.parseInt(objParams[7])));
                    break;
                case "port":
                    entities.add(new Portal(Float.parseFloat(objParams[1]), Float.parseFloat(objParams[2]), Float.parseFloat(objParams[3]),
                            Float.parseFloat(objParams[4]), Float.parseFloat(objParams[5])));
                    break;
                case "shop":
                    entities.add(new Shop(1250, 1400, 650, 1650, 0, 600, 350));
                    break;
                case "pli":
                    int n = Integer.parseInt(objParams[1]);
                    int ptr = 2;
                    for (int j = 0; j < n; j++) {
                        switch (objParams[ptr]) {
                            case "p": {
                                pl.getItemHolder().add(new Pistol(Integer.parseInt(objParams[ptr + 1]), Integer.parseInt(objParams[ptr + 2]),
                                        Integer.parseInt(objParams[ptr + 3])));
                                ptr += 4;
                                break;
                            }
                            case "sg": {
                                pl.getItemHolder().add(new ShotGun(Integer.parseInt(objParams[ptr + 1]), Integer.parseInt(objParams[ptr + 2]),
                                        Integer.parseInt(objParams[ptr + 3])));
                                ptr += 4;
                                break;
                            }
                            case "mg": {
                                pl.getItemHolder().add(new MachineGun(Integer.parseInt(objParams[ptr + 1]), Integer.parseInt(objParams[ptr + 2]),
                                        Integer.parseInt(objParams[ptr + 3])));
                                ptr += 4;
                                break;
                            }
                            case "twi": {
                                pl.getItemHolder().add(new ThrowableWeapon(Integer.parseInt(objParams[ptr + 1]), false, true));
                                ptr += 2;
                                break;
                            }
                            case "tws": {
                                ThrowableWeapon wep = new ThrowableWeapon(Integer.parseInt(objParams[ptr + 1]), true, false);
                                pl.getItemHolder().add(wep);
                                tws = wep;
                                ptr += 2;
                                break;
                            }
                            case "tw": {
                                pl.getItemHolder().add(new ThrowableWeapon(Integer.parseInt(objParams[ptr + 1]), false, false));
                                ptr += 2;
                                break;
                            }
                            case "mel": {
                                pl.getItemHolder().add(new Melee());
                                ptr += 1;
                                break;
                            }
                            case "med": {
                                pl.getItemHolder().add(new MedKit());
                                ptr += 1;
                                break;
                            }
                        }
                    }
                    break;
                case "e":
                    switch (objParams[20]) {
                        case "p": {
                            Enemy e = new Enemy(Float.parseFloat(objParams[1]), Float.parseFloat(objParams[2]),
                                    Float.parseFloat(objParams[3]), Float.parseFloat(objParams[4]), Float.parseFloat(objParams[5]), Float.parseFloat(objParams[6]),
                                    Float.parseFloat(objParams[7]), Float.parseFloat(objParams[8]), Float.parseFloat(objParams[9]), Integer.parseInt(objParams[10]),
                                    Float.parseFloat(objParams[11]), Boolean.parseBoolean(objParams[12]), Boolean.parseBoolean(objParams[13]),
                                    Boolean.parseBoolean(objParams[14]), Boolean.parseBoolean(objParams[15]), new Pistol(Integer.parseInt(objParams[21]),
                                    Integer.parseInt(objParams[22]), Integer.parseInt(objParams[23])));
                            e.setHealth(Float.parseFloat(objParams[16]));
                            e.setVelV(new Vec(Float.parseFloat(objParams[17]), Float.parseFloat(objParams[18])));
                            if (Float.parseFloat(objParams[19]) != 0) {
                                e.setJumped(true);
                                e.setJumpVel(Float.parseFloat(objParams[19]));
                            }

                            entities.add(e);
                            break;
                        }
                        case "sg": {
                            Enemy e = new Enemy(Float.parseFloat(objParams[1]), Float.parseFloat(objParams[2]),
                                    Float.parseFloat(objParams[3]), Float.parseFloat(objParams[4]), Float.parseFloat(objParams[5]), Float.parseFloat(objParams[6]),
                                    Float.parseFloat(objParams[7]), Float.parseFloat(objParams[8]), Float.parseFloat(objParams[9]), Integer.parseInt(objParams[10]),
                                    Float.parseFloat(objParams[11]), Boolean.parseBoolean(objParams[12]), Boolean.parseBoolean(objParams[13]),
                                    Boolean.parseBoolean(objParams[14]), Boolean.parseBoolean(objParams[15]), new ShotGun(Integer.parseInt(objParams[21]),
                                    Integer.parseInt(objParams[22]), Integer.parseInt(objParams[23])));
                            e.setHealth(Float.parseFloat(objParams[16]));
                            e.setVelV(new Vec(Float.parseFloat(objParams[17]), Float.parseFloat(objParams[18])));
                            if (Float.parseFloat(objParams[19]) != 0) {
                                e.setJumped(true);
                                e.setJumpVel(Float.parseFloat(objParams[19]));
                            }

                            entities.add(e);
                            break;
                        }
                        case "mg": {
                            Enemy e = new Enemy(Float.parseFloat(objParams[1]), Float.parseFloat(objParams[2]),
                                    Float.parseFloat(objParams[3]), Float.parseFloat(objParams[4]), Float.parseFloat(objParams[5]), Float.parseFloat(objParams[6]),
                                    Float.parseFloat(objParams[7]), Float.parseFloat(objParams[8]), Float.parseFloat(objParams[9]), Integer.parseInt(objParams[10]),
                                    Float.parseFloat(objParams[11]), Boolean.parseBoolean(objParams[12]), Boolean.parseBoolean(objParams[13]),
                                    Boolean.parseBoolean(objParams[14]), Boolean.parseBoolean(objParams[15]), new MachineGun(Integer.parseInt(objParams[21]),
                                    Integer.parseInt(objParams[22]), Integer.parseInt(objParams[23])));
                            e.setHealth(Float.parseFloat(objParams[16]));
                            e.setVelV(new Vec(Float.parseFloat(objParams[17]), Float.parseFloat(objParams[18])));
                            if (Float.parseFloat(objParams[19]) != 0) {
                                e.setJumped(true);
                                e.setJumpVel(Float.parseFloat(objParams[19]));
                            }

                            entities.add(e);
                            break;
                        }
                        case "tw": {
                            Enemy e = new Enemy(Float.parseFloat(objParams[1]), Float.parseFloat(objParams[2]),
                                    Float.parseFloat(objParams[3]), Float.parseFloat(objParams[4]), Float.parseFloat(objParams[5]), Float.parseFloat(objParams[6]),
                                    Float.parseFloat(objParams[7]), Float.parseFloat(objParams[8]), Float.parseFloat(objParams[9]), Integer.parseInt(objParams[10]),
                                    Float.parseFloat(objParams[11]), Boolean.parseBoolean(objParams[12]), Boolean.parseBoolean(objParams[13]),
                                    Boolean.parseBoolean(objParams[14]), Boolean.parseBoolean(objParams[15]), new ThrowableWeapon(Integer.parseInt(objParams[21]),
                                    false, false));
                            e.setHealth(Float.parseFloat(objParams[16]));
                            e.setVelV(new Vec(Float.parseFloat(objParams[17]), Float.parseFloat(objParams[18])));
                            if (Float.parseFloat(objParams[19]) != 0) {
                                e.setJumped(true);
                                e.setJumpVel(Float.parseFloat(objParams[19]));
                            }

                            entities.add(e);
                            break;
                        }
                        case "twi": {
                            Enemy e = new Enemy(Float.parseFloat(objParams[1]), Float.parseFloat(objParams[2]),
                                    Float.parseFloat(objParams[3]), Float.parseFloat(objParams[4]), Float.parseFloat(objParams[5]), Float.parseFloat(objParams[6]),
                                    Float.parseFloat(objParams[7]), Float.parseFloat(objParams[8]), Float.parseFloat(objParams[9]), Integer.parseInt(objParams[10]),
                                    Float.parseFloat(objParams[11]), Boolean.parseBoolean(objParams[12]), Boolean.parseBoolean(objParams[13]),
                                    Boolean.parseBoolean(objParams[14]), Boolean.parseBoolean(objParams[15]), new ThrowableWeapon(Integer.parseInt(objParams[21]),
                                    false, true));
                            e.setHealth(Float.parseFloat(objParams[16]));
                            e.setVelV(new Vec(Float.parseFloat(objParams[17]), Float.parseFloat(objParams[18])));
                            if (Float.parseFloat(objParams[19]) != 0) {
                                e.setJumped(true);
                                e.setJumpVel(Float.parseFloat(objParams[19]));
                            }

                            entities.add(e);
                            break;
                        }
                        case "mel": {
                            Enemy e = new Enemy(Float.parseFloat(objParams[1]), Float.parseFloat(objParams[2]),
                                    Float.parseFloat(objParams[3]), Float.parseFloat(objParams[4]), Float.parseFloat(objParams[5]), Float.parseFloat(objParams[6]),
                                    Float.parseFloat(objParams[7]), Float.parseFloat(objParams[8]), Float.parseFloat(objParams[9]), Integer.parseInt(objParams[10]),
                                    Float.parseFloat(objParams[11]), Boolean.parseBoolean(objParams[12]), Boolean.parseBoolean(objParams[13]),
                                    Boolean.parseBoolean(objParams[14]), Boolean.parseBoolean(objParams[15]), new Melee());
                            e.setHealth(Float.parseFloat(objParams[16]));
                            e.setVelV(new Vec(Float.parseFloat(objParams[17]), Float.parseFloat(objParams[18])));
                            if (Float.parseFloat(objParams[19]) != 0) {
                                e.setJumped(true);
                                e.setJumpVel(Float.parseFloat(objParams[19]));
                            }

                            entities.add(e);
                            break;
                        }
                    }
                    break;
            }
        }
        if (tws == null && pl != null) {
            for (int i = 0; i < pl.getItemHolder().getItemCount(); i++) {
                if (pl.getItemHolder().getBy(i) != null && pl.getItemHolder().getBy(i) instanceof ThrowableWeapon &&
                        ((ThrowableWeapon) pl.getItemHolder().getBy(i)).getSticky()) {
                    tws = (ThrowableWeapon) pl.getItemHolder().getBy(i);
                    break;
                }
            }
        }
        if (tws != null) {
            for (int i = 0; i < stickyBombs.size(); i++) {
                if (ids.get(i) != -1) stickyBombs.get(i).setParent(entities.get(ids.get(i)));
                tws.addSticky(stickyBombs.get(i));
            }
        }

        if (pl != null) cam.setFollowObject(pl);
    }

    public void nullize() {
        entities.clear();
        manifolds.clear();
        cam = null;
        pl = null;
        pickTo = null;
        floorImage = null;
        MainActivity.pauseSfx();
        nullized = true;
    }

    public boolean nullized() {
        return nullized;
    }

    public String serializeMap() {
        if (pl.getHealth() <= 0) return "";

        StringBuffer res = new StringBuffer();
        res.append(x + " " + y + " " + w + " " + h + " " + wave + "\n");

        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i) == pl) {
                res.append("pl " + pl.getPosV().x + " " + pl.getPosV().y + " " + pl.getZ() + " " + pl.getHealth() + " " + pl.getVelV().x + " " + pl.getVelV().y + " " + pl.getJumpVel()
                        + " " + pl.getMoney());
            }
            else if (entities.get(i) instanceof Pickup) {
                if (((Pickup) entities.get(i)).getItem() instanceof Pistol)
                    res.append("p " + entities.get(i).getPosV().x + " " + entities.get(i).getPosV().y + " " + entities.get(i).getZ() + " " + entities.get(i).getVelV().x + " " +
                            entities.get(i).getVelV().y + " " + entities.get(i).getJumpVel() + " p " + ((Pistol) ((Pickup) entities.get(i)).getItem()).getAmmo() + " " +
                            ((Pistol) ((Pickup) entities.get(i)).getItem()).getCurrentAmmo() + " " + ((Pistol) ((Pickup) entities.get(i)).getItem()).getAmmoReload());
                else if (((Pickup) entities.get(i)).getItem() instanceof ShotGun)
                    res.append("p " + entities.get(i).getPosV().x + " " + entities.get(i).getPosV().y + " " + entities.get(i).getZ() + " " + entities.get(i).getVelV().x + " " +
                            entities.get(i).getVelV().y + " " + entities.get(i).getJumpVel() + " sg " + ((ShotGun) ((Pickup) entities.get(i)).getItem()).getAmmo() + " " +
                            ((ShotGun) ((Pickup) entities.get(i)).getItem()).getCurrentAmmo() + " " + ((ShotGun) ((Pickup) entities.get(i)).getItem()).getAmmoReload());
                else if (((Pickup) entities.get(i)).getItem() instanceof MachineGun)
                    res.append("p " + entities.get(i).getPosV().x + " " + entities.get(i).getPosV().y + " " + entities.get(i).getZ() + " " + entities.get(i).getVelV().x + " " +
                            entities.get(i).getVelV().y + " " + entities.get(i).getJumpVel() + " mg " + ((MachineGun) ((Pickup) entities.get(i)).getItem()).getAmmo() + " " +
                            ((MachineGun) ((Pickup) entities.get(i)).getItem()).getCurrentAmmo() + " " + ((MachineGun) ((Pickup) entities.get(i)).getItem()).getAmmoReload());
                else if (((Pickup) entities.get(i)).getItem() instanceof ThrowableWeapon && ((ThrowableWeapon) ((Pickup) entities.get(i)).getItem()).getInstantExplosion())
                    res.append("p " + entities.get(i).getPosV().x + " " + entities.get(i).getPosV().y + " " + entities.get(i).getZ() + " " + entities.get(i).getVelV().x + " " +
                            entities.get(i).getVelV().y + " " + entities.get(i).getJumpVel() + " twi " + ((ThrowableWeapon) ((Pickup) entities.get(i)).getItem()).getAmmo());
                else if (((Pickup) entities.get(i)).getItem() instanceof ThrowableWeapon && ((ThrowableWeapon) ((Pickup) entities.get(i)).getItem()).getSticky())
                    res.append("p " + entities.get(i).getPosV().x + " " + entities.get(i).getPosV().y + " " + entities.get(i).getZ() + " " + entities.get(i).getVelV().x + " " +
                            entities.get(i).getVelV().y + " " + entities.get(i).getJumpVel() + " tws " + ((ThrowableWeapon) ((Pickup) entities.get(i)).getItem()).getAmmo());
                else if (((Pickup) entities.get(i)).getItem() instanceof ThrowableWeapon)
                    res.append("p " + entities.get(i).getPosV().x + " " + entities.get(i).getPosV().y + " " + entities.get(i).getZ() + " " + entities.get(i).getVelV().x + " " +
                            entities.get(i).getVelV().y + " " + entities.get(i).getJumpVel() + " tw " + ((ThrowableWeapon) ((Pickup) entities.get(i)).getItem()).getAmmo());
                else if (((Pickup) entities.get(i)).getItem() instanceof Melee)
                    res.append("p " + entities.get(i).getPosV().x + " " + entities.get(i).getPosV().y + " " + entities.get(i).getZ() + " " + entities.get(i).getVelV().x + " " +
                            entities.get(i).getVelV().y + " " + entities.get(i).getJumpVel() + " mel");
                else if (((Pickup) entities.get(i)).getItem() instanceof MedKit)
                    res.append("p " + entities.get(i).getPosV().x + " " + entities.get(i).getPosV().y + " " + entities.get(i).getZ() + " " + entities.get(i).getVelV().x + " " +
                            entities.get(i).getVelV().y + " " + entities.get(i).getJumpVel() + " med");
                else if (((Pickup) entities.get(i)).getItem() instanceof Ammo)
                    res.append("p " + entities.get(i).getPosV().x + " " + entities.get(i).getPosV().y + " " + entities.get(i).getZ() + " " + entities.get(i).getVelV().x + " " +
                            entities.get(i).getVelV().y + " " + entities.get(i).getJumpVel() + " a " + ((Ammo) ((Pickup) entities.get(i)).getItem()).getAmount() + " " +
                            ((Pickup) entities.get(i)).getItem().getId());
                else if (((Pickup) entities.get(i)).getItem() instanceof Money)
                    res.append("p " + entities.get(i).getPosV().x + " " + entities.get(i).getPosV().y + " " + entities.get(i).getZ() + " " + entities.get(i).getVelV().x + " " +
                            entities.get(i).getVelV().y + " " + entities.get(i).getJumpVel() + " money " + ((Money) ((Pickup) entities.get(i)).getItem()).getAmount());
            } else if (entities.get(i) instanceof HarmfulArea) {
                res.append("h " + entities.get(i).getPosV().x + " " + entities.get(i).getPosV().y + " " + entities.get(i).getZ() + " " +
                        entities.get(i).getL() + " " + entities.get(i).getShape().getR() + " " + entities.get(i).getDamage() + " " + ((HarmfulArea) entities.get(i)).getCoolDown());
            } else if (entities.get(i) instanceof Enemy) {
                if (((Enemy) entities.get(i)).getWeapon() instanceof Pistol)
                    res.append("e " + entities.get(i).getPosV().x + " " + entities.get(i).getPosV().y + " " + entities.get(i).getZ() + " " +
                            entities.get(i).getL() + " " + entities.get(i).getShape().getR() + " " + (entities.get(i).getInvMass() != 0 ? 1 / entities.get(i).getInvMass() : 0) + " " +
                            entities.get(i).getElasticity() + " " + entities.get(i).getVelForce() + " " + entities.get(i).getMaxHealth() + " " +
                            ((Enemy) entities.get(i)).getCoolDown() + " " + ((Enemy) entities.get(i)).getVisibilityLength() + " " + ((Enemy) entities.get(i)).getMovable() + " " +
                            ((Enemy) entities.get(i)).getFollower() + " " + ((Enemy) entities.get(i)).getJumper() + " " + ((Enemy) entities.get(i)).getFlyable() + " " +
                            entities.get(i).getHealth() + " " + entities.get(i).getVelV().x + " " + entities.get(i).getVelV().y + " " + entities.get(i).getJumpVel() +
                            " p " + ((Pistol) ((Enemy) entities.get(i)).getWeapon()).getAmmo() + " " + ((Pistol) ((Enemy) entities.get(i)).getWeapon()).getCurrentAmmo() + " " +
                            ((Pistol) ((Enemy) entities.get(i)).getWeapon()).getAmmoReload());
                else if (((Enemy) entities.get(i)).getWeapon() instanceof ShotGun)
                    res.append("e " + entities.get(i).getPosV().x + " " + entities.get(i).getPosV().y + " " + entities.get(i).getZ() + " " +
                            entities.get(i).getL() + " " + entities.get(i).getShape().getR() + " " + (entities.get(i).getInvMass() != 0 ? 1 / entities.get(i).getInvMass() : 0) + " " +
                            entities.get(i).getElasticity() + " " + entities.get(i).getVelForce() + " " + entities.get(i).getMaxHealth() + " " +
                            ((Enemy) entities.get(i)).getCoolDown() + " " + ((Enemy) entities.get(i)).getVisibilityLength() + " " + ((Enemy) entities.get(i)).getMovable() + " " +
                            ((Enemy) entities.get(i)).getFollower() + " " + ((Enemy) entities.get(i)).getJumper() + " " + ((Enemy) entities.get(i)).getFlyable() + " " +
                            entities.get(i).getHealth() + " " + entities.get(i).getVelV().x + " " + entities.get(i).getVelV().y + " " + entities.get(i).getJumpVel() +
                            " sg " + ((ShotGun) ((Enemy) entities.get(i)).getWeapon()).getAmmo() + " " + ((ShotGun) ((Enemy) entities.get(i)).getWeapon()).getCurrentAmmo() + " " +
                            ((ShotGun) ((Enemy) entities.get(i)).getWeapon()).getAmmoReload());
                else if (((Enemy) entities.get(i)).getWeapon() instanceof MachineGun)
                    res.append("e " + entities.get(i).getPosV().x + " " + entities.get(i).getPosV().y + " " + entities.get(i).getZ() + " " +
                            entities.get(i).getL() + " " + entities.get(i).getShape().getR() + " " + (entities.get(i).getInvMass() != 0 ? 1 / entities.get(i).getInvMass() : 0) + " " +
                            entities.get(i).getElasticity() + " " + entities.get(i).getVelForce() + " " + entities.get(i).getMaxHealth() + " " +
                            ((Enemy) entities.get(i)).getCoolDown() + " " + ((Enemy) entities.get(i)).getVisibilityLength() + " " + ((Enemy) entities.get(i)).getMovable() + " " +
                            ((Enemy) entities.get(i)).getFollower() + " " + ((Enemy) entities.get(i)).getJumper() + " " + ((Enemy) entities.get(i)).getFlyable() + " " +
                            entities.get(i).getHealth() + " " + entities.get(i).getVelV().x + " " + entities.get(i).getVelV().y + " " + entities.get(i).getJumpVel() +
                            " mg " + ((MachineGun) ((Enemy) entities.get(i)).getWeapon()).getAmmo() + " " + ((MachineGun) ((Enemy) entities.get(i)).getWeapon()).getCurrentAmmo() + " " +
                            ((MachineGun) ((Enemy) entities.get(i)).getWeapon()).getAmmoReload());
                else if (((Enemy) entities.get(i)).getWeapon() instanceof ThrowableWeapon && !((ThrowableWeapon) ((Enemy) entities.get(i)).getWeapon()).getInstantExplosion())
                    res.append("e " + entities.get(i).getPosV().x + " " + entities.get(i).getPosV().y + " " + entities.get(i).getZ() + " " +
                            entities.get(i).getL() + " " + entities.get(i).getShape().getR() + " " + (entities.get(i).getInvMass() != 0 ? 1 / entities.get(i).getInvMass() : 0) + " " +
                            entities.get(i).getElasticity() + " " + entities.get(i).getVelForce() + " " + entities.get(i).getMaxHealth() + " " +
                            ((Enemy) entities.get(i)).getCoolDown() + " " + ((Enemy) entities.get(i)).getVisibilityLength() + " " + ((Enemy) entities.get(i)).getMovable() + " " +
                            ((Enemy) entities.get(i)).getFollower() + " " + ((Enemy) entities.get(i)).getJumper() + " " + ((Enemy) entities.get(i)).getFlyable() + " " +
                            entities.get(i).getHealth() + " " + entities.get(i).getVelV().x + " " + entities.get(i).getVelV().y + " " + entities.get(i).getJumpVel() +
                            " tw " + ((ThrowableWeapon) ((Enemy) entities.get(i)).getWeapon()).getAmmo());
                else if (((Enemy) entities.get(i)).getWeapon() instanceof ThrowableWeapon && ((ThrowableWeapon) ((Enemy) entities.get(i)).getWeapon()).getInstantExplosion())
                    res.append("e " + entities.get(i).getPosV().x + " " + entities.get(i).getPosV().y + " " + entities.get(i).getZ() + " " +
                            entities.get(i).getL() + " " + entities.get(i).getShape().getR() + " " + (entities.get(i).getInvMass() != 0 ? 1 / entities.get(i).getInvMass() : 0) + " " +
                            entities.get(i).getElasticity() + " " + entities.get(i).getVelForce() + " " + entities.get(i).getMaxHealth() + " " +
                            ((Enemy) entities.get(i)).getCoolDown() + " " + ((Enemy) entities.get(i)).getVisibilityLength() + " " + ((Enemy) entities.get(i)).getMovable() + " " +
                            ((Enemy) entities.get(i)).getFollower() + " " + ((Enemy) entities.get(i)).getJumper() + " " + ((Enemy) entities.get(i)).getFlyable() + " " +
                            entities.get(i).getHealth() + " " + entities.get(i).getVelV().x + " " + entities.get(i).getVelV().y + " " + entities.get(i).getJumpVel() +
                            " twi " + ((ThrowableWeapon) ((Enemy) entities.get(i)).getWeapon()).getAmmo());
                else if (((Enemy) entities.get(i)).getWeapon() instanceof Melee)
                    res.append("e " + entities.get(i).getPosV().x + " " + entities.get(i).getPosV().y + " " + entities.get(i).getZ() + " " +
                            entities.get(i).getL() + " " + entities.get(i).getShape().getR() + " " + (entities.get(i).getInvMass() != 0 ? 1 / entities.get(i).getInvMass() : 0) + " " +
                            entities.get(i).getElasticity() + " " + entities.get(i).getVelForce() + " " + entities.get(i).getMaxHealth() + " " +
                            ((Enemy) entities.get(i)).getCoolDown() + " " + ((Enemy) entities.get(i)).getVisibilityLength() + " " + ((Enemy) entities.get(i)).getMovable() + " " +
                            ((Enemy) entities.get(i)).getFollower() + " " + ((Enemy) entities.get(i)).getJumper() + " " + ((Enemy) entities.get(i)).getFlyable() + " " +
                            entities.get(i).getHealth() + " " + entities.get(i).getVelV().x + " " + entities.get(i).getVelV().y + " " + entities.get(i).getJumpVel() + " mel");
            }
            else if (entities.get(i) instanceof Explosion) {
                res.append("exp " + entities.get(i).getPosV().x + " " + entities.get(i).getPosV().y + " " + entities.get(i).getZ() + " " + entities.get(i).getL() + " " +
                        (int)entities.get(i).getShape().getR() + " " + entities.get(i).getMaxHealth() + " " + entities.get(i).getHealth() + " " +
                        ((Explosion) entities.get(i)).getForce());
            }
            else if (entities.get(i) instanceof Bullet) {
                if (entities.get(i).getShape() instanceof Circle)
                    res.append("bp " + entities.get(i).getPosV().x + " " + entities.get(i).getPosV().y + " " + entities.get(i).getZ() + " " + entities.get(i).getL() + " " +
                            (int)entities.get(i).getShape().getR() + " " + (entities.get(i).getInvMass() != 0 ? 1 / entities.get(i).getInvMass() : 0) + " " +
                            entities.get(i).getElasticity() + " " + entities.get(i).getHealth() + " " + entities.get(i).getVelV().x + " " +
                            entities.get(i).getVelV().y);
                else
                    res.append("bm " + entities.get(i).getShape().getVertex()[0].x + " " + entities.get(i).getShape().getVertex()[0].y + " " +
                            entities.get(i).getShape().getVertex()[1].x + " " + entities.get(i).getShape().getVertex()[1].y + " " + entities.get(i).getZ() + " " +
                            entities.get(i).getL() + " " + ((Rectangle) entities.get(i).getShape()).getWidth() + " " +
                            (entities.get(i).getInvMass() != 0 ? 1 / entities.get(i).getInvMass() : 0) + " " + entities.get(i).getElasticity() + " " +
                            entities.get(i).getHealth() + " " + entities.get(i).getVelV().x + " " + entities.get(i).getVelV().y);
            }
            else if (entities.get(i) instanceof ContactBomb) {
                res.append("cbmb " + entities.get(i).getPosV().x + " " + entities.get(i).getPosV().y + " " + entities.get(i).getZ() + " " + entities.get(i).getL() + " " +
                        (int)entities.get(i).getShape().getR() + " " + (entities.get(i).getInvMass() != 0 ? 1 / entities.get(i).getInvMass() : 0) + " " +
                        entities.get(i).getElasticity() + " " + entities.get(i).getHealth() + " " + entities.get(i).getVelV().x + " " +
                        entities.get(i).getVelV().y + " " + entities.get(i).getJumpVel());
            }
            else if (entities.get(i) instanceof Bomb) {
                res.append("bmb " + entities.get(i).getPosV().x + " " + entities.get(i).getPosV().y + " " + entities.get(i).getZ() + " " + entities.get(i).getL() + " " +
                        (int)entities.get(i).getShape().getR() + " " + (entities.get(i).getInvMass() != 0 ? 1 / entities.get(i).getInvMass() : 0) + " " +
                        entities.get(i).getElasticity() + " " + entities.get(i).getHealth() + " " + entities.get(i).getVelV().x + " " +
                        entities.get(i).getVelV().y + " " + entities.get(i).getJumpVel());
            }
            else if (entities.get(i) instanceof StickyBomb) {
                res.append("sbmb " + entities.get(i).getPosV().x + " " + entities.get(i).getPosV().y + " " + entities.get(i).getZ() + " " + entities.get(i).getL() + " " +
                        (int)entities.get(i).getShape().getR() + " " + (entities.get(i).getInvMass() != 0 ? 1 / entities.get(i).getInvMass() : 0) + " " +
                        entities.get(i).getElasticity() + " " + entities.get(i).getVelV().x + " " + entities.get(i).getVelV().y + " " + entities.get(i).getJumpVel() + " " +
                        entities.indexOf(((StickyBomb) entities.get(i)).getParent()));
            }
            else if (entities.get(i) instanceof Portal) {
                res.append("port " + entities.get(i).getPosV().x + " " + entities.get(i).getPosV().y + " " + entities.get(i).getZ() + " " + entities.get(i).getHealth() + " " +
                        entities.get(i).getMaxHealth());
            }
            else if (entities.get(i) instanceof Shop) {
                res.append("shop");
            }
            else if (entities.get(i).getShape() instanceof Rectangle) {
                if (entities.get(i).getImageControl().getTag().equals("n")) res.append("brn");
                else if (entities.get(i).getImageControl().getTag().equals("e")) res.append("bre");
                else if (entities.get(i).getImageControl().getTag().equals("s")) res.append("brs");
                else if (entities.get(i).getImageControl().getTag().equals("w")) res.append("brw");
            }

            res.append("\n");
        }

        if (pl.getItemHolder().getItemCount() != 0) {
            res.append("pli " + pl.getItemHolder().getItemCount());
            for (int i = 0; i < pl.getItemHolder().getItemCount(); i++) {
                Item item = pl.getItemHolder().getBy(i);
                if (item instanceof Pistol)
                    res.append(" p " + ((Pistol) item).getAmmo() + " " + ((Pistol) item).getCurrentAmmo() + " " + ((Pistol) item).getAmmoReload());
                else if (item instanceof ShotGun)
                    res.append(" sg " + ((ShotGun) item).getAmmo() + " " + ((ShotGun) item).getCurrentAmmo() + " " + ((ShotGun) item).getAmmoReload());
                else if (item instanceof MachineGun)
                    res.append(" mg " + ((MachineGun) item).getAmmo() + " " + ((MachineGun) item).getCurrentAmmo() + " " + ((MachineGun) item).getAmmoReload());
                else if (item instanceof ThrowableWeapon && ((ThrowableWeapon) item).getInstantExplosion()) res.append(" twi " + ((ThrowableWeapon) item).getAmmo());
                else if (item instanceof ThrowableWeapon && ((ThrowableWeapon) item).getSticky()) res.append(" tws " + ((ThrowableWeapon) item).getAmmo());
                else if (item instanceof ThrowableWeapon) res.append(" tw " + ((ThrowableWeapon) item).getAmmo());
                else if (item instanceof Melee) res.append(" mel");
                else if (item instanceof MedKit) res.append(" med");
            }
            res.append("\n");
        }

        return res.toString();
    }

    public Vec getPlayerPos() {
        return pl.getPosV();
    }

    public float getPlayerZ() {
        return pl.getZ();
    }

    public void inputHolder(JoyStick input) {
        pl.changeSpeed(input.getXDir(), input.getYDir());
    }

    public void drawPlayerUI(Canvas c, Paint p) {
        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.FILL);
        c.drawRect((float)GameView.WIDTH / 2 - 200 * GameView.HEIGHT_MULTIP, GameView.HEIGHT - 240 * GameView.HEIGHT_MULTIP,
                (float)GameView.WIDTH / 2 + 200 * GameView.HEIGHT_MULTIP, GameView.HEIGHT - 210  * GameView.HEIGHT_MULTIP, p);

        if (pl.getMaxHealth() != 0) {
            p.setColor(Color.rgb(255 - (int) (pl.getHealth() / pl.getMaxHealth() * 255), (int) (pl.getHealth() / pl.getMaxHealth() * 255), 0));
            c.drawRect((float)GameView.WIDTH / 2 - 200 * GameView.HEIGHT_MULTIP, GameView.HEIGHT - 240 * GameView.HEIGHT_MULTIP,
                    (float)GameView.WIDTH / 2 + (-200 + 400 * pl.getHealth() / pl.getMaxHealth()) * GameView.HEIGHT_MULTIP, GameView.HEIGHT - 210  * GameView.HEIGHT_MULTIP, p);
        }

        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.WHITE);
        p.setTextSize(36 * GameView.HEIGHT_MULTIP);
        p.setAntiAlias(true);
        p.setTypeface(MainActivity.font);

        c.drawText("Здоровье:", (float)GameView.WIDTH / 2 - 100 * GameView.HEIGHT_MULTIP, GameView.HEIGHT - 250 * GameView.HEIGHT_MULTIP, p);

        p.setColor(Color.GREEN);
        p.setTextSize(40 * GameView.HEIGHT_MULTIP);
        c.drawText("Баланс: $" + pl.getMoney(), (float)GameView.WIDTH / 2 - 170 * GameView.HEIGHT_MULTIP, GameView.HEIGHT - 290 * GameView.HEIGHT_MULTIP, p);

        p.setAntiAlias(false);
    }

    public void gameLogic(FuncButton[] buttons, JoyStick[] movInp) {
        ItemHolder itemHolder = pl.getItemHolder();

        if (pl.isDestroyed()) {
            gameOver = true;
            return;
        }

        if (buttons[0].getPress()) {
            if (!pl.getJumped()) {
                pl.setJumped(true);
                pl.setJumpVel(pl.getJumpForce());

                MainActivity.playJump();
            }
            buttons[0].setPress(false);
        }

        if (buttons[1].getPress()) {
            if (pickTo != null) {
                if (!(pickTo.getItem() instanceof Ammo) && !(pickTo.getItem() instanceof Money)) {
                    boolean picked = true;

                    if (pickTo.getItem() instanceof MedKit) {
                        for (int i = 0; i < pl.getItemHolder().getItemCount(); i++) {
                            if (itemHolder.getBy(i) instanceof MedKit) {
                                if (((MedKit) itemHolder.getBy(i)).isMaxAmmo()) {
                                    itemHolder.getBy(i).addAmmo(1);

                                    entities.remove(pickTo);
                                }

                                picked = false;
                                break;
                            }
                        }
                    }
                    if (picked)
                        if (itemHolder.add(pickTo.getItem())) {
                            entities.remove(pickTo);
                            itemHolder.setActive(true);
                        }
                }
                else if (pickTo.getItem() instanceof Ammo) {
                    boolean used = false;

                    exit:
                    for (int i = 0; i < itemHolder.getItemCount(); i++) {
                        switch (pickTo.getItem().getId()) {
                            case 0:
                                if (itemHolder.getBy(i) instanceof Pistol) {
                                    itemHolder.getBy(i).addAmmo(((Ammo) pickTo.getItem()).getAmount());
                                    used = true;
                                    break exit;
                                }
                                break;
                            case 1:
                                if (itemHolder.getBy(i) instanceof ShotGun) {
                                    itemHolder.getBy(i).addAmmo(((Ammo) pickTo.getItem()).getAmount());
                                    used = true;
                                    break exit;
                                }
                                break;
                            case 2:
                                if (itemHolder.getBy(i) instanceof MachineGun) {
                                    itemHolder.getBy(i).addAmmo(((Ammo) pickTo.getItem()).getAmount());
                                    used = true;
                                    break exit;
                                }
                                break;
                            case 3:
                                if (itemHolder.getBy(i) instanceof ThrowableWeapon) {
                                    itemHolder.getBy(i).addAmmo(((Ammo) pickTo.getItem()).getAmount());
                                    used = true;
                                    break exit;
                                }
                                break;
                        }
                    }

                    if (used) {
                        entities.remove(pickTo);
                    }
                }
                else {
                    MainActivity.playMoneyGet();

                    pl.changeMoney(((Money) pickTo.getItem()).getAmount());

                    entities.remove(pickTo);
                }
            }
            else if (shop != null) buttons[5].setActive(true);
            else if (itemHolder.getActive()) {
                Vec pickPosV = pl.getPosV().add(pl.getShape().getDirV().multy(pl.getShape().getMaxLength() + itemHolder.getCur().getBody().getMaxLength()));
                Pickup pick;

                if (!(itemHolder.getCur() instanceof MedKit)) {
                    pick = new Pickup(pickPosV.x, pickPosV.y, pl.getZ(), itemHolder.getCur());

                    itemHolder.removeCur();
                }
                else {
                    pick = new Pickup(pickPosV.x, pickPosV.y, pl.getZ(), new MedKit((MedKit)itemHolder.getCur()));

                    if (((MedKit) itemHolder.getCur()).getAmmo() > 1) itemHolder.getCur().addAmmo(-1);
                    else itemHolder.removeCur();
                }

                pick.setVelV(pl.getShape().getDirV().multy(0.05f));
                entities.add(pick);
            }

            buttons[1].setPress(false);
        }

        else if (((ShopUI) buttons[5]).getToPurchase() != null) {
            Item toAmmoAdd = null;

            if (pl.getMoney() < ((ShopUI) buttons[5]).getToPurchaseCost()) ((ShopUI) buttons[5]).purchaseStatus(1);
            else if (!(((ShopUI) buttons[5]).getToPurchase() instanceof Ammo)) {
                boolean used = true;
                if (((ShopUI) buttons[5]).getToPurchase() instanceof MedKit) {
                    for (int i = 0; i < itemHolder.getItemCount(); i++) {
                        if (itemHolder.getBy(i) instanceof MedKit) {
                            used = false;
                            if (((MedKit) itemHolder.getBy(i)).isMaxAmmo()) {
                                itemHolder.getBy(i).addAmmo(1);
                                ((ShopUI) buttons[5]).purchaseStatus(0);
                                pl.changeMoney(-((ShopUI) buttons[5]).getToPurchaseCost());
                            } else used = true;
                        }
                    }
                }
                if (used) {
                    boolean added;
                    if (((ShopUI) buttons[5]).getToPurchase() instanceof MedKit) added = itemHolder.add(new MedKit((MedKit)((ShopUI) buttons[5]).getToPurchase()));
                    else added = itemHolder.add(((ShopUI) buttons[5]).getToPurchase());
                    if (!added) ((ShopUI) buttons[5]).purchaseStatus(2);
                    else {
                        ((ShopUI) buttons[5]).purchaseStatus(0);
                        pl.changeMoney(-((ShopUI) buttons[5]).getToPurchaseCost());
                    }
                }
            }
            else {
                exit:
                for (int i = 0; i < itemHolder.getItemCount(); i++) {
                    switch (((ShopUI) buttons[5]).getToPurchase().getId()) {
                        case 0:
                            if (itemHolder.getBy(i) instanceof Pistol) {
                                toAmmoAdd = itemHolder.getBy(i);
                                break exit;
                            }
                            break;
                        case 1:
                            if (itemHolder.getBy(i) instanceof ShotGun) {
                                toAmmoAdd = itemHolder.getBy(i);
                                break exit;
                            }
                            break;
                        case 2:
                            if (itemHolder.getBy(i) instanceof MachineGun) {
                                toAmmoAdd = itemHolder.getBy(i);
                                break exit;
                            }
                            break;
                        case 3:
                            if (itemHolder.getBy(i) instanceof ThrowableWeapon) {
                                toAmmoAdd = itemHolder.getBy(i);
                                break exit;
                            }
                            break;
                    }
                }
                if (toAmmoAdd == null) ((ShopUI) buttons[5]).purchaseStatus(3);
                else {
                    ((ShopUI) buttons[5]).purchaseStatus(0);
                    pl.changeMoney(-((ShopUI) buttons[5]).getToPurchaseCost());

                    toAmmoAdd.addAmmo(((Ammo) ((ShopUI) buttons[5]).getToPurchase()).getAmount());
                }
            }

            ((ShopUI) buttons[5]).resetToPurchase();
        }

        if (shop == null) buttons[5].setActive(false);

        buttons[1].setActive(false);
        if (pickTo != null) {
            buttons[1].setImage(MainActivity.pickupbutton);
            buttons[1].setActive(true);
        }
        else if ((shop != null && !buttons[5].getActive())) {
            buttons[1].setImage(MainActivity.shopbutton);
            buttons[1].setActive(true);
        }
        else if (itemHolder.getActive()) {
            buttons[1].setImage(MainActivity.dropbutton);
            buttons[1].setActive(true);
        }

        if (movInp[1] != null) {
            if (movInp[1].getClick()) {
                Vec dir = new Vec(movInp[1].getXDir(), movInp[1].getYDir());

                if (itemHolder.getCur() instanceof Pistol) {
                    Body bullet = itemHolder.getCur().getNextBullet(pl.getPosV().add(dir.multy(pl.getShape().getMaxLength())), pl.getZ(), dir);
                    if (bullet != null) {
                        MainActivity.playPistolShot(0);

                        entities.add(bullet);
                        pl.setLastDir(bullet.getVelV().unit());
                    }
                }

                if (itemHolder.getCur() instanceof ShotGun) {
                    Body[] bullets = itemHolder.getCur().getNextBulletArray(pl.getPosV().add(dir.multy(pl.getShape().getMaxLength())), pl.getZ(), dir);
                    if (bullets != null) {
                        MainActivity.playShotGunShot(0);

                        entities.addAll(Arrays.asList(bullets));
                        pl.setLastDir(bullets[2].getVelV().unit());
                    }
                }

                if (itemHolder.getCur() instanceof ThrowableWeapon) {
                    Body bullet = itemHolder.getCur().getNextBullet(pl.getPosV(), pl.getZ() + pl.getL(), dir);
                    if (bullet != null) {
                        if (!(bullet instanceof ContactBomb) && !(bullet instanceof StickyBomb)) {
                            MainActivity.playBomb(0);
                        } else {
                            MainActivity.playBombThrow(0);
                        }

                        entities.add(bullet);
                        pl.setLastDir(bullet.getVelV().unit());
                    }

                    if (((ThrowableWeapon) itemHolder.getCur()).getAmmo() == 0) {
                        if (((ThrowableWeapon) itemHolder.getCur()).getStickiesStatus()) itemHolder.removeCur();
                    }
                }

                if (itemHolder.getCur() instanceof Melee) {
                    Body bullet = itemHolder.getCur().getNextBullet(pl.getPosV().add(dir.multy(pl.getShape().getMaxLength())), pl.getZ(), dir);
                    if (bullet != null) {
                        MainActivity.playMeleeShot(0);

                        entities.add(bullet);
                        pl.setLastDir(bullet.getVelV().unit());
                    }
                }

                movInp[1].setClick(false);
            }
            if (movInp[1].getHold()) {
                Vec dir = new Vec(movInp[1].getXDir(), movInp[1].getYDir());

                if (itemHolder.getCur() instanceof MachineGun) {
                    Body bullet = itemHolder.getCur().getNextBullet(pl.getPosV().add(dir.multy(pl.getShape().getMaxLength())), pl.getZ(), dir);
                    if (bullet != null) {
                        MainActivity.playPistolShot(0);

                        entities.add(bullet);
                        pl.setLastDir(bullet.getVelV().unit());
                    }
                }
            }
        }

        if (itemHolder != null) {
            if (itemHolder.getCur() instanceof ThrowableWeapon) {
                Body toDel = ((ThrowableWeapon) itemHolder.getCur()).toMuchStickies();
                if (toDel != null) {
                    MainActivity.playExplosion(pl.getPosV().subtr(toDel.getPosV()).mag());

                    entities.add(toDel.getExplosion());

                    entities.remove(toDel);
                }
            }

            if (itemHolder.getExtraButton() != null)
                if (itemHolder.getExtraButton().getPress()) {
                    if (itemHolder.getCur() instanceof ThrowableWeapon) {
                        ArrayList<Body> bombs = ((ThrowableWeapon) itemHolder.getCur()).detonate();
                        for (int i = 0; i < bombs.size(); i++) {
                            MainActivity.playExplosion(pl.getPosV().subtr(bombs.get(i).getPosV()).mag());

                            entities.add(bombs.get(i).getExplosion());

                            entities.remove(bombs.get(i));
                        }
                        ((ThrowableWeapon) itemHolder.getCur()).clean();
                        if (((ThrowableWeapon) itemHolder.getCur()).getAmmo() == 0) itemHolder.removeCur();
                    } else if (itemHolder.getCur() instanceof MedKit) {
                        if (!pl.isFullHealth()) {
                            MainActivity.playHeal();

                            pl.changeHealth(((MedKit) itemHolder.getCur()).getAmount());

                            itemHolder.getCur().addAmmo(-1);
                            if (((MedKit) itemHolder.getCur()).getAmmo() <= 0)
                                itemHolder.removeCur();
                        }
                    }

                    if (itemHolder.getExtraButton() != null)
                        itemHolder.getExtraButton().setPress(false);
                }
        }
    }

    public void drawNewWave(Canvas c, Paint p) {
        p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.FILL);
        p.setTextSize(70 * GameView.HEIGHT_MULTIP);
        p.setAntiAlias(true);
        p.setTypeface(MainActivity.font);

        c.drawText("Волна: " + wave, (float)GameView.WIDTH / 2 - p.measureText("Волна: " + wave) / 2, 50 * GameView.HEIGHT_MULTIP, p);

        p.setTextSize(60 * GameView.HEIGHT_MULTIP);
        c.drawText("Осталось  " + robots + " роботов до новой волны!", (float)GameView.WIDTH / 2 - p.measureText("Осталось  " + robots + " роботов до новой волны!") / 2,
                100 * GameView.HEIGHT_MULTIP, p);

        if (newWave) {
            float size = count < 1000 ? (float)count / 10 : count > 5000 ? count < 6000 ? 100 - (float)(count - 5000) / 10 : 0 : 100;
            p.setColor(Color.RED);
            p.setTextSize(size * GameView.HEIGHT_MULTIP);
            c.drawText("НОВАЯ ВОЛНА!", (float) GameView.WIDTH / 2 - p.measureText("НОВАЯ ВОЛНА!") / 2, (100 + size) * GameView.HEIGHT_MULTIP, p);

            if (count > 6000) newWave = false;
        }
    }

    public void allMoving() {
        robots = 0;
        pickTo = null;
        shop = null;

        for (int i = 0; i < entities.size(); i++) {
            entities.get(i).move();
            entities.get(i).jump();

            for (int j = i + 1; j < entities.size(); j++) {
                Object[] sat = Helper.sat(entities.get(i).getShape(), entities.get(j).getShape());

                if (sat != null) {
                    if (entities.get(i) instanceof HarmfulArea && ((HarmfulArea) entities.get(i)).getReady()) entities.get(j).changeHealth(-entities.get(i).getDamage());

                    if (!(entities.get(j) instanceof HarmfulArea) && !(entities.get(i) instanceof HarmfulArea)
                            && !(entities.get(i) instanceof StickyBomb && ((StickyBomb) entities.get(i)).getParent() != null)
                            && !(entities.get(j) instanceof ContactBomb) && !(entities.get(i) instanceof ContactBomb)
                            && !(entities.get(j) instanceof Bullet) && !(entities.get(i) instanceof Bullet))
                        checkZCol(entities.get(i), entities.get(j));

                    if (entities.get(i).getZ() < entities.get(j).getZ() + entities.get(j).getL() && entities.get(i).getZ() + entities.get(i).getL() > entities.get(j).getZ()) {
                        if (entities.get(i) instanceof ContactBomb) {
                            entities.get(i).setHealth(0);
                        }
                        else if (entities.get(j) instanceof ContactBomb) {
                            entities.get(j).setHealth(0);
                        }
                        else {
                            if (!(entities.get(j) instanceof HarmfulArea) && !(entities.get(i) instanceof HarmfulArea)) {
                                if (!(entities.get(i) instanceof Bullet && entities.get(j) instanceof Bullet)
                                        && !(entities.get(i) instanceof Explosion && entities.get(j) instanceof Explosion) && !(entities.get(j) instanceof Explosion)
                                        && !((entities.get(i) instanceof StickyBomb && ((StickyBomb) entities.get(i)).getParent() != null)
                                        || (entities.get(j) instanceof StickyBomb && ((StickyBomb) entities.get(j)).getParent() != null))
                                        && !(entities.get(i).isDestroyed() || entities.get(j).isDestroyed())
                                        && !(entities.get(i).getInvMass() == 0 && entities.get(j).getInvMass() == 0)) {

                                    manifolds.add(new Manifold(entities.get(i), entities.get(j),
                                            (Vec) sat[1], (float) sat[0], (Vec) sat[2]));
                                }
                            }
                        }
                    }
                }
            }

            if (entities.get(i).getZCol()) entities.get(i).setJumped(false);
            else {
                if (entities.get(i).getZ() != 0) {
                    entities.get(i).setJumped(true);
                    entities.get(i).setFloorLevel(0);
                }
            }
            entities.get(i).setZCol(false);

            if (entities.get(i) instanceof HarmfulArea) ((HarmfulArea) entities.get(i)).count();

            else if (entities.get(i) instanceof Bullet) ((Bullet) entities.get(i)).count();

            else if (entities.get(i) instanceof Explosion) ((Explosion) entities.get(i)).count();

            else if (entities.get(i) instanceof Bomb) ((Bomb) entities.get(i)).count();

            else if (entities.get(i) instanceof Enemy) {
                robots++;

                ((Enemy) entities.get(i)).getWeapon().countCoolDown();
                ((Enemy) entities.get(i)).setDirection(pl.getPosV(), pl.getZ() + pl.getL() + 200);

                if (((Enemy) entities.get(i)).count()) {
                    Body[] toAdd = ((Enemy) entities.get(i)).shoot(pl.getZ() + pl.getL() / 2);
                    if (toAdd[0] != null) {
                        if (((Enemy) entities.get(i)).getWeapon() instanceof Pistol || ((Enemy) entities.get(i)).getWeapon() instanceof MachineGun) {
                            MainActivity.playPistolShot(pl.getPosV().subtr(entities.get(i).getPosV()).mag());
                        }
                        else if (((Enemy) entities.get(i)).getWeapon() instanceof ShotGun) {
                            MainActivity.playShotGunShot(pl.getPosV().subtr(entities.get(i).getPosV()).mag());
                        }
                        else if (((Enemy) entities.get(i)).getWeapon() instanceof Melee) {
                            MainActivity.playMeleeShot(pl.getPosV().subtr(entities.get(i).getPosV()).mag());
                        }

                        entities.addAll(Arrays.asList(toAdd));
                    }

                    ((Enemy) entities.get(i)).nullCountToCoolDown();
                }
            }

            else if (entities.get(i) instanceof Pickup) {
                ((Pickup) entities.get(i)).count();

                if (pl.getZ() < entities.get(i).getZ() + entities.get(i).getL() && pl.getZ() + pl.getL() > entities.get(i).getZ()) {
                    Object[] sat = Helper.sat(pl.getShape(), ((Pickup) entities.get(i)).getPickTrigger());

                    if (sat != null) pickTo = (Pickup) entities.get(i);
                }
            }

            else if (entities.get(i) instanceof Shop) {
                if (pl.getZ() < entities.get(i).getZ() + entities.get(i).getL() && pl.getZ() + pl.getL() > entities.get(i).getZ()) {
                    Object[] sat = Helper.sat(pl.getShape(), ((Shop) entities.get(i)).getTrigger());

                    if (sat != null) shop = (Shop)entities.get(i);
                }
            }

            else if (entities.get(i) instanceof Portal) {
                ((Portal) entities.get(i)).count();
                if (((Portal) entities.get(i)).clock()) {
                    Body spaw = ((Portal) entities.get(i)).spawn(wave);
                    if (spaw != null) entities.add(spaw);
                }
            }
            else if (entities.get(i) instanceof StickyBomb) ((StickyBomb) entities.get(i)).playSound(pl.getPosV().subtr(entities.get(i).getPosV()).mag());

            if (entities.get(i).isDestroyed()) {
                if (entities.get(i) instanceof Bomb) {
                    MainActivity.playExplosion(pl.getPosV().subtr(entities.get(i).getPosV()).mag());

                    entities.add(entities.get(i).getExplosion());
                }
                else if (entities.get(i) instanceof Enemy) {
                    Body drop = ((Enemy) entities.get(i)).drop();
                    if (drop != null) {
                        drop.setJumped(true);

                        entities.add(drop);
                    }

                    entities.add(((Enemy) entities.get(i)).dropMoney());
                }
                else if (entities.get(i) instanceof StickyBomb) {
                    ((StickyBomb) entities.get(i)).delFromWeapon();
                }

                entities.remove(i);
                i--;
            }
        }

        int portNum = 3;
        if (wave > 10) portNum = 4;
        if (wave > 30) portNum = 5;
        if (wave > 60) portNum = 6;
        if (wave > 80) portNum = 7;

        if (robots == 0 && count == 0) {
            newWave = true;
            MainActivity.playNewWave();

            for (int i = 0; i < portNum; i++)
                entities.add(new Portal(pl.getPosV().x - 500 + (float)(1000 * Math.random()),
                    pl.getPosV().y - 500 + (float)(1000 * Math.random()), 0, 10000));

            wave++;
            count = 1;
        }

        if (count == 20000) count = 0;
        if (count > 0) count++;

        for (int i = 0; i < manifolds.size(); i++) {
            manifolds.get(i).penetrationResolution();
            manifolds.get(i).collisionResponse();
        }

        manifolds.clear();

        Collections.sort(entities, new Comparator<Body>() {
            @Override
            public int compare(Body a, Body b) {
                return Float.compare(a.getZ(), b.getZ() + b.getL() - 0.001f);
            }
        });

        Collections.sort(entities, new Comparator<Body>() {
            @Override
            public int compare(Body a, Body b) {
                return Float.compare(a.getPosV().y, b.getPosV().y);
            }
        });
    }

    private void checkZCol(Body pl, Body p) {
        if (pl.getZ() < p.getL() + p.getZ() && pl.getZ() + pl.getL() > p.getZ()
                && (pl.getPrevZ() >= p.getPrevZ() + p.getL())) {
            pl.setZ(p.getL() + p.getZ());
            pl.setFloorLevel(p.getL() + p.getZ());

            if (pl instanceof StickyBomb && !(p instanceof StickyBomb)) {
                ((StickyBomb) pl).setParent(p);
            }
            else {
                if (pl instanceof Enemy && ((Enemy) pl).getJumper()) p.changeHealth(-((Enemy) pl).getJumperDamage());
                else p.changeHealth(Math.abs(pl.getJumpVel()) > 2.4 ? -pl.getDamage() * Math.abs(pl.getJumpVel()) / 10 : 0);
                pl.changeHealth(Math.abs(pl.getJumpVel()) > 2.4 ? -p.getDamage() * Math.abs(pl.getJumpVel()) / 10 : 0);

                boolean normal = pl instanceof Enemy && !(((Enemy) pl).getFlyable() || ((Enemy) pl).getJumper());
                if (!(pl instanceof  Enemy) || normal) {
                    if (pl.getJumpVel() < -0.6f) MainActivity.playLanding(this.pl.getPosV().subtr(pl.getPosV()).mag());
                    pl.setJumpVel(Math.abs(pl.getJumpVel() / pl.getBounceCoeff()));
                    if (pl.getJumpVel() < 0.1f) {
                        pl.setJumpVel(0);
                        pl.setJumped(false);
                    }
                }
                else if (((Enemy) pl).getFlyable()) pl.setJumpVel(Math.abs(pl.getJumpVel()));
                else if (((Enemy) pl).getJumper()) {
                    MainActivity.playEnemyJumper(this.pl.getPosV().subtr(pl.getPosV()).mag());

                    ((Enemy) pl).setJumperAsVel();
                }
            }
        }
        else if (p.getZ() < pl.getL() + pl.getZ() && p.getZ() + p.getL() > pl.getZ()
                && (p.getPrevZ() >= pl.getPrevZ() + pl.getL())) {
            p.setZ(pl.getL() + pl.getZ());
            p.setFloorLevel(pl.getL() + pl.getZ());

            if (p instanceof StickyBomb && !(pl instanceof StickyBomb)) {
                ((StickyBomb) p).setParent(pl);
            }
            else {
                if (p instanceof Enemy && ((Enemy) p).getJumper()) pl.changeHealth(-((Enemy) p).getJumperDamage());
                else pl.changeHealth(Math.abs(p.getJumpVel()) > 2.4 ? -p.getDamage() * Math.abs(p.getJumpVel()) / 10 : 0);
                p.changeHealth(Math.abs(p.getJumpVel()) > 2.4 ? -pl.getDamage() * Math.abs(p.getJumpVel()) / 10 : 0);

                boolean normal = p instanceof Enemy && !(((Enemy) p).getFlyable() || ((Enemy) p).getJumper());
                if (!(p instanceof  Enemy) || normal) {
                    if (p.getJumpVel() < -0.6f) MainActivity.playLanding(this.pl.getPosV().subtr(p.getPosV()).mag());
                    p.setJumpVel(Math.abs(p.getJumpVel() / p.getBounceCoeff()));
                    if (p.getJumpVel() < 0.1f) {
                        p.setJumpVel(0);
                        p.setJumped(false);
                    }
                }
                else if (((Enemy) p).getFlyable()) p.setJumpVel(Math.abs(p.getJumpVel()));
                else if (((Enemy) p).getJumper()) {
                    MainActivity.playEnemyJumper(this.pl.getPosV().subtr(p.getPosV()).mag());

                    ((Enemy) p).setJumperAsVel();
                }
            }
        }

        if (pl.getZ() + pl.getL() > p.getZ() && pl.getZ() < p.getL() + p.getZ()
                && (pl.getPrevZ() + pl.getL() <= p.getPrevZ())) {
            pl.setZ(p.getZ() - pl.getL());

            if (pl instanceof StickyBomb && !(p instanceof StickyBomb)) {
                ((StickyBomb) pl).setParent(p);
            }
            else {
                if (p instanceof Enemy && ((Enemy) p).getJumper()) pl.changeHealth(-((Enemy) p).getJumperDamage());
                else pl.changeHealth(Math.abs(pl.getJumpVel()) > 2.4 ? -p.getDamage() * Math.abs(pl.getJumpVel() / 10) : 0);
                p.changeHealth(Math.abs(pl.getJumpVel()) > 2.4 ? -pl.getDamage() * Math.abs(pl.getJumpVel()) / 10 : 0);

                if (p.getInvMass() != 0) {
                    boolean normal = pl instanceof Enemy && !(((Enemy) pl).getFlyable() || ((Enemy) pl).getJumper());
                    if (!(p instanceof Enemy) || normal) {
                        p.setJumpVel(Math.abs(pl.getJumpVel() * p.getInvMass() / pl.getInvMass()));
                        p.setJumped(true);
                    }
                    else if (((Enemy) p).getFlyable()) p.setJumpVel(Math.abs(p.getJumpVel()));
                    else if (((Enemy) p).getJumper()) {
                        ((Enemy) p).setJumperAsVel();

                        MainActivity.playEnemyJumper(this.pl.getPosV().subtr(p.getPosV()).mag());
                    }
                }
                pl.setJumpVel(-Math.abs(pl.getJumpVel() * pl.getInvMass()));
            }
        }

        else if (p.getZ() + p.getL() > pl.getZ() && p.getZ() < pl.getL() + pl.getZ()
                && (p.getPrevZ() + p.getL() <= pl.getPrevZ())) {
            p.setZ(pl.getZ() - p.getL());

            if (p instanceof StickyBomb && !(pl instanceof StickyBomb)) {
                ((StickyBomb) p).setParent(pl);
            }
            else {
                if (pl instanceof Enemy && ((Enemy) pl).getJumper()) p.changeHealth(-((Enemy) pl).getJumperDamage());
                else p.changeHealth(Math.abs(p.getJumpVel()) > 2.4 ? -pl.getDamage() * Math.abs(p.getJumpVel() / 10) : 0);
                pl.changeHealth(Math.abs(p.getJumpVel()) > 2.4 ? -p.getDamage() * Math.abs(p.getJumpVel()) / 10 : 0);

                if (pl.getInvMass() != 0) {
                    boolean normal = p instanceof Enemy && !(((Enemy) p).getFlyable() || ((Enemy) p).getJumper());
                    if (!(pl instanceof Enemy) || normal) {
                        pl.setJumpVel(Math.abs(p.getJumpVel() * pl.getInvMass() / p.getInvMass()));
                        pl.setJumped(true);
                    }
                    else if (((Enemy) pl).getFlyable()) pl.setJumpVel(Math.abs(pl.getJumpVel()));
                    else if (((Enemy) pl).getJumper()) {
                        ((Enemy) pl).setJumperAsVel();

                        MainActivity.playEnemyJumper(this.pl.getPosV().subtr(pl.getPosV()).mag());
                    }
                }
                p.setJumpVel(-Math.abs(p.getJumpVel() * p.getInvMass()));
            }
        }

        if (p.getZ() == pl.getZ() + pl.getL()) {
            if (p.getJumpVel() == 0) {
                if (p.getVelV().mag() < pl.getVelV().mag())
                    p.setVelV(pl.getVelV().multy(p.getInvMass()));
                p.setZCol(true);
            }
        }

        if (p.getZ() + p.getL() == pl.getZ()) {
            if (pl.getJumpVel() == 0) {
                if (pl instanceof Enemy && ((Enemy) pl).getJumper()) ((Enemy) pl).setJumperAsVel();
                pl.setZCol(true);
            }
        }
    }

    public void draw(Canvas c, Paint p) {
        p.setColor(Color.CYAN);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(15);

        floorImage.drawSpecifiedImages(c, p, 0, 0, -cam.getScreenEdgePos().x, -cam.getScreenEdgePos().y);

        for (int i = 0; i < entities.size(); i++) {
            if (Helper.rectIntersect(new float[] {
                    0, 0, GameView.WIDTH, GameView.HEIGHT
            }, entities.get(i).getRawRectangle(-cam.getScreenEdgePos().x, -cam.getScreenEdgePos().y))) {
                entities.get(i).draw(c, p, -cam.getScreenEdgePos().x, -cam.getScreenEdgePos().y);
            }
        }

        drawNewWave(c, p);
        drawPlayerUI(c, p);
    }
}
