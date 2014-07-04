package com.example.testgame.gamecomponents;

public class AnimationRotate extends Animation {


	public AnimationRotate(GameObject parent) {
		super(parent);

	}

	@Override
	public void start() {
		super.start();
		
	}

	@Override
	public void play() {
    this.parent.angleRAD += 5.5f;
    
	}

}
