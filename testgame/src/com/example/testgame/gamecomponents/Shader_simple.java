package com.example.testgame.gamecomponents;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.testgame.R;

import android.app.Activity;
import android.opengl.GLES20;
import android.util.Log;

public class Shader_simple {

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

	// déclaration des attributs du shader : default
	public final String DEFAULT_VSH_ATTRIB_VERTEX_COORD = "aPosition";
	public final String DEFAULT_VSH_ATTRIB_COLOR = "aColor";
	public final String DEFAULT_VSH_ATTRIB_TEXTURE_COORD = "aTexCoord";

	public final String DEFAULT_VSH_UNIFORM_MVP = "uMvp";
	public final String DEFAULT_FSH_UNIFORM_TEXTURE = "tex0";

	private final String fsh_Source = " #ifdef GL_ES"
			+ " precision highp float;" + " #endif"
			+ " uniform sampler2D tex0;" + " varying vec2 vTexCoord; "
			+ " varying vec4 vColor;" + " varying vec3 pos;" + " void main()"
			+ " {"
			+ "    gl_FragColor =  vec4(sin(pos.x), sin(pos.y), 0.0, 1.0);"
			+ " }";

	private final String vsh_Source = "uniform mat4 uMvp;"

	+ "attribute vec3 aPosition;" + "attribute vec2 aTexCoord;"
			+ "attribute vec4 aColor;"

			+ "varying vec4 vColor;" + "varying vec2 vTexCoord;"
			+ "varying vec3 pos;" + "void main() {"
			// on calcule la position du point via la matrice de projection
			+ " pos = aPosition;"
			+ " vec4 position = uMvp * vec4(aPosition.xyz, 1.);"
			// vec4 position = vec4(aPosition.xyz, 1.);
			+ " vColor = aColor;" + " vTexCoord = aTexCoord;"
			// gl_PointSize = 10.;

			// cette commande doit toujours être la dernière du vertex shader.
			+ "	gl_Position =  position;"

			+ "}";

	
	
	public Shader_simple() {
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

		this.mName = "default";
		this.attribListNames.add(this.DEFAULT_VSH_ATTRIB_VERTEX_COORD);
		this.attribListNames.add(this.DEFAULT_VSH_ATTRIB_COLOR);
		this.attribListNames.add(this.DEFAULT_VSH_ATTRIB_TEXTURE_COORD);

		this.uniformListNames.add(this.DEFAULT_VSH_UNIFORM_MVP);
		this.uniformListNames.add(this.DEFAULT_FSH_UNIFORM_TEXTURE);

		this.loadShaders(vsh_Source, fsh_Source);
		this.initAttribLocation();
		this.initUniformLocation();

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
		Log.i(this.getClass().getName(), this.mName + " : " + shaderType
				+ " shader compiled");
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

	public void toto(int i) {

		GLES20.glEnableVertexAttribArray(i);
	}

	public void enableShaderVar() {
		// si l'adresse mémoire de l'objet désigné par mAdressOf_VertexPosition
		// shader.enableShaderVar(); // n'est pas vide

		// GLES20.glEnableVertexAttribArray(mAdressOf_VertexPosition);

		for (String name : this.attribListNames) {
			String temps = name;

			int memoryAdress = this.getAdressOfAttrib(temps);
			if (memoryAdress != -1) {
				GLES20.glEnableVertexAttribArray(this.getAdressOfAttrib(temps));
				Log.i("shader.enableShaderVar()", "Enable attrib " + name
						+ " @ " + this.attribCatlg.get(temps));
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
				Log.i("shader.enableShaderVar()", "Enable Uniform " + name
						+ " @ " + this.uniformCatlg.get(temps));
				if (GLES20.glGetError() != GLES20.GL_NO_ERROR) {
					Log.i("mdebug", "Fail to enable Uniform " + name + "@"
							+ String.valueOf(memoryAdress) + " RC:" + " "
							+ String.valueOf(GLES20.glGetError()));

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
