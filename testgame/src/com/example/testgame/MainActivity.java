package com.example.testgame;

import java.util.ArrayList;

import com.example.testgame.gamecomponents.BitmapProvider;
import com.example.testgame.gamecomponents.GameObject;
import com.example.testgame.gamecomponents.OpenGLActivity;
import com.example.testgame.gamecomponents.Rectangle2D;
import com.example.testgame.gamecomponents.ProgramShaderProvider;
import com.example.testgame.gameobjects.PetitRobot;
import com.example.testgame.gameobjects.ProgramShader_grille;
import com.example.testgame.gameobjects.ProgramShader_simple;
import com.example.testgame.gameobjects.Starship;

import android.os.Bundle;

public class MainActivity extends OpenGLActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// on définit une liste des composants de jeu
		mGLSurfaceView.setRenderer(new GLES20RendererScene01(this));
		
		
	}



}
