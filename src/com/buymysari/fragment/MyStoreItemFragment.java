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

import com.buymysari.Base64;
import com.buymysari.DBAdpter;
import com.buymysari.ImageLoader;
import com.buymysari.MyApplication;
import com.buymysari.R;
import com.buymysari.dto.Mystore_Item_dto;
import com.buymysari.dto.UserInfo_dto;

public class MyStoreItemFragment extends Fragment {

	ListView lv;
	ArrayList<Mystore_Item_dto> list = new ArrayList<Mystore_Item_dto>();
	MyListAdapter adt;
	View rootView;
	private ProgressDialog progress;
	ImageButton store_icon;
	TextView storeName;
	Button subscribe, store_subscr_btn;
	UserInfo_dto usdt;
	MyApplication app;
	Bundle bundle;
	String myInt;
	ImageLoader imageLoader;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.mystoreitemlist, container, false);
		imageLoader=new ImageLoader(getActivity().getApplicationContext());
		app = (MyApplication) getActivity().getApplicationContext();
		progress = new ProgressDialog(getActivity());
		progress.setMessage("Loading...");
		bundle = this.getArguments();
		myInt = bundle.getString("position");
		new JSONTask(progress).execute("Home");

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		lv = (ListView) rootView.findViewById(R.id.mystoreitem_listview);

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

			list = DBAdpter.getMyStoreItemData(myInt);

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
				adt = new MyListAdapter(getActivity());
				lv.setAdapter(adt);

			} else {

				Toast.makeText(getActivity().getApplicationContext(),
						"No Store Data Available", Toast.LENGTH_LONG).show();
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
			convertView = mInflater.inflate(R.layout.custommystoreitem, null);
			ImageButton store_item_img = (ImageButton) convertView
					.findViewById(R.id.mystoreitem_big_img);

			TextView store_closet_txt = (TextView) convertView
					.findViewById(R.id.mystoreitem_closet_txt_view);
			TextView store_item_name_txt = (TextView) convertView
					.findViewById(R.id.mystoreitemName_txt);

			Button store_item_close_btn = (Button) convertView
					.findViewById(R.id.mystoreitem_close_home_btn);

			store_closet_txt.setText("Closeted : "
					+ list.get(position).Closeted_item_track);
			store_item_name_txt.setText(list.get(position).name);

			/*if (list.get(position).image != null) {
				byte[] Image_getByte;
				try {
					Image_getByte = Base64.decode(list.get(position).image);
					ByteArrayInputStream bytes = new ByteArrayInputStream(Image_getByte);
					BitmapDrawable bmd = new BitmapDrawable(bytes);
					Bitmap bm = bmd.getBitmap();
					store_item_img.setImageBitmap(bm);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}*/
			
			imageLoader.DisplayImage(list.get(position).image, store_item_img);
			
			store_item_close_btn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					final String uid = app.getUserID();

					new ClosetTask(progress).execute(
							list.get(position).item_id, myInt, uid);
				}
			});
			return convertView;
		}
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
			String myInt = arg[1];
			String uid = arg[2];

			String msg = DBAdpter.userClosestStore(item_id, myInt, uid);

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


}
