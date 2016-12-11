package com.github.panda3.panda3player.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.github.panda3.panda3player.model.sqlite.DataConstants;
import com.github.panda3.panda3player.model.sqlite.MovieTable;


/**
 * Created by Dominik on 2016-11-26.
 */

public class OpenHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    private Context context;

    public OpenHelper(final Context context) {
        super(context, DataConstants.DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        MovieTable.onCreate(db);

    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        MovieTable.onUpgrade(db, oldVersion, newVersion);
    }
}
