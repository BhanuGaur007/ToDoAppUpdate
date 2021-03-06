package com.example.to_do_app_final.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
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
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.to_do_app_final.R;
import com.example.to_do_app_final.fragment.FavFragment;
import com.example.to_do_app_final.fragment.HomeFragment;
import com.example.to_do_app_final.fragment.Notes;
import com.example.to_do_app_final.fragment.Profile;
import com.example.to_do_app_final.fragment.TaskList;
import com.example.to_do_app_final.manager.PrefranceManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AssignTask extends AppCompatActivity {
    ImageView ivTOp, ivBottom, iv_add;
    AlertDialog.Builder builder;
    Toolbar toolbar;

    CharSequence charSequence;
    int index;
    long delay = 200;
    Handler handler = new Handler();


    String value = " ";
    String docId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_task);
        ivTOp = findViewById(R.id.iv_top);
        ivBottom = findViewById(R.id.iv_bottom);
        iv_add = findViewById(R.id.iv_add);
        builder = new AlertDialog.Builder(this);
        setTitle("Task");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.app_color)));
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Animation animationtop = AnimationUtils.loadAnimation(this,R.anim.top_wave);
        ivTOp.setAnimation(animationtop);
        Animation animationBottom = AnimationUtils.loadAnimation(this,R.anim.bottom_wave);
        ivBottom.setAnimation(animationBottom);

        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(iv_add,
                PropertyValuesHolder.ofFloat("scaleX",1.2f),
                PropertyValuesHolder.ofFloat("scaleY",1.2f)
        );
        objectAnimator.setDuration(400);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        objectAnimator.start();
        addNotesFragment(new TaskList());
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:

                        addNotesFragment(new Notes());

                        Toast.makeText(AssignTask.this, "Home", Toast.LENGTH_SHORT).show();

                        break;
                    case R.id.action_profile:
                        addNotesFragment(new Profile());
                        Toast.makeText(AssignTask.this, "Profile", Toast.LENGTH_SHORT).show();

                        break;
                    case R.id.action_fav:
                        addNotesFragment(new FavFragment());
                        //Toast.makeText(NameOfNotesHolder.this, "Fav", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
    }

    private void addNotesFragment(Fragment fragment) {
        docId=getIntent().getStringExtra("docId");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Bundle bundle=new Bundle();
        bundle.putString("docId",docId);
        fragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.taskList, fragment);
        fragmentTransaction.commit();

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
                new PrefranceManager(AssignTask.this).logOut();
                Intent intent = new Intent(AssignTask.this, Login.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
