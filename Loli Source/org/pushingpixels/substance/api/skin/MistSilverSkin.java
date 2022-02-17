/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.skin;

import org.pushingpixels.substance.api.ColorSchemeAssociationKind;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceColorSchemeBundle;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.colorscheme.LightGrayColorScheme;
import org.pushingpixels.substance.api.colorscheme.MetallicColorScheme;
import org.pushingpixels.substance.api.colorscheme.SteelBlueColorScheme;
import org.pushingpixels.substance.api.painter.border.ClassicBorderPainter;
import org.pushingpixels.substance.api.painter.decoration.MatteDecorationPainter;
import org.pushingpixels.substance.api.painter.fill.MatteFillPainter;
import org.pushingpixels.substance.api.painter.highlight.ClassicHighlightPainter;
import org.pushingpixels.substance.api.shaper.StandardButtonShaper;

public class MistSilverSkin
extends SubstanceSkin {
    public static final String NAME = "Mist Silver";

    public MistSilverSkin() {
        SubstanceColorScheme activeScheme = new MetallicColorScheme().tint(0.1).named("Mist Silver Active");
        SubstanceColorScheme enabledScheme = new MetallicColorScheme().shade(0.05).named("Mist Silver Enabled");
        SubstanceColorScheme disabledScheme = new LightGrayColorScheme().tone(0.2).named("Mist Silver Disabled");
        SubstanceColorScheme lightBlueScheme = new SteelBlueColorScheme().saturate(-0.3).tint(0.5).named("Mist Silver Light Blue");
        SubstanceColorSchemeBundle defaultSchemeBundle = new SubstanceColorSchemeBundle(activeScheme, enabledScheme, disabledScheme);
        defaultSchemeBundle.registerColorScheme(enabledScheme.tone(0.4), ColorSchemeAssociationKind.TEXT_HIGHLIGHT, ComponentState.SELECTED, ComponentState.ROLLOVER_SELECTED);
        defaultSchemeBundle.registerColorScheme(lightBlueScheme, ComponentState.SELECTED);
        defaultSchemeBundle.registerColorScheme(enabledScheme, ColorSchemeAssociationKind.BORDER, ComponentState.SELECTED);
        this.registerDecorationAreaSchemeBundle(defaultSchemeBundle, DecorationAreaType.NONE);
        this.registerDecorationAreaSchemeBundle(new SubstanceColorSchemeBundle(activeScheme, enabledScheme, disabledScheme), lightBlueScheme, DecorationAreaType.GENERAL);
        this.registerAsDecorationArea(enabledScheme, DecorationAreaType.PRIMARY_TITLE_PANE, DecorationAreaType.SECONDARY_TITLE_PANE, DecorationAreaType.HEADER, DecorationAreaType.FOOTER, DecorationAreaType.TOOLBAR);
        this.registerAsDecorationArea(disabledScheme, DecorationAreaType.PRIMARY_TITLE_PANE_INACTIVE, DecorationAreaType.SECONDARY_TITLE_PANE_INACTIVE);
        this.setSelectedTabFadeStart(0.6);
        this.setSelectedTabFadeEnd(1.0);
        this.buttonShaper = new StandardButtonShaper();
        this.fillPainter = new MatteFillPainter();
        this.borderPainter = new ClassicBorderPainter();
        this.decorationPainter = new MatteDecorationPainter();
        this.highlightPainter = new ClassicHighlightPainter();
    }

    @Override
    public String getDisplayName() {
        return NAME;
    }
}

