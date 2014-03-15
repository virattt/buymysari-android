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
import com.marketplacestore.fragment.StoreProfileGridFragment.CustomGridViewAdapter;
import com.marketplacestore.fragment.StoreProfileGridFragment.JSONTask;

public class StoreDetailFragment extends Fragment {

	ListView lv;
	ArrayList<All_list_Store_dto> list = new ArrayList<All_list_Store_dto>();
	MyListAdapter adt;
	View rootView;
	ImageButton store_icon;
	TextView storeName;
	Button subscribe;
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

		store_icon = (ImageButton) rootView
				.findViewById(R.id.list_Store_logo_image);
		storeName = (TextView) rootView.findViewById(R.id.store_list_name);
		bundle = this.getArguments();
		myInt = bundle.getString("position");

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		progress = new ProgressDialog(getActivity());
		progress.setMessage("Loading...");

		new JSONTask(progress).execute("Home");
		

		lv = (ListView) rootView.findViewById(R.id.store_listview);

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

			Log.v("log_tag", "list_size ClosetItems :: " + list.size());

			listSize = list.size() + "";
			return listSize; // This value will be returned to your
								// onPostExecute(result) method
		}

		@Override
		protected void onPostExecute(String result) {
			// Create here your JSONObject...
			Log.v("log_tag", "list ON Post");

			storeName.setText(list.get(0).store_name);
			adt = new MyListAdapter(getActivity());
			lv.setAdapter(adt);

			updateTableList(list.get(0).store_image);

			progress.dismiss();

		}

		// You'll have to override this method on your other tasks that extend
		// from this one and use your JSONObject as needed

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
			ImageButton store_item_img = (ImageButton) convertView
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

			return convertView;
		}
	}

}