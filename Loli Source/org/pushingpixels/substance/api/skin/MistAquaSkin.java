/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.skin;

import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceColorSchemeBundle;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.colorscheme.AquaColorScheme;
import org.pushingpixels.substance.api.colorscheme.LightGrayColorScheme;
import org.pushingpixels.substance.api.colorscheme.MetallicColorScheme;
import org.pushingpixels.substance.api.colorscheme.SteelBlueColorScheme;
import org.pushingpixels.substance.api.painter.border.ClassicBorderPainter;
import org.pushingpixels.substance.api.painter.decoration.MatteDecorationPainter;
import org.pushingpixels.substance.api.painter.fill.MatteFillPainter;
import org.pushingpixels.substance.api.painter.highlight.ClassicHighlightPainter;
import org.pushingpixels.substance.api.shaper.StandardButtonShaper;

public class MistAquaSkin
extends SubstanceSkin {
    public static final String NAME = "Mist Aqua";

    public MistAquaSkin() {
        AquaColorScheme activeScheme = new AquaColorScheme();
        SubstanceColorScheme enabledScheme = new MetallicColorScheme().shade(0.05).named("Mist Aqua Enabled");
        SubstanceColorScheme disabledScheme = new LightGrayColorScheme().tone(0.2).named("Mist Aqua Disabled");
        SubstanceColorSchemeBundle defaultSchemeBundle = new SubstanceColorSchemeBundle(activeScheme, enabledScheme, disabledScheme);
        this.registerDecorationAreaSchemeBundle(defaultSchemeBundle, DecorationAreaType.NONE);
        this.registerDecorationAreaSchemeBundle(new SubstanceColorSchemeBundle(activeScheme, enabledScheme, disabledScheme), new SteelBlueColorScheme().saturate(-0.3).tint(0.5).named("Mist Aqua Background"), DecorationAreaType.GENERAL);
        this.registerAsDecorationArea(enabledScheme, DecorationAreaType.PRIMARY_TITLE_PANE, DecorationAreaType.SECONDARY_TITLE_PANE, DecorationAreaType.HEADER, DecorationAreaType.FOOTER, DecorationAreaType.TOOLBAR);
        this.registerAsDecorationArea(disabledScheme, DecorationAreaType.PRIMARY_TITLE_PANE_INACTIVE, DecorationAreaType.SECONDARY_TITLE_PANE_INACTIVE);
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

