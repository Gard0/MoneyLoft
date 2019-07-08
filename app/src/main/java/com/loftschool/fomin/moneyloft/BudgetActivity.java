package com.loftschool.fomin.moneyloft;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import static com.loftschool.fomin.moneyloft.BudgetFragment.REQUEST_CODE;


public class BudgetActivity extends AppCompatActivity {

    private androidx.appcompat.widget.Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private BudgetViewPagerAdapter mViewPagerAdapter;


    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mViewPagerAdapter = new BudgetViewPagerAdapter(getSupportFragmentManager());

        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.view_pager);
        mViewPager.setAdapter(mViewPagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);
        Objects.requireNonNull(mTabLayout.getTabAt(0)).setText(R.string.outcome);
        Objects.requireNonNull(mTabLayout.getTabAt(1)).setText(R.string.income);

        mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.marigold));

        FloatingActionButton openAddScreenButton = findViewById(R.id.fab_add_screen);
        openAddScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                for (Fragment fragment : fragmentManager.getFragments()) {
                    if (fragment.getUserVisibleHint()) {

                        fragment.startActivityForResult(new Intent(BudgetActivity.this, AddItemActivity.class), REQUEST_CODE);
                    }
                }
            }


        });
    }

    static class BudgetViewPagerAdapter extends FragmentPagerAdapter {

        BudgetViewPagerAdapter(final FragmentManager fm) {
            super(fm);

        }

        @Override
        public Fragment getItem(final int i) {
            switch (i) {
                case 0:
                    return BudgetFragment.newInstance(FragmentType.expense);
                case 1:
                    return BudgetFragment.newInstance(FragmentType.income);
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}