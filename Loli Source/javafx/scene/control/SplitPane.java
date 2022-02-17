/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.scene.control.skin.SplitPaneSkin;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.WeakHashMap;
import javafx.beans.DefaultProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

@DefaultProperty(value="items")
public class SplitPane
extends Control {
    private static final String RESIZABLE_WITH_PARENT = "resizable-with-parent";
    private ObjectProperty<Orientation> orientation;
    private final ObservableList<Node> items = FXCollections.observableArrayList();
    private final ObservableList<Divider> dividers = FXCollections.observableArrayList();
    private final ObservableList<Divider> unmodifiableDividers = FXCollections.unmodifiableObservableList(this.dividers);
    private final WeakHashMap<Integer, Double> dividerCache = new WeakHashMap();
    private static final String DEFAULT_STYLE_CLASS = "split-pane";
    private static final PseudoClass VERTICAL_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("vertical");
    private static final PseudoClass HORIZONTAL_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("horizontal");

    public static void setResizableWithParent(Node node, Boolean bl) {
        if (bl == null) {
            node.getProperties().remove(RESIZABLE_WITH_PARENT);
        } else {
            node.getProperties().put(RESIZABLE_WITH_PARENT, bl);
        }
    }

    public static Boolean isResizableWithParent(Node node) {
        Object v2;
        if (node.hasProperties() && (v2 = node.getProperties().get(RESIZABLE_WITH_PARENT)) != null) {
            return (Boolean)v2;
        }
        return true;
    }

    public SplitPane() {
        this(null);
    }

    public SplitPane(Node ... arrnode) {
        this.getStyleClass().setAll(DEFAULT_STYLE_CLASS);
        ((StyleableProperty)((Object)this.focusTraversableProperty())).applyStyle(null, Boolean.FALSE);
        this.getItems().addListener(new ListChangeListener<Node>(){

            @Override
            public void onChanged(ListChangeListener.Change<? extends Node> change) {
                int n2;
                while (change.next()) {
                    int n3;
                    int n4 = n2 = change.getFrom();
                    for (n3 = 0; n3 < change.getRemovedSize(); ++n3) {
                        if (n4 < SplitPane.this.dividers.size()) {
                            SplitPane.this.dividerCache.put(n4, Double.MAX_VALUE);
                        } else if (n4 == SplitPane.this.dividers.size() && !SplitPane.this.dividers.isEmpty()) {
                            if (change.wasReplaced()) {
                                SplitPane.this.dividerCache.put(n4 - 1, ((Divider)SplitPane.this.dividers.get(n4 - 1)).getPosition());
                            } else {
                                SplitPane.this.dividerCache.put(n4 - 1, Double.MAX_VALUE);
                            }
                        }
                        ++n4;
                    }
                    for (n3 = 0; n3 < SplitPane.this.dividers.size(); ++n3) {
                        if (SplitPane.this.dividerCache.get(n3) != null) continue;
                        SplitPane.this.dividerCache.put(n3, ((Divider)SplitPane.this.dividers.get(n3)).getPosition());
                    }
                }
                SplitPane.this.dividers.clear();
                for (n2 = 0; n2 < SplitPane.this.getItems().size() - 1; ++n2) {
                    if (SplitPane.this.dividerCache.containsKey(n2) && (Double)SplitPane.this.dividerCache.get(n2) != Double.MAX_VALUE) {
                        Divider divider = new Divider();
                        divider.setPosition((Double)SplitPane.this.dividerCache.get(n2));
                        SplitPane.this.dividers.add(divider);
                    } else {
                        SplitPane.this.dividers.add(new Divider());
                    }
                    SplitPane.this.dividerCache.remove(n2);
                }
            }
        });
        if (arrnode != null) {
            this.getItems().addAll(arrnode);
        }
        this.pseudoClassStateChanged(HORIZONTAL_PSEUDOCLASS_STATE, true);
    }

    public final void setOrientation(Orientation orientation) {
        this.orientationProperty().set(orientation);
    }

    public final Orientation getOrientation() {
        return this.orientation == null ? Orientation.HORIZONTAL : (Orientation)((Object)this.orientation.get());
    }

    public final ObjectProperty<Orientation> orientationProperty() {
        if (this.orientation == null) {
            this.orientation = new StyleableObjectProperty<Orientation>(Orientation.HORIZONTAL){

                @Override
                public void invalidated() {
                    boolean bl = this.get() == Orientation.VERTICAL;
                    SplitPane.this.pseudoClassStateChanged(VERTICAL_PSEUDOCLASS_STATE, bl);
                    SplitPane.this.pseudoClassStateChanged(HORIZONTAL_PSEUDOCLASS_STATE, !bl);
                }

                @Override
                public CssMetaData<SplitPane, Orientation> getCssMetaData() {
                    return StyleableProperties.ORIENTATION;
                }

                @Override
                public Object getBean() {
                    return SplitPane.this;
                }

                @Override
                public String getName() {
                    return "orientation";
                }
            };
        }
        return this.orientation;
    }

    public ObservableList<Node> getItems() {
        return this.items;
    }

    public ObservableList<Divider> getDividers() {
        return this.unmodifiableDividers;
    }

    public void setDividerPosition(int n2, double d2) {
        if (this.getDividers().size() <= n2) {
            this.dividerCache.put(n2, d2);
            return;
        }
        if (n2 >= 0) {
            ((Divider)this.getDividers().get(n2)).setPosition(d2);
        }
    }

    public void setDividerPositions(double ... arrd) {
        if (this.dividers.isEmpty()) {
            for (int i2 = 0; i2 < arrd.length; ++i2) {
                this.dividerCache.put(i2, arrd[i2]);
            }
            return;
        }
        for (int i3 = 0; i3 < arrd.length && i3 < this.dividers.size(); ++i3) {
            ((Divider)this.dividers.get(i3)).setPosition(arrd[i3]);
        }
    }

    public double[] getDividerPositions() {
        double[] arrd = new double[this.dividers.size()];
        for (int i2 = 0; i2 < this.dividers.size(); ++i2) {
            arrd[i2] = ((Divider)this.dividers.get(i2)).getPosition();
        }
        return arrd;
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new SplitPaneSkin(this);
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return SplitPane.getClassCssMetaData();
    }

    @Override
    @Deprecated
    protected Boolean impl_cssGetFocusTraversableInitialValue() {
        return Boolean.FALSE;
    }

    public static class Divider {
        private DoubleProperty position;

        public final void setPosition(double d2) {
            this.positionProperty().set(d2);
        }

        public final double getPosition() {
            return this.position == null ? 0.5 : this.position.get();
        }

        public final DoubleProperty positionProperty() {
            if (this.position == null) {
                this.position = new SimpleDoubleProperty(this, "position", 0.5);
            }
            return this.position;
        }
    }

    private static class StyleableProperties {
        private static final CssMetaData<SplitPane, Orientation> ORIENTATION = new CssMetaData<SplitPane, Orientation>("-fx-orientation", new EnumConverter<Orientation>(Orientation.class), Orientation.HORIZONTAL){

            @Override
            public Orientation getInitialValue(SplitPane splitPane) {
                return splitPane.getOrientation();
            }

            @Override
            public boolean isSettable(SplitPane splitPane) {
                return splitPane.orientation == null || !splitPane.orientation.isBound();
            }

            @Override
            public StyleableProperty<Orientation> getStyleableProperty(SplitPane splitPane) {
                return (StyleableProperty)((Object)splitPane.orientationProperty());
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            ArrayList arrayList = new ArrayList(Control.getClassCssMetaData());
            arrayList.add(ORIENTATION);
            STYLEABLES = Collections.unmodifiableList(arrayList);
        }
    }
}

