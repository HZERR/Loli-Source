/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.animation.effects;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import org.pushingpixels.lafwidget.LafWidgetUtilities;
import org.pushingpixels.lafwidget.animation.AnimationConfigurationManager;
import org.pushingpixels.lafwidget.animation.AnimationFacet;
import org.pushingpixels.lafwidget.animation.effects.GhostingListener;
import org.pushingpixels.trident.Timeline;

public class GhostPaintingUtils {
    public static float MIN_ICON_GHOSTING_ALPHA = 0.15f;
    public static float MAX_ICON_GHOSTING_ALPHA = 0.5f;
    public static float MIN_PRESS_GHOSTING_ALPHA = 0.15f;
    public static float MAX_PRESS_GHOSTING_ALPHA = 0.3f;
    public static float DECAY_FACTOR = 1.0f;
    private static LinkedHashMap<String, BufferedImage> componentGhostCache = new LinkedHashMap<String, BufferedImage>(){

        @Override
        protected boolean removeEldestEntry(Map.Entry<String, BufferedImage> eldest) {
            return this.size() > 50;
        }
    };
    private static LinkedHashMap<String, BufferedImage> iconGhostCache = new LinkedHashMap<String, BufferedImage>(){

        @Override
        protected boolean removeEldestEntry(Map.Entry<String, BufferedImage> eldest) {
            return this.size() > 50;
        }
    };

    protected static synchronized BufferedImage getComponentGhostImage(JComponent comp, Timeline ghostPressTimeline, double scaleFactor) {
        String key = ghostPressTimeline.getTimelinePosition() + ":" + comp.hashCode() + ":" + scaleFactor;
        BufferedImage result = componentGhostCache.get(key);
        if (result == null) {
            Rectangle bounds = comp.getBounds();
            double iWidth = (double)bounds.width * scaleFactor;
            double iHeight = (double)bounds.height * scaleFactor;
            result = LafWidgetUtilities.getBlankImage((int)iWidth, (int)iHeight);
            Graphics2D iGraphics = result.createGraphics();
            iGraphics.scale(scaleFactor, scaleFactor);
            comp.paint(iGraphics);
            iGraphics.dispose();
            componentGhostCache.put(key, result);
        }
        return result;
    }

    protected static synchronized BufferedImage getIconGhostImage(JComponent comp, Timeline ghostRolloverTimeline, Icon icon, double scaleFactor) {
        String key = ghostRolloverTimeline.getTimelinePosition() + ":" + comp.hashCode() + ":" + icon.hashCode() + ":" + scaleFactor;
        BufferedImage result = iconGhostCache.get(key);
        if (result == null) {
            int oWidth = icon.getIconWidth();
            int oHeight = icon.getIconHeight();
            double iWidth = (double)oWidth * scaleFactor;
            double iHeight = (double)oHeight * scaleFactor;
            result = LafWidgetUtilities.getBlankImage((int)iWidth, (int)iHeight);
            Graphics2D iGraphics = result.createGraphics();
            iGraphics.scale(scaleFactor, scaleFactor);
            icon.paintIcon(comp, iGraphics, 0, 0);
            iGraphics.dispose();
            iconGhostCache.put(key, result);
        }
        return result;
    }

