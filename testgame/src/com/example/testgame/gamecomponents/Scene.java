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
	private static int glbufferTextureID;
	public OpenGLActivity mActivity;

	public ProgramShaderProvider mProgramShaderProvider;
	public BitmapProvider mBitmapProvider;
	private ArrayList<GameObject> mGameObjectList;

	public final float[] mVMatrix = new float[16];

	public final float[] mVMatrixORTH = new float[16];

	
	// Matrice de projection de la vue
	public float[] mProjectionView = new float[16];

	//matrice de projection orthogonale
	public float[] mProjectionORTH = new float[16];

	
	// public ShaderProvider mShaderProvider;

	public Scene(OpenGLActivity activity) {

		mActivity = activity;
		// le bitmap provider peu servir pour plusieurs scene
		// on le remonte donc au plus haut.
		this.mBitmapProvider = new BitmapProvider(this.getActivity());
		this.mGameObjectList = new ArrayList<GameObject>();
		this.mProgramShaderProvider = new ProgramShaderProvider(mActivity);

		this.preLoading();

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

	private void preLoading() {
		// on charge les textures necessaires à la scène
		loadTextures();
		// on initialise la liste des objets qui serront contenus dans
		// la scène.

		loadGameObjects();
	}

	// @Override
	public void onSurfaceCreated(GL10 gl2, EGLConfig eglConfig) {

		// on ne peux pas créer de programe Shader en dehors du contexte
		// opengl. donc le provider est à recréer à chaque load de la scène

		initProgramShader();

		// on active le texturing 2D
		GLES20.glEnable(GLES20.GL_TEXTURE_2D);

		// Activattion de la gestion de l'Alpha

		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		GLES20.glEnable(GLES20.GL_BLEND);

		// create texture handle
		int[] textures = new int[1];

		// on génère un buffer texture utilisable par OPENGL
		GLES20.glGenTextures(1, textures, 0);

		glbufferTextureID = textures[0];

		// on demande à opengl d'utiliser la première texture
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
		// lorsqu'il y a une modification de la vue , on redéfinie la nouvelle
		// taille de la vue (par exemple quand on incline le téléphone et
		// que l'on passe de la vue portait à la vue paysage

		// le coin en bas à gauche est 0,0
		// la taille de la surface est la même que l'écran

		GLES20.glViewport(0, 0, width, height);

		float ratio = (float) width / height;
		//* pour un affichage Perspective *********************
		// le premier plan de clipping NEAR est défini par
		// 2 points : le point du bas à gauche et le point du haut à droite
		// le point du bas à gauche est à -ratio, -1
		// le point du haut à gauche est à ratio, 1
		// le plan de clipping NEAR est à 1 et le second plan est à 1000.
		
		/**avec cette version le centre est au milieu de l'écran */
		Matrix.frustumM(mProjectionView, 0, -ratio, ratio, -1, 1, 1, 1000);

		/**avec cette version le centre est en bas à gauche de l'écran */
		//Matrix.frustumM(mProjectionView, 0, 0, ratio, 0, 1, 1, 1000);
		
		// Set the camera position (View matrix)
		// le centre de la caméra est en 0,0,-200 (oeuil)
		// la caméra regarde le centre de l'écran 0,0,0
		// le vecteur UP indique l'orientation de la caméra (on peu tourner la
		// caméra)
		//Matrix.setLookAtM(rm, rmOffset, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ)
		Matrix.setLookAtM(mVMatrix, 0, 0, 0, -200, 0, 0f, 0f, 0f, 1.0f, 0.0f);

		//* pour un affichage Orthogonal *********************
		// le (0,0) est en bas à gauche.
		Matrix.orthoM(mProjectionORTH, 0, -0, width, 0, height, -10.f, 10.f);
		Matrix.setIdentityM(mVMatrixORTH, 0);

	}

	// @Override
	public void onDrawFrame(GL10 gl) {
		float startDrawingTime = SystemClock.currentThreadTimeMillis();

		// on commence par effacer l'écran en le remplissant de la
		// couleur souhaitée et on vide le buffer.
		GLES20.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

		// Calculate the projection and view transformation
		// Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);

		// on check les colissions entre tous les éléments de la scène
		CollisionControler.checkAllCollisions(mGameObjectList);

		// Pour chaques GameObject de la scène, on appelle
		// la mise à jour et on le dessine s'il est visible.
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
		// SystemClock.sleep(2000);
	}

	/**
	 * fonction où on charge les objets de la scène dans la phase de Loading
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
		for (GameObject go : GameObjectList) {
			go.mScene = this;
		}
		this.mGameObjectList.addAll(GameObjectList);
	}

	public GameObject getGameObjectByTag(int tagId) {
		GameObject result = null;
		for (GameObject gameObject : this.mGameObjectList) {
			// Log.i("info : ", gameObject.getTagName());
			if (gameObject.getTagName() == tagId) {
				result = gameObject;
			}

		}
		return result;
	}

	public int getHeight() {
		DisplayMetrics metrics = this.getActivity().getResources()
				.getDisplayMetrics();
		return metrics.heightPixels;
	}

	public int getWidth() {
		DisplayMetrics metrics = this.getActivity().getResources()
				.getDisplayMetrics();
		return metrics.widthPixels;
	}

}
