package com.loftschool.fomin.moneyloft;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import static com.loftschool.fomin.moneyloft.BudgetFragment.REQUEST_CODE;
import static com.loftschool.fomin.moneyloft.BudgetFragment.actionMode;
import static com.loftschool.fomin.moneyloft.R.color.dark_grey_blue;


public class BudgetActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private androidx.appcompat.widget.Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private BudgetViewPagerAdapter mViewPagerAdapter;
    private FloatingActionButton mFloatingActionButton;


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
        mViewPager.addOnPageChangeListener(this);


        mTabLayout.setupWithViewPager(mViewPager);
        Objects.requireNonNull(mTabLayout.getTabAt(0)).setText(R.string.outcome);
        Objects.requireNonNull(mTabLayout.getTabAt(1)).setText(R.string.income);
        Objects.requireNonNull(mTabLayout.getTabAt(2)).setText(R.string.balance);

        mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.marigold));

        mFloatingActionButton = findViewById(R.id.fab_add_screen);
        mFloatingActionButton.setOnClickListener(view -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            for (Fragment fragment : fragmentManager.getFragments()) {
                if (fragment.getUserVisibleHint()) {
                    fragment.startActivityForResult(new Intent(BudgetActivity.this, AddItemActivity.class), REQUEST_CODE);
                }
            }
            overridePendingTransition(R.anim.from_right_in, R.anim.alfa_out);
        });
    }

    @Override
    public void onSupportActionModeStarted(@NonNull ActionMode mode) {
        super.onSupportActionModeStarted(mode);

        mToolbar.setBackgroundColor(ContextCompat.getColor(this, dark_grey_blue));
        mTabLayout.setBackgroundColor(ContextCompat.getColor(this, dark_grey_blue));
        mFloatingActionButton.hide();
        getWindow().setStatusBarColor(ContextCompat.getColor(this, dark_grey_blue));
        actionMode = mode;


    }

    @Override
    public void onActionModeStarted(android.view.ActionMode mode) {
        super.onActionModeStarted(mode);
        mToolbar.setBackgroundColor(ContextCompat.getColor(this, dark_grey_blue));
        mTabLayout.setBackgroundColor(ContextCompat.getColor(this, dark_grey_blue));
        mFloatingActionButton.hide();
        getWindow().setStatusBarColor(ContextCompat.getColor(this, dark_grey_blue));


    }

    @Override
    public void onActionModeFinished(android.view.ActionMode mode) {
        super.onActionModeFinished(mode);
        mToolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        mTabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        mFloatingActionButton.show();
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));


    }

    @Override
    public void onSupportActionModeFinished(@NonNull ActionMode mode) {
        super.onSupportActionModeFinished(mode);
        mToolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        mTabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        mFloatingActionButton.show();
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        actionMode = null;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case BudgetViewPagerAdapter.PAGE_EXPENSES:
            case BudgetViewPagerAdapter.PAGE_INCOMES:
                mFloatingActionButton.show();
                break;
            case BudgetViewPagerAdapter.PAGE_BALANCE:
                mFloatingActionButton.hide();
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case ViewPager.SCROLL_STATE_DRAGGING:
            case ViewPager.SCROLL_STATE_SETTLING:
                if (null != actionMode) actionMode.finish();
                break;
        }

    }

    static class BudgetViewPagerAdapter extends FragmentPagerAdapter {

        public static final int PAGE_EXPENSES = 0;
        public static final int PAGE_INCOMES = 1;
        public static final int PAGE_BALANCE = 2;

        BudgetViewPagerAdapter(final FragmentManager fm) {
            super(fm);


        }

        @NonNull
        @Override
        public Fragment getItem(final int i) {
            switch (i) {
                case 0:
                    return BudgetFragment.newInstance(FragmentType.expense);
                case 1:
                    return BudgetFragment.newInstance(FragmentType.income);
                case 2:
                    return BalanceFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}