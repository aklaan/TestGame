package com.example.testgame.gamecomponents;

import android.os.SystemClock;

public abstract class Animation  {

		float startTime;
	float endTime;
	
	public static  enum playingMode {
		NONE, ONCE, REPEAT_N, LOOP
	};

	public static  enum AnimationStatus {
		PLAYING, STOPPED
	};

	GameObject parent;
	public AnimationStatus status;

	public Animation(GameObject parent) {
		this.parent = parent;

	}

	public void start() {
		if (this.status != AnimationStatus.PLAYING) {
			startTime = SystemClock.elapsedRealtime();
			this.status = AnimationStatus.PLAYING;
		}
}

	public void stop() {
		this.status = AnimationStatus.STOPPED;
		endTime = SystemClock.elapsedRealtime();
	}

	public void play() {
		
	}

}
