package com.project.onetoall.android.emoney;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.project.onetoall.android.emoney.adapters.RightNavigationAdapter;
import com.project.onetoall.android.emoney.fragments.MenuTopupFragment;
import com.project.onetoall.android.emoney.fragments.ReceiptFragment;
import com.project.onetoall.android.emoney.fragments.RootMainFragment;
import com.project.onetoall.android.emoney.fragments.RootSettingFragment;
import com.project.onetoall.android.emoney.models.RightNavItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by c.anupol on 8/1/17.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private Context context;
    private ImageView ivBack, ivMenu;
    private ViewPager viewPager;
    private TabLayout tabLayout;
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
    Fragment checkFM;
    SharedPreferences sharedPreferences;
    SectionPagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkBundle();
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_main2);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title_bar_with_back_and_menu);
        context = this;
        initUI();
        txtNavigation.setText(R.string.title_main_activity);
        txtNavigation.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        setSlideBar();
        //viewPager.setAdapter(new SectionPagerAdapter(getSupportFragmentManager()));
        createViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        createTabIcons();


        /*
        MainMenuFragment fragment = new MainMenuFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.rootLayout, fragment).commit();
        */
    }

    private void initUI() {
        ivBack = (ImageView) findViewById(R.id.ivBackAndMenu);
        ivMenu = (ImageView) findViewById(R.id.ivMenu);
        dwLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        dwPane = (RelativeLayout) findViewById(R.id.drawer_pane);
        lvRightNav = (ListView) findViewById(R.id.lvNavList);
        txtNavigation = (TextView) findViewById(R.id.txtNavigationNameWithMenu);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewPagerMain);
        //setEvent
        ivBack.setOnClickListener(this);
        ivMenu.setOnClickListener(this);
        lvRightNav.setOnItemClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBackAndMenu:
                //Fragment currentFragment = getFragmentManager().findFragmentById(R.id.rootLayout);
                //final Myfragment fragment = (Myfragment) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
                fm = getSupportFragmentManager();
                //String tag = fm.findFragmentByTag();

                //Fragment currentFragment = getFragmentManager().findFragmentByTag("TAG_MENU_TOPUP_FRAGMENT");

                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();

                    if (fm.getBackStackEntryCount() == 1) {
                        setInvisibleBackButton(false);
                        setNovigationText(R.string.title_main_activity, View.TEXT_ALIGNMENT_CENTER);
                        //setInvisibleContentNews(true);
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
            listNavItem.add(new RightNavItem(R.string.nav_bar_home, R.drawable.ic_action_home));
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
                    //check current fragment
                    checkFM = getSupportFragmentManager().findFragmentByTag("MainMenu");
                    if (checkFM != null && checkFM.isVisible()) {
                        dwLayout.closeDrawer(dwPane);
                    }
                    else {
                        Intent inGotoHome = new Intent(context,MainActivity.class);
                        startActivity(inGotoHome);
                        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                        finish();
                    }

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
                case 4: //Log-Out
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove("user");
                    editor.remove("first_name");
                    editor.remove("last_name");
                    editor.remove("login");
                    editor.commit();
                    Intent inGotoLogout = new Intent(context, MainActivity.class);
                    inGotoLogout.putExtra("NewStartAct", true);
                    inGotoLogout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(inGotoLogout);
                    overridePendingTransition(R.anim.enter_from_left, R.anim.non_anima);
                    finish();

                    break;
            }
        } else {
            switch (position) {
                case 0: //Login
                    //Intent inGotoLogin = new Intent(context, LoginActivity.class);
                    Intent inGotoLogin = new Intent(context, MainLoginActivity.class);
                    startActivity(inGotoLogin);
                    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                    dwLayout.closeDrawer(dwPane);
                    break;
                case 1: //Contact US
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

    private void checkBundle() {
        try {
            if (getIntent().getBooleanExtra("NewStartAct", false)) {
                finish();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        } catch (Exception e) {
            Log.d("NewStartActivity", e.toString());
        }
    }

    public void setNovigationText(int text, int alignText) {
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

    @Override
    public void onBackPressed() {

        fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();

            if (fm.getBackStackEntryCount() == 1) {
                setInvisibleBackButton(false);
                setNovigationText(R.string.title_main_activity, View.TEXT_ALIGNMENT_CENTER);
                //setInvisibleContentNews(true);
            }
        } else {
            super.onBackPressed();
        }
    }

    private void createTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_layout, null);
        tabOne.setText("Home");
        //tabOne.setTextColor(ContextCompat.getColor(context,R.color.tabBackgroundUnselected));
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.selector_home_icon, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);
        //tabLayout.setTabTextColors(ContextCompat.getColor(context, R.color.tabBackgroundUnselected),ContextCompat.getColor(context, R.color.tabBackgroundUnselected));

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_layout, null);
        tabTwo.setText("Setting");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.selector_setting_icon, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        //tabLayout.getTabAt(0).setText("Home").setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.selector_home_icon,null));
        //tabLayout.getTabAt(1).setText("Setting").setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.selector_setting_icon,null));
        //tabLayout.addTab(tabLayout.newTab().setText("Home").setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.selector_home_icon,null)));
        //tabLayout.addTab(tabLayout.newTab().setText("Setting").setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.selector_setting_icon, null)));
    }

    private void createViewPager(ViewPager viewPager) {
        adapter = new SectionPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new RootMainFragment().newInstance(), "Tab 1");
        adapter.addFrag(new RootSettingFragment().newInstance(), "Tab 2");
        viewPager.setAdapter(adapter);
    }

    private class SectionPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
           /*
            switch (position) {
                case 0:
                default:
                    return new RootMainFragment().newInstance();
                case 1:
                    return new RootSettingFragment().newInstance();
            }
*/
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            //return 2;
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            /*
            switch (position) {
                case 0:
                default:
                    return "Home";
                case 1:
                    return "Setting";
            }
        */
            return mFragmentTitleList.get(position);
        }

    }
}
