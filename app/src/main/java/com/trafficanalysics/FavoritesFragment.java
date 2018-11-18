package com.trafficanalysics;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.trafficanalysics.adapter.FavoritesAdapter;
import com.trafficanalysics.modal.Favorite;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritesFragment extends Fragment {

    RecyclerView mRecyclerView;
    FavoritesAdapter mAdapter;
    public FavoritesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_favorites, container, false);
        final Button addItem = view.findViewById(R.id.addItem);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem1();
            }
        });

        mRecyclerView = view.findViewById(R.id.listFavorites);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);

        {
            list.add(new Favorite("Vườn ươm doanh nghiệp Công nghệ cao","Khu K1-G3, Đường D1, Khu Công nghệ cao, Phường Tân Phú, Quận 9, Hồ Chí Minh"));
            list.add(new Favorite("Suối tiên","120 Xa lộ Hà Nội, Phường Tân Phú, Quận 9, Hồ Chí Minh "));
            list.add(new Favorite("Ho Chi Minh City University of Information Technology","Khu phố 6, P.Linh Trung, Q.Thủ Đức, Tp.Hồ Chí Minh."));
            list.add(new Favorite("Ngã tư Thủ Đức","Quận Thủ Đức, Hồ Chí Minh"));
        }

        mAdapter = new FavoritesAdapter(R.layout.item_favorite, getContext());
        mRecyclerView.setAdapter(mAdapter);
        addItem1();
        return view;
    }

    ArrayList<Favorite> list = new ArrayList<>();

    public void addItem1() {
        LocationApplication.getInstance().arrFavotites.setTheloai(list);
        mAdapter.notifyDataSetChanged();
    }
}
