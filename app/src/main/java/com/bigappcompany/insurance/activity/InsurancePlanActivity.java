package com.bigappcompany.insurance.activity;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bigappcompany.insurance.R;
import com.bigappcompany.insurance.fragment.HaveNoPolicyFragment;
import com.bigappcompany.insurance.fragment.HavePolicyFragment;
import com.bigappcompany.insurance.fragment.InsurancePlanFragment;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class InsurancePlanActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private RelativeLayout rvClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_previous);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Celias.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        initWidgets();
        eventListerns();

        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        View root = tabLayout.getChildAt(0);
        if (root instanceof LinearLayout) {
            ((LinearLayout) root).setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(getResources().getColor(R.color.colorstroke));
            drawable.setSize(2, 1);
            ((LinearLayout) root).setDividerPadding(20);
            ((LinearLayout) root).setDividerDrawable(drawable);
        }

    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void eventListerns() {
        //Adding onTabSelectedListener to swipe views

        rvClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });

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

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }

    private void initWidgets() {

        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        //Initializing the tablayout
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        tabLayout.addTab(tabLayout.newTab().setText("Reliance"));
        tabLayout.addTab(tabLayout.newTab().setText("Bharathi Axa"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        rvClick=(RelativeLayout)findViewById(R.id.rvClick);

    }


    private class PagerAdapter extends FragmentPagerAdapter {

        private int numOfTabs;

        public PagerAdapter(FragmentManager fm, int count) {
            super(fm);
            this.numOfTabs = count;
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = null;

            switch (position) {
                case 0:
                    fragment = new InsurancePlanFragment();
                    break;
                case 1:
                    fragment = new InsurancePlanFragment();
                    break;

            }

            return fragment;
        }

        @Override
        public int getCount() {
            return numOfTabs;
        }
    }
}
