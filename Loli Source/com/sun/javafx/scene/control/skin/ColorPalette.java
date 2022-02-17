/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.skin.ColorPickerSkin;
import com.sun.javafx.scene.control.skin.CustomColorDialog;
import com.sun.javafx.scene.control.skin.Utils;
import com.sun.javafx.scene.traversal.Algorithm;
import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.scene.traversal.ParentTraversalEngine;
import com.sun.javafx.scene.traversal.TraversalContext;
import java.util.Iterator;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PopupControl;
import javafx.scene.control.Separator;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

public class ColorPalette
extends Region {
    private static final int SQUARE_SIZE = 15;
    ColorPickerGrid colorPickerGrid;
    final Hyperlink customColorLink = new Hyperlink(ColorPickerSkin.getString("customColorLink"));
    CustomColorDialog customColorDialog = null;
    private ColorPicker colorPicker;
    private final GridPane customColorGrid = new GridPane();
    private final Separator separator = new Separator();
    private final Label customColorLabel = new Label(ColorPickerSkin.getString("customColorLabel"));
    private PopupControl popupControl;
    private ColorSquare focusedSquare;
    private ContextMenu contextMenu = null;
    private Color mouseDragColor = null;
    private boolean dragDetected = false;
    private int customColorNumber = 0;
    private int customColorRows = 0;
    private int customColorLastRowLength = 0;
    private final ColorSquare hoverSquare = new ColorSquare();
    private static final int NUM_OF_COLUMNS = 12;
    private static double[] RAW_VALUES = new double[]{255.0, 255.0, 255.0, 242.0, 242.0, 242.0, 230.0, 230.0, 230.0, 204.0, 204.0, 204.0, 179.0, 179.0, 179.0, 153.0, 153.0, 153.0, 128.0, 128.0, 128.0, 102.0, 102.0, 102.0, 77.0, 77.0, 77.0, 51.0, 51.0, 51.0, 26.0, 26.0, 26.0, 0.0, 0.0, 0.0, 0.0, 51.0, 51.0, 0.0, 26.0, 128.0, 26.0, 0.0, 104.0, 51.0, 0.0, 51.0, 77.0, 0.0, 26.0, 153.0, 0.0, 0.0, 153.0, 51.0, 0.0, 153.0, 77.0, 0.0, 153.0, 102.0, 0.0, 153.0, 153.0, 0.0, 102.0, 102.0, 0.0, 0.0, 51.0, 0.0, 26.0, 77.0, 77.0, 26.0, 51.0, 153.0, 51.0, 26.0, 128.0, 77.0, 26.0, 77.0, 102.0, 26.0, 51.0, 179.0, 26.0, 26.0, 179.0, 77.0, 26.0, 179.0, 102.0, 26.0, 179.0, 128.0, 26.0, 179.0, 179.0, 26.0, 128.0, 128.0, 26.0, 26.0, 77.0, 26.0, 51.0, 102.0, 102.0, 51.0, 77.0, 179.0, 77.0, 51.0, 153.0, 102.0, 51.0, 102.0, 128.0, 51.0, 77.0, 204.0, 51.0, 51.0, 204.0, 102.0, 51.0, 204.0, 128.0, 51.0, 204.0, 153.0, 51.0, 204.0, 204.0, 51.0, 153.0, 153.0, 51.0, 51.0, 102.0, 51.0, 77.0, 128.0, 128.0, 77.0, 102.0, 204.0, 102.0, 77.0, 179.0, 128.0, 77.0, 128.0, 153.0, 77.0, 102.0, 230.0, 77.0, 77.0, 230.0, 128.0, 77.0, 230.0, 153.0, 77.0, 230.0, 179.0, 77.0, 230.0, 230.0, 77.0, 179.0, 179.0, 77.0, 77.0, 128.0, 77.0, 102.0, 153.0, 153.0, 102.0, 128.0, 230.0, 128.0, 102.0, 204.0, 153.0, 102.0, 153.0, 179.0, 102.0, 128.0, 255.0, 102.0, 102.0, 255.0, 153.0, 102.0, 255.0, 179.0, 102.0, 255.0, 204.0, 102.0, 255.0, 255.0, 77.0, 204.0, 204.0, 102.0, 102.0, 153.0, 102.0, 128.0, 179.0, 179.0, 128.0, 153.0, 255.0, 153.0, 128.0, 230.0, 179.0, 128.0, 179.0, 204.0, 128.0, 153.0, 255.0, 128.0, 128.0, 255.0, 153.0, 128.0, 255.0, 204.0, 128.0, 255.0, 230.0, 102.0, 255.0, 255.0, 102.0, 230.0, 230.0, 128.0, 128.0, 179.0, 128.0, 153.0, 204.0, 204.0, 153.0, 179.0, 255.0, 179.0, 153.0, 255.0, 204.0, 153.0, 204.0, 230.0, 153.0, 179.0, 255.0, 153.0, 153.0, 255.0, 179.0, 128.0, 255.0, 204.0, 153.0, 255.0, 230.0, 128.0, 255.0, 255.0, 128.0, 230.0, 230.0, 153.0, 153.0, 204.0, 153.0, 179.0, 230.0, 230.0, 179.0, 204.0, 255.0, 204.0, 179.0, 255.0, 230.0, 179.0, 230.0, 230.0, 179.0, 204.0, 255.0, 179.0, 179.0, 255.0, 179.0, 153.0, 255.0, 230.0, 179.0, 255.0, 230.0, 153.0, 255.0, 255.0, 153.0, 230.0, 230.0, 179.0, 179.0, 230.0, 179.0, 204.0, 255.0, 255.0, 204.0, 230.0, 255.0, 230.0, 204.0, 255.0, 255.0, 204.0, 255.0, 255.0, 204.0, 230.0, 255.0, 204.0, 204.0, 255.0, 204.0, 179.0, 255.0, 230.0, 204.0, 255.0, 255.0, 179.0, 255.0, 255.0, 204.0, 230.0, 230.0, 204.0, 204.0, 255.0, 204.0};
    private static final int NUM_OF_COLORS = RAW_VALUES.length / 3;
    private static final int NUM_OF_ROWS = NUM_OF_COLORS / 12;

    public ColorPalette(final ColorPicker colorPicker) {
        this.getStyleClass().add("color-palette-region");
        this.colorPicker = colorPicker;
        this.colorPickerGrid = new ColorPickerGrid();
        ((Node)this.colorPickerGrid.getChildren().get(0)).requestFocus();
        this.customColorLabel.setAlignment(Pos.CENTER_LEFT);
        this.customColorLink.setPrefWidth(this.colorPickerGrid.prefWidth(-1.0));
        this.customColorLink.setAlignment(Pos.CENTER);
        this.customColorLink.setFocusTraversable(true);
        this.customColorLink.setVisited(true);
        this.customColorLink.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent actionEvent) {
                if (ColorPalette.this.customColorDialog == null) {
                    ColorPalette.this.customColorDialog = new CustomColorDialog(ColorPalette.this.popupControl);
                    ColorPalette.this.customColorDialog.customColorProperty().addListener((observableValue, color, color2) -> colorPicker.setValue(ColorPalette.this.customColorDialog.customColorProperty().get()));
                    ColorPalette.this.customColorDialog.setOnSave(() -> {
                        Color color = (Color)ColorPalette.this.customColorDialog.customColorProperty().get();
                        ColorPalette.this.buildCustomColors();
                        colorPicker.getCustomColors().add(color);
                        ColorPalette.this.updateSelection(color);
                        Event.fireEvent(colorPicker, new ActionEvent());
                        colorPicker.hide();
                    });
                    ColorPalette.this.customColorDialog.setOnUse(() -> {
                        Event.fireEvent(colorPicker, new ActionEvent());
                        colorPicker.hide();
                    });
                }
                ColorPalette.this.customColorDialog.setCurrentColor((Color)colorPicker.valueProperty().get());
                if (ColorPalette.this.popupControl != null) {
                    ColorPalette.this.popupControl.setAutoHide(false);
                }
                ColorPalette.this.customColorDialog.show();
                ColorPalette.this.customColorDialog.setOnHidden(windowEvent -> {
                    if (ColorPalette.this.popupControl != null) {
                        ColorPalette.this.popupControl.setAutoHide(true);
                    }
                });
            }
        });
        this.initNavigation();
        this.customColorGrid.getStyleClass().add("color-picker-grid");
        this.customColorGrid.setVisible(false);
        this.buildCustomColors();
        colorPicker.getCustomColors().addListener(new ListChangeListener<Color>(){

            @Override
            public void onChanged(ListChangeListener.Change<? extends Color> change) {
                ColorPalette.this.buildCustomColors();
            }
        });
        VBox vBox = new VBox();
        vBox.getStyleClass().add("color-palette");
        vBox.getChildren().addAll(this.colorPickerGrid, this.customColorLabel, this.customColorGrid, this.separator, this.customColorLink);
        this.hoverSquare.setMouseTransparent(true);
        this.hoverSquare.getStyleClass().addAll("hover-square");
        this.setFocusedSquare(null);
        this.getChildren().addAll(vBox, this.hoverSquare);
    }

    private void setFocusedSquare(ColorSquare colorSquare) {
        double d2;
        double d3;
        if (colorSquare == this.focusedSquare) {
            return;
        }
        this.focusedSquare = colorSquare;
        this.hoverSquare.setVisible(this.focusedSquare != null);
        if (this.focusedSquare == null) {
            return;
        }
        if (!this.focusedSquare.isFocused()) {
            this.focusedSquare.requestFocus();
        }
        this.hoverSquare.rectangle.setFill(this.focusedSquare.rectangle.getFill());
        Bounds bounds = colorSquare.localToScene(colorSquare.getLayoutBounds());
        double d4 = bounds.getMinX();
        double d5 = bounds.getMinY();
        double d6 = d3 = this.hoverSquare.getScaleX() == 1.0 ? 0.0 : this.hoverSquare.getWidth() / 4.0;
        if (this.colorPicker.getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
            d4 = this.focusedSquare.getLayoutX();
            d2 = -this.focusedSquare.getWidth() + d3;
        } else {
            d2 = this.focusedSquare.getWidth() / 2.0 + d3;
        }
        this.hoverSquare.setLayoutX(this.snapPosition(d4) - d2);
        this.hoverSquare.setLayoutY(this.snapPosition(d5) - this.focusedSquare.getHeight() / 2.0 + (this.hoverSquare.getScaleY() == 1.0 ? 0.0 : this.focusedSquare.getHeight() / 4.0));
    }

    private void buildCustomColors() {
        Object object;
        int n2;
        ObservableList<Color> observableList = this.colorPicker.getCustomColors();
        this.customColorNumber = observableList.size();
        this.customColorGrid.getChildren().clear();
        if (observableList.isEmpty()) {
            this.customColorLabel.setVisible(false);
            this.customColorLabel.setManaged(false);
            this.customColorGrid.setVisible(false);
            this.customColorGrid.setManaged(false);
            return;
        }
        this.customColorLabel.setVisible(true);
        this.customColorLabel.setManaged(true);
        this.customColorGrid.setVisible(true);
        this.customColorGrid.setManaged(true);
        if (this.contextMenu == null) {
            MenuItem menuItem = new MenuItem(ColorPickerSkin.getString("removeColor"));
            menuItem.setOnAction(actionEvent -> {
                ColorSquare colorSquare = (ColorSquare)this.contextMenu.getOwnerNode();
                observableList.remove(colorSquare.rectangle.getFill());
                this.buildCustomColors();
            });
            this.contextMenu = new ContextMenu(menuItem);
        }
        int n3 = 0;
        int n4 = 0;
        int n5 = observableList.size() % 12;
        int n6 = n5 == 0 ? 0 : 12 - n5;
        this.customColorLastRowLength = n5 == 0 ? 12 : n5;
        for (n2 = 0; n2 < observableList.size(); ++n2) {
            object = (Color)observableList.get(n2);
            ColorSquare colorSquare = new ColorSquare((Color)object, n2, true);
            colorSquare.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
                if (keyEvent.getCode() == KeyCode.DELETE) {
                    observableList.remove(colorSquare.rectangle.getFill());
                    this.buildCustomColors();
                }
            });
            this.customColorGrid.add(colorSquare, n3, n4);
            if (++n3 != 12) continue;
            n3 = 0;
            ++n4;
        }
        for (n2 = 0; n2 < n6; ++n2) {
            object = new ColorSquare();
            this.customColorGrid.add((Node)object, n3, n4);
            ++n3;
        }
        this.customColorRows = n4 + 1;
        this.requestLayout();
    }

    private void initNavigation() {
        this.setOnKeyPressed(keyEvent -> {
            switch (keyEvent.getCode()) {
                case SPACE: 
                case ENTER: {
                    this.processSelectKey((KeyEvent)keyEvent);
                    keyEvent.consume();
                    break;
                }
            }
        });
        this.setImpl_traversalEngine(new ParentTraversalEngine(this, new Algorithm(){

            @Override
            public Node select(Node node, Direction direction, TraversalContext traversalContext) {
                Node node2 = traversalContext.selectInSubtree(traversalContext.getRoot(), node, direction);
                switch (direction) {
                    case NEXT: 
                    case NEXT_IN_LINE: 
                    case PREVIOUS: {
                        return node2;
                    }
                    case LEFT: 
                    case RIGHT: 
                    case UP: 
                    case DOWN: {
                        if (node instanceof ColorSquare) {
                            Node node3 = this.processArrow((ColorSquare)node, direction);
                            return node3 != null ? node3 : node2;
                        }
                        return node2;
                    }
                }
                return null;
            }

            private Node processArrow(ColorSquare colorSquare, Direction direction) {
                int n2 = colorSquare.index / 12;
                int n3 = colorSquare.index % 12;
                if (this.isAtBorder(direction = direction.getDirectionForNodeOrientation(ColorPalette.this.colorPicker.getEffectiveNodeOrientation()), n2, n3, colorSquare.isCustom)) {
                    int n4 = n2;
                    int n5 = n3;
                    boolean bl = colorSquare.isCustom;
                    switch (direction) {
                        case LEFT: 
                        case RIGHT: {
                            if (colorSquare.isCustom) {
                                n4 = Math.floorMod(direction == Direction.LEFT ? n2 - 1 : n2 + 1, ColorPalette.this.customColorRows);
                                n5 = direction == Direction.LEFT ? (n4 == ColorPalette.this.customColorRows - 1 ? ColorPalette.this.customColorLastRowLength - 1 : 11) : 0;
                                break;
                            }
                            n4 = Math.floorMod(direction == Direction.LEFT ? n2 - 1 : n2 + 1, NUM_OF_ROWS);
                            n5 = direction == Direction.LEFT ? 11 : 0;
                            break;
                        }
                        case UP: {
                            n4 = NUM_OF_ROWS - 1;
                            break;
                        }
                        case DOWN: {
                            if (ColorPalette.this.customColorNumber > 0) {
                                bl = true;
                                n4 = 0;
                                n5 = ColorPalette.this.customColorRows > 1 ? n3 : Math.min(ColorPalette.this.customColorLastRowLength - 1, n3);
                                break;
                            }
                            return null;
                        }
                    }
                    if (bl) {
                        return (Node)ColorPalette.this.customColorGrid.getChildren().get(n4 * 12 + n5);
                    }
                    return (Node)ColorPalette.this.colorPickerGrid.getChildren().get(n4 * 12 + n5);
                }
                return null;
            }

            private boolean isAtBorder(Direction direction, int n2, int n3, boolean bl) {
                switch (direction) {
                    case LEFT: {
                        return n3 == 0;
                    }
                    case RIGHT: {
                        return bl && n2 == ColorPalette.this.customColorRows - 1 ? n3 == ColorPalette.this.customColorLastRowLength - 1 : n3 == 11;
                    }
                    case UP: {
                        return !bl && n2 == 0;
                    }
                    case DOWN: {
                        return !bl && n2 == NUM_OF_ROWS - 1;
                    }
                }
                return false;
            }

            @Override
            public Node selectFirst(TraversalContext traversalContext) {
                return (Node)ColorPalette.this.colorPickerGrid.getChildren().get(0);
            }

            @Override
            public Node selectLast(TraversalContext traversalContext) {
                return ColorPalette.this.customColorLink;
            }
        }));
    }

    private void processSelectKey(KeyEvent keyEvent) {
        if (this.focusedSquare != null) {
            this.focusedSquare.selectColor(keyEvent);
        }
    }

    public void setPopupControl(PopupControl popupControl) {
        this.popupControl = popupControl;
    }

    public ColorPickerGrid getColorGrid() {
        return this.colorPickerGrid;
    }

    public boolean isCustomColorDialogShowing() {
        if (this.customColorDialog != null) {
            return this.customColorDialog.isVisible();
        }
        return false;
    }

    public void updateSelection(Color color) {
        this.setFocusedSquare(null);
        for (Node node : this.colorPickerGrid.getSquares()) {
            if (!node.rectangle.getFill().equals(color)) continue;
            this.setFocusedSquare((ColorSquare)node);
            return;
        }
        Iterator<ColorSquare> iterator = this.customColorGrid.getChildren().iterator();
        while (iterator.hasNext()) {
            Node node;
            Node node2 = node = (Node)iterator.next();
            if (!node2.rectangle.getFill().equals(color)) continue;
            this.setFocusedSquare((ColorSquare)node2);
            return;
        }
    }

    class ColorPickerGrid
    extends GridPane {
        private final List<ColorSquare> squares;

        public ColorPickerGrid() {
            this.getStyleClass().add("color-picker-grid");
            this.setId("ColorCustomizerColorGrid");
            int n2 = 0;
            int n3 = 0;
            this.squares = FXCollections.observableArrayList();
            int n4 = RAW_VALUES.length / 3;
            Color[] arrcolor = new Color[n4];
            for (int i2 = 0; i2 < n4; ++i2) {
                arrcolor[i2] = new Color(RAW_VALUES[i2 * 3] / 255.0, RAW_VALUES[i2 * 3 + 1] / 255.0, RAW_VALUES[i2 * 3 + 2] / 255.0, 1.0);
                ColorSquare colorSquare = new ColorSquare(arrcolor[i2], i2);
                this.squares.add(colorSquare);
            }
            for (ColorSquare colorSquare : this.squares) {
                this.add(colorSquare, n2, n3);
                if (++n2 != 12) continue;
                n2 = 0;
                ++n3;
            }
            this.setOnMouseDragged(mouseEvent -> {
                if (!ColorPalette.this.dragDetected) {
                    ColorPalette.this.dragDetected = true;
                    ColorPalette.this.mouseDragColor = (Color)ColorPalette.this.colorPicker.getValue();
                }
                int n2 = com.sun.javafx.util.Utils.clamp(0, (int)mouseEvent.getX() / 16, 11);
                int n3 = com.sun.javafx.util.Utils.clamp(0, (int)mouseEvent.getY() / 16, NUM_OF_ROWS - 1);
                int n4 = n2 + n3 * 12;
                ColorPalette.this.colorPicker.setValue((Color)this.squares.get((int)n4).rectangle.getFill());
                ColorPalette.this.updateSelection((Color)ColorPalette.this.colorPicker.getValue());
            });
            this.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseEvent -> {
                if (ColorPalette.this.colorPickerGrid.getBoundsInLocal().contains(mouseEvent.getX(), mouseEvent.getY())) {
                    ColorPalette.this.updateSelection((Color)ColorPalette.this.colorPicker.getValue());
                    ColorPalette.this.colorPicker.fireEvent(new ActionEvent());
                    ColorPalette.this.colorPicker.hide();
                } else if (ColorPalette.this.mouseDragColor != null) {
                    ColorPalette.this.colorPicker.setValue(ColorPalette.this.mouseDragColor);
                    ColorPalette.this.updateSelection(ColorPalette.this.mouseDragColor);
                }
                ColorPalette.this.dragDetected = false;
            });
        }

        public List<ColorSquare> getSquares() {
            return this.squares;
        }

        @Override
        protected double computePrefWidth(double d2) {
            return 192.0;
        }

        @Override
        protected double computePrefHeight(double d2) {
            return 16 * NUM_OF_ROWS;
        }
    }

    class ColorSquare
    extends StackPane {
        Rectangle rectangle;
        int index;
        boolean isEmpty;
        boolean isCustom;

        public ColorSquare() {
            this(null, -1, false);
        }

        public ColorSquare(Color color, int n2) {
            this(color, n2, false);
        }

        public ColorSquare(Color color, int n2, boolean bl3) {
            this.getStyleClass().add("color-square");
            if (color != null) {
                this.setFocusTraversable(true);
                this.focusedProperty().addListener((observableValue, bl, bl2) -> ColorPalette.this.setFocusedSquare(bl2 != false ? this : null));
                this.addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEvent -> ColorPalette.this.setFocusedSquare(this));
                this.addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent -> ColorPalette.this.setFocusedSquare(null));
                this.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseEvent -> {
                    if (!ColorPalette.this.dragDetected && mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 1) {
                        if (!this.isEmpty) {
                            Color color = (Color)this.rectangle.getFill();
                            ColorPalette.this.colorPicker.setValue(color);
                            ColorPalette.this.colorPicker.fireEvent(new ActionEvent());
                            ColorPalette.this.updateSelection(color);
                            mouseEvent.consume();
                        }
                        ColorPalette.this.colorPicker.hide();
                    } else if ((mouseEvent.getButton() == MouseButton.SECONDARY || mouseEvent.getButton() == MouseButton.MIDDLE) && bl3 && ColorPalette.this.contextMenu != null) {
                        if (!ColorPalette.this.contextMenu.isShowing()) {
                            ColorPalette.this.contextMenu.show(this, Side.RIGHT, 0.0, 0.0);
                            Utils.addMnemonics(ColorPalette.this.contextMenu, this.getScene(), ColorPalette.this.colorPicker.impl_isShowMnemonics());
                        } else {
                            ColorPalette.this.contextMenu.hide();
                            Utils.removeMnemonics(ColorPalette.this.contextMenu, this.getScene());
                        }
                    }
                });
            }
            this.index = n2;
            this.isCustom = bl3;
            this.rectangle = new Rectangle(15.0, 15.0);
            if (color == null) {
                this.rectangle.setFill(Color.WHITE);
                this.isEmpty = true;
            } else {
                this.rectangle.setFill(color);
            }
            this.rectangle.setStrokeType(StrokeType.INSIDE);
            String string = ColorPickerSkin.tooltipString(color);
            Tooltip.install(this, new Tooltip(string == null ? "" : string));
            this.rectangle.getStyleClass().add("color-rect");
            this.getChildren().add(this.rectangle);
        }

        public void selectColor(KeyEvent keyEvent) {
            if (this.rectangle.getFill() != null) {
                if (this.rectangle.getFill() instanceof Color) {
                    ColorPalette.this.colorPicker.setValue((Color)this.rectangle.getFill());
                    ColorPalette.this.colorPicker.fireEvent(new ActionEvent());
                }
                keyEvent.consume();
            }
            ColorPalette.this.colorPicker.hide();
        }
    }
}

