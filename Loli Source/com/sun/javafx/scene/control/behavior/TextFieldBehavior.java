/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import com.sun.javafx.scene.control.behavior.TextInputControlBehavior;
import com.sun.javafx.scene.control.behavior.TwoLevelFocusBehavior;
import com.sun.javafx.scene.control.skin.TextFieldSkin;
import com.sun.javafx.scene.text.HitInfo;
import com.sun.javafx.util.Utils;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Window;

public class TextFieldBehavior
extends TextInputControlBehavior<TextField> {
    private TextFieldSkin skin;
    private ContextMenu contextMenu = new ContextMenu();
    private TwoLevelFocusBehavior tlFocus;
    private ChangeListener<Scene> sceneListener;
    private ChangeListener<Node> focusOwnerListener;
    private boolean focusGainedByMouseClick = false;
    private boolean shiftDown = false;
    private boolean deferClick = false;

    public TextFieldBehavior(TextField textField) {
        super(textField, (List<KeyBinding>)TEXT_INPUT_BINDINGS);
        if (IS_TOUCH_SUPPORTED) {
            this.contextMenu.getStyleClass().add("text-input-context-menu");
        }
        this.handleFocusChange();
        textField.focusedProperty().addListener((observableValue, bl, bl2) -> this.handleFocusChange());
        this.focusOwnerListener = (observableValue, node, node2) -> {
            if (node2 == textField) {
                if (!this.focusGainedByMouseClick) {
                    textField.selectRange(textField.getLength(), 0);
                }
            } else {
                textField.selectRange(0, 0);
            }
        };
        WeakChangeListener<Node> weakChangeListener = new WeakChangeListener<Node>(this.focusOwnerListener);
        this.sceneListener = (observableValue, scene, scene2) -> {
            if (scene != null) {
                scene.focusOwnerProperty().removeListener(weakChangeListener);
            }
            if (scene2 != null) {
                scene2.focusOwnerProperty().addListener(weakChangeListener);
            }
        };
        textField.sceneProperty().addListener(new WeakChangeListener<Scene>(this.sceneListener));
        if (textField.getScene() != null) {
            textField.getScene().focusOwnerProperty().addListener(weakChangeListener);
        }
        if (com.sun.javafx.scene.control.skin.Utils.isTwoLevelFocus()) {
            this.tlFocus = new TwoLevelFocusBehavior(textField);
        }
    }

    @Override
    public void dispose() {
        if (this.tlFocus != null) {
            this.tlFocus.dispose();
        }
        super.dispose();
    }

    private void handleFocusChange() {
        TextField textField = (TextField)this.getControl();
        if (textField.isFocused()) {
            if (PlatformUtil.isIOS()) {
                TextInputTypes textInputTypes = TextInputTypes.TEXT_FIELD;
                if (textField.getClass().equals(PasswordField.class)) {
                    textInputTypes = TextInputTypes.PASSWORD_FIELD;
                } else if (textField.getParent().getClass().equals(ComboBox.class)) {
                    textInputTypes = TextInputTypes.EDITABLE_COMBO;
                }
                Bounds bounds = textField.getBoundsInParent();
                double d2 = bounds.getWidth();
                double d3 = bounds.getHeight();
                Affine3D affine3D = TextFieldBehavior.calculateNodeToSceneTransform(textField);
                String string = textField.getText();
                textField.getScene().getWindow().impl_getPeer().requestInput(string, textInputTypes.ordinal(), d2, d3, affine3D.getMxx(), affine3D.getMxy(), affine3D.getMxz(), affine3D.getMxt(), affine3D.getMyx(), affine3D.getMyy(), affine3D.getMyz(), affine3D.getMyt(), affine3D.getMzx(), affine3D.getMzy(), affine3D.getMzz(), affine3D.getMzt());
            }
            if (!this.focusGainedByMouseClick) {
                this.setCaretAnimating(true);
            }
        } else {
            if (PlatformUtil.isIOS() && textField.getScene() != null) {
                textField.getScene().getWindow().impl_getPeer().releaseInput();
            }
            this.focusGainedByMouseClick = false;
            this.setCaretAnimating(false);
        }
    }

    static Affine3D calculateNodeToSceneTransform(Node node) {
        Affine3D affine3D = new Affine3D();
        do {
            affine3D.preConcatenate(node.impl_getLeafTransform());
        } while ((node = node.getParent()) != null);
        return affine3D;
    }

    public void setTextFieldSkin(TextFieldSkin textFieldSkin) {
        this.skin = textFieldSkin;
    }

    @Override
    protected void fire(KeyEvent keyEvent) {
        TextField textField = (TextField)this.getControl();
        EventHandler<ActionEvent> eventHandler = textField.getOnAction();
        ActionEvent actionEvent = new ActionEvent(textField, null);
        textField.fireEvent(actionEvent);
        textField.commitValue();
        if (eventHandler == null && !actionEvent.isConsumed()) {
            this.forwardToParent(keyEvent);
        }
    }

    @Override
    protected void cancelEdit(KeyEvent keyEvent) {
        TextField textField = (TextField)this.getControl();
        if (textField.getTextFormatter() != null) {
            textField.cancelEdit();
        } else {
            this.forwardToParent(keyEvent);
        }
    }

    @Override
    protected void deleteChar(boolean bl) {
        this.skin.deleteChar(bl);
    }

    @Override
    protected void replaceText(int n2, int n3, String string) {
        this.skin.replaceText(n2, n3, string);
    }

    @Override
    protected void deleteFromLineStart() {
        TextField textField = (TextField)this.getControl();
        int n2 = textField.getCaretPosition();
        if (n2 > 0) {
            this.replaceText(0, n2, "");
        }
    }

    @Override
    protected void setCaretAnimating(boolean bl) {
        if (this.skin != null) {
            this.skin.setCaretAnimating(bl);
        }
    }

    private void beep() {
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        TextField textField = (TextField)this.getControl();
        super.mousePressed(mouseEvent);
        if (!textField.isDisabled()) {
            if (!textField.isFocused()) {
                this.focusGainedByMouseClick = true;
                textField.requestFocus();
            }
            this.setCaretAnimating(false);
            if (mouseEvent.isPrimaryButtonDown() && !mouseEvent.isMiddleButtonDown() && !mouseEvent.isSecondaryButtonDown()) {
                HitInfo hitInfo = this.skin.getIndex(mouseEvent.getX(), mouseEvent.getY());
                String string = textField.textProperty().getValueSafe();
                int n2 = com.sun.javafx.scene.control.skin.Utils.getHitInsertionIndex(hitInfo, string);
                int n3 = textField.getAnchor();
                int n4 = textField.getCaretPosition();
                if (mouseEvent.getClickCount() < 2 && (IS_TOUCH_SUPPORTED || n3 != n4 && (n2 > n3 && n2 < n4 || n2 < n3 && n2 > n4))) {
                    this.deferClick = true;
                } else if (!(mouseEvent.isControlDown() || mouseEvent.isAltDown() || mouseEvent.isShiftDown() || mouseEvent.isMetaDown())) {
                    switch (mouseEvent.getClickCount()) {
                        case 1: {
                            this.mouseSingleClick(hitInfo);
                            break;
                        }
                        case 2: {
                            this.mouseDoubleClick(hitInfo);
                            break;
                        }
                        case 3: {
                            this.mouseTripleClick(hitInfo);
                            break;
                        }
                    }
                } else if (mouseEvent.isShiftDown() && !mouseEvent.isControlDown() && !mouseEvent.isAltDown() && !mouseEvent.isMetaDown() && mouseEvent.getClickCount() == 1) {
                    this.shiftDown = true;
                    if (PlatformUtil.isMac()) {
                        textField.extendSelection(n2);
                    } else {
                        this.skin.positionCaret(hitInfo, true);
                    }
                }
                this.skin.setForwardBias(hitInfo.isLeading());
            }
        }
        if (this.contextMenu.isShowing()) {
            this.contextMenu.hide();
        }
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        TextField textField = (TextField)this.getControl();
        if (!(textField.isDisabled() || this.deferClick || !mouseEvent.isPrimaryButtonDown() || mouseEvent.isMiddleButtonDown() || mouseEvent.isSecondaryButtonDown() || mouseEvent.isControlDown() || mouseEvent.isAltDown() || mouseEvent.isShiftDown() || mouseEvent.isMetaDown())) {
            this.skin.positionCaret(this.skin.getIndex(mouseEvent.getX(), mouseEvent.getY()), true);
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        TextField textField = (TextField)this.getControl();
        super.mouseReleased(mouseEvent);
        if (!textField.isDisabled()) {
            this.setCaretAnimating(false);
            if (this.deferClick) {
                this.deferClick = false;
                this.skin.positionCaret(this.skin.getIndex(mouseEvent.getX(), mouseEvent.getY()), this.shiftDown);
                this.shiftDown = false;
            }
            this.setCaretAnimating(true);
        }
    }

    @Override
    public void contextMenuRequested(ContextMenuEvent contextMenuEvent) {
        TextField textField = (TextField)this.getControl();
        if (this.contextMenu.isShowing()) {
            this.contextMenu.hide();
        } else if (textField.getContextMenu() == null) {
            Object object;
            double d2 = contextMenuEvent.getScreenX();
            double d3 = contextMenuEvent.getScreenY();
            double d4 = contextMenuEvent.getSceneX();
            if (IS_TOUCH_SUPPORTED) {
                Point2D point2D;
                if (textField.getSelection().getLength() == 0) {
                    this.skin.positionCaret(this.skin.getIndex(contextMenuEvent.getX(), contextMenuEvent.getY()), false);
                    point2D = this.skin.getMenuPosition();
                } else {
                    point2D = this.skin.getMenuPosition();
                    if (point2D != null && (point2D.getX() <= 0.0 || point2D.getY() <= 0.0)) {
                        this.skin.positionCaret(this.skin.getIndex(contextMenuEvent.getX(), contextMenuEvent.getY()), false);
                        point2D = this.skin.getMenuPosition();
                    }
                }
                if (point2D != null) {
                    Point2D point2D2 = ((TextField)this.getControl()).localToScene(point2D);
                    Scene scene = ((TextField)this.getControl()).getScene();
                    Window window = scene.getWindow();
                    object = new Point2D(window.getX() + scene.getX() + point2D2.getX(), window.getY() + scene.getY() + point2D2.getY());
                    d2 = ((Point2D)object).getX();
                    d4 = point2D2.getX();
                    d3 = ((Point2D)object).getY();
                }
            }
            this.skin.populateContextMenu(this.contextMenu);
            double d5 = this.contextMenu.prefWidth(-1.0);
            double d6 = d2 - (IS_TOUCH_SUPPORTED ? d5 / 2.0 : 0.0);
            object = Utils.getScreenForPoint(d2, 0.0);
            Rectangle2D rectangle2D = ((Screen)object).getBounds();
            if (d6 < rectangle2D.getMinX()) {
                ((TextField)this.getControl()).getProperties().put("CONTEXT_MENU_SCREEN_X", d2);
                ((TextField)this.getControl()).getProperties().put("CONTEXT_MENU_SCENE_X", d4);
                this.contextMenu.show((Node)this.getControl(), rectangle2D.getMinX(), d3);
            } else if (d2 + d5 > rectangle2D.getMaxX()) {
                double d7 = d5 - (rectangle2D.getMaxX() - d2);
                ((TextField)this.getControl()).getProperties().put("CONTEXT_MENU_SCREEN_X", d2);
                ((TextField)this.getControl()).getProperties().put("CONTEXT_MENU_SCENE_X", d4);
                this.contextMenu.show((Node)this.getControl(), d2 - d7, d3);
            } else {
                ((TextField)this.getControl()).getProperties().put("CONTEXT_MENU_SCREEN_X", 0);
                ((TextField)this.getControl()).getProperties().put("CONTEXT_MENU_SCENE_X", 0);
                this.contextMenu.show((Node)this.getControl(), d6, d3);
            }
        }
        contextMenuEvent.consume();
    }

    protected void mouseSingleClick(HitInfo hitInfo) {
        this.skin.positionCaret(hitInfo, false);
    }

    protected void mouseDoubleClick(HitInfo hitInfo) {
        TextField textField = (TextField)this.getControl();
        textField.previousWord();
        if (PlatformUtil.isWindows()) {
            textField.selectNextWord();
        } else {
            textField.selectEndOfNextWord();
        }
    }

    protected void mouseTripleClick(HitInfo hitInfo) {
        ((TextField)this.getControl()).selectAll();
    }

    static enum TextInputTypes {
        TEXT_FIELD,
        PASSWORD_FIELD,
        EDITABLE_COMBO,
        TEXT_AREA;

    }
}

