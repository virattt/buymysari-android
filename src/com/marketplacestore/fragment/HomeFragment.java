package com.marketplacestore.fragment;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.marketplacestore.Base64;
import com.marketplacestore.DBAdpter;
import com.marketplacestore.R;
import com.marketplacestore.dto.All_list_home_dto;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class HomeFragment extends Fragment {
	
	ListView lv;
	ArrayList<All_list_home_dto> list = new ArrayList<All_list_home_dto>();
	MyListAdapter adt;
	All_list_home_dto list_home;
	String cityName;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.home, container, false);
		
		cityName = getActivity().getIntent().getExtras().getString("cityName").toString();
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		lv = (ListView) rootView.findViewById(R.id.home_listview);
	
		list = DBAdpter.getNewsData(cityName);
		Log.v("log_tag","list_size :: "+ list.size());
		
		adt = new MyListAdapter(getActivity());
		lv.setAdapter(adt);
		
		return rootView;
	}
	
	public class MyListAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public MyListAdapter(Context context) {
			mInflater = LayoutInflater.from(context);

		}

		public int getCount() {
			return list.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			   // if (convertView == null) {
			   convertView = mInflater.inflate(R.layout.custom_home_list, null);

			   ImageButton home_ic_img = (ImageButton) convertView
			     .findViewById(R.id.list_home_logo_image);
			   ImageButton home_big_img = (ImageButton) convertView
			     .findViewById(R.id.sarees_big_img);
			   TextView home_username_txt = (TextView) convertView
			     .findViewById(R.id.home_list_username);
			   TextView home_view_txt = (TextView) convertView
			     .findViewById(R.id.home_view_txt_view);
			   Button close_btn = (Button) convertView
			     .findViewById(R.id.close_home_btn);
			   home_username_txt.setText(list.get(position).store_name.toString());
			   home_view_txt.setText("Views: "+list.get(position).views.toString());
			   Log.v("log_tag", "image :::: " + list.get(position).image);
			   if (list.get(position).image != null) {
			   byte[] Image_getByte;
			   try {
			    Image_getByte = Base64.decode(list.get(position).image);
			    ByteArrayInputStream bytes = new ByteArrayInputStream(
			      Image_getByte);
			    BitmapDrawable bmd = new BitmapDrawable(bytes);
			    Bitmap bm = bmd.getBitmap();
			    home_big_img.setImageBitmap(bm);
			    
			   } catch (IOException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			   }
			   }

			   Log.v("log_tag", "picture :::: " + list.get(position).picture);

			   if (list.get(position).picture != null) {
			    byte[] Image_getByte1;
			    try {
			     Image_getByte1 = Base64.decode(list.get(position).picture);
			     ByteArrayInputStream bytes1 = new ByteArrayInputStream(
			       Image_getByte1);
			     BitmapDrawable bmd1 = new BitmapDrawable(bytes1);
			     Bitmap bm1 = bmd1.getBitmap();
			     home_ic_img.setImageBitmap(bm1);
			    } catch (IOException e) {
			     // TODO Auto-generated catch block
			     e.printStackTrace();
			    }
			   }
			   
			   
			   /*home_big_img.setOnClickListener(new View.OnClickListener() {
			    
			    @Override
			    public void onClick(View v) {
			     // TODO Auto-generated method stub
			    
			     Intent myIntent = new Intent((MarketPlaceActivity)getActivity(), HomeFullImageActivity.class);
			     ((MarketPlaceActivity)getActivity()).startActivity(myIntent);
			     
			             
			    }
			   });*/

			   // }
			   
			   
			   return convertView;
			  }

	}

}