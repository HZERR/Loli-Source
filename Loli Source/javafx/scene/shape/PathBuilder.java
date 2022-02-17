/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.shape;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.ShapeBuilder;
import javafx.util.Builder;

@Deprecated
public class PathBuilder<B extends PathBuilder<B>>
extends ShapeBuilder<B>
implements Builder<Path> {
    private int __set;
    private Collection<? extends PathElement> elements;
    private FillRule fillRule;

    protected PathBuilder() {
    }

    public static PathBuilder<?> create() {
        return new PathBuilder();
    }

    public void applyTo(Path path) {
        super.applyTo(path);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            path.getElements().addAll(this.elements);
        }
        if ((n2 & 2) != 0) {
            path.setFillRule(this.fillRule);
        }
    }

    public B elements(Collection<? extends PathElement> collection) {
        this.elements = collection;
        this.__set |= 1;
        return (B)this;
    }

    public B elements(PathElement ... arrpathElement) {
        return this.elements(Arrays.asList(arrpathElement));
    }

    public B fillRule(FillRule fillRule) {
        this.fillRule = fillRule;
        this.__set |= 2;
        return (B)this;
    }

    @Override
    public Path build() {
        Path path = new Path();
        this.applyTo(path);
        return path;
    }
}

