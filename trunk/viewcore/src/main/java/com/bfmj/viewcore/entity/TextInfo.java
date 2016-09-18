package com.bfmj.viewcore.entity;

import com.bfmj.viewcore.render.GLColor;

import java.io.Serializable;

/**
 * Created by lixianke on 2016/9/13.
 */
public class TextInfo implements Serializable {
    private String content;
    private int size = 32;
    private GLColor color = new GLColor(0xffffff);
    private int lineHeight;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public GLColor getColor() {
        return color;
    }

    public void setColor(GLColor color) {
        this.color = color;
    }

    public int getLineHeight() {
        return lineHeight;
    }

    public void setLineHeight(int lineHeight) {
        this.lineHeight = lineHeight;
    }
}
