package com.example.testgame;

import java.util.ArrayList;

import android.app.Activity;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.example.testgame.gamecomponents.BitmapProvider;
import com.example.testgame.gamecomponents.CollisionControler;
import com.example.testgame.gamecomponents.GameObject;
import com.example.testgame.gamecomponents.OpenGLActivity;
import com.example.testgame.gamecomponents.ProgramShaderProvider;
import com.example.testgame.gamecomponents.Rectangle2D;
import com.example.testgame.gamecomponents.SAT;
import com.example.testgame.gameobjects.PetitRobot;
import com.example.testgame.gameobjects.ProgramShader_forLines;
import com.example.testgame.gameobjects.ProgramShader_grille;
import com.example.testgame.gameobjects.ProgramShader_simple;
import com.example.testgame.gameobjects.Starship;

/**
 * GLES20Renderer: the OGLES 2.0 Thread.
 */
public class GLES20RendererScene01 implements GLSurfaceView.Renderer {

	public final static String TAG_ERROR = "CRITICAL ERROR";

	public static int mTex0;
	public OpenGLActivity mActivity;
	// public DefaultProgramShader mProgramme1;
	public ProgramShaderProvider mProgramShaderProvider;
	public ArrayList<GameObject> mGameObjectList;
	
	// ! Matrix Model View Projection
	public float[] mCurrentMvp = new float[16];

	// ! matrice de transformation des objets
	public float[] mModelView = new float[16];

	// ! matrice de transformation des objets
	public float[] mProjectionView = new float[16];

	// public ShaderProvider mShaderProvider;

	public GLES20RendererScene01(OpenGLActivity activity) {
		mActivity = activity;

	}

	// @Override
	public void onSurfaceCreated(GL10 gl2, EGLConfig eglConfig) {

		// on charge les textures necessaires à la scène
		loadTextures();

		// on ne peux pas créer de programe Shader en dehors du contexte
		// opengl. donc le provider est à recréer à chaque load de la scène
		this.mProgramShaderProvider = new ProgramShaderProvider(mActivity);
		initProgramShader();

		// on initialise la liste des objets qui serront contenus dans
		// la scène.
		mGameObjectList = new ArrayList<GameObject>();
		loadGameObjects();

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

	}

	// @Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// lorsqu'il y a une modification de la vue , on redéfinie la nouvelle
		// taille de la vue (par exemple quand on incline le téléphone et
		// que l'on passe de la vue portait à la vue paysage

		Log.i("", String.valueOf(width) + "/" + String.valueOf(height));

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
		
		Matrix.orthoM(mProjectionView, 0,
				0 ,mActivity.getXScreenLimit() ,
				0,mActivity.getYScreenLimit() ,
				-10.f, 10.f);

		
		
		/**
		 * Matrix.orthoM(mProjectionView , 0 ,-0 , (width /
		 * mActivity.getZoomFactor()) ,-0 , (height /
		 * mActivity.getZoomFactor()), -10.f, 10.f);
		 */
	}

	// @Override
	public void onDrawFrame(GL10 gl) {
		// on commence par effacer l'écran en le remplissant de la
		// couleur souhaitée et on vide le buffer.
		GLES20.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

		//on check les colissions entre tous les éléments de la scène
		CollisionControler.checkAllCollisions(mGameObjectList);
		// Matrix.setIdentityM(this.mModelView, 0);

		for (GameObject gameObject : this.mGameObjectList) {
			gameObject.onUpdate(mActivity);

			// si l'objet doit être visible, on va
			// tenter de le dessiner.
			if (gameObject.isVisible) {

				// si l'objet possède une texture, on la place dans l'unité
				// de texture
				if (gameObject.hasTexture) {
					mActivity.mBitmapProvider.putTextureToGLUnit(
							gameObject.mTexture, 0);
				}

				gameObject.draw(this);

			
				// SystemClock.sleep(2000);

			}

		}

	}

	public void loadGameObjects() {
		Rectangle2D ligne1 = new Rectangle2D(Enums.drawMode.FILL);
		ligne1.setCoord(0, 0);
		ligne1.setHeight(500);
		ligne1.setWidth(2);
		ligne1.setTagName("ligne1");
		this.mActivity.mBitmapProvider.assignTexture(
				this.mActivity.getString(R.string.textureRed), ligne1);

		this.mGameObjectList.add(ligne1);

		//**********
		Rectangle2D ligne2 = new Rectangle2D(Enums.drawMode.FILL);
		ligne2.setCoord(0, 0);
		ligne2.setHeight(2);
		ligne2.setWidth(1000);
		ligne2.setTagName("ligne2");
		this.mActivity.mBitmapProvider.assignTexture(
				this.mActivity.getString(R.string.textureRed), ligne2);

		mGameObjectList.add(ligne2);

		//******************
		Starship mStarship = new Starship();

		mStarship.setHeight(10);
		mStarship.setWidth(10);
		mStarship.setCoord(110, 90);
		mStarship.angleRAD = 0.0f;
		mStarship.setTagName("starship1");
		mStarship.enableColission();
		this.mActivity.mBitmapProvider.assignTexture(
				this.mActivity.getString(R.string.boulerouge), mStarship);

		mGameObjectList.add(mStarship);

		//***********************
		Starship mStarship2 = new Starship();
		mStarship2.setHeight(5);
		mStarship2.setWidth(5);
		mStarship2.enableColission();
		this.mActivity.mBitmapProvider.assignTexture(
				this.mActivity.getString(R.string.boulerouge), mStarship2);
		mStarship2.setTagName("starship2");
		mStarship2.cible = mStarship;
		mStarship2.angleRAD = 45.0f;
		mGameObjectList.add(mStarship2);

		//*********************************
		PetitRobot mPetitRobot = new PetitRobot();
		mPetitRobot.setCoord(50, 50);
		mPetitRobot.setHeight(30);
		mPetitRobot.setWidth(30);
		mPetitRobot.enableColission();
		this.mActivity.mBitmapProvider.assignTexture(
				this.mActivity.getString(R.string.textureRobot), mPetitRobot);

		mGameObjectList.add(mPetitRobot);

		
		
		
		//mStarship.cible = mPetitRobot;

		mStarship2.cible = mStarship;

	}

	public void initProgramShader() {
		ProgramShader_grille shader_grille = new ProgramShader_grille();
		shader_grille.make();
		this.mProgramShaderProvider.add(shader_grille);

		ProgramShader_simple shader_simple = new ProgramShader_simple();
		shader_simple.make();
		this.mProgramShaderProvider.add(shader_simple);

		ProgramShader_forLines shader_forLines = new ProgramShader_forLines();
		shader_forLines.make();
		this.mProgramShaderProvider.add(shader_forLines);
		
	}

	public void loadTextures() {

		this.mActivity.mBitmapProvider.add(this.mActivity
				.getString(R.string.textureStarship));
		this.mActivity.mBitmapProvider.add(this.mActivity
				.getString(R.string.textureRobot));
		this.mActivity.mBitmapProvider.add(this.mActivity
				.getString(R.string.textureRed));
		this.mActivity.mBitmapProvider.add(this.mActivity
				.getString(R.string.boulerouge));

	}

}
