package com.marketplacestore.fragment;

import java.util.ArrayList;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

import com.marketplacestore.Base64;
import com.marketplacestore.CameraActivity;
import com.marketplacestore.DBAdpter;
import com.marketplacestore.MarketPlaceActivity;
import com.marketplacestore.R;
import com.marketplacestore.dto.UserInfo_dto;

public class CreateStoreFragment extends Fragment {

	ImageView imgStorePicture;
	Button btnCreateStore, btnTakePicture;

	EditText edtName, edtWebsite, edtEmail, edtPhone, edtCity, edtCountry,
			edtState;
	String str_name, str_email, str_city, str_mobile, str_state, str_country,
			str_website;
	String result = null;

	String base64string = "";
	byte[] data;
	ArrayList<UserInfo_dto> UserInfoList;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	//	setContentView(R.layout.create_store);
		View view = inflater.inflate(R.layout.create_store, null);
		
		imgStorePicture = (ImageView) view.findViewById(R.id.imageView1);
		btnCreateStore = (Button) view.findViewById(R.id.btnCreateStore);
		btnTakePicture = (Button) view.findViewById(R.id.btnTakePicture);

		edtName = (EditText) view.findViewById(R.id.edtUserName);
		edtWebsite = (EditText) view.findViewById(R.id.edtWebsite);
		edtEmail = (EditText) view.findViewById(R.id.edtEmail);
		edtPhone = (EditText) view.findViewById(R.id.edtPhone);
		edtCity = (EditText) view.findViewById(R.id.edtCity);
		edtCountry = (EditText) view.findViewById(R.id.edtCountry);
		edtState = (EditText) view.findViewById(R.id.edtState);

		btnTakePicture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent cameraAct = new Intent(getActivity(),CameraActivity.class);
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

				result = DBAdpter.createUserStore(str_name, str_email,
						str_website, str_mobile, str_city, str_state,
						str_country);

				Log.v("log_tag", "result register activity :: " + result);

				String Status = null;
				Status = DBAdpter.uploadStorePhoto(base64string);
				Log.v("log_tag", Status);
				
				Toast.makeText(getActivity(), Status, Toast.LENGTH_LONG).show();
				
				if(Status.equals("Profile Picture successfully update"))
				{
					Intent i = new Intent(getActivity(),MarketPlaceActivity.class);
					startActivity(i);
				}
				
			}
		});
	
		return view;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		Log.v("log", " data --> " + data.getByteArrayExtra("data"));
		
		if (requestCode == 1) {
			
				if (data.hasExtra("data")) {
					
					Log.v("log", " request if ");
					    
					Bitmap b = BitmapFactory.decodeByteArray(
							data.getByteArrayExtra("data"), 0,
							data.getByteArrayExtra("data").length);
					
					int w = 300;
					int h = 300;
					
					Matrix mtx = new Matrix();
					int finalDegree = 90;
					mtx.postRotate(finalDegree);
					b = Bitmap.createBitmap(b, 0 , 0 , w , h, mtx , true);
					imgStorePicture.setImageBitmap(b);
					imgStorePicture.setScaleType(ScaleType.FIT_XY);

					base64string = Base64.encodeBytes(data
							.getByteArrayExtra("data"));
					Log.v("log", "base64string " + base64string);
				}
		}
	}
}
