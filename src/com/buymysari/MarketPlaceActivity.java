package com.buymysari;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.buymysari.dto.CateDto;
import com.buymysari.fragment.ClosetFragment;
import com.buymysari.fragment.CreateStoreFragment;
import com.buymysari.fragment.HomeFragment;
import com.buymysari.fragment.ProfileFragment;
import com.buymysari.fragment.SearchItemsFragment;
import com.buymysari.fragment.StoreProfileFragment;
import com.buymysari.fragment.StoreProfileGridFragment;
import com.buymysari.fragment.TakeCameraFragment;
import com.buymysari.layout.MainLayout;
import com.buymysari.R;

public class MarketPlaceActivity extends FragmentActivity {

	public static MainLayout mainLayout;
	Button btMenu;
	private ListView lvMenu, StoreMenu;
	private String[] lvMenuItems;
	private String[] StoreMenuItems;
	public static TextView tvTitle;
	String cityName;
	List<Button> buttons = new ArrayList<Button>();

	public static LinearLayout storeOptions;
	public static Button btnCreateStore;

	ArrayAdapter<String> adapter;
	ArrayList<CateDto> CategoryNames;
	EditText edtSearchText;
	String searchText;

	MyApplication app;
	int cateIdSelected = 0;

	Button btnCl, btnAcc, btnAllCategory;
	Button btnSh;
	Boolean clothStataus = false;

