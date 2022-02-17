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
import org.pushingpixels.substance.api.painter.decoration.MarbleNoiseDecorationPainter;
import org.pushingpixels.substance.api.painter.fill.MatteFillPainter;
import org.pushingpixels.substance.api.painter.highlight.ClassicHighlightPainter;
import org.pushingpixels.substance.api.painter.overlay.BottomLineOverlayPainter;
import org.pushingpixels.substance.api.painter.overlay.TopShadowOverlayPainter;
import org.pushingpixels.substance.api.shaper.ClassicButtonShaper;

public class AutumnSkin
extends SubstanceSkin {
    public static final String NAME = "Autumn";
    private BottomLineOverlayPainter bottomLineOverlayPainter;

    public AutumnSkin() {
        SubstanceColorScheme enabledScheme;
        SubstanceSkin.ColorSchemes schemes = SubstanceSkin.getColorSchemes("org/pushingpixels/substance/api/skin/autumn.colorschemes");
        SubstanceColorScheme activeScheme = schemes.get("Autumn Active");
        SubstanceColorScheme disabledScheme = enabledScheme = schemes.get("Autumn Enabled");
        SubstanceColorSchemeBundle defaultSchemeBundle = new SubstanceColorSchemeBundle(activeScheme, enabledScheme, disabledScheme);
        defaultSchemeBundle.registerColorScheme(disabledScheme, 0.6f, ComponentState.DISABLED_UNSELECTED);
        defaultSchemeBundle.registerColorScheme(activeScheme, 0.6f, ComponentState.DISABLED_SELECTED);
        this.registerDecorationAreaSchemeBundle(defaultSchemeBundle, DecorationAreaType.NONE);
        SubstanceColorSchemeBundle titlePaneSchemeBundle = new SubstanceColorSchemeBundle(activeScheme, enabledScheme, disabledScheme);
        titlePaneSchemeBundle.registerColorScheme(disabledScheme, 0.6f, ComponentState.DISABLED_UNSELECTED);
        titlePaneSchemeBundle.registerColorScheme(activeScheme, 0.6f, ComponentState.DISABLED_SELECTED);
        SubstanceColorScheme borderScheme = enabledScheme.saturate(0.2f);
        titlePaneSchemeBundle.registerColorScheme(borderScheme, ColorSchemeAssociationKind.BORDER, ComponentState.ENABLED);
        this.registerDecorationAreaSchemeBundle(titlePaneSchemeBundle, activeScheme, DecorationAreaType.PRIMARY_TITLE_PANE, DecorationAreaType.SECONDARY_TITLE_PANE);
        SubstanceColorScheme watermarkScheme = schemes.get("Autumn Watermark");
        this.registerAsDecorationArea(activeScheme, DecorationAreaType.PRIMARY_TITLE_PANE, DecorationAreaType.SECONDARY_TITLE_PANE, DecorationAreaType.HEADER);
        this.registerAsDecorationArea(watermarkScheme, DecorationAreaType.GENERAL, DecorationAreaType.FOOTER, DecorationAreaType.TOOLBAR);
        this.addOverlayPainter(TopShadowOverlayPainter.getInstance(), DecorationAreaType.TOOLBAR);
        this.bottomLineOverlayPainter = new BottomLineOverlayPainter(ColorSchemeSingleColorQuery.DARK);
        this.addOverlayPainter(this.bottomLineOverlayPainter, DecorationAreaType.PRIMARY_TITLE_PANE, DecorationAreaType.SECONDARY_TITLE_PANE, DecorationAreaType.HEADER);
        this.buttonShaper = new ClassicButtonShaper();
        this.fillPainter = new MatteFillPainter();
        this.borderPainter = new CompositeBorderPainter(NAME, new ClassicBorderPainter(), new DelegateBorderPainter("Autumn Inner", new ClassicBorderPainter(), new ColorSchemeTransform(){

            @Override
            public SubstanceColorScheme transform(SubstanceColorScheme scheme) {
                return scheme.tint(0.8f);
            }
        }));
        this.highlightPainter = new ClassicHighlightPainter();
        MarbleNoiseDecorationPainter decorationPainter = new MarbleNoiseDecorationPainter();
        decorationPainter.setTextureAlpha(0.7f);
        this.decorationPainter = decorationPainter;
    }

    @Override
    public String getDisplayName() {
        return NAME;
    }
}

