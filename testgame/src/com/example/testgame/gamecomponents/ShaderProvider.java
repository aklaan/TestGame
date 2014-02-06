package com.example.testgame.gamecomponents;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.testgame.R;
import android.app.Activity;
import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

/**
 * leshader provader va compiler les shader et stocker leur adresse mémoire
 * 
 * @author NC10
 * 
 */
public class ShaderProvider {

	// ! activity
	public Activity mActivity;
	public Context glContext;
	public ArrayList<Shader> shaderList;
	public HashMap<String, Integer> catalogShader;
	public Shader mCurrentActiveShader;

	// déclaration des attributs du shader : default
	public final String DEFAULT_VSH_ATTRIB_VERTEX_COORD = "aPosition";
	public final String DEFAULT_VSH_ATTRIB_COLOR = "aColor";
	public final String DEFAULT_VSH_ATTRIB_TEXTURE_COORD = "aTexCoord";

	public final String DEFAULT_VSH_UNIFORM_MVP = "uMvp";
	public final String DEFAULT_FSH_UNIFORM_TEXTURE = "tex0";

	/***
	 * 
	 * @param activity
	 */
	public ShaderProvider(Activity activity) {
		this.mActivity = activity;

		catalogShader = new HashMap<String, Integer>();
		shaderList = new ArrayList<Shader>();

		Shader_simple shader_simple = new Shader_simple();
		shader_simple.make();
		// this.add(makeDefaultShader());
		this.add(shader_simple);
	}

	/***
 * 
 */

	public void add(Shader shader) {

		int newindex = catalogShader.size() + 1;
		catalogShader.put(shader.mName, newindex);
		shaderList.add(shader);
	}

	public Shader getShaderByName(String shaderName) {
		Shader result = null;
		if (catalogShader.get(shaderName) == null) {
			Log.e(this.getClass().getName(), "Shader " + shaderName +" unknow on Catalog");
		} else {
			result = shaderList.get(catalogShader.get(shaderName) - 1);
		}
		return result;
	}

	
	public void use(Shader shader) {

		// use program
		if (this.mCurrentActiveShader != shader) {

			GLES20.glUseProgram(shader.mGLSLProgram_location);
			this.mCurrentActiveShader = shader;
			Log.i("use",
					shader.mName + "@"
							+ String.valueOf(shader.mGLSLProgram_location)
							+ " errcode : "
							+ String.valueOf(GLES20.glGetError()));
		}

		/**
		 * if (shader.mName == "default") { if
		 * (shader.getAdressOfUniform(this.DEFAULT_FSH_UNIFORM_TEXTURE) != -1) {
		 * 
		 * GLES20.glEnable(GLES20.GL_TEXTURE_2D);
		 * GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, GLES20Renderer.mTex0);
		 * GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		 * 
		 * // on alimente la donnée UNIFORM mAdressOf_Texture0 avc un // integer
		 * 0 GLES20.glUniform1i(shader.getAdressOfUniform(this.
		 * DEFAULT_FSH_UNIFORM_TEXTURE), 0); }
		 * 
		 * }
		 */
	}

}
