package com.github.panda3.panda3player.model.sqlite;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * Created by VuYeK on 06.12.2016.
 */
public class MovieTable {

    public static final String TABLE_NAME = "movie";

    public static class MovieColumns implements BaseColumns {
        public static final String URI = "uri";
        public static final String PROGRESS = "progress";
        public static final String FAVOURITE = "favourite";
    }

    public static void onCreate(SQLiteDatabase db) {
        StringBuilder sb = new StringBuilder();

        sb.append("CREATE TABLE " + MovieTable.TABLE_NAME + " (");
        sb.append(BaseColumns._ID + " INTEGER PRIMARY KEY, ");
        sb.append(MovieColumns.URI + " TEXT, ");
        sb.append(MovieColumns.PROGRESS + " INTEGER, ");
        sb.append(MovieColumns.FAVOURITE + " INTEGER");
        sb.append(");");
        db.execSQL(sb.toString());
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieTable.TABLE_NAME);
        MovieTable.onCreate(db);
    }

}
