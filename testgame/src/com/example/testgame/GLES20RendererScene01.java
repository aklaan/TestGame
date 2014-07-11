package com.example.testgame;

import javax.microedition.khronos.opengles.GL10;

import android.os.SystemClock;

import com.example.testgame.R;
import com.example.testgame.gamecomponents.AnimationRightLeftOnX;
import com.example.testgame.gamecomponents.AnimationRotate;
import com.example.testgame.gamecomponents.Button;
import com.example.testgame.gamecomponents.Cube;
import com.example.testgame.gamecomponents.DrawingMode;
import com.example.testgame.gamecomponents.GameObject;
import com.example.testgame.gamecomponents.OpenGLActivity;
import com.example.testgame.gamecomponents.Rectangle2D;
import com.example.testgame.gamecomponents.Scene;
import com.example.testgame.scene01.gameobjects.ArrayGameObject;
import com.example.testgame.scene01.gameobjects.ButtonStart;
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

		Rectangle2D model = new Rectangle2D(DrawingMode.FILL);
		this.getBitmapProvider().linkTexture(R.string.textureTestAnim, model);
		model.setAnimation(new AnimationRotate(model));
		model.getAnimation().start();

		model.setheight(50);
		model.setWidth(50);
		// this.addToScene(model);
		//this.addToScene(ArrayGameObject.make(this.getWidth(), this.getHeight(),	4, 4, model, 1));

		this.getBitmapProvider().linkTexture(R.string.textureBlue, model);

		ButtonStart buttonStart = new ButtonStart(0, 0, 100, 100, this
				.getBitmapProvider().getTexture(R.string.startUp), this
				.getBitmapProvider().getTexture(R.string.startDown));

		
		
		Cube cube = new Cube(DrawingMode.EMPTY);
		cube.setheight(50);
		cube.setWidth(50);
		cube.setDepth(50);
		cube.setCoord(50, 50);
		cube.viewMode="PERS";
		this.addToScene(cube);
		// this.addToScene(buttonStart);
		// this.addToScene(ArrayGameObject.make(this.getWidth(),this.getHeight(),5,5,buttonStart,50));

		/*
		 * Button buttonStart = new
		 * ButtonStart(300,600,80,80,this.getBitmapProvider
		 * ().getTexture(R.string
		 * .startUp),this.getBitmapProvider().getTexture(R.string.startDown));
		 * this.addToScene(buttonStart);
		 */
		/*
		 * Button buttonStart2 = new
		 * ButtonStart(0,0,50,50,this.getBitmapProvider
		 * ().getTexture(R.string.textureRed
		 * ),this.getBitmapProvider().getTexture(R.string.textureRed));
		 * this.addToScene(buttonStart2);
		 * 
		 * Button buttonStart3 = new
		 * ButtonStart(100,100,50,50,this.getBitmapProvider
		 * ().getTexture(R.string
		 * .textureRed),this.getBitmapProvider().getTexture
		 * (R.string.textureRed)); this.addToScene(buttonStart3);
		 */

		Rectangle2D ligne1 = new Rectangle2D(DrawingMode.FILL);
		ligne1.setCoord(0, 0);
		ligne1.setheight((float) this.getHeight());
		ligne1.setWidth(2);
		ligne1.setTagName(R.string.ligne1);
		this.getBitmapProvider().linkTexture(R.string.textureRed, ligne1);

		 this.addToScene(ligne1);

		Rectangle2D ligne100x = new Rectangle2D(DrawingMode.FILL);
		ligne100x.setCoord(100, 0);
		ligne100x.setheight((float) this.getHeight());
		ligne100x.setWidth(1);
		ligne100x.setTagName(R.string.ligne1);
		this.getBitmapProvider().linkTexture(R.string.textureWhite, ligne100x);

		 this.addToScene(ligne100x);

		Rectangle2D ligne200x = new Rectangle2D(DrawingMode.FILL);
		ligne200x.setCoord(200, 0);
		ligne200x.setheight((float) this.getHeight());
		ligne200x.setWidth(1);
		ligne200x.setTagName(R.string.ligne1);
		this.getBitmapProvider().linkTexture(R.string.textureWhite, ligne200x);

		 this.addToScene(ligne200x);

		// **********
		Rectangle2D ligne2 = new Rectangle2D(DrawingMode.FILL);
		ligne2.setCoord(0, 0);
		ligne2.setheight(20);
		ligne2.setWidth(this.getWidth());
		ligne2.setTagName(R.string.ligne2);
		this.getBitmapProvider().linkTexture(R.string.textureRed, ligne2);

		// this.addToScene(ligne2);

		// ******************
		Starship mStarship = new Starship();

		mStarship.setheight(20);
		mStarship.setWidth(20);
		mStarship.setCoord(0, 0);
		
		mStarship.setTagName(R.string.starship1);
		mStarship.enableColission();
		mStarship.viewMode = "PERSPECTIVE";
		this.getBitmapProvider().linkTexture(R.string.textureWhite, mStarship);
		mStarship.setAnimation(new AnimationRightLeftOnX(mStarship));
		this.addToScene(mStarship);

		// ***********************
		Starship mStarship2 = new Starship();
		mStarship2.setheight(5);
		mStarship2.setWidth(5);
		mStarship2.enableColission();
		this.getBitmapProvider().linkTexture(R.string.boulerouge, mStarship2);
		mStarship2.setTagName(R.string.starship2);
		mStarship2.cible = mStarship;
		
		// this.addToScene(mStarship2);

		// *********************************
		PetitRobot mPetitRobot = new PetitRobot();
		mPetitRobot.setCoord(50, 50);
		mPetitRobot.setheight(30);
		mPetitRobot.setWidth(30);
		mPetitRobot.enableColission();
		this.getBitmapProvider()
				.linkTexture(R.string.textureRobot, mPetitRobot);

		// this.addToScene(mPetitRobot);

		mStarship.getGameObjectToListenList().add(mStarship2);

		// mStarship.cible = mPetitRobot;

		mStarship2.cible = mStarship;

	}

	@Override
	public void initProgramShader() {
		this.getProgramShaderProvider().catalogShader.clear();
		this.getProgramShaderProvider().shaderList.clear();

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

		/*
		 * this.getBitmapProvider().add(
		 * this.mActivity.getString(R.string.textureStarship));
		 * this.getBitmapProvider().add(
		 * this.mActivity.getString(R.string.textureRobot));
		 * this.getBitmapProvider().add(
		 * this.mActivity.getString(R.string.textureRed));
		 * this.getBitmapProvider().add(
		 * this.mActivity.getString(R.string.boulerouge));
		 */

		this.getBitmapProvider().add(R.string.textureStarship);
		this.getBitmapProvider().add(R.string.textureRobot);
		this.getBitmapProvider().add(R.string.textureRed);
		this.getBitmapProvider().add(R.string.boulerouge);
		this.getBitmapProvider().add(R.string.startUp);
		this.getBitmapProvider().add(R.string.startDown);
		this.getBitmapProvider().add(R.string.textureWhite);
		this.getBitmapProvider().add(R.string.textureBlue);
		this.getBitmapProvider().add(R.string.textureTest);
		this.getBitmapProvider().add(R.string.textureTestAnim);
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
					starship.angleRADZ += 1.5f;
				}

			}

		}

	}
}
