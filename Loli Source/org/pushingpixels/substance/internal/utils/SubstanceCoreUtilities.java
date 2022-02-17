/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils;

import com.sun.awt.AWTUtilities;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JRootPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ButtonUI;
import javax.swing.plaf.IconUIResource;
import javax.swing.plaf.UIResource;
import javax.swing.text.JTextComponent;
import org.pushingpixels.lafwidget.LafWidgetUtilities;
import org.pushingpixels.lafwidget.utils.TrackableThread;
import org.pushingpixels.substance.api.ColorSchemeAssociationKind;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceConstants;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.UiThreadingViolationException;
import org.pushingpixels.substance.api.colorscheme.BottleGreenColorScheme;
import org.pushingpixels.substance.api.colorscheme.LightAquaColorScheme;
import org.pushingpixels.substance.api.colorscheme.SunfireRedColorScheme;
import org.pushingpixels.substance.api.colorscheme.SunsetColorScheme;
import org.pushingpixels.substance.api.combo.ComboPopupPrototypeCallback;
import org.pushingpixels.substance.api.painter.border.SubstanceBorderPainter;
import org.pushingpixels.substance.api.painter.fill.SubstanceFillPainter;
import org.pushingpixels.substance.api.shaper.SubstanceButtonShaper;
import org.pushingpixels.substance.api.tabbed.TabCloseCallback;
import org.pushingpixels.substance.internal.animation.TransitionAwareUI;
import org.pushingpixels.substance.internal.ui.SubstanceInternalFrameUI;
import org.pushingpixels.substance.internal.ui.SubstanceRootPaneUI;
import org.pushingpixels.substance.internal.utils.HashMapKey;
import org.pushingpixels.substance.internal.utils.MemoryAnalyzer;
import org.pushingpixels.substance.internal.utils.SubstanceColorResource;
import org.pushingpixels.substance.internal.utils.SubstanceColorSchemeUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceColorUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceDropDownButton;
import org.pushingpixels.substance.internal.utils.SubstanceImageCreator;
import org.pushingpixels.substance.internal.utils.SubstanceInternalButton;
import org.pushingpixels.substance.internal.utils.SubstanceInternalFrameTitlePane;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;
import org.pushingpixels.substance.internal.utils.SubstanceSpinnerButton;
import org.pushingpixels.substance.internal.utils.SubstanceTitleButton;
import org.pushingpixels.substance.internal.utils.combo.SubstanceComboPopup;
import org.pushingpixels.substance.internal.utils.icon.ArrowButtonTransitionAwareIcon;
import org.pushingpixels.substance.internal.utils.icon.TransitionAware;
import org.pushingpixels.substance.internal.utils.icon.TransitionAwareIcon;
import org.pushingpixels.substance.internal.utils.menu.SubstanceMenu;
import org.pushingpixels.substance.internal.utils.scroll.SubstanceScrollButton;
import org.pushingpixels.trident.swing.SwingRepaintCallback;

public class SubstanceCoreUtilities {
    public static final String IS_COVERED_BY_LIGHTWEIGHT_POPUPS = "substancelaf.internal.paint.isCoveredByLightweightPopups";
    public static final String TEXT_COMPONENT_AWARE = "substancelaf.internal.textComponentAware";
    public static final boolean reallyThrow = Boolean.valueOf(System.getProperty("insubstantial.checkEDT", "false"));
    public static final boolean reallyPrint = Boolean.valueOf(System.getProperty("insubstantial.logEDT", "true"));
    private static Boolean globalRoundingEnable = null;
    private static boolean defaultRoundingEnable = true;

    private SubstanceCoreUtilities() {
    }

    public static String clipString(FontMetrics metrics, int availableWidth, String fullText) {
        if (metrics.stringWidth(fullText) <= availableWidth) {
            return fullText;
        }
        String ellipses = "...";
        int ellipsesWidth = metrics.stringWidth(ellipses);
        if (ellipsesWidth > availableWidth) {
            return "";
        }
        String starter = "";
        String ender = "";
        int w2 = fullText.length();
        int w22 = w2 / 2 + w2 % 2;
        String prevTitle = "";
        for (int i2 = 0; i2 < w22; ++i2) {
            String newTitle;
            String newStarter = starter + fullText.charAt(i2);
            String newEnder = ender;
            if (w2 - i2 > w22) {
                newEnder = fullText.charAt(w2 - i2 - 1) + newEnder;
            }
            if (metrics.stringWidth(newTitle = newStarter + ellipses + newEnder) > availableWidth) {
                return prevTitle;
            }
            starter = newStarter;
            ender = newEnder;
            prevTitle = newTitle;
        }
        return fullText;
    }

    public static boolean hasIcon(AbstractButton button) {
        return button.getIcon() != null;
    }

    public static boolean hasText(AbstractButton button) {
        String text = button.getText();
        return text != null && text.length() > 0;
    }

    public static boolean isComboBoxButton(AbstractButton button) {
        Container parent = button.getParent();
        return parent != null && (parent instanceof JComboBox || parent.getParent() instanceof JComboBox);
    }

    public static boolean isScrollBarButton(AbstractButton button) {
        Container parent = button.getParent();
        return parent != null && (parent instanceof JScrollBar || parent.getParent() instanceof JScrollBar);
    }

    public static boolean isSpinnerButton(AbstractButton button) {
        Container parent = button.getParent();
        if (!(button instanceof SubstanceSpinnerButton)) {
            return false;
        }
        return parent != null && (parent instanceof JSpinner || parent.getParent() instanceof JSpinner);
    }

    public static boolean isToolBarButton(JComponent component) {
        if (component instanceof SubstanceDropDownButton) {
            return false;
        }
        if (component instanceof SubstanceSpinnerButton) {
            return false;
        }
        Container parent = component.getParent();
        return parent != null && (parent instanceof JToolBar || parent.getParent() instanceof JToolBar);
    }

    public static boolean isScrollButton(JComponent comp) {
        return comp instanceof SubstanceScrollButton;
    }

    public static boolean isButtonNeverPainted(JComponent button) {
        JComponent jparent;
        Object flatProperty;
        Container parent;
        if (button instanceof JCheckBox) {
            return false;
        }
        if (button instanceof JRadioButton) {
            return false;
        }
        Object prop = button.getClientProperty("substancelaf.buttonpaintnever");
        if (prop != null) {
            if (Boolean.TRUE.equals(prop)) {
                return true;
            }
            if (Boolean.FALSE.equals(prop)) {
                return false;
            }
        }
        if (button != null && (parent = button.getParent()) instanceof JComponent && (flatProperty = (jparent = (JComponent)parent).getClientProperty("substancelaf.buttonpaintnever")) != null) {
            if (Boolean.TRUE.equals(flatProperty)) {
                return true;
            }
            if (Boolean.FALSE.equals(flatProperty)) {
                return false;
            }
        }
        return Boolean.TRUE.equals(UIManager.get("substancelaf.buttonpaintnever"));
    }

