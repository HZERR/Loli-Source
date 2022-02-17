/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils;

import java.awt.Color;
import java.awt.Component;
import java.util.Map;
import javax.swing.AbstractButton;
import javax.swing.CellRendererPane;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.UIResource;
import javax.swing.text.JTextComponent;
import org.pushingpixels.substance.api.ColorSchemeAssociationKind;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.ComponentStateFacet;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.internal.animation.StateTransitionTracker;
import org.pushingpixels.substance.internal.animation.TransitionAwareUI;
import org.pushingpixels.substance.internal.utils.SubstanceColorSchemeUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;

public class SubstanceColorUtilities {
    private static final ComponentState EDITABLE = new ComponentState("editable", ComponentState.ENABLED, new ComponentStateFacet[]{ComponentStateFacet.ENABLE, ComponentStateFacet.EDITABLE}, null);
    private static final ComponentState UNEDITABLE = new ComponentState("uneditable", ComponentState.DISABLED_SELECTED, new ComponentStateFacet[]{ComponentStateFacet.ENABLE}, new ComponentStateFacet[]{ComponentStateFacet.EDITABLE});
    private static final ComponentState EDITABLE_DISABLED = new ComponentState("editable disabled", ComponentState.DISABLED_UNSELECTED, new ComponentStateFacet[]{ComponentStateFacet.EDITABLE}, new ComponentStateFacet[]{ComponentStateFacet.ENABLE});
    private static final ComponentState UNEDITABLE_DISABLED = new ComponentState("uneditable disabled", ComponentState.DISABLED_UNSELECTED, null, new ComponentStateFacet[]{ComponentStateFacet.ENABLE, ComponentStateFacet.EDITABLE});

    public static Color getTopBorderColor(SubstanceColorScheme scheme) {
        return scheme.getUltraDarkColor();
    }

    public static Color getMidBorderColor(SubstanceColorScheme scheme) {
        return scheme.getDarkColor();
    }

    public static Color getBottomBorderColor(SubstanceColorScheme scheme) {
        return SubstanceColorUtilities.getInterpolatedColor(scheme.getDarkColor(), scheme.getMidColor(), 0.5);
    }

    public static Color getTopFillColor(SubstanceColorScheme scheme) {
        Color c2 = SubstanceColorUtilities.getInterpolatedColor(scheme.getDarkColor(), scheme.getMidColor(), 0.4);
        return c2;
    }

    public static Color getMidFillColor(SubstanceColorScheme scheme) {
        return scheme.getMidColor();
    }

    public static Color getBottomFillColor(SubstanceColorScheme scheme) {
        return scheme.getUltraLightColor();
    }

    public static Color getTopShineColor(SubstanceColorScheme scheme) {
        return SubstanceColorUtilities.getBottomFillColor(scheme);
    }

    public static Color getBottomShineColor(SubstanceColorScheme scheme) {
        return scheme.getLightColor();
    }

    public static int getInterpolatedRGB(Color color1, Color color2, double color1Likeness) {
        if (color1Likeness < 0.0 || color1Likeness > 1.0) {
            throw new IllegalArgumentException("Color likeness should be in 0.0-1.0 range [is " + color1Likeness + "]");
        }
        int lr = color1.getRed();
        int lg = color1.getGreen();
        int lb = color1.getBlue();
        int la = color1.getAlpha();
        int dr = color2.getRed();
        int dg = color2.getGreen();
        int db = color2.getBlue();
        int da = color2.getAlpha();
        int r2 = lr == dr ? lr : (int)Math.round(color1Likeness * (double)lr + (1.0 - color1Likeness) * (double)dr);
        int g2 = lg == dg ? lg : (int)Math.round(color1Likeness * (double)lg + (1.0 - color1Likeness) * (double)dg);
        int b2 = lb == db ? lb : (int)Math.round(color1Likeness * (double)lb + (1.0 - color1Likeness) * (double)db);
        int a2 = la == da ? la : (int)Math.round(color1Likeness * (double)la + (1.0 - color1Likeness) * (double)da);
        return a2 << 24 | r2 << 16 | g2 << 8 | b2;
    }

