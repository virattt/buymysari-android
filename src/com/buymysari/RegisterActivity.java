package com.buymysari;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		first_name = (EditText) findViewById(R.id.fristName_edt_lg);
		last_name = (EditText) findViewById(R.id.lastName_edt_lg);
		email_edt = (EditText) findViewById(R.id.email_edt_lg);
		password_edt = (EditText) findViewById(R.id.password_edt_lg);

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

					progress = new ProgressDialog(RegisterActivity.this);
					progress.setMessage("Loading...");

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
				Intent i = new Intent(RegisterActivity.this,
						LoginActivity.class);
				startActivity(i);

			} else {
				Toast.makeText(RegisterActivity.this, result, Toast.LENGTH_LONG)
						.show();
			}
			progress.dismiss();
		}
	}
}
