package com.marketplacestore.fragment;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.marketplacestore.Base64;
import com.marketplacestore.DBAdpter;
import com.marketplacestore.MyApplication;
import com.marketplacestore.R;
import com.marketplacestore.dto.All_list_Store_dto;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class StoreProfileGridFragment  extends Fragment{

	
	ArrayList<All_list_Store_dto> gridlist = new ArrayList<All_list_Store_dto>();
	MyApplication app;
	CustomGridViewAdapter adtstore;
	View rootView;
	private ProgressDialog progress;
	GridView gridView;
	ImageView list_Store_profile_grid_logo_image;
	TextView store_profilegrid_name,store_url_txt,store_profile_grid_closet_txt,subscribe_store_profile_txt;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.store_profile_grid,
				container, false);
		app = (MyApplication) getActivity().getApplicationContext();
		
		gridView = (GridView)rootView.findViewById(R.id.gridView1);
		list_Store_profile_grid_logo_image = (ImageView) rootView
				.findViewById(R.id.list_Store_profile_grid_logo_image);
		store_profilegrid_name = (TextView) rootView.findViewById(R.id.store_profilegrid_name);
		store_url_txt = (TextView) rootView.findViewById(R.id.store_url_txt);
		store_profile_grid_closet_txt = (TextView) rootView.findViewById(R.id.store_profile_grid_closet_txt);
		subscribe_store_profile_txt = (TextView) rootView.findViewById(R.id.subscribe_store_profile_txt);
		//list_Store_profile_grid_logo_image.setImageResource(resId)
		
		progress = new ProgressDialog(getActivity());
		progress.setMessage("Loading...");

		new JSONTask(progress).execute("Home");
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

			gridlist = DBAdpter.getStoreData(app.getStoreId());

			Log.v("log_tag", "list_size ClosetItems :: " + gridlist.size());

			listSize = gridlist.size() + "";
			return listSize; // This value will be returned to your
								// onPostExecute(result) method
		}

		@Override
		protected void onPostExecute(String result) {
			// Create here your JSONObject...
			Log.v("log_tag", "list ON Post");

			adtstore = new CustomGridViewAdapter(getActivity().getApplicationContext());
			gridView.setAdapter(adtstore);
			store_profilegrid_name.setText(gridlist.get(0).store_name);
			store_url_txt.setText(gridlist.get(0).website);
			store_profile_grid_closet_txt.setText("Closet In : "+gridlist.get(0).closeted_item_count);
			subscribe_store_profile_txt.setText("Subscribe : "+gridlist.get(0).subscribed_store_count);
			
			updateTableList(gridlist.get(0).store_image);
			
			progress.dismiss();

		}

		// You'll have to override this method on your other tasks that extend
		// from this one and use your JSONObject as needed

	}

	private void updateTableList(String img) {
		byte[] Image_getByte;
		try {
			Image_getByte = Base64.decode(img);
			ByteArrayInputStream bytes = new ByteArrayInputStream(
					Image_getByte);
			BitmapDrawable bmd = new BitmapDrawable(bytes);
			Bitmap bm = bmd.getBitmap();
			list_Store_profile_grid_logo_image.setImageBitmap(bm);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public class CustomGridViewAdapter  extends BaseAdapter {
		private LayoutInflater mInflater;

		public CustomGridViewAdapter(Context context) {
			mInflater = LayoutInflater.from(context);

		}

		public int getCount() {
			return gridlist.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			convertView = mInflater.inflate(R.layout.custom_grid_store, null);
			ImageView store_Name_img = (ImageView) convertView
					.findViewById(R.id.item_image);

			

			if (gridlist.get(position).image != null) {
				byte[] Image_getByte;
				try {
					Image_getByte = Base64.decode(gridlist.get(position).image);
					ByteArrayInputStream bytes = new ByteArrayInputStream(
							Image_getByte);
					BitmapDrawable bmd = new BitmapDrawable(bytes);
					Bitmap bm = bmd.getBitmap();
					store_Name_img.setImageBitmap(bm);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			return convertView;
		}
	}
}
