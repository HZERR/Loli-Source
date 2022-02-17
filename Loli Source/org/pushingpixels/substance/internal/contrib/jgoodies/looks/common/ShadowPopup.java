/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.contrib.jgoodies.looks.common;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JApplet;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JRootPane;
import javax.swing.JWindow;
import javax.swing.Popup;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import org.pushingpixels.substance.internal.contrib.jgoodies.looks.common.ShadowPopupBorder;

public final class ShadowPopup
extends Popup {
    private static final int MAX_CACHE_SIZE = 5;
    private static List cache;
    private static final Border SHADOW_BORDER;
    private static final int SHADOW_SIZE = 5;
    private static boolean canSnapshot;
    private Component owner;
    private Component contents;
    private int x;
    private int y;
    private Popup popup;
    private Border oldBorder;
    private boolean oldOpaque;
    private Container heavyWeightContainer;
    private static final Point POINT;
    private static final Rectangle RECT;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    static Popup getInstance(Component owner, Component contents, int x2, int y2, Popup delegate) {
        Class<ShadowPopup> class_ = ShadowPopup.class;
        synchronized (ShadowPopup.class) {
            if (cache == null) {
                cache = new ArrayList(5);
            }
            ShadowPopup result = cache.size() > 0 ? (ShadowPopup)cache.remove(0) : new ShadowPopup();
            // ** MonitorExit[var6_5] (shouldn't be in output)
            result.reset(owner, contents, x2, y2, delegate);
            return result;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void recycle(ShadowPopup popup) {
        Class<ShadowPopup> class_ = ShadowPopup.class;
        synchronized (ShadowPopup.class) {
            if (cache.size() < 5) {
                cache.add(popup);
            }
            // ** MonitorExit[var1_1] (shouldn't be in output)
            return;
        }
    }

    public static boolean canSnapshot() {
        return canSnapshot;
    }

    @Override
    public void hide() {
        if (this.contents == null) {
            return;
        }
        JComponent parent = (JComponent)this.contents.getParent();
        this.popup.hide();
        if (parent != null && parent.getBorder() == SHADOW_BORDER) {
            parent.setBorder(this.oldBorder);
            parent.setOpaque(this.oldOpaque);
            this.oldBorder = null;
            if (this.heavyWeightContainer != null) {
                parent.putClientProperty("jgoodies.hShadowBg", null);
                parent.putClientProperty("jgoodies.vShadowBg", null);
                this.heavyWeightContainer = null;
            }
        }
        this.owner = null;
        this.contents = null;
        this.popup = null;
        ShadowPopup.recycle(this);
    }

    @Override
    public void show() {
        if (this.heavyWeightContainer != null) {
            this.snapshot();
        }
        this.popup.show();
    }

    private void reset(Component owner, Component contents, int x2, int y2, Popup popup) {
        this.owner = owner;
        this.contents = contents;
        this.popup = popup;
        this.x = x2;
        this.y = y2;
        if (owner instanceof JComboBox) {
            return;
        }
        Dimension contentsPrefSize = contents.getPreferredSize();
        if (contentsPrefSize.width <= 0 || contentsPrefSize.height <= 0) {
            return;
        }
        for (Container p2 = contents.getParent(); p2 != null; p2 = p2.getParent()) {
            if (!(p2 instanceof JWindow) && !(p2 instanceof Panel)) continue;
            p2.setBackground(contents.getBackground());
            this.heavyWeightContainer = p2;
            break;
        }
        JComponent parent = (JComponent)contents.getParent();
        this.oldOpaque = parent.isOpaque();
        this.oldBorder = parent.getBorder();
        parent.setOpaque(false);
        parent.setBorder(SHADOW_BORDER);
        if (this.heavyWeightContainer != null) {
            this.heavyWeightContainer.setSize(this.heavyWeightContainer.getPreferredSize());
        } else {
            parent.setSize(parent.getPreferredSize());
        }
    }

    private void snapshot() {
        try {
            boolean doubleBuffered;
            JComponent c2;
            Graphics2D g2;
            Dimension size = this.heavyWeightContainer.getPreferredSize();
            int width = size.width;
            int height = size.height;
            if (width <= 0 || height <= 5) {
                return;
            }
            Robot robot = new Robot();
            RECT.setBounds(this.x, this.y + height - 5, width, 5);
            BufferedImage hShadowBg = robot.createScreenCapture(RECT);
            RECT.setBounds(this.x + width - 5, this.y, 5, height - 5);
            BufferedImage vShadowBg = robot.createScreenCapture(RECT);
            JComponent parent = (JComponent)this.contents.getParent();
            parent.putClientProperty("jgoodies.hShadowBg", hShadowBg);
            parent.putClientProperty("jgoodies.vShadowBg", vShadowBg);
            Container layeredPane = this.getLayeredPane();
            if (layeredPane == null) {
                return;
            }
            int layeredPaneWidth = layeredPane.getWidth();
            int layeredPaneHeight = layeredPane.getHeight();
            ShadowPopup.POINT.x = this.x;
            ShadowPopup.POINT.y = this.y;
            SwingUtilities.convertPointFromScreen(POINT, layeredPane);
            ShadowPopup.RECT.x = ShadowPopup.POINT.x;
            ShadowPopup.RECT.y = ShadowPopup.POINT.y + height - 5;
            ShadowPopup.RECT.width = width;
            ShadowPopup.RECT.height = 5;
            if (ShadowPopup.RECT.x + ShadowPopup.RECT.width > layeredPaneWidth) {
                ShadowPopup.RECT.width = layeredPaneWidth - ShadowPopup.RECT.x;
            }
            if (ShadowPopup.RECT.y + ShadowPopup.RECT.height > layeredPaneHeight) {
                ShadowPopup.RECT.height = layeredPaneHeight - ShadowPopup.RECT.y;
            }
            if (!RECT.isEmpty()) {
                g2 = hShadowBg.createGraphics();
                ((Graphics)g2).translate(-ShadowPopup.RECT.x, -ShadowPopup.RECT.y);
                g2.setClip(RECT);
                if (layeredPane instanceof JComponent) {
                    c2 = (JComponent)layeredPane;
                    doubleBuffered = c2.isDoubleBuffered();
                    c2.setDoubleBuffered(false);
                    c2.paintAll(g2);
                    c2.setDoubleBuffered(doubleBuffered);
                } else {
                    layeredPane.paintAll(g2);
                }
                g2.dispose();
            }
            ShadowPopup.RECT.x = ShadowPopup.POINT.x + width - 5;
            ShadowPopup.RECT.y = ShadowPopup.POINT.y;
            ShadowPopup.RECT.width = 5;
            ShadowPopup.RECT.height = height - 5;
            if (ShadowPopup.RECT.x + ShadowPopup.RECT.width > layeredPaneWidth) {
                ShadowPopup.RECT.width = layeredPaneWidth - ShadowPopup.RECT.x;
            }
            if (ShadowPopup.RECT.y + ShadowPopup.RECT.height > layeredPaneHeight) {
                ShadowPopup.RECT.height = layeredPaneHeight - ShadowPopup.RECT.y;
            }
            if (!RECT.isEmpty()) {
                g2 = vShadowBg.createGraphics();
                ((Graphics)g2).translate(-ShadowPopup.RECT.x, -ShadowPopup.RECT.y);
                g2.setClip(RECT);
                if (layeredPane instanceof JComponent) {
                    c2 = (JComponent)layeredPane;
                    doubleBuffered = c2.isDoubleBuffered();
                    c2.setDoubleBuffered(false);
                    c2.paintAll(g2);
                    c2.setDoubleBuffered(doubleBuffered);
                } else {
                    layeredPane.paintAll(g2);
                }
                g2.dispose();
            }
        }
        catch (AWTException e2) {
            canSnapshot = false;
        }
        catch (SecurityException e3) {
            canSnapshot = false;
        }
    }

    private Container getLayeredPane() {
        Container parent = null;
        if (this.owner != null) {
            parent = this.owner instanceof Container ? (Container)this.owner : this.owner.getParent();
        }
        for (Container p2 = parent; p2 != null; p2 = p2.getParent()) {
            if (p2 instanceof JRootPane) {
                if (p2.getParent() instanceof JInternalFrame) continue;
                parent = ((JRootPane)p2).getLayeredPane();
                continue;
            }
            if (p2 instanceof Window) {
                if (parent != null) break;
                parent = p2;
                break;
            }
            if (p2 instanceof JApplet) break;
        }
        return parent;
    }

    static {
        SHADOW_BORDER = ShadowPopupBorder.getInstance();
        canSnapshot = true;
        POINT = new Point();
        RECT = new Rectangle();
    }
}

