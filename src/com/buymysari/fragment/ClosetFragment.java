package com.buymysari.fragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.AdapterView;
import android.widget.FrameLayout;
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
import com.buymysari.dto.All_list_home_dto;
import com.buymysari.dto.Closet_dto;
import com.buymysari.fragment.StoreProfileGridFragment.DeletDataTask;

public class ClosetFragment extends Fragment {

	ListView lv;
	ArrayList<Closet_dto> list = new ArrayList<Closet_dto>();
	ArrayList<Closet_dto> newLoadedList;

	MyListAdapter adt;
	All_list_home_dto list_home;

	private ProgressDialog progress;
	MyApplication app;

	public ImageLoader imageLoader;

	private ProgressDialog loadMoreProgress;
	Button btnLoadMore;
	int pageNumber = 1;
	int NoMoredataAvailable = 0;
	TextView txtName, txtWebSite, txt_closet_text;

	int _listposition;
	
	CustomGridViewAdapter adtstore;
	FrameLayout closet_grid_layout;
	GridView closet_gridView;

	ToggleButton btnClosetList, btnClosetGrid;
	String userId;

	Button btnEditUserProfile;
	View rootView;
	Typeface tf;
	int myLastVisiblePos;
	private int mPreviousTotal = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.closet, container, false);
		lv = (ListView) rootView.findViewById(R.id.closet_listview);
		tf = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/ITCAvantGardeStd-BkCn.otf");
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		txtName = (TextView) rootView.findViewById(R.id.txtStoreName);
		txtWebSite = (TextView) rootView.findViewById(R.id.txtStoreWebsite);
		txt_closet_text = (TextView) rootView
				.findViewById(R.id.txt_closet_text);
		app = (MyApplication) getActivity().getApplicationContext();

		String FirstNAme = DBAdpter.fetch_UserDetail_data.get(0)
				.getFirst_name();

		userId = app.getUserID();
		String lastName = DBAdpter.fetch_UserDetail_data.get(0).getLast_name();
		String userImage = DBAdpter.fetch_UserDetail_data.get(0)
				.getStrore_profile_image();
		String emailId = DBAdpter.fetch_UserDetail_data.get(0).getEmail();
		String Mobile = DBAdpter.fetch_UserDetail_data.get(0).getMobile();

		Log.v("log", " userName " + FirstNAme + " " + lastName + " emailId "
				+ emailId + " mobile " + Mobile);
		txtName.setText(FirstNAme + " " + lastName);
		txtWebSite.setText(emailId);
		txt_closet_text.setText(FirstNAme + "'s Closet");

		closet_grid_layout = (FrameLayout) rootView
				.findViewById(R.id.closet_gridview_framelayout);
		closet_gridView = (GridView) rootView
				.findViewById(R.id.closet_gridView);

		myLastVisiblePos = closet_gridView.getFirstVisiblePosition();

		btnEditUserProfile = (Button) rootView
				.findViewById(R.id.btnEditUserProfile);
		btnEditUserProfile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				FragmentManager fm = getFragmentManager();
				FragmentTransaction fragmentTransaction = fm.beginTransaction();
				ProfileFragment fm2 = new ProfileFragment();
				fragmentTransaction
						.replace(R.id.rela_closet_view, fm2, "HELLO");
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();

			}
		});

		String img_url = userImage;
		imageLoader = new ImageLoader(getActivity());
		Bitmap bmp = getBitmapFromUrl(img_url);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			Log.v("log", " Above HoneyComb ");

			CircularImageView Store_ic_img = (CircularImageView) rootView
					.findViewById(R.id.imgStoreProfileCloset);
			// imageLoader.DisplayImage(img_url, Store_ic_img);
			Store_ic_img.setImageBitmap(bmp);
			Store_ic_img.setBorderColor(getResources().getColor(
					R.color.GrayLight));
			Store_ic_img.setBorderWidth(0);
		} else {
			Log.v("log", " Below HoneyComb ");

			ImageView imView = (ImageView) rootView
					.findViewById(R.id.imgStoreProfileCloset);
			imView.setImageBitmap(bmp);
			// imageLoader.DisplayImage(img_url, imView);
		}

		imageLoader = new ImageLoader(getActivity().getApplicationContext());

		progress = new ProgressDialog(getActivity());
		progress.setMessage("Loading...");

		loadMoreProgress = new ProgressDialog(getActivity());
		loadMoreProgress.setMessage("Loading...");

		btnClosetGrid = (ToggleButton) rootView
				.findViewById(R.id.btnClosetGrid);
		btnClosetList = (ToggleButton) rootView
				.findViewById(R.id.btnClosetList);

		txtName.setTypeface(tf);
		txtWebSite.setTypeface(tf);
		txt_closet_text.setTypeface(tf);
		btnEditUserProfile.setTypeface(tf);

		btnClosetGrid.setOnCheckedChangeListener(changeChecker);
		btnClosetList.setOnCheckedChangeListener(changeChecker);

		new JSONTask(progress).execute("Home");

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

		closet_gridView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

				// TODO Auto-generated method stub
				if (scrollState == 0)
					Log.v("log", " Gridview scrolling stopped...");

				if (view.getId() == closet_gridView.getId()) {
					Log.v("log",
							" getFirstVisiblePosition "
									+ view.getFirstVisiblePosition()
									+ " myLastVisiblePos " + myLastVisiblePos);
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

				final int currentFirstVisibleItem = view
						.getFirstVisiblePosition();

				Log.v("log", " Prev Total " + mPreviousTotal + " total count "
						+ totalItemCount);

				if (totalItemCount > mPreviousTotal) {

					mPreviousTotal = totalItemCount;
					Log.v("log", " Prev Total if " + mPreviousTotal
							+ " total count " + totalItemCount);
				}

				if (currentFirstVisibleItem > myLastVisiblePos) {
					// mIsScrollingUp = false;
					Log.v("log", " Gridview scrolling down... NOMOreData  "
							+ NoMoredataAvailable);

					if (NoMoredataAvailable != 1) {
						new loadMoreListView().execute();
						Log.v("log", " NOMOreData if " + NoMoredataAvailable);
					}

				} else if (currentFirstVisibleItem < myLastVisiblePos) {
					// mIsScrollingUp = true;
					Log.v("log", " Gridview... scrolling up...");
				}

				myLastVisiblePos = currentFirstVisibleItem;

			}
		});

	/*	closet_gridView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub

				Log.v("log" ," position grid " + position);
				
				_listposition = position;

				return false;
			}
		});

		registerForContextMenu(closet_gridView);*/
		
		return rootView;
	}

	OnCheckedChangeListener changeChecker = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked) {

				Log.v("log_tag", "buttonView ::: " + buttonView);
				if (buttonView == btnClosetGrid) {
					btnClosetList.setChecked(false);
					lv.setVisibility(View.INVISIBLE);
					closet_gridView.setVisibility(View.VISIBLE);
					btnClosetGrid
							.setBackgroundResource(R.drawable.selected_grid);
					btnClosetList
							.setBackgroundResource(R.drawable.unselected_list);

				}
				if (buttonView == btnClosetList) {
					btnClosetGrid.setChecked(false);
					lv.setVisibility(View.VISIBLE);
					closet_gridView.setVisibility(View.INVISIBLE);
					btnClosetGrid
							.setBackgroundResource(R.drawable.unselected_grid);
					btnClosetList
							.setBackgroundResource(R.drawable.selected_list);

				}

			}
		}
	};

	public class loadMoreListView extends AsyncTask<String, Void, String> {

		public void onPreExecute() {
			loadMoreProgress.show();
		}

		@Override
		protected String doInBackground(String... arg) {
			String listSize = "";

			pageNumber += 1;

			newLoadedList = new ArrayList<Closet_dto>();
			newLoadedList = DBAdpter.getUserClosetData(app.getUserID(),
					pageNumber + "");

			for (int i = 0; i < newLoadedList.size(); i++) {
				list.add(newLoadedList.get(i));
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
				Log.v("log", " Search if" + result);

				int currentPosition = lv.getFirstVisiblePosition();
				adt = new MyListAdapter(getActivity().getApplicationContext());
				lv.setAdapter(adt);
				adt.notifyDataSetChanged();
				lv.setSelectionFromTop(currentPosition + 1, 0);

				adtstore = new CustomGridViewAdapter(getActivity()
						.getApplicationContext());
				closet_gridView.setAdapter(adtstore);
				adtstore.notifyDataSetChanged();
				closet_gridView.setSelection(currentPosition + 1);

			} else {
				Log.v("log", " Search else" + result);
				NoMoredataAvailable = 1;
				Toast.makeText(getActivity().getApplicationContext(),
						"No More Closeted Items Available ", Toast.LENGTH_LONG)
						.show();
				// btnLoadMore.setVisibility(View.GONE);
			}

			if (loadMoreProgress != null) {
				loadMoreProgress.dismiss();
			}
		}

		// You'll have to override this method on your other tasks that extend
		// from this one and use your JSONObject as needed

	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.share, menu);

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.share : {
			
			    Log.v("log", " path --> " + list.get(_listposition).image);
			    
			    String imgPath = list.get(_listposition).image;
			    Bitmap bmp = getBitmapFromUrl(imgPath);
			    
			    Intent share = new Intent(Intent.ACTION_SEND);
			    share.setType("image/jpeg");
			    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			    bmp.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
			     
		        	  File f = new File(Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpeg");
					    try {
					        f.createNewFile();
					        FileOutputStream fo = new FileOutputStream(f);
					        fo.write(bytes.toByteArray());
					    } catch (IOException e1) {                       
					            e1.printStackTrace();
					    }
					    share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/temporary_file.jpeg"));
			  
			    startActivity(Intent.createChooser(share, "Share Image"));
			    break;
			}
		}
		return super.onContextItemSelected(item);
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
			Log.v("log_tag", " DoinBaCK Closet UserID " + app.getUserID());

			list = DBAdpter.getUserClosetData(app.getUserID(), pageNumber + "");
			Log.v("log_tag", "list_size ClosetItems :: " + list.size());

			listSize = list.size() + "";
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

				adtstore = new CustomGridViewAdapter(getActivity()
						.getApplicationContext());
				closet_gridView.setAdapter(adtstore);
			} else {
				Toast.makeText(getActivity().getApplicationContext(),
						"No Closeted Item Available ", Toast.LENGTH_LONG)
						.show();
			}
			progress.dismiss();
		}

		// You'll have to override this method on your other tasks that extend
		// from this one and use your JSONObject as needed

	}

	// list.get(position).getStore_image()
	public Bitmap getBitmapFromUrl(String urlStore) {
		URL url = null;
		try {
			url = new URL(urlStore);
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
		return bmp;
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
			convertView = mInflater.inflate(R.layout.custom_home_list, null);

			String img_url = list.get(position).getStore_image().trim();
			imageLoader = new ImageLoader(getActivity());

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				Log.v("log", " Above HoneyComb ");

				CircularImageView home_ic_img = (CircularImageView) convertView
						.findViewById(R.id.list_home_logo_image);
				imageLoader.DisplayImage(img_url, home_ic_img);
				home_ic_img.setBorderColor(getResources().getColor(
						R.color.GrayLight));
				home_ic_img.setBorderWidth(0);
			} else {
				Log.v("log", " Below HoneyComb ");

				ImageView imView = (ImageView) convertView
						.findViewById(R.id.list_home_logo_image);
				imageLoader.DisplayImage(img_url, imView);
			}

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

			close_btn.setVisibility(View.INVISIBLE);
			home_username_txt.setText(list.get(position).getStore_name());

			home_view_txt.setText(" "
					+ list.get(position).getCloseted_item_track());

			itemName_txt.setText(list.get(position).getName());

			imageLoader.DisplayImage(list.get(position).getImage(),
					home_big_img);

			home_username_txt.setTypeface(tf);
			home_view_txt.setTypeface(tf);
			itemName_txt.setTypeface(tf);

			return convertView;
		}
	}

	public class CustomGridViewAdapter extends BaseAdapter {
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
			convertView = mInflater.inflate(R.layout.closet_custom_grid, null);

			ImageView closet_big_img = (ImageView) convertView
					.findViewById(R.id.closet_item_image);
			if (list.get(position).getImage() != "") {

				imageLoader.DisplayImage(list.get(position).getImage(),
						closet_big_img);

			} else {

				/*
				 * Toast.makeText(getActivity(), "No Item Available",
				 * Toast.LENGTH_SHORT).show();
				 */
			}

			return convertView;
		}
	}

}
