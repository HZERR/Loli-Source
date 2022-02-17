/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.skin.NestedTableColumnHeader;
import com.sun.javafx.scene.control.skin.TableColumnHeader;
import com.sun.javafx.scene.control.skin.TableViewSkinBase;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import com.sun.javafx.scene.control.skin.resources.ControlResources;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Skinnable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumnBase;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class TableHeaderRow
extends StackPane {
    private static final String MENU_SEPARATOR = ControlResources.getString("TableView.nestedColumnControlMenuSeparator");
    private final VirtualFlow flow;
    private final TableViewSkinBase tableSkin;
    private Map<TableColumnBase, CheckMenuItem> columnMenuItems = new HashMap<TableColumnBase, CheckMenuItem>();
    private double scrollX;
    private double tableWidth;
    private Rectangle clip;
    private TableColumnHeader reorderingRegion;
    private StackPane dragHeader;
    private final Label dragHeaderLabel = new Label();
    private final NestedTableColumnHeader header;
    private Region filler;
    private Pane cornerRegion;
    private ContextMenu columnPopupMenu;
    private BooleanProperty reordering = new SimpleBooleanProperty(this, "reordering", false){

        @Override
        protected void invalidated() {
            TableColumnHeader tableColumnHeader = TableHeaderRow.this.getReorderingRegion();
            if (tableColumnHeader != null) {
                double d2 = tableColumnHeader.getNestedColumnHeader() != null ? tableColumnHeader.getNestedColumnHeader().getHeight() : TableHeaderRow.this.getReorderingRegion().getHeight();
                TableHeaderRow.this.dragHeader.resize(TableHeaderRow.this.dragHeader.getWidth(), d2);
                TableHeaderRow.this.dragHeader.setTranslateY(TableHeaderRow.this.getHeight() - d2);
            }
            TableHeaderRow.this.dragHeader.setVisible(TableHeaderRow.this.isReordering());
        }
    };
    private InvalidationListener tableWidthListener = observable -> this.updateTableWidth();
    private InvalidationListener tablePaddingListener = observable -> this.updateTableWidth();
    private ListChangeListener visibleLeafColumnsListener = new ListChangeListener<TableColumn<?, ?>>(){

        @Override
        public void onChanged(ListChangeListener.Change<? extends TableColumn<?, ?>> change) {
            TableHeaderRow.this.header.setHeadersNeedUpdate();
        }
    };
    private final ListChangeListener tableColumnsListener = change -> {
        while (change.next()) {
            this.updateTableColumnListeners(change.getAddedSubList(), change.getRemoved());
        }
    };
    private final InvalidationListener columnTextListener = observable -> {
        TableColumnBase tableColumnBase = (TableColumnBase)((StringProperty)observable).getBean();
        CheckMenuItem checkMenuItem = this.columnMenuItems.get(tableColumnBase);
        if (checkMenuItem != null) {
            checkMenuItem.setText(this.getText(tableColumnBase.getText(), tableColumnBase));
        }
    };
    private final WeakInvalidationListener weakTableWidthListener = new WeakInvalidationListener(this.tableWidthListener);
    private final WeakInvalidationListener weakTablePaddingListener = new WeakInvalidationListener(this.tablePaddingListener);
    private final WeakListChangeListener weakVisibleLeafColumnsListener = new WeakListChangeListener(this.visibleLeafColumnsListener);
    private final WeakListChangeListener weakTableColumnsListener = new WeakListChangeListener(this.tableColumnsListener);
    private final WeakInvalidationListener weakColumnTextListener = new WeakInvalidationListener(this.columnTextListener);

    public TableHeaderRow(TableViewSkinBase tableViewSkinBase) {
        this.tableSkin = tableViewSkinBase;
        this.flow = tableViewSkinBase.flow;
        this.getStyleClass().setAll("column-header-background");
        this.clip = new Rectangle();
        this.clip.setSmooth(false);
        this.clip.heightProperty().bind(this.heightProperty());
        this.setClip(this.clip);
        this.updateTableWidth();
        ((Region)((Object)this.tableSkin.getSkinnable())).widthProperty().addListener(this.weakTableWidthListener);
        ((Region)((Object)this.tableSkin.getSkinnable())).paddingProperty().addListener(this.weakTablePaddingListener);
        tableViewSkinBase.getVisibleLeafColumns().addListener(this.weakVisibleLeafColumnsListener);
        this.columnPopupMenu = new ContextMenu();
        this.updateTableColumnListeners(this.tableSkin.getColumns(), Collections.emptyList());
        this.tableSkin.getVisibleLeafColumns().addListener(this.weakTableColumnsListener);
        this.tableSkin.getColumns().addListener(this.weakTableColumnsListener);
        this.dragHeader = new StackPane();
        this.dragHeader.setVisible(false);
        this.dragHeader.getStyleClass().setAll("column-drag-header");
        this.dragHeader.setManaged(false);
        this.dragHeader.getChildren().add(this.dragHeaderLabel);
        this.header = this.createRootHeader();
        this.header.setFocusTraversable(false);
        this.header.setTableHeaderRow(this);
        this.filler = new Region();
        this.filler.getStyleClass().setAll("filler");
        this.setOnMousePressed(mouseEvent -> ((Node)((Object)tableViewSkinBase.getSkinnable())).requestFocus());
        final StackPane stackPane = new StackPane();
        stackPane.setSnapToPixel(false);
        stackPane.getStyleClass().setAll("show-hide-column-image");
        this.cornerRegion = new StackPane(){

            @Override
            protected void layoutChildren() {
                double d2 = stackPane.snappedLeftInset() + stackPane.snappedRightInset();
                double d3 = stackPane.snappedTopInset() + stackPane.snappedBottomInset();
                stackPane.resize(d2, d3);
                this.positionInArea(stackPane, 0.0, 0.0, this.getWidth(), this.getHeight() - 3.0, 0.0, HPos.CENTER, VPos.CENTER);
            }
        };
        this.cornerRegion.getStyleClass().setAll("show-hide-columns-button");
        this.cornerRegion.getChildren().addAll(stackPane);
        this.cornerRegion.setVisible(this.tableSkin.tableMenuButtonVisibleProperty().get());
        this.tableSkin.tableMenuButtonVisibleProperty().addListener(observable -> {
            this.cornerRegion.setVisible(this.tableSkin.tableMenuButtonVisibleProperty().get());
            this.requestLayout();
        });
        this.cornerRegion.setOnMousePressed(mouseEvent -> {
            this.columnPopupMenu.show(this.cornerRegion, Side.BOTTOM, 0.0, 0.0);
            mouseEvent.consume();
        });
        this.getChildren().addAll(this.filler, this.header, this.cornerRegion, this.dragHeader);
    }

    @Override
    protected void layoutChildren() {
        double d2 = this.scrollX;
        double d3 = this.snapSize(this.header.prefWidth(-1.0));
        double d4 = this.getHeight() - this.snappedTopInset() - this.snappedBottomInset();
        double d5 = this.snapSize(this.flow.getVbar().prefWidth(-1.0));
        this.header.resizeRelocate(d2, this.snappedTopInset(), d3, d4);
        Skinnable skinnable = this.tableSkin.getSkinnable();
        if (skinnable == null) {
            return;
        }
        double d6 = ((Region)((Object)skinnable)).snappedLeftInset() + ((Region)((Object)skinnable)).snappedRightInset();
        double d7 = this.tableWidth - d3 + this.filler.getInsets().getLeft() - d6;
        this.filler.setVisible((d7 -= this.tableSkin.tableMenuButtonVisibleProperty().get() ? d5 : 0.0) > 0.0);
        if (d7 > 0.0) {
            this.filler.resizeRelocate(d2 + d3, this.snappedTopInset(), d7, d4);
        }
        this.cornerRegion.resizeRelocate(this.tableWidth - d5, this.snappedTopInset(), d5, d4);
    }

    @Override
    protected double computePrefWidth(double d2) {
        return this.header.prefWidth(d2);
    }

    @Override
    protected double computeMinHeight(double d2) {
        return this.computePrefHeight(d2);
    }

    @Override
    protected double computePrefHeight(double d2) {
        double d3 = this.header.prefHeight(d2);
        d3 = d3 == 0.0 ? 24.0 : d3;
        return this.snappedTopInset() + d3 + this.snappedBottomInset();
    }

    protected NestedTableColumnHeader createRootHeader() {
        return new NestedTableColumnHeader(this.tableSkin, null);
    }

    protected TableViewSkinBase<?, ?, ?, ?, ?, ?> getTableSkin() {
        return this.tableSkin;
    }

    protected void updateScrollX() {
        this.scrollX = this.flow.getHbar().isVisible() ? -this.flow.getHbar().getValue() : 0.0;
        this.requestLayout();
        this.layout();
    }

    public final void setReordering(boolean bl) {
        this.reordering.set(bl);
    }

    public final boolean isReordering() {
        return this.reordering.get();
    }

    public final BooleanProperty reorderingProperty() {
        return this.reordering;
    }

    public TableColumnHeader getReorderingRegion() {
        return this.reorderingRegion;
    }

    public void setReorderingColumn(TableColumnBase tableColumnBase) {
        this.dragHeaderLabel.setText(tableColumnBase == null ? "" : tableColumnBase.getText());
    }

    public void setReorderingRegion(TableColumnHeader tableColumnHeader) {
        this.reorderingRegion = tableColumnHeader;
        if (tableColumnHeader != null) {
            this.dragHeader.resize(tableColumnHeader.getWidth(), this.dragHeader.getHeight());
        }
    }

    public void setDragHeaderX(double d2) {
        this.dragHeader.setTranslateX(d2);
    }

    public NestedTableColumnHeader getRootHeader() {
        return this.header;
    }

    protected void updateTableWidth() {
        Skinnable skinnable = this.tableSkin.getSkinnable();
        if (skinnable == null) {
            this.tableWidth = 0.0;
        } else {
            Insets insets = ((Region)((Object)skinnable)).getInsets() == null ? Insets.EMPTY : ((Region)((Object)skinnable)).getInsets();
            double d2 = this.snapSize(insets.getLeft()) + this.snapSize(insets.getRight());
            this.tableWidth = this.snapSize(((Region)((Object)skinnable)).getWidth()) - d2;
        }
        this.clip.setWidth(this.tableWidth);
    }

    public TableColumnHeader getColumnHeaderFor(TableColumnBase<?, ?> tableColumnBase) {
        if (tableColumnBase == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(tableColumnBase);
        for (TableColumnBase<?, ?> tableColumnBase2 = tableColumnBase.getParentColumn(); tableColumnBase2 != null; tableColumnBase2 = tableColumnBase2.getParentColumn()) {
            arrayList.add(0, tableColumnBase2);
        }
        TableColumnHeader tableColumnHeader = this.getRootHeader();
        for (int i2 = 0; i2 < arrayList.size(); ++i2) {
            TableColumnBase tableColumnBase3 = (TableColumnBase)arrayList.get(i2);
            tableColumnHeader = this.getColumnHeaderFor(tableColumnBase3, tableColumnHeader);
        }
        return tableColumnHeader;
    }

    public TableColumnHeader getColumnHeaderFor(TableColumnBase<?, ?> tableColumnBase, TableColumnHeader tableColumnHeader) {
        if (tableColumnHeader instanceof NestedTableColumnHeader) {
            ObservableList<TableColumnHeader> observableList = ((NestedTableColumnHeader)tableColumnHeader).getColumnHeaders();
            for (int i2 = 0; i2 < observableList.size(); ++i2) {
                TableColumnHeader tableColumnHeader2 = (TableColumnHeader)observableList.get(i2);
                if (tableColumnHeader2.getTableColumn() != tableColumnBase) continue;
                return tableColumnHeader2;
            }
        }
        return null;
    }

    private void updateTableColumnListeners(List<? extends TableColumnBase<?, ?>> list, List<? extends TableColumnBase<?, ?>> list2) {
        for (TableColumnBase<?, ?> tableColumnBase : list2) {
            this.remove(tableColumnBase);
        }
        this.rebuildColumnMenu();
    }

    private void remove(TableColumnBase<?, ?> tableColumnBase) {
        if (tableColumnBase == null) {
            return;
        }
        CheckMenuItem checkMenuItem = this.columnMenuItems.remove(tableColumnBase);
        if (checkMenuItem != null) {
            tableColumnBase.textProperty().removeListener(this.weakColumnTextListener);
            checkMenuItem.selectedProperty().unbindBidirectional(tableColumnBase.visibleProperty());
            this.columnPopupMenu.getItems().remove(checkMenuItem);
        }
        if (!tableColumnBase.getColumns().isEmpty()) {
            for (TableColumnBase tableColumnBase2 : tableColumnBase.getColumns()) {
                this.remove(tableColumnBase2);
            }
        }
    }

    private void rebuildColumnMenu() {
        this.columnPopupMenu.getItems().clear();
        for (TableColumnBase tableColumnBase : this.getTableSkin().getColumns()) {
            if (tableColumnBase.getColumns().isEmpty()) {
                this.createMenuItem(tableColumnBase);
                continue;
            }
            List<TableColumnBase<?, ?>> list = this.getLeafColumns(tableColumnBase);
            for (TableColumnBase<?, ?> tableColumnBase2 : list) {
                this.createMenuItem(tableColumnBase2);
            }
        }
    }

    private List<TableColumnBase<?, ?>> getLeafColumns(TableColumnBase<?, ?> tableColumnBase) {
        ArrayList arrayList = new ArrayList();
        for (TableColumnBase tableColumnBase2 : tableColumnBase.getColumns()) {
            if (tableColumnBase2.getColumns().isEmpty()) {
                arrayList.add(tableColumnBase2);
                continue;
            }
            arrayList.addAll(this.getLeafColumns(tableColumnBase2));
        }
        return arrayList;
    }

    private void createMenuItem(TableColumnBase<?, ?> tableColumnBase) {
        CheckMenuItem checkMenuItem = this.columnMenuItems.get(tableColumnBase);
        if (checkMenuItem == null) {
            checkMenuItem = new CheckMenuItem();
            this.columnMenuItems.put(tableColumnBase, checkMenuItem);
        }
        checkMenuItem.setText(this.getText(tableColumnBase.getText(), tableColumnBase));
        tableColumnBase.textProperty().addListener(this.weakColumnTextListener);
        checkMenuItem.selectedProperty().bindBidirectional(tableColumnBase.visibleProperty());
        this.columnPopupMenu.getItems().add(checkMenuItem);
    }

    private String getText(String string, TableColumnBase tableColumnBase) {
        String string2 = string;
        for (TableColumnBase tableColumnBase2 = tableColumnBase.getParentColumn(); tableColumnBase2 != null; tableColumnBase2 = tableColumnBase2.getParentColumn()) {
            if (!this.isColumnVisibleInHeader(tableColumnBase2, this.tableSkin.getColumns())) continue;
            string2 = tableColumnBase2.getText() + MENU_SEPARATOR + string2;
        }
        return string2;
    }

    private boolean isColumnVisibleInHeader(TableColumnBase tableColumnBase, List list) {
        if (tableColumnBase == null) {
            return false;
        }
        for (int i2 = 0; i2 < list.size(); ++i2) {
            boolean bl;
            TableColumnBase tableColumnBase2 = (TableColumnBase)list.get(i2);
            if (tableColumnBase.equals(tableColumnBase2)) {
                return true;
            }
            if (tableColumnBase2.getColumns().isEmpty() || !(bl = this.isColumnVisibleInHeader(tableColumnBase, tableColumnBase2.getColumns()))) continue;
            return true;
        }
        return false;
    }
}

