/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene;

import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGPointLight;
import javafx.scene.LightBase;
import javafx.scene.paint.Color;

public class PointLight
extends LightBase {
    public PointLight() {
    }

    public PointLight(Color color) {
        super(color);
    }

    @Override
    @Deprecated
    protected NGNode impl_createPeer() {
        return new NGPointLight();
    }
}

