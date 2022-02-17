/*
 * Decompiled with CFR 0.150.
 */
package javafx.animation;

import javafx.animation.PathTransition;
import javafx.animation.TransitionBuilder;
import javafx.scene.Node;
import javafx.scene.shape.Shape;
import javafx.util.Builder;
import javafx.util.Duration;

@Deprecated
public final class PathTransitionBuilder
extends TransitionBuilder<PathTransitionBuilder>
implements Builder<PathTransition> {
    private int __set;
    private Duration duration;
    private Node node;
    private PathTransition.OrientationType orientation;
    private Shape path;

    protected PathTransitionBuilder() {
    }

    public static PathTransitionBuilder create() {
        return new PathTransitionBuilder();
    }

    public void applyTo(PathTransition pathTransition) {
        super.applyTo(pathTransition);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            pathTransition.setDuration(this.duration);
        }
        if ((n2 & 2) != 0) {
            pathTransition.setNode(this.node);
        }
        if ((n2 & 4) != 0) {
            pathTransition.setOrientation(this.orientation);
        }
        if ((n2 & 8) != 0) {
            pathTransition.setPath(this.path);
        }
    }

    public PathTransitionBuilder duration(Duration duration) {
        this.duration = duration;
        this.__set |= 1;
        return this;
    }

    public PathTransitionBuilder node(Node node) {
        this.node = node;
        this.__set |= 2;
        return this;
    }

    public PathTransitionBuilder orientation(PathTransition.OrientationType orientationType) {
        this.orientation = orientationType;
        this.__set |= 4;
        return this;
    }

    public PathTransitionBuilder path(Shape shape) {
        this.path = shape;
        this.__set |= 8;
        return this;
    }

    @Override
    public PathTransition build() {
        PathTransition pathTransition = new PathTransition();
        this.applyTo(pathTransition);
        return pathTransition;
    }
}

