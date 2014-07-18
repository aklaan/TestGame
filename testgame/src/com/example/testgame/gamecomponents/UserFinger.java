package com.example.testgame.gamecomponents;

import java.util.ArrayList;

import android.opengl.Matrix;
import android.util.Log;

import com.example.testgame.R;

public class UserFinger extends Rectangle2D {

	float worldX;
	float worldY;

	public UserFinger() {
		super(DrawingMode.EMPTY);
		this.setheight(10);
		this.setWidth(10);
		this.enableColission();
		this.mCollisionBox.isVisible = true;
		this.setTagName(R.string.USER_FINGER);
		this.isStatic = false;
	}

	public void onUpdate() {
		// si l'utilisateur touche l'écran
		if (this.getScene().getActivity().mGLSurfaceView.touched) {

			// on met à jour les coordonées
			this.setCoord(
					this.getScene().getActivity().mGLSurfaceView.touchX,
					this.getScene().getHeight()
							- this.getScene().getActivity().mGLSurfaceView.touchY);


			
			
			// on active les colissions
			this.canCollide = true;
			getWorldCoord();
			
			Log.i("UserFinger",
					String.valueOf(this.X) + "/" + String.valueOf(this.Y));

			Log.i("UserFinger World",
					String.valueOf(this.worldX) + "/" + String.valueOf(this.worldY));
			Log.i("--","----------------------------------------------------------------");
		} else {
			// sinon les collisions sont désactivées
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
		
		// je suis obligé de passer par un vecteur 4 pour la multiplication

		for (int i = 0; i < this.mVertices.size(); i++) {
			oldVerticesCoord[0] = this.X; // x
			oldVerticesCoord[1] = this.Y; // y
			oldVerticesCoord[2] = 0; // z
			oldVerticesCoord[3] = 1.f;

			Matrix.multiplyMV(newVerticesCoord, 0, reverseMatrix, 0,
					oldVerticesCoord, 0);
			
			this.worldX = newVerticesCoord[0];
			this.worldY = newVerticesCoord[1];

		}

	}

}
