package com.example.testgame.gamecomponents;

public class Vector2D {

	// Coordonnée de vecteur
	public float x;
	public float y;

	// conctructeur du vecteur nul
	public Vector2D() {
		x = y = 0f;

	}

	public Vector2D(float x, float y) {
		this.setXY(x, y);

	}

	public void setXY(float a, float b) {
		x = a;
		y = b;

	}
	
	public void normalise(){
		
	}
	
}
