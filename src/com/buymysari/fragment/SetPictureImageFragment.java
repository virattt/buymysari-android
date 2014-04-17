package com.buymysari.fragment;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.buymysari.R;

public class SetPictureImageFragment extends Fragment {

	ImageView img;
	Bundle bundle;
	byte[] path;
	byte[] byteArrayimage;
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
		

		
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPurgeable = true; // inPurgeable is used to free up
									// memory while required
		
		
		Bitmap b = BitmapFactory.decodeByteArray(path, 0,path.length,options);
		
		int width = b.getWidth();
        int height = b.getHeight();
        int newWidth = 500;
        int newHeight  = 500;
         float scaleWidth = ((float) newWidth) / width;

         float scaleHeight = ((float) newHeight) / height;

         Matrix matrix = new Matrix();

         matrix.postScale(scaleWidth, scaleHeight);
        
         int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
        
         int finalDegree = 0;
		   
		    if(rotation == 0) {
		    	
					finalDegree = 90;

		    }
		    
		    if(rotation == 1) {
		    	
		    		finalDegree = 270;
		    	
		    }
		    
		    if(rotation == 2) {
			   
			    	
			    
			    	finalDegree = 180;
			   
		    }
		    
		    if(rotation == 3) {
			    
			    	
			   
			    	finalDegree = 90;
			   
		    }
		   
         matrix.postRotate(finalDegree);

         Bitmap resizedBitmap = Bitmap.createBitmap(b, 0, 0,width, height, matrix, true);
         ByteArrayOutputStream stream = new ByteArrayOutputStream();
         resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
         byteArrayimage = stream.toByteArray();

         img.setScaleType(ScaleType.CENTER);
         img.setImageBitmap(resizedBitmap);
		
		
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
				//fragmentTransaction.commit();
				fragmentTransaction.commitAllowingStateLoss();
				Bundle bundle = new Bundle();
				bundle.putByteArray("position", byteArrayimage);
				fm2.setArguments(bundle);
				
			}
		});
		

		return view;
	}
	
}
