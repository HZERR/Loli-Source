/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.mac;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.mac.MacCommonDialogs;
import java.io.File;

public final class MacFileNSURL
extends File {
    private long ptr;

    private static native void _initIDs();

    private MacFileNSURL(String string, long l2) {
        super(string);
        this.ptr = l2;
        Application.checkEventThread();
    }

    private void checkNotDisposed() {
        if (this.ptr == 0L) {
            throw new RuntimeException("The NSURL object has been diposed already");
        }
    }

    private native void _dispose(long var1);

    public void dispose() {
        Application.checkEventThread();
        this.checkNotDisposed();
        this._dispose(this.ptr);
        this.ptr = 0L;
    }

    private native boolean _startAccessingSecurityScopedResource(long var1);

    public boolean startAccessingSecurityScopedResource() {
        Application.checkEventThread();
        this.checkNotDisposed();
        return this._startAccessingSecurityScopedResource(this.ptr);
    }

    private native void _stopAccessingSecurityScopedResource(long var1);

    public void stopAccessingSecurityScopedResource() {
        Application.checkEventThread();
        this.checkNotDisposed();
        this._stopAccessingSecurityScopedResource(this.ptr);
    }

    private native byte[] _getBookmark(long var1, long var3);

    public byte[] getBookmark() {
        Application.checkEventThread();
        this.checkNotDisposed();
        return this._getBookmark(this.ptr, 0L);
    }

    private static native MacFileNSURL _createFromBookmark(byte[] var0, long var1);

    public static MacFileNSURL createFromBookmark(byte[] arrby) {
        Application.checkEventThread();
        if (arrby == null) {
            throw new NullPointerException("data must not be null");
        }
        if (!MacCommonDialogs.isFileNSURLEnabled()) {
            throw new RuntimeException("The system property glass.macosx.enableFileNSURL is not 'true'");
        }
        return MacFileNSURL._createFromBookmark(arrby, 0L);
    }

    public byte[] getDocumentScopedBookmark(MacFileNSURL macFileNSURL) {
        Application.checkEventThread();
        this.checkNotDisposed();
        return this._getBookmark(this.ptr, macFileNSURL.ptr);
    }

    public static MacFileNSURL createFromDocumentScopedBookmark(byte[] arrby, MacFileNSURL macFileNSURL) {
        Application.checkEventThread();
        if (arrby == null) {
            throw new NullPointerException("data must not be null");
        }
        if (!MacCommonDialogs.isFileNSURLEnabled()) {
            throw new RuntimeException("The system property glass.macosx.enableFileNSURL is not 'true'");
        }
        return MacFileNSURL._createFromBookmark(arrby, macFileNSURL.ptr);
    }

    static {
        MacFileNSURL._initIDs();
    }
}

