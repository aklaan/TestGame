package com.example.testgame.gamecomponents;

import android.os.SystemClock;

public class AnimationTurnArround extends Animation {

	GameObject mCible;
	float speed;
	float distance;
	float angle;

	public AnimationTurnArround(GameObject parent, GameObject cible) {
		super(parent);
		this.mCible = cible;
		this.angle = 5.0f;
		this.speed = 10.0f;
		this.distance = 50.0f;
	}

	@Override
	public void start() {
		super.start();

	}

	@Override
	public void play() {

		if (this.mCible != null) {
			this.angle += 0.05f;
			this.parent.X = mCible.X
					+ (float) (Math.cos(this.angle * this.speed) * this.distance);
			this.parent.Y = mCible.Y
					+ (float) (Math.sin(this.angle * this.speed) * this.distance);

		}

	}

}
