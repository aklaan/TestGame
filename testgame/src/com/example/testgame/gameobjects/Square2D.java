package com.example.testgame.gameobjects;

import com.example.testgame.gamecomponents.GameObject;
import com.example.testgame.gamecomponents.OpenGLActivity;
import com.example.testgame.gamecomponents.Vertex;

public class Square2D extends GameObject {

	static final int NB_SQUARE_VERTEX = 4;
    private float i = 0;
    private int sens = 1;
	public Square2D() {

		super(NB_SQUARE_VERTEX, 6);

		// on ajoute les 4 vertex qui compose le carr�
		this.putVertex(0, new Vertex(-1, 1, 0, 0, 0));
		this.putVertex(1, new Vertex(-1, -1, 0, 0, 1));
		this.putVertex(2, new Vertex(1, -1, 0, 1, 1));
		this.putVertex(3, new Vertex(1, 1, 0, 1, 0));

		// on indique l'ordre dans lequel on doit affichier les vertex
		// pour dessiner les 2 triangles qui vont former le carr�.
		this.putIndice(0, 0);
		this.putIndice(1, 1);
		this.putIndice(2, 2);

		this.putIndice(3, 0);
		this.putIndice(4, 2);
		this.putIndice(5, 3);
	}

	@Override
	public void onUpdate(OpenGLActivity activity){
		float limit_y = activity.getYScreenLimit();
		//Log.i("debug",String.valueOf(activity.mGLSurfaceView.getHeight()));
		//Log.i("debug",String.valueOf(i));
		
		float inc = 5.f;
		
		if (this.getCoordY()>limit_y || this.getCoordY()<-limit_y){
			sens = sens*-1;
		}  
		
		inc=inc * sens;
		
		this.Y = this.Y+inc;
	}
	
}