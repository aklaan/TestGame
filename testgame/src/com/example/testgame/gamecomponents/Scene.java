package com.example.testgame.gamecomponents;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

public  class Scene implements GLSurfaceView.Renderer {

	public final static String TAG_ERROR = "CRITICAL ERROR";

	public static int mTex0;
	public OpenGLActivity mActivity;
	// public DefaultProgramShader mProgramme1;
	public ProgramShaderProvider mProgramShaderProvider;
	public BitmapProvider mBitmapProvider;
	public ArrayList<GameObject> mGameObjectList;
	
	// ! Matrix Model View Projection
	public float[] mCurrentMvp = new float[16];

	// ! matrice de transformation des objets
	public float[] mModelView = new float[16];

	// ! matrice de transformation des objets
	public float[] mProjectionView = new float[16];

	// public ShaderProvider mShaderProvider;

	public Scene(OpenGLActivity activity) {
		mActivity = activity;
		//le bitmap provider peu servir pour plusieurs scene
		//on le remonte donc au plus haut.
		this.mBitmapProvider = new BitmapProvider(this.getActivity());

	}

	public OpenGLActivity getActivity(){
		return this.mActivity;
	}
	
	public BitmapProvider getBitmapProvider(){
		return this.mBitmapProvider;
	}
	
	public ProgramShaderProvider getProgramShaderProvider(){
		return this.mProgramShaderProvider;
	}

	
	public float[] getProjectionView(){
		return this.mProjectionView;
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
		/**
		Matrix.orthoM(mProjectionView, 0,
				0 ,mActivity.getXScreenLimit() ,
				0,mActivity.getYScreenLimit() ,
				-10.f, 10.f);
*/
		
		
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
		GLES20.glClearColor(0.f, 0.f, 0.f, 1.0f);
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
					this.getBitmapProvider().putTextureToGLUnit(
							gameObject.mTexture, 0);
				}

				float startDrawingTime = SystemClock.currentThreadTimeMillis();
				gameObject.draw();
				float drawTimeElaps = SystemClock.currentThreadTimeMillis() -startDrawingTime;
			
				
				//si on a mis moins de 30 fps on attend pour
				// ne par surcharger le CPU
				if (drawTimeElaps < (1/30)){
				
					 SystemClock.sleep((long) ((1/30) - drawTimeElaps));
				}
				

			}

		}

	}

	public void loadGameObjects() {
	
	}

	public void initProgramShader() {
			
	}

	public void loadTextures() {


	}

	
	public void addToScene(GameObject gameobject){
		gameobject.mScene = this;
		this.mGameObjectList.add(gameobject);
		
	}
}


