/*
 * Decompiled with CFR 0.150.
 */
package com.gif4j.imageio;

import com.gif4j.imageio.GifImageWriter;
import java.io.IOException;
import java.util.Locale;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.spi.ImageWriterSpi;

public class GifImageWriterSpi
extends ImageWriterSpi {
    public GifImageWriterSpi() {
        super("Gif4J Software, http://www.gif4j.com/", "1.0", new String[]{"gif", "GIF"}, new String[]{"gif", "GIF"}, new String[]{"image/gif", "image/x-gif"}, "com.gif4j.imageio.GifImageWriter", STANDARD_OUTPUT_TYPE, null, false, null, null, null, null, false, null, null, null, null);
    }

    public String getDescription(Locale locale) {
        return "GRAPHICS INTERCHANGE FORMAT(sm) Version 89a";
    }

    public ImageWriter createWriterInstance(Object object) throws IOException {
        return new GifImageWriter(this);
    }

    public boolean canEncodeImage(ImageTypeSpecifier imageTypeSpecifier) {
        return true;
    }
}

