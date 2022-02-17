/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.scene.control.MultiplePropertyChangeListenerHandler;
import com.sun.javafx.scene.control.behavior.TabPaneBehavior;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import com.sun.javafx.scene.control.skin.resources.ControlResources;
import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.scene.traversal.TraversalEngine;
import com.sun.javafx.util.Utils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SkinBase;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.SwipeEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class TabPaneSkin
extends BehaviorSkinBase<TabPane, TabPaneBehavior> {
    private ObjectProperty<TabAnimation> openTabAnimation = new StyleableObjectProperty<TabAnimation>(TabAnimation.GROW){

        @Override
        public CssMetaData<TabPane, TabAnimation> getCssMetaData() {
            return StyleableProperties.OPEN_TAB_ANIMATION;
        }

        @Override
        public Object getBean() {
            return TabPaneSkin.this;
        }

        @Override
        public String getName() {
            return "openTabAnimation";
        }
    };
    private ObjectProperty<TabAnimation> closeTabAnimation = new StyleableObjectProperty<TabAnimation>(TabAnimation.GROW){

        @Override
        public CssMetaData<TabPane, TabAnimation> getCssMetaData() {
            return StyleableProperties.CLOSE_TAB_ANIMATION;
        }

        @Override
        public Object getBean() {
            return TabPaneSkin.this;
        }

        @Override
        public String getName() {
            return "closeTabAnimation";
        }
    };
    private static final double ANIMATION_SPEED = 150.0;
    private static final int SPACER = 10;
    private TabHeaderArea tabHeaderArea;
    private ObservableList<TabContentRegion> tabContentRegions;
    private Rectangle clipRect;
    private Rectangle tabHeaderAreaClipRect;
    private Tab selectedTab;
    private boolean isSelectingTab;
    private double maxw = 0.0;
    private double maxh = 0.0;
    static int CLOSE_BTN_SIZE = 16;
    private static final PseudoClass SELECTED_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("selected");
    private static final PseudoClass TOP_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("top");
    private static final PseudoClass BOTTOM_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("bottom");
    private static final PseudoClass LEFT_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("left");
    private static final PseudoClass RIGHT_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("right");
    private static final PseudoClass DISABLED_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("disabled");

    private static int getRotation(Side side) {
        switch (side) {
            case TOP: {
                return 0;
            }
            case BOTTOM: {
                return 180;
            }
            case LEFT: {
                return -90;
            }
            case RIGHT: {
                return 90;
            }
        }
        return 0;
    }

    private static Node clone(Node node) {
        if (node == null) {
            return null;
        }
        if (node instanceof ImageView) {
            ImageView imageView = (ImageView)node;
            ImageView imageView2 = new ImageView();
            imageView2.setImage(imageView.getImage());
            return imageView2;
        }
        if (node instanceof Label) {
            Label label = (Label)node;
            Label label2 = new Label(label.getText(), label.getGraphic());
            return label2;
        }
        return null;
    }

    public TabPaneSkin(TabPane tabPane) {
        super(tabPane, new TabPaneBehavior(tabPane));
        this.clipRect = new Rectangle(tabPane.getWidth(), tabPane.getHeight());
        ((TabPane)this.getSkinnable()).setClip(this.clipRect);
        this.tabContentRegions = FXCollections.observableArrayList();
        for (Tab tab : ((TabPane)this.getSkinnable()).getTabs()) {
            this.addTabContent(tab);
        }
        this.tabHeaderAreaClipRect = new Rectangle();
        this.tabHeaderArea = new TabHeaderArea();
        this.tabHeaderArea.setClip(this.tabHeaderAreaClipRect);
        this.getChildren().add(this.tabHeaderArea);
        if (((TabPane)this.getSkinnable()).getTabs().size() == 0) {
            this.tabHeaderArea.setVisible(false);
        }
        this.initializeTabListener();
        this.registerChangeListener(tabPane.getSelectionModel().selectedItemProperty(), "SELECTED_TAB");
        this.registerChangeListener(tabPane.sideProperty(), "SIDE");
        this.registerChangeListener(tabPane.widthProperty(), "WIDTH");
        this.registerChangeListener(tabPane.heightProperty(), "HEIGHT");
        this.selectedTab = (Tab)((TabPane)this.getSkinnable()).getSelectionModel().getSelectedItem();
        if (this.selectedTab == null && ((TabPane)this.getSkinnable()).getSelectionModel().getSelectedIndex() != -1) {
            ((TabPane)this.getSkinnable()).getSelectionModel().select(((TabPane)this.getSkinnable()).getSelectionModel().getSelectedIndex());
            this.selectedTab = (Tab)((TabPane)this.getSkinnable()).getSelectionModel().getSelectedItem();
        }
        if (this.selectedTab == null) {
            ((TabPane)this.getSkinnable()).getSelectionModel().selectFirst();
        }
        this.selectedTab = (Tab)((TabPane)this.getSkinnable()).getSelectionModel().getSelectedItem();
        this.isSelectingTab = false;
        this.initializeSwipeHandlers();
    }

    public StackPane getSelectedTabContentRegion() {
        for (TabContentRegion tabContentRegion : this.tabContentRegions) {
            if (!tabContentRegion.getTab().equals(this.selectedTab)) continue;
            return tabContentRegion;
        }
        return null;
    }

    @Override
    protected void handleControlPropertyChanged(String string) {
        super.handleControlPropertyChanged(string);
        if ("SELECTED_TAB".equals(string)) {
            this.isSelectingTab = true;
            this.selectedTab = (Tab)((TabPane)this.getSkinnable()).getSelectionModel().getSelectedItem();
            ((TabPane)this.getSkinnable()).requestLayout();
        } else if ("SIDE".equals(string)) {
            this.updateTabPosition();
        } else if ("WIDTH".equals(string)) {
            this.clipRect.setWidth(((TabPane)this.getSkinnable()).getWidth());
        } else if ("HEIGHT".equals(string)) {
            this.clipRect.setHeight(((TabPane)this.getSkinnable()).getHeight());
        }
    }

    private void removeTabs(List<? extends Tab> list) {
        for (Tab tab : list) {
            Object object;
            Object object2;
            this.stopCurrentAnimation(tab);
            TabHeaderSkin tabHeaderSkin = this.tabHeaderArea.getTabHeaderSkin(tab);
            if (tabHeaderSkin == null) continue;
            tabHeaderSkin.isClosing = true;
            tabHeaderSkin.removeListeners(tab);
            this.removeTabContent(tab);
            ContextMenu contextMenu = this.tabHeaderArea.controlButtons.popup;
            TabMenuItem tabMenuItem = null;
            if (contextMenu != null) {
                object2 = contextMenu.getItems().iterator();
                while (object2.hasNext() && tab != (tabMenuItem = (TabMenuItem)(object = (MenuItem)object2.next())).getTab()) {
                    tabMenuItem = null;
                }
            }
            if (tabMenuItem != null) {
                tabMenuItem.dispose();
                contextMenu.getItems().remove(tabMenuItem);
            }
            object2 = actionEvent -> {
                tabHeaderSkin.animationState = TabAnimationState.NONE;
                this.tabHeaderArea.removeTab(tab);
                this.tabHeaderArea.requestLayout();
                if (((TabPane)this.getSkinnable()).getTabs().isEmpty()) {
                    this.tabHeaderArea.setVisible(false);
                }
            };
            if (this.closeTabAnimation.get() == TabAnimation.GROW) {
                tabHeaderSkin.animationState = TabAnimationState.HIDING;
                object = tabHeaderSkin.currentAnimation = this.createTimeline(tabHeaderSkin, Duration.millis(150.0), 0.0, (EventHandler<ActionEvent>)object2);
                ((Animation)object).play();
                continue;
            }
            object2.handle(null);
        }
    }

    private void stopCurrentAnimation(Tab tab) {
        Timeline timeline;
        TabHeaderSkin tabHeaderSkin = this.tabHeaderArea.getTabHeaderSkin(tab);
        if (tabHeaderSkin != null && (timeline = tabHeaderSkin.currentAnimation) != null && timeline.getStatus() == Animation.Status.RUNNING) {
            timeline.getOnFinished().handle(null);
            timeline.stop();
            tabHeaderSkin.currentAnimation = null;
        }
    }

    private void addTabs(List<? extends Tab> list, int n2) {
        int n3 = 0;
        ArrayList<Node> arrayList = new ArrayList<Node>(this.tabHeaderArea.headersRegion.getChildren());
        for (Node eventTarget : arrayList) {
            TabHeaderSkin tabHeaderSkin = (TabHeaderSkin)eventTarget;
            if (tabHeaderSkin.animationState != TabAnimationState.HIDING) continue;
            this.stopCurrentAnimation(tabHeaderSkin.tab);
        }
        for (Tab tab : list) {
            this.stopCurrentAnimation(tab);
            if (!this.tabHeaderArea.isVisible()) {
                this.tabHeaderArea.setVisible(true);
            }
            int n4 = n2 + n3++;
            this.tabHeaderArea.addTab(tab, n4);
            this.addTabContent(tab);
            TabHeaderSkin tabHeaderSkin = this.tabHeaderArea.getTabHeaderSkin(tab);
            if (tabHeaderSkin == null) continue;
            if (this.openTabAnimation.get() == TabAnimation.GROW) {
                tabHeaderSkin.animationState = TabAnimationState.SHOWING;
                tabHeaderSkin.animationTransition.setValue(0.0);
                tabHeaderSkin.setVisible(true);
                tabHeaderSkin.currentAnimation = this.createTimeline(tabHeaderSkin, Duration.millis(150.0), 1.0, actionEvent -> {
                    tabHeaderSkin.animationState = TabAnimationState.NONE;
                    tabHeaderSkin.setVisible(true);
                    tabHeaderSkin.inner.requestLayout();
                });
                tabHeaderSkin.currentAnimation.play();
                continue;
            }
            tabHeaderSkin.setVisible(true);
            tabHeaderSkin.inner.requestLayout();
        }
    }

    private void initializeTabListener() {
        ((TabPane)this.getSkinnable()).getTabs().addListener(change -> {
            EventTarget eventTarget;
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            int n2 = -1;
            while (change.next()) {
                if (change.wasPermutated()) {
                    TabPane tabPane = (TabPane)this.getSkinnable();
                    ObservableList<Tab> object = tabPane.getTabs();
                    int n3 = change.getTo() - change.getFrom();
                    eventTarget = (Tab)tabPane.getSelectionModel().getSelectedItem();
                    ArrayList arrayList3 = new ArrayList(n3);
                    ((TabPane)this.getSkinnable()).getSelectionModel().clearSelection();
                    TabAnimation tabAnimation = (TabAnimation)((Object)((Object)this.openTabAnimation.get()));
                    TabAnimation tabAnimation2 = (TabAnimation)((Object)((Object)this.closeTabAnimation.get()));
                    this.openTabAnimation.set(TabAnimation.NONE);
                    this.closeTabAnimation.set(TabAnimation.NONE);
                    for (int i2 = change.getFrom(); i2 < change.getTo(); ++i2) {
                        arrayList3.add(object.get(i2));
                    }
                    this.removeTabs(arrayList3);
                    this.addTabs(arrayList3, change.getFrom());
                    this.openTabAnimation.set(tabAnimation);
                    this.closeTabAnimation.set(tabAnimation2);
                    ((TabPane)this.getSkinnable()).getSelectionModel().select((Tab)eventTarget);
                }
                if (change.wasRemoved()) {
                    arrayList.addAll(change.getRemoved());
                }
                if (!change.wasAdded()) continue;
                arrayList2.addAll(change.getAddedSubList());
                n2 = change.getFrom();
            }
            arrayList.removeAll(arrayList2);
            this.removeTabs(arrayList);
            if (!arrayList2.isEmpty()) {
                for (TabContentRegion tabContentRegion : this.tabContentRegions) {
                    Tab tab = tabContentRegion.getTab();
                    eventTarget = this.tabHeaderArea.getTabHeaderSkin(tab);
                    if (((TabHeaderSkin)eventTarget).isClosing || !arrayList2.contains(tabContentRegion.getTab())) continue;
                    arrayList2.remove(tabContentRegion.getTab());
                }
                this.addTabs(arrayList2, n2 == -1 ? this.tabContentRegions.size() : n2);
            }
            ((TabPane)this.getSkinnable()).requestLayout();
        });
    }

    private void addTabContent(Tab tab) {
        TabContentRegion tabContentRegion = new TabContentRegion(tab);
        tabContentRegion.setClip(new Rectangle());
        this.tabContentRegions.add(tabContentRegion);
        this.getChildren().add(0, tabContentRegion);
    }

    private void removeTabContent(Tab tab) {
        for (TabContentRegion tabContentRegion : this.tabContentRegions) {
            if (!tabContentRegion.getTab().equals(tab)) continue;
            tabContentRegion.removeListeners(tab);
            this.getChildren().remove(tabContentRegion);
            this.tabContentRegions.remove(tabContentRegion);
            break;
        }
    }

    private void updateTabPosition() {
        this.tabHeaderArea.setScrollOffset(0.0);
        ((TabPane)this.getSkinnable()).applyCss();
        ((TabPane)this.getSkinnable()).requestLayout();
    }

    private Timeline createTimeline(TabHeaderSkin tabHeaderSkin, Duration duration, double d2, EventHandler<ActionEvent> eventHandler) {
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        KeyValue keyValue = new KeyValue(tabHeaderSkin.animationTransition, d2, Interpolator.LINEAR);
        timeline.getKeyFrames().clear();
        timeline.getKeyFrames().add(new KeyFrame(duration, keyValue));
        timeline.setOnFinished(eventHandler);
        return timeline;
    }

    private boolean isHorizontal() {
        Side side = ((TabPane)this.getSkinnable()).getSide();
        return Side.TOP.equals((Object)side) || Side.BOTTOM.equals((Object)side);
    }

    private void initializeSwipeHandlers() {
        if (IS_TOUCH_SUPPORTED) {
            ((TabPane)this.getSkinnable()).addEventHandler(SwipeEvent.SWIPE_LEFT, swipeEvent -> ((TabPaneBehavior)this.getBehavior()).selectNextTab());
            ((TabPane)this.getSkinnable()).addEventHandler(SwipeEvent.SWIPE_RIGHT, swipeEvent -> ((TabPaneBehavior)this.getBehavior()).selectPreviousTab());
        }
    }

    private boolean isFloatingStyleClass() {
        return ((TabPane)this.getSkinnable()).getStyleClass().contains("floating");
    }

    @Override
    protected double computePrefWidth(double d2, double d3, double d4, double d5, double d6) {
        for (TabContentRegion tabContentRegion : this.tabContentRegions) {
            this.maxw = Math.max(this.maxw, this.snapSize(tabContentRegion.prefWidth(-1.0)));
        }
        boolean bl = this.isHorizontal();
        double d7 = this.snapSize(bl ? this.tabHeaderArea.prefWidth(-1.0) : this.tabHeaderArea.prefHeight(-1.0));
        double d8 = bl ? Math.max(this.maxw, d7) : this.maxw + d7;
        return this.snapSize(d8) + d4 + d6;
    }

    @Override
    protected double computePrefHeight(double d2, double d3, double d4, double d5, double d6) {
        for (TabContentRegion tabContentRegion : this.tabContentRegions) {
            this.maxh = Math.max(this.maxh, this.snapSize(tabContentRegion.prefHeight(-1.0)));
        }
        boolean bl = this.isHorizontal();
        double d7 = this.snapSize(bl ? this.tabHeaderArea.prefHeight(-1.0) : this.tabHeaderArea.prefWidth(-1.0));
        double d8 = bl ? this.maxh + this.snapSize(d7) : Math.max(this.maxh, d7);
        return this.snapSize(d8) + d3 + d5;
    }

    @Override
    public double computeBaselineOffset(double d2, double d3, double d4, double d5) {
        Side side = ((TabPane)this.getSkinnable()).getSide();
        if (side == Side.TOP) {
            return this.tabHeaderArea.getBaselineOffset() + d2;
        }
        return 0.0;
    }

    @Override
    protected void layoutChildren(double d2, double d3, double d4, double d5) {
        double d6;
        TabPane tabPane = (TabPane)this.getSkinnable();
        Side side = tabPane.getSide();
        double d7 = this.snapSize(this.tabHeaderArea.prefHeight(-1.0));
        double d8 = side.equals((Object)Side.RIGHT) ? d2 + d4 - d7 : d2;
        double d9 = d6 = side.equals((Object)Side.BOTTOM) ? d3 + d5 - d7 : d3;
        if (side == Side.TOP) {
            this.tabHeaderArea.resize(d4, d7);
            this.tabHeaderArea.relocate(d8, d6);
            this.tabHeaderArea.getTransforms().clear();
            this.tabHeaderArea.getTransforms().add(new Rotate(TabPaneSkin.getRotation(Side.TOP)));
        } else if (side == Side.BOTTOM) {
            this.tabHeaderArea.resize(d4, d7);
            this.tabHeaderArea.relocate(d4, d6 - d7);
            this.tabHeaderArea.getTransforms().clear();
            this.tabHeaderArea.getTransforms().add(new Rotate(TabPaneSkin.getRotation(Side.BOTTOM), 0.0, d7));
        } else if (side == Side.LEFT) {
            this.tabHeaderArea.resize(d5, d7);
            this.tabHeaderArea.relocate(d8 + d7, d5 - d7);
            this.tabHeaderArea.getTransforms().clear();
            this.tabHeaderArea.getTransforms().add(new Rotate(TabPaneSkin.getRotation(Side.LEFT), 0.0, d7));
        } else if (side == Side.RIGHT) {
            this.tabHeaderArea.resize(d5, d7);
            this.tabHeaderArea.relocate(d8, d3 - d7);
            this.tabHeaderArea.getTransforms().clear();
            this.tabHeaderArea.getTransforms().add(new Rotate(TabPaneSkin.getRotation(Side.RIGHT), 0.0, d7));
        }
        this.tabHeaderAreaClipRect.setX(0.0);
        this.tabHeaderAreaClipRect.setY(0.0);
        if (this.isHorizontal()) {
            this.tabHeaderAreaClipRect.setWidth(d4);
        } else {
            this.tabHeaderAreaClipRect.setWidth(d5);
        }
        this.tabHeaderAreaClipRect.setHeight(d7);
        double d10 = 0.0;
        double d11 = 0.0;
        if (side == Side.TOP) {
            d10 = d2;
            d11 = d3 + d7;
            if (this.isFloatingStyleClass()) {
                d11 -= 1.0;
            }
        } else if (side == Side.BOTTOM) {
            d10 = d2;
            d11 = d3;
            if (this.isFloatingStyleClass()) {
                d11 = 1.0;
            }
        } else if (side == Side.LEFT) {
            d10 = d2 + d7;
            d11 = d3;
            if (this.isFloatingStyleClass()) {
                d10 -= 1.0;
            }
        } else if (side == Side.RIGHT) {
            d10 = d2;
            d11 = d3;
            if (this.isFloatingStyleClass()) {
                d10 = 1.0;
            }
        }
        double d12 = d4 - (this.isHorizontal() ? 0.0 : d7);
        double d13 = d5 - (this.isHorizontal() ? d7 : 0.0);
        int n2 = this.tabContentRegions.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            TabContentRegion tabContentRegion = (TabContentRegion)this.tabContentRegions.get(i2);
            tabContentRegion.setAlignment(Pos.TOP_LEFT);
            if (tabContentRegion.getClip() != null) {
                ((Rectangle)tabContentRegion.getClip()).setWidth(d12);
                ((Rectangle)tabContentRegion.getClip()).setHeight(d13);
            }
            tabContentRegion.resize(d12, d13);
            tabContentRegion.relocate(d10, d11);
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return TabPaneSkin.getClassCssMetaData();
    }

    @Override
    public Object queryAccessibleAttribute(AccessibleAttribute accessibleAttribute, Object ... arrobject) {
        switch (accessibleAttribute) {
            case FOCUS_ITEM: {
                return this.tabHeaderArea.getTabHeaderSkin(this.selectedTab);
            }
            case ITEM_COUNT: {
                return this.tabHeaderArea.headersRegion.getChildren().size();
            }
            case ITEM_AT_INDEX: {
                Integer n2 = (Integer)arrobject[0];
                if (n2 == null) {
                    return null;
                }
                return this.tabHeaderArea.headersRegion.getChildren().get(n2);
            }
        }
        return super.queryAccessibleAttribute(accessibleAttribute, arrobject);
    }

    class TabMenuItem
    extends RadioMenuItem {
        Tab tab;
        private InvalidationListener disableListener;
        private WeakInvalidationListener weakDisableListener;

        public TabMenuItem(Tab tab) {
            super(tab.getText(), TabPaneSkin.clone(tab.getGraphic()));
            this.disableListener = new InvalidationListener(){

                @Override
                public void invalidated(Observable observable) {
                    TabMenuItem.this.setDisable(TabMenuItem.this.tab.isDisable());
                }
            };
            this.weakDisableListener = new WeakInvalidationListener(this.disableListener);
            this.tab = tab;
            this.setDisable(tab.isDisable());
            tab.disableProperty().addListener(this.weakDisableListener);
            this.textProperty().bind(tab.textProperty());
        }

        public Tab getTab() {
            return this.tab;
        }

        public void dispose() {
            this.tab.disableProperty().removeListener(this.weakDisableListener);
        }
    }

    class TabControlButtons
    extends StackPane {
        private StackPane inner;
        private StackPane downArrow;
        private Pane downArrowBtn;
        private boolean showControlButtons;
        private ContextMenu popup;
        private boolean showTabsMenu = false;

        public TabControlButtons() {
            this.getStyleClass().setAll("control-buttons-tab");
            TabPane tabPane = (TabPane)TabPaneSkin.this.getSkinnable();
            this.downArrowBtn = new Pane();
            this.downArrowBtn.getStyleClass().setAll("tab-down-button");
            this.downArrowBtn.setVisible(this.isShowTabsMenu());
            this.downArrow = new StackPane();
            this.downArrow.setManaged(false);
            this.downArrow.getStyleClass().setAll("arrow");
            this.downArrow.setRotate(tabPane.getSide().equals((Object)Side.BOTTOM) ? 180.0 : 0.0);
            this.downArrowBtn.getChildren().add(this.downArrow);
            this.downArrowBtn.setOnMouseClicked(mouseEvent -> this.showPopupMenu());
            this.setupPopupMenu();
            this.inner = new StackPane(){

                @Override
                protected double computePrefWidth(double d2) {
                    double d3 = !TabControlButtons.this.isShowTabsMenu() ? 0.0 : this.snapSize(TabControlButtons.this.downArrow.prefWidth(this.getHeight())) + this.snapSize(TabControlButtons.this.downArrowBtn.prefWidth(this.getHeight()));
                    double d4 = 0.0;
                    if (TabControlButtons.this.isShowTabsMenu()) {
                        d4 += d3;
                    }
                    if (d4 > 0.0) {
                        d4 += this.snappedLeftInset() + this.snappedRightInset();
                    }
                    return d4;
                }

                @Override
                protected double computePrefHeight(double d2) {
                    double d3 = 0.0;
                    if (TabControlButtons.this.isShowTabsMenu()) {
                        d3 = Math.max(d3, this.snapSize(TabControlButtons.this.downArrowBtn.prefHeight(d2)));
                    }
                    if (d3 > 0.0) {
                        d3 += this.snappedTopInset() + this.snappedBottomInset();
                    }
                    return d3;
                }

                @Override
                protected void layoutChildren() {
                    if (TabControlButtons.this.isShowTabsMenu()) {
                        double d2 = 0.0;
                        double d3 = this.snappedTopInset();
                        double d4 = this.snapSize(this.getWidth()) - d2 + this.snappedLeftInset();
                        double d5 = this.snapSize(this.getHeight()) - d3 + this.snappedBottomInset();
                        this.positionArrow(TabControlButtons.this.downArrowBtn, TabControlButtons.this.downArrow, d2, d3, d4, d5);
                    }
                }

                private void positionArrow(Pane pane, StackPane stackPane, double d2, double d3, double d4, double d5) {
                    pane.resize(d4, d5);
                    this.positionInArea(pane, d2, d3, d4, d5, 0.0, HPos.CENTER, VPos.CENTER);
                    double d6 = this.snapSize(stackPane.prefWidth(-1.0));
                    double d7 = this.snapSize(stackPane.prefHeight(-1.0));
                    stackPane.resize(d6, d7);
                    this.positionInArea(stackPane, pane.snappedLeftInset(), pane.snappedTopInset(), d4 - pane.snappedLeftInset() - pane.snappedRightInset(), d5 - pane.snappedTopInset() - pane.snappedBottomInset(), 0.0, HPos.CENTER, VPos.CENTER);
                }
            };
            this.inner.getStyleClass().add("container");
            this.inner.getChildren().add(this.downArrowBtn);
            this.getChildren().add(this.inner);
            tabPane.sideProperty().addListener(observable -> {
                Side side = ((TabPane)TabPaneSkin.this.getSkinnable()).getSide();
                this.downArrow.setRotate(side.equals((Object)Side.BOTTOM) ? 180.0 : 0.0);
            });
            tabPane.getTabs().addListener(change -> this.setupPopupMenu());
            this.showControlButtons = false;
            if (this.isShowTabsMenu()) {
                this.showControlButtons = true;
                this.requestLayout();
            }
            this.getProperties().put(ContextMenu.class, this.popup);
        }

        private void showTabsMenu(boolean bl) {
            boolean bl2 = this.isShowTabsMenu();
            this.showTabsMenu = bl;
            if (this.showTabsMenu && !bl2) {
                this.downArrowBtn.setVisible(true);
                this.showControlButtons = true;
                this.inner.requestLayout();
                TabPaneSkin.this.tabHeaderArea.requestLayout();
            } else if (!this.showTabsMenu && bl2) {
                this.hideControlButtons();
            }
        }

        private boolean isShowTabsMenu() {
            return this.showTabsMenu;
        }

        @Override
        protected double computePrefWidth(double d2) {
            double d3 = this.snapSize(this.inner.prefWidth(d2));
            if (d3 > 0.0) {
                d3 += this.snappedLeftInset() + this.snappedRightInset();
            }
            return d3;
        }

        @Override
        protected double computePrefHeight(double d2) {
            return Math.max(((TabPane)TabPaneSkin.this.getSkinnable()).getTabMinHeight(), this.snapSize(this.inner.prefHeight(d2))) + this.snappedTopInset() + this.snappedBottomInset();
        }

        @Override
        protected void layoutChildren() {
            double d2 = this.snappedLeftInset();
            double d3 = this.snappedTopInset();
            double d4 = this.snapSize(this.getWidth()) - d2 + this.snappedRightInset();
            double d5 = this.snapSize(this.getHeight()) - d3 + this.snappedBottomInset();
            if (this.showControlButtons) {
                this.showControlButtons();
                this.showControlButtons = false;
            }
            this.inner.resize(d4, d5);
            this.positionInArea(this.inner, d2, d3, d4, d5, 0.0, HPos.CENTER, VPos.BOTTOM);
        }

        private void showControlButtons() {
            this.setVisible(true);
            if (this.popup == null) {
                this.setupPopupMenu();
            }
        }

        private void hideControlButtons() {
            if (this.isShowTabsMenu()) {
                this.showControlButtons = true;
            } else {
                this.setVisible(false);
                this.popup.getItems().clear();
                this.popup = null;
            }
            this.requestLayout();
        }

        private void setupPopupMenu() {
            if (this.popup == null) {
                this.popup = new ContextMenu();
            }
            this.popup.getItems().clear();
            ToggleGroup toggleGroup = new ToggleGroup();
            ObservableList<TabMenuItem> observableList = FXCollections.observableArrayList();
            for (Tab tab : ((TabPane)TabPaneSkin.this.getSkinnable()).getTabs()) {
                TabMenuItem tabMenuItem = new TabMenuItem(tab);
                tabMenuItem.setToggleGroup(toggleGroup);
                tabMenuItem.setOnAction(actionEvent -> ((TabPane)TabPaneSkin.this.getSkinnable()).getSelectionModel().select(tab));
                observableList.add(tabMenuItem);
            }
            this.popup.getItems().addAll((Collection<MenuItem>)observableList);
        }

        private void showPopupMenu() {
            for (MenuItem menuItem : this.popup.getItems()) {
                TabMenuItem tabMenuItem = (TabMenuItem)menuItem;
                if (!TabPaneSkin.this.selectedTab.equals(tabMenuItem.getTab())) continue;
                tabMenuItem.setSelected(true);
                break;
            }
            this.popup.show(this.downArrowBtn, Side.BOTTOM, 0.0, 0.0);
        }
    }

    class TabContentRegion
    extends StackPane {
        private TraversalEngine engine;
        private Direction direction = Direction.NEXT;
        private Tab tab;
        private InvalidationListener tabContentListener = observable -> this.updateContent();
        private InvalidationListener tabSelectedListener = new InvalidationListener(){

            @Override
            public void invalidated(Observable observable) {
                TabContentRegion.this.setVisible(TabContentRegion.this.tab.isSelected());
            }
        };
        private WeakInvalidationListener weakTabContentListener = new WeakInvalidationListener(this.tabContentListener);
        private WeakInvalidationListener weakTabSelectedListener = new WeakInvalidationListener(this.tabSelectedListener);

        public Tab getTab() {
            return this.tab;
        }

        public TabContentRegion(Tab tab) {
            this.getStyleClass().setAll("tab-content-area");
            this.setManaged(false);
            this.tab = tab;
            this.updateContent();
            this.setVisible(tab.isSelected());
            tab.selectedProperty().addListener(this.weakTabSelectedListener);
            tab.contentProperty().addListener(this.weakTabContentListener);
        }

        private void updateContent() {
            Node node = this.getTab().getContent();
            if (node == null) {
                this.getChildren().clear();
            } else {
                this.getChildren().setAll(node);
            }
        }

        private void removeListeners(Tab tab) {
            tab.selectedProperty().removeListener(this.weakTabSelectedListener);
            tab.contentProperty().removeListener(this.weakTabContentListener);
        }
    }

    class TabHeaderSkin
    extends StackPane {
        private final Tab tab;
        private Label label;
        private StackPane closeBtn;
        private StackPane inner;
        private Tooltip oldTooltip;
        private Tooltip tooltip;
        private Rectangle clip;
        private boolean isClosing = false;
        private MultiplePropertyChangeListenerHandler listener = new MultiplePropertyChangeListenerHandler(string -> {
            this.handlePropertyChanged((String)string);
            return null;
        });
        private final ListChangeListener<String> styleClassListener = new ListChangeListener<String>(){

            @Override
            public void onChanged(ListChangeListener.Change<? extends String> change) {
                TabHeaderSkin.this.getStyleClass().setAll((Collection<String>)TabHeaderSkin.this.tab.getStyleClass());
            }
        };
        private final WeakListChangeListener<String> weakStyleClassListener = new WeakListChangeListener<String>(this.styleClassListener);
        private final DoubleProperty animationTransition = new SimpleDoubleProperty(this, "animationTransition", 1.0){

            @Override
            protected void invalidated() {
                TabHeaderSkin.this.requestLayout();
            }
        };
        private TabAnimationState animationState = TabAnimationState.NONE;
        private Timeline currentAnimation;

        public Tab getTab() {
            return this.tab;
        }

        public TabHeaderSkin(Tab tab) {
            this.getStyleClass().setAll((Collection<String>)tab.getStyleClass());
            this.setId(tab.getId());
            this.setStyle(tab.getStyle());
            this.setAccessibleRole(AccessibleRole.TAB_ITEM);
            this.tab = tab;
            this.clip = new Rectangle();
            this.setClip(this.clip);
            this.label = new Label(tab.getText(), tab.getGraphic());
            this.label.getStyleClass().setAll("tab-label");
            this.closeBtn = new StackPane(){

                @Override
                protected double computePrefWidth(double d2) {
                    return CLOSE_BTN_SIZE;
                }

                @Override
                protected double computePrefHeight(double d2) {
                    return CLOSE_BTN_SIZE;
                }

                @Override
                public void executeAccessibleAction(AccessibleAction accessibleAction, Object ... arrobject) {
                    switch (accessibleAction) {
                        case FIRE: {
                            Tab tab = TabHeaderSkin.this.getTab();
                            TabPaneBehavior tabPaneBehavior = (TabPaneBehavior)TabPaneSkin.this.getBehavior();
                            if (!tabPaneBehavior.canCloseTab(tab)) break;
                            tabPaneBehavior.closeTab(tab);
                            this.setOnMousePressed(null);
                        }
                    }
                    super.executeAccessibleAction(accessibleAction, arrobject);
                }
            };
            this.closeBtn.setAccessibleRole(AccessibleRole.BUTTON);
            this.closeBtn.setAccessibleText(ControlResources.getString("Accessibility.title.TabPane.CloseButton"));
            this.closeBtn.getStyleClass().setAll("tab-close-button");
            this.closeBtn.setOnMousePressed((EventHandler<? super MouseEvent>)new EventHandler<MouseEvent>(){

                @Override
                public void handle(MouseEvent mouseEvent) {
                    Tab tab = TabHeaderSkin.this.getTab();
                    TabPaneBehavior tabPaneBehavior = (TabPaneBehavior)TabPaneSkin.this.getBehavior();
                    if (tabPaneBehavior.canCloseTab(tab)) {
                        tabPaneBehavior.closeTab(tab);
                        TabHeaderSkin.this.setOnMousePressed(null);
                    }
                }
            });
            this.updateGraphicRotation();
            final Region region = new Region();
            region.setMouseTransparent(true);
            region.getStyleClass().add("focus-indicator");
            this.inner = new StackPane(){

                @Override
                protected void layoutChildren() {
                    TabPane tabPane = (TabPane)TabPaneSkin.this.getSkinnable();
                    double d2 = this.snappedTopInset();
                    double d3 = this.snappedRightInset();
                    double d4 = this.snappedBottomInset();
                    double d5 = this.snappedLeftInset();
                    double d6 = this.getWidth() - (d5 + d3);
                    double d7 = this.getHeight() - (d2 + d4);
                    double d8 = this.snapSize(TabHeaderSkin.this.label.prefWidth(-1.0));
                    double d9 = this.snapSize(TabHeaderSkin.this.label.prefHeight(-1.0));
                    double d10 = TabHeaderSkin.this.showCloseButton() ? this.snapSize(TabHeaderSkin.this.closeBtn.prefWidth(-1.0)) : 0.0;
                    double d11 = TabHeaderSkin.this.showCloseButton() ? this.snapSize(TabHeaderSkin.this.closeBtn.prefHeight(-1.0)) : 0.0;
                    double d12 = this.snapSize(tabPane.getTabMinWidth());
                    double d13 = this.snapSize(tabPane.getTabMaxWidth());
                    double d14 = this.snapSize(tabPane.getTabMaxHeight());
                    double d15 = d8;
                    double d16 = d8;
                    double d17 = d9;
                    double d18 = d15 + d10;
                    double d19 = Math.max(d17, d11);
                    if (d18 > d13 && d13 != Double.MAX_VALUE) {
                        d15 = d13 - d10;
                        d16 = d13 - d10;
                    } else if (d18 < d12) {
                        d15 = d12 - d10;
                    }
                    if (d19 > d14 && d14 != Double.MAX_VALUE) {
                        d17 = d14;
                    }
                    if (TabHeaderSkin.this.animationState != TabAnimationState.NONE) {
                        d15 *= TabHeaderSkin.this.animationTransition.get();
                        TabHeaderSkin.this.closeBtn.setVisible(false);
                    } else {
                        TabHeaderSkin.this.closeBtn.setVisible(TabHeaderSkin.this.showCloseButton());
                    }
                    TabHeaderSkin.this.label.resize(d16, d17);
                    double d20 = d5;
                    double d21 = (d13 < Double.MAX_VALUE ? Math.min(d6, d13) : d6) - d3 - d10;
                    this.positionInArea(TabHeaderSkin.this.label, d20, d2, d15, d7, 0.0, HPos.CENTER, VPos.CENTER);
                    if (TabHeaderSkin.this.closeBtn.isVisible()) {
                        TabHeaderSkin.this.closeBtn.resize(d10, d11);
                        this.positionInArea(TabHeaderSkin.this.closeBtn, d21, d2, d10, d7, 0.0, HPos.CENTER, VPos.CENTER);
                    }
                    int n2 = Utils.isMac() ? 2 : 3;
                    int n3 = Utils.isMac() ? 2 : 1;
                    region.resizeRelocate(d5 - (double)n3, d2 + (double)n2, d6 + (double)(2 * n3), d7 - (double)(2 * n2));
                }
            };
            this.inner.getStyleClass().add("tab-container");
            this.inner.setRotate(((TabPane)TabPaneSkin.this.getSkinnable()).getSide().equals((Object)Side.BOTTOM) ? 180.0 : 0.0);
            this.inner.getChildren().addAll(this.label, this.closeBtn, region);
            this.getChildren().addAll(this.inner);
            this.tooltip = tab.getTooltip();
            if (this.tooltip != null) {
                Tooltip.install(this, this.tooltip);
                this.oldTooltip = this.tooltip;
            }
            this.listener.registerChangeListener(tab.closableProperty(), "CLOSABLE");
            this.listener.registerChangeListener(tab.selectedProperty(), "SELECTED");
            this.listener.registerChangeListener(tab.textProperty(), "TEXT");
            this.listener.registerChangeListener(tab.graphicProperty(), "GRAPHIC");
            this.listener.registerChangeListener(tab.contextMenuProperty(), "CONTEXT_MENU");
            this.listener.registerChangeListener(tab.tooltipProperty(), "TOOLTIP");
            this.listener.registerChangeListener(tab.disableProperty(), "DISABLE");
            this.listener.registerChangeListener(tab.styleProperty(), "STYLE");
            tab.getStyleClass().addListener(this.weakStyleClassListener);
            this.listener.registerChangeListener(((TabPane)TabPaneSkin.this.getSkinnable()).tabClosingPolicyProperty(), "TAB_CLOSING_POLICY");
            this.listener.registerChangeListener(((TabPane)TabPaneSkin.this.getSkinnable()).sideProperty(), "SIDE");
            this.listener.registerChangeListener(((TabPane)TabPaneSkin.this.getSkinnable()).rotateGraphicProperty(), "ROTATE_GRAPHIC");
            this.listener.registerChangeListener(((TabPane)TabPaneSkin.this.getSkinnable()).tabMinWidthProperty(), "TAB_MIN_WIDTH");
            this.listener.registerChangeListener(((TabPane)TabPaneSkin.this.getSkinnable()).tabMaxWidthProperty(), "TAB_MAX_WIDTH");
            this.listener.registerChangeListener(((TabPane)TabPaneSkin.this.getSkinnable()).tabMinHeightProperty(), "TAB_MIN_HEIGHT");
            this.listener.registerChangeListener(((TabPane)TabPaneSkin.this.getSkinnable()).tabMaxHeightProperty(), "TAB_MAX_HEIGHT");
            this.getProperties().put(Tab.class, tab);
            this.getProperties().put(ContextMenu.class, tab.getContextMenu());
            this.setOnContextMenuRequested(contextMenuEvent -> {
                if (this.getTab().getContextMenu() != null) {
                    this.getTab().getContextMenu().show(this.inner, contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY());
                    contextMenuEvent.consume();
                }
            });
            this.setOnMousePressed((EventHandler<? super MouseEvent>)new EventHandler<MouseEvent>(){

                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (TabHeaderSkin.this.getTab().isDisable()) {
                        return;
                    }
                    if (mouseEvent.getButton().equals((Object)MouseButton.MIDDLE)) {
                        if (TabHeaderSkin.this.showCloseButton()) {
                            Tab tab = TabHeaderSkin.this.getTab();
                            TabPaneBehavior tabPaneBehavior = (TabPaneBehavior)TabPaneSkin.this.getBehavior();
                            if (tabPaneBehavior.canCloseTab(tab)) {
                                TabHeaderSkin.this.removeListeners(tab);
                                tabPaneBehavior.closeTab(tab);
                            }
                        }
                    } else if (mouseEvent.getButton().equals((Object)MouseButton.PRIMARY)) {
                        ((TabPaneBehavior)TabPaneSkin.this.getBehavior()).selectTab(TabHeaderSkin.this.getTab());
                    }
                }
            });
            this.pseudoClassStateChanged(SELECTED_PSEUDOCLASS_STATE, tab.isSelected());
            this.pseudoClassStateChanged(DISABLED_PSEUDOCLASS_STATE, tab.isDisable());
            Side side = ((TabPane)TabPaneSkin.this.getSkinnable()).getSide();
            this.pseudoClassStateChanged(TOP_PSEUDOCLASS_STATE, side == Side.TOP);
            this.pseudoClassStateChanged(RIGHT_PSEUDOCLASS_STATE, side == Side.RIGHT);
            this.pseudoClassStateChanged(BOTTOM_PSEUDOCLASS_STATE, side == Side.BOTTOM);
            this.pseudoClassStateChanged(LEFT_PSEUDOCLASS_STATE, side == Side.LEFT);
        }

        private void handlePropertyChanged(String string) {
            if ("CLOSABLE".equals(string)) {
                this.inner.requestLayout();
                this.requestLayout();
            } else if ("SELECTED".equals(string)) {
                this.pseudoClassStateChanged(SELECTED_PSEUDOCLASS_STATE, this.tab.isSelected());
                this.inner.requestLayout();
                this.requestLayout();
            } else if ("TEXT".equals(string)) {
                this.label.setText(this.getTab().getText());
            } else if ("GRAPHIC".equals(string)) {
                this.label.setGraphic(this.getTab().getGraphic());
            } else if (!"CONTEXT_MENU".equals(string)) {
                if ("TOOLTIP".equals(string)) {
                    if (this.oldTooltip != null) {
                        Tooltip.uninstall(this, this.oldTooltip);
                    }
                    this.tooltip = this.tab.getTooltip();
                    if (this.tooltip != null) {
                        Tooltip.install(this, this.tooltip);
                        this.oldTooltip = this.tooltip;
                    }
                } else if ("DISABLE".equals(string)) {
                    this.pseudoClassStateChanged(DISABLED_PSEUDOCLASS_STATE, this.tab.isDisable());
                    this.inner.requestLayout();
                    this.requestLayout();
                } else if ("STYLE".equals(string)) {
                    this.setStyle(this.tab.getStyle());
                } else if ("TAB_CLOSING_POLICY".equals(string)) {
                    this.inner.requestLayout();
                    this.requestLayout();
                } else if ("SIDE".equals(string)) {
                    Side side = ((TabPane)TabPaneSkin.this.getSkinnable()).getSide();
                    this.pseudoClassStateChanged(TOP_PSEUDOCLASS_STATE, side == Side.TOP);
                    this.pseudoClassStateChanged(RIGHT_PSEUDOCLASS_STATE, side == Side.RIGHT);
                    this.pseudoClassStateChanged(BOTTOM_PSEUDOCLASS_STATE, side == Side.BOTTOM);
                    this.pseudoClassStateChanged(LEFT_PSEUDOCLASS_STATE, side == Side.LEFT);
                    this.inner.setRotate(side == Side.BOTTOM ? 180.0 : 0.0);
                    if (((TabPane)TabPaneSkin.this.getSkinnable()).isRotateGraphic()) {
                        this.updateGraphicRotation();
                    }
                } else if ("ROTATE_GRAPHIC".equals(string)) {
                    this.updateGraphicRotation();
                } else if ("TAB_MIN_WIDTH".equals(string)) {
                    this.requestLayout();
                    ((TabPane)TabPaneSkin.this.getSkinnable()).requestLayout();
                } else if ("TAB_MAX_WIDTH".equals(string)) {
                    this.requestLayout();
                    ((TabPane)TabPaneSkin.this.getSkinnable()).requestLayout();
                } else if ("TAB_MIN_HEIGHT".equals(string)) {
                    this.requestLayout();
                    ((TabPane)TabPaneSkin.this.getSkinnable()).requestLayout();
                } else if ("TAB_MAX_HEIGHT".equals(string)) {
                    this.requestLayout();
                    ((TabPane)TabPaneSkin.this.getSkinnable()).requestLayout();
                }
            }
        }

        private void updateGraphicRotation() {
            if (this.label.getGraphic() != null) {
                this.label.getGraphic().setRotate(((TabPane)TabPaneSkin.this.getSkinnable()).isRotateGraphic() ? 0.0 : (double)(((TabPane)TabPaneSkin.this.getSkinnable()).getSide().equals((Object)Side.RIGHT) ? -90.0f : (((TabPane)TabPaneSkin.this.getSkinnable()).getSide().equals((Object)Side.LEFT) ? 90.0f : 0.0f)));
            }
        }

        private boolean showCloseButton() {
            return this.tab.isClosable() && (((TabPane)TabPaneSkin.this.getSkinnable()).getTabClosingPolicy().equals((Object)TabPane.TabClosingPolicy.ALL_TABS) || ((TabPane)TabPaneSkin.this.getSkinnable()).getTabClosingPolicy().equals((Object)TabPane.TabClosingPolicy.SELECTED_TAB) && this.tab.isSelected());
        }

        private void removeListeners(Tab tab) {
            this.listener.dispose();
            this.inner.getChildren().clear();
            this.getChildren().clear();
        }

        @Override
        protected double computePrefWidth(double d2) {
            double d3 = this.snapSize(((TabPane)TabPaneSkin.this.getSkinnable()).getTabMinWidth());
            double d4 = this.snapSize(((TabPane)TabPaneSkin.this.getSkinnable()).getTabMaxWidth());
            double d5 = this.snappedRightInset();
            double d6 = this.snappedLeftInset();
            double d7 = this.snapSize(this.label.prefWidth(-1.0));
            if (this.showCloseButton()) {
                d7 += this.snapSize(this.closeBtn.prefWidth(-1.0));
            }
            if (d7 > d4) {
                d7 = d4;
            } else if (d7 < d3) {
                d7 = d3;
            }
            return d7 += d5 + d6;
        }

        @Override
        protected double computePrefHeight(double d2) {
            double d3 = this.snapSize(((TabPane)TabPaneSkin.this.getSkinnable()).getTabMinHeight());
            double d4 = this.snapSize(((TabPane)TabPaneSkin.this.getSkinnable()).getTabMaxHeight());
            double d5 = this.snappedTopInset();
            double d6 = this.snappedBottomInset();
            double d7 = this.snapSize(this.label.prefHeight(d2));
            if (d7 > d4) {
                d7 = d4;
            } else if (d7 < d3) {
                d7 = d3;
            }
            return d7 += d5 + d6;
        }

        @Override
        protected void layoutChildren() {
            double d2 = (this.snapSize(this.getWidth()) - this.snappedRightInset() - this.snappedLeftInset()) * this.animationTransition.getValue();
            this.inner.resize(d2, this.snapSize(this.getHeight()) - this.snappedTopInset() - this.snappedBottomInset());
            this.inner.relocate(this.snappedLeftInset(), this.snappedTopInset());
        }

        @Override
        protected void setWidth(double d2) {
            super.setWidth(d2);
            this.clip.setWidth(d2);
        }

        @Override
        protected void setHeight(double d2) {
            super.setHeight(d2);
            this.clip.setHeight(d2);
        }

        @Override
        public Object queryAccessibleAttribute(AccessibleAttribute accessibleAttribute, Object ... arrobject) {
            switch (accessibleAttribute) {
                case TEXT: {
                    return this.getTab().getText();
                }
                case SELECTED: {
                    return TabPaneSkin.this.selectedTab == this.getTab();
                }
            }
            return super.queryAccessibleAttribute(accessibleAttribute, arrobject);
        }

        @Override
        public void executeAccessibleAction(AccessibleAction accessibleAction, Object ... arrobject) {
            switch (accessibleAction) {
                case REQUEST_FOCUS: {
                    ((TabPane)TabPaneSkin.this.getSkinnable()).getSelectionModel().select(this.getTab());
                    break;
                }
                default: {
                    super.executeAccessibleAction(accessibleAction, arrobject);
                }
            }
        }
    }

    class TabHeaderArea
    extends StackPane {
        private Rectangle headerClip;
        private StackPane headersRegion;
        private StackPane headerBackground;
        private TabControlButtons controlButtons;
        private boolean measureClosingTabs = false;
        private double scrollOffset;
        private List<TabHeaderSkin> removeTab = new ArrayList<TabHeaderSkin>();

        public TabHeaderArea() {
            this.getStyleClass().setAll("tab-header-area");
            this.setManaged(false);
            TabPane tabPane = (TabPane)TabPaneSkin.this.getSkinnable();
            this.headerClip = new Rectangle();
            this.headersRegion = new StackPane(){

                @Override
                protected double computePrefWidth(double d2) {
                    double d3 = 0.0;
                    for (Node node : this.getChildren()) {
                        TabHeaderSkin tabHeaderSkin = (TabHeaderSkin)node;
                        if (!tabHeaderSkin.isVisible() || !TabHeaderArea.this.measureClosingTabs && tabHeaderSkin.isClosing) continue;
                        d3 += tabHeaderSkin.prefWidth(d2);
                    }
                    return this.snapSize(d3) + this.snappedLeftInset() + this.snappedRightInset();
                }

                @Override
                protected double computePrefHeight(double d2) {
                    double d3 = 0.0;
                    for (Node node : this.getChildren()) {
                        TabHeaderSkin tabHeaderSkin = (TabHeaderSkin)node;
                        d3 = Math.max(d3, tabHeaderSkin.prefHeight(d2));
                    }
                    return this.snapSize(d3) + this.snappedTopInset() + this.snappedBottomInset();
                }

                @Override
                protected void layoutChildren() {
                    if (TabHeaderArea.this.tabsFit()) {
                        TabHeaderArea.this.setScrollOffset(0.0);
                    } else if (!TabHeaderArea.this.removeTab.isEmpty()) {
                        double d2 = 0.0;
                        double d3 = TabPaneSkin.this.tabHeaderArea.getWidth() - this.snapSize(TabHeaderArea.this.controlButtons.prefWidth(-1.0)) - TabHeaderArea.this.firstTabIndent() - 10.0;
                        Iterator iterator = this.getChildren().iterator();
                        while (iterator.hasNext()) {
                            TabHeaderSkin tabHeaderSkin = (TabHeaderSkin)iterator.next();
                            double d4 = this.snapSize(tabHeaderSkin.prefWidth(-1.0));
                            if (TabHeaderArea.this.removeTab.contains(tabHeaderSkin)) {
                                if (d2 < d3) {
                                    TabPaneSkin.this.isSelectingTab = true;
                                }
                                iterator.remove();
                                TabHeaderArea.this.removeTab.remove(tabHeaderSkin);
                                if (TabHeaderArea.this.removeTab.isEmpty()) break;
                            }
                            d2 += d4;
                        }
                    }
                    if (TabPaneSkin.this.isSelectingTab) {
                        TabHeaderArea.this.ensureSelectedTabIsVisible();
                        TabPaneSkin.this.isSelectingTab = false;
                    } else {
                        TabHeaderArea.this.validateScrollOffset();
                    }
                    Side side = ((TabPane)TabPaneSkin.this.getSkinnable()).getSide();
                    double d5 = this.snapSize(this.prefHeight(-1.0));
                    double d6 = side.equals((Object)Side.LEFT) || side.equals((Object)Side.BOTTOM) ? this.snapSize(this.getWidth()) - TabHeaderArea.this.getScrollOffset() : TabHeaderArea.this.getScrollOffset();
                    TabHeaderArea.this.updateHeaderClip();
                    for (Node node : this.getChildren()) {
                        double d7;
                        TabHeaderSkin tabHeaderSkin = (TabHeaderSkin)node;
                        double d8 = this.snapSize(tabHeaderSkin.prefWidth(-1.0) * tabHeaderSkin.animationTransition.get());
                        double d9 = this.snapSize(tabHeaderSkin.prefHeight(-1.0));
                        tabHeaderSkin.resize(d8, d9);
                        double d10 = d7 = side.equals((Object)Side.BOTTOM) ? 0.0 : d5 - d9 - this.snappedBottomInset();
                        if (side.equals((Object)Side.LEFT) || side.equals((Object)Side.BOTTOM)) {
                            tabHeaderSkin.relocate(d6 -= d8, d7);
                            continue;
                        }
                        tabHeaderSkin.relocate(d6, d7);
                        d6 += d8;
                    }
                }
            };
            this.headersRegion.getStyleClass().setAll("headers-region");
            this.headersRegion.setClip(this.headerClip);
            this.headerBackground = new StackPane();
            this.headerBackground.getStyleClass().setAll("tab-header-background");
            int n2 = 0;
            for (Tab tab : tabPane.getTabs()) {
                this.addTab(tab, n2++);
            }
            this.controlButtons = new TabControlButtons();
            this.controlButtons.setVisible(false);
            if (this.controlButtons.isVisible()) {
                this.controlButtons.setVisible(true);
            }
            this.getChildren().addAll(this.headerBackground, this.headersRegion, this.controlButtons);
            this.addEventHandler(ScrollEvent.SCROLL, scrollEvent -> {
                Side side = ((TabPane)TabPaneSkin.this.getSkinnable()).getSide();
                side = side == null ? Side.TOP : side;
                switch (side) {
                    default: {
                        this.setScrollOffset(this.scrollOffset - scrollEvent.getDeltaY());
                        break;
                    }
                    case LEFT: 
                    case RIGHT: {
                        this.setScrollOffset(this.scrollOffset + scrollEvent.getDeltaY());
                    }
                }
            });
        }

        private void updateHeaderClip() {
            Side side = ((TabPane)TabPaneSkin.this.getSkinnable()).getSide();
            double d2 = 0.0;
            double d3 = 0.0;
            double d4 = 0.0;
            double d5 = 0.0;
            double d6 = 0.0;
            double d7 = 0.0;
            double d8 = this.firstTabIndent();
            double d9 = this.snapSize(this.controlButtons.prefWidth(-1.0));
            this.measureClosingTabs = true;
            double d10 = this.snapSize(this.headersRegion.prefWidth(-1.0));
            this.measureClosingTabs = false;
            double d11 = this.snapSize(this.headersRegion.prefHeight(-1.0));
            if (d9 > 0.0) {
                d9 += 10.0;
            }
            if (this.headersRegion.getEffect() instanceof DropShadow) {
                DropShadow dropShadow = (DropShadow)this.headersRegion.getEffect();
                d7 = dropShadow.getRadius();
            }
            d6 = this.snapSize(this.getWidth()) - d9 - d8;
            if (side.equals((Object)Side.LEFT) || side.equals((Object)Side.BOTTOM)) {
                if (d10 < d6) {
                    d4 = d10 + d7;
                } else {
                    d2 = d10 - d6;
                    d4 = d6 + d7;
                }
                d5 = d11;
            } else {
                d2 = -d7;
                d4 = (d10 < d6 ? d10 : d6) + d7;
                d5 = d11;
            }
            this.headerClip.setX(d2);
            this.headerClip.setY(d3);
            this.headerClip.setWidth(d4);
            this.headerClip.setHeight(d5);
        }

        private void addTab(Tab tab, int n2) {
            TabHeaderSkin tabHeaderSkin = new TabHeaderSkin(tab);
            this.headersRegion.getChildren().add(n2, tabHeaderSkin);
        }

        private void removeTab(Tab tab) {
            TabHeaderSkin tabHeaderSkin = this.getTabHeaderSkin(tab);
            if (tabHeaderSkin != null) {
                if (this.tabsFit()) {
                    this.headersRegion.getChildren().remove(tabHeaderSkin);
                } else {
                    this.removeTab.add(tabHeaderSkin);
                    tabHeaderSkin.removeListeners(tab);
                }
            }
        }

        private TabHeaderSkin getTabHeaderSkin(Tab tab) {
            for (Node node : this.headersRegion.getChildren()) {
                TabHeaderSkin tabHeaderSkin = (TabHeaderSkin)node;
                if (!tabHeaderSkin.getTab().equals(tab)) continue;
                return tabHeaderSkin;
            }
            return null;
        }

        private boolean tabsFit() {
            double d2;
            double d3 = this.snapSize(this.headersRegion.prefWidth(-1.0));
            double d4 = d3 + (d2 = this.snapSize(this.controlButtons.prefWidth(-1.0))) + this.firstTabIndent() + 10.0;
            return d4 < this.getWidth();
        }

        private void ensureSelectedTabIsVisible() {
            double d2 = this.snapSize(TabPaneSkin.this.isHorizontal() ? ((TabPane)TabPaneSkin.this.getSkinnable()).getWidth() : ((TabPane)TabPaneSkin.this.getSkinnable()).getHeight());
            double d3 = this.snapSize(this.controlButtons.getWidth());
            double d4 = d2 - d3 - this.firstTabIndent() - 10.0;
            double d5 = 0.0;
            double d6 = 0.0;
            double d7 = 0.0;
            for (Node node : this.headersRegion.getChildren()) {
                TabHeaderSkin tabHeaderSkin = (TabHeaderSkin)node;
                double d8 = this.snapSize(tabHeaderSkin.prefWidth(-1.0));
                if (TabPaneSkin.this.selectedTab != null && TabPaneSkin.this.selectedTab.equals(tabHeaderSkin.getTab())) {
                    d6 = d5;
                    d7 = d8;
                }
                d5 += d8;
            }
            double d9 = this.getScrollOffset();
            double d10 = d6;
            double d11 = d6 + d7;
            double d12 = d4;
            if (d10 < -d9) {
                this.setScrollOffset(-d10);
            } else if (d11 > d12 - d9) {
                this.setScrollOffset(d12 - d11);
            }
        }

        public double getScrollOffset() {
            return this.scrollOffset;
        }

        private void validateScrollOffset() {
            this.setScrollOffset(this.getScrollOffset());
        }

        private void setScrollOffset(double d2) {
            double d3 = this.snapSize(TabPaneSkin.this.isHorizontal() ? ((TabPane)TabPaneSkin.this.getSkinnable()).getWidth() : ((TabPane)TabPaneSkin.this.getSkinnable()).getHeight());
            double d4 = this.snapSize(this.controlButtons.getWidth());
            double d5 = d3 - d4 - this.firstTabIndent() - 10.0;
            double d6 = 0.0;
            for (Node node : this.headersRegion.getChildren()) {
                TabHeaderSkin tabHeaderSkin = (TabHeaderSkin)node;
                double d7 = this.snapSize(tabHeaderSkin.prefWidth(-1.0));
                d6 += d7;
            }
            double d8 = d5 - d2 > d6 && d2 < 0.0 ? d5 - d6 : (d2 > 0.0 ? 0.0 : d2);
            if (d8 != this.scrollOffset) {
                this.scrollOffset = d8;
                this.headersRegion.requestLayout();
            }
        }

        private double firstTabIndent() {
            switch (((TabPane)TabPaneSkin.this.getSkinnable()).getSide()) {
                case TOP: 
                case BOTTOM: {
                    return this.snappedLeftInset();
                }
                case LEFT: 
                case RIGHT: {
                    return this.snappedTopInset();
                }
            }
            return 0.0;
        }

        @Override
        protected double computePrefWidth(double d2) {
            double d3 = TabPaneSkin.this.isHorizontal() ? this.snappedLeftInset() + this.snappedRightInset() : this.snappedTopInset() + this.snappedBottomInset();
            return this.snapSize(this.headersRegion.prefWidth(d2)) + this.controlButtons.prefWidth(d2) + this.firstTabIndent() + 10.0 + d3;
        }

        @Override
        protected double computePrefHeight(double d2) {
            double d3 = TabPaneSkin.this.isHorizontal() ? this.snappedTopInset() + this.snappedBottomInset() : this.snappedLeftInset() + this.snappedRightInset();
            return this.snapSize(this.headersRegion.prefHeight(-1.0)) + d3;
        }

        @Override
        public double getBaselineOffset() {
            if (((TabPane)TabPaneSkin.this.getSkinnable()).getSide() == Side.TOP) {
                return this.headersRegion.getBaselineOffset() + this.snappedTopInset();
            }
            return 0.0;
        }

        @Override
        protected void layoutChildren() {
            double d2 = this.snappedLeftInset();
            double d3 = this.snappedRightInset();
            double d4 = this.snappedTopInset();
            double d5 = this.snappedBottomInset();
            double d6 = this.snapSize(this.getWidth()) - (TabPaneSkin.this.isHorizontal() ? d2 + d3 : d4 + d5);
            double d7 = this.snapSize(this.getHeight()) - (TabPaneSkin.this.isHorizontal() ? d4 + d5 : d2 + d3);
            double d8 = this.snapSize(this.prefHeight(-1.0));
            double d9 = this.snapSize(this.headersRegion.prefWidth(-1.0));
            double d10 = this.snapSize(this.headersRegion.prefHeight(-1.0));
            this.controlButtons.showTabsMenu(!this.tabsFit());
            this.updateHeaderClip();
            this.headersRegion.requestLayout();
            double d11 = this.snapSize(this.controlButtons.prefWidth(-1.0));
            double d12 = this.controlButtons.prefHeight(d11);
            this.controlButtons.resize(d11, d12);
            this.headersRegion.resize(d9, d10);
            if (TabPaneSkin.this.isFloatingStyleClass()) {
                this.headerBackground.setVisible(false);
            } else {
                this.headerBackground.resize(this.snapSize(this.getWidth()), this.snapSize(this.getHeight()));
                this.headerBackground.setVisible(true);
            }
            double d13 = 0.0;
            double d14 = 0.0;
            double d15 = 0.0;
            double d16 = 0.0;
            Side side = ((TabPane)TabPaneSkin.this.getSkinnable()).getSide();
            if (side.equals((Object)Side.TOP)) {
                d13 = d2;
                d14 = d8 - d10 - d5;
                d15 = d6 - d11 + d2;
                d16 = this.snapSize(this.getHeight()) - d12 - d5;
            } else if (side.equals((Object)Side.RIGHT)) {
                d13 = d4;
                d14 = d8 - d10 - d2;
                d15 = d6 - d11 + d4;
                d16 = this.snapSize(this.getHeight()) - d12 - d2;
            } else if (side.equals((Object)Side.BOTTOM)) {
                d13 = this.snapSize(this.getWidth()) - d9 - d2;
                d14 = d8 - d10 - d4;
                d15 = d3;
                d16 = this.snapSize(this.getHeight()) - d12 - d4;
            } else if (side.equals((Object)Side.LEFT)) {
                d13 = this.snapSize(this.getWidth()) - d9 - d4;
                d14 = d8 - d10 - d3;
                d15 = d2;
                d16 = this.snapSize(this.getHeight()) - d12 - d3;
            }
            if (this.headerBackground.isVisible()) {
                this.positionInArea(this.headerBackground, 0.0, 0.0, this.snapSize(this.getWidth()), this.snapSize(this.getHeight()), 0.0, HPos.CENTER, VPos.CENTER);
            }
            this.positionInArea(this.headersRegion, d13, d14, d6, d7, 0.0, HPos.LEFT, VPos.CENTER);
            this.positionInArea(this.controlButtons, d15, d16, d11, d12, 0.0, HPos.CENTER, VPos.CENTER);
        }
    }

    private static class StyleableProperties {
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
        private static final CssMetaData<TabPane, TabAnimation> OPEN_TAB_ANIMATION;
        private static final CssMetaData<TabPane, TabAnimation> CLOSE_TAB_ANIMATION;

        private StyleableProperties() {
        }

        static {
            OPEN_TAB_ANIMATION = new CssMetaData<TabPane, TabAnimation>("-fx-open-tab-animation", new EnumConverter<TabAnimation>(TabAnimation.class), TabAnimation.GROW){

                @Override
                public boolean isSettable(TabPane tabPane) {
                    return true;
                }

                @Override
                public StyleableProperty<TabAnimation> getStyleableProperty(TabPane tabPane) {
                    TabPaneSkin tabPaneSkin = (TabPaneSkin)tabPane.getSkin();
                    return (StyleableProperty)((Object)tabPaneSkin.openTabAnimation);
                }
            };
            CLOSE_TAB_ANIMATION = new CssMetaData<TabPane, TabAnimation>("-fx-close-tab-animation", new EnumConverter<TabAnimation>(TabAnimation.class), TabAnimation.GROW){

                @Override
                public boolean isSettable(TabPane tabPane) {
                    return true;
                }

                @Override
                public StyleableProperty<TabAnimation> getStyleableProperty(TabPane tabPane) {
                    TabPaneSkin tabPaneSkin = (TabPaneSkin)tabPane.getSkin();
                    return (StyleableProperty)((Object)tabPaneSkin.closeTabAnimation);
                }
            };
            ArrayList arrayList = new ArrayList(SkinBase.getClassCssMetaData());
            arrayList.add(OPEN_TAB_ANIMATION);
            arrayList.add(CLOSE_TAB_ANIMATION);
            STYLEABLES = Collections.unmodifiableList(arrayList);
        }
    }

    private static enum TabAnimationState {
        SHOWING,
        HIDING,
        NONE;

    }

    private static enum TabAnimation {
        NONE,
        GROW;

    }
}

