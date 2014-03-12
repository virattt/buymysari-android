package com.marketplacestore;

import com.marketplacestore.fragment.ClosetFragment;
import com.marketplacestore.fragment.CreateStoreFragment;
import com.marketplacestore.fragment.HomeFragment;
import com.marketplacestore.fragment.ProfileFragment;
import com.marketplacestore.layout.MainLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


public class MarketPlaceActivity extends FragmentActivity{
	
	MainLayout mainLayout;
	Button btMenu;	
	private ListView lvMenu , StoreMenu;
	private String[] lvMenuItems;
	private String[] StoreMenuItems;
	TextView tvTitle;
	String cityName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	//	setContentView(R.layout.activity_market_place);
		mainLayout = (MainLayout)this.getLayoutInflater().inflate(R.layout.activity_market_place, null);
        setContentView(mainLayout);
		
        cityName = getIntent().getExtras().getString("cityName").toString();
        DBAdpter.getNewsData(cityName);
        
        Log.v("log", " cityName in MarketActivity " + cityName);
        
        lvMenuItems = getResources().getStringArray(R.array.menu_items);
        lvMenu = (ListView) findViewById(R.id.activity_main_menu_listview);
        lvMenu.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, lvMenuItems));
        lvMenu.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	onMenuItemClick(parent, view, position, id);
            }
        });
        
        StoreMenuItems = getResources().getStringArray(R.array.store_items); 
        StoreMenu = (ListView) findViewById(R.id.activity_store_menu_listview);
        StoreMenu.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, StoreMenuItems));
        StoreMenu.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	onStoreMenuItemClick(parent, view, position, id);
            }
        });
        
        tvTitle = (TextView) findViewById(R.id.activity_main_content_title);
        
		FragmentManager fm = this.getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        
        HomeFragment fragment = new HomeFragment();
        ft.add(R.id.activity_main_content_fragment, fragment);
        ft.commit();
		
	//	activity_main_content_button_menu
		 btMenu = (Button) findViewById(R.id.activity_main_content_button_menu);
	        btMenu.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                // Show/hide the menu
	                toggleMenu(v);
	            }
	        });
		}
	
	public void toggleMenu(View v){
        mainLayout.toggleMenu();
    }
	
	private void onStoreMenuItemClick(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = StoreMenuItems[position];
        
        Log.v("log"," onMenuItemClick Selected Item ===> " + selectedItem);
        
        String currentItem = tvTitle.getText().toString();
        
        // Do nothing if selectedItem is currentItem
        if(selectedItem.compareTo(currentItem) == 0) {
            mainLayout.toggleMenu();
            return;
        }
        
        FragmentManager fm = MarketPlaceActivity.this.getSupportFragmentManager();
	    android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment = null;
        
        if(selectedItem.compareTo("Store Profile") == 0){
        	fragment = new HomeFragment();
        }
        else if(selectedItem.compareTo("Store Gallery") == 0){
        	
        }
        else if(selectedItem.compareTo("Add a Picture") == 0){
        	
        }
        else {
        	fragment = new HomeFragment();
        }
        
        if(fragment != null) {
            // Replace current fragment by this new one
            ft.replace(R.id.activity_main_content_fragment, fragment);
            ft.commit();
            
            // Set title accordingly
            tvTitle.setText(selectedItem);
        }
        
        // Hide menu anyway
        mainLayout.toggleMenu();
	}
	
	private void onMenuItemClick(AdapterView<?> parent, View view, int position, long id) {
	        String selectedItem = lvMenuItems[position];
	        
	        Log.v("log"," onMenuItemClick Selected Item ===> " + selectedItem);
	        
	        String currentItem = tvTitle.getText().toString();
	        
	        // Do nothing if selectedItem is currentItem
	        if(selectedItem.compareTo(currentItem) == 0) {
	            mainLayout.toggleMenu();
	            return;
	        }
	        
	        FragmentManager fm = MarketPlaceActivity.this.getSupportFragmentManager();
		    android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
	        Fragment fragment = null;
	        
	        if(selectedItem.compareTo("My Profile") == 0) {
	            fragment = new ProfileFragment();
	        } 
	        else if(selectedItem.compareTo("My Stores") == 0){ 
	        	fragment = new HomeFragment();
	        }
	        else if(selectedItem.compareTo("My Closet") == 0){
	        	fragment = new ClosetFragment();
	        }
	        else if(selectedItem.compareTo("Create New Store") == 0){
	        	fragment = new CreateStoreFragment();
	        }
	        else {
	        	fragment = new HomeFragment();
	        }
	        
	        if(fragment != null) {
	            // Replace current fragment by this new one
	            ft.replace(R.id.activity_main_content_fragment, fragment);
	            ft.commit();
	            
	            // Set title accordingly
	            tvTitle.setText(selectedItem);
	        }
	        
	        // Hide menu anyway
	        mainLayout.toggleMenu();
	}
	
}
