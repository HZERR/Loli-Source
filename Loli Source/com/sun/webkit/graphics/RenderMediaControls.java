/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.graphics;

import com.sun.webkit.graphics.WCGraphicsContext;
import com.sun.webkit.graphics.WCGraphicsManager;
import com.sun.webkit.graphics.WCImage;
import com.sun.webkit.graphics.WCImageDecoder;
import com.sun.webkit.graphics.WCImageFrame;
import java.util.HashMap;
import java.util.Map;

final class RenderMediaControls {
    private static final int PLAY_BUTTON = 1;
    private static final int PAUSE_BUTTON = 2;
    private static final int DISABLED_PLAY_BUTTON = 3;
    private static final int MUTE_BUTTON = 4;
    private static final int UNMUTE_BUTTON = 5;
    private static final int DISABLED_MUTE_BUTTON = 6;
    private static final int TIME_SLIDER_TRACK = 9;
    private static final int TIME_SLIDER_THUMB = 10;
    private static final int VOLUME_CONTAINER = 11;
    private static final int VOLUME_TRACK = 12;
    private static final int VOLUME_THUMB = 13;
    private static final int TimeSliderTrackUnbufferedColor = RenderMediaControls.rgba(236, 135, 125);
    private static final int TimeSliderTrackBufferedColor = RenderMediaControls.rgba(249, 26, 2);
    private static final int TimeSliderTrackThickness = 3;
    private static final int VolumeTrackColor = RenderMediaControls.rgba(208, 208, 208, 128);
    private static final int VolumeTrackThickness = 1;
    private static final int SLIDER_TYPE_TIME = 0;
    private static final int SLIDER_TYPE_VOLUME = 1;
    private static final Map<String, WCImage> controlImages = new HashMap<String, WCImage>();
    private static final boolean log = false;

    private static String getControlName(int n2) {
        switch (n2) {
            case 1: {
                return "PLAY_BUTTON";
            }
            case 2: {
                return "PAUSE_BUTTON";
            }
            case 3: {
                return "DISABLED_PLAY_BUTTON";
            }
            case 4: {
                return "MUTE_BUTTON";
            }
            case 5: {
                return "UNMUTE_BUTTON";
            }
            case 6: {
                return "DISABLED_MUTE_BUTTON";
            }
            case 9: {
                return "TIME_SLIDER_TRACK";
            }
            case 10: {
                return "TIME_SLIDER_THUMB";
            }
            case 11: {
                return "VOLUME_CONTAINER";
            }
            case 12: {
                return "VOLUME_TRACK";
            }
            case 13: {
                return "VOLUME_THUMB";
            }
        }
        return "{UNKNOWN CONTROL " + n2 + "}";
    }

    private RenderMediaControls() {
    }

    static void paintControl(WCGraphicsContext wCGraphicsContext, int n2, int n3, int n4, int n5, int n6) {
        switch (n2) {
            case 1: {
                RenderMediaControls.paintControlImage("mediaPlay", wCGraphicsContext, n3, n4, n5, n6);
                break;
            }
            case 2: {
                RenderMediaControls.paintControlImage("mediaPause", wCGraphicsContext, n3, n4, n5, n6);
                break;
            }
            case 3: {
                RenderMediaControls.paintControlImage("mediaPlayDisabled", wCGraphicsContext, n3, n4, n5, n6);
                break;
            }
            case 4: {
                RenderMediaControls.paintControlImage("mediaMute", wCGraphicsContext, n3, n4, n5, n6);
                break;
            }
            case 5: {
                RenderMediaControls.paintControlImage("mediaUnmute", wCGraphicsContext, n3, n4, n5, n6);
                break;
            }
            case 6: {
                RenderMediaControls.paintControlImage("mediaMuteDisabled", wCGraphicsContext, n3, n4, n5, n6);
                break;
            }
            case 10: {
                RenderMediaControls.paintControlImage("mediaTimeThumb", wCGraphicsContext, n3, n4, n5, n6);
                break;
            }
            case 11: {
                break;
            }
            case 13: {
                RenderMediaControls.paintControlImage("mediaVolumeThumb", wCGraphicsContext, n3, n4, n5, n6);
                break;
            }
        }
    }

