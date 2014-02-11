package com.example.testgame.gamecomponents;

import java.util.ArrayList;

public class CollisionControler {

	public CollisionControler() {

	}

	public static void checkAllCollisions(ArrayList<GameObject> mGameObjectList) {

		for (GameObject gameObject : mGameObjectList) {

			// on ne contrôle les colisions que pour les objets
			// en mouvement
			if (gameObject.isStatic == false && gameObject.canCollide) {
				gameObject.mCollideWithList.clear();
				for (GameObject go : mGameObjectList) {
					if (go != gameObject && go.canCollide) {

						

							if (checkCollision(go.mCollisionBox,
									gameObject.mCollisionBox)) {

								gameObject.mCollideWithList
										.add(go.mCollisionBox);
							}

						}
					
				}
			}

		}
	}

	static boolean checkCollision(CollisionBox a, CollisionBox b) {

		if (a == b || (a.getCoordX() >= b.getCoordX() + b.getWidth()) // trop à
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
