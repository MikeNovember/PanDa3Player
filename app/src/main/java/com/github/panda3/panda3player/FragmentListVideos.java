package com.github.panda3.panda3player;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class FragmentListVideos extends ListFragment {

    private ListView list;
    private List<String> exampleList;
    final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 5;
    boolean getAccesToSDCard = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_list_videos, container, false);

        exampleList = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(getContext(),  Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {


            } else {

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);


            }
        }else{
            getAccesToSDCard = true;
        }

        if(getAccesToSDCard){
            Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            String[] projection = { MediaStore.Video.VideoColumns.DATA };
            Cursor c = getActivity().getApplicationContext().getContentResolver().query(uri, projection, null, null, null);
            int vidsCount = 0;
            if (c != null) {
                vidsCount = c.getCount();
                while (c.moveToNext()) {
                   // Log.d("VIDEO", c.getString(0));
                    exampleList.add(c.getString(0));
                }
                c.close();
            }
        }else{
            Log.d("PERMISSIONS","NO ACCES TO SD CARD !!!!");
        }

        setListAdapter(new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, exampleList));

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((Activity)context).setTitle("Browse Video");
    }

    public void onListItemClick(ListView lv, View v, final int position, long id) {
        super.onListItemClick(lv, v, position, id);
        Intent intentText = new Intent(getContext(),MainActivity.class);
        intentText.putExtra("uri", exampleList.get(position) );
        startActivity(intentText);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("PERMISSIONS","ok");
                    getAccesToSDCard = true;
                } else {
                    Log.d("PERMISSIONS","denied");
                }
                return;
            }
        }
    }
}
