/*
 * Decompiled with CFR 0.150.
 */
package javafx.animation;

import com.sun.javafx.geom.PathIterator;
import java.util.ArrayList;
import javafx.animation.Transition;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

public final class PathTransition
extends Transition {
    private ObjectProperty<Node> node;
    private double totalLength = 0.0;
    private final ArrayList<Segment> segments = new ArrayList();
    private static final Node DEFAULT_NODE = null;
    private static final int SMOOTH_ZONE = 10;
    private Node cachedNode;
    private ObjectProperty<Duration> duration;
    private static final Duration DEFAULT_DURATION = Duration.millis(400.0);
    private ObjectProperty<Shape> path;
    private static final Shape DEFAULT_PATH = null;
    private ObjectProperty<OrientationType> orientation;
    private static final OrientationType DEFAULT_ORIENTATION = OrientationType.NONE;
    private boolean cachedIsNormalRequired;

    public final void setNode(Node node) {
        if (this.node != null || node != null) {
            this.nodeProperty().set(node);
        }
    }

    public final Node getNode() {
        return this.node == null ? DEFAULT_NODE : (Node)this.node.get();
    }

    public final ObjectProperty<Node> nodeProperty() {
        if (this.node == null) {
            this.node = new SimpleObjectProperty<Node>(this, "node", DEFAULT_NODE);
        }
        return this.node;
    }

    public final void setDuration(Duration duration) {
        if (this.duration != null || !DEFAULT_DURATION.equals(duration)) {
            this.durationProperty().set(duration);
        }
    }

    public final Duration getDuration() {
        return this.duration == null ? DEFAULT_DURATION : (Duration)this.duration.get();
    }

    public final ObjectProperty<Duration> durationProperty() {
        if (this.duration == null) {
            this.duration = new ObjectPropertyBase<Duration>(DEFAULT_DURATION){

                @Override
                public void invalidated() {
                    try {
                        PathTransition.this.setCycleDuration(PathTransition.this.getDuration());
                    }
                    catch (IllegalArgumentException illegalArgumentException) {
                        if (this.isBound()) {
                            this.unbind();
                        }
                        this.set(PathTransition.this.getCycleDuration());
                        throw illegalArgumentException;
                    }
                }

                @Override
                public Object getBean() {
                    return PathTransition.this;
                }

                @Override
                public String getName() {
                    return "duration";
                }
            };
        }
        return this.duration;
    }

    public final void setPath(Shape shape) {
        if (this.path != null || shape != null) {
            this.pathProperty().set(shape);
        }
    }

    public final Shape getPath() {
        return this.path == null ? DEFAULT_PATH : (Shape)this.path.get();
    }

    public final ObjectProperty<Shape> pathProperty() {
        if (this.path == null) {
            this.path = new SimpleObjectProperty<Shape>(this, "path", DEFAULT_PATH);
        }
        return this.path;
    }

    public final void setOrientation(OrientationType orientationType) {
        if (this.orientation != null || !DEFAULT_ORIENTATION.equals((Object)orientationType)) {
            this.orientationProperty().set(orientationType);
        }
    }

    public final OrientationType getOrientation() {
        return this.orientation == null ? OrientationType.NONE : (OrientationType)((Object)this.orientation.get());
    }

    public final ObjectProperty<OrientationType> orientationProperty() {
        if (this.orientation == null) {
            this.orientation = new SimpleObjectProperty<OrientationType>(this, "orientation", DEFAULT_ORIENTATION);
        }
        return this.orientation;
    }

    public PathTransition(Duration duration, Shape shape, Node node) {
        this.setDuration(duration);
        this.setPath(shape);
        this.setNode(node);
        this.setCycleDuration(duration);
    }

    public PathTransition(Duration duration, Shape shape) {
        this(duration, shape, null);
    }

    public PathTransition() {
        this(DEFAULT_DURATION, null, null);
    }

    @Override
    public void interpolate(double d2) {
        double d3 = this.totalLength * Math.min(1.0, Math.max(0.0, d2));
        int n2 = this.findSegment(0, this.segments.size() - 1, d3);
        Segment segment = this.segments.get(n2);
        double d4 = segment.accumLength - segment.length;
        double d5 = d3 - d4;
        double d6 = d5 / segment.length;
        Segment segment2 = segment.prevSeg;
        double d7 = segment2.toX + (segment.toX - segment2.toX) * d6;
        double d8 = segment2.toY + (segment.toY - segment2.toY) * d6;
        double d9 = segment.rotateAngle;
        double d10 = Math.min(10.0, segment.length / 2.0);
        if (d5 < d10 && !segment2.isMoveTo) {
            d9 = PathTransition.interpolate(segment2.rotateAngle, segment.rotateAngle, d5 / d10 / 2.0 + 0.5);
        } else {
            double d11 = segment.length - d5;
            Segment segment3 = segment.nextSeg;
            if (d11 < d10 && segment3 != null && !segment3.isMoveTo) {
                d9 = PathTransition.interpolate(segment.rotateAngle, segment3.rotateAngle, (d10 - d11) / d10 / 2.0);
            }
        }
        this.cachedNode.setTranslateX(d7 - this.cachedNode.impl_getPivotX());
        this.cachedNode.setTranslateY(d8 - this.cachedNode.impl_getPivotY());
        if (this.cachedIsNormalRequired) {
            this.cachedNode.setRotate(d9);
        }
    }

    private Node getTargetNode() {
        Node node = this.getNode();
        return node != null ? node : this.getParentTargetNode();
    }

    @Override
    boolean impl_startable(boolean bl) {
        return super.impl_startable(bl) && (this.getTargetNode() != null && this.getPath() != null && !this.getPath().getLayoutBounds().isEmpty() || !bl && this.cachedNode != null);
    }

    @Override
    void impl_sync(boolean bl) {
        super.impl_sync(bl);
        if (bl || this.cachedNode == null) {
            this.cachedNode = this.getTargetNode();
            this.recomputeSegments();
            this.cachedIsNormalRequired = this.getOrientation() == OrientationType.ORTHOGONAL_TO_TANGENT;
        }
    }

    private void recomputeSegments() {
        this.segments.clear();
        Shape shape = this.getPath();
        Segment segment = Segment.getZeroSegment();
        Segment segment2 = Segment.getZeroSegment();
        float[] arrf = new float[6];
        PathIterator pathIterator = shape.impl_configShape().getPathIterator(shape.impl_getLeafTransform(), 1.0f);
        while (!pathIterator.isDone()) {
            Segment segment3 = null;
            int n2 = pathIterator.currentSegment(arrf);
            double d2 = arrf[0];
            double d3 = arrf[1];
            switch (n2) {
                case 0: {
                    segment3 = segment = Segment.newMoveTo(d2, d3, segment2.accumLength);
                    break;
                }
                case 4: {
                    segment3 = Segment.newClosePath(segment2, segment);
                    if (segment3 != null) break;
                    segment2.convertToClosePath(segment);
                    break;
                }
                case 1: {
                    segment3 = Segment.newLineTo(segment2, d2, d3);
                }
            }
            if (segment3 != null) {
                this.segments.add(segment3);
                segment2 = segment3;
            }
            pathIterator.next();
        }
        this.totalLength = segment2.accumLength;
    }

    private int findSegment(int n2, int n3, double d2) {
        if (n2 == n3) {
            return this.segments.get((int)n2).isMoveTo && n2 > 0 ? this.findSegment(n2 - 1, n2 - 1, d2) : n2;
        }
        int n4 = n2 + (n3 - n2) / 2;
        return this.segments.get((int)n4).accumLength > d2 ? this.findSegment(n2, n4, d2) : this.findSegment(n4 + 1, n3, d2);
    }

    private static double interpolate(double d2, double d3, double d4) {
        double d5 = d3 - d2;
        if (Math.abs(d5) > 180.0) {
            d3 += d5 > 0.0 ? -360.0 : 360.0;
        }
        return PathTransition.normalize(d2 + d4 * (d3 - d2));
    }

    private static double normalize(double d2) {
        while (d2 > 360.0) {
            d2 -= 360.0;
        }
        while (d2 < 0.0) {
            d2 += 360.0;
        }
        return d2;
    }

    private static class Segment {
        private static final Segment zeroSegment = new Segment(true, 0.0, 0.0, 0.0, 0.0, 0.0);
        boolean isMoveTo;
        double length;
        double accumLength;
        double toX;
        double toY;
        double rotateAngle;
        Segment prevSeg;
        Segment nextSeg;

        private Segment(boolean bl, double d2, double d3, double d4, double d5, double d6) {
            this.isMoveTo = bl;
            this.toX = d2;
            this.toY = d3;
            this.length = d4;
            this.accumLength = d5 + d4;
            this.rotateAngle = d6;
        }

        public static Segment getZeroSegment() {
            return zeroSegment;
        }

        public static Segment newMoveTo(double d2, double d3, double d4) {
            return new Segment(true, d2, d3, 0.0, d4, 0.0);
        }

        public static Segment newLineTo(Segment segment, double d2, double d3) {
            double d4 = d2 - segment.toX;
            double d5 = d3 - segment.toY;
            double d6 = Math.sqrt(d4 * d4 + d5 * d5);
            if (d6 >= 1.0 || segment.isMoveTo) {
                Segment segment2;
                double d7 = Math.signum(d5 == 0.0 ? d4 : d5);
                double d8 = d7 * Math.acos(d4 / d6);
                d8 = PathTransition.normalize(d8 / Math.PI * 180.0);
                segment.nextSeg = segment2 = new Segment(false, d2, d3, d6, segment.accumLength, d8);
                segment2.prevSeg = segment;
                return segment2;
            }
            return null;
        }

        public static Segment newClosePath(Segment segment, Segment segment2) {
            Segment segment3 = Segment.newLineTo(segment, segment2.toX, segment2.toY);
            if (segment3 != null) {
                segment3.convertToClosePath(segment2);
            }
            return segment3;
        }

        public void convertToClosePath(Segment segment) {
            Segment segment2;
            this.nextSeg = segment2 = segment.nextSeg;
            segment2.prevSeg = this;
        }
    }

    public static enum OrientationType {
        NONE,
        ORTHOGONAL_TO_TANGENT;

    }
}

