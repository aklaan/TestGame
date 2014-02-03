package com.example.testgame.gamecomponents;

import java.io.IOException;
import java.io.InputStream;

import com.example.testgame.R;
import com.example.testgame.R.string;

import android.app.Activity;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

public class DefaultProgramShader {

	// ! activity
	private Activity mActivity;
	// ! GLES20 Program
	public int mAdressOf_GLSLProgram;
	// ! Vertex shader
	public int mAdressOf_VertexShader;
	// Fragment shader
	public int mAdressOf_FragmentShader;

	// ! ProgramName (Vertex filename is ProgramName.vsh and Fragment filename
	// is ProgramName.fsh)
	private String mProgramName;

	// ! Matrix Model View Projection
	private float[] mMvp = new float[16];

	// ! Matrix Model View Projection utiliée pour dessiner
	private float[] mMvp4Draw = new float[16];

	// ! matrice de Rotation
	private float[] mRotation = new float[16];

	// ! matrice de Projection
	public float[] mProjection = new float[16];

	// attibutes
	public int mAdressOf_VertexPosition;
	public int mAdressOf_VertexColor;
	public int mAdressOf_TextCoord;

	// uniform
	public int mAdressOf_Mvp;

	// sampler
	public int mAdressOf_Texture0;

	public DefaultProgramShader(Activity activity) {
		mActivity = activity;
		mAdressOf_GLSLProgram = 0;
		mAdressOf_VertexShader = 0;
		mAdressOf_FragmentShader = 0;

		// create Program
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
		String vShaderFilename = "shader2.vsh";
		String fShaderFilename = "shader2.fsh";

		// load and compile Shaders
		if (loadShaders(vShaderFilename, fShaderFilename) == false) {
			Log.e(this.getClass().getName(), "Cannot load shaders");
			return false;
		}

		// link
		link();

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

	static float counter = 0;

	/**
	 * 
	 * @param gameobject
	 * @param drawingMode
	 */
	public void enableVertexAttribArray(GameObject mGameObject) {
		// si l'adresse mémoire de l'objet désigné par mAdressOf_VertexPosition
		// n'est pas vide
		if (mAdressOf_VertexPosition != -1) {

			// on va chercher le FloatBuffer où sont stocké les coordonnées des
			// sommets
			// on se positionne au début du Buffer
			mGameObject.getVertices().position(0);

			// on souhaite passer à opengl un tableaux de coordonnées pour
			// alimenter la aPosition des vertex.
			// dans l'objet désigné par l'adresse mAdressOf_VertexPosition (càd
			// aPosition), on va écrire le contenu du FloatBuffer contenant les
			// coordonées des sommets
			// on spécifie comment OPENGL doit interpréter le buffer en
			// spécifiant que chaque index du tableau comporte 3 Float d'une
			// longeur P3FT2FR4FVertex_SIZE_BYTES
			// autrement dit, a la lecture du Buffer, au bout de 3 Float d'une
			// longeur P3FT2FR4FVertex_SIZE_BYTES, opengl cree un nouvel index.
			GLES20.glVertexAttribPointer(mAdressOf_VertexPosition, 3,
					GLES20.GL_FLOAT, false, Vertex.Vertex_COORD_SIZE_BYTES,
					mGameObject.getVertices());

			// on rend l'utilisation de mAdressOf_VertexPosition (càd aPosition)
			// possible par le moteur de rendu
			// dans le cas contraire, OPENGL n'utilisera pas les données passée
			// à aPosition et le fragment
			// se comporte comme si aPosition vaut 0.

			GLES20.glEnableVertexAttribArray(mAdressOf_VertexPosition);

			mGameObject.getVertices().position(0);
			GLES20.glVertexAttribPointer(mAdressOf_TextCoord, 2,
					GLES20.GL_FLOAT, false, Vertex.Vertex_TEXT_SIZE_BYTES,
					mGameObject.getTextCoord());
			GLES20.glEnableVertexAttribArray(mAdressOf_TextCoord);

		}

	}

	public void disableVertexAttribArray() {
		if (mAdressOf_VertexPosition != -1) {
			// on a plus besoin des variables, on les retire du moteur de rendu
			GLES20.glDisableVertexAttribArray(mAdressOf_VertexPosition);
		}
		if (mAdressOf_VertexColor != -1) {
			GLES20.glDisableVertexAttribArray(mAdressOf_VertexColor);
		}
		if (mAdressOf_TextCoord != -1) {
			GLES20.glDisableVertexAttribArray(mAdressOf_TextCoord);
		}
	}

	/**
	 * 
	 * @return
	 */
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

	/**
	 * 
	 * @param vertexFilename
	 * @param fragmentFilename
	 * @return
	 */
	private boolean loadShaders(String vertexFilename, String fragmentFilename) {
		if (mAdressOf_GLSLProgram == 0) {
			Log.e(this.getClass().getName(), "No GLSL Program created!");
			return false;
		}

		// load vertex and fragment shader
		try {
			mAdressOf_VertexShader = loadShader(vertexFilename,
					GLES20.GL_VERTEX_SHADER);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			mAdressOf_FragmentShader = loadShader(fragmentFilename,
					GLES20.GL_FRAGMENT_SHADER);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// if one of shader cannot be read return false
		if (mAdressOf_VertexShader == 0 || mAdressOf_FragmentShader == 0) {
			Log.e(this.getClass().getName(), "Shader doesn' compile");
			return false;
		}

		GLES20.glAttachShader(mAdressOf_GLSLProgram, mAdressOf_VertexShader);
		GLES20.glAttachShader(mAdressOf_GLSLProgram, mAdressOf_FragmentShader);
		return true;
	}

	/**
	 * 
	 * /* load a Vertex or Fragment shader
	 * @throws IOException 
	 */
	private int loadShader(String filename, int shaderType) throws IOException {
		InputStream iStream = null;

		try {
			iStream = mActivity.getAssets()
					.open("shaders/shader2/"+ filename);

		} catch (IOException e) {
			Log.e("Testgame", "Shader simple_Shader cannot be read");

		};

		String source = Util.readStringInput(iStream);

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
		Log.i(this.getClass().getName(), "shader compiled:" + filename);
		return shader;
	}

}
