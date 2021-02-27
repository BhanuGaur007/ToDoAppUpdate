package com.example.to_do_app_final.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.to_do_app_final.Activity.MainActivity;
import com.example.to_do_app_final.Activity.NameOfNotesHolder;
import com.example.to_do_app_final.DataBaseFile.DbHelper;
import com.example.to_do_app_final.R;
import com.example.to_do_app_final.manager.PrefranceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Login_Frgment extends Fragment {
    DbHelper dbHelper;
    ImageView ivTOp, ivBottom;
    TextInputEditText userName,password;
    Button forgetPass,login_btn;
    ImageButton facebook, gmail,twitter;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login__frgment, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        ivTOp = view.findViewById(R.id.iv_top);
        ivBottom =view.findViewById(R.id.iv_bottom);
        userName = view.findViewById(R.id.userName);
        password = view.findViewById(R.id.password);
        forgetPass =view.findViewById(R.id.forgetPass);
        login_btn = view.findViewById(R.id.login_btn);
//        signUp_login = view.findViewById(R.id.signUp_lgn);
        facebook = view.findViewById(R.id.facebook);
        gmail = view.findViewById(R.id.gmail);
        twitter = view.findViewById(R.id.twitter);
        dbHelper = new DbHelper(getContext());
        facebookLogin(view);
        gmailLogin(view);
        twitterLogin(view);
        loginForm(view);

        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // for animation top

        Animation animationtop = AnimationUtils.loadAnimation(getContext(),R.anim.top_wave);
        ivTOp.setAnimation(animationtop);


        // for animation bottom
        Animation animationBottom = AnimationUtils.loadAnimation(getContext(),R.anim.bottom_wave);
        ivBottom.setAnimation(animationBottom);



        return view;

    }
    private void loginForm( View view){

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginCheckerLoginForm();
            }
        });
    }

    private void  loginCheckerLoginForm(){


        verifyFromSQLite();
        //new PrefranceManager(getContext()).loginChecker();
    }
    private void verifyFromSQLite() {
        if (dbHelper.checkUser(userName.getText().toString().trim())){

        }
        else {
            userName.setFocusable(true);
            userName.setError("Invalid Email");
        }
        if (dbHelper.checkUser(userName.getText().toString().trim(),password.getText().toString().trim())){
            loginAuthwithFireBase(userName.getText().toString().trim(),password.getText().toString().trim());
            emptyInputEditText();


        }
       else {
           // Snackbar.make(layout_design_fix,getString(R.string.error_email_exists),Snackbar.LENGTH_LONG).show();


            password.setFocusable(true);
            password.setError("Invalid password");
        }
    }
    private void emptyInputEditText() {
        userName.setText(null);
        password.setText(null);

    }

    private void facebookLogin(View view){
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String google = "http://www.google.com/";
                String urlFacebook = "http://www.facebook.com/";
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlFacebook)));
                }catch (Exception e){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlFacebook)));
                }
            }
        });

    }
    private void gmailLogin(View view){
        gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String google = "http://www.google.com/";
                String urlFacebook = "http://www.gmail.com/";
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlFacebook)));
                }catch (Exception e){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlFacebook)));
                }

            }
        });
    }
    private void twitterLogin(View view){
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String google = "http://www.google.com/";
                String urlFacebook = "http://www.twitter.com/";
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlFacebook)));
                }catch (Exception e){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlFacebook)));
                }
            }
        });
    }

private void loginAuthwithFireBase(String userName, String password){
    mAuth.signInWithEmailAndPassword(userName, password)
            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("Success", "signInWithEmail:success");

                        FirebaseUser user = mAuth.getCurrentUser();

                            new PrefranceManager(getContext()).saveSignUpDetails(userName,password);
                            Intent loginIntent = new Intent(getContext(),NameOfNotesHolder.class);
                            startActivity(loginIntent);




                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("Failed", "signInWithEmail:failure", task.getException());
                        Toast.makeText(getContext(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();

                    }

                    // ...
                }
            });
}



}