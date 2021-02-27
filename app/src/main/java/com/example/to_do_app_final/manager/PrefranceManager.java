package com.example.to_do_app_final.manager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.to_do_app_final.Activity.Login;
import com.example.to_do_app_final.Activity.MainActivity;
import com.example.to_do_app_final.Activity.NameOfNotesHolder;

public class PrefranceManager {
    Context context;
    public static final String PREFERENCE ="Preference";

    public PrefranceManager(Context context) {
        this.context = context;
    }

    public void saveSignUpDetails(String email,String password){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email",email);
        editor.putString("password",password);
//        editor.putString("name",name);
//        editor.putString("phone",phone);
//        editor.putString("gender",gender);

        editor.apply();

    }

//    public void saveLoginDetail(String email, String password){
//        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE,Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("",email);
//        editor.putString("",password);
//        editor.apply();
//    }

    public String getEmail(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE,Context.MODE_PRIVATE);
        return sharedPreferences.getString("email","");
    }
//    public String getGender(){
//        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE,Context.MODE_PRIVATE);
//        return sharedPreferences.getString("gender","");
//    }
//    public  String getNameProfile(){
//        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE,Context.MODE_PRIVATE);
//        return sharedPreferences.getString("name","");
//    }
//    public  String getPhone(){
//        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
//        return sharedPreferences.getString("phone","");
//    }

   public boolean isUserLogOut(){
        SharedPreferences sharedPreferences  = context.getSharedPreferences(PREFERENCE,Context.MODE_PRIVATE);
        return sharedPreferences.getString("email","").isEmpty();
   }
   public void loginChecker(){
        if (isUserLogOut()==true){
            Intent intent = new Intent(context, Login.class);
            context.startActivity(intent);
        }
        else {
            Intent intent = new Intent(context, NameOfNotesHolder.class);
            context.startActivity(intent);
        }
   }
   public  void logOut(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
   }
}
