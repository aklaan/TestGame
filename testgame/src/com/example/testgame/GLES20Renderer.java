package com.example.testgame;

import android.app.Activity;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.example.testgame.gameobjects.Square;
import com.example.testgame.gameobjects.Square2;

import java.util.ArrayList;

/**
 * GLES20Renderer: the OGLES 2.0 Thread.
 */
public class GLES20Renderer implements GLSurfaceView.Renderer {

	public final static String TAG_ERROR = "CRITICAL ERROR";

	public final static int MAX_POINTS = 6;

	public static int mTex0;
	private MainActivity mActivity;
	private DefaultProgramShader mProgramme1;
	private ArrayList<GameObject> mGameObjectList;

	// ! Matrix Model View Projection
	private float[] mMvp = new float[16];

	// ! Matrix Model View Projection utiliée pour dessiner
	private float[] mMvp4Draw = new float[16];

	GLES20Renderer(Activity activity) {
		mActivity = (MainActivity) activity;

	}

	// @Override
	public void onSurfaceCreated(GL10 gl2, EGLConfig eglConfig) {

		
		
		// on déclare un nouveau programme GLSL
		mProgramme1 = new DefaultProgramShader(mActivity);

		// on construit et compile le programme
		mProgramme1.make();

		// on définit une liste des composants de jeu
		mGameObjectList = new ArrayList<GameObject>();

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

		Square mSquare = new Square();
		mSquare.setCoord(10, -500);
		mSquare.setHeight(100.f);
		mSquare.setWidth(100.f);
		mActivity.mBitmapProvider.assignTexture(
				mActivity.getString(R.string.starship), mSquare);
		// mSquare.setTexture(bitmap);

		// mActivity.mBitmapProvider.putTextureToGLUnit(mSquare.mTexture, 0);
		mGameObjectList.add(mSquare);

		Square2 mSquare2 = new Square2();
		
		mSquare2.setCoord(10, 30);
		mSquare2.setHeight(100.f);
		mSquare2.setWidth(100.f);
		
		mActivity.mBitmapProvider.assignTexture(
				mActivity.getString(R.string.texture), mSquare2);
		// mSquare2.setTexture(bitmap);
		// mSquare2.translate(-2.0f, 2.0f);
		mGameObjectList.add(mSquare2);
	}

	// @Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// lorsqu'il y a une modification de la vue , on redéfinie la nouvelle
		// taille de la vue (par exemple quand on incline le téléphone et
		// que l'on passe de la vue portait à la vue paysage
		GLES20.glViewport(0, 0, width, height);
         int factor = 1; 
		// float ratio = (float) width / height;
		Matrix.orthoM(mMvp, 0, -(width / factor), (width / factor),
				-(height / factor), (height / factor), -10.f, 10.f);

	}

	// @Override
	public void onDrawFrame(GL10 gl) {
		// mTimer.addMark();
		// mTimer.logFPS(); // on veut mesurer les fps

		GLES20.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

		// ici on peu demander à dessiner
		// en mode points GL_POINTS ,GL_LINES, GL_TRIANGLES

		for (GameObject gameObject : mGameObjectList) {
			gameObject.onUpdate(mActivity);

			if (gameObject.isVisible) {

				if (gameObject.hasTexture) {
					mActivity.mBitmapProvider.putTextureToGLUnit(
							gameObject.mTexture, 0);
				}

				draw(gameObject, GLES20.GL_TRIANGLES);
			}
		}
	}

	private void checkGlError(String op) {
		int error;
		while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
			Log.e(this.getClass().getSimpleName(), op + ": glError " + error);
		}
	}

	// retourne un float aléatoire entre 0 et 1
	private float getRamdom() {
		float value = (float) (Math.random() * 2. - 1.);
		return value;
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

	public void draw(GameObject gameobject, int drawingMode) {
		// appel la fonction qui passe à enable toutes les variables des shaders
		// pour rappel, si les variables ne sont pas "enable" elle ne sont
		// pas prise en compte dans les shaders

		// théoriquement pas la peine de le faire à chaque frame...
		mProgramme1.enableVertexAttribArray(gameobject);

		if (mProgramme1.mAdressOf_Mvp != -1) {

			mMvp4Draw = mMvp.clone();

			// on calcule la nouvelle matrice de projection qui serra utilisé
			// par le shader.
			Matrix.multiplyMM(mMvp4Draw, 0, mMvp, 0, gameobject.mModelMatrix, 0);
			// on alimente la donnée UNIFORM mAdressOf_Mvp du programme OpenGL
			// avec
			// une matrice de 4 flotant.
			GLES20.glUniformMatrix4fv(mProgramme1.mAdressOf_Mvp, 1, false,
					mMvp4Draw, 0);
		}

		// on se positionne au debut du Buffer des indices
		// qui indiquent dans quel ordre les vertex doivent être dessinés
		gameobject.getIndices().position(0);

		GLES20.glDrawElements(drawingMode, gameobject.getIndices().capacity(),
				GLES20.GL_UNSIGNED_SHORT, gameobject.getIndices());

		mProgramme1.disableVertexAttribArray();

	}

}
