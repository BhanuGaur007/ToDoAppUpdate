package com.example.to_do_app_final.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.to_do_app_final.R;
import com.example.to_do_app_final.fragment.FavFragment;
import com.example.to_do_app_final.fragment.HomeFragment;
import com.example.to_do_app_final.fragment.Profile;
import com.example.to_do_app_final.manager.PrefranceManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    ImageView ivTOp,ivBottom,iv_add;
    AlertDialog.Builder builder;
    Toolbar toolbar;

    CharSequence charSequence;
    int index;
    long delay = 200;
    Handler handler = new Handler();


    String value=" ";

    public FragmentCommunicator fragmentCommunicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivTOp = findViewById(R.id.iv_top);
        ivBottom = findViewById(R.id.iv_bottom);
        iv_add = findViewById(R.id.iv_add);
        builder = new AlertDialog.Builder(this);

        setTitle("LIST OF TASKHOLDER");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.app_color)));


           getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

           Animation animationtop = AnimationUtils.loadAnimation(this,R.anim.top_wave);
           ivTOp.setAnimation(animationtop);


           Animation animationBottom = AnimationUtils.loadAnimation(this,R.anim.bottom_wave);
           ivBottom.setAnimation(animationBottom);



        addFragment(new HomeFragment());
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav_main);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        Intent intent = new Intent(MainActivity.this, NameOfNotesHolder.class);
                        startActivity(intent);

//                        addFragment(new HomeFragment());
//                        Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();

                        break;
                    case R.id.action_profile:
                        addFragment(new Profile());
//                        Intent intent = new Intent(MainActivity.this, Login.class);
//                        startActivity(intent);
                        break;
                    case R.id.action_fav:
                        addFragment(new FavFragment());
                        Toast.makeText(MainActivity.this, "Fav", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
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
                new PrefranceManager(MainActivity.this).logOut();
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle=new Bundle();
        bundle.putString("bundle",value);
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.add_fragment, fragment);
        fragmentTransaction.commit();
    }

    public interface  FragmentCommunicator {
        public void sendData(String value);
    }



}