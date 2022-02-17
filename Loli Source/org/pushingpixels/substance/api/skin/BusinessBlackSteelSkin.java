/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.skin;

import java.awt.Color;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceColorSchemeBundle;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.colorscheme.EbonyColorScheme;
import org.pushingpixels.substance.api.colorscheme.LightGrayColorScheme;
import org.pushingpixels.substance.api.colorscheme.MetallicColorScheme;
import org.pushingpixels.substance.api.colorscheme.SteelBlueColorScheme;
import org.pushingpixels.substance.api.painter.border.ClassicBorderPainter;
import org.pushingpixels.substance.api.painter.decoration.ArcDecorationPainter;
import org.pushingpixels.substance.api.painter.decoration.BrushedMetalDecorationPainter;
import org.pushingpixels.substance.api.painter.fill.ClassicFillPainter;
import org.pushingpixels.substance.api.painter.highlight.ClassicHighlightPainter;
import org.pushingpixels.substance.api.painter.overlay.TopShadowOverlayPainter;
import org.pushingpixels.substance.api.shaper.ClassicButtonShaper;

public class BusinessBlackSteelSkin
extends SubstanceSkin {
    public static final String NAME = "Business Black Steel";

    public BusinessBlackSteelSkin() {
        SubstanceColorScheme activeScheme = new SteelBlueColorScheme().tint(0.15).named("Business Black Steel Active");
        SubstanceColorScheme enabledScheme = new MetallicColorScheme().tint(0.05).named("Business Black Steel Enabled");
        SubstanceColorScheme disabledScheme = new LightGrayColorScheme().tint(0.05).named("Business Black Steel Disabled");
        SubstanceColorSchemeBundle defaultSchemeBundle = new SubstanceColorSchemeBundle(activeScheme, enabledScheme, disabledScheme);
        defaultSchemeBundle.registerHighlightColorScheme(activeScheme, 0.6f, ComponentState.ROLLOVER_UNSELECTED);
        defaultSchemeBundle.registerHighlightColorScheme(activeScheme, 0.8f, ComponentState.SELECTED);
        defaultSchemeBundle.registerHighlightColorScheme(activeScheme, 0.95f, ComponentState.ROLLOVER_SELECTED);
        defaultSchemeBundle.registerHighlightColorScheme(activeScheme, 0.8f, ComponentState.ARMED, ComponentState.ROLLOVER_ARMED);
        this.registerDecorationAreaSchemeBundle(defaultSchemeBundle, DecorationAreaType.NONE);
        SubstanceColorScheme activeHeaderScheme = new EbonyColorScheme().shiftBackground(Color.black, 0.3).tint(0.05).named("Business Black Steel Active Header");
        SubstanceColorScheme enabledHeaderScheme = new EbonyColorScheme().tint(0.05).named("Business Black Steel Enabled Header");
        SubstanceColorSchemeBundle headerSchemeBundle = new SubstanceColorSchemeBundle(activeHeaderScheme, enabledHeaderScheme, disabledScheme);
        headerSchemeBundle.registerColorScheme(enabledHeaderScheme, 0.5f, ComponentState.DISABLED_UNSELECTED, ComponentState.DISABLED_SELECTED);
        headerSchemeBundle.registerHighlightColorScheme(activeScheme, 0.6f, ComponentState.ROLLOVER_UNSELECTED);
        headerSchemeBundle.registerHighlightColorScheme(activeScheme, 0.8f, ComponentState.SELECTED);
        headerSchemeBundle.registerHighlightColorScheme(activeScheme, 0.95f, ComponentState.ROLLOVER_SELECTED);
        headerSchemeBundle.registerHighlightColorScheme(activeScheme, 0.8f, ComponentState.ARMED, ComponentState.ROLLOVER_ARMED);
        this.registerDecorationAreaSchemeBundle(headerSchemeBundle, activeHeaderScheme, DecorationAreaType.PRIMARY_TITLE_PANE, DecorationAreaType.SECONDARY_TITLE_PANE, DecorationAreaType.HEADER);
        SubstanceColorScheme activeGeneralScheme = activeScheme.shade(0.1).saturate(-0.5).named("Business Black Steel Active General");
        SubstanceColorScheme enabledGeneralScheme = activeScheme.tint(0.3).saturate(-0.7).named("Business Black Steel Enabled General");
        SubstanceColorSchemeBundle generalSchemeBundle = new SubstanceColorSchemeBundle(activeGeneralScheme, enabledGeneralScheme, disabledScheme);
        generalSchemeBundle.registerColorScheme(disabledScheme, 0.5f, ComponentState.DISABLED_UNSELECTED);
        this.registerDecorationAreaSchemeBundle(generalSchemeBundle, DecorationAreaType.FOOTER, DecorationAreaType.GENERAL);
        this.addOverlayPainter(TopShadowOverlayPainter.getInstance(), DecorationAreaType.TOOLBAR);
        this.buttonShaper = new ClassicButtonShaper();
        this.fillPainter = new ClassicFillPainter();
        this.borderPainter = new ClassicBorderPainter();
        BrushedMetalDecorationPainter decorationPainter = new BrushedMetalDecorationPainter();
        decorationPainter.setBaseDecorationPainter(new ArcDecorationPainter());
        decorationPainter.setTextureAlpha(0.02f);
        this.decorationPainter = decorationPainter;
        this.highlightPainter = new ClassicHighlightPainter();
        this.borderPainter = new ClassicBorderPainter();
    }

    @Override
    public String getDisplayName() {
        return NAME;
    }
}

