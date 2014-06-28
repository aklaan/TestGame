package com.example.testgame.gamecomponents;

import com.example.testgame.R;


public class Button extends Rectangle2D {
	public Texture textureUp;
	public Texture textureDown;
	public ButtonStatus status;

	public Button(float x, float y, float witdth, float hight,
			Texture textureUp, Texture textureDown) {
		super(DrawingMode.FILL);
		// TODO Auto-generated constructor stub
		this.status = ButtonStatus.UP;
		this.setCoord(x, y);
		this.setHight(hight);
		this.setWidth(witdth);
		this.setTexture(textureUp);
		this.textureUp = textureUp;
		this.textureDown = textureDown;
		this.enableColission();
		
		this.textureEnabled=true;
	}

	@Override
	public void onUpdate() {

		

		if (this.isCollideWith(this.getScene().getGameObjectByTag(R.string.USER_FINGER))) {
			this.setTexture(this.textureDown);
			this.status = ButtonStatus.DOWN;

		}

		else {
			 this.setTexture(textureUp);
			this.status = ButtonStatus.UP;
		}

	}

}
