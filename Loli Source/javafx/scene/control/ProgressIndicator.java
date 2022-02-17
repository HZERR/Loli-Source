/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import com.sun.javafx.scene.control.skin.ProgressIndicatorSkin;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.css.PseudoClass;
import javafx.css.StyleableProperty;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

public class ProgressIndicator
extends Control {
    public static final double INDETERMINATE_PROGRESS = -1.0;
    private ReadOnlyBooleanWrapper indeterminate;
    private DoubleProperty progress;
    private static final String DEFAULT_STYLE_CLASS = "progress-indicator";
    private static final PseudoClass PSEUDO_CLASS_DETERMINATE = PseudoClass.getPseudoClass("determinate");
    private static final PseudoClass PSEUDO_CLASS_INDETERMINATE = PseudoClass.getPseudoClass("indeterminate");

    public ProgressIndicator() {
        this(-1.0);
    }

    public ProgressIndicator(double d2) {
        ((StyleableProperty)((Object)this.focusTraversableProperty())).applyStyle(null, Boolean.FALSE);
        this.setProgress(d2);
        this.getStyleClass().setAll(DEFAULT_STYLE_CLASS);
        this.setAccessibleRole(AccessibleRole.PROGRESS_INDICATOR);
        int n2 = Double.compare(-1.0, d2);
        this.pseudoClassStateChanged(PSEUDO_CLASS_INDETERMINATE, n2 == 0);
        this.pseudoClassStateChanged(PSEUDO_CLASS_DETERMINATE, n2 != 0);
    }

    private void setIndeterminate(boolean bl) {
        this.indeterminatePropertyImpl().set(bl);
    }

    public final boolean isIndeterminate() {
        return this.indeterminate == null ? true : this.indeterminate.get();
    }

    public final ReadOnlyBooleanProperty indeterminateProperty() {
        return this.indeterminatePropertyImpl().getReadOnlyProperty();
    }

    private ReadOnlyBooleanWrapper indeterminatePropertyImpl() {
        if (this.indeterminate == null) {
            this.indeterminate = new ReadOnlyBooleanWrapper(true){

                @Override
                protected void invalidated() {
                    boolean bl = this.get();
                    ProgressIndicator.this.pseudoClassStateChanged(PSEUDO_CLASS_INDETERMINATE, bl);
                    ProgressIndicator.this.pseudoClassStateChanged(PSEUDO_CLASS_DETERMINATE, !bl);
                }

                @Override
                public Object getBean() {
                    return ProgressIndicator.this;
                }

                @Override
                public String getName() {
                    return "indeterminate";
                }
            };
        }
        return this.indeterminate;
    }

    public final void setProgress(double d2) {
        this.progressProperty().set(d2);
    }

    public final double getProgress() {
        return this.progress == null ? -1.0 : this.progress.get();
    }

    public final DoubleProperty progressProperty() {
        if (this.progress == null) {
            this.progress = new DoublePropertyBase(-1.0){

                @Override
                protected void invalidated() {
                    ProgressIndicator.this.setIndeterminate(ProgressIndicator.this.getProgress() < 0.0);
                }

                @Override
                public Object getBean() {
                    return ProgressIndicator.this;
                }

                @Override
                public String getName() {
                    return "progress";
                }
            };
        }
        return this.progress;
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new ProgressIndicatorSkin(this);
    }

    @Override
    @Deprecated
    protected Boolean impl_cssGetFocusTraversableInitialValue() {
        return Boolean.FALSE;
    }

    @Override
    public Object queryAccessibleAttribute(AccessibleAttribute accessibleAttribute, Object ... arrobject) {
        switch (accessibleAttribute) {
            case VALUE: {
                return this.getProgress();
            }
            case MAX_VALUE: {
                return 1.0;
            }
            case MIN_VALUE: {
                return 0.0;
            }
            case INDETERMINATE: {
                return this.isIndeterminate();
            }
        }
        return super.queryAccessibleAttribute(accessibleAttribute, arrobject);
    }
}

