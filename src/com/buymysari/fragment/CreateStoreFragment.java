package com.buymysari.fragment;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

import com.buymysari.Base64;
import com.buymysari.CameraActivity;
import com.buymysari.DBAdpter;
import com.buymysari.ImageLoader;
import com.buymysari.MarketPlaceActivity;
import com.buymysari.MyApplication;
import com.buymysari.R;
import com.buymysari.dto.UserInfo_dto;

public class CreateStoreFragment extends Fragment {

	ImageView imgStorePicture;
	Button btnCreateStore, btnTakePicture, btnUpdateStore;

	EditText edtName, edtWebsite, edtEmail, edtPhone, edtCity, edtCountry,
			edtState;
	String str_name, str_email, str_city, str_mobile, str_state, str_country,
			str_website, str_user_id;
	String result = null;
	MyApplication app;
	String base64string = "";
	byte[] data;
	byte[] Image_getByte1;
	String Status = null;
	ArrayList<UserInfo_dto> UserInfoList;
	String base64st;
	String StoreId;
	public ImageLoader imageLoader;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.create_store);
		View view = inflater.inflate(R.layout.create_store, null);

		imgStorePicture = (ImageView) view.findViewById(R.id.imageView1);
		btnCreateStore = (Button) view.findViewById(R.id.btnCreateStore);
		btnTakePicture = (Button) view.findViewById(R.id.btnTakePicture);
		btnUpdateStore = (Button) view.findViewById(R.id.btnUpdateStore);

		app = (MyApplication) getActivity().getApplicationContext();
		imageLoader=new ImageLoader(getActivity().getApplicationContext());
		
		edtName = (EditText) view.findViewById(R.id.edtUserName);
		edtWebsite = (EditText) view.findViewById(R.id.edtWebsite);
		edtEmail = (EditText) view.findViewById(R.id.edtEmail);
		edtPhone = (EditText) view.findViewById(R.id.edtPhone);
		edtCity = (EditText) view.findViewById(R.id.edtCity);
		edtCountry = (EditText) view.findViewById(R.id.edtCountry);
		edtState = (EditText) view.findViewById(R.id.edtState);

		if (app.getStoreId().equals("null")) {

			btnCreateStore.setVisibility(view.VISIBLE);
			btnUpdateStore.setVisibility(view.INVISIBLE);
			edtName.setText("");
			edtWebsite.setText("");
			edtEmail.setText("");
			edtPhone.setText("");
			edtCity.setText("");
			edtCountry.setText("");
			edtState.setText("");

		} else {
			btnCreateStore.setVisibility(view.INVISIBLE);
			btnUpdateStore.setVisibility(view.VISIBLE);
			edtName.setText(DBAdpter.fetch_UserDetail_data.get(0)
					.getStoreName());
			edtWebsite.setText(DBAdpter.fetch_UserDetail_data.get(0)
					.getStoreWebsite());
			edtEmail.setText(DBAdpter.fetch_UserDetail_data.get(0)
					.getStoreEmail());
			edtPhone.setText(DBAdpter.fetch_UserDetail_data.get(0)
					.getStoreMobile());
			edtCity.setText(DBAdpter.fetch_UserDetail_data.get(0)
					.getStoreCity());
			edtCountry.setText(DBAdpter.fetch_UserDetail_data.get(0)
					.getStoreCountry());
			edtState.setText(DBAdpter.fetch_UserDetail_data.get(0)
					.getStoreState());

		/*	try {
				Image_getByte1 = Base64.decode(DBAdpter.fetch_UserDetail_data.get(0).getStoreImage());
				if(Image_getByte1 != null)
				{
					ByteArrayInputStream bytes1 = new ByteArrayInputStream(Image_getByte1);
					BitmapDrawable bmd1 = new BitmapDrawable(bytes1);
					Bitmap bm1 = bmd1.getBitmap();
					imgStorePicture.setImageBitmap(bm1);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} */
			

			imageLoader.DisplayImage(DBAdpter.fetch_UserDetail_data.get(0).getStoreImage(), imgStorePicture);
			
		}

		btnTakePicture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent cameraAct = new Intent(getActivity(),
						CameraActivity.class);
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

				final String uid = app.getUserID();
				final String store_id = app.getStoreId();

				Log.v("log", " create userID --> " + uid + " store_id = = > "
						+ store_id);

				if ((!str_name.equals("")) && (!str_email.equals(""))
						&& (!str_website.equals("")) && (!str_mobile.equals(""))
						&& (!str_city.equals("")) && (!str_country.equals(""))
						&& (!str_state.equals(""))) {
					Log.v("log_tag", " if " + uid);
					ArrayList<UserInfo_dto> list_result = DBAdpter.createUserStore(str_name, str_email,
							str_website, str_mobile, str_city, str_state,
							str_country, uid);
					
					Log.v("log"," Store " + list_result.get(0).getStoreId());
					
					app.setStoreId(list_result.get(0).getStoreId());
					
					Toast.makeText(getActivity(), " Store id -->  " + list_result.get(0).getStoreId() , Toast.LENGTH_LONG).show();

					Status = DBAdpter.uploadStorePhoto(base64string);
					Log.v("log_tag", Status);

					
					FragmentManager fm = getActivity().getSupportFragmentManager();
					FragmentTransaction fragmentTransaction = fm.beginTransaction();
					StoreProfileGridFragment fm2 = new StoreProfileGridFragment();
					fragmentTransaction.replace(R.id.rela_createStore,fm2, "HELLO");
					fragmentTransaction.addToBackStack(null);
					fragmentTransaction.commit();
					
					MarketPlaceActivity.mainLayout.toggleMenu();
					MarketPlaceActivity.storeOptions.setVisibility(View.VISIBLE);
					MarketPlaceActivity.btnCreateStore.setVisibility(View.GONE);
		        	
					
				} else {
					Toast.makeText(getActivity(), "Pls Fill All value..",Toast.LENGTH_LONG).show();
				}
			}
		});
		
		btnUpdateStore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				str_user_id = DBAdpter.fetch_UserDetail_data.get(0).getUser_id();
				StoreId = DBAdpter.fetch_UserDetail_data.get(0).getStoreId();
				
				str_name = edtName.getText().toString().trim();
				str_website = edtWebsite.getText().toString().trim();
				str_email = edtEmail.getText().toString().trim();
				str_city = edtCity.getText().toString().trim();
				str_mobile = edtPhone.getText().toString().trim();
				
				
				final BitmapDrawable bitmapDrawable = (BitmapDrawable) imgStorePicture.getDrawable();
	            final Bitmap yourBitmap = bitmapDrawable.getBitmap();
	            
	            ByteArrayOutputStream stream = new ByteArrayOutputStream();
	            yourBitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
	            byte[] byteArray = stream.toByteArray();
	            base64st = Base64.encodeBytes(byteArray);
				
				result = DBAdpter.updateUserStore(StoreId,str_name, str_email,str_website, str_mobile, str_city,base64st);
				Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
				
				FragmentManager fm = getActivity().getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fm.beginTransaction();
				StoreProfileGridFragment fm2 = new StoreProfileGridFragment();
				fragmentTransaction.replace(R.id.rela_createStore,fm2, "HELLO");
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();
				
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

				/*
				 * int w = 100; int h = 100;
				 * 
				 * Matrix mtx = new Matrix(); int finalDegree = 90;
				 * mtx.postRotate(finalDegree); b = Bitmap.createBitmap(b, 0 , 0
				 * , w , h, mtx , true);
				 */

				imgStorePicture.setImageBitmap(b);
				imgStorePicture.setScaleType(ScaleType.FIT_XY);

				base64string = Base64.encodeBytes(data
						.getByteArrayExtra("data"));
				Log.v("log", "base64string " + base64string);
			}
		}
	}
}
