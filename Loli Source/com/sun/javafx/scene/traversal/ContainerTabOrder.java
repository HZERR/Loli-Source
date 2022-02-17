/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.traversal;

import com.sun.javafx.scene.traversal.Algorithm;
import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.scene.traversal.TabOrderHelper;
import com.sun.javafx.scene.traversal.TraversalContext;
import java.util.List;
import javafx.geometry.Bounds;
import javafx.scene.Node;

public class ContainerTabOrder
implements Algorithm {
    ContainerTabOrder() {
    }

    @Override
    public Node select(Node node, Direction direction, TraversalContext traversalContext) {
        switch (direction) {
            case NEXT: 
            case NEXT_IN_LINE: {
                return TabOrderHelper.findNextFocusablePeer(node, traversalContext.getRoot(), direction == Direction.NEXT);
            }
            case PREVIOUS: {
                return TabOrderHelper.findPreviousFocusablePeer(node, traversalContext.getRoot());
            }
            case UP: 
            case DOWN: 
            case LEFT: 
            case RIGHT: {
                List<Node> list = traversalContext.getAllTargetNodes();
                int n2 = this.trav2D(traversalContext.getSceneLayoutBounds(node), direction, list, traversalContext);
                if (n2 == -1) break;
                return list.get(n2);
            }
        }
        return null;
    }

    @Override
    public Node selectFirst(TraversalContext traversalContext) {
        return TabOrderHelper.getFirstTargetNode(traversalContext.getRoot());
    }

    @Override
    public Node selectLast(TraversalContext traversalContext) {
        return TabOrderHelper.getLastTargetNode(traversalContext.getRoot());
    }

    private int trav2D(Bounds bounds, Direction direction, List<Node> list, TraversalContext traversalContext) {
        Bounds bounds2 = null;
        double d2 = 0.0;
        int n2 = -1;
        for (int i2 = 0; i2 < list.size(); ++i2) {
            double d3;
            Bounds bounds3 = traversalContext.getSceneLayoutBounds(list.get(i2));
            double d4 = this.outDistance(direction, bounds, bounds3);
            if (this.isOnAxis(direction, bounds, bounds3)) {
                d3 = d4 + this.centerSideDistance(direction, bounds, bounds3) / 100.0;
            } else {
                double d5 = this.cornerSideDistance(direction, bounds, bounds3);
                d3 = 100000.0 + d4 * d4 + 9.0 * d5 * d5;
            }
            if (d4 < 0.0 || bounds2 != null && !(d3 < d2)) continue;
            bounds2 = bounds3;
            d2 = d3;
            n2 = i2;
        }
        return n2;
    }

    private boolean isOnAxis(Direction direction, Bounds bounds, Bounds bounds2) {
        double d2;
        double d3;
        double d4;
        double d5;
        if (direction == Direction.UP || direction == Direction.DOWN) {
            d5 = bounds.getMinX();
            d4 = bounds.getMaxX();
            d3 = bounds2.getMinX();
            d2 = bounds2.getMaxX();
        } else {
            d5 = bounds.getMinY();
            d4 = bounds.getMaxY();
            d3 = bounds2.getMinY();
            d2 = bounds2.getMaxY();
        }
        return d3 <= d4 && d2 >= d5;
    }

    private double outDistance(Direction direction, Bounds bounds, Bounds bounds2) {
        double d2 = direction == Direction.UP ? bounds.getMinY() - bounds2.getMaxY() : (direction == Direction.DOWN ? bounds2.getMinY() - bounds.getMaxY() : (direction == Direction.LEFT ? bounds.getMinX() - bounds2.getMaxX() : bounds2.getMinX() - bounds.getMaxX()));
        return d2;
    }

    private double centerSideDistance(Direction direction, Bounds bounds, Bounds bounds2) {
        double d2;
        double d3;
        if (direction == Direction.UP || direction == Direction.DOWN) {
            d3 = bounds.getMinX() + bounds.getWidth() / 2.0;
            d2 = bounds2.getMinX() + bounds2.getWidth() / 2.0;
        } else {
            d3 = bounds.getMinY() + bounds.getHeight() / 2.0;
            d2 = bounds2.getMinY() + bounds2.getHeight() / 2.0;
        }
        return Math.abs(d2 - d3);
    }

    private double cornerSideDistance(Direction direction, Bounds bounds, Bounds bounds2) {
        double d2 = direction == Direction.UP || direction == Direction.DOWN ? (bounds2.getMinX() > bounds.getMaxX() ? bounds2.getMinX() - bounds.getMaxX() : bounds.getMinX() - bounds2.getMaxX()) : (bounds2.getMinY() > bounds.getMaxY() ? bounds2.getMinY() - bounds.getMaxY() : bounds.getMinY() - bounds2.getMaxY());
        return d2;
    }
}

