package com.example.game.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.game.MainActivity;
import com.example.game.core.GameView;

public class ComplexImage {
    private Bitmap[][] parts;
    private float width, height;
    private String tag;

    public ComplexImage(Bitmap[][] images, float width, float height) {
        this.width = width;
        this.height = height;

        if (images == MainActivity.housingNorth) tag = "n";
        else if (images == MainActivity.housingEast) tag = "e";
        else if (images == MainActivity.housingSouth) tag = "s";
        else if (images == MainActivity.housingWest) tag = "w";

        for (int i = 0; i < images.length; i++) {
            for (int j = 0; j < images[i].length; j++) {
                if (images[i][j] != null) images[i][j] = Bitmap.createScaledBitmap(images[i][j], (int)(width * GameView.HEIGHT_MULTIP) + 1, (int)(height * GameView.HEIGHT_MULTIP) + 1, false);
            }
        }
        parts = images;
    }

    public String getTag() {
        return tag;
    }

    public void drawSpecifiedImages(Canvas c, Paint p, float left, float top, float plX, float plY) {
        int minPosX = (int)((-plX - left) / width);
        int minPosY = (int)((-plY - top) / height);

        int maxPosX = (int)((-plX + GameView.WIDTH / GameView.HEIGHT_MULTIP - left) / width);
        int maxPosY = (int)((-plY + GameView.HEIGHT / GameView.HEIGHT_MULTIP - top) / height);

        for (int i = Math.max(minPosX, 0); i <= maxPosX; i++) {
            if (i >= parts.length) break;
            for (int j = Math.max(minPosY, 0); j <= maxPosY; j++) {
                if (j >= parts[0].length) break;

                if (parts[i][j] != null) c.drawBitmap(parts[i][j], (plX + left + width * i) * GameView.HEIGHT_MULTIP, (plY + top + height * j) * GameView.HEIGHT_MULTIP, p);
            }
        }
    }
}
