package com.example.game.ui.environment;

import com.example.game.core.GameView;
import com.example.game.species.Body;
import com.example.game.util.Vec;

public class Camera {
    private Body followObject;

    public void setFollowObject(Body object) {
        followObject = object;
    }

    public Vec getScreenEdgePos() {
        return new Vec(followObject.getPosV().x - (float)GameView.WIDTH / GameView.HEIGHT_MULTIP / 2,
                followObject.getPosV().y - followObject.getZ() - (float)GameView.HEIGHT / GameView.HEIGHT_MULTIP / 2);
    }

}
