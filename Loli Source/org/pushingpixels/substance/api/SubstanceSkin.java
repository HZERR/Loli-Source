/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api;

import java.awt.Component;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.UIDefaults;
import org.pushingpixels.substance.api.ColorSchemeAssociationKind;
import org.pushingpixels.substance.api.ColorSchemeTransform;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.ComponentStateFacet;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceColorSchemeBundle;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.painter.border.SubstanceBorderPainter;
import org.pushingpixels.substance.api.painter.decoration.SubstanceDecorationPainter;
import org.pushingpixels.substance.api.painter.fill.SubstanceFillPainter;
import org.pushingpixels.substance.api.painter.highlight.SubstanceHighlightPainter;
import org.pushingpixels.substance.api.painter.overlay.SubstanceOverlayPainter;
import org.pushingpixels.substance.api.shaper.SubstanceButtonShaper;
import org.pushingpixels.substance.api.trait.SubstanceTrait;
import org.pushingpixels.substance.api.watermark.SubstanceWatermark;
import org.pushingpixels.substance.internal.utils.SkinUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceColorSchemeUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;

public abstract class SubstanceSkin
implements SubstanceTrait {
    protected Map<DecorationAreaType, SubstanceColorSchemeBundle> colorSchemeBundleMap = new HashMap<DecorationAreaType, SubstanceColorSchemeBundle>();
    protected Map<DecorationAreaType, SubstanceColorScheme> backgroundColorSchemeMap = new HashMap<DecorationAreaType, SubstanceColorScheme>();
    protected Map<DecorationAreaType, List<SubstanceOverlayPainter>> overlayPaintersMap = new HashMap<DecorationAreaType, List<SubstanceOverlayPainter>>();
    protected SubstanceWatermark watermark;
    protected SubstanceButtonShaper buttonShaper;
    protected SubstanceFillPainter fillPainter;
    protected SubstanceBorderPainter borderPainter;
    protected SubstanceBorderPainter highlightBorderPainter;
    protected SubstanceHighlightPainter highlightPainter;
    protected SubstanceDecorationPainter decorationPainter;
    protected Set<DecorationAreaType> decoratedAreaSet = new HashSet<DecorationAreaType>();
    protected double selectedTabFadeStart;
    protected double selectedTabFadeEnd;
    protected SubstanceColorScheme watermarkScheme;
    Set<ComponentState> statesWithAlpha;

    protected SubstanceSkin() {
        this.decoratedAreaSet.add(DecorationAreaType.PRIMARY_TITLE_PANE);
        this.decoratedAreaSet.add(DecorationAreaType.SECONDARY_TITLE_PANE);
        this.selectedTabFadeStart = 0.1;
        this.selectedTabFadeEnd = 0.3;
        this.statesWithAlpha = new HashSet<ComponentState>();
    }

    public final SubstanceWatermark getWatermark() {
        return this.watermark;
    }

    public final SubstanceBorderPainter getBorderPainter() {
        return this.borderPainter;
    }

    public final SubstanceBorderPainter getHighlightBorderPainter() {
        return this.highlightBorderPainter;
    }

    public final SubstanceButtonShaper getButtonShaper() {
        return this.buttonShaper;
    }

    public final SubstanceFillPainter getFillPainter() {
        return this.fillPainter;
    }

    public final SubstanceHighlightPainter getHighlightPainter() {
        return this.highlightPainter;
    }

    public final SubstanceDecorationPainter getDecorationPainter() {
        return this.decorationPainter;
    }

    public void addCustomEntriesToTable(UIDefaults table) {
        if (table == null) {
            return;
        }
        SkinUtilities.addCustomEntriesToTable(table, this);
    }

    public final SubstanceColorScheme getColorScheme(Component comp, ComponentState componentState) {
        DecorationAreaType decorationAreaType;
        if (this.colorSchemeBundleMap.size() > 1 && this.colorSchemeBundleMap.containsKey(decorationAreaType = SubstanceLookAndFeel.getDecorationType(comp))) {
            SubstanceColorScheme registered = this.colorSchemeBundleMap.get(decorationAreaType).getColorScheme(componentState);
            if (registered == null) {
                throw new IllegalStateException("Color scheme shouldn't be null here. Please report this issue");
            }
            return registered;
        }
        SubstanceColorScheme registered = this.colorSchemeBundleMap.get(DecorationAreaType.NONE).getColorScheme(componentState);
        if (registered == null) {
            throw new IllegalStateException("Color scheme shouldn't be null here. Please report this issue");
        }
        return registered;
    }

    public final float getHighlightAlpha(Component comp, ComponentState componentState) {
        Float registered;
        DecorationAreaType decorationAreaType;
        if (this.colorSchemeBundleMap.size() > 1 && this.colorSchemeBundleMap.containsKey(decorationAreaType = SubstanceLookAndFeel.getDecorationType(comp)) && (double)(registered = Float.valueOf(this.colorSchemeBundleMap.get(decorationAreaType).getHighlightAlpha(comp, componentState))).floatValue() >= 0.0) {
            return registered.floatValue();
        }
        Float registered2 = Float.valueOf(this.colorSchemeBundleMap.get(DecorationAreaType.NONE).getHighlightAlpha(comp, componentState));
        if ((double)registered2.floatValue() >= 0.0) {
            return registered2.floatValue();
        }
        boolean isRollover = componentState.isFacetActive(ComponentStateFacet.ROLLOVER);
        boolean isSelected = componentState.isFacetActive(ComponentStateFacet.SELECTION);
        boolean isArmed = componentState.isFacetActive(ComponentStateFacet.ARM);
        if (isRollover && isSelected) {
            return 0.9f;
        }
        if (isRollover && isArmed) {
            return 0.8f;
        }
        if (isSelected) {
            return 0.7f;
        }
        if (isArmed) {
            return 0.6f;
        }
        if (isRollover) {
            return 0.4f;
        }
        return 0.0f;
    }

    public final float getAlpha(Component comp, ComponentState componentState) {
        Float registered;
        DecorationAreaType decorationAreaType;
        if (!this.statesWithAlpha.contains(componentState)) {
            return 1.0f;
        }
        if (this.colorSchemeBundleMap.size() > 1 && this.colorSchemeBundleMap.containsKey(decorationAreaType = SubstanceLookAndFeel.getDecorationType(comp)) && (double)(registered = Float.valueOf(this.colorSchemeBundleMap.get(decorationAreaType).getAlpha(comp, componentState))).floatValue() >= 0.0) {
            return registered.floatValue();
        }
        Float registered2 = Float.valueOf(this.colorSchemeBundleMap.get(DecorationAreaType.NONE).getAlpha(comp, componentState));
        if ((double)registered2.floatValue() >= 0.0) {
            return registered2.floatValue();
        }
        return 1.0f;
    }

    public void registerDecorationAreaSchemeBundle(SubstanceColorSchemeBundle bundle, SubstanceColorScheme backgroundColorScheme, DecorationAreaType ... areaTypes) {
        if (bundle == null) {
            return;
        }
        if (backgroundColorScheme == null) {
            throw new IllegalArgumentException("Cannot pass null background color scheme");
        }
        for (DecorationAreaType areaType : areaTypes) {
            this.decoratedAreaSet.add(areaType);
            this.colorSchemeBundleMap.put(areaType, bundle);
            this.backgroundColorSchemeMap.put(areaType, backgroundColorScheme);
        }
        this.statesWithAlpha.addAll(bundle.getStatesWithAlpha());
    }

    public void registerDecorationAreaSchemeBundle(SubstanceColorSchemeBundle bundle, DecorationAreaType ... areaTypes) {
        this.registerDecorationAreaSchemeBundle(bundle, bundle.getEnabledColorScheme(), areaTypes);
    }

    public void registerAsDecorationArea(SubstanceColorScheme backgroundColorScheme, DecorationAreaType ... areaTypes) {
        if (backgroundColorScheme == null) {
            throw new IllegalArgumentException("Cannot pass null background color scheme");
        }
        for (DecorationAreaType areaType : areaTypes) {
            this.decoratedAreaSet.add(areaType);
            this.backgroundColorSchemeMap.put(areaType, backgroundColorScheme);
        }
    }

    public boolean isRegisteredAsDecorationArea(DecorationAreaType decorationType) {
        return this.decoratedAreaSet.contains(decorationType);
    }

    public SubstanceColorScheme getWatermarkColorScheme() {
        if (this.watermarkScheme != null) {
            return this.watermarkScheme;
        }
        return this.colorSchemeBundleMap.get(DecorationAreaType.NONE).getEnabledColorScheme();
    }

    public final SubstanceColorScheme getActiveColorScheme(DecorationAreaType decorationAreaType) {
        if (this.colorSchemeBundleMap.containsKey(decorationAreaType)) {
            return this.colorSchemeBundleMap.get(decorationAreaType).getActiveColorScheme();
        }
        return this.colorSchemeBundleMap.get(DecorationAreaType.NONE).getActiveColorScheme();
    }

    public final SubstanceColorScheme getEnabledColorScheme(DecorationAreaType decorationAreaType) {
        if (this.colorSchemeBundleMap.containsKey(decorationAreaType)) {
            return this.colorSchemeBundleMap.get(decorationAreaType).getEnabledColorScheme();
        }
        return this.colorSchemeBundleMap.get(DecorationAreaType.NONE).getEnabledColorScheme();
    }

    public final SubstanceColorScheme getDisabledColorScheme(DecorationAreaType decorationAreaType) {
        if (this.colorSchemeBundleMap.containsKey(decorationAreaType)) {
            return this.colorSchemeBundleMap.get(decorationAreaType).getDisabledColorScheme();
        }
        return this.colorSchemeBundleMap.get(DecorationAreaType.NONE).getDisabledColorScheme();
    }

    public final double getSelectedTabFadeStart() {
        return this.selectedTabFadeStart;
    }

    public final double getSelectedTabFadeEnd() {
        return this.selectedTabFadeEnd;
    }

    public void setSelectedTabFadeEnd(double selectedTabFadeEnd) {
        if (selectedTabFadeEnd < 0.0 || selectedTabFadeEnd > 1.0) {
            throw new IllegalArgumentException("Value for selected tab fade end should be in 0.0-1.0 range");
        }
        this.selectedTabFadeEnd = selectedTabFadeEnd;
    }

    public void setSelectedTabFadeStart(double selectedTabFadeStart) {
        if (selectedTabFadeStart < 0.0 || selectedTabFadeStart > 1.0) {
            throw new IllegalArgumentException("Value for selected tab fade start should be in 0.0-1.0 range");
        }
        this.selectedTabFadeStart = selectedTabFadeStart;
    }

    public void addOverlayPainter(SubstanceOverlayPainter overlayPainter, DecorationAreaType ... areaTypes) {
        for (DecorationAreaType areaType : areaTypes) {
            if (!this.overlayPaintersMap.containsKey(areaType)) {
                this.overlayPaintersMap.put(areaType, new ArrayList());
            }
            this.overlayPaintersMap.get(areaType).add(overlayPainter);
        }
    }

    public void removeOverlayPainter(SubstanceOverlayPainter overlayPainter, DecorationAreaType ... areaTypes) {
        for (DecorationAreaType areaType : areaTypes) {
            if (!this.overlayPaintersMap.containsKey(areaType)) {
                return;
            }
            this.overlayPaintersMap.get(areaType).remove(overlayPainter);
            if (this.overlayPaintersMap.get(areaType).size() != 0) continue;
            this.overlayPaintersMap.remove(areaType);
        }
    }

    public List<SubstanceOverlayPainter> getOverlayPainters(DecorationAreaType decorationAreaType) {
        if (!this.overlayPaintersMap.containsKey(decorationAreaType)) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(this.overlayPaintersMap.get(decorationAreaType));
    }

    public final SubstanceColorScheme getColorScheme(DecorationAreaType decorationAreaType, ColorSchemeAssociationKind associationKind, ComponentState componentState) {
        if (this.colorSchemeBundleMap.size() > 1 && this.colorSchemeBundleMap.containsKey(decorationAreaType)) {
            return this.colorSchemeBundleMap.get(decorationAreaType).getColorScheme(associationKind, componentState);
        }
        return this.colorSchemeBundleMap.get(DecorationAreaType.NONE).getColorScheme(associationKind, componentState);
    }

    public final SubstanceColorScheme getColorScheme(Component comp, ColorSchemeAssociationKind associationKind, ComponentState componentState) {
        DecorationAreaType decorationAreaType;
        if (this.colorSchemeBundleMap.size() > 1 && this.colorSchemeBundleMap.containsKey(decorationAreaType = SubstanceLookAndFeel.getDecorationType(comp))) {
            return this.colorSchemeBundleMap.get(decorationAreaType).getColorScheme(associationKind, componentState);
        }
        return this.colorSchemeBundleMap.get(DecorationAreaType.NONE).getColorScheme(associationKind, componentState);
    }

    public SubstanceSkin transform(ColorSchemeTransform transform, final String name) {
        SubstanceSkin result = new SubstanceSkin(){

            @Override
            public String getDisplayName() {
                return name;
            }
        };
        result.borderPainter = this.borderPainter;
        result.buttonShaper = this.buttonShaper;
        result.decorationPainter = this.decorationPainter;
        result.fillPainter = this.fillPainter;
        result.highlightPainter = this.highlightPainter;
        result.highlightBorderPainter = this.highlightBorderPainter;
        result.watermark = this.watermark;
        if (this.watermarkScheme != null) {
            result.watermarkScheme = transform.transform(this.watermarkScheme);
        }
        result.selectedTabFadeEnd = this.selectedTabFadeEnd;
        result.selectedTabFadeStart = this.selectedTabFadeStart;
        if (this.colorSchemeBundleMap != null) {
            result.colorSchemeBundleMap = new HashMap<DecorationAreaType, SubstanceColorSchemeBundle>();
            for (Map.Entry<DecorationAreaType, SubstanceColorSchemeBundle> entry : this.colorSchemeBundleMap.entrySet()) {
                result.colorSchemeBundleMap.put(entry.getKey(), entry.getValue().transform(transform));
            }
        }
        if (this.decoratedAreaSet != null) {
            result.decoratedAreaSet = new HashSet<DecorationAreaType>(this.decoratedAreaSet);
        }
        if (this.backgroundColorSchemeMap != null) {
            result.backgroundColorSchemeMap = new HashMap<DecorationAreaType, SubstanceColorScheme>();
            for (Map.Entry<DecorationAreaType, Object> entry : this.backgroundColorSchemeMap.entrySet()) {
                result.backgroundColorSchemeMap.put(entry.getKey(), transform.transform((SubstanceColorScheme)entry.getValue()));
            }
        }
        result.overlayPaintersMap = new HashMap<DecorationAreaType, List<SubstanceOverlayPainter>>(this.overlayPaintersMap);
        return result;
    }

    public final SubstanceColorScheme getBackgroundColorScheme(DecorationAreaType decorationAreaType) {
        SubstanceColorScheme registered;
        if (this.backgroundColorSchemeMap.containsKey(decorationAreaType)) {
            return this.backgroundColorSchemeMap.get(decorationAreaType);
        }
        if (this.colorSchemeBundleMap.containsKey(decorationAreaType) && (registered = this.colorSchemeBundleMap.get(decorationAreaType).getEnabledColorScheme()) != null) {
            return registered;
        }
        return this.backgroundColorSchemeMap.get(DecorationAreaType.NONE);
    }

    public boolean isValid() {
        if (!this.colorSchemeBundleMap.containsKey(DecorationAreaType.NONE)) {
            return false;
        }
        if (this.getButtonShaper() == null) {
            return false;
        }
        if (this.getFillPainter() == null) {
            return false;
        }
        if (this.getBorderPainter() == null) {
            return false;
        }
        if (this.getHighlightPainter() == null) {
            return false;
        }
        return this.getDecorationPainter() != null;
    }

    public static ColorSchemes getColorSchemes(URL url) {
        return SubstanceColorSchemeUtilities.getColorSchemes(url);
    }

    public static ColorSchemes getColorSchemes(String resourceName) {
        ClassLoader cl = SubstanceCoreUtilities.getClassLoaderForResources();
        return SubstanceColorSchemeUtilities.getColorSchemes(cl.getResource(resourceName));
    }

    public static class ColorSchemes {
        private List<SubstanceColorScheme> schemes = new ArrayList<SubstanceColorScheme>();

        public ColorSchemes() {
        }

        public ColorSchemes(List<SubstanceColorScheme> schemes) {
            this();
            this.schemes.addAll(schemes);
        }

        public int size() {
            return this.schemes.size();
        }

        public SubstanceColorScheme get(int index) {
            return this.schemes.get(index);
        }

        public SubstanceColorScheme get(String displayName) {
            for (SubstanceColorScheme scheme : this.schemes) {
                if (!scheme.getDisplayName().equals(displayName)) continue;
                return scheme;
            }
            return null;
        }

        private int indexOf(String displayName) {
            for (int i2 = 0; i2 < this.schemes.size(); ++i2) {
                SubstanceColorScheme curr = this.schemes.get(i2);
                if (!curr.getDisplayName().equals(displayName)) continue;
                return i2;
            }
            return -1;
        }

        public void replace(String displayName, SubstanceColorScheme scheme) {
            int index = this.indexOf(displayName);
            if (index >= 0) {
                this.schemes.remove(index);
                this.schemes.add(index, scheme);
            }
        }

        public void delete(String displayName) {
            int index = this.indexOf(displayName);
            if (index >= 0) {
                this.schemes.remove(index);
            }
        }

        public void add(SubstanceColorScheme scheme) {
            this.schemes.add(scheme);
        }

        public void switchWithPrevious(String displayName) {
            int index = this.indexOf(displayName);
            if (index >= 0) {
                SubstanceColorScheme scheme = this.schemes.remove(index);
                this.schemes.add(index - 1, scheme);
            }
        }

        public void switchWithNext(String displayName) {
            int index = this.indexOf(displayName);
            if (index >= 0) {
                SubstanceColorScheme scheme = this.schemes.remove(index);
                this.schemes.add(index + 1, scheme);
            }
        }
    }
}

