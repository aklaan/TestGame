package com.example.testgame.gameobjects;

import com.example.testgame.gamecomponents.OpenGLActivity;
import com.example.testgame.gamecomponents.Rectangle2D;

public class PetitRobot extends Rectangle2D {

    private int sens = 1;
	public PetitRobot() {

		super();

			}

	@Override
	public void onUpdate(OpenGLActivity activity){
		float limit_y = activity.getYScreenLimit();
		//Log.i("debug",String.valueOf(activity.mGLSurfaceView.getHeight()));
		//Log.i("debug",String.valueOf(i));
		
		float inc = 5.f;
		
		if (this.getCoordY()>limit_y || this.getCoordY()<-limit_y){
			sens = sens*-1;
		}  
		
		inc=inc * sens;
		
		this.translate(0,inc);
	}
	
}
