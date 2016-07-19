package com.bfmj.viewcore.view;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.bfmj.viewcore.render.GLMesh;
import com.bfmj.viewcore.render.GLRect;
import com.bfmj.viewcore.render.GLVector2;
import com.bfmj.viewcore.render.GLVector3;
import com.bfmj.viewcore.util.GLMatrixState;
import com.bfmj.viewcore.util.GLObjFileUtils;
import com.bfmj.viewcore.util.GLShaderManager;
import com.bfmj.viewcore.util.GLTextureUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.Matrix;

/**
 * 
 * ClassName: GLSenceView <br/>
 * @author lixianke    
 * @date: 2015-3-9 下午1:51:30 <br/>  
 * description:
 */
public class GLSenceView extends GLView {
	private FloatBuffer vertexBuffer, textureVerticesBuffer;
    private int mProgram = -1;
    private int muMVPMatrixHandle;
    private int muAlphaHandle;
    private int muMaskHandle;
    private int mPositionHandle;
    private int mTextureCoordHandle;
    
    private float[] vertices;
    private float[] texCoor;
    
    private int verLen = 0;
    private int textureLen = 0;
	
	private int vboVertexNew = GLRect.bufferIndex++;
	private int vboTextureNew = GLRect.bufferIndex++;
	
	private boolean isSurfaceCreated = false;
	private boolean isNeedInitVertex = false;
	private int mTextureId = -1;
	private int mResId;
    private Bitmap mBitmap;
    private float mScaleX = 1.0f;
    private float mScaleY = 1.0f;
    private float mScaleZ = 1.0f;
	
	public GLSenceView(Context context) {
		super(context);
	}
	
	/**
	 * 设置OBJ模型文件
	 * @author lixianke  @Date 2015-3-12 下午3:45:46
	 * @param filename 文件名，需将obj文件放置到assets文件夹 
	 * @return
	 */
	public void setObjFile(String filename){
		GLMesh mesh = GLObjFileUtils.LoadMesh(getContext().getResources(), filename);
        
        int vCount = mesh.getVertexIndexs().length;
        vertices = new float[vCount * 3];
    	for(int i=0;i < vCount; i++){
    		GLVector3 vector3 = mesh.getVertices()[mesh.getVertexIndexs()[i]];
    		vertices[i * 3]= vector3.getX();
    		vertices[i * 3 + 1]= vector3.getY();
    		vertices[i * 3 + 2]= vector3.getZ();
    	}
        
        int tCount = mesh.getTextureIndexs().length;
        texCoor = new float[tCount * 2];
        for(int i=0;i < tCount; i++){
        	GLVector2 vector2 = mesh.getTextures()[mesh.getTextureIndexs()[i]];
        	texCoor[i * 2] = vector2.getS();
        	texCoor[i * 2 + 1] = vector2.getT();
    	}
        
		isNeedInitVertex = true;
	}
	
	/**
	 * 设置图片
	 * @author lixianke  @Date 2015-3-11 下午5:06:59
	 * @param resId 资源ID
	 * @return
	 */
	public void setImage(int resId){
		if (mResId == resId){
			return;
		}
		
		mResId = resId;
		mBitmap = null;
		
		initImage();
	}
	
	/**
	 * 设置图片
	 * @author lixianke  @Date 2015-3-11 下午5:07:28
	 * @param bitmap Bitmap对象
	 * @return
	 */
	public void setImage(Bitmap bitmap){
		if (mBitmap == bitmap){
			return;
		}
		
		mBitmap = bitmap;
		mResId = 0;
		
		initImage();
	}
	
	/**
	 * 缩放场景， x y z三轴以同样的比例缩放
	 * @author lixianke  @Date 2015-6-19 下午5:33:21
	 * @param scale 缩放比例
	 * @return
	 */
	public void scale(float scale){
		scale(scale, scale, scale);
    }
	
	/**
	 * 缩放场景
	 * @author lixianke  @Date 2015-6-19 下午5:35:07
	 * @param sx x轴缩放比例
	 * @param sy y轴缩放比例
	 * @param sz z轴缩放比例
	 * @return
	 */
	public void scale(float sx, float sy, float sz){   	
//    	float[] mtx = getMatrixState().getScaleMatrix();
//    	System.arraycopy(GLMatrixState.getInitMatrix(), 0, mtx, 0, 16);
    	mScaleX = sx;
    	mScaleY = sy;
    	mScaleZ = sz;
	}
	
