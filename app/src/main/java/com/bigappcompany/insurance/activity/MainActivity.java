package com.bigappcompany.insurance.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.bigappcompany.insurance.R;
import com.bigappcompany.insurance.fragment.HavePolicyFragment;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    Toolbar toolbar;
    int no_of_categories=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        tabLayout=(TabLayout)findViewById(R.id.tabLayout);
        viewPager=(ViewPager)findViewById(R.id.viewPager);

        no_of_categories=3;
//        no_of_categories=YOUR_NO_CATEGORIES;
//
//        for (int i = 0; i < no_of_weeks; i++) {
//            tabLayout.addTab(tabLayout.newTab().setText("TAB " + String.valueOf(i + 1)));
//        }

        for (int i = 0; i < 3; i++) {
            tabLayout.addTab(tabLayout.newTab().setText("TAB " + String.valueOf(i + 1)));
        }

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        //done

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }
    public class ViewPagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;
        Fragment fragment = null;

        public ViewPagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {

            for (int i = 0; i < mNumOfTabs ; i++) {
                if (i == position) {
                    fragment = new HavePolicyFragment();
                    break;
                }
            }
            return fragment;

        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return super.getPageTitle(position);
        }
    }
}
