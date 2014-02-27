package com.example.marketplace;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.marketplace.dto.UserInfo_dto;

public class CreateStoreActivity extends Activity {

	ImageView imgStorePicture;
	Button btnCreateStore ,btnTakePicture;

	EditText edtName, edtWebsite, edtEmail, edtPhone, edtCity, edtCountry,
			edtState;
	String str_name, str_email, str_city, str_mobile, str_state, str_country,
			str_website;
	String result = null;
	
	ArrayList<UserInfo_dto> UserInfoList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_store);

		imgStorePicture = (ImageView)findViewById(R.id.imageView1);
		btnCreateStore = (Button) findViewById(R.id.btnCreateStore);
		btnTakePicture = (Button) findViewById(R.id.btnTakePicture);
		
		edtName = (EditText) findViewById(R.id.edtUserName);
		edtWebsite = (EditText) findViewById(R.id.edtWebsite);
		edtEmail = (EditText) findViewById(R.id.edtEmail);
		edtPhone = (EditText) findViewById(R.id.edtPhone);
		edtCity = (EditText) findViewById(R.id.edtCity);
		edtCountry = (EditText) findViewById(R.id.edtCountry);
		edtState = (EditText) findViewById(R.id.edtState);
		
		btnTakePicture.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				Intent cameraAct = new Intent(CreateStoreActivity.this,CameraActivity.class);
				startActivityForResult(cameraAct, 1);
				
			}
		});

		btnCreateStore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				str_name = edtName.getText().toString().trim();
				str_website = edtWebsite.getText().toString().trim();
				str_email = edtEmail.getText().toString().trim();
				str_city = edtCity.getText().toString().trim();
				str_mobile = edtPhone.getText().toString().trim();
				str_state = edtState.getText().toString().trim();
				str_country = edtCountry.getText().toString().trim();

				Log.v("log", " str_name " + str_name + " " + str_website
						+ " str_email " + str_email + " str_city " + str_city
						+ "str_mobile " + str_mobile + " state " + str_state
						+ "country " + str_country);

				// userID = UserInfoList.get(0).getUser_id();
				// Log.v("log", "  userId " + userID);

				result = DBAdpter.createUserStore(str_name, str_email,
						str_website, str_mobile, str_city, str_state,
						str_country);

				Log.v("log_tag", "result register activity :: " + result);
				Toast.makeText(CreateStoreActivity.this, result, 1).show();

			}
		});
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		  if (requestCode == 1) {

		     if(resultCode == RESULT_OK){      
		         String path = data.getStringExtra("path");
		         
		         Log.v("log"," path " + path);
		         
		         imgStorePicture.setImageURI(Uri.fromFile(new File(path)));
		     }
		     if (resultCode == RESULT_CANCELED) {    
		         //Write your code if there's no result
		     }
		  }
		}//onActivityResult
	
}
