/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api;

import java.awt.Component;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.pushingpixels.substance.api.ColorSchemeAssociationKind;
import org.pushingpixels.substance.api.ColorSchemeTransform;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.ComponentStateFacet;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.internal.colorscheme.BlendBiColorScheme;

public class SubstanceColorSchemeBundle {
    protected SubstanceColorScheme activeColorScheme;
    protected SubstanceColorScheme enabledColorScheme;
    protected SubstanceColorScheme disabledColorScheme;
    protected Map<ComponentState, Float> stateAlphaMap;
    protected Map<ComponentState, Float> stateHighlightSchemeAlphaMap;
    protected SubstanceColorScheme pressedScheme;
    protected SubstanceColorScheme disabledSelectedScheme;
    protected SubstanceColorScheme selectedScheme;
    protected SubstanceColorScheme rolloverSelectedScheme;
    protected Map<ColorSchemeAssociationKind, Map<ComponentState, SubstanceColorScheme>> colorSchemeMap;
    protected Map<ColorSchemeAssociationKind, Map<ComponentState, ComponentState>> bestFillMap;

    public SubstanceColorSchemeBundle(SubstanceColorScheme activeColorScheme, SubstanceColorScheme enabledColorScheme, SubstanceColorScheme disabledColorScheme) {
        this.activeColorScheme = activeColorScheme;
        this.enabledColorScheme = enabledColorScheme;
        this.disabledColorScheme = disabledColorScheme;
        this.stateAlphaMap = new HashMap<ComponentState, Float>();
        this.stateHighlightSchemeAlphaMap = new HashMap<ComponentState, Float>();
        this.colorSchemeMap = new HashMap<ColorSchemeAssociationKind, Map<ComponentState, SubstanceColorScheme>>();
        for (ColorSchemeAssociationKind associationKind : ColorSchemeAssociationKind.values()) {
            this.colorSchemeMap.put(associationKind, new HashMap());
        }
        this.bestFillMap = new HashMap<ColorSchemeAssociationKind, Map<ComponentState, ComponentState>>();
        for (ColorSchemeAssociationKind associationKind : ColorSchemeAssociationKind.values()) {
            this.bestFillMap.put(associationKind, new HashMap());
        }
    }

    public void registerColorScheme(SubstanceColorScheme stateColorScheme, float alpha, ComponentState ... states) {
        if (states != null) {
            for (ComponentState state : states) {
                this.colorSchemeMap.get(ColorSchemeAssociationKind.FILL).put(state, stateColorScheme);
                this.stateAlphaMap.put(state, Float.valueOf(alpha));
            }
        }
    }

    public void registerColorScheme(SubstanceColorScheme stateColorScheme, ComponentState ... states) {
        this.registerColorScheme(stateColorScheme, 1.0f, states);
    }

    public void registerHighlightColorScheme(SubstanceColorScheme stateHighlightScheme, ComponentState ... states) {
        if (states == null || states.length == 0) {
            for (ComponentState state : ComponentState.getAllStates()) {
                if (this.colorSchemeMap.get(ColorSchemeAssociationKind.HIGHLIGHT).containsKey(state) || state.isDisabled() || state == ComponentState.ENABLED) continue;
                this.colorSchemeMap.get(ColorSchemeAssociationKind.HIGHLIGHT).put(state, stateHighlightScheme);
            }
        } else {
            for (ComponentState state : states) {
                this.colorSchemeMap.get(ColorSchemeAssociationKind.HIGHLIGHT).put(state, stateHighlightScheme);
            }
        }
    }

    public void registerHighlightColorScheme(SubstanceColorScheme highlightScheme, float alpha, ComponentState ... states) {
        if (highlightScheme == null) {
            throw new IllegalArgumentException("Cannot pass null color scheme");
        }
        if (states == null || states.length == 0) {
            for (ComponentState state : ComponentState.getAllStates()) {
                if (state.isDisabled() || state == ComponentState.ENABLED) continue;
                if (!this.colorSchemeMap.get(ColorSchemeAssociationKind.HIGHLIGHT).containsKey(state)) {
                    this.colorSchemeMap.get(ColorSchemeAssociationKind.HIGHLIGHT).put(state, highlightScheme);
                }
                if (this.stateHighlightSchemeAlphaMap.containsKey(state)) continue;
                this.stateHighlightSchemeAlphaMap.put(state, Float.valueOf(alpha));
            }
        } else {
            for (ComponentState state : states) {
                this.colorSchemeMap.get(ColorSchemeAssociationKind.HIGHLIGHT).put(state, highlightScheme);
                this.stateHighlightSchemeAlphaMap.put(state, Float.valueOf(alpha));
            }
        }
    }

