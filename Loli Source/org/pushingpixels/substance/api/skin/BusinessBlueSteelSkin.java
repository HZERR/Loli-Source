/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.skin;

import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceColorSchemeBundle;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.colorscheme.LightGrayColorScheme;
import org.pushingpixels.substance.api.colorscheme.MetallicColorScheme;
import org.pushingpixels.substance.api.colorscheme.SteelBlueColorScheme;
import org.pushingpixels.substance.api.painter.border.ClassicBorderPainter;
import org.pushingpixels.substance.api.painter.decoration.ArcDecorationPainter;
import org.pushingpixels.substance.api.painter.decoration.BrushedMetalDecorationPainter;
import org.pushingpixels.substance.api.painter.fill.ClassicFillPainter;
import org.pushingpixels.substance.api.painter.highlight.ClassicHighlightPainter;
import org.pushingpixels.substance.api.shaper.ClassicButtonShaper;

public class BusinessBlueSteelSkin
extends SubstanceSkin {
    public static final String NAME = "Business Blue Steel";

    public BusinessBlueSteelSkin() {
        SubstanceColorScheme activeScheme = new SteelBlueColorScheme().tint(0.15).named("Business Blue Steel Active");
        SubstanceColorScheme enabledScheme = new MetallicColorScheme().tint(0.05).named("Business Blue Steel Enabled");
        SubstanceColorScheme disabledScheme = new LightGrayColorScheme().tint(0.05).named("Business Blue Steel Disabled");
        SubstanceColorSchemeBundle defaultSchemeBundle = new SubstanceColorSchemeBundle(activeScheme, enabledScheme, disabledScheme);
        SubstanceSkin.ColorSchemes kitchenSinkSchemes = SubstanceSkin.getColorSchemes("org/pushingpixels/substance/api/skin/kitchen-sink.colorschemes");
        SubstanceColorScheme highlightColorScheme = kitchenSinkSchemes.get("Business Blue Steel Highlight");
        defaultSchemeBundle.registerHighlightColorScheme(highlightColorScheme, new ComponentState[0]);
        this.registerDecorationAreaSchemeBundle(defaultSchemeBundle, DecorationAreaType.NONE);
        SubstanceColorScheme activeHeaderScheme = activeScheme.saturate(0.2).named("Business Blue Steel Active Header");
        SubstanceColorScheme enabledHeaderScheme = activeScheme.saturate(-0.2).named("Business Blue Steel Enabled Header");
        SubstanceColorSchemeBundle headerSchemeBundle = new SubstanceColorSchemeBundle(activeHeaderScheme, enabledHeaderScheme, enabledHeaderScheme);
        headerSchemeBundle.registerColorScheme(enabledHeaderScheme, 0.5f, ComponentState.DISABLED_UNSELECTED, ComponentState.DISABLED_SELECTED);
        this.registerDecorationAreaSchemeBundle(headerSchemeBundle, DecorationAreaType.PRIMARY_TITLE_PANE, DecorationAreaType.SECONDARY_TITLE_PANE, DecorationAreaType.HEADER, DecorationAreaType.TOOLBAR);
        SubstanceColorScheme activeGeneralScheme = activeScheme.saturate(-0.5).named("Business Blue Steel Active General");
        SubstanceColorScheme enabledGeneralScheme = activeScheme.tint(0.3).saturate(-0.2).named("Business Blue Steel Enabled General");
        SubstanceColorSchemeBundle generalSchemeBundle = new SubstanceColorSchemeBundle(activeGeneralScheme, enabledGeneralScheme, disabledScheme);
        generalSchemeBundle.registerColorScheme(enabledGeneralScheme, 0.7f, ComponentState.DISABLED_UNSELECTED);
        this.registerDecorationAreaSchemeBundle(generalSchemeBundle, DecorationAreaType.FOOTER, DecorationAreaType.GENERAL);
        this.buttonShaper = new ClassicButtonShaper();
        this.fillPainter = new ClassicFillPainter();
        this.borderPainter = new ClassicBorderPainter();
        BrushedMetalDecorationPainter decorationPainter = new BrushedMetalDecorationPainter();
        decorationPainter.setBaseDecorationPainter(new ArcDecorationPainter());
        decorationPainter.setTextureAlpha(0.3f);
        this.decorationPainter = decorationPainter;
        this.highlightPainter = new ClassicHighlightPainter();
        this.borderPainter = new ClassicBorderPainter();
    }

    @Override
    public String getDisplayName() {
        return NAME;
    }
}

