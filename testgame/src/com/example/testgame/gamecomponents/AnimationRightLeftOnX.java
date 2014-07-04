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
		offsetX = 3.0f;
	}

	@Override
	public void play() {
		// se déplacer vers la droite pendant 3 seconde
		float elapsedTime = SystemClock.elapsedRealtime() - startTime;

		if (elapsedTime < 2000) {
			// offsetX += 0.5f;
			this.parent.X += offsetX;
		} else {

			if (elapsedTime >= 2000 && elapsedTime <= 4000) {
				// offsetX -= 0.5f;
				this.parent.X -= offsetX;
			} else {
				this.status = AnimationStatus.STOPPED;
			}
		}

	}

}
