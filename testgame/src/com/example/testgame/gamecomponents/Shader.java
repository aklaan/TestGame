package com.example.testgame.gamecomponents;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.opengl.GLES20;
import android.util.Log;

public class Shader {

	// public String vShaderFilename = "simple_shader.vsh";
	// public String fShaderFilename = "simple_shader.fsh";

	public String mName;

	// ! GLES20 Program
	public int mAdressOf_GLSLProgram;
	// ! Vertex shader
	public int mAdressOf_VertexShader;
	// Fragment shader
	public int mAdressOf_FragmentShader;

	// ! ProgramName (Vertex filename is ProgramName.vsh and Fragment filename
	// is ProgramName.fsh)
	public String mProgramName;

	public ArrayList<String> attribListNames;
	public HashMap<String, Integer> attribCatlg;

	public ArrayList<String> uniformListNames;
	public HashMap<String, Integer> uniformCatlg;

	public Shader() {
		mAdressOf_GLSLProgram = 0;
		mAdressOf_VertexShader = 0;
		mAdressOf_FragmentShader = 0;

		// On crée le GSL Program dans OpenGL et on mémorise
		// son adresse.
		mAdressOf_GLSLProgram = GLES20.glCreateProgram();

		attribListNames = new ArrayList<String>();
		uniformListNames = new ArrayList<String>();
		attribCatlg = new HashMap<String, Integer>();
		uniformCatlg = new HashMap<String, Integer>();
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

	public void initAttribLocation() {
		for (String attribName : this.attribListNames) {
			// je passe pas une variable intermédiaire sinon le PUT ne
			// fonctionne pas bien.
			String temps = attribName;
			this.attribCatlg.put(temps,
					GLES20.glGetAttribLocation(mAdressOf_GLSLProgram, temps));
			Log.i("initAttribLocation",
					attribName
							+ " @ "
							+ GLES20.glGetAttribLocation(mAdressOf_GLSLProgram,
									temps) + " rc: "
							+ String.valueOf(GLES20.glGetError()));
		}
	}

	public void initUniformLocation() {
		for (String uniformName : this.uniformListNames) {
			String temps = uniformName;
			uniformCatlg.put(temps,
					GLES20.glGetUniformLocation(mAdressOf_GLSLProgram, temps));
			Log.i("initUniformLocation",
					uniformName
							+ " @ "
							+ GLES20.glGetUniformLocation(
									mAdressOf_GLSLProgram, temps) + " rc: "
							+ String.valueOf(GLES20.glGetError()));
		}

	}

	public int getAdressOfUniform(String name) {
		return uniformCatlg.get(name);

	}

	public int getAdressOfAttrib(String name) {
		return attribCatlg.get(name);

	}

	/**
	 * 
	 * @param source
	 */
	public void setFragmentShader(String source) {
		this.mAdressOf_FragmentShader = this.loadShader(source,
				GLES20.GL_FRAGMENT_SHADER);

	}

	public void setVertexShader(String source) {
		this.mAdressOf_VertexShader = this.loadShader(source,
				GLES20.GL_VERTEX_SHADER);

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
		Log.i(this.getClass().getName(),  this.mName +" : "+ shaderType + " shader compiled" );
		return shader;
	}

	public boolean link() {
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
		
		Log.i("Shader.link()", "Shader linkded");
		return true;
	}

	public void enableShaderVar() {
		// si l'adresse mémoire de l'objet désigné par mAdressOf_VertexPosition
		// shader.enableShaderVar(); // n'est pas vide

		for (String name : this.attribListNames) {
			String temps = name;
			
			int memoryAdress = this.getAdressOfAttrib(temps);
			if (memoryAdress != -1) {
				GLES20.glEnableVertexAttribArray(memoryAdress);
				Log.i("shader.enableShaderVar()", "Enable attrib " + name + " @ " + this.attribCatlg.get(temps));
				if (GLES20.glGetError() != GLES20.GL_NO_ERROR) {
					Log.i("shader.enableShaderVar()",
							"Fail to enable attrib " + name + "@"

							+ String.valueOf(memoryAdress) + " RC:"
									+ String.valueOf(GLES20.glGetError()));
				}
			}

		}

		for (String name : this.uniformListNames) {
			String temps = name;
			
			int memoryAdress = this.getAdressOfUniform(temps);
			if (memoryAdress != -1) {
				GLES20.glEnableVertexAttribArray(memoryAdress);
				Log.i("shader.enableShaderVar()", "Enable Uniform " + name + " @ " + this.uniformCatlg.get(temps));
				if (GLES20.glGetError() != GLES20.GL_NO_ERROR) {
					Log.i("mdebug",
							"Fail to enable Uniform " + name + "@"
									+ String.valueOf(memoryAdress) + " RC:"
									+ " " + String.valueOf(GLES20.glGetError()));

				}
			}

		}
	}

	public void disableShaderVar() {
		// si l'adresse mémoire de l'objet désigné par mAdressOf_VertexPosition
		// n'est pas vide

		for (String name : this.attribListNames) {
			int memoryAdress = this.attribCatlg.get(name);
			if (memoryAdress != -1) {
				GLES20.glDisableVertexAttribArray(memoryAdress);
			}

		}

		for (String name : this.uniformListNames) {
			int memoryAdress = uniformCatlg.get(name);
			if (memoryAdress != -1) {
				GLES20.glDisableVertexAttribArray(memoryAdress);
			}

		}
	}

}