    public static Color getInterpolatedColor(Color color1, Color color2, double color1Likeness) {
        if (color1.equals(color2)) {
            return color1;
        }
        if (color1Likeness == 1.0) {
            return color1;
        }
        if (color1Likeness == 0.0) {
            return color2;
        }
        return new Color(SubstanceColorUtilities.getInterpolatedRGB(color1, color2, color1Likeness), true);
    }

    public static Color invertColor(Color color) {
        return new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue(), color.getAlpha());
    }

    public static Color getNegativeColor(Color color) {
        return new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue(), color.getAlpha());
    }

    public static int getNegativeColor(int rgb) {
        int transp = rgb >>> 24 & 0xFF;
        int r2 = rgb >>> 16 & 0xFF;
        int g2 = rgb >>> 8 & 0xFF;
        int b2 = rgb >>> 0 & 0xFF;
        return transp << 24 | 255 - r2 << 16 | 255 - g2 << 8 | 255 - b2;
    }

    public static Color getAlphaColor(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    public static Color getSaturatedColor(Color color, double factor) {
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        if (red == green || green == blue) {
            return color;
        }
        float[] hsbvals = new float[3];
        Color.RGBtoHSB(red, green, blue, hsbvals);
        float sat = hsbvals[1];
        sat = factor > 0.0 ? (sat += (float)factor * (1.0f - sat)) : (sat += (float)factor * sat);
        return new Color(Color.HSBtoRGB(hsbvals[0], sat, hsbvals[2]));
    }

    public static Color getHueShiftedColor(Color color, double hueShift) {
        float[] hsbvals = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsbvals);
        float hue = hsbvals[0];
        hue = (float)((double)hue + hueShift);
        if ((double)hue < 0.0) {
            hue = (float)((double)hue + 1.0);
        }
        if ((double)hue > 1.0) {
            hue = (float)((double)hue - 1.0);
        }
        return new Color(Color.HSBtoRGB(hue, hsbvals[1], hsbvals[2]));
    }

    public static Color deriveByBrightness(Color original, Color brightnessSource) {
        float[] hsbvalsOrig = new float[3];
        Color.RGBtoHSB(original.getRed(), original.getGreen(), original.getBlue(), hsbvalsOrig);
        float[] hsbvalsBrightnessSrc = new float[3];
        Color.RGBtoHSB(brightnessSource.getRed(), brightnessSource.getGreen(), brightnessSource.getBlue(), hsbvalsBrightnessSrc);
        return new Color(Color.HSBtoRGB(hsbvalsOrig[0], hsbvalsOrig[1], (hsbvalsBrightnessSrc[2] + hsbvalsOrig[2]) / 2.0f));
    }

    public static ColorUIResource getForegroundColor(SubstanceColorScheme scheme) {
        return new ColorUIResource(scheme.getForegroundColor());
    }

    public static Color getLighterColor(Color color, double diff) {
        int r2 = color.getRed() + (int)(diff * (double)(255 - color.getRed()));
        int g2 = color.getGreen() + (int)(diff * (double)(255 - color.getGreen()));
        int b2 = color.getBlue() + (int)(diff * (double)(255 - color.getBlue()));
        return new Color(r2, g2, b2);
    }

    public static Color getDarkerColor(Color color, double diff) {
        int r2 = (int)((1.0 - diff) * (double)color.getRed());
        int g2 = (int)((1.0 - diff) * (double)color.getGreen());
        int b2 = (int)((1.0 - diff) * (double)color.getBlue());
        return new Color(r2, g2, b2);
    }

    public static int getColorBrightness(int rgb) {
        int oldR = rgb >>> 16 & 0xFF;
        int oldG = rgb >>> 8 & 0xFF;
        int oldB = rgb >>> 0 & 0xFF;
        return (222 * oldR + 707 * oldG + 71 * oldB) / 1000;
    }

    public static Color getFocusColor(Component comp, TransitionAwareUI transitionAwareUI) {
        StateTransitionTracker stateTransitionTracker = transitionAwareUI.getTransitionTracker();
        StateTransitionTracker.ModelStateInfo modelStateInfo = stateTransitionTracker.getModelStateInfo();
        ComponentState currState = modelStateInfo.getCurrModelState();
        Map<ComponentState, StateTransitionTracker.StateContributionInfo> activeStates = modelStateInfo.getStateContributionMap();
        SubstanceColorScheme colorScheme = SubstanceColorSchemeUtilities.getColorScheme(comp, ColorSchemeAssociationKind.MARK, currState);
        if (currState.isDisabled() || activeStates == null || activeStates.size() == 1) {
            return colorScheme.getFocusRingColor();
        }
        float aggrRed = 0.0f;
        float aggrGreen = 0.0f;
        float aggrBlue = 0.0f;
        for (Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> activeEntry : activeStates.entrySet()) {
            ComponentState activeState = activeEntry.getKey();
            float alpha = activeEntry.getValue().getContribution();
            SubstanceColorScheme activeColorScheme = SubstanceColorSchemeUtilities.getColorScheme(comp, ColorSchemeAssociationKind.MARK, activeState);
            Color activeForeground = activeColorScheme.getFocusRingColor();
            aggrRed += alpha * (float)activeForeground.getRed();
            aggrGreen += alpha * (float)activeForeground.getGreen();
            aggrBlue += alpha * (float)activeForeground.getBlue();
        }
        return new Color((int)aggrRed, (int)aggrGreen, (int)aggrBlue);
    }

    public static float getColorStrength(Color color) {
        return (float)Math.max(SubstanceColorUtilities.getColorBrightness(color.getRGB()), SubstanceColorUtilities.getColorBrightness(SubstanceColorUtilities.getNegativeColor(color.getRGB()))) / 255.0f;
    }

    public static Color getMarkColor(SubstanceColorScheme colorScheme, boolean isEnabled) {
        if (colorScheme.isDark()) {
            if (!isEnabled) {
                return colorScheme.getDarkColor();
            }
            return SubstanceColorUtilities.getInterpolatedColor(colorScheme.getForegroundColor(), colorScheme.getUltraLightColor(), 0.9);
        }
        Color color1 = isEnabled ? colorScheme.getUltraDarkColor() : colorScheme.getUltraDarkColor();
        Color color2 = isEnabled ? colorScheme.getDarkColor() : colorScheme.getLightColor();
        return SubstanceColorUtilities.getInterpolatedColor(color1, color2, 0.9);
    }

    public static Color getForegroundColor(Component component, StateTransitionTracker.ModelStateInfo modelStateInfo) {
        AbstractButton button;
        ComponentState currState = modelStateInfo.getCurrModelState();
        Map<ComponentState, StateTransitionTracker.StateContributionInfo> activeStates = modelStateInfo.getStateContributionMap();
        if (component instanceof AbstractButton && (SubstanceCoreUtilities.isButtonNeverPainted(button = (AbstractButton)component) || !button.isContentAreaFilled() || button instanceof JRadioButton || button instanceof JCheckBox) && !currState.isDisabled()) {
            currState = ComponentState.ENABLED;
            activeStates = null;
        }
        SubstanceColorScheme colorScheme = SubstanceColorSchemeUtilities.getColorScheme(component, currState);
        if (currState.isDisabled() || activeStates == null || activeStates.size() == 1) {
            return colorScheme.getForegroundColor();
        }
        float aggrRed = 0.0f;
        float aggrGreen = 0.0f;
        float aggrBlue = 0.0f;
        for (Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> activeEntry : activeStates.entrySet()) {
            ComponentState activeState = activeEntry.getKey();
            float alpha = activeEntry.getValue().getContribution();
            SubstanceColorScheme activeColorScheme = SubstanceColorSchemeUtilities.getColorScheme(component, activeState);
            Color activeForeground = activeColorScheme.getForegroundColor();
            aggrRed += alpha * (float)activeForeground.getRed();
            aggrGreen += alpha * (float)activeForeground.getGreen();
            aggrBlue += alpha * (float)activeForeground.getBlue();
        }
        return new Color((int)aggrRed, (int)aggrGreen, (int)aggrBlue);
    }

    public static Color getMenuComponentForegroundColor(Component menuComponent, StateTransitionTracker.ModelStateInfo modelStateInfo) {
        ComponentState currState = modelStateInfo.getCurrModelStateNoSelection();
        Map<ComponentState, StateTransitionTracker.StateContributionInfo> activeStates = modelStateInfo.getStateNoSelectionContributionMap();
        ColorSchemeAssociationKind currAssocKind = ColorSchemeAssociationKind.FILL;
        if (!currState.isDisabled() && currState != ComponentState.ENABLED) {
            currAssocKind = ColorSchemeAssociationKind.HIGHLIGHT;
        }
        SubstanceColorScheme colorScheme = SubstanceColorSchemeUtilities.getColorScheme(menuComponent, currAssocKind, currState);
        if (currState.isDisabled() || activeStates == null || activeStates.size() == 1) {
            return colorScheme.getForegroundColor();
        }
        float aggrRed = 0.0f;
        float aggrGreen = 0.0f;
        float aggrBlue = 0.0f;
        for (Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> activeEntry : activeStates.entrySet()) {
            ComponentState activeState = activeEntry.getKey();
            float alpha = activeEntry.getValue().getContribution();
            ColorSchemeAssociationKind assocKind = ColorSchemeAssociationKind.FILL;
            if (!activeState.isDisabled() && activeState != ComponentState.ENABLED) {
                assocKind = ColorSchemeAssociationKind.HIGHLIGHT;
            }
            SubstanceColorScheme activeColorScheme = SubstanceColorSchemeUtilities.getColorScheme(menuComponent, assocKind, activeState);
            Color activeForeground = activeColorScheme.getForegroundColor();
            aggrRed += alpha * (float)activeForeground.getRed();
            aggrGreen += alpha * (float)activeForeground.getGreen();
            aggrBlue += alpha * (float)activeForeground.getBlue();
        }
        return new Color((int)aggrRed, (int)aggrGreen, (int)aggrBlue);
    }

    public static Color getBackgroundFillColor(Component component) {
        if (component instanceof JCheckBox || component instanceof JRadioButton || component instanceof JSlider) {
            component = component.getParent() != null ? component.getParent() : component;
        } else if (component instanceof JTextComponent && !component.isOpaque()) {
            component = component.getParent() != null ? component.getParent() : component;
        }
        Color backgr = component.getBackground();
        if (SwingUtilities.getAncestorOfClass(CellRendererPane.class, component) != null) {
            return backgr;
        }
        boolean isBackgroundUiResource = backgr instanceof UIResource;
        if (!isBackgroundUiResource) {
            if (SubstanceCoreUtilities.getColorizationFactor(component) == 1.0 && component.isEnabled()) {
                return backgr;
            }
            SubstanceColorScheme scheme = SubstanceColorSchemeUtilities.getColorScheme(component, component.isEnabled() ? ComponentState.ENABLED : ComponentState.DISABLED_UNSELECTED);
            backgr = scheme.getBackgroundFillColor();
        } else {
            float alpha;
            ComponentState state = component.isEnabled() ? ComponentState.ENABLED : ComponentState.DISABLED_UNSELECTED;
            JTextComponent matchingTextComp = SubstanceCoreUtilities.getTextComponentForTransitions(component);
            if (matchingTextComp != null) {
                component = matchingTextComp;
                boolean isEditable = matchingTextComp.isEditable();
                if (isEditable) {
                    state = component.isEnabled() ? EDITABLE : EDITABLE_DISABLED;
                } else {
                    ComponentState componentState = state = component.isEnabled() ? UNEDITABLE : UNEDITABLE_DISABLED;
                }
            }
            if (component instanceof JMenuItem) {
                state = ComponentState.ENABLED;
            }
            backgr = SubstanceColorUtilities.getDefaultBackgroundColor(component, state);
            if (state.isDisabled() && (alpha = SubstanceColorSchemeUtilities.getAlpha(component, state)) < 1.0f) {
                ColorUIResource defaultColor = SubstanceColorUtilities.getDefaultBackgroundColor(component, ComponentState.ENABLED);
                backgr = SubstanceColorUtilities.getInterpolatedColor(backgr, defaultColor, 1.0f - (1.0f - alpha) / 2.0f);
            }
        }
        return backgr;
    }

    public static Color getOuterTextComponentBorderColor(Color fillBackgroundColor) {
        float[] hsb = Color.RGBtoHSB(fillBackgroundColor.getRed(), fillBackgroundColor.getGreen(), fillBackgroundColor.getBlue(), null);
        hsb[2] = hsb[2] < 0.3f ? 1.0f - (float)Math.pow(1.0f - hsb[2], 1.4) : (hsb[2] < 0.5f ? 1.0f - (float)Math.pow(1.0f - hsb[2], 1.2) : (hsb[2] < 0.75f ? 1.0f - (float)Math.pow(1.0f - hsb[2], 1.7) : 1.0f - (float)Math.pow(1.0f - hsb[2], 2.0)));
        return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
    }

    public static ColorUIResource getDefaultBackgroundColor(boolean toTreatAsTextComponent, SubstanceSkin skin, boolean isDisabled) {
        if (toTreatAsTextComponent || isDisabled) {
            return new ColorUIResource(skin.getEnabledColorScheme(DecorationAreaType.NONE).getTextBackgroundFillColor());
        }
        return new ColorUIResource(skin.getEnabledColorScheme(DecorationAreaType.NONE).getBackgroundFillColor());
    }

    public static ColorUIResource getDefaultBackgroundColor(Component comp, ComponentState compState) {
        if (comp instanceof JTextComponent) {
            return new ColorUIResource(SubstanceColorSchemeUtilities.getColorScheme(comp, compState).getTextBackgroundFillColor());
        }
        return new ColorUIResource(SubstanceLookAndFeel.getCurrentSkin(comp).getBackgroundColorScheme(SubstanceLookAndFeel.getDecorationType(comp)).getBackgroundFillColor());
    }

    public static Color getStripedBackground(JComponent component, int rowIndex) {
        Color backgr = SubstanceColorUtilities.getBackgroundFillColor(component);
        if (backgr == null) {
            return null;
        }
        if (rowIndex % 2 == 0) {
            return backgr;
        }
        int r2 = backgr.getRed();
        int g2 = backgr.getGreen();
        int b2 = backgr.getBlue();
        double coef = 0.96;
        if (!component.isEnabled()) {
            coef = 1.0 - (1.0 - coef) / 2.0;
        }
        ColorUIResource darkerColor = new ColorUIResource((int)(coef * (double)r2), (int)(coef * (double)g2), (int)(coef * (double)b2));
        return darkerColor;
    }

    public static String encode(int number) {
        if (number < 0 || number > 255) {
            throw new IllegalArgumentException("" + number);
        }
        String hex = "0123456789ABCDEF";
        char c1 = hex.charAt(number / 16);
        char c2 = hex.charAt(number % 16);
        return c1 + "" + c2;
    }

    public static String encode(Color color) {
        return "#" + SubstanceColorUtilities.encode(color.getRed()) + SubstanceColorUtilities.encode(color.getGreen()) + SubstanceColorUtilities.encode(color.getBlue());
    }
}

