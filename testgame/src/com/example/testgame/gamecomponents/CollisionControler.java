package com.example.testgame.gamecomponents;

import java.util.ArrayList;

public class CollisionControler {

	public CollisionControler() {

	}

	public static void checkAllCollisions(ArrayList<GameObject> mGameObjectList) {

		for (GameObject gameObject : mGameObjectList) {

			if (gameObject.isStatic == false) {
				gameObject.mCollideWithList.clear();
				for (GameObject go : mGameObjectList) {
					if (checkCollision(go, gameObject)) {

						gameObject.mCollideWithList.add(go);
					}

				}
			}

		}
	}

	static boolean checkCollision(GameObject a, GameObject b) {

		if (a == b 
				|| (a.getCoordX() >= b.getCoordX() + b.getWidth()) // trop à
																		// droite
				|| (a.getCoordX() + a.getWidth() <= b.getCoordX()) // trop à
																	// gauche
				|| (a.getCoordY() >= b.getCoordY() + b.getHeight()) // trop en
																	// bas
				|| (a.getCoordY() + a.getHeight() <= b.getCoordY()) // trop en
		) // haut
			return false;
		else
			return true;
	}

}
