/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.EventListenerImpl;
import com.sun.webkit.dom.HTMLElementImpl;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.html.HTMLFrameSetElement;

public class HTMLFrameSetElementImpl
extends HTMLElementImpl
implements HTMLFrameSetElement {
    HTMLFrameSetElementImpl(long l2) {
        super(l2);
    }

    static HTMLFrameSetElement getImpl(long l2) {
        return (HTMLFrameSetElement)HTMLFrameSetElementImpl.create(l2);
    }

    @Override
    public String getCols() {
        return HTMLFrameSetElementImpl.getColsImpl(this.getPeer());
    }

    static native String getColsImpl(long var0);

    @Override
    public void setCols(String string) {
        HTMLFrameSetElementImpl.setColsImpl(this.getPeer(), string);
    }

    static native void setColsImpl(long var0, String var2);

    @Override
    public String getRows() {
        return HTMLFrameSetElementImpl.getRowsImpl(this.getPeer());
    }

    static native String getRowsImpl(long var0);

    @Override
    public void setRows(String string) {
        HTMLFrameSetElementImpl.setRowsImpl(this.getPeer(), string);
    }

    static native void setRowsImpl(long var0, String var2);

    @Override
    public EventListener getOnblur() {
        return EventListenerImpl.getImpl(HTMLFrameSetElementImpl.getOnblurImpl(this.getPeer()));
    }

    static native long getOnblurImpl(long var0);

    @Override
    public void setOnblur(EventListener eventListener) {
        HTMLFrameSetElementImpl.setOnblurImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnblurImpl(long var0, long var2);

    @Override
    public EventListener getOnerror() {
        return EventListenerImpl.getImpl(HTMLFrameSetElementImpl.getOnerrorImpl(this.getPeer()));
    }

    static native long getOnerrorImpl(long var0);

    @Override
    public void setOnerror(EventListener eventListener) {
        HTMLFrameSetElementImpl.setOnerrorImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnerrorImpl(long var0, long var2);

    @Override
    public EventListener getOnfocus() {
        return EventListenerImpl.getImpl(HTMLFrameSetElementImpl.getOnfocusImpl(this.getPeer()));
    }

    static native long getOnfocusImpl(long var0);

    @Override
    public void setOnfocus(EventListener eventListener) {
        HTMLFrameSetElementImpl.setOnfocusImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnfocusImpl(long var0, long var2);

    @Override
    public EventListener getOnfocusin() {
        return EventListenerImpl.getImpl(HTMLFrameSetElementImpl.getOnfocusinImpl(this.getPeer()));
    }

    static native long getOnfocusinImpl(long var0);

    @Override
    public void setOnfocusin(EventListener eventListener) {
        HTMLFrameSetElementImpl.setOnfocusinImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnfocusinImpl(long var0, long var2);

    @Override
    public EventListener getOnfocusout() {
        return EventListenerImpl.getImpl(HTMLFrameSetElementImpl.getOnfocusoutImpl(this.getPeer()));
    }

    static native long getOnfocusoutImpl(long var0);

    @Override
    public void setOnfocusout(EventListener eventListener) {
        HTMLFrameSetElementImpl.setOnfocusoutImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnfocusoutImpl(long var0, long var2);

    @Override
    public EventListener getOnload() {
        return EventListenerImpl.getImpl(HTMLFrameSetElementImpl.getOnloadImpl(this.getPeer()));
    }

    static native long getOnloadImpl(long var0);

    @Override
    public void setOnload(EventListener eventListener) {
        HTMLFrameSetElementImpl.setOnloadImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnloadImpl(long var0, long var2);

    @Override
    public EventListener getOnresize() {
        return EventListenerImpl.getImpl(HTMLFrameSetElementImpl.getOnresizeImpl(this.getPeer()));
    }

    static native long getOnresizeImpl(long var0);

    @Override
    public void setOnresize(EventListener eventListener) {
        HTMLFrameSetElementImpl.setOnresizeImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnresizeImpl(long var0, long var2);

    @Override
    public EventListener getOnscroll() {
        return EventListenerImpl.getImpl(HTMLFrameSetElementImpl.getOnscrollImpl(this.getPeer()));
    }

    static native long getOnscrollImpl(long var0);

    @Override
    public void setOnscroll(EventListener eventListener) {
        HTMLFrameSetElementImpl.setOnscrollImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnscrollImpl(long var0, long var2);

    public EventListener getOnbeforeunload() {
        return EventListenerImpl.getImpl(HTMLFrameSetElementImpl.getOnbeforeunloadImpl(this.getPeer()));
    }

    static native long getOnbeforeunloadImpl(long var0);

    public void setOnbeforeunload(EventListener eventListener) {
        HTMLFrameSetElementImpl.setOnbeforeunloadImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnbeforeunloadImpl(long var0, long var2);

    public EventListener getOnhashchange() {
        return EventListenerImpl.getImpl(HTMLFrameSetElementImpl.getOnhashchangeImpl(this.getPeer()));
    }

    static native long getOnhashchangeImpl(long var0);

    public void setOnhashchange(EventListener eventListener) {
        HTMLFrameSetElementImpl.setOnhashchangeImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnhashchangeImpl(long var0, long var2);

    public EventListener getOnmessage() {
        return EventListenerImpl.getImpl(HTMLFrameSetElementImpl.getOnmessageImpl(this.getPeer()));
    }

    static native long getOnmessageImpl(long var0);

    public void setOnmessage(EventListener eventListener) {
        HTMLFrameSetElementImpl.setOnmessageImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnmessageImpl(long var0, long var2);

    public EventListener getOnoffline() {
        return EventListenerImpl.getImpl(HTMLFrameSetElementImpl.getOnofflineImpl(this.getPeer()));
    }

    static native long getOnofflineImpl(long var0);

    public void setOnoffline(EventListener eventListener) {
        HTMLFrameSetElementImpl.setOnofflineImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnofflineImpl(long var0, long var2);

    public EventListener getOnonline() {
        return EventListenerImpl.getImpl(HTMLFrameSetElementImpl.getOnonlineImpl(this.getPeer()));
    }

    static native long getOnonlineImpl(long var0);

    public void setOnonline(EventListener eventListener) {
        HTMLFrameSetElementImpl.setOnonlineImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnonlineImpl(long var0, long var2);

    public EventListener getOnpagehide() {
        return EventListenerImpl.getImpl(HTMLFrameSetElementImpl.getOnpagehideImpl(this.getPeer()));
    }

    static native long getOnpagehideImpl(long var0);

    public void setOnpagehide(EventListener eventListener) {
        HTMLFrameSetElementImpl.setOnpagehideImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnpagehideImpl(long var0, long var2);

    public EventListener getOnpageshow() {
        return EventListenerImpl.getImpl(HTMLFrameSetElementImpl.getOnpageshowImpl(this.getPeer()));
    }

    static native long getOnpageshowImpl(long var0);

    public void setOnpageshow(EventListener eventListener) {
        HTMLFrameSetElementImpl.setOnpageshowImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnpageshowImpl(long var0, long var2);

    public EventListener getOnpopstate() {
        return EventListenerImpl.getImpl(HTMLFrameSetElementImpl.getOnpopstateImpl(this.getPeer()));
    }

    static native long getOnpopstateImpl(long var0);

    public void setOnpopstate(EventListener eventListener) {
        HTMLFrameSetElementImpl.setOnpopstateImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnpopstateImpl(long var0, long var2);

    public EventListener getOnstorage() {
        return EventListenerImpl.getImpl(HTMLFrameSetElementImpl.getOnstorageImpl(this.getPeer()));
    }

    static native long getOnstorageImpl(long var0);

    public void setOnstorage(EventListener eventListener) {
        HTMLFrameSetElementImpl.setOnstorageImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnstorageImpl(long var0, long var2);

    public EventListener getOnunload() {
        return EventListenerImpl.getImpl(HTMLFrameSetElementImpl.getOnunloadImpl(this.getPeer()));
    }

    static native long getOnunloadImpl(long var0);

    public void setOnunload(EventListener eventListener) {
        HTMLFrameSetElementImpl.setOnunloadImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnunloadImpl(long var0, long var2);
}

