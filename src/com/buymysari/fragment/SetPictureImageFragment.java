package com.buymysari.fragment;

import com.buymysari.Base64;
import com.buymysari.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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
	Button conform;
	float x;
	Bitmap b;
	

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
		conform=(Button)view.findViewById(R.id.conform);
		

		 b = BitmapFactory.decodeByteArray(path, 0,path.length);
		Log.v("log_tag","SetPictureImageFragment ::: bitmap :: "+b);
		
		int width = b.getWidth();
        int height = b.getHeight();


        int newWidth = 500;

        int newHeight  = 500;

        // calculate the scale - in this case = 0.4f

         float scaleWidth = ((float) newWidth) / width;

         float scaleHeight = ((float) newHeight) / height;

         Matrix matrix = new Matrix();

         matrix.postScale(scaleWidth, scaleHeight);
         matrix.postRotate(90);

         Bitmap resizedBitmap = Bitmap.createBitmap(b, 0, 0,width, height, matrix, true);

         img.setScaleType(ScaleType.CENTER);
         img.setImageBitmap(resizedBitmap);
		
        
		/*img.setImageBitmap(b);
		img.setScaleType(ImageView.ScaleType.FIT_XY);*/
		
		
		conform.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FragmentManager fm = getChildFragmentManager();
				FragmentTransaction fragmentTransaction = fm.beginTransaction();
				SendImageServerFragment fm2 = new SendImageServerFragment();
				// CreateStoreFragment fm2 = new CreateStoreFragment();
				fragmentTransaction.replace(R.id.relative_cameraimageview_fragment, fm2,
						"HELLO");
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();
				Bundle bundle = new Bundle();
				bundle.putByteArray("position", path);
				fm2.setArguments(bundle);
				
				
			}
		});
		

		return view;
	}
	
	
}
