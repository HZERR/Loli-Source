/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene;

import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.GeneralTransform3D;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGParallelCamera;
import javafx.scene.Camera;

public class ParallelCamera
extends Camera {
    @Override
    Camera copy() {
        ParallelCamera parallelCamera = new ParallelCamera();
        parallelCamera.setNearClip(this.getNearClip());
        parallelCamera.setFarClip(this.getFarClip());
        return parallelCamera;
    }

    @Override
    @Deprecated
    protected NGNode impl_createPeer() {
        NGParallelCamera nGParallelCamera = new NGParallelCamera();
        nGParallelCamera.setNearClip((float)this.getNearClip());
        nGParallelCamera.setFarClip((float)this.getFarClip());
        return nGParallelCamera;
    }

    @Override
    final PickRay computePickRay(double d2, double d3, PickRay pickRay) {
        return PickRay.computeParallelPickRay(d2, d3, this.getViewHeight(), this.getCameraTransform(), this.getNearClip(), this.getFarClip(), pickRay);
    }

    @Override
    void computeProjectionTransform(GeneralTransform3D generalTransform3D) {
        double d2;
        double d3 = this.getViewWidth();
        double d4 = d3 > (d2 = this.getViewHeight()) ? d3 / 2.0 : d2 / 2.0;
        generalTransform3D.ortho(0.0, d3, d2, 0.0, -d4, d4);
    }

    @Override
    void computeViewTransform(Affine3D affine3D) {
        affine3D.setToIdentity();
    }

    @Override
    Vec3d computePosition(Vec3d vec3d) {
        if (vec3d == null) {
            vec3d = new Vec3d();
        }
        double d2 = this.getViewWidth() / 2.0;
        double d3 = this.getViewHeight() / 2.0;
        double d4 = d3 / Math.tan(Math.toRadians(15.0));
        vec3d.set(d2, d3, -d4);
        return vec3d;
    }
}

