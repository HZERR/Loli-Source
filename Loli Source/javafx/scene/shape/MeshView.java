/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.shape;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.scene.input.PickResultChooser;
import com.sun.javafx.sg.prism.NGMeshView;
import com.sun.javafx.sg.prism.NGNode;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.scene.shape.Mesh;
import javafx.scene.shape.Shape3D;

public class MeshView
extends Shape3D {
    private ObjectProperty<Mesh> mesh;

    public MeshView() {
    }

    public MeshView(Mesh mesh) {
        this.setMesh(mesh);
    }

    public final void setMesh(Mesh mesh) {
        this.meshProperty().set(mesh);
    }

    public final Mesh getMesh() {
        return this.mesh == null ? null : (Mesh)this.mesh.get();
    }

    public final ObjectProperty<Mesh> meshProperty() {
        if (this.mesh == null) {
            this.mesh = new SimpleObjectProperty<Mesh>((Object)this, "mesh"){
                private Mesh old;
                private final ChangeListener<Boolean> meshChangeListener;
                private final WeakChangeListener<Boolean> weakMeshChangeListener;
                {
                    this.old = null;
                    this.meshChangeListener = (observableValue, bl, bl2) -> {
                        if (bl2.booleanValue()) {
                            MeshView.this.impl_markDirty(DirtyBits.MESH_GEOM);
                            MeshView.this.impl_geomChanged();
                        }
                    };
                    this.weakMeshChangeListener = new WeakChangeListener<Boolean>(this.meshChangeListener);
                }

                @Override
                protected void invalidated() {
                    Mesh mesh;
                    if (this.old != null) {
                        this.old.dirtyProperty().removeListener(this.weakMeshChangeListener);
                    }
                    if ((mesh = (Mesh)this.get()) != null) {
                        mesh.dirtyProperty().addListener(this.weakMeshChangeListener);
                    }
                    MeshView.this.impl_markDirty(DirtyBits.MESH);
                    MeshView.this.impl_markDirty(DirtyBits.MESH_GEOM);
                    MeshView.this.impl_geomChanged();
                    this.old = mesh;
                }
            };
        }
        return this.mesh;
    }

    @Override
    @Deprecated
    public void impl_updatePeer() {
        super.impl_updatePeer();
        NGMeshView nGMeshView = (NGMeshView)this.impl_getPeer();
        if (this.impl_isDirty(DirtyBits.MESH_GEOM) && this.getMesh() != null) {
            this.getMesh().impl_updatePG();
        }
        if (this.impl_isDirty(DirtyBits.MESH)) {
            nGMeshView.setMesh(this.getMesh() == null ? null : this.getMesh().getPGMesh());
        }
    }

    @Override
    @Deprecated
    protected NGNode impl_createPeer() {
        return new NGMeshView();
    }

    @Override
    @Deprecated
    public BaseBounds impl_computeGeomBounds(BaseBounds baseBounds, BaseTransform baseTransform) {
        if (this.getMesh() != null) {
            baseBounds = this.getMesh().computeBounds(baseBounds);
            baseBounds = baseTransform.transform(baseBounds, baseBounds);
        } else {
            baseBounds.makeEmpty();
        }
        return baseBounds;
    }

    @Override
    @Deprecated
    protected boolean impl_computeContains(double d2, double d3) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    @Deprecated
    protected boolean impl_computeIntersects(PickRay pickRay, PickResultChooser pickResultChooser) {
        return this.getMesh().impl_computeIntersects(pickRay, pickResultChooser, this, this.getCullFace(), true);
    }
}

