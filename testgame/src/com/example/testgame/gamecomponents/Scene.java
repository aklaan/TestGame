package com.example.testgame.gamecomponents;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.example.testgame.MySurfaceView.ScreenEvent;
import com.example.testgame.R;

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

	// matrice de projection orthogonale
	public float[] mProjectionORTH = new float[16];

	// public ShaderProvider mShaderProvider;

	public Camera mCamera;
	public Scene(OpenGLActivity activity) {

		mActivity = activity;
		// le bitmap provider peu servir pour plusieurs scene
		// on le remonte donc au plus haut.
		this.mBitmapProvider = new BitmapProvider(this.getActivity());
		this.mGameObjectList = new ArrayList<GameObject>();
		this.mProgramShaderProvider = new ProgramShaderProvider(mActivity);
		this.mCamera = new Camera();
		mCamera.centerZ=100;
		
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
		// on charge les textures necessaires � la sc�ne
		loadTextures();
		// on initialise la liste des objets qui serront contenus dans
		// la sc�ne.

		loadGameObjects();
	}

	// @Override
	public void onSurfaceCreated(GL10 gl2, EGLConfig eglConfig) {

		// on ne peux pas cr�er de programe Shader en dehors du contexte
		// opengl. donc le provider est � recr�er � chaque load de la sc�ne

		initProgramShader();

		// on active le texturing 2D
		GLES20.glEnable(GLES20.GL_TEXTURE_2D);

		// Activattion de la gestion de l'Alpha

		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		GLES20.glEnable(GLES20.GL_BLEND);

		// sp�cifer l'orientation de la d�termination des face avant/arri�re
		// par d�daut c'est CCW (counterClockWiwe) l'inverse des aiguilles d'une
		// montre
		GLES20.glFrontFace(GL10.GL_CCW);

		// on indique quelle face � oculter (par d�faut c'est BACK)
		GLES20.glCullFace(GL10.GL_BACK);

		// Activation du Culling
		GLES20.glEnable(GL10.GL_CULL_FACE);

		//

		// create texture handle
		int[] textures = new int[1];

		// on g�n�re un buffer texture utilisable par OPENGL
		GLES20.glGenTextures(1, textures, 0);

		glbufferTextureID = textures[0];

		// on demande � opengl d'utiliser la premi�re texture
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, glbufferTextureID);

		// d�finition des param�tres de magnification et minification des
		// texture
		// on indique GL_NEAREST pour dire que l'on doit prendre le pixel qui se
		// rapporche le plus
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);

		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

		// param�trage du d�passement des coordon�es de texture
		// GL_CLAMP_TO_EDGE = on �tire la texture pour recouvrir la forme
		// on peu aussi mettre un param�tre pour r�p�ter la texture ou bien
		// effectuer un mirroir

		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
				GLES20.GL_CLAMP_TO_EDGE);

		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
				GLES20.GL_CLAMP_TO_EDGE);

	}

	// @Override
	public void onSurfaceChanged(GL10 gl, int width, int hight) {
		// lorsqu'il y a une modification de la vue , on red�finie la nouvelle
		// taille de la vue (par exemple quand on incline le t�l�phone et
		// que l'on passe de la vue portait � la vue paysage

		// le coin en bas � gauche est 0,0
		// la taille de la surface est la m�me que l'�cran

		GLES20.glViewport(0, 0, width, hight);

		float ratio = (float) width / hight;
		// * pour un affichage Perspective *********************
		// le premier plan de clipping NEAR est d�fini par
		// 2 points : le point du bas � gauche et le point du haut � droite
		// le point du bas � gauche est � -ratio, -1
		// le point du haut � gauche est � ratio, 1
		// le plan de clipping NEAR est � 1 et le second plan est � 1000.

		/** avec cette version le centre est au milieu de l'�cran */
		Matrix.frustumM(mProjectionView, 0, -ratio, ratio, -1, 1, 1, 1000);

		/** avec cette version le centre est en bas � gauche de l'�cran */
		// Matrix.frustumM(mProjectionView, 0, 0, ratio, 0, 1, 1, 1000);

		// Set the camera position (View matrix)
		// le centre de la cam�ra est en 0,0,-200 (oeuil)
		// la cam�ra regarde le centre de l'�cran 0,0,0
		// le vecteur UP indique l'orientation de la cam�ra (on peu tourner la
		// cam�ra)
		// Matrix.setLookAtM(rm, rmOffset, eyeX, eyeY, eyeZ, centerX, centerY,
		// centerZ, upX, upY, upZ)
		
		// attentios, si le z est n�gatif, la cam�ra se retrouve derri�re l'axe
		// et donc le X est invers� 
		Matrix.setLookAtM(mVMatrix, 0, mCamera.centerX, mCamera.centerY, mCamera.centerZ,
				mCamera.eyeX, mCamera.eyeY, mCamera.eyeZ,
				mCamera.orientX, mCamera.orientY, mCamera.orientZ);

		// * pour un affichage Orthogonal *********************
		// le (0,0) est en bas � gauche.
		Matrix.orthoM(mProjectionORTH, 0, -0, width, 0, hight, -10.f, 10.f);
		Matrix.setIdentityM(mVMatrixORTH, 0);

	}

	// @Override
	public void onDrawFrame(GL10 gl) {
		float startDrawingTime = SystemClock.currentThreadTimeMillis();

		// on commence par effacer l'�cran en le remplissant de la
		// couleur souhait�e et on vide le buffer.
		GLES20.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

		
		float incX = 0;
		float incY = 0;
		
		
		switch (this.getActivity().getSurfaceView().event) {
		
		case SCROLL_H_RIGHT:
			incX = +2f; 
			break;
		
		case SCROLL_H_LEFT:
			incX = -2f; 
			break;
		
			
		case SCROLL_V_UP:
			incY = +2f; 
			break;
		
		case SCROLL_V_DOWN:
			incY = -2f; 
			break;
		
		default:
			break;
		
		}
		
		float limitX_RIGHT = +80;
		float limitX_LEFT = -80;
		
		this.mCamera.centerX += incX;
		this.mCamera.centerX = (this.mCamera.centerX > limitX_RIGHT)? limitX_RIGHT: this.mCamera.centerX;
		this.mCamera.centerX = (this.mCamera.centerX < limitX_LEFT)? limitX_LEFT: this.mCamera.centerX;
		this.mCamera.eyeX = this.mCamera.centerX;

		float limitY_UP = +100;
		float limitY_DOWN = -100;
		
		this.mCamera.centerY = (this.mCamera.centerY > limitY_UP)? limitY_UP: this.mCamera.centerY;
		this.mCamera.centerY = (this.mCamera.centerY < limitY_DOWN)? limitY_DOWN: this.mCamera.centerY;
		
		
		this.mCamera.centerY += incY;
		this.mCamera.eyeY = this.mCamera.centerY;
		
		
			Matrix.setLookAtM(mVMatrix, 0, mCamera.centerX, mCamera.centerY, mCamera.centerZ,
					mCamera.eyeX, mCamera.eyeY, mCamera.eyeZ,
					mCamera.orientX, mCamera.orientY, mCamera.orientZ);
			
		
		
		
		// Calculate the projection and view transformation
		// Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);

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
		// SystemClock.sleep(2000);
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
