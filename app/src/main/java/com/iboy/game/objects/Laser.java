package com.iboy.game.objects;

import android.util.Log;

import com.iboy.game.handlers.RotationHandler;
import com.iboy.game.main.AppConstants;

public class Laser {
    public static final int DIAMETER = 50;
    double _x, _y;
    int squares, selectedSquare;
    float durationMilisecond = 2000;

    public Laser(int squares, int selectedSquare) {
        this.squares = squares;
        this.selectedSquare = selectedSquare;
        setPosition();
    }

    private void setPosition() {
        //TODO laser gk nengah soalnya klo bitmap starting position benar, tapi dari gambar jadi memanjang ke kanan bawah
        switch (selectedSquare) {
            case 0:
                _x = AppConstants.HALFSCREEN_WIDTH / 2;
                _y = AppConstants.HALFSCREEN_HEIGHT / 2;
                break;
            case 1:
                _x = AppConstants.HALFSCREEN_WIDTH / 2+AppConstants.HALFSCREEN_WIDTH;
                _y = AppConstants.HALFSCREEN_HEIGHT / 2;
                break;
            case 2:
                _x = AppConstants.HALFSCREEN_WIDTH / 2;
                _y = AppConstants.HALFSCREEN_HEIGHT / 2+AppConstants.HALFSCREEN_HEIGHT;
                break;
            case 3:
                _x = AppConstants.HALFSCREEN_WIDTH / 2+AppConstants.HALFSCREEN_WIDTH;
                _y = AppConstants.HALFSCREEN_HEIGHT / 2+AppConstants.HALFSCREEN_HEIGHT;
                break;
        }
    }
}
