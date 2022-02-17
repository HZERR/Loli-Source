/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.es2;

import com.sun.prism.es2.ES2Context;
import com.sun.prism.impl.BaseMesh;
import com.sun.prism.impl.Disposer;

class ES2Mesh
extends BaseMesh {
    static int count = 0;
    private final ES2Context context;
    private final long nativeHandle;

    private ES2Mesh(ES2Context eS2Context, long l2, Disposer.Record record) {
        super(record);
        this.context = eS2Context;
        this.nativeHandle = l2;
        ++count;
    }

    static ES2Mesh create(ES2Context eS2Context) {
        long l2 = eS2Context.createES2Mesh();
        return new ES2Mesh(eS2Context, l2, new ES2MeshDisposerRecord(eS2Context, l2));
    }

    long getNativeHandle() {
        return this.nativeHandle;
    }

    @Override
    public void dispose() {
        this.disposerRecord.dispose();
        --count;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public boolean buildNativeGeometry(float[] arrf, int n2, int[] arrn, int n3) {
        return this.context.buildNativeGeometry(this.nativeHandle, arrf, n2, arrn, n3);
    }

    @Override
    public boolean buildNativeGeometry(float[] arrf, int n2, short[] arrs, int n3) {
        return this.context.buildNativeGeometry(this.nativeHandle, arrf, n2, arrs, n3);
    }

    static class ES2MeshDisposerRecord
    implements Disposer.Record {
        private final ES2Context context;
        private long nativeHandle;

        ES2MeshDisposerRecord(ES2Context eS2Context, long l2) {
            this.context = eS2Context;
            this.nativeHandle = l2;
        }

        void traceDispose() {
        }

        @Override
        public void dispose() {
            if (this.nativeHandle != 0L) {
                this.traceDispose();
                this.context.releaseES2Mesh(this.nativeHandle);
                this.nativeHandle = 0L;
            }
        }
    }
}

