/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit;

import com.sun.webkit.ContextMenuItem;
import com.sun.webkit.Utilities;
import com.sun.webkit.WebPage;

public abstract class ContextMenu {
    protected abstract void show(ShowContext var1, int var2, int var3);

    protected abstract void appendItem(ContextMenuItem var1);

    protected abstract void insertItem(ContextMenuItem var1, int var2);

    protected abstract int getItemCount();

    private static ContextMenu fwkCreateContextMenu() {
        return Utilities.getUtilities().createContextMenu();
    }

    private void fwkShow(WebPage webPage, long l2, int n2, int n3) {
        this.show(new ShowContext(webPage, l2), n2, n3);
    }

    private void fwkAppendItem(ContextMenuItem contextMenuItem) {
        this.appendItem(contextMenuItem);
    }

    private void fwkInsertItem(ContextMenuItem contextMenuItem, int n2) {
        this.insertItem(contextMenuItem, n2);
    }

    private int fwkGetItemCount() {
        return this.getItemCount();
    }

    private native void twkHandleItemSelected(long var1, int var3);

    public final class ShowContext {
        private final WebPage page;
        private final long pdata;

        private ShowContext(WebPage webPage, long l2) {
            this.page = webPage;
            this.pdata = l2;
        }

        public WebPage getPage() {
            return this.page;
        }

        public void notifyItemSelected(int n2) {
            ContextMenu.this.twkHandleItemSelected(this.pdata, n2);
        }
    }
}

