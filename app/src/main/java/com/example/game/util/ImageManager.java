package com.example.game.util;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;

import com.example.game.characters.pickups.Item;
import com.example.game.characters.pickups.MachineGun;
import com.example.game.characters.pickups.MedKit;
import com.example.game.characters.pickups.Melee;
import com.example.game.characters.pickups.Pistol;
import com.example.game.characters.pickups.ShotGun;
import com.example.game.characters.pickups.ThrowableWeapon;
import com.example.game.species.Bomb;

import javax.crypto.Mac;

public class ImageManager {
    private Bitmap[][] walkFrames, pistolWalkFrames, machineGunWalkFrames, shotGunWalkFrames, meleeWalkFrames, bombWalkFrames, contactBombWalkFrames, stickyBombWalkFrames, medKitWalkFrames;
    private int walkFrame, walkCoolDown;

    public void setWalkFrames(Bitmap[][] walkFrames) {
        this.walkFrames = walkFrames;
    }

    public void setPistolWalkFrames(Bitmap[][] walkFrames) {
        this.pistolWalkFrames = walkFrames;
    }

    public void setMachineGunWalkFrames(Bitmap[][] walkFrames) {
        this.machineGunWalkFrames = walkFrames;
    }

    public void setShotGunWalkFrames(Bitmap[][] walkFrames) {
        this.shotGunWalkFrames = walkFrames;
    }

    public void setMeleeWalkFrames(Bitmap[][] walkFrames) {
        this.meleeWalkFrames = walkFrames;
    }

    public void setBombWalkFrames(Bitmap[][] walkFrames) {
        this.bombWalkFrames = walkFrames;
    }

    public void setContactBombWalkFrames(Bitmap[][] walkFrames) {
        this.contactBombWalkFrames = walkFrames;
    }

    public void setStickyBombWalkFrames(Bitmap[][] walkFrames) {
        this.stickyBombWalkFrames = walkFrames;
    }

    public void setMedKitWalkFrames(Bitmap[][] walkFrames) {
        this.medKitWalkFrames = walkFrames;
    }

    public Bitmap getCurrentStatusFrame(Vec dir, boolean walk, Item item) {
        int pos = 0;
        if (dir.x > -0.38 && dir.x <= 0.38) {
            if (dir.y < 0) pos = 6;
            else pos = 2;
        } else if (dir.x > 0.38 && dir.x <= 0.92) {
            if (dir.y < 0) pos = 7;
            else pos = 1;
        } else if (dir.x <= -0.38 && dir.x > -0.92) {
            if (dir.y < 0) pos = 5;
            else pos = 3;
        } else if (dir.x <= -0.92) pos = 4;

        if (!walk) {
            walkFrame = 0;
            walkCoolDown = 0;
            if (item == null) return walkFrames[pos][7];
            else {
                if (item instanceof Pistol) return pistolWalkFrames[pos][7];
                else if (item instanceof MachineGun) return machineGunWalkFrames[pos][7];
                else if (item instanceof ShotGun) return shotGunWalkFrames[pos][7];
                else if (item instanceof Melee) return meleeWalkFrames[pos][7];
                else if (item instanceof ThrowableWeapon && !((ThrowableWeapon)item).getSticky() && !((ThrowableWeapon)item).getInstantExplosion()) return bombWalkFrames[pos][7];
                else if (item instanceof ThrowableWeapon && ((ThrowableWeapon)item).getSticky()) return stickyBombWalkFrames[pos][7];
                else if (item instanceof ThrowableWeapon) return contactBombWalkFrames[pos][7];
                else if (item instanceof MedKit) return medKitWalkFrames[pos][7];
                else return walkFrames[pos][7];
            }
        }
        else {
            walkCoolDown++;
            if (walkCoolDown == 3) {
                walkFrame = (walkFrame + 1) % walkFrames[0].length;
                walkCoolDown = 0;
            }
            if (item == null) return walkFrames[pos][walkFrame];
            else {
                if (item instanceof Pistol) return pistolWalkFrames[pos][walkFrame];
                else if (item instanceof MachineGun) return machineGunWalkFrames[pos][walkFrame];
                else if (item instanceof ShotGun) return shotGunWalkFrames[pos][walkFrame];
                else if (item instanceof Melee) return meleeWalkFrames[pos][walkFrame];
                else if (item instanceof ThrowableWeapon && !((ThrowableWeapon)item).getSticky() && !((ThrowableWeapon)item).getInstantExplosion()) return bombWalkFrames[pos][walkFrame];
                else if (item instanceof ThrowableWeapon && ((ThrowableWeapon)item).getSticky()) return stickyBombWalkFrames[pos][walkFrame];
                else if (item instanceof ThrowableWeapon) return contactBombWalkFrames[pos][walkFrame];
                else if (item instanceof MedKit) return medKitWalkFrames[pos][walkFrame];
                else return walkFrames[pos][walkFrame];
            }
        }
    }
}
