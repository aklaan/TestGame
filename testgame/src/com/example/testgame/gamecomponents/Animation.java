package com.example.testgame.gamecomponents;

import com.example.testgame.animationStatus;

import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

public class Animation {

	float startTime;
	float offsetX;

	public static enum playingMode {
		NONE, ONCE, REPEAT_N, LOOP
	};

	GameObject parent;
	public animationStatus status;

	public Animation(GameObject parent) {
		this.parent = parent;

	}

	public void start() {
		if (this.status != animationStatus.PLAYING) {
			startTime = SystemClock.elapsedRealtime();

			this.status = animationStatus.PLAYING;
			offsetX = 1.9f;
		}
	}

	public void stop() {
		this.status = animationStatus.STOPPED;

	}

	public void onPlay() {
		// se déplacer vers la droite pendant 3 seconde
		float elapsedTime = SystemClock.elapsedRealtime()- startTime;

		if (elapsedTime < 1000) {
			//offsetX += 0.5f;
			this.parent.X+= offsetX;
		} else {

			if (elapsedTime >= 1000 && elapsedTime <= 2000) {
				//offsetX -= 0.5f;
				this.parent.X-= offsetX;
			} else {
				this.status = animationStatus.STOPPED;
			}
		}
	
	
	}

}
