package com.buymysari;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SplashActivity extends Activity {

	Button login_home_btn;
	Button register_home_btn;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		login_home_btn = (Button) findViewById(R.id.login_home_btn);
		register_home_btn = (Button) findViewById(R.id.register_home_btn);
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
				Intent i = new Intent(SplashActivity.this, RegisterActivity.class);
				startActivity(i);

			}
		});
	}
	
}
