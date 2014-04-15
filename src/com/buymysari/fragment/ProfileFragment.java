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
import android.os.Build;
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
import android.widget.TextView;
import android.widget.Toast;

import com.buymysari.Base64;
import com.buymysari.CameraActivity;
import com.buymysari.CircularImageView;
import com.buymysari.DBAdpter;
import com.buymysari.MyApplication;
import com.buymysari.R;
import com.buymysari.dto.UserInfo_dto;

public class ProfileFragment extends Fragment{
	
	EditText edtFname ,edtLname ,edtEmailID ,edtPassword;
	private String userId;
	MyApplication app;
	ImageView imgUser; 
	String base64string = "";
	byte[] byteArrayimage;
	CircularImageView imguser1;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.profile, container, false);
		
		app = (MyApplication) getActivity().getApplicationContext();
		
		String FirstNAme = DBAdpter.fetch_UserDetail_data.get(0).getFirst_name(); 
		userId = app.getUserID();
		String lastName = DBAdpter.fetch_UserDetail_data.get(0).getLast_name();
		String emailId = DBAdpter.fetch_UserDetail_data.get(0).getEmail();
		String Mobile = DBAdpter.fetch_UserDetail_data.get(0).getMobile();
		
		edtFname = (EditText)rootView.findViewById(R.id.edtProfileFirstname);
		edtLname = (EditText)rootView.findViewById(R.id.edtProfileLastname);
		edtEmailID = (EditText)rootView.findViewById(R.id.edtProfileEmail);
		edtPassword = (EditText)rootView.findViewById(R.id.edtProfilePAssword);
		imgUser = (ImageView)rootView.findViewById(R.id.img_user_image);
		
		Log.v("log", "userId" + userId + " FirstNAme : " + FirstNAme + " LAstName " + lastName + " emailId " + emailId + "Mobile " + Mobile);
		
		edtFname.setText(FirstNAme);
		edtLname.setText(lastName);
		edtEmailID.setText(emailId);
		edtPassword.setText("Enter new Password");
		
		String img_url = DBAdpter.fetch_UserDetail_data.get(0)
				.getStrore_profile_image().toString().trim();
		URL url = null;
		try {
			url = new URL(img_url);
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

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			Log.v("log", " Above HoneyComb ");
			
			imguser1 = (CircularImageView)rootView.findViewById(R.id.img_user_image);
			imguser1.setImageBitmap(bmp);
			imguser1.setBorderColor(getResources().getColor(R.color.GrayLight));
			imguser1.setBorderWidth(0);
		} else {
			Log.v("log", " Below HoneyComb ");

			imgUser = (ImageView)rootView.findViewById(R.id.img_user_image);
			imgUser.setImageBitmap(bmp);
		}
		
		TextView btnTakeUserPhoto = (TextView)rootView.findViewById(R.id.txt_change_profile);
		
		btnTakeUserPhoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				Intent cameraAct = new Intent(getActivity(),CameraActivity.class);
				startActivityForResult(cameraAct, 1);				
			}
		});
		
		Button btnUpdateProfile = (Button)rootView.findViewById(R.id.btn_update_profile);
		
		btnUpdateProfile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		
				String strFname = edtFname.getText().toString();
				String strLname = edtLname.getText().toString();
				String strEmail = edtEmailID.getText().toString();
				String strPassword = edtPassword.getText().toString();
				
				Log.v("log"," profile UserID --> " + userId);
				
				ArrayList<UserInfo_dto>  result_list =  DBAdpter.updateUserInfo(userId,strFname,strLname,strEmail,strPassword);
				Log.v("log", " resultMesssage --> " + result_list.get(0).getMsg());
				
				if(result_list.get(0).getMsg().equals("User information successfully updated"))
				{
					Toast.makeText(getActivity(), result_list.get(0).getMsg() , 1).show();
					
					String message = DBAdpter.updateUserImage(userId,base64string);
					
					Log.v("log"," message -->  "+ message);
					
					FragmentManager fm = getFragmentManager();
					FragmentTransaction fragmentTransaction = fm
							.beginTransaction();
					HomeFragment fm2 = new HomeFragment();
					fragmentTransaction.replace(R.id.relProfileFraLayout,fm2, "HELLO");
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

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					
					imguser1.setImageBitmap(resizedBitmap);
					imguser1.setScaleType(ScaleType.CENTER);
					imguser1.setBorderColor(getResources().getColor(R.color.GrayLight));
					imguser1.setBorderWidth(0);
					
				} else {
					imgUser.setImageBitmap(resizedBitmap);
					imgUser.setScaleType(ScaleType.CENTER);
				}
				
				base64string = Base64.encodeBytes(byteArrayimage);
			}
		}
	}
	
}
