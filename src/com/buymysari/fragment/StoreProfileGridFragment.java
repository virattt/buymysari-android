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
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
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
import com.buymysari.MarketPlaceActivity;
import com.buymysari.MyApplication;
import com.buymysari.R;
import com.buymysari.dto.All_list_Store_dto;

public class StoreProfileGridFragment extends Fragment {

	ArrayList<All_list_Store_dto> gridlist = new ArrayList<All_list_Store_dto>();
	MyApplication app;
	CustomGridViewAdapter adtstore;
	View rootView;
	private ProgressDialog progress;
	GridView gridView;
	String st_id;
	ImageView imView;
	TextView store_profilegrid_name, store_url_txt,
			store_profile_grid_closet_txt, subscribe_store_profile_txt;
	ListView store_listview;
	public ImageLoader imageLoader;
	CircularImageView list_Store_profile_grid_logo_image;
	ToggleButton btnStoreProfileList, btnStoreProfileGrid;

	TextView txtStoreProfileGridAddress;

	MyListAdapter adt;
	int _listposition;
	Button btnEditStore;
	Typeface tf;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.store_profile_grid,
				container, false);
		
		 tf = Typeface.createFromAsset(getActivity().getAssets(),
			    "fonts/ITCAvantGardeStd-BkCn.otf");
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		
		app = (MyApplication) getActivity().getApplicationContext();
		st_id = app.getStoreId();
		imageLoader = new ImageLoader(getActivity().getApplicationContext());

		gridView = (GridView) rootView.findViewById(R.id.gridView1);
		store_listview = (ListView) rootView
				.findViewById(R.id.store_profile_listview);
		txtStoreProfileGridAddress = (TextView) rootView
				.findViewById(R.id.txtStoreProfileGridAddress);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			list_Store_profile_grid_logo_image = (CircularImageView) rootView
					.findViewById(R.id.list_Store_profile_grid_logo_image);
			list_Store_profile_grid_logo_image.setBorderColor(getResources()
					.getColor(R.color.GrayLight));
			list_Store_profile_grid_logo_image.setBorderWidth(0);

		} else {
			Log.v("log", " Below HoneyComb ");
			imView = (ImageView) rootView
					.findViewById(R.id.list_Store_profile_grid_logo_image);
		}

		btnEditStore = (Button) rootView.findViewById(R.id.btnEditStore);

		btnEditStore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				FragmentManager fm = getFragmentManager();
				FragmentTransaction fragmentTransaction = fm.beginTransaction();
				CreateStoreFragment fm2 = new CreateStoreFragment();
				fragmentTransaction.replace(
						R.id.rela_storeprofile_grid_fragment, fm2, "HELLO");
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();

			}
		});

		store_profilegrid_name = (TextView) rootView
				.findViewById(R.id.store_profilegrid_name);
		store_url_txt = (TextView) rootView.findViewById(R.id.store_url_txt);
		store_profile_grid_closet_txt = (TextView) rootView
				.findViewById(R.id.store_profile_grid_closet_txt);
		subscribe_store_profile_txt = (TextView) rootView
				.findViewById(R.id.subscribe_store_profile_txt);

		btnStoreProfileGrid = (ToggleButton) rootView
				.findViewById(R.id.btnStoreProfileGrid);
		btnStoreProfileList = (ToggleButton) rootView
				.findViewById(R.id.btnStoreProfileList);
		
		store_profilegrid_name.setTypeface(tf);
		store_url_txt.setTypeface(tf);
		store_profile_grid_closet_txt.setTypeface(tf);
		subscribe_store_profile_txt.setTypeface(tf);
		btnEditStore.setTypeface(tf);
		txtStoreProfileGridAddress.setTypeface(tf);
		
		btnStoreProfileGrid.setOnCheckedChangeListener(changeChecker);
		btnStoreProfileList.setOnCheckedChangeListener(changeChecker);
		/*
		 * btnStoreProfileList.setOnCheckedChangeListener(new
		 * CompoundButton.OnCheckedChangeListener() {
		 * 
		 * public void onCheckedChanged(CompoundButton buttonView, boolean
		 * isChecked) { // TODO Auto-generated method stub
		 * 
		 * if (isChecked) { store_listview.setVisibility(View.VISIBLE);
		 * gridView.setVisibility(View.INVISIBLE);
		 * buttonView.setBackgroundResource(R.drawable.unselected_list); } else
		 * { buttonView.setBackgroundResource(R.drawable.selected_list); } } });
		 * 
		 * btnStoreProfileGrid.setOnCheckedChangeListener(new
		 * CompoundButton.OnCheckedChangeListener() {
		 * 
		 * public void onCheckedChanged(CompoundButton buttonView, boolean
		 * isChecked) { // TODO Auto-generated method stub
		 * 
		 * if (isChecked) { store_listview.setVisibility(View.INVISIBLE);
		 * gridView.setVisibility(View.VISIBLE);
		 * buttonView.setBackgroundResource(R.drawable.unselected_grid); } else
		 * { buttonView.setBackgroundResource(R.drawable.selected_grid); } } });
		 */

		if (st_id.trim().equals("")) {

			gridView.setVisibility(View.INVISIBLE);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				list_Store_profile_grid_logo_image
						.setVisibility(View.INVISIBLE);
			}
			store_profilegrid_name.setVisibility(View.INVISIBLE);
			store_profilegrid_name.setVisibility(View.INVISIBLE);
			store_url_txt.setVisibility(View.INVISIBLE);
			store_profile_grid_closet_txt.setVisibility(View.INVISIBLE);
			subscribe_store_profile_txt.setVisibility(View.INVISIBLE);

			Toast.makeText(getActivity().getApplicationContext(),
					"No Store Detail Available", Toast.LENGTH_LONG).show();
		} else {
			gridView.setVisibility(View.VISIBLE);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				list_Store_profile_grid_logo_image.setVisibility(View.VISIBLE);
			}
			store_profilegrid_name.setVisibility(View.VISIBLE);
			store_profilegrid_name.setVisibility(View.VISIBLE);
			store_url_txt.setVisibility(View.VISIBLE);
			store_profile_grid_closet_txt.setVisibility(View.VISIBLE);
			subscribe_store_profile_txt.setVisibility(View.VISIBLE);

			progress = new ProgressDialog(getActivity());
			progress.setMessage("Loading...");
			new JSONTask(progress).execute("Home");

		}

		store_listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		store_listview
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int position, long arg3) {
						// TODO Auto-generated method stub

						Log.v("log" ," position list" + position);
						_listposition = position;

						return false;
					}
				});

		gridView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub

				Log.v("log" ," position grid " + position);
				
				_listposition = position;

				return false;
			}
		});

		registerForContextMenu(gridView);
		// registerForContextMenu(store_listview);

		return rootView;
	}

	OnCheckedChangeListener changeChecker = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked) {

				Log.v("log_tag", "buttonView ::: " + buttonView);
				if (buttonView == btnStoreProfileGrid) {
					btnStoreProfileList.setChecked(false);
					store_listview.setVisibility(View.INVISIBLE);
					gridView.setVisibility(View.VISIBLE);
					btnStoreProfileGrid
							.setBackgroundResource(R.drawable.selected_grid);
					btnStoreProfileList
							.setBackgroundResource(R.drawable.unselected_list);

				}
				if (buttonView == btnStoreProfileList) {
					btnStoreProfileGrid.setChecked(false);
					store_listview.setVisibility(View.VISIBLE);
					gridView.setVisibility(View.INVISIBLE);
					btnStoreProfileGrid
							.setBackgroundResource(R.drawable.unselected_grid);
					btnStoreProfileList
							.setBackgroundResource(R.drawable.selected_list);

				}

			}
		}
	};

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.action, menu);

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.delete: {

			new DeletDataTask(progress).execute("Home");
			break;
		}
		case R.id.share : {
			
			   // Intent sharingIntent = new Intent(Intent.ACTION_SEND);
			    Log.v("log", " path --> " + gridlist.get(_listposition).image);
			    
			    String imgPath = gridlist.get(_listposition).image;
			   // File imageFile = new File(imgPath);
			    Bitmap bmp = getBitmapFromUrl(imgPath);
			    
			    Intent share = new Intent(Intent.ACTION_SEND);
			    share.setType("image/jpeg");
			    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			    bmp.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
			    
			 /*   try{
		        	File pictureFile = new File(getActivity().getApplicationContext().getFilesDir(),"/prvteyes.jpeg");
		        	 try {
		        		 	pictureFile.createNewFile();
					        FileOutputStream fo = new FileOutputStream(pictureFile);
					        fo.write(bytes.toByteArray());
					    } catch (IOException e1) {                       
					            e1.printStackTrace();
					    }
		            String imPath = getActivity().getApplicationContext().getFilesDir()+"/prvteyes.jpeg";
		            Log.v("log", "sdcard mounted readonly " + imPath);
		            
		            share.putExtra(Intent.EXTRA_STREAM, Uri.parse(imPath));
		        }catch(Exception e){
		        	Log.v("log","Error " + e.toString());
		       */ 
		        	  File f = new File(Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpeg");
					    try {
					        f.createNewFile();
					        FileOutputStream fo = new FileOutputStream(f);
					        fo.write(bytes.toByteArray());
					    } catch (IOException e1) {                       
					            e1.printStackTrace();
					    }
					    share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/temporary_file.jpeg"));
			    // }
			  
			    startActivity(Intent.createChooser(share, "Share Image"));
			   /* sharingIntent.setType("image/jpeg");
		        Uri screenshotUri = Uri.fromFile(imageFile);
		        try {
		            InputStream stream = getActivity().getContentResolver().openInputStream(screenshotUri);
		        } catch (FileNotFoundException e) {
		            // TODO Auto-generated catch block
		            e.printStackTrace();
		        }
		        
		        sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
		        startActivity(Intent.createChooser(sharingIntent, "Share image"));*/
			    break;
			}
		}
		return super.onContextItemSelected(item);
	}

	class JSONTask extends AsyncTask<String, Void, String> {

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

			gridlist = DBAdpter.getStoreData(st_id);

			listSize = gridlist.size() + "";
			return listSize; // This value will be returned to your
								// onPostExecute(result) method
		}

		@Override
		protected void onPostExecute(String result) {
			// Create here your JSONObject...
			Log.v("log_tag", "list ON Post" + result);

			if (Integer.parseInt(result) > 0) {
				adtstore = new CustomGridViewAdapter(getActivity()
						.getApplicationContext());
				gridView.setAdapter(adtstore);

				adt = new MyListAdapter(getActivity().getApplicationContext());
				store_listview.setAdapter(adt);

				store_profilegrid_name.setText(gridlist.get(0).store_name);
				store_url_txt.setText(gridlist.get(0).website);
				store_profile_grid_closet_txt
						.setText(gridlist.get(0).closeted_item_count);
				subscribe_store_profile_txt
						.setText(gridlist.get(0).subscribed_store_count);
				txtStoreProfileGridAddress.setText(gridlist.get(0).address);

				Log.v("log_tag",
						"gridlist.get(0).store_image :: "
								+ gridlist.get(0).store_image);

				String img_url = gridlist.get(0).store_image.toString().trim();
				imageLoader = new ImageLoader(getActivity());

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					Log.v("log", " Above HoneyComb ");

					Bitmap b = getBitmapFromUrl(img_url);
					MarketPlaceActivity.imView.setImageBitmap(b);
					MarketPlaceActivity.imView.setBorderColor(getResources().getColor(R.color.GrayLight));
					MarketPlaceActivity.imView.setBorderWidth(0);
					list_Store_profile_grid_logo_image.setImageBitmap(b);
				} else {
					Log.v("log", " Below HoneyComb ");
					Bitmap bmp = getBitmapFromUrl(img_url);
					MarketPlaceActivity.imView1.setImageBitmap(bmp);
					imView.setImageBitmap(bmp);
				}

			} else {

				Toast.makeText(getActivity().getApplicationContext(),
						" No Stroe Items Available ", Toast.LENGTH_SHORT)
						.show();
			}

			progress.dismiss();
		}
	}

	class CustomGridViewAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public CustomGridViewAdapter(Context context) {
			mInflater = LayoutInflater.from(context);

		}

		public int getCount() {
			return gridlist.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			convertView = mInflater.inflate(R.layout.custom_grid_store, null);
			ImageView store_Name_img = (ImageView) convertView
					.findViewById(R.id.item_image);

			if (gridlist.get(position).image != "") {

				imageLoader.DisplayImage(gridlist.get(position).image,
						store_Name_img);

			} else {

				/*
				 * Toast.makeText(getActivity(), "No Item Available",
				 * Toast.LENGTH_SHORT).show();
				 */
			}

			return convertView;
		}
	}

	class MyListAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public MyListAdapter(Context context) {
			mInflater = LayoutInflater.from(context);

		}

		public int getCount() {
			return gridlist.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			convertView = mInflater.inflate(R.layout.custom_store_detail_list,
					null);
			final ImageButton store_item_img = (ImageButton) convertView
					.findViewById(R.id.store_big_img);

			TextView store_closet_txt = (TextView) convertView
					.findViewById(R.id.store_closet_txt_view);
			TextView store_item_name_txt = (TextView) convertView
					.findViewById(R.id.itemName_txt);
			ImageView items_view = (ImageView) convertView
					.findViewById(R.id.items_view);
			Button store_item_close_btn = (Button) convertView
					.findViewById(R.id.close_home_btn);

			// store_closet_txt.setText(gridlist.get(position).closeted_item_count);
			store_item_name_txt.setText(gridlist.get(position).name);
			final String uid = app.getUserID();

			/*
			 * if (list.get(position).image != null) { byte[] Image_getByte; try
			 * { Image_getByte = Base64.decode(list.get(position).image);
			 * ByteArrayInputStream bytes = new ByteArrayInputStream(
			 * Image_getByte); BitmapDrawable bmd = new BitmapDrawable(bytes);
			 * Bitmap bm = bmd.getBitmap(); store_item_img.setImageBitmap(bm);
			 * 
			 * } catch (IOException e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); } }
			 */

			imageLoader.DisplayImage(gridlist.get(position).image,
					store_item_img);
			store_item_close_btn.setVisibility(View.INVISIBLE);
			items_view.setVisibility(View.INVISIBLE);

			/*
			 * store_item_close_btn.setOnClickListener(new
			 * View.OnClickListener() {
			 * 
			 * @Override public void onClick(View v) { // TODO Auto-generated
			 * method stub
			 * 
			 * new ClosetTask(progress).execute( gridlist.get(position).item_id,
			 * gridlist.get(position).store_id, uid); } });
			 */

			
			store_closet_txt.setTypeface(tf);
			store_item_name_txt.setTypeface(tf);
			store_item_close_btn.setTypeface(tf);
			
			
			store_item_img.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					registerForContextMenu(store_item_img);
				}
			});

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
	
	
	public class DeletDataTask extends AsyncTask<String, Void, String> {

		public DeletDataTask(ProgressDialog progress) {
			progress = progress;
		}

		public void onPreExecute() {
			progress.show();

		}

		@Override
		protected String doInBackground(String... arg) {

			String msg = DBAdpter.DeleteItem(
					gridlist.get(_listposition).item_id + "", app.getStoreId());

			return msg;
		}

		@Override
		protected void onPostExecute(String result) {
			// Create here your JSONObject...
			gridlist.remove(_listposition);
			adt.notifyDataSetChanged();
			adtstore.notifyDataSetChanged();
			progress.dismiss();
			Toast.makeText(getActivity().getApplicationContext(),
					" DELETED ITEM ", Toast.LENGTH_LONG).show();

		}

	}

}
