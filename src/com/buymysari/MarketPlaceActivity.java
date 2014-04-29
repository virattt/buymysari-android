package com.buymysari;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.buymysari.dto.CateDto;
import com.buymysari.fragment.ClosetFragment;
import com.buymysari.fragment.CreateStoreFragment;
import com.buymysari.fragment.HomeFragment;
import com.buymysari.fragment.ProfileFragment;
import com.buymysari.fragment.SearchItemsFragment;
import com.buymysari.fragment.SetPictureImageFragment;
import com.buymysari.fragment.StoreProfileFragment;
import com.buymysari.fragment.StoreProfileGridFragment;
import com.buymysari.layout.MainLayout;

public class MarketPlaceActivity extends FragmentActivity implements
		OnTouchListener {

	public static MainLayout mainLayout;
	Button btMenu, bottom_home_btn_toolbar, bottom_closet_main_toolbat_btn,
			bottom_favourite_btn;
	private ListView lvMenu, StoreMenu;
	private String[] lvMenuItems;
	private String[] StoreMenuItems;

	public static CircularImageView imView;
	public static ImageView imView1;
	public static TextView tvTitle;
	String cityName;
	GPSTracker gps;
	List<Button> buttons = new ArrayList<Button>();
	ImageLoader imageLoader;
	public static LinearLayout storeOptions, personOptions;

	ArrayAdapter<String> adapter;
	ArrayList<CateDto> CategoryNames;
	EditText edtSearchText;
	String searchText;

	private MyApplication app;
	String cateIdSelected = "";

	Boolean clothStataus = false;

	TextView txtUserName , logout_txt;
	String UserNAme;
	ToggleButton btnCl, btnSh, btnAcc;
	float downXValue, downYValue;
	ImageView search_img;
	
	public static FrameLayout activityMain_content_fragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_market_place);
		mainLayout = (MainLayout) this.getLayoutInflater().inflate(
				R.layout.activity_market_place, null);
		setContentView(mainLayout);
		
		Typeface tf = Typeface.createFromAsset(this.getAssets(), "fonts/ITCAvantGardeStd-BkCn.otf");
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		TextView store_txt =(TextView)findViewById(R.id.store_txt);
		TextView person_txt =(TextView)findViewById(R.id.person_txt);
		store_txt.setTypeface(tf);
		person_txt.setTypeface(tf);
		
		activityMain_content_fragment = (FrameLayout)findViewById(R.id.activity_main_content_fragment);
		app = (MyApplication)this.getApplication();
		
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		logout_txt = (TextView)findViewById(R.id.logout_txt);
		logout_txt.setTypeface(tf);
		
		logout_txt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				 savePreferences("user_id" , "");
				 savePreferences("store_id" , "");
				 app.setStoreId("");
				 app.setUserID("");
				 	
				 Intent i = new Intent(MarketPlaceActivity.this, SplashActivity.class);
				 MarketPlaceActivity.this.finish();
				 startActivity(i);
				 
			}
		});
		
		edtSearchText = (EditText) findViewById(R.id.edtSearchText);
		bottom_home_btn_toolbar = (Button) findViewById(R.id.bottom_home_btn_toolbar);
		search_img = (ImageView) findViewById(R.id.search_img);
		bottom_closet_main_toolbat_btn = (Button) findViewById(R.id.bottom_closet_main_toolbat_btn);
		bottom_favourite_btn = (Button) findViewById(R.id.bottom_favourite_btn);

		txtUserName = (TextView) findViewById(R.id.user_txt);
		
		Log.v("log", " UserID " + app.getUserID());
		Log.v("log", " StoreID " + app.getStoreId());

		String userID = sharedPreferences.getString("user_id","");
		String store_id = sharedPreferences.getString("store_id", "");
		
		Log.v("log", " UserID "+ userID +" store_id --> " + store_id);

		if (!userID.equals("")) {

			UserNAme = DBAdpter.fetch_UserDetail_data.get(0).getFirst_name()
					+ " "
					+ DBAdpter.fetch_UserDetail_data.get(0).getLast_name();

		} else {

			Log.v("log"," Store -->  " + DBAdpter.fetch_UserDetail_data.get(0).getStrore_profile_firstName());
				
			UserNAme = DBAdpter.fetch_UserDetail_data.get(0).getStrore_profile_firstName()
					+ " "
					+ DBAdpter.fetch_UserDetail_data.get(0).getStrore_profile_lastName();
		}

		txtUserName.setText(UserNAme);
		
		
		String img_url = DBAdpter.fetch_UserDetail_data.get(0).getStrore_profile_image().toString().trim();
		Log.v("log"," marketplace img_url " + img_url );
		
		imageLoader = new ImageLoader(MarketPlaceActivity.this);
		Bitmap bm = getBitmapFromUrl(img_url);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			Log.v("log", " Above HoneyComb ");

			imView = (CircularImageView) findViewById(R.id.user_img);
		
			imView.setImageBitmap(bm);
			//imageLoader.DisplayImage(img_url, imView);
			imView.setBorderColor(getResources().getColor(R.color.GrayLight));
			imView.setBorderWidth(0);
		} else {
			Log.v("log", " Below HoneyComb ");

			imView1 = (ImageView) findViewById(R.id.user_img);
			imView1.setImageBitmap(bm);
			//imageLoader.DisplayImage(img_url, imView1);
		}
		
		search_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mainLayout.checkBeforeToggleMenu("OPEN");
				edtSearchText.setFocusable(true);
			}
		});

		gps = new GPSTracker(MarketPlaceActivity.this);

		// check if GPS enabled
		if (gps.canGetLocation()) {

			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();

			Geocoder geocoder = new Geocoder(MarketPlaceActivity.this,
					Locale.ENGLISH);
			List<Address> addresses;
			try {

				addresses = geocoder.getFromLocation(latitude, longitude, 1);
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

		bottom_home_btn_toolbar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FragmentManager fm = MarketPlaceActivity.this
						.getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fm.beginTransaction();
				HomeFragment fm2 = new HomeFragment();
				fragmentTransaction.replace(
						R.id.activity_main_content_fragment, fm2, "HELLO");

				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();
				
				// mainLayout.toggleMenu();
			}
		});

		bottom_closet_main_toolbat_btn
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						FragmentManager fm = MarketPlaceActivity.this
								.getSupportFragmentManager();
						FragmentTransaction fragmentTransaction = fm
								.beginTransaction();
						ClosetFragment fm2 = new ClosetFragment();
						fragmentTransaction.replace(
								R.id.activity_main_content_fragment, fm2,
								"HELLO");

						fragmentTransaction.addToBackStack(null);
						fragmentTransaction.commit();
						// mainLayout.toggleMenu();
					}
				});

		bottom_favourite_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FragmentManager fm = MarketPlaceActivity.this
						.getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fm.beginTransaction();
				StoreProfileFragment fm2 = new StoreProfileFragment();
				fragmentTransaction.replace(
						R.id.activity_main_content_fragment, fm2, "HELLO");

				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();
				// mainLayout.toggleMenu();
			}
		});

		// CategoryNames = DBAdpter.fetchCategoryNames();

		btnCl = (ToggleButton) findViewById(R.id.btnCloth);
		btnSh = (ToggleButton) findViewById(R.id.btnShoes);
		btnAcc = (ToggleButton) findViewById(R.id.btnAcce);

		btnCl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub

				if (isChecked) {
					buttonView.setBackgroundResource(R.drawable.clothes);
					cateIdSelected = cateIdSelected + "1";
				} else {
					buttonView.setBackgroundResource(R.drawable.clothes_active);
				}
				if (btnSh.isChecked()) {
					cateIdSelected = cateIdSelected + ",4";
				}
				if (btnAcc.isChecked()) {
					cateIdSelected = cateIdSelected + ",5";
				}

				FragmentManager fm = MarketPlaceActivity.this
						.getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fm.beginTransaction();
				SearchItemsFragment fm2 = new SearchItemsFragment();
				fragmentTransaction.replace(
						R.id.activity_main_content_fragment, fm2, "HELLO");
				Bundle bundle = new Bundle();
				if (!edtSearchText.getText().toString().equals("")) {
					bundle.putString("searchText", edtSearchText.getText()
							.toString());
				} else {
					bundle.putString("searchText", cityName);
				}
				bundle.putString("searchCateId", cateIdSelected);
				fm2.setArguments(bundle);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();

				mainLayout.toggleMenu();

			}
		});

		btnSh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					buttonView.setBackgroundResource(R.drawable.shoes);
					cateIdSelected = cateIdSelected + ",4";
				} else {
					buttonView.setBackgroundResource(R.drawable.shoes_active);
				}
				if (btnCl.isChecked()) {
					cateIdSelected = cateIdSelected + ",1";
				}
				if (btnAcc.isChecked()) {
					cateIdSelected = cateIdSelected + ",5";
				}

				FragmentManager fm = MarketPlaceActivity.this
						.getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fm.beginTransaction();
				SearchItemsFragment fm2 = new SearchItemsFragment();
				fragmentTransaction.replace(
						R.id.activity_main_content_fragment, fm2, "HELLO");
				Bundle bundle = new Bundle();
				if (!edtSearchText.getText().toString().equals("")) {
					bundle.putString("searchText", edtSearchText.getText()
							.toString());
				} else {
					bundle.putString("searchText", cityName);
				}
				bundle.putString("searchCateId", cateIdSelected);
				fm2.setArguments(bundle);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();

				mainLayout.toggleMenu();
			}
		});

		btnAcc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					buttonView.setBackgroundResource(R.drawable.accessories);
					cateIdSelected = cateIdSelected + ",5";
				} else {
					buttonView
							.setBackgroundResource(R.drawable.accessories_active);
				}

				if (btnCl.isChecked()) {
					cateIdSelected = cateIdSelected + ",1";
				}
				if (btnSh.isChecked()) {
					cateIdSelected = cateIdSelected + ",4";
				}

				FragmentManager fm = MarketPlaceActivity.this
						.getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fm.beginTransaction();
				SearchItemsFragment fm2 = new SearchItemsFragment();
				fragmentTransaction.replace(
						R.id.activity_main_content_fragment, fm2, "HELLO");
				Bundle bundle = new Bundle();
				if (!edtSearchText.getText().toString().equals("")) {
					bundle.putString("searchText", edtSearchText.getText()
							.toString());
				} else {
					bundle.putString("searchText", cityName);
				}
				bundle.putString("searchCateId", cateIdSelected);
				fm2.setArguments(bundle);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();

				mainLayout.toggleMenu();

			}
		});

		lvMenuItems = getResources().getStringArray(R.array.menu_items);
		lvMenu = (ListView) findViewById(R.id.activity_main_menu_listview);
		lvMenu.setAdapter(new ArrayAdapter<String>(this,
				R.layout.custom_textview, lvMenuItems));
		lvMenu.setDivider(null);
		lvMenu.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				onMenuItemClick(parent, view, position, id);
			}
		});

		StoreMenuItems = getResources().getStringArray(R.array.store_items);
		StoreMenu = (ListView) findViewById(R.id.activity_store_menu_listview);
		StoreMenu.setAdapter(new ArrayAdapter<String>(this,
				R.layout.custom_textview, StoreMenuItems));
		StoreMenu.setDivider(null);
		StoreMenu.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				onStoreMenuItemClick(parent, view, position, id);
			}
		});

		/*
		 * CreateMenuItems =
		 * getResources().getStringArray(R.array.create_store_items); CreateMenu
		 * = (ListView) findViewById(R.id.create_activity_store_menu_listview);
		 * CreateMenu.setAdapter(new ArrayAdapter<String>(this,
		 * R.layout.custom_textview, CreateMenuItems));
		 * CreateMenu.setDivider(null); CreateMenu.setOnItemClickListener(new
		 * OnItemClickListener() {
		 * 
		 * @Override public void onItemClick(AdapterView<?> parent, View view,
		 * int position, long id) { onCreateStoreMenuItemClick(parent, view,
		 * position, id); } });
		 */

		tvTitle = (TextView) findViewById(R.id.activity_main_content_title);
		storeOptions = (LinearLayout) findViewById(R.id.StoreProfileOptions);
		personOptions = (LinearLayout) findViewById(R.id.personal_linear);

		Log.v("log", " Marketplace activty storeId --> " + app.getStoreId());

		if (!store_id.equals("") && userID.equals("")) {
			Log.v("log", " StoreId  if ");

			storeOptions.setVisibility(View.VISIBLE);
			personOptions.setVisibility(View.GONE);
			bottom_closet_main_toolbat_btn.setVisibility(View.GONE);
			bottom_favourite_btn.setVisibility(View.GONE);

		} else {
			Log.v("log", " StoreId  else ");

			storeOptions.setVisibility(View.GONE);
			personOptions.setVisibility(View.VISIBLE);
		}

		FragmentManager fm = this.getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();

		HomeFragment fragment = new HomeFragment();
		ft.add(R.id.activity_main_content_fragment, fragment);
		ft.addToBackStack(null);
		ft.commit();
		
		Intent intent = getIntent();
		if (intent.hasExtra("data")) {
			Log.v("log"," if intent " + intent.getByteArrayExtra("data"));
			byte[] data = intent.getByteArrayExtra("data");
			FragmentManager fManager = MarketPlaceActivity.this
					.getSupportFragmentManager();
			android.support.v4.app.FragmentTransaction fTransaction = fManager
					.beginTransaction();
			SetPictureImageFragment setPictureFragment = new SetPictureImageFragment();
			fTransaction.replace(R.id.activity_main_content_fragment,
					setPictureFragment);
			fTransaction.addToBackStack(null);
			Bundle bundle = new Bundle();
			if(intent.hasExtra("image_from"))
			{
				bundle.putString("image_from", intent.getStringExtra("image_from"));	
			}
			bundle.putByteArray("data", data);
			setPictureFragment.setArguments(bundle);
			fTransaction.commit();
		}
		
		edtSearchText.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub

				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(edtSearchText.getWindowToken(),
							0);

					tvTitle.setText("Search Reuslts View");

					if (edtSearchText.getText().toString().equals("")) {

						Log.v("log_tag", "cityName" + cityName);
						Log.v("log_tag", "cityName else :: cateid "
								+ cateIdSelected);

						FragmentManager fm = MarketPlaceActivity.this
								.getSupportFragmentManager();
						FragmentTransaction fragmentTransaction = fm
								.beginTransaction();
						SearchItemsFragment fm2 = new SearchItemsFragment();
						fragmentTransaction.replace(
								R.id.activity_main_content_fragment, fm2,
								"HELLO");
						Bundle bundle = new Bundle();
						bundle.putString("searchText", cityName);
						bundle.putString("searchCateId", cateIdSelected);
						fm2.setArguments(bundle);
						fragmentTransaction.addToBackStack(null);
						fragmentTransaction.commit();
						mainLayout.toggleMenu();
						return true;

					} else {
						Log.v("lig_tag", "cityName else ::"
								+ edtSearchText.getText().toString());

						FragmentManager fm = MarketPlaceActivity.this
								.getSupportFragmentManager();
						FragmentTransaction fragmentTransaction = fm
								.beginTransaction();
						SearchItemsFragment fm2 = new SearchItemsFragment();
						fragmentTransaction.replace(
								R.id.activity_main_content_fragment, fm2,
								"HELLO");
						Bundle bundle = new Bundle();
						bundle.putString("searchText", edtSearchText.getText()
								.toString());
						bundle.putString("searchCateId", cateIdSelected);
						fm2.setArguments(bundle);
						fragmentTransaction.addToBackStack(null);
						fragmentTransaction.commit();
						mainLayout.toggleMenu();
						return true;

					}

				}
				return false;
			}
		});

		// activity_main_content_button_menu
		btMenu = (Button) findViewById(R.id.activity_main_content_button_menu);
		btMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Show/hide the menu
				toggleMenu(v);
			}
		});

		
	}

	public void toggleMenu(View v) {
		mainLayout.toggleMenu();
	}

	private void onStoreMenuItemClick(AdapterView<?> parent, View view,
			int position, long id) {
		String selectedItem = StoreMenuItems[position];

		Log.v("log", " onMenuItemClick Selected Item ===> " + selectedItem);

		String currentItem = tvTitle.getText().toString();

		// Do nothing if selectedItem is currentItem
		if (selectedItem.compareTo(currentItem) == 0) {
			mainLayout.toggleMenu();
			return;
		}

		FragmentManager fm = MarketPlaceActivity.this
				.getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
		Fragment fragment = null;

		
		if (selectedItem.compareTo("Store Profile") == 0) {
			fragment = new CreateStoreFragment();
		} else if (selectedItem.compareTo("Store Gallery") == 0) {
			fragment = new StoreProfileGridFragment();
		} else if (selectedItem.compareTo("Add Picture") == 0) {
			Intent cameraAct = new Intent(MarketPlaceActivity.this,CameraActivity.class);
			cameraAct.putExtra("ImageType", "AddPicture");
			startActivity(cameraAct);	
		} else {
			fragment = new HomeFragment();
		}

		if (fragment != null) {
			// Replace current fragment by this new one
			ft.replace(R.id.activity_main_content_fragment, fragment);
			ft.commit();

			// Set title accordingly
			tvTitle.setText(selectedItem);
		}

		// Hide menu anyway
		mainLayout.toggleMenu();
	}

	private void onMenuItemClick(AdapterView<?> parent, View view,
			int position, long id) {
		String selectedItem = lvMenuItems[position];

		Log.v("log", " onMenuItemClick Selected Item ===> " + selectedItem);

		String currentItem = tvTitle.getText().toString();

		// Do nothing if selectedItem is currentItem
		if (selectedItem.compareTo(currentItem) == 0) {
			mainLayout.toggleMenu();
			return;
		}

		FragmentManager fm = MarketPlaceActivity.this
				.getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
		Fragment fragment = null;

		if (selectedItem.compareTo("My Profile") == 0) {
			fragment = new ProfileFragment();
		} else if (selectedItem.compareTo("My Stores") == 0) {
			fragment = new StoreProfileFragment();
		} else if (selectedItem.compareTo("My Closet") == 0) {
			fragment = new ClosetFragment();
		} else {
			fragment = new HomeFragment();
		}

		if (fragment != null) {
			// Replace current fragment by this new one
			ft.replace(R.id.activity_main_content_fragment, fragment);
			ft.commit();

			// Set title accordingly
			tvTitle.setText(selectedItem);
		}

		// Hide menu anyway
		mainLayout.toggleMenu();
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			// do something
			Log.v("log", "motion detected");
		}
		return true;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		super.dispatchTouchEvent(ev);

		switch (ev.getAction()) {

		case MotionEvent.ACTION_DOWN: {
			// store the X value when the user's finger was pressed down
			downXValue = ev.getX();
			downYValue = ev.getY();
			Log.v("", "= " + downYValue);
			break;
		}

		case MotionEvent.ACTION_UP: {
			// Get the X value when the user released his/her finger
			float currentX = ev.getX();
			float currentY = ev.getY();
			// check if horizontal or vertical movement was bigger

			if (Math.abs(downXValue - currentX) > Math.abs(downYValue
					- currentY)) {
				Log.v("", "x");
				// going backwards: pushing stuff to the right

				Log.v("log", " downXvalue - currentX "
						+ (downXValue - currentX));

				if (downXValue < currentX) {
					Log.v("", "right");
					if (-150 > (downXValue - currentX)) {
						Log.v("log", "right if");
						mainLayout.checkBeforeToggleMenu("OPEN");
					} else {
						Log.v("log", "right else");
					}
				}

				// going forwards: pushing stuff to the left
				if (downXValue > currentX) {
					mainLayout.checkBeforeToggleMenu("CLOSE");

				}

			} else {
				Log.v("", "y ");

				if (downYValue < currentY) {
					Log.v("", "down");

				}
				if (downYValue > currentY) {
					Log.v("", "up");

				}
			}
			break;
		}

		}
		return true;
	}

	
	private void savePreferences(String key, String value) {
	       SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
	       Editor editor = sharedPreferences.edit();
	       editor.putString(key, value);
	       editor.commit();
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
			bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bmp;
	}
	
	/*
	 * @Override public void onBackPressed() { // TODO Auto-generated method
	 * 	stub android.os.Process.killProcess(android.os.Process.myPid()); }
	 */
}
