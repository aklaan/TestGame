package com.example.testgame.scene01.gameobjects;

import android.media.MediaPlayer;

import com.example.testgame.R;
import com.example.testgame.gamecomponents.Button;
import com.example.testgame.gamecomponents.GameObject;
import com.example.testgame.gamecomponents.Texture;

public class ButtonStart extends Button {

	public ButtonStart(float x, float y, float witdth, float height,
			Texture textureUp, Texture textureDown) {
		super(x, y, witdth, height, textureUp, textureDown);
		// TODO Auto-generated constructor stub
	}

	public void onClick() {
	/**	GameObject starship = this.getScene().getGameObjectByTag(
				R.string.starship1);

		starship.getAnimation().start();
		
		*/
		// MediaPlayer mPlayer = null;
		// mPlayer = MediaPlayer.create(this.getScene().getActivity(), R.raw.aaa);
		// mPlayer.start();
		
		
	}
}