    public static SubstanceConstants.FocusKind getFocusKind(Component component) {
        while (component != null) {
            JComponent jcomp;
            Object jcompFocusKind;
            if (component instanceof JComponent && (jcompFocusKind = (jcomp = (JComponent)component).getClientProperty("substancelaf.focusKind")) instanceof SubstanceConstants.FocusKind) {
                return (SubstanceConstants.FocusKind)((Object)jcompFocusKind);
            }
            component = component.getParent();
        }
        Object globalFocusKind = UIManager.get("substancelaf.focusKind");
        if (globalFocusKind instanceof SubstanceConstants.FocusKind) {
            return (SubstanceConstants.FocusKind)((Object)globalFocusKind);
        }
        return SubstanceConstants.FocusKind.ALL_INNER;
    }

    public static boolean toDrawWatermark(Component component) {
        for (Component c2 = component; c2 != null; c2 = c2.getParent()) {
            JComponent jcomp;
            Object obj;
            if (!(c2 instanceof JComponent) || (obj = (jcomp = (JComponent)component).getClientProperty("substancelaf.watermark.visible")) == null) continue;
            if (Boolean.TRUE.equals(obj)) {
                return true;
            }
            if (!Boolean.FALSE.equals(obj)) continue;
            return false;
        }
        Object obj = UIManager.get("substancelaf.watermark.visible");
        if (Boolean.TRUE.equals(obj)) {
            return true;
        }
        if (Boolean.FALSE.equals(obj)) {
            return false;
        }
        if (component instanceof JList) {
            return false;
        }
        if (component instanceof JTree) {
            return false;
        }
        if (component instanceof JTable) {
            return false;
        }
        return !(component instanceof JTextComponent);
    }

    public static SubstanceButtonShaper getButtonShaper(Component comp) {
        Object prop;
        if (comp instanceof JComponent && (prop = ((JComponent)comp).getClientProperty("substancelaf.buttonShaper")) instanceof SubstanceButtonShaper) {
            return (SubstanceButtonShaper)prop;
        }
        SubstanceSkin skin = SubstanceCoreUtilities.getSkin(comp);
        if (skin == null) {
            return null;
        }
        return skin.getButtonShaper();
    }

    public static SubstanceFillPainter getFillPainter(Component comp) {
        return SubstanceCoreUtilities.getSkin(comp).getFillPainter();
    }

    public static boolean isTabModified(Component tabComponent) {
        boolean isWindowModified = false;
        Component comp = tabComponent;
        if (comp instanceof JComponent) {
            JComponent jc = (JComponent)comp;
            isWindowModified = Boolean.TRUE.equals(jc.getClientProperty("windowModified"));
        }
        return isWindowModified;
    }

    public static boolean isRootPaneModified(JRootPane rootPane) {
        return Boolean.TRUE.equals(rootPane.getClientProperty("windowModified"));
    }

    public static boolean isInternalFrameModified(JInternalFrame internalFrame) {
        return Boolean.TRUE.equals(internalFrame.getRootPane().getClientProperty("windowModified"));
    }

    public static boolean isRootPaneAutoDeactivate(JRootPane rp) {
        if (!UIManager.getBoolean("windowAutoDeactivate")) {
            return false;
        }
        if (rp == null) {
            return false;
        }
        Object paneSpecific = rp.getClientProperty("windowAutoDeactivate");
        return !(paneSpecific instanceof Boolean) || (Boolean)paneSpecific != false;
    }

    public static boolean isPaintRootPaneActivated(JRootPane rp) {
        if (SubstanceCoreUtilities.isRootPaneAutoDeactivate(rp)) {
            Container c2 = rp.getParent();
            if (c2 instanceof JInternalFrame) {
                return ((JInternalFrame)c2).isSelected();
            }
            if (c2 instanceof Window) {
                return ((Window)c2).isActive();
            }
            return false;
        }
        return true;
    }

    public static boolean isSecondaryWindow(JRootPane rp) {
        Container c2 = rp.getParent();
        return c2 instanceof JInternalFrame;
    }

