package com.bfmj.viewcore.render;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import com.bfmj.viewcore.util.GLMatrixState;
import com.bfmj.viewcore.util.GLShaderManager;
import com.bfmj.viewcore.view.GLRectView;
import com.bfmj.viewcore.view.GLView;

import android.opengl.GLES30;
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
		1.0f, 0.0f,
		1.0f, 1.0f
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
		GLES30.glGenBuffers( 2, mvbo, 0 );
		vboVertexNew = mvbo[0];
		vboTextureNew = mvbo[1];

    	initVertex();
    	initTextureBuffer();
    	
    	createProgram();
    }
    
    public void setTextureId(int textureId){
    	mTextureId = textureId;
    }

	public void drawViews(ArrayList<GLRectView> views){
		beginDraw();
		for (int j = 0; j < views.size(); j++) {
			GLRectView view = (GLRectView) views.get(j);
			if (view != null && view.isVisible() && view.isSurfaceCreated()) {
				draw(view);
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

			if (render != null && render.getType() == GLRenderParams.RENDER_TYPE_IMAGE && render.getTextureId() > 0){
				GLMatrixState state = view.getMatrixState();
				state.pushMatrix();
				float[] curMatrix = state.getCurrentMatrix();
				Matrix.translateM(curMatrix, 0, 0, 0, d + view.getZPosition() * 0.0001f);
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
	}

	public void beginDraw(){
		GLES30.glBlendFunc(GLES30.GL_SRC_ALPHA, GLES30.GL_ONE_MINUS_SRC_ALPHA);
		GLES30.glEnable(GLES30.GL_BLEND);
		GLES30.glUseProgram(mProgram);
		GLES30.glActiveTexture(GLES30.GL_TEXTURE0);


		GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vboVertexNew);
		GLES30.glEnableVertexAttribArray(0);
		GLES30.glVertexAttribPointer(mPositionHandle, 3, GLES30.GL_FLOAT, false, 0, 0);


		GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vboTextureNew);
		GLES30.glEnableVertexAttribArray(1);
		GLES30.glVertexAttribPointer(mTextureCoordHandle, 2, GLES30.GL_FLOAT, false, 0, 0);


	}

    public void draw(float[] mtx) {

    	if (mTextureId < 0){
    		return;
    	}

		GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, mTextureId);
        GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, mtx, 0);
        GLES30.glUniform1f(muAlphaHandle, getAlpha());
        GLES30.glUniform1f(muMaskHandle, getMask());

        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_STRIP, 0, 4);
//		GLES30.glDrawElements(GLES30.GL_TRIANGLE_STRIP, 4, GLES30.GL_UNSIGNED_SHORT, 0);

	}

	public void endDraw(){
		GLES30.glDisableVertexAttribArray(0);
		GLES30.glDisableVertexAttribArray(1);
		GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);
		GLES30.glDisable(GLES30.GL_BLEND);
		GLES30.glBindBuffer( GLES30.GL_ARRAY_BUFFER, 0 );
	}
    
    private void createProgram(){
		int vertexShader    = GLShaderManager.loadShader(GLES30.GL_VERTEX_SHADER, GLShaderManager.VERTEX_IMAGE);
        int fragmentShader  = GLShaderManager.loadShader(GLES30.GL_FRAGMENT_SHADER, GLShaderManager.FRAGMENT_IMAGE);

        mProgram = GLES30.glCreateProgram();             // create empty OpenGL ES Program
        GLES30.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES30.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES30.glLinkProgram(mProgram);                  // creates OpenGL ES program executables
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
        muAlphaHandle = GLES30.glGetUniformLocation(mProgram, "uAlpha");
        muMaskHandle = GLES30.glGetUniformLocation(mProgram, "uMask");
        mTextureCoordHandle = GLES30.glGetAttribLocation(mProgram, "inputTextureCoordinate");
        mPositionHandle = GLES30.glGetAttribLocation(mProgram, "vPosition");
	}
    
//    private void vertexVBO() {
//
//		GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vboVertexNew);
//		// 传送顶点位置数据
//		GLES30.glVertexAttribPointer(mPositionHandle, 3, GLES30.GL_FLOAT,
//				false, 0, 0);
//		GLES30.glEnableVertexAttribArray(0);
//	}
//
//    private void textureVBO() {
//		GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vboTextureNew);
//		// 传送顶点位置数据
//		GLES30.glVertexAttribPointer(mTextureCoordHandle, 2, GLES30.GL_FLOAT,
//				false, 0, 0);
//		GLES30.glEnableVertexAttribArray(1);
//
//	}
    
    protected void initVertex(){   	
		verLen = getVertices().length*4;
        ByteBuffer bb = ByteBuffer.allocateDirect(verLen);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(getVertices());
        vertexBuffer.position(0);
       
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vboVertexNew);
		GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, verLen, vertexBuffer,
				GLES30.GL_STATIC_DRAW);
//		GLES30.glBindBuffer( GLES30.GL_ARRAY_BUFFER, 0 );
	}
	
	private void initTextureBuffer(){
        textureLen = texCoor.length*4;
		ByteBuffer llbb = ByteBuffer.allocateDirect(textureLen);
		llbb.order(ByteOrder.nativeOrder());
		textureVerticesBuffer=llbb.asFloatBuffer();
		textureVerticesBuffer.put(texCoor);
		textureVerticesBuffer.position(0);
		
		GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vboTextureNew);
		GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, textureLen, textureVerticesBuffer,
				GLES30.GL_STATIC_DRAW);
	}
	
	public void release(){
		GLES30.glDeleteBuffers(2, new int[]{vboVertexNew, vboTextureNew}, 0);
	}
}
