package com.project.onetoall.android.emoney;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.project.onetoall.android.emoney.adapters.ContentNewsAdapter;
import com.project.onetoall.android.emoney.adapters.RightNavigationAdapter;
import com.project.onetoall.android.emoney.fragments.MainMenuFragment;
import com.project.onetoall.android.emoney.fragments.MenuTopupFragment;
import com.project.onetoall.android.emoney.models.RightNavItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by c.anupol on 8/1/17.
 */

public class MainBackupActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {


    private Context context;
    private ImageView ivBack, ivMenu;
    DrawerLayout dwLayout;
    RelativeLayout dwPane;
    private ListView lvRightNav;
    private TextView txtNavigation;
    private List<RightNavItem> listNavItem;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private ViewPager vpContentNews;
    private static final String MY_PREFS = "my_prefs";
    private static final String TAG_MENU_TOPUP_FRAGMENT = "menu_topup_fragment";
    private static final String TAG_TOPUP_FRAGMENT = "topup_fragment";
    FragmentManager fm;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkBundle();
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_main2);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title_bar_with_back_and_menu);
        context = this;
        initUI();
        txtNavigation.setText("Welcome to E-Money by 1-To-All");
        txtNavigation.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);


        setSlideBar();

        MainMenuFragment fragment = new MainMenuFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.rootLayout, fragment).commit();

        ContentNewsAdapter contentNewsAdapter = new ContentNewsAdapter(this);
        vpContentNews.setAdapter(contentNewsAdapter);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new ContentNewsTimeTask(), 2000, 4000);

    }


    //Change Anything
    private void initUI() {
        ivBack = (ImageView) findViewById(R.id.ivBackAndMenu);
        ivMenu = (ImageView) findViewById(R.id.ivMenu);
        dwLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        dwPane = (RelativeLayout) findViewById(R.id.drawer_pane);
        lvRightNav = (ListView) findViewById(R.id.lvNavList);
        txtNavigation = (TextView) findViewById(R.id.txtNavigationNameWithMenu);
        vpContentNews = (ViewPager) findViewById(R.id.vpContentNews);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPagerMain);


        viewPager.setAdapter(new SectionPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        //setEvent
        ivBack.setOnClickListener(this);
        ivMenu.setOnClickListener(this);
        lvRightNav.setOnItemClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBackAndMenu:
                Fragment currentFragment = getFragmentManager().findFragmentById(R.id.rootLayout);
                //final Myfragment fragment = (Myfragment) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
                fm = getSupportFragmentManager();
                //String tag = fm.findFragmentByTag();

                //Fragment currentFragment = getFragmentManager().findFragmentByTag("TAG_MENU_TOPUP_FRAGMENT");

                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();

                    if (fm.getBackStackEntryCount() == 1) {
                        setInvisibleBackButton(false);
                        setNovigationText(" Welcome to E-Money by 1-To-All", View.TEXT_ALIGNMENT_TEXT_START);
                        setInvisibleContentNews(true);
                    }
                }
                break;
            case R.id.ivMenu:
                if (dwLayout.isDrawerOpen(Gravity.RIGHT)) {
                    dwLayout.closeDrawer(Gravity.RIGHT);
                } else {
                    dwLayout.openDrawer(Gravity.RIGHT);
                }
                break;
        }
    }

    private void setSlideBar() {
        sharedPreferences = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        boolean sesstionLogin = sharedPreferences.getBoolean("login", false);

        if (sesstionLogin == true) {
            listNavItem = new ArrayList<RightNavItem>();
            listNavItem.add(new RightNavItem(R.string.nav_bar_login, R.drawable.ic_action_home));
            listNavItem.add(new RightNavItem(R.string.nav_bar_account, R.drawable.ic_action_account));
            listNavItem.add(new RightNavItem(R.string.nav_bar_abountus, R.drawable.ic_action_aboutus));
            listNavItem.add(new RightNavItem(R.string.nav_bar_contactus,R.drawable.ic_action_contact));
            listNavItem.add(new RightNavItem(R.string.nav_bar_logout, R.drawable.ic_action_logout));
            RightNavigationAdapter rightNavigationAdapter = new RightNavigationAdapter(getApplicationContext(), R.layout.item_right_navigation_list, listNavItem);
            lvRightNav.setAdapter(rightNavigationAdapter);
            setTitle(listNavItem.get(0).getTitile());
            lvRightNav.setItemChecked(0, true);

        } else {
            listNavItem = new ArrayList<RightNavItem>();
            listNavItem.add(new RightNavItem(R.string.nav_bar_login, R.drawable.ic_action_login));
            listNavItem.add(new RightNavItem(R.string.nav_bar_abountus, R.drawable.ic_action_aboutus));

            RightNavigationAdapter rightNavigationAdapter = new RightNavigationAdapter(getApplicationContext(), R.layout.item_right_navigation_list, listNavItem);
            lvRightNav.setAdapter(rightNavigationAdapter);
            setTitle(listNavItem.get(0).getTitile());
            lvRightNav.setItemChecked(0, true);
        }


        //lisiNavItemDrwable = new ArrayList<>()


        dwLayout.closeDrawer(dwPane);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, dwLayout, R.string.drawer_opened, R.string.drawer_closed) {

            @Override
            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                invalidateOptionsMenu();
                super.onDrawerClosed(drawerView);

            }
        };

        dwLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        sharedPreferences = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        boolean sesstionLogin = sharedPreferences.getBoolean("login", false);
        if (sesstionLogin == true) {
            switch (position) {
                case 0: //Home
                    break;
                case 1: //Account
                    Toast.makeText(context, "This function not available.", Toast.LENGTH_SHORT).show();
                    break;
                case 2: //AboutUs
                    Toast.makeText(context, "This function not available.", Toast.LENGTH_SHORT).show();
                    break;
                case 3: //Contactus
                    Toast.makeText(context, "This function not available.", Toast.LENGTH_SHORT).show();
                    break;
                case 4: //Setting
                    Toast.makeText(context, "This function not available.", Toast.LENGTH_SHORT).show();
                    break;
                case 5: //Log-Out
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove("user");
                    editor.remove("first_name");
                    editor.remove("last_name");
                    editor.remove("login");
                    editor.commit();
                    Intent inGotoLogout = new Intent(context, MainBackupActivity.class);
                    inGotoLogout.putExtra("NewStartAct", true);
                    inGotoLogout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(inGotoLogout);
                    break;
            }
        } else {
            switch (position) {
                case 0: //Login
                    Intent inGotoLogin = new Intent(context, LoginActivity.class);
                    startActivity(inGotoLogin);
                    break;
                case 1: //Contact US
                    Toast.makeText(context, "This function not available.", Toast.LENGTH_SHORT).show();
                    break;
                case 2: //Setting
                    Toast.makeText(context, "This function not available.", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    }

    public boolean checkSeestionLogin() {
        sharedPreferences = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        boolean sesstionLogin = sharedPreferences.getBoolean("login", false);

        if (sesstionLogin == true) {
            return true;
        }
        return false;
    }

    private void selectFragment(int position) {
        Fragment fragment = new Fragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.rootLayout, fragment);
        transaction.commit();


    }

    private void checkBundle() {
        try {
            if (getIntent().getBooleanExtra("NewStartAct", false)) {
                finish();
                Intent intent = new Intent(this, MainBackupActivity.class);
                startActivity(intent);
            }
        } catch (Exception e) {
            Log.d("NewStartActivity", e.toString());
        }
    }

    public void setNovigationText(String text, int alignText) {
        txtNavigation.setText(text);
        txtNavigation.setTextAlignment(alignText);
    }

    public void setInvisibleBackButton(boolean invis) {
        if (invis == true) {
            ivBack.setVisibility(View.VISIBLE);
        } else {
            ivBack.setVisibility(View.GONE);
        }
    }

    public void setInvisibleContentNews(boolean invis) {
        if (invis == true) {
            vpContentNews.setVisibility(View.VISIBLE);
        } else {
            vpContentNews.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();

            if (fm.getBackStackEntryCount() == 1) {
                setInvisibleBackButton(false);
                setNovigationText(" Welcome to E-Money by 1-To-All", View.TEXT_ALIGNMENT_TEXT_START);
                setInvisibleContentNews(true);
            }
        } else {
            super.onBackPressed();
        }
    }

    public class ContentNewsTimeTask extends TimerTask {


        @Override
        public void run() {
            MainBackupActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    switch (vpContentNews.getCurrentItem()) {
                        case 0:
                            vpContentNews.setCurrentItem(1);
                            break;
                        case 1:
                            vpContentNews.setCurrentItem(2);
                            break;
                        case 2:
                            vpContentNews.setCurrentItem(0);
                            break;
                    }
                }
            });
        }
    }

    private class SectionPagerAdapter extends FragmentPagerAdapter {

        public SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            switch (position) {
                case 0:
                default:
                    return new MainMenuFragment();
                case 1:

                    return new MenuTopupFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                default:
                    return "Home";
                case 1:
                    return "Setting";
            }
        }

    }
}
