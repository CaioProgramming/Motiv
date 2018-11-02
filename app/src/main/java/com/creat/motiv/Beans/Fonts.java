package com.creat.motiv.Beans;

import android.graphics.Typeface;

public class Fonts {


    public Fonts(Typeface font, String fontname) {
        this.font = font;
        this.fontname = fontname;
    }

    public void setFont(Typeface font) {
        this.font = font;
    }

    public Typeface getFont() {
        return font;
    }

    Typeface font;
    String fontname;

    public String getFontname() {
        return fontname;
    }

    public void setFontname(String fontname) {
        this.fontname = fontname;
    }

    public Fonts() {
    }


    public Fonts(Typeface font) {
        this.font = font;
    }
}
