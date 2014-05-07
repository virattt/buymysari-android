package com.buymysari;

import java.util.ArrayList;

import com.buymysari.LoginActivity.JSONTask;
import com.buymysari.dto.UserInfo_dto;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	EditText first_name, last_name, email_edt, password_edt, city_edt,
			mobile_edt, state_edt, country_edt;
	Button register;
	String str_first_name, str_last_name, str_email_edt, str_password_edt,
			str_city_edt, str_mobile_edt, str_state_edt, str_country_edt;
	String result;
	private ProgressDialog progress;
	ArrayList<UserInfo_dto> list = new ArrayList<UserInfo_dto>();
	MyApplication app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		progress = new ProgressDialog(RegisterActivity.this);
		progress.setMessage("Loading...");
		first_name = (EditText) findViewById(R.id.fristName_edt_lg);
		last_name = (EditText) findViewById(R.id.lastName_edt_lg);
		email_edt = (EditText) findViewById(R.id.email_edt_lg);
		password_edt = (EditText) findViewById(R.id.password_edt_lg);
		app = (MyApplication) RegisterActivity.this.getApplicationContext();
		register = (Button) findViewById(R.id.register_btn);

		register.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				str_first_name = first_name.getText().toString().trim();
				str_last_name = last_name.getText().toString().trim();
				str_email_edt = email_edt.getText().toString().trim();
				str_password_edt = password_edt.getText().toString().trim();
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			    imm.hideSoftInputFromWindow(register.getWindowToken(), 0);
				
				if (str_first_name.trim().equals("") && str_last_name.trim().equals("")
						&& str_email_edt.trim().equals("") && str_password_edt.trim().equals("")) {

					Toast.makeText(RegisterActivity.this, "Please Fill All Value", Toast.LENGTH_LONG)
					.show();

				} else {

					

					new PostTask().execute("Home");
				}

			}
		});
		
		Typeface tf = Typeface.createFromAsset(this.getAssets(),
			    "fonts/ITCAvantGardeStd-BkCn.otf");
			  first_name.setTypeface(tf);
			  last_name.setTypeface(tf);
			  email_edt.setTypeface(tf);
			  password_edt.setTypeface(tf);
			  register.setTypeface(tf);
		
	}

	private class PostTask extends AsyncTask<String, Integer, String> {

		public void onPreExecute() {
			progress.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String result = "";

			result = DBAdpter.registerInUser(str_first_name, str_password_edt,
					str_last_name, str_email_edt);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (result.equals("success fully Registered")) {
				Toast.makeText(RegisterActivity.this, result, Toast.LENGTH_LONG)
						.show();
				
				new JSONTask().execute("Home");
				
				/*Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
						
				startActivity(i);*/

			} else {
				Toast.makeText(RegisterActivity.this, result, Toast.LENGTH_LONG)
						.show();
			}
			progress.dismiss();
		}
	}
	
public class JSONTask extends AsyncTask<String, Void, String> {
		
		public void onPreExecute() {
		    progress.show();
		}
		
			
	    @Override
	    protected String doInBackground(String... arg) {
	        String listSize = "";
	        Log.v("log_tag","list DoinBaCK wwww ");
	        
	        list = DBAdpter.getAllUserInfo(str_email_edt, str_password_edt);
	        
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
				
	        	Toast.makeText(RegisterActivity.this, resultNew, Toast.LENGTH_LONG).show();
	        	
	        	Intent inew = new Intent(RegisterActivity.this, MarketPlaceActivity.class);
				finish();
				startActivity(inew);

			} else {
				Toast.makeText(RegisterActivity.this, resultNew, Toast.LENGTH_LONG).show();
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
