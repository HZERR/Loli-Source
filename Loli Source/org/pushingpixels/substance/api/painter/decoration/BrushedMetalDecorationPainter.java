/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.painter.decoration;

import java.net.URL;
import javax.imageio.ImageIO;
import org.pushingpixels.substance.api.painter.decoration.ImageWrapperDecorationPainter;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;

public class BrushedMetalDecorationPainter
extends ImageWrapperDecorationPainter {
    public static final String DISPLAY_NAME = "Brushed Metal";

    public BrushedMetalDecorationPainter() {
        try {
            ClassLoader cl = SubstanceCoreUtilities.getClassLoaderForResources();
            URL metalUrl = cl.getResource("resource/brushed.gif");
            this.originalTile = ImageIO.read(metalUrl);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    @Override
    public String getDisplayName() {
        return DISPLAY_NAME;
    }
}

