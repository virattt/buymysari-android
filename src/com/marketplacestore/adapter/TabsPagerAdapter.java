package com.marketplacestore.adapter;

import com.marketplacestore.fragment.FavouriteFragment;
import com.marketplacestore.fragment.SubscribeFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Top Rated fragment activity
			return new Fragment();
		case 1:
			// Games fragment activity
			return new SubscribeFragment();
		case 2:
			// Movies fragment activity
			return new FavouriteFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}

}
