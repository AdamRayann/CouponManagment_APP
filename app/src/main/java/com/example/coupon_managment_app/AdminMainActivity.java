package com.example.coupon_managment_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class AdminMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.main_drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new AdminFrag())
                    .commit();
            navigationView.setCheckedItem(R.id.nav_home);

        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation item clicks here

        if (item.getItemId() == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new AdminFrag())
                    .commit();
        }else if (item.getItemId() == R.id.nav_settings) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new SettingsFragment())
                    .commit();
        }else if (item.getItemId() == R.id.nav_about) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new AboutFragment())
                    .commit();
        }else if (item.getItemId() == R.id.nav_share) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ShareFragment())
                    .commit();
        } else if (item.getItemId() == R.id.nav_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(AdminMainActivity.this);
            builder.setTitle("Exit Confirmation");
            builder.setMessage("Are you sure you want to logout ?");

            //Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // User clicked "No," so do nothing
                    dialog.dismiss(); // Dismiss the dialog
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
