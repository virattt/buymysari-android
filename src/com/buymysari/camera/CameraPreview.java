package com.buymysari.camera;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.view.View.MeasureSpec;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
	private SurfaceHolder mSurfaceHolder;
	private Camera mCamera;
	 Boolean isPreviewRunning = true; 
	

	// Constructor that obtains context and camera
	 public CameraPreview(Context context, Camera camera) {
	        super(context);
	        mCamera = camera;

	        // Install a SurfaceHolder.Callback so we get notified when the
	        // underlying surface is created and destroyed.
	        mSurfaceHolder = getHolder();
	        mSurfaceHolder.addCallback(this);
	        // deprecated setting, but required on Android versions prior to 3.0
	        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	    }

	@Override
	public void surfaceCreated(SurfaceHolder surfaceHolder) {
		/*try {
			mCamera.setPreviewDisplay(surfaceHolder);
			mCamera.startPreview();
			//mCamera.setDisplayOrientation(90);
		} catch (IOException e) {
			// left blank for now
		}*/
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
		//mCamera.stopPreview();
	//	mCamera.release();
		 if (mCamera != null)
		    {
		        mCamera.stopPreview();
		        mCamera.release();
		    }
	}

	/*@Override
	public void surfaceChanged(SurfaceHolder surfaceHolder, int format,
			int width, int height) {
		// start preview with new settings
		
		try {
			mCamera.stopPreview();
			mCamera.setPreviewDisplay(surfaceHolder);
			mCamera.startPreview();
			//mCamera.setDisplayOrientation(90);
		} catch (Exception e) {
			// intentionally left blank for a test
		}
	}*/
	
	  public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
	        // If your preview can change or rotate, take care of those events here.
	        // Make sure to stop the preview before resizing or reformatting it.
	    	
	        if (mSurfaceHolder.getSurface() == null){
	          // preview surface does not exist
	          return;
	        }

	        // stop preview before making changes
	        if(isPreviewRunning)
	        {
	          mCamera.stopPreview();
	        }
	        
	        Parameters parameters = mCamera.getParameters();

	        WindowManager windo =  (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
	    	Display display = windo.getDefaultDisplay();
	        
	    	if(display.getRotation() == Surface.ROTATION_0)
	        {
	            parameters.setPreviewSize(h,w);          
	            mCamera.setDisplayOrientation(90);
	            
	        }

	        if(display.getRotation() == Surface.ROTATION_90)
	        {
	            parameters.setPreviewSize(w, h);            
	            
	        }

	        if(display.getRotation() == Surface.ROTATION_180)
	        {
	            parameters.setPreviewSize(h, w);  
	         //   mCamera.setDisplayOrientation(270);
	            
	        }

	        if(display.getRotation() == Surface.ROTATION_270)
	        {
	            parameters.setPreviewSize(w, h);
	            mCamera.setDisplayOrientation(180);
	            
	        }
	        // set preview size and make any resize, rotate or
	        // reformatting changes here

	        // start preview with new settings
	        
//	        mCamera.setParameters(parameters);
	        try {
	            mCamera.setPreviewDisplay(mSurfaceHolder);
	            mCamera.startPreview();
	            isPreviewRunning = true;

	        } catch (Exception e){
	            Log.d("log", "Error starting camera preview: " + e.getMessage());
	        }
	    }
	  
	  @Override
	    protected void onMeasure(int widthSpec, int heightSpec) {
	    	int previewWidth = MeasureSpec.getSize(widthSpec);
	    	int previewHeight = MeasureSpec.getSize(heightSpec);
	    	
	    	//Get the padding of the border background
	    	int hPadding = getPaddingLeft() + getPaddingRight();
	    	int vPadding = getPaddingTop() + getPaddingBottom();
	    	
	    	//Resize the preview frame with correct aspect ratio
	    	previewWidth += hPadding;
	    	previewHeight -= vPadding;
	    	
	    	//Add the padding of the border.
	    	previewWidth += hPadding;
	    	previewHeight += vPadding;
	    	
	    	//Ask children to follow the new preview dimension
	    	super.onMeasure(MeasureSpec.makeMeasureSpec(previewWidth, MeasureSpec.EXACTLY),
	    			MeasureSpec.makeMeasureSpec(previewHeight, MeasureSpec.EXACTLY));
	    }
}