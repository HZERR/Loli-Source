/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.paint;

import com.sun.javafx.sg.prism.NGPhongMaterial;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import sun.util.logging.PlatformLogger;

public abstract class Material {
    private final BooleanProperty dirty = new SimpleBooleanProperty(true);

    protected Material() {
        if (!Platform.isSupported(ConditionalFeature.SCENE3D)) {
            String string = Material.class.getName();
            PlatformLogger.getLogger(string).warning("System can't support ConditionalFeature.SCENE3D");
        }
    }

    final boolean isDirty() {
        return this.dirty.getValue();
    }

    void setDirty(boolean bl) {
        this.dirty.setValue(bl);
    }

    @Deprecated
    public final BooleanProperty impl_dirtyProperty() {
        return this.dirty;
    }

    @Deprecated
    public abstract void impl_updatePG();

    @Deprecated
    public abstract NGPhongMaterial impl_getNGMaterial();
}

