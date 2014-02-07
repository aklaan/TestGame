package com.example.testgame.gameobjects;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.example.testgame.GLES20RendererScene01;
import com.example.testgame.gamecomponents.GameObject;
import com.example.testgame.gamecomponents.Vertex;

public class Line2D extends GameObject{

	public Line2D(int nbVertex, int nbIndex) {
		super(2, 2);
		this.putVertex(0, new Vertex(0, 1, 0));
		this.putVertex(1, new Vertex(0, -1, 0));
		
	/*	updateVertices();*/
		// on indique l'ordre dans lequel on doit affichier les vertex
		// pour dessiner les 2 triangles qui vont former le carr�.
		this.putIndice(0, 0);
		this.putIndice(1, 1);
		this.drawMode = GLES20.GL_LINES;
	}

	
	
}
