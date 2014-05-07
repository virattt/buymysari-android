package com.buymysari.fragment;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.buymysari.CircularImageView;
import com.buymysari.DBAdpter;
import com.buymysari.ImageLoader;
import com.buymysari.MarketPlaceActivity;
import com.buymysari.MyApplication;
import com.buymysari.R;
import com.buymysari.dto.MyStore_list_dto;
import com.buymysari.dto.search_items_dto;
import com.buymysari.fragment.SearchItemsFragment.MyListAdapter;
import com.buymysari.fragment.SearchItemsFragment.loadMoreListView;

public class StoreProfileFragment extends Fragment{
	ListView lv;
	ArrayList<MyStore_list_dto> list = new ArrayList<MyStore_list_dto>();
	ArrayList<MyStore_list_dto> newLoadedList;
	MyApplication app;
	MyListAdapter adtstore;
	View rootView;
	private ProgressDialog progress;
	int pageNumber = 1;
	public ImageLoader imageLoader;
	private ProgressDialog loadMoreProgress;
	Button btnLoadMore;
	CircularImageView store_Name_img;

	int visibleThreshold = 20;
	int NoMoredataAvailable = 0;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_store_profile,
				container, false);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		
		app = (MyApplication) getActivity().getApplicationContext();
		lv = (ListView) rootView.findViewById(R.id.myStore_listview);
		imageLoader = new ImageLoader(getActivity().getApplicationContext());

		/*btnLoadMore = new Button(getActivity());
		btnLoadMore.setText("Load More");
		lv.addFooterView(btnLoadMore);*/

		loadMoreProgress = new ProgressDialog(getActivity());
		loadMoreProgress.setMessage("Loading...");

		progress = new ProgressDialog(getActivity());
		progress.setMessage("Loading...");

		new JSONTask(progress).execute("Home");

		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				FragmentManager fm = getFragmentManager();
				FragmentTransaction fragmentTransaction = fm.beginTransaction();
				StoreDetailFragment fm2 = new StoreDetailFragment();
				fragmentTransaction.replace(R.id.rela_myStore_fragment, fm2,
						"HELLO");
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();
				Bundle bundle = new Bundle();
				bundle.putString("position", list.get(position).store_id);
				fm2.setArguments(bundle);
			}
		});

		/*btnLoadMore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// Starting a new async task
				new loadMoreListView().execute();
			}
		});*/
		
		lv.setOnScrollListener(new OnScrollListener(){
			private int mLastFirstVisibleItem;
			
		    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		      // TODO Auto-generated method stub
		    }
		    public void onScrollStateChanged(AbsListView view, int scrollState) {
		      // TODO Auto-generated method stub
		      
		       if(scrollState == 0) 
		      Log.i("a", "scrolling stopped...");


		        if (view.getId() == lv.getId()) {
		        final int currentFirstVisibleItem = lv.getFirstVisiblePosition();
		         if (currentFirstVisibleItem > mLastFirstVisibleItem) {
		           // mIsScrollingUp = false;
		            Log.i("a", "scrolling down...");
		            
		            Log.v("log"," NOMOreData  " + NoMoredataAvailable);
			        if (NoMoredataAvailable != 1) 
			        {
			        	new loadMoreListView().execute();
			        	Log.v("log"," NOMOreData if " + NoMoredataAvailable);
			        }
		            
		        } else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
		           // mIsScrollingUp = true;
		            Log.i("a", "scrolling up...");
		        }

		        mLastFirstVisibleItem = currentFirstVisibleItem;
		    } 
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
			list.clear();
			list = DBAdpter.getMyStoreData(app.getUserID(), pageNumber + "");

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
				adtstore = new MyListAdapter(getActivity()
						.getApplicationContext());
				lv.setAdapter(adtstore);
			} else {

				Toast.makeText(getActivity().getApplicationContext(),
						"No Store Available ", Toast.LENGTH_LONG).show();
			}

			progress.dismiss();

		}
	}

	public class loadMoreListView extends AsyncTask<String, Void, String> {

		public void onPreExecute() {

			loadMoreProgress.show();

		}

		@Override
		protected String doInBackground(String... arg) {
			String listSize = "";

			pageNumber += 1;

			newLoadedList = new ArrayList<MyStore_list_dto>();
			newLoadedList = DBAdpter.getMyStoreData(app.getUserID(), pageNumber
					+ "");

			Log.v("log_tag", "newLoadedList :: " + newLoadedList.size());

			for (int i = 0; i < newLoadedList.size(); i++) {

				list.add(newLoadedList.get(i));
			}
			listSize = newLoadedList.size() + "";
			return listSize;
		}

		@Override
		protected void onPostExecute(String result) {
			// Create here your JSONObject...
			Log.v("log_tag", "Load More List ON Post");

			if (Integer.parseInt(result) > 0) {
				int currentPosition = lv.getFirstVisiblePosition();
				adtstore = new MyListAdapter(getActivity()
						.getApplicationContext());
				lv.setAdapter(adtstore);
				adtstore.notifyDataSetChanged();
				lv.setSelectionFromTop(currentPosition + 1, 0);
			} else {
				NoMoredataAvailable  = 1;
				Toast.makeText(getActivity().getApplicationContext(),
						"No more Store Available ", Toast.LENGTH_LONG).show();
			//	btnLoadMore.setVisibility(View.GONE);
			}

			if (loadMoreProgress != null) {
				loadMoreProgress.dismiss();
			}
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
			convertView = mInflater.inflate(R.layout.custome_mystorelist, null);
			
			String img_url = list.get(position).image.trim();
			imageLoader = new ImageLoader(getActivity());
			
			Bitmap bmp = getBitmapFromUrl(img_url);
			
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				Log.v("log", " Above HoneyComb ");

				store_Name_img = (CircularImageView) convertView.findViewById(R.id.my_Store_logo_image);
			//	imageLoader.DisplayImage(img_url, store_Name_img);
				store_Name_img.setImageBitmap(bmp);
				store_Name_img.setBorderColor(getResources().getColor(R.color.GrayLight));
				store_Name_img.setBorderWidth(0);
			} else {
				Log.v("log", " Below HoneyComb ");

				ImageView imView = (ImageView) convertView.findViewById(R.id.my_Store_logo_image);
			//	imageLoader.DisplayImage(img_url, imView);
				imView.setImageBitmap(bmp);
			}
			
			TextView store_Name_txt = (TextView) convertView.findViewById(R.id.mystore_list_name);
			TextView txtClosetnumber = (TextView)convertView.findViewById(R.id.txtClosetnumber);
			TextView txtAddressSubScribeProfile =(TextView)convertView.findViewById(R.id.txtAddressSubScribeProfile);
			Button staticbtn = (Button)convertView.findViewById(R.id.button1);
			
			store_Name_txt.setText(list.get(position).name);
			txtClosetnumber.setText(list.get(position).closted_items_count);
			txtAddressSubScribeProfile.setText(list.get(position).address);
			
			
			Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),
				    "fonts/ITCAvantGardeStd-BkCn.otf");
			store_Name_txt.setTypeface(tf);
			txtClosetnumber.setTypeface(tf);
			txtAddressSubScribeProfile.setTypeface(tf);
			staticbtn.setTypeface(tf);
			
			
			
			return convertView;
		}
	}
	
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
	
}
