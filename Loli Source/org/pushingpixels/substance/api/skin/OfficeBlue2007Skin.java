/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.skin;

import java.awt.Color;
import org.pushingpixels.substance.api.ColorSchemeAssociationKind;
import org.pushingpixels.substance.api.ColorSchemeSingleColorQuery;
import org.pushingpixels.substance.api.ColorSchemeTransform;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceColorSchemeBundle;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.painter.border.CompositeBorderPainter;
import org.pushingpixels.substance.api.painter.border.DelegateFractionBasedBorderPainter;
import org.pushingpixels.substance.api.painter.border.FractionBasedBorderPainter;
import org.pushingpixels.substance.api.painter.decoration.FractionBasedDecorationPainter;
import org.pushingpixels.substance.api.painter.fill.FractionBasedFillPainter;
import org.pushingpixels.substance.api.painter.highlight.ClassicHighlightPainter;
import org.pushingpixels.substance.api.painter.overlay.BottomLineOverlayPainter;
import org.pushingpixels.substance.api.shaper.ClassicButtonShaper;

public class OfficeBlue2007Skin
extends SubstanceSkin {
    public static final String NAME = "Office Blue 2007";

    public OfficeBlue2007Skin() {
        SubstanceSkin.ColorSchemes colorSchemes = SubstanceSkin.getColorSchemes("org/pushingpixels/substance/api/skin/office2007.colorschemes");
        SubstanceColorScheme activeScheme = colorSchemes.get("Office Blue Active");
        SubstanceColorScheme enabledScheme = colorSchemes.get("Office Blue Enabled");
        SubstanceColorSchemeBundle defaultSchemeBundle = new SubstanceColorSchemeBundle(activeScheme, enabledScheme, enabledScheme);
        defaultSchemeBundle.registerColorScheme(enabledScheme, 0.5f, ComponentState.DISABLED_UNSELECTED);
        defaultSchemeBundle.registerColorScheme(activeScheme, 0.5f, ComponentState.DISABLED_SELECTED);
        SubstanceColorScheme rolloverScheme = colorSchemes.get("Office Blue Rollover");
        SubstanceColorScheme rolloverSelectedScheme = colorSchemes.get("Office Blue Rollover Selected");
        SubstanceColorScheme selectedScheme = colorSchemes.get("Office Blue Selected");
        SubstanceColorScheme pressedScheme = colorSchemes.get("Office Blue Pressed");
        SubstanceColorScheme pressedSelectedScheme = colorSchemes.get("Office Blue Pressed Selected");
        defaultSchemeBundle.registerColorScheme(rolloverScheme, ComponentState.ROLLOVER_UNSELECTED);
        defaultSchemeBundle.registerColorScheme(rolloverSelectedScheme, ComponentState.ROLLOVER_SELECTED);
        defaultSchemeBundle.registerColorScheme(selectedScheme, ComponentState.SELECTED);
        defaultSchemeBundle.registerColorScheme(pressedScheme, ComponentState.PRESSED_UNSELECTED);
        defaultSchemeBundle.registerColorScheme(pressedSelectedScheme, ComponentState.PRESSED_SELECTED);
        defaultSchemeBundle.registerHighlightColorScheme(rolloverScheme, 0.8f, ComponentState.ROLLOVER_UNSELECTED);
        defaultSchemeBundle.registerHighlightColorScheme(selectedScheme, 0.8f, ComponentState.SELECTED);
        defaultSchemeBundle.registerHighlightColorScheme(rolloverSelectedScheme, 0.8f, ComponentState.ROLLOVER_SELECTED);
        defaultSchemeBundle.registerHighlightColorScheme(selectedScheme, 0.8f, ComponentState.ARMED, ComponentState.ROLLOVER_ARMED);
        SubstanceColorScheme borderEnabledScheme = colorSchemes.get("Office Blue Border Enabled");
        SubstanceColorScheme borderActiveScheme = colorSchemes.get("Office Blue Border Active");
        SubstanceColorScheme borderRolloverScheme = colorSchemes.get("Office Border Rollover");
        SubstanceColorScheme borderRolloverSelectedScheme = colorSchemes.get("Office Border Rollover Selected");
        SubstanceColorScheme borderSelectedScheme = colorSchemes.get("Office Border Selected");
        SubstanceColorScheme borderPressedScheme = colorSchemes.get("Office Border Pressed");
        defaultSchemeBundle.registerColorScheme(borderEnabledScheme, ColorSchemeAssociationKind.BORDER, ComponentState.ENABLED);
        defaultSchemeBundle.registerColorScheme(borderEnabledScheme, ColorSchemeAssociationKind.BORDER, ComponentState.DISABLED_SELECTED, ComponentState.DISABLED_UNSELECTED);
        defaultSchemeBundle.registerColorScheme(borderActiveScheme, ColorSchemeAssociationKind.BORDER, ComponentState.DEFAULT);
        defaultSchemeBundle.registerColorScheme(borderRolloverScheme, ColorSchemeAssociationKind.BORDER, ComponentState.ROLLOVER_UNSELECTED);
        defaultSchemeBundle.registerColorScheme(borderRolloverSelectedScheme, ColorSchemeAssociationKind.BORDER, ComponentState.ROLLOVER_SELECTED, ComponentState.ARMED, ComponentState.ROLLOVER_ARMED);
        defaultSchemeBundle.registerColorScheme(borderSelectedScheme, ColorSchemeAssociationKind.BORDER, ComponentState.SELECTED);
        defaultSchemeBundle.registerColorScheme(borderPressedScheme, ColorSchemeAssociationKind.BORDER, ComponentState.PRESSED_SELECTED, ComponentState.PRESSED_UNSELECTED);
        SubstanceColorScheme tabSelectedScheme = colorSchemes.get("Office Blue Tab Selected");
        SubstanceColorScheme tabRolloverScheme = colorSchemes.get("Office Blue Tab Rollover");
        defaultSchemeBundle.registerColorScheme(tabSelectedScheme, ColorSchemeAssociationKind.TAB, ComponentState.SELECTED, ComponentState.ROLLOVER_SELECTED, ComponentState.PRESSED_SELECTED, ComponentState.PRESSED_UNSELECTED);
        defaultSchemeBundle.registerColorScheme(tabRolloverScheme, ColorSchemeAssociationKind.TAB, ComponentState.ROLLOVER_UNSELECTED);
        defaultSchemeBundle.registerColorScheme(borderEnabledScheme, ColorSchemeAssociationKind.TAB_BORDER, ComponentState.SELECTED, ComponentState.ROLLOVER_UNSELECTED);
        defaultSchemeBundle.registerColorScheme(rolloverSelectedScheme, ColorSchemeAssociationKind.TAB_BORDER, ComponentState.ROLLOVER_SELECTED);
        SubstanceColorScheme separatorScheme = colorSchemes.get("Office Blue Separator");
        defaultSchemeBundle.registerColorScheme(separatorScheme, ColorSchemeAssociationKind.SEPARATOR, new ComponentState[0]);
        this.registerDecorationAreaSchemeBundle(defaultSchemeBundle, DecorationAreaType.NONE);
        this.watermarkScheme = colorSchemes.get("Office Blue Watermark");
        SubstanceColorScheme generalWatermarkScheme = colorSchemes.get("Office Blue Header Watermark");
        this.registerAsDecorationArea(generalWatermarkScheme, DecorationAreaType.FOOTER, DecorationAreaType.HEADER, DecorationAreaType.TOOLBAR);
        SubstanceColorScheme titleWatermarkScheme = colorSchemes.get("Office Blue Title Watermark");
        this.registerAsDecorationArea(titleWatermarkScheme, DecorationAreaType.GENERAL, DecorationAreaType.PRIMARY_TITLE_PANE, DecorationAreaType.SECONDARY_TITLE_PANE);
        this.setSelectedTabFadeStart(0.7);
        this.setSelectedTabFadeEnd(0.9);
        this.addOverlayPainter(new BottomLineOverlayPainter(new ColorSchemeSingleColorQuery(){

            @Override
            public Color query(SubstanceColorScheme scheme) {
                Color fg = scheme.getForegroundColor();
                return new Color(fg.getRed(), fg.getGreen(), fg.getBlue(), 72);
            }
        }), DecorationAreaType.PRIMARY_TITLE_PANE, DecorationAreaType.SECONDARY_TITLE_PANE);
        this.buttonShaper = new ClassicButtonShaper();
        this.watermark = null;
        this.fillPainter = new FractionBasedFillPainter(NAME, new float[]{0.0f, 0.49999f, 0.5f, 1.0f}, new ColorSchemeSingleColorQuery[]{ColorSchemeSingleColorQuery.ULTRALIGHT, ColorSchemeSingleColorQuery.LIGHT, ColorSchemeSingleColorQuery.ULTRADARK, ColorSchemeSingleColorQuery.EXTRALIGHT});
        FractionBasedBorderPainter outerBorderPainter = new FractionBasedBorderPainter("Office Blue 2007 Outer", new float[]{0.0f, 0.5f, 1.0f}, new ColorSchemeSingleColorQuery[]{ColorSchemeSingleColorQuery.EXTRALIGHT, ColorSchemeSingleColorQuery.DARK, ColorSchemeSingleColorQuery.MID});
        DelegateFractionBasedBorderPainter innerBorderPainter = new DelegateFractionBasedBorderPainter("Office Blue 2007 Inner", outerBorderPainter, new int[]{-1, -1, -1}, new ColorSchemeTransform(){

            @Override
            public SubstanceColorScheme transform(SubstanceColorScheme scheme) {
                return scheme.tint(0.8f);
            }
        });
        this.borderPainter = new CompositeBorderPainter(NAME, outerBorderPainter, innerBorderPainter);
        this.decorationPainter = new FractionBasedDecorationPainter(NAME, new float[]{0.0f, 0.1199999f, 0.12f, 0.5f, 0.9f, 1.0f}, new ColorSchemeSingleColorQuery[]{ColorSchemeSingleColorQuery.LIGHT, ColorSchemeSingleColorQuery.LIGHT, ColorSchemeSingleColorQuery.ULTRADARK, ColorSchemeSingleColorQuery.MID, ColorSchemeSingleColorQuery.ULTRALIGHT, ColorSchemeSingleColorQuery.LIGHT});
        this.highlightPainter = new ClassicHighlightPainter();
    }

    @Override
    public String getDisplayName() {
        return NAME;
    }
}

