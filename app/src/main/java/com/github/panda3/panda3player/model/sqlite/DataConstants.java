package com.github.panda3.panda3player.model.sqlite;

/**
 * Created by VuYeK on 06.12.2016.
 */

import android.os.Environment;

public class DataConstants {

    private static final String APP_PACKAGE_NAME = "com.github.panda3.panda3player";

    private static final String EXTERNAL_DATA_DIR_NAME = "pd3player";
    public static final String EXTERNAL_DATA_PATH =
            Environment.getExternalStorageDirectory() + "/" + DataConstants.EXTERNAL_DATA_DIR_NAME;

    public static final String DATABASE_NAME = "pd3player.db";
    public static final String DATABASE_PATH =
            Environment.getDataDirectory() + "/data/" + DataConstants.APP_PACKAGE_NAME + "/databases/"
                    + DataConstants.DATABASE_NAME;

    private DataConstants() {
    }
}
