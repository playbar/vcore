package com.bfmj.viewcore.view;

import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.render.GLRenderParams;
import com.bfmj.viewcore.util.GLFontUtils;
import com.bfmj.viewcore.util.GLTextureUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;

/**
 * 
 * ClassName: GLTextView <br/>
 * @author lixianke    
 * @date: 2015-3-9 下午1:51:00 <br/>  
 * description:
 */
public class GLTextView extends GLRectView {
	public static final int ALIGN_LEFT = 0;
	public static final int ALIGN_CENTER = 1;
	public static final int ALIGN_RIGHT = 2;
	public static final int NORMAL = Typeface.NORMAL;
	public static final int BOLD = Typeface.BOLD;
	public static final int ITALIC = Typeface.ITALIC;
	public static final int BOLD_ITALIC = Typeface.BOLD_ITALIC;
	
	private GLRenderParams mRenderParams;
	private String mText = "";
	private int mTextColor = Color.WHITE;
	private GLColor mTextGLColor = null;
	private int mTextSize = 24;
	private int mLineHeight = -1;
	private int mStyle = BOLD;
	private Alignment mAlignment = Alignment.ALIGN_NORMAL;
	
	private float mDefaultWidth = 0;
	private float mDefaultHeight = 0;
	
	private Bitmap mTextBitmap;
	private float mTextPadding = 8;
	private String mTag = "";

	public GLTextView(Context context) {
		super(context);
	}
	
	/**
	 * 设置文字内容
	 * @author lixianke  @Date 2015-3-11 上午11:40:21
	 * @param text 文字内容 
	 * @return 
	 */
	public void setText(String text){
		if (mText == text){
			//return;
		}
		
		mText = text;
		
		initText();
	}
	
	/**
	 * 获取文字内容
	 * @author lixianke  @Date 2015-4-9 下午8:04:25
	 * @param 
	 * @return 文字内容
	 */
	public String getText() {
		return mText;
	}
	
	/**
	 * 设置文字颜色
	 * @author lixianke  @Date 2015-3-11 上午11:42:13
	 * @param color android.graphics.Color对象
	 * @return 
	 */
	public void setTextColor(int color){
		mTextColor = color;
		mTextGLColor = null;
		
		initText();
	}
	
	/**
	 * 设置文字颜色
	 * @author lixianke  @Date 2015-3-11 上午11:43:46
	 * @param color GLColor对象 
	 * @return 无
	 */
	public void setTextColor(GLColor color){
		mTextGLColor = color;
		mTextColor = Color.WHITE;
		
		initText();
	}
	
	/**
	 * 设置字体大小
	 * @author lixianke  @Date 2015-3-11 上午11:46:22
	 * @param size 文字大小  
	 * @return
	 */
	public void setTextSize(int size){
		mTextSize = size;
		
		initText();
	}
	
	/**
	 * 设置字体样式
	 * @author lixianke  @Date 2015-3-16 下午6:16:00
	 * @param style 取值为 NORMAL、BOLD、ITALIC、BOLD_ITALIC;
	 * @return 无
	 */
	public void setStyle(int style){
		mStyle = style;
		
		initText();
	}
	
	/**
	 * 设置行距
	 * @author lixianke  @Date 2015-3-11 下午4:38:08
	 * @param lineHeight 行距值
	 * @return
	 */
	public void setLineHeight(int lineHeight){
		mLineHeight = lineHeight;
	}
	
	/**
	 * 设置对齐方式
	 * @author lixianke  @Date 2015-3-11 上午11:47:08
	 * @param alignment 对齐方式，取值为GLTextView.ALIGN_LEFT、GLTextView.ALIGN_CENTER、GLTextView.ALIGN_RIGHT  
	 * @return
	 */
	public void setAlignment(int alignment){
		if (alignment == ALIGN_LEFT){
			mAlignment = Alignment.ALIGN_NORMAL;
		} else if (alignment == ALIGN_CENTER){
			mAlignment = Alignment.ALIGN_CENTER;
		} else if (alignment == ALIGN_RIGHT){
			mAlignment = Alignment.ALIGN_OPPOSITE;
		}
	}
	
