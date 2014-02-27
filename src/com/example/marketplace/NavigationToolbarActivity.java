package com.example.marketplace;

import com.example.marketplace.R;
import com.example.marketplace.R.drawable;
import com.example.marketplace.R.id;
import com.example.marketplace.R.layout;
import com.example.marketplace.animation.CollapseAnimationDemo;
import com.example.marketplace.animation.ExpandAnimation;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TabHost;

public class NavigationToolbarActivity extends TabActivity  {
	/** Called when the activity is first created. */
	LinearLayout MenuList;
	Button btnToggleMenuList;
	int screenWidth;
	boolean isExpanded;
	private View view;
	private int LastWidth;
	private int ToWidth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.navigate_toolbar);

		MenuList = (LinearLayout) findViewById(R.id.linearLayout2);

		btnToggleMenuList = (Button) findViewById(R.id.button1);
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		screenWidth = metrics.widthPixels;

		
		 	Resources res = getResources(); // Resource object to get Drawables
	        TabHost tabHost = getTabHost();  // The activity TabHost
	        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	        Intent intent;  // Reusable Intent for each tab
	 
	        // Create an Intent to launch an Activity for the tab (to be reused)
	        intent = new Intent().setClass(this, HomeActivity.class);
	 
	        // Initialize a TabSpec for each tab and add it to the TabHost
	        spec = tabHost.newTabSpec("").setIndicator("Home",
	                          res.getDrawable(R.drawable.home_button))
	                      .setContent(intent);
	        tabHost.addTab(spec);
	 
	        // Points tabs
	        intent = new Intent().setClass(this, SubScribeActivity.class);
	        spec = tabHost.newTabSpec("subscribed").setIndicator("SubScribed",
	                          res.getDrawable(R.drawable.ic_launcher))
	                      .setContent(intent);
	        tabHost.addTab(spec);
	 
	        // Social tabs
	        intent = new Intent().setClass(this, ClosetActivity.class);
	        spec = tabHost.newTabSpec("closet").setIndicator("Closet",
	                          res.getDrawable(R.drawable.ic_launcher))
	                      .setContent(intent);
	        tabHost.addTab(spec);
	        tabHost.setCurrentTab(0);
		
		btnToggleMenuList.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				// TODO Auto-generated method stub

				if (isExpanded) {
					isExpanded = false;
					
					Log.v("log", " Exapanded in if" + screenWidth);
					// MenuList.startAnimation(new CollapseAnimation(MenuList,
					// 0,(int)(screenWidth*0.7), 20));
					
					MenuList.startAnimation(new CollapseAnimationDemo(MenuList,0, (int)(screenWidth*0.7) , 5) {
						
						
						@Override
						public void onAnimationEnd(Animation arg0) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onAnimationRepeat(Animation animation) {
							// TODO Auto-generated method stub
							
							view = MenuList;
							
							LayoutParams lyp = view.getLayoutParams();
							Log.v("log", " Exa IF lyp " + lyp);
							
							lyp.width = lyp.width - (int)(screenWidth*0.7) / 20;
							Log.v("log", " Exa IF lyp.width  repeat" + lyp.width);
							
							view.setLayoutParams(lyp);
						}

						@Override
						public void onAnimationStart(Animation animation) {
							// TODO Auto-generated method stub
							view = MenuList;
							
							LayoutParams lyp = view.getLayoutParams();
							
							Log.v("log", " Exa IF lyp.width " + lyp.width);
							
							LastWidth = lyp.width;
						}
					});

				} else {
					isExpanded = true;
					
					Log.v("log", " Is Not Exapanded in else"+ screenWidth);
					
					MenuList.startAnimation(new ExpandAnimation(MenuList,0,(int)(screenWidth*0.7), 5) {
						
						@Override
						public void onAnimationStart(Animation animation) {
							// TODO Auto-generated method stub
							view = MenuList;
							
							LayoutParams lyp = view.getLayoutParams();
					        lyp.width = 0;
					        
					        Log.v("log","  screenWidth  0.7 start : " + (int)(screenWidth*0.7));
					        Log.v("log", " Exa else Start lyp " + lyp.width);
					        
					        view.setLayoutParams(lyp);
					        LastWidth = 0;
						}
						
						@Override
						public void onAnimationRepeat(Animation animation) {
							// TODO Auto-generated method stub
							view = MenuList;
							
							LayoutParams lyp = view.getLayoutParams();
							
							Log.v("log","  screenWidth  0.7 repeat: " + (int)(screenWidth*0.7));
							
					        lyp.width = LastWidth += (int)(screenWidth*0.7)/20;
					        
					        Log.v("log", " Exa else AniRe lyp  " + lyp.width);
					        
					        view.setLayoutParams(lyp);
						}
						
						@Override
						public void onAnimationEnd(Animation animation) {
							// TODO Auto-generated method stub
							
						}
					});
				}

			}
		});
	}
}