package com.bfmj.viewcore.render;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import com.bfmj.viewcore.util.GLShaderManager;

import android.opengl.GLES20;

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
	
	private int vboVertexNew = bufferIndex++;
	private int vboTextureNew = bufferIndex++;

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
    	initVertex();
    	initTextureBuffer();
    	
    	createProgram();
    }
    
    public void setTextureId(int textureId){
    	mTextureId = textureId;
    }
    
    public void draw(float[] mtx) {
    	if (mTextureId < 0){
    		return;
    	}
    	
    	GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
    	GLES20.glEnable(GLES20.GL_BLEND);
    	
		GLES20.glUseProgram(mProgram);
		
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId);
        
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, mtx, 0);
        GLES20.glUniform1f(muAlphaHandle, getAlpha());
        GLES20.glUniform1f(muMaskHandle, getMask());

        vertexVBO();
        textureVBO();
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, getVertices().length / 3);

        GLES20.glDisableVertexAttribArray(0);
        GLES20.glDisableVertexAttribArray(1);
        
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glDisable(GLES20.GL_BLEND);
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
    
    private void vertexVBO() {
		
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboVertexNew);
		// 传送顶点位置数据
		GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT,
				false, 0, 0);
		GLES20.glEnableVertexAttribArray(0);
	}
    
    private void textureVBO() {
		
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboTextureNew);
		// 传送顶点位置数据
		GLES20.glVertexAttribPointer(mTextureCoordHandle, 2, GLES20.GL_FLOAT,
				false, 0, 0);
		GLES20.glEnableVertexAttribArray(1);
		
	}
    
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
