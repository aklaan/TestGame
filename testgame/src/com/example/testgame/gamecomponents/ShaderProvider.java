package com.example.testgame.gamecomponents;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.testgame.R;
import com.example.testgame.R.string;

import android.app.Activity;
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
	public ArrayList<Shader> shaderList;
	public HashMap<String,String> catalogShader;
	

	/***
	 * 
	 * @param activity
	 */
	public ShaderProvider(Activity activity) {
		mActivity = activity;
		
		Shader sh = new Shader();
		sh = makeDefaultShader();
		this.add(sh);
	}

	/***
 * 
 */

	public void add(Shader shader) {
		
		int newindex = catalogShader.size() + 1;
		catalogShader.put(shader.name, String.valueOf(newindex));
		shaderList.add(shader);
	}
	
	
	public Shader getShaderByName(String shaderName) {
		Shader result = null;
		if (catalogShader.get(shaderName) == null) {
			Log.e(this.getClass().getName(), "Shader unknow on Catalog");
		} else {
			result = shaderList.get(Integer.parseInt(catalogShader.get(shaderName))-1);
		}
		return result;
	}

	
	
	
	
	
	
	
	
	
	
	
	private Shader makeDefaultShader() {
		Shader defaultShader = new Shader();
		// HashMap<String, String> map;
		// map = new HashMap<String, String>();
		defaultShader.name = "default";
		defaultShader.attribListNames.add(mActivity.getString(R.string.vertex_position));
		defaultShader.attribListNames.add("aColor");
		defaultShader.attribListNames.add(mActivity.getString(R.string.texture_position));

		defaultShader.uniformListNames.add(mActivity.getString(R.string.umvp_matrix));
		defaultShader.uniformListNames.add("tex0");

		InputStream iStream = null;
		Log.i("debug",mActivity.getString(R.string.defaultshader_vertex_source));
		try {
			
		
			iStream = mActivity.getAssets().open(
					mActivity.getString(R.string.defaultshader_vertex_source));

			String vertexCode;
			vertexCode = Util.readStringInput(iStream);
			iStream = null;
			iStream = mActivity.getAssets()
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
		defaultShader.initAttribLocation();
	
	return defaultShader;
	}



	
	
	/**
	 * 
	 * @param gameobject
	 * @param drawingMode
	 */
	

}
