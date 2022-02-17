/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import org.pushingpixels.substance.api.ColorSchemeAssociationKind;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.internal.utils.SubstanceColorSchemeUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;

public class UpdateOptimizationInfo {
    private JComponent component;
    public boolean toDrawWatermark;
    private Map<ComponentState, SubstanceColorScheme> highlightSchemeMap;
    private Map<ComponentState, SubstanceColorScheme> highlightBorderSchemeMap;
    private Map<ComponentState, SubstanceColorScheme> borderSchemeMap;
    private Map<ComponentState, SubstanceColorScheme> fillSchemeMap;
    private Map<ComponentState, Float> highlightAlphaMap;
    private SubstanceColorScheme defaultScheme;
    public DecorationAreaType decorationAreaType;
    public boolean isInDecorationArea;

    public UpdateOptimizationInfo(JComponent component) {
        this.component = component;
        this.toDrawWatermark = SubstanceCoreUtilities.toDrawWatermark(this.component);
        this.defaultScheme = SubstanceColorSchemeUtilities.getColorScheme(this.component, ComponentState.ENABLED);
        this.decorationAreaType = SubstanceLookAndFeel.getDecorationType(this.component);
        SubstanceSkin skin = SubstanceCoreUtilities.getSkin(this.component);
        this.isInDecorationArea = this.decorationAreaType != null && skin.isRegisteredAsDecorationArea(this.decorationAreaType) && SubstanceCoreUtilities.isOpaque(this.component);
    }

    public SubstanceColorScheme getHighlightColorScheme(ComponentState state) {
        SubstanceColorScheme result;
        if (this.highlightSchemeMap == null) {
            this.highlightSchemeMap = new HashMap<ComponentState, SubstanceColorScheme>();
        }
        if ((result = this.highlightSchemeMap.get(state)) == null) {
            result = SubstanceColorSchemeUtilities.getColorScheme(this.component, ColorSchemeAssociationKind.HIGHLIGHT, state);
            this.highlightSchemeMap.put(state, result);
        }
        return result;
    }

    public SubstanceColorScheme getBorderColorScheme(ComponentState state) {
        SubstanceColorScheme result;
        if (this.borderSchemeMap == null) {
            this.borderSchemeMap = new HashMap<ComponentState, SubstanceColorScheme>();
        }
        if ((result = this.borderSchemeMap.get(state)) == null) {
            result = SubstanceColorSchemeUtilities.getColorScheme(this.component, ColorSchemeAssociationKind.BORDER, state);
            this.borderSchemeMap.put(state, result);
        }
        return result;
    }

    public SubstanceColorScheme getFillColorScheme(ComponentState state) {
        SubstanceColorScheme result;
        if (state == ComponentState.ENABLED) {
            return this.defaultScheme;
        }
        if (this.fillSchemeMap == null) {
            this.fillSchemeMap = new HashMap<ComponentState, SubstanceColorScheme>();
        }
        if ((result = this.fillSchemeMap.get(state)) == null) {
            result = SubstanceColorSchemeUtilities.getColorScheme(this.component, state);
            this.fillSchemeMap.put(state, result);
        }
        return result;
    }

    public SubstanceColorScheme getHighlightBorderColorScheme(ComponentState state) {
        SubstanceColorScheme result;
        if (this.highlightBorderSchemeMap == null) {
            this.highlightBorderSchemeMap = new HashMap<ComponentState, SubstanceColorScheme>();
        }
        if ((result = this.highlightBorderSchemeMap.get(state)) == null) {
            result = SubstanceColorSchemeUtilities.getColorScheme(this.component, ColorSchemeAssociationKind.HIGHLIGHT_BORDER, state);
            this.highlightBorderSchemeMap.put(state, result);
        }
        return result;
    }

    public float getHighlightAlpha(ComponentState state) {
        if (this.highlightAlphaMap == null) {
            this.highlightAlphaMap = new HashMap<ComponentState, Float>();
        }
        if (!this.highlightAlphaMap.containsKey(state)) {
            this.highlightAlphaMap.put(state, Float.valueOf(SubstanceColorSchemeUtilities.getHighlightAlpha(this.component, state)));
        }
        return this.highlightAlphaMap.get(state).floatValue();
    }

    public SubstanceColorScheme getDefaultScheme() {
        return this.defaultScheme;
    }
}

