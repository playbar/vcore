package com.bfmj.viewcore.util;

import android.opengl.GLES20;

/**
 * Created by mac on 16/8/2.
 */

public class GLShaderStatus {

    private int mProgram;
    private int muMVPMatrixHandle;
    private int muColorHandle;
    private int maPositionHandle;

    private void createProgram(String strVer, String strFra){
        int vertexShader    = GLShaderManager.loadShader(GLES20.GL_VERTEX_SHADER, strVer);
        int fragmentShader  = GLShaderManager.loadShader(GLES20.GL_FRAGMENT_SHADER, strFra);

        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        muColorHandle = GLES20.glGetUniformLocation(mProgram, "uColor");
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
    }

}
