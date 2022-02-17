/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.TextAreaBehavior;
import com.sun.javafx.scene.control.skin.TextInputControlSkin;
import com.sun.javafx.scene.control.skin.Utils;
import com.sun.javafx.scene.text.HitInfo;
import com.sun.javafx.tk.FontMetrics;
import java.util.Collection;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableIntegerValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.geometry.VerticalDirection;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.IndexRange;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Paint;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class TextAreaSkin
extends TextInputControlSkin<TextArea, TextAreaBehavior> {
    private final TextArea textArea;
    private final boolean USE_MULTIPLE_NODES = false;
    private double computedMinWidth = Double.NEGATIVE_INFINITY;
    private double computedMinHeight = Double.NEGATIVE_INFINITY;
    private double computedPrefWidth = Double.NEGATIVE_INFINITY;
    private double computedPrefHeight = Double.NEGATIVE_INFINITY;
    private double widthForComputedPrefHeight = Double.NEGATIVE_INFINITY;
    private double characterWidth;
    private double lineHeight;
    private ContentView contentView = new ContentView();
    private Group paragraphNodes = new Group();
    private Text promptNode;
    private ObservableBooleanValue usePromptText;
    private ObservableIntegerValue caretPosition;
    private Group selectionHighlightGroup = new Group();
    private ScrollPane scrollPane;
    private Bounds oldViewportBounds;
    private VerticalDirection scrollDirection = null;
    private Path characterBoundingPath = new Path();
    private Timeline scrollSelectionTimeline = new Timeline();
    private EventHandler<ActionEvent> scrollSelectionHandler = actionEvent -> {
        switch (this.scrollDirection) {
            case UP: {
                break;
            }
        }
    };
    public static final int SCROLL_RATE = 30;
    private double pressX;
    private double pressY;
    private boolean handlePressed;
    double targetCaretX = -1.0;
    private static final Path tmpCaretPath = new Path();

    @Override
    protected void invalidateMetrics() {
        this.computedMinWidth = Double.NEGATIVE_INFINITY;
        this.computedMinHeight = Double.NEGATIVE_INFINITY;
        this.computedPrefWidth = Double.NEGATIVE_INFINITY;
        this.computedPrefHeight = Double.NEGATIVE_INFINITY;
    }

    public TextAreaSkin(final TextArea textArea) {
        super(textArea, new TextAreaBehavior(textArea));
        ((TextAreaBehavior)this.getBehavior()).setTextAreaSkin(this);
        this.textArea = textArea;
        this.caretPosition = new IntegerBinding(){
            {
                this.bind(textArea.caretPositionProperty());
            }

            @Override
            protected int computeValue() {
                return textArea.getCaretPosition();
            }
        };
        this.caretPosition.addListener((observableValue, number, number2) -> {
            this.targetCaretX = -1.0;
            if (number2.intValue() > number.intValue()) {
                this.setForwardBias(true);
            }
        });
        this.forwardBiasProperty().addListener(observable -> {
            if (textArea.getWidth() > 0.0) {
                this.updateTextNodeCaretPos(textArea.getCaretPosition());
            }
        });
        this.scrollPane = new ScrollPane();
        this.scrollPane.setFitToWidth(textArea.isWrapText());
        this.scrollPane.setContent(this.contentView);
        this.getChildren().add(this.scrollPane);
        ((TextArea)this.getSkinnable()).addEventFilter(ScrollEvent.ANY, scrollEvent -> {
            if (scrollEvent.isDirect() && this.handlePressed) {
                scrollEvent.consume();
            }
        });
        this.selectionHighlightGroup.setManaged(false);
        this.selectionHighlightGroup.setVisible(false);
        this.contentView.getChildren().add(this.selectionHighlightGroup);
        this.paragraphNodes.setManaged(false);
        this.contentView.getChildren().add(this.paragraphNodes);
        this.caretPath.setManaged(false);
        this.caretPath.setStrokeWidth(1.0);
        this.caretPath.fillProperty().bind(this.textFill);
        this.caretPath.strokeProperty().bind(this.textFill);
        this.caretPath.opacityProperty().bind(new DoubleBinding(){
            {
                this.bind(TextAreaSkin.this.caretVisible);
            }

            @Override
            protected double computeValue() {
                return TextAreaSkin.this.caretVisible.get() ? 1.0 : 0.0;
            }
        });
        this.contentView.getChildren().add(this.caretPath);
        if (SHOW_HANDLES) {
            this.contentView.getChildren().addAll(this.caretHandle, this.selectionHandle1, this.selectionHandle2);
        }
        this.scrollPane.hvalueProperty().addListener((observableValue, number, number2) -> ((TextArea)this.getSkinnable()).setScrollLeft(number2.doubleValue() * this.getScrollLeftMax()));
        this.scrollPane.vvalueProperty().addListener((observableValue, number, number2) -> ((TextArea)this.getSkinnable()).setScrollTop(number2.doubleValue() * this.getScrollTopMax()));
        this.scrollSelectionTimeline.setCycleCount(-1);
        ObservableList<KeyFrame> observableList = this.scrollSelectionTimeline.getKeyFrames();
        observableList.clear();
        observableList.add(new KeyFrame(Duration.millis(350.0), this.scrollSelectionHandler, new KeyValue[0]));
        int n2 = 1;
        for (int i2 = 0; i2 < n2; ++i2) {
            String string = n2 == 1 ? textArea.textProperty().getValueSafe() : (CharSequence)textArea.getParagraphs().get(i2);
            this.addParagraphNode(i2, string.toString());
        }
        textArea.selectionProperty().addListener((observableValue, indexRange, indexRange2) -> {
            textArea.requestLayout();
            this.contentView.requestLayout();
        });
        textArea.wrapTextProperty().addListener((observableValue, bl, bl2) -> {
            this.invalidateMetrics();
            this.scrollPane.setFitToWidth((boolean)bl2);
        });
        textArea.prefColumnCountProperty().addListener((observableValue, number, number2) -> {
            this.invalidateMetrics();
            this.updatePrefViewportWidth();
        });
        textArea.prefRowCountProperty().addListener((observableValue, number, number2) -> {
            this.invalidateMetrics();
            this.updatePrefViewportHeight();
        });
        this.updateFontMetrics();
        this.fontMetrics.addListener(observable -> this.updateFontMetrics());
        this.contentView.paddingProperty().addListener(observable -> {
            this.updatePrefViewportWidth();
            this.updatePrefViewportHeight();
        });
        this.scrollPane.viewportBoundsProperty().addListener(observable -> {
            if (this.scrollPane.getViewportBounds() != null) {
                Bounds bounds = this.scrollPane.getViewportBounds();
                if (this.oldViewportBounds == null || this.oldViewportBounds.getWidth() != bounds.getWidth() || this.oldViewportBounds.getHeight() != bounds.getHeight()) {
                    this.invalidateMetrics();
                    this.oldViewportBounds = bounds;
                    this.contentView.requestLayout();
                }
            }
        });
        textArea.scrollTopProperty().addListener((observableValue, number, number2) -> {
            double d2 = number2.doubleValue() < this.getScrollTopMax() ? number2.doubleValue() / this.getScrollTopMax() : 1.0;
            this.scrollPane.setVvalue(d2);
        });
        textArea.scrollLeftProperty().addListener((observableValue, number, number2) -> {
            double d2 = number2.doubleValue() < this.getScrollLeftMax() ? number2.doubleValue() / this.getScrollLeftMax() : 1.0;
            this.scrollPane.setHvalue(d2);
        });
        textArea.textProperty().addListener(observable -> {
            this.invalidateMetrics();
            ((Text)this.paragraphNodes.getChildren().get(0)).setText(textArea.textProperty().getValueSafe());
            this.contentView.requestLayout();
        });
        this.usePromptText = new BooleanBinding(){
            {
                this.bind(textArea.textProperty(), textArea.promptTextProperty());
            }

            @Override
            protected boolean computeValue() {
                String string = textArea.getText();
                String string2 = textArea.getPromptText();
                return (string == null || string.isEmpty()) && string2 != null && !string2.isEmpty();
            }
        };
        if (this.usePromptText.get()) {
            this.createPromptNode();
        }
        this.usePromptText.addListener(observable -> {
            this.createPromptNode();
            textArea.requestLayout();
        });
        this.updateHighlightFill();
        this.updatePrefViewportWidth();
        this.updatePrefViewportHeight();
        if (textArea.isFocused()) {
            this.setCaretAnimating(true);
        }
        if (SHOW_HANDLES) {
            this.selectionHandle1.setRotate(180.0);
            EventHandler<MouseEvent> eventHandler = mouseEvent -> {
                this.pressX = mouseEvent.getX();
                this.pressY = mouseEvent.getY();
                this.handlePressed = true;
                mouseEvent.consume();
            };
            EventHandler<MouseEvent> eventHandler2 = mouseEvent -> {
                this.handlePressed = false;
            };
            this.caretHandle.setOnMousePressed(eventHandler);
            this.selectionHandle1.setOnMousePressed(eventHandler);
            this.selectionHandle2.setOnMousePressed(eventHandler);
            this.caretHandle.setOnMouseReleased(eventHandler2);
            this.selectionHandle1.setOnMouseReleased(eventHandler2);
            this.selectionHandle2.setOnMouseReleased(eventHandler2);
            this.caretHandle.setOnMouseDragged(mouseEvent -> {
                Text text = this.getTextNode();
                Point2D point2D = text.localToScene(0.0, 0.0);
                Point2D point2D2 = new Point2D(mouseEvent.getSceneX() - point2D.getX() + 10.0 - this.pressX + this.caretHandle.getWidth() / 2.0, mouseEvent.getSceneY() - point2D.getY() - this.pressY - 6.0);
                HitInfo hitInfo = text.impl_hitTestChar(this.translateCaretPosition(point2D2));
                int n2 = hitInfo.getCharIndex();
                if (n2 > 0) {
                    int n3 = text.getImpl_caretPosition();
                    text.setImpl_caretPosition(n2);
                    PathElement pathElement = text.getImpl_caretShape()[0];
                    if (pathElement instanceof MoveTo && ((MoveTo)pathElement).getY() > mouseEvent.getY() - this.getTextTranslateY()) {
                        hitInfo.setCharIndex(n2 - 1);
                    }
                    text.setImpl_caretPosition(n3);
                }
                this.positionCaret(hitInfo, false, false);
                mouseEvent.consume();
            });
            this.selectionHandle1.setOnMouseDragged((EventHandler<? super MouseEvent>)new EventHandler<MouseEvent>(){

                @Override
                public void handle(MouseEvent mouseEvent) {
                    TextArea textArea = (TextArea)TextAreaSkin.this.getSkinnable();
                    Text text = TextAreaSkin.this.getTextNode();
                    Point2D point2D = text.localToScene(0.0, 0.0);
                    Point2D point2D2 = new Point2D(mouseEvent.getSceneX() - point2D.getX() + 10.0 - TextAreaSkin.this.pressX + TextAreaSkin.this.selectionHandle1.getWidth() / 2.0, mouseEvent.getSceneY() - point2D.getY() - TextAreaSkin.this.pressY + TextAreaSkin.this.selectionHandle1.getHeight() + 5.0);
                    HitInfo hitInfo = text.impl_hitTestChar(TextAreaSkin.this.translateCaretPosition(point2D2));
                    int n2 = hitInfo.getCharIndex();
                    if (textArea.getAnchor() < textArea.getCaretPosition()) {
                        textArea.selectRange(textArea.getCaretPosition(), textArea.getAnchor());
                    }
                    if (n2 > 0) {
                        if (n2 >= textArea.getAnchor()) {
                            n2 = textArea.getAnchor();
                        }
                        int n3 = text.getImpl_caretPosition();
                        text.setImpl_caretPosition(n2);
                        PathElement pathElement = text.getImpl_caretShape()[0];
                        if (pathElement instanceof MoveTo && ((MoveTo)pathElement).getY() > mouseEvent.getY() - TextAreaSkin.this.getTextTranslateY()) {
                            hitInfo.setCharIndex(n2 - 1);
                        }
                        text.setImpl_caretPosition(n3);
                    }
                    TextAreaSkin.this.positionCaret(hitInfo, true, false);
                    mouseEvent.consume();
                }
            });
            this.selectionHandle2.setOnMouseDragged((EventHandler<? super MouseEvent>)new EventHandler<MouseEvent>(){

                @Override
                public void handle(MouseEvent mouseEvent) {
                    TextArea textArea = (TextArea)TextAreaSkin.this.getSkinnable();
                    Text text = TextAreaSkin.this.getTextNode();
                    Point2D point2D = text.localToScene(0.0, 0.0);
                    Point2D point2D2 = new Point2D(mouseEvent.getSceneX() - point2D.getX() + 10.0 - TextAreaSkin.this.pressX + TextAreaSkin.this.selectionHandle2.getWidth() / 2.0, mouseEvent.getSceneY() - point2D.getY() - TextAreaSkin.this.pressY - 6.0);
                    HitInfo hitInfo = text.impl_hitTestChar(TextAreaSkin.this.translateCaretPosition(point2D2));
                    int n2 = hitInfo.getCharIndex();
                    if (textArea.getAnchor() > textArea.getCaretPosition()) {
                        textArea.selectRange(textArea.getCaretPosition(), textArea.getAnchor());
                    }
                    if (n2 > 0) {
                        if (n2 <= textArea.getAnchor() + 1) {
                            n2 = Math.min(textArea.getAnchor() + 2, textArea.getLength());
                        }
                        int n3 = text.getImpl_caretPosition();
                        text.setImpl_caretPosition(n2);
                        PathElement pathElement = text.getImpl_caretShape()[0];
                        if (pathElement instanceof MoveTo && ((MoveTo)pathElement).getY() > mouseEvent.getY() - TextAreaSkin.this.getTextTranslateY()) {
                            hitInfo.setCharIndex(n2 - 1);
                        }
                        text.setImpl_caretPosition(n3);
                        TextAreaSkin.this.positionCaret(hitInfo, true, false);
                    }
                    mouseEvent.consume();
                }
            });
        }
    }

    @Override
    protected void layoutChildren(double d2, double d3, double d4, double d5) {
        this.scrollPane.resizeRelocate(d2, d3, d4, d5);
    }

    private void createPromptNode() {
        if (this.promptNode == null && this.usePromptText.get()) {
            this.promptNode = new Text();
            this.contentView.getChildren().add(0, this.promptNode);
            this.promptNode.setManaged(false);
            this.promptNode.getStyleClass().add("text");
            this.promptNode.visibleProperty().bind(this.usePromptText);
            this.promptNode.fontProperty().bind(((TextArea)this.getSkinnable()).fontProperty());
            this.promptNode.textProperty().bind(((TextArea)this.getSkinnable()).promptTextProperty());
            this.promptNode.fillProperty().bind(this.promptTextFill);
        }
    }

    private void addParagraphNode(int n2, String string) {
        TextArea textArea = (TextArea)this.getSkinnable();
        Text text = new Text(string);
        text.setTextOrigin(VPos.TOP);
        text.setManaged(false);
        text.getStyleClass().add("text");
        text.boundsTypeProperty().addListener((observableValue, textBoundsType, textBoundsType2) -> {
            this.invalidateMetrics();
            this.updateFontMetrics();
        });
        this.paragraphNodes.getChildren().add(n2, text);
        text.fontProperty().bind(textArea.fontProperty());
        text.fillProperty().bind(this.textFill);
        text.impl_selectionFillProperty().bind(this.highlightTextFill);
    }

    @Override
    public void dispose() {
        throw new UnsupportedOperationException();
    }

    @Override
    public double computeBaselineOffset(double d2, double d3, double d4, double d5) {
        Text text = (Text)this.paragraphNodes.getChildren().get(0);
        return Utils.getAscent(((TextArea)this.getSkinnable()).getFont(), text.getBoundsType()) + this.contentView.snappedTopInset() + this.textArea.snappedTopInset();
    }

    @Override
    public char getCharacter(int n2) {
        Text text;
        int n3;
        int n4;
        int n5 = this.paragraphNodes.getChildren().size();
        int n6 = 0;
        String string = null;
        for (n3 = n2; n6 < n5 && n3 >= (n4 = (string = (text = (Text)this.paragraphNodes.getChildren().get(n6)).getText()).length() + 1); n3 -= n4, ++n6) {
        }
        return n3 == string.length() ? (char)'\n' : string.charAt(n3);
    }

    @Override
    public int getInsertionPoint(double d2, double d3) {
        TextArea textArea = (TextArea)this.getSkinnable();
        int n2 = this.paragraphNodes.getChildren().size();
        int n3 = -1;
        if (n2 > 0) {
            if (d3 < this.contentView.snappedTopInset()) {
                Text text = (Text)this.paragraphNodes.getChildren().get(0);
                n3 = this.getNextInsertionPoint(text, d2, -1, VerticalDirection.DOWN);
            } else if (d3 > this.contentView.snappedTopInset() + this.contentView.getHeight()) {
                int n4 = n2 - 1;
                Text text = (Text)this.paragraphNodes.getChildren().get(n4);
                n3 = this.getNextInsertionPoint(text, d2, -1, VerticalDirection.UP) + (textArea.getLength() - text.getText().length());
            } else {
                int n5 = 0;
                for (int i2 = 0; i2 < n2; ++i2) {
                    Text text = (Text)this.paragraphNodes.getChildren().get(i2);
                    Bounds bounds = text.getBoundsInLocal();
                    double d4 = text.getLayoutY() + bounds.getMinY();
                    if (d3 >= d4 && d3 < d4 + text.getBoundsInLocal().getHeight()) {
                        n3 = this.getInsertionPoint(text, d2 - text.getLayoutX(), d3 - text.getLayoutY()) + n5;
                        break;
                    }
                    n5 += text.getText().length() + 1;
                }
            }
        }
        return n3;
    }

    public void positionCaret(HitInfo hitInfo, boolean bl, boolean bl2) {
        boolean bl3;
        int n2 = Utils.getHitInsertionIndex(hitInfo, ((TextArea)this.getSkinnable()).getText());
        boolean bl4 = bl3 = n2 > 0 && n2 <= ((TextArea)this.getSkinnable()).getLength() && ((TextArea)this.getSkinnable()).getText().codePointAt(n2 - 1) == 10;
        if (!hitInfo.isLeading() && bl3) {
            hitInfo.setLeading(true);
            --n2;
        }
        if (bl) {
            if (bl2) {
                ((TextArea)this.getSkinnable()).extendSelection(n2);
            } else {
                ((TextArea)this.getSkinnable()).selectPositionCaret(n2);
            }
        } else {
            ((TextArea)this.getSkinnable()).positionCaret(n2);
        }
        this.setForwardBias(hitInfo.isLeading());
    }

    private double getScrollTopMax() {
        return Math.max(0.0, this.contentView.getHeight() - this.scrollPane.getViewportBounds().getHeight());
    }

    private double getScrollLeftMax() {
        return Math.max(0.0, this.contentView.getWidth() - this.scrollPane.getViewportBounds().getWidth());
    }

    private int getInsertionPoint(Text text, double d2, double d3) {
        HitInfo hitInfo = text.impl_hitTestChar(new Point2D(d2, d3));
        return Utils.getHitInsertionIndex(hitInfo, text.getText());
    }

    public int getNextInsertionPoint(double d2, int n2, VerticalDirection verticalDirection) {
        return 0;
    }

    private int getNextInsertionPoint(Text text, double d2, int n2, VerticalDirection verticalDirection) {
        return 0;
    }

    @Override
    public Rectangle2D getCharacterBounds(int n2) {
        double d2;
        TextArea textArea = (TextArea)this.getSkinnable();
        int n3 = this.paragraphNodes.getChildren().size();
        int n4 = textArea.getLength() + 1;
        Text text = null;
        while (n2 < (n4 -= (text = (Text)this.paragraphNodes.getChildren().get(--n3)).getText().length() + 1)) {
        }
        int n5 = n2 - n4;
        boolean bl = false;
        if (n5 == text.getText().length()) {
            --n5;
            bl = true;
        }
        this.characterBoundingPath.getElements().clear();
        this.characterBoundingPath.getElements().addAll(text.impl_getRangeShape(n5, n5 + 1));
        this.characterBoundingPath.setLayoutX(text.getLayoutX());
        this.characterBoundingPath.setLayoutY(text.getLayoutY());
        Bounds bounds = this.characterBoundingPath.getBoundsInLocal();
        double d3 = bounds.getMinX() + text.getLayoutX() - textArea.getScrollLeft();
        double d4 = bounds.getMinY() + text.getLayoutY() - textArea.getScrollTop();
        double d5 = bounds.isEmpty() ? 0.0 : bounds.getWidth();
        double d6 = d2 = bounds.isEmpty() ? 0.0 : bounds.getHeight();
        if (bl) {
            d3 += d5;
            d5 = 0.0;
        }
        return new Rectangle2D(d3, d4, d5, d2);
    }

    @Override
    public void scrollCharacterToVisible(int n2) {
        Platform.runLater(() -> {
            if (((TextArea)this.getSkinnable()).getLength() == 0) {
                return;
            }
            Rectangle2D rectangle2D = this.getCharacterBounds(n2);
            this.scrollBoundsToVisible(rectangle2D);
        });
    }

    private void scrollCaretToVisible() {
        TextArea textArea = (TextArea)this.getSkinnable();
        Bounds bounds = this.caretPath.getLayoutBounds();
        double d2 = bounds.getMinX() - textArea.getScrollLeft();
        double d3 = bounds.getMinY() - textArea.getScrollTop();
        double d4 = bounds.getWidth();
        double d5 = bounds.getHeight();
        if (SHOW_HANDLES) {
            if (this.caretHandle.isVisible()) {
                d5 += this.caretHandle.getHeight();
            } else if (this.selectionHandle1.isVisible() && this.selectionHandle2.isVisible()) {
                d2 -= this.selectionHandle1.getWidth() / 2.0;
                d3 -= this.selectionHandle1.getHeight();
                d4 += this.selectionHandle1.getWidth() / 2.0 + this.selectionHandle2.getWidth() / 2.0;
                d5 += this.selectionHandle1.getHeight() + this.selectionHandle2.getHeight();
            }
        }
        if (d4 > 0.0 && d5 > 0.0) {
            this.scrollBoundsToVisible(new Rectangle2D(d2, d3, d4, d5));
        }
    }

    private void scrollBoundsToVisible(Rectangle2D rectangle2D) {
        double d2;
        TextArea textArea = (TextArea)this.getSkinnable();
        Bounds bounds = this.scrollPane.getViewportBounds();
        double d3 = bounds.getWidth();
        double d4 = bounds.getHeight();
        double d5 = textArea.getScrollTop();
        double d6 = textArea.getScrollLeft();
        double d7 = 6.0;
        if (rectangle2D.getMinY() < 0.0) {
            d2 = d5 + rectangle2D.getMinY();
            if (d2 <= this.contentView.snappedTopInset()) {
                d2 = 0.0;
            }
            textArea.setScrollTop(d2);
        } else if (this.contentView.snappedTopInset() + rectangle2D.getMaxY() > d4) {
            d2 = d5 + this.contentView.snappedTopInset() + rectangle2D.getMaxY() - d4;
            if (d2 >= this.getScrollTopMax() - this.contentView.snappedBottomInset()) {
                d2 = this.getScrollTopMax();
            }
            textArea.setScrollTop(d2);
        }
        if (rectangle2D.getMinX() < 0.0) {
            d2 = d6 + rectangle2D.getMinX() - d7;
            if (d2 <= this.contentView.snappedLeftInset() + d7) {
                d2 = 0.0;
            }
            textArea.setScrollLeft(d2);
        } else if (this.contentView.snappedLeftInset() + rectangle2D.getMaxX() > d3) {
            d2 = d6 + this.contentView.snappedLeftInset() + rectangle2D.getMaxX() - d3 + d7;
            if (d2 >= this.getScrollLeftMax() - this.contentView.snappedRightInset() - d7) {
                d2 = this.getScrollLeftMax();
            }
            textArea.setScrollLeft(d2);
        }
    }

    private void updatePrefViewportWidth() {
        int n2 = ((TextArea)this.getSkinnable()).getPrefColumnCount();
        this.scrollPane.setPrefViewportWidth((double)n2 * this.characterWidth + this.contentView.snappedLeftInset() + this.contentView.snappedRightInset());
        this.scrollPane.setMinViewportWidth(this.characterWidth + this.contentView.snappedLeftInset() + this.contentView.snappedRightInset());
    }

    private void updatePrefViewportHeight() {
        int n2 = ((TextArea)this.getSkinnable()).getPrefRowCount();
        this.scrollPane.setPrefViewportHeight((double)n2 * this.lineHeight + this.contentView.snappedTopInset() + this.contentView.snappedBottomInset());
        this.scrollPane.setMinViewportHeight(this.lineHeight + this.contentView.snappedTopInset() + this.contentView.snappedBottomInset());
    }

    private void updateFontMetrics() {
        Text text = (Text)this.paragraphNodes.getChildren().get(0);
        this.lineHeight = Utils.getLineHeight(((TextArea)this.getSkinnable()).getFont(), text.getBoundsType());
        this.characterWidth = ((FontMetrics)this.fontMetrics.get()).computeStringWidth("W");
    }

    @Override
    protected void updateHighlightFill() {
        for (Node node : this.selectionHighlightGroup.getChildren()) {
            Path path = (Path)node;
            path.setFill((Paint)this.highlightFill.get());
        }
    }

    private double getTextTranslateX() {
        return this.contentView.snappedLeftInset();
    }

    private double getTextTranslateY() {
        return this.contentView.snappedTopInset();
    }

    private double getTextLeft() {
        return 0.0;
    }

    private Point2D translateCaretPosition(Point2D point2D) {
        return point2D;
    }

    private Text getTextNode() {
        return (Text)this.paragraphNodes.getChildren().get(0);
    }

    public HitInfo getIndex(double d2, double d3) {
        Point2D point2D;
        Text text = this.getTextNode();
        HitInfo hitInfo = text.impl_hitTestChar(this.translateCaretPosition(point2D = new Point2D(d2 - text.getLayoutX(), d3 - this.getTextTranslateY())));
        int n2 = hitInfo.getCharIndex();
        if (n2 > 0) {
            int n3 = text.getImpl_caretPosition();
            text.setImpl_caretPosition(n2);
            PathElement pathElement = text.getImpl_caretShape()[0];
            if (pathElement instanceof MoveTo && ((MoveTo)pathElement).getY() > d3 - this.getTextTranslateY()) {
                hitInfo.setCharIndex(n2 - 1);
            }
            text.setImpl_caretPosition(n3);
        }
        return hitInfo;
    }

    @Override
    public void nextCharacterVisually(boolean bl) {
        if (this.isRTL()) {
            bl = !bl;
        }
        Text text = this.getTextNode();
        Bounds bounds = this.caretPath.getLayoutBounds();
        if (this.caretPath.getElements().size() == 4) {
            bounds = new Path((PathElement)this.caretPath.getElements().get(0), (PathElement)this.caretPath.getElements().get(1)).getLayoutBounds();
        }
        double d2 = bl ? bounds.getMaxX() : bounds.getMinX();
        double d3 = (bounds.getMinY() + bounds.getMaxY()) / 2.0;
        HitInfo hitInfo = text.impl_hitTestChar(new Point2D(d2, d3));
        Path path = new Path(text.impl_getRangeShape(hitInfo.getCharIndex(), hitInfo.getCharIndex() + 1));
        if (bl && path.getLayoutBounds().getMaxX() > bounds.getMaxX() || !bl && path.getLayoutBounds().getMinX() < bounds.getMinX()) {
            hitInfo.setLeading(!hitInfo.isLeading());
            this.positionCaret(hitInfo, false, false);
        } else {
            int n2 = this.textArea.getCaretPosition();
            this.targetCaretX = bl ? 0.0 : Double.MAX_VALUE;
            this.downLines(bl ? 1 : -1, false, false);
            this.targetCaretX = -1.0;
            if (n2 == this.textArea.getCaretPosition()) {
                if (bl) {
                    this.textArea.forward();
                } else {
                    this.textArea.backward();
                }
            }
        }
    }

    protected void downLines(int n2, boolean bl, boolean bl2) {
        Text text = this.getTextNode();
        Bounds bounds = this.caretPath.getLayoutBounds();
        double d2 = (bounds.getMinY() + bounds.getMaxY()) / 2.0 + (double)n2 * this.lineHeight;
        if (d2 < 0.0) {
            d2 = 0.0;
        }
        double d3 = this.targetCaretX >= 0.0 ? this.targetCaretX : bounds.getMaxX();
        HitInfo hitInfo = text.impl_hitTestChar(this.translateCaretPosition(new Point2D(d3, d2)));
        int n3 = hitInfo.getCharIndex();
        int n4 = text.getImpl_caretPosition();
        boolean bl3 = text.isImpl_caretBias();
        text.setImpl_caretBias(hitInfo.isLeading());
        text.setImpl_caretPosition(n3);
        tmpCaretPath.getElements().clear();
        tmpCaretPath.getElements().addAll(text.getImpl_caretShape());
        tmpCaretPath.setLayoutX(text.getLayoutX());
        tmpCaretPath.setLayoutY(text.getLayoutY());
        Bounds bounds2 = tmpCaretPath.getLayoutBounds();
        double d4 = (bounds2.getMinY() + bounds2.getMaxY()) / 2.0;
        text.setImpl_caretBias(bl3);
        text.setImpl_caretPosition(n4);
        if (n3 > 0) {
            if (n2 > 0 && d4 > d2) {
                hitInfo.setCharIndex(n3 - 1);
            }
            if (n3 >= this.textArea.getLength() && this.getCharacter(n3 - 1) == '\n') {
                hitInfo.setLeading(true);
            }
        }
        if (n2 == 0 || n2 > 0 && d4 > bounds.getMaxY() || n2 < 0 && d4 < bounds.getMinY()) {
            this.positionCaret(hitInfo, bl, bl2);
            this.targetCaretX = d3;
        }
    }

    public void previousLine(boolean bl) {
        this.downLines(-1, bl, false);
    }

    public void nextLine(boolean bl) {
        this.downLines(1, bl, false);
    }

    public void previousPage(boolean bl) {
        this.downLines(-((int)(this.scrollPane.getViewportBounds().getHeight() / this.lineHeight)), bl, false);
    }

    public void nextPage(boolean bl) {
        this.downLines((int)(this.scrollPane.getViewportBounds().getHeight() / this.lineHeight), bl, false);
    }

    public void lineStart(boolean bl, boolean bl2) {
        this.targetCaretX = 0.0;
        this.downLines(0, bl, bl2);
        this.targetCaretX = -1.0;
    }

    public void lineEnd(boolean bl, boolean bl2) {
        this.targetCaretX = Double.MAX_VALUE;
        this.downLines(0, bl, bl2);
        this.targetCaretX = -1.0;
    }

    public void paragraphStart(boolean bl, boolean bl2) {
        TextArea textArea = (TextArea)this.getSkinnable();
        String string = textArea.textProperty().getValueSafe();
        int n2 = textArea.getCaretPosition();
        if (n2 > 0) {
            if (bl && string.codePointAt(n2 - 1) == 10) {
                --n2;
            }
            while (n2 > 0 && string.codePointAt(n2 - 1) != 10) {
                --n2;
            }
            if (bl2) {
                textArea.selectPositionCaret(n2);
            } else {
                textArea.positionCaret(n2);
            }
        }
    }

    public void paragraphEnd(boolean bl, boolean bl2, boolean bl3) {
        TextArea textArea = (TextArea)this.getSkinnable();
        String string = textArea.textProperty().getValueSafe();
        int n2 = textArea.getCaretPosition();
        int n3 = string.length();
        boolean bl4 = false;
        if (n2 < n3) {
            if (bl && string.codePointAt(n2) == 10) {
                ++n2;
                bl4 = true;
            }
            if (!bl2 || !bl4) {
                while (n2 < n3 && string.codePointAt(n2) != 10) {
                    ++n2;
                }
                if (bl2 && n2 < n3) {
                    ++n2;
                }
            }
            if (bl3) {
                textArea.selectPositionCaret(n2);
            } else {
                textArea.positionCaret(n2);
            }
        }
    }

    private void updateTextNodeCaretPos(int n2) {
        Text text = this.getTextNode();
        if (this.isForwardBias()) {
            text.setImpl_caretPosition(n2);
        } else {
            text.setImpl_caretPosition(n2 - 1);
        }
        text.impl_caretBiasProperty().set(this.isForwardBias());
    }

    @Override
    protected PathElement[] getUnderlineShape(int n2, int n3) {
        int n4 = 0;
        for (Node node : this.paragraphNodes.getChildren()) {
            Text text = (Text)node;
            int n5 = n4 + text.textProperty().getValueSafe().length();
            if (n5 >= n2) {
                return text.impl_getUnderlineShape(n2 - n4, n3 - n4);
            }
            n4 = n5 + 1;
        }
        return null;
    }

    @Override
    protected PathElement[] getRangeShape(int n2, int n3) {
        int n4 = 0;
        for (Node node : this.paragraphNodes.getChildren()) {
            Text text = (Text)node;
            int n5 = n4 + text.textProperty().getValueSafe().length();
            if (n5 >= n2) {
                return text.impl_getRangeShape(n2 - n4, n3 - n4);
            }
            n4 = n5 + 1;
        }
        return null;
    }

    @Override
    protected void addHighlight(List<? extends Node> list, int n2) {
        int n3 = 0;
        Node node = null;
        for (Node node2 : this.paragraphNodes.getChildren()) {
            Text text = (Text)node2;
            int n4 = n3 + text.textProperty().getValueSafe().length();
            if (n4 >= n2) {
                node = text;
                break;
            }
            n3 = n4 + 1;
        }
        if (node != null) {
            for (Node node2 : list) {
                node2.setLayoutX(node.getLayoutX());
                node2.setLayoutY(node.getLayoutY());
            }
        }
        this.contentView.getChildren().addAll((Collection<Node>)list);
    }

    @Override
    protected void removeHighlight(List<? extends Node> list) {
        this.contentView.getChildren().removeAll((Collection<?>)list);
    }

    public void deleteChar(boolean bl) {
        boolean bl2;
        boolean bl3 = bl ? !((TextArea)this.getSkinnable()).deletePreviousChar() : (bl2 = !((TextArea)this.getSkinnable()).deleteNextChar());
        if (bl2) {
            // empty if block
        }
    }

    @Override
    public Point2D getMenuPosition() {
        this.contentView.layoutChildren();
        Point2D point2D = super.getMenuPosition();
        if (point2D != null) {
            point2D = new Point2D(Math.max(0.0, point2D.getX() - this.contentView.snappedLeftInset() - ((TextArea)this.getSkinnable()).getScrollLeft()), Math.max(0.0, point2D.getY() - this.contentView.snappedTopInset() - ((TextArea)this.getSkinnable()).getScrollTop()));
        }
        return point2D;
    }

    public Bounds getCaretBounds() {
        return ((TextArea)this.getSkinnable()).sceneToLocal(this.caretPath.localToScene(this.caretPath.getBoundsInLocal()));
    }

    @Override
    protected Object queryAccessibleAttribute(AccessibleAttribute accessibleAttribute, Object ... arrobject) {
        switch (accessibleAttribute) {
            case LINE_FOR_OFFSET: 
            case LINE_START: 
            case LINE_END: 
            case BOUNDS_FOR_RANGE: 
            case OFFSET_AT_POINT: {
                Text text = this.getTextNode();
                return text.queryAccessibleAttribute(accessibleAttribute, arrobject);
            }
        }
        return super.queryAccessibleAttribute(accessibleAttribute, arrobject);
    }

    private /* synthetic */ void lambda$new$244(ListChangeListener.Change change) {
        while (change.next()) {
            int n2 = change.getFrom();
            int n3 = change.getTo();
            List list = change.getRemoved();
            if (n2 < n3) {
                int n4;
                int n5;
                if (list.isEmpty()) {
                    n5 = n3;
                    for (n4 = n2; n4 < n5; ++n4) {
                        this.addParagraphNode(n4, ((CharSequence)change.getList().get(n4)).toString());
                    }
                    continue;
                }
                n5 = n3;
                for (n4 = n2; n4 < n5; ++n4) {
                    Node node = (Node)this.paragraphNodes.getChildren().get(n4);
                    Text text = (Text)node;
                    text.setText(((CharSequence)change.getList().get(n4)).toString());
                }
                continue;
            }
            this.paragraphNodes.getChildren().subList(n2, n2 + list.size()).clear();
        }
    }

    private class ContentView
    extends Region {
        private ContentView() {
            this.getStyleClass().add("content");
            this.addEventHandler(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
                ((TextAreaBehavior)TextAreaSkin.this.getBehavior()).mousePressed((MouseEvent)mouseEvent);
                mouseEvent.consume();
            });
            this.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseEvent -> {
                ((TextAreaBehavior)TextAreaSkin.this.getBehavior()).mouseReleased((MouseEvent)mouseEvent);
                mouseEvent.consume();
            });
            this.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseEvent -> {
                ((TextAreaBehavior)TextAreaSkin.this.getBehavior()).mouseDragged((MouseEvent)mouseEvent);
                mouseEvent.consume();
            });
        }

        @Override
        protected ObservableList<Node> getChildren() {
            return super.getChildren();
        }

        @Override
        public Orientation getContentBias() {
            return Orientation.HORIZONTAL;
        }

        @Override
        protected double computePrefWidth(double d2) {
            if (TextAreaSkin.this.computedPrefWidth < 0.0) {
                double d3 = 0.0;
                Object object = TextAreaSkin.this.paragraphNodes.getChildren().iterator();
                while (object.hasNext()) {
                    Node node = (Node)object.next();
                    Text text = (Text)node;
                    d3 = Math.max(d3, Utils.computeTextWidth(text.getFont(), text.getText(), 0.0));
                }
                TextAreaSkin.this.computedPrefWidth = Math.max(d3 += this.snappedLeftInset() + this.snappedRightInset(), (object = TextAreaSkin.this.scrollPane.getViewportBounds()) != null ? ((Bounds)object).getWidth() : 0.0);
            }
            return TextAreaSkin.this.computedPrefWidth;
        }

        @Override
        protected double computePrefHeight(double d2) {
            if (d2 != TextAreaSkin.this.widthForComputedPrefHeight) {
                TextAreaSkin.this.invalidateMetrics();
                TextAreaSkin.this.widthForComputedPrefHeight = d2;
            }
            if (TextAreaSkin.this.computedPrefHeight < 0.0) {
                double d3 = d2 == -1.0 ? 0.0 : Math.max(d2 - (this.snappedLeftInset() + this.snappedRightInset()), 0.0);
                double d4 = 0.0;
                Object object = TextAreaSkin.this.paragraphNodes.getChildren().iterator();
                while (object.hasNext()) {
                    Node node = (Node)object.next();
                    Text text = (Text)node;
                    d4 += Utils.computeTextHeight(text.getFont(), text.getText(), d3, text.getBoundsType());
                }
                TextAreaSkin.this.computedPrefHeight = Math.max(d4 += this.snappedTopInset() + this.snappedBottomInset(), (object = TextAreaSkin.this.scrollPane.getViewportBounds()) != null ? ((Bounds)object).getHeight() : 0.0);
            }
            return TextAreaSkin.this.computedPrefHeight;
        }

        @Override
        protected double computeMinWidth(double d2) {
            if (TextAreaSkin.this.computedMinWidth < 0.0) {
                double d3 = this.snappedLeftInset() + this.snappedRightInset();
                TextAreaSkin.this.computedMinWidth = Math.min(TextAreaSkin.this.characterWidth + d3, this.computePrefWidth(d2));
            }
            return TextAreaSkin.this.computedMinWidth;
        }

        @Override
        protected double computeMinHeight(double d2) {
            if (TextAreaSkin.this.computedMinHeight < 0.0) {
                double d3 = this.snappedTopInset() + this.snappedBottomInset();
                TextAreaSkin.this.computedMinHeight = Math.min(TextAreaSkin.this.lineHeight + d3, this.computePrefHeight(d2));
            }
            return TextAreaSkin.this.computedMinHeight;
        }

        @Override
        public void layoutChildren() {
            int n2;
            Text text;
            int n3;
            int n4;
            Object object;
            TextArea textArea = (TextArea)TextAreaSkin.this.getSkinnable();
            double d2 = this.getWidth();
            double d3 = this.snappedTopInset();
            double d4 = this.snappedLeftInset();
            double d5 = Math.max(d2 - (d4 + this.snappedRightInset()), 0.0);
            double d6 = d3;
            ObservableList<Node> observableList = TextAreaSkin.this.paragraphNodes.getChildren();
            for (int i2 = 0; i2 < observableList.size(); ++i2) {
                object = (Node)observableList.get(i2);
                Text text2 = (Text)object;
                text2.setWrappingWidth(d5);
                Bounds bounds = text2.getBoundsInLocal();
                text2.setLayoutX(d4);
                text2.setLayoutY(d6);
                d6 += bounds.getHeight();
            }
            if (TextAreaSkin.this.promptNode != null) {
                TextAreaSkin.this.promptNode.setLayoutX(d4);
                TextAreaSkin.this.promptNode.setLayoutY(d3 + TextAreaSkin.this.promptNode.getBaselineOffset());
                TextAreaSkin.this.promptNode.setWrappingWidth(d5);
            }
            IndexRange indexRange = textArea.getSelection();
            object = TextAreaSkin.this.caretPath.getBoundsInParent();
            TextAreaSkin.this.selectionHighlightGroup.getChildren().clear();
            int n5 = textArea.getCaretPosition();
            int n6 = textArea.getAnchor();
            if (TextInputControlSkin.SHOW_HANDLES) {
                if (indexRange.getLength() > 0) {
                    TextAreaSkin.this.selectionHandle1.resize(TextAreaSkin.this.selectionHandle1.prefWidth(-1.0), TextAreaSkin.this.selectionHandle1.prefHeight(-1.0));
                    TextAreaSkin.this.selectionHandle2.resize(TextAreaSkin.this.selectionHandle2.prefWidth(-1.0), TextAreaSkin.this.selectionHandle2.prefHeight(-1.0));
                } else {
                    TextAreaSkin.this.caretHandle.resize(TextAreaSkin.this.caretHandle.prefWidth(-1.0), TextAreaSkin.this.caretHandle.prefHeight(-1.0));
                }
                if (indexRange.getLength() > 0) {
                    n4 = observableList.size();
                    n3 = textArea.getLength() + 1;
                    text = null;
                    while (n6 < (n3 -= (text = (Text)observableList.get(--n4)).getText().length() + 1)) {
                    }
                    TextAreaSkin.this.updateTextNodeCaretPos(n6 - n3);
                    TextAreaSkin.this.caretPath.getElements().clear();
                    TextAreaSkin.this.caretPath.getElements().addAll(text.getImpl_caretShape());
                    TextAreaSkin.this.caretPath.setLayoutX(text.getLayoutX());
                    TextAreaSkin.this.caretPath.setLayoutY(text.getLayoutY());
                    Bounds bounds = TextAreaSkin.this.caretPath.getBoundsInParent();
                    if (n5 < n6) {
                        TextAreaSkin.this.selectionHandle2.setLayoutX(bounds.getMinX() - TextAreaSkin.this.selectionHandle2.getWidth() / 2.0);
                        TextAreaSkin.this.selectionHandle2.setLayoutY(bounds.getMaxY() - 1.0);
                    } else {
                        TextAreaSkin.this.selectionHandle1.setLayoutX(bounds.getMinX() - TextAreaSkin.this.selectionHandle1.getWidth() / 2.0);
                        TextAreaSkin.this.selectionHandle1.setLayoutY(bounds.getMinY() - TextAreaSkin.this.selectionHandle1.getHeight() + 1.0);
                    }
                }
            }
            n4 = observableList.size();
            n3 = textArea.getLength() + 1;
            text = null;
            while (n5 < (n3 -= (text = (Text)observableList.get(--n4)).getText().length() + 1)) {
            }
            TextAreaSkin.this.updateTextNodeCaretPos(n5 - n3);
            TextAreaSkin.this.caretPath.getElements().clear();
            TextAreaSkin.this.caretPath.getElements().addAll(text.getImpl_caretShape());
            TextAreaSkin.this.caretPath.setLayoutX(text.getLayoutX());
            text.setLayoutX(2.0 * text.getLayoutX() - text.getBoundsInParent().getMinX());
            TextAreaSkin.this.caretPath.setLayoutY(text.getLayoutY());
            if (object == null || !object.equals(TextAreaSkin.this.caretPath.getBoundsInParent())) {
                TextAreaSkin.this.scrollCaretToVisible();
            }
            n4 = indexRange.getStart();
            n3 = indexRange.getEnd();
            int n7 = observableList.size();
            for (int i3 = 0; i3 < n7; i3 += 1) {
                Node node = (Node)observableList.get(i3);
                Text text3 = (Text)node;
                n2 = text3.getText().length() + 1;
                if (n3 > n4 && n4 < n2) {
                    text3.setImpl_selectionStart(n4);
                    text3.setImpl_selectionEnd(Math.min(n3, n2));
                    Path path = new Path();
                    path.setManaged(false);
                    path.setStroke(null);
                    PathElement[] arrpathElement = text3.getImpl_selectionShape();
                    if (arrpathElement != null) {
                        path.getElements().addAll(arrpathElement);
                    }
                    TextAreaSkin.this.selectionHighlightGroup.getChildren().add(path);
                    TextAreaSkin.this.selectionHighlightGroup.setVisible(true);
                    path.setLayoutX(text3.getLayoutX());
                    path.setLayoutY(text3.getLayoutY());
                    TextAreaSkin.this.updateHighlightFill();
                } else {
                    text3.setImpl_selectionStart(-1);
                    text3.setImpl_selectionEnd(-1);
                    TextAreaSkin.this.selectionHighlightGroup.setVisible(false);
                }
                n4 = Math.max(0, n4 - n2);
                n3 = Math.max(0, n3 - n2);
            }
            if (TextInputControlSkin.SHOW_HANDLES) {
                Bounds bounds = TextAreaSkin.this.caretPath.getBoundsInParent();
                if (indexRange.getLength() > 0) {
                    if (n5 < n6) {
                        TextAreaSkin.this.selectionHandle1.setLayoutX(bounds.getMinX() - TextAreaSkin.this.selectionHandle1.getWidth() / 2.0);
                        TextAreaSkin.this.selectionHandle1.setLayoutY(bounds.getMinY() - TextAreaSkin.this.selectionHandle1.getHeight() + 1.0);
                    } else {
                        TextAreaSkin.this.selectionHandle2.setLayoutX(bounds.getMinX() - TextAreaSkin.this.selectionHandle2.getWidth() / 2.0);
                        TextAreaSkin.this.selectionHandle2.setLayoutY(bounds.getMaxY() - 1.0);
                    }
                } else {
                    TextAreaSkin.this.caretHandle.setLayoutX(bounds.getMinX() - TextAreaSkin.this.caretHandle.getWidth() / 2.0 + 1.0);
                    TextAreaSkin.this.caretHandle.setLayoutY(bounds.getMaxY());
                }
            }
            if (TextAreaSkin.this.scrollPane.getPrefViewportWidth() == 0.0 || TextAreaSkin.this.scrollPane.getPrefViewportHeight() == 0.0) {
                TextAreaSkin.this.updatePrefViewportWidth();
                TextAreaSkin.this.updatePrefViewportHeight();
                if (this.getParent() != null && TextAreaSkin.this.scrollPane.getPrefViewportWidth() > 0.0 || TextAreaSkin.this.scrollPane.getPrefViewportHeight() > 0.0) {
                    this.getParent().requestLayout();
                }
            }
            Bounds bounds = TextAreaSkin.this.scrollPane.getViewportBounds();
            n7 = TextAreaSkin.this.scrollPane.isFitToWidth() ? 1 : 0;
            int n8 = TextAreaSkin.this.scrollPane.isFitToHeight();
            int n9 = textArea.isWrapText() || this.computePrefWidth(-1.0) <= bounds.getWidth() ? 1 : 0;
            int n10 = n2 = this.computePrefHeight(d2) <= bounds.getHeight() ? 1 : 0;
            if (n7 != n9 || n8 != n2) {
                Platform.runLater(() -> this.lambda$layoutChildren$228(n9 != 0, n2 != 0));
                this.getParent().requestLayout();
            }
        }

        private /* synthetic */ void lambda$layoutChildren$228(boolean bl, boolean bl2) {
            TextAreaSkin.this.scrollPane.setFitToWidth(bl);
            TextAreaSkin.this.scrollPane.setFitToHeight(bl2);
        }
    }
}

