package com.buymysari.fragment;

import com.buymysari.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class SetPictureImageFragment extends Fragment {

	ImageView img;
	Bundle bundle;
	byte[] path;
	Button btnTakecanclePicture;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		View view = inflater.inflate(R.layout.capturepicturefragment, null);
		
		Log.v("log_tag","SetPictureImageFragment");
		bundle = this.getArguments();
		path = bundle.getByteArray("position");
		
		Log.v("log_tag","SetPictureImageFragment ::: Path :: "+path);
		img = (ImageView) view.findViewById(R.id.camera_preview_fragment_imageview);
		btnTakecanclePicture=(Button)view.findViewById(R.id.btnTakecanclePicture);
		//img.setScaleType(ImageView.ScaleType.FIT_XY);

		Bitmap b = BitmapFactory.decodeByteArray(path, 0,path.length);
		Log.v("log_tag","SetPictureImageFragment ::: bitmap :: "+b);
		
		img.setImageBitmap(b);
		img.setScaleType(ImageView.ScaleType.FIT_XY);
		
	/*	btnTakecanclePicture.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FragmentManager fm = getFragmentManager();
				FragmentTransaction fragmentTransaction = fm.beginTransaction();
				TakeCameraFragment fm2 = new TakeCameraFragment();
				fragmentTransaction.replace(R.id.relative_cameraimageview_fragment, fm2,
						"HELLO");
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();
				
				
				/*FragmentManager fm = getFragmentManager();
			    FragmentTransaction fragmentTransaction = fm
			      .beginTransaction();
			    CreateStoreFragment fm2 = new CreateStoreFragment();
			    fragmentTransaction.replace(R.id.relative_cameraimageview_fragment,
			      fm2, "HELLO");
			    fragmentTransaction.addToBackStack(null);
			    fragmentTransaction.commit();
			}
		});
		*/

		return view;
	}
}
