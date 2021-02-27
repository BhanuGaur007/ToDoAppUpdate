package com.example.to_do_app_final.fragment;

import android.content.Intent;
import android.database.Cursor;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.to_do_app_final.Activity.MainActivity;
import com.example.to_do_app_final.Activity.NameOfNotesHolder;
import com.example.to_do_app_final.DataBaseFile.DbHelper;
import com.example.to_do_app_final.R;
import com.example.to_do_app_final.manager.PrefranceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import pl.droidsonroids.gif.GifImageView;

public class SignUp_Screen extends Fragment {

    DbHelper dbHelper;
    GifImageView progressBar;
    LinearLayout progressBarLayout;
    ImageView ivTOp, ivBottom;
    TextInputEditText name,password,phone,email;
    Button register;
    String emailValidation ;
    String passwordValidate ;
    String nameValidation;
    String phoneValidation;
    String genderValidate;
    boolean isGenderSelected=false;
    ImageButton facebook, gmail,twitter;
    RadioGroup gender;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String passPattern = "^" +
            //"(?=.*[0-9])" +         //at least 1 digit
            //"(?=.*[a-z])" +         //at least 1 lower case letter
            //"(?=.*[A-Z])" +         //at least 1 upper case letter
            "(?=.*[a-zA-Z])" +      //any letter
            "(?=.*[@#$%^&+=])" +    //at least 1 special character
            "(?=\\S+$)" +           //no white spaces
            ".{4,}" +               //at least 4 characters
            "$";
    String phonePattern = "[0-9]{10}";
    String genderValue="MALE";
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseFirestore db;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sign_up__screen, container, false);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();
        ivTOp = view.findViewById(R.id.iv_top);
        ivBottom =view.findViewById(R.id.iv_bottom);
        name = view.findViewById(R.id.name);
        password = view.findViewById(R.id.password);
        phone = view.findViewById(R.id.phone);
        email = view.findViewById(R.id.email);
        gender = view.findViewById(R.id.gender);
        register = view.findViewById(R.id.register);
        facebook = view.findViewById(R.id.facebook);
        gmail = view.findViewById(R.id.gmail);
        twitter = view.findViewById(R.id.twitter);
        dbHelper = new DbHelper(getContext());
        progressBar = view.findViewById(R.id.progressBar);
        progressBarLayout = view.findViewById(R.id.progressBarLayout);




        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int childCount = group.getChildCount();

                for (int x = 0; x < childCount; x++) {
                    RadioButton btn = (RadioButton) group.getChildAt(x);
                    if(btn.getId()==R.id.male){
                        btn.setText("MALE");
                    }else{
                        btn.setText("FEMALE");
                    }
                    if (btn.getId() == checkedId) {
                        isGenderSelected=true;
                        genderValidate=btn.getText().toString();// here gender will contain M or F.

                    }
                }

                Log.e("Gender",genderValidate);
            }
        });

        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // for animation top
        Animation animationtop = AnimationUtils.loadAnimation(getContext(),R.anim.top_wave);
        ivTOp.setAnimation(animationtop);
        // for animation bottom
        Animation animationBottom = AnimationUtils.loadAnimation(getContext(),R.anim.bottom_wave);
        ivBottom.setAnimation(animationBottom);

        facebookLogin(view);
        gmailLogin(view);
        twitterLogin(view);
        register(view);


        return view;

    }
    private  boolean nameValidation(){
        nameValidation = name.getText().toString().trim();
        if (nameValidation.isEmpty()){
            name.requestFocus();
            name.setError("Name Can't be empty");
            return false;
        }
        else if (name.length() > 25) {
          //  Toast.makeText(getContext(), "Name is more then 25 latter so short your name ", Toast.LENGTH_LONG).show();
            name.requestFocus();
            name.setError("Name is more then 25 latter so short your name ");
            return true;

        }
        return true;
    }
    private boolean genderValidation(){
      if(isGenderSelected==false){
          gender.requestFocus();
          return false;
      }

        return true;
    }
    private boolean validEmail(){
        emailValidation = email.getText().toString().trim();
        if (emailValidation.isEmpty()){
            email.requestFocus();
            email.setError("Email Can't be Empty");
            return false;

        }
        else if (!emailValidation.matches(emailPattern)){
            email.requestFocus();
            email.setError("Email Address Invalid");
            return false;
        }
        else email.setError(null);
        return true;


    }
    private boolean validatePassword(){
        passwordValidate = password.getText().toString().trim();
        if (passwordValidate.isEmpty()){
            password.requestFocus();
            password.setError("Password can't be Empty");
            return false;

        }
        else if (!passwordValidate.matches(passPattern)){
            password.requestFocus();
            password.setError("Password too weak ! Use one Cap later small latter  number and special character");
            return false;
        }
        else
            password.setError(null);
            return true;
    }
    public boolean phoneNumberValidation(){
        phoneValidation = phone.getText().toString().trim();
        if (phoneValidation.isEmpty()){
            phone.requestFocus();
            phone.setError("Phone Number Can't be empty");
        }else if (phone.length() !=10){
            Toast.makeText(getContext(), "Enter Valid Phone Number", Toast.LENGTH_LONG).show();
            phone.requestFocus();
            phone.setError("Enter Valid Phone Number");
            return false;
        }

        return true;
    }
    private void register(View view) {
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                genderValidation();
//                phoneNumberValidation();
//                nameValidation();
//                validEmail();
//                validatePassword();
            if (nameValidation() && validEmail() && validatePassword() && phoneNumberValidation() && genderValidation()){
                new PrefranceManager(getContext()).saveSignUpDetails(emailValidation,passwordValidate);
                insertDataSignUp(view);

           }



                //if (nameValidation() && validEmail() && validatePassword() && phoneNumberValidation() && genderValidation()){ insertDataSignUp(view); }

            }

        });
    }
    private void insertDataSignUp(View view){
        progressBarLayout.setVisibility(View.VISIBLE);
        String userName = name.getText().toString();
        String userEmail = email.getText().toString();
        String userPhone = phone.getText().toString();
        String userGender = genderValidate;
       String userPass = password.getText().toString();
        boolean checkinsertData = dbHelper.insertContact(userName,userEmail,userPhone,userGender,userPass);
        if (checkinsertData==true){

            signUpwithEmailFIrebae(userName,userEmail,userPhone,userGender,userPass);
        }
        else {
            progressBarLayout.setVisibility(View.GONE);
            Toast.makeText(getContext(), "SomeThing Is Missisng ", Toast.LENGTH_SHORT).show();
        }
    }
