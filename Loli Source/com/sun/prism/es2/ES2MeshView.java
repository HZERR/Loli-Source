/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.es2;

import com.sun.prism.Graphics;
import com.sun.prism.Material;
import com.sun.prism.es2.ES2Context;
import com.sun.prism.es2.ES2Light;
import com.sun.prism.es2.ES2Mesh;
import com.sun.prism.es2.ES2PhongMaterial;
import com.sun.prism.impl.BaseMeshView;
import com.sun.prism.impl.Disposer;

class ES2MeshView
extends BaseMeshView {
    static int count = 0;
    private final ES2Context context;
    private final long nativeHandle;
    private float ambientLightRed = 0.0f;
    private float ambientLightBlue = 0.0f;
    private float ambientLightGreen = 0.0f;
    private ES2Light[] lights = new ES2Light[3];
    private final ES2Mesh mesh;
    private ES2PhongMaterial material;

    private ES2MeshView(ES2Context eS2Context, long l2, ES2Mesh eS2Mesh, Disposer.Record record) {
        super(record);
        this.context = eS2Context;
        this.mesh = eS2Mesh;
        this.nativeHandle = l2;
        ++count;
    }

    static ES2MeshView create(ES2Context eS2Context, ES2Mesh eS2Mesh) {
        long l2 = eS2Context.createES2MeshView(eS2Mesh);
        return new ES2MeshView(eS2Context, l2, eS2Mesh, new ES2MeshViewDisposerRecord(eS2Context, l2));
    }

    @Override
    public void setCullingMode(int n2) {
        this.context.setCullingMode(this.nativeHandle, n2);
    }

    @Override
    public void setMaterial(Material material) {
        this.context.setMaterial(this.nativeHandle, material);
        this.material = (ES2PhongMaterial)material;
    }

    @Override
    public void setWireframe(boolean bl) {
        this.context.setWireframe(this.nativeHandle, bl);
    }

    @Override
    public void setAmbientLight(float f2, float f3, float f4) {
        this.ambientLightRed = f2;
        this.ambientLightGreen = f3;
        this.ambientLightBlue = f4;
        this.context.setAmbientLight(this.nativeHandle, f2, f3, f4);
    }

    float getAmbientLightRed() {
        return this.ambientLightRed;
    }

    float getAmbientLightGreen() {
        return this.ambientLightGreen;
    }

    float getAmbientLightBlue() {
        return this.ambientLightBlue;
    }

    @Override
    public void setPointLight(int n2, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        if (n2 >= 0 && n2 <= 2) {
            this.lights[n2] = new ES2Light(f2, f3, f4, f5, f6, f7, f8);
            this.context.setPointLight(this.nativeHandle, n2, f2, f3, f4, f5, f6, f7, f8);
        }
    }

    ES2Light[] getPointLights() {
        return this.lights;
    }

    @Override
    public void render(Graphics graphics) {
        this.material.lockTextureMaps();
        this.context.renderMeshView(this.nativeHandle, graphics, this);
        this.material.unlockTextureMaps();
    }

    ES2PhongMaterial getMaterial() {
        return this.material;
    }

    @Override
    public void dispose() {
        this.material = null;
        this.lights = null;
        this.disposerRecord.dispose();
        --count;
    }

    public int getCount() {
        return count;
    }

    static class ES2MeshViewDisposerRecord
    implements Disposer.Record {
        private final ES2Context context;
        private long nativeHandle;

        ES2MeshViewDisposerRecord(ES2Context eS2Context, long l2) {
            this.context = eS2Context;
            this.nativeHandle = l2;
        }

        void traceDispose() {
        }

        @Override
        public void dispose() {
            if (this.nativeHandle != 0L) {
                this.traceDispose();
                this.context.releaseES2MeshView(this.nativeHandle);
                this.nativeHandle = 0L;
            }
        }
    }
}

