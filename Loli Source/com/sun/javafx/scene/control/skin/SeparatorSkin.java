/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import java.util.Collections;
import javafx.geometry.Orientation;
import javafx.scene.control.Separator;
import javafx.scene.layout.Region;

public class SeparatorSkin
extends BehaviorSkinBase<Separator, BehaviorBase<Separator>> {
    private static final double DEFAULT_LENGTH = 10.0;
    private final Region line = new Region();

    public SeparatorSkin(Separator separator) {
        super(separator, new BehaviorBase<Separator>(separator, Collections.emptyList()));
        this.line.getStyleClass().setAll("line");
        this.getChildren().add(this.line);
        this.registerChangeListener(separator.orientationProperty(), "ORIENTATION");
        this.registerChangeListener(separator.halignmentProperty(), "HALIGNMENT");
        this.registerChangeListener(separator.valignmentProperty(), "VALIGNMENT");
    }

    @Override
    protected void handleControlPropertyChanged(String string) {
        super.handleControlPropertyChanged(string);
        if ("ORIENTATION".equals(string) || "HALIGNMENT".equals(string) || "VALIGNMENT".equals(string)) {
            ((Separator)this.getSkinnable()).requestLayout();
        }
    }

    @Override
    protected void layoutChildren(double d2, double d3, double d4, double d5) {
        Separator separator = (Separator)this.getSkinnable();
        if (separator.getOrientation() == Orientation.HORIZONTAL) {
            this.line.resize(d4, this.line.prefHeight(-1.0));
        } else {
            this.line.resize(this.line.prefWidth(-1.0), d5);
        }
        this.positionInArea(this.line, d2, d3, d4, d5, 0.0, separator.getHalignment(), separator.getValignment());
    }

    @Override
    protected double computeMinWidth(double d2, double d3, double d4, double d5, double d6) {
        return this.computePrefWidth(d2, d3, d4, d5, d6);
    }

    @Override
    protected double computeMinHeight(double d2, double d3, double d4, double d5, double d6) {
        return this.computePrefHeight(d2, d3, d4, d5, d6);
    }

    @Override
    protected double computePrefWidth(double d2, double d3, double d4, double d5, double d6) {
        Separator separator = (Separator)this.getSkinnable();
        double d7 = separator.getOrientation() == Orientation.VERTICAL ? this.line.prefWidth(-1.0) : 10.0;
        return d7 + d6 + d4;
    }

    @Override
    protected double computePrefHeight(double d2, double d3, double d4, double d5, double d6) {
        Separator separator = (Separator)this.getSkinnable();
        double d7 = separator.getOrientation() == Orientation.VERTICAL ? 10.0 : this.line.prefHeight(-1.0);
        return d7 + d3 + d5;
    }

    @Override
    protected double computeMaxWidth(double d2, double d3, double d4, double d5, double d6) {
        Separator separator = (Separator)this.getSkinnable();
        return separator.getOrientation() == Orientation.VERTICAL ? separator.prefWidth(d2) : Double.MAX_VALUE;
    }

    @Override
    protected double computeMaxHeight(double d2, double d3, double d4, double d5, double d6) {
        Separator separator = (Separator)this.getSkinnable();
        return separator.getOrientation() == Orientation.VERTICAL ? Double.MAX_VALUE : separator.prefHeight(d2);
    }
}

