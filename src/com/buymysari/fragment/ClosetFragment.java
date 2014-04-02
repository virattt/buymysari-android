package com.buymysari.fragment;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
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
import com.buymysari.dto.All_list_home_dto;
import com.buymysari.dto.Closet_dto;

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
	 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.closet, container, false);
		lv = (ListView) rootView.findViewById(R.id.closet_listview);
		
		app = (MyApplication) getActivity().getApplicationContext();
		imageLoader=new ImageLoader(getActivity().getApplicationContext());
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		
		progress = new ProgressDialog(getActivity());
		progress.setMessage("Loading...");
		
		loadMoreProgress = new ProgressDialog(getActivity());
		loadMoreProgress.setMessage("Loading...");
		
		btnLoadMore = new Button(getActivity());
		btnLoadMore.setText("Load More");
		lv.addFooterView(btnLoadMore);
		
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
	
	
public class loadMoreListView extends AsyncTask<String, Void, String> {
		
		public void onPreExecute() {
			
			loadMoreProgress.show(); 
		    
		}
		
	    @Override
	    protected String doInBackground(String... arg) {
	        String listSize = "";
	        
	        pageNumber += 1;
	        
	        newLoadedList = new ArrayList<Closet_dto>();
	        newLoadedList = DBAdpter.getUserClosetData(app.getUserID() , pageNumber+"");
			
			for(int i=0;i< newLoadedList.size() ;i++)
			{
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
	    		adt = new MyListAdapter(getActivity().getApplicationContext());
	    		lv.setAdapter(adt);
		        adt.notifyDataSetChanged();
		        lv.setSelectionFromTop(currentPosition + 1, 0);   
	    	}
	    	else
	    	{
	    		Log.v("log"," Search else" + result);
	    		
	    		Toast.makeText(getActivity().getApplicationContext(),
					"No More Closeted Items Available ", Toast.LENGTH_LONG).show();
	    		btnLoadMore.setVisibility(View.GONE);
	    	}
	    	
	    	if(loadMoreProgress != null)
	    	{
	    		loadMoreProgress.dismiss();
	    	}
	    }

	    // You'll have to override this method on your other tasks that extend from this one and use your JSONObject as needed
	   
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
	        Log.v("log_tag"," DoinBaCK Closet UserID " + app.getUserID());
	        
	        list = DBAdpter.getUserClosetData(app.getUserID() , pageNumber+"");
			Log.v("log_tag","list_size ClosetItems :: "+ list.size());
	        
	        listSize = list.size() +"";
	        return listSize; // This value will be returned to your onPostExecute(result) method
	    }

	    @Override
	    protected void onPostExecute(String result) {
	        // Create here your JSONObject...
	    	Log.v("log_tag","list ON Post");
	    	if (Integer.parseInt(result) > 0) {
	    		adt = new MyListAdapter(getActivity().getApplicationContext());
	    		lv.setAdapter(adt);
	    	}
	    	else
	    	{
	    		Toast.makeText(getActivity().getApplicationContext(),
						"No Closeted Item Available ", Toast.LENGTH_LONG).show();	
	    	}
			 progress.dismiss();
			
	    }

	    // You'll have to override this method on your other tasks that extend from this one and use your JSONObject as needed
	   
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
				convertView = mInflater.inflate(R.layout.custom_home_list,
						null);
				
						CircularImageView home_ic_img = (CircularImageView) convertView.findViewById(R.id.list_home_logo_image);
						home_ic_img.setBorderColor(getResources().getColor(R.color.GrayLight));
						home_ic_img.setBorderWidth(0);
						home_ic_img.addShadow();
						
						ImageButton home_big_img = (ImageButton) convertView.findViewById(R.id.sarees_big_img);
						TextView home_username_txt = (TextView) convertView.findViewById(R.id.home_list_username);
						TextView home_view_txt = (TextView) convertView.findViewById(R.id.home_view_txt_view);
						TextView itemName_txt = (TextView) convertView.findViewById(R.id.itemName_txt);
						
						Button close_btn =(Button) convertView.findViewById(R.id.close_home_btn);
						
						close_btn.setVisibility(View.INVISIBLE);
						home_username_txt.setText(list.get(position).getStore_name());
						Log.v("log"," Closet Item Name In Adater " + list.get(position).getStore_name());
						Log.v("log_tag","Closet Item  image :::: " +list.get(position).getImage());
						home_view_txt.setText("Closeted "+list.get(position).getCloseted_item_track());
						
						itemName_txt.setText(list.get(position).getName());
						  
						imageLoader.DisplayImage(list.get(position).getImage(), home_big_img);
						
						imageLoader.DisplayImage(list.get(position).getStore_image(), home_ic_img);
			return convertView;
		}
	}

	
}
