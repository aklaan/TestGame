package com.example.testgame;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

public class GameObject {

	private String TagName = "";
	public Texture mTexture;
	
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

	//private ByteBuffer mTexture;
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
		Matrix.setIdentityM(mModelMatrix, 0);
		Matrix.translateM(mModelMatrix, 0, x, y, 0f);
	}

	public void rotate(float anglRAD) {
		float[] wrkRotationMatrix = new float[16];
		float[] wrkModelMatrix = new float[16];

		Matrix.setRotateEulerM(wrkRotationMatrix, 0, 0.f, 0.f, anglRAD);
		wrkModelMatrix = mModelMatrix.clone();
		Matrix.multiplyMM(mModelMatrix, 0, wrkModelMatrix, 0,wrkRotationMatrix, 0);
				
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

	public void setTexture(Texture texture){
		mTexture = texture;
	}
	
	
	public void onUpdate() {

	}

	public String getTagName() {
		return TagName;
	}

	public void setTagName(String tagName) {
		TagName = tagName;
	}

}
