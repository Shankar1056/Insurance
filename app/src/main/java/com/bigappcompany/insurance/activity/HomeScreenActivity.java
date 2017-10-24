package com.bigappcompany.insurance.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.bigappcompany.insurance.R;
import com.bigappcompany.insurance.adapter.MenuListAdapter;
import com.bigappcompany.insurance.fragment.AboutUs;
import com.bigappcompany.insurance.fragment.FindDetailsFragment;
import com.bigappcompany.insurance.fragment.MyInsuranceFragment;
import com.bigappcompany.insurance.model.MenuListPojo;

import java.util.ArrayList;
import java.util.Arrays;

import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout;
import nl.psdcompany.duonavigationdrawer.views.DuoMenuView;
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HomeScreenActivity extends AppCompatActivity {

    private ArrayList<MenuListPojo> menuListData;
    private ListView list;
    private ArrayList<String> mTitles = new ArrayList<>();
    private MenuListAdapter mMenuAdapter;
    private DuoMenuView duoMenuView;
    private Fragment fragment;
    private FragmentTransaction fragmentTransaction;
    private DuoDrawerLayout drawerLayout;
    private DuoDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitles = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.menuOptions)));
        setContentView(R.layout.activity_homescreen);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Celias.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        drawerListerns();
        initViews();
        eventListners();
        prepareList();
        switchFragments(0);

    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void drawerListerns() {

        drawerLayout = (DuoDrawerLayout) findViewById(R.id.drawer);
        drawerToggle = new DuoDrawerToggle(this, drawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

    }

    public void openDrawer(){

        drawerLayout.openDrawer();
    }

    private void initViews() {
        duoMenuView = (DuoMenuView) findViewById(R.id.menu);
        duoMenuView.invalidate();
        duoMenuView.setBackground(R.drawable.bg_navdrawer);

    }


    private void eventListners() {

        duoMenuView.setOnMenuClickListener(new DuoMenuView.OnMenuClickListener() {
            @Override
            public void onFooterClicked() {

            }

            @Override
            public void onHeaderClicked() {

            }

            @Override
            public void onOptionClicked(int position, Object objectClicked) {
                switchFragments(position);
            }
        });

    }


    private void switchFragments(int position) {
        drawerLayout.closeDrawer();
        switch (position) {
            case 0:
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragment = new FindDetailsFragment();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.commit();
                break;
            case 1:

                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragment = new MyInsuranceFragment();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.commit();
                break;

            case 2:
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragment = new AboutUs("aboutus");
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.commit();
                break;
            case 3:
    
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragment = new AboutUs("terms");
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.commit();
                break;
            

            case 4:
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragment = new AboutUs("policy");
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.commit();
                break;

            case 5:
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragment = new AboutUs("refund");
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.commit();
                break;
    
            case 6:
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragment = new AboutUs("contactus");
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.commit();
                break;

            case 7:
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragment = new FindDetailsFragment();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.commit();
                break;

            default:
                break;
        }
    }


    private void prepareList() {


        menuListData = new ArrayList<MenuListPojo>();

        MenuListPojo ojtpojo = new MenuListPojo();
        ojtpojo.setName("Home");
        menuListData.add(ojtpojo);

        ojtpojo = new MenuListPojo();
        ojtpojo.setName("My Insurance");
        menuListData.add(ojtpojo);

        ojtpojo = new MenuListPojo();
        ojtpojo.setName("About Us");
        menuListData.add(ojtpojo);


        ojtpojo = new MenuListPojo();
        ojtpojo.setName("Terms");
        menuListData.add(ojtpojo);

        ojtpojo = new MenuListPojo();
        ojtpojo.setName("Policy");
        menuListData.add(ojtpojo);
    
        ojtpojo = new MenuListPojo();
        ojtpojo.setName("Cancellation");
        menuListData.add(ojtpojo);
    
        ojtpojo = new MenuListPojo();
        ojtpojo.setName("Contact Us");
        menuListData.add(ojtpojo);
        
        
        ojtpojo = new MenuListPojo();
        ojtpojo.setName("Share");
        menuListData.add(ojtpojo);
    
    
        ojtpojo = new MenuListPojo();
        ojtpojo.setName("Rate Us");
        menuListData.add(ojtpojo);
        
        MenuListAdapter value = new MenuListAdapter(this, menuListData);
//        list.setAdapter(value);
        duoMenuView.setAdapter(value);
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        openDrawer();
    }
    public  void finishActivity()
    {
	    this.finish();
    }
}
