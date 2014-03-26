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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.buymysari.Base64;
import com.buymysari.DBAdpter;
import com.buymysari.ImageLoader;
import com.buymysari.MyApplication;
import com.buymysari.R;
import com.buymysari.dto.MyStore_list_dto;

public class StoreProfileFragment extends Fragment {
	ListView lv;
	ArrayList<MyStore_list_dto> list = new ArrayList<MyStore_list_dto>();
	MyApplication app;
	MyListAdapter adtstore;
	View rootView;
	private ProgressDialog progress;
	
	ImageLoader imageLoader;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_store_profile,
				container, false);
		app = (MyApplication) getActivity().getApplicationContext();
		lv = (ListView) rootView.findViewById(R.id.myStore_listview);
		imageLoader=new ImageLoader(getActivity().getApplicationContext());
		
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

			list = DBAdpter.getMyStoreData(app.getUserID());

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
			convertView = mInflater.inflate(R.layout.custome_mystorelist, null);
			ImageButton store_Name_img = (ImageButton) convertView
					.findViewById(R.id.my_Store_logo_image);

			TextView store_Name_txt = (TextView) convertView
					.findViewById(R.id.mystore_list_name);

			store_Name_txt.setText(list.get(position).name);

			/*if (list.get(position).image != null) {
				byte[] Image_getByte;
				try {
					Image_getByte = Base64.decode(list.get(position).image);
					ByteArrayInputStream bytes = new ByteArrayInputStream(
							Image_getByte);
					BitmapDrawable bmd = new BitmapDrawable(bytes);
					Bitmap bm = bmd.getBitmap();
					store_Name_img.setImageBitmap(bm);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}*/
			
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
