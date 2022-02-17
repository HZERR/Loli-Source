/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.effect;

import com.sun.javafx.util.Utils;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.IntegerPropertyBase;
import javafx.beans.property.SimpleBooleanProperty;

public class FloatMap {
    private com.sun.scenario.effect.FloatMap map;
    private float[] buf;
    private boolean mapBufferDirty = true;
    private BooleanProperty effectDirty;
    private IntegerProperty width;
    private IntegerProperty height;

    com.sun.scenario.effect.FloatMap getImpl() {
        return this.map;
    }

    private void updateBuffer() {
        if (this.getWidth() > 0 && this.getHeight() > 0) {
            int n2 = Utils.clampMax(this.getWidth(), 4096);
            int n3 = Utils.clampMax(this.getHeight(), 4096);
            int n4 = n2 * n3 * 4;
            this.buf = new float[n4];
            this.mapBufferDirty = true;
        }
    }

    private void impl_update() {
        if (this.mapBufferDirty) {
            this.map = new com.sun.scenario.effect.FloatMap(Utils.clamp(1, this.getWidth(), 4096), Utils.clamp(1, this.getHeight(), 4096));
            this.mapBufferDirty = false;
        }
        this.map.put(this.buf);
    }

    void impl_sync() {
        if (this.impl_isEffectDirty()) {
            this.impl_update();
            this.impl_clearDirty();
        }
    }

    private void setEffectDirty(boolean bl) {
        this.effectDirtyProperty().set(bl);
    }

    final BooleanProperty effectDirtyProperty() {
        if (this.effectDirty == null) {
            this.effectDirty = new SimpleBooleanProperty(this, "effectDirty");
        }
        return this.effectDirty;
    }

    @Deprecated
    boolean impl_isEffectDirty() {
        return this.effectDirty == null ? false : this.effectDirty.get();
    }

    private void impl_markDirty() {
        this.setEffectDirty(true);
    }

    private void impl_clearDirty() {
        this.setEffectDirty(false);
    }

    public FloatMap() {
        this.updateBuffer();
        this.impl_markDirty();
    }

    public FloatMap(int n2, int n3) {
        this.setWidth(n2);
        this.setHeight(n3);
        this.updateBuffer();
        this.impl_markDirty();
    }

    public final void setWidth(int n2) {
        this.widthProperty().set(n2);
    }

    public final int getWidth() {
        return this.width == null ? 1 : this.width.get();
    }

    public final IntegerProperty widthProperty() {
        if (this.width == null) {
            this.width = new IntegerPropertyBase(1){

                @Override
                public void invalidated() {
                    FloatMap.this.updateBuffer();
                    FloatMap.this.impl_markDirty();
                }

                @Override
                public Object getBean() {
                    return FloatMap.this;
                }

                @Override
                public String getName() {
                    return "width";
                }
            };
        }
        return this.width;
    }

    public final void setHeight(int n2) {
        this.heightProperty().set(n2);
    }

    public final int getHeight() {
        return this.height == null ? 1 : this.height.get();
    }

    public final IntegerProperty heightProperty() {
        if (this.height == null) {
            this.height = new IntegerPropertyBase(1){

                @Override
                public void invalidated() {
                    FloatMap.this.updateBuffer();
                    FloatMap.this.impl_markDirty();
                }

                @Override
                public Object getBean() {
                    return FloatMap.this;
                }

                @Override
                public String getName() {
                    return "height";
                }
            };
        }
        return this.height;
    }

    public void setSample(int n2, int n3, int n4, float f2) {
        this.buf[(n2 + n3 * this.getWidth()) * 4 + n4] = f2;
        this.impl_markDirty();
    }

    public void setSamples(int n2, int n3, float f2) {
        int n4 = (n2 + n3 * this.getWidth()) * 4;
        this.buf[n4 + 0] = f2;
        this.impl_markDirty();
    }

    public void setSamples(int n2, int n3, float f2, float f3) {
        int n4 = (n2 + n3 * this.getWidth()) * 4;
        this.buf[n4 + 0] = f2;
        this.buf[n4 + 1] = f3;
        this.impl_markDirty();
    }

    public void setSamples(int n2, int n3, float f2, float f3, float f4) {
        int n4 = (n2 + n3 * this.getWidth()) * 4;
        this.buf[n4 + 0] = f2;
        this.buf[n4 + 1] = f3;
        this.buf[n4 + 2] = f4;
        this.impl_markDirty();
    }

    public void setSamples(int n2, int n3, float f2, float f3, float f4, float f5) {
        int n4 = (n2 + n3 * this.getWidth()) * 4;
        this.buf[n4 + 0] = f2;
        this.buf[n4 + 1] = f3;
        this.buf[n4 + 2] = f4;
        this.buf[n4 + 3] = f5;
        this.impl_markDirty();
    }

    @Deprecated
    public FloatMap impl_copy() {
        FloatMap floatMap = new FloatMap(this.getWidth(), this.getHeight());
        System.arraycopy(this.buf, 0, floatMap.buf, 0, this.buf.length);
        return floatMap;
    }
}

