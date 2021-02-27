package com.example.to_do_app_final.Activity;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.example.to_do_app_final.fragment.Login_Frgment;
import com.example.to_do_app_final.fragment.SignUp_Screen;

public class MyAdapter extends FragmentStatePagerAdapter {
    int totalTab;
    private Context myContext;

    public MyAdapter(@NonNull Context context, FragmentManager fm, int totalTabs) {

        super(fm);
        this.myContext = context;
        this.totalTab = totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Login_Frgment login_frgment = new Login_Frgment();
                return login_frgment;
            case 1:
                SignUp_Screen signUp_screen = new SignUp_Screen();
                return signUp_screen;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTab;
    }
}
