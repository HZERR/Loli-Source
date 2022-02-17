/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.behavior.ToolBarBehavior;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import com.sun.javafx.scene.control.skin.resources.ControlResources;
import com.sun.javafx.scene.traversal.Algorithm;
import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.scene.traversal.ParentTraversalEngine;
import com.sun.javafx.scene.traversal.TraversalContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.event.EventTarget;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.SkinBase;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ToolBarSkin
extends BehaviorSkinBase<ToolBar, ToolBarBehavior> {
    private Pane box;
    private ToolBarOverflowMenu overflowMenu;
    private boolean overflow = false;
    private double previousWidth = 0.0;
    private double previousHeight = 0.0;
    private double savedPrefWidth = 0.0;
    private double savedPrefHeight = 0.0;
    private ObservableList<MenuItem> overflowMenuItems = FXCollections.observableArrayList();
    private boolean needsUpdate = false;
    private final ParentTraversalEngine engine;
    private DoubleProperty spacing;
    private ObjectProperty<Pos> boxAlignment;

    public ToolBarSkin(ToolBar toolBar) {
        super(toolBar, new ToolBarBehavior(toolBar));
        this.initialize();
        this.registerChangeListener(toolBar.orientationProperty(), "ORIENTATION");
        this.engine = new ParentTraversalEngine((Parent)((Object)this.getSkinnable()), new Algorithm(){

            private Node selectPrev(int n2, TraversalContext traversalContext) {
                for (int i2 = n2; i2 >= 0; --i2) {
                    Node node;
                    Node node2 = (Node)ToolBarSkin.this.box.getChildren().get(i2);
                    if (node2.isDisabled() || !node2.impl_isTreeVisible()) continue;
                    if (node2 instanceof Parent && (node = traversalContext.selectLastInParent((Parent)node2)) != null) {
                        return node;
                    }
                    if (!node2.isFocusTraversable()) continue;
                    return node2;
                }
                return null;
            }

            private Node selectNext(int n2, TraversalContext traversalContext) {
                int n3 = ToolBarSkin.this.box.getChildren().size();
                for (int i2 = n2; i2 < n3; ++i2) {
                    Node node;
                    Node node2 = (Node)ToolBarSkin.this.box.getChildren().get(i2);
                    if (node2.isDisabled() || !node2.impl_isTreeVisible()) continue;
                    if (node2.isFocusTraversable()) {
                        return node2;
                    }
                    if (!(node2 instanceof Parent) || (node = traversalContext.selectFirstInParent((Parent)node2)) == null) continue;
                    return node;
                }
                return null;
            }

            @Override
            public Node select(Node node, Direction direction, TraversalContext traversalContext) {
                Node node2;
                int n2;
                ObservableList<Node> observableList = ToolBarSkin.this.box.getChildren();
                if (node == ToolBarSkin.this.overflowMenu) {
                    if (direction.isForward()) {
                        return null;
                    }
                    Node node3 = this.selectPrev(observableList.size() - 1, traversalContext);
                    if (node3 != null) {
                        return node3;
                    }
                }
                if ((n2 = observableList.indexOf(node)) < 0) {
                    node2 = node.getParent();
                    while (!observableList.contains(node2)) {
                        node2 = node2.getParent();
                    }
                    Node node4 = traversalContext.selectInSubtree((Parent)node2, node, direction);
                    if (node4 != null) {
                        return node4;
                    }
                    n2 = observableList.indexOf(node);
                    if (direction == Direction.NEXT) {
                        direction = Direction.NEXT_IN_LINE;
                    }
                }
                if (n2 >= 0) {
                    if (direction.isForward()) {
                        node2 = this.selectNext(n2 + 1, traversalContext);
                        if (node2 != null) {
                            return node2;
                        }
                        if (ToolBarSkin.this.overflow) {
                            ToolBarSkin.this.overflowMenu.requestFocus();
                            return ToolBarSkin.this.overflowMenu;
                        }
                    } else {
                        node2 = this.selectPrev(n2 - 1, traversalContext);
                        if (node2 != null) {
                            return node2;
                        }
                    }
                }
                return null;
            }

            @Override
            public Node selectFirst(TraversalContext traversalContext) {
                Node node = this.selectNext(0, traversalContext);
                if (node != null) {
                    return node;
                }
                if (ToolBarSkin.this.overflow) {
                    return ToolBarSkin.this.overflowMenu;
                }
                return null;
            }

            @Override
            public Node selectLast(TraversalContext traversalContext) {
                if (ToolBarSkin.this.overflow) {
                    return ToolBarSkin.this.overflowMenu;
                }
                return this.selectPrev(ToolBarSkin.this.box.getChildren().size() - 1, traversalContext);
            }
        });
        ((ToolBar)this.getSkinnable()).setImpl_traversalEngine(this.engine);
        toolBar.focusedProperty().addListener((observableValue, bl, bl2) -> {
            if (bl2.booleanValue()) {
                if (!this.box.getChildren().isEmpty()) {
                    ((Node)this.box.getChildren().get(0)).requestFocus();
                } else {
                    this.overflowMenu.requestFocus();
                }
            }
        });
        toolBar.getItems().addListener(change -> {
            while (change.next()) {
                for (Node node : change.getRemoved()) {
                    this.box.getChildren().remove(node);
                }
                this.box.getChildren().addAll(change.getAddedSubList());
            }
            this.needsUpdate = true;
            ((ToolBar)this.getSkinnable()).requestLayout();
        });
    }

    public final void setSpacing(double d2) {
        this.spacingProperty().set(this.snapSpace(d2));
    }

    public final double getSpacing() {
        return this.spacing == null ? 0.0 : this.snapSpace(this.spacing.get());
    }

    public final DoubleProperty spacingProperty() {
        if (this.spacing == null) {
            this.spacing = new StyleableDoubleProperty(){

                @Override
                protected void invalidated() {
                    double d2 = this.get();
                    if (((ToolBar)ToolBarSkin.this.getSkinnable()).getOrientation() == Orientation.VERTICAL) {
                        ((VBox)ToolBarSkin.this.box).setSpacing(d2);
                    } else {
                        ((HBox)ToolBarSkin.this.box).setSpacing(d2);
                    }
                }

                @Override
                public Object getBean() {
                    return ToolBarSkin.this;
                }

                @Override
                public String getName() {
                    return "spacing";
                }

                @Override
                public CssMetaData<ToolBar, Number> getCssMetaData() {
                    return StyleableProperties.SPACING;
                }
            };
        }
        return this.spacing;
    }

    public final void setBoxAlignment(Pos pos) {
        this.boxAlignmentProperty().set(pos);
    }

    public final Pos getBoxAlignment() {
        return this.boxAlignment == null ? Pos.TOP_LEFT : (Pos)((Object)this.boxAlignment.get());
    }

    public final ObjectProperty<Pos> boxAlignmentProperty() {
        if (this.boxAlignment == null) {
            this.boxAlignment = new StyleableObjectProperty<Pos>(Pos.TOP_LEFT){

                @Override
                public void invalidated() {
                    Pos pos = (Pos)((Object)this.get());
                    if (((ToolBar)ToolBarSkin.this.getSkinnable()).getOrientation() == Orientation.VERTICAL) {
                        ((VBox)ToolBarSkin.this.box).setAlignment(pos);
                    } else {
                        ((HBox)ToolBarSkin.this.box).setAlignment(pos);
                    }
                }

                @Override
                public Object getBean() {
                    return ToolBarSkin.this;
                }

                @Override
                public String getName() {
                    return "boxAlignment";
                }

                @Override
                public CssMetaData<ToolBar, Pos> getCssMetaData() {
                    return StyleableProperties.ALIGNMENT;
                }
            };
        }
        return this.boxAlignment;
    }

    @Override
    protected void handleControlPropertyChanged(String string) {
        super.handleControlPropertyChanged(string);
        if ("ORIENTATION".equals(string)) {
            this.initialize();
        }
    }

    @Override
    protected double computeMinWidth(double d2, double d3, double d4, double d5, double d6) {
        ToolBar toolBar = (ToolBar)this.getSkinnable();
        return toolBar.getOrientation() == Orientation.VERTICAL ? this.computePrefWidth(-1.0, d3, d4, d5, d6) : this.snapSize(this.overflowMenu.prefWidth(-1.0)) + d6 + d4;
    }

    @Override
    protected double computeMinHeight(double d2, double d3, double d4, double d5, double d6) {
        ToolBar toolBar = (ToolBar)this.getSkinnable();
        return toolBar.getOrientation() == Orientation.VERTICAL ? this.snapSize(this.overflowMenu.prefHeight(-1.0)) + d3 + d5 : this.computePrefHeight(-1.0, d3, d4, d5, d6);
    }

    @Override
    protected double computePrefWidth(double d2, double d3, double d4, double d5, double d6) {
        double d7 = 0.0;
        ToolBar toolBar = (ToolBar)this.getSkinnable();
        if (toolBar.getOrientation() == Orientation.HORIZONTAL) {
            for (Node node : toolBar.getItems()) {
                d7 += this.snapSize(node.prefWidth(-1.0)) + this.getSpacing();
            }
            d7 -= this.getSpacing();
        } else {
            for (Node node : toolBar.getItems()) {
                d7 = Math.max(d7, this.snapSize(node.prefWidth(-1.0)));
            }
            if (toolBar.getItems().size() > 0) {
                this.savedPrefWidth = d7;
            } else {
                d7 = this.savedPrefWidth;
            }
        }
        return d6 + d7 + d4;
    }

    @Override
    protected double computePrefHeight(double d2, double d3, double d4, double d5, double d6) {
        double d7 = 0.0;
        ToolBar toolBar = (ToolBar)this.getSkinnable();
        if (toolBar.getOrientation() == Orientation.VERTICAL) {
            for (Node node : toolBar.getItems()) {
                d7 += this.snapSize(node.prefHeight(-1.0)) + this.getSpacing();
            }
            d7 -= this.getSpacing();
        } else {
            for (Node node : toolBar.getItems()) {
                d7 = Math.max(d7, this.snapSize(node.prefHeight(-1.0)));
            }
            if (toolBar.getItems().size() > 0) {
                this.savedPrefHeight = d7;
            } else {
                d7 = this.savedPrefHeight;
            }
        }
        return d3 + d7 + d5;
    }

    @Override
    protected double computeMaxWidth(double d2, double d3, double d4, double d5, double d6) {
        return ((ToolBar)this.getSkinnable()).getOrientation() == Orientation.VERTICAL ? this.snapSize(((ToolBar)this.getSkinnable()).prefWidth(-1.0)) : Double.MAX_VALUE;
    }

    @Override
    protected double computeMaxHeight(double d2, double d3, double d4, double d5, double d6) {
        return ((ToolBar)this.getSkinnable()).getOrientation() == Orientation.VERTICAL ? Double.MAX_VALUE : this.snapSize(((ToolBar)this.getSkinnable()).prefHeight(-1.0));
    }

    @Override
    protected void layoutChildren(double d2, double d3, double d4, double d5) {
        ToolBar toolBar = (ToolBar)this.getSkinnable();
        if (toolBar.getOrientation() == Orientation.VERTICAL) {
            if (this.snapSize(toolBar.getHeight()) != this.previousHeight || this.needsUpdate) {
                ((VBox)this.box).setSpacing(this.getSpacing());
                ((VBox)this.box).setAlignment(this.getBoxAlignment());
                this.previousHeight = this.snapSize(toolBar.getHeight());
                this.addNodesToToolBar();
            }
        } else if (this.snapSize(toolBar.getWidth()) != this.previousWidth || this.needsUpdate) {
            ((HBox)this.box).setSpacing(this.getSpacing());
            ((HBox)this.box).setAlignment(this.getBoxAlignment());
            this.previousWidth = this.snapSize(toolBar.getWidth());
            this.addNodesToToolBar();
        }
        this.needsUpdate = false;
        double d6 = d4;
        double d7 = d5;
        if (((ToolBar)this.getSkinnable()).getOrientation() == Orientation.VERTICAL) {
            d7 -= this.overflow ? this.snapSize(this.overflowMenu.prefHeight(-1.0)) : 0.0;
        } else {
            d6 -= this.overflow ? this.snapSize(this.overflowMenu.prefWidth(-1.0)) : 0.0;
        }
        this.box.resize(d6, d7);
        this.positionInArea(this.box, d2, d3, d6, d7, 0.0, HPos.CENTER, VPos.CENTER);
        if (this.overflow) {
            double d8 = this.snapSize(this.overflowMenu.prefWidth(-1.0));
            double d9 = this.snapSize(this.overflowMenu.prefHeight(-1.0));
            double d10 = d2;
            double d11 = d2;
            if (((ToolBar)this.getSkinnable()).getOrientation() == Orientation.VERTICAL) {
                HPos hPos;
                if (d6 == 0.0) {
                    d6 = this.savedPrefWidth;
                }
                d10 = HPos.LEFT.equals((Object)(hPos = ((VBox)this.box).getAlignment().getHpos())) ? d2 + Math.abs((d6 - d8) / 2.0) : (HPos.RIGHT.equals((Object)hPos) ? this.snapSize(toolBar.getWidth()) - this.snappedRightInset() - d6 + Math.abs((d6 - d8) / 2.0) : d2 + Math.abs((this.snapSize(toolBar.getWidth()) - d2 + this.snappedRightInset() - d8) / 2.0));
                d11 = this.snapSize(toolBar.getHeight()) - d9 - d3;
            } else {
                VPos vPos;
                if (d7 == 0.0) {
                    d7 = this.savedPrefHeight;
                }
                d11 = VPos.TOP.equals((Object)(vPos = ((HBox)this.box).getAlignment().getVpos())) ? d3 + Math.abs((d7 - d9) / 2.0) : (VPos.BOTTOM.equals((Object)vPos) ? this.snapSize(toolBar.getHeight()) - this.snappedBottomInset() - d7 + Math.abs((d7 - d9) / 2.0) : d3 + Math.abs((d7 - d9) / 2.0));
                d10 = this.snapSize(toolBar.getWidth()) - d8 - this.snappedRightInset();
            }
            this.overflowMenu.resize(d8, d9);
            this.positionInArea(this.overflowMenu, d10, d11, d8, d9, 0.0, HPos.CENTER, VPos.CENTER);
        }
    }

    private void initialize() {
        this.box = ((ToolBar)this.getSkinnable()).getOrientation() == Orientation.VERTICAL ? new VBox() : new HBox();
        this.box.getStyleClass().add("container");
        this.box.getChildren().addAll((Collection<Node>)((ToolBar)this.getSkinnable()).getItems());
        this.overflowMenu = new ToolBarOverflowMenu(this.overflowMenuItems);
        this.overflowMenu.setVisible(false);
        this.overflowMenu.setManaged(false);
        this.getChildren().clear();
        this.getChildren().add(this.box);
        this.getChildren().add(this.overflowMenu);
        this.previousWidth = 0.0;
        this.previousHeight = 0.0;
        this.savedPrefWidth = 0.0;
        this.savedPrefHeight = 0.0;
        this.needsUpdate = true;
        ((ToolBar)this.getSkinnable()).requestLayout();
    }

    private void addNodesToToolBar() {
        ToolBar toolBar = (ToolBar)this.getSkinnable();
        double d2 = 0.0;
        d2 = ((ToolBar)this.getSkinnable()).getOrientation() == Orientation.VERTICAL ? this.snapSize(toolBar.getHeight()) - this.snappedTopInset() - this.snappedBottomInset() + this.getSpacing() : this.snapSize(toolBar.getWidth()) - this.snappedLeftInset() - this.snappedRightInset() + this.getSpacing();
        double d3 = 0.0;
        boolean bl = false;
        for (Node node : ((ToolBar)this.getSkinnable()).getItems()) {
            d3 = ((ToolBar)this.getSkinnable()).getOrientation() == Orientation.VERTICAL ? (d3 += this.snapSize(node.prefHeight(-1.0)) + this.getSpacing()) : (d3 += this.snapSize(node.prefWidth(-1.0)) + this.getSpacing());
            if (!(d3 > d2)) continue;
            bl = true;
            break;
        }
        if (bl) {
            d2 = ((ToolBar)this.getSkinnable()).getOrientation() == Orientation.VERTICAL ? (d2 -= this.snapSize(this.overflowMenu.prefHeight(-1.0))) : (d2 -= this.snapSize(this.overflowMenu.prefWidth(-1.0)));
            d2 -= this.getSpacing();
        }
        d3 = 0.0;
        this.overflowMenuItems.clear();
        this.box.getChildren().clear();
        Object object = ((ToolBar)this.getSkinnable()).getItems().iterator();
        while (object.hasNext()) {
            String string;
            EventTarget eventTarget;
            Node node;
            node = (Node)object.next();
            node.getStyleClass().remove("menu-item");
            node.getStyleClass().remove("custom-menu-item");
            d3 = ((ToolBar)this.getSkinnable()).getOrientation() == Orientation.VERTICAL ? (d3 += this.snapSize(node.prefHeight(-1.0)) + this.getSpacing()) : (d3 += this.snapSize(node.prefWidth(-1.0)) + this.getSpacing());
            if (d3 <= d2) {
                this.box.getChildren().add(node);
                continue;
            }
            if (node.isFocused()) {
                if (!this.box.getChildren().isEmpty()) {
                    eventTarget = this.engine.selectLast();
                    if (eventTarget != null) {
                        ((Node)eventTarget).requestFocus();
                    }
                } else {
                    this.overflowMenu.requestFocus();
                }
            }
            if (node instanceof Separator) {
                this.overflowMenuItems.add(new SeparatorMenuItem());
                continue;
            }
            eventTarget = new CustomMenuItem(node);
            switch (string = node.getTypeSelector()) {
                case "Button": 
                case "Hyperlink": 
                case "Label": {
                    ((CustomMenuItem)eventTarget).setHideOnClick(true);
                    break;
                }
                case "CheckBox": 
                case "ChoiceBox": 
                case "ColorPicker": 
                case "ComboBox": 
                case "DatePicker": 
                case "MenuButton": 
                case "PasswordField": 
                case "RadioButton": 
                case "ScrollBar": 
                case "ScrollPane": 
                case "Slider": 
                case "SplitMenuButton": 
                case "SplitPane": 
                case "TextArea": 
                case "TextField": 
                case "ToggleButton": 
                case "ToolBar": {
                    ((CustomMenuItem)eventTarget).setHideOnClick(false);
                }
            }
            this.overflowMenuItems.add((MenuItem)eventTarget);
        }
        boolean bl2 = this.overflow = this.overflowMenuItems.size() > 0;
        if (!this.overflow && this.overflowMenu.isFocused() && (object = this.engine.selectLast()) != null) {
            ((Node)object).requestFocus();
        }
        this.overflowMenu.setVisible(this.overflow);
        this.overflowMenu.setManaged(this.overflow);
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return ToolBarSkin.getClassCssMetaData();
    }

    @Override
    protected Object queryAccessibleAttribute(AccessibleAttribute accessibleAttribute, Object ... arrobject) {
        switch (accessibleAttribute) {
            case OVERFLOW_BUTTON: {
                return this.overflowMenu;
            }
        }
        return super.queryAccessibleAttribute(accessibleAttribute, arrobject);
    }

    @Override
    protected void executeAccessibleAction(AccessibleAction accessibleAction, Object ... arrobject) {
        switch (accessibleAction) {
            case SHOW_MENU: {
                this.overflowMenu.fire();
                break;
            }
            default: {
                super.executeAccessibleAction(accessibleAction, arrobject);
            }
        }
    }

    private static class StyleableProperties {
        private static final CssMetaData<ToolBar, Number> SPACING = new CssMetaData<ToolBar, Number>("-fx-spacing", SizeConverter.getInstance(), (Number)0.0){

            @Override
            public boolean isSettable(ToolBar toolBar) {
                ToolBarSkin toolBarSkin = (ToolBarSkin)toolBar.getSkin();
                return toolBarSkin.spacing == null || !toolBarSkin.spacing.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(ToolBar toolBar) {
                ToolBarSkin toolBarSkin = (ToolBarSkin)toolBar.getSkin();
                return (StyleableProperty)((Object)toolBarSkin.spacingProperty());
            }
        };
        private static final CssMetaData<ToolBar, Pos> ALIGNMENT = new CssMetaData<ToolBar, Pos>("-fx-alignment", new EnumConverter<Pos>(Pos.class), Pos.TOP_LEFT){

            @Override
            public boolean isSettable(ToolBar toolBar) {
                ToolBarSkin toolBarSkin = (ToolBarSkin)toolBar.getSkin();
                return toolBarSkin.boxAlignment == null || !toolBarSkin.boxAlignment.isBound();
            }

            @Override
            public StyleableProperty<Pos> getStyleableProperty(ToolBar toolBar) {
                ToolBarSkin toolBarSkin = (ToolBarSkin)toolBar.getSkin();
                return (StyleableProperty)((Object)toolBarSkin.boxAlignmentProperty());
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            ArrayList arrayList = new ArrayList(SkinBase.getClassCssMetaData());
            String string = ALIGNMENT.getProperty();
            int n2 = arrayList.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                CssMetaData cssMetaData = (CssMetaData)arrayList.get(i2);
                if (!string.equals(cssMetaData.getProperty())) continue;
                arrayList.remove(cssMetaData);
            }
            arrayList.add(SPACING);
            arrayList.add(ALIGNMENT);
            STYLEABLES = Collections.unmodifiableList(arrayList);
        }
    }

    class ToolBarOverflowMenu
    extends StackPane {
        private StackPane downArrow;
        private ContextMenu popup;
        private ObservableList<MenuItem> menuItems;

        public ToolBarOverflowMenu(ObservableList<MenuItem> observableList) {
            this.getStyleClass().setAll("tool-bar-overflow-button");
            this.setAccessibleRole(AccessibleRole.BUTTON);
            this.setAccessibleText(ControlResources.getString("Accessibility.title.ToolBar.OverflowButton"));
            this.setFocusTraversable(true);
            this.menuItems = observableList;
            this.downArrow = new StackPane();
            this.downArrow.getStyleClass().setAll("arrow");
            this.downArrow.setOnMousePressed(mouseEvent -> this.fire());
            this.setOnKeyPressed(keyEvent -> {
                if (KeyCode.SPACE.equals((Object)keyEvent.getCode())) {
                    if (!this.popup.isShowing()) {
                        this.popup.getItems().clear();
                        this.popup.getItems().addAll((Collection<MenuItem>)this.menuItems);
                        this.popup.show(this.downArrow, Side.BOTTOM, 0.0, 0.0);
                    }
                    keyEvent.consume();
                } else if (KeyCode.ESCAPE.equals((Object)keyEvent.getCode())) {
                    if (this.popup.isShowing()) {
                        this.popup.hide();
                    }
                    keyEvent.consume();
                } else if (KeyCode.ENTER.equals((Object)keyEvent.getCode())) {
                    this.fire();
                    keyEvent.consume();
                }
            });
            this.visibleProperty().addListener((observableValue, bl, bl2) -> {
                if (bl2.booleanValue() && ToolBarSkin.this.box.getChildren().isEmpty()) {
                    this.setFocusTraversable(true);
                }
            });
            this.popup = new ContextMenu();
            this.setVisible(false);
            this.setManaged(false);
            this.getChildren().add(this.downArrow);
        }

        private void fire() {
            if (this.popup.isShowing()) {
                this.popup.hide();
            } else {
                this.popup.getItems().clear();
                this.popup.getItems().addAll((Collection<MenuItem>)this.menuItems);
                this.popup.show(this.downArrow, Side.BOTTOM, 0.0, 0.0);
            }
        }

        @Override
        protected double computePrefWidth(double d2) {
            return this.snappedLeftInset() + this.snappedRightInset();
        }

        @Override
        protected double computePrefHeight(double d2) {
            return this.snappedTopInset() + this.snappedBottomInset();
        }

        @Override
        protected void layoutChildren() {
            double d2 = this.snapSize(this.downArrow.prefWidth(-1.0));
            double d3 = this.snapSize(this.downArrow.prefHeight(-1.0));
            double d4 = (this.snapSize(this.getWidth()) - d2) / 2.0;
            double d5 = (this.snapSize(this.getHeight()) - d3) / 2.0;
            if (((ToolBar)ToolBarSkin.this.getSkinnable()).getOrientation() == Orientation.VERTICAL) {
                this.downArrow.setRotate(0.0);
            }
            this.downArrow.resize(d2, d3);
            this.positionInArea(this.downArrow, d4, d5, d2, d3, 0.0, HPos.CENTER, VPos.CENTER);
        }

        @Override
        public void executeAccessibleAction(AccessibleAction accessibleAction, Object ... arrobject) {
            switch (accessibleAction) {
                case FIRE: {
                    this.fire();
                    break;
                }
                default: {
                    super.executeAccessibleAction(accessibleAction, new Object[0]);
                }
            }
        }
    }
}