	private void initText(){
		if (!isSurfaceCreated()){
			return;
		}
		
		if (mText == null){
			return;
		}
		
		if (mRenderParams != null){
			removeRender(mRenderParams);
			mRenderParams = null;
		}
		
		int textureId = -1;
		
		mTextBitmap = createBitmap();
		if (mTextBitmap != null){
			textureId = GLTextureUtils.initImageTexture(getContext(), mTextBitmap, true);
		}
		
		if (textureId > -1){
			mRenderParams = new GLRenderParams(GLRenderParams.RENDER_TYPE_IMAGE);
			mRenderParams.setTextureId(textureId);
			updateRenderSize(mRenderParams, getInnerWidth(), getInnerHeight());
		}
		
		if (mRenderParams != null){
			addRender(mRenderParams);
		}
	}
	
	private Bitmap createBitmap(){
		int width = (int)getInnerWidth();
		int height = (int)getInnerHeight();
		
		if (width <= 0){
			return null;
		}
		
		float scale = (float)mTextSize / 28;
		width = (int)(width / scale);
		height = (int)(height / scale);
		
        TextPaint p = new TextPaint();
        //字体设置  
        String fontType = "宋体";
        //Typeface typeface = Typeface.create(fontType, mStyle);
        //消除锯齿  
        p.setAntiAlias(true); 
        //字体颜色
        if (mTextGLColor != null){
        	p.setARGB((int)(mTextGLColor.getA() * 255), (int)(mTextGLColor.getR() * 255), 
        			(int)(mTextGLColor.getG() * 255), (int)(mTextGLColor.getB() * 255));
        } else {
        	p.setColor(mTextColor);
        }
        
        p.setTypeface(GLFontUtils.getInstance(getContext()).getFontTypeface());
        p.setTextSize(28);
        
        float lineHeight = mLineHeight == -1 ? 1.0f : (float)mLineHeight / mTextSize / 1.2f;
        
        //绘制字体
        float textWidth = width - mTextPadding;
    	StaticLayout sl = new StaticLayout(mText, p, (int)textWidth, mAlignment, lineHeight, 0.0f, true);
    	
    	float maxWidth = 0;
    	if (mDefaultWidth == WRAP_CONTENT){
    		for (int i = 0; i < sl.getLineCount(); i++) {
				if (sl.getLineWidth(i) > maxWidth){
					maxWidth = sl.getLineWidth(i);
				}
			}
    		
    		if (maxWidth > 0){
    			textWidth = maxWidth + 8;
    			sl = new StaticLayout(mText, p, (int)textWidth, mAlignment, lineHeight, 0.0f, true);
    		}
    		
    		width = sl.getWidth();
    		setWidth(width * scale + getPaddingLeft() + getPaddingRight() + 8);
    	}
    	
    	if (mDefaultHeight == WRAP_CONTENT){
    		height = sl.getHeight();
    		setHeight(height * scale + getPaddingTop() + getPaddingBottom());
    	}
    	
    	if (width < 1 || height < 1){
    		return null;
    	}
    	
    	Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.TRANSPARENT);
        
    	sl.draw(canvas);
        
        return bitmap;
	}
	
	@Override
	public void initDraw() {
		super.initDraw();
		
		initText();
	}
	
	@Override
	public void setLayoutParams(float x, float y, float width, float height) {
		mDefaultWidth = width;
		mDefaultHeight = height;
		
		super.setLayoutParams(x, y, width, height);
	}

	/**
	 * 设置文本的padding,默认为8
	 * @author linzanxian  @Date 2015-10-12 上午10:06:32
	 * @param padding 内边距  
	 * @return void
	 */
	public void setTextPadding(float padding) {
		mTextPadding = padding;
	}
	
	/**
	 * 添加标签
	 * @author linzanxian  @Date 2015-10-13 上午10:06:32
	 * @param tag 标签
	 * @return void
	 */
	public void setTag(String tag) {
		mTag = tag;
	}
	
	public String getTag() {
		return mTag;
	}
}