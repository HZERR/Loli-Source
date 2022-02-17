/*
 * Decompiled with CFR 0.150.
 */
package javafx.animation;

import javafx.animation.RotateTransition;
import javafx.animation.TransitionBuilder;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.util.Builder;
import javafx.util.Duration;

@Deprecated
public final class RotateTransitionBuilder
extends TransitionBuilder<RotateTransitionBuilder>
implements Builder<RotateTransition> {
    private int __set;
    private Point3D axis;
    private double byAngle;
    private Duration duration;
    private double fromAngle;
    private Node node;
    private double toAngle;

    protected RotateTransitionBuilder() {
    }

    public static RotateTransitionBuilder create() {
        return new RotateTransitionBuilder();
    }

    public void applyTo(RotateTransition rotateTransition) {
        super.applyTo(rotateTransition);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            rotateTransition.setAxis(this.axis);
        }
        if ((n2 & 2) != 0) {
            rotateTransition.setByAngle(this.byAngle);
        }
        if ((n2 & 4) != 0) {
            rotateTransition.setDuration(this.duration);
        }
        if ((n2 & 8) != 0) {
            rotateTransition.setFromAngle(this.fromAngle);
        }
        if ((n2 & 0x10) != 0) {
            rotateTransition.setNode(this.node);
        }
        if ((n2 & 0x20) != 0) {
            rotateTransition.setToAngle(this.toAngle);
        }
    }

    public RotateTransitionBuilder axis(Point3D point3D) {
        this.axis = point3D;
        this.__set |= 1;
        return this;
    }

    public RotateTransitionBuilder byAngle(double d2) {
        this.byAngle = d2;
        this.__set |= 2;
        return this;
    }

    public RotateTransitionBuilder duration(Duration duration) {
        this.duration = duration;
        this.__set |= 4;
        return this;
    }

    public RotateTransitionBuilder fromAngle(double d2) {
        this.fromAngle = d2;
        this.__set |= 8;
        return this;
    }

    public RotateTransitionBuilder node(Node node) {
        this.node = node;
        this.__set |= 0x10;
        return this;
    }

    public RotateTransitionBuilder toAngle(double d2) {
        this.toAngle = d2;
        this.__set |= 0x20;
        return this;
    }

    @Override
    public RotateTransition build() {
        RotateTransition rotateTransition = new RotateTransition();
        this.applyTo(rotateTransition);
        return rotateTransition;
    }
}

