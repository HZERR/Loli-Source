/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene;

import javafx.scene.PerspectiveCamera;
import javafx.util.Builder;

@Deprecated
public class PerspectiveCameraBuilder<B extends PerspectiveCameraBuilder<B>>
implements Builder<PerspectiveCamera> {
    private boolean __set;
    private double fieldOfView;

    protected PerspectiveCameraBuilder() {
    }

    public static PerspectiveCameraBuilder<?> create() {
        return new PerspectiveCameraBuilder();
    }

    public void applyTo(PerspectiveCamera perspectiveCamera) {
        if (this.__set) {
            perspectiveCamera.setFieldOfView(this.fieldOfView);
        }
    }

    public B fieldOfView(double d2) {
        this.fieldOfView = d2;
        this.__set = true;
        return (B)this;
    }

    @Override
    public PerspectiveCamera build() {
        PerspectiveCamera perspectiveCamera = new PerspectiveCamera();
        this.applyTo(perspectiveCamera);
        return perspectiveCamera;
    }
}

