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

import com.buymysari.Base64;
import com.buymysari.DBAdpter;
import com.buymysari.GPSTracker;
import com.buymysari.MyApplication;
import com.buymysari.R;
import com.buymysari.dto.All_list_home_dto;

public class HomeFragment extends Fragment {
	
	ListView lv;
	ArrayList<All_list_home_dto> list;
	MyListAdapter adt;
	All_list_home_dto list_home;
	String cityName;
	View rootView;
	private static final long DOUBLE_PRESS_INTERVAL = 250; // in millis
	private long lastPressTime;
	boolean mHasDoubleClicked;
	ProgressDialog progress;
	MyApplication app;
	GPSTracker gps;
	int mCurCheckPosition = 0;
	 
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.home, container, false);
		app = (MyApplication) getActivity().getApplicationContext();
		lv = (ListView)	rootView.findViewById(R.id.home_listview);
		
		//cityName = getActivity().getIntent().getExtras().getString("cityName").toString();
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		
		 if (savedInstanceState != null) {
	            // Restore last state for checked position.
	            mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
	            Log.v("log", " get Save Status if --> " + mCurCheckPosition);
	     }
		 else
		 {
			 Log.v("log", " get Save Status else --> " + mCurCheckPosition);
			 new JSONTask().execute();
		 }
		
		//progress = new ProgressDialog(getActivity());
		//progress.setMessage("Loading...");
		
		return rootView;
	}
	
	@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        
        this.getActivity().getSupportFragmentManager().putFragment(outState,"listAdded", HomeFragment.this);
        		
        //outState.putInt("curChoice", mCurCheckPosition);
    }

	public class JSONTask extends AsyncTask<String, Void, String> {
		
		public void onPreExecute() {
			progress = new ProgressDialog(getActivity());
			progress.setMessage("Loading...");
			progress.show(); 
		    
			/*  gps = new GPSTracker(getActivity().getApplicationContext());

		    // check if GPS enabled
		    if (gps.canGetLocation()) {

		     double latitude = gps.getLatitude();
		     double longitude = gps.getLongitude();

		     Geocoder geocoder = new Geocoder(getActivity().getApplicationContext(),
		       Locale.ENGLISH);
		     List<Address> addresses;
		     try {

		      addresses = geocoder
		        .getFromLocation(latitude, longitude, 1);
		      Log.v("log_tag", "cityName ::: " + addresses);

		      cityName = addresses.get(0).getLocality();
		      Log.v("log_tag", "cityName ::: " + cityName);

		     } catch (IOException e) {
		      // TODO Auto-generated catch block
		      e.printStackTrace();
		     }

		    } else {

		     gps.showSettingsAlert();
		    }*/
			
			cityName = "ahmedabad";
		}
		
	    @Override
	    protected String doInBackground(String... arg) {
	        String listSize = "";
	        Log.v("log_tag","list DoinBaCK " + cityName);
	        
	        if(DBAdpter.fetch_list_home_data != null)
	        {
	        	Log.v("log"," fetch list Size ---> " + DBAdpter.fetch_list_home_data.size());
	        	list = DBAdpter.fetch_list_home_data;
	        	listSize = DBAdpter.fetch_list_home_data.size() + "";
	        }
	        else
	        {
	        	list = new ArrayList<All_list_home_dto>();
	        	
	        	list = DBAdpter.getNewsData(cityName);
	        	Log.v("log_tag","list_size :: "+ list.size());
		        listSize = list.size() +"";
        	}
	        
	        return listSize; // This value will be returned to your onPostExecute(result) method
	    }

	    @Override
	    protected void onPostExecute(String result) {
	        // Create here your JSONObject...
	    	Log.v("log_tag","list ON Post");
	    	
	    	if (Integer.parseInt(result) > 0) {
	    		adt = new MyListAdapter(getActivity(),list);
				lv.setAdapter(adt);
			} else {
				Toast.makeText(getActivity().getApplicationContext(),
						"No Items Available ", Toast.LENGTH_LONG).show();
			}
	   
	    	if(progress != null)
	    	{
	    		progress.dismiss();
	    	}
	    }

	    // You'll have to override this method on your other tasks that extend from this one and use your JSONObject as needed
	   
	}
	
	public class MyListAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public MyListAdapter(Context context ,ArrayList<All_list_home_dto> list) {
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

			home_username_txt.setText(list.get(position).store_name.toString());
			home_view_txt.setText("Views: "
					+ list.get(position).views.toString());
			itemName_txt.setText(list.get(position).name.toString());
			final String uid= app.getUserID();
			Log.v("log_tag", "image :::: " + list.get(position).image);
			if (list.get(position).image.equals("")) {
				byte[] Image_getByte;
				try {
					Image_getByte = Base64.decode(list.get(position).image);
					ByteArrayInputStream bytes = new ByteArrayInputStream(
							Image_getByte);
					BitmapDrawable bmd = new BitmapDrawable(bytes);
					Bitmap bm = bmd.getBitmap();
					
					/*	Bitmap bm = BitmapFactory.decodeByteArray(
							Image_getByte, 0,
							Image_getByte.length); */
					
					home_big_img.setImageBitmap(bm);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			Log.v("log_tag", "picture : position --> " + position);
		/*	Log.v("log_tag", "picture :::: " + list.get(position).picture);

			if (list.get(position).picture.equals("")) {
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
         */
			home_big_img.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					findDoubleClick(list.get(position).store_id,
							list.get(position).item_id);
					
					if (mHasDoubleClicked) {
						new ClosetTask(progress).execute(list.get(position).item_id,list.get(position).store_id,uid);
					}
				}
			});
			close_btn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					
					new ClosetTask(progress).execute(list.get(position).item_id,list.get(position).store_id,uid);
				}
			});
			
			return convertView;
		}
	}

	private boolean findDoubleClick(final String str_id, final String itemId) {
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
						FragmentManager fm = getFragmentManager();
						FragmentTransaction fragmentTransaction = fm.beginTransaction();
						StoreDetailFragment fm2 = new StoreDetailFragment();
						fragmentTransaction.replace(R.id.rela_home_fragment,fm2, "HELLO");
						fragmentTransaction.addToBackStack(null);
						fragmentTransaction.commit();
						Bundle bundle = new Bundle();
						bundle.putString("position", str_id);
						fm2.setArguments(bundle);
						DBAdpter.updateItemView(itemId);
					}
				}
			};
			Message m = new Message();
			myHandler.sendMessageDelayed(m, DOUBLE_PRESS_INTERVAL);
		}
		lastPressTime = pressTime;
		return mHasDoubleClicked;
	}
	
	public class ClosetTask extends AsyncTask<String, Void, String> {

		public ClosetTask(ProgressDialog progress) {
			progress = progress;
		}

		public void onPreExecute() {
	//		progress.show();
		
		}

		@Override
		protected String doInBackground(String... arg) {
			String item_id = arg[0];
			String store_id = arg[1];
			String uid = arg[2];
			
			String msg = DBAdpter.userClosestStore(
					item_id,
					store_id, uid);
			
			return msg; 
		}

		@Override
		protected void onPostExecute(String result) {
			// Create here your JSONObject...
			
		//	progress.dismiss();
			Toast.makeText(getActivity().getApplicationContext(), result,
					Toast.LENGTH_LONG).show();

		}

		

	}
	
}