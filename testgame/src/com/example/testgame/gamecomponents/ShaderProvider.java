package com.example.testgame.gamecomponents;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.testgame.GLES20Renderer;
import com.example.testgame.R;
import com.example.testgame.R.string;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.Matrix;
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
	public Shader mCurrentActiveShader ;
	
	//déclaration des attributs du shader : default
	public final String DEFAULT_VSH_ATTRIB_VERTEX_COORD = "aPosition";
	public final String DEFAULT_VSH_ATTRIB_COLOR = "aColor";
	public final String DEFAULT_VSH_ATTRIB_TEXTURE_COORD= "aTexCoord";
	
	public final String DEFAULT_VSH_UNIFORM_MVP= "uMvp";
	public final String DEFAULT_FSH_UNIFORM_TEXTURE= "tex0";
	/***
	 * 
	 * @param activity
	 */
	public ShaderProvider(Activity activity) {
		this.mActivity = activity;
		
		catalogShader = new HashMap<String, Integer>() ;
		shaderList = new ArrayList<Shader>() ;
		
		this.add(makeDefaultShader());
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
			Log.e(this.getClass().getName(), "Shader unknow on Catalog");
		} else {
			result = shaderList.get(catalogShader
					.get(shaderName) - 1);
		}
		return result;
	}

	private Shader makeDefaultShader() {
		Shader defaultShader = new Shader();
		// HashMap<String, String> map;
		// map = new HashMap<String, String>();
		defaultShader.mName = "default";
		defaultShader.attribListNames.add(this.DEFAULT_VSH_ATTRIB_VERTEX_COORD);
		defaultShader.attribListNames.add(this.DEFAULT_VSH_ATTRIB_COLOR);
		//defaultShader.attribListNames.add(this.DEFAULT_VSH_ATTRIB_TEXTURE_COORD);

		defaultShader.uniformListNames.add(this.DEFAULT_VSH_UNIFORM_MVP);
		defaultShader.uniformListNames.add(this.DEFAULT_FSH_UNIFORM_TEXTURE);

		InputStream iStream = null;
		Log.i("debug",
				this.mActivity.getString(R.string.defaultshader_vertex_source));
		try {

			iStream = this.mActivity.getAssets().open(
					mActivity.getString(R.string.defaultshader_vertex_source));

			String vertexCode;
			vertexCode = Util.readStringInput(iStream);
			iStream = null;
			iStream = this.mActivity.getAssets()
					.open(mActivity
							.getString(R.string.defaultshader_fragment_source));

			String fragmentCode;
			fragmentCode = Util.readStringInput(iStream);

			defaultShader.loadShaders(vertexCode, fragmentCode);

		} catch (IOException e) {
			Log.e("Testgame", "Shader simple_Shader cannot be read");

		}

		defaultShader.link();
		defaultShader.initAttribLocation();
		defaultShader.initUniformLocation();

		return defaultShader;
	}

	public void use(Shader shader) {
		
		// use program
		if (this.mCurrentActiveShader != shader){
			GLES20.glUseProgram(shader.mAdressOf_GLSLProgram);	
		this.mCurrentActiveShader = shader;
		Log.i("use", shader.mName + "@"
				+ String.valueOf(shader.mAdressOf_GLSLProgram)
				+" errcode : "
				+ String.valueOf(GLES20.glGetError()));
		}
		
  /**
		if (shader.mName == "default") {
			if (shader.getAdressOfUniform(this.DEFAULT_FSH_UNIFORM_TEXTURE) != -1) {
				
				GLES20.glEnable(GLES20.GL_TEXTURE_2D);
				GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, GLES20Renderer.mTex0);
				GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

				// on alimente la donnée UNIFORM mAdressOf_Texture0 avc un
				// integer 0
				GLES20.glUniform1i(shader.getAdressOfUniform(this.DEFAULT_FSH_UNIFORM_TEXTURE), 0);
			}

		}

	*/
	}

}
