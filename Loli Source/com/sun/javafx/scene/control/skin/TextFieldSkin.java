/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.PasswordFieldBehavior;
import com.sun.javafx.scene.control.behavior.TextFieldBehavior;
import com.sun.javafx.scene.control.skin.TextInputControlSkin;
import com.sun.javafx.scene.control.skin.Utils;
import com.sun.javafx.scene.text.HitInfo;
import com.sun.javafx.tk.FontMetrics;
import java.util.Collection;
import java.util.List;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableDoubleValue;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.IndexRange;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class TextFieldSkin
extends TextInputControlSkin<TextField, TextFieldBehavior> {
    private Pane textGroup = new Pane();
    private Group handleGroup;
    private Rectangle clip = new Rectangle();
    private Text textNode = new Text();
    private Text promptNode;
    private Path selectionHighlightPath = new Path();
    private Path characterBoundingPath = new Path();
    private ObservableBooleanValue usePromptText;
    private DoubleProperty textTranslateX = new SimpleDoubleProperty(this, "textTranslateX");
    private double caretWidth;
    protected ObservableDoubleValue textRight;
    private double pressX;
    private double pressY;
    public static final char BULLET = '\u25cf';

    protected int translateCaretPosition(int n2) {
        return n2;
    }

    protected Point2D translateCaretPosition(Point2D point2D) {
        return point2D;
    }

    public TextFieldSkin(TextField textField) {
        this(textField, textField instanceof PasswordField ? new PasswordFieldBehavior((PasswordField)textField) : new TextFieldBehavior(textField));
    }

    public TextFieldSkin(final TextField textField, TextFieldBehavior textFieldBehavior) {
        super(textField, textFieldBehavior);
        textFieldBehavior.setTextFieldSkin(this);
        textField.caretPositionProperty().addListener((observableValue, number, number2) -> {
            if (textField.getWidth() > 0.0) {
                this.updateTextNodeCaretPos(textField.getCaretPosition());
                if (!this.isForwardBias()) {
                    this.setForwardBias(true);
                }
                this.updateCaretOff();
            }
        });
        this.forwardBiasProperty().addListener(observable -> {
            if (textField.getWidth() > 0.0) {
                this.updateTextNodeCaretPos(textField.getCaretPosition());
                this.updateCaretOff();
            }
        });
        this.textRight = new DoubleBinding(){
            {
                this.bind(TextFieldSkin.this.textGroup.widthProperty());
            }

            @Override
            protected double computeValue() {
                return TextFieldSkin.this.textGroup.getWidth();
            }
        };
        this.clip.setSmooth(false);
        this.clip.setX(0.0);
        this.clip.widthProperty().bind(this.textGroup.widthProperty());
        this.clip.heightProperty().bind(this.textGroup.heightProperty());
        this.textGroup.setClip(this.clip);
        this.textGroup.getChildren().addAll(this.selectionHighlightPath, this.textNode, new Group(this.caretPath));
        this.getChildren().add(this.textGroup);
        if (SHOW_HANDLES) {
            this.handleGroup = new Group();
            this.handleGroup.setManaged(false);
            this.handleGroup.getChildren().addAll(this.caretHandle, this.selectionHandle1, this.selectionHandle2);
            this.getChildren().add(this.handleGroup);
        }
        this.textNode.setManaged(false);
        this.textNode.getStyleClass().add("text");
        this.textNode.fontProperty().bind(textField.fontProperty());
        this.textNode.layoutXProperty().bind(this.textTranslateX);
        this.textNode.textProperty().bind(new StringBinding(){
            {
                this.bind(textField.textProperty());
            }

            @Override
            protected String computeValue() {
                return TextFieldSkin.this.maskText(textField.textProperty().getValueSafe());
            }
        });
        this.textNode.fillProperty().bind(this.textFill);
        this.textNode.impl_selectionFillProperty().bind((ObservableValue<Paint>)new ObjectBinding<Paint>(){
            {
                this.bind(TextFieldSkin.this.highlightTextFill, TextFieldSkin.this.textFill, textField.focusedProperty());
            }

            @Override
            protected Paint computeValue() {
                return textField.isFocused() ? (Paint)TextFieldSkin.this.highlightTextFill.get() : (Paint)TextFieldSkin.this.textFill.get();
            }
        });
        this.updateTextNodeCaretPos(textField.getCaretPosition());
        textField.selectionProperty().addListener(observable -> this.updateSelection());
        this.selectionHighlightPath.setManaged(false);
        this.selectionHighlightPath.setStroke(null);
        this.selectionHighlightPath.layoutXProperty().bind(this.textTranslateX);
        this.selectionHighlightPath.visibleProperty().bind(textField.anchorProperty().isNotEqualTo(textField.caretPositionProperty()).and(textField.focusedProperty()));
        this.selectionHighlightPath.fillProperty().bind(this.highlightFill);
        this.textNode.impl_selectionShapeProperty().addListener(observable -> this.updateSelection());
        this.caretPath.setManaged(false);
        this.caretPath.setStrokeWidth(1.0);
        this.caretPath.fillProperty().bind(this.textFill);
        this.caretPath.strokeProperty().bind(this.textFill);
        this.caretPath.opacityProperty().bind(new DoubleBinding(){
            {
                this.bind(TextFieldSkin.this.caretVisible);
            }

            @Override
            protected double computeValue() {
                return TextFieldSkin.this.caretVisible.get() ? 1.0 : 0.0;
            }
        });
        this.caretPath.layoutXProperty().bind(this.textTranslateX);
        this.textNode.impl_caretShapeProperty().addListener(observable -> {
            this.caretPath.getElements().setAll((Object[])this.textNode.impl_caretShapeProperty().get());
            if (this.caretPath.getElements().size() == 0) {
                this.updateTextNodeCaretPos(textField.getCaretPosition());
            } else if (this.caretPath.getElements().size() != 4) {
                this.caretWidth = Math.round(this.caretPath.getLayoutBounds().getWidth());
            }
        });
        textField.fontProperty().addListener(observable -> {
            textField.requestLayout();
            ((TextField)this.getSkinnable()).requestLayout();
        });
        this.registerChangeListener(textField.prefColumnCountProperty(), "prefColumnCount");
        if (textField.isFocused()) {
            this.setCaretAnimating(true);
        }
        textField.alignmentProperty().addListener(observable -> {
            if (textField.getWidth() > 0.0) {
                this.updateTextPos();
                this.updateCaretOff();
                textField.requestLayout();
            }
        });
        this.usePromptText = new BooleanBinding(){
            {
                this.bind(textField.textProperty(), textField.promptTextProperty(), TextFieldSkin.this.promptTextFill);
            }

            @Override
            protected boolean computeValue() {
                String string = textField.getText();
                String string2 = textField.getPromptText();
                return (string == null || string.isEmpty()) && string2 != null && !string2.isEmpty() && !((Paint)TextFieldSkin.this.promptTextFill.get()).equals(Color.TRANSPARENT);
            }
        };
        this.promptTextFill.addListener(observable -> this.updateTextPos());
        textField.textProperty().addListener(observable -> {
            if (!((TextFieldBehavior)this.getBehavior()).isEditing()) {
                this.updateTextPos();
            }
        });
        if (this.usePromptText.get()) {
            this.createPromptNode();
        }
        this.usePromptText.addListener(observable -> {
            this.createPromptNode();
            textField.requestLayout();
        });
        if (SHOW_HANDLES) {
            this.selectionHandle1.setRotate(180.0);
            EventHandler<MouseEvent> eventHandler = mouseEvent -> {
                this.pressX = mouseEvent.getX();
                this.pressY = mouseEvent.getY();
                mouseEvent.consume();
            };
            this.caretHandle.setOnMousePressed(eventHandler);
            this.selectionHandle1.setOnMousePressed(eventHandler);
            this.selectionHandle2.setOnMousePressed(eventHandler);
            this.caretHandle.setOnMouseDragged(mouseEvent -> {
                Point2D point2D = new Point2D(this.caretHandle.getLayoutX() + mouseEvent.getX() + this.pressX - this.textNode.getLayoutX(), this.caretHandle.getLayoutY() + mouseEvent.getY() - this.pressY - 6.0);
                HitInfo hitInfo = this.textNode.impl_hitTestChar(this.translateCaretPosition(point2D));
                this.positionCaret(hitInfo, false);
                mouseEvent.consume();
            });
            this.selectionHandle1.setOnMouseDragged((EventHandler<? super MouseEvent>)new EventHandler<MouseEvent>(){

                @Override
                public void handle(MouseEvent mouseEvent) {
                    TextField textField = (TextField)TextFieldSkin.this.getSkinnable();
                    Point2D point2D = TextFieldSkin.this.textNode.localToScene(0.0, 0.0);
                    Point2D point2D2 = new Point2D(mouseEvent.getSceneX() - point2D.getX() + 10.0 - TextFieldSkin.this.pressX + TextFieldSkin.this.selectionHandle1.getWidth() / 2.0, mouseEvent.getSceneY() - point2D.getY() - TextFieldSkin.this.pressY - 6.0);
                    HitInfo hitInfo = TextFieldSkin.this.textNode.impl_hitTestChar(TextFieldSkin.this.translateCaretPosition(point2D2));
                    int n2 = hitInfo.getCharIndex();
                    if (textField.getAnchor() < textField.getCaretPosition()) {
                        textField.selectRange(textField.getCaretPosition(), textField.getAnchor());
                    }
                    if (n2 >= 0) {
                        if (n2 >= textField.getAnchor() - 1) {
                            hitInfo.setCharIndex(Math.max(0, textField.getAnchor() - 1));
                        }
                        TextFieldSkin.this.positionCaret(hitInfo, true);
                    }
                    mouseEvent.consume();
                }
            });
            this.selectionHandle2.setOnMouseDragged((EventHandler<? super MouseEvent>)new EventHandler<MouseEvent>(){

                @Override
                public void handle(MouseEvent mouseEvent) {
                    TextField textField = (TextField)TextFieldSkin.this.getSkinnable();
                    Point2D point2D = TextFieldSkin.this.textNode.localToScene(0.0, 0.0);
                    Point2D point2D2 = new Point2D(mouseEvent.getSceneX() - point2D.getX() + 10.0 - TextFieldSkin.this.pressX + TextFieldSkin.this.selectionHandle2.getWidth() / 2.0, mouseEvent.getSceneY() - point2D.getY() - TextFieldSkin.this.pressY - 6.0);
                    HitInfo hitInfo = TextFieldSkin.this.textNode.impl_hitTestChar(TextFieldSkin.this.translateCaretPosition(point2D2));
                    int n2 = hitInfo.getCharIndex();
                    if (textField.getAnchor() > textField.getCaretPosition()) {
                        textField.selectRange(textField.getCaretPosition(), textField.getAnchor());
                    }
                    if (n2 > 0) {
                        if (n2 <= textField.getAnchor()) {
                            hitInfo.setCharIndex(Math.min(textField.getAnchor() + 1, textField.getLength()));
                        }
                        TextFieldSkin.this.positionCaret(hitInfo, true);
                    }
                    mouseEvent.consume();
                }
            });
        }
    }

    private void updateTextNodeCaretPos(int n2) {
        if (n2 == 0 || this.isForwardBias()) {
            this.textNode.setImpl_caretPosition(n2);
        } else {
            this.textNode.setImpl_caretPosition(n2 - 1);
        }
        this.textNode.impl_caretBiasProperty().set(this.isForwardBias());
    }

    private void createPromptNode() {
        if (this.promptNode != null || !this.usePromptText.get()) {
            return;
        }
        this.promptNode = new Text();
        this.textGroup.getChildren().add(0, this.promptNode);
        this.promptNode.setManaged(false);
        this.promptNode.getStyleClass().add("text");
        this.promptNode.visibleProperty().bind(this.usePromptText);
        this.promptNode.fontProperty().bind(((TextField)this.getSkinnable()).fontProperty());
        this.promptNode.textProperty().bind(((TextField)this.getSkinnable()).promptTextProperty());
        this.promptNode.fillProperty().bind(this.promptTextFill);
        this.updateSelection();
    }

    private void updateSelection() {
        TextField textField = (TextField)this.getSkinnable();
        IndexRange indexRange = textField.getSelection();
        if (indexRange == null || indexRange.getLength() == 0) {
            this.textNode.impl_selectionStartProperty().set(-1);
            this.textNode.impl_selectionEndProperty().set(-1);
        } else {
            this.textNode.impl_selectionStartProperty().set(indexRange.getStart());
            this.textNode.impl_selectionEndProperty().set(indexRange.getStart());
            this.textNode.impl_selectionEndProperty().set(indexRange.getEnd());
        }
        PathElement[] arrpathElement = (PathElement[])this.textNode.impl_selectionShapeProperty().get();
        if (arrpathElement == null) {
            this.selectionHighlightPath.getElements().clear();
        } else {
            this.selectionHighlightPath.getElements().setAll(arrpathElement);
        }
        if (SHOW_HANDLES && indexRange != null && indexRange.getLength() > 0) {
            int n2 = textField.getCaretPosition();
            int n3 = textField.getAnchor();
            this.updateTextNodeCaretPos(n3);
            Bounds bounds = this.caretPath.getBoundsInParent();
            if (n2 < n3) {
                this.selectionHandle2.setLayoutX(bounds.getMinX() - this.selectionHandle2.getWidth() / 2.0);
            } else {
                this.selectionHandle1.setLayoutX(bounds.getMinX() - this.selectionHandle1.getWidth() / 2.0);
            }
            this.updateTextNodeCaretPos(n2);
            bounds = this.caretPath.getBoundsInParent();
            if (n2 < n3) {
                this.selectionHandle1.setLayoutX(bounds.getMinX() - this.selectionHandle1.getWidth() / 2.0);
            } else {
                this.selectionHandle2.setLayoutX(bounds.getMinX() - this.selectionHandle2.getWidth() / 2.0);
            }
        }
    }

    @Override
    protected void handleControlPropertyChanged(String string) {
        if ("prefColumnCount".equals(string)) {
            ((TextField)this.getSkinnable()).requestLayout();
        } else {
            super.handleControlPropertyChanged(string);
        }
    }

    @Override
    protected double computePrefWidth(double d2, double d3, double d4, double d5, double d6) {
        TextField textField = (TextField)this.getSkinnable();
        double d7 = ((FontMetrics)this.fontMetrics.get()).computeStringWidth("W");
        int n2 = textField.getPrefColumnCount();
        return (double)n2 * d7 + d6 + d4;
    }

    @Override
    protected double computeMinHeight(double d2, double d3, double d4, double d5, double d6) {
        return this.computePrefHeight(d2, d3, d4, d5, d6);
    }

    @Override
    protected double computePrefHeight(double d2, double d3, double d4, double d5, double d6) {
        return d3 + this.textNode.getLayoutBounds().getHeight() + d5;
    }

    @Override
    protected double computeMaxHeight(double d2, double d3, double d4, double d5, double d6) {
        return ((TextField)this.getSkinnable()).prefHeight(d2);
    }

    @Override
    public double computeBaselineOffset(double d2, double d3, double d4, double d5) {
        return d2 + this.textNode.getBaselineOffset();
    }

    private void updateTextPos() {
        double d2 = this.textTranslateX.get();
        double d3 = this.textNode.getLayoutBounds().getWidth();
        switch (this.getHAlignment()) {
            case CENTER: {
                double d4;
                double d5 = this.textRight.get() / 2.0;
                if (this.usePromptText.get()) {
                    d4 = d5 - this.promptNode.getLayoutBounds().getWidth() / 2.0;
                    this.promptNode.setLayoutX(d4);
                } else {
                    d4 = d5 - d3 / 2.0;
                }
                if (!(d4 + d3 <= this.textRight.get())) break;
                this.textTranslateX.set(d4);
                break;
            }
            case RIGHT: {
                double d6 = this.textRight.get() - d3 - this.caretWidth / 2.0;
                if (d6 > d2 || d6 > 0.0) {
                    this.textTranslateX.set(d6);
                }
                if (!this.usePromptText.get()) break;
                this.promptNode.setLayoutX(this.textRight.get() - this.promptNode.getLayoutBounds().getWidth() - this.caretWidth / 2.0);
                break;
            }
            default: {
                double d7 = this.caretWidth / 2.0;
                if (d7 < d2 || d7 + d3 <= this.textRight.get()) {
                    this.textTranslateX.set(d7);
                }
                if (!this.usePromptText.get()) break;
                this.promptNode.layoutXProperty().set(d7);
            }
        }
    }

    protected void updateCaretOff() {
        double d2 = 0.0;
        double d3 = this.caretPath.getLayoutBounds().getMinX() + this.textTranslateX.get();
        if (d3 < 0.0) {
            d2 = d3;
        } else if (d3 > this.textRight.get() - this.caretWidth) {
            d2 = d3 - (this.textRight.get() - this.caretWidth);
        }
        switch (this.getHAlignment()) {
            case CENTER: {
                this.textTranslateX.set(this.textTranslateX.get() - d2);
                break;
            }
            case RIGHT: {
                this.textTranslateX.set(Math.max(this.textTranslateX.get() - d2, this.textRight.get() - this.textNode.getLayoutBounds().getWidth() - this.caretWidth / 2.0));
                break;
            }
            default: {
                this.textTranslateX.set(Math.min(this.textTranslateX.get() - d2, this.caretWidth / 2.0));
            }
        }
        if (SHOW_HANDLES) {
            this.caretHandle.setLayoutX(d3 - this.caretHandle.getWidth() / 2.0 + 1.0);
        }
    }

    public void replaceText(int n2, int n3, String string) {
        double d2 = this.textNode.getBoundsInParent().getMaxX();
        double d3 = this.caretPath.getLayoutBounds().getMaxX() + this.textTranslateX.get();
        ((TextField)this.getSkinnable()).replaceText(n2, n3, string);
        this.scrollAfterDelete(d2, d3);
    }

    public void deleteChar(boolean bl) {
        boolean bl2;
        double d2 = this.textNode.getBoundsInParent().getMaxX();
        double d3 = this.caretPath.getLayoutBounds().getMaxX() + this.textTranslateX.get();
        boolean bl3 = bl ? !((TextField)this.getSkinnable()).deletePreviousChar() : (bl2 = !((TextField)this.getSkinnable()).deleteNextChar());
        if (!bl2) {
            this.scrollAfterDelete(d2, d3);
        }
    }

    public void scrollAfterDelete(double d2, double d3) {
        Bounds bounds = this.textNode.getLayoutBounds();
        Bounds bounds2 = this.textNode.localToParent(bounds);
        Bounds bounds3 = this.clip.getBoundsInParent();
        Bounds bounds4 = this.caretPath.getLayoutBounds();
        switch (this.getHAlignment()) {
            case RIGHT: {
                if (bounds2.getMaxX() > bounds3.getMaxX()) {
                    double d4 = d3 - bounds4.getMaxX() - this.textTranslateX.get();
                    if (bounds2.getMaxX() + d4 < bounds3.getMaxX()) {
                        d4 = d2 <= bounds3.getMaxX() ? d2 - bounds2.getMaxX() : bounds3.getMaxX() - bounds2.getMaxX();
                    }
                    this.textTranslateX.set(this.textTranslateX.get() + d4);
                    break;
                }
                this.updateTextPos();
                break;
            }
            default: {
                if (!(bounds2.getMinX() < bounds3.getMinX() + this.caretWidth / 2.0) || !(bounds2.getMaxX() <= bounds3.getMaxX())) break;
                double d5 = d3 - bounds4.getMaxX() - this.textTranslateX.get();
                if (bounds2.getMaxX() + d5 < bounds3.getMaxX()) {
                    d5 = d2 <= bounds3.getMaxX() ? d2 - bounds2.getMaxX() : bounds3.getMaxX() - bounds2.getMaxX();
                }
                this.textTranslateX.set(this.textTranslateX.get() + d5);
            }
        }
        this.updateCaretOff();
    }

    public HitInfo getIndex(double d2, double d3) {
        Point2D point2D = new Point2D(d2 - this.textTranslateX.get() - this.snappedLeftInset(), d3 - this.snappedTopInset());
        return this.textNode.impl_hitTestChar(this.translateCaretPosition(point2D));
    }

    public void positionCaret(HitInfo hitInfo, boolean bl) {
        TextField textField = (TextField)this.getSkinnable();
        int n2 = Utils.getHitInsertionIndex(hitInfo, textField.textProperty().getValueSafe());
        if (bl) {
            textField.selectPositionCaret(n2);
        } else {
            textField.positionCaret(n2);
        }
        this.setForwardBias(hitInfo.isLeading());
    }

    @Override
    public Rectangle2D getCharacterBounds(int n2) {
        double d2;
        double d3;
        double d4;
        double d5;
        Bounds bounds;
        if (n2 == this.textNode.getText().length()) {
            bounds = this.textNode.getBoundsInLocal();
            d5 = bounds.getMaxX();
            d4 = 0.0;
            d3 = 0.0;
            d2 = bounds.getMaxY();
        } else {
            this.characterBoundingPath.getElements().clear();
            this.characterBoundingPath.getElements().addAll(this.textNode.impl_getRangeShape(n2, n2 + 1));
            this.characterBoundingPath.setLayoutX(this.textNode.getLayoutX());
            this.characterBoundingPath.setLayoutY(this.textNode.getLayoutY());
            bounds = this.characterBoundingPath.getBoundsInLocal();
            d5 = bounds.getMinX();
            d4 = bounds.getMinY();
            d3 = bounds.isEmpty() ? 0.0 : bounds.getWidth();
            d2 = bounds.isEmpty() ? 0.0 : bounds.getHeight();
        }
        bounds = this.textGroup.getBoundsInParent();
        return new Rectangle2D(d5 + bounds.getMinX() + this.textTranslateX.get(), d4 + bounds.getMinY(), d3, d2);
    }

    @Override
    protected PathElement[] getUnderlineShape(int n2, int n3) {
        return this.textNode.impl_getUnderlineShape(n2, n3);
    }

    @Override
    protected PathElement[] getRangeShape(int n2, int n3) {
        return this.textNode.impl_getRangeShape(n2, n3);
    }

    @Override
    protected void addHighlight(List<? extends Node> list, int n2) {
        this.textGroup.getChildren().addAll((Collection<Node>)list);
    }

    @Override
    protected void removeHighlight(List<? extends Node> list) {
        this.textGroup.getChildren().removeAll((Collection<?>)list);
    }

    @Override
    public void nextCharacterVisually(boolean bl) {
        if (this.isRTL()) {
            bl = !bl;
        }
        Bounds bounds = this.caretPath.getLayoutBounds();
        if (this.caretPath.getElements().size() == 4) {
            bounds = new Path((PathElement)this.caretPath.getElements().get(0), (PathElement)this.caretPath.getElements().get(1)).getLayoutBounds();
        }
        double d2 = bl ? bounds.getMaxX() : bounds.getMinX();
        double d3 = (bounds.getMinY() + bounds.getMaxY()) / 2.0;
        HitInfo hitInfo = this.textNode.impl_hitTestChar(this.translateCaretPosition(new Point2D(d2, d3)));
        Path path = new Path(this.textNode.impl_getRangeShape(hitInfo.getCharIndex(), hitInfo.getCharIndex() + 1));
        if (bl && path.getLayoutBounds().getMaxX() > bounds.getMaxX() || !bl && path.getLayoutBounds().getMinX() < bounds.getMinX()) {
            hitInfo.setLeading(!hitInfo.isLeading());
        }
        this.positionCaret(hitInfo, false);
    }

    @Override
    protected void layoutChildren(double d2, double d3, double d4, double d5) {
        super.layoutChildren(d2, d3, d4, d5);
        if (this.textNode != null) {
            double d6;
            Bounds bounds = this.textNode.getLayoutBounds();
            double d7 = this.textNode.getBaselineOffset();
            double d8 = bounds.getHeight() - d7;
            switch (((TextField)this.getSkinnable()).getAlignment().getVpos()) {
                case TOP: {
                    d6 = d7;
                    break;
                }
                case CENTER: {
                    d6 = (d7 + this.textGroup.getHeight() - d8) / 2.0;
                    break;
                }
                default: {
                    d6 = this.textGroup.getHeight() - d8;
                }
            }
            this.textNode.setY(d6);
            if (this.promptNode != null) {
                this.promptNode.setY(d6);
            }
            if (((TextField)this.getSkinnable()).getWidth() > 0.0) {
                this.updateTextPos();
                this.updateCaretOff();
            }
        }
        if (SHOW_HANDLES) {
            this.handleGroup.setLayoutX(d2 + this.textTranslateX.get());
            this.handleGroup.setLayoutY(d3);
            this.selectionHandle1.resize(this.selectionHandle1.prefWidth(-1.0), this.selectionHandle1.prefHeight(-1.0));
            this.selectionHandle2.resize(this.selectionHandle2.prefWidth(-1.0), this.selectionHandle2.prefHeight(-1.0));
            this.caretHandle.resize(this.caretHandle.prefWidth(-1.0), this.caretHandle.prefHeight(-1.0));
            Bounds bounds = this.caretPath.getBoundsInParent();
            this.caretHandle.setLayoutY(bounds.getMaxY() - 1.0);
            this.selectionHandle1.setLayoutY(bounds.getMinY() - this.selectionHandle1.getHeight() + 1.0);
            this.selectionHandle2.setLayoutY(bounds.getMaxY() - 1.0);
        }
    }

    protected HPos getHAlignment() {
        HPos hPos = ((TextField)this.getSkinnable()).getAlignment().getHpos();
        return hPos;
    }

    @Override
    public Point2D getMenuPosition() {
        Point2D point2D = super.getMenuPosition();
        if (point2D != null) {
            point2D = new Point2D(Math.max(0.0, point2D.getX() - this.textNode.getLayoutX() - this.snappedLeftInset() + this.textTranslateX.get()), Math.max(0.0, point2D.getY() - this.textNode.getLayoutY() - this.snappedTopInset()));
        }
        return point2D;
    }

    @Override
    protected String maskText(String string) {
        if (this.getSkinnable() instanceof PasswordField) {
            int n2 = string.length();
            StringBuilder stringBuilder = new StringBuilder(n2);
            for (int i2 = 0; i2 < n2; ++i2) {
                stringBuilder.append('\u25cf');
            }
            return stringBuilder.toString();
        }
        return string;
    }

    @Override
    protected Object queryAccessibleAttribute(AccessibleAttribute accessibleAttribute, Object ... arrobject) {
        switch (accessibleAttribute) {
            case BOUNDS_FOR_RANGE: 
            case OFFSET_AT_POINT: {
                return this.textNode.queryAccessibleAttribute(accessibleAttribute, arrobject);
            }
        }
        return super.queryAccessibleAttribute(accessibleAttribute, arrobject);
    }
}

