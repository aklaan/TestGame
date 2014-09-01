package com.rdupuis.gamefactory.components;

import android.opengl.Matrix;
import com.rdupuis.gamefactory.R;
import com.rdupuis.gamefactory.components.shapes.Rectangle2D;
import com.rdupuis.gamefactory.enums.DrawingMode;

public class UserFinger extends Rectangle2D {

	float worldX;
	float worldY;
	float histoX;
	float histoY;

	public final static String USER_FINGER_TAG = "USER_FINGER_TAG";
	
	public UserFinger() {
		super(DrawingMode.EMPTY);
		this.setHeight(100);
		this.setWidth(100);
		this.enableColission();
		this.mCollisionBox.isVisible = true;
		this.setTagName(USER_FINGER_TAG);
		this.isStatic = false;
		// on fait expr�s de d�finir le premier point loin de l'�cran
		//pour �viter les colision au premier cycle 
		this.X = 10000;
		
	}

	public void onUpdate() {
		// si l'utilisateur touche l'�cran
		if (this.getScene().getActivity().mGLSurfaceView.touched) {

			// on met � jour les coordon�es
			this.setCoord(
					this.getScene().getActivity().mGLSurfaceView.touchX,
					this.getScene().getHeight()
							- this.getScene().getActivity().mGLSurfaceView.touchY);

			this.histoX = this.getScene().getActivity().mGLSurfaceView.histoX;
			this.histoY = this.getScene().getActivity().mGLSurfaceView.histoY;

			// on active les colissions
			this.canCollide = true;
			//getWorldCoord();

			/**
			 * Log.i("UserFinger", String.valueOf(this.X) + "/" +
			 * String.valueOf(this.Y));
			 * 
			 * Log.i("UserFinger World", String.valueOf(this.worldX) + "/" +
			 * String.valueOf(this.worldY)); Log.i("--",
			 * "----------------------------------------------------------------"
			 * );
			 */
		} else {
			// sinon les collisions sont d�sactiv�es
			this.canCollide = false;
		}

	}

	public float[] getHomogenicCoord() {
		float[] homCoord = new float[4];

		homCoord[0] = (2.0f * this.X) / this.getScene().getWidth() - 1.0f;
		homCoord[1] = (2.0f * this.Y) / this.getScene().getHeight() - 1.0f;
		homCoord[2] = 1.0f;
		homCoord[3] = 1.0f;
		return homCoord;
	}

	// pour retourver les coordon�es du pointeur dans le monde, il faut faire le
	// cheminement inverse
	// ecran -> coorcon�es homog�nes -> coordon�e projection -> coordon�es model
	public void getWorldCoord() {
		float[] projCoord = new float[4];
		float[] viewCoord = new float[4];

		float[] reverseProjectionView = new float[16];
		float[] reverseVMatrix = new float[16];
		

		Matrix.invertM(reverseProjectionView, 0, this.getScene()
				.getProjectionView(), 0);
		Matrix.invertM(reverseVMatrix, 0, this.getScene().mVMatrix, 0);

		// je suis oblig� de passer par un vecteur 4 pour la multiplication
		// calcul du point plac� sur le premier plan de clipping

		// les coordonn�e du pointeur doivent �tre normalis�
		// attention les coordon�e ANDROID sont invers�es par rapport � OpenGL
		// le 0,0 est en haut a gauche pour android tandis qu'il est en bas �
		// droite pour GL

		Matrix.multiplyMV(projCoord, 0, reverseProjectionView, 0,
				this.getHomogenicCoord(), 0);

		Matrix.multiplyMV(viewCoord, 0, reverseVMatrix, 0, projCoord, 0);

		// on divise par W pour revenir en coordon�es World
		// le W correspond � la mise en perspective effectu� par la matrice de
		// projection.
		// comme on a utilis� la matrice inverse on a invers� aussi la
		// perspective. du coup il faut la
		// remettre dans le bon sens.
		viewCoord[0] = viewCoord[0] / viewCoord[3];
		viewCoord[1] = viewCoord[1] / viewCoord[3];
		viewCoord[2] = viewCoord[2] / viewCoord[3];

		this.worldX = viewCoord[0];
		this.worldY = viewCoord[1];

	}

}
