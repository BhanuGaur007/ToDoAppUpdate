package com.example.to_do_app_final.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.to_do_app_final.R;
import com.example.to_do_app_final.fragment.HomeFragment;
import com.example.to_do_app_final.fragment.TaskList;
import com.example.to_do_app_final.manager.PrefranceManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class RecyclerView extends AppCompatActivity {
    ImageView ivTOp,ivBottom,iv_add;
    CharSequence charSequence;
    int index;
    long delay = 200;
    Handler handler = new Handler();

    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        ivTOp = findViewById(R.id.iv_top);
        ivBottom = findViewById(R.id.iv_bottom);
        iv_add = findViewById(R.id.iv_add);

        setTitle("Task List");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Animation animationtop = AnimationUtils.loadAnimation(this,R.anim.top_wave);
        ivTOp.setAnimation(animationtop);
        Animation animationBottom = AnimationUtils.loadAnimation(this,R.anim.bottom_wave);
        ivBottom.setAnimation(animationBottom);

        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(iv_add,
                PropertyValuesHolder.ofFloat("scaleX",1.2f),
                PropertyValuesHolder.ofFloat("scaleY",1.2f)
        );
        objectAnimator.setDuration(200);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        objectAnimator.start();

        addFragment(new TaskList());
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Intent homeIntent = new Intent(RecyclerView.this,MainActivity.class);
                        startActivity(homeIntent);
                        Toast.makeText(RecyclerView.this, "Home", Toast.LENGTH_SHORT).show();

                        break;
                    case R.id.action_profile:
                        Intent intent = new Intent(RecyclerView.this, Login.class);
                        startActivity(intent);
                        break;
                    case R.id.action_fav:
                        Toast.makeText(RecyclerView.this, "Fav", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logOutOption:
                new PrefranceManager(RecyclerView.this).logOut();
                Intent intent = new Intent(RecyclerView.this, Login.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void addFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.add_fragment, fragment);
        fragmentTransaction.commit();
    }
}