    public SubstanceColorScheme getColorScheme(ComponentState componentState) {
        ComponentState bestFit;
        SubstanceColorScheme registered = this.colorSchemeMap.get(ColorSchemeAssociationKind.FILL).get(componentState);
        if (registered != null) {
            return registered;
        }
        Map<ComponentState, ComponentState> bestFitForFill = this.bestFillMap.get(ColorSchemeAssociationKind.FILL);
        if (!bestFitForFill.containsKey(componentState)) {
            Set<ComponentState> registeredStates = this.colorSchemeMap.get(ColorSchemeAssociationKind.FILL).keySet();
            bestFitForFill.put(componentState, componentState.bestFit(registeredStates));
        }
        if ((bestFit = bestFitForFill.get(componentState)) != null && (registered = this.colorSchemeMap.get(ColorSchemeAssociationKind.FILL).get(bestFit)) != null) {
            return registered;
        }
        if (componentState.isFacetActive(ComponentStateFacet.PRESS)) {
            if (this.pressedScheme == null) {
                this.pressedScheme = this.activeColorScheme.shade(0.2).saturate(0.1);
            }
            return this.pressedScheme;
        }
        if (componentState == ComponentState.DISABLED_SELECTED) {
            if (this.disabledSelectedScheme == null) {
                this.disabledSelectedScheme = new BlendBiColorScheme(this.activeColorScheme, this.disabledColorScheme, 0.25);
            }
            return this.disabledSelectedScheme;
        }
        if (componentState == ComponentState.SELECTED) {
            if (this.selectedScheme == null) {
                this.selectedScheme = this.activeColorScheme.saturate(0.2);
            }
            return this.selectedScheme;
        }
        if (componentState == ComponentState.ROLLOVER_SELECTED) {
            if (this.rolloverSelectedScheme == null) {
                this.rolloverSelectedScheme = this.activeColorScheme.tint(0.1).saturate(0.1);
            }
            return this.rolloverSelectedScheme;
        }
        ComponentState hardFallback = componentState.getHardFallback();
        if (hardFallback != null) {
            return this.getColorScheme(hardFallback);
        }
        if (componentState == ComponentState.ENABLED) {
            return this.enabledColorScheme;
        }
        if (componentState.isDisabled()) {
            return this.disabledColorScheme;
        }
        return this.activeColorScheme;
    }

    public float getHighlightAlpha(Component comp, ComponentState componentState) {
        Float registered = this.stateHighlightSchemeAlphaMap.get(componentState);
        if (registered != null) {
            return registered.floatValue();
        }
        return -1.0f;
    }

    public float getAlpha(Component comp, ComponentState componentState) {
        Float registered = this.stateAlphaMap.get(componentState);
        if (registered != null) {
            return registered.floatValue();
        }
        return -1.0f;
    }

    public SubstanceColorScheme getActiveColorScheme() {
        return this.activeColorScheme;
    }

    public SubstanceColorScheme getEnabledColorScheme() {
        return this.enabledColorScheme;
    }

    public SubstanceColorScheme getDisabledColorScheme() {
        return this.disabledColorScheme;
    }

    public void registerColorScheme(SubstanceColorScheme scheme, ColorSchemeAssociationKind associationKind, ComponentState ... states) {
        if (scheme == null) {
            throw new IllegalArgumentException("Cannot pass null color scheme");
        }
        if (states == null || states.length == 0) {
            for (ComponentState state : ComponentState.getAllStates()) {
                if (this.colorSchemeMap.get(associationKind).containsKey(state)) continue;
                this.colorSchemeMap.get(associationKind).put(state, scheme);
            }
        } else {
            for (ComponentState state : states) {
                this.colorSchemeMap.get(associationKind).put(state, scheme);
            }
        }
    }

    public SubstanceColorScheme getColorScheme(ColorSchemeAssociationKind associationKind, ComponentState componentState) {
        ComponentState bestFit;
        if (associationKind == ColorSchemeAssociationKind.FILL) {
            return this.getColorScheme(componentState);
        }
        SubstanceColorScheme registered = this.colorSchemeMap.get(associationKind).get(componentState);
        if (registered != null) {
            return registered;
        }
        Map<ComponentState, ComponentState> bestFitForState = this.bestFillMap.get(associationKind);
        if (!bestFitForState.containsKey(componentState)) {
            Set<ComponentState> registeredStates = this.colorSchemeMap.get(associationKind).keySet();
            bestFitForState.put(componentState, componentState.bestFit(registeredStates));
        }
        if ((bestFit = bestFitForState.get(componentState)) != null && (registered = this.colorSchemeMap.get(associationKind).get(bestFit)) != null) {
            return registered;
        }
        ColorSchemeAssociationKind fallback = associationKind.getFallback();
        if (fallback == null) {
            return null;
        }
        return this.getColorScheme(fallback, componentState);
    }

    SubstanceColorSchemeBundle transform(ColorSchemeTransform transform) {
        SubstanceColorSchemeBundle result = new SubstanceColorSchemeBundle(transform.transform(this.activeColorScheme), transform.transform(this.enabledColorScheme), transform.transform(this.disabledColorScheme));
        for (Map.Entry<ColorSchemeAssociationKind, Map<ComponentState, SubstanceColorScheme>> entry : this.colorSchemeMap.entrySet()) {
            for (Map.Entry<ComponentState, SubstanceColorScheme> subEntry : entry.getValue().entrySet()) {
                result.colorSchemeMap.get(entry.getKey()).put(subEntry.getKey(), transform.transform(subEntry.getValue()));
            }
        }
        if (this.stateAlphaMap != null) {
            result.stateAlphaMap = new HashMap<ComponentState, Float>(this.stateAlphaMap);
        }
        if (this.stateHighlightSchemeAlphaMap != null) {
            result.stateHighlightSchemeAlphaMap = new HashMap<ComponentState, Float>(this.stateHighlightSchemeAlphaMap);
        }
        return result;
    }

    Set<ComponentState> getStatesWithAlpha() {
        HashSet<ComponentState> result = new HashSet<ComponentState>();
        for (Map.Entry<ComponentState, Float> alphaEntry : this.stateAlphaMap.entrySet()) {
            if (!(alphaEntry.getValue().floatValue() < 1.0f)) continue;
            result.add(alphaEntry.getKey());
        }
        return result;
    }
}

