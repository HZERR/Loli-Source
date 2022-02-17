/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene;

import javafx.geometry.Rectangle2D;
import javafx.scene.Camera;
import javafx.scene.SnapshotParameters;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Transform;
import javafx.util.Builder;

@Deprecated
public class SnapshotParametersBuilder<B extends SnapshotParametersBuilder<B>>
implements Builder<SnapshotParameters> {
    private int __set;
    private Camera camera;
    private boolean depthBuffer;
    private Paint fill;
    private Transform transform;
    private Rectangle2D viewport;

    protected SnapshotParametersBuilder() {
    }

    public static SnapshotParametersBuilder<?> create() {
        return new SnapshotParametersBuilder();
    }

    public void applyTo(SnapshotParameters snapshotParameters) {
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            snapshotParameters.setCamera(this.camera);
        }
        if ((n2 & 2) != 0) {
            snapshotParameters.setDepthBuffer(this.depthBuffer);
        }
        if ((n2 & 4) != 0) {
            snapshotParameters.setFill(this.fill);
        }
        if ((n2 & 8) != 0) {
            snapshotParameters.setTransform(this.transform);
        }
        if ((n2 & 0x10) != 0) {
            snapshotParameters.setViewport(this.viewport);
        }
    }

    public B camera(Camera camera) {
        this.camera = camera;
        this.__set |= 1;
        return (B)this;
    }

    public B depthBuffer(boolean bl) {
        this.depthBuffer = bl;
        this.__set |= 2;
        return (B)this;
    }

    public B fill(Paint paint) {
        this.fill = paint;
        this.__set |= 4;
        return (B)this;
    }

    public B transform(Transform transform) {
        this.transform = transform;
        this.__set |= 8;
        return (B)this;
    }

    public B viewport(Rectangle2D rectangle2D) {
        this.viewport = rectangle2D;
        this.__set |= 0x10;
        return (B)this;
    }

    @Override
    public SnapshotParameters build() {
        SnapshotParameters snapshotParameters = new SnapshotParameters();
        this.applyTo(snapshotParameters);
        return snapshotParameters;
    }
}

