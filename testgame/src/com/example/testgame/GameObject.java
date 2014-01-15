package com.example.testgame;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.content.Context;
import android.opengl.Matrix;
import android.util.Log;

public class GameObject {

	private String mTagName = "";
	public Texture mTexture;
	public Boolean hasTexture;
	public Boolean isVisible;
	private float width = 1.f;
	private float height = 1.f;
    private int X=0;
    private int Y=0;
	
	
	public static final int FLOAT_SIZE = 4; // on indique que le nombre de byte
											// pour un float est de 4
	// un byte n'est pas obligatoirement égal à 8 bit
	// cela dépend du matériel. en général il est très souvant egal à
	// 8 bit ce qui fait qu'un byte est très souvent égal à un Octet
	// mais comme ce n'est pas toujours le cas, on parle en byte et non en octet
	// pour être précis.

	public static final int SHORT_SIZE = 2; // ici on indique qu'un short est
											// codé sur 2 byte
	// soit généralement 2 octets
	// soit : 00000000 00000000

	// ! Vertices
	private FloatBuffer mVertices; // définition d'un tableau de flotants
	// ! indices
	private ShortBuffer mIndices;
	// ! coordonées de texture
	private FloatBuffer mTextCoord;

	// private ByteBuffer mTexture;
	public int mTextureWidth;
	public int mTextureHeight;

	// ! matrice du modele
	public float[] mModelMatrix = new float[16];

	// constructeur
	public GameObject(int nbVertex, int nbIndex) {
		mVertices = ByteBuffer.allocateDirect(nbVertex * 3 * FLOAT_SIZE)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		mIndices = ByteBuffer.allocateDirect(nbIndex * SHORT_SIZE)
				.order(ByteOrder.nativeOrder()).asShortBuffer();
		mTextCoord = ByteBuffer.allocateDirect(nbVertex * 2 * FLOAT_SIZE)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		Matrix.setIdentityM(mModelMatrix, 0);
		hasTexture = false;
		mTagName = "";
		isVisible = true;
	}

	// setter vertices
	public void putVertex(int index, Vertex vertex) {
		// la position physique en mémoire des bytes qui représentent le vertex
		// c'est la taille d'un vertex en bytes x l'index

		// ici on se positionne dans le buffer à l'endroit où l'on va ecrire le
		// prochain vertex
		mVertices.position(Vertex.Vertex_COORD_SIZE * index);
		mVertices.put(vertex.x).put(vertex.y).put(vertex.z);
		// on se repositionne en 0 , prêt pour la relecture
		mVertices.position(0);

		mTextCoord.position(Vertex.Vertex_TEXT_SIZE * index);
		mTextCoord.put(vertex.u).put(vertex.v);
		// on se repositionne en 0 , prêt pour la relecture
		mTextCoord.position(0);

	}

	public void translate(float x, float y) {

		Matrix.translateM(mModelMatrix, 0, x, y, 0f);
	}

	public void scale(float scaleX, float scaleY) {

		Matrix.scaleM(mModelMatrix, 0, scaleX, scaleY, 0f);
	}

	public void rotate(float anglRAD) {
		float[] wrkRotationMatrix = new float[16];
		float[] wrkModelMatrix = new float[16];

		Matrix.setRotateEulerM(wrkRotationMatrix, 0, 0.f, 0.f, anglRAD);
		wrkModelMatrix = mModelMatrix.clone();
		Matrix.multiplyMM(mModelMatrix, 0, wrkModelMatrix, 0,
				wrkRotationMatrix, 0);

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
	public FloatBuffer getVertices() {
		return mVertices;
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

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
		updateModelMatrix();
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
		updateModelMatrix();
	}

	public void setCoord(int x, int y) {
		this.X=x;
		this.Y=y;
		updateModelMatrix();
		
	}

private void updateModelMatrix(){
	Matrix.setIdentityM(mModelMatrix, 0);
	
	//Log.i("debug","width = "+ String.valueOf(width)+" / height="+String.valueOf(height));
	this.translate(X,Y);
	this.scale(width, height);
}

public void onUpdate(OpenGLActivity openGLActivity) {
	// TODO Auto-generated method stub
	
}

}
