package com.rdupuis.gamefactory;

import javax.microedition.khronos.opengles.GL10;

import android.os.SystemClock;


import com.rdupuis.gamefactory.animations.AnimationRightLeftOnX;
import com.rdupuis.gamefactory.animations.AnimationRotate;
import com.rdupuis.gamefactory.components.GameObject;
import com.rdupuis.gamefactory.components.OpenGLActivity;
import com.rdupuis.gamefactory.components.Scene;
import com.rdupuis.gamefactory.components.shapes.Cube;
import com.rdupuis.gamefactory.components.shapes.Rectangle2D;
import com.rdupuis.gamefactory.enums.DrawingMode;
import com.rdupuis.gamefactory.scene01.components.ButtonStart;
import com.rdupuis.gamefactory.scene01.components.PetitRobot;
import com.rdupuis.gamefactory.scene01.components.ProgramShader_forLines;
import com.rdupuis.gamefactory.scene01.components.ProgramShader_grille;
import com.rdupuis.gamefactory.scene01.components.ProgramShader_simple;
import com.rdupuis.gamefactory.scene01.components.Starship;

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

		
		Rectangle2D background = new Rectangle2D(DrawingMode.FILL);
		background.setCoord((float) this.getWidth()/2, (float) this.getHeight()/2);
		background.sethight((float) this.getHeight());
		background.setWidth((float) this.getWidth());;
		background.setTagName(R.string.background);
		this.getBitmapProvider().linkTexture(R.string.textureisoland, background);

		 this.addToScene(background);
		
		
		 Rectangle2D spyro = new Rectangle2D(DrawingMode.FILL);
		 spyro.setCoord((float) this.getWidth()/2, (float) this.getHeight()/2);
		 spyro.X += 100;
		 spyro.sethight(100);
		 spyro.setWidth(100);
			spyro.setTagName(R.string.spyro);
			this.getBitmapProvider().linkTexture(R.string.texturespyro, spyro);

			 this.addToScene(spyro);
			
		
		Rectangle2D model = new Rectangle2D(DrawingMode.FILL);
		this.getBitmapProvider().linkTexture(R.string.textureTestAnim, model);
		model.setAnimation(new AnimationRotate(model));
		model.getAnimation().start();

		model.sethight(50);
		model.setWidth(50);
		// this.addToScene(model);
		//this.addToScene(ArrayGameObject.make(this.getWidth(), this.getHeight(),	4, 4, model, 1));

		this.getBitmapProvider().linkTexture(R.string.textureBlue, model);

		ButtonStart buttonStart = new ButtonStart(0, 0, 100, 100, this
				.getBitmapProvider().getTexture(R.string.startUp), this
				.getBitmapProvider().getTexture(R.string.startDown));

		
		
		Cube cube = new Cube(DrawingMode.EMPTY);
		cube.sethight(5);
		cube.setWidth(200);
		cube.setDepth(100);
		cube.setCoord(0, 0,0);
		cube.viewMode="PERS";
		this.getBitmapProvider().linkTexture(R.string.textureWhite, cube);
		this.addToScene(cube);
		
		cube = new Cube(DrawingMode.EMPTY);
		cube.sethight(20);
		cube.setWidth(20);
		cube.setDepth(20);
		cube.setCoord(0, 10,0);
		cube.viewMode="PERS";
		this.getBitmapProvider().linkTexture(R.string.textureWhite, cube);
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

		Rectangle2D ligne = new Rectangle2D(DrawingMode.FILL);
		ligne.setCoord(0, 0);
		ligne.viewMode="pers";
		ligne.sethight((float) this.getHeight());
		ligne.setWidth(1);
		ligne.setTagName(R.string.ligne1);
		this.getBitmapProvider().linkTexture(R.string.textureRed, ligne);

		 this.addToScene(ligne);


			 ligne = new Rectangle2D(DrawingMode.FILL);
			ligne.setCoord(0, 0);
			ligne.viewMode="pers";
			ligne.sethight(1);
			ligne.setWidth((float) this.getWidth());
			ligne.setTagName(R.string.ligne1);
			this.getBitmapProvider().linkTexture(R.string.textureRed, ligne);

			 this.addToScene(ligne);

		 
		 
		
		Rectangle2D ligne1 = new Rectangle2D(DrawingMode.FILL);
		ligne1.setCoord(0, 0);
		ligne1.sethight((float) this.getHeight());
		ligne1.setWidth(2);
		ligne1.setTagName(R.string.ligne1);
		this.getBitmapProvider().linkTexture(R.string.textureRed, ligne1);

		 this.addToScene(ligne1);

		Rectangle2D ligne100x = new Rectangle2D(DrawingMode.FILL);
		ligne100x.setCoord(100, this.getHeight()/2);
		ligne100x.sethight((float) this.getHeight());
		ligne100x.setWidth(1);
		ligne100x.setTagName(R.string.ligne1);
		this.getBitmapProvider().linkTexture(R.string.textureWhite, ligne100x);

		 this.addToScene(ligne100x);

		Rectangle2D ligne200x = new Rectangle2D(DrawingMode.FILL);
		ligne200x.setCoord(200, this.getHeight()/2);
		ligne200x.sethight((float) this.getHeight());
		ligne200x.setWidth(1);
		ligne200x.setTagName(R.string.ligne1);
		this.getBitmapProvider().linkTexture(R.string.textureWhite, ligne200x);

		 this.addToScene(ligne200x);

		// **********
		Rectangle2D ligne2 = new Rectangle2D(DrawingMode.FILL);
		ligne2.setCoord(0, 0);
		ligne2.sethight(20);
		ligne2.setWidth(this.getWidth());
		ligne2.setTagName(R.string.ligne2);
		this.getBitmapProvider().linkTexture(R.string.textureRed, ligne2);

		// this.addToScene(ligne2);

		// ******************
		Starship mStarship = new Starship();

		mStarship.sethight(100);
		mStarship.setWidth(100);
		mStarship.setCoord(0, 0);
		
		mStarship.setTagName(R.string.starship1);
		mStarship.enableColission();
		//mStarship.viewMode = "PERSPECTIVE";
		this.getBitmapProvider().linkTexture(R.string.textureWhite, mStarship);
		mStarship.setAnimation(new AnimationRightLeftOnX(mStarship));
		this.addToScene(mStarship);

		// ***********************
		Starship mStarship2 = new Starship();
		mStarship2.sethight(10);
		mStarship2.setWidth(10);
		mStarship.setCoord(0, 20);
		mStarship2.viewMode="pers";
		mStarship2.enableColission();
		this.getBitmapProvider().linkTexture(R.string.boulerouge, mStarship2);
		mStarship2.setTagName(R.string.starship2);
		mStarship2.cible = mStarship;
		
		// this.addToScene(mStarship2);

		// *********************************
		PetitRobot mPetitRobot = new PetitRobot();
		mPetitRobot.setCoord(0, 0);
		
		mPetitRobot.sethight(10);
		mPetitRobot.setWidth(10);
		mPetitRobot.enableColission();
		this.getBitmapProvider()
				.linkTexture(R.string.textureRobot, mPetitRobot);

//		 this.addToScene(mPetitRobot);

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
		this.getBitmapProvider().add(R.string.textureisoland);
		this.getBitmapProvider().add(R.string.texturespyro);
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
