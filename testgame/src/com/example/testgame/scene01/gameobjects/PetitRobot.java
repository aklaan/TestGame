

package com.example.testgame.scene01.gameobjects;



import com.example.testgame.R;
import com.example.testgame.gamecomponents.DrawingMode;
import com.example.testgame.gamecomponents.GameObject;
import com.example.testgame.gamecomponents.Rectangle2D;

public class PetitRobot extends Rectangle2D {

    private int sens = 1;
	public PetitRobot() {

		super(DrawingMode.FILL);
		this.setTagName(R.string.petit_robot);
		this.isStatic=false;		
	}

	@Override
	public void onUpdate(){
		float limit_y = this.getScene().getHeight();
		//Log.i("debug",String.valueOf(activity.mGLSurfaceView.getHeight()));
		//Log.i("debug",String.valueOf(i));
		
		float inc = 0.f;
		
		if (this.getCoordY()>limit_y || this.getCoordY()<-limit_y){
			sens = sens*-1;
		}  
		
		inc=inc * sens;
		
		this.Y = Y+inc;

	
		if (!this.mCollideWithList.isEmpty()){
			this.setheight(this.getHeight()+5.f);
			if (this.getHeight() > 700){this.setheight(0);}
		}
		//test des colisions
				for (GameObject go : this.mCollideWithList) {
					//Log.i("petitrobot", "i'm collide with : " + go.getTagName());

				}

	
	
	}
	
}