    public static void paintGhostImages(Component mainComponent, Graphics g2) {
        Timeline timeline;
        JComponent comp;
        if (!mainComponent.isShowing()) {
            return;
        }
        if (!mainComponent.isVisible()) {
            return;
        }
        if (!mainComponent.isDisplayable()) {
            return;
        }
        if (SwingUtilities.getWindowAncestor(mainComponent) == null) {
            return;
        }
        Graphics2D graphics = (Graphics2D)g2.create();
        Rectangle mainRect = mainComponent.getBounds();
        mainRect.setLocation(mainComponent.getLocationOnScreen());
        if (AnimationConfigurationManager.getInstance().isAnimationAllowed(AnimationFacet.GHOSTING_BUTTON_PRESS, mainComponent)) {
            Map<JComponent, Timeline> runningGhostPressTimelines = GhostingListener.getRunningGhostPressTimelines();
            for (Map.Entry<JComponent, Timeline> entry : runningGhostPressTimelines.entrySet()) {
                comp = entry.getKey();
                timeline = entry.getValue();
                if (comp == mainComponent || !comp.isShowing() || !comp.isVisible()) continue;
                if (!comp.isDisplayable()) {
                    return;
                }
                Rectangle compRect = comp.getBounds();
                compRect.setLocation(comp.getLocationOnScreen());
                int dx = compRect.x - mainRect.x;
                int dy = compRect.y - mainRect.y;
                compRect.x -= compRect.width / 2;
                compRect.y -= compRect.height / 2;
                compRect.width *= 2;
                compRect.height *= 2;
                if (!mainRect.intersects(compRect)) continue;
                float fade = timeline.getTimelinePosition();
                double start = (double)MAX_PRESS_GHOSTING_ALPHA - 0.0015 * compRect.getWidth();
                float coef = Math.max((float)start, MIN_PRESS_GHOSTING_ALPHA);
                float opFactor = coef * (1.0f - DECAY_FACTOR * fade);
                double iFactor = 1.0 + (double)fade;
                graphics.setComposite(LafWidgetUtilities.getAlphaComposite(mainComponent, opFactor));
                Rectangle bounds = comp.getBounds();
                BufferedImage ghost = GhostPaintingUtils.getComponentGhostImage(comp, timeline, iFactor);
                graphics.drawImage((Image)ghost, dx -= (ghost.getWidth() - bounds.width) / 2, dy -= (ghost.getHeight() - bounds.height) / 2, null);
            }
        }
        if (AnimationConfigurationManager.getInstance().isAnimationAllowed(AnimationFacet.GHOSTING_ICON_ROLLOVER, mainComponent)) {
            Map<JComponent, Timeline> runningGhostRolloverTimelines = GhostingListener.getRunningGhostRolloverTimelines();
            for (Map.Entry<JComponent, Timeline> entry : runningGhostRolloverTimelines.entrySet()) {
                JComponent jc;
                comp = entry.getKey();
                timeline = entry.getValue();
                if (comp == mainComponent || !(comp instanceof JComponent) || !(jc = comp).isShowing() || !jc.isVisible()) continue;
                Rectangle compRect = jc.getBounds();
                compRect.setLocation(jc.getLocationOnScreen());
                int dx = compRect.x - mainRect.x;
                int dy = compRect.y - mainRect.y;
                compRect.x -= compRect.width / 2;
                compRect.y -= compRect.height / 2;
                compRect.width *= 2;
                compRect.height *= 2;
                if (!mainRect.intersects(compRect)) continue;
                float fade = timeline.getTimelinePosition();
                Icon icon = null;
                Rectangle iconRect = (Rectangle)jc.getClientProperty("icon.bounds");
                if (iconRect != null) {
                    icon = jc instanceof AbstractButton ? LafWidgetUtilities.getIcon((AbstractButton)jc) : (Icon)jc.getClientProperty("icon");
                }
                if (icon == null || iconRect == null) continue;
                double iFactor = 1.0 + (double)fade;
                BufferedImage iImage = GhostPaintingUtils.getIconGhostImage(comp, timeline, icon, iFactor);
                int iWidth = iImage.getWidth();
                int iHeight = iImage.getHeight();
                double start = MAX_ICON_GHOSTING_ALPHA - (MAX_ICON_GHOSTING_ALPHA - MIN_ICON_GHOSTING_ALPHA) * (float)(iWidth - 16) / 48.0f;
                float coef = Math.max((float)start, MIN_ICON_GHOSTING_ALPHA);
                float opFactor = coef * (1.0f - DECAY_FACTOR * fade);
                graphics.setComposite(LafWidgetUtilities.getAlphaComposite(mainComponent, opFactor));
                graphics.drawImage((Image)iImage, (dx -= (iWidth - icon.getIconWidth()) / 2) + iconRect.x, (dy -= (iHeight - icon.getIconHeight()) / 2) + iconRect.y, null);
            }
        }
        graphics.dispose();
    }

    public static void paintGhostIcon(Graphics2D graphics, AbstractButton b2, Icon icon) {
        GhostPaintingUtils.paintGhostIcon(graphics, b2, icon, (Rectangle)b2.getClientProperty("icon.bounds"));
    }

    public static void paintGhostIcon(Graphics2D graphics, AbstractButton b2, Rectangle iconRectangle) {
        GhostPaintingUtils.paintGhostIcon(graphics, b2, LafWidgetUtilities.getIcon(b2), iconRectangle);
    }

    public static void paintGhostIcon(Graphics2D graphics, Component b2, Icon icon, Rectangle iconRectangle) {
        if (!AnimationConfigurationManager.getInstance().isAnimationAllowed(AnimationFacet.GHOSTING_ICON_ROLLOVER, b2)) {
            return;
        }
        if (!(b2 instanceof JComponent)) {
            return;
        }
        GhostingListener gl = (GhostingListener)((JComponent)b2).getClientProperty("lafwidget.internal.ghostListenerKey");
        if (gl == null) {
            return;
        }
        Timeline ghostRolloverTimeline = gl.getGhostIconRolloverTimeline();
        if (ghostRolloverTimeline.getState() != Timeline.TimelineState.IDLE) {
            float fade = ghostRolloverTimeline.getTimelinePosition();
            if (icon != null && iconRectangle != null) {
                double iFactor = 1.0 + (double)fade;
                BufferedImage iImage = GhostPaintingUtils.getIconGhostImage((JComponent)b2, ghostRolloverTimeline, icon, iFactor);
                int iWidth = iImage.getWidth();
                int iHeight = iImage.getHeight();
                int dx = (iWidth - icon.getIconWidth()) / 2;
                int dy = (iHeight - icon.getIconHeight()) / 2;
                double start = MAX_ICON_GHOSTING_ALPHA - (MAX_ICON_GHOSTING_ALPHA - MIN_ICON_GHOSTING_ALPHA) * (float)(iWidth - 16) / 48.0f;
                float coef = Math.max((float)start, MIN_ICON_GHOSTING_ALPHA);
                float opFactor = coef * (1.0f - DECAY_FACTOR * fade);
                graphics.setComposite(LafWidgetUtilities.getAlphaComposite(b2, opFactor));
                graphics.drawImage((Image)iImage, iconRectangle.x - dx, iconRectangle.y - dy, null);
            }
        }
    }
}

