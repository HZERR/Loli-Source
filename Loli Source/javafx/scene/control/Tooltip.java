/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import com.sun.javafx.beans.IDProperty;
import com.sun.javafx.css.StyleManager;
import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.css.converters.StringConverter;
import com.sun.javafx.scene.control.skin.TooltipSkin;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.css.CssMetaData;
import javafx.css.FontCssMetaData;
import javafx.css.SimpleStyleableBooleanProperty;
import javafx.css.SimpleStyleableDoubleProperty;
import javafx.css.SimpleStyleableObjectProperty;
import javafx.css.StyleOrigin;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.css.StyleableStringProperty;
import javafx.event.EventHandler;
import javafx.geometry.NodeOrientation;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.PopupControl;
import javafx.scene.control.Skin;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Window;
import javafx.util.Duration;

@IDProperty(value="id")
public class Tooltip
extends PopupControl {
    private static String TOOLTIP_PROP_KEY = "javafx.scene.control.Tooltip";
    private static int TOOLTIP_XOFFSET = 10;
    private static int TOOLTIP_YOFFSET = 7;
    private static TooltipBehavior BEHAVIOR = new TooltipBehavior(new Duration(1000.0), new Duration(5000.0), new Duration(200.0), false);
    private final StringProperty text = new SimpleStringProperty(this, "text", ""){

        @Override
        protected void invalidated() {
            super.invalidated();
            String string = this.get();
            if (Tooltip.this.isShowing() && string != null && !string.equals(Tooltip.this.getText())) {
                Tooltip.this.setAnchorX(BEHAVIOR.lastMouseX);
                Tooltip.this.setAnchorY(BEHAVIOR.lastMouseY);
            }
        }
    };
    private final ObjectProperty<TextAlignment> textAlignment = new SimpleStyleableObjectProperty<TextAlignment>(TEXT_ALIGNMENT, this, "textAlignment", TextAlignment.LEFT);
    private final ObjectProperty<OverrunStyle> textOverrun = new SimpleStyleableObjectProperty<OverrunStyle>(TEXT_OVERRUN, this, "textOverrun", OverrunStyle.ELLIPSIS);
    private final BooleanProperty wrapText = new SimpleStyleableBooleanProperty(WRAP_TEXT, this, "wrapText", false);
    private final ObjectProperty<Font> font = new StyleableObjectProperty<Font>(Font.getDefault()){
        private boolean fontSetByCss;
        {
            this.fontSetByCss = false;
        }

        @Override
        public void applyStyle(StyleOrigin styleOrigin, Font font) {
            try {
                this.fontSetByCss = true;
                super.applyStyle(styleOrigin, font);
            }
            catch (Exception exception) {
                throw exception;
            }
            finally {
                this.fontSetByCss = false;
            }
        }

        @Override
        public void set(Font font) {
            Font font2 = (Font)this.get();
            StyleOrigin styleOrigin = ((StyleableObjectProperty)Tooltip.this.font).getStyleOrigin();
            if (styleOrigin == null || (font != null ? !font.equals(font2) : font2 != null)) {
                super.set(font);
            }
        }

        @Override
        protected void invalidated() {
            if (!this.fontSetByCss) {
                Tooltip.this.bridge.impl_reapplyCSS();
            }
        }

        @Override
        public CssMetaData<CSSBridge, Font> getCssMetaData() {
            return FONT;
        }

        @Override
        public Object getBean() {
            return Tooltip.this;
        }

        @Override
        public String getName() {
            return "font";
        }
    };
    private final ObjectProperty<Node> graphic = new StyleableObjectProperty<Node>(){

        @Override
        public CssMetaData getCssMetaData() {
            return GRAPHIC;
        }

        @Override
        public Object getBean() {
            return Tooltip.this;
        }

        @Override
        public String getName() {
            return "graphic";
        }
    };
    private StyleableStringProperty imageUrl = null;
    private final ObjectProperty<ContentDisplay> contentDisplay = new SimpleStyleableObjectProperty<ContentDisplay>(CONTENT_DISPLAY, this, "contentDisplay", ContentDisplay.LEFT);
    private final DoubleProperty graphicTextGap = new SimpleStyleableDoubleProperty(GRAPHIC_TEXT_GAP, this, "graphicTextGap", 4.0);
    private final ReadOnlyBooleanWrapper activated = new ReadOnlyBooleanWrapper(this, "activated");
    private static final CssMetaData<CSSBridge, Font> FONT = new FontCssMetaData<CSSBridge>("-fx-font", Font.getDefault()){

        @Override
        public boolean isSettable(CSSBridge cSSBridge) {
            return !cSSBridge.tooltip.fontProperty().isBound();
        }

        @Override
        public StyleableProperty<Font> getStyleableProperty(CSSBridge cSSBridge) {
            return (StyleableProperty)((Object)cSSBridge.tooltip.fontProperty());
        }
    };
    private static final CssMetaData<CSSBridge, TextAlignment> TEXT_ALIGNMENT = new CssMetaData<CSSBridge, TextAlignment>("-fx-text-alignment", new EnumConverter<TextAlignment>(TextAlignment.class), TextAlignment.LEFT){

        @Override
        public boolean isSettable(CSSBridge cSSBridge) {
            return !cSSBridge.tooltip.textAlignmentProperty().isBound();
        }

        @Override
        public StyleableProperty<TextAlignment> getStyleableProperty(CSSBridge cSSBridge) {
            return (StyleableProperty)((Object)cSSBridge.tooltip.textAlignmentProperty());
        }
    };
    private static final CssMetaData<CSSBridge, OverrunStyle> TEXT_OVERRUN = new CssMetaData<CSSBridge, OverrunStyle>("-fx-text-overrun", new EnumConverter<OverrunStyle>(OverrunStyle.class), OverrunStyle.ELLIPSIS){

        @Override
        public boolean isSettable(CSSBridge cSSBridge) {
            return !cSSBridge.tooltip.textOverrunProperty().isBound();
        }

        @Override
        public StyleableProperty<OverrunStyle> getStyleableProperty(CSSBridge cSSBridge) {
            return (StyleableProperty)((Object)cSSBridge.tooltip.textOverrunProperty());
        }
    };
    private static final CssMetaData<CSSBridge, Boolean> WRAP_TEXT = new CssMetaData<CSSBridge, Boolean>("-fx-wrap-text", BooleanConverter.getInstance(), Boolean.FALSE){

        @Override
        public boolean isSettable(CSSBridge cSSBridge) {
            return !cSSBridge.tooltip.wrapTextProperty().isBound();
        }

        @Override
        public StyleableProperty<Boolean> getStyleableProperty(CSSBridge cSSBridge) {
            return (StyleableProperty)((Object)cSSBridge.tooltip.wrapTextProperty());
        }
    };
    private static final CssMetaData<CSSBridge, String> GRAPHIC = new CssMetaData<CSSBridge, String>("-fx-graphic", StringConverter.getInstance()){

        @Override
        public boolean isSettable(CSSBridge cSSBridge) {
            return !cSSBridge.tooltip.graphicProperty().isBound();
        }

        @Override
        public StyleableProperty<String> getStyleableProperty(CSSBridge cSSBridge) {
            return cSSBridge.tooltip.imageUrlProperty();
        }
    };
    private static final CssMetaData<CSSBridge, ContentDisplay> CONTENT_DISPLAY = new CssMetaData<CSSBridge, ContentDisplay>("-fx-content-display", new EnumConverter<ContentDisplay>(ContentDisplay.class), ContentDisplay.LEFT){

        @Override
        public boolean isSettable(CSSBridge cSSBridge) {
            return !cSSBridge.tooltip.contentDisplayProperty().isBound();
        }

        @Override
        public StyleableProperty<ContentDisplay> getStyleableProperty(CSSBridge cSSBridge) {
            return (StyleableProperty)((Object)cSSBridge.tooltip.contentDisplayProperty());
        }
    };
    private static final CssMetaData<CSSBridge, Number> GRAPHIC_TEXT_GAP = new CssMetaData<CSSBridge, Number>("-fx-graphic-text-gap", SizeConverter.getInstance(), (Number)4.0){

        @Override
        public boolean isSettable(CSSBridge cSSBridge) {
            return !cSSBridge.tooltip.graphicTextGapProperty().isBound();
        }

        @Override
        public StyleableProperty<Number> getStyleableProperty(CSSBridge cSSBridge) {
            return (StyleableProperty)((Object)cSSBridge.tooltip.graphicTextGapProperty());
        }
    };
    private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

    public static void install(Node node, Tooltip tooltip) {
        Tooltip.BEHAVIOR.install(node, tooltip);
    }

    public static void uninstall(Node node, Tooltip tooltip) {
        Tooltip.BEHAVIOR.uninstall(node);
    }

    public Tooltip() {
        this(null);
    }

    public Tooltip(String string) {
        if (string != null) {
            this.setText(string);
        }
        this.bridge = new CSSBridge();
        this.getContent().setAll(this.bridge);
        this.getStyleClass().setAll("tooltip");
    }

    public final StringProperty textProperty() {
        return this.text;
    }

    public final void setText(String string) {
        this.textProperty().setValue(string);
    }

    public final String getText() {
        return this.text.getValue() == null ? "" : this.text.getValue();
    }

    public final ObjectProperty<TextAlignment> textAlignmentProperty() {
        return this.textAlignment;
    }

    public final void setTextAlignment(TextAlignment textAlignment) {
        this.textAlignmentProperty().setValue(textAlignment);
    }

    public final TextAlignment getTextAlignment() {
        return (TextAlignment)((Object)this.textAlignmentProperty().getValue());
    }

    public final ObjectProperty<OverrunStyle> textOverrunProperty() {
        return this.textOverrun;
    }

    public final void setTextOverrun(OverrunStyle overrunStyle) {
        this.textOverrunProperty().setValue(overrunStyle);
    }

    public final OverrunStyle getTextOverrun() {
        return (OverrunStyle)((Object)this.textOverrunProperty().getValue());
    }

    public final BooleanProperty wrapTextProperty() {
        return this.wrapText;
    }

    public final void setWrapText(boolean bl) {
        this.wrapTextProperty().setValue(bl);
    }

    public final boolean isWrapText() {
        return this.wrapTextProperty().getValue();
    }

    public final ObjectProperty<Font> fontProperty() {
        return this.font;
    }

    public final void setFont(Font font) {
        this.fontProperty().setValue(font);
    }

    public final Font getFont() {
        return (Font)this.fontProperty().getValue();
    }

    public final ObjectProperty<Node> graphicProperty() {
        return this.graphic;
    }

    public final void setGraphic(Node node) {
        this.graphicProperty().setValue(node);
    }

    public final Node getGraphic() {
        return (Node)this.graphicProperty().getValue();
    }

    private StyleableStringProperty imageUrlProperty() {
        if (this.imageUrl == null) {
            this.imageUrl = new StyleableStringProperty(){
                StyleOrigin origin = StyleOrigin.USER;

                @Override
                public void applyStyle(StyleOrigin styleOrigin, String string) {
                    this.origin = styleOrigin;
                    if (Tooltip.this.graphic == null || !Tooltip.this.graphic.isBound()) {
                        super.applyStyle(styleOrigin, string);
                    }
                    this.origin = StyleOrigin.USER;
                }

                @Override
                protected void invalidated() {
                    String string = super.get();
                    if (string == null) {
                        ((StyleableProperty)((Object)Tooltip.this.graphicProperty())).applyStyle(this.origin, null);
                    } else {
                        String string2;
                        Object object;
                        Image image;
                        Node node = Tooltip.this.getGraphic();
                        if (node instanceof ImageView && (image = ((ImageView)(object = (ImageView)node)).getImage()) != null && string.equals(string2 = image.impl_getUrl())) {
                            return;
                        }
                        object = StyleManager.getInstance().getCachedImage(string);
                        if (object != null) {
                            ((StyleableProperty)((Object)Tooltip.this.graphicProperty())).applyStyle(this.origin, new ImageView((Image)object));
                        }
                    }
                }

                @Override
                public String get() {
                    Image image;
                    Node node = Tooltip.this.getGraphic();
                    if (node instanceof ImageView && (image = ((ImageView)node).getImage()) != null) {
                        return image.impl_getUrl();
                    }
                    return null;
                }

                @Override
                public StyleOrigin getStyleOrigin() {
                    return Tooltip.this.graphic != null ? ((StyleableProperty)((Object)Tooltip.this.graphic)).getStyleOrigin() : null;
                }

                @Override
                public Object getBean() {
                    return Tooltip.this;
                }

                @Override
                public String getName() {
                    return "imageUrl";
                }

                @Override
                public CssMetaData<CSSBridge, String> getCssMetaData() {
                    return GRAPHIC;
                }
            };
        }
        return this.imageUrl;
    }

    public final ObjectProperty<ContentDisplay> contentDisplayProperty() {
        return this.contentDisplay;
    }

    public final void setContentDisplay(ContentDisplay contentDisplay) {
        this.contentDisplayProperty().setValue(contentDisplay);
    }

    public final ContentDisplay getContentDisplay() {
        return (ContentDisplay)((Object)this.contentDisplayProperty().getValue());
    }

    public final DoubleProperty graphicTextGapProperty() {
        return this.graphicTextGap;
    }

    public final void setGraphicTextGap(double d2) {
        this.graphicTextGapProperty().setValue(d2);
    }

    public final double getGraphicTextGap() {
        return this.graphicTextGapProperty().getValue();
    }

    final void setActivated(boolean bl) {
        this.activated.set(bl);
    }

    public final boolean isActivated() {
        return this.activated.get();
    }

    public final ReadOnlyBooleanProperty activatedProperty() {
        return this.activated.getReadOnlyProperty();
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new TooltipSkin(this);
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return STYLEABLES;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return Tooltip.getClassCssMetaData();
    }

    @Override
    public Styleable getStyleableParent() {
        if (BEHAVIOR.hoveredNode == null) {
            return super.getStyleableParent();
        }
        return BEHAVIOR.hoveredNode;
    }

    static {
        ArrayList arrayList = new ArrayList(PopupControl.getClassCssMetaData());
        arrayList.add(FONT);
        arrayList.add(TEXT_ALIGNMENT);
        arrayList.add(TEXT_OVERRUN);
        arrayList.add(WRAP_TEXT);
        arrayList.add(GRAPHIC);
        arrayList.add(CONTENT_DISPLAY);
        arrayList.add(GRAPHIC_TEXT_GAP);
        STYLEABLES = Collections.unmodifiableList(arrayList);
    }

    private static class TooltipBehavior {
        private Timeline activationTimer = new Timeline();
        private Timeline hideTimer = new Timeline();
        private Timeline leftTimer = new Timeline();
        private Node hoveredNode;
        private Tooltip activatedTooltip;
        private Tooltip visibleTooltip;
        private double lastMouseX;
        private double lastMouseY;
        private boolean hideOnExit;
        private EventHandler<MouseEvent> MOVE_HANDLER = mouseEvent -> {
            this.lastMouseX = mouseEvent.getScreenX();
            this.lastMouseY = mouseEvent.getScreenY();
            if (this.hideTimer.getStatus() == Animation.Status.RUNNING) {
                return;
            }
            this.hoveredNode = (Node)mouseEvent.getSource();
            Tooltip tooltip = (Tooltip)this.hoveredNode.getProperties().get(TOOLTIP_PROP_KEY);
            if (tooltip != null) {
                Window window = this.getWindow(this.hoveredNode);
                boolean bl = this.isWindowHierarchyVisible(this.hoveredNode);
                if (window != null && bl) {
                    if (this.leftTimer.getStatus() == Animation.Status.RUNNING) {
                        if (this.visibleTooltip != null) {
                            this.visibleTooltip.hide();
                        }
                        this.visibleTooltip = tooltip;
                        tooltip.show(window, mouseEvent.getScreenX() + (double)TOOLTIP_XOFFSET, mouseEvent.getScreenY() + (double)TOOLTIP_YOFFSET);
                        this.leftTimer.stop();
                        this.hideTimer.playFromStart();
                    } else {
                        tooltip.setActivated(true);
                        this.activatedTooltip = tooltip;
                        this.activationTimer.stop();
                        this.activationTimer.playFromStart();
                    }
                }
            }
        };
        private EventHandler<MouseEvent> LEAVING_HANDLER = mouseEvent -> {
            if (this.activationTimer.getStatus() == Animation.Status.RUNNING) {
                this.activationTimer.stop();
            } else if (this.hideTimer.getStatus() == Animation.Status.RUNNING) {
                assert (this.visibleTooltip != null);
                this.hideTimer.stop();
                if (this.hideOnExit) {
                    this.visibleTooltip.hide();
                }
                this.leftTimer.playFromStart();
            }
            this.hoveredNode = null;
            this.activatedTooltip = null;
            if (this.hideOnExit) {
                this.visibleTooltip = null;
            }
        };
        private EventHandler<MouseEvent> KILL_HANDLER = mouseEvent -> {
            this.activationTimer.stop();
            this.hideTimer.stop();
            this.leftTimer.stop();
            if (this.visibleTooltip != null) {
                this.visibleTooltip.hide();
            }
            this.hoveredNode = null;
            this.activatedTooltip = null;
            this.visibleTooltip = null;
        };

        TooltipBehavior(Duration duration, Duration duration2, Duration duration3, boolean bl) {
            this.hideOnExit = bl;
            this.activationTimer.getKeyFrames().add(new KeyFrame(duration, new KeyValue[0]));
            this.activationTimer.setOnFinished(actionEvent -> {
                assert (this.activatedTooltip != null);
                Window window = this.getWindow(this.hoveredNode);
                boolean bl = this.isWindowHierarchyVisible(this.hoveredNode);
                if (window != null && window.isShowing() && bl) {
                    double d2 = this.lastMouseX;
                    double d3 = this.lastMouseY;
                    NodeOrientation nodeOrientation = this.hoveredNode.getEffectiveNodeOrientation();
                    this.activatedTooltip.getScene().setNodeOrientation(nodeOrientation);
                    if (nodeOrientation == NodeOrientation.RIGHT_TO_LEFT) {
                        d2 -= this.activatedTooltip.getWidth();
                    }
                    this.activatedTooltip.show(window, d2 + (double)TOOLTIP_XOFFSET, d3 + (double)TOOLTIP_YOFFSET);
                    if (d3 + (double)TOOLTIP_YOFFSET > this.activatedTooltip.getAnchorY()) {
                        this.activatedTooltip.hide();
                        this.activatedTooltip.show(window, d2 + (double)TOOLTIP_XOFFSET, d3 -= this.activatedTooltip.getHeight());
                    }
                    this.visibleTooltip = this.activatedTooltip;
                    this.hoveredNode = null;
                    this.hideTimer.playFromStart();
                }
                this.activatedTooltip.setActivated(false);
                this.activatedTooltip = null;
            });
            this.hideTimer.getKeyFrames().add(new KeyFrame(duration2, new KeyValue[0]));
            this.hideTimer.setOnFinished(actionEvent -> {
                assert (this.visibleTooltip != null);
                this.visibleTooltip.hide();
                this.visibleTooltip = null;
                this.hoveredNode = null;
            });
            this.leftTimer.getKeyFrames().add(new KeyFrame(duration3, new KeyValue[0]));
            this.leftTimer.setOnFinished(actionEvent -> {
                if (!bl) {
                    assert (this.visibleTooltip != null);
                    this.visibleTooltip.hide();
                    this.visibleTooltip = null;
                    this.hoveredNode = null;
                }
            });
        }

        private void install(Node node, Tooltip tooltip) {
            if (node == null) {
                return;
            }
            node.addEventHandler(MouseEvent.MOUSE_MOVED, this.MOVE_HANDLER);
            node.addEventHandler(MouseEvent.MOUSE_EXITED, this.LEAVING_HANDLER);
            node.addEventHandler(MouseEvent.MOUSE_PRESSED, this.KILL_HANDLER);
            node.getProperties().put(TOOLTIP_PROP_KEY, tooltip);
        }

        private void uninstall(Node node) {
            if (node == null) {
                return;
            }
            node.removeEventHandler(MouseEvent.MOUSE_MOVED, this.MOVE_HANDLER);
            node.removeEventHandler(MouseEvent.MOUSE_EXITED, this.LEAVING_HANDLER);
            node.removeEventHandler(MouseEvent.MOUSE_PRESSED, this.KILL_HANDLER);
            Tooltip tooltip = (Tooltip)node.getProperties().get(TOOLTIP_PROP_KEY);
            if (tooltip != null) {
                node.getProperties().remove(TOOLTIP_PROP_KEY);
                if (tooltip.equals(this.visibleTooltip) || tooltip.equals(this.activatedTooltip)) {
                    this.KILL_HANDLER.handle(null);
                }
            }
        }

        private Window getWindow(Node node) {
            Scene scene = node == null ? null : node.getScene();
            return scene == null ? null : scene.getWindow();
        }

        private boolean isWindowHierarchyVisible(Node node) {
            Parent parent;
            boolean bl = node != null;
            Parent parent2 = parent = node == null ? null : node.getParent();
            while (parent != null && bl) {
                bl = parent.isVisible();
                parent = parent.getParent();
            }
            return bl;
        }
    }

    private final class CSSBridge
    extends PopupControl.CSSBridge {
        private Tooltip tooltip;

        CSSBridge() {
            this.tooltip = Tooltip.this;
            this.setAccessibleRole(AccessibleRole.TOOLTIP);
        }
    }
}

