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
import org.pushingpixels.substance.api.painter.overlay.BottomShadowOverlayPainter;
import org.pushingpixels.substance.api.painter.overlay.TopBezelOverlayPainter;
import org.pushingpixels.substance.api.painter.overlay.TopLineOverlayPainter;
import org.pushingpixels.substance.api.shaper.ClassicButtonShaper;

public class TwilightSkin
extends SubstanceSkin {
    public static final String NAME = "Twilight";
    private BottomLineOverlayPainter toolbarBottomLineOverlayPainter;
    private TopLineOverlayPainter toolbarTopLineOverlayPainter;
    private TopBezelOverlayPainter footerTopBezelOverlayPainter;

    public TwilightSkin() {
        SubstanceSkin.ColorSchemes schemes = SubstanceSkin.getColorSchemes("org/pushingpixels/substance/api/skin/twilight.colorschemes");
        SubstanceColorScheme activeScheme = schemes.get("Twilight Active");
        SubstanceColorScheme enabledScheme = schemes.get("Twilight Enabled");
        SubstanceColorSchemeBundle defaultSchemeBundle = new SubstanceColorSchemeBundle(activeScheme, enabledScheme, enabledScheme);
        defaultSchemeBundle.registerColorScheme(enabledScheme, 0.5f, ComponentState.DISABLED_UNSELECTED);
        SubstanceColorScheme borderDisabledSelectedScheme = schemes.get("Twilight Selected Disabled Border");
        SubstanceColorScheme borderScheme = schemes.get("Twilight Border");
        defaultSchemeBundle.registerColorScheme(borderDisabledSelectedScheme, ColorSchemeAssociationKind.BORDER, ComponentState.DISABLED_SELECTED);
        defaultSchemeBundle.registerColorScheme(borderScheme, ColorSchemeAssociationKind.BORDER, new ComponentState[0]);
        SubstanceColorScheme markActiveScheme = schemes.get("Twilight Mark Active");
        defaultSchemeBundle.registerColorScheme(markActiveScheme, ColorSchemeAssociationKind.MARK, ComponentState.getActiveStates());
        SubstanceColorScheme separatorScheme = schemes.get("Twilight Separator");
        defaultSchemeBundle.registerColorScheme(separatorScheme, ColorSchemeAssociationKind.SEPARATOR, new ComponentState[0]);
        SubstanceColorScheme watermarkScheme = schemes.get("Twilight Watermark");
        this.registerDecorationAreaSchemeBundle(defaultSchemeBundle, watermarkScheme, DecorationAreaType.NONE);
        SubstanceColorSchemeBundle decorationsSchemeBundle = new SubstanceColorSchemeBundle(activeScheme, enabledScheme, enabledScheme);
        decorationsSchemeBundle.registerColorScheme(enabledScheme, 0.5f, ComponentState.DISABLED_UNSELECTED);
        decorationsSchemeBundle.registerColorScheme(borderDisabledSelectedScheme, ColorSchemeAssociationKind.BORDER, ComponentState.DISABLED_SELECTED);
        decorationsSchemeBundle.registerColorScheme(borderScheme, ColorSchemeAssociationKind.BORDER, new ComponentState[0]);
        decorationsSchemeBundle.registerColorScheme(markActiveScheme, ColorSchemeAssociationKind.MARK, ComponentState.getActiveStates());
        SubstanceColorScheme separatorDecorationsScheme = schemes.get("Twilight Decorations Separator");
        decorationsSchemeBundle.registerColorScheme(separatorDecorationsScheme, ColorSchemeAssociationKind.SEPARATOR, new ComponentState[0]);
        SubstanceColorScheme decorationsWatermarkScheme = schemes.get("Twilight Decorations Watermark");
        this.registerDecorationAreaSchemeBundle(decorationsSchemeBundle, decorationsWatermarkScheme, DecorationAreaType.TOOLBAR, DecorationAreaType.GENERAL, DecorationAreaType.FOOTER);
        SubstanceColorSchemeBundle headerSchemeBundle = new SubstanceColorSchemeBundle(activeScheme, enabledScheme, enabledScheme);
        headerSchemeBundle.registerColorScheme(enabledScheme, 0.5f, ComponentState.DISABLED_UNSELECTED);
        SubstanceColorScheme headerBorderScheme = schemes.get("Twilight Header Border");
        headerSchemeBundle.registerColorScheme(borderDisabledSelectedScheme, ColorSchemeAssociationKind.BORDER, ComponentState.DISABLED_SELECTED);
        headerSchemeBundle.registerColorScheme(headerBorderScheme, ColorSchemeAssociationKind.BORDER, new ComponentState[0]);
        headerSchemeBundle.registerColorScheme(markActiveScheme, ColorSchemeAssociationKind.MARK, ComponentState.getActiveStates());
        headerSchemeBundle.registerHighlightColorScheme(activeScheme, 0.7f, ComponentState.ROLLOVER_UNSELECTED, ComponentState.ROLLOVER_ARMED, ComponentState.ARMED);
        headerSchemeBundle.registerHighlightColorScheme(activeScheme, 0.8f, ComponentState.SELECTED);
        headerSchemeBundle.registerHighlightColorScheme(activeScheme, 1.0f, ComponentState.ROLLOVER_SELECTED);
        SubstanceColorScheme headerWatermarkScheme = schemes.get("Twilight Header Watermark");
        this.registerDecorationAreaSchemeBundle(headerSchemeBundle, headerWatermarkScheme, DecorationAreaType.PRIMARY_TITLE_PANE, DecorationAreaType.SECONDARY_TITLE_PANE, DecorationAreaType.HEADER);
        this.setSelectedTabFadeStart(0.2);
        this.setSelectedTabFadeEnd(0.9);
        this.addOverlayPainter(BottomShadowOverlayPainter.getInstance(), DecorationAreaType.TOOLBAR);
        this.addOverlayPainter(BottomShadowOverlayPainter.getInstance(), DecorationAreaType.FOOTER);
        this.toolbarBottomLineOverlayPainter = new BottomLineOverlayPainter(new ColorSchemeSingleColorQuery(){

            @Override
            public Color query(SubstanceColorScheme scheme) {
                return scheme.getUltraDarkColor().darker();
            }
        });
        this.addOverlayPainter(this.toolbarBottomLineOverlayPainter, DecorationAreaType.TOOLBAR);
        this.toolbarTopLineOverlayPainter = new TopLineOverlayPainter(new ColorSchemeSingleColorQuery(){

            @Override
            public Color query(SubstanceColorScheme scheme) {
                Color fg = scheme.getForegroundColor();
                return new Color(fg.getRed(), fg.getGreen(), fg.getBlue(), 32);
            }
        });
        this.addOverlayPainter(this.toolbarTopLineOverlayPainter, DecorationAreaType.TOOLBAR);
        this.footerTopBezelOverlayPainter = new TopBezelOverlayPainter(new ColorSchemeSingleColorQuery(){

            @Override
            public Color query(SubstanceColorScheme scheme) {
                return scheme.getUltraDarkColor().darker();
            }
        }, new ColorSchemeSingleColorQuery(){

            @Override
            public Color query(SubstanceColorScheme scheme) {
                Color fg = scheme.getForegroundColor();
                return new Color(fg.getRed(), fg.getGreen(), fg.getBlue(), 32);
            }
        });
        this.addOverlayPainter(this.footerTopBezelOverlayPainter, DecorationAreaType.FOOTER);
        this.buttonShaper = new ClassicButtonShaper();
        this.watermark = null;
        this.fillPainter = new StandardFillPainter();
        this.decorationPainter = new MatteDecorationPainter();
        this.highlightPainter = new ClassicHighlightPainter();
        this.borderPainter = new CompositeBorderPainter(NAME, new ClassicBorderPainter(), new DelegateBorderPainter("Twilight Inner", new ClassicBorderPainter(), 0x40FFFFFF, 0x20FFFFFF, 0xFFFFFF, new ColorSchemeTransform(){

            @Override
            public SubstanceColorScheme transform(SubstanceColorScheme scheme) {
                return scheme.tint(0.3);
            }
        }));
    }

    @Override
    public String getDisplayName() {
        return NAME;
    }
}

