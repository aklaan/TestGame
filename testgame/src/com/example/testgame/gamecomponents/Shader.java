package com.example.testgame.gamecomponents;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.opengl.GLES20;
import android.util.Log;

public class Shader {

	public String mName;

	// ! GLES20 Program
	// ! GLES20 Program
	public int mGLSLProgram_location;
	// ! Vertex shader
	public int VertexShader_location;
	// Fragment shader
	public int FragmentShader_location;


	public Shader() {
		mGLSLProgram_location = 0;
		VertexShader_location = 0;
		FragmentShader_location = 0;
	}

	public void delete() {
		// delete Vertex shader
		if (VertexShader_location != 0) {
			GLES20.glDeleteShader(VertexShader_location);
		}
		// delete Fragment shader
		if (FragmentShader_location != 0) {
			GLES20.glDeleteShader(FragmentShader_location);
		}
		// delete a GLES20 program
		if (mGLSLProgram_location != 0) {
			GLES20.glDeleteProgram(mGLSLProgram_location);
		}
	}

	
	/**
	 * 
	 * @param source
	 */
	public void setFragmentShader(String source) {
		this.FragmentShader_location = this.loadShader(source,
				GLES20.GL_FRAGMENT_SHADER);

	}

	public void setVertexShader(String source) {
		this.VertexShader_location = this.loadShader(source,
				GLES20.GL_VERTEX_SHADER);

	}

	/**
	 * 
	 * @param vertexFilename
	 * @param fragmentFilename
	 * @return
	 */
	public boolean loadShaders(String vertexCode, String fragmentCode) {
		if (this.mGLSLProgram_location == 0) {
			Log.e(this.getClass().getName(), "No GLSL Program created!");
			return false;
		}

		this.setFragmentShader(fragmentCode);
		this.setVertexShader(vertexCode);

		// if one of shader cannot be read return false
		if (this.FragmentShader_location == 0 || this.VertexShader_location == 0) {
			Log.e(this.getClass().getName(), "Shader doesn' compile");
			return false;
		}

		GLES20.glAttachShader(this.mGLSLProgram_location, this.VertexShader_location);
		GLES20.glAttachShader(this.mGLSLProgram_location,this.FragmentShader_location);
		link();
		return true;
	}

	/**
	 * 
	 * /* load a Vertex or Fragment shader
	 * 
	 * @throws IOException
	 */
	private int loadShader(String source, int shaderType) {

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
		Log.i(this.getClass().getName(), this.mName + " : " + shaderType
				+ " shader compiled");
		return shader;
	}

	public boolean link() {
		if (this.mGLSLProgram_location == 0) {
			Log.e(this.getClass().getName(),
					"Please create a GL program before Link shaders!");
			return false;
		}

		GLES20.glLinkProgram(this.mGLSLProgram_location);

		int[] linkStatus = new int[1];

		GLES20.glGetProgramiv(this.mGLSLProgram_location, GLES20.GL_LINK_STATUS,
				linkStatus, 0);

		if (linkStatus[0] != GLES20.GL_TRUE) {
			Log.e(this.getClass().getName(), "Could not link program: ");
			Log.e(this.getClass().getName(),
					"logs:" + GLES20.glGetProgramInfoLog(this.mGLSLProgram_location));
			GLES20.glDeleteProgram(this.mGLSLProgram_location);
			this.mGLSLProgram_location = 0;
			return false;
		}

		Log.i("Shader.link()", "Shader linkded");
		return true;
	}

}
