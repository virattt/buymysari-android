package com.buymysari;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.buymysari.dto.UserInfo_dto;

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

		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		
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

				//	  InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				//    imm.hideSoftInputFromWindow(login.getWindowToken(), 0);
					
					new JSONTask().execute("Home");
					
				} else {
					Toast.makeText(LoginActivity.this, "No Internet Available ", Toast.LENGTH_LONG)
							.show();
				}

			}
		});
		
		Typeface tf = Typeface.createFromAsset(this.getAssets(),
			    "fonts/ITCAvantGardeStd-BkCn.otf");
			  uname.setTypeface(tf);
			  pass.setTypeface(tf);
			  login.setTypeface(tf);
		
	}
	
	@Override
    public void onPause(){

        super.onPause();
        if(progress != null)
        	progress.dismiss();
    }

	
public class JSONTask extends AsyncTask<String, Void, String> {
		
		public void onPreExecute() {
		    progress.show();
		}
		
			
	    @Override
	    protected String doInBackground(String... arg) {
	        String listSize = "";
	        Log.v("log_tag","list DoinBaCK wwww ");
	        
	        list = DBAdpter.getAllUserInfo(strUname, strPass);
	        
	       return listSize; // This value will be returned to your onPostExecute(result) method
	    }

	    @Override
	    protected void onPostExecute(String result) {
	        // Create here your JSONObject...
	    	Log.v("log_tag","list ON Post");
	    		
	    	String resultNew = list.get(0).msg;
			String user_id = list.get(0).user_id;	
			/*Log.v("log_tag","list DoinBaCK "+user_id);
			 app.setUserID(user_id);
			 Log.v("log_tag","list DoinBaCK storeId "+list.get(0).strore_profile_id );*/
			 
			if(list.get(0).strore_profile_id != null )
			{
				String store_id = list.get(0).strore_profile_id;
		       
		        app.setStoreId(store_id);	
		        app.setUserID("");
		        savePreferences("store_id" , store_id);
		        savePreferences("user_id" , "");
		        
		        Log.v("log_tag","list DoinBaCK if "+user_id);
			}
			else if(user_id != null){
				app.setStoreId("");	
		        app.setUserID(user_id);
		        
		        savePreferences("user_id" , user_id);
		        savePreferences("store_id" , "");
		        
		        Log.v("log_tag","list DoinBaCK else"+list.get(0).strore_profile_id);
			}
			
	        if (resultNew.equals("success fully login")) {
				
	        	Toast.makeText(LoginActivity.this, resultNew, Toast.LENGTH_LONG).show();
	        	
	        	Intent inew = new Intent(LoginActivity.this, MarketPlaceActivity.class);
				startActivity(inew);

			} else {
				Toast.makeText(LoginActivity.this, resultNew, Toast.LENGTH_LONG).show();
			}
	        
	        if(progress != null)
	        	progress.dismiss();
	    	}
	}

	private void savePreferences(String key, String value) {
       SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
       Editor editor = sharedPreferences.edit();
       editor.putString(key, value);
       editor.commit();
   }


}
