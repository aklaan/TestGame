package com.example.testgame.gamecomponents;

import java.util.ArrayList;

public class CollisionControler {

	public CollisionControler() {

	}

	public void checkAllCollisions(ArrayList<GameObject> mGameObjectList) {

	}

	static boolean checkCollision(GameObject a, GameObject b) {

		if ((a.getCoordX() >= b.getCoordX() + b.getWidth()) // trop à droite
				|| (a.getCoordX() + a.getWidth() <= b.getCoordX()) // trop à
																	// gauche
				|| (a.getCoordY() >= b.getCoordY() + b.getHeight()) // trop en
																	// bas
				|| (a.getCoordY() + a.getHeight() <= b.getCoordY())) // trop en
																		// haut
			return false;
		else
			return true;
	}

}
