package com.buymysari.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.buymysari.DBAdpter;
import com.buymysari.MarketPlaceActivity;
import com.buymysari.MyApplication;
import com.buymysari.R;
import com.buymysari.R.color;
import com.buymysari.SegmentedRadioGroup;
import com.buymysari.SegmentedRadioGroupMale;

public class SendImageServerFragment extends Fragment {

	Button sendImg;
	EditText edt_txt;
	MyApplication app;
	View view;
	Bundle bundle;
	byte[] path;
	String base64string = "";
	Bitmap b;
	String cat_id = "";
	String gender = "";
	String item_edt_text="";
	private ProgressDialog progress;
	InputMethodManager mgr;
	ToggleButton button_cloth_send_img, button_shoes_send_img,
			button_acce_send_img;
	ToggleButton button_one_male, button_one_female;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		view = inflater.inflate(R.layout.sendimageserver, null);

		bundle = this.getArguments();
		path = bundle.getByteArray("position");

		sendImg = (Button) view.findViewById(R.id.btn_send_image);
		edt_txt = (EditText) view.findViewById(R.id.edt_text_store_name);
		
		button_cloth_send_img = (ToggleButton) view
				.findViewById(R.id.button_cloth_send_img);
		button_shoes_send_img = (ToggleButton) view
				.findViewById(R.id.button_shoes_send_img);
		button_acce_send_img = (ToggleButton) view
				.findViewById(R.id.button_acce_send_img);
		button_one_male = (ToggleButton) view
				.findViewById(R.id.button_one_male);
		button_one_female = (ToggleButton) view
				.findViewById(R.id.button_one_female);
		edt_txt.requestFocus();
		
		mgr = (InputMethodManager) getActivity().getSystemService(
				Context.INPUT_METHOD_SERVICE);
		mgr.showSoftInput(edt_txt, InputMethodManager.SHOW_IMPLICIT);
		app = (MyApplication) getActivity().getApplicationContext();

		button_cloth_send_img.setOnCheckedChangeListener(changeChecker);
		button_shoes_send_img.setOnCheckedChangeListener(changeChecker);
		button_acce_send_img.setOnCheckedChangeListener(changeChecker);

		button_one_male.setOnCheckedChangeListener(changeCheckerMale);
		button_one_female.setOnCheckedChangeListener(changeCheckerMale);

		
		
		sendImg.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				hideKeybord(edt_txt);
				item_edt_text = edt_txt.getText().toString();

				// b = BitmapFactory.decodeByteArray(path, 0, path.length);
				base64string = Base64.encodeToString(path, Base64.DEFAULT);
				Log.v("log_tag", "Check the Null Value catid"+cat_id.equals(""));
				Log.v("log_tag", "Check the Null Value gender"+gender.equals(""));
				
				if (!cat_id.equals("") && !gender.equals("") && !item_edt_text.equals("")) {
					Log.v("log_tag", "Check the Null Value");
					progress = new ProgressDialog(getActivity());
					progress.setMessage("Loading...");
					new SendImageServerTask(progress).execute("Home");
					
				} else {
					
					Toast.makeText(getActivity(), "Plese Select Item",
							Toast.LENGTH_LONG).show();
				}

			}
		});

		return view;
	}

	

	OnCheckedChangeListener changeChecker = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked) {

				Log.v("log_tag", "buttonView ::: " + buttonView);
				if (buttonView == button_cloth_send_img) {
					Log.v("log_tag", "button_cloth_send_img ::: ");
					cat_id = "1";
					button_cloth_send_img.setChecked(false);
					button_cloth_send_img
							.setBackgroundResource(R.drawable.clothes);
					button_shoes_send_img
							.setBackgroundResource(R.drawable.shoes_active);
					button_acce_send_img
							.setBackgroundResource(R.drawable.accessories_active);

				}
				if (buttonView == button_shoes_send_img) {
					button_shoes_send_img.setChecked(false);
					cat_id = "4";
					Log.v("log_tag", "button_cloth_send_img ::: ");
					button_cloth_send_img
							.setBackgroundResource(R.drawable.clothes_active);
					button_shoes_send_img
							.setBackgroundResource(R.drawable.shoes);
					button_acce_send_img
							.setBackgroundResource(R.drawable.accessories_active);

				}
				if (buttonView == button_acce_send_img) {
					button_acce_send_img.setChecked(false);
					cat_id = "5";
					Log.v("log_tag", "button_acce_send_img ::: ");
					button_cloth_send_img
							.setBackgroundResource(R.drawable.clothes_active);
					button_shoes_send_img
							.setBackgroundResource(R.drawable.shoes_active);
					button_acce_send_img
							.setBackgroundResource(R.drawable.accessories);

				}

			}
		}
	};

	OnCheckedChangeListener changeCheckerMale = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked) {

				Log.v("log_tag", "buttonView ::: " + buttonView);
				if (buttonView == button_one_male) {
					Log.v("log_tag", "button_one_male ::: ");
					gender = "Male";
					button_one_male.setChecked(false);

					button_one_male.setBackgroundColor(Color
							.parseColor("#8e2124"));
					button_one_female.setBackgroundColor(Color
							.parseColor("#221010"));
				}
				if (buttonView == button_one_female) {
					button_one_female.setChecked(false);
					gender = "Female";
					Log.v("log_tag", "button_one_female ::: ");

					button_one_male.setBackgroundColor(Color
							.parseColor("#221010"));
					button_one_female.setBackgroundColor(Color
							.parseColor("#8e2124"));

				}
			}
		}
	};

	public class SendImageServerTask extends AsyncTask<String, Void, String> {

		public SendImageServerTask(ProgressDialog progress) {
			progress = progress;
		}

		public void onPreExecute() {
			progress.show();

		}

		@Override
		protected String doInBackground(String... arg) {

			String msg = DBAdpter.userUpdateImageStore(app.getStoreId(),
					item_edt_text, cat_id, base64string, gender);
			Log.v("log_tag", " Msg " + msg);
			return msg;
		}

		@Override
		protected void onPostExecute(String result) {
			// Create here your JSONObject...

			progress.dismiss();
			Toast.makeText(getActivity().getApplicationContext(), result,
					Toast.LENGTH_LONG).show();

			FragmentManager fm = getActivity().getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fm.beginTransaction();
			StoreProfileGridFragment fm2 = new StoreProfileGridFragment();
			fragmentTransaction.replace(R.id.relative_sendimage_send, fm2,
					"HELLO");
			fragmentTransaction.addToBackStack(null);
			// fragmentTransaction.commit();
			fragmentTransaction.commitAllowingStateLoss();
			// hideKeybord(edt_txt);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			layoutParams.setMargins(0, 70, 0, 0);
			MarketPlaceActivity.activityMain_content_fragment.setLayoutParams(layoutParams);
		}

	}

	public void hideKeybord(View view) {
		mgr.hideSoftInputFromWindow(view.getWindowToken(),
				InputMethodManager.RESULT_UNCHANGED_SHOWN);
	}

}
