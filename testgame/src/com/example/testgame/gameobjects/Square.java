package com.example.testgame.gameobjects;

import com.example.testgame.OpenGLActivity;

public class Square extends Rectangle {

	public Square(){
		super();
		
		
	}
	@Override
	public void onUpdate(OpenGLActivity activity){
			if (activity.mGLSurfaceView.touched){
				this.rotate(0.5f);		
			}
		
		
	}
	
}
