/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import javafx.geometry.Side;
import javafx.scene.control.ControlBuilder;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.util.Builder;

@Deprecated
public class TabPaneBuilder<B extends TabPaneBuilder<B>>
extends ControlBuilder<B>
implements Builder<TabPane> {
    private int __set;
    private boolean rotateGraphic;
    private SingleSelectionModel<Tab> selectionModel;
    private Side side;
    private TabPane.TabClosingPolicy tabClosingPolicy;
    private double tabMaxHeight;
    private double tabMaxWidth;
    private double tabMinHeight;
    private double tabMinWidth;
    private Collection<? extends Tab> tabs;

    protected TabPaneBuilder() {
    }

    public static TabPaneBuilder<?> create() {
        return new TabPaneBuilder();
    }

    private void __set(int n2) {
        this.__set |= 1 << n2;
    }

    public void applyTo(TabPane tabPane) {
        super.applyTo(tabPane);
        int n2 = this.__set;
        while (n2 != 0) {
            int n3 = Integer.numberOfTrailingZeros(n2);
            n2 &= ~(1 << n3);
            switch (n3) {
                case 0: {
                    tabPane.setRotateGraphic(this.rotateGraphic);
                    break;
                }
                case 1: {
                    tabPane.setSelectionModel(this.selectionModel);
                    break;
                }
                case 2: {
                    tabPane.setSide(this.side);
                    break;
                }
                case 3: {
                    tabPane.setTabClosingPolicy(this.tabClosingPolicy);
                    break;
                }
                case 4: {
                    tabPane.setTabMaxHeight(this.tabMaxHeight);
                    break;
                }
                case 5: {
                    tabPane.setTabMaxWidth(this.tabMaxWidth);
                    break;
                }
                case 6: {
                    tabPane.setTabMinHeight(this.tabMinHeight);
                    break;
                }
                case 7: {
                    tabPane.setTabMinWidth(this.tabMinWidth);
                    break;
                }
                case 8: {
                    tabPane.getTabs().addAll(this.tabs);
                }
            }
        }
    }

    public B rotateGraphic(boolean bl) {
        this.rotateGraphic = bl;
        this.__set(0);
        return (B)this;
    }

    public B selectionModel(SingleSelectionModel<Tab> singleSelectionModel) {
        this.selectionModel = singleSelectionModel;
        this.__set(1);
        return (B)this;
    }

    public B side(Side side) {
        this.side = side;
        this.__set(2);
        return (B)this;
    }

    public B tabClosingPolicy(TabPane.TabClosingPolicy tabClosingPolicy) {
        this.tabClosingPolicy = tabClosingPolicy;
        this.__set(3);
        return (B)this;
    }

    public B tabMaxHeight(double d2) {
        this.tabMaxHeight = d2;
        this.__set(4);
        return (B)this;
    }

    public B tabMaxWidth(double d2) {
        this.tabMaxWidth = d2;
        this.__set(5);
        return (B)this;
    }

    public B tabMinHeight(double d2) {
        this.tabMinHeight = d2;
        this.__set(6);
        return (B)this;
    }

    public B tabMinWidth(double d2) {
        this.tabMinWidth = d2;
        this.__set(7);
        return (B)this;
    }

    public B tabs(Collection<? extends Tab> collection) {
        this.tabs = collection;
        this.__set(8);
        return (B)this;
    }

    public B tabs(Tab ... arrtab) {
        return this.tabs(Arrays.asList(arrtab));
    }

    @Override
    public TabPane build() {
        TabPane tabPane = new TabPane();
        this.applyTo(tabPane);
        return tabPane;
    }
}

