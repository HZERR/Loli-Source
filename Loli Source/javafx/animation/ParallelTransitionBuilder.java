/*
 * Decompiled with CFR 0.150.
 */
package javafx.animation;

import java.util.Arrays;
import java.util.Collection;
import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.animation.TransitionBuilder;
import javafx.scene.Node;
import javafx.util.Builder;

@Deprecated
public final class ParallelTransitionBuilder
extends TransitionBuilder<ParallelTransitionBuilder>
implements Builder<ParallelTransition> {
    private int __set;
    private Collection<? extends Animation> children;
    private Node node;

    protected ParallelTransitionBuilder() {
    }

    public static ParallelTransitionBuilder create() {
        return new ParallelTransitionBuilder();
    }

    public void applyTo(ParallelTransition parallelTransition) {
        super.applyTo(parallelTransition);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            parallelTransition.getChildren().addAll(this.children);
        }
        if ((n2 & 2) != 0) {
            parallelTransition.setNode(this.node);
        }
    }

    public ParallelTransitionBuilder children(Collection<? extends Animation> collection) {
        this.children = collection;
        this.__set |= 1;
        return this;
    }

    public ParallelTransitionBuilder children(Animation ... arranimation) {
        return this.children(Arrays.asList(arranimation));
    }

    public ParallelTransitionBuilder node(Node node) {
        this.node = node;
        this.__set |= 2;
        return this;
    }

    @Override
    public ParallelTransition build() {
        ParallelTransition parallelTransition = new ParallelTransition();
        this.applyTo(parallelTransition);
        return parallelTransition;
    }
}

