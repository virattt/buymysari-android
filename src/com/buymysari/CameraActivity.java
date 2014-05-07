package com.buymysari;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;

import com.buymysari.camera.CameraPreview;

public class CameraActivity extends Activity {

	Camera mCamera;
	CameraPreview mCameraPreview;
	protected static final int MEDIA_TYPE_IMAGE = 0;
	static String FilePAth = "";
	Button takePicture , btnGlr , btnCancelCamera;
	static String base64string = "";
	String ImageType;

	final int RESULT_LOAD_IMAGE = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_preview);

		mCamera = getCameraInstance();

		mCamera.startPreview();
		mCameraPreview = new CameraPreview(CameraActivity.this, mCamera);
		FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
		preview.addView(mCameraPreview);

		takePicture = (Button) findViewById(R.id.btnTakePicture);
		takePicture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mCamera.takePicture(null, null,null, mPicture);
			}
		});

		Intent intent = getIntent();
		
		if (intent.hasExtra("ImageType")) {
			ImageType = getIntent().getStringExtra("ImageType").toString();
			
			Log.v("log", " ImageType in Camera Activity -- >  " + ImageType);
		}
		
		btnGlr = (Button)findViewById(R.id.btnGallary);
		btnGlr.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, RESULT_LOAD_IMAGE );
				
			}
		});
		
		btnCancelCamera = (Button)findViewById(R.id.btnCancelCamera);
		btnCancelCamera.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				Intent intent = new Intent(getApplication(), MarketPlaceActivity.class);
				startActivity(intent);
			}
		});
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		releaseCamera();
	}

	private void releaseCamera() {
		if (mCamera != null) {
			mCamera.release(); // release the camera for other applications
			mCamera = null;
		}
	}

	private Camera getCameraInstance() {

		try {
			Log.v("log_tag", "camera try:::" + mCamera);
			mCamera = Camera.open();

		} catch (Exception e) {
			// cannot get camera or does not exist
			Log.v("log_tag", "camera catch:::" + mCamera);
			releaseCamera();
		}
		return mCamera;
	}

	private static File getOutputMediaFile() {
		
		File mediaStorageDir = new File(
				Environment.getExternalStorageDirectory(),
				"MyCameraApp");
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("MyCameraApp", "failed to create directory");
				return null;
			}
		}
		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());

		FilePAth = mediaStorageDir.getPath() + File.separator + "IMG_"
				+ timeStamp + ".jpg";

		Log.v("log", " FilePAth " + FilePAth);

		File mediaFile;
		mediaFile = new File(FilePAth);
		
		return mediaFile;
	}

	 PictureCallback mPicture = new PictureCallback() {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			/*File pictureFile = getOutputMediaFile();
			if (pictureFile == null) {
				
				ContextWrapper cw = new ContextWrapper(getApplicationContext());
		         // path to /data/data/yourapp/app_data/imageDir
		        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
		        // Create imageDir
		        pictureFile =new File(directory,"profile.jpg");
				
			//	return;
			}
			
			try {

				FileOutputStream fos = new FileOutputStream(pictureFile);
				fos.write(data);
				fos.close();

				// mCamera.startPreview();
			} catch (FileNotFoundException e) {

			} catch (IOException e) {
			}*/
			
			if (ImageType.equals("AddPicture")) {
				Intent i = new Intent(CameraActivity.this,MarketPlaceActivity.class);
				i.putExtra("data", data);
				startActivity(i);
			} else {
				Intent returnIntent = new Intent();
				returnIntent.putExtra("data", data);
				setResult(RESULT_OK, returnIntent);
				CameraActivity.this.finish();
			}
		}
	};
	
	 /*private PictureCallback mPicture = new PictureCallback() {

	      
		    @Override
		    public void onPictureTaken(byte[] data, Camera camera) {

		    	File pictureFile = null;
		    	String imPath = null;
		    	
		    	String state = Environment.getExternalStorageState();
		    	Log.v("log", " state  " + state);
		    
		    	try{
		        	pictureFile = new File(getApplicationContext().getFilesDir(),"/prvteyes.jpg");
		            imPath = getApplicationContext().getFilesDir()+"/prvteyes.jpg";
		            Log.d("Test", "sdcard mounted readonly" + imPath);
		        }catch(Exception e){
		        	Log.v("log","Error " + e.toString());
		        
		        	if (Environment.MEDIA_MOUNTED.equals(state)) {
			            Log.d("Test", "sdcard mounted and writable");
			       
			            File path = Environment.getExternalStoragePublicDirectory(
				                   Environment.DIRECTORY_PICTURES);
				           //File file = new File(path, "DemoPicture.jpg");
				        
				         pictureFile = new File(path + "/prvteyes.jpg");
				         imPath =  path + "/prvteyes.jpg";
				//     	 Log.v("log"," image path : : : : :   " + imPath);
			        
			        }
		        }
		        
		        
		       try {
		            FileOutputStream fos = new FileOutputStream(pictureFile);
		            fos.write(data);
		            fos.close();
		            
		            if (ImageType.equals("AddPicture")) {
						Intent i = new Intent(getBaseContext(),MarketPlaceActivity.class);
						i.putExtra("data", data);
						startActivity(i);
					} else {
						Intent returnIntent = new Intent();
						returnIntent.putExtra("data", data);
						setResult(RESULT_OK, returnIntent);
						CameraActivity.this.finish();
					}

		            
		        } catch (FileNotFoundException e) {
		 //           Log.v("log", "File not found: " + e.getMessage());
		        } catch (IOException e) {
		            Log.d("log", "Error accessing file: " + e.getMessage());
		        }
		        
		    }
		};
		*/


	public void onBackPressed() {
		Intent returnIntent = new Intent();
		returnIntent.putExtra("path", FilePAth);
		setResult(RESULT_OK, returnIntent);
		finish();
	};
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
          /*  String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            Log.v("log"," picturePath --> selected Gallary Image path --> " + picturePath);
            cursor.close();*/
            
            InputStream iStream;
            byte[] inputData = null;
            
			try {
				iStream = getContentResolver().openInputStream(selectedImage);
				inputData = getBytes(iStream);
				
				Log.v("log"," selected Gallary Image ByteArray --> " + inputData);
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
			if (ImageType.equals("AddPicture")) {
				Intent i = new Intent(getBaseContext(),MarketPlaceActivity.class);
				i.putExtra("data", inputData);
				i.putExtra("image_from", "Gallary");
				startActivity(i);
			} else {
				Intent returnIntent = new Intent();
				returnIntent.putExtra("data", inputData);
				returnIntent.putExtra("image_from", "Gallary");
				setResult(RESULT_OK, returnIntent);
				CameraActivity.this.finish();
			}
			//    ImageView imageView = (ImageView) findViewById(R.id.imgView);
            //    imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
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
