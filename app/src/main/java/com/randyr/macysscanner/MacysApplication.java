package com.randyr.macysscanner;

import android.app.Application;

/**
 * Created by Isa on 4/4/18.
 */

public class MacysApplication extends Application {

    private static boolean visible;

    public static boolean isVisible() {
        return visible;
    }

    public static void setVisible(boolean visible) {
        MacysApplication.visible = visible;
    }
}
