/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.PaintConverter;
import com.sun.javafx.scene.control.behavior.TextInputControlBehavior;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import com.sun.javafx.scene.control.skin.FXVK;
import com.sun.javafx.scene.control.skin.resources.ControlResources;
import com.sun.javafx.scene.input.ExtendedInputMethodRequests;
import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;
import java.lang.ref.WeakReference;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.AccessibleAction;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.IndexRange;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.Clipboard;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.InputMethodHighlight;
import javafx.scene.input.InputMethodTextRun;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.Shape;
import javafx.scene.shape.VLineTo;
import javafx.stage.Window;
import javafx.util.Duration;

public abstract class TextInputControlSkin<T extends TextInputControl, B extends TextInputControlBehavior<T>>
extends BehaviorSkinBase<T, B> {
    static boolean preload = false;
    protected static final boolean SHOW_HANDLES;
    protected final ObservableObjectValue<FontMetrics> fontMetrics;
    protected final ObjectProperty<Paint> textFill = new StyleableObjectProperty<Paint>((Paint)Color.BLACK){

        @Override
        public Object getBean() {
            return TextInputControlSkin.this;
        }

        @Override
        public String getName() {
            return "textFill";
        }

        @Override
        public CssMetaData<TextInputControl, Paint> getCssMetaData() {
            return StyleableProperties.TEXT_FILL;
        }
    };
    protected final ObjectProperty<Paint> promptTextFill = new StyleableObjectProperty<Paint>((Paint)Color.GRAY){

        @Override
        public Object getBean() {
            return TextInputControlSkin.this;
        }

        @Override
        public String getName() {
            return "promptTextFill";
        }

        @Override
        public CssMetaData<TextInputControl, Paint> getCssMetaData() {
            return StyleableProperties.PROMPT_TEXT_FILL;
        }
    };
    protected final ObjectProperty<Paint> highlightFill = new StyleableObjectProperty<Paint>((Paint)Color.DODGERBLUE){

        @Override
        protected void invalidated() {
            TextInputControlSkin.this.updateHighlightFill();
        }

        @Override
        public Object getBean() {
            return TextInputControlSkin.this;
        }

        @Override
        public String getName() {
            return "highlightFill";
        }

        @Override
        public CssMetaData<TextInputControl, Paint> getCssMetaData() {
            return StyleableProperties.HIGHLIGHT_FILL;
        }
    };
    protected final ObjectProperty<Paint> highlightTextFill = new StyleableObjectProperty<Paint>((Paint)Color.WHITE){

        @Override
        protected void invalidated() {
            TextInputControlSkin.this.updateHighlightTextFill();
        }

        @Override
        public Object getBean() {
            return TextInputControlSkin.this;
        }

        @Override
        public String getName() {
            return "highlightTextFill";
        }

        @Override
        public CssMetaData<TextInputControl, Paint> getCssMetaData() {
            return StyleableProperties.HIGHLIGHT_TEXT_FILL;
        }
    };
    protected final BooleanProperty displayCaret = new StyleableBooleanProperty(true){

        @Override
        public Object getBean() {
            return TextInputControlSkin.this;
        }

        @Override
        public String getName() {
            return "displayCaret";
        }

        @Override
        public CssMetaData<TextInputControl, Boolean> getCssMetaData() {
            return StyleableProperties.DISPLAY_CARET;
        }
    };
    private BooleanProperty forwardBias = new SimpleBooleanProperty(this, "forwardBias", true);
    private BooleanProperty blink = new SimpleBooleanProperty(this, "blink", true);
    protected ObservableBooleanValue caretVisible;
    private CaretBlinking caretBlinking = new CaretBlinking(this.blink);
    protected final Path caretPath = new Path();
    protected StackPane caretHandle = null;
    protected StackPane selectionHandle1 = null;
    protected StackPane selectionHandle2 = null;
    private static final boolean IS_FXVK_SUPPORTED;
    private static boolean USE_FXVK;
    static int vkType;
    private int imstart;
    private int imlength;
    private List<Shape> imattrs = new ArrayList<Shape>();
    final MenuItem undoMI = new ContextMenuItem("Undo");
    final MenuItem redoMI = new ContextMenuItem("Redo");
    final MenuItem cutMI = new ContextMenuItem("Cut");
    final MenuItem copyMI = new ContextMenuItem("Copy");
    final MenuItem pasteMI = new ContextMenuItem("Paste");
    final MenuItem deleteMI = new ContextMenuItem("DeleteSelection");
    final MenuItem selectWordMI = new ContextMenuItem("SelectWord");
    final MenuItem selectAllMI = new ContextMenuItem("SelectAll");
    final MenuItem separatorMI = new SeparatorMenuItem();

    public BooleanProperty forwardBiasProperty() {
        return this.forwardBias;
    }

    public void setForwardBias(boolean bl) {
        this.forwardBias.set(bl);
    }

    public boolean isForwardBias() {
        return this.forwardBias.get();
    }

    public Point2D getMenuPosition() {
        if (SHOW_HANDLES) {
            if (this.caretHandle.isVisible()) {
                return new Point2D(this.caretHandle.getLayoutX() + this.caretHandle.getWidth() / 2.0, this.caretHandle.getLayoutY());
            }
            if (this.selectionHandle1.isVisible() && this.selectionHandle2.isVisible()) {
                return new Point2D((this.selectionHandle1.getLayoutX() + this.selectionHandle1.getWidth() / 2.0 + this.selectionHandle2.getLayoutX() + this.selectionHandle2.getWidth() / 2.0) / 2.0, this.selectionHandle2.getLayoutY() + this.selectionHandle2.getHeight() / 2.0);
            }
            return null;
        }
        throw new UnsupportedOperationException();
    }

    public void toggleUseVK() {
        if (++vkType < 4) {
            USE_FXVK = true;
            ((TextInputControl)this.getSkinnable()).getProperties().put("vkType", FXVK.VK_TYPE_NAMES[vkType]);
            FXVK.attach((Node)((Object)this.getSkinnable()));
        } else {
            FXVK.detach();
            vkType = -1;
            USE_FXVK = false;
        }
    }

    public TextInputControlSkin(final T t2, B b2) {
        super(t2, b2);
        this.fontMetrics = new ObjectBinding<FontMetrics>(){
            {
                this.bind(t2.fontProperty());
            }

            @Override
            protected FontMetrics computeValue() {
                TextInputControlSkin.this.invalidateMetrics();
                return Toolkit.getToolkit().getFontLoader().getFontMetrics(t2.getFont());
            }
        };
        this.caretVisible = new BooleanBinding((TextInputControl)t2){
            final /* synthetic */ TextInputControl val$textInput;
            {
                this.val$textInput = textInputControl;
                this.bind(this.val$textInput.focusedProperty(), this.val$textInput.anchorProperty(), this.val$textInput.caretPositionProperty(), this.val$textInput.disabledProperty(), this.val$textInput.editableProperty(), TextInputControlSkin.this.displayCaret, TextInputControlSkin.this.blink);
            }

            @Override
            protected boolean computeValue() {
                return !TextInputControlSkin.this.blink.get() && TextInputControlSkin.this.displayCaret.get() && this.val$textInput.isFocused() && (PlatformUtil.isWindows() || this.val$textInput.getCaretPosition() == this.val$textInput.getAnchor()) && !this.val$textInput.isDisabled() && this.val$textInput.isEditable();
            }
        };
        if (SHOW_HANDLES) {
            this.caretHandle = new StackPane();
            this.selectionHandle1 = new StackPane();
            this.selectionHandle2 = new StackPane();
            this.caretHandle.setManaged(false);
            this.selectionHandle1.setManaged(false);
            this.selectionHandle2.setManaged(false);
            this.caretHandle.visibleProperty().bind(new BooleanBinding((TextInputControl)t2){
                final /* synthetic */ TextInputControl val$textInput;
                {
                    this.val$textInput = textInputControl;
                    this.bind(this.val$textInput.focusedProperty(), this.val$textInput.anchorProperty(), this.val$textInput.caretPositionProperty(), this.val$textInput.disabledProperty(), this.val$textInput.editableProperty(), this.val$textInput.lengthProperty(), TextInputControlSkin.this.displayCaret);
                }

                @Override
                protected boolean computeValue() {
                    return TextInputControlSkin.this.displayCaret.get() && this.val$textInput.isFocused() && this.val$textInput.getCaretPosition() == this.val$textInput.getAnchor() && !this.val$textInput.isDisabled() && this.val$textInput.isEditable() && this.val$textInput.getLength() > 0;
                }
            });
            this.selectionHandle1.visibleProperty().bind(new BooleanBinding((TextInputControl)t2){
                final /* synthetic */ TextInputControl val$textInput;
                {
                    this.val$textInput = textInputControl;
                    this.bind(this.val$textInput.focusedProperty(), this.val$textInput.anchorProperty(), this.val$textInput.caretPositionProperty(), this.val$textInput.disabledProperty(), TextInputControlSkin.this.displayCaret);
                }

                @Override
                protected boolean computeValue() {
                    return TextInputControlSkin.this.displayCaret.get() && this.val$textInput.isFocused() && this.val$textInput.getCaretPosition() != this.val$textInput.getAnchor() && !this.val$textInput.isDisabled();
                }
            });
            this.selectionHandle2.visibleProperty().bind(new BooleanBinding((TextInputControl)t2){
                final /* synthetic */ TextInputControl val$textInput;
                {
                    this.val$textInput = textInputControl;
                    this.bind(this.val$textInput.focusedProperty(), this.val$textInput.anchorProperty(), this.val$textInput.caretPositionProperty(), this.val$textInput.disabledProperty(), TextInputControlSkin.this.displayCaret);
                }

                @Override
                protected boolean computeValue() {
                    return TextInputControlSkin.this.displayCaret.get() && this.val$textInput.isFocused() && this.val$textInput.getCaretPosition() != this.val$textInput.getAnchor() && !this.val$textInput.isDisabled();
                }
            });
            this.caretHandle.getStyleClass().setAll("caret-handle");
            this.selectionHandle1.getStyleClass().setAll("selection-handle");
            this.selectionHandle2.getStyleClass().setAll("selection-handle");
            this.selectionHandle1.setId("selection-handle-1");
            this.selectionHandle2.setId("selection-handle-2");
        }
        if (IS_FXVK_SUPPORTED) {
            Window window;
            Scene scene;
            if (preload && (scene = ((Node)t2).getScene()) != null && (window = scene.getWindow()) != null) {
                FXVK.init(t2);
            }
            ((Node)t2).focusedProperty().addListener(observable -> {
                if (USE_FXVK) {
                    Scene scene = ((TextInputControl)this.getSkinnable()).getScene();
                    if (t2.isEditable() && t2.isFocused()) {
                        FXVK.attach(t2);
                    } else if (!(scene != null && scene.getWindow() != null && scene.getWindow().isFocused() && scene.getFocusOwner() instanceof TextInputControl && ((TextInputControl)scene.getFocusOwner()).isEditable())) {
                        FXVK.detach();
                    }
                }
            });
        }
        if (((Node)t2).getOnInputMethodTextChanged() == null) {
            ((Node)t2).setOnInputMethodTextChanged(inputMethodEvent -> this.handleInputMethodEvent((InputMethodEvent)inputMethodEvent));
        }
        ((Node)t2).setInputMethodRequests(new ExtendedInputMethodRequests((TextInputControl)t2){
            final /* synthetic */ TextInputControl val$textInput;
            {
                this.val$textInput = textInputControl;
            }

            @Override
            public Point2D getTextLocation(int n2) {
                Scene scene = ((TextInputControl)TextInputControlSkin.this.getSkinnable()).getScene();
                Window window = scene.getWindow();
                Rectangle2D rectangle2D = TextInputControlSkin.this.getCharacterBounds(this.val$textInput.getSelection().getStart() + n2);
                Point2D point2D = ((TextInputControl)TextInputControlSkin.this.getSkinnable()).localToScene(rectangle2D.getMinX(), rectangle2D.getMaxY());
                Point2D point2D2 = new Point2D(window.getX() + scene.getX() + point2D.getX(), window.getY() + scene.getY() + point2D.getY());
                return point2D2;
            }

            @Override
            public int getLocationOffset(int n2, int n3) {
                return TextInputControlSkin.this.getInsertionPoint(n2, n3);
            }

            @Override
            public void cancelLatestCommittedText() {
            }

            @Override
            public String getSelectedText() {
                TextInputControl textInputControl = (TextInputControl)TextInputControlSkin.this.getSkinnable();
                IndexRange indexRange = textInputControl.getSelection();
                return textInputControl.getText(indexRange.getStart(), indexRange.getEnd());
            }

            @Override
            public int getInsertPositionOffset() {
                int n2 = ((TextInputControl)TextInputControlSkin.this.getSkinnable()).getCaretPosition();
                if (n2 < TextInputControlSkin.this.imstart) {
                    return n2;
                }
                if (n2 < TextInputControlSkin.this.imstart + TextInputControlSkin.this.imlength) {
                    return TextInputControlSkin.this.imstart;
                }
                return n2 - TextInputControlSkin.this.imlength;
            }

            @Override
            public String getCommittedText(int n2, int n3) {
                TextInputControl textInputControl = (TextInputControl)TextInputControlSkin.this.getSkinnable();
                if (n2 < TextInputControlSkin.this.imstart) {
                    if (n3 <= TextInputControlSkin.this.imstart) {
                        return textInputControl.getText(n2, n3);
                    }
                    return textInputControl.getText(n2, TextInputControlSkin.this.imstart) + textInputControl.getText(TextInputControlSkin.this.imstart + TextInputControlSkin.this.imlength, n3 + TextInputControlSkin.this.imlength);
                }
                return textInputControl.getText(n2 + TextInputControlSkin.this.imlength, n3 + TextInputControlSkin.this.imlength);
            }

            @Override
            public int getCommittedTextLength() {
                return ((TextInputControl)TextInputControlSkin.this.getSkinnable()).getText().length() - TextInputControlSkin.this.imlength;
            }
        });
    }

    protected String maskText(String string) {
        return string;
    }

    public char getCharacter(int n2) {
        return '\u0000';
    }

    public int getInsertionPoint(double d2, double d3) {
        return 0;
    }

    public Rectangle2D getCharacterBounds(int n2) {
        return null;
    }

    public void scrollCharacterToVisible(int n2) {
    }

    protected void invalidateMetrics() {
    }

    protected void updateTextFill() {
    }

    protected void updateHighlightFill() {
    }

    protected void updateHighlightTextFill() {
    }

    protected void handleInputMethodEvent(InputMethodEvent inputMethodEvent) {
        TextInputControl textInputControl = (TextInputControl)this.getSkinnable();
        if (textInputControl.isEditable() && !textInputControl.textProperty().isBound() && !textInputControl.isDisabled()) {
            CharSequence charSequence;
            if (PlatformUtil.isIOS()) {
                textInputControl.setText(inputMethodEvent.getCommitted());
                return;
            }
            if (this.imlength != 0) {
                this.removeHighlight(this.imattrs);
                this.imattrs.clear();
                textInputControl.selectRange(this.imstart, this.imstart + this.imlength);
            }
            if (inputMethodEvent.getCommitted().length() != 0) {
                charSequence = inputMethodEvent.getCommitted();
                textInputControl.replaceText(textInputControl.getSelection(), (String)charSequence);
            }
            this.imstart = textInputControl.getSelection().getStart();
            charSequence = new StringBuilder();
            for (InputMethodTextRun object : inputMethodEvent.getComposed()) {
                ((StringBuilder)charSequence).append(object.getText());
            }
            textInputControl.replaceText(textInputControl.getSelection(), ((StringBuilder)charSequence).toString());
            this.imlength = ((StringBuilder)charSequence).length();
            if (this.imlength != 0) {
                int n2 = this.imstart;
                for (InputMethodTextRun inputMethodTextRun : inputMethodEvent.getComposed()) {
                    int n3 = n2 + inputMethodTextRun.getText().length();
                    this.createInputMethodAttributes(inputMethodTextRun.getHighlight(), n2, n3);
                    n2 = n3;
                }
                this.addHighlight(this.imattrs, this.imstart);
                int n4 = inputMethodEvent.getCaretPosition();
                if (n4 >= 0 && n4 < this.imlength) {
                    textInputControl.selectRange(this.imstart + n4, this.imstart + n4);
                }
            }
        }
    }

    protected abstract PathElement[] getUnderlineShape(int var1, int var2);

    protected abstract PathElement[] getRangeShape(int var1, int var2);

    protected abstract void addHighlight(List<? extends Node> var1, int var2);

    protected abstract void removeHighlight(List<? extends Node> var1);

    public abstract void nextCharacterVisually(boolean var1);

    private void createInputMethodAttributes(InputMethodHighlight inputMethodHighlight, int n2, int n3) {
        double d2 = 0.0;
        double d3 = 0.0;
        double d4 = 0.0;
        double d5 = 0.0;
        PathElement[] arrpathElement = this.getUnderlineShape(n2, n3);
        for (int i2 = 0; i2 < arrpathElement.length; ++i2) {
            PathElement pathElement = arrpathElement[i2];
            if (pathElement instanceof MoveTo) {
                d2 = d3 = ((MoveTo)pathElement).getX();
                d4 = d5 = ((MoveTo)pathElement).getY();
            } else if (pathElement instanceof LineTo) {
                d2 = d2 < ((LineTo)pathElement).getX() ? d2 : ((LineTo)pathElement).getX();
                d3 = d3 > ((LineTo)pathElement).getX() ? d3 : ((LineTo)pathElement).getX();
                d4 = d4 < ((LineTo)pathElement).getY() ? d4 : ((LineTo)pathElement).getY();
                d5 = d5 > ((LineTo)pathElement).getY() ? d5 : ((LineTo)pathElement).getY();
            } else if (pathElement instanceof HLineTo) {
                d2 = d2 < ((HLineTo)pathElement).getX() ? d2 : ((HLineTo)pathElement).getX();
                d3 = d3 > ((HLineTo)pathElement).getX() ? d3 : ((HLineTo)pathElement).getX();
            } else if (pathElement instanceof VLineTo) {
                d4 = d4 < ((VLineTo)pathElement).getY() ? d4 : ((VLineTo)pathElement).getY();
                double d6 = d5 = d5 > ((VLineTo)pathElement).getY() ? d5 : ((VLineTo)pathElement).getY();
            }
            if (!(pathElement instanceof ClosePath) && i2 != arrpathElement.length - 1 && (i2 >= arrpathElement.length - 1 || !(arrpathElement[i2 + 1] instanceof MoveTo))) continue;
            Shape shape = null;
            if (inputMethodHighlight == InputMethodHighlight.SELECTED_RAW) {
                shape = new Path();
                ((Path)shape).getElements().addAll(this.getRangeShape(n2, n3));
                shape.setFill(Color.BLUE);
                shape.setOpacity(0.3f);
            } else if (inputMethodHighlight == InputMethodHighlight.UNSELECTED_RAW) {
                shape = new Line(d2 + 2.0, d5 + 1.0, d3 - 2.0, d5 + 1.0);
                shape.setStroke((Paint)this.textFill.get());
                shape.setStrokeWidth(d5 - d4);
                ObservableList<Double> observableList = shape.getStrokeDashArray();
                observableList.add(2.0);
                observableList.add(2.0);
            } else if (inputMethodHighlight == InputMethodHighlight.SELECTED_CONVERTED) {
                shape = new Line(d2 + 2.0, d5 + 1.0, d3 - 2.0, d5 + 1.0);
                shape.setStroke((Paint)this.textFill.get());
                shape.setStrokeWidth((d5 - d4) * 3.0);
            } else if (inputMethodHighlight == InputMethodHighlight.UNSELECTED_CONVERTED) {
                shape = new Line(d2 + 2.0, d5 + 1.0, d3 - 2.0, d5 + 1.0);
                shape.setStroke((Paint)this.textFill.get());
                shape.setStrokeWidth(d5 - d4);
            }
            if (shape == null) continue;
            shape.setManaged(false);
            this.imattrs.add(shape);
        }
    }

    protected boolean isRTL() {
        return ((TextInputControl)this.getSkinnable()).getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT;
    }

    public void setCaretAnimating(boolean bl) {
        if (bl) {
            this.caretBlinking.start();
        } else {
            this.caretBlinking.stop();
            this.blink.set(true);
        }
    }

    public void populateContextMenu(ContextMenu contextMenu) {
        TextInputControl textInputControl = (TextInputControl)this.getSkinnable();
        boolean bl = textInputControl.isEditable();
        boolean bl2 = textInputControl.getLength() > 0;
        boolean bl3 = textInputControl.getSelection().getLength() > 0;
        boolean bl4 = this.maskText("A") != "A";
        ObservableList<MenuItem> observableList = contextMenu.getItems();
        if (SHOW_HANDLES) {
            observableList.clear();
            if (!bl4 && bl3) {
                if (bl) {
                    observableList.add(this.cutMI);
                }
                observableList.add(this.copyMI);
            }
            if (bl && Clipboard.getSystemClipboard().hasString()) {
                observableList.add(this.pasteMI);
            }
            if (bl2) {
                if (!bl3) {
                    observableList.add(this.selectWordMI);
                }
                observableList.add(this.selectAllMI);
            }
            this.selectWordMI.getProperties().put("refreshMenu", Boolean.TRUE);
            this.selectAllMI.getProperties().put("refreshMenu", Boolean.TRUE);
        } else {
            if (bl) {
                observableList.setAll(this.undoMI, this.redoMI, this.cutMI, this.copyMI, this.pasteMI, this.deleteMI, this.separatorMI, this.selectAllMI);
            } else {
                observableList.setAll(this.copyMI, this.separatorMI, this.selectAllMI);
            }
            this.undoMI.setDisable(!((TextInputControl)this.getSkinnable()).isUndoable());
            this.redoMI.setDisable(!((TextInputControl)this.getSkinnable()).isRedoable());
            this.cutMI.setDisable(bl4 || !bl3);
            this.copyMI.setDisable(bl4 || !bl3);
            this.pasteMI.setDisable(!Clipboard.getSystemClipboard().hasString());
            this.deleteMI.setDisable(!bl3);
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return TextInputControlSkin.getClassCssMetaData();
    }

    @Override
    protected void executeAccessibleAction(AccessibleAction accessibleAction, Object ... arrobject) {
        switch (accessibleAction) {
            case SHOW_TEXT_RANGE: {
                Integer n2 = (Integer)arrobject[0];
                Integer n3 = (Integer)arrobject[1];
                if (n2 == null || n3 == null) break;
                this.scrollCharacterToVisible(n3);
                this.scrollCharacterToVisible(n2);
                this.scrollCharacterToVisible(n3);
                break;
            }
            default: {
                super.executeAccessibleAction(accessibleAction, arrobject);
            }
        }
    }

    static {
        AccessController.doPrivileged(() -> {
            String string = System.getProperty("com.sun.javafx.virtualKeyboard.preload");
            if (string != null && string.equalsIgnoreCase("PRERENDER")) {
                preload = true;
            }
            return null;
        });
        SHOW_HANDLES = IS_TOUCH_SUPPORTED && !PlatformUtil.isIOS();
        USE_FXVK = IS_FXVK_SUPPORTED = Platform.isSupported(ConditionalFeature.VIRTUAL_KEYBOARD);
        vkType = -1;
    }

    private static class StyleableProperties {
        private static final CssMetaData<TextInputControl, Paint> TEXT_FILL = new CssMetaData<TextInputControl, Paint>("-fx-text-fill", PaintConverter.getInstance(), (Paint)Color.BLACK){

            @Override
            public boolean isSettable(TextInputControl textInputControl) {
                TextInputControlSkin textInputControlSkin = (TextInputControlSkin)textInputControl.getSkin();
                return textInputControlSkin.textFill == null || !textInputControlSkin.textFill.isBound();
            }

            @Override
            public StyleableProperty<Paint> getStyleableProperty(TextInputControl textInputControl) {
                TextInputControlSkin textInputControlSkin = (TextInputControlSkin)textInputControl.getSkin();
                return (StyleableProperty)((Object)textInputControlSkin.textFill);
            }
        };
        private static final CssMetaData<TextInputControl, Paint> PROMPT_TEXT_FILL = new CssMetaData<TextInputControl, Paint>("-fx-prompt-text-fill", PaintConverter.getInstance(), (Paint)Color.GRAY){

            @Override
            public boolean isSettable(TextInputControl textInputControl) {
                TextInputControlSkin textInputControlSkin = (TextInputControlSkin)textInputControl.getSkin();
                return textInputControlSkin.promptTextFill == null || !textInputControlSkin.promptTextFill.isBound();
            }

            @Override
            public StyleableProperty<Paint> getStyleableProperty(TextInputControl textInputControl) {
                TextInputControlSkin textInputControlSkin = (TextInputControlSkin)textInputControl.getSkin();
                return (StyleableProperty)((Object)textInputControlSkin.promptTextFill);
            }
        };
        private static final CssMetaData<TextInputControl, Paint> HIGHLIGHT_FILL = new CssMetaData<TextInputControl, Paint>("-fx-highlight-fill", PaintConverter.getInstance(), (Paint)Color.DODGERBLUE){

            @Override
            public boolean isSettable(TextInputControl textInputControl) {
                TextInputControlSkin textInputControlSkin = (TextInputControlSkin)textInputControl.getSkin();
                return textInputControlSkin.highlightFill == null || !textInputControlSkin.highlightFill.isBound();
            }

            @Override
            public StyleableProperty<Paint> getStyleableProperty(TextInputControl textInputControl) {
                TextInputControlSkin textInputControlSkin = (TextInputControlSkin)textInputControl.getSkin();
                return (StyleableProperty)((Object)textInputControlSkin.highlightFill);
            }
        };
        private static final CssMetaData<TextInputControl, Paint> HIGHLIGHT_TEXT_FILL = new CssMetaData<TextInputControl, Paint>("-fx-highlight-text-fill", PaintConverter.getInstance(), (Paint)Color.WHITE){

            @Override
            public boolean isSettable(TextInputControl textInputControl) {
                TextInputControlSkin textInputControlSkin = (TextInputControlSkin)textInputControl.getSkin();
                return textInputControlSkin.highlightTextFill == null || !textInputControlSkin.highlightTextFill.isBound();
            }

            @Override
            public StyleableProperty<Paint> getStyleableProperty(TextInputControl textInputControl) {
                TextInputControlSkin textInputControlSkin = (TextInputControlSkin)textInputControl.getSkin();
                return (StyleableProperty)((Object)textInputControlSkin.highlightTextFill);
            }
        };
        private static final CssMetaData<TextInputControl, Boolean> DISPLAY_CARET = new CssMetaData<TextInputControl, Boolean>("-fx-display-caret", BooleanConverter.getInstance(), Boolean.TRUE){

            @Override
            public boolean isSettable(TextInputControl textInputControl) {
                TextInputControlSkin textInputControlSkin = (TextInputControlSkin)textInputControl.getSkin();
                return textInputControlSkin.displayCaret == null || !textInputControlSkin.displayCaret.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(TextInputControl textInputControl) {
                TextInputControlSkin textInputControlSkin = (TextInputControlSkin)textInputControl.getSkin();
                return (StyleableProperty)((Object)textInputControlSkin.displayCaret);
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            ArrayList arrayList = new ArrayList(SkinBase.getClassCssMetaData());
            arrayList.add(TEXT_FILL);
            arrayList.add(PROMPT_TEXT_FILL);
            arrayList.add(HIGHLIGHT_FILL);
            arrayList.add(HIGHLIGHT_TEXT_FILL);
            arrayList.add(DISPLAY_CARET);
            STYLEABLES = Collections.unmodifiableList(arrayList);
        }
    }

    class ContextMenuItem
    extends MenuItem {
        ContextMenuItem(String string) {
            super(ControlResources.getString("TextInputControl.menu." + string));
            this.setOnAction(actionEvent -> ((TextInputControlBehavior)TextInputControlSkin.this.getBehavior()).callAction(string));
        }
    }

    private static final class CaretBlinking {
        private final Timeline caretTimeline;
        private final WeakReference<BooleanProperty> blinkPropertyRef;

        public CaretBlinking(BooleanProperty booleanProperty) {
            this.blinkPropertyRef = new WeakReference<BooleanProperty>(booleanProperty);
            this.caretTimeline = new Timeline();
            this.caretTimeline.setCycleCount(-1);
            this.caretTimeline.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, actionEvent -> this.setBlink(false), new KeyValue[0]), new KeyFrame(Duration.seconds(0.5), actionEvent -> this.setBlink(true), new KeyValue[0]), new KeyFrame(Duration.seconds(1.0), new KeyValue[0]));
        }

        public void start() {
            this.caretTimeline.play();
        }

        public void stop() {
            this.caretTimeline.stop();
        }

        private void setBlink(boolean bl) {
            BooleanProperty booleanProperty = (BooleanProperty)this.blinkPropertyRef.get();
            if (booleanProperty == null) {
                this.caretTimeline.stop();
                return;
            }
            booleanProperty.set(bl);
        }
    }
}

