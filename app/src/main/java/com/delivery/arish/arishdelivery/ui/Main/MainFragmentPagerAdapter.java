package com.delivery.arish.arishdelivery.ui.Main;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.delivery.arish.arishdelivery.R;
import com.delivery.arish.arishdelivery.ui.Main.fragments.AddNewCategoryFragment;
import com.delivery.arish.arishdelivery.ui.Main.fragments.AddNewRestaurantFragment;
import com.delivery.arish.arishdelivery.ui.Main.fragments.GetAllOrdersFragment;
import com.delivery.arish.arishdelivery.ui.Main.fragments.GetLastOrdersFragment;

class MainFragmentPagerAdapter extends FragmentPagerAdapter {

    private final Context mContext;

    public MainFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }


    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new AddNewRestaurantFragment();
            case 1:
                return new AddNewCategoryFragment();
            case 2:
                return new GetAllOrdersFragment();
            default:
                return new GetLastOrdersFragment();
        }
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 4;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return mContext.getString(R.string.rstaurants);
            case 1:
                return mContext.getString(R.string.category);
            case 2:
                return mContext.getString(R.string.all_orders);
            case 3:
                return mContext.getString(R.string.last_orders);
            default:
                return null;
        }
    }

}