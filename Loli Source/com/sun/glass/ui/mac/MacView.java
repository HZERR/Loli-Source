/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.mac;

import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.View;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Map;
import java.util.TreeSet;

final class MacView
extends View {
    private static final long multiClickTime;
    private static final int multiClickMaxX;
    private static final int multiClickMaxY;

    MacView() {
    }

    private static native void _initIDs();

    private static native long _getMultiClickTime_impl();

    private static native int _getMultiClickMaxX_impl();

    private static native int _getMultiClickMaxY_impl();

    static long getMultiClickTime_impl() {
        return multiClickTime;
    }

    static int getMultiClickMaxX_impl() {
        return multiClickMaxX;
    }

    static int getMultiClickMaxY_impl() {
        return multiClickMaxY;
    }

    @Override
    protected native int _getNativeFrameBuffer(long var1);

    @Override
    protected native long _create(Map var1);

    @Override
    protected native int _getX(long var1);

    @Override
    protected native int _getY(long var1);

    @Override
    protected native void _setParent(long var1, long var3);

    @Override
    protected native boolean _close(long var1);

    @Override
    protected native void _scheduleRepaint(long var1);

    @Override
    protected native void _begin(long var1);

    @Override
    protected native void _end(long var1);

    @Override
    protected native boolean _enterFullscreen(long var1, boolean var3, boolean var4, boolean var5);

    @Override
    protected native void _exitFullscreen(long var1, boolean var3);

    @Override
    protected native void _enableInputMethodEvents(long var1, boolean var3);

    @Override
    protected void _uploadPixels(long l2, Pixels pixels) {
        Buffer buffer = pixels.getPixels();
        if (buffer.isDirect()) {
            this._uploadPixelsDirect(l2, buffer, pixels.getWidth(), pixels.getHeight(), pixels.getScale());
        } else if (buffer.hasArray()) {
            if (pixels.getBytesPerComponent() == 1) {
                ByteBuffer byteBuffer = (ByteBuffer)buffer;
                this._uploadPixelsByteArray(l2, byteBuffer.array(), byteBuffer.arrayOffset(), pixels.getWidth(), pixels.getHeight(), pixels.getScale());
            } else {
                IntBuffer intBuffer = (IntBuffer)buffer;
                this._uploadPixelsIntArray(l2, intBuffer.array(), intBuffer.arrayOffset(), pixels.getWidth(), pixels.getHeight(), pixels.getScale());
            }
        } else {
            this._uploadPixelsDirect(l2, pixels.asByteBuffer(), pixels.getWidth(), pixels.getHeight(), pixels.getScale());
        }
    }

    native void _uploadPixelsDirect(long var1, Buffer var3, int var4, int var5, float var6);

    native void _uploadPixelsByteArray(long var1, byte[] var3, int var4, int var5, int var6, float var7);

    native void _uploadPixelsIntArray(long var1, int[] var3, int var4, int var5, int var6, float var7);

    @Override
    protected long _getNativeView(long l2) {
        return l2;
    }

    protected native long _getNativeLayer(long var1);

    public long getNativeLayer() {
        return this._getNativeLayer(this.getNativeView());
    }

    protected native int _getNativeRemoteLayerId(long var1, String var3);

    @Override
    public int getNativeRemoteLayerId(String string) {
        return this._getNativeRemoteLayerId(this.getNativeLayer(), string);
    }

    protected native void _hostRemoteLayerId(long var1, int var3);

    public void hostRemoteLayerId(int n2) {
        this._hostRemoteLayerId(this.getNativeLayer(), n2);
    }

    protected void notifyInputMethodMac(String string, int n2, int n3, int n4, int n5, int n6) {
        byte[] arrby = new byte[]{(byte)n2};
        int[] arrn = new int[]{0, n3};
        if (n2 == 4) {
            this.notifyInputMethod(string, null, arrn, arrby, n3, n4, 0);
        } else if (n6 > 0 && string != null && string.length() > 0 && n5 >= 0 && n6 + n5 <= string.length()) {
            TreeSet<Integer> treeSet = new TreeSet<Integer>();
            treeSet.add(0);
            treeSet.add(n5);
            treeSet.add(n5 + n6);
            treeSet.add(string.length());
            int[] arrn2 = new int[treeSet.size()];
            int n7 = 0;
            Object object = treeSet.iterator();
            while (object.hasNext()) {
                int n8;
                arrn2[n7] = n8 = ((Integer)object.next()).intValue();
                ++n7;
            }
            object = new byte[arrn2.length - 1];
            for (n7 = 0; n7 < arrn2.length - 1; ++n7) {
                object[n7] = arrn2[n7] == n5 ? 1 : 2;
            }
            this.notifyInputMethod(string, arrn2, arrn2, (byte[])object, 0, n4, 0);
        } else {
            this.notifyInputMethod(string, null, arrn, arrby, 0, n4, 0);
        }
    }

    static {
        MacView._initIDs();
        multiClickTime = MacView._getMultiClickTime_impl();
        multiClickMaxX = MacView._getMultiClickMaxX_impl();
        multiClickMaxY = MacView._getMultiClickMaxY_impl();
    }
}

