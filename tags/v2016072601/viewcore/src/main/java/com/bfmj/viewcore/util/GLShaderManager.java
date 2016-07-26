package com.bfmj.viewcore.util;

import android.opengl.GLES20;

/**
 * 
 * ClassName: GLShaderManager <br/>
 * @author lixianke    
 * @date: 2015-3-9 上午9:46:19 <br/>  
 * description:
 */
public class GLShaderManager {
	
	public static final String VERTEX_IMAGE =
		"uniform mat4 uMVPMatrix;" +
		"uniform float uAlpha;" +
		"uniform float uMask;" +
        "attribute vec3 vPosition;" +
        "attribute vec2 inputTextureCoordinate;" +
        "varying vec2 textureCoordinate;" +
        "varying float vAlpha;" +
        "varying float vMask;" +
        "void main()" +
        "{"+
            "gl_Position = uMVPMatrix * vec4(vPosition, 1);"+
            "textureCoordinate = inputTextureCoordinate;" +
            "vAlpha = uAlpha;" +
            "vMask = uMask;" +
        "}";
	
	public static final String FRAGMENT_IMAGE =
		"precision mediump float;" +
        "varying vec2 textureCoordinate;\n" +
        "uniform sampler2D s_texture;\n" +
        "varying float vAlpha;" +
        "varying float vMask;" +
        "void main() {" +
        	"vec4 color = texture2D( s_texture, textureCoordinate );\n" +
        	"gl_FragColor = vec4(color.r * vMask, color.g * vMask, color.b * vMask, color.a * vAlpha);\n" +
        "}";
	
	public static final String VERTEX_SENCE =
			"uniform mat4 uMVPMatrix;" +
			"uniform float uAlpha;" +
			"uniform float uMask;" +
	        "attribute vec3 aPosition;" +
	        "attribute vec2 inputTextureCoordinate;" +
	        "varying vec2 textureCoordinate;" +
	        "varying float vAlpha;" +
	        "varying float vMask;" +
	        "void main()" +
	        "{"+
	            "gl_Position = uMVPMatrix * vec4(aPosition, 1);"+
	            "textureCoordinate = inputTextureCoordinate;" +
	            "vAlpha = uAlpha;" +
	            "vMask = uMask;" +
	        "}";
		
	public static final String FRAGMENT_SENCE =
			"precision mediump float;" +
	        "varying vec2 textureCoordinate;\n" +
	        "uniform sampler2D s_texture;\n" +
	        "varying float vAlpha;" +
	        "varying float vMask;" +
	        "void main() {" +
	        	"vec4 color = texture2D( s_texture, textureCoordinate );\n" +
	        	"gl_FragColor = vec4(color.r * vMask, color.g * vMask, color.b * vMask, color.a * vAlpha);\n" +
	        "}";
	
	public static final String VERTEX_COLOR =
		"uniform mat4 uMVPMatrix;" +
		"uniform vec4 uColor;" + 
        "attribute vec3 aPosition;" +
        "varying vec4 vColor;" +
        "void main(){"+
            "gl_Position = uMVPMatrix * vec4(aPosition, 1);"+
            "vColor = uColor;" +
        "}";
	
	public static final String FRAGMENT_COLOR =
		"precision mediump float;" +
		"varying vec4 vColor;" +
        "void main(){" +
        	"gl_FragColor = vColor;\n" +
        "}";
	
	public static final String VERTEX_POINT =
		"uniform mat4 uMVPMatrix;" +
        "attribute vec3 aPosition;" +
        "attribute float aSize;" +
        "attribute vec4 aColor;" +
        "varying vec4 vColor;" +
        "void main()" +
        "{"+	
        	"gl_PointSize = aSize;"+
            "gl_Position = uMVPMatrix * vec4(aPosition, 1);"+
            "vColor = aColor;"+
        "}";

	public static final String FRAGMENT_POINT =
        "precision mediump float;" +
        "varying vec4 vColor;" +
        "void main() {" +
        	"gl_FragColor = vColor;\n" +
        "}";
	
	public static final String VERTEX_VIDEO =
		"uniform mat4 uMVPMatrix;" +
        "attribute vec3 vPosition;" +
        "attribute vec2 inputTextureCoordinate;" +
        "varying vec2 textureCoordinate;" +
        "void main()" +
        "{"+
            "gl_Position = uMVPMatrix * vec4(vPosition, 1);"+
            "textureCoordinate = inputTextureCoordinate;" +
        "}";

	public static final String FRAGMENT_VIDEO =
       "#extension GL_OES_EGL_image_external : require\n"+
        "precision mediump float;" +
        "varying vec2 textureCoordinate;\n" +
        "uniform samplerExternalOES s_texture;\n" +
        "void main() {" +
        "  gl_FragColor = texture2D( s_texture, textureCoordinate );\n" +
        "}";
	
	public static int loadShader(int type, String shaderCode){
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }
}
