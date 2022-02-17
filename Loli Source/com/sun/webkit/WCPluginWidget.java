/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit;

import com.sun.webkit.WCWidget;
import com.sun.webkit.WebPage;
import com.sun.webkit.WebPageClient;
import com.sun.webkit.graphics.WCGraphicsContext;
import com.sun.webkit.graphics.WCRectangle;
import com.sun.webkit.network.URLs;
import com.sun.webkit.plugin.Plugin;
import com.sun.webkit.plugin.PluginListener;
import com.sun.webkit.plugin.PluginManager;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

final class WCPluginWidget
extends WCWidget
implements PluginListener {
    private static final Logger log = Logger.getLogger(WCPluginWidget.class.getName());
    private final Plugin plugin;
    private long pData = 0L;

    private static native void initIDs();

    private WCPluginWidget(WebPage webPage, Plugin plugin, int n2, int n3) {
        super(webPage);
        this.plugin = plugin;
        this.setBounds(0, 0, n2, n3);
        WebPageClient webPageClient = webPage.getPageClient();
        this.plugin.activate(null == webPageClient ? null : webPageClient.getContainer(), this);
    }

    @Override
    protected void requestFocus() {
        this.plugin.requestFocus();
    }

    private static WCPluginWidget create(WebPage webPage, int n2, int n3, String string, String string2, String[] arrstring, String[] arrstring2) {
        URL uRL = null;
        try {
            uRL = URLs.newURL(string);
        }
        catch (MalformedURLException malformedURLException) {
            log.log(Level.FINE, null, malformedURLException);
        }
        return new WCPluginWidget(webPage, PluginManager.createPlugin(uRL, string2, arrstring, arrstring2), n2, n3);
    }

    private void fwkSetNativeContainerBounds(int n2, int n3, int n4, int n5) {
        this.plugin.setNativeContainerBounds(n2, n3, n4, n5);
    }

    @Override
    void setBounds(int n2, int n3, int n4, int n5) {
        super.setBounds(n2, n3, n4, n5);
        this.plugin.setBounds(n2, n3, n4, n5);
    }

    private void setEnabled(boolean bl) {
        this.plugin.setEnabled(bl);
    }

    @Override
    protected void setVisible(boolean bl) {
        this.plugin.setVisible(bl);
    }

    @Override
    protected void destroy() {
        this.pData = 0L;
        this.plugin.destroy();
    }

    private void paint(WCGraphicsContext wCGraphicsContext, int n2, int n3, int n4, int n5) {
        WCRectangle wCRectangle = this.getBounds();
        WCRectangle wCRectangle2 = wCRectangle.intersection(new WCRectangle(n2, n3, n4, n5));
        if (!wCRectangle2.isEmpty()) {
            wCGraphicsContext.translate(wCRectangle.getX(), wCRectangle.getY());
            wCRectangle2.translate(-wCRectangle.getX(), -wCRectangle.getY());
            wCGraphicsContext.setClip(wCRectangle2.getIntX(), wCRectangle2.getIntY(), wCRectangle2.getIntWidth(), wCRectangle2.getIntHeight());
            this.plugin.paint(wCGraphicsContext, wCRectangle2.getIntX(), wCRectangle2.getIntY(), wCRectangle2.getIntWidth(), wCRectangle2.getIntHeight());
        }
    }

    private native WCRectangle twkConvertToPage(WCRectangle var1);

    private native void twkInvalidateWindowlessPluginRect(int var1, int var2, int var3, int var4);

    private boolean fwkHandleMouseEvent(String string, int n2, int n3, int n4, int n5, int n6, boolean bl, boolean bl2, boolean bl3, boolean bl4, boolean bl5, long l2) {
        return this.plugin.handleMouseEvent(string, n2, n3, n4, n5, n6, bl, bl2, bl3, bl4, bl5, l2);
    }

    @Override
    public void fwkRedraw(int n2, int n3, int n4, int n5, boolean bl) {
        this.twkInvalidateWindowlessPluginRect(n2, n3, n4, n5);
    }

    private native void twkSetPlugunFocused(boolean var1);

    @Override
    public String fwkEvent(int n2, String string, String string2) {
        if (-1 == n2 && Boolean.parseBoolean(string2)) {
            this.twkSetPlugunFocused(Boolean.valueOf(string2));
        }
        return "";
    }

    static {
        WCPluginWidget.initIDs();
    }
}

