/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Cursor;
import com.sun.glass.ui.Size;
import com.sun.javafx.cursor.CursorFrame;
import com.sun.javafx.cursor.ImageCursorFrame;
import com.sun.javafx.iio.common.PushbroomScaler;
import com.sun.javafx.iio.common.ScalerFactory;
import com.sun.prism.Image;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import javafx.geometry.Dimension2D;

final class CursorUtils {
    private CursorUtils() {
    }

    public static Cursor getPlatformCursor(CursorFrame cursorFrame) {
        Cursor cursor = cursorFrame.getPlatformCursor(Cursor.class);
        if (cursor != null) {
            return cursor;
        }
        Cursor cursor2 = CursorUtils.createPlatformCursor(cursorFrame);
        cursorFrame.setPlatforCursor(Cursor.class, cursor2);
        return cursor2;
    }

    public static Dimension2D getBestCursorSize(int n2, int n3) {
        Size size = Cursor.getBestSize(n2, n3);
        return new Dimension2D(size.width, size.height);
    }

    private static Cursor createPlatformCursor(CursorFrame cursorFrame) {
        Application application = Application.GetApplication();
        switch (cursorFrame.getCursorType()) {
            case CROSSHAIR: {
                return application.createCursor(3);
            }
            case TEXT: {
                return application.createCursor(2);
            }
            case WAIT: {
                return application.createCursor(14);
            }
            case DEFAULT: {
                return application.createCursor(1);
            }
            case OPEN_HAND: {
                return application.createCursor(5);
            }
            case CLOSED_HAND: {
                return application.createCursor(4);
            }
            case HAND: {
                return application.createCursor(6);
            }
            case H_RESIZE: {
                return application.createCursor(11);
            }
            case V_RESIZE: {
                return application.createCursor(12);
            }
            case MOVE: {
                return application.createCursor(19);
            }
            case DISAPPEAR: {
                return application.createCursor(13);
            }
            case SW_RESIZE: {
                return application.createCursor(15);
            }
            case SE_RESIZE: {
                return application.createCursor(16);
            }
            case NW_RESIZE: {
                return application.createCursor(17);
            }
            case NE_RESIZE: {
                return application.createCursor(18);
            }
            case N_RESIZE: 
            case S_RESIZE: {
                return application.createCursor(12);
            }
            case W_RESIZE: 
            case E_RESIZE: {
                return application.createCursor(11);
            }
            case NONE: {
                return application.createCursor(-1);
            }
            case IMAGE: {
                return CursorUtils.createPlatformImageCursor((ImageCursorFrame)cursorFrame);
            }
        }
        System.err.println("unhandled Cursor: " + (Object)((Object)cursorFrame.getCursorType()));
        return application.createCursor(1);
    }

    private static Cursor createPlatformImageCursor(ImageCursorFrame imageCursorFrame) {
        return CursorUtils.createPlatformImageCursor(imageCursorFrame.getPlatformImage(), (float)imageCursorFrame.getHotspotX(), (float)imageCursorFrame.getHotspotY());
    }

    private static Cursor createPlatformImageCursor(Object object, float f2, float f3) {
        ByteBuffer byteBuffer;
        if (object == null) {
            throw new IllegalArgumentException("QuantumToolkit.createImageCursor: no image");
        }
        assert (object instanceof Image);
        Image image = (Image)object;
        int n2 = image.getHeight();
        int n3 = image.getWidth();
        Dimension2D dimension2D = CursorUtils.getBestCursorSize(n3, n2);
        float f4 = (float)dimension2D.getWidth();
        float f5 = (float)dimension2D.getHeight();
        if (f4 <= 0.0f || f5 <= 0.0f) {
            return Application.GetApplication().createCursor(1);
        }
        switch (image.getPixelFormat()) {
            case INT_ARGB_PRE: {
                return CursorUtils.createPlatformImageCursor((int)f2, (int)f3, n3, n2, image.getPixelBuffer());
            }
            case BYTE_RGB: 
            case BYTE_BGRA_PRE: 
            case BYTE_GRAY: {
                byteBuffer = (ByteBuffer)image.getPixelBuffer();
                break;
            }
            default: {
                throw new IllegalArgumentException("QuantumToolkit.createImageCursor: bad image format");
            }
        }
        float f6 = f4 / (float)n3;
        float f7 = f5 / (float)n2;
        int n4 = (int)(f2 * f6);
        int n5 = (int)(f3 * f7);
        PushbroomScaler pushbroomScaler = ScalerFactory.createScaler(n3, n2, image.getBytesPerPixelUnit(), (int)f4, (int)f5, true);
        byte[] arrby = new byte[byteBuffer.limit()];
        int n6 = image.getScanlineStride();
        for (int i2 = 0; i2 < n2; ++i2) {
            byteBuffer.position(i2 * n6);
            byteBuffer.get(arrby, 0, n6);
            if (pushbroomScaler == null) continue;
            pushbroomScaler.putSourceScanline(arrby, 0);
        }
        byteBuffer.rewind();
        Image image2 = image.iconify(pushbroomScaler.getDestination(), (int)f4, (int)f5);
        return CursorUtils.createPlatformImageCursor(n4, n5, image2.getWidth(), image2.getHeight(), image2.getPixelBuffer());
    }

    private static Cursor createPlatformImageCursor(int n2, int n3, int n4, int n5, Object object) {
        Application application = Application.GetApplication();
        return application.createCursor(n2, n3, application.createPixels(n4, n5, (IntBuffer)object));
    }
}

