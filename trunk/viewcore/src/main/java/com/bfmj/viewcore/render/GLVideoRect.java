package com.bfmj.viewcore.render;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.bfmj.viewcore.util.GLShaderManager;

import android.annotation.SuppressLint;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;

/**
 * 
 * ClassName: GLVideoRect <br/>
 * @author lixianke    
 * @date: 2015-3-17 下午2:47:40 <br/>  
 * description:
 */
public class GLVideoRect extends GLRect {
	public enum TextureType {
		TEXTURE_TYPE_ALL, TEXTURE_TYPE_LEFT, TEXTURE_TYPE_RIGHT, TEXTURE_TYPE_TOP, TEXTURE_TYPE_BOTTOM
	}
	private static GLVideoRect instance;
	
    private int mProgram;
    private int muMVPMatrixHandle;
    private int mPositionHandle;
    private int mTextureCoordHandle;
	
	private int vboVertexNew = 0; 	//bufferIndex++;
	private int vboTextureNew = 0;	//bufferIndex++;
	private int vboTextureLeftNew = 0;	//bufferIndex++;
	private int vboTextureRightNew = 0;	//bufferIndex++;
	private int vboTextureTopNew = 0;
	private int vboTextureBottomNew = 0;
	private int []mvbo = new int[6];

	private TextureType mTextureType = TextureType.TEXTURE_TYPE_ALL;
	
	private float vertices[] = {
       -0.5f,  0.5f,
       -0.5f, -0.5f,
        0.5f, -0.5f,
       -0.5f,  0.5f,
        0.5f, -0.5f,
       	0.5f,  0.5f
    };
	
	public static float[] texture_all = {
    	0.0f, 0.0f,
    	0.0f, 1.0f,
        1.0f, 1.0f,
    	0.0f, 0.0f,
        1.0f, 1.0f,
        1.0f, 0.0f
    };
	
	public static float[] texture_left = {
    	0.0f, 0.0f,
    	0.0f, 1.0f,
        0.5f, 1.0f,
    	0.0f, 0.0f,
        0.5f, 1.0f,
        0.5f, 0.0f
    };
	
	public static float[] texture_right = {
    	0.5f, 0.0f,
    	0.5f, 1.0f,
        1.0f, 1.0f,
    	0.5f, 0.0f,
        1.0f, 1.0f,
        1.0f, 0.0f
    };

	public static float[] texture_top = {
			0.0f, 0.0f,
			0.0f, 0.5f,
			1.0f, 0.5f,
			0.0f, 0.0f,
			1.0f, 0.5f,
			1.0f, 0.0f
	};

	public static float[] texture_bottom = {
			0.0f, 0.5f,
			0.0f, 1.0f,
			1.0f, 1.0f,
			0.0f, 0.5f,
			1.0f, 1.0f,
			1.0f, 0.5f
	};
    
    private int mTextureId = -1;
    
    public static GLVideoRect getInstance(){
    	if (instance == null){
    		instance = new GLVideoRect();
    	}
    	return instance;
    }
    
    public static void initInstance(){
    	if (instance != null){
    		instance.release();
    		instance = null;
    	}
    	instance = new GLVideoRect();
    }
    
    public GLVideoRect(){
    	init();
    }
    
    private void init(){
		GLES20.glGenBuffers( 6, mvbo, 0 );
		vboVertexNew = mvbo[0];
		vboTextureNew = mvbo[1];
		vboTextureLeftNew = mvbo[2];
		vboTextureRightNew = mvbo[3];
		vboTextureTopNew = mvbo[4];
		vboTextureBottomNew = mvbo[5];

    	initVertex();
    	initTextureBuffer();
    	
    	createProgram();
    }
    
    public void setTextureId(int textureId){
    	mTextureId = textureId;
    }
    
    public void setTextureType(TextureType type){
    	mTextureType = type;
    }
    
