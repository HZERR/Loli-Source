/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.painter.decoration;

import org.pushingpixels.substance.api.painter.decoration.ImageWrapperDecorationPainter;
import org.pushingpixels.substance.internal.utils.NoiseFactory;
import org.pushingpixels.substance.internal.utils.SubstanceColorSchemeUtilities;

public class MarbleNoiseDecorationPainter
extends ImageWrapperDecorationPainter {
    public static final String DISPLAY_NAME = "Marble Noise";

    public MarbleNoiseDecorationPainter() {
        this.originalTile = NoiseFactory.getNoiseImage(SubstanceColorSchemeUtilities.METALLIC_SKIN, 400, 400, 0.8, 0.8, false, true, true);
    }

    @Override
    public String getDisplayName() {
        return DISPLAY_NAME;
    }
}

