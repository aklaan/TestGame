package com.example.testgame.gamecomponents;

import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.testgame.R;

import android.app.Activity;
import android.opengl.GLES20;
import android.util.Log;

public class Shader_simple extends Shader {
	
	// déclaration des attributs spécifiques au shader
	public final String VSH_ATTRIB_VERTEX_COORD = "aPosition";
	public int attrib_vertex_coord_location;

	public final String VSH_ATTRIB_COLOR = "aColor";
	public int attrib_color_location;

	public final String VSH_ATTRIB_TEXTURE_COORD = "aTexCoord";
	public int attrib_texture_coord_location;

	public final String VSH_UNIFORM_MVP = "uMvp";
	public int uniform_mvp_location;

	public final String FSH_UNIFORM_TEXTURE = "tex0";
	public int uniform_texture_location;

	private final String fsh_Source = 
			  "#ifdef GL_ES \n"
			+ " precision highp float; \n" 
			+ " #endif \n"
			+ " uniform sampler2D " + this.FSH_UNIFORM_TEXTURE+";" 
			+ " varying vec2 vTexCoord; "
			+ " varying vec4 vColor;" 
			+ " varying vec3 pos;" 
			+ " void main() {"
//		    + "    gl_FragColor =  vColor;"
			+ "gl_FragColor = texture2D(tex0, vTexCoord);"
			// + "    gl_FragColor =  vec4(sin(pos.x), sin(pos.y), 0.0, 1.0);"
			+ " " + "}";

	private final String vsh_Source =
			  "uniform mat4 "	+ this.VSH_UNIFORM_MVP + ";" 
			+ "attribute vec3 " + this.VSH_ATTRIB_VERTEX_COORD + ";"
			+ "attribute vec2 " + this.VSH_ATTRIB_TEXTURE_COORD + ";"
			+ "attribute vec4 " + this.VSH_ATTRIB_COLOR + ";"
			+ "varying vec4 vColor;" 
			+ "varying vec2 vTexCoord;"
			+ "varying vec3 pos;" 
			+ "void main() {"
			// on calcule la position du point via la matrice de projection
			+ " pos = aPosition;"
			+ " vec4 position = uMvp * vec4(aPosition.xyz, 1.);"
			// vec4 position = vec4(aPosition.xyz, 1.);
			+ " vColor = aColor;" + " vTexCoord = aTexCoord;"
			// gl_PointSize = 10.;
			// cette commande doit toujours être la dernière du vertex shader.
			+ "	gl_Position =  position;" + "}";

	public Shader_simple() {

	}

	public void make() {
		Log.i("", fsh_Source);
		Log.i("", vsh_Source);
		mGLSLProgram_location = 0;
		VertexShader_location = 0;
		FragmentShader_location = 0;

		// On crée le GSL Program dans OpenGL et on mémorise
		// son adresse.
		mGLSLProgram_location = GLES20.glCreateProgram();

		this.mName = "simple";
		
		this.loadShaders(vsh_Source, fsh_Source);
		this.initLocations();
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

	public void initLocations() {
		// les attribs
		this.attrib_vertex_coord_location = GLES20.glGetAttribLocation(
				mGLSLProgram_location, this.VSH_ATTRIB_VERTEX_COORD);
		this.attrib_color_location = GLES20.glGetAttribLocation(
				mGLSLProgram_location, this.VSH_ATTRIB_COLOR);
		this.attrib_texture_coord_location = GLES20.glGetAttribLocation(
				this.mGLSLProgram_location, this.VSH_ATTRIB_TEXTURE_COORD);

		// les Uniforms

		this.uniform_mvp_location = GLES20.glGetUniformLocation(
				this.mGLSLProgram_location, this.VSH_UNIFORM_MVP);
		this.uniform_texture_location = GLES20.glGetUniformLocation(
				this.mGLSLProgram_location, this.FSH_UNIFORM_TEXTURE);
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
		if (mGLSLProgram_location == 0) {
			Log.e(this.getClass().getName(), "No GLSL Program created!");
			return false;
		}

		this.setFragmentShader(fragmentCode);
		this.setVertexShader(vertexCode);

		// if one of shader cannot be read return false
		if (VertexShader_location == 0 || FragmentShader_location == 0) {
			Log.e(this.getClass().getName(), "Shader doesn' compile");
			return false;
		}

		GLES20.glAttachShader(mGLSLProgram_location, VertexShader_location);
		GLES20.glAttachShader(mGLSLProgram_location, FragmentShader_location);
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

	// *******************************************************************
	public boolean link() {
		if (mGLSLProgram_location == 0) {
			Log.e(this.getClass().getName(),
					"Please create a GL program before Link shaders!");
			return false;
		}

		GLES20.glLinkProgram(mGLSLProgram_location);

		int[] linkStatus = new int[1];

		GLES20.glGetProgramiv(mGLSLProgram_location, GLES20.GL_LINK_STATUS,
				linkStatus, 0);

		if (linkStatus[0] != GLES20.GL_TRUE) {
			Log.e(this.getClass().getName(), "Could not link program: ");
			Log.e(this.getClass().getName(),
					"logs:" + GLES20.glGetProgramInfoLog(mGLSLProgram_location));
			GLES20.glDeleteProgram(mGLSLProgram_location);
			mGLSLProgram_location = 0;
			return false;
		}

		Log.i("Shader.link()", "Shader linkded");
		return true;
	}

	// *******************************************************************
	// Attention : il ne faut pas rendre enable un attribut non valorisé
	// sinon c'est ecran noir !
	
	public void enableShaderVar() {
		GLES20.glEnableVertexAttribArray(this.attrib_vertex_coord_location);
		//GLES20.glEnableVertexAttribArray(this.attrib_color_location);
		GLES20.glEnableVertexAttribArray(this.attrib_texture_coord_location);
		
		///les uniforms ne sont pas attrib !!
		//GLES20.glEnableVertexAttribArray(this.uniform_mvp_location);
		//GLES20.glEnableVertexAttribArray(this.uniform_texture_location);
	}

	// **************************************************************************
	public void disableShaderVar() {
		GLES20.glDisableVertexAttribArray(this.attrib_vertex_coord_location);
		GLES20.glDisableVertexAttribArray(this.attrib_color_location);
		GLES20.glDisableVertexAttribArray(this.attrib_texture_coord_location);
		GLES20.glDisableVertexAttribArray(this.uniform_mvp_location);
		GLES20.glDisableVertexAttribArray(this.uniform_texture_location);

	}



public void alimVertices(FloatBuffer fb){
	
	GLES20.glVertexAttribPointer(this.attrib_vertex_coord_location, 3,
			GLES20.GL_FLOAT, false, Vertex.Vertex_COORD_SIZE_BYTES,fb);
			

}
}