    static void paintTimeSliderTrack(WCGraphicsContext wCGraphicsContext, float f2, float f3, float[] arrf, int n2, int n3, int n4, int n5) {
        n3 += (n5 - 3) / 2;
        n5 = 3;
        int n6 = RenderMediaControls.fwkGetSliderThumbSize(0) >> 16 & 0xFFFF;
        n4 -= n6;
        n2 += n6 / 2;
        if (!(f2 < 0.0f)) {
            float f4 = 1.0f / f2 * (float)n4;
            float f5 = 0.0f;
            for (int i2 = 0; i2 < arrf.length; i2 += 2) {
                wCGraphicsContext.fillRect((float)n2 + f4 * f5, n3, f4 * (arrf[i2] - f5), n5, TimeSliderTrackUnbufferedColor);
                wCGraphicsContext.fillRect((float)n2 + f4 * arrf[i2], n3, f4 * (arrf[i2 + 1] - arrf[i2]), n5, TimeSliderTrackBufferedColor);
                f5 = arrf[i2 + 1];
            }
            if (f5 < f2) {
                wCGraphicsContext.fillRect((float)n2 + f4 * f5, n3, f4 * (f2 - f5), n5, TimeSliderTrackUnbufferedColor);
            }
        }
    }

    static void paintVolumeTrack(WCGraphicsContext wCGraphicsContext, float f2, boolean bl, int n2, int n3, int n4, int n5) {
        n2 += (n4 + 1 - 1) / 2;
        n4 = 1;
        int n6 = RenderMediaControls.fwkGetSliderThumbSize(0) & 0xFFFF;
        wCGraphicsContext.fillRect(n2, n3 += n6 / 2, n4, n5 -= n6, VolumeTrackColor);
    }

    private static int fwkGetSliderThumbSize(int n2) {
        WCImage wCImage = null;
        switch (n2) {
            case 0: {
                wCImage = RenderMediaControls.getControlImage("mediaTimeThumb");
                break;
            }
            case 1: {
                wCImage = RenderMediaControls.getControlImage("mediaVolumeThumb");
            }
        }
        if (wCImage != null) {
            return wCImage.getWidth() << 16 | wCImage.getHeight();
        }
        return 0;
    }

    private static WCImage getControlImage(String string) {
        WCImage wCImage = controlImages.get(string);
        if (wCImage == null) {
            WCImageDecoder wCImageDecoder = WCGraphicsManager.getGraphicsManager().getImageDecoder();
            wCImageDecoder.loadFromResource(string);
            WCImageFrame wCImageFrame = wCImageDecoder.getFrame(0, null);
            if (wCImageFrame != null) {
                wCImage = wCImageFrame.getFrame();
                controlImages.put(string, wCImage);
            }
        }
        return wCImage;
    }

    private static void paintControlImage(String string, WCGraphicsContext wCGraphicsContext, int n2, int n3, int n4, int n5) {
        WCImage wCImage = RenderMediaControls.getControlImage(string);
        if (wCImage != null) {
            n2 += (n4 - wCImage.getWidth()) / 2;
            n4 = wCImage.getWidth();
            n3 += (n5 - wCImage.getHeight()) / 2;
            n5 = wCImage.getHeight();
            wCGraphicsContext.drawImage(wCImage, n2, n3, n4, n5, 0.0f, 0.0f, wCImage.getWidth(), wCImage.getHeight());
        }
    }

    private static int rgba(int n2, int n3, int n4, int n5) {
        return (n5 & 0xFF) << 24 | (n2 & 0xFF) << 16 | (n3 & 0xFF) << 8 | n4 & 0xFF;
    }

    private static int rgba(int n2, int n3, int n4) {
        return RenderMediaControls.rgba(n2, n3, n4, 255);
    }

    private static void log(String string) {
        System.out.println(string);
        System.out.flush();
    }
}

