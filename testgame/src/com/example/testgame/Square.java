package com.example.testgame;

public class Square extends GameObject {

	static final int NB_SQUARE_VERTEX = 4;

	public Square() {

		super(NB_SQUARE_VERTEX);

		// on ajoute les 4 vertex qui compose le carré
		this.putVertice(1, new Vertex(-1, 1, 0));
		this.putVertice(2, new Vertex(-1, -1, 0));
		this.putVertice(3, new Vertex(1, -1, 0));
		this.putVertice(4, new Vertex(1, 1, 0));

		//on indique l'ordre dans lequel on doit affichier les vertex
		//pour dessiner les 2 triangles qui vont former le carré.
		this.putIndice(1, 0);
		this.putIndice(2, 1);
		this.putIndice(3, 2);
		this.putIndice(4, 0);
		this.putIndice(5, 2);
		this.putIndice(6, 3);
	}

}
