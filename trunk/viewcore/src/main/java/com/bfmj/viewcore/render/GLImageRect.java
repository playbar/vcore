package com.bfmj.viewcore.render;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import com.bfmj.viewcore.util.GLMatrixState;
import com.bfmj.viewcore.util.GLShaderManager;
import com.bfmj.viewcore.view.GLRectView;
import com.bfmj.viewcore.view.GLView;

import android.opengl.GLES20;
import android.opengl.Matrix;

/**
 * 
 * ClassName: GLImageRect <br/>
 * @author lixianke    
 * @date: 2015-3-17 下午2:49:51 <br/>  
 * description:
 */
public class GLImageRect extends GLRect {
	private static GLImageRect instance;
	
	private FloatBuffer vertexBuffer, textureVerticesBuffer;
    private int mProgram;
    private int muMVPMatrixHandle;
    private int muAlphaHandle;
    private int muMaskHandle;
    private int mPositionHandle;
    private int mTextureCoordHandle;
    
    private int verLen=0;
    private int textureLen = 0;
	
	private int vboVertexNew = 0;  //bufferIndex++;
	private int vboTextureNew = 0; // bufferIndex++;
	private int []mvbo = new int[2];

    private float texCoor[] = {
    	0.0f, 0.0f,
    	0.0f, 1.0f,
        1.0f, 1.0f,
    	0.0f, 0.0f,
        1.0f, 1.0f,
        1.0f, 0.0f
    };
    
    private int mTextureId = -1;
    
    public static GLImageRect getInstance(){
    	if (instance == null){
    		instance = new GLImageRect();
    	}
    	return instance;
    }
    
    public static void initInstance(){
    	if (instance != null){
    		instance.release();
    		instance = null;
    	}
    	instance = new GLImageRect();
    }
    
    private GLImageRect(){
    	init();
    }
    
    private void init(){
		GLES20.glGenBuffers( 2, mvbo, 0 );
		vboVertexNew = mvbo[0];
		vboTextureNew = mvbo[1];

    	initVertex();
    	initTextureBuffer();
    	
    	createProgram();
    }
    
    public void setTextureId(int textureId){
    	mTextureId = textureId;
    }

	public void drawViews(ArrayList<GLView> views){
		beginDraw();
		for (int j = 0; j < views.size(); j++) {
			if (views.get(j) instanceof  GLRectView){
				GLRectView view = (GLRectView) views.get(j);
				if (view != null) {
					draw(view);
				}
			}
		}
		endDraw();
	}

	private void draw(GLRectView view){
		float d = -view.getDepth();
		for (int i = 0; i < view.getRenders().size(); i++) {
			GLRenderParams render = null;
			try {
				render = view.getRenders().get(i);
			} catch (Exception e) {}

			if (render == null) {
				continue;
			}

			GLMatrixState state = view.getMatrixState();
			state.pushMatrix();
			float[] curMatrix = state.getCurrentMatrix();
			Matrix.translateM(curMatrix, 0, 0, 0, d + view.getZPosition() * 0.0008f);
			if (view.getAngelX() != 0){
				Matrix.rotateM(curMatrix, 0, view.getAngelX(), 1, 0, 0);
			}
			if (view.getAngelY() != 0){
				Matrix.rotateM(curMatrix, 0, view.getAngelY(), 0, 1, 0);
			}
			if (view.getAngelZ() != 0){
				Matrix.rotateM(curMatrix, 0, view.getAngelZ(), 0, 0, 1);
			}

			if (render.getScaleX() != 1.0f || render.getScaleY() != 1.0f){
				Matrix.scaleM(curMatrix, 0, render.getScaleX(), render.getScaleY(), 1);
			}

			setTextureId(render.getTextureId());
			setAlpha(render.getAlpha());
			setMask(render.getMask());
			draw(state.getFinalMatrix());

			d += 0.0004f;
			state.popMatrix();
		}
	}

	public void beginDraw(){
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glUseProgram(mProgram);
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);


		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboVertexNew);
		GLES20.glEnableVertexAttribArray(0);
		GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 0, 0);


		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboTextureNew);
		GLES20.glEnableVertexAttribArray(1);
		GLES20.glVertexAttribPointer(mTextureCoordHandle, 2, GLES20.GL_FLOAT, false, 0, 0);


	}

    public void draw(float[] mtx) {

    	if (mTextureId < 0){
    		return;
    	}

		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId);
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, mtx, 0);
        GLES20.glUniform1f(muAlphaHandle, getAlpha());
        GLES20.glUniform1f(muMaskHandle, getMask());

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, getVertices().length / 3);

	}

	public void endDraw(){
		GLES20.glDisableVertexAttribArray(0);
		GLES20.glDisableVertexAttribArray(1);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
		GLES20.glDisable(GLES20.GL_BLEND);
		GLES20.glBindBuffer( GLES20.GL_ARRAY_BUFFER, 0 );
	}
    
    private void createProgram(){
		int vertexShader    = GLShaderManager.loadShader(GLES20.GL_VERTEX_SHADER, GLShaderManager.VERTEX_IMAGE);
        int fragmentShader  = GLShaderManager.loadShader(GLES20.GL_FRAGMENT_SHADER, GLShaderManager.FRAGMENT_IMAGE);

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL ES Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // creates OpenGL ES program executables
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        muAlphaHandle = GLES20.glGetUniformLocation(mProgram, "uAlpha");
        muMaskHandle = GLES20.glGetUniformLocation(mProgram, "uMask");
        mTextureCoordHandle = GLES20.glGetAttribLocation(mProgram, "inputTextureCoordinate");
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
	}
    
//    private void vertexVBO() {
//
//		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboVertexNew);
//		// 传送顶点位置数据
//		GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT,
//				false, 0, 0);
//		GLES20.glEnableVertexAttribArray(0);
//	}
//
//    private void textureVBO() {
//		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboTextureNew);
//		// 传送顶点位置数据
//		GLES20.glVertexAttribPointer(mTextureCoordHandle, 2, GLES20.GL_FLOAT,
//				false, 0, 0);
//		GLES20.glEnableVertexAttribArray(1);
//
//	}
    
    protected void initVertex(){   	
		verLen = getVertices().length*4;
        ByteBuffer bb = ByteBuffer.allocateDirect(verLen);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(getVertices());
        vertexBuffer.position(0);
       
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboVertexNew);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, verLen, vertexBuffer,
				GLES20.GL_STATIC_DRAW);
//		GLES20.glBindBuffer( GLES20.GL_ARRAY_BUFFER, 0 );
	}
	
	private void initTextureBuffer(){
        textureLen = texCoor.length*4;
		ByteBuffer llbb = ByteBuffer.allocateDirect(textureLen);
		llbb.order(ByteOrder.nativeOrder());
		textureVerticesBuffer=llbb.asFloatBuffer();
		textureVerticesBuffer.put(texCoor);
		textureVerticesBuffer.position(0);
		
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboTextureNew);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, textureLen, textureVerticesBuffer,
				GLES20.GL_STATIC_DRAW);
	}
	
	public void release(){
		GLES20.glDeleteBuffers(2, new int[]{vboVertexNew, vboTextureNew}, 0);
	}
}
