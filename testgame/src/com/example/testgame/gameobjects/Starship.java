package com.example.testgame.gameobjects;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.example.testgame.Enums;
import com.example.testgame.R;
import com.example.testgame.gamecomponents.Animation;
import com.example.testgame.gamecomponents.AnimationRightLeftOnX;
import com.example.testgame.gamecomponents.GameObject;
import com.example.testgame.gamecomponents.OpenGLActivity;
import com.example.testgame.gamecomponents.Rectangle2D;

public class Starship extends Rectangle2D {

	public GameObject cible;
	public float lastTouch;

	public Starship() {
		super(Enums.drawMode.FILL);

		this.setTagName("starship");
		this.isStatic = false;

		Matrix.setIdentityM(this.mModelView, 0);

	}

	@Override
	public void onUpdate(OpenGLActivity activity) {

		int newTextureId = R.string.boulerouge;

		// on incr�mente l'angle de rotation a chaque images
		// ici je souhaite que l'objet tourne sur lui m�me constament
		angleRAD = angleRAD + 0.05F;

		// action supl�mentaire
		if (activity.mGLSurfaceView.touched) {

			float elapsedTime = SystemClock.elapsedRealtime() - this.lastTouch;
			// on attend une 1/4 de seconde avant de valider un autre touch
			if (elapsedTime > 250) {
				this.angleRAD += 0.5f;
				this.lastTouch = SystemClock.elapsedRealtime();

				if (this.getTagName() == "starship1") {
					
					//s'il y a une animation en cours, il faut attendre qu'elle
					//soit termin�e avant d'en d�marrer une autre.
					if (this.getAnimation() != null) {
						if (this.getAnimation().status == Animation.AnimationStatus.STOPPED) {

							this.setAnimation(new AnimationRightLeftOnX(this));
							this.getAnimation().start();

						}

					} else {
						this.setAnimation(new AnimationRightLeftOnX(this));
						this.getAnimation().start();

					}

				}

			}
		}

		
		//-----------------------------------------
		// traiter des colisions avec les autres objets
		//---------------------------------------------------------
		if (!this.mCollideWithList.isEmpty()) {
			for (GameObject go : this.mCollideWithList) {

				// newTextureId = R.string.textureRobot;
			}
		}

		//-----------------------------------------
		// traiter des objets � �couter
		//--------------------------------------------------------
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
		
		//-----------------------------------------
		// traiter l'animation
		//--------------------------------------------------------
		if (this.getAnimation() != null) {

			if (this.getAnimation().status == Animation.AnimationStatus.PLAYING) {
				this.getAnimation().play();
				newTextureId = R.string.textureRobot;
			}
			if (this.getAnimation().status == Animation.AnimationStatus.STOPPED) {
				this.setAnimation(null);

			}

		}

		// ici on souhaite effectuer une translation
		// de l'objet de tel mani�re qu'il d�crive un cercle
		// autour de la cible
		/**
		 * if (this.cible != null) {
		 * 
		 * this.X = cible.X + (float) (Math.cos(this.angleRAD) * 50.0f); this.Y
		 * = cible.Y + (float) (Math.sin(this.angleRAD) * 50.0f);
		 * 
		 * }
		 */
		// a la fin des mises � jour on connais les nouvelles coordon�es
		// on peut calculer la matrice
		this.updateModelView();

		
		//-----------------------------------------
		//Mettre a jour la boite de colision
		//------------------------------------------
		if (this.canCollide) {
			this.mCollisionBox.update();
		
		}
		
		
		//-----------------------------------------------------
		// Gestion des modifications de la texture
		//------------------------------------------------------
		if (this.mTexture.textureNameID != newTextureId) {
			this.getScene()
					.getBitmapProvider()
					.assignTexture(
							this.getScene().mActivity.getString(newTextureId),
							this);

			this.mTexture.textureNameID = newTextureId;
		}

	}

	//----------------------------------------------
	//Dessiner
	//-----------------------------------------------
	@Override
	public void draw() {

		ProgramShader_simple sh = (ProgramShader_simple) this.getScene()
				.getProgramShaderProvider().getShaderByName("simple");
		
		this.getScene().getProgramShaderProvider().use(sh);

		// on charge les coordon�es de texture
		this.getTextCoord().rewind();
		sh.setTextureCoord(this.getTextCoord());

		// if (sh.attrib_color_location != -1) {
		// this.getVertices().position(0);
		// GLES20.glVertexAttribPointer(sh.attrib_color_location, 4,
		// GLES20.GL_FLOAT, false, Vertex.Vertex_TEXT_SIZE_BYTES, color);

		sh.enableShaderVar();

		// �quivalent du PUSH
		// this.mModelView = renderer.mModelView.clone();

		float[] mMvp = new float[16];
		
		// Calcul de la Matrice Vue/Projection
		// on r�cup�re la matrice de projection valable � l'ensemble de la sc�ne
		// (vue de cam�ra)
		// et la matrice de la forme (scale - rotate - translate)
		Matrix.multiplyMM(mMvp, 0, this.getScene().getProjectionView(), 0,
				this.mModelView, 0);

		// pour calculer les coordon�es de l'objet � l'�cran il faut r�cup�rer
		// chaques vertex
		// et appliquer la matrice.
		// je ne sais pas si Opengl calcule les point en parall�le
		// mais faire le calcul dans le prog �a me semble plus couteux ??
		// Toutefois j'ai besoin des coordon�es calcul�es pour le calcul des
		// colisions...

		// on se positionne au debut du Buffer des indices
		// qui indiquent dans quel ordre les vertex doivent �tre dessin�s
		this.getIndices().rewind();

		// on charge les coordonn�es des vertices
		this.getFbVertices().rewind();
		sh.setVerticesCoord(this.getFbVertices());

		// on alimente la donn�e UNIFORM mAdressOf_Mvp du programme OpenGL
		// avec
		// une matrice de 4 flotant.

		// ici on alimente une variable globale OPENGL
		// OPENGL va conserver sa valeur dans son propre r�f�rentiel

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

		// on dessine les vertex dans l'ordre index�
		GLES20.glDrawElements(drawMode, this.getIndices().capacity(),
				GLES20.GL_UNSIGNED_SHORT, this.getIndices());

		// si la forme poss�de une boite de colision et que l'on souhaite la voir
		// a l'�cran, on la dessine.
		if (this.canCollide) {
			//this.mCollisionBox.update();
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
