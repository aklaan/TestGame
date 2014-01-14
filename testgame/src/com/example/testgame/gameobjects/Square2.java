package com.example.testgame.gameobjects;

import com.example.testgame.GameObject;
import com.example.testgame.Vertex;

import android.opengl.Matrix;
import android.util.Log;

public class Square2 extends GameObject {

	static final int NB_SQUARE_VERTEX = 4;
    private float i = 0;
    private int sens = 1;
	public Square2() {

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
		Matrix.setIdentityM(mModelMatrix, 0);
		this.scale(10f, 10f);
		this.rotate(0.5f);
		
		if (i>5 || i<-5){
			sens = sens*-1;
		}  
		i=i+(0.2f * sens);
		this.translate(i,0.f);
	}
	
}
