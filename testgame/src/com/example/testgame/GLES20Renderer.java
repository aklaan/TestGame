package com.example.testgame;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.example.testgame.gameobjects.Square;
import com.example.testgame.gameobjects.Square2;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

/**
 * GLES20Renderer: the OGLES 2.0 Thread.
  */
public class GLES20Renderer implements GLSurfaceView.Renderer {

	public final static String TAG_ERROR = "CRITICAL ERROR";

	public final static int MAX_POINTS = 6;

	public static int mTex0;

	private MainActivity mActivity;

	private GLSLProgram mProgramme1;

	private ArrayList<GameObject> mGameObjectList;

	
	
	GLES20Renderer(Activity activity) {
		mActivity = (MainActivity) activity;
		
	}

	// @Override
	public void onSurfaceCreated(GL10 gl2, EGLConfig eglConfig) {
	
		// on déclare un nouveau programme GLSL
		mProgramme1 = new GLSLProgram(mActivity, "simple_shader");

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

			
		mProgramme1.use();
	
		
		
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(mActivity.getAssets().open(
					"spaceship.png"));
		} catch (IOException e) {
			Log.e(this.getClass().getName(), "texture not found");
			return;
		}
		
		
		Square mSquare = new Square();
		mActivity.mBitmapProvider.assignTexture("spaceship.png", mSquare);
		//mSquare.setTexture(bitmap);
		
		//mActivity.mBitmapProvider.putTextureToGLUnit(mSquare.mTexture, 0);
		mGameObjectList.add(mSquare);
		
		Square2 mSquare2 = new Square2();
		mActivity.mBitmapProvider.assignTexture("texture.png", mSquare2);
		//mSquare2.setTexture(bitmap);
		//mSquare2.translate(-2.0f, 2.0f);
		mGameObjectList.add(mSquare2);
	}

	// @Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		//lorsqu'il y a une modification de la vue , on redéfinie la nouvelle
		// taille de la vue (par exemple quand on incline le téléphone et 
		// que l'on passe de la vue portait à la vue paysage
		GLES20.glViewport(0, 0, width, height);
	}

	// @Override
	public void onDrawFrame(GL10 gl) {
		//mTimer.addMark();
		//mTimer.logFPS(); // on veut mesurer les fps

		GLES20.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

	
		
		// ici on peu demander à dessiner
		// en mode points GL_POINTS ,GL_LINES, GL_TRIANGLES
		
	
		
		for (GameObject go : mGameObjectList){
			go.onUpdate();
			mActivity.mBitmapProvider.putTextureToGLUnit(go.mTexture, 0);
			mProgramme1.draw(go, GLES20.GL_TRIANGLES);	
		}
	  	
	//	mProgramme1.draw(mSquare, GLES20.GL_POINTS, MAX_POINTS);
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
}
