package com.example.testgame.gamecomponents;

import java.util.ArrayList;

import android.util.Log;


public class SAT {
	static ArrayList<Vector2D> mTempVertices = new ArrayList<Vector2D>();
	static ArrayList<Vector2D> mFinalVertices = new ArrayList<Vector2D>();
	ArrayList<Vector2D> mNormals = new ArrayList<Vector2D>();
	
	
	
	

	// reconstituer la liste des vextex dans le bon ordre à partir du floatbuffer mvertice et shortbuffer mindice de la forme
	public static void getVertices(GameObject mShape){
		
		
			
			// lire le tableau des index pour reconstituer le tableau final des vertices
	
			mShape.getIndices().rewind();
			
			Vector2D toto = new Vector2D();
			while (mShape.getIndices().hasRemaining()){
			int currentIndex = mShape.getIndices().get();
			
			toto.x = mShape.mVertices.get(currentIndex).x;
			toto.y = mShape.mVertices.get(currentIndex).y;
			
				mFinalVertices.add(toto);
				
			}
			
			Log.i("fin","fin");
			
	}
	
	
	
	public static ArrayList<Vector2D> getNormals(GameObject mShape){
		
	
		return null;
		
		
	}
	
	
	
}
