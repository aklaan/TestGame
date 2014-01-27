package com.example.testgame.gamecomponents;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.testgame.R;

import android.opengl.GLES20;
import android.util.Log;

public class Shader {

	public String vShaderFilename = "simple_shader.vsh";
	public String fShaderFilename = "simple_shader.fsh";
	
	// ! GLES20 Program
	public int mAdressOf_GLSLProgram;
	// ! Vertex shader
	public int mAdressOf_VertexShader;
	// Fragment shader
	public int mAdressOf_FragmentShader;

	// ! ProgramName (Vertex filename is ProgramName.vsh and Fragment filename
	// is ProgramName.fsh)
	public String mProgramName;
	
	
	public 	ArrayList<String> attribListNames;
	public 	HashMap<String, String> attribCatlg;
	
	public ArrayList<String> uniformListNames; 
	public HashMap<String, String> uniformCatlg; 
	
	public Shader(){
		mAdressOf_GLSLProgram = 0;
		mAdressOf_VertexShader = 0;
		mAdressOf_FragmentShader = 0;
		
		// On crée le GSL Program dans OpenGL et on mémorise
		// son adresse.
		mAdressOf_GLSLProgram = GLES20.glCreateProgram();

	}
	
	public void delete() {
		// delete Vertex shader
		if (mAdressOf_VertexShader != 0) {
			GLES20.glDeleteShader(mAdressOf_VertexShader);
		}
		// delete Fragment shader
		if (mAdressOf_FragmentShader != 0) {
			GLES20.glDeleteShader(mAdressOf_FragmentShader);
		}
		// delete a GLES20 program
		if (mAdressOf_GLSLProgram != 0) {
			GLES20.glDeleteProgram(mAdressOf_GLSLProgram);
		}
	}


	public boolean make() {
		
for (String names : this.attribListNames){
	
	
	
}
		
		// attributes
		mAdressOf_VertexPosition = GLES20.glGetAttribLocation(mAdressOf_GLSLProgram, "aPosition");
		mAdressOf_VertexColor = GLES20.glGetAttribLocation(mAdressOf_GLSLProgram, "aColor");
		mAdressOf_TextCoord = GLES20.glGetAttribLocation(mAdressOf_GLSLProgram, "aTexCoord");

		// uniforms
		mAdressOf_Mvp = GLES20.glGetUniformLocation(mAdressOf_GLSLProgram,"uMvp");

		// samplers
		mAdressOf_Texture0 = GLES20.glGetUniformLocation(mAdressOf_GLSLProgram,"tex0");

		Log.i(this.getClass().getName(),
				"Default Shader program compiled & linked: ");
		return true;
	}

	/**
	 * 
	 * @param source
	 */
	public void setFragmentShader(String source){
		this.mAdressOf_FragmentShader = this.loadShader(source, GLES20.GL_FRAGMENT_SHADER);
		
	}
	
	public void setVertexShader(String source){
		this.mAdressOf_VertexShader = this.loadShader(source, GLES20.GL_VERTEX_SHADER);
		
	}
	
	/**
	 * 
	 * @param vertexFilename
	 * @param fragmentFilename
	 * @return
	 */
	public boolean loadShaders(String vertexCode, String fragmentCode) {
		if (mAdressOf_GLSLProgram == 0) {
			Log.e(this.getClass().getName(), "No GLSL Program created!");
			return false;
		}

		this.setFragmentShader(fragmentCode);
		this.setVertexShader(vertexCode);
		
				// if one of shader cannot be read return false
		if (mAdressOf_VertexShader == 0 || mAdressOf_FragmentShader == 0) {
			Log.e(this.getClass().getName(), "Shader doesn' compile");
			return false;
		}

		GLES20.glAttachShader(mAdressOf_GLSLProgram, mAdressOf_VertexShader);
		GLES20.glAttachShader(mAdressOf_GLSLProgram, mAdressOf_FragmentShader);
		link();
		return true;
	}

	/**
	 * 
	 * /* load a Vertex or Fragment shader
	 * @throws IOException 
	 */
	private int loadShader(String source,int shaderType) {
		
		int shader = GLES20.glCreateShader(shaderType);

		if (shader != 0) {
			GLES20.glShaderSource(shader, source);
			GLES20.glCompileShader(shader);
			int[] compiled = new int[1];
			GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
			if (compiled[0] == 0) {
				Log.e(this.getClass().getName(), "Could not compile shader "
						+ shaderType + ":");
				Log.e(this.getClass().getName(),
						GLES20.glGetShaderInfoLog(shader));
				GLES20.glDeleteShader(shader);
				shader = 0;
			}
		}
		
		return shader;
	}


	private boolean link() {
		if (mAdressOf_GLSLProgram == 0) {
			Log.e(this.getClass().getName(),
					"Please create a GL program before Link shaders!");
			return false;
		}

		GLES20.glLinkProgram(mAdressOf_GLSLProgram);

		int[] linkStatus = new int[1];

		GLES20.glGetProgramiv(mAdressOf_GLSLProgram, GLES20.GL_LINK_STATUS,
				linkStatus, 0);

		if (linkStatus[0] != GLES20.GL_TRUE) {
			Log.e(this.getClass().getName(), "Could not link program: ");
			Log.e(this.getClass().getName(),
					"logs:" + GLES20.glGetProgramInfoLog(mAdressOf_GLSLProgram));
			GLES20.glDeleteProgram(mAdressOf_GLSLProgram);
			mAdressOf_GLSLProgram = 0;
			return false;
		}
		return true;
	}


}



