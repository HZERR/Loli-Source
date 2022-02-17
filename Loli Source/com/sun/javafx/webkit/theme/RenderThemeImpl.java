/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.webkit.theme;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import com.sun.javafx.webkit.Accessor;
import com.sun.javafx.webkit.theme.Renderer;
import com.sun.webkit.LoadListenerClient;
import com.sun.webkit.graphics.Ref;
import com.sun.webkit.graphics.RenderTheme;
import com.sun.webkit.graphics.WCGraphicsContext;
import com.sun.webkit.graphics.WCSize;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Control;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;

public final class RenderThemeImpl
extends RenderTheme {
    private static final Logger log = Logger.getLogger(RenderThemeImpl.class.getName());
    private Accessor accessor;
    private boolean isDefault;
    private Pool<FormControl> pool;

    public RenderThemeImpl(Accessor accessor) {
        this.accessor = accessor;
        this.pool = new Pool<FormControl>(formControl -> accessor.removeChild(formControl.asControl()), FormControl.class);
        accessor.addViewListener(new ViewListener(this.pool, accessor));
    }

    public RenderThemeImpl() {
        this.isDefault = true;
    }

    private void ensureNotDefault() {
        if (this.isDefault) {
            throw new IllegalStateException("the method should not be called in this context");
        }
    }

    @Override
    protected Ref createWidget(long l2, int n2, int n3, int n4, int n5, int n6, ByteBuffer byteBuffer) {
        this.ensureNotDefault();
        FormControl formControl = this.pool.get(l2);
        WidgetType widgetType = WidgetType.convert(n2);
        if (formControl == null || formControl.getType() != widgetType) {
            if (formControl != null) {
                this.accessor.removeChild(formControl.asControl());
            }
            switch (widgetType) {
                case TEXTFIELD: {
                    formControl = new FormTextField();
                    break;
                }
                case BUTTON: {
                    formControl = new FormButton();
                    break;
                }
                case CHECKBOX: {
                    formControl = new FormCheckBox();
                    break;
                }
                case RADIOBUTTON: {
                    formControl = new FormRadioButton();
                    break;
                }
                case MENULIST: {
                    formControl = new FormMenuList();
                    break;
                }
                case MENULISTBUTTON: {
                    formControl = new FormMenuListButton();
                    break;
                }
                case SLIDER: {
                    formControl = new FormSlider();
                    break;
                }
                case PROGRESSBAR: {
                    formControl = new FormProgressBar(WidgetType.PROGRESSBAR);
                    break;
                }
                case METER: {
                    formControl = new FormProgressBar(WidgetType.METER);
                    break;
                }
                default: {
                    log.log(Level.ALL, "unknown widget index: {0}", n2);
                    return null;
                }
            }
            formControl.asControl().setFocusTraversable(false);
            this.pool.put(l2, formControl, this.accessor.getPage().getUpdateContentCycleID());
            this.accessor.addChild(formControl.asControl());
        }
        formControl.setState(n3);
        Control control = formControl.asControl();
        if (control.getWidth() != (double)n4 || control.getHeight() != (double)n5) {
            control.resize(n4, n5);
        }
        if (control.isManaged()) {
            control.setManaged(false);
        }
        if (widgetType == WidgetType.SLIDER) {
            Slider slider = (Slider)control;
            byteBuffer.order(ByteOrder.nativeOrder());
            slider.setOrientation(byteBuffer.getInt() == 0 ? Orientation.HORIZONTAL : Orientation.VERTICAL);
            slider.setMax(byteBuffer.getFloat());
            slider.setMin(byteBuffer.getFloat());
            slider.setValue(byteBuffer.getFloat());
        } else if (widgetType == WidgetType.PROGRESSBAR) {
            ProgressBar progressBar = (ProgressBar)control;
            byteBuffer.order(ByteOrder.nativeOrder());
            progressBar.setProgress(byteBuffer.getInt() == 1 ? (double)byteBuffer.getFloat() : -1.0);
        } else if (widgetType == WidgetType.METER) {
            ProgressBar progressBar = (ProgressBar)control;
            byteBuffer.order(ByteOrder.nativeOrder());
            progressBar.setProgress(byteBuffer.getFloat());
            progressBar.setStyle(this.getMeterStyle(byteBuffer.getInt()));
        }
        return new FormControlRef(formControl);
    }

    private String getMeterStyle(int n2) {
        switch (n2) {
            case 1: {
                return "-fx-accent: yellow";
            }
            case 2: {
                return "-fx-accent: red";
            }
        }
        return "-fx-accent: green";
    }

    @Override
    public void drawWidget(WCGraphicsContext wCGraphicsContext, Ref ref, int n2, int n3) {
        Control control;
        this.ensureNotDefault();
        FormControl formControl = ((FormControlRef)ref).asFormControl();
        if (formControl != null && (control = formControl.asControl()) != null) {
            wCGraphicsContext.saveState();
            wCGraphicsContext.translate(n2, n3);
            Renderer.getRenderer().render(control, wCGraphicsContext);
            wCGraphicsContext.restoreState();
        }
    }

    @Override
    public WCSize getWidgetSize(Ref ref) {
        this.ensureNotDefault();
        FormControl formControl = ((FormControlRef)ref).asFormControl();
        if (formControl != null) {
            Control control = formControl.asControl();
            return new WCSize((float)control.getWidth(), (float)control.getHeight());
        }
        return new WCSize(0.0f, 0.0f);
    }

    @Override
    protected int getRadioButtonSize() {
        String string = Application.getUserAgentStylesheet();
        if ("MODENA".equalsIgnoreCase(string)) {
            return 20;
        }
        if ("CASPIAN".equalsIgnoreCase(string)) {
            return 19;
        }
        return 20;
    }

    @Override
    protected int getSelectionColor(int n2) {
        switch (n2) {
            case 0: {
                return -16739329;
            }
            case 1: {
                return -1;
            }
        }
        return 0;
    }

    private static boolean hasState(int n2, int n3) {
        return (n2 & n3) != 0;
    }

    private static final class FormMenuListButton
    extends Button
    implements FormControl {
        private static final int MAX_WIDTH = 20;
        private static final int MIN_WIDTH = 16;

        @Override
        public Control asControl() {
            return this;
        }

        @Override
        public void setState(int n2) {
            this.setDisabled(!RenderThemeImpl.hasState(n2, 4));
            this.setHover(RenderThemeImpl.hasState(n2, 32));
            this.setPressed(RenderThemeImpl.hasState(n2, 16));
            if (this.isPressed()) {
                this.arm();
            } else {
                this.disarm();
            }
        }

        private FormMenuListButton() {
            this.setSkin(new Skin());
            this.setFocusTraversable(false);
            this.getStyleClass().add("form-select-button");
        }

        @Override
        public void resize(double d2, double d3) {
            d2 = d3 > 20.0 ? 20.0 : (d3 < 16.0 ? 16.0 : d3);
            super.resize(d2, d3);
            this.setTranslateX(-d2);
        }

        @Override
        public WidgetType getType() {
            return WidgetType.MENULISTBUTTON;
        }

        private final class Skin
        extends BehaviorSkinBase {
            Skin() {
                super(FormMenuListButton.this, new BehaviorBase<FormMenuListButton>(FormMenuListButton.this, Collections.EMPTY_LIST));
                Region region = new Region();
                region.getStyleClass().add("arrow");
                region.setMaxSize(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
                BorderPane borderPane = new BorderPane();
                borderPane.setCenter(region);
                this.getChildren().add(borderPane);
            }
        }
    }

    private static final class FormMenuList
    extends ChoiceBox
    implements FormControl {
        private FormMenuList() {
            ArrayList<String> arrayList = new ArrayList<String>();
            arrayList.add("");
            this.setItems(FXCollections.observableList(arrayList));
        }

        @Override
        public Control asControl() {
            return this;
        }

        @Override
        public void setState(int n2) {
            this.setDisabled(!RenderThemeImpl.hasState(n2, 4));
            this.setFocused(RenderThemeImpl.hasState(n2, 8));
            this.setHover(RenderThemeImpl.hasState(n2, 32) && !this.isDisabled());
        }

        @Override
        public WidgetType getType() {
            return WidgetType.MENULIST;
        }
    }

    private static final class FormProgressBar
    extends ProgressBar
    implements FormControl {
        private final WidgetType type;

        private FormProgressBar(WidgetType widgetType) {
            this.type = widgetType;
        }

        @Override
        public Control asControl() {
            return this;
        }

        @Override
        public void setState(int n2) {
            this.setDisabled(!RenderThemeImpl.hasState(n2, 4));
            this.setFocused(RenderThemeImpl.hasState(n2, 8));
            this.setHover(RenderThemeImpl.hasState(n2, 32) && !this.isDisabled());
        }

        @Override
        public WidgetType getType() {
            return this.type;
        }
    }

    private static final class FormSlider
    extends Slider
    implements FormControl {
        private FormSlider() {
        }

        @Override
        public Control asControl() {
            return this;
        }

        @Override
        public void setState(int n2) {
            this.setDisabled(!RenderThemeImpl.hasState(n2, 4));
            this.setFocused(RenderThemeImpl.hasState(n2, 8));
            this.setHover(RenderThemeImpl.hasState(n2, 32) && !this.isDisabled());
        }

        @Override
        public WidgetType getType() {
            return WidgetType.SLIDER;
        }
    }

    private static final class FormRadioButton
    extends RadioButton
    implements FormControl {
        private FormRadioButton() {
        }

        @Override
        public Control asControl() {
            return this;
        }

        @Override
        public void setState(int n2) {
            this.setDisabled(!RenderThemeImpl.hasState(n2, 4));
            this.setFocused(RenderThemeImpl.hasState(n2, 8));
            this.setHover(RenderThemeImpl.hasState(n2, 32) && !this.isDisabled());
            this.setSelected(RenderThemeImpl.hasState(n2, 1));
        }

        @Override
        public WidgetType getType() {
            return WidgetType.RADIOBUTTON;
        }
    }

    private static final class FormCheckBox
    extends CheckBox
    implements FormControl {
        private FormCheckBox() {
        }

        @Override
        public Control asControl() {
            return this;
        }

        @Override
        public void setState(int n2) {
            this.setDisabled(!RenderThemeImpl.hasState(n2, 4));
            this.setFocused(RenderThemeImpl.hasState(n2, 8));
            this.setHover(RenderThemeImpl.hasState(n2, 32) && !this.isDisabled());
            this.setSelected(RenderThemeImpl.hasState(n2, 1));
        }

        @Override
        public WidgetType getType() {
            return WidgetType.CHECKBOX;
        }
    }

    private static final class FormTextField
    extends TextField
    implements FormControl {
        private FormTextField() {
            this.setStyle("-fx-display-caret: false");
        }

        @Override
        public Control asControl() {
            return this;
        }

        @Override
        public void setState(int n2) {
            this.setDisabled(!RenderThemeImpl.hasState(n2, 4));
            this.setEditable(RenderThemeImpl.hasState(n2, 64));
            this.setFocused(RenderThemeImpl.hasState(n2, 8));
            this.setHover(RenderThemeImpl.hasState(n2, 32) && !this.isDisabled());
        }

        @Override
        public WidgetType getType() {
            return WidgetType.TEXTFIELD;
        }
    }

    private static final class FormButton
    extends Button
    implements FormControl {
        private FormButton() {
        }

        @Override
        public Control asControl() {
            return this;
        }

        @Override
        public void setState(int n2) {
            this.setDisabled(!RenderThemeImpl.hasState(n2, 4));
            this.setFocused(RenderThemeImpl.hasState(n2, 8));
            this.setHover(RenderThemeImpl.hasState(n2, 32) && !this.isDisabled());
            this.setPressed(RenderThemeImpl.hasState(n2, 16));
            if (this.isPressed()) {
                this.arm();
            } else {
                this.disarm();
            }
        }

        @Override
        public WidgetType getType() {
            return WidgetType.BUTTON;
        }
    }

    private static interface FormControl
    extends Widget {
        public Control asControl();

        public void setState(int var1);
    }

    static interface Widget {
        public WidgetType getType();
    }

    private static final class FormControlRef
    extends Ref {
        private final WeakReference<FormControl> fcRef;

        private FormControlRef(FormControl formControl) {
            this.fcRef = new WeakReference<FormControl>(formControl);
        }

        private FormControl asFormControl() {
            return (FormControl)this.fcRef.get();
        }
    }

    static class ViewListener
    implements InvalidationListener {
        private final Pool pool;
        private final Accessor accessor;
        private LoadListenerClient loadListener;

        ViewListener(Pool pool, Accessor accessor) {
            this.pool = pool;
            this.accessor = accessor;
        }

        @Override
        public void invalidated(Observable observable) {
            this.pool.clear();
            if (this.accessor.getPage() != null && this.loadListener == null) {
                this.loadListener = new LoadListenerClient(){

                    @Override
                    public void dispatchLoadEvent(long l2, int n2, String string, String string2, double d2, int n3) {
                        if (n2 == 0) {
                            pool.clear();
                        }
                    }

                    @Override
                    public void dispatchResourceLoadEvent(long l2, int n2, String string, String string2, double d2, int n3) {
                    }
                };
                this.accessor.getPage().addLoadListenerClient(this.loadListener);
            }
        }
    }

    static final class Pool<T extends Widget> {
        private static final int INITIAL_CAPACITY = 100;
        private int capacity = 100;
        private final LinkedHashMap<Long, Integer> ids = new LinkedHashMap();
        private final Map<Long, WeakReference<T>> pool = new HashMap<Long, WeakReference<T>>();
        private final Notifier<T> notifier;
        private final String type;

        Pool(Notifier<T> notifier, Class<T> class_) {
            this.notifier = notifier;
            this.type = class_.getSimpleName();
        }

        T get(long l2) {
            if (log.isLoggable(Level.FINE)) {
                log.log(Level.FINE, "type: {0}, size: {1}, id: 0x{2}", new Object[]{this.type, this.pool.size(), Long.toHexString(l2)});
            }
            assert (this.ids.size() == this.pool.size());
            WeakReference<T> weakReference = this.pool.get(l2);
            if (weakReference == null) {
                return null;
            }
            Widget widget = (Widget)weakReference.get();
            if (widget == null) {
                return null;
            }
            Integer n2 = (Integer)this.ids.remove(l2);
            this.ids.put(l2, n2);
            return (T)widget;
        }

        void put(long l2, T t2, int n2) {
            if (log.isLoggable(Level.FINEST)) {
                log.log(Level.FINEST, "size: {0}, id: 0x{1}, control: {2}", new Object[]{this.pool.size(), Long.toHexString(l2), t2.getType()});
            }
            if (this.ids.size() >= this.capacity) {
                Long l3 = this.ids.keySet().iterator().next();
                Integer n3 = this.ids.get(l3);
                if (n3 != n2) {
                    this.ids.remove(l3);
                    Widget widget = (Widget)this.pool.remove(l3).get();
                    if (widget != null) {
                        this.notifier.notifyRemoved(widget);
                    }
                } else {
                    this.capacity = Math.min(this.capacity, (int)Math.ceil(1.073741823E9)) * 2;
                }
            }
            this.ids.put(l2, n2);
            this.pool.put(l2, new WeakReference<T>(t2));
        }

        void clear() {
            if (log.isLoggable(Level.FINE)) {
                log.fine("size: " + this.pool.size() + ", controls: " + this.pool.values());
            }
            if (this.pool.size() == 0) {
                return;
            }
            this.ids.clear();
            for (WeakReference<T> weakReference : this.pool.values()) {
                Widget widget = (Widget)weakReference.get();
                if (widget == null) continue;
                this.notifier.notifyRemoved(widget);
            }
            this.pool.clear();
            this.capacity = 100;
        }

        static interface Notifier<T> {
            public void notifyRemoved(T var1);
        }
    }

    static enum WidgetType {
        TEXTFIELD(0),
        BUTTON(1),
        CHECKBOX(2),
        RADIOBUTTON(3),
        MENULIST(4),
        MENULISTBUTTON(5),
        SLIDER(6),
        PROGRESSBAR(7),
        METER(8),
        SCROLLBAR(9);

        private static final HashMap<Integer, WidgetType> map;
        private final int value;

        private WidgetType(int n3) {
            this.value = n3;
        }

        private static WidgetType convert(int n2) {
            return map.get(n2);
        }

        static {
            map = new HashMap();
            for (WidgetType widgetType : WidgetType.values()) {
                map.put(widgetType.value, widgetType);
            }
        }
    }
}

