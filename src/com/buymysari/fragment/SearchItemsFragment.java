package com.buymysari.fragment;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
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

import com.buymysari.CircularImageView;
import com.buymysari.DBAdpter;
import com.buymysari.ImageLoader;
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
	ArrayList<search_items_dto> newLoadedList;
	
	MyListAdapter adt;
	search_items_dto list_home;
	private static final long DOUBLE_PRESS_INTERVAL = 250; // in millis
	private long lastPressTime;
	boolean mHasDoubleClicked;
	String searchText;
	String searchCateId;
	
	int pageNumber = 1;
	 public ImageLoader imageLoader; 
	 private ProgressDialog loadMoreProgress;
	 Button btnLoadMore;
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
		
		imageLoader=new ImageLoader(getActivity().getApplicationContext());

		progress = new ProgressDialog(getActivity());
		progress.setMessage("Loading...");
		
		loadMoreProgress = new ProgressDialog(getActivity());
		loadMoreProgress.setMessage("Loading...");
		
		btnLoadMore = new Button(getActivity());
		btnLoadMore.setText("Load More");
		lv.addFooterView(btnLoadMore);
		
		Bundle bundle = this.getArguments();
		searchText = bundle.getString("searchText");
		searchCateId = bundle.getString("searchCateId");
		Log.v("log_tag", "searchText SearchItemsFragment --> " + searchText + " searchCateId -- > " + searchCateId );
		
		new JSONTask(progress).execute("Home");
		
		btnLoadMore.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		        // Starting a new async task
		        new loadMoreListView().execute();
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
			Log.v("log_tag", "list DoinBaCK SearchItemsFragment ---> "  + searchText  + " searchCateId " + searchCateId);
			
			list.clear();
			
			Log.v("log_tag", " searchCateId " + searchCateId);
			
			list = DBAdpter.getSearchItemsData(searchText , searchCateId , pageNumber+"");
			
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
			
			progress.dismiss();
			
			if (Integer.parseInt(result) > 0) {
				adt = new MyListAdapter();
				lv.setAdapter(adt);
			}
			else
			{
				Toast.makeText(getActivity().getApplicationContext(), "There is no Search item", Toast.LENGTH_LONG).show();
			}
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
	        
	        Log.v("log_tag","Search Load More searchCateId --> " + searchCateId + " searchText  --> " + searchText);
	        
	        newLoadedList = new ArrayList<search_items_dto>();
			newLoadedList = DBAdpter.getSearchItemsData(searchText , searchCateId , pageNumber+""); 
			
			for(int i=0;i< newLoadedList.size() ;i++)
			{
				/*search_items_dto search_items = new search_items_dto();
				
				search_items.setItem_id(newLoadedList.get(i).getItem_id());
				search_items.setItem_name(newLoadedList.get(i).getItem_name());
				search_items.setGender(newLoadedList.get(i).getGender());
				search_items.setItem_image(newLoadedList.get(i).getItem_image());

				search_items.setStore_id(newLoadedList.get(i).getStore_id());
				search_items.setStore_name(newLoadedList.get(i).getStore_name());
				search_items.setStore_image(newLoadedList.get(i).getStore_image());
				search_items.setViews(newLoadedList.get(i).getViews());*/
				
				list.add(newLoadedList.get(i));
			}
	        
			listSize = newLoadedList.size()+"";
			
	        return listSize; // This value will be returned to your onPostExecute(result) method
	    }

	    @Override
	    protected void onPostExecute(String result) {
	        // Create here your JSONObject...
	    	Log.v("log_tag","Load More List ON Post");
	    	
	    	if (Integer.parseInt(result) > 0) {
	    		Log.v("log"," Search if" + result);
	    		
	    		int currentPosition = lv.getFirstVisiblePosition();
	    		adt = new MyListAdapter();
		    	lv.setAdapter(adt);
		        adt.notifyDataSetChanged();
		        lv.setSelectionFromTop(currentPosition + 1, 0);   
	    	}
	    	else
	    	{
	    		Log.v("log"," Search else" + result);
	    		
	    		Toast.makeText(getActivity().getApplicationContext(),
					"No More Search Items Available ", Toast.LENGTH_LONG).show();
	    		btnLoadMore.setVisibility(View.GONE);
	    	}
	    	
	    	if(loadMoreProgress != null)
	    	{
	    		loadMoreProgress.dismiss();
	    	}
	    }

	    // You'll have to override this method on your other tasks that extend from this one and use your JSONObject as needed
	   
	}
	

	 class MyListAdapter extends BaseAdapter {
		
		private class ViewHolder {
			public TextView home_username_txt;
			public CircularImageView home_ic_img;
			public ImageButton home_big_img;
			public TextView home_view_txt;
			public TextView itemName_txt;
			public Button close_btn;
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
			
			final ViewHolder holder;
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(R.layout.custom_home_list, parent, false);	 
			holder = new ViewHolder();
				
			holder.home_ic_img = (CircularImageView) convertView.findViewById(R.id.list_home_logo_image);
			holder.home_big_img = (ImageButton) convertView.findViewById(R.id.sarees_big_img);
			holder.home_username_txt = (TextView) convertView.findViewById(R.id.home_list_username);
			holder.home_view_txt = (TextView) convertView.findViewById(R.id.home_view_txt_view);
			holder.itemName_txt = (TextView) convertView.findViewById(R.id.itemName_txt);
			holder.close_btn = (Button) convertView.findViewById(R.id.close_home_btn);

			holder.home_ic_img.setBorderColor(getResources().getColor(R.color.GrayLight));
			holder.home_ic_img.setBorderWidth(0);
			
			holder.home_username_txt.setText(list.get(position).getStore_name().toString());
			holder.home_view_txt.setText(" " + list.get(position).getViews().toString());
			holder.itemName_txt.setText(list.get(position).getItem_name().toString());
			final String uid = app.getUserID();
			
			Log.v("log_tag", "Item image :::: " + list.get(position).getItem_image());
			
			imageLoader.DisplayImage(list.get(position).getItem_image(), holder.home_big_img);
			 
			Log.v("log_tag", " Store picture :::: " + list.get(position).getStore_image());
		
			imageLoader.DisplayImage(list.get(position).getStore_image(), holder.home_ic_img);
			
			
			holder.home_big_img.setOnClickListener(new View.OnClickListener() {

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
			
			holder.close_btn.setOnClickListener(new View.OnClickListener() {

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
			convertView.setTag(holder);
			}
			else
			{
				holder = (ViewHolder) convertView.getTag();
			}
			return convertView;
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