package com.github.panda3.panda3player.manager;

/**
 * Created by Dominik on 2016-11-27.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.github.panda3.panda3player.DAO.MovieDao;
import com.github.panda3.panda3player.DAO.OpenHelper;
import com.github.panda3.panda3player.model.Movie;
import com.github.panda3.panda3player.model.sqlite.DataConstants;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class DataManager {
    private Context context;

    private SQLiteDatabase db;

    private MovieDao movieDao;


    public DataManager(Context context) {

        this.context = context;

        SQLiteOpenHelper openHelper = new OpenHelper(this.context);
        db = openHelper.getWritableDatabase();

        movieDao = new MovieDao(db);

    }

    public SQLiteDatabase getDb() {
        return db;
    }

    private void openDb() {
        if (!db.isOpen()) {
            db = SQLiteDatabase.openDatabase(DataConstants.DATABASE_PATH, null, SQLiteDatabase.OPEN_READWRITE);

            movieDao = new MovieDao(db);
        }
    }

    private void closeDb() {
        if (db.isOpen()) {
            db.close();
        }
    }


    public Movie getMovie(long movieId) {
            Movie movie = movieDao.get(movieId);
        return movie;
    }

    public List<Movie> getStudents() {
        return movieDao.getALL();
    }


    public long saveMovie(Movie movie) {
        long movieID = 0L;
        try {
            db.beginTransaction();

            movieID = movieDao.save(movie);

            db.setTransactionSuccessful();
        } catch (SQLException e) {
            movieID = 0L;
        } finally {
            db.endTransaction();
        }

        return movieID;
    }


    public boolean deleteMovie(long movieId) {
        boolean result = false;

        try {
            db.beginTransaction();
            Movie movie = this.getMovie(movieId);
            if (movie != null) {

                movieDao.delete(movie);
            }
            db.setTransactionSuccessful();
            result = true;
        } catch (SQLException e) {
            Log.e("XXX", "Błąd przy usuwaniu filmu (transakcję wycofano)", e);
        } finally {
            db.endTransaction();
        }
        return result;
    }




    public long updateMovie(Movie movie) {
        long studentID = 0L;
        try {
            db.beginTransaction();


            movieDao.update(movie);

            db.setTransactionSuccessful();
        } catch (SQLException e) {
            studentID = 0L;
        } finally {
            db.endTransaction();
        }

        return studentID;
    }

   
}
