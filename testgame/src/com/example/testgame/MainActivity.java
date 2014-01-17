package com.example.testgame;

import com.example.testgame.gamecomponents.BitmapProvider;
import com.example.testgame.gamecomponents.OpenGLActivity;

import android.os.Bundle;

public class MainActivity extends OpenGLActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void init() {

		mBitmapProvider = new BitmapProvider(this);
		mBitmapProvider.add(getString(R.string.textureStarship));
		mBitmapProvider.add(getString(R.string.textureRobot));

	}

}
