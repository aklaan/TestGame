package com.example.testgame.gamecomponents;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import com.example.testgame.GLES20RendererScene01;
import com.example.testgame.R;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

public class GameObject {

	private String mTagName = "";
	public Texture mTexture;
	public Boolean hasTexture;
	public Boolean isVisible;

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
	public ArrayList<CollisionBox> mCollideWithList;

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

		hasTexture = false;
		mTagName = "";
		isVisible = true;
		Matrix.setIdentityM(this.mRotationMatrix, 0);

		Matrix.setIdentityM(this.mTransformUpdateView, 0);

		this.mCollideWithList = new ArrayList<CollisionBox>();
		this.mVertices = new ArrayList<Vertex>();
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

	public void onUpdate(Context context) {

	}

	public String getTagName() {
		return mTagName;
	}

	public void setTagName(String tagName) {
		mTagName = tagName;
	}

	/**
	 * public float getWidth() { return width; }
	 * 
	 * public void setWidth(float width) { // this.width = width; //
	 * updateModelMatrix(); }
	 * 
	 * public float getHeight() { return height; }
	 * 
	 * public void setHeight(float height) { this.height = height;
	 * updateModelMatrix(); }
	 */
	public void setCoord(float x, float y) {
		this.X = x;
		this.Y = y;
		updateModelMatrix();

	}

	public float getCoordX() {
		return X;

	}

	public float getCoordY() {
		return Y;

	}

	private void updateModelMatrix() {
		// Matrix.setIdentityM(mModelMatrix, 0);

		// Log.i("debug","width = "+
		// String.valueOf(width)+" / height="+String.valueOf(height));

		// this.scale(width, height);
		// this.translate(X,Y);
	}

	public void onUpdate(OpenGLActivity openGLActivity) {
		// TODO Auto-generated method stub

	}

	public void draw(GLES20RendererScene01 renderer) {

	}

	public void turnArround(GameObject cible, float angle, float rayon) {

		Matrix.setIdentityM(this.mTransformUpdateView, 0);
		angleRAD += angle;
		float[] wrkmodelView = new float[16];
		float[] wrkRotation = new float[16];
		Matrix.translateM(mTransformUpdateView, 0, cible.X, cible.Y, 0);
		Matrix.translateM(mTransformUpdateView, 0, rayon, 0, 0);
		Matrix.setRotateEulerM(wrkRotation, 0, 0, 0, angleRAD);

		wrkmodelView = mTransformUpdateView.clone();
		Matrix.multiplyMM(mTransformUpdateView, 0, wrkmodelView, 0,
				wrkRotation, 0);

	}


	public ArrayList<Vertex> applyModelView(float[] modelView){
		
		// on récupère les vertices de l'objet 
		//et on calcule leur coordonées dans le monde 		
		float[] oldVerticesCoord = new float[4];
		float[] newVerticesCoord = new float[4];

		ArrayList<Vertex> mModelViewVertices; // définition d'un tableau de flotants
		mModelViewVertices = new ArrayList<Vertex>();
		
		// je suis obligé de passer par un vecteur 4 pour la multiplication

		for (int i = 0; i < this.mVertices.size(); i++) {
			oldVerticesCoord[0] = this.mVertices.get(i).x; // x
			oldVerticesCoord[1] = this.mVertices.get(i).y; // y
			oldVerticesCoord[2] = this.mVertices.get(i).z; // z
			oldVerticesCoord[3] = 1.f;


			Matrix.multiplyMV(newVerticesCoord, 0, modelView, 0, oldVerticesCoord,
					0);
			mModelViewVertices.add(new Vertex(newVerticesCoord[0],newVerticesCoord[1],0));
			
		
		
		}
		
		return mModelViewVertices;
		
		
		
	}

	public Vertex getCenterVertex(float[] modelView){
		
		
		float[] origin = {0,0,0,1};
		float[] neworigin = new float[4];
		
		
			Matrix.multiplyMV(neworigin, 0, modelView, 0, origin,0);
			return new Vertex(neworigin[0],neworigin[1],0);
			
		
	}

}
