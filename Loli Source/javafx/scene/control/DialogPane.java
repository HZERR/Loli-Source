/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import com.sun.javafx.css.StyleManager;
import com.sun.javafx.css.converters.StringConverter;
import com.sun.javafx.scene.control.skin.Utils;
import com.sun.javafx.scene.control.skin.resources.ControlResources;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import javafx.beans.DefaultProperty;
import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.StyleOrigin;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.css.StyleableStringProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

@DefaultProperty(value="buttonTypes")
public class DialogPane
extends Pane {
    private final GridPane headerTextPanel;
    private final Label contentLabel;
    private final StackPane graphicContainer;
    private final Node buttonBar;
    private final ObservableList<ButtonType> buttons = FXCollections.observableArrayList();
    private final Map<ButtonType, Node> buttonNodes = new WeakHashMap<ButtonType, Node>();
    private Node detailsButton;
    private Dialog<?> dialog;
    private final ObjectProperty<Node> graphicProperty = new StyleableObjectProperty<Node>(){
        WeakReference<Node> graphicRef = new WeakReference<Object>(null);

        @Override
        public CssMetaData getCssMetaData() {
            return StyleableProperties.GRAPHIC;
        }

        @Override
        public Object getBean() {
            return DialogPane.this;
        }

        @Override
        public String getName() {
            return "graphic";
        }

        @Override
        protected void invalidated() {
            Node node = (Node)this.graphicRef.get();
            if (node != null) {
                DialogPane.this.getChildren().remove(node);
            }
            Node node2 = DialogPane.this.getGraphic();
            this.graphicRef = new WeakReference<Node>(node2);
            DialogPane.this.updateHeaderArea();
        }
    };
    private StyleableStringProperty imageUrl = null;
    private final ObjectProperty<Node> header = new SimpleObjectProperty<Node>(null){
        WeakReference<Node> headerRef;
        {
            this.headerRef = new WeakReference<Object>(null);
        }

        @Override
        protected void invalidated() {
            Node node = (Node)this.headerRef.get();
            if (node != null) {
                DialogPane.this.getChildren().remove(node);
            }
            Node node2 = DialogPane.this.getHeader();
            this.headerRef = new WeakReference<Node>(node2);
            DialogPane.this.updateHeaderArea();
        }
    };
    private final StringProperty headerText = new SimpleStringProperty(this, "headerText"){

        @Override
        protected void invalidated() {
            DialogPane.this.updateHeaderArea();
            DialogPane.this.requestLayout();
        }
    };
    private final ObjectProperty<Node> content = new SimpleObjectProperty<Node>(null){
        WeakReference<Node> contentRef;
        {
            this.contentRef = new WeakReference<Object>(null);
        }

        @Override
        protected void invalidated() {
            Node node = (Node)this.contentRef.get();
            if (node != null) {
                DialogPane.this.getChildren().remove(node);
            }
            Node node2 = DialogPane.this.getContent();
            this.contentRef = new WeakReference<Node>(node2);
            DialogPane.this.updateContentArea();
        }
    };
    private final StringProperty contentText = new SimpleStringProperty(this, "contentText"){

        @Override
        protected void invalidated() {
            DialogPane.this.updateContentArea();
            DialogPane.this.requestLayout();
        }
    };
    private final ObjectProperty<Node> expandableContentProperty = new SimpleObjectProperty<Node>(null){
        WeakReference<Node> expandableContentRef;
        {
            this.expandableContentRef = new WeakReference<Object>(null);
        }

        @Override
        protected void invalidated() {
            Node node = (Node)this.expandableContentRef.get();
            if (node != null) {
                DialogPane.this.getChildren().remove(node);
            }
            Node node2 = DialogPane.this.getExpandableContent();
            this.expandableContentRef = new WeakReference<Node>(node2);
            if (node2 != null) {
                node2.setVisible(DialogPane.this.isExpanded());
                node2.setManaged(DialogPane.this.isExpanded());
                if (!node2.getStyleClass().contains("expandable-content")) {
                    node2.getStyleClass().add("expandable-content");
                }
                DialogPane.this.getChildren().add(node2);
            }
        }
    };
    private final BooleanProperty expandedProperty = new SimpleBooleanProperty(this, "expanded", false){

        @Override
        protected void invalidated() {
            Node node = DialogPane.this.getExpandableContent();
            if (node != null) {
                node.setVisible(DialogPane.this.isExpanded());
            }
            DialogPane.this.requestLayout();
        }
    };
    private double oldHeight = -1.0;

    static Label createContentLabel(String string) {
        Label label = new Label(string);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setMaxHeight(Double.MAX_VALUE);
        label.getStyleClass().add("content");
        label.setWrapText(true);
        label.setPrefWidth(360.0);
        return label;
    }

    public DialogPane() {
        this.getStyleClass().add("dialog-pane");
        this.headerTextPanel = new GridPane();
        this.getChildren().add(this.headerTextPanel);
        this.graphicContainer = new StackPane();
        this.contentLabel = DialogPane.createContentLabel("");
        this.getChildren().add(this.contentLabel);
        this.buttonBar = this.createButtonBar();
        if (this.buttonBar != null) {
            this.getChildren().add(this.buttonBar);
        }
        this.buttons.addListener(change -> {
            while (change.next()) {
                if (change.wasRemoved()) {
                    for (ButtonType buttonType : change.getRemoved()) {
                        this.buttonNodes.remove(buttonType);
                    }
                }
                if (!change.wasAdded()) continue;
                for (ButtonType buttonType : change.getAddedSubList()) {
                    if (this.buttonNodes.containsKey(buttonType)) continue;
                    this.buttonNodes.put(buttonType, this.createButton(buttonType));
                }
            }
        });
    }

    public final ObjectProperty<Node> graphicProperty() {
        return this.graphicProperty;
    }

    public final Node getGraphic() {
        return (Node)this.graphicProperty.get();
    }

    public final void setGraphic(Node node) {
        this.graphicProperty.set(node);
    }

    private StyleableStringProperty imageUrlProperty() {
        if (this.imageUrl == null) {
            this.imageUrl = new StyleableStringProperty(){
                StyleOrigin origin = StyleOrigin.USER;

                @Override
                public void applyStyle(StyleOrigin styleOrigin, String string) {
                    this.origin = styleOrigin;
                    if (DialogPane.this.graphicProperty == null || !DialogPane.this.graphicProperty.isBound()) {
                        super.applyStyle(styleOrigin, string);
                    }
                    this.origin = StyleOrigin.USER;
                }

                @Override
                protected void invalidated() {
                    String string = super.get();
                    if (string == null) {
                        ((StyleableProperty)((Object)DialogPane.this.graphicProperty())).applyStyle(this.origin, null);
                    } else {
                        String string2;
                        Object object;
                        Image image;
                        Node node = DialogPane.this.getGraphic();
                        if (node instanceof ImageView && (image = ((ImageView)(object = (ImageView)node)).getImage()) != null && string.equals(string2 = image.impl_getUrl())) {
                            return;
                        }
                        object = StyleManager.getInstance().getCachedImage(string);
                        if (object != null) {
                            ((StyleableProperty)((Object)DialogPane.this.graphicProperty())).applyStyle(this.origin, new ImageView((Image)object));
                        }
                    }
                }

                @Override
                public String get() {
                    Image image;
                    Node node = DialogPane.this.getGraphic();
                    if (node instanceof ImageView && (image = ((ImageView)node).getImage()) != null) {
                        return image.impl_getUrl();
                    }
                    return null;
                }

                @Override
                public StyleOrigin getStyleOrigin() {
                    return DialogPane.this.graphicProperty != null ? ((StyleableProperty)((Object)DialogPane.this.graphicProperty)).getStyleOrigin() : null;
                }

                @Override
                public Object getBean() {
                    return DialogPane.this;
                }

                @Override
                public String getName() {
                    return "imageUrl";
                }

                @Override
                public CssMetaData<DialogPane, String> getCssMetaData() {
                    return StyleableProperties.GRAPHIC;
                }
            };
        }
        return this.imageUrl;
    }

    public final Node getHeader() {
        return (Node)this.header.get();
    }

    public final void setHeader(Node node) {
        this.header.setValue(node);
    }

    public final ObjectProperty<Node> headerProperty() {
        return this.header;
    }

    public final void setHeaderText(String string) {
        this.headerText.set(string);
    }

    public final String getHeaderText() {
        return (String)this.headerText.get();
    }

    public final StringProperty headerTextProperty() {
        return this.headerText;
    }

    public final Node getContent() {
        return (Node)this.content.get();
    }

    public final void setContent(Node node) {
        this.content.setValue(node);
    }

    public final ObjectProperty<Node> contentProperty() {
        return this.content;
    }

    public final void setContentText(String string) {
        this.contentText.set(string);
    }

    public final String getContentText() {
        return (String)this.contentText.get();
    }

    public final StringProperty contentTextProperty() {
        return this.contentText;
    }

    public final ObjectProperty<Node> expandableContentProperty() {
        return this.expandableContentProperty;
    }

    public final Node getExpandableContent() {
        return (Node)this.expandableContentProperty.get();
    }

    public final void setExpandableContent(Node node) {
        this.expandableContentProperty.set(node);
    }

    public final BooleanProperty expandedProperty() {
        return this.expandedProperty;
    }

    public final boolean isExpanded() {
        return this.expandedProperty().get();
    }

    public final void setExpanded(boolean bl) {
        this.expandedProperty().set(bl);
    }

    public final ObservableList<ButtonType> getButtonTypes() {
        return this.buttons;
    }

    public final Node lookupButton(ButtonType buttonType) {
        return this.buttonNodes.get(buttonType);
    }

    protected Node createButtonBar() {
        ButtonBar buttonBar = new ButtonBar();
        buttonBar.setMaxWidth(Double.MAX_VALUE);
        this.updateButtons(buttonBar);
        this.getButtonTypes().addListener(change -> this.updateButtons(buttonBar));
        this.expandableContentProperty().addListener(observable -> this.updateButtons(buttonBar));
        return buttonBar;
    }

    protected Node createButton(ButtonType buttonType) {
        Button button = new Button(buttonType.getText());
        ButtonBar.ButtonData buttonData = buttonType.getButtonData();
        ButtonBar.setButtonData(button, buttonData);
        button.setDefaultButton(buttonType != null && buttonData.isDefaultButton());
        button.setCancelButton(buttonType != null && buttonData.isCancelButton());
        button.addEventHandler(ActionEvent.ACTION, actionEvent -> {
            if (actionEvent.isConsumed()) {
                return;
            }
            if (this.dialog != null) {
                this.dialog.impl_setResultAndClose(buttonType, true);
            }
        });
        return button;
    }

    protected Node createDetailsButton() {
        Hyperlink hyperlink = new Hyperlink();
        String string = ControlResources.getString("Dialog.detail.button.more");
        String string2 = ControlResources.getString("Dialog.detail.button.less");
        InvalidationListener invalidationListener = observable -> {
            boolean bl = this.isExpanded();
            hyperlink.setText(bl ? string2 : string);
            hyperlink.getStyleClass().setAll("details-button", bl ? "less" : "more");
        };
        invalidationListener.invalidated(null);
        this.expandedProperty().addListener(invalidationListener);
        hyperlink.setOnAction(actionEvent -> this.setExpanded(!this.isExpanded()));
        return hyperlink;
    }

    @Override
    protected void layoutChildren() {
        double d2;
        double d3;
        double d4;
        double d5;
        double d6;
        boolean bl = this.hasHeader();
        double d7 = Math.max(this.minWidth(-1.0), this.getWidth());
        double d8 = this.minHeight(d7);
        double d9 = this.prefHeight(d7);
        double d10 = this.maxHeight(d7);
        double d11 = this.getHeight();
        double d12 = d6 = this.dialog == null ? 0.0 : this.dialog.dialog.getSceneHeight();
        if (d9 > d11 && d9 > d8 && (d9 <= d6 || d6 == 0.0)) {
            d5 = d9;
            this.resize(d7, d5);
        } else {
            boolean bl2;
            boolean bl3 = bl2 = d11 > this.oldHeight;
            if (bl2) {
                double d13 = d11 < d9 ? Math.min(d9, d11) : Math.max(d9, d6);
                d5 = Utils.boundedSize(d13, d8, d10);
            } else {
                d5 = Utils.boundedSize(Math.min(d11, d6), d8, d10);
            }
            this.resize(d7, d5);
        }
        this.oldHeight = d5 -= this.snappedTopInset() + this.snappedBottomInset();
        double d14 = this.snappedLeftInset();
        double d15 = this.snappedTopInset();
        double d16 = this.snappedRightInset();
        double d17 = this.snappedBottomInset();
        Node node = this.getActualHeader();
        Node node2 = this.getActualContent();
        Node node3 = this.getActualGraphic();
        Node node4 = this.getExpandableContent();
        double d18 = bl || node3 == null ? 0.0 : node3.prefWidth(-1.0);
        double d19 = bl ? node.prefHeight(d7) : 0.0;
        double d20 = this.buttonBar == null ? 0.0 : this.buttonBar.prefHeight(d7);
        double d21 = bl || node3 == null ? 0.0 : node3.prefHeight(-1.0);
        double d22 = d7 - d18 - d14 - d16;
        if (this.isExpanded()) {
            d4 = this.isExpanded() ? node2.prefHeight(d22) : 0.0;
            d3 = bl ? d4 : Math.max(d21, d4);
            d2 = d5 - (d19 + d3 + d20);
        } else {
            d2 = this.isExpanded() ? node4.prefHeight(d7) : 0.0;
            d4 = d5 - (d19 + d2 + d20);
            d3 = bl ? d4 : Math.max(d21, d4);
        }
        double d23 = d14;
        double d24 = d15;
        if (!bl) {
            if (node3 != null) {
                node3.resizeRelocate(d23, d24, d18, d21);
                d23 += d18;
            }
        } else {
            node.resizeRelocate(d23, d24, d7 - (d14 + d16), d19);
            d24 += d19;
        }
        node2.resizeRelocate(d23, d24, d22, d4);
        d24 += bl ? d4 : d3;
        if (node4 != null) {
            node4.resizeRelocate(d14, d24, d7 - d16, d2);
            d24 += d2;
        }
        if (this.buttonBar != null) {
            this.buttonBar.resizeRelocate(d14, d24, d7 - (d14 + d16), d20);
        }
    }

    @Override
    protected double computeMinWidth(double d2) {
        double d3 = this.hasHeader() ? this.getActualHeader().minWidth(d2) + 10.0 : 0.0;
        double d4 = this.getActualContent().minWidth(d2);
        double d5 = this.buttonBar == null ? 0.0 : this.buttonBar.minWidth(d2);
        double d6 = this.getActualGraphic().minWidth(d2);
        double d7 = 0.0;
        Node node = this.getExpandableContent();
        if (this.isExpanded() && node != null) {
            d7 = node.minWidth(d2);
        }
        double d8 = this.snappedLeftInset() + (this.hasHeader() ? 0.0 : d6) + Math.max(Math.max(d3, d7), Math.max(d4, d5)) + this.snappedRightInset();
        return this.snapSize(d8);
    }

    @Override
    protected double computeMinHeight(double d2) {
        boolean bl = this.hasHeader();
        double d3 = bl ? this.getActualHeader().minHeight(d2) : 0.0;
        double d4 = this.buttonBar == null ? 0.0 : this.buttonBar.minHeight(d2);
        Node node = this.getActualGraphic();
        double d5 = bl ? 0.0 : node.minWidth(-1.0);
        double d6 = bl ? 0.0 : node.minHeight(d2);
        Node node2 = this.getActualContent();
        double d7 = d2 == -1.0 ? -1.0 : (bl ? d2 : d2 - d5);
        double d8 = node2.minHeight(d7);
        double d9 = 0.0;
        Node node3 = this.getExpandableContent();
        if (this.isExpanded() && node3 != null) {
            d9 = node3.minHeight(d2);
        }
        double d10 = this.snappedTopInset() + d3 + Math.max(d6, d8) + d9 + d4 + this.snappedBottomInset();
        return this.snapSize(d10);
    }

    @Override
    protected double computePrefWidth(double d2) {
        double d3 = this.hasHeader() ? this.getActualHeader().prefWidth(d2) + 10.0 : 0.0;
        double d4 = this.getActualContent().prefWidth(d2);
        double d5 = this.buttonBar == null ? 0.0 : this.buttonBar.prefWidth(d2);
        double d6 = this.getActualGraphic().prefWidth(d2);
        double d7 = 0.0;
        Node node = this.getExpandableContent();
        if (this.isExpanded() && node != null) {
            d7 = node.prefWidth(d2);
        }
        double d8 = this.snappedLeftInset() + (this.hasHeader() ? 0.0 : d6) + Math.max(Math.max(d3, d7), Math.max(d4, d5)) + this.snappedRightInset();
        return this.snapSize(d8);
    }

    @Override
    protected double computePrefHeight(double d2) {
        boolean bl = this.hasHeader();
        double d3 = bl ? this.getActualHeader().prefHeight(d2) : 0.0;
        double d4 = this.buttonBar == null ? 0.0 : this.buttonBar.prefHeight(d2);
        Node node = this.getActualGraphic();
        double d5 = bl ? 0.0 : node.prefWidth(-1.0);
        double d6 = bl ? 0.0 : node.prefHeight(d2);
        Node node2 = this.getActualContent();
        double d7 = d2 == -1.0 ? -1.0 : (bl ? d2 : d2 - d5);
        double d8 = node2.prefHeight(d7);
        double d9 = 0.0;
        Node node3 = this.getExpandableContent();
        if (this.isExpanded() && node3 != null) {
            d9 = node3.prefHeight(d2);
        }
        double d10 = this.snappedTopInset() + d3 + Math.max(d6, d8) + d9 + d4 + this.snappedBottomInset();
        return this.snapSize(d10);
    }

    private void updateButtons(ButtonBar buttonBar) {
        buttonBar.getButtons().clear();
        if (this.hasExpandableContent()) {
            if (this.detailsButton == null) {
                this.detailsButton = this.createDetailsButton();
            }
            ButtonBar.setButtonData(this.detailsButton, ButtonBar.ButtonData.HELP_2);
            buttonBar.getButtons().add(this.detailsButton);
            ButtonBar.setButtonUniformSize(this.detailsButton, false);
        }
        boolean bl = false;
        for (ButtonType buttonType : this.getButtonTypes()) {
            Node node = this.buttonNodes.computeIfAbsent(buttonType, buttonType2 -> this.createButton(buttonType));
            if (node instanceof Button) {
                ButtonBar.ButtonData buttonData = buttonType.getButtonData();
                ((Button)node).setDefaultButton(!bl && buttonData != null && buttonData.isDefaultButton());
                ((Button)node).setCancelButton(buttonData != null && buttonData.isCancelButton());
                bl |= buttonData != null && buttonData.isDefaultButton();
            }
            buttonBar.getButtons().add(node);
        }
    }

    private Node getActualContent() {
        Node node = this.getContent();
        return node == null ? this.contentLabel : node;
    }

    private Node getActualHeader() {
        Node node = this.getHeader();
        return node == null ? this.headerTextPanel : node;
    }

    private Node getActualGraphic() {
        return this.headerTextPanel;
    }

    private void updateHeaderArea() {
        Node node = this.getHeader();
        if (node != null) {
            if (!this.getChildren().contains(node)) {
                this.getChildren().add(node);
            }
            this.headerTextPanel.setVisible(false);
            this.headerTextPanel.setManaged(false);
        } else {
            Node node2;
            String string = this.getHeaderText();
            this.headerTextPanel.getChildren().clear();
            this.headerTextPanel.getStyleClass().clear();
            this.headerTextPanel.setMaxWidth(Double.MAX_VALUE);
            if (string != null && !string.isEmpty()) {
                this.headerTextPanel.getStyleClass().add("header-panel");
            }
            Label label = new Label(string);
            label.setWrapText(true);
            label.setAlignment(Pos.CENTER_LEFT);
            label.setMaxWidth(Double.MAX_VALUE);
            label.setMaxHeight(Double.MAX_VALUE);
            this.headerTextPanel.add(label, 0, 0);
            this.graphicContainer.getChildren().clear();
            if (!this.graphicContainer.getStyleClass().contains("graphic-container")) {
                this.graphicContainer.getStyleClass().add("graphic-container");
            }
            if ((node2 = this.getGraphic()) != null) {
                this.graphicContainer.getChildren().add(node2);
            }
            this.headerTextPanel.add(this.graphicContainer, 1, 0);
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setFillWidth(true);
            columnConstraints.setHgrow(Priority.ALWAYS);
            ColumnConstraints columnConstraints2 = new ColumnConstraints();
            columnConstraints2.setFillWidth(false);
            columnConstraints2.setHgrow(Priority.NEVER);
            this.headerTextPanel.getColumnConstraints().setAll(columnConstraints, columnConstraints2);
            this.headerTextPanel.setVisible(true);
            this.headerTextPanel.setManaged(true);
        }
    }

    private void updateContentArea() {
        Node node = this.getContent();
        if (node != null) {
            if (!this.getChildren().contains(node)) {
                this.getChildren().add(node);
            }
            if (!node.getStyleClass().contains("content")) {
                node.getStyleClass().add("content");
            }
            this.contentLabel.setVisible(false);
            this.contentLabel.setManaged(false);
        } else {
            String string = this.getContentText();
            boolean bl = string != null && !string.isEmpty();
            this.contentLabel.setText(bl ? string : "");
            this.contentLabel.setVisible(bl);
            this.contentLabel.setManaged(bl);
        }
    }

    boolean hasHeader() {
        return this.getHeader() != null || this.isTextHeader();
    }

    private boolean isTextHeader() {
        String string = this.getHeaderText();
        return string != null && !string.isEmpty();
    }

    boolean hasExpandableContent() {
        return this.getExpandableContent() != null;
    }

    void setDialog(Dialog<?> dialog) {
        this.dialog = dialog;
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return DialogPane.getClassCssMetaData();
    }

    private static class StyleableProperties {
        private static final CssMetaData<DialogPane, String> GRAPHIC = new CssMetaData<DialogPane, String>("-fx-graphic", StringConverter.getInstance()){

            @Override
            public boolean isSettable(DialogPane dialogPane) {
                return dialogPane.graphicProperty == null || !dialogPane.graphicProperty.isBound();
            }

            @Override
            public StyleableProperty<String> getStyleableProperty(DialogPane dialogPane) {
                return dialogPane.imageUrlProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            ArrayList arrayList = new ArrayList(Region.getClassCssMetaData());
            Collections.addAll(arrayList, GRAPHIC);
            STYLEABLES = Collections.unmodifiableList(arrayList);
        }
    }
}

