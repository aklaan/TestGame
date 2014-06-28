package com.example.testgame.gamecomponents;

import com.example.testgame.R;



public class UserFinger extends Rectangle2D {

	public UserFinger() {
		super(DrawingMode.EMPTY);
		this.setHight(10);
		this.setWidth(10);
		this.enableColission();
		this.setTagName(R.string.USER_FINGER);
		this.isStatic=false;
	}

	public void onUpdate() {
		// si l'utilisateur touche l'�cran
		if (this.getScene().getActivity().mGLSurfaceView.touched) {

			// on met � jour les coordon�es
			this.setCoord(
					this.getScene().getActivity().mGLSurfaceView.touchX,
					this.getScene().getActivity().getYScreenLimit()
							- this.getScene().getActivity().mGLSurfaceView.touchY);
			// on active les colissions
			this.canCollide = true;
		} else {
			//sinon les collisions sont d�sactiv�es
			this.canCollide = false;
		}

	}
}
