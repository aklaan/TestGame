package com.example.testgame;

import com.example.testgame.gamecomponents.OpenGLActivity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends OpenGLActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		// on définit une liste des composants de jeu
		mGLSurfaceView.setRenderer(new GLES20RendererScene01(this));
		
		
	}

	protected void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
	}
	

}
