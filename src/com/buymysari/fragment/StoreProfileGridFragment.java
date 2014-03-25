package com.buymysari.fragment;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.buymysari.Base64;
import com.buymysari.DBAdpter;
import com.buymysari.MyApplication;
import com.buymysari.R;
import com.buymysari.dto.All_list_Store_dto;

public class StoreProfileGridFragment extends Fragment {

	ArrayList<All_list_Store_dto> gridlist = new ArrayList<All_list_Store_dto>();
	MyApplication app;
	CustomGridViewAdapter adtstore;
	View rootView;
	private ProgressDialog progress;
	GridView gridView;
	String st_id;
	ImageView list_Store_profile_grid_logo_image;
	TextView store_profilegrid_name, store_url_txt,
			store_profile_grid_closet_txt, subscribe_store_profile_txt;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.store_profile_grid,
				container, false);
		app = (MyApplication) getActivity().getApplicationContext();
		st_id = app.getStoreId();

		Log.v("log_tag", "st_id  ::: " + st_id);

		gridView = (GridView) rootView.findViewById(R.id.gridView1);
		list_Store_profile_grid_logo_image = (ImageView) rootView
				.findViewById(R.id.list_Store_profile_grid_logo_image);
		store_profilegrid_name = (TextView) rootView
				.findViewById(R.id.store_profilegrid_name);
		store_url_txt = (TextView) rootView.findViewById(R.id.store_url_txt);
		store_profile_grid_closet_txt = (TextView) rootView
				.findViewById(R.id.store_profile_grid_closet_txt);
		subscribe_store_profile_txt = (TextView) rootView
				.findViewById(R.id.subscribe_store_profile_txt);

		if (st_id.trim().equals("")) {

			gridView.setVisibility(View.INVISIBLE);
			list_Store_profile_grid_logo_image.setVisibility(View.INVISIBLE);
			store_profilegrid_name.setVisibility(View.INVISIBLE);
			store_profilegrid_name.setVisibility(View.INVISIBLE);
			store_url_txt.setVisibility(View.INVISIBLE);
			store_profile_grid_closet_txt.setVisibility(View.INVISIBLE);
			subscribe_store_profile_txt.setVisibility(View.INVISIBLE);

			Toast.makeText(getActivity().getApplicationContext(),
					"No Store Detail Available", Toast.LENGTH_LONG).show();
		} else {
			gridView.setVisibility(View.VISIBLE);
			list_Store_profile_grid_logo_image.setVisibility(View.VISIBLE);
			store_profilegrid_name.setVisibility(View.VISIBLE);
			store_profilegrid_name.setVisibility(View.VISIBLE);
			store_url_txt.setVisibility(View.VISIBLE);
			store_profile_grid_closet_txt.setVisibility(View.VISIBLE);
			subscribe_store_profile_txt.setVisibility(View.VISIBLE);

			progress = new ProgressDialog(getActivity());
			progress.setMessage("Loading...");
			new JSONTask(progress).execute("Home");

		}

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

			gridlist = DBAdpter.getStoreData(st_id);

			listSize = gridlist.size() + "";
			return listSize; // This value will be returned to your
								// onPostExecute(result) method
		}

		@Override
		protected void onPostExecute(String result) {
			// Create here your JSONObject...
			Log.v("log_tag", "list ON Post" + result);

			if (Integer.parseInt(result) > 0) {
				adtstore = new CustomGridViewAdapter(getActivity()
						.getApplicationContext());
				gridView.setAdapter(adtstore);
				store_profilegrid_name.setText(gridlist.get(0).store_name);
				store_url_txt.setText(gridlist.get(0).website);
				store_profile_grid_closet_txt.setText("Closet In : "
						+ gridlist.get(0).closeted_item_count);
				subscribe_store_profile_txt.setText("Subscribe : "
						+ gridlist.get(0).subscribed_store_count);
				updateTableList(gridlist.get(0).store_image);

			} else {

				Toast.makeText(getActivity().getApplicationContext(),
						" No Stroe Items Available ", Toast.LENGTH_LONG).show();
			}

			progress.dismiss();

		}

		// You'll have to override this method on your other tasks that extend
		// from this one and use your JSONObject as needed

	}

	private void updateTableList(String img) {

		if (img.trim().equals("")) {
			list_Store_profile_grid_logo_image
					.setImageResource(R.drawable.ic_launcher);
		} else {
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
	}

	public class CustomGridViewAdapter extends BaseAdapter {
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
