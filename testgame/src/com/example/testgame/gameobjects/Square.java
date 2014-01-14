package com.example.testgame.gameobjects;

public class Square extends Rectangle {

	public Square(){
		super();
		this.scale(10f, 10f);
		this.translate(0f,-10f);
	}
	@Override
	public void onUpdate(){
		
		
		
		this.rotate(0.5f);
	}
	
}
