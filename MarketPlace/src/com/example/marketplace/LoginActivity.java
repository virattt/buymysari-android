package com.example.marketplace;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	EditText uname;
	EditText pass;
	Button login;
	String strUname;
	String strPass;
	String result;
	ArrayList<UserInfo_dto> list = new ArrayList<UserInfo_dto>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		uname = (EditText) findViewById(R.id.user_edt_lg);
		pass = (EditText) findViewById(R.id.pass_edt_lg);
		login = (Button) findViewById(R.id.login);

		login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				strUname = uname.getText().toString().trim();
				strPass = pass.getText().toString().trim();

				// DBAdpter.getAllUserInfo(strUname, strPass);

				list = DBAdpter.getAllUserInfo(strUname, strPass);
				String result = list.get(0).msg;
				/*
				 * for (int i = 0; i < list.size(); i++) {
				 * 
				 * String user_id = list.get(i).user_id; Log.v("UserLogin",
				 * user_id); }
				 */

				if (result.equals("success fully login")) {
					Toast.makeText(LoginActivity.this, result, 1).show();
					Intent i = new Intent(LoginActivity.this, Navigation.class);
					startActivity(i);

				} else {
					Toast.makeText(LoginActivity.this, result, 1).show();
				}

			}
		});

	}
}
