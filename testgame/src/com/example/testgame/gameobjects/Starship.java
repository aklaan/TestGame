package com.example.testgame.gameobjects;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import com.example.testgame.Enums;
import com.example.testgame.GLES20RendererScene01;
import com.example.testgame.R;
import com.example.testgame.animationStatus;
import com.example.testgame.gamecomponents.Animation;
import com.example.testgame.gamecomponents.GameObject;
import com.example.testgame.gamecomponents.OpenGLActivity;
import com.example.testgame.gamecomponents.Rectangle2D;
import com.example.testgame.gamecomponents.Vertex;

public class Starship extends Rectangle2D {

	public GameObject cible;
	public Animation anim;

	public Starship() {
		super(Enums.drawMode.FILL);

		this.setTagName("starship");
		this.isStatic = false;
		this.anim = new Animation(this);
		Matrix.setIdentityM(this.mModelView, 0);

	}

	@Override
	public void onUpdate(OpenGLActivity activity) {

		int newTextureId = R.string.boulerouge;

		// on incrémente l'angle de rotation a chaque images
		// ici je souhaite que l'objet tourne sur lui même constament
		angleRAD = angleRAD + 0.05F;

		// action suplémentaire
		if (activity.mGLSurfaceView.touched) {
			this.angleRAD += 0.5f;

			if (this.getTagName() == "starship1") {
				anim.start();
			}

		}

		// traiter des colisions avec les autres objets
		if (!this.mCollideWithList.isEmpty()) {
			for (GameObject go : this.mCollideWithList) {

				// newTextureId = R.string.textureRobot;
			}
		}

		// traiter des objets à écouter
		if (!this.getGameObjectToListenList().isEmpty()) {
			for (GameObject go : this.getGameObjectToListenList()) {

				if (go.getTagName() == "starship2") {

					if (!go.mCollideWithList.isEmpty()) {
						for (GameObject collider : go.mCollideWithList) {

							if (collider.getTagName() == "robot") {
								newTextureId = R.string.textureRobot;
							}

						}

					} else {
						newTextureId = R.string.boulerouge;
					}

				}
			}

		}

		if (anim.status == animationStatus.PLAYING) {
			anim.onPlay();
			newTextureId = R.string.textureRobot;
		}

		// ici on souhaite effectuer une translation
		// de l'objet de tel manière qu'il décrive un cercle
		// autour de la cible

		if (this.cible != null) {

			this.X = cible.X + (float) (Math.cos(this.angleRAD) * 50.0f);
			this.Y = cible.Y + (float) (Math.sin(this.angleRAD) * 50.0f);

		}

		// a la fin des mises à jour on connais les nouvelles coordonées
		// on peut calculer la matrice
		this.updateModelView();

		// gestion des modifications de la texture
		if (this.mTexture.textureNameID != newTextureId) {
			this.getScene()
					.getBitmapProvider()
					.assignTexture(
							this.getScene().mActivity.getString(newTextureId),
							this);

			this.mTexture.textureNameID = newTextureId;
		}

	}

	@Override
	public void draw() {

		ProgramShader_simple sh = (ProgramShader_simple) this.getScene()
				.getProgramShaderProvider().getShaderByName("simple");
		this.getScene().getProgramShaderProvider().use(sh);

		// on charge les coordonées de texture
		this.getTextCoord().rewind();
		sh.setTextureCoord(this.getTextCoord());

		// if (sh.attrib_color_location != -1) {
		// this.getVertices().position(0);
		// GLES20.glVertexAttribPointer(sh.attrib_color_location, 4,
		// GLES20.GL_FLOAT, false, Vertex.Vertex_TEXT_SIZE_BYTES, color);

		sh.enableShaderVar();

		// équivalent du PUSH
		// this.mModelView = renderer.mModelView.clone();

		float[] mMvp = new float[16];
		// Calcul de la Matrice Vue/Projection
		// on récupère la matrice de projection valable à l'ensemble de la scène
		// (vue de caméra)
		// et la matrice de la forme (scale - rotate - translate)
		Matrix.multiplyMM(mMvp, 0, this.getScene().getProjectionView(), 0,
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
				this.mCollisionBox.draw();
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
