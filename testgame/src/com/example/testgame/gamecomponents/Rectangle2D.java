package com.example.testgame.gamecomponents;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.example.testgame.GLES20Renderer;

public class Rectangle2D extends GameObject {

	static final int NB_RECTANGLE_VERTEX = 4;
	private float width = 1;
	private float height = 1;

	public Rectangle2D() {

		super(NB_RECTANGLE_VERTEX, 6);
		// par défaut un rectangle a la forme d'un carré
		// on ajoute les 4 vertex qui compose le carré
		// les 3 premiers chiffre sont les coordonées X,Y,Z
		// les 2 derniers U et W

		this.putVertex(0, new Vertex(-1, 1, 0, 0, 0));
		this.putVertex(1, new Vertex(-1, -1, 0, 0, 1));
		this.putVertex(2, new Vertex(1, -1, 0, 1, 1));
		this.putVertex(3, new Vertex(1, 1, 0, 1, 0));

		/* updateVertices(); */
		// on indique l'ordre dans lequel on doit affichier les vertex
		// pour dessiner les 2 triangles qui vont former le carré.
		this.putIndice(0, 0);
		this.putIndice(1, 1);
		this.putIndice(2, 2);

		this.putIndice(3, 0);
		this.putIndice(4, 2);
		this.putIndice(5, 3);
	}

	public void onUpdate(OpenGLActivity openGLActivity) {
		// TODO Auto-generated method stub

	}

	public void setHeight(int h) {
		this.height = h;
		updateVertices();
	}

	public void setWidth(int w) {
		this.width = w;
		updateVertices();
	}

	private void updateVertices() {

		float w = (float) width / 1;
		float h = (float) height / 1;

		this.putVertex(0, new Vertex(-w, h, 0, 0, 0));
		this.putVertex(1, new Vertex(-w, -h, 0, 0, 1));
		this.putVertex(2, new Vertex(w, -h, 0, 1, 1));
		this.putVertex(3, new Vertex(w, h, 0, 1, 0));
	}

	@Override
	public void draw(float[] modelMatrix) {

		/**
		float[] mMvp = new float[16];
		// équivalent du PUSH
		this.mBackupModelView = modelMatrix.clone();

		Matrix.translateM(modelMatrix, 0, X, Y, 0);

		Matrix.multiplyMM(mMvp, 0, renderer.mProjectionView, 0,
				renderer.mModelView, 0);
		// on alimente la donnée UNIFORM mAdressOf_Mvp du programme OpenGL
		// avec
		// une matrice de 4 flotant.
		GLES20.glUniformMatrix4fv(renderer.mProgramme1.mAdressOf_Mvp, 1, false,
				mMvp, 0);

		// on se positionne au debut du Buffer des indices
		// qui indiquent dans quel ordre les vertex doivent être dessinés
		this.getIndices().position(0);

		GLES20.glDrawElements(drawMode, this.getIndices().capacity(),
				GLES20.GL_UNSIGNED_SHORT, this.getIndices());

		renderer.mProgramme1.disableVertexAttribArray();
		// équivalent du POP
		renderer.mModelView = this.mBackupModelView;

	*/
	}

}
