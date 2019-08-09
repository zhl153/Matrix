package com.laioffer.matrix;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.text.DecimalFormat;
import java.util.Locale;

public class ControlPanel extends AppCompatActivity {
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_panel);

        Toolbar toolbar = findViewById(R.id.toolbar); // set our own action bar
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar(); // get bar
        actionbar.setDisplayHomeAsUpEnabled(true); // set home button
        actionbar.setHomeAsUpIndicator(R.drawable.baseline_home_black_18dp); // add home indicator

        drawerLayout = findViewById(R.id.drawer_layout); // when to open

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
            new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    // set item as selected to persist highlight
                    menuItem.setChecked(true); // 监听
                    // close drawer when item is tapped
                    drawerLayout.closeDrawers();
                    // Add code here to update the UI based on the item selected
                    // For example, swap UI fragments here
                    if (menuItem.getItemId() == R.id.drawer_logout) { // 点击logout（id）则logout
                        Config.username = null;
                        logout();
                    }
                    return true;
                }
            });

        // location tracker
        final LocationTracker mLocationTracker = new LocationTracker(this);
        drawerLayout.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        // Respond when the drawer's position changes
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        final TextView user_textview = (TextView) drawerView.findViewById(R.id.user_name);
                        final TextView location_textview = (TextView) drawerView.findViewById(R.id.user_location);

                        // Respond when the drawer is opened
                        mLocationTracker.getLocation();
                        final double longitude = mLocationTracker.getLongitude();
                        final double latitude = mLocationTracker.getLatitude();

                        if (Config.username == null) { // logged out
                            user_textview.setText("");
                            location_textview.setText("");
                        } else {
                            user_textview.setText(Config.username);
                            // String.format(Locale.getDefault(),"%.2f", longitude), String.format(Locale.getDefault(), "%.2f", latitude)
//                            location_textview.setText(getString(R.string.gps_string, longitude, latitude));
                            location_textview.setText("Lat=" + new DecimalFormat(".##").
                                    format(latitude) + ",Lon=" + new DecimalFormat(".##").
                                    format(longitude));
                        }
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        // Respond when the drawer is closed
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        // Respond when the drawer motion state changes
                    }
                });
        // add Fragment to the activity
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, MainFragment.newInstance()).commit();
    }
    private void logout() { // 登出操作
        Intent intent = new Intent(this, OnBoardingActivity.class);
        startActivity(intent);
        finish(); // 不保存在backstack，直接destroy， 不finish则可后退回界面
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { // when click home open drawer
        switch (item.getItemId()) {
            case android.R.id.home: // 点击home
                drawerLayout.openDrawer(GravityCompat.START); // 打开左边导航栏
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
