package com.example.testgame.gameobjects;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import com.example.testgame.GLES20Renderer;
import com.example.testgame.gamecomponents.GameObject;
import com.example.testgame.gamecomponents.OpenGLActivity;
import com.example.testgame.gamecomponents.Rectangle2D;

public class Starship extends Rectangle2D {

	public GameObject cible;

	public Starship() {
		super();
		this.setTagName("starship");
		this.isStatic = false;

	}

	@Override
	public void onUpdate(OpenGLActivity activity) {
		
		if (this.cible != null) {
			this.turnArround(cible, 0.5f, 150f);
		
		Log.i(this.getTagName(),"X:"+String.valueOf(this.X) + "- Y:"+String.valueOf(this.Y) + "- CibleX:"+String.valueOf(cible.X) + " - CibleY:"+String.valueOf(cible.Y));
		
		}
		
		if (activity.mGLSurfaceView.touched) {
			this.angleRAD += Math.PI;

			
		}

		for (GameObject go : this.mCollideWithList) {
			Log.i("starship", "i'm collide with : " + go.getTagName());

		}

	}

	@Override
	public void draw(GLES20Renderer GLES20Renderer) {

		// A FAIRE....
		// - activer le bon shader si cet objet n'utilise pas celui en cours
		GLES20Renderer.mProgramme1.enableVertexAttribArray(this);

		Matrix.setIdentityM(mModelView, 0);
		float[] mMvp = new float[16];
		float[] wrkmodelView = new float[16];

		wrkmodelView = this.mModelView.clone();
		
		Matrix.multiplyMM(this.mModelView, 0, wrkmodelView, 0,
				this.mTransformUpdateView, 0);
		// this.turnArround(GLES20Renderer.mActivity.mGameObjectList.get(5),
		// 0.5f, 300f);

		// Matrix.setRotateEulerM(mRotationMatrix, 0, 0, 0, angleRAD + 10.0f);
		// wrkmodelView = mModelView.clone();
		// Matrix.multiplyMM(mModelView, 0, wrkmodelView, 0,
		// this.mRotationMatrix,0);

		// Matrix.translateM(mModelView, 0, X, Y, 0);
		// Matrix.setRotateEulerM(mRotationMatrix, 0, 0, 0, angleRAD);

		// wrkmodelView = mModelView.clone();
		// Matrix.multiplyMM(mModelView, 0, wrkmodelView, 0,
		// this.mRotationMatrix, 0);

		// angleRAD = angleRAD + 0.5F;

		Matrix.multiplyMM(mMvp, 0, GLES20Renderer.mProjectionView, 0,
				mModelView, 0);

		Log.i("draw-"+this.getTagName(),"X:"+String.valueOf(this.X) + "- Y:"+String.valueOf(this.Y) );

		
		
		this.X = mModelView[12];
		this.Y = mModelView[13];

		// on alimente la donnée UNIFORM mAdressOf_Mvp du programme OpenGL
		// avec
		// une matrice de 4 flotant.
		GLES20.glUniformMatrix4fv(GLES20Renderer.mProgramme1.mAdressOf_Mvp, 1,
				false, mMvp, 0);

		// on se positionne au debut du Buffer des indices
		// qui indiquent dans quel ordre les vertex doivent être dessinés
		this.getIndices().position(0);

		GLES20.glDrawElements(drawMode, this.getIndices().capacity(),
				GLES20.GL_UNSIGNED_SHORT, this.getIndices());

		GLES20Renderer.mProgramme1.disableVertexAttribArray();

	}

}
