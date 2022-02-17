/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.sg.prism.NGAmbientLight;
import com.sun.javafx.sg.prism.NGLightBase;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGPhongMaterial;
import com.sun.javafx.sg.prism.NGPointLight;
import com.sun.javafx.sg.prism.NGTriangleMesh;
import com.sun.prism.Graphics;
import com.sun.prism.Material;
import com.sun.prism.MeshView;
import com.sun.prism.PrinterGraphics;
import com.sun.prism.ResourceFactory;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;

public abstract class NGShape3D
extends NGNode {
    private NGPhongMaterial material;
    private DrawMode drawMode;
    private CullFace cullFace;
    private boolean materialDirty = false;
    private boolean drawModeDirty = false;
    NGTriangleMesh mesh;
    private MeshView meshView;

    public void setMaterial(NGPhongMaterial nGPhongMaterial) {
        this.material = nGPhongMaterial;
        this.materialDirty = true;
        this.visualsChanged();
    }

    public void setDrawMode(Object object) {
        this.drawMode = (DrawMode)((Object)object);
        this.drawModeDirty = true;
        this.visualsChanged();
    }

    public void setCullFace(Object object) {
        this.cullFace = (CullFace)((Object)object);
        this.visualsChanged();
    }

    void invalidate() {
        this.meshView = null;
        this.visualsChanged();
    }

    private void renderMeshView(Graphics graphics) {
        graphics.setup3DRendering();
        ResourceFactory resourceFactory = graphics.getResourceFactory();
        if (this.meshView == null && this.mesh != null) {
            this.meshView = resourceFactory.createMeshView(this.mesh.createMesh(resourceFactory));
            this.drawModeDirty = true;
            this.materialDirty = true;
        }
        if (this.meshView == null || !this.mesh.validate()) {
            return;
        }
        Material material = this.material.createMaterial(resourceFactory);
        if (this.materialDirty) {
            this.meshView.setMaterial(material);
            this.materialDirty = false;
        }
        int n2 = this.cullFace.ordinal();
        if (this.cullFace.ordinal() != MeshView.CULL_NONE && graphics.getTransformNoClone().getDeterminant() < 0.0) {
            n2 = n2 == MeshView.CULL_BACK ? MeshView.CULL_FRONT : MeshView.CULL_BACK;
        }
        this.meshView.setCullingMode(n2);
        if (this.drawModeDirty) {
            this.meshView.setWireframe(this.drawMode == DrawMode.LINE);
            this.drawModeDirty = false;
        }
        int n3 = 0;
        if (graphics.getLights() == null || graphics.getLights()[0] == null) {
            this.meshView.setAmbientLight(0.0f, 0.0f, 0.0f);
            Vec3d vec3d = graphics.getCameraNoClone().getPositionInWorld(null);
            this.meshView.setPointLight(n3++, (float)vec3d.x, (float)vec3d.y, (float)vec3d.z, 1.0f, 1.0f, 1.0f, 1.0f);
        } else {
            NGLightBase nGLightBase;
            float f2 = 0.0f;
            float f3 = 0.0f;
            float f4 = 0.0f;
            for (int i2 = 0; i2 < graphics.getLights().length && (nGLightBase = graphics.getLights()[i2]) != null; ++i2) {
                if (!nGLightBase.affects(this)) continue;
                float f5 = nGLightBase.getColor().getRed();
                float f6 = nGLightBase.getColor().getGreen();
                float f7 = nGLightBase.getColor().getBlue();
                if (nGLightBase instanceof NGPointLight) {
                    NGPointLight nGPointLight = (NGPointLight)nGLightBase;
                    if (f5 == 0.0f && f6 == 0.0f && f7 == 0.0f) continue;
                    Affine3D affine3D = nGPointLight.getWorldTransform();
                    this.meshView.setPointLight(n3++, (float)affine3D.getMxt(), (float)affine3D.getMyt(), (float)affine3D.getMzt(), f5, f6, f7, 1.0f);
                    continue;
                }
                if (!(nGLightBase instanceof NGAmbientLight)) continue;
                f2 += f5;
                f4 += f6;
                f3 += f7;
            }
            f2 = NGShape3D.saturate(f2);
            f4 = NGShape3D.saturate(f4);
            f3 = NGShape3D.saturate(f3);
            this.meshView.setAmbientLight(f2, f4, f3);
        }
        while (n3 < 3) {
            this.meshView.setPointLight(n3++, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
        }
        this.meshView.render(graphics);
    }

    private static float saturate(float f2) {
        return f2 < 1.0f ? (f2 < 0.0f ? 0.0f : f2) : 1.0f;
    }

    public void setMesh(NGTriangleMesh nGTriangleMesh) {
        this.mesh = nGTriangleMesh;
        this.meshView = null;
        this.visualsChanged();
    }

    @Override
    protected void renderContent(Graphics graphics) {
        if (!Platform.isSupported(ConditionalFeature.SCENE3D) || this.material == null || graphics instanceof PrinterGraphics) {
            return;
        }
        this.renderMeshView(graphics);
    }

    @Override
    boolean isShape3D() {
        return true;
    }

    @Override
    protected boolean hasOverlappingContents() {
        return false;
    }

    @Override
    public void release() {
    }
}

