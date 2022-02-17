/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control.cell;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Cell;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;

class CellUtils {
    static int TREE_VIEW_HBOX_GRAPHIC_PADDING = 3;
    private static final StringConverter<?> defaultStringConverter = new StringConverter<Object>(){

        @Override
        public String toString(Object object) {
            return object == null ? null : object.toString();
        }

        @Override
        public Object fromString(String string) {
            return string;
        }
    };
    private static final StringConverter<?> defaultTreeItemStringConverter = new StringConverter<TreeItem<?>>(){

        @Override
        public String toString(TreeItem<?> treeItem) {
            return treeItem == null || treeItem.getValue() == null ? "" : treeItem.getValue().toString();
        }

        @Override
        public TreeItem<?> fromString(String string) {
            return new TreeItem<String>(string);
        }
    };

    CellUtils() {
    }

    static <T> StringConverter<T> defaultStringConverter() {
        return defaultStringConverter;
    }

    static <T> StringConverter<TreeItem<T>> defaultTreeItemStringConverter() {
        return defaultTreeItemStringConverter;
    }

    private static <T> String getItemText(Cell<T> cell, StringConverter<T> stringConverter) {
        return stringConverter == null ? (cell.getItem() == null ? "" : cell.getItem().toString()) : stringConverter.toString(cell.getItem());
    }

    static Node getGraphic(TreeItem<?> treeItem) {
        return treeItem == null ? null : treeItem.getGraphic();
    }

    static <T> void updateItem(Cell<T> cell, StringConverter<T> stringConverter, ChoiceBox<T> choiceBox) {
        CellUtils.updateItem(cell, stringConverter, null, null, choiceBox);
    }

    static <T> void updateItem(Cell<T> cell, StringConverter<T> stringConverter, HBox hBox, Node node, ChoiceBox<T> choiceBox) {
        if (cell.isEmpty()) {
            cell.setText(null);
            cell.setGraphic(null);
        } else if (cell.isEditing()) {
            if (choiceBox != null) {
                choiceBox.getSelectionModel().select(cell.getItem());
            }
            cell.setText(null);
            if (node != null) {
                hBox.getChildren().setAll(node, choiceBox);
                cell.setGraphic(hBox);
            } else {
                cell.setGraphic(choiceBox);
            }
        } else {
            cell.setText(CellUtils.getItemText(cell, stringConverter));
            cell.setGraphic(node);
        }
    }

    static <T> ChoiceBox<T> createChoiceBox(Cell<T> cell, ObservableList<T> observableList, ObjectProperty<StringConverter<T>> objectProperty) {
        ChoiceBox<T> choiceBox = new ChoiceBox<T>(observableList);
        choiceBox.setMaxWidth(Double.MAX_VALUE);
        choiceBox.converterProperty().bind(objectProperty);
        choiceBox.getSelectionModel().selectedItemProperty().addListener((observableValue, object, object2) -> {
            if (cell.isEditing()) {
                cell.commitEdit(object2);
            }
        });
        return choiceBox;
    }

    static <T> void updateItem(Cell<T> cell, StringConverter<T> stringConverter, TextField textField) {
        CellUtils.updateItem(cell, stringConverter, null, null, textField);
    }

    static <T> void updateItem(Cell<T> cell, StringConverter<T> stringConverter, HBox hBox, Node node, TextField textField) {
        if (cell.isEmpty()) {
            cell.setText(null);
            cell.setGraphic(null);
        } else if (cell.isEditing()) {
            if (textField != null) {
                textField.setText(CellUtils.getItemText(cell, stringConverter));
            }
            cell.setText(null);
            if (node != null) {
                hBox.getChildren().setAll(node, textField);
                cell.setGraphic(hBox);
            } else {
                cell.setGraphic(textField);
            }
        } else {
            cell.setText(CellUtils.getItemText(cell, stringConverter));
            cell.setGraphic(node);
        }
    }

    static <T> void startEdit(Cell<T> cell, StringConverter<T> stringConverter, HBox hBox, Node node, TextField textField) {
        if (textField != null) {
            textField.setText(CellUtils.getItemText(cell, stringConverter));
        }
        cell.setText(null);
        if (node != null) {
            hBox.getChildren().setAll(node, textField);
            cell.setGraphic(hBox);
        } else {
            cell.setGraphic(textField);
        }
        textField.selectAll();
        textField.requestFocus();
    }

    static <T> void cancelEdit(Cell<T> cell, StringConverter<T> stringConverter, Node node) {
        cell.setText(CellUtils.getItemText(cell, stringConverter));
        cell.setGraphic(node);
    }

    static <T> TextField createTextField(Cell<T> cell, StringConverter<T> stringConverter) {
        TextField textField = new TextField(CellUtils.getItemText(cell, stringConverter));
        textField.setOnAction(actionEvent -> {
            if (stringConverter == null) {
                throw new IllegalStateException("Attempting to convert text input into Object, but provided StringConverter is null. Be sure to set a StringConverter in your cell factory.");
            }
            cell.commitEdit(stringConverter.fromString(textField.getText()));
            actionEvent.consume();
        });
        textField.setOnKeyReleased(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ESCAPE) {
                cell.cancelEdit();
                keyEvent.consume();
            }
        });
        return textField;
    }

    static <T> void updateItem(Cell<T> cell, StringConverter<T> stringConverter, ComboBox<T> comboBox) {
        CellUtils.updateItem(cell, stringConverter, null, null, comboBox);
    }

    static <T> void updateItem(Cell<T> cell, StringConverter<T> stringConverter, HBox hBox, Node node, ComboBox<T> comboBox) {
        if (cell.isEmpty()) {
            cell.setText(null);
            cell.setGraphic(null);
        } else if (cell.isEditing()) {
            if (comboBox != null) {
                comboBox.getSelectionModel().select(cell.getItem());
            }
            cell.setText(null);
            if (node != null) {
                hBox.getChildren().setAll(node, comboBox);
                cell.setGraphic(hBox);
            } else {
                cell.setGraphic(comboBox);
            }
        } else {
            cell.setText(CellUtils.getItemText(cell, stringConverter));
            cell.setGraphic(node);
        }
    }

    static <T> ComboBox<T> createComboBox(Cell<T> cell, ObservableList<T> observableList, ObjectProperty<StringConverter<T>> objectProperty) {
        ComboBox<T> comboBox = new ComboBox<T>(observableList);
        comboBox.converterProperty().bind(objectProperty);
        comboBox.setMaxWidth(Double.MAX_VALUE);
        comboBox.getSelectionModel().selectedItemProperty().addListener((observableValue, object, object2) -> {
            if (cell.isEditing()) {
                cell.commitEdit(object2);
            }
        });
        return comboBox;
    }
}

