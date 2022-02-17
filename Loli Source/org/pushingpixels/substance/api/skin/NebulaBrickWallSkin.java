/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.skin;

import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.colorscheme.OrangeColorScheme;
import org.pushingpixels.substance.api.skin.NebulaSkin;

public class NebulaBrickWallSkin
extends NebulaSkin {
    public static final String NAME = "Nebula Brick Wall";

    public NebulaBrickWallSkin() {
        this.registerAsDecorationArea(new OrangeColorScheme(), DecorationAreaType.PRIMARY_TITLE_PANE, DecorationAreaType.SECONDARY_TITLE_PANE, DecorationAreaType.HEADER);
    }

    @Override
    public String getDisplayName() {
        return NAME;
    }
}

