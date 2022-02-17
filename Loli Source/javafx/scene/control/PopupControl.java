/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.css.CssError;
import com.sun.javafx.css.StyleManager;
import com.sun.javafx.css.converters.StringConverter;
import com.sun.javafx.scene.control.Logging;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.StringProperty;
import javafx.beans.value.WritableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.css.StyleableStringProperty;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.control.Skinnable;
import javafx.scene.layout.Pane;
import javafx.stage.PopupWindow;
import javafx.stage.Window;
import sun.util.logging.PlatformLogger;

public class PopupControl
extends PopupWindow
implements Skinnable,
Styleable {
    public static final double USE_PREF_SIZE = Double.NEGATIVE_INFINITY;
    public static final double USE_COMPUTED_SIZE = -1.0;
    protected CSSBridge bridge;
    private final ObjectProperty<Skin<?>> skin = new ObjectPropertyBase<Skin<?>>(){
        private Skin<?> oldValue;

        @Override
        public void set(Skin<?> skin) {
            if (skin == null ? this.oldValue == null : this.oldValue != null && skin.getClass().equals(this.oldValue.getClass())) {
                return;
            }
            super.set(skin);
        }

        @Override
        protected void invalidated() {
            Skin skin = (Skin)this.get();
            PopupControl.this.currentSkinClassName = skin == null ? null : skin.getClass().getName();
            PopupControl.this.skinClassNameProperty().set(PopupControl.this.currentSkinClassName);
            if (this.oldValue != null) {
                this.oldValue.dispose();
            }
            this.oldValue = (Skin)this.getValue();
            PopupControl.this.prefWidthCache = -1.0;
            PopupControl.this.prefHeightCache = -1.0;
            PopupControl.this.minWidthCache = -1.0;
            PopupControl.this.minHeightCache = -1.0;
            PopupControl.this.maxWidthCache = -1.0;
            PopupControl.this.maxHeightCache = -1.0;
            PopupControl.this.skinSizeComputed = false;
            Node node = PopupControl.this.getSkinNode();
            if (node != null) {
                PopupControl.this.bridge.getChildren().setAll(node);
            } else {
                PopupControl.this.bridge.getChildren().clear();
            }
            PopupControl.this.bridge.impl_reapplyCSS();
            PlatformLogger platformLogger = Logging.getControlsLogger();
            if (platformLogger.isLoggable(PlatformLogger.Level.FINEST)) {
                platformLogger.finest("Stored skin[" + this.getValue() + "] on " + this);
            }
        }

        @Override
        public Object getBean() {
            return PopupControl.this;
        }

        @Override
        public String getName() {
            return "skin";
        }
    };
    private String currentSkinClassName = null;
    private StringProperty skinClassName = null;
    private DoubleProperty minWidth;
    private DoubleProperty minHeight;
    private DoubleProperty prefWidth;
    private DoubleProperty prefHeight;
    private DoubleProperty maxWidth;
    private DoubleProperty maxHeight;
    private double prefWidthCache = -1.0;
    private double prefHeightCache = -1.0;
    private double minWidthCache = -1.0;
    private double minHeightCache = -1.0;
    private double maxWidthCache = -1.0;
    private double maxHeightCache = -1.0;
    private boolean skinSizeComputed = false;
    private static final CssMetaData<CSSBridge, String> SKIN;
    private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

    public PopupControl() {
        this.bridge = new CSSBridge();
        this.setAnchorLocation(PopupWindow.AnchorLocation.CONTENT_TOP_LEFT);
        this.getContent().add(this.bridge);
    }

    public final StringProperty idProperty() {
        return this.bridge.idProperty();
    }

    public final void setId(String string) {
        this.idProperty().set(string);
    }

    @Override
    public final String getId() {
        return (String)this.idProperty().get();
    }

    @Override
    public final ObservableList<String> getStyleClass() {
        return this.bridge.getStyleClass();
    }

    public final void setStyle(String string) {
        this.styleProperty().set(string);
    }

    @Override
    public final String getStyle() {
        return (String)this.styleProperty().get();
    }

    public final StringProperty styleProperty() {
        return this.bridge.styleProperty();
    }

    @Override
    public final ObjectProperty<Skin<?>> skinProperty() {
        return this.skin;
    }

    @Override
    public final void setSkin(Skin<?> skin) {
        this.skinProperty().setValue(skin);
    }

    @Override
    public final Skin<?> getSkin() {
        return (Skin)this.skinProperty().getValue();
    }

    private StringProperty skinClassNameProperty() {
        if (this.skinClassName == null) {
            this.skinClassName = new StyleableStringProperty(){

                @Override
                public void set(String string) {
                    if (string == null || string.isEmpty() || string.equals(this.get())) {
                        return;
                    }
                    super.set(string);
                }

                @Override
                public void invalidated() {
                    if (this.get() != null && !this.get().equals(PopupControl.this.currentSkinClassName)) {
                        Control.loadSkinClass(PopupControl.this, this.get());
                    }
                }

                @Override
                public Object getBean() {
                    return PopupControl.this;
                }

                @Override
                public String getName() {
                    return "skinClassName";
                }

                @Override
                public CssMetaData<CSSBridge, String> getCssMetaData() {
                    return SKIN;
                }
            };
        }
        return this.skinClassName;
    }

    private Node getSkinNode() {
        return this.getSkin() == null ? null : this.getSkin().getNode();
    }

    public final void setMinWidth(double d2) {
        this.minWidthProperty().set(d2);
    }

    public final double getMinWidth() {
        return this.minWidth == null ? -1.0 : this.minWidth.get();
    }

    public final DoubleProperty minWidthProperty() {
        if (this.minWidth == null) {
            this.minWidth = new DoublePropertyBase(-1.0){

                @Override
                public void invalidated() {
                    if (PopupControl.this.isShowing()) {
                        PopupControl.this.bridge.requestLayout();
                    }
                }

                @Override
                public Object getBean() {
                    return PopupControl.this;
                }

                @Override
                public String getName() {
                    return "minWidth";
                }
            };
        }
        return this.minWidth;
    }

    public final void setMinHeight(double d2) {
        this.minHeightProperty().set(d2);
    }

    public final double getMinHeight() {
        return this.minHeight == null ? -1.0 : this.minHeight.get();
    }

    public final DoubleProperty minHeightProperty() {
        if (this.minHeight == null) {
            this.minHeight = new DoublePropertyBase(-1.0){

                @Override
                public void invalidated() {
                    if (PopupControl.this.isShowing()) {
                        PopupControl.this.bridge.requestLayout();
                    }
                }

                @Override
                public Object getBean() {
                    return PopupControl.this;
                }

                @Override
                public String getName() {
                    return "minHeight";
                }
            };
        }
        return this.minHeight;
    }

    public void setMinSize(double d2, double d3) {
        this.setMinWidth(d2);
        this.setMinHeight(d3);
    }

    public final void setPrefWidth(double d2) {
        this.prefWidthProperty().set(d2);
    }

    public final double getPrefWidth() {
        return this.prefWidth == null ? -1.0 : this.prefWidth.get();
    }

    public final DoubleProperty prefWidthProperty() {
        if (this.prefWidth == null) {
            this.prefWidth = new DoublePropertyBase(-1.0){

                @Override
                public void invalidated() {
                    if (PopupControl.this.isShowing()) {
                        PopupControl.this.bridge.requestLayout();
                    }
                }

                @Override
                public Object getBean() {
                    return PopupControl.this;
                }

                @Override
                public String getName() {
                    return "prefWidth";
                }
            };
        }
        return this.prefWidth;
    }

    public final void setPrefHeight(double d2) {
        this.prefHeightProperty().set(d2);
    }

    public final double getPrefHeight() {
        return this.prefHeight == null ? -1.0 : this.prefHeight.get();
    }

    public final DoubleProperty prefHeightProperty() {
        if (this.prefHeight == null) {
            this.prefHeight = new DoublePropertyBase(-1.0){

                @Override
                public void invalidated() {
                    if (PopupControl.this.isShowing()) {
                        PopupControl.this.bridge.requestLayout();
                    }
                }

                @Override
                public Object getBean() {
                    return PopupControl.this;
                }

                @Override
                public String getName() {
                    return "prefHeight";
                }
            };
        }
        return this.prefHeight;
    }

    public void setPrefSize(double d2, double d3) {
        this.setPrefWidth(d2);
        this.setPrefHeight(d3);
    }

    public final void setMaxWidth(double d2) {
        this.maxWidthProperty().set(d2);
    }

    public final double getMaxWidth() {
        return this.maxWidth == null ? -1.0 : this.maxWidth.get();
    }

    public final DoubleProperty maxWidthProperty() {
        if (this.maxWidth == null) {
            this.maxWidth = new DoublePropertyBase(-1.0){

                @Override
                public void invalidated() {
                    if (PopupControl.this.isShowing()) {
                        PopupControl.this.bridge.requestLayout();
                    }
                }

                @Override
                public Object getBean() {
                    return PopupControl.this;
                }

                @Override
                public String getName() {
                    return "maxWidth";
                }
            };
        }
        return this.maxWidth;
    }

    public final void setMaxHeight(double d2) {
        this.maxHeightProperty().set(d2);
    }

    public final double getMaxHeight() {
        return this.maxHeight == null ? -1.0 : this.maxHeight.get();
    }

    public final DoubleProperty maxHeightProperty() {
        if (this.maxHeight == null) {
            this.maxHeight = new DoublePropertyBase(-1.0){

                @Override
                public void invalidated() {
                    if (PopupControl.this.isShowing()) {
                        PopupControl.this.bridge.requestLayout();
                    }
                }

                @Override
                public Object getBean() {
                    return PopupControl.this;
                }

                @Override
                public String getName() {
                    return "maxHeight";
                }
            };
        }
        return this.maxHeight;
    }

    public void setMaxSize(double d2, double d3) {
        this.setMaxWidth(d2);
        this.setMaxHeight(d3);
    }

    public final double minWidth(double d2) {
        double d3 = this.getMinWidth();
        if (d3 == -1.0) {
            if (this.minWidthCache == -1.0) {
                this.minWidthCache = this.recalculateMinWidth(d2);
            }
            return this.minWidthCache;
        }
        if (d3 == Double.NEGATIVE_INFINITY) {
            return this.prefWidth(d2);
        }
        return d3;
    }

    public final double minHeight(double d2) {
        double d3 = this.getMinHeight();
        if (d3 == -1.0) {
            if (this.minHeightCache == -1.0) {
                this.minHeightCache = this.recalculateMinHeight(d2);
            }
            return this.minHeightCache;
        }
        if (d3 == Double.NEGATIVE_INFINITY) {
            return this.prefHeight(d2);
        }
        return d3;
    }

    public final double prefWidth(double d2) {
        double d3 = this.getPrefWidth();
        if (d3 == -1.0) {
            if (this.prefWidthCache == -1.0) {
                this.prefWidthCache = this.recalculatePrefWidth(d2);
            }
            return this.prefWidthCache;
        }
        if (d3 == Double.NEGATIVE_INFINITY) {
            return this.prefWidth(d2);
        }
        return d3;
    }

    public final double prefHeight(double d2) {
        double d3 = this.getPrefHeight();
        if (d3 == -1.0) {
            if (this.prefHeightCache == -1.0) {
                this.prefHeightCache = this.recalculatePrefHeight(d2);
            }
            return this.prefHeightCache;
        }
        if (d3 == Double.NEGATIVE_INFINITY) {
            return this.prefHeight(d2);
        }
        return d3;
    }

    public final double maxWidth(double d2) {
        double d3 = this.getMaxWidth();
        if (d3 == -1.0) {
            if (this.maxWidthCache == -1.0) {
                this.maxWidthCache = this.recalculateMaxWidth(d2);
            }
            return this.maxWidthCache;
        }
        if (d3 == Double.NEGATIVE_INFINITY) {
            return this.prefWidth(d2);
        }
        return d3;
    }

    public final double maxHeight(double d2) {
        double d3 = this.getMaxHeight();
        if (d3 == -1.0) {
            if (this.maxHeightCache == -1.0) {
                this.maxHeightCache = this.recalculateMaxHeight(d2);
            }
            return this.maxHeightCache;
        }
        if (d3 == Double.NEGATIVE_INFINITY) {
            return this.prefHeight(d2);
        }
        return d3;
    }

    private double recalculateMinWidth(double d2) {
        this.recomputeSkinSize();
        return this.getSkinNode() == null ? 0.0 : this.getSkinNode().minWidth(d2);
    }

    private double recalculateMinHeight(double d2) {
        this.recomputeSkinSize();
        return this.getSkinNode() == null ? 0.0 : this.getSkinNode().minHeight(d2);
    }

    private double recalculateMaxWidth(double d2) {
        this.recomputeSkinSize();
        return this.getSkinNode() == null ? 0.0 : this.getSkinNode().maxWidth(d2);
    }

    private double recalculateMaxHeight(double d2) {
        this.recomputeSkinSize();
        return this.getSkinNode() == null ? 0.0 : this.getSkinNode().maxHeight(d2);
    }

    private double recalculatePrefWidth(double d2) {
        this.recomputeSkinSize();
        return this.getSkinNode() == null ? 0.0 : this.getSkinNode().prefWidth(d2);
    }

    private double recalculatePrefHeight(double d2) {
        this.recomputeSkinSize();
        return this.getSkinNode() == null ? 0.0 : this.getSkinNode().prefHeight(d2);
    }

    private void recomputeSkinSize() {
        if (!this.skinSizeComputed) {
            this.bridge.applyCss();
            this.skinSizeComputed = true;
        }
    }

    protected Skin<?> createDefaultSkin() {
        return null;
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return STYLEABLES;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return PopupControl.getClassCssMetaData();
    }

    public final void pseudoClassStateChanged(PseudoClass pseudoClass, boolean bl) {
        this.bridge.pseudoClassStateChanged(pseudoClass, bl);
    }

    @Override
    public String getTypeSelector() {
        return "PopupControl";
    }

    @Override
    public Styleable getStyleableParent() {
        Scene scene;
        Node node = this.getOwnerNode();
        if (node != null) {
            return node;
        }
        Window window = this.getOwnerWindow();
        if (window != null && (scene = window.getScene()) != null) {
            return scene.getRoot();
        }
        return this.bridge.getParent();
    }

    @Override
    public final ObservableSet<PseudoClass> getPseudoClassStates() {
        return FXCollections.emptyObservableSet();
    }

    @Deprecated
    public Node impl_styleableGetNode() {
        return this.bridge;
    }

    static {
        if (Application.getUserAgentStylesheet() == null) {
            PlatformImpl.setDefaultPlatformUserAgentStylesheet();
        }
        SKIN = new CssMetaData<CSSBridge, String>("-fx-skin", StringConverter.getInstance()){

            @Override
            public boolean isSettable(CSSBridge cSSBridge) {
                return !cSSBridge.popupControl.skinProperty().isBound();
            }

            @Override
            public StyleableProperty<String> getStyleableProperty(CSSBridge cSSBridge) {
                return (StyleableProperty)((Object)cSSBridge.popupControl.skinClassNameProperty());
            }
        };
        ArrayList arrayList = new ArrayList();
        Collections.addAll(arrayList, SKIN);
        STYLEABLES = Collections.unmodifiableList(arrayList);
    }

    protected class CSSBridge
    extends Pane {
        private final PopupControl popupControl;

        protected CSSBridge() {
            this.popupControl = PopupControl.this;
        }

        @Override
        public void requestLayout() {
            PopupControl.this.prefWidthCache = -1.0;
            PopupControl.this.prefHeightCache = -1.0;
            PopupControl.this.minWidthCache = -1.0;
            PopupControl.this.minHeightCache = -1.0;
            PopupControl.this.maxWidthCache = -1.0;
            PopupControl.this.maxHeightCache = -1.0;
            super.requestLayout();
        }

        @Override
        public Styleable getStyleableParent() {
            return PopupControl.this.getStyleableParent();
        }

        @Deprecated
        protected void setSkinClassName(String string) {
        }

        @Override
        public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
            return PopupControl.this.getCssMetaData();
        }

        @Override
        @Deprecated
        public List<String> impl_getAllParentStylesheets() {
            Styleable styleable = this.getStyleableParent();
            if (styleable instanceof Parent) {
                return ((Parent)styleable).impl_getAllParentStylesheets();
            }
            return null;
        }

        @Override
        @Deprecated
        protected void impl_processCSS(WritableValue<Boolean> writableValue) {
            super.impl_processCSS(writableValue);
            if (PopupControl.this.getSkin() == null) {
                Skin<?> skin = PopupControl.this.createDefaultSkin();
                if (skin != null) {
                    PopupControl.this.skinProperty().set(skin);
                    super.impl_processCSS(writableValue);
                } else {
                    String string = "The -fx-skin property has not been defined in CSS for " + this + " and createDefaultSkin() returned null.";
                    ObservableList<CssError> observableList = StyleManager.getErrors();
                    if (observableList != null) {
                        CssError cssError = new CssError(string);
                        observableList.add(cssError);
                    }
                    Logging.getControlsLogger().severe(string);
                }
            }
        }
    }
}

