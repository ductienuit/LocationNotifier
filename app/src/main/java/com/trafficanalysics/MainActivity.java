package com.trafficanalysics;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationItem;
import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationView;
import com.luseen.luseenbottomnavigation.BottomNavigation.OnBottomNavigationItemClickListener;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;
import com.transitionseverywhere.extra.Scale;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ActionBar toolbar;
    FrameLayout contentContainer;
    ArrayList<Fragment> arrFragment = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFragment();

        addControl();

        toolbar.setTitle("Lịch thi đấu");

        loadFragment(arrFragment.get(0));
    }

    private void initFragment() {
        arrFragment.add(new FavoritesFragment());
        arrFragment.add(new MapViewFragment());
        arrFragment.add(new SettingFragment());
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private void addControl() {
        toolbar = getSupportActionBar();
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);

        if (bottomNavigationView != null) {
            bottomNavigationView.isWithText(true);
            //bottomNavigationView.activateTabletMode();
            bottomNavigationView.isColoredBackground(false);
            bottomNavigationView.setTextActiveSize(getResources().getDimension(R.dimen._9sdp));
            bottomNavigationView.setTextInactiveSize(getResources().getDimension(R.dimen._9sdp));
            bottomNavigationView.setItemActiveColorWithoutColoredBackground(ContextCompat.getColor(this, R.color.activeItem));
            //bottomNavigationView.setFont(Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/"));

        }

        contentContainer = findViewById(R.id.contentContainer);

        addNavItem();

        addEvent();
    }
    private void addEvent() {
        bottomNavigationView.setOnBottomNavigationItemClickListener(new OnBottomNavigationItemClickListener() {
            @Override
            public void onNavigationItemClick(int index) {
                TransitionSet set = new TransitionSet()
                        .addTransition(new Scale(0.7f))
                        .addTransition(new Fade())
                        .setInterpolator(new LinearOutSlowInInterpolator());

                switch (index) {
                    case 0:
                        TransitionManager.beginDelayedTransition(contentContainer, set);
                        toolbar.setTitle("Favorites");
                        loadFragment(arrFragment.get(0));
                        break;
                    case 1:
                        TransitionManager.beginDelayedTransition(contentContainer, set);
                        toolbar.setTitle("Map");
                        loadFragment(arrFragment.get(1));
                        break;
                    case 2:
                        TransitionManager.beginDelayedTransition(contentContainer, set);
                        toolbar.setTitle("Setting");
                        loadFragment(arrFragment.get(2));
                        break;
                }
            }
        });
    }

    private void addNavItem() {
        BottomNavigationItem dateItem = new BottomNavigationItem
                ("Yêu thích", ContextCompat.getColor(this, R.color.colorPrimary), R.drawable.ic_star_border);
        BottomNavigationItem channelItem = new BottomNavigationItem
                ("Bản đồ", ContextCompat.getColor(this, R.color.colorPrimary), R.drawable.ic_explore);
        BottomNavigationItem newItem = new BottomNavigationItem
                ("Cài đặt", ContextCompat.getColor(this, R.color.colorPrimary), R.drawable.ic_settings);

        bottomNavigationView.addTab(dateItem);
        bottomNavigationView.addTab(channelItem);
        bottomNavigationView.addTab(newItem);
    }
}
