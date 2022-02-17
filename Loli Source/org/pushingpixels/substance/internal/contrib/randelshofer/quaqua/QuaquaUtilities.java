/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.contrib.randelshofer.quaqua;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.text.JTextComponent;
import javax.swing.text.View;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.util.Methods;

public class QuaquaUtilities
extends BasicGraphicsUtils
implements SwingConstants {
    private QuaquaUtilities() {
    }

    public static boolean isLeftToRight(Component c2) {
        return c2.getComponentOrientation().isLeftToRight();
    }

    public static void drawStringUnderlineCharAt(Graphics g2, String text, int underlinedIndex, int x2, int y2) {
        g2.drawString(text, x2, y2);
        if (underlinedIndex >= 0 && underlinedIndex < text.length()) {
            FontMetrics fm = g2.getFontMetrics();
            int underlineRectX = x2 + fm.stringWidth(text.substring(0, underlinedIndex));
            int underlineRectY = y2;
            int underlineRectWidth = fm.charWidth(text.charAt(underlinedIndex));
            int underlineRectHeight = 1;
            g2.fillRect(underlineRectX, underlineRectY + fm.getDescent() - 1, underlineRectWidth, underlineRectHeight);
        }
    }

    static int findDisplayedMnemonicIndex(String text, int mnemonic) {
        if (text == null || mnemonic == 0) {
            return -1;
        }
        char uc = Character.toUpperCase((char)mnemonic);
        char lc = Character.toLowerCase((char)mnemonic);
        int uci = text.indexOf(uc);
        int lci = text.indexOf(lc);
        if (uci == -1) {
            return lci;
        }
        if (lci == -1) {
            return uci;
        }
        return lci < uci ? lci : uci;
    }

    public static boolean isOnActiveWindow(Component c2) {
        Boolean value;
        if (c2 instanceof JComponent && (value = (Boolean)((JComponent)c2).getClientProperty("Frame.active")) != null && value.booleanValue()) {
            return true;
        }
        Window window = SwingUtilities.getWindowAncestor(c2);
        boolean isOnActiveWindow = window == null ? true : (window instanceof Frame || window instanceof Dialog ? Methods.invokeGetter((Object)window, "isActive", true) : (Methods.invokeGetter((Object)window, "getFocusableWindowState", true) ? Methods.invokeGetter((Object)window, "isFocused", true) : true));
        if (isOnActiveWindow && c2 instanceof JComponent) {
            ((JComponent)c2).putClientProperty("Frame.active", isOnActiveWindow);
        }
        return isOnActiveWindow;
    }

    public static String getKeyModifiersText(int modifiers, boolean leftToRight) {
        return QuaquaUtilities.getKeyModifiersUnicode(modifiers, leftToRight);
    }

    static String getKeyModifiersUnicode(int modifiers, boolean leftToRight) {
        char[] cs = new char[4];
        int count = 0;
        if (leftToRight) {
            if ((modifiers & 2) != 0) {
                cs[count++] = 8963;
            }
            if ((modifiers & 0x28) != 0) {
                cs[count++] = 8997;
            }
            if ((modifiers & 1) != 0) {
                cs[count++] = 8679;
            }
            if ((modifiers & 4) != 0) {
                cs[count++] = 8984;
            }
        } else {
            if ((modifiers & 4) != 0) {
                cs[count++] = 8984;
            }
            if ((modifiers & 1) != 0) {
                cs[count++] = 8679;
            }
            if ((modifiers & 0x28) != 0) {
                cs[count++] = 8997;
            }
            if ((modifiers & 2) != 0) {
                cs[count++] = 8963;
            }
        }
        return new String(cs, 0, count);
    }

    public static void repaintBorder(JComponent component) {
        JComponent c2 = component;
        Border border = null;
        Container container = component.getParent();
        if (container instanceof JViewport && (c2 = (JComponent)container.getParent()) != null) {
            border = c2.getBorder();
        }
        if (border == null) {
            border = component.getBorder();
            c2 = component;
        }
        if (border != null && c2 != null) {
            int w2 = c2.getWidth();
            int h2 = c2.getHeight();
            Insets insets = c2.getInsets();
            c2.repaint(0, 0, w2, insets.top);
            c2.repaint(0, 0, insets.left, h2);
            c2.repaint(0, h2 - insets.bottom, w2, insets.bottom);
            c2.repaint(w2 - insets.right, 0, insets.right, h2);
        }
    }

    public static final Object beginGraphics(Graphics2D graphics2d) {
        Object object = graphics2d.getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING);
        graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        return object;
    }

    public static final void endGraphics(Graphics2D graphics2d, Object oldHints) {
        if (oldHints != null) {
            graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, oldHints);
        }
    }

    public static final boolean isFocused(Component component) {
        Component c2;
        if (QuaquaUtilities.isOnActiveWindow(component) && (c2 = component) instanceof JComponent) {
            JViewport viewport;
            if (c2 instanceof JScrollPane && (viewport = ((JScrollPane)component).getViewport()) != null) {
                c2 = viewport.getView();
            }
            if (c2 instanceof JTextComponent && !((JTextComponent)c2).isEditable()) {
                return false;
            }
            return c2 != null && (((JComponent)c2).hasFocus() || ((JComponent)c2).getClientProperty("Quaqua.drawFocusBorder") == Boolean.TRUE);
        }
        return false;
    }

    static boolean isHeadless() {
        return Methods.invokeStaticGetter(GraphicsEnvironment.class, "isHeadless", false);
    }

    public static int getLeftSideBearing(Font f2, String string) {
        return (Integer)Methods.invokeStatic("com.sun.java.swing.SwingUtilities2", "getLeftSideBearing", new Class[]{Font.class, String.class}, new Object[]{f2, string}, new Integer(0));
    }

    static void provideErrorFeedback(Component component) {
        Toolkit toolkit = null;
        toolkit = component != null ? component.getToolkit() : Toolkit.getDefaultToolkit();
        toolkit.beep();
    }

    public static BufferedImage createBufferedImage(URL location) {
        BufferedImage buf;
        Image image = Toolkit.getDefaultToolkit().createImage(location);
        if (image instanceof BufferedImage) {
            buf = (BufferedImage)image;
        } else {
            QuaquaUtilities.loadImage(image);
            buf = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(image.getWidth(null), image.getHeight(null), 1);
            Graphics g2 = buf.getGraphics();
            g2.drawImage(image, 0, 0, null);
            g2.dispose();
            image.flush();
        }
        return buf;
    }

    public static TexturePaint createTexturePaint(URL location) {
        BufferedImage texture = QuaquaUtilities.createBufferedImage(location);
        TexturePaint paint = new TexturePaint(texture, new Rectangle(0, 0, texture.getWidth(), texture.getHeight()));
        return paint;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void loadImage(Image image) {
        MediaTracker tracker;
        Component component = new Component(){};
        MediaTracker mediaTracker = tracker = new MediaTracker(component);
        synchronized (mediaTracker) {
            int id = 0;
            tracker.addImage(image, id);
            try {
                tracker.waitForID(id, 0L);
            }
            catch (InterruptedException e2) {
                System.out.println("INTERRUPTED while loading Image");
            }
            int loadStatus = tracker.statusID(id, false);
            tracker.removeImage(image, id);
        }
    }

    public static String layoutCompoundLabel(JComponent c2, FontMetrics fm, String text, Icon icon, int verticalAlignment, int horizontalAlignment, int verticalTextPosition, int horizontalTextPosition, Rectangle viewR, Rectangle iconR, Rectangle textR, int textIconGap) {
        boolean orientationIsLeftToRight = true;
        int hAlign = horizontalAlignment;
        int hTextPos = horizontalTextPosition;
        if (c2 != null && !c2.getComponentOrientation().isLeftToRight()) {
            orientationIsLeftToRight = false;
        }
        switch (horizontalAlignment) {
            case 10: {
                hAlign = orientationIsLeftToRight ? 2 : 4;
                break;
            }
            case 11: {
                hAlign = orientationIsLeftToRight ? 4 : 2;
            }
        }
        switch (horizontalTextPosition) {
            case 10: {
                hTextPos = orientationIsLeftToRight ? 2 : 4;
                break;
            }
            case 11: {
                hTextPos = orientationIsLeftToRight ? 4 : 2;
            }
        }
        return QuaquaUtilities.layoutCompoundLabelImpl(c2, fm, text, icon, verticalAlignment, hAlign, verticalTextPosition, hTextPos, viewR, iconR, textR, textIconGap);
    }

    public static String layoutCompoundLabel(FontMetrics fm, String text, Icon icon, int verticalAlignment, int horizontalAlignment, int verticalTextPosition, int horizontalTextPosition, Rectangle viewR, Rectangle iconR, Rectangle textR, int textIconGap) {
        return QuaquaUtilities.layoutCompoundLabelImpl(null, fm, text, icon, verticalAlignment, horizontalAlignment, verticalTextPosition, horizontalTextPosition, viewR, iconR, textR, textIconGap);
    }

    private static String layoutCompoundLabelImpl(JComponent c2, FontMetrics fm, String text, Icon icon, int verticalAlignment, int horizontalAlignment, int verticalTextPosition, int horizontalTextPosition, Rectangle viewR, Rectangle iconR, Rectangle textR, int textIconGap) {
        int availTextWidth;
        int gap;
        if (icon != null) {
            iconR.width = icon.getIconWidth();
            iconR.height = icon.getIconHeight();
        } else {
            iconR.height = 0;
            iconR.width = 0;
        }
        boolean textIsEmpty = text == null || text.equals("");
        int lsb = 0;
        View v2 = null;
        if (textIsEmpty) {
            textR.height = 0;
            textR.width = 0;
            text = "";
        } else {
            View view = v2 = c2 != null ? (View)c2.getClientProperty("html") : null;
            if (v2 != null) {
                textR.width = (int)v2.getPreferredSpan(0);
                textR.height = (int)v2.getPreferredSpan(1);
            } else {
                textR.width = SwingUtilities.computeStringWidth(fm, text);
                lsb = QuaquaUtilities.getLeftSideBearing(fm.getFont(), text);
                if (lsb < 0) {
                    textR.width -= lsb;
                }
                textR.height = fm.getHeight();
            }
        }
        int n2 = gap = textIsEmpty || icon == null ? 0 : textIconGap;
        if (!textIsEmpty && textR.width > (availTextWidth = horizontalTextPosition == 0 ? viewR.width : viewR.width - (iconR.width + gap))) {
            if (v2 != null) {
                textR.width = availTextWidth;
            } else {
                int nChars;
                String clipString = "...";
                int totalWidth = SwingUtilities.computeStringWidth(fm, clipString);
                int len = text.length();
                for (nChars = 0; nChars < len; ++nChars) {
                    int charIndex;
                    int n3 = charIndex = nChars % 2 == 0 ? nChars / 2 : len - 1 - nChars / 2;
                    if ((totalWidth += fm.charWidth(text.charAt(charIndex))) > availTextWidth) break;
                }
                text = text.substring(0, nChars / 2) + clipString + text.substring(len - nChars / 2);
                textR.width = SwingUtilities.computeStringWidth(fm, text);
            }
        }
        textR.y = verticalTextPosition == 1 ? (horizontalTextPosition != 0 ? 0 : -(textR.height + gap)) : (verticalTextPosition == 0 ? iconR.height / 2 - textR.height / 2 : (horizontalTextPosition != 0 ? iconR.height - textR.height : iconR.height + gap));
        textR.x = horizontalTextPosition == 2 ? -(textR.width + gap) : (horizontalTextPosition == 0 ? iconR.width / 2 - textR.width / 2 : iconR.width + gap);
        int labelR_x = Math.min(iconR.x, textR.x);
        int labelR_width = Math.max(iconR.x + iconR.width, textR.x + textR.width) - labelR_x;
        int labelR_y = Math.min(iconR.y, textR.y);
        int labelR_height = Math.max(iconR.y + iconR.height, textR.y + textR.height) - labelR_y;
        int dy = verticalAlignment == 1 ? viewR.y - labelR_y : (verticalAlignment == 0 ? viewR.y + viewR.height / 2 - (labelR_y + labelR_height / 2) : viewR.y + viewR.height - (labelR_y + labelR_height));
        int dx = horizontalAlignment == 2 ? viewR.x - labelR_x : (horizontalAlignment == 4 ? viewR.x + viewR.width - (labelR_x + labelR_width) : viewR.x + viewR.width / 2 - (labelR_x + labelR_width / 2));
        textR.x += dx;
        textR.y += dy;
        iconR.x += dx;
        iconR.y += dy;
        if (lsb < 0) {
            textR.width += lsb;
            textR.x -= lsb;
        }
        return text;
    }

    public static void configureGraphics(Graphics gr) {
        Graphics2D g2 = (Graphics2D)gr;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    }

    public static Component compositeRequestFocus(Component component) {
        try {
            if (component instanceof Container) {
                Object policy;
                Component comp;
                Container container = (Container)component;
                if (Methods.invokeGetter((Object)container, "isFocusCycleRoot", false) && (comp = (Component)Methods.invoke(policy = Methods.invokeGetter((Object)container, "getFocusTraversalPolicy", null), "getDefaultComponent", Container.class, container)) != null) {
                    comp.requestFocus();
                    return comp;
                }
                Container rootAncestor = (Container)Methods.invokeGetter((Object)container, "getFocusCycleRootAncestor", null);
                if (rootAncestor != null) {
                    Object policy2 = Methods.invokeGetter((Object)rootAncestor, "getFocusTraversalPolicy", null);
                    Component comp2 = (Component)Methods.invoke(policy2, "getComponentAfter", new Class[]{Container.class, Component.class}, new Object[]{rootAncestor, container});
                    if (comp2 != null && SwingUtilities.isDescendingFrom(comp2, container)) {
                        comp2.requestFocus();
                        return comp2;
                    }
                }
            }
        }
        catch (NoSuchMethodException noSuchMethodException) {
            // empty catch block
        }
        if (Methods.invokeGetter((Object)component, "isFocusable", true)) {
            component.requestFocus();
            return component;
        }
        return null;
    }
}

