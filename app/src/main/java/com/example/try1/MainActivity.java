package com.example.try1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.try1.ui.home.homeFragment;
import com.example.try1.ui.info.infoFragment;
import com.example.try1.ui.map.MapsFragment;
import com.example.try1.ui.myLocFrag.MyLocFrag;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadFragment(new homeFragment());
        BottomNavigationView bottomNavigationView = findViewById(R.id.botnav);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLay, fragment)
                    .addToBackStack(null)
                    .commit();
            return true;
        }
        return false;
    }
    @Override

    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        int itemId = menuItem.getItemId();
        if (itemId == R.id.home_frag) {
            fragment = new homeFragment();
        } else if (itemId == R.id.map_frag) {
            fragment = new MapsFragment();
        } else if (itemId == R.id.myloc_frag) {
            fragment = new MyLocFrag();
        } else if (itemId == R.id.info_frag) {
            fragment = new infoFragment();
        }
        return loadFragment(fragment);
    }
    /* I don't know why this switch case doesn't work, so I used if else statement
    switch (itemId){
            case R.id.home_frag:
                fragment = new homeFragment();
                break;
            case R.id.map_frag:
                fragment = new MapsFragment();
                break;
            case R.id.info_frag:
                fragment = new infoFragment();
                break;
        }*/
}

