/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.AccordionBehavior;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import com.sun.javafx.scene.control.skin.TitledPaneSkin;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Skin;
import javafx.scene.control.TitledPane;
import javafx.scene.shape.Rectangle;

public class AccordionSkin
extends BehaviorSkinBase<Accordion, AccordionBehavior> {
    private TitledPane firstTitledPane;
    private Rectangle clipRect;
    private boolean forceRelayout = false;
    private boolean relayout = false;
    private double previousHeight = 0.0;
    private TitledPane expandedPane = null;
    private TitledPane previousPane = null;
    private Map<TitledPane, ChangeListener<Boolean>> listeners = new HashMap<TitledPane, ChangeListener<Boolean>>();

    public AccordionSkin(Accordion accordion) {
        super(accordion, new AccordionBehavior(accordion));
        accordion.getPanes().addListener(change -> {
            if (this.firstTitledPane != null) {
                this.firstTitledPane.getStyleClass().remove("first-titled-pane");
            }
            if (!accordion.getPanes().isEmpty()) {
                this.firstTitledPane = (TitledPane)accordion.getPanes().get(0);
                this.firstTitledPane.getStyleClass().add("first-titled-pane");
            }
            this.getChildren().setAll((Collection<Node>)accordion.getPanes());
            while (change.next()) {
                this.removeTitledPaneListeners(change.getRemoved());
                this.initTitledPaneListeners(change.getAddedSubList());
            }
            this.forceRelayout = true;
        });
        if (!accordion.getPanes().isEmpty()) {
            this.firstTitledPane = (TitledPane)accordion.getPanes().get(0);
            this.firstTitledPane.getStyleClass().add("first-titled-pane");
        }
        this.clipRect = new Rectangle(accordion.getWidth(), accordion.getHeight());
        ((Accordion)this.getSkinnable()).setClip(this.clipRect);
        this.initTitledPaneListeners(accordion.getPanes());
        this.getChildren().setAll((Collection<Node>)accordion.getPanes());
        ((Accordion)this.getSkinnable()).requestLayout();
        this.registerChangeListener(((Accordion)this.getSkinnable()).widthProperty(), "WIDTH");
        this.registerChangeListener(((Accordion)this.getSkinnable()).heightProperty(), "HEIGHT");
    }

    @Override
    protected void handleControlPropertyChanged(String string) {
        super.handleControlPropertyChanged(string);
        if ("WIDTH".equals(string)) {
            this.clipRect.setWidth(((Accordion)this.getSkinnable()).getWidth());
        } else if ("HEIGHT".equals(string)) {
            this.clipRect.setHeight(((Accordion)this.getSkinnable()).getHeight());
            this.relayout = true;
        }
    }

    @Override
    protected double computeMinHeight(double d2, double d3, double d4, double d5, double d6) {
        double d7 = 0.0;
        if (this.expandedPane != null) {
            d7 += this.expandedPane.minHeight(d2);
        }
        if (this.previousPane != null && !this.previousPane.equals(this.expandedPane)) {
            d7 += this.previousPane.minHeight(d2);
        }
        for (Node node : this.getChildren()) {
            TitledPane titledPane = (TitledPane)node;
            if (titledPane.equals(this.expandedPane) || titledPane.equals(this.previousPane)) continue;
            Skin<?> skin = ((TitledPane)node).getSkin();
            if (skin instanceof TitledPaneSkin) {
                TitledPaneSkin titledPaneSkin = (TitledPaneSkin)skin;
                d7 += titledPaneSkin.getTitleRegionSize(d2);
                continue;
            }
            d7 += titledPane.minHeight(d2);
        }
        return d7 + d3 + d5;
    }

    @Override
    protected double computePrefHeight(double d2, double d3, double d4, double d5, double d6) {
        double d7 = 0.0;
        if (this.expandedPane != null) {
            d7 += this.expandedPane.prefHeight(d2);
        }
        if (this.previousPane != null && !this.previousPane.equals(this.expandedPane)) {
            d7 += this.previousPane.prefHeight(d2);
        }
        for (Node node : this.getChildren()) {
            TitledPane titledPane = (TitledPane)node;
            if (titledPane.equals(this.expandedPane) || titledPane.equals(this.previousPane)) continue;
            Skin<?> skin = ((TitledPane)node).getSkin();
            if (skin instanceof TitledPaneSkin) {
                TitledPaneSkin titledPaneSkin = (TitledPaneSkin)skin;
                d7 += titledPaneSkin.getTitleRegionSize(d2);
                continue;
            }
            d7 += titledPane.prefHeight(d2);
        }
        return d7 + d3 + d5;
    }

    @Override
    protected void layoutChildren(double d2, double d3, double d4, double d5) {
        boolean bl = this.forceRelayout || this.relayout && this.previousHeight != d5;
        this.forceRelayout = false;
        this.previousHeight = d5;
        double d6 = 0.0;
        for (TitledPane titledPane : ((Accordion)this.getSkinnable()).getPanes()) {
            if (titledPane.equals(this.expandedPane)) continue;
            TitledPaneSkin titledPaneSkin = (TitledPaneSkin)titledPane.getSkin();
            d6 += this.snapSize(titledPaneSkin.getTitleRegionSize(d4));
        }
        double d7 = d5 - d6;
        for (TitledPane titledPane : ((Accordion)this.getSkinnable()).getPanes()) {
            double d8;
            Skin<?> skin = titledPane.getSkin();
            if (skin instanceof TitledPaneSkin) {
                ((TitledPaneSkin)skin).setMaxTitledPaneHeightForAccordion(d7);
                d8 = this.snapSize(((TitledPaneSkin)skin).getTitledPaneHeightForAccordion());
            } else {
                d8 = titledPane.prefHeight(d4);
            }
            titledPane.resize(d4, d8);
            boolean bl2 = true;
            if (!bl && this.previousPane != null && this.expandedPane != null) {
                ObservableList<TitledPane> observableList = ((Accordion)this.getSkinnable()).getPanes();
                int n2 = observableList.indexOf(this.previousPane);
                int n3 = observableList.indexOf(this.expandedPane);
                int n4 = observableList.indexOf(titledPane);
                if (n2 < n3) {
                    if (n4 <= n3) {
                        titledPane.relocate(d2, d3);
                        d3 += d8;
                        bl2 = false;
                    }
                } else if (n2 > n3) {
                    if (n4 <= n2) {
                        titledPane.relocate(d2, d3);
                        d3 += d8;
                        bl2 = false;
                    }
                } else {
                    titledPane.relocate(d2, d3);
                    d3 += d8;
                    bl2 = false;
                }
            }
            if (!bl2) continue;
            titledPane.relocate(d2, d3);
            d3 += d8;
        }
    }

    private void initTitledPaneListeners(List<? extends TitledPane> list) {
        for (TitledPane titledPane : list) {
            titledPane.setExpanded(titledPane == ((Accordion)this.getSkinnable()).getExpandedPane());
            if (titledPane.isExpanded()) {
                this.expandedPane = titledPane;
            }
            ChangeListener<Boolean> changeListener = this.expandedPropertyListener(titledPane);
            titledPane.expandedProperty().addListener(changeListener);
            this.listeners.put(titledPane, changeListener);
        }
    }

    private void removeTitledPaneListeners(List<? extends TitledPane> list) {
        for (TitledPane titledPane : list) {
            if (!this.listeners.containsKey(titledPane)) continue;
            titledPane.expandedProperty().removeListener(this.listeners.get(titledPane));
            this.listeners.remove(titledPane);
        }
    }

    private ChangeListener<Boolean> expandedPropertyListener(TitledPane titledPane) {
        return (observableValue, bl, bl2) -> {
            this.previousPane = this.expandedPane;
            Accordion accordion = (Accordion)this.getSkinnable();
            if (bl2.booleanValue()) {
                if (this.expandedPane != null) {
                    this.expandedPane.setExpanded(false);
                }
                if (titledPane != null) {
                    accordion.setExpandedPane(titledPane);
                }
                this.expandedPane = accordion.getExpandedPane();
            } else {
                this.expandedPane = null;
                accordion.setExpandedPane(null);
            }
        };
    }
}

