package com.example.testgame.gamecomponents;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Application;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;

public class Scene implements GLSurfaceView.Renderer {

	public final static String TAG_ERROR = "CRITICAL ERROR";
private float height;
private float width;

	
	private static int glbufferTextureID;
	public OpenGLActivity mActivity;
	
	public ProgramShaderProvider mProgramShaderProvider;
	public BitmapProvider mBitmapProvider;
	private ArrayList<GameObject> mGameObjectList;

	// Matrice de projection de la vue
	public float[] mProjectionView = new float[16];

	// public ShaderProvider mShaderProvider;

	public Scene(OpenGLActivity activity) {
		
	
		mActivity = activity;
		// le bitmap provider peu servir pour plusieurs scene
		// on le remonte donc au plus haut.
		this.mBitmapProvider = new BitmapProvider(this.getActivity());
		this.mGameObjectList = new ArrayList<GameObject>();
		this.mProgramShaderProvider = new ProgramShaderProvider(mActivity);
		
		UserFinger userFinger = new UserFinger();
		this.addToScene(userFinger);
	}

	public OpenGLActivity getActivity() {
		return this.mActivity;
	}

	public BitmapProvider getBitmapProvider() {
		return this.mBitmapProvider;
	}

	public ProgramShaderProvider getProgramShaderProvider() {
		return this.mProgramShaderProvider;
	}

	public float[] getProjectionView() {
		return this.mProjectionView;
	}

	// @Override
	public void onSurfaceCreated(GL10 gl2, EGLConfig eglConfig) {

		// on charge les textures necessaires � la sc�ne
		loadTextures();

		// on ne peux pas cr�er de programe Shader en dehors du contexte
		// opengl. donc le provider est � recr�er � chaque load de la sc�ne
		
		initProgramShader();

		// on initialise la liste des objets qui serront contenus dans
		// la sc�ne.
		
		loadGameObjects();

		// on active le texturing 2D
		GLES20.glEnable(GLES20.GL_TEXTURE_2D);

		//Activattion de la gestion de l'Alpha
		
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		GLES20.glEnable(GLES20.GL_BLEND);

		
		// create texture handle
		int[] textures = new int[1];

		// on g�n�re un buffer texture utilisable par OPENGL
		GLES20.glGenTextures(1, textures, 0);

		glbufferTextureID = textures[0];

		// on demande � opengl d'utiliser la premi�re texture
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, glbufferTextureID);

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
		// lorsqu'il y a une modification de la vue , on red�finie la nouvelle
		// taille de la vue (par exemple quand on incline le t�l�phone et
		// que l'on passe de la vue portait � la vue paysage

		Log.i("", String.valueOf(width) + "/" + String.valueOf(height));

		GLES20.glViewport(0, 0, width, height);
		
	
		// le (0,0) est en bas � gauche.
		
		  Matrix.orthoM(mProjectionView, 0, 0 ,width ,
		  0,height, -10.f, 10.f);
		 

		/**
		 * Matrix.orthoM(mProjectionView , 0 ,-0 , (width /
		 * mActivity.getZoomFactor()) ,-0 , (height /
		 * mActivity.getZoomFactor()), -10.f, 10.f);
		 */
	}

	// @Override
	public void onDrawFrame(GL10 gl) {
		float startDrawingTime = SystemClock.currentThreadTimeMillis();

		// on commence par effacer l'�cran en le remplissant de la
		// couleur souhait�e et on vide le buffer.
		GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

		// on check les colissions entre tous les �l�ments de la sc�ne
		CollisionControler.checkAllCollisions(mGameObjectList);

		// Pour chaques GameObject de la sc�ne, on appelle
		// la mise � jour et on le dessine s'il est visible.
		for (GameObject gameObject : this.mGameObjectList) {

			gameObject.mainUpdate(mActivity);

			
			if (gameObject.isVisible) {
				gameObject.draw();
			}
		}

		float drawTimeElaps = SystemClock.currentThreadTimeMillis()
				- startDrawingTime;

		// si on a mis moins de 60 fps pour tout faire on attend pour
		// ne par surcharger le CPU
		if (drawTimeElaps < (1000 / 60)) {

			SystemClock.sleep((long) ((1000 / 60) - drawTimeElaps));
		}
		//SystemClock.sleep(2000);
	}

	/**
	 * fonction o� on charge les objets de la sc�ne dans la phase de Loading
	 * game
	 */
	public void loadGameObjects() {

	}

	/**
	 * 
	 */
	public void initProgramShader() {

	}

	public void loadTextures() {

	}

	public void addToScene(GameObject gameobject) {
		gameobject.mScene = this;
		this.mGameObjectList.add(gameobject);

	}

	
	public void addToScene(ArrayList<GameObject> GameObjectList) {
		for (GameObject go :GameObjectList ){
			go.mScene = this;
		}
		this.mGameObjectList.addAll(GameObjectList);
	}
	
	
	public GameObject getGameObjectByTag(int tagId) {
		GameObject result = null;
		for (GameObject gameObject : this.mGameObjectList) {
		//Log.i("info : ", gameObject.getTagName());
			if (gameObject.getTagName() == tagId) {
				result = gameObject;
			}

		}
		return result;
	}

	public int getHeight() {
		DisplayMetrics metrics =this.getActivity().getResources().getDisplayMetrics(); 
		return metrics.heightPixels;
	}

	

	public int getWidth() {
		DisplayMetrics metrics = this.getActivity().getResources().getDisplayMetrics();
				return metrics.widthPixels;
	}

	

}