//    private void insertData(){
//        boolean result=dbHelper.insertContact("name","email","12344","male","12334433");
//        if(result==true){
//            Log.i("sucess","Successfully Inserted");
//        } else {
//            Log.i("sucess","Failed Inserted");
//        }
//        if (nameValidation() && validEmail() && validatePassword() && phoneNumberValidation() && genderValidation()){
//            new PrefranceManager(getContext()).saveSignUpDetails(nameValidation,emailValidation,phoneValidation,genderValidate,passwordValidate);
//               Intent intent = new Intent(getContext(), NameOfNotesHolder.class);
//               startActivity(intent);
//
//        }
//
//    }
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
    private void signUpwithEmailFIrebae(String name,String email,String phone,String gender,String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressBarLayout.setVisibility(View.GONE);
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Success", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String uid=user.getUid();

                            cloudStoreFirebaseInsertData(uid,name,email,phone,gender,password);

                            Intent inserIntent = new Intent(getContext(),NameOfNotesHolder.class);
                            Log.i("user","userid");
                            startActivity(inserIntent);
                            Toast.makeText(getContext(), "Data Inserted", Toast.LENGTH_SHORT).show();

                        } else {
                            progressBarLayout.setVisibility(View.GONE);
                            // If sign in fails, display a message to the user.
                            Log.w("Failed", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
    private void cloudStoreFirebaseInsertData(String id,String name,String email,String phone,String gender,String password){
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("uid",id);
        user.put("name", name);
        user.put("email", email);
        user.put("phone", phone);
        user.put("gender",gender);
        user.put("password",password);
        user.put("imageUrl","");


// Add a new document with a generated ID
        db.collection("users").document(id).collection("details")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        progressBarLayout.setVisibility(View.GONE);
                        Log.d("Success", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Failed", "Error adding document", e);
                        progressBarLayout.setVisibility(View.GONE);
                    }
                });
    }






}