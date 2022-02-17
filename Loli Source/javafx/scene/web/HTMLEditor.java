/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.web;

import com.sun.javafx.scene.web.skin.HTMLEditorSkin;
import javafx.css.StyleableProperty;
import javafx.print.PrinterJob;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

public class HTMLEditor
extends Control {
    public HTMLEditor() {
        ((StyleableProperty)((Object)super.skinClassNameProperty())).applyStyle(null, "com.sun.javafx.scene.web.skin.HTMLEditorSkin");
        this.getStyleClass().add("html-editor");
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new HTMLEditorSkin(this);
    }

    public String getHtmlText() {
        return ((HTMLEditorSkin)this.getSkin()).getHTMLText();
    }

    public void setHtmlText(String string) {
        ((HTMLEditorSkin)this.getSkin()).setHTMLText(string);
    }

    public void print(PrinterJob printerJob) {
        ((HTMLEditorSkin)this.getSkin()).print(printerJob);
    }
}

