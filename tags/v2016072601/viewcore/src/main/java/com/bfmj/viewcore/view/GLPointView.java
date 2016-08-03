package com.bfmj.viewcore.view;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.render.GLPoint;
import com.bfmj.viewcore.render.GLRect;
import com.bfmj.viewcore.util.GLMatrixState;
import com.bfmj.viewcore.util.GLShaderManager;

import android.content.Context;
import android.opengl.GLES20;

/**
 * 
 * ClassName: GLPointView <br/>
 * @author lixianke    
 * @date: 2015-3-9 下午1:51:10 <br/>  
 * description:
 */
public class GLPointView extends GLView {
	private FloatBuffer vertexBuffer, sizeBuffer, colorBuffer;
    private int mProgram = -1;
    private int muMVPMatrixHandle;
    private int maPositionHandle, maSizeHandle, maColorHandle;
    
	IntBuffer ibb = null;
	
	private int vboVertexNew = 0;//GLRect.bufferIndex++;
	private int vboSizeNew = 0;//GLRect.bufferIndex++;
	private int vboColorNew = 0;//GLRect.bufferIndex++;
	private int []mvbo = new int[3];
	
	private boolean isNeedInitVertex = false;
	
	private ArrayList<GLPoint> points;
	private float changeDirection = 1.0f;
	private float changeColor = 0.2f;

	public GLPointView(Context context) {
		super(context);
	}
	
	@Override
	public void initDraw() {
		if (points != null){
			GLES20.glGenBuffers( 3, mvbo, 0 );
			vboVertexNew = mvbo[0];
			vboSizeNew = mvbo[1];
			vboColorNew = mvbo[2];
			initVertex();
			isNeedInitVertex = false;
		}
	}
	
	/**
	 * 添加点列表
	 * @author lixianke  @Date 2015-3-16 下午4:05:52
	 * @param points 点对象列表 
	 * @return 
	 */
	public void addPoint(ArrayList<GLPoint> points){
		if (this.points == null){
			this.points = points;
		} else {
			this.points.addAll(points);
		}
		isNeedInitVertex = true;
	}
	
	/**
	 * 清空点列表
	 * @author lixianke  @Date 2015-3-16 下午4:05:52
	 * @return 
	 */
	public void removeAll(){
		if (this.points != null){
			this.points.clear();
			this.points = null;
		}
	}
	
	/**
	 * 添加点对象
	 * @author lixianke  @Date 2015-3-16 下午4:06:41
	 * @param point 点对象 
	 * @return 
	 */
	public void addPoint(GLPoint point){
		if (this.points == null){
			this.points = new ArrayList<GLPoint>();
			
		}
		
		this.points.add(point);
		isNeedInitVertex = true;
	}
	
	/**
	 * 删除点对象
	 * @author lixianke  @Date 2015-3-16 下午4:06:41
	 * @param point 点对象 
	 * @return 
	 */
	public void removePoint(GLPoint point){
		if (this.points != null){
			this.points.remove(point);
		}
		isNeedInitVertex = true;
	}

	@Override
	public void draw(boolean isLeft) {
		if (points == null || points.size() == 0){
    		return;
    	}
		
		if (isNeedInitVertex){
    		initVertex();
    		isNeedInitVertex = false;
    	} else {
    		initColor();
    	}
		
		GLMatrixState state = getMatrixState();
		state.pushMatrix();
		
		getEyeMatrix(state.getVMatrix(), isLeft);
    	
    	GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_SRC_COLOR);
    	GLES20.glEnable(GLES20.GL_BLEND);
    	
