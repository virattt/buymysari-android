package com.example.marketplace;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		first_name = (EditText) findViewById(R.id.fristName_edt_lg);
		last_name = (EditText) findViewById(R.id.lastName_edt_lg);
		email_edt = (EditText) findViewById(R.id.email_edt_lg);
		password_edt = (EditText) findViewById(R.id.password_edt_lg);
		city_edt = (EditText) findViewById(R.id.city_edt_lg);
		mobile_edt = (EditText) findViewById(R.id.mobile_edt_lg);
		state_edt = (EditText) findViewById(R.id.state_edt_lg);
		country_edt = (EditText) findViewById(R.id.country_edt_lg);
		register = (Button) findViewById(R.id.register_btn);

		register.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				str_first_name = first_name.getText().toString().trim();
				str_last_name = last_name.getText().toString().trim();
				str_email_edt = email_edt.getText().toString().trim();
				str_password_edt = password_edt.getText().toString().trim();
				str_city_edt = city_edt.getText().toString().trim();
				str_mobile_edt = mobile_edt.getText().toString().trim();
				str_state_edt = state_edt.getText().toString().trim();
				str_country_edt = country_edt.getText().toString().trim();

				result = DBAdpter.registerInUser(str_first_name,
						str_password_edt, str_last_name, str_email_edt,
						str_city_edt, str_state_edt, str_country_edt,
						str_mobile_edt);
				Log.v("log_tag","result register activity :: "+ result);
				Toast.makeText(RegisterActivity.this, result, 1).show();
			}
		});
	}
}
