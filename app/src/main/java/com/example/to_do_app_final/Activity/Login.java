package com.example.to_do_app_final.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.Toolbar;

import com.example.to_do_app_final.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener;

public class Login extends AppCompatActivity {
    ViewPager viewpager;
    TabLayout tabLayout;
     MyAdapter adapter;
     Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        setTitle("Login / Sign Up");
//        getActionBar().hide();
        getSupportActionBar().hide();
    tabLayout = findViewById(R.id.tab_layout);
    viewpager = findViewById(R.id.viewPager);


    tabLayout.addTab(tabLayout.newTab().setText("Login"));
    tabLayout.addTab(tabLayout.newTab().setText("Sign Up"));
    tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    final MyAdapter adapter = new MyAdapter(this,getSupportFragmentManager(), tabLayout.getTabCount());
        viewpager.setAdapter(adapter);
        viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });







    }
}