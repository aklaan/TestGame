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
import android.util.Log;



public class GameObject {

	
	
    public static final int FLOAT_SIZE = 4; // on indique que le nombre de byte pour un float est de 4
    // un byte n'est pas obligatoirement �gal � 8 bit 
	// cela d�pend du mat�riel. en g�n�ral il est tr�s souvant egal � 
	// 8 bit ce qui fait qu'un byte est tr�s souvent �gal � un Octet
	// mais comme ce n'est pas toujours le cas, on parle en byte et non en octet
	// pour �tre pr�cis.

public static final int SHORT_SIZE = 2; //ici on indique qu'un short est cod� sur 2 byte 
	//soit g�n�ralement 2 octets
	// soit : 00000000 00000000

//! Vertices
private FloatBuffer mVertices;			// d�finition d'un tableau de flotants
//! indices
private ShortBuffer mIndices;
//! coordon�es de texture
private FloatBuffer mTextCoord;

private ByteBuffer mTexture;
private int mTextureWidth;
private int mTextureHeight;
// constructeur
public GameObject(int nbVertex, int nbIndex) {
    mVertices = ByteBuffer.allocateDirect(nbVertex * 3 *FLOAT_SIZE).order(ByteOrder.nativeOrder()).asFloatBuffer();
    mIndices = ByteBuffer.allocateDirect(nbIndex * SHORT_SIZE).order(ByteOrder.nativeOrder()).asShortBuffer();
    mTextCoord = ByteBuffer.allocateDirect(nbVertex * 2 *FLOAT_SIZE).order(ByteOrder.nativeOrder()).asFloatBuffer();
}



//setter vertices
public void putVertex(int index, Vertex vertex) {
	// la position physique en m�moire des bytes qui repr�sentent le vertex 
	// c'est la taille d'un vertex en bytes x l'index
	
	// ici on se positionne dans le buffer � l'endroit o� l'on va ecrire le prochain vertex
    mVertices.position(Vertex.Vertex_COORD_SIZE *  index);
    mVertices.put(vertex.x).put(vertex.y).put(vertex.z);
    // on se repositionne en 0 , pr�t pour la relecture
    mVertices.position(0);

    
    mTextCoord.position(Vertex.Vertex_TEXT_SIZE *  index);
    mTextCoord.put(vertex.u).put(vertex.v);
    // on se repositionne en 0 , pr�t pour la relecture
    mTextCoord.position(0);



}



//setter indices
public void putIndice(int index, int indice) {
    // on se positionne a l'index dans le buffer
	// comme on a qu'un seul short a placer on ne fait pas comme dans putvertice
	mIndices.position(index);
	// on ecrit le short
    mIndices.put((short)indice);
    // on se repositionne en z�ro
    mIndices.position(0);
}

//getter vertices
public FloatBuffer getVertices() {
	return mVertices;
}


//getter TextCoord
public FloatBuffer getTextCoord() {
	return mTextCoord;
}


//getter indices
public ShortBuffer getIndices() {
    return mIndices;
}





// load a texture
public void setTexture(Bitmap texture){

	mTextureWidth = texture.getWidth();
	mTextureHeight = texture.getHeight();
// on d�fini un buffer contenant tous les points de l'image
// il en a (longeur x hauteur)
// pour chaque point on a 4 bytes . 3 pour la couleur RVB et 1 pour
// l'alpha
mTexture = ByteBuffer.allocateDirect(texture.getHeight()
		* texture.getWidth() * 4);

// on indique que les bytes dans le buffer doivent
// �tre enregistr� selon le sens de lecture natif de l'architecture CPU
// (de gaucha a droite ou vice et versa)
mTexture.order(ByteOrder.nativeOrder());

byte buffer[] = new byte[4];
// pour chaque pixel composant l'image, on m�morise sa couleur et
// l'alpha
// dans le buffer
for (int i = 0; i < mTextureHeight; i++) {
	for (int j = 0; j < mTextureWidth; j++) {
		int color = texture.getPixel(j, i);
		buffer[0] = (byte) Color.red(color);
		buffer[1] = (byte) Color.green(color);
		buffer[2] = (byte) Color.blue(color);
		buffer[3] = (byte) Color.alpha(color);
		mTexture.put(buffer);
	}
}
// on se place a la position 0 du buffer - pr�s � �tre lu plus tard
mTexture.position(0);

}

//charger la texture m�moris� dans le buffer dans le moteur de rendu comme 
		//�tant la texture 0,1,2,...
public void putTextureToGLUnit(int unit){
	GLES20.glTexImage2D(GL10.GL_TEXTURE_2D, unit, GL10.GL_RGBA,
			mTextureWidth, mTextureHeight, 0, GL10.GL_RGBA,
			GL10.GL_UNSIGNED_BYTE, mTexture);
	
}

}

