/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.shape;

import com.sun.javafx.geom.Path2D;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGSVGPath;
import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.util.Logging;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.scene.paint.Paint;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.Shape;

public class SVGPath
extends Shape {
    private ObjectProperty<FillRule> fillRule;
    private Path2D path2d;
    private StringProperty content;
    private Object svgPathObject;

    public final void setFillRule(FillRule fillRule) {
        if (this.fillRule != null || fillRule != FillRule.NON_ZERO) {
            this.fillRuleProperty().set(fillRule);
        }
    }

    public final FillRule getFillRule() {
        return this.fillRule == null ? FillRule.NON_ZERO : (FillRule)((Object)this.fillRule.get());
    }

    public final ObjectProperty<FillRule> fillRuleProperty() {
        if (this.fillRule == null) {
            this.fillRule = new ObjectPropertyBase<FillRule>(FillRule.NON_ZERO){

                @Override
                public void invalidated() {
                    SVGPath.this.impl_markDirty(DirtyBits.SHAPE_FILLRULE);
                    SVGPath.this.impl_geomChanged();
                }

                @Override
                public Object getBean() {
                    return SVGPath.this;
                }

                @Override
                public String getName() {
                    return "fillRule";
                }
            };
        }
        return this.fillRule;
    }

    public final void setContent(String string) {
        this.contentProperty().set(string);
    }

    public final String getContent() {
        return this.content == null ? "" : (String)this.content.get();
    }

    public final StringProperty contentProperty() {
        if (this.content == null) {
            this.content = new StringPropertyBase(""){

                @Override
                public void invalidated() {
                    SVGPath.this.impl_markDirty(DirtyBits.NODE_CONTENTS);
                    SVGPath.this.impl_geomChanged();
                    SVGPath.this.path2d = null;
                }

                @Override
                public Object getBean() {
                    return SVGPath.this;
                }

                @Override
                public String getName() {
                    return "content";
                }
            };
        }
        return this.content;
    }

    @Override
    @Deprecated
    protected NGNode impl_createPeer() {
        return new NGSVGPath();
    }

    @Override
    @Deprecated
    public Path2D impl_configShape() {
        if (this.path2d == null) {
            this.path2d = this.createSVGPath2D();
        } else {
            this.path2d.setWindingRule(this.getFillRule() == FillRule.NON_ZERO ? 1 : 0);
        }
        return this.path2d;
    }

    @Override
    @Deprecated
    public void impl_updatePeer() {
        super.impl_updatePeer();
        if (this.impl_isDirty(DirtyBits.SHAPE_FILLRULE) || this.impl_isDirty(DirtyBits.NODE_CONTENTS)) {
            NGSVGPath nGSVGPath = (NGSVGPath)this.impl_getPeer();
            if (nGSVGPath.acceptsPath2dOnUpdate()) {
                if (this.svgPathObject == null) {
                    this.svgPathObject = new Path2D();
                }
                Path2D path2D = (Path2D)this.svgPathObject;
                path2D.setTo(this.impl_configShape());
            } else {
                this.svgPathObject = this.createSVGPathObject();
            }
            nGSVGPath.setContent(this.svgPathObject);
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("SVGPath[");
        String string = this.getId();
        if (string != null) {
            stringBuilder.append("id=").append(string).append(", ");
        }
        stringBuilder.append("content=\"").append(this.getContent()).append("\"");
        stringBuilder.append(", fill=").append(this.getFill());
        stringBuilder.append(", fillRule=").append((Object)this.getFillRule());
        Paint paint = this.getStroke();
        if (paint != null) {
            stringBuilder.append(", stroke=").append(paint);
            stringBuilder.append(", strokeWidth=").append(this.getStrokeWidth());
        }
        return stringBuilder.append("]").toString();
    }

    private Path2D createSVGPath2D() {
        try {
            return Toolkit.getToolkit().createSVGPath2D(this);
        }
        catch (RuntimeException runtimeException) {
            Logging.getJavaFXLogger().warning("Failed to configure svg path \"{0}\": {1}", this.getContent(), runtimeException.getMessage());
            return Toolkit.getToolkit().createSVGPath2D(new SVGPath());
        }
    }

    private Object createSVGPathObject() {
        try {
            return Toolkit.getToolkit().createSVGPathObject(this);
        }
        catch (RuntimeException runtimeException) {
            Logging.getJavaFXLogger().warning("Failed to configure svg path \"{0}\": {1}", this.getContent(), runtimeException.getMessage());
            return Toolkit.getToolkit().createSVGPathObject(new SVGPath());
        }
    }
}

