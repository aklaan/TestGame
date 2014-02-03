package com.example.testgame;

import java.util.ArrayList;

import com.example.testgame.gamecomponents.BitmapProvider;
import com.example.testgame.gamecomponents.GameObject;
import com.example.testgame.gamecomponents.OpenGLActivity;
import com.example.testgame.gamecomponents.Rectangle2D;
import com.example.testgame.gamecomponents.ShaderProvider;
import com.example.testgame.gameobjects.PetitRobot;
import com.example.testgame.gameobjects.Starship;

import android.os.Bundle;


public class MainActivity extends OpenGLActivity {
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// on définit une liste des composants de jeu
				
			
				
				Rectangle2D ligne1 = new Rectangle2D();
				ligne1.setCoord(0, 0);
				ligne1.setHeight(200);
				ligne1.setWidth(2);
				ligne1.setTagName("ligne1");
				this.mBitmapProvider.assignTexture(
						this.getString(R.string.textureRed), ligne1);

				mGameObjectList.add(ligne1);

				
				Rectangle2D ligne2 = new Rectangle2D();
				ligne2.setCoord(0, 0);
				ligne2.setHeight(2);
				ligne2.setWidth(1000);
				ligne2.setTagName("ligne2");
				this.mBitmapProvider.assignTexture(
						this.getString(R.string.textureRed), ligne2);

				//mGameObjectList.add(ligne2);
				
				
				Rectangle2D ligne3 = new Rectangle2D();
				ligne3.setCoord(0, 200);
				ligne3.setHeight(2);
				ligne3.setWidth(1000);
				ligne3.setTagName("ligne3");

				this.mBitmapProvider.assignTexture(
						this.getString(R.string.textureRed), ligne3);

				//mGameObjectList.add(ligne3);
				
				
			Starship mStarship = new Starship();
				
				mStarship.setHeight(100);
				mStarship.setWidth(100);
				mStarship.angleRAD = 0.0f;
				mStarship.setTagName("starship1");
				
				this.mBitmapProvider.assignTexture(
						this.getString(R.string.boulerouge), mStarship);

			  	mGameObjectList.add(mStarship);

				Starship mStarship2 = new Starship();
				
				mStarship2.setHeight(50);
				mStarship2.setWidth(50);
				this.mBitmapProvider.assignTexture(
						this.getString(R.string.boulerouge), mStarship2);
				mStarship2.setTagName("starship2");
				mStarship2.cible = mStarship;
				mStarship2.angleRAD=45.0f;
				//mGameObjectList.add(mStarship2);

				PetitRobot mPetitRobot = new PetitRobot();
				mPetitRobot.setCoord(50,0);
				mPetitRobot.setHeight(10);
				mPetitRobot.setWidth(10);

				this.mBitmapProvider.assignTexture(
						this.getString(R.string.textureRobot), mPetitRobot);

				//mGameObjectList.add(mPetitRobot);
			
	
				mStarship.cible=mPetitRobot;
	}

	@Override
	public void init() {

		
		mBitmapProvider.add(getString(R.string.textureStarship));
		mBitmapProvider.add(getString(R.string.textureRobot));
		mBitmapProvider.add(getString(R.string.textureRed));
		mBitmapProvider.add(getString(R.string.boulerouge));

	}

}
