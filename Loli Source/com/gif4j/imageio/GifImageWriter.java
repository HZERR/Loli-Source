/*
 * Decompiled with CFR 0.150.
 */
package com.gif4j.imageio;

import com.gif4j.GifEncoder;
import com.gif4j.imageio.GifImageWriterSpi;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.DataOutput;
import java.io.IOException;
import javax.imageio.IIOImage;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageOutputStream;

public class GifImageWriter
extends ImageWriter {
    protected GifImageWriter(GifImageWriterSpi gifImageWriterSpi) {
        super(gifImageWriterSpi);
    }

    public IIOMetadata getDefaultStreamMetadata(ImageWriteParam imageWriteParam) {
        return null;
    }

    public IIOMetadata getDefaultImageMetadata(ImageTypeSpecifier imageTypeSpecifier, ImageWriteParam imageWriteParam) {
        return null;
    }

    public IIOMetadata convertStreamMetadata(IIOMetadata iIOMetadata, ImageWriteParam imageWriteParam) {
        return null;
    }

    public IIOMetadata convertImageMetadata(IIOMetadata iIOMetadata, ImageTypeSpecifier imageTypeSpecifier, ImageWriteParam imageWriteParam) {
        return null;
    }

    public void setOutput(Object object) {
        super.setOutput(object);
        if (object != null && !(object instanceof ImageOutputStream)) {
            throw new IllegalArgumentException("output not an instance of ImageOutputStream!");
        }
    }

    public void write(IIOMetadata iIOMetadata, IIOImage iIOImage, ImageWriteParam imageWriteParam) throws IOException {
        if (iIOImage == null) {
            throw new IllegalArgumentException("image is null!");
        }
        RenderedImage renderedImage = iIOImage.getRenderedImage();
        if (this.getOutput() == null) {
            throw new IllegalStateException("output not set and is null!");
        }
        if (iIOImage.hasRaster()) {
            throw new UnsupportedOperationException("Doesn't support Raster-based IIOImage(s)");
        }
        if (!(renderedImage instanceof BufferedImage)) {
            throw new IOException("RenderedImage not an instance of BufferedImage!");
        }
        GifEncoder.encode((BufferedImage)renderedImage, (DataOutput)this.getOutput());
    }
}

