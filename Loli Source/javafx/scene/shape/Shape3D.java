/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.shape;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.BoxBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.sg.prism.NGShape3D;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.scene.Node;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.PredefinedMeshManager;
import sun.util.logging.PlatformLogger;

public abstract class Shape3D
extends Node {
    private static final PhongMaterial DEFAULT_MATERIAL = new PhongMaterial();
    PredefinedMeshManager manager = PredefinedMeshManager.getInstance();
    int key = 0;
    private ObjectProperty<Material> material;
    private ObjectProperty<DrawMode> drawMode;
    private ObjectProperty<CullFace> cullFace;

    protected Shape3D() {
        if (!Platform.isSupported(ConditionalFeature.SCENE3D)) {
            String string = Shape3D.class.getName();
            PlatformLogger.getLogger(string).warning("System can't support ConditionalFeature.SCENE3D");
        }
    }

    public final void setMaterial(Material material) {
        this.materialProperty().set(material);
    }

    public final Material getMaterial() {
        return this.material == null ? null : (Material)this.material.get();
    }

    public final ObjectProperty<Material> materialProperty() {
        if (this.material == null) {
            this.material = new SimpleObjectProperty<Material>((Object)this, "material"){
                private Material old;
                private final ChangeListener<Boolean> materialChangeListener;
                private final WeakChangeListener<Boolean> weakMaterialChangeListener;
                {
                    this.old = null;
                    this.materialChangeListener = (observableValue, bl, bl2) -> {
                        if (bl2.booleanValue()) {
                            Shape3D.this.impl_markDirty(DirtyBits.MATERIAL);
                        }
                    };
                    this.weakMaterialChangeListener = new WeakChangeListener<Boolean>(this.materialChangeListener);
                }

                @Override
                protected void invalidated() {
                    Material material;
                    if (this.old != null) {
                        this.old.impl_dirtyProperty().removeListener(this.weakMaterialChangeListener);
                    }
                    if ((material = (Material)this.get()) != null) {
                        material.impl_dirtyProperty().addListener(this.weakMaterialChangeListener);
                    }
                    Shape3D.this.impl_markDirty(DirtyBits.MATERIAL);
                    Shape3D.this.impl_geomChanged();
                    this.old = material;
                }
            };
        }
        return this.material;
    }

    public final void setDrawMode(DrawMode drawMode) {
        this.drawModeProperty().set(drawMode);
    }

    public final DrawMode getDrawMode() {
        return this.drawMode == null ? DrawMode.FILL : (DrawMode)((Object)this.drawMode.get());
    }

    public final ObjectProperty<DrawMode> drawModeProperty() {
        if (this.drawMode == null) {
            this.drawMode = new SimpleObjectProperty<DrawMode>((Object)this, "drawMode", DrawMode.FILL){

                @Override
                protected void invalidated() {
                    Shape3D.this.impl_markDirty(DirtyBits.NODE_DRAWMODE);
                }
            };
        }
        return this.drawMode;
    }

    public final void setCullFace(CullFace cullFace) {
        this.cullFaceProperty().set(cullFace);
    }

    public final CullFace getCullFace() {
        return this.cullFace == null ? CullFace.BACK : (CullFace)((Object)this.cullFace.get());
    }

    public final ObjectProperty<CullFace> cullFaceProperty() {
        if (this.cullFace == null) {
            this.cullFace = new SimpleObjectProperty<CullFace>((Object)this, "cullFace", CullFace.BACK){

                @Override
                protected void invalidated() {
                    Shape3D.this.impl_markDirty(DirtyBits.NODE_CULLFACE);
                }
            };
        }
        return this.cullFace;
    }

    @Override
    @Deprecated
    public BaseBounds impl_computeGeomBounds(BaseBounds baseBounds, BaseTransform baseTransform) {
        return new BoxBounds(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    }

    @Override
    @Deprecated
    protected boolean impl_computeContains(double d2, double d3) {
        return false;
    }

    @Override
    @Deprecated
    public void impl_updatePeer() {
        super.impl_updatePeer();
        NGShape3D nGShape3D = (NGShape3D)this.impl_getPeer();
        if (this.impl_isDirty(DirtyBits.MATERIAL)) {
            Material material = this.getMaterial() == null ? DEFAULT_MATERIAL : this.getMaterial();
            material.impl_updatePG();
            nGShape3D.setMaterial(material.impl_getNGMaterial());
        }
        if (this.impl_isDirty(DirtyBits.NODE_DRAWMODE)) {
            nGShape3D.setDrawMode((Object)(this.getDrawMode() == null ? DrawMode.FILL : this.getDrawMode()));
        }
        if (this.impl_isDirty(DirtyBits.NODE_CULLFACE)) {
            nGShape3D.setCullFace((Object)(this.getCullFace() == null ? CullFace.BACK : this.getCullFace()));
        }
    }

    @Override
    @Deprecated
    public Object impl_processMXNode(MXNodeAlgorithm mXNodeAlgorithm, MXNodeAlgorithmContext mXNodeAlgorithmContext) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

