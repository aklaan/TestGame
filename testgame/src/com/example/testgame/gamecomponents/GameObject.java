package com.example.testgame.gamecomponents;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

public class GameObject implements Drawable,Cloneable {
	private int mTagName = 0;
	public Texture mTexture;
	public int newTextureId;
	public Boolean textureEnabled;
	public Boolean isVisible;
	public Scene mScene;

	// top permettant de savoir si l'objet est statique ou qu'il
	// a la possibilité d'être en mouvement. ceci va servir
	// pour le calcul des collisions
	public Boolean isStatic = true;
	public Boolean canCollide = false;
	// coordonnées du centre de l'objet
	public float X = 0;
	public float Y = 0;

	public boolean rotation = false;

	public float rotationAxisX = 0.f;
	public float rotationAxisY = 0.f;
	public float rotationAngl = 0.f; // en radian

	public CollisionBox mCollisionBox;
	// tableau des objets avec lesquel le gameobject rentre en collision
	public ArrayList<GameObject> mCollideWithList;

	public ArrayList<GameObject> mGameObjectToListenList;
	public Animation mAnimation;

	public float[] mRotationMatrix = new float[16];
	public float[] mModelView = new float[16];
	public float[] mTransformUpdateView = new float[16];

	public static final int FLOAT_SIZE = 4;

	public int drawMode = GLES20.GL_TRIANGLES;

	public float angleRAD = 0.0f;
	// on indique qu'il faut 4 byte pour repésenter un float
	// 00000000 00000000 00000000 00000000

	// un byte n'est pas obligatoirement égal à 8 bit
	// cela dépend du matériel. en général il est très souvant egal à
	// 8 bit ce qui fait qu'un byte est très souvent égal à un Octet
	// mais comme ce n'est pas toujours le cas, on parle en byte et non en octet
	// pour être plus précis.

	public static final int SHORT_SIZE = 2;
	// ici on indique qu'un short est codé sur 2 byte
	// soit généralement 2 octets
	// soit : 00000000 00000000

	// ! Vertices
	public FloatBuffer mFbVertices; // définition d'un tableau de flotants

	public ArrayList<Vertex> mVertices; // définition d'un tableau de flotants

	// ! indices
	private ShortBuffer mIndices;

	// ! coordonées de texture
	private FloatBuffer mTextCoord;

	// private ByteBuffer mTexture;
	public int mTextureWidth;
	public int mTextureHeight;

	// constructeur
	public GameObject() {

		textureEnabled = false;
		mTagName = 0;
		isVisible = true;
		Matrix.setIdentityM(this.mRotationMatrix, 0);

		Matrix.setIdentityM(this.mTransformUpdateView, 0);

		this.mCollideWithList = new ArrayList<GameObject>();
		this.mGameObjectToListenList = new ArrayList<GameObject>();

		this.mVertices = new ArrayList<Vertex>();
	}

	public ArrayList<GameObject> getGameObjectToListenList() {
		return this.mGameObjectToListenList;
	}

	public Animation getAnimation() {
		return this.mAnimation;
	}

	public void setAnimation(Animation anim) {
		this.mAnimation = anim;
	}

	public void initBuffers(int nbIndex) {
		int nbVertex = mVertices.size();

		mFbVertices = ByteBuffer.allocateDirect(nbVertex * 3 * FLOAT_SIZE)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();

		mIndices = ByteBuffer.allocateDirect(nbIndex * SHORT_SIZE)
				.order(ByteOrder.nativeOrder()).asShortBuffer();
		mTextCoord = ByteBuffer.allocateDirect(nbVertex * 2 * FLOAT_SIZE)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();

	}

	public void enableColission() {
		this.mCollisionBox = new CollisionBox(this);
		this.canCollide = true;
	}

	public void disableColission() {
		this.mCollisionBox = null;
		this.canCollide = false;
	}

