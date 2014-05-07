package com.buymysari.fragment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
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
import android.widget.TextView;
import android.widget.Toast;
import com.buymysari.Base64;
import com.buymysari.CameraActivity;
import com.buymysari.CircularImageView;
import com.buymysari.DBAdpter;
import com.buymysari.ImageLoader;
import com.buymysari.MarketPlaceActivity;
import com.buymysari.MyApplication;
import com.buymysari.R;
import com.buymysari.dto.Store_profile_dto;
import com.buymysari.dto.UserInfo_dto;

public class CreateStoreFragment extends Fragment {

	// ImageView imgStorePicture;
	Button btnUpdateStore;
	TextView btnTakePicture;

	EditText first_name, last_name, email_edt, password_edt, store_name_edt,
			website_edt, phone_edt, city_edt, address_edt ,edtStoreProfilePassword;
	String str_first_name, str_last_name, str_email_edt, str_password_edt,
			str_store_name_edt, str_website_edt, str_phone_edt, str_city_edt,
			str_address_edt ,str_edtStoreProfilePassword;

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
	ImageView imView;
	CircularImageView imgStorePicture;
	ArrayList<UserInfo_dto> list_result;
	private ProgressDialog progress;
	Bitmap resizedBitmap;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.create_store);
		View view = inflater.inflate(R.layout.create_store, null);

		// imgStorePicture = (ImageView) view.findViewById(R.id.imageView1);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		btnTakePicture = (TextView) view.findViewById(R.id.btnTakePicture);
		btnUpdateStore = (Button) view.findViewById(R.id.register_btn_update_store);

		app = (MyApplication) getActivity().getApplicationContext();
		imageLoader = new ImageLoader(getActivity().getApplicationContext());

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			imgStorePicture = (CircularImageView) view.findViewById(R.id.imageView1);
			imgStorePicture.setBorderColor(getResources().getColor(R.color.GrayLight));
			imgStorePicture.setBorderWidth(0);
		} else {
			Log.v("log", " Below HoneyComb ");
			imView = (ImageView) view.findViewById(R.id.imageView1);
		}

		first_name = (EditText) view.findViewById(R.id.fristName_edt_lg_update_store);
		last_name = (EditText) view.findViewById(R.id.lastName_edt_lg_update_store);
		email_edt = (EditText) view.findViewById(R.id.email_edt_lg_update_store);

		store_name_edt = (EditText) view.findViewById(R.id.store_name_update_edt);
		website_edt = (EditText) view.findViewById(R.id.store_website_update_edt);
		phone_edt = (EditText) view.findViewById(R.id.store_phone_update_edt);
		city_edt = (EditText) view.findViewById(R.id.store_address_update_edt);
		address_edt = (EditText) view.findViewById(R.id.store_city_update_edt);
		edtStoreProfilePassword = (EditText) view.findViewById(R.id.edtStoreProfilePassword);
		
		
		Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),
			    "fonts/ITCAvantGardeStd-BkCn.otf");
		btnTakePicture.setTypeface(tf);
		btnUpdateStore.setTypeface(tf);
		first_name.setTypeface(tf);
		last_name.setTypeface(tf);
		email_edt.setTypeface(tf);
		store_name_edt.setTypeface(tf);
		website_edt.setTypeface(tf);
		phone_edt.setTypeface(tf);
		city_edt.setTypeface(tf);
		address_edt.setTypeface(tf);
		edtStoreProfilePassword.setTypeface(tf);
		
		progress = new ProgressDialog(getActivity());
		progress.setMessage("Loading...");
		
		new JSONTask().execute();
		
		btnTakePicture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent cameraAct = new Intent(getActivity(),CameraActivity.class);
				cameraAct.putExtra("ImageType", "StorePicture");
				startActivityForResult(cameraAct, 1);
			}
		});

		btnUpdateStore.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				StoreId = app.getStoreId();

				str_first_name = first_name.getText().toString().trim();
				str_last_name = last_name.getText().toString().trim();
				str_email_edt = email_edt.getText().toString().trim();
				str_store_name_edt = store_name_edt.getText().toString().trim();
				str_website_edt = website_edt.getText().toString().trim();
				str_phone_edt = phone_edt.getText().toString().trim();
				str_city_edt = city_edt.getText().toString().trim();
				str_address_edt = address_edt.getText().toString().trim();
				str_edtStoreProfilePassword= edtStoreProfilePassword.getText().toString().trim();

				if ((!str_first_name.equals("")) && (!str_last_name.equals(""))
						&& (!str_email_edt.equals(""))
						&& (!str_store_name_edt.equals(""))
						&& (!str_website_edt.equals(""))
						&& (!str_phone_edt.equals(""))
						&& (!str_city_edt.equals(""))
						&& (!str_address_edt.equals(""))) {

					progress = new ProgressDialog(getActivity());
					progress.setMessage("Loading...");
					

					final BitmapDrawable bitmapDrawable = (BitmapDrawable) imgStorePicture.getDrawable();
					final Bitmap yourBitmap = bitmapDrawable.getBitmap();

					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					yourBitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
					byte[] byteArray = stream.toByteArray();
					base64st = Base64.encodeBytes(byteArray);
					Log.v("log_tag", "base64st" + base64st);
					new ConformDataTask(progress).execute("Home");
				}
			}
		});
		return view;
	}

	public class ConformDataTask extends AsyncTask<String, Void, String> {

		public ConformDataTask(ProgressDialog progress) {
			progress = progress;
		}

		public void onPreExecute() {
			progress.show();

		}

		@Override
		protected String doInBackground(String... arg) {
			String listSize = "";
			list_result = DBAdpter.updateUserStore(StoreId, str_first_name,
					str_last_name, str_email_edt, str_store_name_edt,
					str_website_edt, str_phone_edt, str_city_edt,
					str_address_edt, base64st , str_edtStoreProfilePassword);

			listSize = list_result.size() + "";
			return listSize;
		}

		@Override
		protected void onPostExecute(String result) {
			// Create here your JSONObject...
			Toast.makeText(getActivity(), "sucessfully update",
					Toast.LENGTH_LONG).show();

			FragmentManager fm = getActivity().getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fm.beginTransaction();
			StoreProfileGridFragment fm2 = new StoreProfileGridFragment();
			fragmentTransaction.replace(R.id.rela_createStore, fm2, "HELLO");
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();
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
			
			return listSize; // This value will be returned to your
								// onPostExecute(result) method
		}

		@Override
		protected void onPostExecute(String result) {
			// Create here your JSONObject...
			Log.v("log_tag", "list ON Post");
			
			if (DBAdpter.fetch_UserDetail_data != null) {

				first_name.setText(DBAdpter.fetch_UserDetail_data.get(0)
						.getStrore_profile_firstName());
				last_name.setText(DBAdpter.fetch_UserDetail_data.get(0)
						.getStrore_profile_lastName());
				email_edt.setText(DBAdpter.fetch_UserDetail_data.get(0)
						.getStrore_profile_email());

				store_name_edt.setText(DBAdpter.fetch_UserDetail_data.get(0)
						.getStrore_profile_name());
				website_edt.setText(DBAdpter.fetch_UserDetail_data.get(0)
						.getStrore_profile_website());

				city_edt.setText(DBAdpter.fetch_UserDetail_data.get(0)
						.getStrore_profile_city());
				address_edt.setText(DBAdpter.fetch_UserDetail_data.get(0)
						.getStrore_profile_Address());
				phone_edt.setText(DBAdpter.fetch_UserDetail_data.get(0)
						.getStrore_profile_mobile());

			}
			
			String img_url = DBAdpter.fetch_UserDetail_data.get(0).getStrore_profile_image().toString().trim();
			Log.v("log_tag", "img_url  " + img_url);

			if (img_url.equals("")) {

			} else {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					Bitmap b = getBitmapFromUrl(img_url);
					//imageLoader.DisplayImage(img_url, imgStorePicture);
					imgStorePicture.setImageBitmap(b);
				} else {
					Bitmap bmp = getBitmapFromUrl(img_url);
					imView.setImageBitmap(bmp);
					//imageLoader.DisplayImage(img_url, imView);
				}
			}
			
			progress.dismiss();
			
		}

		// You'll have to override this method on your other tasks that extend
		// from this one and use your JSONObject as needed

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
				if (data.hasExtra("image_from")) {
					resizedBitmap = Bitmap.createBitmap(b, 0, 0, width, height,
							matrix, true);
					
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
					byteArrayimage = stream.toByteArray();
					
				}
				else
				{
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

				resizedBitmap = Bitmap.createBitmap(b, 0, 0, width,
						height, matrix, true);
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
				byteArrayimage = stream.toByteArray();
			}
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

					imgStorePicture.setImageBitmap(resizedBitmap);
					imgStorePicture.setScaleType(ScaleType.CENTER);
					MarketPlaceActivity.imView.setImageBitmap(resizedBitmap);
					MarketPlaceActivity.imView.setScaleType(ScaleType.CENTER);

				} else {
					imView.setImageBitmap(resizedBitmap);
					imView.setScaleType(ScaleType.CENTER);
					MarketPlaceActivity.imView1.setImageBitmap(resizedBitmap);
					MarketPlaceActivity.imView1.setScaleType(ScaleType.CENTER);
				}

				base64string = Base64.encodeBytes(byteArrayimage);
			}
		}
	}
	
	public Bitmap getBitmapFromUrl(String urlStore) {
		URL url = null;
		try {
			url = new URL(urlStore);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Bitmap bmp = null;
		try {
			bmp = BitmapFactory.decodeStream(url.openConnection()
					.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bmp;
	}
	
	

}
