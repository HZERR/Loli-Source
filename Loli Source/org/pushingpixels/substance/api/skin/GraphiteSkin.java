/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.skin;

import org.pushingpixels.substance.api.ColorSchemeAssociationKind;
import org.pushingpixels.substance.api.ColorSchemeSingleColorQuery;
import org.pushingpixels.substance.api.ColorSchemeTransform;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceColorSchemeBundle;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.colorscheme.EbonyColorScheme;
import org.pushingpixels.substance.api.painter.border.ClassicBorderPainter;
import org.pushingpixels.substance.api.painter.border.CompositeBorderPainter;
import org.pushingpixels.substance.api.painter.border.DelegateBorderPainter;
import org.pushingpixels.substance.api.painter.decoration.FlatDecorationPainter;
import org.pushingpixels.substance.api.painter.fill.FractionBasedFillPainter;
import org.pushingpixels.substance.api.painter.highlight.ClassicHighlightPainter;
import org.pushingpixels.substance.api.shaper.ClassicButtonShaper;

public class GraphiteSkin
extends SubstanceSkin {
    public static final String NAME = "Graphite";

    public GraphiteSkin() {
        SubstanceSkin.ColorSchemes schemes = SubstanceSkin.getColorSchemes("org/pushingpixels/substance/api/skin/graphite.colorschemes");
        SubstanceColorScheme activeScheme = schemes.get("Graphite Active");
        SubstanceColorScheme selectedDisabledScheme = schemes.get("Graphite Selected Disabled");
        SubstanceColorScheme selectedScheme = schemes.get("Graphite Selected");
        SubstanceColorScheme disabledScheme = schemes.get("Graphite Disabled");
        SubstanceColorScheme enabledScheme = schemes.get("Graphite Enabled");
        SubstanceColorScheme backgroundScheme = schemes.get("Graphite Background");
        SubstanceColorSchemeBundle defaultSchemeBundle = new SubstanceColorSchemeBundle(activeScheme, enabledScheme, disabledScheme);
        SubstanceColorScheme highlightScheme = schemes.get("Graphite Highlight");
        defaultSchemeBundle.registerHighlightColorScheme(highlightScheme, 0.6f, ComponentState.ROLLOVER_UNSELECTED);
        defaultSchemeBundle.registerHighlightColorScheme(highlightScheme, 0.8f, ComponentState.SELECTED);
        defaultSchemeBundle.registerHighlightColorScheme(highlightScheme, 1.0f, ComponentState.ROLLOVER_SELECTED);
        defaultSchemeBundle.registerHighlightColorScheme(highlightScheme, 0.75f, ComponentState.ARMED, ComponentState.ROLLOVER_ARMED);
        SubstanceColorScheme borderScheme = schemes.get("Graphite Border");
        SubstanceColorScheme separatorScheme = schemes.get("Graphite Separator");
        defaultSchemeBundle.registerColorScheme((SubstanceColorScheme)new EbonyColorScheme(), ColorSchemeAssociationKind.HIGHLIGHT_BORDER, ComponentState.getActiveStates());
        defaultSchemeBundle.registerColorScheme(borderScheme, ColorSchemeAssociationKind.BORDER, new ComponentState[0]);
        defaultSchemeBundle.registerColorScheme(separatorScheme, ColorSchemeAssociationKind.SEPARATOR, new ComponentState[0]);
        SubstanceColorScheme textHighlightScheme = schemes.get("Graphite Text Highlight");
        defaultSchemeBundle.registerColorScheme(textHighlightScheme, ColorSchemeAssociationKind.TEXT_HIGHLIGHT, ComponentState.SELECTED, ComponentState.ROLLOVER_SELECTED);
        defaultSchemeBundle.registerColorScheme(highlightScheme, ComponentState.ARMED, ComponentState.ROLLOVER_ARMED);
        SubstanceColorScheme highlightMarkScheme = schemes.get("Graphite Highlight Mark");
        defaultSchemeBundle.registerColorScheme(highlightMarkScheme, ColorSchemeAssociationKind.HIGHLIGHT_MARK, ComponentState.getActiveStates());
        defaultSchemeBundle.registerColorScheme(highlightMarkScheme, ColorSchemeAssociationKind.MARK, ComponentState.ROLLOVER_SELECTED, ComponentState.ROLLOVER_UNSELECTED);
        defaultSchemeBundle.registerColorScheme(borderScheme, ColorSchemeAssociationKind.MARK, ComponentState.SELECTED);
        defaultSchemeBundle.registerColorScheme(disabledScheme, 0.5f, ComponentState.DISABLED_UNSELECTED);
        defaultSchemeBundle.registerColorScheme(selectedDisabledScheme, 0.65f, ComponentState.DISABLED_SELECTED);
        defaultSchemeBundle.registerColorScheme(highlightScheme, ComponentState.ROLLOVER_SELECTED);
        defaultSchemeBundle.registerColorScheme(selectedScheme, ComponentState.SELECTED);
        SubstanceColorScheme tabHighlightScheme = schemes.get("Graphite Tab Highlight");
        defaultSchemeBundle.registerColorScheme(tabHighlightScheme, ColorSchemeAssociationKind.TAB, ComponentState.ROLLOVER_SELECTED);
        this.registerDecorationAreaSchemeBundle(defaultSchemeBundle, backgroundScheme, DecorationAreaType.NONE);
        this.setSelectedTabFadeStart(0.1);
        this.setSelectedTabFadeEnd(0.3);
        this.buttonShaper = new ClassicButtonShaper();
        this.watermark = null;
        this.fillPainter = new FractionBasedFillPainter(NAME, new float[]{0.0f, 0.5f, 1.0f}, new ColorSchemeSingleColorQuery[]{ColorSchemeSingleColorQuery.ULTRALIGHT, ColorSchemeSingleColorQuery.LIGHT, ColorSchemeSingleColorQuery.LIGHT});
        this.decorationPainter = new FlatDecorationPainter();
        this.highlightPainter = new ClassicHighlightPainter();
        this.borderPainter = new CompositeBorderPainter(NAME, new ClassicBorderPainter(), new DelegateBorderPainter("Graphite Inner", new ClassicBorderPainter(), -1593835521, 0x60FFFFFF, 0x60FFFFFF, new ColorSchemeTransform(){

            @Override
            public SubstanceColorScheme transform(SubstanceColorScheme scheme) {
                return scheme.tint(0.25);
            }
        }));
        this.highlightBorderPainter = new ClassicBorderPainter();
    }

    @Override
    public String getDisplayName() {
        return NAME;
    }
}

