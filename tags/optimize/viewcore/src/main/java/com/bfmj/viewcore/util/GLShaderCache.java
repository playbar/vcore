package com.bfmj.viewcore.util;

import java.util.HashMap;

/**
 * Created by mac on 16/8/2.
 */

public class GLShaderCache {


    private HashMap<String, GLShaderStatus> mShaders = new HashMap<>();

    public void addShader(String key, GLShaderStatus p ){
        mShaders.put(key, p);
    }

    public GLShaderStatus getShader( String key ){
        GLShaderStatus val = mShaders.get(key);
        return val;
    }

}