	// setter vertices
	public void putVertex(int index, Vertex vertex) {
		// la position physique en mémoire des bytes qui représentent le vertex
		// c'est la taille d'un vertex en bytes x l'index
		mFbVertices.rewind();
		// ici on se positionne dans le buffer à l'endroit où l'on va ecrire le
		// prochain vertex
		mFbVertices.position(Vertex.Vertex_COORD_SIZE * index);
		mFbVertices.put(vertex.x).put(vertex.y).put(vertex.z);
		// on se repositionne en 0 , prêt pour la relecture
		mFbVertices.rewind();

		mTextCoord.position(Vertex.Vertex_TEXT_SIZE * index);
		mTextCoord.put(vertex.u).put(vertex.v);
		// on se repositionne en 0 , prêt pour la relecture
		mTextCoord.rewind();

	}

	public void rotate(float x, float y, float anglRAD) {

		X = X + (float) (Math.cos(anglRAD));
		Y = Y + (float) (Math.sin(anglRAD));
		// Matrix.translateM(wrkresult, 0, x, y, 0);
		Log.i("debug", String.valueOf(X) + " / " + String.valueOf(Y));

		Log.i("debug", String.valueOf(Math.cos(anglRAD)));

	}

	// setter indices
	public void putIndice(int index, int indice) {
		// on se positionne a l'index dans le buffer
		// comme on a qu'un seul short a placer on ne fait pas comme dans
		// putvertice
		mIndices.position(index);
		// on ecrit le short
		mIndices.put((short) indice);
		// on se repositionne en zéro
		mIndices.position(0);
	}

	public Scene getScene() {
		return this.mScene;
	}

	// getter vertices
	public FloatBuffer getFbVertices() {

		for (int i = 0; i < this.mVertices.size(); i++) {
			this.putVertex(i, this.mVertices.get(i));
		}

		return mFbVertices;
	}

	// getter TextCoord
	public FloatBuffer getTextCoord() {
		return mTextCoord;
	}

	// getter indices
	public ShortBuffer getIndices() {
		return mIndices;
	}

	public void setTexture(Texture texture) {
		mTexture = texture;

	}

	public void onUpdate() {

	}

	public int getTagName() {
		return mTagName;
	}

	public void setTagName(int tagid) {
		mTagName = tagid;
	}

	/**
	 * public float getWidth() { return width; }
	 * 
	 * public void setWidth(float width) { // this.width = width; //
	 * updateModelMatrix(); }
	 * 
	 * public float getHeight() { return height; }
	 * 
	 * public void setheight(float height) { this.height = height;
	 * updateModelMatrix(); }
	 */
	public void setCoord(float x, float y) {
		this.X = x;
		this.Y = y;

	}

	public float getCoordX() {
		return X;

	}

	public float getCoordY() {
		return Y;

	}

	public void draw() {

	}

	public ArrayList<Vertex> applyModelView(float[] modelView) {

		// on récupère les vertices de l'objet
		// et on calcule leur coordonées dans le monde
		float[] oldVerticesCoord = new float[4];
		float[] newVerticesCoord = new float[4];

		ArrayList<Vertex> mModelViewVertices; // définition d'un tableau de
												// flotants
		mModelViewVertices = new ArrayList<Vertex>();

		// je suis obligé de passer par un vecteur 4 pour la multiplication

		for (int i = 0; i < this.mVertices.size(); i++) {
			oldVerticesCoord[0] = this.mVertices.get(i).x; // x
			oldVerticesCoord[1] = this.mVertices.get(i).y; // y
			oldVerticesCoord[2] = this.mVertices.get(i).z; // z
			oldVerticesCoord[3] = 1.f;

			Matrix.multiplyMV(newVerticesCoord, 0, modelView, 0,
					oldVerticesCoord, 0);
			mModelViewVertices.add(new Vertex(newVerticesCoord[0],
					newVerticesCoord[1], 0));

		}

		return mModelViewVertices;

	}

	// fabrique la nouvelle ModelView en fonction des modifications
	// apportées.
	public void updateModelView() {
		float[] wrkModelView = new float[16];
		float[] wrkRotationMatrix = new float[16];

		Matrix.setIdentityM(wrkModelView, 0);

		Matrix.translateM(wrkModelView, 0, this.X, this.Y, 0);

		Matrix.setRotateEulerM(wrkRotationMatrix, 0, 0, 0, this.angleRAD);

		Matrix.multiplyMM(this.mModelView, 0, wrkModelView, 0,
				wrkRotationMatrix, 0);

	}

	/**
	 * Fonction de mise à jour générale
	 * 
	 * @param openGLActivity
	 */

