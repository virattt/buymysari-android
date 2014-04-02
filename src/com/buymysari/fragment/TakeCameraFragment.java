package com.buymysari.fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.buymysari.R;
import com.buymysari.camera.CameraPreview;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class TakeCameraFragment extends Fragment {
	Camera mCamera = null;
	private CameraPreview mCameraPreview;

	protected static final int MEDIA_TYPE_IMAGE = 0;
	static String FilePAth = "";
	Button takePicture;
	static String base64string = "";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final View rootView = inflater.inflate(R.layout.camerafragment,
				container, false);

		mCamera = getCameraInstance();

		Log.v("log_tag", "mCamera :: " + mCamera);

		mCameraPreview = new CameraPreview(getActivity(), mCamera);
		FrameLayout preview = (FrameLayout) rootView
				.findViewById(R.id.camera_preview_fragment);

		preview.addView(mCameraPreview);

		takePicture = (Button) rootView
				.findViewById(R.id.btnTakePicturefragment);
		takePicture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				mCamera.takePicture(null, null, mPictureframent);

			}
		});

		return rootView;

	}

	public boolean checkCameraHardware(Context context) {
		if (context.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)) {
			// this device has a camera
			return true;
		} else {
			// no camera on this device
			return false;
		}
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
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				"MyCameraAppFragment");

		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("MyCameraApp", "failed to create directory");
				return null;
			}
		}
		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());

		FilePAth = mediaStorageDir.getPath() + File.separator + "IMG_fragment_"
				+ timeStamp + ".jpg";

		Log.v("log", " FilePAth " + FilePAth);

		File mediaFile;
		mediaFile = new File(mediaStorageDir.getPath() + File.separator
				+ "IMG_fragment_" + timeStamp + ".jpg");

		return mediaFile;
	}

	PictureCallback mPictureframent = new PictureCallback() {
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

				
				FragmentManager fm = getFragmentManager();

				FragmentTransaction fragmentTransaction = fm.beginTransaction();
				SetPictureImageFragment fm2 = new SetPictureImageFragment();
				fragmentTransaction.replace(R.id.relative_camerafragment_id,
						fm2, "HELLO");
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();
				Bundle bundle = new Bundle();
				bundle.putByteArray("position", data);
				fm2.setArguments(bundle);
				mCamera.startPreview();

			} catch (FileNotFoundException e) {

			} catch (IOException e) {
			}
		}
	};

}
