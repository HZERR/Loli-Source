/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.canvas;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.sg.prism.GrowableDataBuffer;
import com.sun.javafx.sg.prism.NGCanvas;
import com.sun.javafx.sg.prism.NGNode;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;

public class Canvas
extends Node {
    static final int DEFAULT_VAL_BUF_SIZE = 1024;
    static final int DEFAULT_OBJ_BUF_SIZE = 32;
    private static final int SIZE_HISTORY = 5;
    private GrowableDataBuffer current;
    private boolean rendererBehind;
    private int[] recentvalsizes = new int[5];
    private int[] recentobjsizes = new int[5];
    private int lastsizeindex;
    private GraphicsContext theContext;
    private DoubleProperty width;
    private DoubleProperty height;

    public Canvas() {
        this(0.0, 0.0);
    }

    public Canvas(double d2, double d3) {
        this.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        this.setWidth(d2);
        this.setHeight(d3);
    }

    private static int max(int[] arrn, int n2) {
        for (int n3 : arrn) {
            if (n2 >= n3) continue;
            n2 = n3;
        }
        return n2;
    }

    GrowableDataBuffer getBuffer() {
        this.impl_markDirty(DirtyBits.NODE_CONTENTS);
        this.impl_markDirty(DirtyBits.NODE_FORCE_SYNC);
        if (this.current == null) {
            int n2 = Canvas.max(this.recentvalsizes, 1024);
            int n3 = Canvas.max(this.recentobjsizes, 32);
            this.current = GrowableDataBuffer.getBuffer(n2, n3);
            this.theContext.updateDimensions();
        }
        return this.current;
    }

    boolean isRendererFallingBehind() {
        return this.rendererBehind;
    }

    public GraphicsContext getGraphicsContext2D() {
        if (this.theContext == null) {
            this.theContext = new GraphicsContext(this);
        }
        return this.theContext;
    }

    public final void setWidth(double d2) {
        this.widthProperty().set(d2);
    }

    public final double getWidth() {
        return this.width == null ? 0.0 : this.width.get();
    }

    public final DoubleProperty widthProperty() {
        if (this.width == null) {
            this.width = new DoublePropertyBase(){

                @Override
                public void invalidated() {
                    Canvas.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                    Canvas.this.impl_geomChanged();
                    if (Canvas.this.theContext != null) {
                        Canvas.this.theContext.updateDimensions();
                    }
                }

                @Override
                public Object getBean() {
                    return Canvas.this;
                }

                @Override
                public String getName() {
                    return "width";
                }
            };
        }
        return this.width;
    }

    public final void setHeight(double d2) {
        this.heightProperty().set(d2);
    }

    public final double getHeight() {
        return this.height == null ? 0.0 : this.height.get();
    }

    public final DoubleProperty heightProperty() {
        if (this.height == null) {
            this.height = new DoublePropertyBase(){

                @Override
                public void invalidated() {
                    Canvas.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                    Canvas.this.impl_geomChanged();
                    if (Canvas.this.theContext != null) {
                        Canvas.this.theContext.updateDimensions();
                    }
                }

                @Override
                public Object getBean() {
                    return Canvas.this;
                }

                @Override
                public String getName() {
                    return "height";
                }
            };
        }
        return this.height;
    }

    @Override
    @Deprecated
    protected NGNode impl_createPeer() {
        return new NGCanvas();
    }

    @Override
    @Deprecated
    public void impl_updatePeer() {
        NGCanvas nGCanvas;
        super.impl_updatePeer();
        if (this.impl_isDirty(DirtyBits.NODE_GEOMETRY)) {
            nGCanvas = (NGCanvas)this.impl_getPeer();
            nGCanvas.updateBounds((float)this.getWidth(), (float)this.getHeight());
        }
        if (this.impl_isDirty(DirtyBits.NODE_CONTENTS)) {
            nGCanvas = (NGCanvas)this.impl_getPeer();
            if (this.current != null && !this.current.isEmpty()) {
                if (--this.lastsizeindex < 0) {
                    this.lastsizeindex = 4;
                }
                this.recentvalsizes[this.lastsizeindex] = this.current.writeValuePosition();
                this.recentobjsizes[this.lastsizeindex] = this.current.writeObjectPosition();
                this.rendererBehind = nGCanvas.updateRendering(this.current);
                this.current = null;
            }
        }
    }

    @Override
    @Deprecated
    protected boolean impl_computeContains(double d2, double d3) {
        double d4 = this.getWidth();
        double d5 = this.getHeight();
        return d4 > 0.0 && d5 > 0.0 && d2 >= 0.0 && d3 >= 0.0 && d2 < d4 && d3 < d5;
    }

    @Override
    @Deprecated
    public BaseBounds impl_computeGeomBounds(BaseBounds baseBounds, BaseTransform baseTransform) {
        baseBounds = new RectBounds(0.0f, 0.0f, (float)this.getWidth(), (float)this.getHeight());
        baseBounds = baseTransform.transform(baseBounds, baseBounds);
        return baseBounds;
    }

    @Override
    @Deprecated
    public Object impl_processMXNode(MXNodeAlgorithm mXNodeAlgorithm, MXNodeAlgorithmContext mXNodeAlgorithmContext) {
        return mXNodeAlgorithm.processLeafNode(this, mXNodeAlgorithmContext);
    }
}

