package com.marketplacestore.fragment;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.transition.Visibility;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.marketplacestore.Base64;
import com.marketplacestore.DBAdpter;
import com.marketplacestore.R;
import com.marketplacestore.dto.All_list_home_dto;
import com.marketplacestore.dto.Closet_dto;

public class ClosetFragment extends Fragment {
	
	ListView lv;
	ArrayList<Closet_dto> list = new ArrayList<Closet_dto>();
	MyListAdapter adt;
	All_list_home_dto list_home;
	String cityName;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.closet, container, false);
	
		cityName = getActivity().getIntent().getExtras().getString("cityName").toString();
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		lv = (ListView) rootView.findViewById(R.id.closet_listview);
	
		list = DBAdpter.getClosetData("1");
		Log.v("log_tag","list_size ClosetItems :: "+ list.size());
		
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
			//if (convertView == null) {
				convertView = mInflater.inflate(R.layout.custom_home_list,
						null);
				
						ImageButton home_ic_img = (ImageButton) convertView.findViewById(R.id.list_home_logo_image);
						ImageButton home_big_img = (ImageButton) convertView.findViewById(R.id.sarees_big_img);
						TextView home_username_txt = (TextView) convertView.findViewById(R.id.home_list_username);
						TextView home_view_txt = (TextView) convertView.findViewById(R.id.home_view_txt_view);
						TextView itemName_txt = (TextView) convertView.findViewById(R.id.itemName_txt);
						
						Button close_btn =(Button) convertView.findViewById(R.id.close_home_btn);
						
						close_btn.setVisibility(View.INVISIBLE);
						home_username_txt.setText(list.get(position).getStore_name());
						Log.v("log"," Closet Item NAme IN Adater " + list.get(position).getStore_name());
						
						Log.v("log_tag","image :::: " +list.get(position).getImage());
						home_view_txt.setText("Closeted "+list.get(position).getCloseted_item_track());
						
						itemName_txt.setText(list.get(position).getName());
						  
						byte[] Image_getByte;
						try {
							Image_getByte = Base64.decode(list.get(position).getImage());
							 ByteArrayInputStream bytes = new ByteArrayInputStream(Image_getByte);
							   BitmapDrawable bmd = new BitmapDrawable(bytes);
							   Bitmap bm = bmd.getBitmap(); 
							   home_big_img.setImageBitmap(bm);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						  
						if (list.get(position).getStore_image() != null) {
							    byte[] Image_getByte1;
							    try {
							     Image_getByte1 = Base64.decode(list.get(position).getStore_image());
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
			//}
			return convertView;
		}
	}

	
}
