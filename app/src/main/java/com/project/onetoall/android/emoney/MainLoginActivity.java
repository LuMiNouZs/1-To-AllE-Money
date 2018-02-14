package com.project.onetoall.android.emoney;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.project.onetoall.android.emoney.fragments.LoginFragment;
import com.project.onetoall.android.emoney.fragments.RootMainFragment;
import com.project.onetoall.android.emoney.fragments.RootSettingFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by c.anupol on 11/12/17.
 */

public class MainLoginActivity extends FragmentActivity{

    private Context context;
    private ViewPager viewPager;

    FragmentManager fm;
    Fragment checkFM;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);
        context = this;
        setContentView(R.layout.fragment_root_login);


        LoginFragment fragmentLogin = new LoginFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.rootLoginFrame, fragmentLogin, "Login").commit();
    }

}
