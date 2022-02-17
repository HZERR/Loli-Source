/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.utils;

import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.PrintGraphics;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.print.PrinterGraphics;
import java.util.HashMap;
import java.util.Map;
import javax.swing.CellRendererPane;
import javax.swing.SwingUtilities;
import org.pushingpixels.lafwidget.utils.LookUtils;

public class RenderingUtils {
    private static final String PROP_DESKTOPHINTS = "awt.font.desktophints";
    private static Map<String, Map> desktopHintsCache = new HashMap<String, Map>();

    public static Map installDesktopHintsOld(Graphics2D g2, Component c2) {
        if (SwingUtilities.getAncestorOfClass(CellRendererPane.class, c2) != null) {
            return null;
        }
        HashMap<RenderingHints.Key, Object> oldRenderingHints = null;
        Map desktopHints = RenderingUtils.desktopHints(g2);
        if (desktopHints != null && !desktopHints.isEmpty()) {
            Font font;
            oldRenderingHints = new HashMap<RenderingHints.Key, Object>(desktopHints.size());
            for (RenderingHints.Key key : desktopHints.keySet()) {
                oldRenderingHints.put(key, g2.getRenderingHint(key));
            }
            g2.addRenderingHints(desktopHints);
            if (c2 != null && (font = c2.getFont()) != null && font.getSize() > 15) {
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            }
        } else if (LookUtils.IS_JAVA_6 && LookUtils.IS_OS_MAC) {
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }
        return oldRenderingHints;
    }

    public static void installDesktopHints(Graphics2D g2, Component c2) {
        if (SwingUtilities.getAncestorOfClass(CellRendererPane.class, c2) != null) {
            return;
        }
        Map desktopHints = RenderingUtils.desktopHints(g2);
        if (desktopHints != null && !desktopHints.isEmpty()) {
            Font font;
            g2.addRenderingHints(desktopHints);
            if (c2 != null && (font = c2.getFont()) != null && font.getSize() > 15) {
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            }
        } else if (LookUtils.IS_JAVA_6 && LookUtils.IS_OS_MAC) {
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }
    }

    private static Map desktopHints(Graphics2D g2) {
        GraphicsDevice device;
        if (RenderingUtils.isPrinting(g2)) {
            return null;
        }
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        String deviceId = "";
        GraphicsConfiguration config = g2.getDeviceConfiguration();
        if (config != null && (device = config.getDevice()) != null) {
            deviceId = device.getIDstring();
        }
        if (!desktopHintsCache.containsKey(deviceId)) {
            Object aaHint;
            HashMap desktopHints = (HashMap)toolkit.getDesktopProperty("awt.font.desktophints." + deviceId);
            if (desktopHints == null) {
                desktopHints = (Map)toolkit.getDesktopProperty(PROP_DESKTOPHINTS);
            }
            if (desktopHints != null && ((aaHint = desktopHints.get(RenderingHints.KEY_TEXT_ANTIALIASING)) == RenderingHints.VALUE_TEXT_ANTIALIAS_OFF || aaHint == RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT)) {
                desktopHints = null;
            }
            if (desktopHints == null) {
                desktopHints = new HashMap();
            }
            desktopHintsCache.put(deviceId, desktopHints);
        }
        return desktopHintsCache.get(deviceId);
    }

    private static boolean isPrinting(Graphics g2) {
        return g2 instanceof PrintGraphics || g2 instanceof PrinterGraphics;
    }
}

