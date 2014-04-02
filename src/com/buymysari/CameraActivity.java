package com.buymysari;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
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
	Button takePicture;
	static String base64string = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_preview);

		mCamera = getCameraInstance();
		mCameraPreview = new CameraPreview(CameraActivity.this, mCamera);
		FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
		preview.addView(mCameraPreview);

		takePicture = (Button) findViewById(R.id.btnTakePicture);
		takePicture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mCamera.takePicture(null, null, mPicture);
			}
		});
	}

	@Override
	protected void onDestroy() {
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
		Camera camera = null;
		try {
			camera = Camera.open();
		} catch (Exception e) {
			// cannot get camera or does not exist
			releaseCamera();
		}
		return camera;
	}

	private static File getOutputMediaFile() {
		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
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
		mediaFile = new File(mediaStorageDir.getPath() + File.separator
				+ "IMG_" + timeStamp + ".jpg");

		return mediaFile;
	}

	PictureCallback mPicture = new PictureCallback() {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			File pictureFile = getOutputMediaFile();
			if (pictureFile == null) {
				return;
			}
			try {

				FileOutputStream fos = new FileOutputStream(pictureFile);
				fos.write(data);
				fos.close();

				Log.v("log_tag", "fos" + fos);
				// mCamera.startPreview();
				Intent returnIntent = new Intent();
				returnIntent.putExtra("data", data);
				setResult(RESULT_OK, returnIntent);
				finish();

			} catch (FileNotFoundException e) {

			} catch (IOException e) {
			}
		}
	};

	public void onBackPressed() {
		Intent returnIntent = new Intent();
		returnIntent.putExtra("path", FilePAth);
		setResult(RESULT_OK, returnIntent);
		finish();
	};
}
