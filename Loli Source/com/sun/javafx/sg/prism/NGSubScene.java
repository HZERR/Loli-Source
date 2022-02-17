/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.sg.prism.NGCamera;
import com.sun.javafx.sg.prism.NGLightBase;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.prism.CompositeMode;
import com.sun.prism.Graphics;
import com.sun.prism.RTTexture;
import com.sun.prism.RenderTarget;
import com.sun.prism.Texture;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Paint;

public class NGSubScene
extends NGNode {
    private float slWidth;
    private float slHeight;
    private double lastScaledW;
    private double lastScaledH;
    private RTTexture rtt;
    private RTTexture resolveRTT = null;
    private NGNode root = null;
    private boolean renderSG = true;
    private final boolean depthBuffer;
    private final boolean msaa;
    private Paint fillPaint;
    private NGCamera camera;
    private NGLightBase[] lights;
    private boolean isOpaque = false;
    static final double THRESHOLD = 0.00390625;

    public NGSubScene(boolean bl, boolean bl2) {
        this.depthBuffer = bl;
        this.msaa = bl2;
    }

    private NGSubScene() {
        this(false, false);
    }

    public void setRoot(NGNode nGNode) {
        this.root = nGNode;
    }

    public void setFillPaint(Object object) {
        this.fillPaint = (Paint)object;
    }

    public void setCamera(NGCamera nGCamera) {
        this.camera = nGCamera == null ? NGCamera.INSTANCE : nGCamera;
    }

    public void setWidth(float f2) {
        if (this.slWidth != f2) {
            this.slWidth = f2;
            this.geometryChanged();
            this.invalidateRTT();
        }
    }

    public void setHeight(float f2) {
        if (this.slHeight != f2) {
            this.slHeight = f2;
            this.geometryChanged();
            this.invalidateRTT();
        }
    }

    public NGLightBase[] getLights() {
        return this.lights;
    }

    public void setLights(NGLightBase[] arrnGLightBase) {
        this.lights = arrnGLightBase;
    }

    public void markContentDirty() {
        this.visualsChanged();
    }

    @Override
    protected void visualsChanged() {
        this.renderSG = true;
        super.visualsChanged();
    }

    @Override
    protected void geometryChanged() {
        this.renderSG = true;
        super.geometryChanged();
    }

    private void invalidateRTT() {
        if (this.rtt != null) {
            this.rtt.dispose();
            this.rtt = null;
        }
    }

    @Override
    protected boolean hasOverlappingContents() {
        return false;
    }

    private void applyBackgroundFillPaint(Graphics graphics) {
        this.isOpaque = true;
        if (this.fillPaint != null) {
            if (this.fillPaint instanceof Color) {
                Color color = (Color)this.fillPaint;
                this.isOpaque = (double)color.getAlpha() >= 1.0;
                graphics.clear(color);
            } else {
                if (!this.fillPaint.isOpaque()) {
                    graphics.clear();
                    this.isOpaque = false;
                }
                graphics.setPaint(this.fillPaint);
                graphics.fillRect(0.0f, 0.0f, this.rtt.getContentWidth(), this.rtt.getContentHeight());
            }
        } else {
            this.isOpaque = false;
            graphics.clear();
        }
    }

    @Override
    public void renderForcedContent(Graphics graphics) {
        this.root.renderForcedContent(graphics);
    }

    private static double hypot(double d2, double d3, double d4) {
        return Math.sqrt(d2 * d2 + d3 * d3 + d4 * d4);
    }

    @Override
    protected void renderContent(Graphics graphics) {
        if ((double)this.slWidth <= 0.0 || (double)this.slHeight <= 0.0) {
            return;
        }
        BaseTransform baseTransform = graphics.getTransformNoClone();
        double d2 = NGSubScene.hypot(baseTransform.getMxx(), baseTransform.getMyx(), baseTransform.getMzx());
        double d3 = NGSubScene.hypot(baseTransform.getMxy(), baseTransform.getMyy(), baseTransform.getMzy());
        double d4 = (double)this.slWidth * d2;
        double d5 = (double)this.slHeight * d3;
        int n2 = (int)Math.ceil(d4 - 0.00390625);
        int n3 = (int)Math.ceil(d5 - 0.00390625);
        if (Math.max(Math.abs(d4 - this.lastScaledW), Math.abs(d5 - this.lastScaledH)) > 0.00390625) {
            if (this.rtt != null && (n2 != this.rtt.getContentWidth() || n3 != this.rtt.getContentHeight())) {
                this.invalidateRTT();
            }
            this.renderSG = true;
            this.lastScaledW = d4;
            this.lastScaledH = d5;
        }
        if (this.rtt != null) {
            this.rtt.lock();
            if (this.rtt.isSurfaceLost()) {
                this.renderSG = true;
                this.rtt = null;
            }
        }
        if (this.renderSG || !this.root.isClean()) {
            Object object;
            if (this.rtt == null) {
                object = graphics.getResourceFactory();
                this.rtt = object.createRTTexture(n2, n3, Texture.WrapMode.CLAMP_TO_ZERO, this.msaa);
            }
            object = this.rtt.createGraphics();
            object.scale((float)d2, (float)d3);
            object.setLights(this.lights);
            object.setDepthBuffer(this.depthBuffer);
            if (this.camera != null) {
                object.setCamera(this.camera);
            }
            this.applyBackgroundFillPaint((Graphics)object);
            this.root.render((Graphics)object);
            this.root.clearDirtyTree();
            this.renderSG = false;
        }
        if (this.msaa) {
            int n4 = this.rtt.getContentX();
            int n5 = this.rtt.getContentY();
            int n6 = n4 + n2;
            int n7 = n5 + n3;
            if ((this.isOpaque || graphics.getCompositeMode() == CompositeMode.SRC) && NGSubScene.isDirectBlitTransform(baseTransform, d2, d3) && !graphics.isDepthTest()) {
                int n8 = (int)(baseTransform.getMxt() + 0.5);
                int n9 = (int)(baseTransform.getMyt() + 0.5);
                RenderTarget renderTarget = graphics.getRenderTarget();
                int n10 = renderTarget.getContentX() + n8;
                int n11 = renderTarget.getContentY() + n9;
                int n12 = n10 + n2;
                int n13 = n11 + n3;
                int n14 = renderTarget.getContentWidth();
                int n15 = renderTarget.getContentHeight();
                int n16 = n12 > n14 ? n14 - n12 : 0;
                int n17 = n13 > n15 ? n15 - n13 : 0;
                graphics.blit(this.rtt, null, n4, n5, n6 + n16, n7 + n17, n10, n11, n12 + n16, n13 + n17);
            } else {
                if (this.resolveRTT != null && (this.resolveRTT.getContentWidth() < n2 || this.resolveRTT.getContentHeight() < n3)) {
                    this.resolveRTT.dispose();
                    this.resolveRTT = null;
                }
                if (this.resolveRTT != null) {
                    this.resolveRTT.lock();
                    if (this.resolveRTT.isSurfaceLost()) {
                        this.resolveRTT = null;
                    }
                }
                if (this.resolveRTT == null) {
                    this.resolveRTT = graphics.getResourceFactory().createRTTexture(n2, n3, Texture.WrapMode.CLAMP_TO_ZERO, false);
                }
                this.resolveRTT.createGraphics().blit(this.rtt, this.resolveRTT, n4, n5, n6, n7, n4, n5, n6, n7);
                graphics.drawTexture(this.resolveRTT, 0.0f, 0.0f, (float)((double)n2 / d2), (float)((double)n3 / d3), 0.0f, 0.0f, n2, n3);
                this.resolveRTT.unlock();
            }
        } else {
            graphics.drawTexture(this.rtt, 0.0f, 0.0f, (float)((double)n2 / d2), (float)((double)n3 / d3), 0.0f, 0.0f, n2, n3);
        }
        this.rtt.unlock();
    }

    private static boolean isDirectBlitTransform(BaseTransform baseTransform, double d2, double d3) {
        if (d2 == 1.0 && d3 == 1.0) {
            return baseTransform.isTranslateOrIdentity();
        }
        if (!baseTransform.is2D()) {
            return false;
        }
        return baseTransform.getMxx() == d2 && baseTransform.getMxy() == 0.0 && baseTransform.getMyx() == 0.0 && baseTransform.getMyy() == d3;
    }

    public NGCamera getCamera() {
        return this.camera;
    }
}

