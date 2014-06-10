package com.example.testgame.gamecomponents;

import android.os.SystemClock;

public class AnimationRightLeftOnX extends Animation {

	float offsetX;

	public AnimationRightLeftOnX(GameObject parent) {
		super(parent);

	}

	@Override
	public void start() {
		super.start();
		offsetX = 1.9f;
	}

	@Override
	public void play() {
		// se déplacer vers la droite pendant 3 seconde
		float elapsedTime = SystemClock.elapsedRealtime() - startTime;

		if (elapsedTime < 1000) {
			// offsetX += 0.5f;
			this.parent.X += offsetX;
		} else {

			if (elapsedTime >= 1000 && elapsedTime <= 2000) {
				// offsetX -= 0.5f;
				this.parent.X -= offsetX;
			} else {
				this.status = AnimationStatus.STOPPED;
			}
		}

	}

}
