package com.example.testgame;

public class Vertex {

	public float x;
	public float y;
	public float z;

    public final static int Vertex_SIZE = 3;
    public final static int Vertex_SIZE_BYTES = 3*4;


public Vertex() {
	x=y=z=0f;
		
}

public Vertex(float a, float b, float c) {
	x=a;
	y=b;
	z=c;	
}


public void setCoord(float a, float b, float c) {
	x=a;
	y=b;
	z=c;	
}


}
