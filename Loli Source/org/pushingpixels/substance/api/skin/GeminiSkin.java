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
import org.pushingpixels.substance.api.painter.border.DelegateFractionBasedBorderPainter;
import org.pushingpixels.substance.api.painter.border.FractionBasedBorderPainter;
import org.pushingpixels.substance.api.painter.decoration.MatteDecorationPainter;
import org.pushingpixels.substance.api.painter.fill.FractionBasedFillPainter;
import org.pushingpixels.substance.api.painter.highlight.ClassicHighlightPainter;
import org.pushingpixels.substance.api.painter.overlay.BottomLineOverlayPainter;
import org.pushingpixels.substance.api.painter.overlay.BottomShadowOverlayPainter;
import org.pushingpixels.substance.api.painter.overlay.TopBezelOverlayPainter;
import org.pushingpixels.substance.api.painter.overlay.TopLineOverlayPainter;
import org.pushingpixels.substance.api.shaper.ClassicButtonShaper;

public class GeminiSkin
extends SubstanceSkin {
    public static final String NAME = "Gemini";
    private BottomLineOverlayPainter menuOverlayPainter;
    private TopLineOverlayPainter toolbarOverlayPainter;
    private BottomLineOverlayPainter toolbarBottomLineOverlayPainter;
    private TopBezelOverlayPainter footerTopBezelOverlayPainter;

    public GeminiSkin() {
        SubstanceSkin.ColorSchemes schemes = SubstanceSkin.getColorSchemes("org/pushingpixels/substance/api/skin/gemini.colorschemes");
        SubstanceColorScheme grayScheme = schemes.get("Gemini Gray");
        SubstanceColorScheme lightGrayScheme = schemes.get("Gemini Light Gray");
        SubstanceColorSchemeBundle defaultSchemeBundle = new SubstanceColorSchemeBundle(grayScheme, grayScheme, lightGrayScheme);
        SubstanceColorScheme highlightScheme = schemes.get("Gemini Highlight");
        SubstanceColorScheme highlightBorderScheme = schemes.get("Gemini Highlight Border");
        GeminiSkin.applyHighlightColorScheme(defaultSchemeBundle, highlightScheme, highlightBorderScheme);
        SubstanceColorScheme grayBorderScheme = schemes.get("Gemini Gray Border");
        SubstanceColorScheme lightGrayBorderScheme = schemes.get("Gemini Light Gray Border");
        SubstanceColorScheme lightGraySeparatorScheme = schemes.get("Gemini Light Gray Separator");
        defaultSchemeBundle.registerColorScheme(grayBorderScheme, ColorSchemeAssociationKind.BORDER, new ComponentState[0]);
        defaultSchemeBundle.registerColorScheme(lightGrayBorderScheme, ColorSchemeAssociationKind.BORDER, ComponentState.DISABLED_DEFAULT, ComponentState.DISABLED_SELECTED, ComponentState.DISABLED_UNSELECTED);
        defaultSchemeBundle.registerColorScheme(grayScheme, ComponentState.ROLLOVER_UNSELECTED);
        defaultSchemeBundle.registerColorScheme(lightGraySeparatorScheme, ColorSchemeAssociationKind.SEPARATOR, new ComponentState[0]);
        defaultSchemeBundle.registerColorScheme(grayScheme, ColorSchemeAssociationKind.MARK, new ComponentState[0]);
        defaultSchemeBundle.registerColorScheme(lightGrayScheme, 0.5f, ComponentState.DISABLED_UNSELECTED);
        defaultSchemeBundle.registerColorScheme(lightGrayScheme, 0.7f, ComponentState.DISABLED_SELECTED);
        SubstanceColorScheme whiteBackgroundScheme = schemes.get("Gemini White Background");
        this.registerDecorationAreaSchemeBundle(defaultSchemeBundle, whiteBackgroundScheme, DecorationAreaType.NONE);
        SubstanceColorSchemeBundle generalSchemeBundle = new SubstanceColorSchemeBundle(grayScheme, grayScheme, lightGrayScheme);
        generalSchemeBundle.registerColorScheme(grayScheme, ComponentState.ROLLOVER_UNSELECTED);
        generalSchemeBundle.registerColorScheme(grayScheme, ColorSchemeAssociationKind.MARK, new ComponentState[0]);
        generalSchemeBundle.registerColorScheme(grayBorderScheme, ColorSchemeAssociationKind.BORDER, new ComponentState[0]);
        GeminiSkin.applyHighlightColorScheme(generalSchemeBundle, highlightScheme, highlightBorderScheme);
        this.registerDecorationAreaSchemeBundle(generalSchemeBundle, grayScheme, DecorationAreaType.GENERAL, DecorationAreaType.FOOTER);
        SubstanceColorScheme blackColorScheme = schemes.get("Gemini Black");
        SubstanceColorSchemeBundle headerSchemeBundle = new SubstanceColorSchemeBundle(blackColorScheme, blackColorScheme, blackColorScheme);
        headerSchemeBundle.registerColorScheme(blackColorScheme, 0.5f, ComponentState.DISABLED_SELECTED, ComponentState.DISABLED_UNSELECTED);
        headerSchemeBundle.registerColorScheme(blackColorScheme, ComponentState.ROLLOVER_UNSELECTED);
        headerSchemeBundle.registerColorScheme(blackColorScheme, ColorSchemeAssociationKind.MARK, new ComponentState[0]);
        headerSchemeBundle.registerColorScheme(blackColorScheme, ColorSchemeAssociationKind.BORDER, new ComponentState[0]);
        GeminiSkin.applyHighlightColorScheme(headerSchemeBundle, highlightScheme, highlightBorderScheme);
        this.registerDecorationAreaSchemeBundle(headerSchemeBundle, blackColorScheme, DecorationAreaType.PRIMARY_TITLE_PANE, DecorationAreaType.SECONDARY_TITLE_PANE, DecorationAreaType.HEADER);
        SubstanceColorScheme darkBlueColorScheme = schemes.get("Gemini Dark Blue");
        SubstanceColorScheme darkBlueBackgroundColorScheme = schemes.get("Gemini Dark Blue Background");
        SubstanceColorSchemeBundle toolbarSchemeBundle = new SubstanceColorSchemeBundle(blackColorScheme, darkBlueColorScheme, darkBlueColorScheme);
        toolbarSchemeBundle.registerColorScheme(blackColorScheme, 0.5f, ComponentState.DISABLED_SELECTED);
        toolbarSchemeBundle.registerColorScheme(darkBlueColorScheme, 0.5f, ComponentState.DISABLED_UNSELECTED);
        toolbarSchemeBundle.registerColorScheme(blackColorScheme, ComponentState.ROLLOVER_UNSELECTED);
        toolbarSchemeBundle.registerColorScheme(darkBlueColorScheme, ColorSchemeAssociationKind.MARK, new ComponentState[0]);
        toolbarSchemeBundle.registerColorScheme(darkBlueColorScheme, ColorSchemeAssociationKind.BORDER, new ComponentState[0]);
        GeminiSkin.applyHighlightColorScheme(toolbarSchemeBundle, highlightScheme, darkBlueColorScheme);
        this.registerDecorationAreaSchemeBundle(toolbarSchemeBundle, darkBlueBackgroundColorScheme, DecorationAreaType.TOOLBAR);
        this.setSelectedTabFadeStart(0.15);
        this.setSelectedTabFadeEnd(0.25);
        this.footerTopBezelOverlayPainter = new TopBezelOverlayPainter(ColorSchemeSingleColorQuery.DARK, ColorSchemeSingleColorQuery.ULTRALIGHT);
        this.addOverlayPainter(this.footerTopBezelOverlayPainter, DecorationAreaType.FOOTER);
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
        this.addOverlayPainter(BottomShadowOverlayPainter.getInstance(), DecorationAreaType.TOOLBAR);
        this.toolbarBottomLineOverlayPainter = new BottomLineOverlayPainter(ColorSchemeSingleColorQuery.ULTRADARK);
        this.addOverlayPainter(this.toolbarBottomLineOverlayPainter, DecorationAreaType.TOOLBAR);
        this.buttonShaper = new ClassicButtonShaper();
        this.watermark = null;
        this.fillPainter = new FractionBasedFillPainter(NAME, new float[]{0.0f, 0.5f, 1.0f}, new ColorSchemeSingleColorQuery[]{ColorSchemeSingleColorQuery.EXTRALIGHT, ColorSchemeSingleColorQuery.LIGHT, ColorSchemeSingleColorQuery.MID});
        this.decorationPainter = new MatteDecorationPainter();
        this.highlightPainter = new ClassicHighlightPainter();
        FractionBasedBorderPainter outerBorderPainter = new FractionBasedBorderPainter("Gemini Outer", new float[]{0.0f, 0.5f, 1.0f}, new ColorSchemeSingleColorQuery[]{ColorSchemeSingleColorQuery.ULTRADARK, ColorSchemeSingleColorQuery.ULTRADARK, ColorSchemeSingleColorQuery.ULTRADARK});
        DelegateFractionBasedBorderPainter innerBorderPainter = new DelegateFractionBasedBorderPainter("Gemini Inner", outerBorderPainter, new int[]{0x60FFFFFF, 0x40FFFFFF, 0x20FFFFFF}, new ColorSchemeTransform(){

            @Override
            public SubstanceColorScheme transform(SubstanceColorScheme scheme) {
                return scheme.tint(0.7f);
            }
        });
        this.borderPainter = new CompositeBorderPainter(NAME, outerBorderPainter, innerBorderPainter);
        this.highlightBorderPainter = new ClassicBorderPainter();
    }

    private static void applyHighlightColorScheme(SubstanceColorSchemeBundle schemeBundle, SubstanceColorScheme highlightScheme, SubstanceColorScheme highlightBorderScheme) {
        schemeBundle.registerHighlightColorScheme(highlightScheme, 0.75f, ComponentState.ROLLOVER_UNSELECTED);
        schemeBundle.registerHighlightColorScheme(highlightScheme, 0.9f, ComponentState.SELECTED);
        schemeBundle.registerHighlightColorScheme(highlightScheme, 1.0f, ComponentState.ROLLOVER_SELECTED);
        schemeBundle.registerHighlightColorScheme(highlightScheme, 1.0f, ComponentState.ARMED, ComponentState.ROLLOVER_ARMED);
        schemeBundle.registerColorScheme(highlightBorderScheme, ColorSchemeAssociationKind.BORDER, ComponentState.ROLLOVER_ARMED, ComponentState.ROLLOVER_SELECTED, ComponentState.ROLLOVER_UNSELECTED);
        schemeBundle.registerColorScheme(highlightScheme, ColorSchemeAssociationKind.FILL, ComponentState.SELECTED, ComponentState.ROLLOVER_SELECTED);
        schemeBundle.registerColorScheme(highlightScheme, ColorSchemeAssociationKind.HIGHLIGHT_BORDER, ComponentState.getActiveStates());
        schemeBundle.registerColorScheme(highlightScheme, ColorSchemeAssociationKind.TEXT_HIGHLIGHT, ComponentState.SELECTED, ComponentState.ROLLOVER_SELECTED);
        schemeBundle.registerColorScheme(highlightScheme, ComponentState.ARMED, ComponentState.ROLLOVER_ARMED);
    }

    @Override
    public String getDisplayName() {
        return NAME;
    }
}

