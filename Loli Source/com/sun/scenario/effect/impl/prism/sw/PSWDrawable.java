/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.prism.sw;

import com.sun.glass.ui.Screen;
import com.sun.prism.Graphics;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.Image;
import com.sun.prism.RTTexture;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.scenario.effect.impl.HeapImage;
import com.sun.scenario.effect.impl.prism.PrDrawable;
import java.nio.IntBuffer;
import java.util.Arrays;

public class PSWDrawable
extends PrDrawable
implements HeapImage {
    private RTTexture rtt;
    private Image image;
    private boolean heapDirty;
    private boolean vramDirty;

    private PSWDrawable(RTTexture rTTexture, boolean bl) {
        super(rTTexture);
        this.rtt = rTTexture;
        this.vramDirty = bl;
    }

    public static PSWDrawable create(RTTexture rTTexture) {
        return new PSWDrawable(rTTexture, true);
    }

    static int getCompatibleWidth(Screen screen, int n2) {
        ResourceFactory resourceFactory = GraphicsPipeline.getPipeline().getResourceFactory(screen);
        return resourceFactory.getRTTWidth(n2, Texture.WrapMode.CLAMP_TO_ZERO);
    }

    static int getCompatibleHeight(Screen screen, int n2) {
        ResourceFactory resourceFactory = GraphicsPipeline.getPipeline().getResourceFactory(screen);
        return resourceFactory.getRTTHeight(n2, Texture.WrapMode.CLAMP_TO_ZERO);
    }

    static PSWDrawable create(Screen screen, int n2, int n3) {
        ResourceFactory resourceFactory = GraphicsPipeline.getPipeline().getResourceFactory(screen);
        RTTexture rTTexture = resourceFactory.createRTTexture(n2, n3, Texture.WrapMode.CLAMP_TO_ZERO);
        return new PSWDrawable(rTTexture, false);
    }

    @Override
    public boolean isLost() {
        return this.rtt == null || this.rtt.isSurfaceLost();
    }

    @Override
    public void flush() {
        if (this.rtt != null) {
            this.rtt.dispose();
            this.rtt = null;
            this.image = null;
        }
    }

    @Override
    public Object getData() {
        return this;
    }

    @Override
    public int getContentWidth() {
        return this.rtt.getContentWidth();
    }

    @Override
    public int getContentHeight() {
        return this.rtt.getContentHeight();
    }

    @Override
    public int getMaxContentWidth() {
        return this.rtt.getMaxContentWidth();
    }

    @Override
    public int getMaxContentHeight() {
        return this.rtt.getMaxContentHeight();
    }

    @Override
    public void setContentWidth(int n2) {
        this.rtt.setContentWidth(n2);
    }

    @Override
    public void setContentHeight(int n2) {
        this.rtt.setContentHeight(n2);
    }

    @Override
    public int getPhysicalWidth() {
        return this.rtt.getContentWidth();
    }

    @Override
    public int getPhysicalHeight() {
        return this.rtt.getContentHeight();
    }

    @Override
    public int getScanlineStride() {
        return this.rtt.getContentWidth();
    }

    @Override
    public int[] getPixelArray() {
        int[] arrn = this.rtt.getPixels();
        if (arrn != null) {
            return arrn;
        }
        if (this.image == null) {
            int n2 = this.rtt.getContentWidth();
            int n3 = this.rtt.getContentHeight();
            arrn = new int[n2 * n3];
            this.image = Image.fromIntArgbPreData(arrn, n2, n3);
        }
        IntBuffer intBuffer = (IntBuffer)this.image.getPixelBuffer();
        if (this.vramDirty) {
            this.rtt.readPixels(intBuffer);
            this.vramDirty = false;
        }
        this.heapDirty = true;
        return intBuffer.array();
    }

    @Override
    public RTTexture getTextureObject() {
        if (this.heapDirty) {
            int n2 = this.rtt.getContentWidth();
            int n3 = this.rtt.getContentHeight();
            Screen screen = this.rtt.getAssociatedScreen();
            ResourceFactory resourceFactory = GraphicsPipeline.getPipeline().getResourceFactory(screen);
            Texture texture = resourceFactory.createTexture(this.image, Texture.Usage.DEFAULT, Texture.WrapMode.CLAMP_TO_EDGE);
            Graphics graphics = this.createGraphics();
            graphics.drawTexture(texture, 0.0f, 0.0f, n2, n3);
            graphics.sync();
            texture.dispose();
            this.heapDirty = false;
        }
        return this.rtt;
    }

    @Override
    public Graphics createGraphics() {
        this.vramDirty = true;
        return this.rtt.createGraphics();
    }

    @Override
    public void clear() {
        Graphics graphics = this.createGraphics();
        graphics.clear();
        if (this.image != null) {
            IntBuffer intBuffer = (IntBuffer)this.image.getPixelBuffer();
            Arrays.fill(intBuffer.array(), 0);
        }
        this.heapDirty = false;
        this.vramDirty = false;
    }
}

