package com.example.fahadali.diabetesapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.example.fahadali.diabetesapp.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class HomeMenu_activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, Runnable {


    Button blodsukker_BTN;
    Button påmindelser_BTN;
    Button testLogUd_BTN;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    User user;
    private TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_menu);

        User.getUserInstance().observers.add(this);

        user = User.getUserInstance();

        System.out.println("SNAPSHOT3: "+ User.getUserInstance());
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        tv = findViewById(R.id.nuværendeUser);

        System.out.println("KIG HEEER: "+user.getFirstName());


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        blodsukker_BTN = (Button) findViewById(R.id.bloodsukker_BTN);
        påmindelser_BTN = (Button) findViewById(R.id.påmindelser_BTN);
        testLogUd_BTN = findViewById(R.id.logUd_BTN);

        blodsukker_BTN.setOnClickListener(this);
        påmindelser_BTN.setOnClickListener(this);
        testLogUd_BTN.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        User.getUserInstance().observers.remove(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        if(v == blodsukker_BTN){
            Intent intent = new Intent(this, BSugarOverview_activity.class);
            startActivity(intent);
        }

        if(v == påmindelser_BTN){
            Intent intent = new Intent(this, ReminderTypeSelector_activity.class);
            startActivity(intent);
        }

        if(v == testLogUd_BTN){
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signOut();
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

            Log.i("CURRENT USER: ", "After sign out: "+firebaseUser);

            user.nullifyUser();
            System.out.println("SNAPSHOT2: "+ User.getUserInstance());
        finish();

        }

    }

    @Override
    public void run() {
        tv.setText(User.getUserInstance().getFirstName());
        System.out.println("SNAPSHOT3X: "+ User.getUserInstance());
    }
}
