/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.shape;

import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.scene.shape.PathUtils;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGPath;
import java.util.Collection;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.StyleableProperty;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.Shape;

public class Path
extends Shape {
    private Path2D path2d = null;
    private ObjectProperty<FillRule> fillRule;
    private boolean isPathValid;
    private final ObservableList<PathElement> elements;

    public Path() {
        ((StyleableProperty)((Object)this.fillProperty())).applyStyle(null, null);
        ((StyleableProperty)((Object)this.strokeProperty())).applyStyle(null, Color.BLACK);
        this.elements = new TrackableObservableList<PathElement>(){

            @Override
            protected void onChanged(ListChangeListener.Change<PathElement> change) {
                ObservableList<PathElement> observableList = change.getList();
                boolean bl = false;
                while (change.next()) {
                    int n2;
                    List<PathElement> list = change.getRemoved();
                    for (n2 = 0; n2 < change.getRemovedSize(); ++n2) {
                        list.get(n2).removeNode(Path.this);
                    }
                    for (n2 = change.getFrom(); n2 < change.getTo(); ++n2) {
                        ((PathElement)observableList.get(n2)).addNode(Path.this);
                    }
                    bl |= change.getFrom() == 0;
                }
                if (Path.this.path2d != null) {
                    change.reset();
                    change.next();
                    if (change.getFrom() == change.getList().size() && !change.wasRemoved() && change.wasAdded()) {
                        for (int i2 = change.getFrom(); i2 < change.getTo(); ++i2) {
                            ((PathElement)observableList.get(i2)).impl_addTo(Path.this.path2d);
                        }
                    } else {
                        Path.this.path2d = null;
                    }
                }
                if (bl) {
                    Path.this.isPathValid = Path.this.impl_isFirstPathElementValid();
                }
                Path.this.impl_markDirty(DirtyBits.NODE_CONTENTS);
                Path.this.impl_geomChanged();
            }
        };
    }

    public Path(PathElement ... arrpathElement) {
        ((StyleableProperty)((Object)this.fillProperty())).applyStyle(null, null);
        ((StyleableProperty)((Object)this.strokeProperty())).applyStyle(null, Color.BLACK);
        this.elements = new /* invalid duplicate definition of identical inner class */;
        if (arrpathElement != null) {
            this.elements.addAll(arrpathElement);
        }
    }

    public Path(Collection<? extends PathElement> collection) {
        ((StyleableProperty)((Object)this.fillProperty())).applyStyle(null, null);
        ((StyleableProperty)((Object)this.strokeProperty())).applyStyle(null, Color.BLACK);
        this.elements = new /* invalid duplicate definition of identical inner class */;
        if (collection != null) {
            this.elements.addAll(collection);
        }
    }

    void markPathDirty() {
        this.path2d = null;
        this.impl_markDirty(DirtyBits.NODE_CONTENTS);
        this.impl_geomChanged();
    }

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
                    Path.this.impl_markDirty(DirtyBits.NODE_CONTENTS);
                    Path.this.impl_geomChanged();
                }

                @Override
                public Object getBean() {
                    return Path.this;
                }

                @Override
                public String getName() {
                    return "fillRule";
                }
            };
        }
        return this.fillRule;
    }

    public final ObservableList<PathElement> getElements() {
        return this.elements;
    }

    @Override
    @Deprecated
    protected NGNode impl_createPeer() {
        return new NGPath();
    }

    @Override
    @Deprecated
    public Path2D impl_configShape() {
        if (this.isPathValid) {
            if (this.path2d == null) {
                this.path2d = PathUtils.configShape(this.getElements(), this.getFillRule() == FillRule.EVEN_ODD);
            } else {
                this.path2d.setWindingRule(this.getFillRule() == FillRule.NON_ZERO ? 1 : 0);
            }
            return this.path2d;
        }
        return new Path2D();
    }

    @Override
    @Deprecated
    protected Bounds impl_computeLayoutBounds() {
        if (this.isPathValid) {
            return super.impl_computeLayoutBounds();
        }
        return new BoundingBox(0.0, 0.0, -1.0, -1.0);
    }

    private boolean impl_isFirstPathElementValid() {
        ObservableList<PathElement> observableList = this.getElements();
        if (observableList != null && observableList.size() > 0) {
            PathElement pathElement = (PathElement)observableList.get(0);
            if (!pathElement.isAbsolute()) {
                System.err.printf("First element of the path can not be relative. Path: %s\n", this);
                return false;
            }
            if (pathElement instanceof MoveTo) {
                return true;
            }
            System.err.printf("Missing initial moveto in path definition. Path: %s\n", this);
            return false;
        }
        return true;
    }

    @Override
    @Deprecated
    public void impl_updatePeer() {
        super.impl_updatePeer();
        if (this.impl_isDirty(DirtyBits.NODE_CONTENTS)) {
            NGPath nGPath = (NGPath)this.impl_getPeer();
            if (nGPath.acceptsPath2dOnUpdate()) {
                nGPath.updateWithPath2d(this.impl_configShape());
            } else {
                nGPath.reset();
                if (this.isPathValid) {
                    nGPath.setFillRule(this.getFillRule());
                    for (PathElement pathElement : this.getElements()) {
                        pathElement.addTo(nGPath);
                    }
                    nGPath.update();
                }
            }
        }
    }

    @Override
    @Deprecated
    protected Paint impl_cssGetFillInitialValue() {
        return null;
    }

    @Override
    @Deprecated
    protected Paint impl_cssGetStrokeInitialValue() {
        return Color.BLACK;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Path[");
        String string = this.getId();
        if (string != null) {
            stringBuilder.append("id=").append(string).append(", ");
        }
        stringBuilder.append("elements=").append(this.getElements());
        stringBuilder.append(", fill=").append(this.getFill());
        stringBuilder.append(", fillRule=").append((Object)this.getFillRule());
        Paint paint = this.getStroke();
        if (paint != null) {
            stringBuilder.append(", stroke=").append(paint);
            stringBuilder.append(", strokeWidth=").append(this.getStrokeWidth());
        }
        return stringBuilder.append("]").toString();
    }
}

