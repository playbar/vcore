package bfmj.viewcore.util;

import android.opengl.Matrix;

/**
 * 
 * ClassName: GLMatrixState <br/>
 * @author lixianke    
 * @date: 2015-3-9 上午9:49:47 <br/>  
 * description:
 */
public class GLMatrixState {
	private float[] mProjMatrix = new float[16];
	private float[] mVMatrix = new float[16];
	private float[] mTranslationMatrix = new float[16];
	private float[] mScaleMatrix = new float[16];
	private float[] mRotateMatrix = new float[16];
    private float[] currMatrix;
    
    //保护变换矩阵的栈  
    static float[][] mStack=new float[10][16];
    static int stackTop=-1;
    
    /**
     * 初始化矩阵
     * @author lixianke  @Date 2015-3-20 上午11:59:13
     * @param oMatrix 矩阵 
     * @return
     */
    public static float[] getInitMatrix(){
    	float[] matrix = new float[16];
    	Matrix.setRotateM(matrix, 0, 0, 1, 0, 0);
    	return matrix;
    }
    
    
    public GLMatrixState(){
    	initStack();
    }
    
    private void initStack(){
    	currMatrix = getInitMatrix();
    	mTranslationMatrix = getInitMatrix();
    	mScaleMatrix = getInitMatrix();
    	mRotateMatrix = getInitMatrix();
    }
  
    //保护变换矩阵
    public void pushMatrix(){
    	stackTop++;
    	for(int i=0;i<16;i++)
    	{
    		mStack[stackTop][i]=currMatrix[i];
    	}
    }
    
    //恢复变换矩阵
    public void popMatrix(){
    	for(int i=0;i<16;i++)
    	{
    		currMatrix[i]=mStack[stackTop][i];
    	}
    	stackTop--;
    }
    
    public float[] getFinalMatrix(){
    	float[] mMVPMatrix=new float[16];
    	Matrix.multiplyMM(mMVPMatrix, 0, mScaleMatrix, 0, mRotateMatrix, 0);
    	Matrix.multiplyMM(mMVPMatrix, 0, mTranslationMatrix , 0, mMVPMatrix, 0);
    	Matrix.multiplyMM(mMVPMatrix, 0, currMatrix, 0, mMVPMatrix, 0);
    	Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, mMVPMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0); 
        return mMVPMatrix;
    }
    
    public void setVMatrix(float[] matrix){
    	System.arraycopy(matrix, 0, mVMatrix, 0, 16);
    }
    
    public float[] getVMatrix(){
    	return mVMatrix;
    }
    
    public float[] getProjMatrix(){
    	return mProjMatrix;
    }
    
    public float[] getCurrentMatrix(){
    	return currMatrix;
    }
    
    public float[] getTranslationMatrix(){
    	return mTranslationMatrix;
    }
    
    public float[] getScaleMatrix(){
    	return mScaleMatrix;
    }
    
    public float[] getRotateMatrix(){
    	return mRotateMatrix;
    }
}