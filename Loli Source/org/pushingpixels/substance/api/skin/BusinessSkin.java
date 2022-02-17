/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.skin;

import org.pushingpixels.substance.api.ColorSchemeTransform;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceColorSchemeBundle;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.colorscheme.MetallicColorScheme;
import org.pushingpixels.substance.api.painter.border.ClassicBorderPainter;
import org.pushingpixels.substance.api.painter.border.CompositeBorderPainter;
import org.pushingpixels.substance.api.painter.border.DelegateBorderPainter;
import org.pushingpixels.substance.api.painter.decoration.ArcDecorationPainter;
import org.pushingpixels.substance.api.painter.decoration.BrushedMetalDecorationPainter;
import org.pushingpixels.substance.api.painter.fill.ClassicFillPainter;
import org.pushingpixels.substance.api.painter.highlight.ClassicHighlightPainter;
import org.pushingpixels.substance.api.shaper.ClassicButtonShaper;

public class BusinessSkin
extends SubstanceSkin {
    public static final String NAME = "Business";

    public BusinessSkin() {
        SubstanceColorScheme enabledScheme;
        SubstanceColorScheme activeScheme = new MetallicColorScheme().tint(0.15).named("Business Active");
        SubstanceColorScheme disabledScheme = enabledScheme = new MetallicColorScheme().shade(0.1).named("Business Enabled");
        SubstanceColorSchemeBundle defaultSchemeBundle = new SubstanceColorSchemeBundle(activeScheme, enabledScheme, disabledScheme);
        SubstanceSkin.ColorSchemes kitchenSinkSchemes = SubstanceSkin.getColorSchemes("org/pushingpixels/substance/api/skin/kitchen-sink.colorschemes");
        SubstanceColorScheme highlightColorScheme = kitchenSinkSchemes.get("Business Highlight");
        defaultSchemeBundle.registerHighlightColorScheme(highlightColorScheme, new ComponentState[0]);
        defaultSchemeBundle.registerColorScheme(disabledScheme, 0.4f, ComponentState.DISABLED_UNSELECTED);
        defaultSchemeBundle.registerColorScheme(activeScheme, 0.4f, ComponentState.DISABLED_SELECTED);
        defaultSchemeBundle.registerColorScheme(activeScheme, ComponentState.SELECTED);
        this.registerDecorationAreaSchemeBundle(defaultSchemeBundle, DecorationAreaType.NONE);
        this.registerAsDecorationArea(enabledScheme, DecorationAreaType.PRIMARY_TITLE_PANE, DecorationAreaType.SECONDARY_TITLE_PANE, DecorationAreaType.HEADER, DecorationAreaType.FOOTER);
        this.registerAsDecorationArea(disabledScheme, DecorationAreaType.PRIMARY_TITLE_PANE_INACTIVE, DecorationAreaType.SECONDARY_TITLE_PANE_INACTIVE);
        SubstanceSkin.ColorSchemes kitchenSkinSchemes = SubstanceSkin.getColorSchemes("org/pushingpixels/substance/api/skin/kitchen-sink.colorschemes");
        this.registerAsDecorationArea(kitchenSkinSchemes.get("LightGray General Watermark"), DecorationAreaType.GENERAL);
        this.setSelectedTabFadeStart(0.6);
        this.setSelectedTabFadeEnd(1.0);
        this.buttonShaper = new ClassicButtonShaper();
        this.fillPainter = new ClassicFillPainter();
        this.borderPainter = new CompositeBorderPainter(NAME, new ClassicBorderPainter(), new DelegateBorderPainter("Business Inner", new ClassicBorderPainter(), new ColorSchemeTransform(){

            @Override
            public SubstanceColorScheme transform(SubstanceColorScheme scheme) {
                return scheme.tint(0.9f);
            }
        }));
        BrushedMetalDecorationPainter decorationPainter = new BrushedMetalDecorationPainter();
        decorationPainter.setBaseDecorationPainter(new ArcDecorationPainter());
        decorationPainter.setTextureAlpha(0.2f);
        this.decorationPainter = decorationPainter;
        this.highlightPainter = new ClassicHighlightPainter();
    }

    @Override
    public String getDisplayName() {
        return NAME;
    }
}

