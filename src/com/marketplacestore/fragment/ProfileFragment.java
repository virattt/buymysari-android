package com.marketplacestore.fragment;

import android.content.Intent;
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
import android.widget.Toast;
import com.marketplacestore.DBAdpter;
import com.marketplacestore.MarketPlaceActivity;
import com.marketplacestore.MyApplication;
import com.marketplacestore.R;

public class ProfileFragment extends Fragment{
	
	EditText edtFname ,edtLname ,edtEmailID ,edtPassword;
	private String userId;
	MyApplication app;
	
	
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
		
		Log.v("log", "userId" + userId + " FirstNAme : " + FirstNAme + " LAstName " + lastName + " emailId " + emailId + "Mobile " + Mobile);
		
		edtFname.setText(FirstNAme);
		edtLname.setText(lastName);
		edtEmailID.setText(emailId);
		edtPassword.setText("Enter new Password");
		
		Button btnCreateStore = (Button)rootView.findViewById(R.id.btnProfileCreateStore);
		Button btnCreateupdate = (Button)rootView.findViewById(R.id.btnProfileUpdate);
		
		btnCreateStore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				FragmentManager fm = getFragmentManager();
				FragmentTransaction fragmentTransaction = fm
						.beginTransaction();
				CreateStoreFragment fm2 = new CreateStoreFragment();
				fragmentTransaction.replace(R.id.relProfileFraLayout,
						fm2, "HELLO");
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();
				
			}
		});
		
		btnCreateupdate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		
				String strFname = edtFname.getText().toString();
				String strLname = edtLname.getText().toString();
				String strEmail = edtEmailID.getText().toString();
				String strPassword = edtPassword.getText().toString();
				
				Log.v("log"," profile UserID --> " + userId);
				
				String  result_message =  DBAdpter.updateUserInfo(userId,strFname,strLname,strEmail,strPassword);
				Log.v("log", " resultMesssage --> " + result_message);
				
				if(result_message.equals("User information successfully updated"))
				{
					Toast.makeText(getActivity(), result_message , 1).show();
					
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
}