    @SuppressLint("InlinedApi")
	public void draw(float[] mtx) {
    	if (mTextureId < 0){
    		return;
    	}
    	
    	GLES20.glDisable(GLES20.GL_DEPTH_TEST);
    	
		GLES20.glUseProgram(mProgram);
		
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mTextureId);
        
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, mtx, 0);

        vertexVBO();
        textureVBO();
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertices.length / 2);

        GLES20.glDisableVertexAttribArray(0);
        GLES20.glDisableVertexAttribArray(1);
        
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		GLES20.glBindBuffer( GLES20.GL_ARRAY_BUFFER, 0 );
	}
    
    private void createProgram(){
		int vertexShader    = GLShaderManager.loadShader(GLES20.GL_VERTEX_SHADER, GLShaderManager.VERTEX_VIDEO);
        int fragmentShader  = GLShaderManager.loadShader(GLES20.GL_FRAGMENT_SHADER, GLShaderManager.FRAGMENT_VIDEO);

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL ES Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // creates OpenGL ES program executables
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        mTextureCoordHandle = GLES20.glGetAttribLocation(mProgram, "inputTextureCoordinate");
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
	}
    
    private void vertexVBO() {
		
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboVertexNew);
		// 传送顶点位置数据
		GLES20.glVertexAttribPointer(mPositionHandle, 2, GLES20.GL_FLOAT,
				false, 0, 0);
		GLES20.glEnableVertexAttribArray(0);
	}
    
    private void textureVBO() {
    	int buffer = vboTextureNew;

		switch (mTextureType){
			case TEXTURE_TYPE_LEFT:
				buffer = vboTextureLeftNew;
				break;
			case TEXTURE_TYPE_RIGHT:
				buffer = vboTextureRightNew;
				break;
			case TEXTURE_TYPE_TOP:
				buffer = vboTextureTopNew;
				break;
			case TEXTURE_TYPE_BOTTOM:
				buffer = vboTextureBottomNew;
				break;
		}
		
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffer);
		// 传送顶点位置数据
		GLES20.glVertexAttribPointer(mTextureCoordHandle, 2, GLES20.GL_FLOAT,
				false, 0, 0);
		GLES20.glEnableVertexAttribArray(1);
		
	}
    
    protected void initVertex(){
		int verLen = vertices.length*4;
        ByteBuffer bb = ByteBuffer.allocateDirect(verLen);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);
       
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboVertexNew);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, verLen, vertexBuffer,
				GLES20.GL_STATIC_DRAW);
	}
	
	private void initTextureBuffer(){
		// all
		float[] texCoor = texture_all;
		int textureLen = texCoor.length*4;
		ByteBuffer llbb = ByteBuffer.allocateDirect(textureLen);
		llbb.order(ByteOrder.nativeOrder());
		FloatBuffer textureVerticesBuffer = llbb.asFloatBuffer();
		textureVerticesBuffer.put(texCoor);
		textureVerticesBuffer.position(0);
		
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboTextureNew);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, textureLen, textureVerticesBuffer,
				GLES20.GL_STATIC_DRAW);
		
		// left
		texCoor = texture_left;
		
        textureLen = texCoor.length*4;
		llbb = ByteBuffer.allocateDirect(textureLen);
		llbb.order(ByteOrder.nativeOrder());
		textureVerticesBuffer=llbb.asFloatBuffer();
		textureVerticesBuffer.put(texCoor);
		textureVerticesBuffer.position(0);
		
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboTextureLeftNew);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, textureLen, textureVerticesBuffer,
				GLES20.GL_STATIC_DRAW);
		
		// right
		texCoor = texture_right;
		
        textureLen = texCoor.length*4;
		llbb = ByteBuffer.allocateDirect(textureLen);
		llbb.order(ByteOrder.nativeOrder());
		textureVerticesBuffer=llbb.asFloatBuffer();
		textureVerticesBuffer.put(texCoor);
		textureVerticesBuffer.position(0);
		
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboTextureRightNew);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, textureLen, textureVerticesBuffer,
				GLES20.GL_STATIC_DRAW);

		// right
		texCoor = texture_top;

		textureLen = texCoor.length*4;
		llbb = ByteBuffer.allocateDirect(textureLen);
		llbb.order(ByteOrder.nativeOrder());
		textureVerticesBuffer=llbb.asFloatBuffer();
		textureVerticesBuffer.put(texCoor);
		textureVerticesBuffer.position(0);

		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboTextureTopNew);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, textureLen, textureVerticesBuffer,
				GLES20.GL_STATIC_DRAW);

		// right
		texCoor = texture_bottom;

		textureLen = texCoor.length*4;
		llbb = ByteBuffer.allocateDirect(textureLen);
		llbb.order(ByteOrder.nativeOrder());
		textureVerticesBuffer=llbb.asFloatBuffer();
		textureVerticesBuffer.put(texCoor);
		textureVerticesBuffer.position(0);

		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboTextureBottomNew);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, textureLen, textureVerticesBuffer,
				GLES20.GL_STATIC_DRAW);
	}
	
	public void release(){
		GLES20.glDeleteBuffers(4, new int[]{vboVertexNew, vboTextureNew, vboTextureLeftNew, vboTextureRightNew, vboTextureTopNew, vboTextureBottomNew}, 0);
	}
}
