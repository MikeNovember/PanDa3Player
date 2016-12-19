package com.github.panda3.panda3player;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.github.panda3.panda3player.manager.DataManager;
import com.github.panda3.panda3player.model.Movie;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DBUpdateService extends Service {
    public Runnable mRunnable = null;
    private DataManager mDataManager;
    private List<Movie> mMovies;
    private List<String> mMovieFiles;

    public DBUpdateService() { }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Gdzie się kuźwa ryjesz ?");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final Handler mHandler = new Handler();
        mDataManager  = new DataManager(this);
        mRunnable = new Runnable() {
            @Override
            public void run() {
                mMovieFiles = new ArrayList<String>();
                Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                String[] projection = { MediaStore.Video.VideoColumns.DATA };
                Cursor c = getApplicationContext().getContentResolver().query(uri, projection, null, null, null);
                int vidsCount = 0;
                if (c != null) {
                    vidsCount = c.getCount();
                    while (c.moveToNext()) {
                        mMovieFiles.add(c.getString(0));
                    }
                    c.close();
                }

                mMovies = mDataManager.getStudents();

                // delete all movies from db, that do not exist in the filesystem anymore
                for (Movie movie : mMovies) {
                    File file = new File(movie.getUri());
                    if (!file.exists()) {
                        mDataManager.deleteMovie(movie.getId());
                    }
                }

                // add to db all new movies
                for (String moviePath : mMovieFiles) {
                    boolean isInDB = false;
                    for (Movie dbMovie : mMovies) {
                        if (moviePath == dbMovie.getUri()) {
                            isInDB = true;
                            break;
                        }
                    }
                    if (!isInDB) {
                        mDataManager.saveMovie(new Movie(moviePath, 0));
                    }
                }

                //Toast.makeText(getApplicationContext(), "Updated movie database", Toast.LENGTH_LONG).show();
                mHandler.postDelayed(mRunnable, 10 * 1000);
            }
        };
        mHandler.postDelayed(mRunnable, 10 * 1000);

        return super.onStartCommand(intent, flags, startId);
    }
}
