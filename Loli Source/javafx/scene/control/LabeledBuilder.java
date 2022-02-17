/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ControlBuilder;
import javafx.scene.control.Labeled;
import javafx.scene.control.OverrunStyle;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

@Deprecated
public abstract class LabeledBuilder<B extends LabeledBuilder<B>>
extends ControlBuilder<B> {
    private int __set;
    private Pos alignment;
    private ContentDisplay contentDisplay;
    private String ellipsisString;
    private Font font;
    private Node graphic;
    private double graphicTextGap;
    private boolean mnemonicParsing;
    private String text;
    private TextAlignment textAlignment;
    private Paint textFill;
    private OverrunStyle textOverrun;
    private boolean underline;
    private boolean wrapText;

    protected LabeledBuilder() {
    }

    private void __set(int n2) {
        this.__set |= 1 << n2;
    }

    public void applyTo(Labeled labeled) {
        super.applyTo(labeled);
        int n2 = this.__set;
        while (n2 != 0) {
            int n3 = Integer.numberOfTrailingZeros(n2);
            n2 &= ~(1 << n3);
            switch (n3) {
                case 0: {
                    labeled.setAlignment(this.alignment);
                    break;
                }
                case 1: {
                    labeled.setContentDisplay(this.contentDisplay);
                    break;
                }
                case 2: {
                    labeled.setEllipsisString(this.ellipsisString);
                    break;
                }
                case 3: {
                    labeled.setFont(this.font);
                    break;
                }
                case 4: {
                    labeled.setGraphic(this.graphic);
                    break;
                }
                case 5: {
                    labeled.setGraphicTextGap(this.graphicTextGap);
                    break;
                }
                case 6: {
                    labeled.setMnemonicParsing(this.mnemonicParsing);
                    break;
                }
                case 7: {
                    labeled.setText(this.text);
                    break;
                }
                case 8: {
                    labeled.setTextAlignment(this.textAlignment);
                    break;
                }
                case 9: {
                    labeled.setTextFill(this.textFill);
                    break;
                }
                case 10: {
                    labeled.setTextOverrun(this.textOverrun);
                    break;
                }
                case 11: {
                    labeled.setUnderline(this.underline);
                    break;
                }
                case 12: {
                    labeled.setWrapText(this.wrapText);
                }
            }
        }
    }

    public B alignment(Pos pos) {
        this.alignment = pos;
        this.__set(0);
        return (B)this;
    }

    public B contentDisplay(ContentDisplay contentDisplay) {
        this.contentDisplay = contentDisplay;
        this.__set(1);
        return (B)this;
    }

    public B ellipsisString(String string) {
        this.ellipsisString = string;
        this.__set(2);
        return (B)this;
    }

    public B font(Font font) {
        this.font = font;
        this.__set(3);
        return (B)this;
    }

    public B graphic(Node node) {
        this.graphic = node;
        this.__set(4);
        return (B)this;
    }

    public B graphicTextGap(double d2) {
        this.graphicTextGap = d2;
        this.__set(5);
        return (B)this;
    }

    public B mnemonicParsing(boolean bl) {
        this.mnemonicParsing = bl;
        this.__set(6);
        return (B)this;
    }

    public B text(String string) {
        this.text = string;
        this.__set(7);
        return (B)this;
    }

    public B textAlignment(TextAlignment textAlignment) {
        this.textAlignment = textAlignment;
        this.__set(8);
        return (B)this;
    }

    public B textFill(Paint paint) {
        this.textFill = paint;
        this.__set(9);
        return (B)this;
    }

    public B textOverrun(OverrunStyle overrunStyle) {
        this.textOverrun = overrunStyle;
        this.__set(10);
        return (B)this;
    }

    public B underline(boolean bl) {
        this.underline = bl;
        this.__set(11);
        return (B)this;
    }

    public B wrapText(boolean bl) {
        this.wrapText = bl;
        this.__set(12);
        return (B)this;
    }
}

