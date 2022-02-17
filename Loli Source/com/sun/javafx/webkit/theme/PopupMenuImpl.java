/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.webkit.theme;

import com.sun.javafx.webkit.theme.ContextMenuImpl;
import com.sun.webkit.Invoker;
import com.sun.webkit.PopupMenu;
import com.sun.webkit.WebPage;
import com.sun.webkit.WebPageClient;
import com.sun.webkit.graphics.WCFont;
import com.sun.webkit.graphics.WCPoint;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.web.WebView;

public final class PopupMenuImpl
extends PopupMenu {
    private static final Logger log = Logger.getLogger(PopupMenuImpl.class.getName());
    private final ContextMenu popupMenu = new ContextMenu();

    public PopupMenuImpl() {
        this.popupMenu.setOnHidden(windowEvent -> {
            log.finer("onHidden");
            Invoker.getInvoker().postOnEventThread(() -> {
                log.finer("onHidden: notifying");
                this.notifyPopupClosed();
            });
        });
        this.popupMenu.setOnAction(actionEvent -> {
            MenuItem menuItem = (MenuItem)actionEvent.getTarget();
            log.log(Level.FINE, "onAction: item={0}", menuItem);
            this.notifySelectionCommited(this.popupMenu.getItems().indexOf(menuItem));
        });
    }

    @Override
    protected void show(WebPage webPage, int n2, int n3, int n4) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "show at [{0}, {1}], width={2}", new Object[]{n2, n3, n4});
        }
        this.popupMenu.setPrefWidth(n4);
        this.popupMenu.setPrefHeight(this.popupMenu.getHeight());
        PopupMenuImpl.doShow(this.popupMenu, webPage, n2, n3);
    }

    @Override
    protected void hide() {
        log.fine("hiding");
        this.popupMenu.hide();
    }

    @Override
    protected void appendItem(String string, boolean bl, boolean bl2, boolean bl3, int n2, int n3, WCFont wCFont) {
        MenuItem menuItem;
        if (log.isLoggable(Level.FINEST)) {
            log.log(Level.FINEST, "itemText={0}, isLabel={1}, isSeparator={2}, isEnabled={3}, bgColor={4}, fgColor={5}, font={6}", new Object[]{string, bl, bl2, bl3, n2, n3, wCFont});
        }
        if (bl2) {
            menuItem = new ContextMenuImpl.SeparatorImpl(null);
        } else {
            menuItem = new MenuItem(string);
            menuItem.setDisable(!bl3);
        }
        this.popupMenu.getItems().add(menuItem);
    }

    @Override
    protected void setSelectedItem(int n2) {
        log.log(Level.FINEST, "index={0}", n2);
    }

    static void doShow(ContextMenu contextMenu, WebPage webPage, int n2, int n3) {
        WebPageClient webPageClient = webPage.getPageClient();
        assert (webPageClient != null);
        WCPoint wCPoint = webPageClient.windowToScreen(new WCPoint(n2, n3));
        contextMenu.show(((WebView)webPageClient.getContainer()).getScene().getWindow(), (double)wCPoint.getX(), (double)wCPoint.getY());
    }
}

