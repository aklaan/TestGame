	package com.example.testgame.gamecomponents;

	import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

	import com.example.testgame.R;
import com.example.testgame.R.string;

	import android.app.Activity;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;
 

/**
 * leshader provader va compiler les shader et stocker leur adresse mémoire
 * @author NC10
 *
 */
public class ShaderProvider {


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




		// attibutes
		private int mAdressOf_VertexPosition;
		private int mAdressOf_VertexColor;
		private int mAdressOf_TextCoord;

		// uniform
		public int mAdressOf_Mvp;

		// sampler
		public int mAdressOf_Texture0;

		
		/***
		 * 
		 * @param activity
		 */
		public ShaderProvider(Activity activity) {
			mActivity = activity;
			mAdressOf_GLSLProgram = 0;
			mAdressOf_VertexShader = 0;
			mAdressOf_FragmentShader = 0;

			// create Program
			mAdressOf_GLSLProgram = GLES20.glCreateProgram();

		}
/***
 * 
 */
		
		private void  makeDefaultShader(){
			Shader defaultShader = new Shader();
			HashMap<String, String> map;
			map = new HashMap<String, String>();

		defaultShader.attribListNames.add("aPosition");
		defaultShader.attribListNames.add("aColor");
		defaultShader.attribListNames.add("aTexCoord");
		
		defaultShader.uniformListNames.add("uMvp");
		defaultShader.uniformListNames.add("tex0");
		
		String vShaderFilename = "simple_shader.vsh";
		String fShaderFilename = "simple_shader.fsh";

		InputStream iStream = null;

		try {
			iStream = mActivity.getAssets()
					.open(mActivity.getString(R.string.shaderfolder) + "/default/"
							+ vShaderFilename);

		} catch (IOException e) {
			Log.e("Testgame", "Shader simple_Shader cannot be read");

		};

		String vertexCode = Util.readStringInput(iStream);

		 iStream = null;

		try {
			iStream = mActivity.getAssets()
					.open(mActivity.getString(R.string.shaderfolder) + "/default/"
							+ fShaderFilename);

		} catch (IOException e) {
			Log.e("Testgame", "Shader simple_Shader cannot be read");

		};

		String fragmentCode = Util.readStringInput(iStream);
		
		
		
		
		defaultShader.loadShaders(vertexCode, fragmentCode);
		
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
/***
 * 
 * @return
 */
		public boolean make() {
			String vShaderFilename = "simple_shader.vsh";
			String fShaderFilename = "simple_shader.fsh";

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
		
			}

}
