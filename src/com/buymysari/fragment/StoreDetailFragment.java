package com.buymysari.fragment;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
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
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.buymysari.CircularImageView;
import com.buymysari.DBAdpter;
import com.buymysari.ImageLoader;
import com.buymysari.MyApplication;
import com.buymysari.R;
import com.buymysari.dto.All_list_Store_dto;

public class StoreDetailFragment extends Fragment {

	ListView lv;
	ArrayList<All_list_Store_dto> list = new ArrayList<All_list_Store_dto>();
	MyListAdapter adt;
	View rootView;
	GridView gridView;

	TextView storeName;
	Button store_subscribe_btn;
	MyApplication app;
	private ProgressDialog progress;
	Bundle bundle;
	String myInt;
	ImageLoader imageLoader;
	CircularImageView store_icon;
	ImageView imView;
	TextView txtStoreProfileGridAddress;
	CustomGridViewAdapter adtstore;
	ToggleButton btnStoreProfileList, btnStoreProfileGrid;
	TextView store_url_txt, store_profile_grid_closet_txt,
			subscribe_store_profile_txt;
	Typeface tf ;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.store_detail_list, container,
				false);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		app = (MyApplication) getActivity().getApplicationContext();
		imageLoader = new ImageLoader(getActivity().getApplicationContext());
		 tf = Typeface.createFromAsset(getActivity().getAssets(),
			    "fonts/ITCAvantGardeStd-BkCn.otf");
		store_subscribe_btn = (Button) rootView
				.findViewById(R.id.store_subscribe_btn);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

			store_icon = (CircularImageView) rootView
					.findViewById(R.id.list_Store_logo_image);
			store_icon.setBorderColor(getResources()
					.getColor(R.color.GrayLight));
			store_icon.setBorderWidth(0);

		} else {
			Log.v("log", " Below HoneyComb ");

			imView = (ImageView) rootView
					.findViewById(R.id.list_Store_logo_image);
		}

		storeName = (TextView) rootView.findViewById(R.id.store_list_name);
		gridView = (GridView) rootView.findViewById(R.id.gridView_store_detail);

		store_url_txt = (TextView) rootView
				.findViewById(R.id.store_url_detail_txt);
		store_profile_grid_closet_txt = (TextView) rootView
				.findViewById(R.id.store_profile_grid_detail_closet_txt);
		subscribe_store_profile_txt = (TextView) rootView
				.findViewById(R.id.subscribe_store_detail_profile_txt);
		txtStoreProfileGridAddress = (TextView) rootView
				.findViewById(R.id.txtStoreProfileGridAddress);
		bundle = this.getArguments();
		myInt = bundle.getString("position");
		lv = (ListView) rootView.findViewById(R.id.store_listview);
		Log.v("log_tag", "myInt " + myInt);

		if (!app.getUserID().equals("")) {

			store_subscribe_btn.setVisibility(View.VISIBLE);
			
			
			Boolean msg = DBAdpter.checkSubscribeStore(app.getUserID(), myInt);
			
			if(msg == true){
				store_subscribe_btn.setEnabled(false);
				store_subscribe_btn.setBackgroundColor(Color.parseColor("#41d845"));
				store_subscribe_btn.setText("SUBSCRIBED");
				
			}else{
				
				store_subscribe_btn.setEnabled(true);
				store_subscribe_btn.setBackgroundColor(Color.parseColor("#d84146"));
				store_subscribe_btn.setText("SUBSCRIBE");
			}
		} else {
			store_subscribe_btn.setVisibility(View.INVISIBLE);
		}

		btnStoreProfileGrid = (ToggleButton) rootView
				.findViewById(R.id.btnStoreProfileGrid);
		btnStoreProfileList = (ToggleButton) rootView
				.findViewById(R.id.btnStoreProfileList);
		
		store_url_txt.setTypeface(tf);
		store_profile_grid_closet_txt.setTypeface(tf);
		subscribe_store_profile_txt.setTypeface(tf);
		txtStoreProfileGridAddress.setTypeface(tf);
		storeName.setTypeface(tf);
		store_subscribe_btn.setTypeface(tf);
		
		
		
		
		
		
		
		
		
		
		btnStoreProfileGrid.setOnCheckedChangeListener(changeChecker);
		btnStoreProfileList.setOnCheckedChangeListener(changeChecker);
		/*btnStoreProfileList
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub

						if (isChecked) {
							lv.setVisibility(View.VISIBLE);
							gridView.setVisibility(View.INVISIBLE);
							buttonView
									.setBackgroundResource(R.drawable.unselected_list);
						} else {
							buttonView
									.setBackgroundResource(R.drawable.selected_list);
						}
					}
				});

		btnStoreProfileGrid
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub

						if (isChecked) {
							lv.setVisibility(View.INVISIBLE);
							gridView.setVisibility(View.VISIBLE);
							buttonView
									.setBackgroundResource(R.drawable.unselected_grid);
						} else {
							buttonView
									.setBackgroundResource(R.drawable.selected_grid);
						}
					}
				});
*/
		if (myInt.trim().equals("")) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				store_icon.setVisibility(View.INVISIBLE);
			}
			// store_subscribe_btn.setVisibility(View.INVISIBLE);
			Toast.makeText(getActivity().getApplicationContext(),
					"No Store Detail Available", Toast.LENGTH_LONG).show();

		} else {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				store_icon.setVisibility(View.VISIBLE);
			}
			// store_subscribe_btn.setVisibility(View.VISIBLE);
			progress = new ProgressDialog(getActivity());
			progress.setMessage("Loading...");
			new JSONTask(progress).execute("Home");

		}

		
		store_subscribe_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				new SubscribeTask(progress).execute(app.getUserID(), myInt);
				
				
			}
		});

		return rootView;

	}
	OnCheckedChangeListener changeChecker = new OnCheckedChangeListener() {

	    @Override
	    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	        if (isChecked){
	        	
	        	Log.v("log_tag", "buttonView ::: "+buttonView);
	            if (buttonView == btnStoreProfileGrid) {
	            	btnStoreProfileList.setChecked(false);
	            	lv.setVisibility(View.INVISIBLE);
					gridView.setVisibility(View.VISIBLE);
					btnStoreProfileGrid.setBackgroundResource(R.drawable.selected_grid);
					btnStoreProfileList.setBackgroundResource(R.drawable.unselected_list);
	            	
	              
	            }
	            if (buttonView == btnStoreProfileList) {
	            	btnStoreProfileGrid.setChecked(false);
	            	lv.setVisibility(View.VISIBLE);
					gridView.setVisibility(View.INVISIBLE);
					btnStoreProfileGrid.setBackgroundResource(R.drawable.unselected_grid);
					btnStoreProfileList.setBackgroundResource(R.drawable.selected_list);
	                
	            }
	           
	        }
	    }
	};
	public class JSONTask extends AsyncTask<String, Void, String> {

		public JSONTask(ProgressDialog progress) {
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
				store_url_txt.setText(list.get(0).website);
				store_profile_grid_closet_txt
						.setText(list.get(0).closeted_item_count);
				subscribe_store_profile_txt
						.setText(list.get(0).subscribed_store_count);
				txtStoreProfileGridAddress.setText(list.get(0).address);
				adt = new MyListAdapter(getActivity().getApplicationContext());
				lv.setAdapter(adt);

				adtstore = new CustomGridViewAdapter(getActivity()
						.getApplicationContext());
				gridView.setAdapter(adtstore);
				updateTableList(list.get(0).store_image);
			} else {

				Toast.makeText(getActivity().getApplicationContext(),
						" No Stroe Items Available ", Toast.LENGTH_LONG).show();
			}
			progress.dismiss();

		}

	}

	class CustomGridViewAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public CustomGridViewAdapter(Context context) {
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
			convertView = mInflater.inflate(R.layout.custom_grid_store, null);
			ImageView store_Name_img = (ImageView) convertView
					.findViewById(R.id.item_image);

			if (list.get(position).image != "") {

				imageLoader.DisplayImage(list.get(position).image,
						store_Name_img);

			} else {

				/*
				 * Toast.makeText(getActivity(), "No Item Available",
				 * Toast.LENGTH_SHORT).show();
				 */
			}

			return convertView;
		}
	}

	private void updateTableList(String img) {
		String img_url = img.toString().trim();
		URL url = null;
		try {
			url = new URL(img_url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Bitmap bmp = null;
		try {
			bmp = BitmapFactory.decodeStream(url.openConnection()
					.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			store_icon.setImageBitmap(bmp);
		} else {
			imView.setImageBitmap(bmp);
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
			TextView store_eye_name_txt = (TextView) convertView
					.findViewById(R.id.textView1);

			Button store_item_close_btn = (Button) convertView
					.findViewById(R.id.close_home_btn);
			ImageView eye = (ImageView) convertView
					.findViewById(R.id.items_view);
			store_closet_txt.setText(" "
					+ list.get(position).closeted_item_track);
			store_item_name_txt.setText(list.get(position).name);
			store_eye_name_txt.setText(" " + list.get(position).views);
			
			
			
			store_closet_txt.setTypeface(tf);
			store_item_name_txt.setTypeface(tf);
			store_eye_name_txt.setTypeface(tf);
			store_item_close_btn.setTypeface(tf);
			
			
			
			final String uid = app.getUserID();

			imageLoader.DisplayImage(list.get(position).image, store_item_img);

			if (!app.getUserID().equals("")) {

				store_closet_txt.setVisibility(View.VISIBLE);
				store_item_close_btn.setVisibility(View.VISIBLE);
				eye.setVisibility(View.VISIBLE);
				store_eye_name_txt.setVisibility(View.VISIBLE);

			} else {

				store_closet_txt.setVisibility(View.INVISIBLE);
				store_item_close_btn.setVisibility(View.INVISIBLE);
				eye.setVisibility(View.INVISIBLE);
				store_eye_name_txt.setVisibility(View.INVISIBLE);

			}

			store_item_close_btn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					new ClosetTask(progress).execute(
							list.get(position).item_id,
							list.get(position).store_id, uid);
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

	public class ClosetTask extends AsyncTask<String, Void, String> {

		public ClosetTask(ProgressDialog progress) {
			progress = progress;
		}

		public void onPreExecute() {
			progress.show();

		}

		@Override
		protected String doInBackground(String... arg) {
			String item_id = arg[0];
			String store_id = arg[1];
			String uid = arg[2];

			String msg = DBAdpter.userClosestStore(item_id, store_id, uid);

			return msg;
		}

		@Override
		protected void onPostExecute(String result) {
			// Create here your JSONObject...

			progress.dismiss();
			Toast.makeText(getActivity().getApplicationContext(), result,
					Toast.LENGTH_LONG).show();

		}

	}

	public class SubscribeTask extends AsyncTask<String, Void, String> {

		public SubscribeTask(ProgressDialog progress) {
			progress = progress;
		}

		public void onPreExecute() {
			progress.show();

		}

		@Override
		protected String doInBackground(String... arg) {
			String user_id = arg[0];
			String myInt = arg[1];

			String msg = DBAdpter.storeSubscribeData(user_id, myInt);

			return msg;
		}

		@Override
		protected void onPostExecute(String result) {
			// Create here your JSONObject...

			progress.dismiss();
			store_subscribe_btn.setEnabled(false);
			store_subscribe_btn.setBackgroundColor(Color.parseColor("#41d845"));
			store_subscribe_btn.setText("SUBSCRIBED");
			Toast.makeText(getActivity().getApplicationContext(), result,
					Toast.LENGTH_LONG).show();

		}

	}

}