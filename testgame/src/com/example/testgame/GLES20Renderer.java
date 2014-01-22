package com.example.testgame;

import android.app.Activity;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.example.testgame.gamecomponents.CollisionControler;
import com.example.testgame.gamecomponents.DefaultProgramShader;
import com.example.testgame.gamecomponents.GameObject;
import com.example.testgame.gamecomponents.Rectangle2D;
import com.example.testgame.gameobjects.Starship;
import com.example.testgame.gameobjects.PetitRobot;

import java.util.ArrayList;

/**
 * GLES20Renderer: the OGLES 2.0 Thread.
 */
public class GLES20Renderer implements GLSurfaceView.Renderer {

	public final static String TAG_ERROR = "CRITICAL ERROR";

	public static int mTex0;
	public MainActivity mActivity;
	public DefaultProgramShader mProgramme1;
	
	// ! Matrix Model View Projection
	public float[] mMvp = new float[16];

	// ! Matrix Model View Projection utiliée pour dessiner
	private float[] mSavedModelView = new float[16];

	// ! matrice de transformation des objets
	public float[] mModelView = new float[16];

	// ! matrice de transformation des objets
	public float[] mProjectionView = new float[16];

	public GLES20Renderer(Activity activity) {
		mActivity = (MainActivity) activity;

	}

	// @Override
	public void onSurfaceCreated(GL10 gl2, EGLConfig eglConfig) {

		// on déclare un nouveau programme GLSL
		mProgramme1 = new DefaultProgramShader(mActivity);

		// on construit et compile le programme
		mProgramme1.make();

		

		// on active le texturing 2D
		GLES20.glEnable(GLES20.GL_TEXTURE_2D);

		// create texture handle
		int[] textures = new int[1];

		// on génère un buffer texture utilisable par OPENGL
		GLES20.glGenTextures(1, textures, 0);

		mTex0 = textures[0];

		// on demande à opengl d'utiliser la première texture
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTex0);

		// set parameters
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);

		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
				GLES20.GL_CLAMP_TO_EDGE);

		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
				GLES20.GL_CLAMP_TO_EDGE);

		use();

		
	
	}

	// @Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// lorsqu'il y a une modification de la vue , on redéfinie la nouvelle
		// taille de la vue (par exemple quand on incline le téléphone et
		// que l'on passe de la vue portait à la vue paysage
		GLES20.glViewport(0, 0, width, height);
		mActivity.setXScreenLimit(width);
		mActivity.setYScreenLimit(height);
		Matrix.orthoM(mProjectionView, 0,
				-(mActivity.getXScreenLimit() / mActivity.getZoomFactor()),
				(mActivity.getXScreenLimit() / mActivity.getZoomFactor()),
				-(mActivity.getYScreenLimit() / mActivity.getZoomFactor()),
				(mActivity.getYScreenLimit() / mActivity.getZoomFactor()),
				-10.f, 10.f);

		// le (0,0) est en bas à gauche.
	/**
		  Matrix.orthoM(mProjectionView , 0 ,-0 , (width / mActivity.getZoomFactor()) ,-0 ,
		  (height / mActivity.getZoomFactor()), -10.f, 10.f);
		*/ 
	}

	// @Override
	public void onDrawFrame(GL10 gl) {
		// mTimer.addMark();
		// mTimer.logFPS(); // on veut mesurer les fps

		GLES20.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

		// ici on peu demander à dessiner
		// en mode points GL_POINTS ,GL_LINES, GL_TRIANGLES

		// CollisionControler.checkAllCollisions(mGameObjectList);

		for (GameObject gameObject : mActivity.mGameObjectList) {
			gameObject.onUpdate(mActivity);

			if (gameObject.isVisible) {

				if (gameObject.hasTexture) {
					mActivity.mBitmapProvider.putTextureToGLUnit(
							gameObject.mTexture, 0);
				}

				gameObject.draw(this);
			}
		}
	}

	void use() {
		// use program
		GLES20.glUseProgram(mProgramme1.mAdressOf_GLSLProgram);

		if (mProgramme1.mAdressOf_Mvp != -1) {
			// Log.d(this.getClass().getName(),"setMvp");
			// counter += 1.f;
			// on calcule la matrice "mRotation" a utiliser pour pivoter
			// d'un angle de x radian
			// ici l'angle c'est counter
			// le pivot est au centre à 0,0,0
			// Matrix.setRotateEulerM(mRotation, 0, 0.f, 0.f, counter);

			// on calcule la nouvelle matrice de projection mMvp
			// Matrix.multiplyMM(mMvp, 0, mProjection, 0, mRotation, 0);
			// Log.i("mMvp use",String.valueOf(mMvp[0]));

			// on alimente la donnée UNIFORM mAdressOf_Mvp du programme OpenGL
			// avec
			// une matrice de 4 flotant
			GLES20.glUniformMatrix4fv(mProgramme1.mAdressOf_Mvp, 1, false,
					mMvp, 0);
		}

		if (mProgramme1.mAdressOf_Texture0 != -1) {
			GLES20.glEnable(GLES20.GL_TEXTURE_2D);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, GLES20Renderer.mTex0);
			GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
			// on alimente la donnée UNIFORM mAdressOf_Texture0 avc un integer 0
			GLES20.glUniform1i(mProgramme1.mAdressOf_Texture0, 0);
		}
	}

	public void drawaaaaa(GameObject gameobject, int drawingMode) {
		// appel la fonction qui passe à enable toutes les variables des shaders
		// pour rappel, si les variables ne sont pas "enable" elle ne sont
		// pas prise en compte dans les shaders

		Matrix.setIdentityM(mModelView, 0);
		
		// théoriquement pas la peine de le faire à chaque frame...
		mProgramme1.enableVertexAttribArray(gameobject);

		if (mProgramme1.mAdressOf_Mvp != -1) {

			mSavedModelView = mModelView.clone();

	
			// on calcule la nouvelle matrice de projection qui serra utilisé
			// par le shader.

			
			float [] wrkmodelView = new float[16];
			wrkmodelView = mModelView.clone();
				Matrix.multiplyMM(mModelView, 0, wrkmodelView, 0, gameobject.mRotationMatrix,0);
				gameobject.rotation=false;
			
				Matrix.translateM(mModelView, 0, gameobject.getCoordX(),
						gameobject.getCoordY(), 0);
			// Matrix.multiplyMM(mSavedModelView, 0, mMvp, 0,
			// gameobject.mModelMatrix, 0);
			Matrix.multiplyMM(mMvp, 0, mProjectionView, 0, mModelView, 0);
			// on alimente la donnée UNIFORM mAdressOf_Mvp du programme OpenGL
			// avec
			// une matrice de 4 flotant.
			GLES20.glUniformMatrix4fv(mProgramme1.mAdressOf_Mvp, 1, false,
					mMvp, 0);
		}

		// on se positionne au debut du Buffer des indices
		// qui indiquent dans quel ordre les vertex doivent être dessinés
		gameobject.getIndices().position(0);

		GLES20.glDrawElements(drawingMode, gameobject.getIndices().capacity(),
				GLES20.GL_UNSIGNED_SHORT, gameobject.getIndices());

		mProgramme1.disableVertexAttribArray();

	}

}