		GLES20.glUseProgram(mProgram);
        
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, getMatrixState().getFinalMatrix(), 0);

        vertexVBO();
        sizeVBO();
        colorVBO();
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, points.size());

        GLES20.glDisableVertexAttribArray(0);
        GLES20.glDisableVertexAttribArray(1);
        GLES20.glDisableVertexAttribArray(2);
        
        GLES20.glDisable(GLES20.GL_BLEND);
        
        state.popMatrix();
	}
	
	private void createProgram(){
		int vertexShader    = GLShaderManager.loadShader(GLES20.GL_VERTEX_SHADER, GLShaderManager.VERTEX_POINT);
        int fragmentShader  = GLShaderManager.loadShader(GLES20.GL_FRAGMENT_SHADER, GLShaderManager.FRAGMENT_POINT);

        mProgram = GLES20.glCreateProgram();       // create empty OpenGL ES Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // creates OpenGL ES program executables        
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        maSizeHandle = GLES20.glGetAttribLocation(mProgram, "aSize");
        maColorHandle = GLES20.glGetAttribLocation(mProgram, "aColor");
	}
	
    private void vertexVBO(){
		
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboVertexNew);
		// 传送顶点位置数据
		GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT,
				false, 0, 0);
		GLES20.glEnableVertexAttribArray(0);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
	}
    
    private void sizeVBO(){
		
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboSizeNew);
		// 传送顶点位置数据
		GLES20.glVertexAttribPointer(maSizeHandle, 1, GLES20.GL_FLOAT,
				false, 0, 0);
		GLES20.glEnableVertexAttribArray(1);
	}
    
    private void colorVBO(){
		
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboColorNew);
		// 传送顶点位置数据
		GLES20.glVertexAttribPointer(maColorHandle, 4, GLES20.GL_FLOAT,
				false, 0, 0);
		GLES20.glEnableVertexAttribArray(2);
	}
    
    private void initVertex(){
    	int count = points.size();
    	float[] vertices = new float[count * 3];
    	float[] sizes = new float[count];
    	float[] colors = new float[count * 4];
    	
    	for (int i = 0; i < count; i++) {
    		GLPoint point = points.get(i);
    		
    		vertices[3 * i] = point.getX();
    		vertices[3 * i + 1] = point.getY();
    		vertices[3 * i + 2] = point.getZ();
    		
    		sizes[i] = point.getSize();
    		
    		GLColor color = point.getColor();
    		colors[4 * i] = color.getR();
    		colors[4 * i + 1] = color.getG();
    		colors[4 * i + 2] = color.getB();
    		colors[4 * i + 3] = color.getA();
		}
    	
		int verLen = vertices.length*4;
        ByteBuffer bb = ByteBuffer.allocateDirect(verLen);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);
       
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboVertexNew);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, verLen, vertexBuffer,
				GLES20.GL_STATIC_DRAW);
		
		int sizeLen = sizes.length*4;
        ByteBuffer sbb = ByteBuffer.allocateDirect(sizeLen);
        sbb.order(ByteOrder.nativeOrder());
        sizeBuffer = sbb.asFloatBuffer();
        sizeBuffer.put(sizes);
        sizeBuffer.position(0);
       
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboSizeNew);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, sizeLen, sizeBuffer,
				GLES20.GL_STATIC_DRAW);
		
		int colorLen = colors.length*4;
        ByteBuffer cbb = ByteBuffer.allocateDirect(colorLen);
        cbb.order(ByteOrder.nativeOrder());
        colorBuffer = cbb.asFloatBuffer();
        colorBuffer.put(colors);
        colorBuffer.position(0);
       
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboColorNew);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, colorLen, colorBuffer,
				GLES20.GL_STATIC_DRAW);
		
		if (mProgram == -1){
			createProgram();
		}
	}
    
    private void initColor(){
    	changeColor += 0.004f * changeDirection;
    	if (changeColor >= 0.8f || changeColor <= 0.2f){
    		changeDirection *= -1.0f;
    	}
    	int count = points.size();
    	float[] colors = new float[count * 4];
    	
    	for (int i = 0; i < count; i++) {
    		GLPoint point = points.get(i);
    		
    		GLColor color = point.getColor();
    		colors[4 * i] = color.getR();
    		colors[4 * i + 1] = color.getG();
    		colors[4 * i + 2] = color.getB();
    		
    		float a = color.getA();
    		if (a  < 0.8){
    			a += changeColor;
    			if (a > 1.0){
    				a = 1.0f - (a - 1.0f);
    			}
    		}
    		
    		colors[4 * i + 3] = a;
		}

		int colorLen = colors.length*4;
        ByteBuffer cbb = ByteBuffer.allocateDirect(colorLen);
        cbb.order(ByteOrder.nativeOrder());
        colorBuffer = cbb.asFloatBuffer();
        colorBuffer.put(colors);
        colorBuffer.position(0);
       
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboColorNew);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, colorLen, colorBuffer,
				GLES20.GL_STATIC_DRAW);
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
		// TODO Auto-generated method stub
		
	}
}