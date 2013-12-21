package com.example.testgame;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Vector;

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



// constructeur
public GameObject(int nbVertex, int nbIndex) {
    mVertices = ByteBuffer.allocateDirect(nbVertex * 3 *FLOAT_SIZE).order(ByteOrder.nativeOrder()).asFloatBuffer();
    mIndices = ByteBuffer.allocateDirect(nbIndex * SHORT_SIZE).order(ByteOrder.nativeOrder()).asShortBuffer();

}



//setter vertices
public void putVertice(int index, Vertex vertex) {
	// la position physique en m�moire des bytes qui repr�sentent le vertex 
	// c'est la taille d'un vertex en bytes x l'index
	
	// ici on se positionne dans le buffer � l'endroit o� l'on va ecrire le prochain vertex
    Log.i("debug",String.valueOf(Vertex.Vertex_SIZE *  index));
	
    mVertices.position(Vertex.Vertex_SIZE *  index);
    
    mVertices.put(vertex.x).put(vertex.y).put(vertex.z);

   
    // on se repositionne en 0 , pr�t pour la relecture
   
    mVertices.position(0);
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

//getter indices
public ShortBuffer getIndices() {
    return mIndices;
}

}


