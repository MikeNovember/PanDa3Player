package com.github.panda3.panda3player;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.github.panda3.panda3player.DAO.MovieDao;
import com.github.panda3.panda3player.manager.DataManager;

/**
 * Created by VuYeK on 24/11/16.
 */

public class FragmentVideo extends Fragment {
    private VideoView videoView;
    private int position = 0;
    private MediaController mediaController;
    private Button newActivityButton;
    private static final String LOG_TAG = "FRAGMENT VIDEO";
    private String uri;
    private long miliseconds = 0;
    View view;
    private ImageView banner;

    private DataManager dataManager;
    private SharedPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.video_fragment, container, false);

        videoView = (VideoView) view.findViewById(R.id.videoView);

        if (mediaController == null) {
            mediaController = new MediaController(getActivity(), true);

            // Set the videoView that acts as the anchor for the MediaController.
            mediaController.setAnchorView(videoView);
            // Set MediaController for VideoView
            videoView.setMediaController(mediaController);
        }

        banner = (ImageView) view.findViewById(R.id.banner);

        try {
            if(uri != null){
                videoView.setVideoURI(Uri.parse(uri));
            }
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        videoView.requestFocus();
        // When the video file ready for playback.
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mediaPlayer) {

                setPosition(dataManager.getProgress(getUri()));

                videoView.seekTo(position);
                if (position == 0) {
                    videoView.start();
                }
                // When video Screen change size.
                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        // Re-Set the videoView that acts as the anchor for the MediaController
                        mediaController.setAnchorView(videoView);
                    }
                });
            }
        });

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return true;
            }
        });

        newActivityButton = (Button) view.findViewById(R.id.fullscreenVideo);
        newActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startFullScreenActivity(view);
            }
        });

        Button button2 = (Button) view.findViewById(R.id.favAdd);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataManager.setFavourites(getUri(),10);
            }
        });
        return view;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setPosition(int ms) {
        position = ms;
    }

    public String getUri(){
        return uri;
    }

    public int getPosition(){
        return videoView.getCurrentPosition();
    }

    private void startFullScreenActivity(View view) {
        Log.i(LOG_TAG, "FullScreen enabled");
        Intent intent = new Intent(getActivity(), FullscreenActivity.class);
        intent.putExtra("uri", uri);
        intent.putExtra("position", videoView.getCurrentPosition());
        startActivityForResult(intent, 1);
    }

    // Find ID corresponding to the name of the resource (in the directory raw).
    public int getRawResIdByName(String resName) {
        String pkgName = this.getActivity().getPackageName();
        // Return 0 if not found.
        int resID = this.getResources().getIdentifier(resName, "raw", pkgName);
        Log.i("AndroidVideoView", "Res Name: " + resName + "==> Res ID = " + resID);
        return resID;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        position = data.getIntExtra("position", 0);
        uri = data.getStringExtra("uri");
        videoView.setVideoURI(Uri.parse(uri));
        videoView.seekTo(position);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Video Player");
    }


    // When you change direction of phone, this method will be called.
    // It store the state of video (Current position)
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        // Store current position.
        savedInstanceState.putInt("CurrentPosition", videoView.getCurrentPosition());
        savedInstanceState.putString("CurrentUri", uri);
        videoView.pause();
    }


    // After rotating the phone. This method is called.
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            //Restore the fragment's state here
            // Get saved position.
            position = savedInstanceState.getInt("CurrentPosition");
            uri = savedInstanceState.getString("CurrentUri");
            if(uri != null){
                videoView.setVideoURI(Uri.parse(uri));
                videoView.seekTo(position);
            }
        }
    }

    @Override
    public void onStop(){
        super.onStop();

        dataManager.setProgress(getUri(),getPosition());

    }
    public void setBannerImage(Uri imageUri) {
        Log.d("FRAGMENT", "Kurwa weszło");
        banner.setImageURI(imageUri);
    }
}