    public static boolean isRoundedCorners(Component c2) {
        Object o2;
        if (globalRoundingEnable == null) {
            String s2 = System.getProperty("substancelaf.windowRoundedCorners");
            globalRoundingEnable = s2 == null || s2.length() == 0 || Boolean.valueOf(s2) != false;
            UIManager.getDefaults().addPropertyChangeListener(new PropertyChangeListener(){

                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if ("windowRoundedCorners".equals(evt.getPropertyName())) {
                        defaultRoundingEnable = !(evt.getNewValue() instanceof Boolean) || (Boolean)evt.getNewValue() != false;
                    }
                }
            });
            if (globalRoundingEnable.booleanValue()) {
                globalRoundingEnable = AWTUtilities.isTranslucencySupported(AWTUtilities.Translucency.PERPIXEL_TRANSPARENT);
            }
            if (globalRoundingEnable.booleanValue() && (o2 = UIManager.get("windowRoundedCorners")) instanceof Boolean) {
                defaultRoundingEnable = (Boolean)o2;
            }
        }
        if (!globalRoundingEnable.booleanValue()) {
            return false;
        }
        boolean round = defaultRoundingEnable;
        if (c2 instanceof JComponent && (o2 = ((JComponent)c2).getClientProperty("windowRoundedCorners")) instanceof Boolean) {
            round = (Boolean)o2;
        }
        if (round) {
            Component p2;
            for (p2 = c2; !(p2 instanceof Window) && !(p2 instanceof JInternalFrame) && p2 != null; p2 = p2.getParent()) {
            }
            if (p2 instanceof Frame) {
                if ((((Frame)p2).getExtendedState() & 6) == 6) {
                    round = false;
                }
            } else if (c2 instanceof JInternalFrame) {
                round = !((JInternalFrame)c2).isMaximum();
            }
        }
        return round;
    }

    public static boolean hasCloseButton(JTabbedPane tabbedPane, int tabIndex) {
        Object tabProp;
        int tabCount = tabbedPane.getTabCount();
        if (tabIndex < 0 || tabIndex >= tabCount) {
            return false;
        }
        if (!tabbedPane.isEnabledAt(tabIndex)) {
            return false;
        }
        Component tabComponent = tabbedPane.getComponentAt(tabIndex);
        if (tabComponent instanceof JComponent) {
            Object compProp = ((JComponent)tabComponent).getClientProperty("substancelaf.tabbedpanehasclosebuttons");
            if (Boolean.TRUE.equals(compProp)) {
                return true;
            }
            if (Boolean.FALSE.equals(compProp)) {
                return false;
            }
        }
        if (Boolean.TRUE.equals(tabProp = tabbedPane.getClientProperty("substancelaf.tabbedpanehasclosebuttons"))) {
            return true;
        }
        if (Boolean.FALSE.equals(tabProp)) {
            return false;
        }
        return UIManager.getBoolean("substancelaf.tabbedpanehasclosebuttons");
    }

    public static int getCloseButtonSize(JTabbedPane tabbedPane, int tabIndex) {
        if (!SubstanceCoreUtilities.hasCloseButton(tabbedPane, tabIndex)) {
            return 0;
        }
        return SubstanceSizeUtils.getTabCloseIconSize(SubstanceSizeUtils.getComponentFontSize(tabbedPane));
    }

    public static SubstanceConstants.TabContentPaneBorderKind getContentBorderKind(JTabbedPane tabbedPane) {
        Object tabProp = tabbedPane.getClientProperty("substancelaf.tabbedPaneContentBorderKind");
        if (tabProp instanceof SubstanceConstants.TabContentPaneBorderKind) {
            return (SubstanceConstants.TabContentPaneBorderKind)((Object)tabProp);
        }
        Object globalProp = UIManager.get("substancelaf.tabbedPaneContentBorderKind");
        if (globalProp instanceof SubstanceConstants.TabContentPaneBorderKind) {
            return (SubstanceConstants.TabContentPaneBorderKind)((Object)globalProp);
        }
        return SubstanceConstants.TabContentPaneBorderKind.DOUBLE_FULL;
    }

    public static boolean toAnimateCloseIconOfModifiedTab(JTabbedPane tabbedPane, int tabIndex) {
        Object tabProp;
        int tabCount = tabbedPane.getTabCount();
        if (tabIndex < 0 || tabIndex >= tabCount) {
            return false;
        }
        if (!SubstanceCoreUtilities.hasCloseButton(tabbedPane, tabIndex)) {
            return false;
        }
        Component tabComponent = tabbedPane.getComponentAt(tabIndex);
        if (tabComponent instanceof JComponent) {
            Object compProp = ((JComponent)tabComponent).getClientProperty("substancelaf.tabbedpaneclosebuttonsmodifiedanimation");
            if (Boolean.TRUE.equals(compProp)) {
                return true;
            }
            if (Boolean.FALSE.equals(compProp)) {
                return false;
            }
        }
        if (Boolean.TRUE.equals(tabProp = tabbedPane.getClientProperty("substancelaf.tabbedpaneclosebuttonsmodifiedanimation"))) {
            return true;
        }
        if (Boolean.FALSE.equals(tabProp)) {
            return false;
        }
        return UIManager.getBoolean("substancelaf.tabbedpaneclosebuttonsmodifiedanimation");
    }

    public static BufferedImage getBlankImage(int width, int height) {
        BufferedImage compatibleImage;
        if (MemoryAnalyzer.isRunning() && (width >= 100 || height >= 100)) {
            StackTraceElement[] stack = Thread.currentThread().getStackTrace();
            StringBuilder sb = new StringBuilder();
            int count = 0;
            for (StackTraceElement stackEntry : stack) {
                if (count++ > 8) break;
                sb.append(stackEntry.getClassName()).append(".").append(stackEntry.getMethodName()).append(" [").append(stackEntry.getLineNumber()).append("]").append("\n");
            }
            MemoryAnalyzer.enqueueUsage("Blank " + width + "*" + height + "\n" + sb.toString());
        }
        if (GraphicsEnvironment.isHeadless()) {
            compatibleImage = new BufferedImage(width, height, 2);
        } else {
            GraphicsEnvironment e2 = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice d2 = e2.getDefaultScreenDevice();
            GraphicsConfiguration c2 = d2.getDefaultConfiguration();
            compatibleImage = c2.createCompatibleImage(width, height, 3);
        }
        return compatibleImage;
    }

    public static VolatileImage getBlankVolatileImage(int width, int height) {
        if (MemoryAnalyzer.isRunning() && (width >= 100 || height >= 100)) {
            StackTraceElement[] stack = Thread.currentThread().getStackTrace();
            StringBuilder sb = new StringBuilder();
            int count = 0;
            for (StackTraceElement stackEntry : stack) {
                if (count++ > 8) break;
                sb.append(stackEntry.getClassName()).append(".").append(stackEntry.getMethodName()).append(" [").append(stackEntry.getLineNumber()).append("]").append("\n");
            }
            MemoryAnalyzer.enqueueUsage("Blank " + width + "*" + height + "\n" + sb.toString());
        }
        GraphicsEnvironment e2 = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice d2 = e2.getDefaultScreenDevice();
        GraphicsConfiguration c2 = d2.getDefaultConfiguration();
        VolatileImage compatibleImage = c2.createCompatibleVolatileImage(width, height, 3);
        return compatibleImage;
    }

    public static boolean hasNoMinSizeProperty(AbstractButton button) {
        Object noMinSizeProperty = button.getClientProperty("substancelaf.buttonnominsize");
        if (Boolean.TRUE.equals(noMinSizeProperty)) {
            return true;
        }
        if (Boolean.FALSE.equals(noMinSizeProperty)) {
            return false;
        }
        Container parent = button.getParent();
        if (parent instanceof JComponent) {
            noMinSizeProperty = ((JComponent)parent).getClientProperty("substancelaf.buttonnominsize");
            if (Boolean.TRUE.equals(noMinSizeProperty)) {
                return true;
            }
            if (Boolean.FALSE.equals(noMinSizeProperty)) {
                return false;
            }
        }
        return Boolean.TRUE.equals(UIManager.get("substancelaf.buttonnominsize"));
    }

    public static boolean hasFlatAppearance(Component comp, boolean defaultValue) {
        Object flatProperty;
        JComponent jparent;
        Object flatProperty2;
        Container parent;
        JComponent jcomp;
        Object flatProperty3;
        if (comp instanceof JCheckBox) {
            return defaultValue;
        }
        if (comp instanceof JRadioButton) {
            return defaultValue;
        }
        Component c2 = comp;
        if (c2 instanceof JComponent && (flatProperty3 = (jcomp = (JComponent)c2).getClientProperty("substancelaf.componentFlat")) != null) {
            if (Boolean.TRUE.equals(flatProperty3)) {
                return true;
            }
            if (Boolean.FALSE.equals(flatProperty3)) {
                return false;
            }
        }
        if (c2 != null && (parent = c2.getParent()) instanceof JComponent && (flatProperty2 = (jparent = (JComponent)parent).getClientProperty("substancelaf.componentFlat")) != null) {
            if (Boolean.TRUE.equals(flatProperty2)) {
                return true;
            }
            if (Boolean.FALSE.equals(flatProperty2)) {
                return false;
            }
        }
        if ((flatProperty = UIManager.get("substancelaf.componentFlat")) != null) {
            if (Boolean.TRUE.equals(flatProperty)) {
                return true;
            }
            if (Boolean.FALSE.equals(flatProperty)) {
                return false;
            }
        }
        return defaultValue;
    }

    public static boolean hasFlatAppearance(AbstractButton button) {
        if (button instanceof JCheckBox) {
            return false;
        }
        if (button instanceof JRadioButton) {
            return false;
        }
        return SubstanceCoreUtilities.isToolBarButton(button) && SubstanceCoreUtilities.hasFlatAppearance(button, true) || SubstanceCoreUtilities.hasFlatAppearance(button, false);
    }

    public static int getPopupFlyoutOrientation(JComboBox combobox) {
        Object comboProperty = combobox.getClientProperty("substancelaf.comboboxpopupFlyoutOrientation");
        if (comboProperty instanceof Integer) {
            return (Integer)comboProperty;
        }
        Object globalProperty = UIManager.get("substancelaf.comboboxpopupFlyoutOrientation");
        if (globalProperty instanceof Integer) {
            return (Integer)globalProperty;
        }
        return 5;
    }

    public static void makeNonOpaque(Component comp, Map<Component, Boolean> opacitySnapshot) {
        if (comp instanceof JComponent) {
            JComponent jcomp = (JComponent)comp;
            opacitySnapshot.put(comp, jcomp.isOpaque());
            jcomp.setOpaque(false);
        }
        if (comp instanceof Container) {
            Container cont = (Container)comp;
            for (int i2 = 0; i2 < cont.getComponentCount(); ++i2) {
                SubstanceCoreUtilities.makeNonOpaque(cont.getComponent(i2), opacitySnapshot);
            }
        }
    }

    public static void restoreOpaque(Component comp, Map<Component, Boolean> opacitySnapshot) {
        if (comp instanceof JComponent) {
            JComponent jcomp = (JComponent)comp;
            if (opacitySnapshot.containsKey(comp)) {
                jcomp.setOpaque(opacitySnapshot.get(comp));
            } else {
                jcomp.setOpaque(true);
            }
        }
        if (comp instanceof Container) {
            Container cont = (Container)comp;
            for (int i2 = 0; i2 < cont.getComponentCount(); ++i2) {
                SubstanceCoreUtilities.restoreOpaque(cont.getComponent(i2), opacitySnapshot);
            }
        }
    }

    public static BufferedImage createCompatibleImage(BufferedImage image) {
        GraphicsEnvironment e2 = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice d2 = e2.getDefaultScreenDevice();
        GraphicsConfiguration c2 = d2.getDefaultConfiguration();
        BufferedImage compatibleImage = c2.createCompatibleImage(image.getWidth(), image.getHeight(), 3);
        Graphics g2 = compatibleImage.getGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return compatibleImage;
    }

    public static boolean useThemedDefaultIcon(JComponent comp) {
        if (comp instanceof SubstanceInternalButton) {
            return false;
        }
        return Boolean.TRUE.equals(UIManager.get("substancelaf.useThemedDefaultIcons"));
    }

    public static TabCloseCallback getTabCloseCallback(MouseEvent me, JTabbedPane tabbedPane, int tabIndex) {
        Object compProp;
        int tabCount = tabbedPane.getTabCount();
        if (tabIndex < 0 || tabIndex >= tabCount) {
            return null;
        }
        Component tabComponent = tabbedPane.getComponentAt(tabIndex);
        if (tabComponent instanceof JComponent && (compProp = ((JComponent)tabComponent).getClientProperty("substancelaf.tabbedpanecloseCallback")) instanceof TabCloseCallback) {
            return (TabCloseCallback)compProp;
        }
        Object tabProp = tabbedPane.getClientProperty("substancelaf.tabbedpanecloseCallback");
        if (tabProp instanceof TabCloseCallback) {
            return (TabCloseCallback)tabProp;
        }
        Object globProp = UIManager.get("substancelaf.tabbedpanecloseCallback");
        if (globProp instanceof TabCloseCallback) {
            return (TabCloseCallback)globProp;
        }
        return null;
    }

    public static BufferedImage blendImagesVertical(BufferedImage imageTop, BufferedImage imageBottom, double start, double end) {
        int width = imageTop.getWidth();
        if (width != imageBottom.getWidth()) {
            throw new IllegalArgumentException("Widths are not the same: " + imageTop.getWidth() + " and " + imageBottom.getWidth());
        }
        int height = imageTop.getHeight();
        if (height != imageBottom.getHeight()) {
            throw new IllegalArgumentException("Heights are not the same: " + imageTop.getHeight() + " and " + imageBottom.getHeight());
        }
        BufferedImage result = SubstanceCoreUtilities.getBlankImage(width, height);
        Graphics2D graphics = (Graphics2D)result.getGraphics().create();
        int endY = (int)(end * (double)height);
        int startY = (int)(start * (double)height);
        int rampHeight = endY - startY;
        if (rampHeight == 0) {
            graphics.drawImage(imageTop, 0, 0, width, startY, 0, 0, width, startY, null);
            graphics.drawImage(imageBottom, 0, startY, width, height, 0, startY, width, height, null);
        } else {
            BufferedImage rampBottom = SubstanceCoreUtilities.getBlankImage(width, rampHeight);
            Graphics2D rampBottomG = (Graphics2D)rampBottom.getGraphics();
            rampBottomG.setPaint(new GradientPaint(new Point(0, 0), new Color(0, 0, 0, 255), new Point(0, rampHeight), new Color(0, 0, 0, 0)));
            rampBottomG.fillRect(0, 0, width, rampHeight);
            BufferedImage tempBottom = SubstanceCoreUtilities.getBlankImage(width, height - startY);
            Graphics2D tempBottomG = (Graphics2D)tempBottom.getGraphics();
            tempBottomG.drawImage(imageBottom, 0, 0, width, height - startY, 0, startY, width, height, null);
            tempBottomG.setComposite(AlphaComposite.DstOut);
            tempBottomG.drawImage((Image)rampBottom, 0, 0, null);
            tempBottomG.setComposite(AlphaComposite.SrcOver);
            graphics.drawImage((Image)imageTop, 0, 0, null);
            graphics.drawImage((Image)tempBottom, 0, startY, null);
        }
        graphics.dispose();
        return result;
    }

    public static BufferedImage blendImagesHorizontal(BufferedImage imageLeft, BufferedImage imageRight, double start, double end) {
        int width = imageLeft.getWidth();
        if (width != imageRight.getWidth()) {
            throw new IllegalArgumentException("Widths are not the same: " + imageLeft.getWidth() + " and " + imageRight.getWidth());
        }
        int height = imageLeft.getHeight();
        if (height != imageRight.getHeight()) {
            throw new IllegalArgumentException("Heights are not the same: " + imageLeft.getHeight() + " and " + imageRight.getHeight());
        }
        BufferedImage result = SubstanceCoreUtilities.getBlankImage(width, height);
        Graphics2D graphics = (Graphics2D)result.getGraphics().create();
        int endX = (int)(end * (double)width);
        int startX = (int)(start * (double)width);
        int rampWidth = endX - startX;
        if (rampWidth == 0) {
            graphics.drawImage(imageLeft, 0, 0, startX, height, 0, 0, startX, height, null);
            graphics.drawImage(imageRight, startX, 0, width, height, startX, 0, width, height, null);
        } else {
            BufferedImage rampRight = SubstanceCoreUtilities.getBlankImage(rampWidth, height);
            Graphics2D rampRightG = (Graphics2D)rampRight.getGraphics();
            rampRightG.setPaint(new GradientPaint(new Point(0, 0), new Color(0, 0, 0, 255), new Point(rampWidth, 0), new Color(0, 0, 0, 0)));
            rampRightG.fillRect(0, 0, rampWidth, height);
            BufferedImage tempRight = SubstanceCoreUtilities.getBlankImage(width - startX, height);
            Graphics2D tempRightG = (Graphics2D)tempRight.getGraphics();
            tempRightG.drawImage(imageRight, 0, 0, width - startX, height, startX, 0, width, height, null);
            tempRightG.setComposite(AlphaComposite.DstOut);
            tempRightG.drawImage((Image)rampRight, 0, 0, null);
            tempRightG.setComposite(AlphaComposite.SrcOver);
            graphics.drawImage((Image)imageLeft, 0, 0, null);
            graphics.drawImage((Image)tempRight, startX, 0, null);
        }
        graphics.dispose();
        return result;
    }

    public static SubstanceColorScheme getOptionPaneColorScheme(int messageType, SubstanceColorScheme mainScheme) {
        if (!SubstanceLookAndFeel.isToUseConstantThemesOnDialogs()) {
            return mainScheme;
        }
        switch (messageType) {
            case 1: {
                return new BottleGreenColorScheme();
            }
            case 3: {
                return new LightAquaColorScheme();
            }
            case 2: {
                return new SunsetColorScheme();
            }
            case 0: {
                return new SunfireRedColorScheme();
            }
        }
        return null;
    }

    public static Object getComboPopupPrototypeDisplayValue(JComboBox combo) {
        Object objProp = combo.getClientProperty("substancelaf.comboPopupPrototype");
        if (objProp == null) {
            objProp = UIManager.get("substancelaf.comboPopupPrototype");
        }
        if (objProp == null) {
            return null;
        }
        if (objProp instanceof ComboPopupPrototypeCallback) {
            ComboPopupPrototypeCallback callback = (ComboPopupPrototypeCallback)objProp;
            return callback.getPopupPrototypeDisplayValue(combo);
        }
        return objProp;
    }

    public static SubstanceConstants.ScrollPaneButtonPolicyKind getScrollPaneButtonsPolicyKind(JScrollBar scrollBar) {
        Object jspKind;
        Container parent = scrollBar.getParent();
        if (parent instanceof JScrollPane && (jspKind = ((JScrollPane)parent).getClientProperty("substancelaf.scrollPaneButtonsPolicy")) instanceof SubstanceConstants.ScrollPaneButtonPolicyKind) {
            return (SubstanceConstants.ScrollPaneButtonPolicyKind)((Object)jspKind);
        }
        Object globalJspKind = UIManager.get("substancelaf.scrollPaneButtonsPolicy");
        if (globalJspKind instanceof SubstanceConstants.ScrollPaneButtonPolicyKind) {
            return (SubstanceConstants.ScrollPaneButtonPolicyKind)((Object)globalJspKind);
        }
        return SubstanceConstants.ScrollPaneButtonPolicyKind.OPPOSITE;
    }

    public static Set<SubstanceConstants.Side> getSides(JComponent component, String propertyName) {
        if (component == null) {
            return null;
        }
        Object prop = component.getClientProperty(propertyName);
        if (prop == null) {
            return null;
        }
        if (prop instanceof Set) {
            return (Set)prop;
        }
        if (prop != null && prop instanceof SubstanceConstants.Side) {
            EnumSet<SubstanceConstants.Side> result = EnumSet.noneOf(SubstanceConstants.Side.class);
            result.add((SubstanceConstants.Side)((Object)prop));
            return result;
        }
        return null;
    }

    public static float getToolbarButtonCornerRadius(JComponent button, Insets insets) {
        JToolBar toolbar = null;
        for (Container c2 = button.getParent(); c2 != null; c2 = c2.getParent()) {
            if (!(c2 instanceof JToolBar)) continue;
            toolbar = (JToolBar)c2;
            break;
        }
        if (toolbar == null) {
            return 2.0f;
        }
        int width = button.getWidth();
        int height = button.getHeight();
        if (insets != null) {
            width -= insets.left + insets.right;
            height -= insets.top + insets.bottom;
        }
        float maxRadius = width > height ? (float)height / 2.0f : (float)width / 2.0f;
        Object buttonProp = button.getClientProperty("substancelaf.cornerRadius");
        if (buttonProp instanceof Float) {
            return Math.min(maxRadius, ((Float)buttonProp).floatValue());
        }
        Object toolbarProp = toolbar.getClientProperty("substancelaf.cornerRadius");
        if (toolbarProp instanceof Float) {
            return Math.min(maxRadius, ((Float)toolbarProp).floatValue());
        }
        Object globalProp = UIManager.get("substancelaf.cornerRadius");
        if (globalProp instanceof Float) {
            return Math.min(maxRadius, ((Float)globalProp).floatValue());
        }
        return 2.0f;
    }

    public static int getEchoPerChar(JPasswordField jpf) {
        int result;
        Object obj = jpf.getClientProperty("substancelaf.passwordEchoPerChar");
        if (obj != null && obj instanceof Integer && (result = ((Integer)obj).intValue()) >= 1) {
            return result;
        }
        obj = UIManager.get("substancelaf.passwordEchoPerChar");
        if (obj != null && obj instanceof Integer && (result = ((Integer)obj).intValue()) >= 1) {
            return result;
        }
        return 1;
    }

    public static BufferedImage softClip(int width, int height, BufferedImage source, Shape clipShape) {
        BufferedImage img = SubstanceCoreUtilities.getBlankImage(width, height);
        Graphics2D g2 = img.createGraphics();
        g2.setComposite(AlphaComposite.Clear);
        g2.fillRect(0, 0, width, height);
        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fill(clipShape);
        g2.setComposite(AlphaComposite.SrcAtop);
        g2.drawImage((Image)source, 0, 0, null);
        g2.dispose();
        return img;
    }

    public static boolean toShowExtraWidgets(Component component) {
        for (Component c2 = component; c2 != null; c2 = c2.getParent()) {
            JComponent jcomp;
            Object componentProp;
            if (!(c2 instanceof JComponent) || (componentProp = (jcomp = (JComponent)c2).getClientProperty("substancelaf.addWidgets")) == null) continue;
            if (Boolean.TRUE.equals(componentProp)) {
                return false;
            }
            if (!Boolean.FALSE.equals(componentProp)) continue;
            return true;
        }
        return Boolean.TRUE.equals(UIManager.get("substancelaf.addWidgets"));
    }

    public static Icon getThemedIcon(Component comp, Icon orig) {
        SubstanceColorScheme colorScheme = SubstanceColorSchemeUtilities.getColorScheme(comp, ComponentState.ENABLED);
        float brightnessFactor = colorScheme.isDark() ? 0.2f : 0.8f;
        return new ImageIcon(SubstanceImageCreator.getColorSchemeImage(comp, orig, colorScheme, brightnessFactor));
    }

    public static Icon getThemedIcon(JTabbedPane tab, int tabIndex, Icon orig) {
        SubstanceColorScheme colorScheme = SubstanceColorSchemeUtilities.getColorScheme(tab, tabIndex, ColorSchemeAssociationKind.TAB, ComponentState.ENABLED);
        float brightnessFactor = colorScheme.isDark() ? 0.2f : 0.8f;
        return new ImageIcon(SubstanceImageCreator.getColorSchemeImage(tab, orig, colorScheme, brightnessFactor));
    }

    public static Icon getOriginalIcon(AbstractButton b2, Icon defaultIcon) {
        ButtonModel model = b2.getModel();
        Icon icon = b2.getIcon();
        if (icon == null) {
            icon = defaultIcon;
        }
        if (icon.getClass().isAnnotationPresent(TransitionAware.class)) {
            return icon;
        }
        Icon tmpIcon = null;
        if (icon != null) {
            if (!model.isEnabled()) {
                tmpIcon = model.isSelected() ? b2.getDisabledSelectedIcon() : b2.getDisabledIcon();
            } else if (model.isPressed() && model.isArmed()) {
                tmpIcon = b2.getPressedIcon();
            } else if (b2.isRolloverEnabled() && model.isRollover()) {
                if (model.isSelected()) {
                    tmpIcon = b2.getRolloverSelectedIcon();
                    if (tmpIcon == null) {
                        tmpIcon = b2.getSelectedIcon();
                    }
                } else {
                    tmpIcon = b2.getRolloverIcon();
                }
            } else if (model.isSelected()) {
                tmpIcon = b2.getSelectedIcon();
            }
            if (tmpIcon != null) {
                icon = tmpIcon;
            }
        }
        return icon;
    }

    public static SubstanceConstants.MenuGutterFillKind getMenuGutterFillKind() {
        Object globalSetting = UIManager.get("substancelaf.menuGutterFillKind");
        if (globalSetting instanceof SubstanceConstants.MenuGutterFillKind) {
            return (SubstanceConstants.MenuGutterFillKind)((Object)globalSetting);
        }
        return SubstanceConstants.MenuGutterFillKind.HARD;
    }

    public static Container getHeaderParent(Component c2) {
        Container result = null;
        for (Container comp = c2.getParent(); comp != null; comp = comp.getParent()) {
            if (comp instanceof JLayeredPane && result == null) {
                result = comp;
            }
            if (result != null || !(comp instanceof Window)) continue;
            result = comp;
        }
        return result;
    }

    public static void paintFocus(Graphics g2, Component mainComp, Component focusedComp, TransitionAwareUI transitionAwareUI, Shape focusShape, Rectangle textRect, float maxAlphaCoef, int extraPadding) {
        float focusStrength = transitionAwareUI.getTransitionTracker().getFocusStrength(focusedComp.hasFocus());
        if (focusStrength == 0.0f) {
            return;
        }
        SubstanceConstants.FocusKind focusKind = SubstanceCoreUtilities.getFocusKind(mainComp);
        if (focusKind == SubstanceConstants.FocusKind.NONE) {
            return;
        }
        Graphics2D graphics = (Graphics2D)g2.create();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        float alpha = maxAlphaCoef * focusStrength;
        graphics.setComposite(LafWidgetUtilities.getAlphaComposite(mainComp, alpha, g2));
        Color color = SubstanceColorUtilities.getFocusColor(mainComp, transitionAwareUI);
        graphics.setColor(color);
        focusKind.paintFocus(mainComp, focusedComp, transitionAwareUI, graphics, focusShape, textRect, extraPadding);
        graphics.dispose();
    }

    public static boolean isTitleCloseButton(JComponent ab) {
        return ab instanceof SubstanceTitleButton && Boolean.TRUE.equals(ab.getClientProperty("substancelaf.internal.isTitleCloseButton"));
    }

    public static void uninstallMenu(JMenuItem menuItem) {
        SubstanceMenu sMenu;
        ButtonUI menuItemUI;
        if (menuItem instanceof JMenu) {
            JMenu menu = (JMenu)menuItem;
            for (Component comp : menu.getMenuComponents()) {
                if (!(comp instanceof JMenuItem)) continue;
                SubstanceCoreUtilities.uninstallMenu((JMenuItem)comp);
            }
        }
        if ((menuItemUI = menuItem.getUI()) instanceof SubstanceMenu && (sMenu = (SubstanceMenu)((Object)menuItemUI)).getAssociatedMenuItem() != null) {
            menuItemUI.uninstallUI(menuItem);
        }
        for (ActionListener actionListener : menuItem.getActionListeners()) {
            menuItem.removeActionListener(actionListener);
        }
        menuItem.removeAll();
    }

    public static Icon getIcon(String iconResource) {
        ClassLoader cl = SubstanceCoreUtilities.getClassLoaderForResources();
        URL iconUrl = cl.getResource(iconResource);
        if (iconUrl == null) {
            return null;
        }
        return new IconUIResource(new ImageIcon(iconUrl));
    }

    public static ClassLoader getClassLoaderForResources() {
        ClassLoader cl = (ClassLoader)UIManager.get("ClassLoader");
        if (cl == null) {
            cl = Thread.currentThread().getContextClassLoader();
        }
        return cl;
    }

    public static boolean isCoveredByLightweightPopups(Component comp) {
        int popupIndexToStartWith;
        JRootPane rootPane = SwingUtilities.getRootPane(comp);
        if (rootPane == null) {
            return false;
        }
        Component[] popups = rootPane.getLayeredPane().getComponentsInLayer(JLayeredPane.POPUP_LAYER);
        if (popups == null) {
            return false;
        }
        Rectangle compBoundsConverted = SwingUtilities.convertRectangle(comp.getParent(), comp.getBounds(), rootPane.getLayeredPane());
        for (int i2 = popupIndexToStartWith = SubstanceCoreUtilities.getPopupParentIndexOf(comp, popups) - 1; i2 >= 0; --i2) {
            Component popup = popups[i2];
            if (!compBoundsConverted.intersects(popup.getBounds())) continue;
            return true;
        }
        return false;
    }

    public static int getPopupParentIndexOf(Component comp, Component[] popups) {
        for (int i2 = 0; i2 < popups.length; ++i2) {
            Component popup = popups[i2];
            for (Component currComp = comp; currComp != null; currComp = currComp.getParent()) {
                if (currComp != popup) continue;
                return i2;
            }
        }
        return popups.length;
    }

    public static ResourceBundle getResourceBundle(JComponent jcomp) {
        if (LafWidgetUtilities.toIgnoreGlobalLocale(jcomp)) {
            return SubstanceLookAndFeel.getLabelBundle(jcomp.getLocale());
        }
        return SubstanceLookAndFeel.getLabelBundle();
    }

    public static SubstanceBorderPainter getBorderPainter(Component comp) {
        return SubstanceCoreUtilities.getSkin(comp).getBorderPainter();
    }

    public static SubstanceBorderPainter getHighlightBorderPainter(Component comp) {
        SubstanceBorderPainter result = SubstanceCoreUtilities.getSkin(comp).getHighlightBorderPainter();
        if (result != null) {
            return result;
        }
        return SubstanceCoreUtilities.getBorderPainter(comp);
    }

    public static String getHierarchy(Component comp) {
        StringBuffer buffer = new StringBuffer();
        SubstanceCoreUtilities.getHierarchy(comp, buffer, 0);
        while (comp instanceof Window) {
            Window w2 = (Window)comp;
            if ((comp = w2.getOwner()) == null) continue;
            buffer.append("Owner --->\n");
            SubstanceCoreUtilities.getHierarchy(comp, buffer, 0);
        }
        return buffer.toString();
    }

    public static void getHierarchy(Component comp, StringBuffer buffer, int level) {
        for (int i2 = 0; i2 < level; ++i2) {
            buffer.append("   ");
        }
        String name = comp.getName();
        if (comp instanceof Dialog) {
            name = ((Dialog)comp).getTitle();
        }
        if (comp instanceof Frame) {
            name = ((Frame)comp).getTitle();
        }
        buffer.append(comp.getClass().getName() + "[" + name + "]\n");
        if (comp instanceof Container) {
            Container cont = (Container)comp;
            for (int i3 = 0; i3 < cont.getComponentCount(); ++i3) {
                SubstanceCoreUtilities.getHierarchy(cont.getComponent(i3), buffer, level + 1);
            }
        }
    }

    public static JComponent getTitlePane(JRootPane rootPane) {
        JInternalFrame jif = (JInternalFrame)SwingUtilities.getAncestorOfClass(JInternalFrame.class, rootPane);
        if (jif != null && jif.getUI() instanceof SubstanceInternalFrameUI) {
            SubstanceInternalFrameUI ui = (SubstanceInternalFrameUI)jif.getUI();
            return ui.getTitlePane();
        }
        SubstanceRootPaneUI ui = (SubstanceRootPaneUI)rootPane.getUI();
        if (ui == null) {
            return null;
        }
        return ui.getTitlePane();
    }

    public static Icon getArrowIcon(AbstractButton button, int orientation) {
        ArrowButtonTransitionAwareIcon result = new ArrowButtonTransitionAwareIcon(button, orientation);
        return result;
    }

    public static Icon getArrowIcon(JComponent comp, TransitionAwareIcon.TransitionAwareUIDelegate transitionAwareUIDelegate, int orientation) {
        ArrowButtonTransitionAwareIcon result = new ArrowButtonTransitionAwareIcon(comp, transitionAwareUIDelegate, orientation);
        return result;
    }

    public static double getColorizationFactor(Component c2) {
        Component invoker;
        JPopupMenu popupMenu = null;
        while (c2 != null) {
            JComponent jcomp;
            Object compProp;
            if (c2 instanceof JComponent && (compProp = (jcomp = (JComponent)c2).getClientProperty("substancelaf.colorizationFactor")) instanceof Double) {
                return (Double)compProp;
            }
            if (c2 instanceof JPopupMenu) {
                popupMenu = (JPopupMenu)c2;
            }
            c2 = c2.getParent();
        }
        if (popupMenu != null && popupMenu != (invoker = popupMenu.getInvoker())) {
            return SubstanceCoreUtilities.getColorizationFactor(popupMenu.getInvoker());
        }
        Object globalProp = UIManager.get("substancelaf.colorizationFactor");
        if (globalProp instanceof Double) {
            return (Double)globalProp;
        }
        return 0.5;
    }

    public static SubstanceSkin getSkin(Component c2) {
        Object skinProp;
        Container frame;
        if (!SubstanceLookAndFeel.isCurrentLookAndFeel()) {
            return null;
        }
        if (!SubstanceRootPaneUI.hasCustomSkinOnAtLeastOneRootPane()) {
            return SubstanceLookAndFeel.getCurrentSkin();
        }
        SubstanceComboPopup comboPopup = (SubstanceComboPopup)SwingUtilities.getAncestorOfClass(SubstanceComboPopup.class, c2);
        if (comboPopup != null) {
            return SubstanceCoreUtilities.getSkin(comboPopup.getCombobox());
        }
        JRootPane rootPane = SwingUtilities.getRootPane(c2);
        if (c2 instanceof SubstanceInternalFrameTitlePane && (frame = c2.getParent()) != null && frame instanceof JInternalFrame) {
            rootPane = ((JInternalFrame)frame).getRootPane();
        }
        if (c2 != null && c2.getParent() instanceof SubstanceInternalFrameTitlePane && (frame = c2.getParent().getParent()) != null && frame instanceof JInternalFrame) {
            rootPane = ((JInternalFrame)frame).getRootPane();
        }
        if (rootPane != null && (skinProp = rootPane.getClientProperty("substancelaf.skin")) instanceof SubstanceSkin) {
            return (SubstanceSkin)skinProp;
        }
        return SubstanceLookAndFeel.getCurrentSkin();
    }

    public static HashMapKey getHashKey(Object ... objects) {
        return new HashMapKey(objects);
    }

    public static void stopThreads() {
        TrackableThread.requestStopAllThreads();
    }

    public static String getVmParameter(String parameterName) {
        try {
            String paramValue = System.getProperty(parameterName);
            return paramValue;
        }
        catch (Exception exc) {
            return null;
        }
    }

    public static boolean reallyPrintThreadingExceptions() {
        return reallyPrint;
    }

    public static boolean reallyThrowThreadingExceptions() {
        return reallyThrow;
    }

    public static void testComponentCreationThreadingViolation(Component comp) {
        if ((SubstanceCoreUtilities.reallyPrintThreadingExceptions() || SubstanceCoreUtilities.reallyThrowThreadingExceptions()) && !SwingUtilities.isEventDispatchThread()) {
            UiThreadingViolationException uiThreadingViolationError = new UiThreadingViolationException("Component creation must be done on Event Dispatch Thread");
            if (SubstanceCoreUtilities.reallyPrintThreadingExceptions()) {
                uiThreadingViolationError.printStackTrace(System.err);
            }
            if (SubstanceCoreUtilities.reallyThrowThreadingExceptions()) {
                throw uiThreadingViolationError;
            }
        }
    }

    public static void testComponentStateChangeThreadingViolation(Component comp) {
        if ((SubstanceCoreUtilities.reallyPrintThreadingExceptions() || SubstanceCoreUtilities.reallyThrowThreadingExceptions()) && !SwingUtilities.isEventDispatchThread()) {
            UiThreadingViolationException uiThreadingViolationError = new UiThreadingViolationException("Component state change must be done on Event Dispatch Thread");
            if (SubstanceCoreUtilities.reallyPrintThreadingExceptions()) {
                uiThreadingViolationError.printStackTrace(System.err);
            }
            if (SubstanceCoreUtilities.reallyThrowThreadingExceptions()) {
                throw uiThreadingViolationError;
            }
        }
    }

    public static void testWindowCloseThreadingViolation(Window w2) {
        if ((SubstanceCoreUtilities.reallyPrintThreadingExceptions() || SubstanceCoreUtilities.reallyThrowThreadingExceptions()) && !SwingUtilities.isEventDispatchThread()) {
            UiThreadingViolationException uiThreadingViolationError = new UiThreadingViolationException("Window close must be done on Event Dispatch Thread");
            if (SubstanceCoreUtilities.reallyPrintThreadingExceptions()) {
                uiThreadingViolationError.printStackTrace(System.err);
            }
            if (SubstanceCoreUtilities.reallyThrowThreadingExceptions()) {
                throw uiThreadingViolationError;
            }
        }
    }

    public static void traceSubstanceApiUsage(Component comp, String message) {
        Window w2 = SwingUtilities.getWindowAncestor(comp);
        String wTitle = null;
        if (w2 instanceof Frame) {
            wTitle = ((Frame)w2).getTitle();
        }
        if (w2 instanceof Dialog) {
            wTitle = ((Dialog)w2).getTitle();
        }
        throw new IllegalArgumentException(message + " [component " + comp.getClass().getSimpleName() + " in window " + w2.getClass().getSimpleName() + ":'" + wTitle + "' under " + UIManager.getLookAndFeel().getName() + "]");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static BufferedImage getScaledIconImage(List<Image> imageList, int width, int height) {
        if (width == 0 || height == 0) {
            return null;
        }
        Image bestImage = null;
        int bestWidth = 0;
        int bestHeight = 0;
        double bestSimilarity = 3.0;
        for (Image im : imageList) {
            double scaleMeasure;
            int adjh;
            int adjw;
            int ih;
            int iw;
            if (im == null) continue;
            try {
                iw = im.getWidth(null);
                ih = im.getHeight(null);
            }
            catch (Exception e2) {
                continue;
            }
            if (iw <= 0 || ih <= 0) continue;
            double scaleFactor = Math.min((double)width / (double)iw, (double)height / (double)ih);
            if (scaleFactor >= 2.0) {
                scaleFactor = Math.floor(scaleFactor);
                adjw = iw * (int)scaleFactor;
                adjh = ih * (int)scaleFactor;
                scaleMeasure = 1.0 - 0.5 / scaleFactor;
            } else if (scaleFactor >= 1.0) {
                adjw = iw;
                adjh = ih;
                scaleMeasure = 0.0;
            } else if (scaleFactor >= 0.75) {
                adjw = iw * 3 / 4;
                adjh = ih * 3 / 4;
                scaleMeasure = 0.3;
            } else if (scaleFactor >= 0.6666) {
                adjw = iw * 2 / 3;
                adjh = ih * 2 / 3;
                scaleMeasure = 0.33;
            } else {
                double scaleDivider = Math.ceil(1.0 / scaleFactor);
                adjw = (int)Math.round((double)iw / scaleDivider);
                adjh = (int)Math.round((double)ih / scaleDivider);
                scaleMeasure = 1.0 - 1.0 / scaleDivider;
            }
            double similarity = ((double)width - (double)adjw) / (double)width + ((double)height - (double)adjh) / (double)height + scaleMeasure;
            if (similarity < bestSimilarity) {
                bestSimilarity = similarity;
                bestImage = im;
                bestWidth = adjw;
                bestHeight = adjh;
            }
            if (similarity != 0.0) continue;
            break;
        }
        if (bestImage == null) {
            return null;
        }
        BufferedImage bimage = new BufferedImage(width, height, 2);
        Graphics2D g2 = bimage.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        try {
            int x2 = (width - bestWidth) / 2;
            int y2 = (height - bestHeight) / 2;
            g2.drawImage(bestImage, x2, y2, bestWidth, bestHeight, null);
        }
        finally {
            g2.dispose();
        }
        return bimage;
    }

    public static boolean canReplaceChildBackgroundColor(Color background) {
        return background instanceof UIResource || background instanceof SubstanceColorResource;
    }

    public static JTextComponent getTextComponentForTransitions(Component c2) {
        if (!(c2 instanceof JComponent)) {
            return null;
        }
        TextComponentAware tcaui = (TextComponentAware)((JComponent)c2).getClientProperty(TEXT_COMPONENT_AWARE);
        if (tcaui != null) {
            return tcaui.getTextComponent(c2);
        }
        if (c2 instanceof JTextComponent) {
            return (JTextComponent)c2;
        }
        return null;
    }

    public static SwingRepaintCallback getTextComponentRepaintCallback(JTextComponent textComponent) {
        for (Container c2 = textComponent; c2 != null; c2 = c2.getParent()) {
            TextComponentAware tcaui;
            if (!(c2 instanceof JComponent) || (tcaui = (TextComponentAware)((JComponent)c2).getClientProperty(TEXT_COMPONENT_AWARE)) == null) continue;
            return new SwingRepaintCallback(c2);
        }
        return new SwingRepaintCallback(textComponent);
    }

    public static boolean isOpaque(Component c2) {
        return c2.isOpaque();
    }

    public static interface TextComponentAware<T> {
        public JTextComponent getTextComponent(T var1);
    }
}

