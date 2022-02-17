/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.ControlBuilder;
import javafx.scene.control.ScrollPane;
import javafx.util.Builder;

@Deprecated
public class ScrollPaneBuilder<B extends ScrollPaneBuilder<B>>
extends ControlBuilder<B>
implements Builder<ScrollPane> {
    private int __set;
    private Node content;
    private boolean fitToHeight;
    private boolean fitToWidth;
    private ScrollPane.ScrollBarPolicy hbarPolicy;
    private double hmax;
    private double hmin;
    private double hvalue;
    private boolean pannable;
    private double prefViewportHeight;
    private double prefViewportWidth;
    private ScrollPane.ScrollBarPolicy vbarPolicy;
    private Bounds viewportBounds;
    private double vmax;
    private double vmin;
    private double vvalue;

    protected ScrollPaneBuilder() {
    }

    public static ScrollPaneBuilder<?> create() {
        return new ScrollPaneBuilder();
    }

    private void __set(int n2) {
        this.__set |= 1 << n2;
    }

    public void applyTo(ScrollPane scrollPane) {
        super.applyTo(scrollPane);
        int n2 = this.__set;
        while (n2 != 0) {
            int n3 = Integer.numberOfTrailingZeros(n2);
            n2 &= ~(1 << n3);
            switch (n3) {
                case 0: {
                    scrollPane.setContent(this.content);
                    break;
                }
                case 1: {
                    scrollPane.setFitToHeight(this.fitToHeight);
                    break;
                }
                case 2: {
                    scrollPane.setFitToWidth(this.fitToWidth);
                    break;
                }
                case 3: {
                    scrollPane.setHbarPolicy(this.hbarPolicy);
                    break;
                }
                case 4: {
                    scrollPane.setHmax(this.hmax);
                    break;
                }
                case 5: {
                    scrollPane.setHmin(this.hmin);
                    break;
                }
                case 6: {
                    scrollPane.setHvalue(this.hvalue);
                    break;
                }
                case 7: {
                    scrollPane.setPannable(this.pannable);
                    break;
                }
                case 8: {
                    scrollPane.setPrefViewportHeight(this.prefViewportHeight);
                    break;
                }
                case 9: {
                    scrollPane.setPrefViewportWidth(this.prefViewportWidth);
                    break;
                }
                case 10: {
                    scrollPane.setVbarPolicy(this.vbarPolicy);
                    break;
                }
                case 11: {
                    scrollPane.setViewportBounds(this.viewportBounds);
                    break;
                }
                case 12: {
                    scrollPane.setVmax(this.vmax);
                    break;
                }
                case 13: {
                    scrollPane.setVmin(this.vmin);
                    break;
                }
                case 14: {
                    scrollPane.setVvalue(this.vvalue);
                }
            }
        }
    }

    public B content(Node node) {
        this.content = node;
        this.__set(0);
        return (B)this;
    }

    public B fitToHeight(boolean bl) {
        this.fitToHeight = bl;
        this.__set(1);
        return (B)this;
    }

    public B fitToWidth(boolean bl) {
        this.fitToWidth = bl;
        this.__set(2);
        return (B)this;
    }

    public B hbarPolicy(ScrollPane.ScrollBarPolicy scrollBarPolicy) {
        this.hbarPolicy = scrollBarPolicy;
        this.__set(3);
        return (B)this;
    }

    public B hmax(double d2) {
        this.hmax = d2;
        this.__set(4);
        return (B)this;
    }

    public B hmin(double d2) {
        this.hmin = d2;
        this.__set(5);
        return (B)this;
    }

    public B hvalue(double d2) {
        this.hvalue = d2;
        this.__set(6);
        return (B)this;
    }

    public B pannable(boolean bl) {
        this.pannable = bl;
        this.__set(7);
        return (B)this;
    }

    public B prefViewportHeight(double d2) {
        this.prefViewportHeight = d2;
        this.__set(8);
        return (B)this;
    }

    public B prefViewportWidth(double d2) {
        this.prefViewportWidth = d2;
        this.__set(9);
        return (B)this;
    }

    public B vbarPolicy(ScrollPane.ScrollBarPolicy scrollBarPolicy) {
        this.vbarPolicy = scrollBarPolicy;
        this.__set(10);
        return (B)this;
    }

    public B viewportBounds(Bounds bounds) {
        this.viewportBounds = bounds;
        this.__set(11);
        return (B)this;
    }

    public B vmax(double d2) {
        this.vmax = d2;
        this.__set(12);
        return (B)this;
    }

    public B vmin(double d2) {
        this.vmin = d2;
        this.__set(13);
        return (B)this;
    }

    public B vvalue(double d2) {
        this.vvalue = d2;
        this.__set(14);
        return (B)this;
    }

    @Override
    public ScrollPane build() {
        ScrollPane scrollPane = new ScrollPane();
        this.applyTo(scrollPane);
        return scrollPane;
    }
}

