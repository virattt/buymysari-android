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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.marketplacestore.Base64;
import com.marketplacestore.DBAdpter;
import com.marketplacestore.MyApplication;
import com.marketplacestore.R;
import com.marketplacestore.dto.All_list_Store_dto;
import com.marketplacestore.dto.Mystore_Item_dto;
import com.marketplacestore.dto.UserInfo_dto;

public class MyStoreItemFragment extends Fragment {

	ListView lv;
	ArrayList<Mystore_Item_dto> list = new ArrayList<Mystore_Item_dto>();
	MyListAdapter adt;
	View rootView;
	ImageButton store_icon;
	TextView storeName;
	Button subscribe, store_subscr_btn;
	UserInfo_dto usdt;
	MyApplication app;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.mystoreitemlist, container,
				false);
		app = (MyApplication) getActivity().getApplicationContext();
		
		Bundle bundle = this.getArguments();
		String myInt = bundle.getString("position");
		Log.v("log_tag", "myInt" + myInt);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		list = DBAdpter.getMyStoreItemData(myInt);
		
		
		lv = (ListView) rootView.findViewById(R.id.mystoreitem_listview);
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

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			convertView = mInflater.inflate(R.layout.custommystoreitem,
					null);
			ImageButton store_item_img = (ImageButton) convertView
					.findViewById(R.id.mystoreitem_big_img);

			TextView store_closet_txt = (TextView) convertView
					.findViewById(R.id.mystoreitem_closet_txt_view);
			TextView store_item_name_txt = (TextView) convertView
					.findViewById(R.id.mystoreitemName_txt);

			Button store_item_close_btn = (Button) convertView
					.findViewById(R.id.mystoreitem_close_home_btn);

			store_closet_txt.setText("Closeted : "
					+ list.get(position).Closeted_item_track);
			store_item_name_txt.setText(list.get(position).name);
			final String uid = app.getUserID();

			if (list.get(position).image != null) {
				byte[] Image_getByte;
				try {
					Image_getByte = Base64.decode(list.get(position).image);
					ByteArrayInputStream bytes = new ByteArrayInputStream(
							Image_getByte);
					BitmapDrawable bmd = new BitmapDrawable(bytes);
					Bitmap bm = bmd.getBitmap();
					store_item_img.setImageBitmap(bm);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			store_item_close_btn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					/*String msg = DBAdpter.userClosestStore(
							list.get(position).item_id,
							list.get(position).store_id, uid);
					Toast.makeText(getActivity().getApplicationContext(), msg,
							1).show();*/
				}
			});
			return convertView;
		}
	}



}
