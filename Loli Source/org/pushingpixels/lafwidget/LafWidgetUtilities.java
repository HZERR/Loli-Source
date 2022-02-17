/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;
import java.util.ResourceBundle;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.CellRendererPane;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.JTextComponent;
import org.pushingpixels.lafwidget.LafWidgetRepository;
import org.pushingpixels.lafwidget.UiThreadingViolationException;
import org.pushingpixels.lafwidget.animation.AnimationConfigurationManager;
import org.pushingpixels.lafwidget.animation.AnimationFacet;

public class LafWidgetUtilities {
    public static final String PREVIEW_MODE = "lafwidgets.internal.previewMode";

    private LafWidgetUtilities() {
    }

    public static BufferedImage getBlankImage(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, 2);
        Graphics2D graphics = (Graphics2D)image.getGraphics().create();
        graphics.setColor(new Color(0, 0, 0, 0));
        graphics.setComposite(AlphaComposite.Src);
        graphics.fillRect(0, 0, width, height);
        graphics.dispose();
        return image;
    }

    public static BufferedImage createCompatibleImage(BufferedImage image) {
        GraphicsEnvironment e2 = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice d2 = e2.getDefaultScreenDevice();
        GraphicsConfiguration c2 = d2.getDefaultConfiguration();
        BufferedImage compatibleImage = c2.createCompatibleImage(image.getWidth(), image.getHeight());
        Graphics g2 = compatibleImage.getGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return compatibleImage;
    }

    public static BufferedImage createThumbnail(BufferedImage image, int requestedThumbWidth) {
        float ratio = (float)image.getWidth() / (float)image.getHeight();
        int width = image.getWidth();
        BufferedImage thumb = image;
        do {
            if ((width /= 2) < requestedThumbWidth) {
                width = requestedThumbWidth;
            }
            BufferedImage temp = new BufferedImage(width, (int)((float)width / ratio), 2);
            Graphics2D g2 = temp.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(thumb, 0, 0, temp.getWidth(), temp.getHeight(), null);
            g2.dispose();
            thumb = temp;
        } while (width != requestedThumbWidth);
        return thumb;
    }

    public static Icon getSearchIcon(int dimension, boolean leftToRight) {
        BufferedImage result = LafWidgetUtilities.getBlankImage(dimension, dimension);
        Graphics2D graphics = (Graphics2D)result.getGraphics().create();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(Color.black);
        graphics.setStroke(new BasicStroke(1.5f));
        if (leftToRight) {
            int xc = (int)(0.6 * (double)dimension);
            int yc = (int)(0.45 * (double)dimension);
            int r2 = (int)(0.3 * (double)dimension);
            graphics.drawOval(xc - r2, yc - r2, 2 * r2, 2 * r2);
            graphics.setStroke(new BasicStroke(3.0f));
            GeneralPath handle = new GeneralPath();
            handle.moveTo((float)((double)xc - (double)r2 / Math.sqrt(2.0)), (float)((double)yc + (double)r2 / Math.sqrt(2.0)));
            handle.lineTo(1.8f, (float)dimension - 2.2f);
            graphics.draw(handle);
        } else {
            int xc = (int)(0.4 * (double)dimension);
            int yc = (int)(0.45 * (double)dimension);
            int r3 = (int)(0.3 * (double)dimension);
            graphics.drawOval(xc - r3, yc - r3, 2 * r3, 2 * r3);
            graphics.setStroke(new BasicStroke(3.0f));
            GeneralPath handle = new GeneralPath();
            handle.moveTo((float)((double)xc + (double)r3 / Math.sqrt(2.0)), (float)((double)yc + (double)r3 / Math.sqrt(2.0)));
            handle.lineTo((float)dimension - 2.5f, (float)dimension - 2.2f);
            graphics.draw(handle);
        }
        graphics.dispose();
        return new ImageIcon(result);
    }

    public static Icon getHexaMarker(int value) {
        BufferedImage result = LafWidgetUtilities.getBlankImage(9, 9);
        Color offColor = Color.gray;
        Color onColor = Color.black;
        boolean bit1 = ((value %= 16) & 1) != 0;
        boolean bit2 = (value & 2) != 0;
        boolean bit3 = (value & 4) != 0;
        boolean bit4 = (value & 8) != 0;
        Graphics2D graphics = (Graphics2D)result.getGraphics().create();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(bit1 ? onColor : offColor);
        graphics.fillOval(5, 5, 4, 4);
        graphics.setColor(bit2 ? onColor : offColor);
        graphics.fillOval(5, 0, 4, 4);
        graphics.setColor(bit3 ? onColor : offColor);
        graphics.fillOval(0, 5, 4, 4);
        graphics.setColor(bit4 ? onColor : offColor);
        graphics.fillOval(0, 0, 4, 4);
        graphics.dispose();
        return new ImageIcon(result);
    }

    public static void makePreviewable(Component comp, Map<Component, Boolean> dbSnapshot) {
        if (comp instanceof JComponent) {
            JComponent jcomp = (JComponent)comp;
            dbSnapshot.put(jcomp, jcomp.isDoubleBuffered());
            jcomp.setDoubleBuffered(false);
            jcomp.putClientProperty(PREVIEW_MODE, Boolean.TRUE);
        }
        if (comp instanceof Container) {
            Container cont = (Container)comp;
            for (int i2 = 0; i2 < cont.getComponentCount(); ++i2) {
                LafWidgetUtilities.makePreviewable(cont.getComponent(i2), dbSnapshot);
            }
        }
    }

    public static void restorePreviewable(Component comp, Map<Component, Boolean> dbSnapshot) {
        if (comp instanceof JComponent) {
            JComponent jcomp = (JComponent)comp;
            if (dbSnapshot.containsKey(comp)) {
                Boolean buffered = dbSnapshot.get(comp);
                jcomp.setDoubleBuffered(buffered == null ? false : buffered);
                jcomp.putClientProperty(PREVIEW_MODE, null);
            } else {
                Container parent = comp.getParent();
                if (parent instanceof JComponent && dbSnapshot.containsKey(parent)) {
                    Boolean buffered = dbSnapshot.get(parent);
                    jcomp.setDoubleBuffered(buffered == null ? false : buffered);
                    jcomp.putClientProperty(PREVIEW_MODE, null);
                }
            }
        }
        if (comp instanceof Container) {
            Container cont = (Container)comp;
            for (int i2 = 0; i2 < cont.getComponentCount(); ++i2) {
                LafWidgetUtilities.restorePreviewable(cont.getComponent(i2), dbSnapshot);
            }
        }
    }

    public static Icon getSmallLockIcon() {
        BufferedImage result = LafWidgetUtilities.getBlankImage(6, 8);
        Color fore = Color.black;
        Color fill = new Color(208, 208, 48);
        Graphics2D graphics = (Graphics2D)result.getGraphics().create();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        graphics.setColor(fill);
        graphics.fillRect(1, 3, 4, 4);
        graphics.setColor(fore);
        graphics.drawLine(0, 3, 0, 7);
        graphics.drawLine(5, 3, 5, 7);
        graphics.drawLine(0, 7, 5, 7);
        graphics.drawLine(1, 2, 4, 2);
        graphics.drawLine(1, 1, 1, 2);
        graphics.drawLine(4, 1, 4, 2);
        graphics.drawLine(2, 0, 3, 0);
        graphics.drawLine(2, 4, 3, 4);
        graphics.drawLine(2, 5, 3, 5);
        graphics.dispose();
        return new ImageIcon(result);
    }

    public static boolean hasTextFocusSelectAllProperty(JTextComponent textComp) {
        for (Container comp = textComp; comp != null; comp = comp.getParent()) {
            if (!(comp instanceof JComponent)) continue;
            Object textFocusSelectAllProperty = ((JComponent)comp).getClientProperty("lafwidgets.textSelectAllOnFocus");
            if (Boolean.TRUE.equals(textFocusSelectAllProperty)) {
                return true;
            }
            if (!Boolean.FALSE.equals(textFocusSelectAllProperty)) continue;
            return false;
        }
        return Boolean.TRUE.equals(UIManager.get("lafwidgets.textSelectAllOnFocus"));
    }

    public static boolean hasTextFlipSelectOnEscapeProperty(JTextComponent textComp) {
        Object textFocusSelectAllProperty = textComp.getClientProperty("lafwidgets.textFlipSelectOnEscape");
        return Boolean.TRUE.equals(textFocusSelectAllProperty);
    }

    public static boolean hasTextEditContextMenu(JTextComponent textComp) {
        Object textEditContextMenuProperty = textComp.getClientProperty("lafwidgets.textEditContextMenu");
        if (Boolean.TRUE.equals(textEditContextMenuProperty)) {
            return true;
        }
        if (Boolean.FALSE.equals(textEditContextMenuProperty)) {
            return false;
        }
        return Boolean.TRUE.equals(UIManager.get("lafwidgets.textEditContextMenu"));
    }

    public static boolean hasAutoScroll(JScrollPane scrollPane) {
        Object compProperty = scrollPane.getClientProperty("lafwidget.scroll.auto");
        if (Boolean.TRUE.equals(compProperty)) {
            return true;
        }
        if (Boolean.FALSE.equals(compProperty)) {
            return false;
        }
        return Boolean.TRUE.equals(UIManager.get("lafwidget.scroll.auto"));
    }

    public static boolean hasAutomaticDnDSupport(JTree tree) {
        Object dndProperty = tree.getClientProperty("lafwidgets.treeAutoDnDSupport");
        if (Boolean.TRUE.equals(dndProperty)) {
            return true;
        }
        if (Boolean.FALSE.equals(dndProperty)) {
            return false;
        }
        return Boolean.TRUE.equals(UIManager.get("lafwidgets.treeAutoDnDSupport"));
    }

    public static boolean toIgnoreGlobalLocale(JComponent jcomp) {
        if (jcomp == null) {
            return false;
        }
        return Boolean.TRUE.equals(jcomp.getClientProperty("lafwidgets.ignoreGlobalLocale"));
    }

    public static ResourceBundle getResourceBundle(JComponent jcomp) {
        if (LafWidgetUtilities.toIgnoreGlobalLocale(jcomp)) {
            return LafWidgetRepository.getLabelBundle(jcomp.getLocale());
        }
        return LafWidgetRepository.getLabelBundle();
    }

    public static boolean hasNoAnimations(Component comp, AnimationFacet animationFacet) {
        return !AnimationConfigurationManager.getInstance().isAnimationAllowed(animationFacet, comp);
    }

    public static Icon getIcon(AbstractButton b2) {
        Icon icon = b2.getIcon();
        if (icon == null) {
            return null;
        }
        ButtonModel model = b2.getModel();
        Icon tmpIcon = null;
        if (icon != null) {
            if (!model.isEnabled()) {
                tmpIcon = model.isSelected() ? b2.getDisabledSelectedIcon() : b2.getDisabledIcon();
            } else if (model.isPressed() && model.isArmed()) {
                tmpIcon = b2.getPressedIcon();
            } else if (b2.isRolloverEnabled() && model.isRollover()) {
                tmpIcon = model.isSelected() ? b2.getRolloverSelectedIcon() : b2.getRolloverIcon();
            } else if (model.isSelected()) {
                tmpIcon = b2.getSelectedIcon();
            }
            if (tmpIcon != null) {
                icon = tmpIcon;
            }
        }
        return icon;
    }

    public static boolean toIgnoreAnimations(Component comp) {
        if (comp instanceof JMenuItem) {
            return false;
        }
        return SwingUtilities.getAncestorOfClass(CellRendererPane.class, comp) != null;
    }

    public static void testComponentStateChangeThreadingViolation(Component comp) {
        if (!SwingUtilities.isEventDispatchThread()) {
            UiThreadingViolationException uiThreadingViolationError = new UiThreadingViolationException("Component state change must be done on Event Dispatch Thread");
            uiThreadingViolationError.printStackTrace(System.err);
            throw uiThreadingViolationError;
        }
    }

    public static void firePropertyChangeEvent(JComponent component, String propertyName, Object oldValue, Object newValue) {
        PropertyChangeEvent pce = new PropertyChangeEvent(component, propertyName, oldValue, newValue);
        for (PropertyChangeListener general : component.getPropertyChangeListeners()) {
            general.propertyChange(pce);
        }
        for (PropertyChangeListener specific : component.getPropertyChangeListeners(propertyName)) {
            specific.propertyChange(pce);
        }
    }

    public static Composite getAlphaComposite(Component c2, float translucency, Graphics g2) {
        float finalAlpha;
        AlphaComposite ac;
        Graphics2D g2d;
        Composite existingComposite;
        float xFactor = 1.0f;
        if (g2 instanceof Graphics2D && (existingComposite = (g2d = (Graphics2D)g2).getComposite()) instanceof AlphaComposite && (ac = (AlphaComposite)existingComposite).getRule() == 3) {
            xFactor = ac.getAlpha();
        }
        if ((finalAlpha = translucency * xFactor) < 0.0f) {
            finalAlpha = 0.0f;
        }
        if (finalAlpha > 1.0f) {
            finalAlpha = 1.0f;
        }
        if (finalAlpha == 1.0f) {
            return AlphaComposite.SrcOver;
        }
        return AlphaComposite.SrcOver.derive(finalAlpha);
    }

    public static Composite getAlphaComposite(Component c2, float translucency) {
        return LafWidgetUtilities.getAlphaComposite(c2, translucency, null);
    }

    public static Composite getAlphaComposite(Component c2, Graphics g2) {
        return LafWidgetUtilities.getAlphaComposite(c2, 1.0f, g2);
    }

    public static Composite getAlphaComposite(Component c2) {
        return LafWidgetUtilities.getAlphaComposite(c2, 1.0f, null);
    }
}

