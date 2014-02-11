package com.example.testgame.gamecomponents;

import com.example.testgame.Enums;
import com.example.testgame.GLES20RendererScene01;
import com.example.testgame.gameobjects.ProgramShader_forLines;
import com.example.testgame.gameobjects.ProgramShader_simple;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

public class CollisionBox extends Rectangle2D{

	public float offsetX;
	public float offsetY;
	public GameObject parent;
	
	public CollisionBox(GameObject go){
		super(Enums.drawMode.EMPTY);
		this.isVisible=true;
		this.drawMode = GLES20.GL_LINES;
		this.hasTexture = false;
		this.offsetX = 10.0f;
		this.offsetY = 10.0f;
		this.parent = go;
		
				
		this.update();
	}
	
	
	public void update(){
		// aller rechercher les points limite de la forme et en déduire
		// un rectangle avec un retrait edgelimit
		
		// naviguer dans le float buffer des x,y,z
		
		
		int taillemax = this.parent.mVertices.capacity();
		this.parent.mVertices.position(0);
		int i=0;
		float xread=0f;
		float yread=0f;
		float xmin=0f;
		float xmax=0f;
		float ymin=0f;
		float ymax=0f;
		
		while (i<taillemax-1){
		
			//lecture du X
			xread = this.parent.mVertices.get();
			xmin = (xread <xmin)? xread:xmin;
			xmax = (xread >xmax)? xread:xmax;
			i++;
			//lecture du Y
			yread = this.parent.mVertices.get();
			ymin = (yread <ymin)? xread:xmin;
			ymax = (yread >ymax)? xread:xmax;
			i++;
			//lecture du Z
			xread = this.parent.mVertices.get();
			i++;	
		/*	Log.i("xy",String.valueOf(i)
					+" / "
					+String.valueOf(taillemax)
					+" : "
					+String.valueOf(xmin)+ "/" + String.valueOf(xmax));
		*/
		}
		
		this.setWidth(xmax-xmin - ((xmax-xmin)*(offsetX/100)));
		this.setHeight(ymax-ymin - ((ymax-ymin)*(offsetY/100)));
		this.setCoord(this.parent.X, this.parent.Y);
	}
	
	@Override
	public void draw (GLES20RendererScene01 renderer) {

		ProgramShader_forLines sh = (ProgramShader_forLines) renderer.mProgramShaderProvider
				.getShaderByName("forLines");
		renderer.mProgramShaderProvider.use(sh);

		GLES20.glDisable(GLES20.GL_TEXTURE_2D);

		// on se positionne au debut du Buffer des indices
		// qui indiquent dans quel ordre les vertex doivent être dessinés
		this.getIndices().position(0);

		// on charge les coordonnées des vertices
		sh.setVerticesCoord(this.getVertices());

		// on charge les coordonées de texture
		this.getVertices().position(0);
		sh.setTextureCoord(this.getTextCoord());

		// if (sh.attrib_color_location != -1) {
		// this.getVertices().position(0);
		// GLES20.glVertexAttribPointer(sh.attrib_color_location, 4,
		// GLES20.GL_FLOAT, false, Vertex.Vertex_TEXT_SIZE_BYTES, color);

		sh.enableShaderVar();
		
		
		float[] mMvp = new float[16];
		// équivalent du PUSH
		this.mBackupModelView = renderer.mModelView.clone();

		Matrix.setIdentityM(renderer.mModelView, 0);
		
		Matrix.translateM(renderer.mModelView, 0, X, Y, 0);

		Matrix.multiplyMM(mMvp, 0, renderer.mProjectionView, 0,
				renderer.mModelView, 0);
		// on alimente la donnée UNIFORM mAdressOf_Mvp du programme OpenGL
		// avec
		// une matrice de 4 flotant.
		GLES20.glUniformMatrix4fv(sh.uniform_mvp_location, 1, false,
				mMvp, 0);

		// on se positionne au debut du Buffer des indices
		// qui indiquent dans quel ordre les vertex doivent être dessinés
		this.getIndices().position(0);

		
		GLES20.glDrawElements(this.drawMode, this.getIndices().capacity(),
				GLES20.GL_UNSIGNED_SHORT, this.getIndices());

		//renderer.mProgramme1.disableVertexAttribArray();
		// équivalent du POP
		renderer.mModelView = this.mBackupModelView;
//		renderer.mProgramme1.disableVertexAttribArray();

		GLES20.glEnable(GLES20.GL_TEXTURE_2D);

	}

	
	
	
	
	
}
