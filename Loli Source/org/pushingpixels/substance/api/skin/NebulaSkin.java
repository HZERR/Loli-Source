/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.skin;

import java.awt.Color;
import org.pushingpixels.substance.api.ColorSchemeAssociationKind;
import org.pushingpixels.substance.api.ColorSchemeSingleColorQuery;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.ComponentStateFacet;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceColorSchemeBundle;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.painter.border.FlatBorderPainter;
import org.pushingpixels.substance.api.painter.decoration.ArcDecorationPainter;
import org.pushingpixels.substance.api.painter.decoration.MarbleNoiseDecorationPainter;
import org.pushingpixels.substance.api.painter.fill.SubduedFillPainter;
import org.pushingpixels.substance.api.painter.highlight.ClassicHighlightPainter;
import org.pushingpixels.substance.api.painter.overlay.BottomLineOverlayPainter;
import org.pushingpixels.substance.api.painter.overlay.TopShadowOverlayPainter;
import org.pushingpixels.substance.api.shaper.ClassicButtonShaper;

public class NebulaSkin
extends SubstanceSkin {
    public static final String NAME = "Nebula";
    private BottomLineOverlayPainter bottomLineOverlayPainter;

    public NebulaSkin() {
        SubstanceSkin.ColorSchemes schemes = SubstanceSkin.getColorSchemes("org/pushingpixels/substance/api/skin/nebula.colorschemes");
        SubstanceColorScheme activeScheme = schemes.get("Nebula Active");
        SubstanceColorScheme enabledScheme = schemes.get("Nebula Enabled");
        SubstanceColorScheme rolloverUnselectedScheme = schemes.get("Nebula Rollover Unselected");
        SubstanceColorScheme pressedScheme = schemes.get("Nebula Pressed");
        SubstanceColorScheme rolloverSelectedScheme = schemes.get("Nebula Rollover Selected");
        SubstanceColorScheme disabledScheme = schemes.get("Nebula Disabled");
        SubstanceColorSchemeBundle defaultSchemeBundle = new SubstanceColorSchemeBundle(activeScheme, enabledScheme, disabledScheme);
        defaultSchemeBundle.registerColorScheme(rolloverUnselectedScheme, ComponentState.ROLLOVER_UNSELECTED);
        defaultSchemeBundle.registerColorScheme(rolloverSelectedScheme, ComponentState.ROLLOVER_SELECTED);
        defaultSchemeBundle.registerColorScheme(pressedScheme, ComponentState.PRESSED_SELECTED, ComponentState.PRESSED_UNSELECTED, ComponentState.ARMED, ComponentState.ROLLOVER_ARMED);
        defaultSchemeBundle.registerColorScheme(rolloverUnselectedScheme, ColorSchemeAssociationKind.BORDER, ComponentState.SELECTED);
        defaultSchemeBundle.registerHighlightColorScheme(pressedScheme, 0.6f, ComponentState.ROLLOVER_UNSELECTED);
        defaultSchemeBundle.registerHighlightColorScheme(pressedScheme, 0.8f, ComponentState.SELECTED);
        defaultSchemeBundle.registerHighlightColorScheme(pressedScheme, 0.95f, ComponentState.ROLLOVER_SELECTED);
        defaultSchemeBundle.registerHighlightColorScheme(pressedScheme, 0.8f, ComponentState.ARMED, ComponentState.ROLLOVER_ARMED);
        ComponentState determinateState = new ComponentState("determinate", new ComponentStateFacet[]{ComponentStateFacet.ENABLE, ComponentStateFacet.DETERMINATE}, null);
        ComponentState indeterminateState = new ComponentState("indeterminate", new ComponentStateFacet[]{ComponentStateFacet.ENABLE}, new ComponentStateFacet[]{ComponentStateFacet.DETERMINATE});
        SubstanceColorScheme determinateScheme = schemes.get("Nebula Determinate");
        SubstanceColorScheme determinateBorderScheme = schemes.get("Nebula Determinate Border");
        defaultSchemeBundle.registerColorScheme(determinateScheme, determinateState, indeterminateState);
        defaultSchemeBundle.registerColorScheme(determinateBorderScheme, ColorSchemeAssociationKind.BORDER, determinateState, indeterminateState);
        ComponentState determinateDisabledState = new ComponentState("determinate disabled", new ComponentStateFacet[]{ComponentStateFacet.DETERMINATE}, new ComponentStateFacet[]{ComponentStateFacet.ENABLE});
        ComponentState indeterminateDisabledState = new ComponentState("indeterminate disabled", null, new ComponentStateFacet[]{ComponentStateFacet.ENABLE, ComponentStateFacet.DETERMINATE});
        SubstanceColorScheme determinateDisabledScheme = schemes.get("Nebula Determinate Disabled");
        SubstanceColorScheme determinateDisabledBorderScheme = schemes.get("Nebula Determinate Disabled Border");
        defaultSchemeBundle.registerColorScheme(determinateDisabledScheme, determinateDisabledState, indeterminateDisabledState);
        defaultSchemeBundle.registerColorScheme(determinateDisabledBorderScheme, ColorSchemeAssociationKind.BORDER, determinateDisabledState, indeterminateDisabledState);
        this.registerDecorationAreaSchemeBundle(defaultSchemeBundle, DecorationAreaType.NONE);
        this.registerAsDecorationArea(activeScheme.saturate(-0.5), DecorationAreaType.PRIMARY_TITLE_PANE, DecorationAreaType.SECONDARY_TITLE_PANE, DecorationAreaType.HEADER, DecorationAreaType.FOOTER, DecorationAreaType.GENERAL);
        this.addOverlayPainter(TopShadowOverlayPainter.getInstance(), DecorationAreaType.TOOLBAR);
        this.bottomLineOverlayPainter = new BottomLineOverlayPainter(new ColorSchemeSingleColorQuery(){

            @Override
            public Color query(SubstanceColorScheme scheme) {
                Color dark = scheme.getDarkColor();
                return new Color(dark.getRed(), dark.getGreen(), dark.getBlue(), 160);
            }
        });
        this.addOverlayPainter(this.bottomLineOverlayPainter, DecorationAreaType.PRIMARY_TITLE_PANE, DecorationAreaType.SECONDARY_TITLE_PANE, DecorationAreaType.HEADER);
        this.buttonShaper = new ClassicButtonShaper();
        this.fillPainter = new SubduedFillPainter();
        MarbleNoiseDecorationPainter decorationPainter = new MarbleNoiseDecorationPainter();
        decorationPainter.setBaseDecorationPainter(new ArcDecorationPainter());
        decorationPainter.setTextureAlpha(0.3f);
        this.decorationPainter = decorationPainter;
        this.highlightPainter = new ClassicHighlightPainter();
        this.borderPainter = new FlatBorderPainter();
    }

    @Override
    public String getDisplayName() {
        return NAME;
    }
}

