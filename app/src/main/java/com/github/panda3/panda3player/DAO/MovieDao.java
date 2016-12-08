package com.github.panda3.panda3player.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;

import com.github.panda3.panda3player.model.Movie;
import com.github.panda3.panda3player.model.sqlite.MovieTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pstrzesak on 2016-12-08.
 */

public class MovieDao {
    private static final String Insert = "insert into "+ MovieTable.TABLE_NAME +
            "(" + MovieTable.MovieColumns.URI + ") values (?)";

    private SQLiteDatabase db;
    private SQLiteStatement insertStatement;

    public MovieDao(SQLiteDatabase db){
        this.db = db;
        insertStatement = db.compileStatement(MovieDao.Insert);
    }

    public long save(Movie entity){
        insertStatement.clearBindings();
        insertStatement.bindString(1, entity.getUri());
        return insertStatement.executeInsert();
    }

    public void update(Movie entity){
        final ContentValues values = new ContentValues();
        values.put(MovieTable.MovieColumns.URI, entity.getUri());
        db.update(MovieTable.TABLE_NAME, values, BaseColumns._ID + " = ?", new String[]{String.valueOf(entity.getId())});
    }

//    public void updateFavourite(Movie entity){
//        final ContentValues values = new ContentValues();
//        values.put(MovieTable.MovieColumns.FAVOURITE, entity.getFavourite());
//        db.update(MovieTable.TABLE_NAME, values, BaseColumns._ID + " = ?", new String[]{String.valueOf(entity.getId())});
//    }

//    public void updateProgress(Movie entity){
//        final ContentValues values = new ContentValues();
//        values.put(MovieTable.MovieColumns.PROGRESS, entity.getProgress());
//        db.update(MovieTable.TABLE_NAME, values, BaseColumns._ID + " = ?", new String[]{String.valueOf(entity.getId())});
//    }

    public void delete(Movie entity){
        if(entity.getId()>0)
            db.delete(MovieTable.TABLE_NAME, BaseColumns._ID + " = ?", new String[]{String.valueOf(entity.getId())});
    }

    public Movie get(long id){
        Movie movie = null;
        Cursor c = db.query(MovieTable.TABLE_NAME, new String[]{BaseColumns._ID, MovieTable.MovieColumns.URI},
                            BaseColumns._ID + " = ?", new String[]{String.valueOf(id)},null,null,null,"1");
        if(c.moveToFirst()){
            movie = new Movie(c.getString(1));
            movie.setId(c.getLong(0));
        }
        if(!c.isClosed())
            c.close();

        return movie;
    }

    public List<Movie> getALL(){
        List<Movie> list = new ArrayList<>();
        Cursor c = db.query(MovieTable.TABLE_NAME, new String[]{BaseColumns._ID, MovieTable.MovieColumns.URI},
                            null, null, null, null, MovieTable.MovieColumns.URI, null);
        if(c.moveToFirst()) {
            do {
                Movie movie = new Movie(c.getString(1));
                movie.setId(c.getLong(0));
                list.add(movie);
            } while (c.moveToNext());
        }
        if(!c.isClosed())
            c.close();

        return list;
    }

//    public List<Movie> getAllFavourite(Integer fav){
//        List<Movie> list = new ArrayList<>();
//        String sql = "select _id, url from " + MovieTable.TABLE_NAME + "where " + MovieTable.MovieColumns.FAVOURITE
//                + " > ? ";
//        Cursor c = db.rawQuery(sql, new String[]{fav.toString()});
//        if(c.moveToFirst()) {
//            do {
//                Movie movie = new Movie(c.getString(1));
//                movie.setId(c.getLong(0));
//                list.add(movie);
//            } while (c.moveToNext());
//        }
//        if(!c.isClosed())
//            c.close();
//
//        return list;
//    }

//        public List<Movie> getAllProgress(){
//        List<Movie> list = new ArrayList<>();
//        String sql = "select _id, url from " + MovieTable.TABLE_NAME + "where " + MovieTable.MovieColumns.PROGRESS
//                + " < ? ";
//        Cursor c = db.rawQuery(sql, new String[]{"100"});
//        if(c.moveToFirst()) {
//            do {
//                Movie movie = new Movie(c.getString(1));
//                movie.setId(c.getLong(0));
//                list.add(movie);
//            } while (c.moveToNext());
//        }
//        if(!c.isClosed())
//            c.close();
//
//        return list;
//    }


    public Movie find(String uri){
        Movie movie = null;
        String sql = "select _id, url from " + MovieTable.TABLE_NAME + "where upper(" + MovieTable.MovieColumns.URI
                        + ") = ? limit 1";
        Cursor c = db.rawQuery(sql, new String[]{uri.toUpperCase()});
        if(c.moveToFirst()){
            movie = new Movie(c.getString(1));
            movie.setId(c.getLong(0));
        }
        if(!c.isClosed())
            c.close();

        return movie;
    }

}
