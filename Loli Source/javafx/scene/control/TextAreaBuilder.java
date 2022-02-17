/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputControlBuilder;
import javafx.util.Builder;

@Deprecated
public class TextAreaBuilder<B extends TextAreaBuilder<B>>
extends TextInputControlBuilder<B>
implements Builder<TextArea> {
    private int __set;
    private Collection<? extends CharSequence> paragraphs;
    private int prefColumnCount;
    private int prefRowCount;
    private String promptText;
    private double scrollLeft;
    private double scrollTop;
    private boolean wrapText;

    protected TextAreaBuilder() {
    }

    public static TextAreaBuilder<?> create() {
        return new TextAreaBuilder();
    }

    public void applyTo(TextArea textArea) {
        super.applyTo(textArea);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            textArea.getParagraphs().addAll(this.paragraphs);
        }
        if ((n2 & 2) != 0) {
            textArea.setPrefColumnCount(this.prefColumnCount);
        }
        if ((n2 & 4) != 0) {
            textArea.setPrefRowCount(this.prefRowCount);
        }
        if ((n2 & 8) != 0) {
            textArea.setPromptText(this.promptText);
        }
        if ((n2 & 0x10) != 0) {
            textArea.setScrollLeft(this.scrollLeft);
        }
        if ((n2 & 0x20) != 0) {
            textArea.setScrollTop(this.scrollTop);
        }
        if ((n2 & 0x40) != 0) {
            textArea.setWrapText(this.wrapText);
        }
    }

    public B paragraphs(Collection<? extends CharSequence> collection) {
        this.paragraphs = collection;
        this.__set |= 1;
        return (B)this;
    }

    public B paragraphs(CharSequence ... arrcharSequence) {
        return this.paragraphs(Arrays.asList(arrcharSequence));
    }

    public B prefColumnCount(int n2) {
        this.prefColumnCount = n2;
        this.__set |= 2;
        return (B)this;
    }

    public B prefRowCount(int n2) {
        this.prefRowCount = n2;
        this.__set |= 4;
        return (B)this;
    }

    @Override
    public B promptText(String string) {
        this.promptText = string;
        this.__set |= 8;
        return (B)this;
    }

    public B scrollLeft(double d2) {
        this.scrollLeft = d2;
        this.__set |= 0x10;
        return (B)this;
    }

    public B scrollTop(double d2) {
        this.scrollTop = d2;
        this.__set |= 0x20;
        return (B)this;
    }

    public B wrapText(boolean bl) {
        this.wrapText = bl;
        this.__set |= 0x40;
        return (B)this;
    }

    @Override
    public TextArea build() {
        TextArea textArea = new TextArea();
        this.applyTo(textArea);
        return textArea;
    }
}

