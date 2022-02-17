/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.PopupControlBuilder;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

@Deprecated
public class TooltipBuilder<B extends TooltipBuilder<B>>
extends PopupControlBuilder<B> {
    private int __set;
    private ContentDisplay contentDisplay;
    private Font font;
    private Node graphic;
    private double graphicTextGap;
    private String text;
    private TextAlignment textAlignment;
    private OverrunStyle textOverrun;
    private boolean wrapText;

    protected TooltipBuilder() {
    }

    public static TooltipBuilder<?> create() {
        return new TooltipBuilder();
    }

    private void __set(int n2) {
        this.__set |= 1 << n2;
    }

    public void applyTo(Tooltip tooltip) {
        super.applyTo(tooltip);
        int n2 = this.__set;
        while (n2 != 0) {
            int n3 = Integer.numberOfTrailingZeros(n2);
            n2 &= ~(1 << n3);
            switch (n3) {
                case 0: {
                    tooltip.setContentDisplay(this.contentDisplay);
                    break;
                }
                case 1: {
                    tooltip.setFont(this.font);
                    break;
                }
                case 2: {
                    tooltip.setGraphic(this.graphic);
                    break;
                }
                case 3: {
                    tooltip.setGraphicTextGap(this.graphicTextGap);
                    break;
                }
                case 4: {
                    tooltip.setText(this.text);
                    break;
                }
                case 5: {
                    tooltip.setTextAlignment(this.textAlignment);
                    break;
                }
                case 6: {
                    tooltip.setTextOverrun(this.textOverrun);
                    break;
                }
                case 7: {
                    tooltip.setWrapText(this.wrapText);
                }
            }
        }
    }

    public B contentDisplay(ContentDisplay contentDisplay) {
        this.contentDisplay = contentDisplay;
        this.__set(0);
        return (B)this;
    }

    public B font(Font font) {
        this.font = font;
        this.__set(1);
        return (B)this;
    }

    public B graphic(Node node) {
        this.graphic = node;
        this.__set(2);
        return (B)this;
    }

    public B graphicTextGap(double d2) {
        this.graphicTextGap = d2;
        this.__set(3);
        return (B)this;
    }

    public B text(String string) {
        this.text = string;
        this.__set(4);
        return (B)this;
    }

    public B textAlignment(TextAlignment textAlignment) {
        this.textAlignment = textAlignment;
        this.__set(5);
        return (B)this;
    }

    public B textOverrun(OverrunStyle overrunStyle) {
        this.textOverrun = overrunStyle;
        this.__set(6);
        return (B)this;
    }

    public B wrapText(boolean bl) {
        this.wrapText = bl;
        this.__set(7);
        return (B)this;
    }

    @Override
    public Tooltip build() {
        Tooltip tooltip = new Tooltip();
        this.applyTo(tooltip);
        return tooltip;
    }
}

