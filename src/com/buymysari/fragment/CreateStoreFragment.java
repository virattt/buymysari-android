package com.buymysari.fragment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.buymysari.dto.Store_profile_dto;

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
	byte[] byteArrayimage;
	String Status = null;
	ArrayList<Store_profile_dto> storeProfileInfoList;
	Store_profile_dto str_prof_data;
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
		imageLoader = new ImageLoader(getActivity().getApplicationContext());

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
			if (DBAdpter.fetch_list_StoreProfile_data != null) {
				if (DBAdpter.fetch_list_StoreProfile_data.get(0)
						.getStrore_prof_name() != null) {
					edtName.setText(DBAdpter.fetch_list_StoreProfile_data
							.get(0).getStrore_prof_name());
					edtWebsite.setText(DBAdpter.fetch_list_StoreProfile_data
							.get(0).getStrore_prof_website());
					edtEmail.setText(DBAdpter.fetch_list_StoreProfile_data.get(
							0).getStrore_prof_email());
					edtPhone.setText(DBAdpter.fetch_list_StoreProfile_data.get(
							0).getStrore_prof_mobile());
					edtCity.setText(DBAdpter.fetch_list_StoreProfile_data
							.get(0).getStrore_prof_city());
					edtCountry.setText(DBAdpter.fetch_list_StoreProfile_data
							.get(0).getStrore_prof_country());
					edtState.setText(DBAdpter.fetch_list_StoreProfile_data.get(
							0).getStrore_prof_state());

				/*	imageLoader.DisplayImage(
							DBAdpter.fetch_list_StoreProfile_data.get(0)
									.getStrore_prof_image(), imgStorePicture);*/
					
					
					  String img_url= DBAdpter.fetch_list_StoreProfile_data.get(0).getStrore_prof_image().toString().trim();
						        URL url = null;
						     try {
						      url = new URL(img_url);
						     } catch (MalformedURLException e) {
						      // TODO Auto-generated catch block
						      e.printStackTrace();
						     }
						     Bitmap bmp = null; 
						        try {
						      bmp=BitmapFactory.decodeStream(url.openConnection().getInputStream());
						     } catch (IOException e) {
						      // TODO Auto-generated catch block
						      e.printStackTrace();
						     }
						        imgStorePicture.setImageBitmap(bmp);
				}
			}
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

				final String uid = app.getUserID();

				if ((!str_name.equals("")) && (!str_email.equals(""))
						&& (!str_website.equals(""))
						&& (!str_mobile.equals("")) && (!str_city.equals(""))
						&& (!str_country.equals("")) && (!str_state.equals(""))) {

					ArrayList<Store_profile_dto> list_result = DBAdpter
							.createUserStore(str_name, str_email, str_website,
									str_mobile, str_city, str_state,
									str_country, uid);

					app.setStoreId(list_result.get(0).getStrore_prof_id());
					// app.setUserID(list_result.get(0).getStrore_prof_user_id());

					Toast.makeText(
							getActivity(),
							" Store id -->  "
									+ list_result.get(0).getStrore_prof_id(),
							Toast.LENGTH_LONG).show();

					Status = DBAdpter.uploadStorePhoto(base64string);

					FragmentManager fm = getActivity()
							.getSupportFragmentManager();
					FragmentTransaction fragmentTransaction = fm
							.beginTransaction();
					StoreProfileGridFragment fm2 = new StoreProfileGridFragment();
					fragmentTransaction.replace(R.id.rela_createStore, fm2,
							"HELLO");
					fragmentTransaction.addToBackStack(null);
					fragmentTransaction.commit();

					MarketPlaceActivity.mainLayout.toggleMenu();
					MarketPlaceActivity.storeOptions
							.setVisibility(View.VISIBLE);
					MarketPlaceActivity.btnCreateStore.setVisibility(View.GONE);

				} else {
					Toast.makeText(getActivity(), "Pls Fill All value..",
							Toast.LENGTH_LONG).show();
				}
			}
		});

		btnUpdateStore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				str_user_id = app.getUserID();
				StoreId = app.getStoreId();

				str_name = edtName.getText().toString().trim();
				str_website = edtWebsite.getText().toString().trim();
				str_email = edtEmail.getText().toString().trim();
				str_city = edtCity.getText().toString().trim();
				str_mobile = edtPhone.getText().toString().trim();

				final BitmapDrawable bitmapDrawable = (BitmapDrawable) imgStorePicture
						.getDrawable();
				final Bitmap yourBitmap = bitmapDrawable.getBitmap();

				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				yourBitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
				byte[] byteArray = stream.toByteArray();
				base64st = Base64.encodeBytes(byteArray);

				

				ArrayList<Store_profile_dto> list_result = DBAdpter
						.updateUserStore(StoreId, str_name, str_email,
								str_website, str_mobile, str_city, base64st);
				Toast.makeText(getActivity(), "sucessfully update",
						Toast.LENGTH_LONG).show();

				FragmentManager fm = getActivity().getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fm.beginTransaction();
				StoreProfileGridFragment fm2 = new StoreProfileGridFragment();
				fragmentTransaction
						.replace(R.id.rela_createStore, fm2, "HELLO");
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();

			}
		});
		return view;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1) {

			if (data.hasExtra("data")) {

				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inPurgeable = true; // inPurgeable is used to free up
											// memory while required

				Bitmap b = BitmapFactory.decodeByteArray(
						data.getByteArrayExtra("data"), 0,
						data.getByteArrayExtra("data").length, options);

				int width = b.getWidth();
				int height = b.getHeight();
				int newWidth = 100;
				int newHeight = 80;
				float scaleWidth = ((float) newWidth) / width;

				float scaleHeight = ((float) newHeight) / height;

				Matrix matrix = new Matrix();

				matrix.postScale(scaleWidth, scaleHeight);

				int rotation = getActivity().getWindowManager()
						.getDefaultDisplay().getRotation();

				int finalDegree = 0;

				if (rotation == 0) {

					finalDegree = 90;

				}

				if (rotation == 1) {

					finalDegree = 270;

				}

				if (rotation == 2) {

					finalDegree = 180;

				}

				if (rotation == 3) {

					finalDegree = 90;

				}

				matrix.postRotate(finalDegree);

				Bitmap resizedBitmap = Bitmap.createBitmap(b, 0, 0, width,
						height, matrix, true);
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
				byteArrayimage = stream.toByteArray();

				imgStorePicture.setImageBitmap(resizedBitmap);
				imgStorePicture.setScaleType(ScaleType.CENTER);

				base64string = Base64.encodeBytes(byteArrayimage);
			}
		}
	}

}
