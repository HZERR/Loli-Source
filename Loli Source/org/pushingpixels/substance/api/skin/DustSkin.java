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
import org.pushingpixels.substance.api.painter.border.ClassicBorderPainter;
import org.pushingpixels.substance.api.painter.border.CompositeBorderPainter;
import org.pushingpixels.substance.api.painter.border.DelegateBorderPainter;
import org.pushingpixels.substance.api.painter.decoration.MatteDecorationPainter;
import org.pushingpixels.substance.api.painter.fill.StandardFillPainter;
import org.pushingpixels.substance.api.painter.highlight.ClassicHighlightPainter;
import org.pushingpixels.substance.api.painter.overlay.BottomLineOverlayPainter;
import org.pushingpixels.substance.api.painter.overlay.TopLineOverlayPainter;
import org.pushingpixels.substance.api.shaper.ClassicButtonShaper;

public class DustSkin
extends SubstanceSkin {
    public static final String NAME = "Dust";
    private BottomLineOverlayPainter menuOverlayPainter;
    private TopLineOverlayPainter toolbarOverlayPainter;

    public DustSkin() {
        SubstanceSkin.ColorSchemes schemes = SubstanceSkin.getColorSchemes("org/pushingpixels/substance/api/skin/dust.colorschemes");
        SubstanceColorScheme activeScheme = schemes.get("Dust Active");
        SubstanceColorScheme enabledScheme = schemes.get("Dust Enabled");
        SubstanceColorSchemeBundle defaultSchemeBundle = new SubstanceColorSchemeBundle(activeScheme, enabledScheme, enabledScheme);
        defaultSchemeBundle.registerColorScheme(enabledScheme, 0.5f, ComponentState.DISABLED_UNSELECTED);
        defaultSchemeBundle.registerColorScheme(activeScheme, 0.5f, ComponentState.DISABLED_SELECTED);
        SubstanceColorScheme borderEnabledScheme = schemes.get("Dust Border Enabled");
        SubstanceColorScheme borderActiveScheme = schemes.get("Dust Border Active");
        defaultSchemeBundle.registerColorScheme(borderEnabledScheme, ColorSchemeAssociationKind.BORDER, ComponentState.ENABLED, ComponentState.DISABLED_SELECTED, ComponentState.DISABLED_UNSELECTED);
        defaultSchemeBundle.registerColorScheme(borderActiveScheme, ColorSchemeAssociationKind.BORDER, ComponentState.getActiveStates());
        defaultSchemeBundle.registerColorScheme(borderEnabledScheme, ColorSchemeAssociationKind.MARK, new ComponentState[0]);
        this.registerDecorationAreaSchemeBundle(defaultSchemeBundle, DecorationAreaType.NONE);
        SubstanceColorScheme headerActiveScheme = schemes.get("Dust Header Active");
        SubstanceColorScheme headerEnabledScheme = schemes.get("Dust Header Enabled");
        SubstanceColorScheme headerWatermarkScheme = schemes.get("Dust Header Watermark");
        SubstanceColorScheme headerSeparatorScheme = schemes.get("Dust Header Separator");
        SubstanceColorScheme headerBorderScheme = schemes.get("Dust Header Border");
        SubstanceColorSchemeBundle headerSchemeBundle = new SubstanceColorSchemeBundle(headerActiveScheme, headerEnabledScheme, headerEnabledScheme);
        headerSchemeBundle.registerColorScheme(headerEnabledScheme, 0.7f, ComponentState.DISABLED_UNSELECTED);
        headerSchemeBundle.registerColorScheme(headerActiveScheme, 0.7f, ComponentState.DISABLED_SELECTED);
        headerSchemeBundle.registerColorScheme(headerBorderScheme, ColorSchemeAssociationKind.BORDER, new ComponentState[0]);
        headerSchemeBundle.registerColorScheme(headerSeparatorScheme, ColorSchemeAssociationKind.SEPARATOR, new ComponentState[0]);
        headerSchemeBundle.registerHighlightColorScheme(headerActiveScheme, 1.0f, new ComponentState[0]);
        headerSchemeBundle.registerHighlightColorScheme(headerActiveScheme, 0.0f, ComponentState.ENABLED);
        this.registerDecorationAreaSchemeBundle(headerSchemeBundle, DecorationAreaType.TOOLBAR);
        this.registerDecorationAreaSchemeBundle(headerSchemeBundle, headerWatermarkScheme, DecorationAreaType.PRIMARY_TITLE_PANE, DecorationAreaType.SECONDARY_TITLE_PANE, DecorationAreaType.HEADER, DecorationAreaType.FOOTER);
        this.setSelectedTabFadeStart(0.1);
        this.setSelectedTabFadeEnd(0.3);
        this.menuOverlayPainter = new BottomLineOverlayPainter(new ColorSchemeSingleColorQuery(){

            @Override
            public Color query(SubstanceColorScheme scheme) {
                return scheme.getUltraDarkColor().darker();
            }
        });
        this.toolbarOverlayPainter = new TopLineOverlayPainter(new ColorSchemeSingleColorQuery(){

            @Override
            public Color query(SubstanceColorScheme scheme) {
                Color fg = scheme.getForegroundColor();
                return new Color(fg.getRed(), fg.getGreen(), fg.getBlue(), 32);
            }
        });
        this.addOverlayPainter(this.menuOverlayPainter, DecorationAreaType.HEADER);
        this.addOverlayPainter(this.toolbarOverlayPainter, DecorationAreaType.TOOLBAR);
        this.buttonShaper = new ClassicButtonShaper();
        this.watermark = null;
        this.fillPainter = new StandardFillPainter();
        this.decorationPainter = new MatteDecorationPainter();
        this.highlightPainter = new ClassicHighlightPainter();
        this.borderPainter = new CompositeBorderPainter(NAME, new ClassicBorderPainter(), new DelegateBorderPainter("Dust Inner", new ClassicBorderPainter(), 0x60FFFFFF, 0x30FFFFFF, 0x18FFFFFF, new ColorSchemeTransform(){

            @Override
            public SubstanceColorScheme transform(SubstanceColorScheme scheme) {
                return scheme.shiftBackground(scheme.getUltraLightColor(), 0.8).tint(0.6).saturate(0.2);
            }
        }));
    }

    @Override
    public String getDisplayName() {
        return NAME;
    }
}

