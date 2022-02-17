/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.skin;

import org.pushingpixels.substance.api.ColorSchemeAssociationKind;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.ComponentStateFacet;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceColorSchemeBundle;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.colorscheme.SteelBlueColorScheme;
import org.pushingpixels.substance.api.painter.border.GlassBorderPainter;
import org.pushingpixels.substance.api.painter.decoration.ArcDecorationPainter;
import org.pushingpixels.substance.api.painter.fill.ClassicFillPainter;
import org.pushingpixels.substance.api.painter.highlight.GlassHighlightPainter;
import org.pushingpixels.substance.api.painter.overlay.BottomLineOverlayPainter;
import org.pushingpixels.substance.api.shaper.ClassicButtonShaper;
import org.pushingpixels.substance.api.skin.CopyMutableColorScheme;
import org.pushingpixels.substance.internal.colorscheme.BlendBiColorScheme;

public class CeruleanSkin
extends SubstanceSkin {
    public static final String NAME = "Cerulean";
    private BottomLineOverlayPainter bottomLineOverlayPainter;

    public CeruleanSkin() {
        SubstanceSkin.ColorSchemes schemes = SubstanceSkin.getColorSchemes("org/pushingpixels/substance/api/skin/nebula.colorschemes");
        SubstanceColorScheme activeScheme = schemes.get("Nebula Active");
        SubstanceColorScheme enabledScheme = schemes.get("Nebula Enabled").saturate(-0.9);
        SubstanceColorScheme rolloverUnselectedScheme = schemes.get("Nebula Rollover Unselected");
        SubstanceColorScheme pressedScheme = schemes.get("Nebula Pressed");
        SubstanceColorScheme rolloverSelectedScheme = schemes.get("Nebula Rollover Selected");
        SubstanceColorScheme disabledScheme = schemes.get("Nebula Disabled").saturate(-0.9);
        SubstanceColorSchemeBundle defaultSchemeBundle = new SubstanceColorSchemeBundle(activeScheme, enabledScheme, disabledScheme);
        CopyMutableColorScheme steelBlue = new CopyMutableColorScheme("Cerulean Hover", new SteelBlueColorScheme().tint(0.4));
        steelBlue.setForegroundColor(enabledScheme.getForegroundColor());
        double saturate = 0.1;
        double tint = 0.4;
        double shade = tint / 4.0;
        CopyMutableColorScheme pressed = new CopyMutableColorScheme("Cerulean Pressed", steelBlue.saturate(saturate).shade(shade));
        defaultSchemeBundle.registerColorScheme((SubstanceColorScheme)pressed, ComponentState.PRESSED_SELECTED, ComponentState.PRESSED_UNSELECTED);
        defaultSchemeBundle.registerColorScheme((SubstanceColorScheme)new BlendBiColorScheme(steelBlue, disabledScheme, 0.25), ComponentState.DISABLED_SELECTED);
        defaultSchemeBundle.registerColorScheme(steelBlue.tint(tint).saturate(saturate), ComponentState.SELECTED);
        defaultSchemeBundle.registerColorScheme(steelBlue.shade(shade / 2.0).saturate(saturate / 2.0), ComponentState.ROLLOVER_SELECTED);
        defaultSchemeBundle.registerColorScheme(steelBlue.tint(tint / 2.0).saturate(saturate / 2.0), ComponentState.ROLLOVER_UNSELECTED);
        defaultSchemeBundle.registerColorScheme(steelBlue.shade(0.5), ColorSchemeAssociationKind.MARK, ComponentState.getActiveStates());
        defaultSchemeBundle.registerColorScheme((SubstanceColorScheme)steelBlue, ColorSchemeAssociationKind.BORDER, ComponentState.getActiveStates());
        ComponentState determinateState = new ComponentState("determinate enabled", new ComponentStateFacet[]{ComponentStateFacet.ENABLE, ComponentStateFacet.DETERMINATE, ComponentStateFacet.SELECTION}, null);
        ComponentState determinateDisabledState = new ComponentState("determinate disabled", new ComponentStateFacet[]{ComponentStateFacet.DETERMINATE, ComponentStateFacet.SELECTION}, new ComponentStateFacet[]{ComponentStateFacet.ENABLE});
        ComponentState indeterminateState = new ComponentState("indeterminate enabled", new ComponentStateFacet[]{ComponentStateFacet.ENABLE, ComponentStateFacet.SELECTION}, new ComponentStateFacet[]{ComponentStateFacet.DETERMINATE});
        ComponentState indeterminateDisabledState = new ComponentState("indeterminate disabled", null, new ComponentStateFacet[]{ComponentStateFacet.DETERMINATE, ComponentStateFacet.ENABLE, ComponentStateFacet.SELECTION});
        defaultSchemeBundle.registerColorScheme(rolloverSelectedScheme, determinateState, indeterminateState);
        defaultSchemeBundle.registerColorScheme(rolloverSelectedScheme, ColorSchemeAssociationKind.BORDER, determinateState, indeterminateState);
        defaultSchemeBundle.registerColorScheme(disabledScheme, determinateDisabledState, indeterminateDisabledState);
        defaultSchemeBundle.registerColorScheme(disabledScheme, ColorSchemeAssociationKind.BORDER, determinateDisabledState, indeterminateDisabledState);
        ComponentState editable = new ComponentState("editable", new ComponentStateFacet[]{ComponentStateFacet.ENABLE, ComponentStateFacet.EDITABLE}, null);
        ComponentState uneditable = new ComponentState("uneditable", editable, new ComponentStateFacet[]{ComponentStateFacet.ENABLE}, new ComponentStateFacet[]{ComponentStateFacet.EDITABLE});
        defaultSchemeBundle.registerColorScheme(defaultSchemeBundle.getColorScheme(editable), ColorSchemeAssociationKind.FILL, uneditable);
        SubstanceSkin.ColorSchemes kitchenSinkSchemes = SubstanceSkin.getColorSchemes("org/pushingpixels/substance/api/skin/kitchen-sink.colorschemes");
        SubstanceColorScheme highlightColorScheme = kitchenSinkSchemes.get("Moderate Highlight");
        defaultSchemeBundle.registerHighlightColorScheme(highlightColorScheme, new ComponentState[0]);
        this.registerDecorationAreaSchemeBundle(defaultSchemeBundle, DecorationAreaType.NONE);
        CopyMutableColorScheme chrome = new CopyMutableColorScheme("Cerulean Chrome", pressedScheme);
        chrome.setUltraDarkColor(chrome.getExtraLightColor());
        this.registerDecorationAreaSchemeBundle(new SubstanceColorSchemeBundle(pressedScheme, pressedScheme, disabledScheme), chrome, DecorationAreaType.PRIMARY_TITLE_PANE, DecorationAreaType.SECONDARY_TITLE_PANE);
        this.registerAsDecorationArea(enabledScheme, DecorationAreaType.PRIMARY_TITLE_PANE_INACTIVE, DecorationAreaType.SECONDARY_TITLE_PANE_INACTIVE);
        this.registerAsDecorationArea(activeScheme.saturate(-0.75), DecorationAreaType.HEADER, DecorationAreaType.FOOTER, DecorationAreaType.GENERAL);
        this.buttonShaper = new ClassicButtonShaper();
        this.fillPainter = new ClassicFillPainter();
        this.decorationPainter = new ArcDecorationPainter();
        this.highlightPainter = new GlassHighlightPainter();
        this.borderPainter = new GlassBorderPainter();
    }

    @Override
    public String getDisplayName() {
        return NAME;
    }
}

