package com.example.testgame.gamecomponents;

import java.util.ArrayList;

import com.example.testgame.GLES20RendererScene01;
import com.example.testgame.MySurfaceView;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class OpenGLActivity extends Activity {

	public static float DEFAULT_ZOOM_FACTOR = 2.f;
	// ! OpenGL SurfaceView
	public MySurfaceView mGLSurfaceView;
	


	
	private float mXScreenLimit;
	private float mYScreenLimit;
	private float mZoomFactor;

	
	
	public MySurfaceView getSurfaceView(){
		return this.mGLSurfaceView;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mZoomFactor = DEFAULT_ZOOM_FACTOR;

		if (!isOGLES20Compatible()) {
			// C++ Reflex sorry
			mGLSurfaceView = null;
			showOGLES20ErrorDialogBox();
			return;
		}

		// We don't use Layout. But you can.
		// create an OpenGLView
		mGLSurfaceView = new MySurfaceView(this);

		mGLSurfaceView.setDebugFlags(GLSurfaceView.DEBUG_CHECK_GL_ERROR);
		mGLSurfaceView.setEGLContextClientVersion(2);
	//	mGLSurfaceView.setRenderer(new GLES20RendererScene01(this));

		setContentView(mGLSurfaceView);

		


	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mGLSurfaceView != null) {
			mGLSurfaceView.onResume();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mGLSurfaceView != null) {
			mGLSurfaceView.onPause();
		}
	}

	/* This method verify that your Phone is compatible with OGLES 2.x */
	private boolean isOGLES20Compatible() {
		ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		ConfigurationInfo info = am.getDeviceConfigurationInfo();
		return (info.reqGlEsVersion >= 0x20000);
	}

	/* show an error message */
	private void showOGLES20ErrorDialogBox() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("No OpenGL ES 2.0 GPU Found!").setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						finish();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}



	public float getXScreenLimit() {
		return mXScreenLimit;
	}

	public void setXScreenLimit(float mXScreenLimit) {
		this.mXScreenLimit = mXScreenLimit;
	}

	public float getYScreenLimit() {
		return mYScreenLimit;
	}

	public void setYScreenLimit(float mYScreenLimit) {
		this.mYScreenLimit = mYScreenLimit;
	}

	public float getZoomFactor() {
		return mZoomFactor;
	}

	public void setZoomFactor(float newFactor) {

		if (newFactor != 0) {
			this.mZoomFactor = newFactor;
		}

	}

}
