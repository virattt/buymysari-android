package com.buymysari.fragment;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.buymysari.CircularImageView;
import com.buymysari.DBAdpter;
import com.buymysari.GPSTracker;
import com.buymysari.ImageLoader;
import com.buymysari.MyApplication;
import com.buymysari.R;
import com.buymysari.dto.All_list_home_dto;

public  class HomeFragment extends Fragment {

	ListView lv;
	ArrayList<All_list_home_dto> list;
	ArrayList<All_list_home_dto> newLoadedList;

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
	Button btnLoadMore;
	int pageNumber = 1;
	private ProgressDialog loadMoreProgress;

	int visibleThreshold = 20;
	public ImageLoader imageLoader;

	int NoMoredataAvailable = 0;
	private PopupWindow pwindo;

	int currentX;
	int currentY;
	
	public static boolean listLoaded = false;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.home, container, false);
		app = (MyApplication) getActivity().getApplicationContext();
		lv = (ListView) rootView.findViewById(R.id.home_listview);
		imageLoader = new ImageLoader(getActivity().getApplicationContext());

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		loadMoreProgress = new ProgressDialog(getActivity());
		loadMoreProgress.setMessage("Loading...");

		/*if(listLoaded)
		{
			Log.v("log", " get Save Status if --> ");
		} else {*/
			Log.v("log", " get Save Status else --> " );
			new JSONTask().execute();
			listLoaded = true;
		//}
		
		lv.setOnScrollListener(new OnScrollListener() {
			private int mLastFirstVisibleItem;

			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
			}

			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

				if (scrollState == 0)
					Log.i("a", "scrolling stopped...");

				if (view.getId() == lv.getId()) {
					final int currentFirstVisibleItem = lv
							.getFirstVisiblePosition();
					if (currentFirstVisibleItem > mLastFirstVisibleItem) {
						// mIsScrollingUp = false;
						Log.i("a", "scrolling down...");

						Log.v("log", " NOMOreData  " + NoMoredataAvailable);
						if (NoMoredataAvailable != 1) {
							new loadMoreListView().execute();
							Log.v("log", " NOMOreData if "
									+ NoMoredataAvailable);
						}

					} else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
						// mIsScrollingUp = true;
						Log.i("a", "scrolling up...");
					}

					mLastFirstVisibleItem = currentFirstVisibleItem;
				}
			}
		});
		
		lv.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				final int action = event.getAction();
				
				if (action == MotionEvent.ACTION_DOWN ) {
					Log.v("Log", " Action Down ") ;
				} else if (action == MotionEvent.ACTION_UP) {
					Log.v("", " Action Up ");
					currentX = (int) event.getX();
					currentY = (int) event.getY();
					Log.v("Log", " Action Up  currentX --> " + currentX +"  currenty -- >" + currentX) ;
				}
								
				return false;
			}
		});
		
		return rootView;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("message", "This is my message to be reloaded");
	}

	public class JSONTask extends AsyncTask<String, Void, String> {

		public void onPreExecute() {
			progress = new ProgressDialog(getActivity());
			progress.setMessage("Loading...");
			progress.show();

			gps = new GPSTracker(getActivity().getApplicationContext());

			// check if GPS enabled
			if (gps.canGetLocation()) {

				double latitude = gps.getLatitude();
				double longitude = gps.getLongitude();

				Geocoder geocoder = new Geocoder(getActivity()
						.getApplicationContext(), Locale.ENGLISH);
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
			}

			//	cityName = "ahmedabad";
		}

		@Override
		protected String doInBackground(String... arg) {
			String listSize = "";
			/*
			 * Log.v("log_tag", "list DoinBaCK " + cityName); Log.v("log_tag",
			 * "DBAdpter.fetch_list_home_data" + DBAdpter.fetch_list_home_data);
			 * 
			 * if (DBAdpter.fetch_list_home_data != null) { Log.v("log",
			 * " fetch list Size ---> " + DBAdpter.fetch_list_home_data.size());
			 * list = DBAdpter.fetch_list_home_data; listSize =
			 * DBAdpter.fetch_list_home_data.size() + ""; } else { list = new
			 * ArrayList<All_list_home_dto>(); list =
			 * DBAdpter.getNewsData(cityName, pageNumber + "");
			 * 
			 * Log.v("log_tag", "list_size :: " + list.size()); }
			 */
			list = new ArrayList<All_list_home_dto>();
			list = DBAdpter.getNewsData(cityName, pageNumber + "");

			Log.v("log_tag", "list_size :: " + list.size());

			listSize = list.size() + "";
			return listSize; // This value will be returned to your
								// onPostExecute(result) method
		}

		@Override
		protected void onPostExecute(String result) {
			// Create here your JSONObject...
			Log.v("log_tag", "list ON Post" + result);

			if (Integer.parseInt(result) > 0) {

				adt = new MyListAdapter(getActivity(), list);
				lv.setAdapter(adt);
			} else {

				Toast.makeText(getActivity(), "No Items Available ",
						Toast.LENGTH_LONG).show();
			}

			if (progress != null) {
				progress.dismiss();
			}
		}

		// You'll have to override this method on your other tasks that extend
		// from this one and use your JSONObject as needed

	}

	public class loadMoreListView extends AsyncTask<String, Void, String> {

		public void onPreExecute() {

			loadMoreProgress.show();
			// cityName = "ahmedabad";
		}

		@Override
		protected String doInBackground(String... arg) {
			String listSize = "";

			pageNumber += 1;

			Log.v("log_tag", "list DoinBaCK " + cityName);

			newLoadedList = DBAdpter.getNewsData(cityName, pageNumber + "");

			for (int j = 0; j < newLoadedList.size(); j++) {

				list.add(newLoadedList.get(j));
			}

			listSize = newLoadedList.size() + "";

			return listSize; // This value will be returned to your
								// onPostExecute(result) method
		}

		@Override
		protected void onPostExecute(String result) {
			// Create here your JSONObject...
			Log.v("log_tag", "Load More List ON Post");

			if (Integer.parseInt(result) > 0) {

				Log.v("log", " home if" + result);

				int currentPosition = lv.getFirstVisiblePosition();
				adt = new MyListAdapter(getActivity().getApplicationContext(),
						list);
				lv.setAdapter(adt);
				adt.notifyDataSetChanged();
				lv.setSelectionFromTop(currentPosition + 1, 0);

			} else {

				NoMoredataAvailable = 1;

				Log.v("log", " home else " + result);

				Toast.makeText(getActivity().getApplicationContext(),
						"No More Items Available ", Toast.LENGTH_LONG).show();
				// btnLoadMore.setVisibility(View.GONE);
			}

			if (loadMoreProgress != null) {
				loadMoreProgress.dismiss();
			}

		}

		// You'll have to override this method on your other tasks that extend
		// from this one and use your JSONObject as needed

	}

	public class MyListAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public MyListAdapter(Context context, ArrayList<All_list_home_dto> list) {
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

			URL url = null;
			try {
				url = new URL(list.get(position).picture);
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
				CircularImageView home_ic_img = (CircularImageView) convertView
						.findViewById(R.id.list_home_logo_image);
				home_ic_img.setImageBitmap(bmp);
				home_ic_img.setBorderColor(getResources().getColor(
						R.color.GrayLight));
				home_ic_img.setBorderWidth(0);

			} else {
				Log.v("log", " Below HoneyComb ");

				ImageView imView = (ImageView) convertView
						.findViewById(R.id.list_home_logo_image);
				imView.setImageBitmap(bmp);
			}

			ImageButton home_big_img = (ImageButton) convertView
					.findViewById(R.id.sarees_big_img);
			TextView home_username_txt = (TextView) convertView
					.findViewById(R.id.home_list_username);
			TextView home_view_txt = (TextView) convertView
					.findViewById(R.id.home_view_txt_view);

			TextView itemName_txt = (TextView) convertView
					.findViewById(R.id.itemName_txt);
			TextView closeted_txt = (TextView) convertView
					.findViewById(R.id.closeted_view_txt_view);
			Button close_btn = (Button) convertView
					.findViewById(R.id.close_home_btn);
			ImageView home_view_image = (ImageView) convertView
					.findViewById(R.id.list_home_text_view);

			Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),
					"fonts/ITCAvantGardeStd-BkCn.otf");
			home_username_txt.setTypeface(tf);
			home_view_txt.setTypeface(tf);
			itemName_txt.setTypeface(tf);
			closeted_txt.setTypeface(tf);
			close_btn.setTypeface(tf);

			if (!app.getUserID().equals("")) {

				home_view_image.setVisibility(View.VISIBLE);
				closeted_txt.setVisibility(View.VISIBLE);
				close_btn.setVisibility(View.VISIBLE);
				home_view_txt.setVisibility(View.VISIBLE);

			} else {

				home_view_image.setVisibility(View.INVISIBLE);
				closeted_txt.setVisibility(View.INVISIBLE);
				close_btn.setVisibility(View.INVISIBLE);
				home_view_txt.setVisibility(View.INVISIBLE);

			}

			home_username_txt.setText(list.get(position).store_name.toString());
			home_view_txt.setText(" " + list.get(position).views.toString());
			itemName_txt.setText(list.get(position).name.toString());
			closeted_txt.setText(" "
					+ list.get(position).Closeted_item_track.toString());
			final String uid = app.getUserID();

			imageLoader.DisplayImage(list.get(position).image, home_big_img);

			home_big_img.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					findDoubleClick(list.get(position).store_id,list.get(position).item_id);

					if (!app.getUserID().equals("")) {
						if (mHasDoubleClicked) {
							if (app.getStoreId().equals("")) {
								new ClosetTask(progress).execute(
										list.get(position).item_id,
										list.get(position).store_id, uid);
							//	initiatePopupWindow();
							} else {
							}
						}
					} else {
					}
				}
			});

			close_btn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					if (app.getStoreId().equals("")) {
						new ClosetTask(progress).execute(
								list.get(position).item_id,
								list.get(position).store_id, uid);
					} else {

					}
				}
			});

			return convertView;
		}
	}

	private void initiatePopupWindow() {
		try {
			// We need to get the instance of the LayoutInflater
			LayoutInflater inflater = (LayoutInflater) this.getActivity()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.custom_dialog,
					(ViewGroup) rootView.findViewById(R.id.popup_element));
			pwindo = new PopupWindow(layout, 80, 80, true);
			pwindo.showAsDropDown(lv , currentX , currentY);
			//pwindo.showAtLocation(layout, Gravity.CENTER, currentX, currentY);

		} catch (Exception e) {
			e.printStackTrace();
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
						FragmentTransaction fragmentTransaction = fm
								.beginTransaction();
						StoreDetailFragment fm2 = new StoreDetailFragment();
						fragmentTransaction.replace(R.id.rela_home_fragment,
								fm2, "HELLO");
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
		final Dialog dialog;

		public ClosetTask(ProgressDialog progress) {
			dialog = new Dialog(getActivity());
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.custom_dialog);
		}

		public void onPreExecute() {
		//	dialog.show();
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
			dialog.dismiss();
			Toast.makeText(getActivity().getApplicationContext(), result,
					Toast.LENGTH_LONG).show();

		}
	}

	/*
	 * @Override public void onScroll(AbsListView view, int firstVisibleItem,
	 * int visibleItemCount, int totalItemCount) { }
	 * 
	 * @Override public void onScrollStateChanged(AbsListView view, int
	 * scrollState) { if (scrollState == SCROLL_STATE_IDLE) { if
	 * (lv.getLastVisiblePosition() >= lv.getCount() - visibleThreshold) { new
	 * loadMoreListView().execute(); } } }
	 */
}