package com.iboy.game.objects;

import com.iboy.game.main.AppConstants;

public class Player {
    int x=100, y= AppConstants.HALFSCREEN_HEIGHT;
    int playerSize=4;
    int y_speed=4;
    int playerSpeed=1;

    public void reset() {
        x=100;
        y=AppConstants.HALFSCREEN_HEIGHT;
    }
}
