package com.example.testgame.gameobjects;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import com.example.testgame.Enums;
import com.example.testgame.GLES20RendererScene01;
import com.example.testgame.gamecomponents.GameObject;
import com.example.testgame.gamecomponents.OpenGLActivity;
import com.example.testgame.gamecomponents.Rectangle2D;
import com.example.testgame.gamecomponents.Vertex;

public class Starship extends Rectangle2D {

	public GameObject cible;

	public Starship() {
		super(Enums.drawMode.FILL);

		this.setTagName("starship");
		this.isStatic = false;

	}

	@Override
	public void onUpdate(OpenGLActivity activity) {

		//on incrémente l'angle de rotation a chaque images 
				angleRAD = angleRAD + 0.05F;

		if (activity.mGLSurfaceView.touched) {
			this.angleRAD += 0.05f;

		}

		//test des colisions
		for (GameObject go : this.mCollideWithList) {
			Log.i("starship", "i'm collide with : " + go.getTagName());

		}

	}

	@Override
	public void draw(GLES20RendererScene01 renderer) {

		ProgramShader_simple sh = (ProgramShader_simple) renderer.mProgramShaderProvider
				.getShaderByName("simple");
		renderer.mProgramShaderProvider.use(sh);

		// on charge les coordonées de texture
		this.getTextCoord().rewind();
		sh.setTextureCoord(this.getTextCoord());

		// if (sh.attrib_color_location != -1) {
		// this.getVertices().position(0);
		// GLES20.glVertexAttribPointer(sh.attrib_color_location, 4,
		// GLES20.GL_FLOAT, false, Vertex.Vertex_TEXT_SIZE_BYTES, color);

		sh.enableShaderVar();

		// équivalent du PUSH
	//	this.mModelView = renderer.mModelView.clone();
		
		float[] mMvp = new float[16];
		float[] wrkmodelView = new float[16];

		//on initialise la matrice de transformation modele
		Matrix.setIdentityM(this.mModelView, 0);
		
		//on initialise la matrice temporaire
		wrkmodelView = this.mModelView.clone();

		//on replace le centre de l'objet au coordonées souhaitées
		Matrix.translateM(wrkmodelView, 0, X, Y, 0);
		
		//on calcule une matrice de rotation
		Matrix.setRotateEulerM(mRotationMatrix, 0, 0, 0, angleRAD + 10.0f);

	
		//on calcule la nouvelle matrice de transformation modele
		//qui tiens compte de la translation puis de la rotation.
			Matrix.multiplyMM(this.mModelView, 0, wrkmodelView, 0,
					this.mRotationMatrix, 0);

			
			
		//mise à jour du centre de la forme
			this.X = this.getCenterVertex(cible.mModelView).x;
			this.Y = this.getCenterVertex(cible.mModelView).y;

			
		//ici on souhaite effectuer une translation 
		// de l'objet de tel manière qu'il décrive un cercle 
		//autour de la cible
			
		  if (this.cible != null) {
		    this.X = this.getCenterVertex(cible.mModelView).x;
   			this.Y = this.getCenterVertex(cible.mModelView).y;

			Matrix.translateM(this.mModelView, 0,
					(float) ((Math
							.cos(this.angleRAD) * 50.0f)),
					(float) ((Math
							.sin(this.angleRAD) * 50.0f)), 0);

		}

		// Calcul de la Matrice Vue/Projection
		// on récupère la matrice de projection valable à l'ensemble de la scène
		// (vue de caméra)
		// et la matrice de la forme (scale - rotate - translate)
		Matrix.multiplyMM(mMvp, 0, renderer.mProjectionView, 0,
				this.mModelView, 0);

		// pour calculer les coordonées de l'objet à l'écran il faut récupérer
		// chaques vertex
		// et appliquer la matrice.
		// je ne sais pas si Opengl calcule les point en parallèle
		// mais faire le calcul dans le prog ça me semble plus couteux ??
		// Toutefois j'ai besoin des coordonées calculées pour le calcul des
		// colisions...

	
		// on se positionne au debut du Buffer des indices
		// qui indiquent dans quel ordre les vertex doivent être dessinés
		this.getIndices().rewind();

		// on charge les coordonnées des vertices
		this.getFbVertices().rewind();
		sh.setVerticesCoord(this.getFbVertices());

		// Log.i("draw-"+this.getTagName(),"X:"+String.valueOf(this.X) +
		// "- Y:"+String.valueOf(this.Y) );

		// on memorise les coordonées écran de l'objet
//		this.X = this.mModelView[12];
//		this.Y = this.mModelView[13];

		// on alimente la donnée UNIFORM mAdressOf_Mvp du programme OpenGL
		// avec
		// une matrice de 4 flotant.

		// ici on alimente une variable globale OPENGL
		// OPENGL va conserver sa valeur dans son propre référentiel

		GLES20.glUniformMatrix4fv(sh.uniform_mvp_location, 1, false, mMvp, 0);

		/**
		 * if (GLES20.glGetError() != GLES20.GL_NO_ERROR){ Log.i("debug",
		 * "starship - glUniformMatrix4fv - RC:"
		 * +String.valueOf(GLES20.glGetError())); }
		 * 
		 * 
		 * if (GLES20.glGetError() != GLES20.GL_NO_ERROR){
		 * Log.i("starship.draw()", "GLES20.glVertexAttribPointerRC:"
		 * +String.valueOf(GLES20.glGetError())); }
		 */

		// on dessine les vertex dans l'ordre indexé
		GLES20.glDrawElements(drawMode, this.getIndices().capacity(),
				GLES20.GL_UNSIGNED_SHORT, this.getIndices());

		// si la forme possède une boite de colision on met à jour les infos de
		// position
		// et si elle est visible on la dessine.
		if (this.canCollide) {
			this.mCollisionBox.update();
			if (mCollisionBox.isVisible) {
				this.mCollisionBox.draw(renderer);
			}
		}
		/**
		 * int err =GLES20.glGetError(); if (err != GLES20.GL_NO_ERROR){
		 * Log.i("starship.draw()", "GLES20.glDrawElements - RC:"
		 * +String.valueOf(err)); }
		 */
		// Log.i("starShip Draw : ", String.valueOf(GLES20.glGetError()));
		// mShader.disableShaderVar();
		// renderer.mProgramme1.disableVertexAttribArray();
	}

}
