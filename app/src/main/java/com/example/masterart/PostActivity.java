package com.example.masterart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.masterart.Fragment.HomeFragment;
import com.example.masterart.Fragment.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PostActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

    }

}
