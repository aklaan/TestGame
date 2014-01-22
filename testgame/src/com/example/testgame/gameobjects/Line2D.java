package com.example.testgame.gameobjects;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.example.testgame.GLES20Renderer;
import com.example.testgame.gamecomponents.GameObject;
import com.example.testgame.gamecomponents.Vertex;

public class Line2D extends GameObject{

	public Line2D(int nbVertex, int nbIndex) {
		super(2, 2);
		this.putVertex(0, new Vertex(0, 1, 0));
		this.putVertex(1, new Vertex(0, -1, 0));
		
	/*	updateVertices();*/
		// on indique l'ordre dans lequel on doit affichier les vertex
		// pour dessiner les 2 triangles qui vont former le carré.
		this.putIndice(0, 0);
		this.putIndice(1, 1);
		this.drawMode = GLES20.GL_LINES;
	}

	
	@Override
	public void draw(GLES20Renderer GLES20Renderer){
	
	
		float [] mMvp = new float[16];
	        	
		Matrix.multiplyMM(mMvp, 0, GLES20Renderer.mProjectionView, 0, mModelView, 0);
		
		GLES20.glUniformMatrix4fv(GLES20Renderer.mProgramme1.mAdressOf_Mvp, 1, false,
				mMvp, 0);
	
		
	
		// on se positionne au debut du Buffer des indices
				// qui indiquent dans quel ordre les vertex doivent être dessinés
				this.getIndices().position(0);

				GLES20.glDrawElements(drawMode, this.getIndices().capacity(),
						GLES20.GL_UNSIGNED_SHORT, this.getIndices());

				
	
	}
}
