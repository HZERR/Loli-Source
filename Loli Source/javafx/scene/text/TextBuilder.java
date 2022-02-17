/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.text;

import javafx.geometry.VPos;
import javafx.scene.shape.ShapeBuilder;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBoundsType;
import javafx.util.Builder;

@Deprecated
public class TextBuilder<B extends TextBuilder<B>>
extends ShapeBuilder<B>
implements Builder<Text> {
    private int __set;
    private TextBoundsType boundsType;
    private Font font;
    private FontSmoothingType fontSmoothingType;
    private boolean impl_caretBias;
    private int impl_caretPosition;
    private int impl_selectionEnd;
    private int impl_selectionStart;
    private boolean strikethrough;
    private String text;
    private TextAlignment textAlignment;
    private VPos textOrigin;
    private boolean underline;
    private double wrappingWidth;
    private double x;
    private double y;

    protected TextBuilder() {
    }

    public static TextBuilder<?> create() {
        return new TextBuilder();
    }

    private void __set(int n2) {
        this.__set |= 1 << n2;
    }

    public void applyTo(Text text) {
        super.applyTo(text);
        int n2 = this.__set;
        while (n2 != 0) {
            int n3 = Integer.numberOfTrailingZeros(n2);
            n2 &= ~(1 << n3);
            switch (n3) {
                case 0: {
                    text.setBoundsType(this.boundsType);
                    break;
                }
                case 1: {
                    text.setFont(this.font);
                    break;
                }
                case 2: {
                    text.setFontSmoothingType(this.fontSmoothingType);
                    break;
                }
                case 3: {
                    text.setImpl_caretBias(this.impl_caretBias);
                    break;
                }
                case 4: {
                    text.setImpl_caretPosition(this.impl_caretPosition);
                    break;
                }
                case 6: {
                    text.setImpl_selectionEnd(this.impl_selectionEnd);
                    break;
                }
                case 8: {
                    text.setImpl_selectionStart(this.impl_selectionStart);
                    break;
                }
                case 9: {
                    text.setStrikethrough(this.strikethrough);
                    break;
                }
                case 10: {
                    text.setText(this.text);
                    break;
                }
                case 11: {
                    text.setTextAlignment(this.textAlignment);
                    break;
                }
                case 12: {
                    text.setTextOrigin(this.textOrigin);
                    break;
                }
                case 13: {
                    text.setUnderline(this.underline);
                    break;
                }
                case 14: {
                    text.setWrappingWidth(this.wrappingWidth);
                    break;
                }
                case 15: {
                    text.setX(this.x);
                    break;
                }
                case 16: {
                    text.setY(this.y);
                }
            }
        }
    }

    public B boundsType(TextBoundsType textBoundsType) {
        this.boundsType = textBoundsType;
        this.__set(0);
        return (B)this;
    }

    public B font(Font font) {
        this.font = font;
        this.__set(1);
        return (B)this;
    }

    public B fontSmoothingType(FontSmoothingType fontSmoothingType) {
        this.fontSmoothingType = fontSmoothingType;
        this.__set(2);
        return (B)this;
    }

    @Deprecated
    public B impl_caretBias(boolean bl) {
        this.impl_caretBias = bl;
        this.__set(3);
        return (B)this;
    }

    @Deprecated
    public B impl_caretPosition(int n2) {
        this.impl_caretPosition = n2;
        this.__set(4);
        return (B)this;
    }

    @Deprecated
    public B impl_selectionEnd(int n2) {
        this.impl_selectionEnd = n2;
        this.__set(6);
        return (B)this;
    }

    @Deprecated
    public B impl_selectionStart(int n2) {
        this.impl_selectionStart = n2;
        this.__set(8);
        return (B)this;
    }

    public B strikethrough(boolean bl) {
        this.strikethrough = bl;
        this.__set(9);
        return (B)this;
    }

    public B text(String string) {
        this.text = string;
        this.__set(10);
        return (B)this;
    }

    public B textAlignment(TextAlignment textAlignment) {
        this.textAlignment = textAlignment;
        this.__set(11);
        return (B)this;
    }

    public B textOrigin(VPos vPos) {
        this.textOrigin = vPos;
        this.__set(12);
        return (B)this;
    }

    public B underline(boolean bl) {
        this.underline = bl;
        this.__set(13);
        return (B)this;
    }

    public B wrappingWidth(double d2) {
        this.wrappingWidth = d2;
        this.__set(14);
        return (B)this;
    }

    public B x(double d2) {
        this.x = d2;
        this.__set(15);
        return (B)this;
    }

    public B y(double d2) {
        this.y = d2;
        this.__set(16);
        return (B)this;
    }

    @Override
    public Text build() {
        Text text = new Text();
        this.applyTo(text);
        return text;
    }
}

