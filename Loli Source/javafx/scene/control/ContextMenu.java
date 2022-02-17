/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import com.sun.javafx.beans.IDProperty;
import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.scene.control.skin.ContextMenuSkin;
import com.sun.javafx.util.Utils;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PopupControl;
import javafx.scene.control.Skin;
import javafx.stage.Window;

@IDProperty(value="id")
public class ContextMenu
extends PopupControl {
    private ObjectProperty<EventHandler<ActionEvent>> onAction = new ObjectPropertyBase<EventHandler<ActionEvent>>(){

        @Override
        protected void invalidated() {
            ContextMenu.this.setEventHandler(ActionEvent.ACTION, (EventHandler)this.get());
        }

        @Override
        public Object getBean() {
            return ContextMenu.this;
        }

        @Override
        public String getName() {
            return "onAction";
        }
    };
    private final ObservableList<MenuItem> items = new TrackableObservableList<MenuItem>(){

        @Override
        protected void onChanged(ListChangeListener.Change<MenuItem> change) {
            while (change.next()) {
                for (MenuItem menuItem : change.getRemoved()) {
                    menuItem.setParentPopup(null);
                }
                for (MenuItem menuItem : change.getAddedSubList()) {
                    if (menuItem.getParentPopup() != null) {
                        menuItem.getParentPopup().getItems().remove(menuItem);
                    }
                    menuItem.setParentPopup(ContextMenu.this);
                }
            }
        }
    };
    @Deprecated
    private final BooleanProperty impl_showRelativeToWindow = new SimpleBooleanProperty(false);
    private static final String DEFAULT_STYLE_CLASS = "context-menu";

    public ContextMenu() {
        this.getStyleClass().setAll(DEFAULT_STYLE_CLASS);
        this.setAutoHide(true);
        this.setConsumeAutoHidingEvents(false);
    }

    public ContextMenu(MenuItem ... arrmenuItem) {
        this();
        this.items.addAll(arrmenuItem);
    }

    public final void setOnAction(EventHandler<ActionEvent> eventHandler) {
        this.onActionProperty().set(eventHandler);
    }

    public final EventHandler<ActionEvent> getOnAction() {
        return (EventHandler)this.onActionProperty().get();
    }

    public final ObjectProperty<EventHandler<ActionEvent>> onActionProperty() {
        return this.onAction;
    }

    public final ObservableList<MenuItem> getItems() {
        return this.items;
    }

    public final boolean isImpl_showRelativeToWindow() {
        return this.impl_showRelativeToWindow.get();
    }

    public final void setImpl_showRelativeToWindow(boolean bl) {
        this.impl_showRelativeToWindow.set(bl);
    }

    public final BooleanProperty impl_showRelativeToWindowProperty() {
        return this.impl_showRelativeToWindow;
    }

    public void show(Node node, Side side, double d2, double d3) {
        HPos hPos;
        if (node == null) {
            return;
        }
        if (this.getItems().size() == 0) {
            return;
        }
        this.getScene().setNodeOrientation(node.getEffectiveNodeOrientation());
        HPos hPos2 = side == Side.LEFT ? HPos.LEFT : (hPos = side == Side.RIGHT ? HPos.RIGHT : HPos.CENTER);
        VPos vPos = side == Side.TOP ? VPos.TOP : (side == Side.BOTTOM ? VPos.BOTTOM : VPos.CENTER);
        Point2D point2D = Utils.pointRelativeTo(node, this.prefWidth(-1.0), this.prefHeight(-1.0), hPos, vPos, d2, d3, true);
        this.doShow(node, point2D.getX(), point2D.getY());
    }

    @Override
    public void show(Node node, double d2, double d3) {
        if (node == null) {
            return;
        }
        if (this.getItems().size() == 0) {
            return;
        }
        this.getScene().setNodeOrientation(node.getEffectiveNodeOrientation());
        this.doShow(node, d2, d3);
    }

    private void doShow(Node node, double d2, double d3) {
        Event.fireEvent(this, new Event(Menu.ON_SHOWING));
        if (this.isImpl_showRelativeToWindow()) {
            Window window;
            Scene scene = node == null ? null : node.getScene();
            Window window2 = window = scene == null ? null : scene.getWindow();
            if (window == null) {
                return;
            }
            super.show(window, d2, d3);
        } else {
            super.show(node, d2, d3);
        }
        Event.fireEvent(this, new Event(Menu.ON_SHOWN));
    }

    @Override
    public void hide() {
        if (!this.isShowing()) {
            return;
        }
        Event.fireEvent(this, new Event(Menu.ON_HIDING));
        super.hide();
        Event.fireEvent(this, new Event(Menu.ON_HIDDEN));
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new ContextMenuSkin(this);
    }
}

