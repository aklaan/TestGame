package com.example.testgame.gameobjects;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import com.example.testgame.GLES20Renderer;
import com.example.testgame.gamecomponents.GameObject;
import com.example.testgame.gamecomponents.OpenGLActivity;
import com.example.testgame.gamecomponents.Rectangle2D;
import com.example.testgame.gamecomponents.Shader_simple;
import com.example.testgame.gamecomponents.Vertex;


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
	public void draw(GLES20Renderer renderer) {

		Shader_simple sh= (Shader_simple) renderer.mShaderProvider.getShaderByName("simple");
		GLES20.glUseProgram(sh.mGLSLProgram_location);

		// on se positionne au debut du Buffer des indices
		// qui indiquent dans quel ordre les vertex doivent �tre dessin�s
		this.getIndices().position(0);
		sh.alimVertices(this.getVertices());
		//GLES20.glVertexAttribPointer(sh.attrib_vertex_coord_location, 3,
		//		GLES20.GL_FLOAT, false, Vertex.Vertex_COORD_SIZE_BYTES,
		//		this.getVertices());

		this.getVertices().position(0);
		
		float[] color =  {0.5f,0.5f,0.5f,0.5f};
		
		
		//if (sh.attrib_color_location != -1) {
        //    this.getVertices().position(0);
         //   GLES20.glVertexAttribPointer(sh.attrib_color_location, 4, GLES20.GL_FLOAT, false, Vertex.Vertex_TEXT_SIZE_BYTES, color);

		
		
		  
		  GLES20.glVertexAttribPointer(sh.attrib_texture_coord_location, 2, GLES20.GL_FLOAT,
		  false, Vertex.Vertex_TEXT_SIZE_BYTES, this.getTextCoord());
		 

		
		 sh.enableShaderVar();

				
		
		
		// �quivalent du PUSH
		this.mBackupModelView = renderer.mModelView.clone();
		float[] mMvp = new float[16];
		float[] wrkmodelView = new float[16];

		Matrix.setIdentityM(renderer.mModelView, 0);
		wrkmodelView = renderer.mModelView.clone();

		// on applique la transfo command�e en update

		Matrix.setRotateEulerM(mRotationMatrix, 0, 0, 0, angleRAD + 10.0f);

		Matrix.multiplyMM(renderer.mModelView, 0, wrkmodelView, 0,
				this.mRotationMatrix, 0);

		// Matrix.translateM(mModelView, 0, X, Y, 0);
		// Matrix.setRotateEulerM(mRotationMatrix, 0, 0, 0, angleRAD);

		// wrkmodelView = mModelView.clone();
		// Matrix.multiplyMM(mModelView, 0, wrkmodelView, 0,
		// this.mRotationMatrix, 0);

		angleRAD = angleRAD + 0.05F;
		/**
		 * if (this.cible != null) {
		 * 
		 * Matrix.translateM(renderer.mModelView, 0, (float) (cible.X - (float)
		 * renderer.mModelView[12] + (Math.cos(angleRAD) * 50.0f)), (float)
		 * (cible.Y - (float) renderer.mModelView[13] + (Math .sin(angleRAD) *
		 * 50.0f)), 0);
		 * 
		 * // Log.i(this.getTagName(),"X:"+String.valueOf(this.X) + //
		 * "- Y:"+String.valueOf(this.Y) + //
		 * "- CibleX:"+String.valueOf(cible.X) + //
		 * " - CibleY:"+String.valueOf(cible.Y));
		 * 
		 * }
		 */
		Matrix.multiplyMM(mMvp, 0, renderer.mProjectionView, 0,
				renderer.mModelView, 0);

		// Log.i("draw-"+this.getTagName(),"X:"+String.valueOf(this.X) +
		// "- Y:"+String.valueOf(this.Y) );

		this.X = renderer.mModelView[12];
		this.Y = renderer.mModelView[13];

		// on alimente la donn�e UNIFORM mAdressOf_Mvp du programme OpenGL
		// avec
		// une matrice de 4 flotant.

		
		// ici on alimente une variable globale OPENGL
		// OPENGL va conserver sa valeur dans son propre r�f�rentiel
		
		GLES20.glUniformMatrix4fv(sh.uniform_mvp_location, 1, false, mMvp, 0);

		// GLES20.glUniformMatrix4fv(renderer.mProgramme1.mAdressOf_Mvp, 1,
		// false, mMvp, 0);
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
		GLES20.glDrawElements(drawMode, this.getIndices().capacity(),
				GLES20.GL_UNSIGNED_SHORT, this.getIndices());

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
