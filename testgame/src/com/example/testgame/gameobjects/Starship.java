package com.example.testgame.gameobjects;

import android.content.Context;
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
		
				
		if (activity.mGLSurfaceView.touched) {
			this.angleRAD += Math.PI;

			
		}

		for (GameObject go : this.mCollideWithList) {
			Log.i("starship", "i'm collide with : " + go.getTagName());

		}

	}

	@Override
	public void draw(float[] ModelView) {
		
		//équivalent du PUSH
		this.mBackupModelView = ModelView.clone();
		float[] mMvp = new float[16];
		float[] wrkmodelView = new float[16];

		wrkmodelView = ModelView.clone();
		
		//on applique la transfo commandée en update
		
		// Matrix.setRotateEulerM(mRotationMatrix, 0, 0, 0, angleRAD + 10.0f);
		
		// Matrix.multiplyMM(renderer.mModelView, 0, wrkmodelView, 0,
	//	 this.mRotationMatrix,0);

		// Matrix.translateM(mModelView, 0, X, Y, 0);
		// Matrix.setRotateEulerM(mRotationMatrix, 0, 0, 0, angleRAD);

		// wrkmodelView = mModelView.clone();
		// Matrix.multiplyMM(mModelView, 0, wrkmodelView, 0,
		// this.mRotationMatrix, 0);

		 angleRAD = angleRAD + 0.05F;

		 
		 
		 if (this.cible != null) {
		
				Matrix.translateM(ModelView, 0
						, (float) (cible.X-(float)ModelView[12] +(Math.cos(angleRAD)*50.0f))
						, (float) (cible.Y-(float)ModelView[13] +(Math.sin(angleRAD)*50.0f))
						, 0);
			 
			
			//Log.i(this.getTagName(),"X:"+String.valueOf(this.X) + "- Y:"+String.valueOf(this.Y) + "- CibleX:"+String.valueOf(cible.X) + " - CibleY:"+String.valueOf(cible.Y));
			
			}

		 
		 
		 
	//	Matrix.multiplyMM(mMvp, 0, renderer.mProjectionView, 0,
		//		renderer.mModelView, 0);

		//Log.i("draw-"+this.getTagName(),"X:"+String.valueOf(this.X) + "- Y:"+String.valueOf(this.Y) );

		
		
		this.X = ModelView[12];
		this.Y = ModelView[13];

		// on alimente la donnée UNIFORM mAdressOf_Mvp du programme OpenGL
		// avec
		// une matrice de 4 flotant.
		GLES20.glUniformMatrix4fv(renderer.mProgramme1.mAdressOf_Mvp, 1,
				false, mMvp, 0);

		// on se positionne au debut du Buffer des indices
		// qui indiquent dans quel ordre les vertex doivent être dessinés
		this.getIndices().position(0);

		GLES20.glDrawElements(drawMode, this.getIndices().capacity(),
				GLES20.GL_UNSIGNED_SHORT, this.getIndices());

		renderer.mProgramme1.disableVertexAttribArray();
	
		 
		
		//équivalent du POP
		renderer.mModelView = this.mBackupModelView.clone();
		this.mBackupModelView = renderer.mModelView.clone();
	}

}
