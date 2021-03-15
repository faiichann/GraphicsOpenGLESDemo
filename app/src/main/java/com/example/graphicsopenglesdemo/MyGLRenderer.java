package com.example.graphicsopenglesdemo;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyGLRenderer implements GLSurfaceView.Renderer {
	private Triangle triangle;
	private Square square;


	@Override
	public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
		triangle = new Triangle();
		square = new Square();
		// Set the background frame color
		GLES20.glClearColor(0.0f,0.0f,0.0f,1.0f);
	}
	// mMVPMatrix is an abbreviation for "Model View Projection Matrix"
	private final float[] mvpMatrix = new float[16]; // modelview projection matrix
	private final float[] projectionMatrix = new float[16];
	private final float[] viewMatrix = new float[16];

	@Override
	public void onSurfaceChanged(GL10 gl10, int width, int height) {
		GLES20.glViewport(0,0,width,height);
		float ratio = (float)width/height;
		Matrix.frustumM(projectionMatrix,0,-ratio, ratio, -1,1,3,7);
	}
	private float[] mRotationMatrix = new float[16];
	private float[] rotationMatrix = new float[16];
	@Override
	public void onDrawFrame(GL10 gl10) {
		float[] scratch = new float[16];
		//redraw background color
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		Matrix.setLookAtM(viewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
		// Create a rotation transformation for the triangle
//		long time = SystemClock.uptimeMillis() % 4000L; // former demo
//		float angle = 0.090f * ((int) time);						// former demo
//		Matrix.setRotateM(mRotationMatrix, 0, angle, 0, 0, -1.0f); // former demo
		Matrix.setRotateM(rotationMatrix,0,angle,0,0,-1.0f);
		// Combine the rotation matrix with the projection and camera view
		// Note that the mMVPMatrix factor *must be first* in order
		// for the matrix multiplication product to be correct.
		Matrix.multiplyMM(scratch, 0, mvpMatrix, 0, rotationMatrix, 0);
		// Draw triangle
		triangle.draw();
	}
	public static int loadShader(int type, String shaderCode){
		// create a vertex shader type (GLES20.GL_VERTEX_SHADER)
		// or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
		int shader = GLES20.glCreateShader(type);
		// add the source code to the shader and compile it
		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);
		return shader;
	}
	public volatile float angle;
	public float getAngle() {
		return angle;
	}
	public void setAngle(float angle) {
		this.angle = angle;
	}
}

