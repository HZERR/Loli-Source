/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import com.sun.javafx.collections.UnmodifiableListSet;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.skin.TabPaneSkin;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javafx.beans.DefaultProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.Side;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Skin;
import javafx.scene.control.Tab;

@DefaultProperty(value="tabs")
public class TabPane
extends Control {
    private static final double DEFAULT_TAB_MIN_WIDTH = 0.0;
    private static final double DEFAULT_TAB_MAX_WIDTH = Double.MAX_VALUE;
    private static final double DEFAULT_TAB_MIN_HEIGHT = 0.0;
    private static final double DEFAULT_TAB_MAX_HEIGHT = Double.MAX_VALUE;
    public static final String STYLE_CLASS_FLOATING = "floating";
    private ObservableList<Tab> tabs = FXCollections.observableArrayList();
    private ObjectProperty<SingleSelectionModel<Tab>> selectionModel = new SimpleObjectProperty<SingleSelectionModel<Tab>>(this, "selectionModel");
    private ObjectProperty<Side> side;
    private ObjectProperty<TabClosingPolicy> tabClosingPolicy;
    private BooleanProperty rotateGraphic;
    private DoubleProperty tabMinWidth;
    private DoubleProperty tabMaxWidth;
    private DoubleProperty tabMinHeight;
    private DoubleProperty tabMaxHeight;
    private static final PseudoClass TOP_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("top");
    private static final PseudoClass BOTTOM_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("bottom");
    private static final PseudoClass LEFT_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("left");
    private static final PseudoClass RIGHT_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("right");

    public TabPane() {
        this(null);
    }

    public TabPane(Tab ... arrtab) {
        Side side;
        this.getStyleClass().setAll("tab-pane");
        this.setAccessibleRole(AccessibleRole.TAB_PANE);
        this.setSelectionModel(new TabPaneSelectionModel(this));
        this.tabs.addListener(change -> {
            while (change.next()) {
                for (Tab tab : change.getRemoved()) {
                    if (tab == null || this.getTabs().contains(tab)) continue;
                    tab.setTabPane(null);
                }
                for (Tab tab : change.getAddedSubList()) {
                    if (tab == null) continue;
                    tab.setTabPane(this);
                }
            }
        });
        if (arrtab != null) {
            this.getTabs().addAll(arrtab);
        }
        this.pseudoClassStateChanged(TOP_PSEUDOCLASS_STATE, (side = this.getSide()) == Side.TOP);
        this.pseudoClassStateChanged(RIGHT_PSEUDOCLASS_STATE, side == Side.RIGHT);
        this.pseudoClassStateChanged(BOTTOM_PSEUDOCLASS_STATE, side == Side.BOTTOM);
        this.pseudoClassStateChanged(LEFT_PSEUDOCLASS_STATE, side == Side.LEFT);
    }

    public final ObservableList<Tab> getTabs() {
        return this.tabs;
    }

    public final void setSelectionModel(SingleSelectionModel<Tab> singleSelectionModel) {
        this.selectionModel.set(singleSelectionModel);
    }

    public final SingleSelectionModel<Tab> getSelectionModel() {
        return (SingleSelectionModel)this.selectionModel.get();
    }

    public final ObjectProperty<SingleSelectionModel<Tab>> selectionModelProperty() {
        return this.selectionModel;
    }

    public final void setSide(Side side) {
        this.sideProperty().set(side);
    }

    public final Side getSide() {
        return this.side == null ? Side.TOP : (Side)((Object)this.side.get());
    }

    public final ObjectProperty<Side> sideProperty() {
        if (this.side == null) {
            this.side = new ObjectPropertyBase<Side>(Side.TOP){
                private Side oldSide;

                @Override
                protected void invalidated() {
                    this.oldSide = (Side)((Object)this.get());
                    TabPane.this.pseudoClassStateChanged(TOP_PSEUDOCLASS_STATE, this.oldSide == Side.TOP || this.oldSide == null);
                    TabPane.this.pseudoClassStateChanged(RIGHT_PSEUDOCLASS_STATE, this.oldSide == Side.RIGHT);
                    TabPane.this.pseudoClassStateChanged(BOTTOM_PSEUDOCLASS_STATE, this.oldSide == Side.BOTTOM);
                    TabPane.this.pseudoClassStateChanged(LEFT_PSEUDOCLASS_STATE, this.oldSide == Side.LEFT);
                }

                @Override
                public Object getBean() {
                    return TabPane.this;
                }

                @Override
                public String getName() {
                    return "side";
                }
            };
        }
        return this.side;
    }

    public final void setTabClosingPolicy(TabClosingPolicy tabClosingPolicy) {
        this.tabClosingPolicyProperty().set(tabClosingPolicy);
    }

    public final TabClosingPolicy getTabClosingPolicy() {
        return this.tabClosingPolicy == null ? TabClosingPolicy.SELECTED_TAB : (TabClosingPolicy)((Object)this.tabClosingPolicy.get());
    }

    public final ObjectProperty<TabClosingPolicy> tabClosingPolicyProperty() {
        if (this.tabClosingPolicy == null) {
            this.tabClosingPolicy = new SimpleObjectProperty<TabClosingPolicy>(this, "tabClosingPolicy", TabClosingPolicy.SELECTED_TAB);
        }
        return this.tabClosingPolicy;
    }

    public final void setRotateGraphic(boolean bl) {
        this.rotateGraphicProperty().set(bl);
    }

    public final boolean isRotateGraphic() {
        return this.rotateGraphic == null ? false : this.rotateGraphic.get();
    }

    public final BooleanProperty rotateGraphicProperty() {
        if (this.rotateGraphic == null) {
            this.rotateGraphic = new SimpleBooleanProperty(this, "rotateGraphic", false);
        }
        return this.rotateGraphic;
    }

    public final void setTabMinWidth(double d2) {
        this.tabMinWidthProperty().setValue(d2);
    }

    public final double getTabMinWidth() {
        return this.tabMinWidth == null ? 0.0 : this.tabMinWidth.getValue();
    }

    public final DoubleProperty tabMinWidthProperty() {
        if (this.tabMinWidth == null) {
            this.tabMinWidth = new StyleableDoubleProperty(0.0){

                @Override
                public CssMetaData<TabPane, Number> getCssMetaData() {
                    return StyleableProperties.TAB_MIN_WIDTH;
                }

                @Override
                public Object getBean() {
                    return TabPane.this;
                }

                @Override
                public String getName() {
                    return "tabMinWidth";
                }
            };
        }
        return this.tabMinWidth;
    }

    public final void setTabMaxWidth(double d2) {
        this.tabMaxWidthProperty().setValue(d2);
    }

    public final double getTabMaxWidth() {
        return this.tabMaxWidth == null ? Double.MAX_VALUE : this.tabMaxWidth.getValue();
    }

    public final DoubleProperty tabMaxWidthProperty() {
        if (this.tabMaxWidth == null) {
            this.tabMaxWidth = new StyleableDoubleProperty(Double.MAX_VALUE){

                @Override
                public CssMetaData<TabPane, Number> getCssMetaData() {
                    return StyleableProperties.TAB_MAX_WIDTH;
                }

                @Override
                public Object getBean() {
                    return TabPane.this;
                }

                @Override
                public String getName() {
                    return "tabMaxWidth";
                }
            };
        }
        return this.tabMaxWidth;
    }

    public final void setTabMinHeight(double d2) {
        this.tabMinHeightProperty().setValue(d2);
    }

    public final double getTabMinHeight() {
        return this.tabMinHeight == null ? 0.0 : this.tabMinHeight.getValue();
    }

    public final DoubleProperty tabMinHeightProperty() {
        if (this.tabMinHeight == null) {
            this.tabMinHeight = new StyleableDoubleProperty(0.0){

                @Override
                public CssMetaData<TabPane, Number> getCssMetaData() {
                    return StyleableProperties.TAB_MIN_HEIGHT;
                }

                @Override
                public Object getBean() {
                    return TabPane.this;
                }

                @Override
                public String getName() {
                    return "tabMinHeight";
                }
            };
        }
        return this.tabMinHeight;
    }

    public final void setTabMaxHeight(double d2) {
        this.tabMaxHeightProperty().setValue(d2);
    }

    public final double getTabMaxHeight() {
        return this.tabMaxHeight == null ? Double.MAX_VALUE : this.tabMaxHeight.getValue();
    }

    public final DoubleProperty tabMaxHeightProperty() {
        if (this.tabMaxHeight == null) {
            this.tabMaxHeight = new StyleableDoubleProperty(Double.MAX_VALUE){

                @Override
                public CssMetaData<TabPane, Number> getCssMetaData() {
                    return StyleableProperties.TAB_MAX_HEIGHT;
                }

                @Override
                public Object getBean() {
                    return TabPane.this;
                }

                @Override
                public String getName() {
                    return "tabMaxHeight";
                }
            };
        }
        return this.tabMaxHeight;
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new TabPaneSkin(this);
    }

    @Override
    public Node lookup(String string) {
        Node node;
        block1: {
            Tab tab;
            node = super.lookup(string);
            if (node != null) break block1;
            Iterator iterator = this.tabs.iterator();
            while (iterator.hasNext() && (node = (tab = (Tab)iterator.next()).lookup(string)) == null) {
            }
        }
        return node;
    }

    @Override
    public Set<Node> lookupAll(String string) {
        if (string == null) {
            return null;
        }
        ArrayList<Node> arrayList = new ArrayList<Node>();
        arrayList.addAll(super.lookupAll(string));
        for (Tab tab : this.tabs) {
            arrayList.addAll(tab.lookupAll(string));
        }
        return new UnmodifiableListSet<Node>(arrayList);
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return TabPane.getClassCssMetaData();
    }

    public static enum TabClosingPolicy {
        SELECTED_TAB,
        ALL_TABS,
        UNAVAILABLE;

    }

    static class TabPaneSelectionModel
    extends SingleSelectionModel<Tab> {
        private final TabPane tabPane;

        public TabPaneSelectionModel(TabPane tabPane) {
            if (tabPane == null) {
                throw new NullPointerException("TabPane can not be null");
            }
            this.tabPane = tabPane;
            ListChangeListener listChangeListener = change -> {
                while (change.next()) {
                    for (Tab tab : change.getRemoved()) {
                        if (tab == null || this.tabPane.getTabs().contains(tab) || !tab.isSelected()) continue;
                        tab.setSelected(false);
                        int n2 = change.getFrom();
                        this.findNearestAvailableTab(n2, true);
                    }
                    if (!change.wasAdded() && !change.wasRemoved() || this.getSelectedIndex() == this.tabPane.getTabs().indexOf(this.getSelectedItem())) continue;
                    this.clearAndSelect(this.tabPane.getTabs().indexOf(this.getSelectedItem()));
                }
                if (this.getSelectedIndex() == -1 && this.getSelectedItem() == null && this.tabPane.getTabs().size() > 0) {
                    this.findNearestAvailableTab(0, true);
                } else if (this.tabPane.getTabs().isEmpty()) {
                    this.clearSelection();
                }
            };
            if (this.tabPane.getTabs() != null) {
                this.tabPane.getTabs().addListener(listChangeListener);
            }
        }

        @Override
        public void select(int n2) {
            if (n2 < 0 || this.getItemCount() > 0 && n2 >= this.getItemCount() || n2 == this.getSelectedIndex() && this.getModelItem(n2).isSelected()) {
                return;
            }
            if (this.getSelectedIndex() >= 0 && this.getSelectedIndex() < this.tabPane.getTabs().size()) {
                ((Tab)this.tabPane.getTabs().get(this.getSelectedIndex())).setSelected(false);
            }
            this.setSelectedIndex(n2);
            Tab tab = this.getModelItem(n2);
            if (tab != null) {
                this.setSelectedItem(tab);
            }
            if (this.getSelectedIndex() >= 0 && this.getSelectedIndex() < this.tabPane.getTabs().size()) {
                ((Tab)this.tabPane.getTabs().get(this.getSelectedIndex())).setSelected(true);
            }
            this.tabPane.notifyAccessibleAttributeChanged(AccessibleAttribute.FOCUS_ITEM);
        }

        @Override
        public void select(Tab tab) {
            int n2 = this.getItemCount();
            for (int i2 = 0; i2 < n2; ++i2) {
                Tab tab2 = this.getModelItem(i2);
                if (tab2 == null || !tab2.equals(tab)) continue;
                this.select(i2);
                return;
            }
            if (tab != null) {
                this.setSelectedItem(tab);
            }
        }

        @Override
        protected Tab getModelItem(int n2) {
            ObservableList<Tab> observableList = this.tabPane.getTabs();
            if (observableList == null) {
                return null;
            }
            if (n2 < 0 || n2 >= observableList.size()) {
                return null;
            }
            return (Tab)observableList.get(n2);
        }

        @Override
        protected int getItemCount() {
            ObservableList<Tab> observableList = this.tabPane.getTabs();
            return observableList == null ? 0 : observableList.size();
        }

        private Tab findNearestAvailableTab(int n2, boolean bl) {
            int n3 = this.getItemCount();
            int n4 = 1;
            Tab tab = null;
            while (true) {
                Tab tab2;
                Tab tab3;
                int n5;
                if ((n5 = n2 - n4) >= 0 && (tab3 = this.getModelItem(n5)) != null && !tab3.isDisable()) {
                    tab = tab3;
                    break;
                }
                int n6 = n2 + n4 - 1;
                if (n6 < n3 && (tab2 = this.getModelItem(n6)) != null && !tab2.isDisable()) {
                    tab = tab2;
                    break;
                }
                if (n5 < 0 && n6 >= n3) break;
                ++n4;
            }
            if (bl && tab != null) {
                this.select(tab);
            }
            return tab;
        }
    }

    private static class StyleableProperties {
        private static final CssMetaData<TabPane, Number> TAB_MIN_WIDTH = new CssMetaData<TabPane, Number>("-fx-tab-min-width", SizeConverter.getInstance(), (Number)0.0){

            @Override
            public boolean isSettable(TabPane tabPane) {
                return tabPane.tabMinWidth == null || !tabPane.tabMinWidth.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(TabPane tabPane) {
                return (StyleableProperty)((Object)tabPane.tabMinWidthProperty());
            }
        };
        private static final CssMetaData<TabPane, Number> TAB_MAX_WIDTH = new CssMetaData<TabPane, Number>("-fx-tab-max-width", SizeConverter.getInstance(), (Number)Double.MAX_VALUE){

            @Override
            public boolean isSettable(TabPane tabPane) {
                return tabPane.tabMaxWidth == null || !tabPane.tabMaxWidth.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(TabPane tabPane) {
                return (StyleableProperty)((Object)tabPane.tabMaxWidthProperty());
            }
        };
        private static final CssMetaData<TabPane, Number> TAB_MIN_HEIGHT = new CssMetaData<TabPane, Number>("-fx-tab-min-height", SizeConverter.getInstance(), (Number)0.0){

            @Override
            public boolean isSettable(TabPane tabPane) {
                return tabPane.tabMinHeight == null || !tabPane.tabMinHeight.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(TabPane tabPane) {
                return (StyleableProperty)((Object)tabPane.tabMinHeightProperty());
            }
        };
        private static final CssMetaData<TabPane, Number> TAB_MAX_HEIGHT = new CssMetaData<TabPane, Number>("-fx-tab-max-height", SizeConverter.getInstance(), (Number)Double.MAX_VALUE){

            @Override
            public boolean isSettable(TabPane tabPane) {
                return tabPane.tabMaxHeight == null || !tabPane.tabMaxHeight.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(TabPane tabPane) {
                return (StyleableProperty)((Object)tabPane.tabMaxHeightProperty());
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            ArrayList arrayList = new ArrayList(Control.getClassCssMetaData());
            arrayList.add(TAB_MIN_WIDTH);
            arrayList.add(TAB_MAX_WIDTH);
            arrayList.add(TAB_MIN_HEIGHT);
            arrayList.add(TAB_MAX_HEIGHT);
            STYLEABLES = Collections.unmodifiableList(arrayList);
        }
    }
}

