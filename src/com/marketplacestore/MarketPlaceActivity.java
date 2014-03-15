package com.marketplacestore;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.marketplacestore.dto.CateDto;
import com.marketplacestore.fragment.ClosetFragment;
import com.marketplacestore.fragment.CreateStoreFragment;
import com.marketplacestore.fragment.HomeFragment;
import com.marketplacestore.fragment.ProfileFragment;
import com.marketplacestore.fragment.SearchItemsFragment;
import com.marketplacestore.fragment.StoreProfileFragment;
import com.marketplacestore.fragment.StoreProfileGridFragment;
import com.marketplacestore.layout.MainLayout;


public class MarketPlaceActivity extends FragmentActivity implements View.OnClickListener{
	
	MainLayout mainLayout;
	Button btMenu;	
	private ListView lvMenu , StoreMenu;
	private String[] lvMenuItems;
	private String[] StoreMenuItems;
	public static TextView tvTitle;
	String cityName;
	List<Button> buttons = new ArrayList<Button>();
	
	ArrayAdapter<String> adapter;
	ArrayList<CateDto> CategoryNames;
	EditText edtSearchText;
	String searchText;
	TableRow tr;
	
	int cateIdSelected;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	//	setContentView(R.layout.activity_market_place);
		mainLayout = (MainLayout)this.getLayoutInflater().inflate(R.layout.activity_market_place, null);
        setContentView(mainLayout);
		
        edtSearchText = (EditText) findViewById(R.id.edtSearchText);
        
		TableLayout layout = (TableLayout)findViewById(R.id.tab_layout);
        layout.setLayoutParams( new TableLayout.LayoutParams(60,40));
        layout.setPadding(1,1,1,1);
        
        CategoryNames = DBAdpter.fetchCategoryNames();
        Log.v("log"," CategoryNames ---> " + CategoryNames);
        Log.v("log"," Category Names ---> " + CategoryNames.get(0).getCategory_name() + " CateId ==> " + CategoryNames.get(0).getCate_id());
        // .charAt(0)
       // for (int f=0; f<=13; f++) {
           tr = (TableRow)findViewById(R.id.tableRow1);
            for (int c=0; c < CategoryNames.size() ; c++) {
                Button b = new Button (this);
                b.setId(Integer.parseInt(CategoryNames.get(c).getCate_id()));
                b.setText(CategoryNames.get(c).getCategory_name());
                b.setTextSize(15.0f);
                b.setTextColor(Color.BLACK);
                b.setOnClickListener(this);
                buttons.add(b);
                tr.addView(b, 30,30);
           } // for
        
        cityName = getIntent().getExtras().getString("cityName").toString();
              
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
        FragmentTransaction ft = fm.beginTransaction();
        
        HomeFragment fragment = new HomeFragment();
        ft.add(R.id.activity_main_content_fragment, fragment);
        ft.commit();
		
        edtSearchText.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				
				if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
			            (keyCode == KeyEvent.KEYCODE_ENTER)) {
			          // Perform action on key press
			          Toast.makeText(MarketPlaceActivity.this," SearchText --> " + edtSearchText.getText().toString(), Toast.LENGTH_SHORT).show();
			          
			          	tvTitle.setText("Search Reuslts View");
			          
			          	FragmentManager fm = MarketPlaceActivity.this.getSupportFragmentManager();
						FragmentTransaction fragmentTransaction = fm
								.beginTransaction();
						SearchItemsFragment fm2 = new SearchItemsFragment();
						fragmentTransaction.replace(R.id.rela_home_fragment,
								fm2, "HELLO");
						fragmentTransaction.addToBackStack(null);
						fragmentTransaction.commit();
						Bundle bundle = new Bundle();
						bundle.putString("searchText", edtSearchText.getText().toString());
					//	bundle.putString("searchCateId", cateIdSelected +"");
						fm2.setArguments(bundle);
			          
			          return true;
			        }
				
				return false;
			}
		});
        
        
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
        	fragment = new StoreProfileGridFragment();
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
	        
	        if(selectedItem.compareTo("Home") == 0) {
	            fragment = new HomeFragment();
	        } 
	        else if(selectedItem.compareTo("My Profile") == 0) {
	            fragment = new ProfileFragment();
	        } 
	        else if(selectedItem.compareTo("My Stores") == 0){ 
	        	fragment = new StoreProfileFragment();
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

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		// Cate_
		Log.v("log", " Id --> " + view.getId());
	/*	cateIdSelected  = view.getId();
		
		  for (int c=0; c < CategoryNames.size() ; c++) {
			  if((Integer.parseInt(CategoryNames.get(c).getCate_id()) != view.getId()))
			  {
				  Log.v("log"," For Cate ID --> " + CategoryNames.get(c).getCate_id());
				  
				  for(Button b: buttons) {
					     if(b.getId() != Integer.parseInt(CategoryNames.get(c).getCate_id())) {
					    	 b.setEnabled(true); 
					     } 
				  }
			  }
		  }
		  
		  	tvTitle.setText("Search Reuslts View");
          
        	FragmentManager fm = MarketPlaceActivity.this.getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fm
					.beginTransaction();
			SearchItemsFragment fm2 = new SearchItemsFragment();
			fragmentTransaction.replace(R.id.rela_home_fragment,
					fm2, "HELLO");
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();
			Bundle bundle = new Bundle();
			bundle.putString("searchText", edtSearchText.getText().toString());
			bundle.putString("searchCateId", view.getId() +"");
			fm2.setArguments(bundle);
		
	//	((Button) view).setText("*");
        ((Button) view).setEnabled(false);
      */  
	}
}
