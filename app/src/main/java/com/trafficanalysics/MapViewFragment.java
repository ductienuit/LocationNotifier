package com.trafficanalysics;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.GeoPosition;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.common.PositioningManager;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapFragment;
import com.here.android.mpa.search.AutoSuggest;
import com.here.android.mpa.search.AutoSuggestQuery;
import com.here.android.mpa.search.AutoSuggestSearch;
import com.here.android.mpa.search.DiscoveryResult;
import com.here.android.mpa.search.DiscoveryResultPage;
import com.here.android.mpa.search.ErrorCode;
import com.here.android.mpa.search.PlaceLink;
import com.here.android.mpa.search.ResultListener;
import com.here.android.mpa.search.SearchRequest;
import com.trafficanalysics.search.ResultListActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapViewFragment extends Fragment {
    public static List<DiscoveryResult> s_ResultList;
    // permissions request code
    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;

    /**
     * Permissions that need to be explicitly requested from end user.
     */
    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE };

    private Map map = null;
    private MapFragment mapFragment = null;

    private boolean paused = false;

    private PositioningManager positioningManager;

    static MapViewFragment mInstance;



    public static MapViewFragment getmInstance() {
        if(mInstance==null){
            mInstance = new MapViewFragment();
        }
        return mInstance;
    }

    public MapViewFragment() {

    }

    private static View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_map, container, false);
        } catch (InflateException e) {
            /* map is already there, just return view as it is */
        }

        addControl();
        init();
        return view;
    }

    private void addControl() {
        SearchView btnSearch = view.findViewById(R.id.btnSearch);
        btnSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                SearchRequest search = new SearchRequest("Hello");
                search.setQueryText(s);
                search.execute(new ResultListener< DiscoveryResultPage >(){

                    @Override
                    public void onCompleted(DiscoveryResultPage discoveryResultPage, ErrorCode errorCode) {
                        if (errorCode == ErrorCode.NONE) {

                            s_ResultList = discoveryResultPage.getItems();
                            for (DiscoveryResult item : s_ResultList) {
                                /*
                                 * Add a marker for each result of PlaceLink type.For best usability, map can be
                                 * also adjusted to display all markers.This can be done by merging the bounding
                                 * box of each result and then zoom the map to the merged one.
                                 */
                                if (item.getResultType() == DiscoveryResult.ResultType.PLACE) {
                                    PlaceLink placeLink = (PlaceLink) item;
                                    //addMarkerAtPlace(placeLink);
                                }
                            }
                            Intent intent = new Intent(getActivity(), ResultListActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getActivity(),
                                    "ERROR:Discovery search request returned return error code+ " + errorCode,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                return false;
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (positioningManager != null) {
            positioningManager.stop();
            paused = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (positioningManager != null) {
            positioningManager.start(PositioningManager.LocationMethod.GPS_NETWORK);
            paused = false;
        }
    }

    @Override
    public void onDestroy() {
        if (positioningManager != null) {
            positioningManager.removeListener(posListener);
        }
        map = null;
        super.onDestroy();
    }

    private com.here.android.mpa.mapping.MapFragment getMapFragment() {
        return (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map);
    }

    private void init() {
        mapFragment = getMapFragment();

        mapFragment.init(new OnEngineInitListener() {
            @Override
            public void onEngineInitializationCompleted(Error error) {
                if (error == Error.NONE) {
                    map = mapFragment.getMap();
                    map.setCenter(new GeoCoordinate(10.855575, 106.788788, 0.0),
                            Map.Animation.LINEAR);
                    map.setZoomLevel((map.getMaxZoomLevel() + map.getMinZoomLevel()) / 2);


                    positioningManager = PositioningManager.getInstance();
                    positioningManager.addListener(new WeakReference<>(posListener));
                    positioningManager.start(PositioningManager.LocationMethod.GPS_NETWORK);
                    map.getPositionIndicator().setVisible(true);

                } else {
                    System.out.println("Error: Cannot initialize Map Fragment");
                }
            }
        });
    }

    protected void checkPermission() {
        final List<String> missingPermissions = new ArrayList<String>();
        // check all required dynamic permissions
        for (final String permission : REQUIRED_SDK_PERMISSIONS) {
            final int result = ContextCompat.checkSelfPermission(getActivity(), permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }
        if (!missingPermissions.isEmpty()) {
            // request all missing permissions
            final String[] permissions = missingPermissions
                    .toArray(new String[missingPermissions.size()]);
            ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS,
                    grantResults);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                for (int index = permissions.length - 1; index >= 0; --index) {
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        // exit the app if one permission is not granted
                        Toast.makeText(getActivity(), "Required permission '" + permissions[index]
                                + "' not granted, exiting", Toast.LENGTH_LONG).show();
//                        finish();
                        return;
                    }
                }
                // all permissions were granted
                init();
                break;
        }
    }


    private PositioningManager.OnPositionChangedListener posListener = new PositioningManager.OnPositionChangedListener() {
        @Override
        public void onPositionUpdated(PositioningManager.LocationMethod locationMethod, GeoPosition geoPosition, boolean b) {
            if (!paused) {
                map.setCenter(geoPosition.getCoordinate(), Map.Animation.LINEAR);
            }
        }

        @Override
        public void onPositionFixChanged(PositioningManager.LocationMethod locationMethod, PositioningManager.LocationStatus locationStatus) {

        }
    };
}
