package com.marketplacestore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.marketplacestore.dto.UserInfo_dto;

public class LoginActivity extends Activity {
	EditText uname;
	EditText pass;
	Button login;
	String strUname;
	String strPass;
	String result;
	ArrayList<UserInfo_dto> list = new ArrayList<UserInfo_dto>();
	GPSTracker gps;
	String cityName;
	MyApplication app;
	ConnectionDetector cd;
	Boolean isInternetPresent = false;
	private ProgressDialog progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		app = (MyApplication) LoginActivity.this.getApplicationContext();

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		uname = (EditText) findViewById(R.id.user_edt_lg);
		pass = (EditText) findViewById(R.id.pass_edt_lg);
		login = (Button) findViewById(R.id.login);

		progress = new ProgressDialog(LoginActivity.this);
		progress.setMessage("Loading...");

		cd = new ConnectionDetector(getApplicationContext());

		login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				isInternetPresent = cd.isConnectingToInternet();

				if (isInternetPresent) {
					// TODO Auto-generated method stub
					strUname = uname.getText().toString().trim();
					strPass = pass.getText().toString().trim();

					new JSONTask(progress).execute("Home");
					
				} else {
					Toast.makeText(LoginActivity.this, "No Internet Available ", 1)
							.show();
				}

			}
		});
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
	        Log.v("log_tag","list DoinBaCK ");
	        
	        list = DBAdpter.getAllUserInfo(strUname, strPass);
	        
	        return listSize; // This value will be returned to your onPostExecute(result) method
	    }

	    @Override
	    protected void onPostExecute(String result) {
	        // Create here your JSONObject...
	    	Log.v("log_tag","list ON Post");
	    		
	    	String resultNew = list.get(0).msg;
			String user_id = list.get(0).user_id;
			
			if(list.get(0).store_id != null )
			{
				String store_id = list.get(0).store_id;
		        app.setUserID(user_id);
		        app.setStoreId(store_id);	
			}
			
	        if (resultNew.equals("success fully login")) {
				
	        	Toast.makeText(LoginActivity.this, resultNew, Toast.LENGTH_LONG).show();
	        	
				gps = new GPSTracker(LoginActivity.this);

				// check if GPS enabled
				if (gps.canGetLocation()) {

					double latitude = gps.getLatitude();
					double longitude = gps.getLongitude();

					Geocoder geocoder = new Geocoder(LoginActivity.this,Locale.ENGLISH);
					List<Address> addresses;
					try {

						addresses = geocoder.getFromLocation(latitude,longitude, 1);
						Log.v("log_tag", "cityName ::: " + addresses);

						cityName = addresses.get(0).getLocality();
						Log.v("log_tag", "cityName ::: " + cityName);

						Intent inew = new Intent(LoginActivity.this, MarketPlaceActivity.class);
						inew.putExtra("cityName", cityName);
						startActivity(inew);
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {

					gps.showSettingsAlert();
				}

			} else {
				Toast.makeText(LoginActivity.this, resultNew, Toast.LENGTH_LONG).show();
			}
			 progress.dismiss();
			
	    }
	}
}
