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
import org.pushingpixels.substance.api.colorscheme.DarkMetallicColorScheme;
import org.pushingpixels.substance.api.colorscheme.EbonyColorScheme;
import org.pushingpixels.substance.api.painter.border.GlassBorderPainter;
import org.pushingpixels.substance.api.painter.decoration.ArcDecorationPainter;
import org.pushingpixels.substance.api.painter.fill.GlassFillPainter;
import org.pushingpixels.substance.api.painter.highlight.ClassicHighlightPainter;
import org.pushingpixels.substance.api.shaper.ClassicButtonShaper;
import org.pushingpixels.substance.api.watermark.SubstanceCrosshatchWatermark;

public class RavenSkin
extends SubstanceSkin {
    public static final String NAME = "Raven";

    public RavenSkin() {
        SubstanceSkin.ColorSchemes schemes = SubstanceSkin.getColorSchemes("org/pushingpixels/substance/api/skin/graphite.colorschemes");
        EbonyColorScheme activeScheme = new EbonyColorScheme();
        DarkMetallicColorScheme enabledScheme = new DarkMetallicColorScheme();
        SubstanceColorScheme disabledScheme = schemes.get("Raven Disabled");
        SubstanceColorScheme selectedDisabledScheme = schemes.get("Raven Selected Disabled");
        SubstanceColorSchemeBundle defaultSchemeBundle = new SubstanceColorSchemeBundle(activeScheme, enabledScheme, disabledScheme);
        SubstanceColorScheme highlightScheme = schemes.get("Graphite Highlight");
        defaultSchemeBundle.registerHighlightColorScheme(highlightScheme, 0.6f, ComponentState.ROLLOVER_UNSELECTED);
        defaultSchemeBundle.registerHighlightColorScheme(highlightScheme, 0.8f, ComponentState.SELECTED);
        defaultSchemeBundle.registerHighlightColorScheme(highlightScheme, 1.0f, ComponentState.ROLLOVER_SELECTED);
        defaultSchemeBundle.registerHighlightColorScheme(highlightScheme, 0.75f, ComponentState.ARMED, ComponentState.ROLLOVER_ARMED);
        defaultSchemeBundle.registerColorScheme((SubstanceColorScheme)new EbonyColorScheme(), ColorSchemeAssociationKind.HIGHLIGHT_BORDER, ComponentState.getActiveStates());
        SubstanceColorScheme textHighlightScheme = schemes.get("Graphite Text Highlight");
        defaultSchemeBundle.registerColorScheme(textHighlightScheme, ColorSchemeAssociationKind.TEXT_HIGHLIGHT, ComponentState.SELECTED, ComponentState.ROLLOVER_SELECTED);
        defaultSchemeBundle.registerColorScheme(highlightScheme, ComponentState.ARMED, ComponentState.ROLLOVER_ARMED);
        SubstanceColorScheme highlightMarkScheme = schemes.get("Raven Highlight Mark");
        defaultSchemeBundle.registerColorScheme(highlightMarkScheme, ColorSchemeAssociationKind.HIGHLIGHT_MARK, ComponentState.getActiveStates());
        defaultSchemeBundle.registerColorScheme(disabledScheme, 0.5f, ComponentState.DISABLED_UNSELECTED);
        defaultSchemeBundle.registerColorScheme(selectedDisabledScheme, 0.65f, ComponentState.DISABLED_SELECTED);
        SubstanceColorScheme tabHighlightScheme = schemes.get("Graphite Tab Highlight");
        defaultSchemeBundle.registerColorScheme(highlightScheme, ComponentState.SELECTED);
        defaultSchemeBundle.registerColorScheme(tabHighlightScheme, ColorSchemeAssociationKind.TAB, ComponentState.SELECTED);
        defaultSchemeBundle.registerColorScheme((SubstanceColorScheme)activeScheme, ColorSchemeAssociationKind.BORDER, ComponentState.SELECTED, ComponentState.ROLLOVER_SELECTED, ComponentState.ROLLOVER_UNSELECTED);
        SubstanceColorScheme selectedMarkScheme = schemes.get("Raven Selected Mark");
        defaultSchemeBundle.registerColorScheme(selectedMarkScheme, ColorSchemeAssociationKind.MARK, ComponentState.SELECTED, ComponentState.ROLLOVER_SELECTED, ComponentState.DISABLED_SELECTED);
        defaultSchemeBundle.registerColorScheme(selectedMarkScheme, ColorSchemeAssociationKind.MARK, ComponentState.ROLLOVER_UNSELECTED);
        defaultSchemeBundle.registerColorScheme((SubstanceColorScheme)activeScheme, ColorSchemeAssociationKind.BORDER, ComponentState.DISABLED_SELECTED);
        this.registerDecorationAreaSchemeBundle(defaultSchemeBundle, DecorationAreaType.NONE);
        this.registerAsDecorationArea(enabledScheme, DecorationAreaType.PRIMARY_TITLE_PANE, DecorationAreaType.SECONDARY_TITLE_PANE, DecorationAreaType.HEADER, DecorationAreaType.FOOTER, DecorationAreaType.GENERAL, DecorationAreaType.TOOLBAR);
        this.registerAsDecorationArea(disabledScheme, DecorationAreaType.PRIMARY_TITLE_PANE_INACTIVE, DecorationAreaType.SECONDARY_TITLE_PANE_INACTIVE);
        this.watermarkScheme = activeScheme.shade(0.4);
        this.buttonShaper = new ClassicButtonShaper();
        this.watermark = new SubstanceCrosshatchWatermark();
        this.fillPainter = new GlassFillPainter();
        this.decorationPainter = new ArcDecorationPainter();
        this.highlightPainter = new ClassicHighlightPainter();
        this.borderPainter = new GlassBorderPainter();
    }

    @Override
    public String getDisplayName() {
        return NAME;
    }
}

