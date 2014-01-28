package com.example.testgame.gamecomponents;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import com.example.testgame.GLES20Renderer;
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
	public String usedShaderName = "";
	public ShaderProvider mShaderProvider;

	// top permettant de savoir si l'objet est statique ou qu'il
	// a la possibilit� d'�tre en mouvement. ceci va servir
	// pour le calcul des collisions
	public Boolean isStatic = true;

	// coordonn�es du centre de l'objet
	public float X = 0;
	public float Y = 0;
	public boolean rotation = false;
	public float rotationAxisX = 0.f;
	public float rotationAxisY = 0.f;
	public float rotationAngl = 0.f; // en radian
	public ArrayList<GameObject> mCollideWithList;

	public float[] mRotationMatrix = new float[16];
	public float[] mBackupModelView = new float[16];
	public float[] mTransformUpdateView = new float[16];

	public static final int FLOAT_SIZE = 4;
	public int drawMode;
	public float angleRAD = 0.0f;
	// on indique qu'il faut 4 byte pour rep�senter un float
	// 00000000 00000000 00000000 00000000

	// un byte n'est pas obligatoirement �gal � 8 bit
	// cela d�pend du mat�riel. en g�n�ral il est tr�s souvant egal �
	// 8 bit ce qui fait qu'un byte est tr�s souvent �gal � un Octet
	// mais comme ce n'est pas toujours le cas, on parle en byte et non en octet
	// pour �tre plus pr�cis.

	public static final int SHORT_SIZE = 2;
	// ici on indique qu'un short est cod� sur 2 byte
	// soit g�n�ralement 2 octets
	// soit : 00000000 00000000

	// ! Vertices
	public FloatBuffer mVertices; // d�finition d'un tableau de flotants

	// ! indices
	private ShortBuffer mIndices;

	// ! coordon�es de texture
	private FloatBuffer mTextCoord;

	// private ByteBuffer mTexture;
	public int mTextureWidth;
	public int mTextureHeight;

	// constructeur
	public GameObject(int nbVertex, int nbIndex) {
		mVertices = ByteBuffer.allocateDirect(nbVertex * 3 * FLOAT_SIZE)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		mIndices = ByteBuffer.allocateDirect(nbIndex * SHORT_SIZE)
				.order(ByteOrder.nativeOrder()).asShortBuffer();
		mTextCoord = ByteBuffer.allocateDirect(nbVertex * 2 * FLOAT_SIZE)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();

		hasTexture = false;
		mTagName = "";
		isVisible = true;
		Matrix.setIdentityM(this.mRotationMatrix, 0);

		Matrix.setIdentityM(this.mTransformUpdateView, 0);
		this.drawMode = GLES20.GL_TRIANGLES;
		this.mCollideWithList = new ArrayList<GameObject>();
	

	}

	// setter vertices
	public void putVertex(int index, Vertex vertex) {
		// la position physique en m�moire des bytes qui repr�sentent le vertex
		// c'est la taille d'un vertex en bytes x l'index
		mVertices.position(0);
		// ici on se positionne dans le buffer � l'endroit o� l'on va ecrire le
		// prochain vertex
		mVertices.position(Vertex.Vertex_COORD_SIZE * index);
		mVertices.put(vertex.x).put(vertex.y).put(vertex.z);
		// on se repositionne en 0 , pr�t pour la relecture
		mVertices.position(0);

		mTextCoord.position(Vertex.Vertex_TEXT_SIZE * index);
		mTextCoord.put(vertex.u).put(vertex.v);
		// on se repositionne en 0 , pr�t pour la relecture
		mTextCoord.position(0);

	}

	public void rotate(float x, float y, float anglRAD) {

		X = X + (float) (Math.cos(anglRAD));
		Y = Y + (float) (Math.sin(anglRAD));
		// Matrix.translateM(wrkresult, 0, x, y, 0);
		Log.i("deug", String.valueOf(X) + " / " + String.valueOf(Y));

		Log.i("", String.valueOf(Math.cos(anglRAD)));

	}

	// setter indices
	public void putIndice(int index, int indice) {
		// on se positionne a l'index dans le buffer
		// comme on a qu'un seul short a placer on ne fait pas comme dans
		// putvertice
		mIndices.position(index);
		// on ecrit le short
		mIndices.put((short) indice);
		// on se repositionne en z�ro
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
	public void setCoord(int x, int y) {
		this.X = x;
		this.Y = y;
		// updateModelMatrix();

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

	public void draw(float[] ModelMatrix ) {

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

	public void sendVertexCoord() {

		Shader sh = new Shader();
		sh = mShaderProvider.getShaderByName(this.usedShaderName);

		int mAdressOf_VertexPosition = Integer.parseInt(sh.attribCatlg
				.get(mShaderProvider.mActivity.getString(R.string.vertex_position)));

		if (mAdressOf_VertexPosition != -1) {
			GLES20.glVertexAttribPointer(mAdressOf_VertexPosition, 3,
					GLES20.GL_FLOAT, false, Vertex.Vertex_COORD_SIZE_BYTES,
					this.getVertices());
		}

	}


	public void sendTextureCoord() {

		Shader sh = new Shader();
		sh = mShaderProvider.getShaderByName(this.usedShaderName);

		int mAdressOf_texturePosition = Integer.parseInt(sh.attribCatlg
				.get(mShaderProvider.mActivity.getString(R.string.texture_position)));

		if (mAdressOf_texturePosition != -1) {
			GLES20.glVertexAttribPointer(mAdressOf_texturePosition, 3,
					GLES20.GL_FLOAT, false, Vertex.Vertex_COORD_SIZE_BYTES,
					this.getVertices());
		}

	}



}