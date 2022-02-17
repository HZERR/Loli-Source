/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.skin;

import org.pushingpixels.substance.api.ColorSchemeAssociationKind;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceColorSchemeBundle;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.painter.fill.MatteFillPainter;
import org.pushingpixels.substance.api.skin.DustSkin;

public class DustCoffeeSkin
extends DustSkin {
    public static final String NAME = "Dust Coffee";

    public DustCoffeeSkin() {
        SubstanceSkin.ColorSchemes kitchenSinkSchemes = SubstanceSkin.getColorSchemes("org/pushingpixels/substance/api/skin/kitchen-sink.colorschemes");
        SubstanceColorScheme activeScheme = kitchenSinkSchemes.get("Coffee Active");
        SubstanceSkin.ColorSchemes schemes = SubstanceSkin.getColorSchemes("org/pushingpixels/substance/api/skin/dust.colorschemes");
        SubstanceColorScheme enabledScheme = schemes.get("Dust Coffee Enabled");
        SubstanceColorScheme watermarkScheme = schemes.get("Dust Coffee Watermark");
        SubstanceColorSchemeBundle defaultSchemeBundle = new SubstanceColorSchemeBundle(activeScheme, enabledScheme, enabledScheme);
        defaultSchemeBundle.registerColorScheme(enabledScheme, 0.5f, ComponentState.DISABLED_UNSELECTED);
        defaultSchemeBundle.registerColorScheme(activeScheme, 0.5f, ComponentState.DISABLED_SELECTED);
        SubstanceColorScheme borderEnabledScheme = schemes.get("Dust Border Enabled");
        defaultSchemeBundle.registerColorScheme(borderEnabledScheme, ColorSchemeAssociationKind.BORDER, ComponentState.ENABLED, ComponentState.DISABLED_SELECTED, ComponentState.DISABLED_UNSELECTED);
        defaultSchemeBundle.registerColorScheme(activeScheme, ColorSchemeAssociationKind.BORDER, ComponentState.getActiveStates());
        defaultSchemeBundle.registerColorScheme(borderEnabledScheme, ColorSchemeAssociationKind.MARK, new ComponentState[0]);
        SubstanceColorScheme textHighlightScheme = schemes.get("Dust Coffee Text Highlight");
        defaultSchemeBundle.registerColorScheme(textHighlightScheme, ColorSchemeAssociationKind.TEXT_HIGHLIGHT, ComponentState.SELECTED, ComponentState.ROLLOVER_SELECTED);
        defaultSchemeBundle.registerHighlightColorScheme(activeScheme, 0.6f, ComponentState.ROLLOVER_UNSELECTED, ComponentState.ARMED);
        defaultSchemeBundle.registerHighlightColorScheme(activeScheme, 0.8f, ComponentState.SELECTED);
        defaultSchemeBundle.registerHighlightColorScheme(activeScheme, 1.0f, ComponentState.ROLLOVER_SELECTED, ComponentState.ROLLOVER_ARMED);
        this.registerDecorationAreaSchemeBundle(defaultSchemeBundle, watermarkScheme, DecorationAreaType.NONE);
        this.fillPainter = new MatteFillPainter();
    }

    @Override
    public String getDisplayName() {
        return NAME;
    }
}

