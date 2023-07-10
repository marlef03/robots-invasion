package com.example.game.util;

import com.example.game.species.Body;
import com.example.game.species.Bullet;
import com.example.game.species.Explosion;
import com.example.game.species.StickyBomb;

public class Manifold {
    Body obj1, obj2;
    Vec normal, cp;
    float penetration;

    public Manifold(Body obj1, Body obj2, Vec normal, float penetration, Vec cp) {
        this.obj1 = obj1;
        this.obj2 = obj2;
        this.normal = normal;
        this.penetration = penetration;
        this.cp = cp;
    }

    public void penetrationResolution() {
        if (obj1.getInvMass() + obj2.getInvMass() == 0) return;

        Vec penRes = normal.multy(penetration / (obj1.getInvMass() + obj2.getInvMass()));
        if (!(obj1 instanceof Explosion) && !(obj2 instanceof Explosion)
                && (!(obj1 instanceof StickyBomb) && !(obj2 instanceof StickyBomb))) {
            obj1.setPosV(obj1.getPosV().add(penRes.multy(obj1.getInvMass())));
            obj2.setPosV(obj2.getPosV().add(penRes.multy(-obj2.getInvMass())));
        }
        else if (obj1 instanceof Explosion) {
            obj2.changeHealth(-obj1.getDamage());

            obj2.setVelV(obj2.getVelV().add(penRes.unit().multy(-obj2.getInvMass() * ((Explosion) obj1).getForce() / 40)));

            if (obj2.getZ() + obj2.getL() >= obj1.getZ() + obj1.getL() / 2) obj2.setJumpVel(obj2.getInvMass() * Math.abs(((Explosion) obj1).getForce() * 40));
            else obj2.setJumpVel(-obj2.getInvMass() * Math.abs(((Explosion) obj1).getForce() * 40));
            obj2.setJumped(true);
        }

        else if (obj2 instanceof Explosion) {
            obj1.changeHealth(-obj2.getDamage());

            obj1.setVelV(obj1.getVelV().add(penRes.unit().multy(obj1.getInvMass() * ((Explosion) obj2).getForce() / 40)));

            if (obj1.getZ() + obj1.getL() >= obj2.getZ() + obj2.getL() / 2) obj1.setJumpVel(obj1.getInvMass() * Math.abs(((Explosion) obj2).getForce() * 40));
            else obj1.setJumpVel(-obj1.getInvMass() * Math.abs(((Explosion) obj2).getForce() * 40));
            obj1.setJumped(true);
        }

        if (obj1 instanceof StickyBomb && obj2 instanceof StickyBomb) {
            obj1.setPosV(obj1.getPosV().add(penRes.multy(obj1.getInvMass())));
            obj2.setPosV(obj2.getPosV().add(penRes.multy(-obj2.getInvMass())));
        }

        else if (obj1 instanceof StickyBomb) {
            obj1.setPosV(obj1.getPosV().add(penRes.multy(obj1.getInvMass())));

            ((StickyBomb) obj1).setParent(obj2);
        }

        else if (obj2 instanceof StickyBomb) {
            obj2.setPosV(obj2.getPosV().add(penRes.multy(-obj2.getInvMass())));

            ((StickyBomb) obj2).setParent(obj1);
        }

        if (obj1 instanceof Bullet) {
            obj1.setHealth(0);
            obj2.changeHealth(-obj1.getDamage());
        }
        else if (obj2 instanceof Bullet) {
            obj2.setHealth(0);
            obj1.changeHealth(-obj2.getDamage());
        }
    }

    public void collisionResponse() {
        if (obj1 instanceof Explosion || obj2 instanceof Explosion || (obj1 instanceof StickyBomb && !(obj2 instanceof StickyBomb))
                || (obj2 instanceof StickyBomb && !(obj1 instanceof StickyBomb))) return;

        Vec collArm1 = cp.subtr(obj1.getShape().getPosV());
        Vec rotVel1 = new Vec(-(float) (obj1.getAngleVel() * collArm1.y), (float) (obj1.getAngleVel() * collArm1.x));
        Vec closVel1 = obj1.getVelV().add(rotVel1);

        Vec collArm2 = cp.subtr(obj2.getShape().getPosV());
        Vec rotVel2 = new Vec(-(float) (obj2.getAngleVel() * collArm2.y), (float) (obj2.getAngleVel() * collArm2.x));
        Vec closVel2 = obj2.getVelV().add(rotVel2);


        float impAug1 = Vec.cross(collArm1, normal);
        impAug1 = impAug1 * obj1.getInvInertia() * impAug1;
        float impAug2 = Vec.cross(collArm2, normal);
        impAug2 = impAug2 * obj2.getInvInertia() * impAug2;

        Vec relVel = closVel1.subtr(closVel2);
        float sepVel = Vec.dot(relVel, normal);
        float new_sepVel = -sepVel * Math.min(obj1.getElasticity(), obj2.getElasticity());
        float vsep_diff = new_sepVel - sepVel;

        float impulse = vsep_diff / (obj1.getInvMass() + obj2.getInvMass() + impAug1 + impAug2);
        Vec impulseVec = normal.multy(impulse);

        float oldVel1 = obj1.getVelV().mag();
        float oldVel2 = obj2.getVelV().mag();

        obj1.setVelV(obj1.getVelV().add(impulseVec.multy(obj1.getInvMass())));
        obj2.setVelV(obj2.getVelV().add(impulseVec.multy(-obj2.getInvMass())));

        obj1.addAngleVel(obj1.getInvInertia() * Vec.cross(collArm1, impulseVec));
        obj2.addAngleVel(-obj2.getInvInertia() * Vec.cross(collArm2, impulseVec));

        if (oldVel1 > 2 && obj1.getVelV().mag() / oldVel1 < 0.5f) obj1.changeHealth(-obj2.getDamage() * oldVel1);
        if (oldVel2 > 2 && obj2.getVelV().mag() / oldVel2 < 0.5f) obj2.changeHealth(-obj1.getDamage() * oldVel2);
    }
}
