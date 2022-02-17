/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.skin;

import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceColorSchemeBundle;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.colorscheme.CharcoalColorScheme;
import org.pushingpixels.substance.api.painter.border.GlassBorderPainter;
import org.pushingpixels.substance.api.painter.decoration.ArcDecorationPainter;
import org.pushingpixels.substance.api.painter.fill.GlassFillPainter;
import org.pushingpixels.substance.api.painter.highlight.ClassicHighlightPainter;
import org.pushingpixels.substance.api.shaper.ClassicButtonShaper;

public class EmeraldDuskSkin
extends SubstanceSkin {
    public static final String NAME = "Emerald Dusk";

    public EmeraldDuskSkin() {
        SubstanceColorScheme activeScheme = new CharcoalColorScheme().hueShift(0.3).named("Emerald Dusk Active");
        SubstanceColorScheme enabledScheme = new CharcoalColorScheme().hueShift(0.4).named("Emerald Dusk Enabled");
        SubstanceColorSchemeBundle defaultSchemeBundle = new SubstanceColorSchemeBundle(activeScheme, enabledScheme, enabledScheme);
        defaultSchemeBundle.registerColorScheme(activeScheme, 0.5f, ComponentState.DISABLED_SELECTED);
        defaultSchemeBundle.registerColorScheme(enabledScheme, 0.4f, ComponentState.DISABLED_UNSELECTED);
        this.registerDecorationAreaSchemeBundle(defaultSchemeBundle, DecorationAreaType.NONE);
        this.registerAsDecorationArea(enabledScheme, DecorationAreaType.PRIMARY_TITLE_PANE, DecorationAreaType.SECONDARY_TITLE_PANE, DecorationAreaType.HEADER, DecorationAreaType.FOOTER, DecorationAreaType.GENERAL, DecorationAreaType.TOOLBAR);
        this.buttonShaper = new ClassicButtonShaper();
        this.fillPainter = new GlassFillPainter();
        this.decorationPainter = new ArcDecorationPainter();
        this.borderPainter = new GlassBorderPainter();
        this.highlightPainter = new ClassicHighlightPainter();
    }

    @Override
    public String getDisplayName() {
        return NAME;
    }
}

