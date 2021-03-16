package com.example.graphicsopenglesdemo;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyGLRenderer implements GLSurfaceView.Renderer {
	private Triangle triangle;
	private Square square;
	private Cube cube;


//	@Override
//	public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
//		triangle = new Triangle();
//		square = new Square();
//		// Set the background frame color
//		GLES20.glClearColor(0.0f,0.0f,0.0f,1.0f);
//	}
//	// mMVPMatrix is an abbreviation for "Model View Projection Matrix"
//	private final float[] mvpMatrix = new float[16]; // modelview projection matrix
//	private final float[] projectionMatrix = new float[16];
//	private final float[] viewMatrix = new float[16];
//
//	@Override
//	public void onSurfaceChanged(GL10 gl10, int width, int height) {
//		GLES20.glViewport(0,0,width,height);
//		float ratio = (float)width/height;
//		Matrix.frustumM(projectionMatrix,0,-ratio, ratio, -1,1,3,7);
//	}
//	private float[] rotationMatrix = new float[16];
//	@Override
//	public void onDrawFrame(GL10 gl10) {
//		float[] scratch = new float[16];
//		//redraw background color
//		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
//		Matrix.setLookAtM(viewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
//		// Create a rotation transformation for the triangle
////		long time = SystemClock.uptimeMillis() % 4000L; // former demo
////    	float angle = 0.090f * ((int) time);						// former demo
////		Matrix.setRotateM(mRotationMatrix, 0, angle, 0, 0, -1.0f); // former demo
//		Matrix.setRotateM(rotationMatrix,0,angle,0,0,-1.0f);
//		// Combine the rotation matrix with the projection and camera view
//		// Note that the mMVPMatrix factor *must be first* in order
//		// for the matrix multiplication product to be correct.
//		Matrix.multiplyMM(scratch, 0, mvpMatrix, 0, rotationMatrix, 0);
//		// Draw triangle
//		square.draw();
//		//triangle.draw();
//	}
//	public static int loadShader(int type, String shaderCode){
//		// create a vertex shader type (GLES20.GL_VERTEX_SHADER)
//		// or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
//		int shader = GLES20.glCreateShader(type);
//		// add the source code to the shader and compile it
//		GLES20.glShaderSource(shader, shaderCode);
//		GLES20.glCompileShader(shader);
//		return shader;
//	}
//	public volatile float angle;
//	public float getAngle() {
//		return angle;
//	}
//	public void setAngle(float angle) {
//		this.angle = angle;
//	}
private static final String TAG = "MyGLRenderer";
	private Triangle mTriangle;
	private Square   mSquare;

	// mMVPMatrix is an abbreviation for "Model View Projection Matrix"
	private final float[] mMVPMatrix = new float[16];
	private final float[] mProjectionMatrix = new float[16];
	private final float[] mViewMatrix = new float[16];
	private final float[] mRotationMatrix = new float[16];

	private float mAngle;
	private Cube mCube;

	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config) {

		// Set the background frame color
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		mTriangle = new Triangle();
		mSquare   = new Square();
		mCube = new Cube();
	}

	@Override
	public void onDrawFrame(GL10 unused) {
		float[] scratch = new float[16];

		// Draw background color
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

		// Set the camera position (View matrix)
		Matrix.setLookAtM(mViewMatrix, 0, 2, 2, -6, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

		// Calculate the projection and view transformation
		Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

		// Draw square
		//mSquare.draw(mMVPMatrix);
		mCube.draw(mMVPMatrix);
		// Create a rotation for the triangle

		// Use the following code to generate constant rotation.
		// Leave this code out when using TouchEvents.
		// long time = SystemClock.uptimeMillis() % 4000L;
		// float angle = 0.090f * ((int) time);

		Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0, 0, 1.0f);

		// Combine the rotation matrix with the projection and camera view
		// Note that the mMVPMatrix factor *must be first* in order
		// for the matrix multiplication product to be correct.
		Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);

		// Draw triangle
		//mTriangle.draw(scratch);
	}

	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height) {
		// Adjust the viewport based on geometry changes,
		// such as screen rotation
		GLES20.glViewport(0, 0, width, height);

		float ratio = (float) width / height;

		// this projection matrix is applied to object coordinates
		// in the onDrawFrame() method
		Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);

	}

	/**
	 * Utility method for compiling a OpenGL shader.
	 *
	 * <p><strong>Note:</strong> When developing shaders, use the checkGlError()
	 * method to debug shader coding errors.</p>
	 *
	 * @param type - Vertex or fragment shader type.
	 * @param shaderCode - String containing the shader code.
	 * @return - Returns an id for the shader.
	 */
	public static int loadShader(int type, String shaderCode){

		// create a vertex shader type (GLES20.GL_VERTEX_SHADER)
		// or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
		int shader = GLES20.glCreateShader(type);

		// add the source code to the shader and compile it
		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);

		return shader;
	}

	/**
	 * Utility method for debugging OpenGL calls. Provide the name of the call
	 * just after making it:
	 *
	 * <pre>
	 * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
	 * MyGLRenderer.checkGlError("glGetUniformLocation");</pre>
	 *
	 * If the operation is not successful, the check throws an error.
	 *
	 * @param glOperation - Name of the OpenGL call to check.
	 */
	public static void checkGlError(String glOperation) {
		int error;
		while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
			Log.e(TAG, glOperation + ": glError " + error);
			throw new RuntimeException(glOperation + ": glError " + error);
		}
	}

	/**
	 * Returns the rotation angle of the triangle shape (mTriangle).
	 *
	 * @return - A float representing the rotation angle.
	 */
	public float getAngle() {
		return mAngle;
	}

	/**
	 * Sets the rotation angle of the triangle shape (mTriangle).
	 */
	public void setAngle(float angle) {
		mAngle = angle;
	}
}

