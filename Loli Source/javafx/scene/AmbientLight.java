/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene;

import com.sun.javafx.sg.prism.NGAmbientLight;
import com.sun.javafx.sg.prism.NGNode;
import javafx.scene.LightBase;
import javafx.scene.paint.Color;

public class AmbientLight
extends LightBase {
    public AmbientLight() {
    }

    public AmbientLight(Color color) {
        super(color);
    }

    @Override
    @Deprecated
    protected NGNode impl_createPeer() {
        return new NGAmbientLight();
    }
}

