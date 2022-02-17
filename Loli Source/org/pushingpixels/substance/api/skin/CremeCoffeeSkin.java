/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.skin;

import org.pushingpixels.substance.api.ColorSchemeTransform;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceColorSchemeBundle;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.colorscheme.CremeColorScheme;
import org.pushingpixels.substance.api.colorscheme.LightGrayColorScheme;
import org.pushingpixels.substance.api.painter.border.CompositeBorderPainter;
import org.pushingpixels.substance.api.painter.border.DelegateBorderPainter;
import org.pushingpixels.substance.api.painter.border.GlassBorderPainter;
import org.pushingpixels.substance.api.painter.decoration.ArcDecorationPainter;
import org.pushingpixels.substance.api.painter.fill.MatteFillPainter;
import org.pushingpixels.substance.api.painter.highlight.ClassicHighlightPainter;
import org.pushingpixels.substance.api.shaper.ClassicButtonShaper;

public class CremeCoffeeSkin
extends SubstanceSkin {
    public static final String NAME = "Creme Coffee";

    public CremeCoffeeSkin() {
        SubstanceSkin.ColorSchemes kitchenSinkSchemes = SubstanceSkin.getColorSchemes("org/pushingpixels/substance/api/skin/kitchen-sink.colorschemes");
        SubstanceColorScheme activeScheme = kitchenSinkSchemes.get("Coffee Active");
        CremeColorScheme enabledScheme = new CremeColorScheme();
        SubstanceColorScheme disabledScheme = new LightGrayColorScheme().tint(0.35).named("Creme Coffee Disabled");
        SubstanceColorSchemeBundle defaultSchemeBundle = new SubstanceColorSchemeBundle(activeScheme, enabledScheme, disabledScheme);
        this.registerDecorationAreaSchemeBundle(defaultSchemeBundle, DecorationAreaType.NONE);
        this.registerAsDecorationArea(enabledScheme, DecorationAreaType.PRIMARY_TITLE_PANE, DecorationAreaType.SECONDARY_TITLE_PANE, DecorationAreaType.HEADER, DecorationAreaType.FOOTER, DecorationAreaType.GENERAL, DecorationAreaType.TOOLBAR);
        this.registerAsDecorationArea(disabledScheme, DecorationAreaType.PRIMARY_TITLE_PANE_INACTIVE, DecorationAreaType.SECONDARY_TITLE_PANE_INACTIVE);
        this.buttonShaper = new ClassicButtonShaper();
        this.fillPainter = new MatteFillPainter();
        this.decorationPainter = new ArcDecorationPainter();
        this.highlightPainter = new ClassicHighlightPainter();
        this.borderPainter = new CompositeBorderPainter(NAME, new GlassBorderPainter(), new DelegateBorderPainter("Creme Coffee Inner", new GlassBorderPainter(), new ColorSchemeTransform(){

            @Override
            public SubstanceColorScheme transform(SubstanceColorScheme scheme) {
                return scheme.tint(0.8f);
            }
        }));
    }

    @Override
    public String getDisplayName() {
        return NAME;
    }
}

