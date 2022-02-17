/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils.icon;

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JSlider;
import javax.swing.JTree;
import javax.swing.plaf.SliderUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicSliderUI;
import org.pushingpixels.substance.api.ColorSchemeAssociationKind;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.painter.border.SubstanceBorderPainter;
import org.pushingpixels.substance.api.painter.fill.SubstanceFillPainter;
import org.pushingpixels.substance.internal.animation.StateTransitionTracker;
import org.pushingpixels.substance.internal.animation.TransitionAwareUI;
import org.pushingpixels.substance.internal.utils.HashMapKey;
import org.pushingpixels.substance.internal.utils.LazyResettableHashMap;
import org.pushingpixels.substance.internal.utils.SubstanceColorSchemeUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceImageCreator;
import org.pushingpixels.substance.internal.utils.SubstanceOutlineUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;

public class SubstanceIconFactory {
    private static LazyResettableHashMap<Icon> sliderHorizontalIcons = new LazyResettableHashMap("SubstanceIconFactory.sliderHorizontalIcon");
    private static LazyResettableHashMap<Icon> sliderRoundIcons = new LazyResettableHashMap("SubstanceIconFactory.sliderRoundIcon");
    private static LazyResettableHashMap<Icon> sliderVerticalIcons = new LazyResettableHashMap("SubstanceIconFactory.sliderVerticalIcon");
    private static LazyResettableHashMap<Icon> treeIcons = new LazyResettableHashMap("SubstanceIconFactory.treeIcon");
    private static final Map<IconKind, LazyResettableHashMap<Icon>> titlePaneIcons = SubstanceIconFactory.createTitlePaneIcons();

    public static Icon getSliderHorizontalIcon(int size, boolean isMirrorred) {
        HashMapKey key = SubstanceCoreUtilities.getHashKey(size, isMirrorred);
        if (sliderHorizontalIcons.get(key) == null) {
            SliderHorizontalIcon icon = new SliderHorizontalIcon(size, isMirrorred);
            sliderHorizontalIcons.put(key, icon);
        }
        return sliderHorizontalIcons.get(key);
    }

    public static Icon getSliderRoundIcon(int size) {
        HashMapKey key = SubstanceCoreUtilities.getHashKey(size);
        if (sliderRoundIcons.get(key) == null) {
            SliderRoundIcon icon = new SliderRoundIcon(size);
            sliderRoundIcons.put(key, icon);
        }
        return sliderRoundIcons.get(key);
    }

    public static Icon getSliderVerticalIcon(int size, boolean isMirrorred) {
        HashMapKey key = SubstanceCoreUtilities.getHashKey(size, isMirrorred);
        if (sliderVerticalIcons.get(key) == null) {
            SliderVerticalIcon icon = new SliderVerticalIcon(size, isMirrorred);
            sliderVerticalIcons.put(key, icon);
        }
        return sliderVerticalIcons.get(key);
    }

    public static Icon getTreeIcon(JTree tree, boolean isCollapsed) {
        int fontSize = SubstanceSizeUtils.getComponentFontSize(tree);
        int size = SubstanceSizeUtils.getTreeIconSize(fontSize);
        HashMapKey key = SubstanceCoreUtilities.getHashKey(size, isCollapsed);
        if (treeIcons.get(key) == null) {
            TreeIcon icon = new TreeIcon(size, isCollapsed);
            treeIcons.put(key, icon);
        }
        return treeIcons.get(key);
    }

    private static Map<IconKind, LazyResettableHashMap<Icon>> createTitlePaneIcons() {
        HashMap<IconKind, LazyResettableHashMap<Icon>> result = new HashMap<IconKind, LazyResettableHashMap<Icon>>();
        result.put(IconKind.CLOSE, new LazyResettableHashMap("Close title pane icons"));
        result.put(IconKind.MINIMIZE, new LazyResettableHashMap("Minimize title pane icons"));
        result.put(IconKind.MAXIMIZE, new LazyResettableHashMap("Maximize title pane icons"));
        result.put(IconKind.RESTORE, new LazyResettableHashMap("Restore title pane icons"));
        return result;
    }

