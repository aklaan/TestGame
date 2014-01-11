package com.example.testgame.gameobjects;

import com.example.testgame.GameObject;
import com.example.testgame.Vertex;

import android.util.Log;

public class Square extends GameObject {

	static final int NB_SQUARE_VERTEX = 4;

	public Square() {

		super(NB_SQUARE_VERTEX, 6);

		// on ajoute les 4 vertex qui compose le carré
		this.putVertex(0, new Vertex(-1, 1, 0, 0, 0));
		this.putVertex(1, new Vertex(-1, -1, 0, 0, 1));
		this.putVertex(2, new Vertex(1, -1, 0, 1, 1));
		this.putVertex(3, new Vertex(1, 1, 0, 1, 0));

		// on indique l'ordre dans lequel on doit affichier les vertex
		// pour dessiner les 2 triangles qui vont former le carré.
		this.putIndice(0, 0);
		this.putIndice(1, 1);
		this.putIndice(2, 2);

		this.putIndice(3, 0);
		this.putIndice(4, 2);
		this.putIndice(5, 3);
	}

	@Override
	public void onUpdate(){
		this.rotate(0.1f);
	}
	
}
