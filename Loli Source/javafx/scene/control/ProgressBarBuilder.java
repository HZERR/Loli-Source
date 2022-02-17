/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicatorBuilder;

@Deprecated
public class ProgressBarBuilder<B extends ProgressBarBuilder<B>>
extends ProgressIndicatorBuilder<B> {
    protected ProgressBarBuilder() {
    }

    public static ProgressBarBuilder<?> create() {
        return new ProgressBarBuilder();
    }

    @Override
    public ProgressBar build() {
        ProgressBar progressBar = new ProgressBar();
        this.applyTo(progressBar);
        return progressBar;
    }
}

