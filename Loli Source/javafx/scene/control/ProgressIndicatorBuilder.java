/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import javafx.scene.control.ControlBuilder;
import javafx.scene.control.ProgressIndicator;
import javafx.util.Builder;

@Deprecated
public class ProgressIndicatorBuilder<B extends ProgressIndicatorBuilder<B>>
extends ControlBuilder<B>
implements Builder<ProgressIndicator> {
    private boolean __set;
    private double progress;

    protected ProgressIndicatorBuilder() {
    }

    public static ProgressIndicatorBuilder<?> create() {
        return new ProgressIndicatorBuilder();
    }

    public void applyTo(ProgressIndicator progressIndicator) {
        super.applyTo(progressIndicator);
        if (this.__set) {
            progressIndicator.setProgress(this.progress);
        }
    }

    public B progress(double d2) {
        this.progress = d2;
        this.__set = true;
        return (B)this;
    }

    @Override
    public ProgressIndicator build() {
        ProgressIndicator progressIndicator = new ProgressIndicator();
        this.applyTo(progressIndicator);
        return progressIndicator;
    }
}

