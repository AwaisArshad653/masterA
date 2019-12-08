package com.example.masterart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.masterart.Fragment.HomeFragment;
import com.example.masterart.Fragment.NotificationFragment;
import com.example.masterart.Fragment.ProfileFragment;
import com.example.masterart.Fragment.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {


    BottomNavigationView bottomNavigationView;
    Fragment selectedFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Initialization();

        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListner);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();

    }


    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListner =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch (menuItem.getItemId()){
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_search:
                            selectedFragment = new SearchFragment();
                            break;
                        case R.id.nav_add:
                            startActivity(new Intent(MainActivity.this ,PostActivity.class));
                            break;
                        case R.id.nav_favourite:
                            selectedFragment = new NotificationFragment();
                            break;
                        case R.id.nav_profile:
                            SharedPreferences.Editor editor = getSharedPreferences("PREFS",MODE_PRIVATE).edit();
                            editor.putString("profileid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                            editor.apply();
                            selectedFragment = new ProfileFragment();
                            break;

                    }

                    if(selectedFragment != null)
                    {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
                    }
                    return false;
                }
            };

    void Initialization()
    {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }
}
