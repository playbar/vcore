package bfmj.viewcore.render;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import android.opengl.GLES20;
import bfmj.viewcore.util.GLShaderManager;

/**
 * 
 * ClassName: GLColorRect <br/>
 * @author lixianke    
 * @date: 2015-3-9 下午3:13:45 <br/>  
 * description:
 */
public class GLColorRect extends GLRect {
	private static GLColorRect instance;
	
	private FloatBuffer vertexBuffer;
    private int mProgram;
    private int muMVPMatrixHandle;
    private int muColorHandle;
    private int maPositionHandle;
    
    private int verLen=0;
	
	private int vboVertexNew = bufferIndex++;
    
    private float[] mColor;
    
    public static GLColorRect getInstance(){
    	if (instance == null){
    		instance = new GLColorRect();
    	}
    	return instance;
    }
    
    public static void initInstance(){
    	if (instance != null){
    		instance.release();
    		instance = null;
    	}
    	instance = new GLColorRect();
    }
    
    private GLColorRect(){
    	init();
    }
    
    private void init(){
		initVertex();
    	
    	createProgram();
    }
    
    /**
     * 设置颜色
     * @author lixianke  @Date 2015-3-17 下午2:48:56
     * @param color 颜色值 rgba数组
     * @return
     */
    public void setColor(float[] color){
    	mColor = color;
    }
    
    @Override
    public void setAlpha(float alpha){
    	mColor[3] = alpha;
    }
    
    public void draw(float[] mtx) {
    	if (mColor == null){
    		return;
    	}

    	GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_SRC_COLOR);
    	GLES20.glEnable(GLES20.GL_BLEND);

		GLES20.glUseProgram(mProgram);
        
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, mtx, 0);
        
        float[] color = new float[]{
        	mColor[0] * getMask(),
        	mColor[1] * getMask(),
        	mColor[2] * getMask(),
        	mColor[3]
        };
        
        GLES20.glUniform4fv(muColorHandle, 1, color, 0);

        vertexVBO();
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, getVertices().length / 3);

        GLES20.glDisableVertexAttribArray(0);
        
        GLES20.glDisable(GLES20.GL_BLEND);
	}
    
    private void createProgram(){
		int vertexShader    = GLShaderManager.loadShader(GLES20.GL_VERTEX_SHADER, GLShaderManager.VERTEX_COLOR);
        int fragmentShader  = GLShaderManager.loadShader(GLES20.GL_FRAGMENT_SHADER, GLShaderManager.FRAGMENT_COLOR);

        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix"); 
        muColorHandle = GLES20.glGetUniformLocation(mProgram, "uColor");
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
	}
    
    private void vertexVBO() {
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboVertexNew);
		// 传送顶点位置数据
		GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT,
				false, 0, 0);
		GLES20.glEnableVertexAttribArray(0);
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
    
    public void release(){
		GLES20.glDeleteBuffers(1, new int[]{vboVertexNew}, 0);
	}
}
