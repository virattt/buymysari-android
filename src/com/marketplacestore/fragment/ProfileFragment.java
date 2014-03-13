package com.marketplacestore.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.marketplacestore.DBAdpter;
import com.marketplacestore.R;

public class ProfileFragment extends Fragment{
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.profile, container, false);
		
		String FirstNAme = DBAdpter.fetch_UserDetail_data.get(0).getFirst_name(); 
		
		Log.v("log", " FirstNAme : " + FirstNAme);
		
		return rootView;
	}
}
