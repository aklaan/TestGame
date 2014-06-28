package com.example.testgame;

import javax.microedition.khronos.opengles.GL10;

import android.os.SystemClock;

import com.example.testgame.R;
import com.example.testgame.gamecomponents.Button;
import com.example.testgame.gamecomponents.DrawingMode;
import com.example.testgame.gamecomponents.GameObject;
import com.example.testgame.gamecomponents.OpenGLActivity;
import com.example.testgame.gamecomponents.Rectangle2D;
import com.example.testgame.gamecomponents.Scene;
import com.example.testgame.scene01.gameobjects.PetitRobot;
import com.example.testgame.scene01.gameobjects.ProgramShader_forLines;
import com.example.testgame.scene01.gameobjects.ProgramShader_grille;
import com.example.testgame.scene01.gameobjects.ProgramShader_simple;
import com.example.testgame.scene01.gameobjects.Starship;

/**
 * GLES20Renderer: the OGLES 2.0 Thread.
 */
public class GLES20RendererScene01 extends Scene {

	public GLES20RendererScene01(OpenGLActivity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void loadGameObjects() {

		Button buttonStart = new Button(300,300,80,40,this.getBitmapProvider().getTexture(R.string.textureRed),this.getBitmapProvider().getTexture(R.string.boulerouge));
				
		this.addToScene(buttonStart);
		
		Rectangle2D ligne1 = new Rectangle2D(DrawingMode.FILL);
		ligne1.setCoord(0, 0);
		ligne1.setHight(500);
		ligne1.setWidth(2);
		ligne1.setTagName(R.string.ligne1);
		this.getBitmapProvider().linkTexture(R.string.textureRed, ligne1);

		this.addToScene(ligne1);

		// **********
		Rectangle2D ligne2 = new Rectangle2D(DrawingMode.FILL);
		ligne2.setCoord(0, 0);
		ligne2.setHight(2);
		ligne2.setWidth(1000);
		ligne2.setTagName(R.string.ligne2);
		this.getBitmapProvider().linkTexture(R.string.textureRed, ligne2);

		this.addToScene(ligne2);

		// ******************
		Starship mStarship = new Starship();

		mStarship.setHight(50);
		mStarship.setWidth(50);
		mStarship.setCoord(110, 90);
		mStarship.angleRAD = 0.0f;
		mStarship.setTagName(R.string.starship1);
		mStarship.enableColission();

		this.getBitmapProvider().linkTexture(R.string.boulerouge, mStarship);

		this.addToScene(mStarship);
		mStarship.getGameObjectToListenList().add(buttonStart);
		// ***********************
		Starship mStarship2 = new Starship();
		mStarship2.setHight(5);
		mStarship2.setWidth(5);
		mStarship2.enableColission();
		this.getBitmapProvider().linkTexture(R.string.boulerouge, mStarship2);
		mStarship2.setTagName(R.string.starship2);
		mStarship2.cible = mStarship;
		mStarship2.angleRAD = 0.0f;
		this.addToScene(mStarship2);

		// *********************************
		PetitRobot mPetitRobot = new PetitRobot();
		mPetitRobot.setCoord(50, 50);
		mPetitRobot.setHight(30);
		mPetitRobot.setWidth(30);
		mPetitRobot.enableColission();
		this.getBitmapProvider().linkTexture(R.string.textureRobot, mPetitRobot);

		this.addToScene(mPetitRobot);

		mStarship.getGameObjectToListenList().add(mStarship2);

		// mStarship.cible = mPetitRobot;

		mStarship2.cible = mStarship;

	}

	@Override
	public void initProgramShader() {
		ProgramShader_grille shader_grille = new ProgramShader_grille();
		shader_grille.make();
		this.getProgramShaderProvider().add(shader_grille);

		this.getProgramShaderProvider().add(new ProgramShader_simple());

		ProgramShader_forLines shader_forLines = new ProgramShader_forLines();
		shader_forLines.make();
		this.getProgramShaderProvider().add(shader_forLines);

	}

	@Override
	public void loadTextures() {

/*		this.getBitmapProvider().add(
				this.mActivity.getString(R.string.textureStarship));
		this.getBitmapProvider().add(
				this.mActivity.getString(R.string.textureRobot));
		this.getBitmapProvider().add(
				this.mActivity.getString(R.string.textureRed));
		this.getBitmapProvider().add(
				this.mActivity.getString(R.string.boulerouge));
*/
		
		this.getBitmapProvider().add(R.string.textureStarship);
		this.getBitmapProvider().add(R.string.textureRobot);
		this.getBitmapProvider().add(R.string.textureRed);
		this.getBitmapProvider().add(R.string.boulerouge);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		super.onDrawFrame(gl);

		// action suplémentaire
		if (this.getActivity().getSurfaceView().touched) {

			float elapsedTime = SystemClock.elapsedRealtime()
					- this.getActivity().getSurfaceView().getLastTouchTime();
			// on attend une 1/4 de seconde avant de valider un autre touch
			if (elapsedTime > 00) {

				GameObject starship = getGameObjectByTag(R.string.starship1);
				if (starship != null) {
					starship.angleRAD += 1.5f;
				}

			}

		}

	}
}
