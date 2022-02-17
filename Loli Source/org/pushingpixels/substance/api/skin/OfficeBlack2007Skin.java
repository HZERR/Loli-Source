/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.skin;

import java.awt.Color;
import org.pushingpixels.substance.api.ColorSchemeAssociationKind;
import org.pushingpixels.substance.api.ColorSchemeSingleColorQuery;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceColorSchemeBundle;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.colorscheme.LightGrayColorScheme;
import org.pushingpixels.substance.api.painter.border.ClassicBorderPainter;
import org.pushingpixels.substance.api.painter.decoration.FractionBasedDecorationPainter;
import org.pushingpixels.substance.api.painter.fill.FractionBasedFillPainter;
import org.pushingpixels.substance.api.painter.highlight.ClassicHighlightPainter;
import org.pushingpixels.substance.api.painter.overlay.BottomLineOverlayPainter;
import org.pushingpixels.substance.api.shaper.ClassicButtonShaper;

public class OfficeBlack2007Skin
extends SubstanceSkin {
    public static final String NAME = "Office Black 2007";

    public OfficeBlack2007Skin() {
        SubstanceSkin.ColorSchemes colorSchemes = SubstanceSkin.getColorSchemes("org/pushingpixels/substance/api/skin/office2007.colorschemes");
        SubstanceColorScheme activeScheme = colorSchemes.get("Office Silver Active");
        SubstanceColorScheme enabledScheme = colorSchemes.get("Office Black Enabled");
        SubstanceColorScheme disabledScheme = new LightGrayColorScheme().tint(0.05).named("Office Black Disabled");
        SubstanceColorSchemeBundle defaultSchemeBundle = new SubstanceColorSchemeBundle(activeScheme, enabledScheme, enabledScheme);
        defaultSchemeBundle.registerColorScheme(enabledScheme, 0.5f, ComponentState.DISABLED_UNSELECTED);
        defaultSchemeBundle.registerColorScheme(activeScheme, 0.5f, ComponentState.DISABLED_SELECTED);
        SubstanceColorScheme rolloverScheme = colorSchemes.get("Office Silver Rollover");
        SubstanceColorScheme rolloverSelectedScheme = colorSchemes.get("Office Silver Rollover Selected");
        SubstanceColorScheme selectedScheme = colorSchemes.get("Office Silver Selected");
        SubstanceColorScheme pressedScheme = colorSchemes.get("Office Silver Pressed");
        SubstanceColorScheme pressedSelectedScheme = colorSchemes.get("Office Silver Pressed Selected");
        defaultSchemeBundle.registerColorScheme(rolloverScheme, ComponentState.ROLLOVER_UNSELECTED);
        defaultSchemeBundle.registerColorScheme(rolloverSelectedScheme, ComponentState.ROLLOVER_SELECTED);
        defaultSchemeBundle.registerColorScheme(selectedScheme, ComponentState.SELECTED);
        defaultSchemeBundle.registerColorScheme(pressedScheme, ComponentState.PRESSED_UNSELECTED);
        defaultSchemeBundle.registerColorScheme(pressedSelectedScheme, ComponentState.PRESSED_SELECTED);
        defaultSchemeBundle.registerHighlightColorScheme(rolloverScheme, 0.8f, ComponentState.ROLLOVER_UNSELECTED);
        defaultSchemeBundle.registerHighlightColorScheme(selectedScheme, 0.8f, ComponentState.SELECTED);
        defaultSchemeBundle.registerHighlightColorScheme(rolloverSelectedScheme, 0.8f, ComponentState.ROLLOVER_SELECTED);
        defaultSchemeBundle.registerHighlightColorScheme(selectedScheme, 0.8f, ComponentState.ARMED, ComponentState.ROLLOVER_ARMED);
        SubstanceColorScheme borderEnabledScheme = colorSchemes.get("Office Silver Border Enabled");
        SubstanceColorScheme borderActiveScheme = colorSchemes.get("Office Silver Border Active");
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
        SubstanceColorScheme markEnabledScheme = colorSchemes.get("Office Black Mark Enabled");
        defaultSchemeBundle.registerColorScheme(markEnabledScheme, ColorSchemeAssociationKind.MARK, ComponentState.ENABLED);
        defaultSchemeBundle.registerColorScheme(markEnabledScheme, ColorSchemeAssociationKind.MARK, ComponentState.DISABLED_SELECTED, ComponentState.DISABLED_UNSELECTED);
        this.registerDecorationAreaSchemeBundle(defaultSchemeBundle, DecorationAreaType.NONE);
        SubstanceColorScheme tabSelectedScheme = colorSchemes.get("Office Silver Tab Selected");
        SubstanceColorScheme tabRolloverScheme = colorSchemes.get("Office Silver Tab Rollover");
        defaultSchemeBundle.registerColorScheme(tabSelectedScheme, ColorSchemeAssociationKind.TAB, ComponentState.SELECTED, ComponentState.ROLLOVER_SELECTED, ComponentState.PRESSED_SELECTED, ComponentState.PRESSED_UNSELECTED);
        defaultSchemeBundle.registerColorScheme(tabRolloverScheme, ColorSchemeAssociationKind.TAB, ComponentState.ROLLOVER_UNSELECTED);
        defaultSchemeBundle.registerColorScheme(borderEnabledScheme, ColorSchemeAssociationKind.TAB_BORDER, ComponentState.SELECTED, ComponentState.ROLLOVER_UNSELECTED);
        defaultSchemeBundle.registerColorScheme(rolloverSelectedScheme, ColorSchemeAssociationKind.TAB_BORDER, ComponentState.ROLLOVER_SELECTED);
        SubstanceColorScheme separatorScheme = colorSchemes.get("Office Silver Separator");
        defaultSchemeBundle.registerColorScheme(separatorScheme, ColorSchemeAssociationKind.SEPARATOR, new ComponentState[0]);
        this.registerDecorationAreaSchemeBundle(defaultSchemeBundle, DecorationAreaType.NONE);
        SubstanceColorScheme activeHeaderScheme = colorSchemes.get("Office Black Header Active");
        SubstanceColorScheme enabledHeaderScheme = colorSchemes.get("Office Black Header Enabled");
        SubstanceColorSchemeBundle headerSchemeBundle = new SubstanceColorSchemeBundle(activeHeaderScheme, enabledHeaderScheme, disabledScheme);
        SubstanceColorScheme headerMarkEnabledScheme = colorSchemes.get("Office Black Header Mark Enabled");
        headerSchemeBundle.registerColorScheme(headerMarkEnabledScheme, ColorSchemeAssociationKind.MARK, ComponentState.ENABLED);
        headerSchemeBundle.registerColorScheme(markEnabledScheme, ColorSchemeAssociationKind.MARK, ComponentState.DISABLED_SELECTED, ComponentState.DISABLED_UNSELECTED);
        headerSchemeBundle.registerColorScheme(enabledHeaderScheme, 0.5f, ComponentState.DISABLED_UNSELECTED, ComponentState.DISABLED_SELECTED);
        headerSchemeBundle.registerHighlightColorScheme(activeScheme, 0.6f, ComponentState.ROLLOVER_UNSELECTED);
        headerSchemeBundle.registerHighlightColorScheme(activeScheme, 0.8f, ComponentState.SELECTED);
        headerSchemeBundle.registerHighlightColorScheme(activeScheme, 0.95f, ComponentState.ROLLOVER_SELECTED);
        headerSchemeBundle.registerHighlightColorScheme(activeScheme, 0.8f, ComponentState.ARMED, ComponentState.ROLLOVER_ARMED);
        this.registerDecorationAreaSchemeBundle(headerSchemeBundle, activeHeaderScheme, DecorationAreaType.PRIMARY_TITLE_PANE, DecorationAreaType.HEADER);
        SubstanceColorScheme enabledFooterScheme = colorSchemes.get("Office Black Footer Enabled");
        SubstanceColorSchemeBundle footerSchemeBundle = new SubstanceColorSchemeBundle(activeHeaderScheme, enabledFooterScheme, disabledScheme);
        SubstanceColorScheme borderFooterEnabledScheme = colorSchemes.get("Office Black Footer Border Enabled");
        footerSchemeBundle.registerColorScheme(borderFooterEnabledScheme, ColorSchemeAssociationKind.BORDER, ComponentState.ENABLED, ComponentState.DISABLED_SELECTED, ComponentState.DISABLED_UNSELECTED);
        footerSchemeBundle.registerColorScheme(borderFooterEnabledScheme, ColorSchemeAssociationKind.BORDER, ComponentState.getActiveStates());
        footerSchemeBundle.registerColorScheme(rolloverScheme, ComponentState.ROLLOVER_UNSELECTED);
        footerSchemeBundle.registerColorScheme(rolloverSelectedScheme, ComponentState.ROLLOVER_SELECTED);
        footerSchemeBundle.registerColorScheme(selectedScheme, ComponentState.SELECTED);
        footerSchemeBundle.registerColorScheme(pressedScheme, ComponentState.PRESSED_UNSELECTED);
        footerSchemeBundle.registerColorScheme(pressedSelectedScheme, ComponentState.PRESSED_SELECTED);
        SubstanceColorScheme footerSeparatorScheme = colorSchemes.get("Office Black Footer Separator");
        footerSchemeBundle.registerColorScheme(footerSeparatorScheme, ColorSchemeAssociationKind.SEPARATOR, new ComponentState[0]);
        this.registerDecorationAreaSchemeBundle(footerSchemeBundle, activeHeaderScheme, DecorationAreaType.FOOTER);
        this.watermarkScheme = colorSchemes.get("Office Black Watermark");
        this.registerAsDecorationArea(this.watermarkScheme, DecorationAreaType.GENERAL);
        SubstanceColorScheme headerWatermarkScheme = colorSchemes.get("Office Black Header Watermark");
        this.registerAsDecorationArea(headerWatermarkScheme, DecorationAreaType.HEADER, DecorationAreaType.TOOLBAR);
        this.setSelectedTabFadeStart(0.6);
        this.setSelectedTabFadeEnd(0.9);
        this.addOverlayPainter(new BottomLineOverlayPainter(new ColorSchemeSingleColorQuery(){

            @Override
            public Color query(SubstanceColorScheme scheme) {
                Color fg = scheme.getUltraDarkColor();
                return new Color(fg.getRed(), fg.getGreen(), fg.getBlue(), 192);
            }
        }), DecorationAreaType.PRIMARY_TITLE_PANE);
        this.buttonShaper = new ClassicButtonShaper();
        this.watermark = null;
        this.fillPainter = new FractionBasedFillPainter(NAME, new float[]{0.0f, 0.49999f, 0.5f, 1.0f}, new ColorSchemeSingleColorQuery[]{ColorSchemeSingleColorQuery.LIGHT, ColorSchemeSingleColorQuery.LIGHT, ColorSchemeSingleColorQuery.MID, ColorSchemeSingleColorQuery.LIGHT});
        this.borderPainter = new ClassicBorderPainter();
        this.decorationPainter = new FractionBasedDecorationPainter(NAME, new float[]{0.0f, 0.2499999f, 0.25f, 0.6f, 1.0f}, new ColorSchemeSingleColorQuery[]{ColorSchemeSingleColorQuery.LIGHT, ColorSchemeSingleColorQuery.MID, ColorSchemeSingleColorQuery.DARK, ColorSchemeSingleColorQuery.DARK, ColorSchemeSingleColorQuery.MID}, DecorationAreaType.PRIMARY_TITLE_PANE, DecorationAreaType.FOOTER);
        this.highlightPainter = new ClassicHighlightPainter();
    }

    @Override
    public String getDisplayName() {
        return NAME;
    }
}

