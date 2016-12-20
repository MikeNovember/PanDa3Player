package com.github.panda3.panda3player;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.panda3.panda3player.manager.DataManager;

import java.util.ArrayList;
import java.util.List;


public class FragmentListFavourites extends Fragment {

    private ListView list;
    private List<String> exampleList;

    private DataManager dataManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_list_favourites, container, false);

        exampleList = new ArrayList<>();

//        exampleList.add("fav1");
//        exampleList.add("fav2");
//        exampleList.add("fav2");

        exampleList=dataManager.getFavourite(5);


        list = (ListView) view.findViewById(R.id.favouritesList);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, exampleList);
        list.setAdapter(arrayAdapter);

//        list.setOnLongClickListener(new AdapterView.OnItemLongClickListener(){
//            @Override
//            public void onItemLongClick(AdapterView<?> parent, View view, int position, long id){
//                String uri = String.valueOf(parent.getAdapter().getItem(position));
//                dataManager.deleteFromFavourites(uri);
//            }
//        });

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Favourite Videos");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        AdapterView.OnItemLongClickListener listener = new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String uri = String.valueOf(parent.getAdapter().getItem(position));
                dataManager.deleteFromFavourites(uri);
                return false;
            }
        };

    }
}
