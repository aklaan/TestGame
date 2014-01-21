package com.example.testgame.gameobjects;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import com.example.testgame.GLES20Renderer;
import com.example.testgame.gamecomponents.GameObject;
import com.example.testgame.gamecomponents.OpenGLActivity;
import com.example.testgame.gamecomponents.Rectangle2D;

public class Starship extends Rectangle2D {

	private float angle;

	public Starship() {
		super();
this.setTagName("starship");
this.isStatic=false;

	}

	@Override
	public void onUpdate(OpenGLActivity activity) {
		if (activity.mGLSurfaceView.touched) {
			float [] wrkmodelView = new float[16];
			wrkmodelView = mModelView.clone();
			//Matrix.multiplyMM(mModelView, 0, wrkmodelView, 0, this.getRotation(0,0,0.5f),0);
		}
		
		for (GameObject go : this.mCollideWithList) {
			Log.i("starship", "i'm collide with : " + go.getTagName());
			
		}

	}

	@Override
	public void draw(GLES20Renderer GLES20Renderer){
	
		// A FAIRE....
		// - activer le bon shader si cet objet n'utilise pas celui en cours
		GLES20Renderer.mProgramme1.enableVertexAttribArray(this);
		
		float [] mMvp = new float[16];
		float [] wrkmodelView = new float[16];
		Matrix.setIdentityM(wrkmodelView, 0);
		wrkmodelView = mModelView.clone();
	//	Matrix.multiplyMM(mModelView, 0, wrkmodelView, 0, this.getRotation(0,0,0.05f),0);
		angle=angle+0.05F;
		this.rotate(1, 10, angle);
		wrkmodelView = mModelView.clone();
		//Matrix.multiplyMM(mModelView, 0, wrkmodelView, 0, this.mRotationMatrix,0);
		Matrix.translateM(wrkmodelView, 0, this.getCoordX(),this.getCoordY(), 0);
	
		Matrix.multiplyMM(mMvp, 0, GLES20Renderer.mProjectionView, 0, wrkmodelView, 0);
		// on alimente la donnée UNIFORM mAdressOf_Mvp du programme OpenGL
		// avec
		// une matrice de 4 flotant.
		GLES20.glUniformMatrix4fv(GLES20Renderer.mProgramme1.mAdressOf_Mvp, 1, false,
				mMvp, 0);
	
	
	
		// on se positionne au debut du Buffer des indices
				// qui indiquent dans quel ordre les vertex doivent être dessinés
				this.getIndices().position(0);

				GLES20.glDrawElements(GLES20.GL_TRIANGLES, this.getIndices().capacity(),
						GLES20.GL_UNSIGNED_SHORT, this.getIndices());

				GLES20Renderer.mProgramme1.disableVertexAttribArray();
	
	}
	
}
