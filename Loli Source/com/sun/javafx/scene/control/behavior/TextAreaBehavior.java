/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import com.sun.javafx.scene.control.behavior.TextFieldBehavior;
import com.sun.javafx.scene.control.behavior.TextInputControlBehavior;
import com.sun.javafx.scene.control.behavior.TextInputControlBindings;
import com.sun.javafx.scene.control.behavior.TwoLevelFocusBehavior;
import com.sun.javafx.scene.control.skin.TextAreaSkin;
import com.sun.javafx.scene.text.HitInfo;
import com.sun.javafx.util.Utils;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TextArea;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Window;

public class TextAreaBehavior
extends TextInputControlBehavior<TextArea> {
    protected static final List<KeyBinding> TEXT_AREA_BINDINGS = new ArrayList<KeyBinding>();
    private TextAreaSkin skin;
    private ContextMenu contextMenu = new ContextMenu();
    private TwoLevelFocusBehavior tlFocus;
    private boolean focusGainedByMouseClick = false;
    private boolean shiftDown = false;
    private boolean deferClick = false;

    public TextAreaBehavior(TextArea textArea) {
        super(textArea, TEXT_AREA_BINDINGS);
        if (IS_TOUCH_SUPPORTED) {
            this.contextMenu.getStyleClass().add("text-input-context-menu");
        }
        textArea.focusedProperty().addListener(new ChangeListener<Boolean>(){

            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean bl, Boolean bl2) {
                TextArea textArea = (TextArea)TextAreaBehavior.this.getControl();
                if (textArea.isFocused()) {
                    if (PlatformUtil.isIOS()) {
                        Bounds bounds = textArea.getBoundsInParent();
                        double d2 = bounds.getWidth();
                        double d3 = bounds.getHeight();
                        Affine3D affine3D = TextFieldBehavior.calculateNodeToSceneTransform(textArea);
                        String string = textArea.textProperty().getValueSafe();
                        textArea.getScene().getWindow().impl_getPeer().requestInput(string, TextFieldBehavior.TextInputTypes.TEXT_AREA.ordinal(), d2, d3, affine3D.getMxx(), affine3D.getMxy(), affine3D.getMxz(), affine3D.getMxt(), affine3D.getMyx(), affine3D.getMyy(), affine3D.getMyz(), affine3D.getMyt(), affine3D.getMzx(), affine3D.getMzy(), affine3D.getMzz(), affine3D.getMzt());
                    }
                    if (!TextAreaBehavior.this.focusGainedByMouseClick) {
                        TextAreaBehavior.this.setCaretAnimating(true);
                    }
                } else {
                    if (PlatformUtil.isIOS() && textArea.getScene() != null) {
                        textArea.getScene().getWindow().impl_getPeer().releaseInput();
                    }
                    TextAreaBehavior.this.focusGainedByMouseClick = false;
                    TextAreaBehavior.this.setCaretAnimating(false);
                }
            }
        });
        if (com.sun.javafx.scene.control.skin.Utils.isTwoLevelFocus()) {
            this.tlFocus = new TwoLevelFocusBehavior(textArea);
        }
    }

    @Override
    public void dispose() {
        if (this.tlFocus != null) {
            this.tlFocus.dispose();
        }
        super.dispose();
    }

    public void setTextAreaSkin(TextAreaSkin textAreaSkin) {
        this.skin = textAreaSkin;
    }

    @Override
    public void callAction(String string) {
        TextArea textArea = (TextArea)this.getControl();
        boolean bl = false;
        if (textArea.isEditable()) {
            this.setEditing(true);
            bl = true;
            if ("InsertNewLine".equals(string)) {
                this.insertNewLine();
            } else if ("TraverseOrInsertTab".equals(string)) {
                this.insertTab();
            } else {
                bl = false;
            }
            this.setEditing(false);
        }
        if (!bl) {
            bl = true;
            if ("LineStart".equals(string)) {
                this.lineStart(false, false);
            } else if ("LineEnd".equals(string)) {
                this.lineEnd(false, false);
            } else if ("SelectLineStart".equals(string)) {
                this.lineStart(true, false);
            } else if ("SelectLineStartExtend".equals(string)) {
                this.lineStart(true, true);
            } else if ("SelectLineEnd".equals(string)) {
                this.lineEnd(true, false);
            } else if ("SelectLineEndExtend".equals(string)) {
                this.lineEnd(true, true);
            } else if ("PreviousLine".equals(string)) {
                this.skin.previousLine(false);
            } else if ("NextLine".equals(string)) {
                this.skin.nextLine(false);
            } else if ("SelectPreviousLine".equals(string)) {
                this.skin.previousLine(true);
            } else if ("SelectNextLine".equals(string)) {
                this.skin.nextLine(true);
            } else if ("ParagraphStart".equals(string)) {
                this.skin.paragraphStart(true, false);
            } else if ("ParagraphEnd".equals(string)) {
                this.skin.paragraphEnd(true, PlatformUtil.isWindows(), false);
            } else if ("SelectParagraphStart".equals(string)) {
                this.skin.paragraphStart(true, true);
            } else if ("SelectParagraphEnd".equals(string)) {
                this.skin.paragraphEnd(true, PlatformUtil.isWindows(), true);
            } else if ("PreviousPage".equals(string)) {
                this.skin.previousPage(false);
            } else if ("NextPage".equals(string)) {
                this.skin.nextPage(false);
            } else if ("SelectPreviousPage".equals(string)) {
                this.skin.previousPage(true);
            } else if ("SelectNextPage".equals(string)) {
                this.skin.nextPage(true);
            } else if ("TraverseOrInsertTab".equals(string)) {
                string = "TraverseNext";
                bl = false;
            } else {
                bl = false;
            }
        }
        if (!bl) {
            super.callAction(string);
        }
    }

    private void insertNewLine() {
        TextArea textArea = (TextArea)this.getControl();
        textArea.replaceSelection("\n");
    }

    private void insertTab() {
        TextArea textArea = (TextArea)this.getControl();
        textArea.replaceSelection("\t");
    }

    @Override
    protected void deleteChar(boolean bl) {
        this.skin.deleteChar(bl);
    }

    @Override
    protected void deleteFromLineStart() {
        TextArea textArea = (TextArea)this.getControl();
        int n2 = textArea.getCaretPosition();
        if (n2 > 0) {
            this.lineStart(false, false);
            int n3 = textArea.getCaretPosition();
            if (n2 > n3) {
                this.replaceText(n3, n2, "");
            }
        }
    }

    private void lineStart(boolean bl, boolean bl2) {
        this.skin.lineStart(bl, bl2);
    }

    private void lineEnd(boolean bl, boolean bl2) {
        this.skin.lineEnd(bl, bl2);
    }

    @Override
    protected void scrollCharacterToVisible(int n2) {
        this.skin.scrollCharacterToVisible(n2);
    }

    @Override
    protected void replaceText(int n2, int n3, String string) {
        ((TextArea)this.getControl()).replaceText(n2, n3, string);
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        TextArea textArea = (TextArea)this.getControl();
        super.mousePressed(mouseEvent);
        if (!textArea.isDisabled()) {
            if (!textArea.isFocused()) {
                this.focusGainedByMouseClick = true;
                textArea.requestFocus();
            }
            this.setCaretAnimating(false);
            if (mouseEvent.getButton() == MouseButton.PRIMARY && !mouseEvent.isMiddleButtonDown() && !mouseEvent.isSecondaryButtonDown()) {
                HitInfo hitInfo = this.skin.getIndex(mouseEvent.getX(), mouseEvent.getY());
                int n2 = com.sun.javafx.scene.control.skin.Utils.getHitInsertionIndex(hitInfo, textArea.textProperty().getValueSafe());
                int n3 = textArea.getAnchor();
                int n4 = textArea.getCaretPosition();
                if (mouseEvent.getClickCount() < 2 && (mouseEvent.isSynthesized() || n3 != n4 && (n2 > n3 && n2 < n4 || n2 < n3 && n2 > n4))) {
                    this.deferClick = true;
                } else if (!(mouseEvent.isControlDown() || mouseEvent.isAltDown() || mouseEvent.isShiftDown() || mouseEvent.isMetaDown() || mouseEvent.isShortcutDown())) {
                    switch (mouseEvent.getClickCount()) {
                        case 1: {
                            this.skin.positionCaret(hitInfo, false, false);
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
                } else if (!(!mouseEvent.isShiftDown() || mouseEvent.isControlDown() || mouseEvent.isAltDown() || mouseEvent.isMetaDown() || mouseEvent.isShortcutDown() || mouseEvent.getClickCount() != 1)) {
                    this.shiftDown = true;
                    if (PlatformUtil.isMac()) {
                        textArea.extendSelection(n2);
                    } else {
                        this.skin.positionCaret(hitInfo, true, false);
                    }
                }
            }
            if (this.contextMenu.isShowing()) {
                this.contextMenu.hide();
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        TextArea textArea = (TextArea)this.getControl();
        if (!(textArea.isDisabled() || mouseEvent.isSynthesized() || mouseEvent.getButton() != MouseButton.PRIMARY || mouseEvent.isMiddleButtonDown() || mouseEvent.isSecondaryButtonDown() || mouseEvent.isControlDown() || mouseEvent.isAltDown() || mouseEvent.isShiftDown() || mouseEvent.isMetaDown())) {
            this.skin.positionCaret(this.skin.getIndex(mouseEvent.getX(), mouseEvent.getY()), true, false);
        }
        this.deferClick = false;
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        TextArea textArea = (TextArea)this.getControl();
        super.mouseReleased(mouseEvent);
        if (!textArea.isDisabled()) {
            this.setCaretAnimating(false);
            if (this.deferClick) {
                this.deferClick = false;
                this.skin.positionCaret(this.skin.getIndex(mouseEvent.getX(), mouseEvent.getY()), this.shiftDown, false);
                this.shiftDown = false;
            }
            this.setCaretAnimating(true);
        }
    }

    @Override
    public void contextMenuRequested(ContextMenuEvent contextMenuEvent) {
        TextArea textArea = (TextArea)this.getControl();
        if (this.contextMenu.isShowing()) {
            this.contextMenu.hide();
        } else if (textArea.getContextMenu() == null) {
            Object object;
            double d2 = contextMenuEvent.getScreenX();
            double d3 = contextMenuEvent.getScreenY();
            double d4 = contextMenuEvent.getSceneX();
            if (IS_TOUCH_SUPPORTED) {
                Point2D point2D;
                if (textArea.getSelection().getLength() == 0) {
                    this.skin.positionCaret(this.skin.getIndex(contextMenuEvent.getX(), contextMenuEvent.getY()), false, false);
                    point2D = this.skin.getMenuPosition();
                } else {
                    point2D = this.skin.getMenuPosition();
                    if (point2D != null && (point2D.getX() <= 0.0 || point2D.getY() <= 0.0)) {
                        this.skin.positionCaret(this.skin.getIndex(contextMenuEvent.getX(), contextMenuEvent.getY()), false, false);
                        point2D = this.skin.getMenuPosition();
                    }
                }
                if (point2D != null) {
                    Point2D point2D2 = ((TextArea)this.getControl()).localToScene(point2D);
                    Scene scene = ((TextArea)this.getControl()).getScene();
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
                ((TextArea)this.getControl()).getProperties().put("CONTEXT_MENU_SCREEN_X", d2);
                ((TextArea)this.getControl()).getProperties().put("CONTEXT_MENU_SCENE_X", d4);
                this.contextMenu.show((Node)this.getControl(), rectangle2D.getMinX(), d3);
            } else if (d2 + d5 > rectangle2D.getMaxX()) {
                double d7 = d5 - (rectangle2D.getMaxX() - d2);
                ((TextArea)this.getControl()).getProperties().put("CONTEXT_MENU_SCREEN_X", d2);
                ((TextArea)this.getControl()).getProperties().put("CONTEXT_MENU_SCENE_X", d4);
                this.contextMenu.show((Node)this.getControl(), d2 - d7, d3);
            } else {
                ((TextArea)this.getControl()).getProperties().put("CONTEXT_MENU_SCREEN_X", 0);
                ((TextArea)this.getControl()).getProperties().put("CONTEXT_MENU_SCENE_X", 0);
                this.contextMenu.show((Node)this.getControl(), d6, d3);
            }
        }
        contextMenuEvent.consume();
    }

    @Override
    protected void setCaretAnimating(boolean bl) {
        this.skin.setCaretAnimating(bl);
    }

    protected void mouseDoubleClick(HitInfo hitInfo) {
        TextArea textArea = (TextArea)this.getControl();
        textArea.previousWord();
        if (PlatformUtil.isWindows()) {
            textArea.selectNextWord();
        } else {
            textArea.selectEndOfNextWord();
        }
    }

    protected void mouseTripleClick(HitInfo hitInfo) {
        this.skin.paragraphStart(false, false);
        this.skin.paragraphEnd(false, PlatformUtil.isWindows(), true);
    }

    static {
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.HOME, KeyEvent.KEY_PRESSED, "LineStart"));
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.END, KeyEvent.KEY_PRESSED, "LineEnd"));
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.UP, KeyEvent.KEY_PRESSED, "PreviousLine"));
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_UP, KeyEvent.KEY_PRESSED, "PreviousLine"));
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.DOWN, KeyEvent.KEY_PRESSED, "NextLine"));
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_DOWN, KeyEvent.KEY_PRESSED, "NextLine"));
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.PAGE_UP, KeyEvent.KEY_PRESSED, "PreviousPage"));
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.PAGE_DOWN, KeyEvent.KEY_PRESSED, "NextPage"));
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.ENTER, KeyEvent.KEY_PRESSED, "InsertNewLine"));
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.TAB, KeyEvent.KEY_PRESSED, "TraverseOrInsertTab"));
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.HOME, KeyEvent.KEY_PRESSED, "SelectLineStart").shift());
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.END, KeyEvent.KEY_PRESSED, "SelectLineEnd").shift());
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.UP, KeyEvent.KEY_PRESSED, "SelectPreviousLine").shift());
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_UP, KeyEvent.KEY_PRESSED, "SelectPreviousLine").shift());
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.DOWN, KeyEvent.KEY_PRESSED, "SelectNextLine").shift());
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_DOWN, KeyEvent.KEY_PRESSED, "SelectNextLine").shift());
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.PAGE_UP, KeyEvent.KEY_PRESSED, "SelectPreviousPage").shift());
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.PAGE_DOWN, KeyEvent.KEY_PRESSED, "SelectNextPage").shift());
        if (PlatformUtil.isMac()) {
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.LEFT, KeyEvent.KEY_PRESSED, "LineStart").shortcut());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_LEFT, KeyEvent.KEY_PRESSED, "LineStart").shortcut());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, KeyEvent.KEY_PRESSED, "LineEnd").shortcut());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_RIGHT, KeyEvent.KEY_PRESSED, "LineEnd").shortcut());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.UP, KeyEvent.KEY_PRESSED, "Home").shortcut());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_UP, KeyEvent.KEY_PRESSED, "Home").shortcut());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.DOWN, KeyEvent.KEY_PRESSED, "End").shortcut());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_DOWN, KeyEvent.KEY_PRESSED, "End").shortcut());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.LEFT, KeyEvent.KEY_PRESSED, "SelectLineStartExtend").shift().shortcut());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_LEFT, KeyEvent.KEY_PRESSED, "SelectLineStartExtend").shift().shortcut());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, KeyEvent.KEY_PRESSED, "SelectLineEndExtend").shift().shortcut());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_RIGHT, KeyEvent.KEY_PRESSED, "SelectLineEndExtend").shift().shortcut());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.UP, KeyEvent.KEY_PRESSED, "SelectHomeExtend").shortcut().shift());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_UP, KeyEvent.KEY_PRESSED, "SelectHomeExtend").shortcut().shift());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.DOWN, KeyEvent.KEY_PRESSED, "SelectEndExtend").shortcut().shift());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_DOWN, KeyEvent.KEY_PRESSED, "SelectEndExtend").shortcut().shift());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.UP, KeyEvent.KEY_PRESSED, "ParagraphStart").alt());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_UP, KeyEvent.KEY_PRESSED, "ParagraphStart").alt());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.DOWN, KeyEvent.KEY_PRESSED, "ParagraphEnd").alt());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_DOWN, KeyEvent.KEY_PRESSED, "ParagraphEnd").alt());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.UP, KeyEvent.KEY_PRESSED, "SelectParagraphStart").alt().shift());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_UP, KeyEvent.KEY_PRESSED, "SelectParagraphStart").alt().shift());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.DOWN, KeyEvent.KEY_PRESSED, "SelectParagraphEnd").alt().shift());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_DOWN, KeyEvent.KEY_PRESSED, "SelectParagraphEnd").alt().shift());
        } else {
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.UP, KeyEvent.KEY_PRESSED, "ParagraphStart").ctrl());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_UP, KeyEvent.KEY_PRESSED, "ParagraphStart").ctrl());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.DOWN, KeyEvent.KEY_PRESSED, "ParagraphEnd").ctrl());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_DOWN, KeyEvent.KEY_PRESSED, "ParagraphEnd").ctrl());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.UP, KeyEvent.KEY_PRESSED, "SelectParagraphStart").ctrl().shift());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_UP, KeyEvent.KEY_PRESSED, "SelectParagraphStart").ctrl().shift());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.DOWN, KeyEvent.KEY_PRESSED, "SelectParagraphEnd").ctrl().shift());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_DOWN, KeyEvent.KEY_PRESSED, "SelectParagraphEnd").ctrl().shift());
        }
        TEXT_AREA_BINDINGS.addAll(TextInputControlBindings.BINDINGS);
        TEXT_AREA_BINDINGS.add(new KeyBinding(null, KeyEvent.KEY_PRESSED, "Consume"));
    }
}