	CircularImageView imView;
	TextView txtUserName;
	String UserNAme;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_market_place);
		mainLayout = (MainLayout) this.getLayoutInflater().inflate(R.layout.activity_market_place, null);
		setContentView(mainLayout);

		app = (MyApplication) this.getApplicationContext();
		edtSearchText = (EditText) findViewById(R.id.edtSearchText);

		imView = (CircularImageView) findViewById(R.id.user_img);
		txtUserName = (TextView) findViewById(R.id.user_txt);

		UserNAme = DBAdpter.fetch_UserDetail_data.get(0).getFirst_name() + " "
				+ DBAdpter.fetch_UserDetail_data.get(0).getLast_name();
		txtUserName.setText(UserNAme);

		String img_url = DBAdpter.fetch_list_StoreProfile_data.get(0)
				.getStrore_prof_image().toString().trim();
		URL url = null;
		try {
			url = new URL(img_url);
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
		imView.setImageBitmap(bmp);
		imView.setBorderColor(getResources().getColor(R.color.GrayLight));
		imView.setBorderWidth(0);
		
		// CategoryNames = DBAdpter.fetchCategoryNames();

		btnCl = (Button) findViewById(R.id.btnCloth);
		btnSh = (Button) findViewById(R.id.btnShoes);
		btnAcc = (Button) findViewById(R.id.btnAcce);
		btnAllCategory = (Button) findViewById(R.id.btnAll);

		btnCl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.v("log", " cloth ");

				btnCl.setEnabled(false);
				btnAcc.setEnabled(true);
				btnSh.setEnabled(true);
				btnAllCategory.setEnabled(true);

				cateIdSelected = 1;

				FragmentManager fm = MarketPlaceActivity.this
						.getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fm.beginTransaction();
				SearchItemsFragment fm2 = new SearchItemsFragment();
				fragmentTransaction.replace(
						R.id.activity_main_content_fragment, fm2, "HELLO");
				Bundle bundle = new Bundle();
				bundle.putString("searchText", edtSearchText.getText()
						.toString());
				bundle.putString("searchCateId", cateIdSelected + "");
				fm2.setArguments(bundle);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();

				mainLayout.toggleMenu();
			}
		});

		btnSh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Log.v("log", " Shoes ");

				btnSh.setEnabled(false);
				btnCl.setEnabled(true);
				btnAcc.setEnabled(true);
				btnAllCategory.setEnabled(true);

				cateIdSelected = 4;
				FragmentManager fm = MarketPlaceActivity.this
						.getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fm.beginTransaction();
				SearchItemsFragment fm2 = new SearchItemsFragment();
				fragmentTransaction.replace(
						R.id.activity_main_content_fragment, fm2, "HELLO");
				Bundle bundle = new Bundle();
				bundle.putString("searchText", edtSearchText.getText()
						.toString());
				bundle.putString("searchCateId", cateIdSelected + "");
				fm2.setArguments(bundle);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();

				mainLayout.toggleMenu();
			}
		});

		btnAcc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Log.v("log", " Acce ");

				btnAcc.setEnabled(false);
				btnSh.setEnabled(true);
				btnCl.setEnabled(true);
				btnAllCategory.setEnabled(true);

				cateIdSelected = 5;

				FragmentManager fm = MarketPlaceActivity.this
						.getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fm.beginTransaction();
				SearchItemsFragment fm2 = new SearchItemsFragment();
				fragmentTransaction.replace(
						R.id.activity_main_content_fragment, fm2, "HELLO");
				Bundle bundle = new Bundle();
				bundle.putString("searchText", edtSearchText.getText()
						.toString());
				bundle.putString("searchCateId", cateIdSelected + "");
				fm2.setArguments(bundle);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();

				mainLayout.toggleMenu();
			}
		});

		btnAllCategory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Log.v("log", " All ");

				cateIdSelected = 0;

				btnAllCategory.setEnabled(false);
				btnAcc.setEnabled(true);
				btnSh.setEnabled(true);
				btnCl.setEnabled(true);

				FragmentManager fm = MarketPlaceActivity.this
						.getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fm.beginTransaction();
				SearchItemsFragment fm2 = new SearchItemsFragment();
				fragmentTransaction.replace(
						R.id.activity_main_content_fragment, fm2, "HELLO");
				Bundle bundle = new Bundle();
				bundle.putString("searchText", edtSearchText.getText()
						.toString());
				bundle.putString("searchCateId", cateIdSelected + "");
				fm2.setArguments(bundle);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();

				mainLayout.toggleMenu();

			}
		});

		/*
		 * tr = (TableRow)findViewById(R.id.tableRow1); for (int c=0; c <
		 * CategoryNames.size() ; c++) { Button b = new Button (this);
		 * b.setId(Integer.parseInt(CategoryNames.get(c).getCate_id()));
		 * b.setText(CategoryNames.get(c).getCategory_name());
		 * b.setTextSize(15.0f); b.setTextColor(Color.BLACK);
		 * b.setOnClickListener(this); buttons.add(b); tr.addView(b, 60,60); }
		 */

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

		tvTitle = (TextView) findViewById(R.id.activity_main_content_title);
		storeOptions = (LinearLayout) findViewById(R.id.StoreProfileOptions);

		Log.v("log", " Marketplace activty storeId --> " + app.getStoreId());

		btnCreateStore = (Button) findViewById(R.id.btnCrStore);

		Log.v("log", " Marketplace StoreID ====>  " + app.getStoreId());

		if (!app.getStoreId().equals("null")) {
			Log.v("log", " StoreId  if ");

			storeOptions.setVisibility(View.VISIBLE);
			btnCreateStore.setVisibility(View.GONE);
		} else {
			Log.v("log", " StoreId  else ");

			storeOptions.setVisibility(View.GONE);
			btnCreateStore.setVisibility(View.VISIBLE);
		}

		btnCreateStore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				FragmentManager fm = MarketPlaceActivity.this
						.getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fm.beginTransaction();
				CreateStoreFragment fm2 = new CreateStoreFragment();
				fragmentTransaction.replace(
						R.id.activity_main_content_fragment, fm2, "HELLO");
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();

				mainLayout.toggleMenu();
			}
		});

		FragmentManager fm = this.getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();

		HomeFragment fragment = new HomeFragment();
		ft.add(R.id.activity_main_content_fragment, fragment);
		ft.commit();

		edtSearchText.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub

				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					// Perform action on key press
					Toast.makeText(
							MarketPlaceActivity.this,
							" SearchText --> "
									+ edtSearchText.getText().toString(),
							Toast.LENGTH_SHORT).show();

					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(edtSearchText.getWindowToken(),
							0);

					tvTitle.setText("Search Reuslts View");

					FragmentManager fm = MarketPlaceActivity.this
							.getSupportFragmentManager();
					FragmentTransaction fragmentTransaction = fm
							.beginTransaction();
					SearchItemsFragment fm2 = new SearchItemsFragment();
					fragmentTransaction.replace(
							R.id.activity_main_content_fragment, fm2, "HELLO");
					Bundle bundle = new Bundle();
					bundle.putString("searchText", edtSearchText.getText()
							.toString());
					bundle.putString("searchCateId", cateIdSelected + "");
					fm2.setArguments(bundle);
					fragmentTransaction.addToBackStack(null);
					fragmentTransaction.commit();
					mainLayout.toggleMenu();
					return true;
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
			fragment = new TakeCameraFragment();
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

		if (selectedItem.compareTo("Home") == 0) {
			fragment = new HomeFragment();
		} else if (selectedItem.compareTo("My Profile") == 0) {
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

	/*
	 * @Override public void onBackPressed() { // TODO Auto-generated method
	 * stub android.os.Process.killProcess(android.os.Process.myPid()); }
	 */
}
