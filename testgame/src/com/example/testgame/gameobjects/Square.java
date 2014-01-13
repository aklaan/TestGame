package com.example.testgame.gameobjects;

public class Square extends Rectangle {

	@Override
	public void onUpdate(){
		this.rotate(0.1f);
	}
	
}
