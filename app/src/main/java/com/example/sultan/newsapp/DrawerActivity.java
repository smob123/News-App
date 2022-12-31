package com.example.sultan.newsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar; //the top toolbar
    private String region = "us"; //the default region for the news api
    private MainActivity activity; //main activity instance to handle different navigation drawer screens
    private CollapsingToolbarLayout collapsingToolbar;
    private ImageView toolbarBackground; //the background image for the collapsing toolbar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        collapsingToolbar = findViewById(R.id.collapsingToolbar);
        toolbarBackground = findViewById(R.id.toolbarImage);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setTitle("Headlines");

        //check if there is a value passed from regionActivity

        if (getIntent().getExtras() != null) {
            region = getIntent().getExtras().getString("code");
        }

        //set the start tab to be the trending tab
        onNavigationItemSelected(navigationView.getMenu().getItem(0));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            activity.onBackPressed();
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.region) {
            startActivity(new Intent(DrawerActivity.this, RegionActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //create new fragment and change the collapsing tool bar's text
        collapsingToolbar.setTitle(item.toString());

        activity = new MainActivity();

        if (id == R.id.headlines) {
            activity.setUrl("https://newsapi.org/v2/top-headlines?country=" + region + "&apiKey=1b3db723c84947058381da0ff4b821f7");
            toolbarBackground.setImageResource(R.drawable.headlines);
        } else {
            activity.setUrl("https://newsapi.org/v2/everything?q=" + item.toString() + "&apiKey=1b3db723c84947058381da0ff4b821f7");

            //set the header image
            switch (id) {
                case R.id.science:
                    toolbarBackground.setImageResource(R.drawable.science);
                    break;
                case R.id.weather:
                    toolbarBackground.setImageResource(R.drawable.weather);
                    break;
                case R.id.tech:
                    toolbarBackground.setImageResource(R.drawable.tech);
                    break;
                case R.id.sports:
                    toolbarBackground.setImageResource(R.drawable.sport);
                    break;
                case R.id.food:
                    toolbarBackground.setImageResource(R.drawable.food);
                    break;
                case R.id.stock:
                    toolbarBackground.setImageResource(R.drawable.stock);
                    break;
            }
        }

        //setup the new fragment and create a transaction
        Fragment activityFrag = activity;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        //replace view
        transaction.replace(R.id.content_frame, activityFrag);

        //add transaction to back stack
        transaction.addToBackStack(null);

        //commit transaction
        transaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
