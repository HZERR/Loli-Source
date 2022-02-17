/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.shape;

import com.sun.javafx.geom.Path2D;
import com.sun.javafx.sg.prism.NGPath;
import javafx.scene.shape.PathElement;

public class ClosePath
extends PathElement {
    @Override
    void addTo(NGPath nGPath) {
        nGPath.addClosePath();
    }

    @Override
    @Deprecated
    public void impl_addTo(Path2D path2D) {
        path2D.closePath();
    }

    public String toString() {
        return "ClosePath";
    }
}

