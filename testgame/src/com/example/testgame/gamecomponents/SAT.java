package com.example.testgame.gamecomponents;

import java.util.ArrayList;

import android.util.Log;

public class SAT {
	ArrayList<Vector2D> mTempVertices = new ArrayList<Vector2D>();
	ArrayList<Vector2D> mSortedVertices = new ArrayList<Vector2D>();
	ArrayList<Vector2D> mNormals = new ArrayList<Vector2D>();

	// reconstituer la liste des vextex dans le bon ordre à partir du
	// floatbuffer mvertice et shortbuffer mindice de la forme
	public void getVerticesInOrder(GameObject mShape) {

		// lire le tableau des index pour reconstituer le tableau final des
		// vertices

		mShape.getIndices().rewind();

		Vector2D toto = new Vector2D();
		while (mShape.getIndices().hasRemaining()) {

			int currentIndex = mShape.getIndices().get();

			Log.i("index buffer", String.valueOf(currentIndex));

			
			mSortedVertices.add(new Vector2D(
					mShape.mVertices.get(currentIndex).x
					,mShape.mVertices.get(currentIndex).y));

		}

	}

	public void getNormals(GameObject mShape) {

		
		//le vecteur normal à(X,Y) est (-Y,X)
		getVerticesInOrder(mShape);
		int size = this.mSortedVertices.size();

		for (int i = 0; i < size - 1; i++) {
			this.mNormals.add(new Vector2D(

			-(this.mSortedVertices.get(i + 1).y - this.mSortedVertices.get(i).y)
			,this.mSortedVertices.get(i + 1).x	- this.mSortedVertices.get(i).x

			));

		}

		// la dernière normale entre le premier et le dernier point
		this.mNormals.add(new Vector2D(

		-(this.mSortedVertices.get(0).y - this.mSortedVertices.get(size - 1).y)
		,this.mSortedVertices.get(0).x	- this.mSortedVertices.get(size - 1).x

		));

		Log.i("fin", "fin");

	}

}
