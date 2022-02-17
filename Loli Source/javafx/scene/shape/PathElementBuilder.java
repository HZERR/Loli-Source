/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.shape;

import javafx.scene.shape.PathElement;

@Deprecated
public abstract class PathElementBuilder<B extends PathElementBuilder<B>> {
    private boolean __set;
    private boolean absolute;

    protected PathElementBuilder() {
    }

    public void applyTo(PathElement pathElement) {
        if (this.__set) {
            pathElement.setAbsolute(this.absolute);
        }
    }

    public B absolute(boolean bl) {
        this.absolute = bl;
        this.__set = true;
        return (B)this;
    }
}

