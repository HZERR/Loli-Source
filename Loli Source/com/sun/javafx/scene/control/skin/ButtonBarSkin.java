/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class ButtonBarSkin
extends BehaviorSkinBase<ButtonBar, BehaviorBase<ButtonBar>> {
    private static final double GAP_SIZE = 10.0;
    private static final String CATEGORIZED_TYPES = "LRHEYNXBIACO";
    public static final String BUTTON_DATA_PROPERTY = "javafx.scene.control.ButtonBar.ButtonData";
    public static final String BUTTON_SIZE_INDEPENDENCE = "javafx.scene.control.ButtonBar.independentSize";
    private static final double DO_NOT_CHANGE_SIZE = Double.MAX_VALUE;
    private HBox layout;
    private InvalidationListener buttonDataListener = observable -> this.layoutButtons();

    public ButtonBarSkin(ButtonBar buttonBar) {
        super(buttonBar, new BehaviorBase<ButtonBar>(buttonBar, Collections.emptyList()));
        this.layout = new HBox(10.0){

            @Override
            protected void layoutChildren() {
                ButtonBarSkin.this.resizeButtons();
                super.layoutChildren();
            }
        };
        this.layout.setAlignment(Pos.CENTER);
        this.layout.getStyleClass().add("container");
        this.getChildren().add(this.layout);
        this.layoutButtons();
        this.updateButtonListeners(buttonBar.getButtons(), true);
        buttonBar.getButtons().addListener(change -> {
            while (change.next()) {
                this.updateButtonListeners(change.getRemoved(), false);
                this.updateButtonListeners(change.getAddedSubList(), true);
            }
            this.layoutButtons();
        });
        this.registerChangeListener(buttonBar.buttonOrderProperty(), "BUTTON_ORDER");
        this.registerChangeListener(buttonBar.buttonMinWidthProperty(), "BUTTON_MIN_WIDTH");
    }

    private void updateButtonListeners(List<? extends Node> list, boolean bl) {
        if (list != null) {
            for (Node node : list) {
                ObjectProperty objectProperty;
                ObservableMap<Object, Object> observableMap = node.getProperties();
                if (!observableMap.containsKey(BUTTON_DATA_PROPERTY) || (objectProperty = (ObjectProperty)observableMap.get(BUTTON_DATA_PROPERTY)) == null) continue;
                if (bl) {
                    objectProperty.addListener(this.buttonDataListener);
                    continue;
                }
                objectProperty.removeListener(this.buttonDataListener);
            }
        }
    }

    @Override
    protected void handleControlPropertyChanged(String string) {
        super.handleControlPropertyChanged(string);
        if ("BUTTON_ORDER".equals(string)) {
            this.layoutButtons();
        } else if ("BUTTON_MIN_WIDTH".equals(string)) {
            this.resizeButtons();
        }
    }

    private void layoutButtons() {
        ButtonBar buttonBar = (ButtonBar)this.getSkinnable();
        ObservableList<Node> observableList = buttonBar.getButtons();
        double d2 = buttonBar.getButtonMinWidth();
        String string = ((ButtonBar)this.getSkinnable()).getButtonOrder();
        this.layout.getChildren().clear();
        if (string == null) {
            throw new IllegalStateException("ButtonBar buttonOrder string can not be null");
        }
        if (string == "") {
            Spacer.DYNAMIC.add(this.layout, true);
            for (Node node : observableList) {
                this.sizeButton(node, d2, Double.MAX_VALUE, Double.MAX_VALUE);
                this.layout.getChildren().add(node);
                HBox.setHgrow(node, Priority.NEVER);
            }
        } else {
            this.doButtonOrderLayout(string);
        }
    }

    private void doButtonOrderLayout(String string) {
        Object object;
        int n2;
        int n3;
        int n4;
        ButtonBar buttonBar = (ButtonBar)this.getSkinnable();
        ObservableList<Node> observableList = buttonBar.getButtons();
        double d2 = buttonBar.getButtonMinWidth();
        Map<String, List<Node>> map = this.buildButtonMap(observableList);
        char[] arrc = string.toCharArray();
        int n5 = 0;
        Spacer spacer = Spacer.NONE;
        for (n4 = 0; n4 < arrc.length; ++n4) {
            boolean bl;
            n3 = arrc[n4];
            n2 = n5 <= 0 && n5 >= observableList.size() - 1 ? 1 : 0;
            boolean bl2 = bl = !this.layout.getChildren().isEmpty();
            if (n3 == 43) {
                spacer = spacer.replace(Spacer.DYNAMIC);
                continue;
            }
            if (n3 == 95 && bl) {
                spacer = spacer.replace(Spacer.FIXED);
                continue;
            }
            object = map.get(String.valueOf((char)n3).toUpperCase());
            if (object == null) continue;
            spacer.add(this.layout, n2 != 0);
            Iterator iterator = object.iterator();
            while (iterator.hasNext()) {
                Node node = (Node)iterator.next();
                this.sizeButton(node, d2, Double.MAX_VALUE, Double.MAX_VALUE);
                this.layout.getChildren().add(node);
                HBox.setHgrow(node, Priority.NEVER);
                ++n5;
            }
            spacer = spacer.replace(Spacer.NONE);
        }
        n4 = 0;
        n3 = observableList.size();
        for (n2 = 0; n2 < n3; n2 += 1) {
            Node node = (Node)observableList.get(n2);
            if (!(node instanceof Button) || !((Button)node).isDefaultButton()) continue;
            node.requestFocus();
            n4 = 1;
            break;
        }
        if (n4 == 0) {
            for (n2 = 0; n2 < n3; n2 += 1) {
                Node node = (Node)observableList.get(n2);
                object = ButtonBar.getButtonData(node);
                if (object == null || !object.isDefaultButton()) continue;
                node.requestFocus();
                n4 = 1;
                break;
            }
        }
    }

    private void resizeButtons() {
        ButtonBar buttonBar = (ButtonBar)this.getSkinnable();
        double d2 = buttonBar.getButtonMinWidth();
        ObservableList<Node> observableList = buttonBar.getButtons();
        double d3 = d2;
        for (Node node : observableList) {
            if (!ButtonBar.isButtonUniformSize(node)) continue;
            d3 = Math.max(node.prefWidth(-1.0), d3);
        }
        for (Node node : observableList) {
            if (!ButtonBar.isButtonUniformSize(node)) continue;
            this.sizeButton(node, Double.MAX_VALUE, d3, Double.MAX_VALUE);
        }
    }

    private void sizeButton(Node node, double d2, double d3, double d4) {
        if (node instanceof Region) {
            Region region = (Region)node;
            if (d2 != Double.MAX_VALUE) {
                region.setMinWidth(d2);
            }
            if (d3 != Double.MAX_VALUE) {
                region.setPrefWidth(d3);
            }
            if (d4 != Double.MAX_VALUE) {
                region.setMaxWidth(d4);
            }
        }
    }

    private String getButtonType(Node node) {
        String string;
        ButtonBar.ButtonData buttonData = ButtonBar.getButtonData(node);
        if (buttonData == null) {
            buttonData = ButtonBar.ButtonData.OTHER;
        }
        string = (string = buttonData.getTypeCode()).length() > 0 ? string.substring(0, 1) : "";
        return CATEGORIZED_TYPES.contains(string.toUpperCase()) ? string : ButtonBar.ButtonData.OTHER.getTypeCode();
    }

    private Map<String, List<Node>> buildButtonMap(List<? extends Node> list) {
        HashMap<String, List<Node>> hashMap = new HashMap<String, List<Node>>();
        for (Node node : list) {
            if (node == null) continue;
            String string = this.getButtonType(node);
            ArrayList<Node> arrayList = (ArrayList<Node>)hashMap.get(string);
            if (arrayList == null) {
                arrayList = new ArrayList<Node>();
                hashMap.put(string, arrayList);
            }
            arrayList.add(node);
        }
        return hashMap;
    }

    private static enum Spacer {
        FIXED{

            @Override
            protected Node create(boolean bl) {
                if (bl) {
                    return null;
                }
                Region region = new Region();
                ButtonBar.setButtonData(region, ButtonBar.ButtonData.SMALL_GAP);
                region.setMinWidth(10.0);
                HBox.setHgrow(region, Priority.NEVER);
                return region;
            }
        }
        ,
        DYNAMIC{

            @Override
            protected Node create(boolean bl) {
                Region region = new Region();
                ButtonBar.setButtonData(region, ButtonBar.ButtonData.BIG_GAP);
                region.setMinWidth(bl ? 0.0 : 10.0);
                HBox.setHgrow(region, Priority.ALWAYS);
                return region;
            }

            @Override
            public Spacer replace(Spacer spacer) {
                return FIXED == spacer ? this : spacer;
            }
        }
        ,
        NONE;


        protected Node create(boolean bl) {
            return null;
        }

        public Spacer replace(Spacer spacer) {
            return spacer;
        }

        public void add(Pane pane, boolean bl) {
            Node node = this.create(bl);
            if (node != null) {
                pane.getChildren().add(node);
            }
        }
    }
}

