/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.mac;

import java.util.HashMap;

final class MacPasteboard {
    public static final int General = 1;
    public static final int DragAndDrop = 2;
    public static final int UtfIndex = 0;
    public static final int ObjectIndex = 1;
    public static final String UtfString = "public.utf8-plain-text";
    public static final String UtfPdf = "com.adobe.pdf";
    public static final String UtfTiff = "public.tiff";
    public static final String UtfPng = "public.png";
    public static final String UtfRtf = "public.rtf";
    public static final String UtfRtfd = "com.apple.flat-rtfd";
    public static final String UtfHtml = "public.html";
    public static final String UtfTabularText = "public.utf8-tab-separated-values-text";
    public static final String UtfFont = "com.apple.cocoa.pasteboard.character-formatting";
    public static final String UtfColor = "com.apple.cocoa.pasteboard.color";
    public static final String UtfSound = "com.apple.cocoa.pasteboard.sound";
    public static final String UtfMultipleTextSelection = "com.apple.cocoa.pasteboard.multiple-text-selection";
    public static final String UtfFindPanelSearchOptions = "com.apple.cocoa.pasteboard.find-panel-search-options";
    public static final String UtfUrl = "public.url";
    public static final String UtfFileUrl = "public.file-url";
    public static final String UtfRawImageType = "application.x-java-rawimage";
    public static final String UtfDragImageType = "application.x-java-drag-image";
    public static final String UtfDragImageOffset = "application.x-java-drag-image-offset";
    private long ptr = 0L;
    private boolean user;

    private static native void _initIDs();

    private native long _createSystemPasteboard(int var1);

    public MacPasteboard(int n2) {
        this.user = false;
        this.ptr = this._createSystemPasteboard(n2);
    }

    private native long _createUserPasteboard(String var1);

    public MacPasteboard(String string) {
        this.user = true;
        this.ptr = this._createUserPasteboard(string);
    }

    public long getNativePasteboard() {
        this.assertValid();
        return this.ptr;
    }

    private native String _getName(long var1);

    public String getName() {
        this.assertValid();
        return this._getName(this.ptr);
    }

    private native String[][] _getUTFs(long var1);

    public String[][] getUTFs() {
        this.assertValid();
        return this._getUTFs(this.ptr);
    }

    private native byte[] _getItemAsRawImage(long var1, int var3);

    public byte[] getItemAsRawImage(int n2) {
        this.assertValid();
        return this._getItemAsRawImage(this.ptr, n2);
    }

    private native String _getItemStringForUTF(long var1, int var3, String var4);

    public String getItemStringForUTF(int n2, String string) {
        this.assertValid();
        return this._getItemStringForUTF(this.ptr, n2, string);
    }

    private native byte[] _getItemBytesForUTF(long var1, int var3, String var4);

    public byte[] getItemBytesForUTF(int n2, String string) {
        this.assertValid();
        return this._getItemBytesForUTF(this.ptr, n2, string);
    }

    private native long _putItemsFromArray(long var1, Object[] var3, int var4);

    public long putItemsFromArray(Object[] arrobject, int n2) {
        return this._putItemsFromArray(this.ptr, arrobject, n2);
    }

    private Object[] hashMapToArray(HashMap<String, Object> hashMap) {
        Object[] arrobject = null;
        if (hashMap != null && hashMap.size() > 0) {
            arrobject = new Object[hashMap.size()];
            int n2 = 0;
            for (String string : hashMap.keySet()) {
                Object[] arrobject2 = new Object[]{string, hashMap.get(string)};
                arrobject[n2++] = arrobject2;
            }
        }
        return arrobject;
    }

    public long putItems(HashMap<String, Object>[] arrhashMap, int n2) {
        this.assertValid();
        Object[] arrobject = new Object[arrhashMap.length];
        for (int i2 = 0; i2 < arrhashMap.length; ++i2) {
            arrobject[i2] = this.hashMapToArray(arrhashMap[i2]);
        }
        return this.putItemsFromArray(arrobject, n2);
    }

    private native long _clear(long var1);

    public long clear() {
        this.assertValid();
        return this._clear(this.ptr);
    }

    private native long _getSeed(long var1);

    public long getSeed() {
        this.assertValid();
        return this._getSeed(this.ptr);
    }

    private native int _getAllowedOperation(long var1);

    public int getAllowedOperation() {
        this.assertValid();
        return this._getAllowedOperation(this.ptr);
    }

    private native void _release(long var1);

    public void release() {
        this.assertValid();
        if (this.ptr != 0L && this.user) {
            this._release(this.ptr);
        }
        this.ptr = 0L;
    }

    private void assertValid() {
        if (this.ptr == 0L) {
            throw new IllegalStateException("The MacPasteboard is not valid");
        }
    }

    static {
        MacPasteboard._initIDs();
    }
}