	public void mainUpdate(OpenGLActivity openGLActivity) {

		// traiter les opérations diverses à effectuer lors de
		// la mise à jour
		this.onUpdate();

		// traiter les actions a faire en cas de colissions
		this.applyCollisions();

		// traiter la lecture de l'animation si elle existe
		this.applyAnimation();

		// A la fin des mises à jour on connais les nouvelles coordonées
		// on peut calculer la nouvelle matrice modelView
		this.updateModelView();

		// -----------------------------------------
		// Mettre a jour la boite de colision si elle existe
		// ------------------------------------------
		if (this.canCollide) {
			this.mCollisionBox.update();
		}

		// -----------------------------------------------------
		// Traiter les évènements écoutés sur les autres objets
		// -----------------------------------------------------
		updateListerners();

		// -----------------------------------------------------
		// Gestion des modifications de la texture
		// ------------------------------------------------------
		
/**		if (this.textureEnabled && this.mTexture.textureNameID != newTextureId) {
			this.getScene().getBitmapProvider().linkTexture(newTextureId, this);

			this.mTexture.textureNameID = newTextureId;
		}
*/
		if (textureEnabled) {
			this.getScene().getBitmapProvider()
					.putTextureToGLUnit(this.mTexture, 0);
		}

	}



	/***************************************************
	 * Traiter des colisions avec les autres objets
	 ***************************************************/
	public void applyCollisions() {

		if (!this.mCollideWithList.isEmpty()) {
			for (GameObject go : this.mCollideWithList) {

				onCollideWith(go);// newTextureId = R.string.textureRobot;
			}
		}

	}

	/***********************************************
	 * Traiter l'animation si elle existe
	 **********************************************/
	public void applyAnimation() {
		// -----------------------------------------
		// traiter l'animation
		// --------------------------------------------------------
		if (this.getAnimation() != null) {

			if (this.getAnimation().status == Animation.AnimationStatus.PLAYING) {
				this.getAnimation().play();
				// traiter les actions suplémentaires lors de la lecture
				onAnimationPlay();
			}
			if (this.getAnimation().status == Animation.AnimationStatus.STOPPED) {
				//this.setAnimation(null);
				// traiter les actions suplémentaires a la fin de la lecture
				onAnimationStop();
			}

		}
	}

	/********************************************************
	 * On écoute les objets note : on dépend de l'ordre dans lequel sont traité
	 * les objets
	 * 
	 *******************************************************/
	public void updateListerners() {

		if (!this.getGameObjectToListenList().isEmpty()) {
			for (GameObject go : this.getGameObjectToListenList()) {

				onListenGameObject(go);

			}
		}

	}

	/**************************************************************************
	 * Actions a effectuer en cas de colission avec un autre Objet
	 * 
	 * @param gameObject
	 *************************************************************************/
	public void onCollideWith(GameObject gameObject) {

	}

	/**************************************************************************
	 * Actions effectuer lorsque l'on écoute les objets
	 * 
	 * @param go
	 *************************************************************************/
	public void onListenGameObject(GameObject go) {

	}

	/**************************************************************************
	 * Actions effectuer lorsque l'animation joue
	 * 
	 *************************************************************************/
	public void onAnimationPlay() {

	}

	/**************************************************************************
	 * Actions effectuer lorsque l'animation s'arrête
	 * 
	 *************************************************************************/
	public void onAnimationStop() {

	}

	public boolean isCollideWith(GameObject gameobject) {

		return this.mCollideWithList.contains(gameobject);

	}
public GameObject clone() throws CloneNotSupportedException{
	GameObject gameobject = (GameObject)super.clone();
	
	gameobject.mCollideWithList = new ArrayList<GameObject>();
	gameobject.mGameObjectToListenList = new ArrayList<GameObject>();

	//on réinitialise le lien de parenté avec l'animation
	if (gameobject.getAnimation() != null){
		Animation anim = (Animation) gameobject.getAnimation().clone();
				
		anim.parent = gameobject;
		
		gameobject.setAnimation(anim);
	}
	
	//si l'objet source peu entrer en collision on 
	//redéfini un nouvelle boite de colision pour la cible
	//sinon elle va avoir la même que la source
		if (gameobject.canCollide){
			gameobject.enableColission();
			
		}
	return gameobject;
}
}
