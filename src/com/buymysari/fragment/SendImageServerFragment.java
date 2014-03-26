package com.buymysari.fragment;

import com.buymysari.DBAdpter;
import com.buymysari.MarketPlaceActivity;
import com.buymysari.MyApplication;
import com.buymysari.R;
import com.buymysari.SegmentedRadioGroup;
import com.buymysari.SegmentedRadioGroupMale;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class SendImageServerFragment extends Fragment {
	SegmentedRadioGroup segmentText;
	SegmentedRadioGroupMale segmentTextMale;
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
	private ProgressDialog progress;

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
	//	view.setFocusableInTouchMode(true);
	//	view.requestFocus();
		bundle = this.getArguments();
		path = bundle.getByteArray("position");
		segmentText = (SegmentedRadioGroup) view
				.findViewById(R.id.segment_text);
		segmentTextMale = (SegmentedRadioGroupMale) view
				.findViewById(R.id.segment_text_male);
		sendImg = (Button) view.findViewById(R.id.btn_send_image);
		edt_txt = (EditText) view.findViewById(R.id.edt_text_store_name);
		app = (MyApplication) getActivity().getApplicationContext();
		

		sendImg.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				int segTxt = segmentText.getCheckedRadioButtonId();
				RadioButton radiocatButton = (RadioButton) view
						.findViewById(segTxt);
				int segmentTextMaleTxt = segmentTextMale
						.getCheckedRadioButtonId();
				RadioButton radioSexButton = (RadioButton) view
						.findViewById(segmentTextMaleTxt);

				b = BitmapFactory.decodeByteArray(path, 0, path.length);
				base64string = Base64.encodeToString(path, Base64.DEFAULT);
				
				if (radiocatButton.getText().toString().equals("S")) {
					cat_id = "4";

				} else if (radiocatButton.getText().toString().equals("C")) {
					cat_id = "1";
				} else if (radiocatButton.getText().toString().equals("A")) {
					cat_id = "5";
				}
				
				
				if (radioSexButton.getText().toString().equals("M")) {
					gender = "Male";

				} else if (radioSexButton.getText().toString().equals("F")) {
					gender = "Female";
				}

				progress = new ProgressDialog(getActivity());
				progress.setMessage("Loading...");
				new SendImageServerTask(progress).execute("Home");
				
			}
		});

		return view;
	}

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
					"bags", cat_id, base64string, gender);
			Log.v("log_tag", " Msg "+ msg);
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
			fragmentTransaction.replace(R.id.relative_sendimage_send,fm2, "HELLO");
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();
			
			MarketPlaceActivity.mainLayout.toggleMenu();
			MarketPlaceActivity.storeOptions.setVisibility(View.VISIBLE);
			MarketPlaceActivity.btnCreateStore.setVisibility(View.GONE);
			

		}

	}

}