    public static Icon getTitlePaneIcon(IconKind iconKind, SubstanceColorScheme scheme, SubstanceColorScheme backgroundScheme) {
        LazyResettableHashMap<Icon> kindMap = titlePaneIcons.get((Object)iconKind);
        HashMapKey key = SubstanceCoreUtilities.getHashKey(scheme.getDisplayName(), backgroundScheme.getDisplayName());
        Icon result = kindMap.get(key);
        if (result != null) {
            return result;
        }
        switch (iconKind) {
            case CLOSE: {
                result = SubstanceImageCreator.getCloseIcon(scheme, backgroundScheme);
                break;
            }
            case MINIMIZE: {
                result = SubstanceImageCreator.getMinimizeIcon(scheme, backgroundScheme);
                break;
            }
            case MAXIMIZE: {
                result = SubstanceImageCreator.getMaximizeIcon(scheme, backgroundScheme);
                break;
            }
            case RESTORE: {
                result = SubstanceImageCreator.getRestoreIcon(scheme, backgroundScheme);
            }
        }
        kindMap.put(key, result);
        return result;
    }

    public static enum IconKind {
        CLOSE,
        MINIMIZE,
        MAXIMIZE,
        RESTORE;

    }

    private static class TreeIcon
    implements Icon,
    UIResource {
        private static LazyResettableHashMap<Icon> icons = new LazyResettableHashMap("SubstanceIconFactory.TreeIcon");
        private boolean isCollapsed;
        private int size;

        public TreeIcon(int size, boolean isCollapsed) {
            this.isCollapsed = isCollapsed;
            this.size = size;
        }

        private static Icon getIcon(JTree tree, boolean isCollapsed) {
            ComponentState state = tree == null || tree.isEnabled() ? ComponentState.ENABLED : ComponentState.DISABLED_UNSELECTED;
            SubstanceColorScheme fillScheme = SubstanceColorSchemeUtilities.getColorScheme(tree, state);
            SubstanceColorScheme borderScheme = SubstanceColorSchemeUtilities.getColorScheme(tree, ColorSchemeAssociationKind.BORDER, state);
            int fontSize = SubstanceSizeUtils.getComponentFontSize(tree);
            HashMapKey key = SubstanceCoreUtilities.getHashKey(fontSize, fillScheme.getDisplayName(), borderScheme.getDisplayName(), isCollapsed);
            Icon result = icons.get(key);
            if (result != null) {
                return result;
            }
            result = new ImageIcon(SubstanceImageCreator.getTreeIcon(tree, fillScheme, borderScheme, isCollapsed));
            icons.put(key, result);
            return result;
        }

        @Override
        public void paintIcon(Component c2, Graphics g2, int x2, int y2) {
            if (!(g2 instanceof Graphics2D)) {
                return;
            }
            JTree tree = c2 instanceof JTree ? (JTree)c2 : null;
            Icon iconToDraw = TreeIcon.getIcon(tree, this.isCollapsed);
            iconToDraw.paintIcon(c2, g2, x2, y2);
        }

        @Override
        public int getIconWidth() {
            return this.size;
        }

        @Override
        public int getIconHeight() {
            return this.size;
        }
    }

