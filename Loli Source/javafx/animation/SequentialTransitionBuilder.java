/*
 * Decompiled with CFR 0.150.
 */
package javafx.animation;

import java.util.Arrays;
import java.util.Collection;
import javafx.animation.Animation;
import javafx.animation.SequentialTransition;
import javafx.animation.TransitionBuilder;
import javafx.scene.Node;
import javafx.util.Builder;

@Deprecated
public final class SequentialTransitionBuilder
extends TransitionBuilder<SequentialTransitionBuilder>
implements Builder<SequentialTransition> {
    private int __set;
    private Collection<? extends Animation> children;
    private Node node;

    protected SequentialTransitionBuilder() {
    }

    public static SequentialTransitionBuilder create() {
        return new SequentialTransitionBuilder();
    }

    public void applyTo(SequentialTransition sequentialTransition) {
        super.applyTo(sequentialTransition);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            sequentialTransition.getChildren().addAll(this.children);
        }
        if ((n2 & 2) != 0) {
            sequentialTransition.setNode(this.node);
        }
    }

    public SequentialTransitionBuilder children(Collection<? extends Animation> collection) {
        this.children = collection;
        this.__set |= 1;
        return this;
    }

    public SequentialTransitionBuilder children(Animation ... arranimation) {
        return this.children(Arrays.asList(arranimation));
    }

    public SequentialTransitionBuilder node(Node node) {
        this.node = node;
        this.__set |= 2;
        return this;
    }

    @Override
    public SequentialTransition build() {
        SequentialTransition sequentialTransition = new SequentialTransition();
        this.applyTo(sequentialTransition);
        return sequentialTransition;
    }
}