	/**
	 * 平移场景
	 * @author lixianke  @Date 2015-6-19 下午5:38:38
	 * @param tx x轴平移距离
	 * @param ty y轴平移距离
	 * @param tz z轴平移距离
	 * @return
	 */
	public void translate(float tx, float ty, float tz) {
		Matrix.translateM(getMatrixState().getCurrentMatrix(), 0, tx, ty, tz);
	}
	
	/**
	 * 旋转场景
	 * @author lixianke  @Date 2015-6-19 下午5:39:51
	 * @param angle 旋转角度
	 * @param rx 沿x的向量分量
	 * @param ry 沿y的向量分量
	 * @param rz 沿z的向量分量 
	 * @return
	 */
	public void rotate(float angle, float rx, float ry, float rz){
		Matrix.rotateM(getMatrixState().getCurrentMatrix(), 0, angle, rx, ry, rz);
	}

	@Override
	public void initDraw() {
		isSurfaceCreated = true;
		initImage();
	}

	@Override
	public void draw(boolean isLeft) {
		if (!isVisible() || !isSurfaceCreated || vertices == null || mTextureId < 0){
    		return;
    	}
    	
    	if (isNeedInitVertex){
    		initVertex();
    		isNeedInitVertex = false;
    	}
    	
    	GLMatrixState state = getMatrixState();
    	
    	getEyeMatrix(state.getVMatrix(), isLeft);
    	
    	state.pushMatrix();
    	Matrix.scaleM(state.getCurrentMatrix(), 0, mScaleX, mScaleY, mScaleZ);
    	
    	GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
    	GLES20.glEnable(GLES20.GL_BLEND);
    	
		GLES20.glUseProgram(mProgram);
		
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId);
        
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, getMatrixState().getFinalMatrix(), 0);
        GLES20.glUniform1f(muAlphaHandle, getAlpha());
        GLES20.glUniform1f(muMaskHandle, getMask());

        vertexVBO();
        textureVBO();
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertices.length / 3);

        GLES20.glDisableVertexAttribArray(0);
        GLES20.glDisableVertexAttribArray(1);
        
        GLES20.glDisable(GLES20.GL_BLEND);
        
        state.popMatrix();
	}
	
	private void init(){
		
    	initVertex();

    	initTextureBuffer();

    	createProgram();
    }
	
	private void createProgram(){
		int vertexShader    = GLShaderManager.loadShader(GLES20.GL_VERTEX_SHADER, GLShaderManager.VERTEX_SENCE);
        int fragmentShader  = GLShaderManager.loadShader(GLES20.GL_FRAGMENT_SHADER, GLShaderManager.FRAGMENT_SENCE);

        mProgram = GLES20.glCreateProgram();       // create empty OpenGL ES Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // creates OpenGL ES program executables        
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        muAlphaHandle = GLES20.glGetUniformLocation(mProgram, "uAlpha");
        muMaskHandle = GLES20.glGetUniformLocation(mProgram, "uMask");
        mTextureCoordHandle = GLES20.glGetAttribLocation(mProgram, "inputTextureCoordinate");
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
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
		if (vertices == null){
			return;
		}
		
		verLen = vertices.length*4;
        ByteBuffer bb = ByteBuffer.allocateDirect(verLen);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);
       
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboVertexNew);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, verLen, vertexBuffer,
				GLES20.GL_STATIC_DRAW);
	}
	
	private void initTextureBuffer(){
		if (texCoor == null){
			return;
		}
		
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
	
	private void initImage(){
		if (!isSurfaceCreated){
			return;
		}
		
		boolean isRecycle = true;
		Bitmap bitmap = null;
		if (mResId != 0){
			InputStream is = getContext().getResources().openRawResource(mResId);
	        
	        try {
	        	bitmap = BitmapFactory.decodeStream(is);
	        } finally {
	            try {
	                is.close();
	            } catch(IOException e) {
	                e.printStackTrace();
	            }
	        }
		} else if (mBitmap != null){
			bitmap = mBitmap;
			isRecycle = false;
		}

    	if (bitmap != null){
    		releaseTexture(mTextureId);
			mTextureId = GLTextureUtils.initImageTexture(getContext(), bitmap, isRecycle);
		}
    	
    	if (mTextureId > 0){
    		init();
    	}
	}
	
	@Override
	public void onBeforeDraw() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAfterDraw() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSurfaceCreated() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSurfaceChanged(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void release() {
		if (mTextureId > -1){
			BaseViewActivity activity = (BaseViewActivity)getContext();
			if (activity != null && activity.getRootView() != null) {
				activity.getRootView().queueEvent(new Runnable() {
					
					@Override
					public void run() {
						GLTextureUtils.releaseTexture(mTextureId);
						mTextureId = -1;
					}
				});
			}
		}
	}
}
