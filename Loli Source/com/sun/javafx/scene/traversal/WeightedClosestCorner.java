/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.traversal;

import com.sun.javafx.scene.traversal.Algorithm;
import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.scene.traversal.TraversalContext;
import java.util.List;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;

public class WeightedClosestCorner
implements Algorithm {
    WeightedClosestCorner() {
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

    @Override
    public Node select(Node node, Direction direction, TraversalContext traversalContext) {
        Node node2 = null;
        List<Node> list = traversalContext.getAllTargetNodes();
        int n2 = this.traverse(traversalContext.getSceneLayoutBounds(node), direction, list, traversalContext);
        if (n2 != -1) {
            node2 = list.get(n2);
        }
        return node2;
    }

    @Override
    public Node selectFirst(TraversalContext traversalContext) {
        List<Node> list = traversalContext.getAllTargetNodes();
        Point2D point2D = new Point2D(0.0, 0.0);
        if (list.size() > 0) {
            Node node = list.get(0);
            double d2 = point2D.distance(traversalContext.getSceneLayoutBounds(list.get(0)).getMinX(), traversalContext.getSceneLayoutBounds(list.get(0)).getMinY());
            for (int i2 = 1; i2 < list.size(); ++i2) {
                double d3 = point2D.distance(traversalContext.getSceneLayoutBounds(list.get(i2)).getMinX(), traversalContext.getSceneLayoutBounds(list.get(i2)).getMinY());
                if (!(d2 > d3)) continue;
                d2 = d3;
                node = list.get(i2);
            }
            return node;
        }
        return null;
    }

    @Override
    public Node selectLast(TraversalContext traversalContext) {
        return null;
    }

    public int traverse(Bounds bounds, Direction direction, List<Node> list, TraversalContext traversalContext) {
        int n2 = direction == Direction.NEXT || direction == Direction.NEXT_IN_LINE || direction == Direction.PREVIOUS ? this.trav1D(bounds, direction, list, traversalContext) : this.trav2D(bounds, direction, list, traversalContext);
        return n2;
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

    private int compare1D(Bounds bounds, Bounds bounds2) {
        int n2 = 0;
        double d2 = (bounds.getMinY() + bounds.getMaxY()) / 2.0;
        double d3 = (bounds2.getMinY() + bounds2.getMaxY()) / 2.0;
        double d4 = (bounds.getMinX() + bounds.getMaxX()) / 2.0;
        double d5 = (bounds2.getMinX() + bounds2.getMaxX()) / 2.0;
        double d6 = bounds.hashCode();
        double d7 = bounds2.hashCode();
        if (d2 < d3) {
            n2 = -1;
        } else if (d2 > d3) {
            n2 = 1;
        } else if (d4 < d5) {
            n2 = -1;
        } else if (d4 > d5) {
            n2 = 1;
        } else if (d6 < d7) {
            n2 = -1;
        } else if (d6 > d7) {
            n2 = 1;
        }
        return n2;
    }

    private int compare1D(Bounds bounds, Bounds bounds2, Direction direction) {
        return direction != Direction.PREVIOUS ? -this.compare1D(bounds, bounds2) : this.compare1D(bounds, bounds2);
    }

    private int trav1D(Bounds bounds, Direction direction, List<Node> list, TraversalContext traversalContext) {
        int n2 = -1;
        int n3 = -1;
        for (int i2 = 0; i2 < list.size(); ++i2) {
            if (n3 == -1 || this.compare1D(traversalContext.getSceneLayoutBounds(list.get(i2)), traversalContext.getSceneLayoutBounds(list.get(n3)), direction) < 0) {
                n3 = i2;
            }
            if (this.compare1D(traversalContext.getSceneLayoutBounds(list.get(i2)), bounds, direction) < 0 || n2 != -1 && this.compare1D(traversalContext.getSceneLayoutBounds(list.get(i2)), traversalContext.getSceneLayoutBounds(list.get(n2)), direction) >= 0) continue;
            n2 = i2;
        }
        return n2 == -1 ? n3 : n2;
    }
}

