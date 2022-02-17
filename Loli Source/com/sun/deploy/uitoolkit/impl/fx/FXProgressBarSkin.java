/*
 * Decompiled with CFR 0.150.
 */
package com.sun.deploy.uitoolkit.impl.fx;

import com.sun.javafx.scene.control.skin.ProgressBarSkin;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;

public class FXProgressBarSkin
extends ProgressBarSkin {
    Rectangle topGradient = new Rectangle(0.0, 0.0, new RadialGradient(0.05, 0.0, 0.5, 0.0, this.gradientRadius, true, CycleMethod.NO_CYCLE, new Stop(0.0, Color.rgb(255, 255, 255, 0.82)), new Stop(0.13, Color.rgb(255, 255, 255, 0.82)), new Stop(0.98, Color.rgb(255, 255, 255, 0.0))));
    Rectangle bottomGradient = new Rectangle(0.0, 0.0, new RadialGradient(0.05, 0.0, 0.5, 1.0, this.gradientRadius, true, CycleMethod.NO_CYCLE, new Stop(0.0, Color.rgb(255, 255, 255, 0.82)), new Stop(0.13, Color.rgb(255, 255, 255, 0.82)), new Stop(0.98, Color.rgb(255, 255, 255, 0.0))));
    Rectangle verticalLines;
    double gradientMargin = 4.0;
    double gradientRadius = 0.55;
    double gradientTweak = 1.4;

    public FXProgressBarSkin(ProgressBar progressBar) {
        super(progressBar);
        this.topGradient.setManaged(false);
        this.bottomGradient.setManaged(false);
        ((StackPane)this.getChildren().get(1)).getChildren().addAll(this.topGradient, this.bottomGradient);
        this.verticalLines = new Rectangle(0.0, 0.0, new LinearGradient(0.0, 0.0, 14.0, 0.0, false, CycleMethod.REPEAT, new Stop(0.0, Color.TRANSPARENT), new Stop(0.93, Color.TRANSPARENT), new Stop(0.93, Color.rgb(184, 184, 184, 0.2)), new Stop(1.0, Color.rgb(184, 184, 184, 0.2))));
        this.verticalLines.setManaged(false);
        this.getChildren().add(this.verticalLines);
    }

    @Override
    protected void layoutChildren(double d2, double d3, double d4, double d5) {
        super.layoutChildren(d2, d3, d4, d5);
        if (((ProgressIndicator)this.getSkinnable()).isIndeterminate()) {
            return;
        }
        StackPane stackPane = (StackPane)this.getChildren().get(0);
        StackPane stackPane2 = (StackPane)this.getChildren().get(1);
        if (!stackPane2.getChildren().contains(this.topGradient)) {
            stackPane2.getChildren().add(this.topGradient);
        }
        if (!stackPane2.getChildren().contains(this.bottomGradient)) {
            stackPane2.getChildren().add(this.bottomGradient);
        }
        if (!this.getChildren().contains(this.verticalLines)) {
            this.getChildren().add(this.verticalLines);
        }
        double d6 = Math.floor(d4 / 14.0) * 14.0 / d4;
        double d7 = stackPane2.getWidth() * d6;
        double d8 = stackPane2.getHeight();
        stackPane.resize(stackPane.getWidth() * d6, stackPane.getHeight());
        stackPane2.resize(d7, d8);
        this.topGradient.setX(d2 + this.gradientMargin);
        this.topGradient.setY(d3 + 0.5);
        this.topGradient.setWidth(d7 - 2.0 * this.gradientMargin);
        this.topGradient.setHeight(d8 * 0.3 / this.gradientRadius * this.gradientTweak);
        this.bottomGradient.setX(d2 + this.gradientMargin);
        this.bottomGradient.setWidth(d7 - 2.0 * this.gradientMargin);
        double d9 = d8 * 0.21 / this.gradientRadius * this.gradientTweak;
        this.bottomGradient.setY(d8 - d9 - 0.5);
        this.bottomGradient.setHeight(d9);
        this.verticalLines.setX(d2);
        this.verticalLines.setY(d3);
        this.verticalLines.setWidth(d4 * d6);
        this.verticalLines.setHeight(d5);
    }
}

