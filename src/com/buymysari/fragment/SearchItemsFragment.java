package com.buymysari.fragment;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import com.buymysari.DBAdpter;
import com.buymysari.MyApplication;
import com.buymysari.R;
import com.buymysari.dto.search_items_dto;

public class SearchItemsFragment extends Fragment {

	Bitmap image_url;
	Bitmap Store_Image_Url;
	
	View rootView;
	private ProgressDialog progress;
	MyApplication app;
	ListView lv;
	
	ArrayList<search_items_dto> list = new ArrayList<search_items_dto>();
	MyListAdapter adt;
	search_items_dto list_home;
	private static final long DOUBLE_PRESS_INTERVAL = 250; // in millis
	private long lastPressTime;
	boolean mHasDoubleClicked;
	String searchText;
	String searchCateId;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.search_items, container, false);

		app = (MyApplication) getActivity().getApplicationContext();
		lv = (ListView) rootView.findViewById(R.id.search_items_listview);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		progress = new ProgressDialog(getActivity());
		progress.setMessage("Loading...");
		
		Bundle bundle = this.getArguments();
		searchText = bundle.getString("searchText");
		searchCateId = bundle.getString("searchCateId");
		Log.v("log_tag", "searchText SearchItemsFragment --> " + searchText + " searchCateId -- > " + searchCateId );
		
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
			Log.v("log_tag", "list DoinBaCK SearchItemsFragment ---> "  + searchText  + " searchCateId " + searchCateId);
			
			list.clear();
			
			Log.v("log_tag", " searchCateId " + searchCateId);
			
			if(searchCateId.equals(""))
			{
				Log.v("log"," cate id  if --> " +searchCateId);
				list = DBAdpter.getSearchItemsData(searchText , "");
			}
			else
			{
				Log.v("log"," cate id  else --> " +searchCateId);
				list = DBAdpter.getSearchItemsData(searchText , searchCateId);
			}
			
			// item_id item_name gender category_name item_image store_id store_name store_image
			
			Log.v("log_tag","list_size --> SearchItemsFragment -->  "+ list.size());
			listSize = list.size() +"";
			return listSize; // This value will be returned to your
								// onPostExecute(result) method
		}

		@Override
		protected void onPostExecute(String result) {
			// Create here your JSONObject...
			Log.v("log_tag", "list ON Post");
			if (Integer.parseInt(result) > 0) {
				adt = new MyListAdapter(getActivity().getApplicationContext());
				lv.setAdapter(adt);
			}
			progress.dismiss();

		}

		// You'll have to override this method on your other tasks that extend
		// from this one and use your JSONObject as needed

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
			convertView = mInflater.inflate(R.layout.custom_home_list, null);

			ImageButton home_ic_img = (ImageButton) convertView
					.findViewById(R.id.list_home_logo_image);
			ImageButton home_big_img = (ImageButton) convertView
					.findViewById(R.id.sarees_big_img);
			TextView home_username_txt = (TextView) convertView
					.findViewById(R.id.home_list_username);
			TextView home_view_txt = (TextView) convertView
					.findViewById(R.id.home_view_txt_view);

			TextView itemName_txt = (TextView) convertView
					.findViewById(R.id.itemName_txt);

			Button close_btn = (Button) convertView
					.findViewById(R.id.close_home_btn);

			home_username_txt.setText(list.get(position).getStore_name().toString());
			home_view_txt.setText("Views: " + list.get(position).getViews().toString());
			itemName_txt.setText(list.get(position).getItem_name().toString());
			final String uid = app.getUserID();
			
			Log.v("log_tag", "Item image :::: " + list.get(position).getItem_image());
			
			image_url = getBitmapFromURL(list.get(position).getItem_image());
			home_big_img.setImageBitmap(image_url);
		
			
		//	image_url = BitmapFactory.decodeStream((InputStream)new URL(list.get(position).getItem_image()).getContent());
			
		/*	if (list.get(position).getItem_image() != null) {
				byte[] Image_getByte;
				try {
					Image_getByte = Base64.decode(list.get(position).getItem_image());
					ByteArrayInputStream bytes = new ByteArrayInputStream(
							Image_getByte);
					BitmapDrawable bmd = new BitmapDrawable(bytes);
					Bitmap bm = bmd.getBitmap();
					home_big_img.setImageBitmap(bm);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}*/

			Log.v("log_tag", " Store picture :::: " + list.get(position).getStore_image());
			Store_Image_Url = getBitmapFromURL(list.get(position).getStore_image());
			home_ic_img.setImageBitmap(Store_Image_Url);
			
			
		//		Store_Image_Url = BitmapFactory.decodeStream((InputStream)new URL(list.get(position).getStore_image()).getContent());
			
			
		/*	if (list.get(position).getStore_image() != null) {
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
			}*/

			home_big_img.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					findDoubleClick(list.get(position).store_id);

					if (mHasDoubleClicked) {
						String msg = DBAdpter.userClosestStore(
								list.get(position).getItem_id(),
								list.get(position).getStore_id(), uid);
						Toast.makeText(getActivity().getApplicationContext(),
								msg, 1).show();
					}

				}
			});
			close_btn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					String msg = DBAdpter.userClosestStore(
							list.get(position).getItem_id(),
							list.get(position).getStore_id(), uid);
					Toast.makeText(getActivity().getApplicationContext(), msg,
							1).show();
				}
			});

			return convertView;
		}
	}

	public static Bitmap getBitmapFromURL(String src) {
	    try {
	        URL url = new URL(src);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setDoInput(true);
	        connection.connect();
	        InputStream input = connection.getInputStream();
	        Bitmap myBitmap = BitmapFactory.decodeStream(input);
	        return myBitmap;
	    } catch (IOException e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	private boolean findDoubleClick(final String str_id) {
		// Get current time in nano seconds.
		long pressTime = System.currentTimeMillis();
		// If double click...
		if (pressTime - lastPressTime <= DOUBLE_PRESS_INTERVAL) {
			mHasDoubleClicked = true;

			// double click event....
		} else { // If not double click....
			mHasDoubleClicked = false;
			Handler myHandler = new Handler() {
				public void handleMessage(Message m) {

					if (!mHasDoubleClicked) {
						// single click event
						FragmentManager fm = getFragmentManager();
						FragmentTransaction fragmentTransaction = fm
								.beginTransaction();
						StoreDetailFragment fm2 = new StoreDetailFragment();
						fragmentTransaction.replace(R.id.rela_search_fragment,
								fm2, "HELLO");
						fragmentTransaction.addToBackStack(null);
						fragmentTransaction.commit();
						Bundle bundle = new Bundle();
						bundle.putString("position", str_id);
						fm2.setArguments(bundle);
					}
				}
			};
			Message m = new Message();
			myHandler.sendMessageDelayed(m, DOUBLE_PRESS_INTERVAL);
		}
		lastPressTime = pressTime;
		return mHasDoubleClicked;
	}
}