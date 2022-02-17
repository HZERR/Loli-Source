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
import org.pushingpixels.substance.api.painter.border.ClassicBorderPainter;
import org.pushingpixels.substance.api.painter.border.CompositeBorderPainter;
import org.pushingpixels.substance.api.painter.border.DelegateBorderPainter;
import org.pushingpixels.substance.api.painter.decoration.FlatDecorationPainter;
import org.pushingpixels.substance.api.painter.fill.FractionBasedFillPainter;
import org.pushingpixels.substance.api.painter.highlight.FractionBasedHighlightPainter;
import org.pushingpixels.substance.api.shaper.ClassicButtonShaper;

public class GraphiteAquaSkin
extends SubstanceSkin {
    public static final String NAME = "Graphite Aqua";

    public GraphiteAquaSkin() {
        SubstanceSkin.ColorSchemes schemes = SubstanceSkin.getColorSchemes("org/pushingpixels/substance/api/skin/graphite.colorschemes");
        SubstanceColorScheme selectedDisabledScheme = schemes.get("Graphite Selected Disabled");
        SubstanceColorScheme selectedScheme = schemes.get("Graphite Selected");
        SubstanceColorScheme disabledScheme = schemes.get("Graphite Disabled");
        SubstanceColorScheme enabledScheme = schemes.get("Graphite Enabled");
        SubstanceColorScheme backgroundScheme = schemes.get("Graphite Background");
        SubstanceColorSchemeBundle defaultSchemeBundle = new SubstanceColorSchemeBundle(enabledScheme, enabledScheme, disabledScheme);
        SubstanceColorScheme highlightScheme = schemes.get(NAME);
        defaultSchemeBundle.registerHighlightColorScheme(highlightScheme, 0.75f, ComponentState.ROLLOVER_UNSELECTED);
        defaultSchemeBundle.registerHighlightColorScheme(highlightScheme, 0.9f, ComponentState.SELECTED);
        defaultSchemeBundle.registerHighlightColorScheme(highlightScheme, 1.0f, ComponentState.ROLLOVER_SELECTED);
        defaultSchemeBundle.registerHighlightColorScheme(highlightScheme, 1.0f, ComponentState.ARMED, ComponentState.ROLLOVER_ARMED);
        defaultSchemeBundle.registerColorScheme(highlightScheme, ColorSchemeAssociationKind.BORDER, ComponentState.ROLLOVER_ARMED, ComponentState.ROLLOVER_SELECTED, ComponentState.ROLLOVER_UNSELECTED);
        defaultSchemeBundle.registerColorScheme(highlightScheme, ColorSchemeAssociationKind.FILL, ComponentState.SELECTED, ComponentState.ROLLOVER_SELECTED);
        SubstanceColorScheme borderScheme = schemes.get("Graphite Border");
        SubstanceColorScheme separatorScheme = schemes.get("Graphite Separator");
        defaultSchemeBundle.registerColorScheme(highlightScheme, ColorSchemeAssociationKind.HIGHLIGHT_BORDER, ComponentState.getActiveStates());
        defaultSchemeBundle.registerColorScheme(borderScheme, ColorSchemeAssociationKind.BORDER, new ComponentState[0]);
        defaultSchemeBundle.registerColorScheme(separatorScheme, ColorSchemeAssociationKind.SEPARATOR, new ComponentState[0]);
        defaultSchemeBundle.registerColorScheme(borderScheme, ColorSchemeAssociationKind.MARK, new ComponentState[0]);
        defaultSchemeBundle.registerColorScheme(highlightScheme, ColorSchemeAssociationKind.TEXT_HIGHLIGHT, ComponentState.SELECTED, ComponentState.ROLLOVER_SELECTED);
        defaultSchemeBundle.registerColorScheme(highlightScheme, ComponentState.ARMED, ComponentState.ROLLOVER_ARMED);
        defaultSchemeBundle.registerColorScheme(disabledScheme, 0.5f, ComponentState.DISABLED_UNSELECTED);
        defaultSchemeBundle.registerColorScheme(selectedDisabledScheme, 0.5f, ComponentState.DISABLED_SELECTED);
        defaultSchemeBundle.registerColorScheme(highlightScheme, ComponentState.ROLLOVER_SELECTED);
        defaultSchemeBundle.registerColorScheme(selectedScheme, ComponentState.SELECTED);
        SubstanceColorScheme tabHighlightScheme = schemes.get("Graphite Tab Highlight");
        defaultSchemeBundle.registerColorScheme(tabHighlightScheme, ColorSchemeAssociationKind.TAB, ComponentState.ROLLOVER_SELECTED);
        this.registerDecorationAreaSchemeBundle(defaultSchemeBundle, backgroundScheme, DecorationAreaType.NONE);
        this.setSelectedTabFadeStart(0.15);
        this.setSelectedTabFadeEnd(0.25);
        this.buttonShaper = new ClassicButtonShaper();
        this.watermark = null;
        this.fillPainter = new FractionBasedFillPainter(NAME, new float[]{0.0f, 0.5f, 1.0f}, new ColorSchemeSingleColorQuery[]{ColorSchemeSingleColorQuery.LIGHT, ColorSchemeSingleColorQuery.MID, ColorSchemeSingleColorQuery.MID});
        this.decorationPainter = new FlatDecorationPainter();
        this.highlightPainter = new FractionBasedHighlightPainter(NAME, new float[]{0.0f, 0.5f, 1.0f}, new ColorSchemeSingleColorQuery[]{ColorSchemeSingleColorQuery.EXTRALIGHT, ColorSchemeSingleColorQuery.LIGHT, ColorSchemeSingleColorQuery.MID});
        this.borderPainter = new CompositeBorderPainter(NAME, new ClassicBorderPainter(), new DelegateBorderPainter("Graphite Aqua Inner", new ClassicBorderPainter(), -1056964609, -1862270977, 0x30FFFFFF, new ColorSchemeTransform(){

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

