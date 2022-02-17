/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.css.CssError;
import com.sun.javafx.css.StyleManager;
import com.sun.javafx.css.converters.StringConverter;
import com.sun.javafx.scene.control.ControlAcceleratorSupport;
import com.sun.javafx.scene.control.Logging;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.WritableValue;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.css.StyleableStringProperty;
import javafx.event.EventHandler;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;
import javafx.scene.control.Skinnable;
import javafx.scene.control.Tooltip;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.Region;
import sun.util.logging.PlatformLogger;

public abstract class Control
extends Region
implements Skinnable {
    private List<CssMetaData<? extends Styleable, ?>> styleableProperties;
    private SkinBase<?> skinBase;
    private static final EventHandler<ContextMenuEvent> contextMenuHandler;
    private ObjectProperty<Skin<?>> skin = new StyleableObjectProperty<Skin<?>>(){
        private Skin<?> oldValue;

        @Override
        public void set(Skin<?> skin) {
            if (skin == null ? this.oldValue == null : this.oldValue != null && skin.getClass().equals(this.oldValue.getClass())) {
                return;
            }
            super.set(skin);
        }

        @Override
        protected void invalidated() {
            Object object;
            Skin skin = (Skin)this.get();
            Control.this.currentSkinClassName = skin == null ? null : skin.getClass().getName();
            Control.this.skinClassNameProperty().set(Control.this.currentSkinClassName);
            if (this.oldValue != null) {
                this.oldValue.dispose();
            }
            this.oldValue = skin;
            Control.this.skinBase = null;
            if (skin instanceof SkinBase) {
                Control.this.skinBase = (SkinBase)skin;
            } else {
                object = Control.this.getSkinNode();
                if (object != null) {
                    Control.this.getChildren().setAll(object);
                } else {
                    Control.this.getChildren().clear();
                }
            }
            Control.this.styleableProperties = null;
            Control.this.impl_reapplyCSS();
            object = Logging.getControlsLogger();
            if (((PlatformLogger)object).isLoggable(PlatformLogger.Level.FINEST)) {
                ((PlatformLogger)object).finest("Stored skin[" + this.getValue() + "] on " + this);
            }
        }

        @Override
        public CssMetaData getCssMetaData() {
            return StyleableProperties.SKIN;
        }

        @Override
        public Object getBean() {
            return Control.this;
        }

        @Override
        public String getName() {
            return "skin";
        }
    };
    private ObjectProperty<Tooltip> tooltip;
    private ObjectProperty<ContextMenu> contextMenu = new SimpleObjectProperty<ContextMenu>((Object)this, "contextMenu"){
        private WeakReference<ContextMenu> contextMenuRef;

        @Override
        protected void invalidated() {
            ContextMenu contextMenu;
            ContextMenu contextMenu2 = contextMenu = this.contextMenuRef == null ? null : (ContextMenu)this.contextMenuRef.get();
            if (contextMenu != null) {
                ControlAcceleratorSupport.removeAcceleratorsFromScene(contextMenu.getItems(), Control.this);
            }
            ContextMenu contextMenu3 = (ContextMenu)this.get();
            this.contextMenuRef = new WeakReference<ContextMenu>(contextMenu3);
            if (contextMenu3 != null) {
                contextMenu3.setImpl_showRelativeToWindow(true);
                ControlAcceleratorSupport.addAcceleratorsIntoScene(contextMenu3.getItems(), Control.this);
            }
        }
    };
    private String currentSkinClassName = null;
    private StringProperty skinClassName;
    static final /* synthetic */ boolean $assertionsDisabled;

    private static Class<?> loadClass(String string, Object object) throws ClassNotFoundException {
        try {
            return Class.forName(string, false, Control.class.getClassLoader());
        }
        catch (ClassNotFoundException classNotFoundException) {
            if (Thread.currentThread().getContextClassLoader() != null) {
                try {
                    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                    return Class.forName(string, false, classLoader);
                }
                catch (ClassNotFoundException classNotFoundException2) {
                    // empty catch block
                }
            }
            if (object != null) {
                for (Class<?> class_ = object.getClass(); class_ != null; class_ = class_.getSuperclass()) {
                    try {
                        ClassLoader classLoader = class_.getClassLoader();
                        return Class.forName(string, false, classLoader);
                    }
                    catch (ClassNotFoundException classNotFoundException3) {
                        continue;
                    }
                }
            }
            throw classNotFoundException;
        }
    }

    @Override
    public final ObjectProperty<Skin<?>> skinProperty() {
        return this.skin;
    }

    @Override
    public final void setSkin(Skin<?> skin) {
        this.skinProperty().set(skin);
    }

    @Override
    public final Skin<?> getSkin() {
        return (Skin)this.skinProperty().getValue();
    }

    public final ObjectProperty<Tooltip> tooltipProperty() {
        if (this.tooltip == null) {
            this.tooltip = new ObjectPropertyBase<Tooltip>(){
                private Tooltip old = null;

                @Override
                protected void invalidated() {
                    Tooltip tooltip = (Tooltip)this.get();
                    if (tooltip != this.old) {
                        if (this.old != null) {
                            Tooltip.uninstall(Control.this, this.old);
                        }
                        if (tooltip != null) {
                            Tooltip.install(Control.this, tooltip);
                        }
                        this.old = tooltip;
                    }
                }

                @Override
                public Object getBean() {
                    return Control.this;
                }

                @Override
                public String getName() {
                    return "tooltip";
                }
            };
        }
        return this.tooltip;
    }

    public final void setTooltip(Tooltip tooltip) {
        this.tooltipProperty().setValue(tooltip);
    }

    public final Tooltip getTooltip() {
        return this.tooltip == null ? null : (Tooltip)this.tooltip.getValue();
    }

    public final ObjectProperty<ContextMenu> contextMenuProperty() {
        return this.contextMenu;
    }

    public final void setContextMenu(ContextMenu contextMenu) {
        this.contextMenu.setValue(contextMenu);
    }

    public final ContextMenu getContextMenu() {
        return this.contextMenu == null ? null : (ContextMenu)this.contextMenu.getValue();
    }

    protected Control() {
        StyleableProperty styleableProperty = (StyleableProperty)((Object)this.focusTraversableProperty());
        styleableProperty.applyStyle(null, Boolean.TRUE);
        this.addEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED, contextMenuHandler);
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    protected double computeMinWidth(double d2) {
        if (this.skinBase != null) {
            return this.skinBase.computeMinWidth(d2, this.snappedTopInset(), this.snappedRightInset(), this.snappedBottomInset(), this.snappedLeftInset());
        }
        Node node = this.getSkinNode();
        return node == null ? 0.0 : node.minWidth(d2);
    }

    @Override
    protected double computeMinHeight(double d2) {
        if (this.skinBase != null) {
            return this.skinBase.computeMinHeight(d2, this.snappedTopInset(), this.snappedRightInset(), this.snappedBottomInset(), this.snappedLeftInset());
        }
        Node node = this.getSkinNode();
        return node == null ? 0.0 : node.minHeight(d2);
    }

    @Override
    protected double computeMaxWidth(double d2) {
        if (this.skinBase != null) {
            return this.skinBase.computeMaxWidth(d2, this.snappedTopInset(), this.snappedRightInset(), this.snappedBottomInset(), this.snappedLeftInset());
        }
        Node node = this.getSkinNode();
        return node == null ? 0.0 : node.maxWidth(d2);
    }

    @Override
    protected double computeMaxHeight(double d2) {
        if (this.skinBase != null) {
            return this.skinBase.computeMaxHeight(d2, this.snappedTopInset(), this.snappedRightInset(), this.snappedBottomInset(), this.snappedLeftInset());
        }
        Node node = this.getSkinNode();
        return node == null ? 0.0 : node.maxHeight(d2);
    }

    @Override
    protected double computePrefWidth(double d2) {
        if (this.skinBase != null) {
            return this.skinBase.computePrefWidth(d2, this.snappedTopInset(), this.snappedRightInset(), this.snappedBottomInset(), this.snappedLeftInset());
        }
        Node node = this.getSkinNode();
        return node == null ? 0.0 : node.prefWidth(d2);
    }

    @Override
    protected double computePrefHeight(double d2) {
        if (this.skinBase != null) {
            return this.skinBase.computePrefHeight(d2, this.snappedTopInset(), this.snappedRightInset(), this.snappedBottomInset(), this.snappedLeftInset());
        }
        Node node = this.getSkinNode();
        return node == null ? 0.0 : node.prefHeight(d2);
    }

    @Override
    public double getBaselineOffset() {
        if (this.skinBase != null) {
            return this.skinBase.computeBaselineOffset(this.snappedTopInset(), this.snappedRightInset(), this.snappedBottomInset(), this.snappedLeftInset());
        }
        Node node = this.getSkinNode();
        return node == null ? 0.0 : node.getBaselineOffset();
    }

    @Override
    protected void layoutChildren() {
        if (this.skinBase != null) {
            double d2 = this.snappedLeftInset();
            double d3 = this.snappedTopInset();
            double d4 = this.snapSize(this.getWidth()) - d2 - this.snappedRightInset();
            double d5 = this.snapSize(this.getHeight()) - d3 - this.snappedBottomInset();
            this.skinBase.layoutChildren(d2, d3, d4, d5);
        } else {
            Node node = this.getSkinNode();
            if (node != null) {
                node.resizeRelocate(0.0, 0.0, this.getWidth(), this.getHeight());
            }
        }
    }

    protected Skin<?> createDefaultSkin() {
        return null;
    }

    ObservableList<Node> getControlChildren() {
        return this.getChildren();
    }

    private Node getSkinNode() {
        if (!$assertionsDisabled && this.skinBase != null) {
            throw new AssertionError();
        }
        Skin<?> skin = this.getSkin();
        return skin == null ? null : skin.getNode();
    }

    @Deprecated
    protected StringProperty skinClassNameProperty() {
        if (this.skinClassName == null) {
            this.skinClassName = new StyleableStringProperty(){

                @Override
                public void set(String string) {
                    if (string == null || string.isEmpty() || string.equals(this.get())) {
                        return;
                    }
                    super.set(string);
                }

                @Override
                public void invalidated() {
                    if (this.get() != null && !this.get().equals(Control.this.currentSkinClassName)) {
                        Control.loadSkinClass(Control.this, (String)Control.this.skinClassName.get());
                    }
                }

                @Override
                public Object getBean() {
                    return Control.this;
                }

                @Override
                public String getName() {
                    return "skinClassName";
                }

                @Override
                public CssMetaData<Control, String> getCssMetaData() {
                    return StyleableProperties.SKIN;
                }
            };
        }
        return this.skinClassName;
    }

    static void loadSkinClass(Skinnable skinnable, String string) {
        if (string == null || string.isEmpty()) {
            String string2 = "Empty -fx-skin property specified for control " + skinnable;
            ObservableList<CssError> observableList = StyleManager.getErrors();
            if (observableList != null) {
                CssError cssError = new CssError(string2);
                observableList.add(cssError);
            }
            Logging.getControlsLogger().severe(string2);
            return;
        }
        try {
            Class<?> class_ = Control.loadClass(string, skinnable);
            if (!Skin.class.isAssignableFrom(class_)) {
                String string3 = "'" + string + "' is not a valid Skin class for control " + skinnable;
                ObservableList<CssError> observableList = StyleManager.getErrors();
                if (observableList != null) {
                    CssError cssError = new CssError(string3);
                    observableList.add(cssError);
                }
                Logging.getControlsLogger().severe(string3);
                return;
            }
            Constructor<?>[] arrconstructor = class_.getConstructors();
            Constructor<?> constructor = null;
            for (Constructor<?> constructor2 : arrconstructor) {
                Class<?>[] arrclass = constructor2.getParameterTypes();
                if (arrclass.length != 1 || !Skinnable.class.isAssignableFrom(arrclass[0])) continue;
                constructor = constructor2;
                break;
            }
            if (constructor == null) {
                String cssError = "No valid constructor defined in '" + string + "' for control " + skinnable + ".\r\nYou must provide a constructor that accepts a single " + "Skinnable (e.g. Control or PopupControl) parameter in " + string + ".";
                ObservableList<CssError> observableList = StyleManager.getErrors();
                if (observableList != null) {
                    CssError cssError2 = new CssError(cssError);
                    observableList.add(cssError2);
                }
                Logging.getControlsLogger().severe(cssError);
            } else {
                Skin cssError = (Skin)constructor.newInstance(skinnable);
                skinnable.skinProperty().set(cssError);
            }
        }
        catch (InvocationTargetException invocationTargetException) {
            String string4 = "Failed to load skin '" + string + "' for control " + skinnable;
            ObservableList<CssError> observableList = StyleManager.getErrors();
            if (observableList != null) {
                CssError cssError = new CssError(string4 + " :" + invocationTargetException.getLocalizedMessage());
                observableList.add(cssError);
            }
            Logging.getControlsLogger().severe(string4, invocationTargetException.getCause());
        }
        catch (Exception exception) {
            String string5 = "Failed to load skin '" + string + "' for control " + skinnable;
            ObservableList<CssError> observableList = StyleManager.getErrors();
            if (observableList != null) {
                CssError cssError = new CssError(string5 + " :" + exception.getLocalizedMessage());
                observableList.add(cssError);
            }
            Logging.getControlsLogger().severe(string5, exception);
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override
    public final List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        if (this.styleableProperties == null) {
            CssMetaData<Styleable, ?> cssMetaData;
            int n2;
            int n3;
            HashMap hashMap = new HashMap();
            List<CssMetaData<Styleable, ?>> list = this.getControlCssMetaData();
            int n4 = n3 = list != null ? list.size() : 0;
            for (n2 = 0; n2 < n3; ++n2) {
                cssMetaData = list.get(n2);
                if (cssMetaData == null) continue;
                hashMap.put(cssMetaData.getProperty(), cssMetaData);
            }
            list = this.skinBase != null ? this.skinBase.getCssMetaData() : null;
            int n5 = n3 = list != null ? list.size() : 0;
            for (n2 = 0; n2 < n3; ++n2) {
                cssMetaData = list.get(n2);
                if (cssMetaData == null) continue;
                hashMap.put(cssMetaData.getProperty(), cssMetaData);
            }
            this.styleableProperties = new ArrayList();
            this.styleableProperties.addAll(hashMap.values());
        }
        return this.styleableProperties;
    }

    protected List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return Control.getClassCssMetaData();
    }

    @Override
    @Deprecated
    protected void impl_processCSS(WritableValue<Boolean> writableValue) {
        super.impl_processCSS(writableValue);
        if (this.getSkin() == null) {
            Skin<?> skin = this.createDefaultSkin();
            if (skin != null) {
                this.skinProperty().set(skin);
                super.impl_processCSS(writableValue);
            } else {
                String string = "The -fx-skin property has not been defined in CSS for " + this + " and createDefaultSkin() returned null.";
                ObservableList<CssError> observableList = StyleManager.getErrors();
                if (observableList != null) {
                    CssError cssError = new CssError(string);
                    observableList.add(cssError);
                }
                Logging.getControlsLogger().severe(string);
            }
        }
    }

    @Override
    @Deprecated
    protected Boolean impl_cssGetFocusTraversableInitialValue() {
        return Boolean.TRUE;
    }

    @Override
    public Object queryAccessibleAttribute(AccessibleAttribute accessibleAttribute, Object ... arrobject) {
        Object object;
        switch (accessibleAttribute) {
            case HELP: {
                String string = this.getAccessibleHelp();
                if (string != null && !string.isEmpty()) {
                    return string;
                }
                Tooltip tooltip = this.getTooltip();
                return tooltip == null ? "" : tooltip.getText();
            }
        }
        if (this.skinBase != null && (object = this.skinBase.queryAccessibleAttribute(accessibleAttribute, arrobject)) != null) {
            return object;
        }
        return super.queryAccessibleAttribute(accessibleAttribute, arrobject);
    }

    @Override
    public void executeAccessibleAction(AccessibleAction accessibleAction, Object ... arrobject) {
        if (this.skinBase != null) {
            this.skinBase.executeAccessibleAction(accessibleAction, arrobject);
        }
        super.executeAccessibleAction(accessibleAction, arrobject);
    }

    static {
        boolean bl = $assertionsDisabled = !Control.class.desiredAssertionStatus();
        if (Application.getUserAgentStylesheet() == null) {
            PlatformImpl.setDefaultPlatformUserAgentStylesheet();
        }
        contextMenuHandler = contextMenuEvent -> {
            Control control;
            if (contextMenuEvent.isConsumed()) {
                return;
            }
            Object object = contextMenuEvent.getSource();
            if (object instanceof Control && (control = (Control)object).getContextMenu() != null) {
                control.getContextMenu().show(control, contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY());
                contextMenuEvent.consume();
            }
        };
    }

    private static class StyleableProperties {
        private static final CssMetaData<Control, String> SKIN = new CssMetaData<Control, String>("-fx-skin", StringConverter.getInstance()){

            @Override
            public boolean isSettable(Control control) {
                return control.skin == null || !control.skin.isBound();
            }

            @Override
            public StyleableProperty<String> getStyleableProperty(Control control) {
                return (StyleableProperty)((Object)control.skinClassNameProperty());
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            ArrayList arrayList = new ArrayList(Region.getClassCssMetaData());
            arrayList.add(SKIN);
            STYLEABLES = Collections.unmodifiableList(arrayList);
        }
    }
}

