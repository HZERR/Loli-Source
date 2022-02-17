/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.ComboBoxBaseBehavior;
import com.sun.javafx.scene.control.skin.ComboBoxBaseSkin;
import com.sun.javafx.scene.input.ExtendedInputMethodRequests;
import com.sun.javafx.scene.traversal.Algorithm;
import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.scene.traversal.ParentTraversalEngine;
import com.sun.javafx.scene.traversal.TraversalContext;
import com.sun.javafx.util.Utils;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ObservableValue;
import javafx.css.Styleable;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Node;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.PopupControl;
import javafx.scene.control.Skin;
import javafx.scene.control.Skinnable;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;

public abstract class ComboBoxPopupControl<T>
extends ComboBoxBaseSkin<T> {
    protected PopupControl popup;
    public static final String COMBO_BOX_STYLE_CLASS = "combo-box-popup";
    private boolean popupNeedsReconfiguring = true;
    private final ComboBoxBase<T> comboBoxBase;
    private TextField textField;
    private EventHandler<MouseEvent> textFieldMouseEventHandler = mouseEvent -> {
        ComboBoxBase comboBoxBase = (ComboBoxBase)this.getSkinnable();
        if (!mouseEvent.getTarget().equals(comboBoxBase)) {
            comboBoxBase.fireEvent(mouseEvent.copyFor(comboBoxBase, comboBoxBase));
            mouseEvent.consume();
        }
    };
    private EventHandler<DragEvent> textFieldDragEventHandler = dragEvent -> {
        ComboBoxBase comboBoxBase = (ComboBoxBase)this.getSkinnable();
        if (!dragEvent.getTarget().equals(comboBoxBase)) {
            comboBoxBase.fireEvent(dragEvent.copyFor(comboBoxBase, comboBoxBase));
            dragEvent.consume();
        }
    };
    private String initialTextFieldValue = null;

    public ComboBoxPopupControl(ComboBoxBase<T> comboBoxBase, ComboBoxBaseBehavior<T> comboBoxBaseBehavior) {
        super(comboBoxBase, comboBoxBaseBehavior);
        this.comboBoxBase = comboBoxBase;
        TextField textField = this.textField = this.getEditor() != null ? this.getEditableInputNode() : null;
        if (this.textField != null) {
            this.getChildren().add(this.textField);
        }
        comboBoxBase.focusedProperty().addListener((observableValue, bl, bl2) -> {
            if (this.getEditor() != null) {
                ((FakeFocusTextField)this.textField).setFakeFocus((boolean)bl2);
                if (!bl2.booleanValue()) {
                    this.setTextFromTextFieldIntoComboBoxValue();
                }
            }
        });
        comboBoxBase.addEventFilter(KeyEvent.ANY, keyEvent -> {
            if (this.textField == null || this.getEditor() == null) {
                this.handleKeyEvent((KeyEvent)keyEvent, false);
            } else {
                if (keyEvent.getTarget().equals(this.textField)) {
                    return;
                }
                switch (keyEvent.getCode()) {
                    case ESCAPE: 
                    case F10: {
                        break;
                    }
                    case ENTER: {
                        this.handleKeyEvent((KeyEvent)keyEvent, true);
                        break;
                    }
                    default: {
                        this.textField.fireEvent(keyEvent.copyFor(this.textField, this.textField));
                        keyEvent.consume();
                    }
                }
            }
        });
        if (comboBoxBase.getOnInputMethodTextChanged() == null) {
            comboBoxBase.setOnInputMethodTextChanged(inputMethodEvent -> {
                if (this.textField != null && this.getEditor() != null && comboBoxBase.getScene().getFocusOwner() == comboBoxBase && this.textField.getOnInputMethodTextChanged() != null) {
                    this.textField.getOnInputMethodTextChanged().handle((InputMethodEvent)inputMethodEvent);
                }
            });
        }
        comboBoxBase.setImpl_traversalEngine(new ParentTraversalEngine(comboBoxBase, new Algorithm(){

            @Override
            public Node select(Node node, Direction direction, TraversalContext traversalContext) {
                return null;
            }

            @Override
            public Node selectFirst(TraversalContext traversalContext) {
                return null;
            }

            @Override
            public Node selectLast(TraversalContext traversalContext) {
                return null;
            }
        }));
        this.updateEditable();
    }

    protected abstract Node getPopupContent();

    protected PopupControl getPopup() {
        if (this.popup == null) {
            this.createPopup();
        }
        return this.popup;
    }

    @Override
    public void show() {
        if (this.getSkinnable() == null) {
            throw new IllegalStateException("ComboBox is null");
        }
        Node node = this.getPopupContent();
        if (node == null) {
            throw new IllegalStateException("Popup node is null");
        }
        if (this.getPopup().isShowing()) {
            return;
        }
        this.positionAndShowPopup();
    }

    @Override
    public void hide() {
        if (this.popup != null && this.popup.isShowing()) {
            this.popup.hide();
        }
    }

    private Point2D getPrefPopupPosition() {
        return Utils.pointRelativeTo((Node)((Object)this.getSkinnable()), this.getPopupContent(), HPos.CENTER, VPos.BOTTOM, 0.0, 0.0, true);
    }

    private void positionAndShowPopup() {
        PopupControl popupControl = this.getPopup();
        popupControl.getScene().setNodeOrientation(((ComboBoxBase)this.getSkinnable()).getEffectiveNodeOrientation());
        Node node = this.getPopupContent();
        this.sizePopup();
        Point2D point2D = this.getPrefPopupPosition();
        this.popupNeedsReconfiguring = true;
        this.reconfigurePopup();
        ComboBoxBase comboBoxBase = (ComboBoxBase)this.getSkinnable();
        popupControl.show(comboBoxBase.getScene().getWindow(), this.snapPosition(point2D.getX()), this.snapPosition(point2D.getY()));
        node.requestFocus();
        this.sizePopup();
    }

    private void sizePopup() {
        Node node = this.getPopupContent();
        if (node instanceof Region) {
            Region region = (Region)node;
            double d2 = this.snapSize(region.prefHeight(0.0));
            double d3 = this.snapSize(region.minHeight(0.0));
            double d4 = this.snapSize(region.maxHeight(0.0));
            double d5 = this.snapSize(Math.min(Math.max(d2, d3), Math.max(d3, d4)));
            double d6 = this.snapSize(region.prefWidth(d5));
            double d7 = this.snapSize(region.minWidth(d5));
            double d8 = this.snapSize(region.maxWidth(d5));
            double d9 = this.snapSize(Math.min(Math.max(d6, d7), Math.max(d7, d8)));
            node.resize(d9, d5);
        } else {
            node.autosize();
        }
    }

    private void createPopup() {
        this.popup = new PopupControl(){
            {
                this.setSkin(new Skin<Skinnable>(){

                    @Override
                    public Skinnable getSkinnable() {
                        return ComboBoxPopupControl.this.getSkinnable();
                    }

                    @Override
                    public Node getNode() {
                        return ComboBoxPopupControl.this.getPopupContent();
                    }

                    @Override
                    public void dispose() {
                    }
                });
            }

            @Override
            public Styleable getStyleableParent() {
                return ComboBoxPopupControl.this.getSkinnable();
            }
        };
        this.popup.getStyleClass().add("combo-box-popup");
        this.popup.setConsumeAutoHidingEvents(false);
        this.popup.setAutoHide(true);
        this.popup.setAutoFix(true);
        this.popup.setHideOnEscape(true);
        this.popup.setOnAutoHide(event -> ((ComboBoxBaseBehavior)this.getBehavior()).onAutoHide());
        this.popup.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> ((ComboBoxBaseBehavior)this.getBehavior()).onAutoHide());
        this.popup.addEventHandler(WindowEvent.WINDOW_HIDDEN, windowEvent -> ((ComboBoxBase)this.getSkinnable()).notifyAccessibleAttributeChanged(AccessibleAttribute.FOCUS_NODE));
        InvalidationListener invalidationListener = observable -> {
            this.popupNeedsReconfiguring = true;
            this.reconfigurePopup();
        };
        ((ComboBoxBase)this.getSkinnable()).layoutXProperty().addListener(invalidationListener);
        ((ComboBoxBase)this.getSkinnable()).layoutYProperty().addListener(invalidationListener);
        ((ComboBoxBase)this.getSkinnable()).widthProperty().addListener(invalidationListener);
        ((ComboBoxBase)this.getSkinnable()).heightProperty().addListener(invalidationListener);
        ((ComboBoxBase)this.getSkinnable()).sceneProperty().addListener(observable -> {
            if (((ObservableValue)observable).getValue() == null) {
                this.hide();
            }
        });
    }

    void reconfigurePopup() {
        double d2;
        if (this.popup == null) {
            return;
        }
        boolean bl = this.popup.isShowing();
        if (!bl) {
            return;
        }
        if (!this.popupNeedsReconfiguring) {
            return;
        }
        this.popupNeedsReconfiguring = false;
        Point2D point2D = this.getPrefPopupPosition();
        Node node = this.getPopupContent();
        double d3 = node.prefWidth(-1.0);
        double d4 = node.prefHeight(-1.0);
        if (point2D.getX() > -1.0) {
            this.popup.setAnchorX(point2D.getX());
        }
        if (point2D.getY() > -1.0) {
            this.popup.setAnchorY(point2D.getY());
        }
        if (d3 > -1.0) {
            this.popup.setMinWidth(d3);
        }
        if (d4 > -1.0) {
            this.popup.setMinHeight(d4);
        }
        Bounds bounds = node.getLayoutBounds();
        double d5 = bounds.getWidth();
        double d6 = bounds.getHeight();
        double d7 = d5 < d3 ? d3 : d5;
        double d8 = d2 = d6 < d4 ? d4 : d6;
        if (d7 != d5 || d2 != d6) {
            node.resize(d7, d2);
            if (node instanceof Region) {
                ((Region)node).setMinSize(d7, d2);
                ((Region)node).setPrefSize(d7, d2);
            }
        }
    }

    protected abstract TextField getEditor();

    protected abstract StringConverter<T> getConverter();

    protected TextField getEditableInputNode() {
        if (this.textField == null && this.getEditor() != null) {
            this.textField = this.getEditor();
            this.textField.setFocusTraversable(false);
            this.textField.promptTextProperty().bind(this.comboBoxBase.promptTextProperty());
            this.textField.tooltipProperty().bind(this.comboBoxBase.tooltipProperty());
            this.initialTextFieldValue = this.textField.getText();
        }
        return this.textField;
    }

    protected void setTextFromTextFieldIntoComboBoxValue() {
        StringConverter<T> stringConverter;
        if (this.getEditor() != null && (stringConverter = this.getConverter()) != null) {
            T t2;
            T t3 = t2 = this.comboBoxBase.getValue();
            String string = this.textField.getText();
            if (t2 == null && (string == null || string.isEmpty())) {
                t3 = null;
            } else {
                try {
                    t3 = stringConverter.fromString(string);
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
            if (!(t3 == null && t2 == null || t3 != null && t3.equals(t2))) {
                this.comboBoxBase.setValue(t3);
            }
            this.updateDisplayNode();
        }
    }

    protected void updateDisplayNode() {
        if (this.textField != null && this.getEditor() != null) {
            T t2 = this.comboBoxBase.getValue();
            StringConverter<T> stringConverter = this.getConverter();
            if (this.initialTextFieldValue != null && !this.initialTextFieldValue.isEmpty()) {
                this.textField.setText(this.initialTextFieldValue);
                this.initialTextFieldValue = null;
            } else {
                String string = stringConverter.toString(t2);
                if (t2 == null || string == null) {
                    this.textField.setText("");
                } else if (!string.equals(this.textField.getText())) {
                    this.textField.setText(string);
                }
            }
        }
    }

    private void handleKeyEvent(KeyEvent keyEvent, boolean bl) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            this.setTextFromTextFieldIntoComboBoxValue();
            if (bl && this.comboBoxBase.getOnAction() != null) {
                keyEvent.consume();
            } else {
                this.forwardToParent(keyEvent);
            }
        } else if (keyEvent.getCode() == KeyCode.F4) {
            if (keyEvent.getEventType() == KeyEvent.KEY_RELEASED) {
                if (this.comboBoxBase.isShowing()) {
                    this.comboBoxBase.hide();
                } else {
                    this.comboBoxBase.show();
                }
            }
            keyEvent.consume();
        }
    }

    private void forwardToParent(KeyEvent keyEvent) {
        if (this.comboBoxBase.getParent() != null) {
            this.comboBoxBase.getParent().fireEvent(keyEvent);
        }
    }

    protected void updateEditable() {
        final TextField textField = this.getEditor();
        if (this.getEditor() == null) {
            if (this.textField != null) {
                this.textField.removeEventFilter(MouseEvent.DRAG_DETECTED, this.textFieldMouseEventHandler);
                this.textField.removeEventFilter(DragEvent.ANY, this.textFieldDragEventHandler);
                this.comboBoxBase.setInputMethodRequests(null);
            }
        } else if (textField != null) {
            textField.addEventFilter(MouseEvent.DRAG_DETECTED, this.textFieldMouseEventHandler);
            textField.addEventFilter(DragEvent.ANY, this.textFieldDragEventHandler);
            this.comboBoxBase.setInputMethodRequests(new ExtendedInputMethodRequests(){

                @Override
                public Point2D getTextLocation(int n2) {
                    return textField.getInputMethodRequests().getTextLocation(n2);
                }

                @Override
                public int getLocationOffset(int n2, int n3) {
                    return textField.getInputMethodRequests().getLocationOffset(n2, n3);
                }

                @Override
                public void cancelLatestCommittedText() {
                    textField.getInputMethodRequests().cancelLatestCommittedText();
                }

                @Override
                public String getSelectedText() {
                    return textField.getInputMethodRequests().getSelectedText();
                }

                @Override
                public int getInsertPositionOffset() {
                    return ((ExtendedInputMethodRequests)textField.getInputMethodRequests()).getInsertPositionOffset();
                }

                @Override
                public String getCommittedText(int n2, int n3) {
                    return ((ExtendedInputMethodRequests)textField.getInputMethodRequests()).getCommittedText(n2, n3);
                }

                @Override
                public int getCommittedTextLength() {
                    return ((ExtendedInputMethodRequests)textField.getInputMethodRequests()).getCommittedTextLength();
                }
            });
        }
        this.textField = textField;
    }

    public static final class FakeFocusTextField
    extends TextField {
        @Override
        public void requestFocus() {
            if (this.getParent() != null) {
                this.getParent().requestFocus();
            }
        }

        public void setFakeFocus(boolean bl) {
            this.setFocused(bl);
        }

        @Override
        public Object queryAccessibleAttribute(AccessibleAttribute accessibleAttribute, Object ... arrobject) {
            switch (accessibleAttribute) {
                case FOCUS_ITEM: {
                    return this.getParent();
                }
            }
            return super.queryAccessibleAttribute(accessibleAttribute, arrobject);
        }
    }
}

