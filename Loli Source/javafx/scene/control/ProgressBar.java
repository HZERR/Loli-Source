/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import com.sun.javafx.scene.control.skin.ProgressBarSkin;
import javafx.css.StyleableProperty;
import javafx.geometry.Orientation;
import javafx.scene.AccessibleAttribute;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Skin;

public class ProgressBar
extends ProgressIndicator {
    private static final String DEFAULT_STYLE_CLASS = "progress-bar";

    public ProgressBar() {
        this(-1.0);
    }

    public ProgressBar(double d2) {
        ((StyleableProperty)((Object)this.focusTraversableProperty())).applyStyle(null, Boolean.FALSE);
        this.setProgress(d2);
        this.getStyleClass().setAll(DEFAULT_STYLE_CLASS);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new ProgressBarSkin(this);
    }

    @Override
    @Deprecated
    protected Boolean impl_cssGetFocusTraversableInitialValue() {
        return Boolean.FALSE;
    }

    @Override
    public Object queryAccessibleAttribute(AccessibleAttribute accessibleAttribute, Object ... arrobject) {
        switch (accessibleAttribute) {
            case ORIENTATION: {
                return Orientation.HORIZONTAL;
            }
        }
        return super.queryAccessibleAttribute(accessibleAttribute, arrobject);
    }
}

