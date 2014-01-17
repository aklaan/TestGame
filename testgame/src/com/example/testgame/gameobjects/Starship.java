package com.example.testgame.gameobjects;

import com.example.testgame.gamecomponents.OpenGLActivity;
import com.example.testgame.gamecomponents.Rectangle2D;

public class Starship extends Rectangle2D {

	public Starship(){
		super();
		
		
	}
	@Override
	public void onUpdate(OpenGLActivity activity){
			if (activity.mGLSurfaceView.touched){
				this.rotate(0.5f);		
			}
		
		
	}
	
}
