package com.rdupuis.gamefactory.scene01.components;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.rdupuis.gamefactory.R;
import com.rdupuis.gamefactory.components.GameObject;
import com.rdupuis.gamefactory.components.OpenGLActivity;
import com.rdupuis.gamefactory.components.shapes.Rectangle2D;
import com.rdupuis.gamefactory.enums.DrawingMode;

public class Starship extends Rectangle2D {

	public GameObject cible;
	

	public Starship() {
		super(DrawingMode.FILL);

		
		this.isStatic = false;

		Matrix.setIdentityM(this.mModelView, 0);

	}

	@Override
	public void onUpdate() {

		// on incrémente l'angle de rotation a chaque images
		// ici je souhaite que l'objet tourne sur lui même constament
		//angleRADZ = angleRADZ + 0.5F;

		
/**		
		// action suplémentaire
		if (activity.mGLSurfaceView.touched) {
			screenTouched = true;
			
			float elapsedTime = SystemClock.elapsedRealtime() - this.lastTouch;
			// on attend une 1/4 de seconde avant de valider un autre touch
			if (elapsedTime > 250) {
				this.angleRAD += 0.5f;
				this.lastTouch = SystemClock.elapsedRealtime();

				if (this.getTagName() == "starship1") {

					// s'il y a une animation en cours, il faut attendre qu'elle
					// soit terminée avant d'en démarrer une autre.
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
		
			
			
		
			if (this.getTagName() == "starship2" && screenTouched) {

				// s'il y a une animation en cours, il faut attendre qu'elle
				// soit terminée avant d'en démarrer une autre.
				if (this.getAnimation() != null) {
					if (this.getAnimation().status == Animation.AnimationStatus.STOPPED) {

						this.setAnimation(new AnimationTurnArround(this,
								this.cible, 100.f));
						this.getAnimation().start();

					}

				} else {
					this.setAnimation(new AnimationTurnArround(this,
							this.cible, 50.f ));
					this.getAnimation().start();

				}

			}
		
		}

		if (this.getTagName() == "starship2" && !screenTouched && this.getAnimation() != null){
			this.getAnimation().stop();
		}
		// ici on souhaite effectuer une translation
		// de l'objet de tel manière qu'il décrive un cercle
		// autour de la cible
		/**
		 * if (this.cible != null) {
		 * 
		 * this.X = cible.X + (float) (Math.cos(this.angleRAD) * 50.0f); this.Y
		 * = cible.Y + (float) (Math.sin(this.angleRAD) * 50.0f);
		 * 
		 * }
		 */

	}

	// ----------------------------------------------
	// Dessiner
	// -----------------------------------------------
	//@Override
	public void xxxxxxxxxxxdraw() {

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

	
		
		
		
	//	mMvp = this.getScene().mMVPMatrix;
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

		// si la forme possède une boite de colision et que l'on souhaite la
		// voir
		// a l'écran, on la dessine.
		if (this.canCollide) {
			// this.mCollisionBox.update();
			if (mCollisionBox.isVisible) {
				this.mCollisionBox.draw();
			}
		}

	}

	@Override
	public void onListenGameObject(GameObject go) {
		if (go.getTagName() == R.string.starship2) {

			if (!go.mCollideWithList.isEmpty()) {
				for (GameObject collider : go.mCollideWithList) {

					if (collider.getTagName() == R.string.petit_robot) {
						newTextureId = R.string.textureRobot;
					}

				}

			} else {
				newTextureId = R.string.boulerouge;
			}

		}

	}

	public void onAnimationPlay() {
		newTextureId = R.string.textureRobot;
	}
}
