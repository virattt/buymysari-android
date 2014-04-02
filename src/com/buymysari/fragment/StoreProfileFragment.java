package com.buymysari.fragment;

import java.util.ArrayList;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.buymysari.DBAdpter;
import com.buymysari.ImageLoader;
import com.buymysari.MyApplication;
import com.buymysari.R;
import com.buymysari.dto.MyStore_list_dto;
import com.buymysari.dto.search_items_dto;
import com.buymysari.fragment.SearchItemsFragment.MyListAdapter;
import com.buymysari.fragment.SearchItemsFragment.loadMoreListView;

public class StoreProfileFragment extends Fragment {
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
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_store_profile,
				container, false);
		app = (MyApplication) getActivity().getApplicationContext();
		lv = (ListView) rootView.findViewById(R.id.myStore_listview);
		imageLoader=new ImageLoader(getActivity().getApplicationContext());
		
		 btnLoadMore = new Button(getActivity());
		btnLoadMore.setText("Load More");
		lv.addFooterView(btnLoadMore);
		
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
				MyStoreItemFragment fm2 = new MyStoreItemFragment();
				fragmentTransaction.replace(R.id.rela_myStore_fragment, fm2,"HELLO");
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();
				Bundle bundle = new Bundle();
				bundle.putString("position", list.get(position).store_id);
				fm2.setArguments(bundle);
			}
		});
		
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
			list.clear();
			list = DBAdpter.getMyStoreData(app.getUserID(),pageNumber+"");

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
				adtstore = new MyListAdapter(getActivity().getApplicationContext());
				lv.setAdapter(adtstore);
			} else {

				Toast.makeText(getActivity().getApplicationContext(),
						"No Profile Data Available ", Toast.LENGTH_LONG).show();
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
			newLoadedList = DBAdpter.getMyStoreData(app.getUserID(),pageNumber+"");
			
		Log.v("log_tag", "newLoadedList :: "+newLoadedList.size());
			
			for(int i=0;i< newLoadedList.size() ;i++)
			{
				
				
				list.add(newLoadedList.get(i));
			}
			listSize = newLoadedList.size() +"";
	        return listSize; 
	    }

	    @Override
	    protected void onPostExecute(String result) {
	        // Create here your JSONObject...
	    	Log.v("log_tag","Load More List ON Post");
	    	
	    	if (Integer.parseInt(result) > 0) {
	    	       int currentPosition = lv.getFirstVisiblePosition();
	    	       adtstore = new MyListAdapter(getActivity().getApplicationContext());
		   			lv.setAdapter(adtstore);
		   			adtstore.notifyDataSetChanged();
	    	          lv.setSelectionFromTop(currentPosition + 1, 0);   
	    	      }
	    	      else
	    	      {
	    	       Toast.makeText(getActivity().getApplicationContext(),
	    	    		   		"No Store Data Available ", Toast.LENGTH_LONG).show();
	    	       btnLoadMore.setVisibility(View.GONE);
	    	      }
	    	
	    	
	    	
	    	if(loadMoreProgress != null)
	    	{
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
			ImageButton store_Name_img = (ImageButton) convertView
					.findViewById(R.id.my_Store_logo_image);

			TextView store_Name_txt = (TextView) convertView
					.findViewById(R.id.mystore_list_name);

			store_Name_txt.setText(list.get(position).name);

			
			
			imageLoader.DisplayImage(list.get(position).image, store_Name_img);
			
			store_Name_img.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

				}
			});

			return convertView;
		}
	}
}
