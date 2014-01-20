package com.example.testgame.gameobjects;

import android.util.Log;

import com.example.testgame.gamecomponents.GameObject;
import com.example.testgame.gamecomponents.OpenGLActivity;
import com.example.testgame.gamecomponents.Rectangle2D;

public class Starship extends Rectangle2D {

	public Starship() {
		super();
this.setTagName("starship");
this.isStatic=false;
	}

	@Override
	public void onUpdate(OpenGLActivity activity) {
		if (activity.mGLSurfaceView.touched) {
			this.rotate(0.5f);
		}
		
		for (GameObject go : this.mCollideWithList) {
			Log.i("starship", "i'm collide with : " + go.getTagName());
			this.rotate(0.5f);
		}

	}

}
