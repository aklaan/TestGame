package com.example.testgame;

import android.util.Log;

public class Square extends GameObject {

	static final int NB_SQUARE_VERTEX = 4;

	public Square() {

		super(NB_SQUARE_VERTEX,6);

		// on ajoute les 4 vertex qui compose le carré
		Log.i("debug", "vertx 1");
		this.putVertice(0, new Vertex(-1, 1, 0));
		Log.i("debug", "vertx 2");
		this.putVertice(1, new Vertex(-1, -1, 0));
		this.putVertice(2, new Vertex(1, -1, 0));
		this.putVertice(3, new Vertex(1, 1, 0));

		//on indique l'ordre dans lequel on doit affichier les vertex
		//pour dessiner les 2 triangles qui vont former le carré.
		this.putIndice(0, 0);
		this.putIndice(1, 1);
		this.putIndice(2, 2);
		this.putIndice(3, 0);
		this.putIndice(4, 2);
		this.putIndice(5, 3);
	}

}
