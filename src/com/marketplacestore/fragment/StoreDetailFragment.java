package com.marketplacestore.fragment;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.marketplacestore.fragment.HomeFragment.MyListAdapter;
import com.marketplacestore.fragment.StoreProfileGridFragment.CustomGridViewAdapter;
import com.marketplacestore.fragment.StoreProfileGridFragment.JSONTask;

public class StoreDetailFragment extends Fragment {

	ListView lv;
	ArrayList<All_list_Store_dto> list = new ArrayList<All_list_Store_dto>();
	MyListAdapter adt;
	View rootView;
	ImageButton store_icon;
	TextView storeName;
	Button store_subscribe_btn;
	MyApplication app;
	private ProgressDialog progress;
	Bundle bundle;
	String myInt;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.store_detail_list, container,
				false);
		app = (MyApplication) getActivity().getApplicationContext();

		store_subscribe_btn = (Button) rootView
				.findViewById(R.id.store_subscribe_btn);
		store_icon = (ImageButton) rootView
				.findViewById(R.id.list_Store_logo_image);
		storeName = (TextView) rootView.findViewById(R.id.store_list_name);
		bundle = this.getArguments();
		myInt = bundle.getString("position");
		lv = (ListView) rootView.findViewById(R.id.store_listview);
		Log.v("log_tag", "myInt " + myInt);

		if (myInt.trim().equals("")) {

			store_icon.setVisibility(View.INVISIBLE);
			store_subscribe_btn.setVisibility(View.INVISIBLE);
			store_icon.setVisibility(View.INVISIBLE);
			Toast.makeText(getActivity().getApplicationContext(), "No Store Detail Available",
					Toast.LENGTH_LONG).show();
			
		} else {
			store_icon.setVisibility(View.VISIBLE);
			store_subscribe_btn.setVisibility(View.VISIBLE);
			store_icon.setVisibility(View.VISIBLE);
			progress = new ProgressDialog(getActivity());
			progress.setMessage("Loading...");
			new JSONTask(progress).execute("Home");
			
		}

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		store_subscribe_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String msg = DBAdpter.storeSubscribeData(app.getUserID(), myInt);
				Toast.makeText(getActivity().getApplicationContext(), msg,
						Toast.LENGTH_LONG).show();
			}
		});
		
		return rootView;
	}

	public class JSONTask extends AsyncTask<String, Void, String> {

		public JSONTask(ProgressDialog progress) {
			progress = progress;
		}

		public void onPreExecute() {
			progress.show();
		}

		@Override
		protected String doInBackground(String... arg) {
			String listSize = "";
			Log.v("log_tag", "list DoinBaCK ");

			list = DBAdpter.getStoreData(myInt);
			listSize = list.size() + "";
			return listSize; // This value will be returned to your
								// onPostExecute(result) method
		}

		@Override
		protected void onPostExecute(String result) {
			// Create here your JSONObject...
			Log.v("log_tag", "list ON Post");
			
			
			
			if (Integer.parseInt(result) > 0) {
				storeName.setText(list.get(0).store_name);
				updateTableList(list.get(0).store_image);
				adt = new MyListAdapter(getActivity());
				lv.setAdapter(adt);
			} else {

				Toast.makeText(getActivity().getApplicationContext(),
						" No Stroe Items Available ", Toast.LENGTH_LONG).show();
			}
			progress.dismiss();

		}

	

	}

	private void updateTableList(String img) {
		byte[] Image_getByte;
		try {
			Image_getByte = Base64.decode(img);
			ByteArrayInputStream bytes = new ByteArrayInputStream(Image_getByte);
			BitmapDrawable bmd = new BitmapDrawable(bytes);
			Bitmap bm = bmd.getBitmap();
			store_icon.setImageBitmap(bm);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			convertView = mInflater.inflate(R.layout.custom_store_detail_list,
					null);
			final ImageButton store_item_img = (ImageButton) convertView
					.findViewById(R.id.store_big_img);

			TextView store_closet_txt = (TextView) convertView
					.findViewById(R.id.store_closet_txt_view);
			TextView store_item_name_txt = (TextView) convertView
					.findViewById(R.id.itemName_txt);

			Button store_item_close_btn = (Button) convertView
					.findViewById(R.id.close_home_btn);

			store_closet_txt.setText("Closeted : "
					+ list.get(position).closeted_item_count);
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

					String msg = DBAdpter.userClosestStore(
							list.get(position).item_id,
							list.get(position).store_id, uid);
					Toast.makeText(getActivity().getApplicationContext(), msg,
							1).show();
				}
			});
			store_item_img.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					registerForContextMenu(store_item_img);
				}
			});

			return convertView;
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		// menu.setHeaderTitle("Context Menu");
		menu.add(0, v.getId(), 0, "Facebook");
		menu.add(0, v.getId(), 0, "Twitter");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getTitle() == "Facebook") {
			Toast.makeText(getActivity().getApplicationContext(), "Facebook", 1)
					.show();
		} else if (item.getTitle() == "Twitter") {
			Toast.makeText(getActivity().getApplicationContext(), "Twitter", 1)
					.show();
		} else {
			return false;
		}
		return true;
	}

}