package com.example.testgame.gamecomponents;

import android.os.SystemClock;

import com.example.testgame.R;

public abstract class Button extends Rectangle2D implements Clikable {
	public Texture textureUp;
	public Texture textureDown;
	public ButtonStatus status;
	private float lastClick;
	private final float DELAY_BTWN_CLICK = 200; //200ms

	public Button(float x, float y, float witdth, float height,
			Texture textureUp, Texture textureDown) {
		super(DrawingMode.FILL);
		// TODO Auto-generated constructor stub
		this.status = ButtonStatus.UP;
		this.setCoord(x, y);
		this.setheight(height);
		this.setWidth(witdth);
		// this.setTexture(textureUp);
		this.textureUp = textureUp;
		this.textureDown = textureDown;
		this.enableColission();
		this.isStatic = false;
		this.textureEnabled = true;
	}

	@Override
	public void onUpdate() {
		if (SystemClock.elapsedRealtime() - this.lastClick > DELAY_BTWN_CLICK) {

			if (this.isCollideWith(this.getScene().getGameObjectByTag(
					R.string.USER_FINGER))) {
				this.setTexture(this.textureDown);
				this.status = ButtonStatus.DOWN;
				lastClick = SystemClock.elapsedRealtime();
				this.onClick();
			}

			else {
				this.setTexture(textureUp);
				this.status = ButtonStatus.UP;
			}
		}
	}

	public void onClick() {

	}
}