    private static class SliderVerticalIcon
    implements Icon,
    UIResource {
        private static LazyResettableHashMap<Icon> icons = new LazyResettableHashMap("SubstanceIconFactory.SliderVerticalIcon");
        private int size;
        private boolean isMirrorred;

        public SliderVerticalIcon(int size, boolean isMirrorred) {
            this.size = size;
            this.isMirrorred = isMirrorred;
        }

        private Icon getIcon(JSlider slider, StateTransitionTracker stateTransitionTracker) {
            StateTransitionTracker.ModelStateInfo modelStateInfo = stateTransitionTracker.getModelStateInfo();
            Map<ComponentState, StateTransitionTracker.StateContributionInfo> activeStates = modelStateInfo.getStateContributionMap();
            ComponentState currState = stateTransitionTracker.getModelStateInfo().getCurrModelState();
            float activeStrength = stateTransitionTracker.getActiveStrength();
            int height = (int)((double)this.size * (2.0 + (double)activeStrength) / 3.0);
            height = Math.min(height, this.size - 2);
            int delta = (this.size - height) / 2 - 1;
            SubstanceFillPainter fillPainter = SubstanceCoreUtilities.getFillPainter(slider);
            SubstanceBorderPainter borderPainter = SubstanceCoreUtilities.getBorderPainter(slider);
            SubstanceColorScheme baseFillScheme = SubstanceColorSchemeUtilities.getColorScheme(slider, currState);
            SubstanceColorScheme baseBorderScheme = SubstanceColorSchemeUtilities.getColorScheme(slider, ColorSchemeAssociationKind.BORDER, currState);
            HashMapKey baseKey = SubstanceCoreUtilities.getHashKey(this.size, height, slider.getComponentOrientation(), baseFillScheme.getDisplayName(), baseBorderScheme.getDisplayName(), fillPainter.getDisplayName(), borderPainter.getDisplayName(), this.isMirrorred);
            Icon baseLayer = icons.get(baseKey);
            if (baseLayer == null) {
                baseLayer = this.getSingleLayer(slider, height, delta, fillPainter, borderPainter, baseFillScheme, baseBorderScheme);
                icons.put(baseKey, baseLayer);
            }
            if (currState.isDisabled() || activeStates.size() == 1) {
                return baseLayer;
            }
            BufferedImage result = SubstanceCoreUtilities.getBlankImage(baseLayer.getIconWidth(), baseLayer.getIconHeight());
            Graphics2D g2d = result.createGraphics();
            baseLayer.paintIcon(slider, g2d, 0, 0);
            for (Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> activeEntry : activeStates.entrySet()) {
                float contribution;
                ComponentState activeState = activeEntry.getKey();
                if (activeState == currState || (contribution = activeEntry.getValue().getContribution()) == 0.0f) continue;
                SubstanceColorScheme fillScheme = SubstanceColorSchemeUtilities.getColorScheme(slider, activeState);
                SubstanceColorScheme borderScheme = SubstanceColorSchemeUtilities.getColorScheme(slider, ColorSchemeAssociationKind.BORDER, activeState);
                HashMapKey key = SubstanceCoreUtilities.getHashKey(this.size, height, slider.getComponentOrientation(), fillScheme.getDisplayName(), borderScheme.getDisplayName(), fillPainter.getDisplayName(), borderPainter.getDisplayName(), this.isMirrorred);
                Icon layer = icons.get(key);
                if (layer == null) {
                    layer = this.getSingleLayer(slider, height, delta, fillPainter, borderPainter, fillScheme, borderScheme);
                    icons.put(key, layer);
                }
                g2d.setComposite(AlphaComposite.SrcOver.derive(contribution));
                layer.paintIcon(slider, g2d, 0, 0);
            }
            g2d.dispose();
            return new ImageIcon(result);
        }

        private Icon getSingleLayer(JSlider slider, int height, int delta, SubstanceFillPainter fillPainter, SubstanceBorderPainter borderPainter, SubstanceColorScheme fillScheme, SubstanceColorScheme borderScheme) {
            int borderDelta = (int)Math.floor((double)SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(slider)) / 2.0);
            GeneralPath contour = SubstanceOutlineUtilities.getTriangleButtonOutline(height, this.size, 2.0f, borderDelta);
            BufferedImage stateImage = SubstanceCoreUtilities.getBlankImage(this.size - 1, this.size - 1);
            Graphics2D g2d = stateImage.createGraphics();
            g2d.translate(delta, 0);
            fillPainter.paintContourBackground(g2d, slider, height, this.size, contour, false, fillScheme, true);
            int borderThickness = (int)SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(slider));
            GeneralPath contourInner = SubstanceOutlineUtilities.getTriangleButtonOutline(height, this.size, 2.0f, borderThickness + borderDelta);
            borderPainter.paintBorder(g2d, slider, height, this.size - 1, contour, contourInner, borderScheme);
            stateImage = this.isMirrorred ? SubstanceImageCreator.getRotated(stateImage, 1) : SubstanceImageCreator.getRotated(stateImage, 3);
            if (!slider.getComponentOrientation().isLeftToRight()) {
                stateImage = SubstanceImageCreator.getRotated(stateImage, 2);
            }
            return new ImageIcon(stateImage);
        }

        @Override
        public void paintIcon(Component c2, Graphics g2, int x2, int y2) {
            if (!(g2 instanceof Graphics2D)) {
                return;
            }
            JSlider slider = (JSlider)c2;
            TransitionAwareUI transitionAwareUI = (TransitionAwareUI)((Object)slider.getUI());
            StateTransitionTracker stateTransitionTracker = transitionAwareUI.getTransitionTracker();
            Icon iconToDraw = this.getIcon(slider, stateTransitionTracker);
            iconToDraw.paintIcon(c2, g2, x2, y2);
        }

        @Override
        public int getIconWidth() {
            return this.size - 1;
        }

        @Override
        public int getIconHeight() {
            return this.size - 1;
        }
    }

    private static class SliderRoundIcon
    implements Icon,
    UIResource {
        private static LazyResettableHashMap<Icon> icons = new LazyResettableHashMap("SubstanceIconFactory.SliderRoundIcon");
        private int size;

        public SliderRoundIcon(int size) {
            this.size = size;
        }

        private Icon getIcon(JSlider slider, StateTransitionTracker stateTransitionTracker) {
            StateTransitionTracker.ModelStateInfo modelStateInfo = stateTransitionTracker.getModelStateInfo();
            Map<ComponentState, StateTransitionTracker.StateContributionInfo> activeStates = modelStateInfo.getStateContributionMap();
            ComponentState currState = stateTransitionTracker.getModelStateInfo().getCurrModelState();
            float activeStrength = stateTransitionTracker.getActiveStrength();
            int width = (int)((double)this.size * (2.0 + (double)activeStrength) / 3.0);
            if ((width = Math.min(width, this.size - 2)) % 2 == 0) {
                --width;
            }
            int delta = (this.size - width) / 2;
            SubstanceFillPainter fillPainter = SubstanceCoreUtilities.getFillPainter(slider);
            SubstanceBorderPainter borderPainter = SubstanceCoreUtilities.getBorderPainter(slider);
            SubstanceColorScheme baseFillScheme = SubstanceColorSchemeUtilities.getColorScheme(slider, currState);
            SubstanceColorScheme baseBorderScheme = SubstanceColorSchemeUtilities.getColorScheme(slider, ColorSchemeAssociationKind.BORDER, currState);
            HashMapKey baseKey = SubstanceCoreUtilities.getHashKey(this.size, width, baseFillScheme.getDisplayName(), baseBorderScheme.getDisplayName(), fillPainter.getDisplayName(), borderPainter.getDisplayName());
            Icon baseLayer = icons.get(baseKey);
            if (baseLayer == null) {
                baseLayer = this.getSingleLayer(slider, width, delta, fillPainter, borderPainter, baseFillScheme, baseBorderScheme);
                icons.put(baseKey, baseLayer);
            }
            if (currState.isDisabled() || activeStates.size() == 1) {
                return baseLayer;
            }
            BufferedImage result = SubstanceCoreUtilities.getBlankImage(baseLayer.getIconWidth(), baseLayer.getIconHeight());
            Graphics2D g2d = result.createGraphics();
            baseLayer.paintIcon(slider, g2d, 0, 0);
            for (Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> activeEntry : activeStates.entrySet()) {
                float contribution;
                ComponentState activeState = activeEntry.getKey();
                if (activeState == currState || (contribution = activeEntry.getValue().getContribution()) == 0.0f) continue;
                SubstanceColorScheme fillScheme = SubstanceColorSchemeUtilities.getColorScheme(slider, activeState);
                SubstanceColorScheme borderScheme = SubstanceColorSchemeUtilities.getColorScheme(slider, ColorSchemeAssociationKind.BORDER, activeState);
                HashMapKey key = SubstanceCoreUtilities.getHashKey(this.size, width, fillScheme.getDisplayName(), borderScheme.getDisplayName(), fillPainter.getDisplayName(), borderPainter.getDisplayName());
                Icon layer = icons.get(key);
                if (layer == null) {
                    layer = this.getSingleLayer(slider, width, delta, fillPainter, borderPainter, fillScheme, borderScheme);
                    icons.put(key, layer);
                }
                g2d.setComposite(AlphaComposite.SrcOver.derive(contribution));
                layer.paintIcon(slider, g2d, 0, 0);
            }
            g2d.dispose();
            return new ImageIcon(result);
        }

        private Icon getSingleLayer(JSlider slider, int width, int delta, SubstanceFillPainter fillPainter, SubstanceBorderPainter borderPainter, SubstanceColorScheme fillScheme, SubstanceColorScheme borderScheme) {
            int borderDelta = (int)Math.floor((double)SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(slider)) / 2.0);
            Ellipse2D.Float contour = new Ellipse2D.Float(borderDelta, borderDelta, width - 2 * borderDelta - 1, width - 2 * borderDelta - 1);
            BufferedImage stateImage = SubstanceCoreUtilities.getBlankImage(this.size - 1, this.size - 1);
            Graphics2D g2d = stateImage.createGraphics();
            g2d.translate(delta, delta);
            fillPainter.paintContourBackground(g2d, slider, width, this.size - 1, contour, false, fillScheme, true);
            int borderThickness = (int)SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(slider));
            Ellipse2D.Float contourInner = new Ellipse2D.Float(borderDelta + borderThickness, borderDelta + borderThickness, width - 2 * borderDelta - 2 * borderThickness - 1, width - 2 * borderDelta - 2 * borderThickness - 1);
            borderPainter.paintBorder(g2d, slider, width, this.size - 1, contour, contourInner, borderScheme);
            return new ImageIcon(stateImage);
        }

        @Override
        public void paintIcon(Component c2, Graphics g2, int x2, int y2) {
            if (!(g2 instanceof Graphics2D)) {
                return;
            }
            JSlider slider = (JSlider)c2;
            TransitionAwareUI transitionAwareUI = (TransitionAwareUI)((Object)slider.getUI());
            StateTransitionTracker stateTransitionTracker = transitionAwareUI.getTransitionTracker();
            Icon iconToDraw = this.getIcon(slider, stateTransitionTracker);
            iconToDraw.paintIcon(c2, g2, x2, y2);
        }

        @Override
        public int getIconWidth() {
            return this.size - 1;
        }

        @Override
        public int getIconHeight() {
            return this.size - 1;
        }
    }

    private static class SliderHorizontalIcon
    implements Icon,
    UIResource {
        private static LazyResettableHashMap<Icon> icons = new LazyResettableHashMap("SubstanceIconFactory.SliderHorizontalIcon");
        private int size;
        private boolean isMirrorred;

        public SliderHorizontalIcon(int size, boolean isMirrorred) {
            this.size = size;
            this.isMirrorred = isMirrorred;
        }

        private Icon getIcon(JSlider slider, StateTransitionTracker stateTransitionTracker) {
            StateTransitionTracker.ModelStateInfo modelStateInfo = stateTransitionTracker.getModelStateInfo();
            Map<ComponentState, StateTransitionTracker.StateContributionInfo> activeStates = modelStateInfo.getStateContributionMap();
            ComponentState currState = stateTransitionTracker.getModelStateInfo().getCurrModelState();
            float activeStrength = stateTransitionTracker.getActiveStrength();
            int width = (int)((double)this.size * (2.0 + (double)activeStrength) / 3.0);
            width = Math.min(width, this.size - 2);
            int delta = (this.size - width) / 2;
            SubstanceFillPainter fillPainter = SubstanceCoreUtilities.getFillPainter(slider);
            SubstanceBorderPainter borderPainter = SubstanceCoreUtilities.getBorderPainter(slider);
            SubstanceColorScheme baseFillScheme = SubstanceColorSchemeUtilities.getColorScheme(slider, currState);
            SubstanceColorScheme baseBorderScheme = SubstanceColorSchemeUtilities.getColorScheme(slider, ColorSchemeAssociationKind.BORDER, currState);
            HashMapKey baseKey = SubstanceCoreUtilities.getHashKey(this.size, width, baseFillScheme.getDisplayName(), baseBorderScheme.getDisplayName(), fillPainter.getDisplayName(), borderPainter.getDisplayName(), this.isMirrorred);
            Icon baseLayer = icons.get(baseKey);
            if (baseLayer == null) {
                baseLayer = this.getSingleLayer(slider, width, delta, fillPainter, borderPainter, baseFillScheme, baseBorderScheme);
                icons.put(baseKey, baseLayer);
            }
            if (currState.isDisabled() || activeStates.size() == 1) {
                return baseLayer;
            }
            BufferedImage result = SubstanceCoreUtilities.getBlankImage(baseLayer.getIconWidth(), baseLayer.getIconHeight());
            Graphics2D g2d = result.createGraphics();
            baseLayer.paintIcon(slider, g2d, 0, 0);
            for (Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> activeEntry : activeStates.entrySet()) {
                float contribution;
                ComponentState activeState = activeEntry.getKey();
                if (activeState == currState || (contribution = activeEntry.getValue().getContribution()) == 0.0f) continue;
                SubstanceColorScheme fillScheme = SubstanceColorSchemeUtilities.getColorScheme(slider, activeState);
                SubstanceColorScheme borderScheme = SubstanceColorSchemeUtilities.getColorScheme(slider, ColorSchemeAssociationKind.BORDER, activeState);
                HashMapKey key = SubstanceCoreUtilities.getHashKey(this.size, width, fillScheme.getDisplayName(), borderScheme.getDisplayName(), fillPainter.getDisplayName(), borderPainter.getDisplayName(), this.isMirrorred);
                Icon layer = icons.get(key);
                if (layer == null) {
                    layer = this.getSingleLayer(slider, width, delta, fillPainter, borderPainter, fillScheme, borderScheme);
                    icons.put(key, layer);
                }
                g2d.setComposite(AlphaComposite.SrcOver.derive(contribution));
                layer.paintIcon(slider, g2d, 0, 0);
            }
            g2d.dispose();
            return new ImageIcon(result);
        }

        private Icon getSingleLayer(JSlider slider, int width, int delta, SubstanceFillPainter fillPainter, SubstanceBorderPainter borderPainter, SubstanceColorScheme fillScheme, SubstanceColorScheme borderScheme) {
            int borderDelta = (int)Math.floor((double)SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(slider)) / 2.0);
            GeneralPath contour = SubstanceOutlineUtilities.getTriangleButtonOutline(width, this.size - 1, 2.0f, borderDelta);
            BufferedImage stateImage = SubstanceCoreUtilities.getBlankImage(this.size - 1, this.size - 1);
            Graphics2D g2d = stateImage.createGraphics();
            g2d.translate(delta, 0);
            fillPainter.paintContourBackground(g2d, slider, width, this.size - 1, contour, false, fillScheme, true);
            int borderThickness = (int)SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(slider));
            GeneralPath contourInner = SubstanceOutlineUtilities.getTriangleButtonOutline(width, this.size - 1, 2.0f, borderThickness + borderDelta);
            borderPainter.paintBorder(g2d, slider, width, this.size - 1, contour, contourInner, borderScheme);
            g2d.translate(-delta, 0);
            if (this.isMirrorred) {
                stateImage = SubstanceImageCreator.getRotated(stateImage, 2);
            }
            return new ImageIcon(stateImage);
        }

        @Override
        public void paintIcon(Component c2, Graphics g2, int x2, int y2) {
            if (!(g2 instanceof Graphics2D)) {
                return;
            }
            JSlider slider = (JSlider)c2;
            TransitionAwareUI transitionAwareUI = (TransitionAwareUI)((Object)slider.getUI());
            StateTransitionTracker stateTransitionTracker = transitionAwareUI.getTransitionTracker();
            Icon iconToDraw = this.getIcon(slider, stateTransitionTracker);
            iconToDraw.paintIcon(c2, g2, x2, y2);
        }

        @Override
        public int getIconWidth() {
            return this.size - 1;
        }

        @Override
        public int getIconHeight() {
            return this.size - 1;
        }
    }

    private static class TrackableSlider
    implements TransitionAwareUI {
        private JSlider slider;
        private Field thumbRectField;
        private ButtonModel transitionModel;
        private StateTransitionTracker stateTransitionTracker;

        public TrackableSlider(JSlider slider, ButtonModel transitionModel) {
            this.slider = slider;
            this.transitionModel = transitionModel;
            SliderUI sliderUI = slider.getUI();
            if (sliderUI instanceof BasicSliderUI) {
                try {
                    this.thumbRectField = BasicSliderUI.class.getDeclaredField("thumbRect");
                    this.thumbRectField.setAccessible(true);
                }
                catch (Exception exc) {
                    this.thumbRectField = null;
                }
            }
            this.stateTransitionTracker = new StateTransitionTracker(this.slider, this.transitionModel);
        }

        @Override
        public boolean isInside(MouseEvent me) {
            try {
                Rectangle thumbB = (Rectangle)this.thumbRectField.get(this.slider.getUI());
                if (thumbB == null) {
                    return false;
                }
                return thumbB.contains(me.getX(), me.getY());
            }
            catch (Exception exc) {
                return false;
            }
        }

        @Override
        public StateTransitionTracker getTransitionTracker() {
            return this.stateTransitionTracker;
        }
    }
}

