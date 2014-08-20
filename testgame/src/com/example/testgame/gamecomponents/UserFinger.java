package com.example.testgame.gamecomponents;

import java.util.ArrayList;

import android.opengl.Matrix;
import android.util.Log;

import com.example.testgame.R;

public class UserFinger extends Rectangle2D {

	float worldX;
	float worldY;
	float histoX;
	float histoY;

	public UserFinger() {
		super(DrawingMode.EMPTY);
		this.sethight(10);
		this.setWidth(10);
		this.enableColission();
		this.mCollisionBox.isVisible = true;
		this.setTagName(R.string.USER_FINGER);
		this.isStatic = false;
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
			getWorldCoord();
			
	/**		Log.i("UserFinger",
					String.valueOf(this.X) + "/" + String.valueOf(this.Y));

			Log.i("UserFinger World",
					String.valueOf(this.worldX) + "/" + String.valueOf(this.worldY));
			Log.i("--","----------------------------------------------------------------");
		*/
		} else {
			// sinon les collisions sont d�sactiv�es
			this.canCollide = false;
		}

	}

	public void getWorldCoord() {
		float[] oldVerticesCoord = new float[4];
		float[] newVerticesCoord = new float[4];
		float[] reverseMatrix = new float[16];
		float[] mMVP = new float[16];
		
		
        Matrix.multiplyMM(mMVP, 0, this.getScene().getProjectionView(), 0, this.getScene().mVMatrix, 0);

		Matrix.invertM(reverseMatrix, 0, mMVP, 0);
		
		// je suis oblig� de passer par un vecteur 4 pour la multiplication
		// calcul du point plac� sur le premier plan de clipping
		
		// les coordonn�e du pointeur doivent �tre normalis�
		//attention les coordon�e ANDROID sont invers�es par rapport � OpenGL
		// le 0,0 est en haut a gauche pour android tandis qu'il est en bas � droite pour GL
		for (int i = 0; i < this.mVertices.size(); i++) {
			oldVerticesCoord[0] = this.X / this.getScene().getWidth() *2-1; // x
			oldVerticesCoord[1] = (float) -(this.Y /this.getScene().getHeight()*2-1); // y
			oldVerticesCoord[2] = 1;      // z
			oldVerticesCoord[3] = 1.f;    //w

			Matrix.multiplyMV(newVerticesCoord, 0, reverseMatrix, 0,
					oldVerticesCoord, 0);
			
			//on divise par W pour revenir en coordon�es World
			//le W correspond � la mise en perspective effectu� par la matrice de projection.
			//comme on a utilis� la matrice inverse on a invers� aussi la perspective. du coup il faut la 
			//remettre dans le bon sens.
			newVerticesCoord[0] = newVerticesCoord[0]/newVerticesCoord[3];
			newVerticesCoord[1] = newVerticesCoord[1]/newVerticesCoord[3];
			newVerticesCoord[2] = newVerticesCoord[2]/newVerticesCoord[3];
			
			this.worldX = newVerticesCoord[0];
			this.worldY = newVerticesCoord[1];

		}

	}

}
