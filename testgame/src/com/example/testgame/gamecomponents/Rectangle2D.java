package com.example.testgame.gamecomponents;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.example.testgame.Enums;
import com.example.testgame.GLES20RendererScene01;
import com.example.testgame.gameobjects.ProgramShader_grille;
import com.example.testgame.gameobjects.ProgramShader_simple;

public class Rectangle2D extends GameObject {

	static final int NB_RECTANGLE_VERTEX = 4;
	private float width = 1;
	private float height = 1;

	public Rectangle2D(Enums.drawMode mode) {
		super();

		this.mVertices.add(new Vertex(-1f, 1f, 0f, 0f, 0f));
		this.mVertices.add(new Vertex(-1f, -1f, 0f, 0f, 1f));
		this.mVertices.add(new Vertex(1f, -1f, 0f, 1f, 1f));
		this.mVertices.add(new Vertex(1f, 1f, 0, 1f, 0f));

		switch (mode) {
		// on dessine que les lignes de contour
		case EMPTY:

			this.initBuffers(8);
			this.putIndice(0, 0);
			this.putIndice(1, 1);

			this.putIndice(2, 1);
			this.putIndice(3, 2);

			this.putIndice(4, 2);
			this.putIndice(5, 3);

			this.putIndice(6, 3);
			this.putIndice(7, 0);

			break;
		// on dessine des triangles plein
		case FILL:

			this.initBuffers(6);

			// on indique l'ordre dans lequel on doit affichier les vertex
			// pour dessiner les 2 triangles qui vont former le carré.
			this.putIndice(0, 0);
			this.putIndice(1, 1);
			this.putIndice(2, 2);

			this.putIndice(3, 0);
			this.putIndice(4, 2);
			this.putIndice(5, 3);
			break;
		}

		// par défaut un rectangle a la forme d'un carré
		// on ajoute les 4 vertex qui compose le carré
		// les 3 premiers chiffre sont les coordonées X,Y,Z
		// les 2 derniers U et W

		/**
		 * this.putVertex(0, new Vertex(-1f, 1f, 0f, 0f, 0f)); this.putVertex(1,
		 * new Vertex(-1f, -1f, 0f, 0f, 1f)); this.putVertex(2, new Vertex(1f,
		 * -1f, 0f, 1f, 1f)); this.putVertex(3, new Vertex(1f, 1f, 0, 1f, 0f));
		 */

	}

	public void onUpdate(OpenGLActivity openGLActivity) {
		// TODO Auto-generated method stub

	}

	public void setHeight(float h) {
		this.height = h;
		updateVertices();
		if (this.canCollide) {
			this.mCollisionBox.update();
		}
	}

	public float getHeight() {
		return this.height;
	}

	public float getWidth() {
		return this.width;
	}

	public void setWidth(float w) {
		this.width = w;
		updateVertices();
		if (this.canCollide) {
			this.mCollisionBox.update();
		}
	}

	private void updateVertices() {

		float w = (float) width /1;
		float h = (float) height / 1;

		
		this.mVertices.get(0).x = -w;
		this.mVertices.get(0).y = h;
		
		this.mVertices.get(1).x = -w;
		this.mVertices.get(1).y = -h;
		
		this.mVertices.get(2).x = w;
		this.mVertices.get(2).y = -h;
		
		this.mVertices.get(3).x = w;
		this.mVertices.get(3).y = h;
		
	}

	@Override
	public void draw(GLES20RendererScene01 renderer) {

		ProgramShader_grille sh = (ProgramShader_grille) renderer.mProgramShaderProvider
				.getShaderByName("grille");
		renderer.mProgramShaderProvider.use(sh);

		// on se positionne au debut du Buffer des indices
		// qui indiquent dans quel ordre les vertex doivent être dessinés
		this.getIndices().rewind();

		// on charge les coordonnées des vertices
		sh.setVerticesCoord(this.getFbVertices());
		this.getFbVertices().rewind();
		
		// on charge les coordonées de texture
		
		sh.setTextureCoord(this.getTextCoord());

		// if (sh.attrib_color_location != -1) {
		// this.getVertices().position(0);
		// GLES20.glVertexAttribPointer(sh.attrib_color_location, 4,
		// GLES20.GL_FLOAT, false, Vertex.Vertex_TEXT_SIZE_BYTES, color);

		sh.enableShaderVar();

		float[] mMvp = new float[16];
		// équivalent du PUSH
		
		
		// ici on est dans le cas où on souhaite dessiner qu'un seul objet
		// récupérer la modelview au niveau de la scène n'a pas d'interet
		// en revanche si notre objet est composé de plusieurs forme
		// il faut pouvoir éventuellement mémoriser la model view
		// pour pourvoir la réapliquer a des formes.
		
		// ici je commence à dessiner un nouvel objet
		// je repart donc de zéro 
		this.mModelView = renderer.mModelView.clone();

		Matrix.setIdentityM(this.mModelView, 0);

		Matrix.translateM(this.mModelView, 0, X, Y, 0);

		Matrix.multiplyMM(mMvp, 0, renderer.mProjectionView, 0,
				this.mModelView, 0);
		
		// On alimente la donnée UNIFORM mAdressOf_Mvp du programme OpenGL
		// avec
		// une matrice de 4 flotant.
		GLES20.glUniformMatrix4fv(sh.uniform_mvp_location, 1, false, mMvp, 0);

		// on se positionne au debut du Buffer des indices
		// qui indiquent dans quel ordre les vertex doivent être dessinés
		this.getIndices().rewind();

		GLES20.glDrawElements(drawMode, this.getIndices().capacity(),
				GLES20.GL_UNSIGNED_SHORT, this.getIndices());

		// renderer.mProgramme1.disableVertexAttribArray();
		// équivalent du POP
		//renderer.mModelView = this.mBackupModelView;
		// renderer.mProgramme1.disableVertexAttribArray();

	}

}
