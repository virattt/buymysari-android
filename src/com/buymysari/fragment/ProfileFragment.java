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
import com.buymysari.MarketPlaceActivity;
import com.buymysari.MyApplication;
import com.buymysari.R;
import com.buymysari.SplashActivity;
import com.buymysari.dto.UserInfo_dto;
import com.buymysari.fragment.ClosetFragment.CustomGridViewAdapter;
import com.buymysari.fragment.ClosetFragment.MyListAdapter;

public class ProfileFragment extends Fragment{
	
	EditText edtFname ,edtLname ,edtEmailID ,edtPassword;
	private String userId;
	MyApplication app;
	ImageView imgUser; 
	String base64st;
	byte[] byteArrayimage;
	CircularImageView imguser1;
	
	 ProgressDialog progress;
	Bitmap resizedBitmap;
	private View rootView;
	Bitmap bmp;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.profile, container, false);
		app = (MyApplication) getActivity().getApplicationContext();
		edtFname = (EditText)rootView.findViewById(R.id.edtProfileFirstname);
		edtLname = (EditText)rootView.findViewById(R.id.edtProfileLastname);
		edtEmailID = (EditText)rootView.findViewById(R.id.edtProfileEmail);
		edtPassword = (EditText)rootView.findViewById(R.id.edtProfilePAssword);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		progress = new ProgressDialog(getActivity());
		progress.setMessage("Loading...");
		
		new JSONTask().execute();
				
		TextView btnTakeUserPhoto = (TextView)rootView.findViewById(R.id.txt_change_profile);
		
		btnTakeUserPhoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				Intent cameraAct = new Intent(getActivity(),CameraActivity.class);
				cameraAct.putExtra("ImageType", "ProfilePicture");
				startActivityForResult(cameraAct, 1);				
			}
		});
		
		Button btnUpdateProfile = (Button)rootView.findViewById(R.id.btn_update_profile);
		
		
		Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),
			    "fonts/ITCAvantGardeStd-BkCn.otf");
		edtFname.setTypeface(tf);
		edtLname.setTypeface(tf);
		edtEmailID.setTypeface(tf);
		edtPassword.setTypeface(tf);
		btnTakeUserPhoto.setTypeface(tf);
		btnUpdateProfile.setTypeface(tf);
		
		btnUpdateProfile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		
				String strFname = edtFname.getText().toString();
				String strLname = edtLname.getText().toString();
				String strEmail = edtEmailID.getText().toString();
				String strPassword = edtPassword.getText().toString();
				
				Log.v("log", " strPassword "  +  strPassword);
				
				ArrayList<UserInfo_dto>  result_list =  DBAdpter.updateUserInfo(userId,strFname,strLname,strEmail,strPassword,base64st);
				
				
				if(result_list.get(0).getMsg().equals("User information successfully updated"))
				{
					Toast.makeText(getActivity(), result_list.get(0).getMsg() , 1).show();
					
					String message = DBAdpter.updateUserImage(userId,base64st);
					
					Log.v("log"," message -->  "+ message);
					
					FragmentManager fm = getFragmentManager();
					FragmentTransaction fragmentTransaction = fm
							.beginTransaction();
					
					Fragment homeFrag = fm.findFragmentByTag("HOME");
					if (homeFrag != null) {
						Log.v("fragment", "got fragement");
						fragmentTransaction.replace(R.id.activity_main_content_fragment, homeFrag, "HOME");
					} else {
						HomeFragment hoFrag = new HomeFragment();
						fragmentTransaction.replace(R.id.activity_main_content_fragment, hoFrag, "HOME");
					}

					fragmentTransaction.addToBackStack(null);
					fragmentTransaction.commit();
					
				}
			}
		});
		
		return rootView;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1) {
			Log.v("log"," OnActivity  Result");
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
					
					imguser1.setImageBitmap(resizedBitmap);
					imguser1.setScaleType(ScaleType.CENTER);
					imguser1.setBorderColor(getResources().getColor(R.color.GrayLight));
					imguser1.setBorderWidth(0);
					MarketPlaceActivity.imView.setImageBitmap(resizedBitmap);
					MarketPlaceActivity.imView.setScaleType(ScaleType.CENTER);
					
				} else {
					imgUser.setImageBitmap(resizedBitmap);
					imgUser.setScaleType(ScaleType.CENTER);
					MarketPlaceActivity.imView1.setImageBitmap(resizedBitmap);
					MarketPlaceActivity.imView1.setScaleType(ScaleType.CENTER);
				}
				
				base64st = Base64.encodeBytes(byteArrayimage);
			}
		}
	}
	
	public class JSONTask extends AsyncTask<String, Void, String> {

		public void onPreExecute() {
			progress.show();
		}

		@Override
		protected String doInBackground(String... arg) {
			String listSize = "";
			
			String FirstNAme = DBAdpter.fetch_UserDetail_data.get(0).getFirst_name(); 
			userId = app.getUserID();
			String lastName = DBAdpter.fetch_UserDetail_data.get(0).getLast_name();
			String emailId = DBAdpter.fetch_UserDetail_data.get(0).getEmail();
			String Mobile = DBAdpter.fetch_UserDetail_data.get(0).getMobile();
			
			Log.v("log", "userId" + userId + " FirstNAme : " + FirstNAme + " LAstName " + lastName + " emailId " + emailId + "Mobile " + Mobile);
			
			edtFname.setText(FirstNAme);
			edtLname.setText(lastName);
			edtEmailID.setText(emailId);
			edtPassword.setText("");
			
			String img_url = DBAdpter.fetch_UserDetail_data.get(0).getStrore_profile_image().toString().trim();
			bmp = getBitmapFromUrl(img_url);
			
			return listSize; // This value will be returned to your
								// onPostExecute(result) method
		}

		@Override
		protected void onPostExecute(String result) {
			// Create here your JSONObject...
			Log.v("log_tag", "list ON Post");
			progress.dismiss();
			
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				Log.v("log", " Above HoneyComb ");
				
				imguser1 = (CircularImageView) rootView.findViewById(R.id.img_user_image);
						imguser1.setImageBitmap(bmp);
				imguser1.setBorderColor(getResources().getColor(R.color.GrayLight));
				imguser1.setBorderWidth(0);
				
				
				final BitmapDrawable bitmapDrawable = (BitmapDrawable) imguser1
						.getDrawable();
				final Bitmap yourBitmap = bitmapDrawable.getBitmap();

				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				yourBitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
				byte[] byteArray = stream.toByteArray();
				base64st = Base64.encodeBytes(byteArray);
				Log.v("log_tag", "base64st" + base64st);
				
				
			} else {
				Log.v("log", " Below HoneyComb ");

				imgUser = (ImageView)rootView.findViewById(R.id.img_user_image);
				imgUser.setImageBitmap(bmp);
				final BitmapDrawable bitmapDrawable = (BitmapDrawable) imgUser
						.getDrawable();
				final Bitmap yourBitmap = bitmapDrawable.getBitmap();

				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				yourBitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
				byte[] byteArray = stream.toByteArray();
				base64st = Base64.encodeBytes(byteArray);
				Log.v("log_tag", "base64st" + base64st);
			}

			
		}

		// You'll have to override this method on your other tasks that extend
		// from this one and use your JSONObject as needed

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
