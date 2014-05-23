package com.example.testgame.gamecomponents;

import android.opengl.Matrix;
import android.os.SystemClock;

public class Animation {

	float startTime;
	public static enum playingMode {NONE,ONCE,REPEAT_N,LOOP};
	public static enum animationStatus {PLAYING,STOPPED};
	GameObject parent;
	
	
	public Animation(GameObject parent) {
		this.parent = parent;
	}

	public void start() {
		startTime = SystemClock.currentThreadTimeMillis();
	}

	public void stop() {
	}

public void onPlay(){
	// se déplacer vers la droite pendant 3 seconde
float elapsedTime = SystemClock.currentThreadTimeMillis() - startTime;
	if (elapsedTime < 3000){
		Matrix.translateM(this.parent.mModelView,0,  1.f, 0, 0);
	}

	// se déplacer vers la gauche pendant 3 seconde
}


}
