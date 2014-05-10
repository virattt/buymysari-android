package com.buymysari.fragment;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.buymysari.CameraActivity;
import com.buymysari.R;

public class SetPictureImageFragment extends Fragment {

	ImageView img;
	Bundle bundle;
	byte[] path;
	byte[] byteArrayimage;
	Button conform;
	float x;
	
	Button chnagepicture;

	int moveX , moveY;
	
	Bitmap resizedBitmap;
	Bitmap b;
	Matrix matrix;
	int height;
	int width;
	
	ImageView crop_select_img;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		View view = inflater.inflate(R.layout.capturepicturefragment, null);

		Log.v("log_tag", "SetPictureImageFragment");
		bundle = this.getArguments();
		Uri imageURI = null;
		if(bundle.getString("image_from") != null){
			Log.v("image+from","in if");
			imageURI = Uri.parse(bundle.getString("data"));
			Log.v("image+from",bundle.getString("data"));
			InputStream iStream;
	        byte[] inputData = null;
			try {
				iStream = getActivity().getContentResolver().openInputStream(imageURI);
				Log.v("image","parsed uri ");
				inputData = getBytes(iStream);
				path = inputData;
				Log.v("log"," selected Gallary Image ByteArray --> " + inputData);
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			path = bundle.getByteArray("data");
		}
		
		Log.v("log_tag", "SetPictureImageFragment ::: Path :: " + path);
		img = (ImageView) view.findViewById(R.id.camera_preview_fragment_imageview);
		chnagepicture = (Button) view.findViewById(R.id.btnChangePicture);
		conform = (Button) view.findViewById(R.id.conform);

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPurgeable = true; // inPurgeable is used to free up
									// memory while required

		b = BitmapFactory.decodeByteArray(path, 0, path.length, options);

		chnagepicture.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), CameraActivity.class);
				intent.putExtra("ImageType", "AddPicture");
				getActivity().startActivity(intent);
			}
		});

		crop_select_img =(ImageView)view.findViewById(R.id.crop_select_img);
		
		crop_select_img.setOnTouchListener(new OnTouchListener()
		{
		    PointF DownPT = new PointF(); // Record Mouse Position When Pressed Down
		    PointF StartPT = new PointF(); // Record Start Position of 'img'

		    @Override
		    public boolean onTouch(View v, MotionEvent event)
		    {
		        int eid = event.getAction();
		        switch (eid)
		        {
		            case MotionEvent.ACTION_MOVE :
		                PointF mv = new PointF( event.getX() - DownPT.x, event.getY() - DownPT.y);
		                crop_select_img.setX(0);
		                Log.v("log"," move y--->  " + (StartPT.y+mv.y) + " move  -->" + (img.getY()+img.getHeight()));
		                if((StartPT.y+mv.y)>=img.getY() && (StartPT.y+mv.y+crop_select_img.getHeight())<=(img.getY()+img.getHeight())){
		                	crop_select_img.setY((int)(StartPT.y+mv.y));
		                }
		                
		                StartPT = new PointF( crop_select_img.getX(), crop_select_img.getY() );
		                break;
		            case MotionEvent.ACTION_DOWN :
		                DownPT.x = event.getX();
		                DownPT.y = event.getY();
		                
		                Log.v("log"," down x--->  " + event.getX() + " y--> " + event.getY());
		                
		                StartPT = new PointF( crop_select_img.getX(), crop_select_img.getY() );
		                break;
		            case MotionEvent.ACTION_UP :
		                // Nothing have to do
		                break;
		            default :
		                break;
		        }
		        return true;
		    }
		});
		
		
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		
		int device_width = displaymetrics.widthPixels;
		
		Log.v("log", " device width " + device_width);
		
		width = b.getWidth();
		height = b.getHeight();
		
		int newWidth = 500;
		int newHeight = 500;
		
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		
		matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		
		
			int rotation = getActivity().getWindowManager().getDefaultDisplay()
					.getRotation();
			Log.v("log", " rotation " + rotation);

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
			Log.v("log"," width " + width + " height " + height);
			
			resizedBitmap = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(),matrix, true);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			//resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
			byteArrayimage = stream.toByteArray();

			img.setScaleType(ScaleType.FIT_XY);
			img.setImageBitmap(resizedBitmap);
		

		conform.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				moveX = (int)crop_select_img.getX();
				moveY = (int)crop_select_img.getY();
				
				float scaleWidth = (float) 1.0;
			    float scaleHeight = (float) 1.0;
			    // create a matrix for the manipulation
			    Matrix matrix = new Matrix();
			    // resize the bit map
			    matrix.postScale(scaleWidth, scaleHeight);
				Bitmap bitmap = Bitmap.createBitmap(resizedBitmap , 0, (int)crop_select_img.getY()*(int)(resizedBitmap.getHeight()/img.getHeight()), resizedBitmap.getWidth(), crop_select_img.getHeight(), matrix, false);
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
				byteArrayimage = stream.toByteArray();
				Log.v("log"," Move x--->  " + moveX + " y--> " + moveY + " byteArrayimage " + byteArrayimage);
				FragmentManager fm = getChildFragmentManager();
				FragmentTransaction fragmentTransaction = fm.beginTransaction();
				SendImageServerFragment fm2 = new SendImageServerFragment();
				// CreateStoreFragment fm2 = new CreateStoreFragment();
				fragmentTransaction.replace(
						R.id.relative_cameraimageview_fragment, fm2, "HELLO");
				fragmentTransaction.addToBackStack(null);
				// fragmentTransaction.commit();
				fragmentTransaction.commitAllowingStateLoss();
				Bundle bundle = new Bundle();
				bundle.putByteArray("position", byteArrayimage);
				fm2.setArguments(bundle);
			}
		});
		return view;
	}
	
	public byte[] getBytes(InputStream inputStream) throws IOException {
	      ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
	      int bufferSize = 1024;
	      byte[] buffer = new byte[bufferSize];

	      int len = 0;
	      while ((len = inputStream.read(buffer)) != -1) {
	        byteBuffer.write(buffer, 0, len);
	      }
	      return byteBuffer.toByteArray();
	    }
}
