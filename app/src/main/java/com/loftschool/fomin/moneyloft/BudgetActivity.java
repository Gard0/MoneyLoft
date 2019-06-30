package com.loftschool.fomin.moneyloft;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;


public class BudgetActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private BudgetViewPagerAdapter mViewPagerAdapter;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        mViewPagerAdapter = new BudgetViewPagerAdapter(getSupportFragmentManager());

        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.view_pager);
        mViewPager.setAdapter(mViewPagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);
    }

    static class BudgetViewPagerAdapter extends FragmentPagerAdapter {

        BudgetViewPagerAdapter(final FragmentManager fm) {
            super(fm);

        }

        @Override
        public Fragment getItem(final int i) {
           switch (i){
               case 0:
                   return BudgetFragment.newInstance("outcome");
               case 1:
                   return BudgetFragment.newInstance("income");
           }
           return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}