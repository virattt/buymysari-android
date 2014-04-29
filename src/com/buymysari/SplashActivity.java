package com.buymysari;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SplashActivity extends Activity {

	Button login_home_btn;
	Button register_home_btn;
	Button register_store_btn;
	
	private ProgressDialog progress;
	MyApplication app;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		
		app = (MyApplication) this.getApplicationContext();
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		String userID = sharedPreferences.getString("user_id","");
		String store_id = sharedPreferences.getString("store_id", "");
		
		progress = new ProgressDialog(SplashActivity.this);
		progress.setMessage("Loading...");
		
		Log.v("log", " UserID "+ userID +" store_id --> " + store_id);

		if(!userID.equals("") && store_id.equals(""))
		{
			Log.v("log", " if UserID "+ userID +" store_id --> " + store_id);
			app.setStoreId("");	
	        app.setUserID(userID);
			// DBAdpter.AuthUserInfo(userID, "user");
		
			new AuthTask().execute(userID, "user");
			
		}
		else if(!store_id.equals("") && userID.equals(""))
		{
			app.setStoreId(store_id);	
	        app.setUserID("");
			Log.v("log", " else if UserID "+ userID +" store_id --> " + store_id);
			
			new AuthTask().execute(store_id,"store");
		}
		
		
		login_home_btn = (Button) findViewById(R.id.login_home_btn);
		register_home_btn = (Button) findViewById(R.id.register_home_btn);
		register_store_btn = (Button) findViewById(R.id.register_store_btn);
		login_home_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(SplashActivity.this, LoginActivity.class);
				startActivity(i);
			}
		});

		register_home_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(SplashActivity.this,
						RegisterActivity.class);
				startActivity(i);

			}
		});

		register_store_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(SplashActivity.this, StoreRegister.class);
				startActivity(i);

			}
		});
		
		Typeface tf = Typeface.createFromAsset(this.getAssets(), "fonts/ITCAvantGardeStd-BkCn.otf");
		login_home_btn.setTypeface(tf);
		register_home_btn.setTypeface(tf);
		register_store_btn.setTypeface(tf);
		
	}
	
	public class AuthTask extends AsyncTask<String, Void, String> {

		public void onPreExecute() {
			progress.show();
		}

		@Override
		protected String doInBackground(String... arg) {
			String msg = "";
			String id = arg[0];
			String typeText = arg[1];
			
			DBAdpter.AuthUserInfo(id, typeText);

			return msg;
		}

		@Override
		protected void onPostExecute(String result) {
			// Create here your JSONObject...
			progress.dismiss();
			
			Intent i = new Intent(SplashActivity.this,MarketPlaceActivity.class);
			startActivity(i);

		}
	}
}
