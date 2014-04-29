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

public class StoreRegister extends Activity {
	EditText first_name, last_name, email_edt, password_edt, store_name_edt,
			website_edt, phone_edt, city_edt, address_edt;
	Button register_store_btn;
	String str_first_name, str_last_name, str_email_edt, str_password_edt,
			str_store_name_edt, str_website_edt, str_phone_edt, str_city_edt,
			str_address_edt;
	private ProgressDialog progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_store);

		first_name = (EditText) findViewById(R.id.fristName_edt_lg_store);
		last_name = (EditText) findViewById(R.id.lastName_edt_lg_store);
		email_edt = (EditText) findViewById(R.id.email_edt_lg_store);
		password_edt = (EditText) findViewById(R.id.password_edt_lg_store);
		store_name_edt = (EditText) findViewById(R.id.store_name_edt);
		website_edt = (EditText) findViewById(R.id.store_website_edt);
		phone_edt = (EditText) findViewById(R.id.store_phone_edt);
		city_edt = (EditText) findViewById(R.id.store_address_edt);
		address_edt = (EditText) findViewById(R.id.store_city_edt);

		register_store_btn = (Button) findViewById(R.id.register_btn_store);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		register_store_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				str_first_name = first_name.getText().toString().trim();
				str_last_name = last_name.getText().toString().trim();
				str_email_edt = email_edt.getText().toString().trim();
				str_password_edt = password_edt.getText().toString().trim();

				str_store_name_edt = store_name_edt.getText().toString().trim();
				str_website_edt = website_edt.getText().toString().trim();
				str_phone_edt = phone_edt.getText().toString().trim();
				str_city_edt = city_edt.getText().toString().trim();
				str_address_edt = address_edt.getText().toString().trim();

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(
						register_store_btn.getWindowToken(), 0);

				if (str_first_name.trim().equals("")
						&& str_last_name.trim().equals("")
						&& str_email_edt.trim().equals("")
						&& str_password_edt.trim().equals("")
						&& str_store_name_edt.trim().equals("")
						&& str_website_edt.trim().equals("")
						&& str_phone_edt.trim().equals("")
						&& str_city_edt.trim().equals("")
						&& str_address_edt.trim().equals("")) {

					Toast.makeText(StoreRegister.this, "Please Fill All Value",
							Toast.LENGTH_LONG).show();

				} else {

					progress = new ProgressDialog(StoreRegister.this);
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
		store_name_edt.setTypeface(tf);
		website_edt.setTypeface(tf);
		phone_edt.setTypeface(tf);
		city_edt.setTypeface(tf);
		address_edt.setTypeface(tf);
		register_store_btn.setTypeface(tf);

	}

	private class PostTask extends AsyncTask<String, Integer, String> {

		public void onPreExecute() {
			progress.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String result = "";

			result = DBAdpter.registerInStore(str_first_name, str_password_edt,
					str_last_name, str_email_edt, str_store_name_edt,
					str_website_edt, str_phone_edt, str_city_edt,
					str_address_edt);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (result.equals("successfully Registered")) {
				Toast.makeText(StoreRegister.this, result, Toast.LENGTH_LONG)
						.show();
				Intent i = new Intent(StoreRegister.this, LoginActivity.class);
				startActivity(i);

			} else {
				Toast.makeText(StoreRegister.this, result, Toast.LENGTH_LONG)
						.show();
			}
			progress.dismiss();
		}
	}
}
