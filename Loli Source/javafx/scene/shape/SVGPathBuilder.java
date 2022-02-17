/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.shape;

import javafx.scene.shape.FillRule;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.ShapeBuilder;
import javafx.util.Builder;

@Deprecated
public class SVGPathBuilder<B extends SVGPathBuilder<B>>
extends ShapeBuilder<B>
implements Builder<SVGPath> {
    private int __set;
    private String content;
    private FillRule fillRule;

    protected SVGPathBuilder() {
    }

    public static SVGPathBuilder<?> create() {
        return new SVGPathBuilder();
    }

    public void applyTo(SVGPath sVGPath) {
        super.applyTo(sVGPath);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            sVGPath.setContent(this.content);
        }
        if ((n2 & 2) != 0) {
            sVGPath.setFillRule(this.fillRule);
        }
    }

    public B content(String string) {
        this.content = string;
        this.__set |= 1;
        return (B)this;
    }

    public B fillRule(FillRule fillRule) {
        this.fillRule = fillRule;
        this.__set |= 2;
        return (B)this;
    }

    @Override
    public SVGPath build() {
        SVGPath sVGPath = new SVGPath();
        this.applyTo(sVGPath);
        return sVGPath;
    }
}

