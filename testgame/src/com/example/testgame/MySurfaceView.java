package com.example.testgame;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

public class MySurfaceView extends GLSurfaceView {

	public float touchX = 0;
	public float touchY = 0;
	public boolean touched = false;

	public MySurfaceView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
			switch (e.getAction()) {
		case MotionEvent.ACTION_UP:
			touched = false;
			break;
		case MotionEvent.ACTION_DOWN:
			
			this.touchX = e.getX();
			this.touchY = e.getY();
			touched = true;
			break;	
			}

		return true;

	}

}
