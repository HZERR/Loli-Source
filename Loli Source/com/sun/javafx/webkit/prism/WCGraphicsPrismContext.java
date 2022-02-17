/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.webkit.prism;

import com.sun.glass.ui.Screen;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.Metrics;
import com.sun.javafx.font.PGFont;
import com.sun.javafx.geom.Arc2D;
import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.Line2D;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.text.GlyphList;
import com.sun.javafx.scene.text.TextLayout;
import com.sun.javafx.sg.prism.NGImageView;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGPath;
import com.sun.javafx.sg.prism.NGRectangle;
import com.sun.javafx.sg.prism.NGShape;
import com.sun.javafx.sg.prism.NGText;
import com.sun.javafx.text.TextRun;
import com.sun.javafx.webkit.prism.PrismImage;
import com.sun.javafx.webkit.prism.TextUtilities;
import com.sun.javafx.webkit.prism.WCLinearGradient;
import com.sun.javafx.webkit.prism.WCPathImpl;
import com.sun.javafx.webkit.prism.WCRadialGradient;
import com.sun.javafx.webkit.prism.WCStrokeImpl;
import com.sun.prism.BasicStroke;
import com.sun.prism.CompositeMode;
import com.sun.prism.Graphics;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.Image;
import com.sun.prism.MaskTextureGraphics;
import com.sun.prism.PrinterGraphics;
import com.sun.prism.RTTexture;
import com.sun.prism.ReadbackGraphics;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Gradient;
import com.sun.prism.paint.ImagePattern;
import com.sun.prism.paint.Paint;
import com.sun.scenario.effect.Blend;
import com.sun.scenario.effect.Color4f;
import com.sun.scenario.effect.DropShadow;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.prism.PrDrawable;
import com.sun.scenario.effect.impl.prism.PrEffectHelper;
import com.sun.scenario.effect.impl.prism.PrFilterContext;
import com.sun.scenario.effect.impl.prism.PrRenderer;
import com.sun.webkit.graphics.Ref;
import com.sun.webkit.graphics.RenderTheme;
import com.sun.webkit.graphics.ScrollBarTheme;
import com.sun.webkit.graphics.WCFont;
import com.sun.webkit.graphics.WCGradient;
import com.sun.webkit.graphics.WCGraphicsContext;
import com.sun.webkit.graphics.WCIcon;
import com.sun.webkit.graphics.WCImage;
import com.sun.webkit.graphics.WCPath;
import com.sun.webkit.graphics.WCPoint;
import com.sun.webkit.graphics.WCRectangle;
import com.sun.webkit.graphics.WCSize;
import com.sun.webkit.graphics.WCTransform;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

class WCGraphicsPrismContext
extends WCGraphicsContext {
    private static final Logger log = Logger.getLogger(WCGraphicsPrismContext.class.getName());
    private static final boolean DEBUG_DRAW_CLIP_SHAPE = Boolean.valueOf(AccessController.doPrivileged(() -> System.getProperty("com.sun.webkit.debugDrawClipShape", "false")));
    Graphics baseGraphics;
    private BaseTransform baseTransform;
    private final List<ContextState> states = new ArrayList<ContextState>();
    private ContextState state = new ContextState();
    private Graphics cachedGraphics = null;
    private int fontSmoothingType;
    private boolean isRootLayerValid = false;
    private static final BasicStroke focusRingStroke = new BasicStroke(1.1f, 0, 1, 0.0f, new float[]{1.0f}, 0.0f);

    WCGraphicsPrismContext(Graphics graphics) {
        this.state.setClip(graphics.getClipRect());
        this.state.setAlpha(graphics.getExtraAlpha());
        this.baseGraphics = graphics;
        this.initBaseTransform(graphics.getTransformNoClone());
    }

    WCGraphicsPrismContext() {
    }

    public Type type() {
        return Type.PRIMARY;
    }

    final void initBaseTransform(BaseTransform baseTransform) {
        this.baseTransform = new Affine3D(baseTransform);
        this.state.setTransform((Affine3D)this.baseTransform);
    }

    private void resetCachedGraphics() {
        this.cachedGraphics = null;
    }

    @Override
    public Object getPlatformGraphics() {
        return this.getGraphics(false);
    }

    Graphics getGraphics(boolean bl) {
        Object object;
        if (this.cachedGraphics == null) {
            object = this.state.getLayerNoClone();
            this.cachedGraphics = object != null ? ((Layer)object).getGraphics() : this.baseGraphics;
            this.state.apply(this.cachedGraphics);
            if (log.isLoggable(Level.FINE)) {
                log.fine("getPlatformGraphics for " + this + " : " + this.cachedGraphics);
            }
        }
        object = this.cachedGraphics.getClipRectNoClone();
        return bl && object != null && ((Rectangle)object).isEmpty() ? null : this.cachedGraphics;
    }

    @Override
    public void saveState() {
        this.state.markAsRestorePoint();
        this.saveStateInternal();
    }

    private void saveStateInternal() {
        this.states.add(this.state);
        this.state = this.state.clone();
    }

    private void startNewLayer(Layer layer) {
        this.saveStateInternal();
        Rectangle rectangle = this.state.getClipNoClone();
        Affine3D affine3D = new Affine3D(BaseTransform.getTranslateInstance(-rectangle.x, -rectangle.y));
        affine3D.concatenate(this.state.getTransformNoClone());
        rectangle.x = 0;
        rectangle.y = 0;
        Graphics graphics = this.getGraphics(true);
        if (graphics != null && graphics != this.baseGraphics) {
            layer.init(graphics);
        }
        this.state.setTransform(affine3D);
        this.state.setLayer(layer);
        this.resetCachedGraphics();
    }

    private void renderLayer(Layer layer) {
        WCTransform wCTransform = this.getTransform();
        this.setTransform(new WCTransform(1.0, 0.0, 0.0, 1.0, layer.getX(), layer.getY()));
        Graphics graphics = this.getGraphics(true);
        if (graphics != null) {
            layer.render(graphics);
        }
        this.setTransform(wCTransform);
    }

    private void restoreStateInternal() {
        int n2 = this.states.size();
        if (n2 == 0) {
            assert (false) : "Unbalanced restoreState";
            return;
        }
        Layer layer = this.state.getLayerNoClone();
        this.state = this.states.remove(n2 - 1);
        if (layer != this.state.getLayerNoClone()) {
            this.renderLayer(layer);
            layer.dispose();
            if (log.isLoggable(Level.FINE)) {
                log.fine("Popped layer " + layer);
            }
        } else {
            this.resetCachedGraphics();
        }
    }

    @Override
    public void restoreState() {
        log.fine("restoring state");
        do {
            this.restoreStateInternal();
        } while (!this.state.isRestorePoint());
    }

    private void flushAllLayers() {
        if (this.state == null) {
            return;
        }
        if (this.isRootLayerValid) {
            log.fine("FlushAllLayers: root layer is valid, skipping");
            return;
        }
        if (log.isLoggable(Level.FINE)) {
            log.fine("FlushAllLayers");
        }
        ContextState contextState = this.state;
        for (int i2 = this.states.size() - 1; i2 >= 0; --i2) {
            Layer layer = this.state.getLayerNoClone();
            this.state = this.states.get(i2);
            if (layer != this.state.getLayerNoClone()) {
                this.renderLayer(layer);
                continue;
            }
            this.resetCachedGraphics();
        }
        Layer layer = this.state.getLayerNoClone();
        if (layer != null) {
            this.renderLayer(layer);
        }
        this.state = contextState;
        this.isRootLayerValid = true;
    }

    @Override
    public void dispose() {
        if (!this.states.isEmpty()) {
            log.fine("Unbalanced saveState/restoreState");
        }
        for (ContextState contextState : this.states) {
            if (contextState.getLayerNoClone() == null) continue;
            contextState.getLayerNoClone().dispose();
        }
        this.states.clear();
        if (this.state != null && this.state.getLayerNoClone() != null) {
            this.state.getLayerNoClone().dispose();
        }
        this.state = null;
    }

    @Override
    public void setClip(WCPath wCPath, boolean bl) {
        Object object;
        Affine3D affine3D = new Affine3D(this.state.getTransformNoClone());
        wCPath.transform(affine3D.getMxx(), affine3D.getMyx(), affine3D.getMxy(), affine3D.getMyy(), affine3D.getMxt(), affine3D.getMyt());
        if (!bl) {
            object = wCPath.getBounds();
            int n2 = (int)Math.floor(((WCRectangle)object).getX());
            int n3 = (int)Math.floor(((WCRectangle)object).getY());
            int n4 = (int)Math.ceil(((WCRectangle)object).getMaxX()) - n2;
            int n5 = (int)Math.ceil(((WCRectangle)object).getMaxY()) - n3;
            this.state.clip(new Rectangle(n2, n3, n4, n5));
        }
        object = this.state.getClipNoClone();
        if (bl) {
            wCPath.addRect(((Rectangle)object).x, ((Rectangle)object).y, ((Rectangle)object).width, ((Rectangle)object).height);
        }
        wCPath.translate(-((Rectangle)object).x, -((Rectangle)object).y);
        ClipLayer clipLayer = new ClipLayer(this.getGraphics(false), (Rectangle)object, wCPath, this.type() == Type.DEDICATED);
        this.startNewLayer(clipLayer);
        if (log.isLoggable(Level.FINE)) {
            log.fine("setClip(WCPath " + wCPath.getID() + ")");
            log.fine("Pushed layer " + clipLayer);
        }
    }

    private Rectangle transformClip(Rectangle rectangle) {
        if (rectangle == null) {
            return null;
        }
        float[] arrf = new float[]{rectangle.x, rectangle.y, rectangle.x + rectangle.width, rectangle.y, rectangle.x, rectangle.y + rectangle.height, rectangle.x + rectangle.width, rectangle.y + rectangle.height};
        this.state.getTransformNoClone().transform(arrf, 0, arrf, 0, 4);
        float f2 = Math.min(arrf[0], Math.min(arrf[2], Math.min(arrf[4], arrf[6])));
        float f3 = Math.max(arrf[0], Math.max(arrf[2], Math.max(arrf[4], arrf[6])));
        float f4 = Math.min(arrf[1], Math.min(arrf[3], Math.min(arrf[5], arrf[7])));
        float f5 = Math.max(arrf[1], Math.max(arrf[3], Math.max(arrf[5], arrf[7])));
        return new Rectangle(new RectBounds(f2, f4, f3, f5));
    }

    private void setClip(Rectangle rectangle) {
        Affine3D affine3D = this.state.getTransformNoClone();
        if (affine3D.getMxy() == 0.0 && affine3D.getMxz() == 0.0 && affine3D.getMyx() == 0.0 && affine3D.getMyz() == 0.0 && affine3D.getMzx() == 0.0 && affine3D.getMzy() == 0.0) {
            Rectangle rectangle2;
            this.state.clip(this.transformClip(rectangle));
            if (log.isLoggable(Level.FINE)) {
                log.log(Level.FINE, "setClip({0})", rectangle);
            }
            if (DEBUG_DRAW_CLIP_SHAPE && (rectangle2 = this.state.getClipNoClone()) != null && rectangle2.width >= 2 && rectangle2.height >= 2) {
                WCTransform wCTransform = this.getTransform();
                this.setTransform(new WCTransform(1.0, 0.0, 0.0, 1.0, 0.0, 0.0));
                Graphics graphics = this.getGraphics(true);
                if (graphics != null) {
                    float f2 = (float)Math.random();
                    graphics.setPaint(new Color(f2, 1.0f - f2, 0.5f, 0.1f));
                    graphics.setStroke(new BasicStroke());
                    graphics.fillRect(rectangle2.x, rectangle2.y, rectangle2.width, rectangle2.height);
                    graphics.setPaint(new Color(1.0f - f2, f2, 0.5f, 1.0f));
                    graphics.drawRect(rectangle2.x, rectangle2.y, rectangle2.width, rectangle2.height);
                }
                this.setTransform(wCTransform);
                this.state.clip(new Rectangle(rectangle2.x + 1, rectangle2.y + 1, rectangle2.width - 2, rectangle2.height - 2));
            }
            if (this.cachedGraphics != null) {
                this.cachedGraphics.setClipRect(this.state.getClipNoClone());
            }
        } else {
            WCPathImpl wCPathImpl = new WCPathImpl();
            ((WCPath)wCPathImpl).addRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
            this.setClip(wCPathImpl, false);
        }
    }

    @Override
    public void setClip(int n2, int n3, int n4, int n5) {
        this.setClip(new Rectangle(n2, n3, n4, n5));
    }

    @Override
    public void setClip(WCRectangle wCRectangle) {
        this.setClip(new Rectangle((int)wCRectangle.getX(), (int)wCRectangle.getY(), (int)wCRectangle.getWidth(), (int)wCRectangle.getHeight()));
    }

    @Override
    public WCRectangle getClip() {
        Rectangle rectangle = this.state.getClipNoClone();
        return rectangle == null ? null : new WCRectangle(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    protected Rectangle getClipRectNoClone() {
        return this.state.getClipNoClone();
    }

    protected Affine3D getTransformNoClone() {
        return this.state.getTransformNoClone();
    }

    @Override
    public void translate(float f2, float f3) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "translate({0},{1})", new Object[]{Float.valueOf(f2), Float.valueOf(f3)});
        }
        this.state.translate(f2, f3);
        if (this.cachedGraphics != null) {
            this.cachedGraphics.translate(f2, f3);
        }
    }

    @Override
    public void scale(float f2, float f3) {
        if (log.isLoggable(Level.FINE)) {
            log.fine("scale(" + f2 + " " + f3 + ")");
        }
        this.state.scale(f2, f3);
        if (this.cachedGraphics != null) {
            this.cachedGraphics.scale(f2, f3);
        }
    }

    @Override
    public void rotate(float f2) {
        if (log.isLoggable(Level.FINE)) {
            log.fine("rotate(" + f2 + ")");
        }
        this.state.rotate(f2);
        if (this.cachedGraphics != null) {
            this.cachedGraphics.setTransform(this.state.getTransformNoClone());
        }
    }

    protected boolean shouldRenderRect(float f2, float f3, float f4, float f5, DropShadow dropShadow, BasicStroke basicStroke) {
        return true;
    }

    protected boolean shouldRenderShape(Shape shape, DropShadow dropShadow, BasicStroke basicStroke) {
        return true;
    }

    protected boolean shouldCalculateIntersection() {
        return false;
    }

    @Override
    public void fillRect(final float f2, final float f3, final float f4, final float f5, final Integer n2) {
        if (log.isLoggable(Level.FINE)) {
            String string = n2 != null ? "fillRect(%f, %f, %f, %f, 0x%x)" : "fillRect(%f, %f, %f, %f, null)";
            log.fine(String.format(string, Float.valueOf(f2), Float.valueOf(f3), Float.valueOf(f4), Float.valueOf(f5), n2));
        }
        if (!this.shouldRenderRect(f2, f3, f4, f5, this.state.getShadowNoClone(), null)) {
            return;
        }
        new Composite(){

            @Override
            void doPaint(Graphics graphics) {
                Paint paint = n2 != null ? WCGraphicsPrismContext.createColor(n2) : WCGraphicsPrismContext.this.state.getPaintNoClone();
                DropShadow dropShadow = WCGraphicsPrismContext.this.state.getShadowNoClone();
                if (dropShadow != null) {
                    NGRectangle nGRectangle = new NGRectangle();
                    nGRectangle.updateRectangle(f2, f3, f4, f5, 0.0f, 0.0f);
                    WCGraphicsPrismContext.this.render(graphics, dropShadow, paint, null, nGRectangle);
                } else {
                    graphics.setPaint(paint);
                    graphics.fillRect(f2, f3, f4, f5);
                }
            }
        }.paint();
    }

    @Override
    public void fillRoundedRect(final float f2, final float f3, final float f4, final float f5, final float f6, final float f7, final float f8, final float f9, final float f10, final float f11, final float f12, final float f13, final int n2) {
        if (log.isLoggable(Level.FINE)) {
            log.fine(String.format("fillRoundedRect(%f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, 0x%x)", Float.valueOf(f2), Float.valueOf(f3), Float.valueOf(f4), Float.valueOf(f5), Float.valueOf(f6), Float.valueOf(f7), Float.valueOf(f8), Float.valueOf(f9), Float.valueOf(f10), Float.valueOf(f11), Float.valueOf(f12), Float.valueOf(f13), n2));
        }
        if (!this.shouldRenderRect(f2, f3, f4, f5, this.state.getShadowNoClone(), null)) {
            return;
        }
        new Composite(){

            @Override
            void doPaint(Graphics graphics) {
                float f22 = (f6 + f8 + f10 + f12) / 2.0f;
                float f32 = (f7 + f9 + f11 + f13) / 2.0f;
                Color color = WCGraphicsPrismContext.createColor(n2);
                DropShadow dropShadow = WCGraphicsPrismContext.this.state.getShadowNoClone();
                if (dropShadow != null) {
                    NGRectangle nGRectangle = new NGRectangle();
                    nGRectangle.updateRectangle(f2, f3, f4, f5, f22, f32);
                    WCGraphicsPrismContext.this.render(graphics, dropShadow, color, null, nGRectangle);
                } else {
                    graphics.setPaint(color);
                    graphics.fillRoundRect(f2, f3, f4, f5, f22, f32);
                }
            }
        }.paint();
    }

    @Override
    public void clearRect(final float f2, final float f3, final float f4, final float f5) {
        if (log.isLoggable(Level.FINE)) {
            log.fine(String.format("clearRect(%f, %f, %f, %f)", Float.valueOf(f2), Float.valueOf(f3), Float.valueOf(f4), Float.valueOf(f5)));
        }
        if (this.shouldCalculateIntersection()) {
            return;
        }
        new Composite(){

            @Override
            void doPaint(Graphics graphics) {
                graphics.clearQuad(f2, f3, f2 + f4, f3 + f5);
            }
        }.paint();
    }

    @Override
    public void setFillColor(int n2) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, String.format("setFillColor(0x%x)", n2));
        }
        this.state.setPaint(WCGraphicsPrismContext.createColor(n2));
    }

    @Override
    public void setFillGradient(WCGradient wCGradient) {
        if (log.isLoggable(Level.FINE)) {
            log.fine("setFillGradient(" + wCGradient + ")");
        }
        this.state.setPaint((Gradient)wCGradient.getPlatformGradient());
    }

    @Override
    public void setTextMode(boolean bl, boolean bl2, boolean bl3) {
        if (log.isLoggable(Level.FINE)) {
            log.fine("setTextMode(fill:" + bl + ",stroke:" + bl2 + ",clip:" + bl3 + ")");
        }
        this.state.setTextMode(bl, bl2, bl3);
    }

    @Override
    public void setFontSmoothingType(int n2) {
        this.fontSmoothingType = n2;
    }

    @Override
    public int getFontSmoothingType() {
        return this.fontSmoothingType;
    }

    @Override
    public void setStrokeStyle(int n2) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "setStrokeStyle({0})", n2);
        }
        this.state.getStrokeNoClone().setStyle(n2);
    }

    @Override
    public void setStrokeColor(int n2) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, String.format("setStrokeColor(0x%x)", n2));
        }
        this.state.getStrokeNoClone().setPaint(WCGraphicsPrismContext.createColor(n2));
    }

    @Override
    public void setStrokeWidth(float f2) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "setStrokeWidth({0})", new Object[]{Float.valueOf(f2)});
        }
        this.state.getStrokeNoClone().setThickness(f2);
    }

    @Override
    public void setStrokeGradient(WCGradient wCGradient) {
        if (log.isLoggable(Level.FINE)) {
            log.fine("setStrokeGradient(" + wCGradient + ")");
        }
        this.state.getStrokeNoClone().setPaint((Gradient)wCGradient.getPlatformGradient());
    }

    @Override
    public void setLineDash(float f2, float ... arrf) {
        int n2;
        if (log.isLoggable(Level.FINE)) {
            StringBuilder stringBuilder = new StringBuilder("[");
            for (n2 = 0; n2 < arrf.length; ++n2) {
                stringBuilder.append(arrf[n2]).append(',');
            }
            stringBuilder.append(']');
            log.log(Level.FINE, "setLineDash({0},{1}", new Object[]{Float.valueOf(f2), stringBuilder});
        }
        this.state.getStrokeNoClone().setDashOffset(f2);
        if (arrf != null) {
            boolean bl = true;
            for (n2 = 0; n2 < arrf.length; ++n2) {
                if (arrf[n2] == 0.0f) continue;
                bl = false;
                break;
            }
            if (bl) {
                arrf = null;
            }
        }
        this.state.getStrokeNoClone().setDashSizes(arrf);
    }

    @Override
    public void setLineCap(int n2) {
        if (log.isLoggable(Level.FINE)) {
            log.fine("setLineCap(" + n2 + ")");
        }
        this.state.getStrokeNoClone().setLineCap(n2);
    }

    @Override
    public void setLineJoin(int n2) {
        if (log.isLoggable(Level.FINE)) {
            log.fine("setLineJoin(" + n2 + ")");
        }
        this.state.getStrokeNoClone().setLineJoin(n2);
    }

    @Override
    public void setMiterLimit(float f2) {
        if (log.isLoggable(Level.FINE)) {
            log.fine("setMiterLimit(" + f2 + ")");
        }
        this.state.getStrokeNoClone().setMiterLimit(f2);
    }

    @Override
    public void setShadow(float f2, float f3, float f4, int n2) {
        if (log.isLoggable(Level.FINE)) {
            String string = "setShadow(%f, %f, %f, 0x%x)";
            log.fine(String.format(string, Float.valueOf(f2), Float.valueOf(f3), Float.valueOf(f4), n2));
        }
        this.state.setShadow(this.createShadow(f2, f3, f4, n2));
    }

    @Override
    public void drawPolygon(final WCPath wCPath, boolean bl) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "drawPolygon({0})", new Object[]{bl});
        }
        if (!this.shouldRenderShape(((WCPathImpl)wCPath).getPlatformPath(), null, this.state.getStrokeNoClone().getPlatformStroke())) {
            return;
        }
        new Composite(){

            @Override
            void doPaint(Graphics graphics) {
                Path2D path2D = (Path2D)wCPath.getPlatformPath();
                graphics.setPaint(WCGraphicsPrismContext.this.state.getPaintNoClone());
                graphics.fill(path2D);
                if (WCGraphicsPrismContext.this.state.getStrokeNoClone().apply(graphics)) {
                    graphics.draw(path2D);
                }
            }
        }.paint();
    }

    @Override
    public void drawLine(final int n2, final int n3, final int n4, final int n5) {
        Line2D line2D;
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "drawLine({0}, {1}, {2}, {3})", new Object[]{n2, n3, n4, n5});
        }
        if (!this.shouldRenderShape(line2D = new Line2D(n2, n3, n4, n5), null, this.state.getStrokeNoClone().getPlatformStroke())) {
            return;
        }
        new Composite(){

            @Override
            void doPaint(Graphics graphics) {
                if (WCGraphicsPrismContext.this.state.getStrokeNoClone().apply(graphics)) {
                    graphics.drawLine(n2, n3, n4, n5);
                }
            }
        }.paint();
    }

    @Override
    public void drawPattern(final WCImage wCImage, final WCRectangle wCRectangle, final WCTransform wCTransform, final WCPoint wCPoint, final WCRectangle wCRectangle2) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "drawPattern({0}, {1}, {2}, {3})", new Object[]{wCRectangle2.getIntX(), wCRectangle2.getIntY(), wCRectangle2.getIntWidth(), wCRectangle2.getIntHeight()});
        }
        if (!this.shouldRenderRect(wCRectangle2.getX(), wCRectangle2.getY(), wCRectangle2.getWidth(), wCRectangle2.getHeight(), null, null)) {
            return;
        }
        if (wCImage != null) {
            new Composite(){

                @Override
                void doPaint(Graphics graphics) {
                    float f2 = wCPoint.getX() + wCRectangle.getX() * (float)wCTransform.getMatrix()[0];
                    float f3 = wCPoint.getY() + wCRectangle.getY() * (float)wCTransform.getMatrix()[3];
                    float f4 = wCRectangle.getWidth() * (float)wCTransform.getMatrix()[0];
                    float f5 = wCRectangle.getHeight() * (float)wCTransform.getMatrix()[3];
                    Image image = ((PrismImage)wCImage).getImage();
                    if (!wCRectangle.contains(new WCRectangle(0.0f, 0.0f, wCImage.getWidth(), wCImage.getHeight()))) {
                        image = image.createSubImage(wCRectangle.getIntX(), wCRectangle.getIntY(), (int)Math.ceil(wCRectangle.getWidth()), (int)Math.ceil(wCRectangle.getHeight()));
                    }
                    graphics.setPaint(new ImagePattern(image, f2, f3, f4, f5, false, false));
                    graphics.fillRect(wCRectangle2.getX(), wCRectangle2.getY(), wCRectangle2.getWidth(), wCRectangle2.getHeight());
                }
            }.paint();
        }
    }

    @Override
    public void drawImage(final WCImage wCImage, final float f2, final float f3, final float f4, final float f5, final float f6, final float f7, final float f8, final float f9) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "drawImage(img, dst({0},{1},{2},{3}), src({4},{5},{6},{7}))", new Object[]{Float.valueOf(f2), Float.valueOf(f3), Float.valueOf(f4), Float.valueOf(f5), Float.valueOf(f6), Float.valueOf(f7), Float.valueOf(f8), Float.valueOf(f9)});
        }
        if (!this.shouldRenderRect(f2, f3, f4, f5, this.state.getShadowNoClone(), null)) {
            return;
        }
        if (wCImage instanceof PrismImage) {
            new Composite(){

                @Override
                void doPaint(Graphics graphics) {
                    PrismImage prismImage = (PrismImage)wCImage;
                    DropShadow dropShadow = WCGraphicsPrismContext.this.state.getShadowNoClone();
                    if (dropShadow != null) {
                        NGImageView nGImageView = new NGImageView();
                        nGImageView.setImage(prismImage.getImage());
                        nGImageView.setX(f2);
                        nGImageView.setY(f3);
                        nGImageView.setViewport(f6, f7, f8, f9, f4, f5);
                        nGImageView.setContentBounds(new RectBounds(f2, f3, f2 + f4, f3 + f5));
                        WCGraphicsPrismContext.this.render(graphics, dropShadow, null, null, nGImageView);
                    } else {
                        prismImage.draw(graphics, (int)f2, (int)f3, (int)(f2 + f4), (int)(f3 + f5), (int)f6, (int)f7, (int)(f6 + f8), (int)(f7 + f9));
                    }
                }
            }.paint();
        }
    }

    @Override
    public void drawBitmapImage(final ByteBuffer byteBuffer, final int n2, final int n3, final int n4, final int n5) {
        if (!this.shouldRenderRect(n2, n3, n4, n5, null, null)) {
            return;
        }
        new Composite(){

            @Override
            void doPaint(Graphics graphics) {
                byteBuffer.order(ByteOrder.nativeOrder());
                Image image = Image.fromByteBgraPreData(byteBuffer, n4, n5);
                ResourceFactory resourceFactory = graphics.getResourceFactory();
                Texture texture = resourceFactory.createTexture(image, Texture.Usage.STATIC, Texture.WrapMode.REPEAT);
                graphics.drawTexture(texture, n2, n3, n2 + n4, n3 + n5, 0.0f, 0.0f, n4, n5);
                texture.dispose();
            }
        }.paint();
    }

    @Override
    public void drawIcon(WCIcon wCIcon, int n2, int n3) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "UNIMPLEMENTED drawIcon ({0}, {1})", new Object[]{n2, n3});
        }
    }

    @Override
    public void drawRect(final int n2, final int n3, final int n4, final int n5) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "drawRect({0}, {1}, {2}, {3})", new Object[]{n2, n3, n4, n5});
        }
        if (!this.shouldRenderRect(n2, n3, n4, n5, null, this.state.getStrokeNoClone().getPlatformStroke())) {
            return;
        }
        new Composite(){

            @Override
            void doPaint(Graphics graphics) {
                Paint paint = WCGraphicsPrismContext.this.state.getPaintNoClone();
                if (paint != null && paint.isOpaque()) {
                    graphics.setPaint(paint);
                    graphics.fillRect(n2, n3, n4, n5);
                }
                if (WCGraphicsPrismContext.this.state.getStrokeNoClone().apply(graphics)) {
                    graphics.drawRect(n2, n3, n4, n5);
                }
            }
        }.paint();
    }

    @Override
    public void drawString(WCFont wCFont, int[] arrn, float[] arrf, final float f2, final float f3) {
        if (log.isLoggable(Level.FINE)) {
            log.fine(String.format("Drawing %d glyphs @(%.1f, %.1f)", arrn.length, Float.valueOf(f2), Float.valueOf(f3)));
        }
        final PGFont pGFont = (PGFont)wCFont.getPlatformFont();
        final TextRun textRun = TextUtilities.createGlyphList(arrn, arrf, f2, f3);
        final DropShadow dropShadow = this.state.getShadowNoClone();
        final BasicStroke basicStroke = this.state.isTextStroke() ? this.state.getStrokeNoClone().getPlatformStroke() : null;
        final FontStrike fontStrike = pGFont.getStrike(this.getTransformNoClone(), this.getFontSmoothingType());
        if (this.shouldCalculateIntersection()) {
            Metrics metrics = fontStrike.getMetrics();
            textRun.setMetrics(metrics.getAscent(), metrics.getDescent(), metrics.getLineGap());
            if (!this.shouldRenderRect(f2, f3, textRun.getWidth(), textRun.getHeight(), dropShadow, basicStroke)) {
                return;
            }
        }
        new Composite(){

            @Override
            void doPaint(Graphics graphics) {
                Paint paint;
                Paint paint2 = paint = WCGraphicsPrismContext.this.state.isTextFill() ? WCGraphicsPrismContext.this.state.getPaintNoClone() : null;
                if (dropShadow != null) {
                    NGText nGText = new NGText();
                    nGText.setGlyphs(new GlyphList[]{textRun});
                    nGText.setFont(pGFont);
                    nGText.setFontSmoothingType(WCGraphicsPrismContext.this.fontSmoothingType);
                    WCGraphicsPrismContext.this.render(graphics, dropShadow, paint, basicStroke, nGText);
                } else {
                    if (paint != null) {
                        graphics.setPaint(paint);
                        graphics.drawString(textRun, fontStrike, f2, f3, null, 0, 0);
                    }
                    if (basicStroke != null && (paint = (Paint)WCGraphicsPrismContext.this.state.getStrokeNoClone().getPaint()) != null) {
                        graphics.setPaint(paint);
                        graphics.setStroke(basicStroke);
                        graphics.draw(fontStrike.getOutline(textRun, BaseTransform.getTranslateInstance(f2, f3)));
                    }
                }
            }
        }.paint();
    }

    @Override
    public void drawString(WCFont wCFont, String string, boolean bl, int n2, int n3, float f2, float f3) {
        GlyphList[] arrglyphList;
        if (log.isLoggable(Level.FINE)) {
            log.fine(String.format("str='%s' (length=%d), from=%d, to=%d, rtl=%b, @(%.1f, %.1f)", string, string.length(), n2, n3, bl, Float.valueOf(f2), Float.valueOf(f3)));
        }
        TextLayout textLayout = TextUtilities.createLayout(string.substring(n2, n3), wCFont.getPlatformFont());
        int n4 = 0;
        for (GlyphList glyphList : arrglyphList = textLayout.getRuns()) {
            n4 += glyphList.getGlyphCount();
        }
        int[] arrn = new int[n4];
        float[] arrf = new float[n4];
        n4 = 0;
        for (GlyphList glyphList : textLayout.getRuns()) {
            int n5 = glyphList.getGlyphCount();
            for (int i2 = 0; i2 < n5; ++i2) {
                arrn[n4] = glyphList.getGlyphCode(i2);
                arrf[n4] = glyphList.getPosX(i2 + 1) - glyphList.getPosX(i2);
                ++n4;
            }
        }
        f2 = bl ? (f2 += TextUtilities.getLayoutWidth(string.substring(n2), wCFont.getPlatformFont()) - textLayout.getBounds().getWidth()) : (f2 += TextUtilities.getLayoutWidth(string.substring(0, n2), wCFont.getPlatformFont()));
        this.drawString(wCFont, arrn, arrf, f2, f3);
    }

    @Override
    public void setComposite(int n2) {
        log.log(Level.FINE, "setComposite({0})", n2);
        this.state.setCompositeOperation(n2);
    }

    @Override
    public void drawEllipse(final int n2, final int n3, final int n4, final int n5) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "drawEllipse({0}, {1}, {2}, {3})", new Object[]{n2, n3, n4, n5});
        }
        if (!this.shouldRenderRect(n2, n3, n4, n5, null, this.state.getStrokeNoClone().getPlatformStroke())) {
            return;
        }
        new Composite(){

            @Override
            void doPaint(Graphics graphics) {
                graphics.setPaint(WCGraphicsPrismContext.this.state.getPaintNoClone());
                graphics.fillEllipse(n2, n3, n4, n5);
                if (WCGraphicsPrismContext.this.state.getStrokeNoClone().apply(graphics)) {
                    graphics.drawEllipse(n2, n3, n4, n5);
                }
            }
        }.paint();
    }

    @Override
    public void drawFocusRing(final int n2, final int n3, final int n4, final int n5, final int n6) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, String.format("drawFocusRing: %d, %d, %d, %d, 0x%x", n2, n3, n4, n5, n6));
        }
        if (!this.shouldRenderRect(n2, n3, n4, n5, null, focusRingStroke)) {
            return;
        }
        new Composite(){

            @Override
            void doPaint(Graphics graphics) {
                graphics.setPaint(WCGraphicsPrismContext.createColor(n6));
                BasicStroke basicStroke = graphics.getStroke();
                graphics.setStroke(focusRingStroke);
                graphics.drawRoundRect(n2, n3, n4, n5, 4.0f, 4.0f);
                graphics.setStroke(basicStroke);
            }
        }.paint();
    }

    @Override
    public void setAlpha(float f2) {
        log.log(Level.FINE, "setAlpha({0})", Float.valueOf(f2));
        this.state.setAlpha(f2);
        if (null != this.cachedGraphics) {
            this.cachedGraphics.setExtraAlpha(this.state.getAlpha());
        }
    }

    @Override
    public float getAlpha() {
        return this.state.getAlpha();
    }

    @Override
    public void beginTransparencyLayer(float f2) {
        TransparencyLayer transparencyLayer = new TransparencyLayer(this.getGraphics(false), this.state.getClipNoClone(), f2);
        if (log.isLoggable(Level.FINE)) {
            log.fine(String.format("beginTransparencyLayer(%s)", transparencyLayer));
        }
        this.state.markAsRestorePoint();
        this.startNewLayer(transparencyLayer);
    }

    @Override
    public void endTransparencyLayer() {
        if (log.isLoggable(Level.FINE)) {
            log.fine(String.format("endTransparencyLayer(%s)", this.state.getLayerNoClone()));
        }
        this.restoreState();
    }

    @Override
    public void drawWidget(final RenderTheme renderTheme, final Ref ref, final int n2, final int n3) {
        WCSize wCSize = renderTheme.getWidgetSize(ref);
        if (!this.shouldRenderRect(n2, n3, wCSize.getWidth(), wCSize.getHeight(), null, null)) {
            return;
        }
        new Composite(){

            @Override
            void doPaint(Graphics graphics) {
                renderTheme.drawWidget(WCGraphicsPrismContext.this, ref, n2, n3);
            }
        }.paint();
    }

    @Override
    public void drawScrollbar(ScrollBarTheme scrollBarTheme, Ref ref, int n2, int n3, int n4, int n5) {
        scrollBarTheme.paint(this, ref, n2, n3, n4, n5);
    }

    private static Rectangle intersect(Rectangle rectangle, Rectangle rectangle2) {
        if (rectangle == null) {
            return rectangle2;
        }
        RectBounds rectBounds = rectangle.toRectBounds();
        rectBounds.intersectWith(rectangle2);
        rectangle.setBounds(rectBounds);
        return rectangle;
    }

    static Color createColor(int n2) {
        float f2 = (float)(0xFF & n2 >> 24) / 255.0f;
        float f3 = (float)(0xFF & n2 >> 16) / 255.0f;
        float f4 = (float)(0xFF & n2 >> 8) / 255.0f;
        float f5 = (float)(0xFF & n2) / 255.0f;
        return new Color(f3, f4, f5, f2);
    }

    private static Color4f createColor4f(int n2) {
        float f2 = (float)(0xFF & n2 >> 24) / 255.0f;
        float f3 = (float)(0xFF & n2 >> 16) / 255.0f;
        float f4 = (float)(0xFF & n2 >> 8) / 255.0f;
        float f5 = (float)(0xFF & n2) / 255.0f;
        return new Color4f(f3, f4, f5, f2);
    }

    private DropShadow createShadow(float f2, float f3, float f4, int n2) {
        if (f2 == 0.0f && f3 == 0.0f && f4 == 0.0f) {
            return null;
        }
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX((int)f2);
        dropShadow.setOffsetY((int)f3);
        dropShadow.setRadius(f4 < 0.0f ? 0.0f : (f4 > 127.0f ? 127.0f : f4));
        dropShadow.setColor(WCGraphicsPrismContext.createColor4f(n2));
        return dropShadow;
    }

    private void render(Graphics graphics, Effect effect, Paint paint, BasicStroke basicStroke, NGNode nGNode) {
        if (nGNode instanceof NGShape) {
            NGShape nGShape = (NGShape)nGNode;
            Shape shape = nGShape.getShape();
            Paint paint2 = (Paint)this.state.getStrokeNoClone().getPaint();
            if (basicStroke != null && paint2 != null) {
                shape = basicStroke.createStrokedShape(shape);
                nGShape.setDrawStroke(basicStroke);
                nGShape.setDrawPaint(paint2);
                nGShape.setMode(paint == null ? NGShape.Mode.STROKE : NGShape.Mode.STROKE_FILL);
            } else {
                nGShape.setMode(paint == null ? NGShape.Mode.EMPTY : NGShape.Mode.FILL);
            }
            nGShape.setFillPaint(paint);
            nGShape.setContentBounds(shape.getBounds());
        }
        boolean bl = graphics.hasPreCullingBits();
        graphics.setHasPreCullingBits(false);
        nGNode.setEffect(effect);
        nGNode.render(graphics);
        graphics.setHasPreCullingBits(bl);
    }

    private static FilterContext getFilterContext(Graphics graphics) {
        Screen screen = graphics.getAssociatedScreen();
        if (screen == null) {
            ResourceFactory resourceFactory = graphics.getResourceFactory();
            return PrFilterContext.getPrinterContext(resourceFactory);
        }
        return PrFilterContext.getInstance(screen);
    }

    @Override
    public void strokeArc(int n2, int n3, int n4, int n5, int n6, int n7) {
        if (log.isLoggable(Level.FINE)) {
            log.fine(String.format("strokeArc(%d, %d, %d, %d, %d, %d)", n2, n3, n4, n5, n6, n7));
        }
        final Arc2D arc2D = new Arc2D(n2, n3, n4, n5, n6, n7, 0);
        if (this.state.getStrokeNoClone().isApplicable() && !this.shouldRenderShape(arc2D, null, this.state.getStrokeNoClone().getPlatformStroke())) {
            return;
        }
        new Composite(){

            @Override
            void doPaint(Graphics graphics) {
                if (WCGraphicsPrismContext.this.state.getStrokeNoClone().apply(graphics)) {
                    graphics.draw(arc2D);
                }
            }
        }.paint();
    }

    @Override
    public WCImage getImage() {
        return null;
    }

    @Override
    public void strokeRect(final float f2, final float f3, final float f4, final float f5, float f6) {
        BasicStroke basicStroke;
        if (log.isLoggable(Level.FINE)) {
            log.fine(String.format("strokeRect_FFFFF(%f, %f, %f, %f, %f)", Float.valueOf(f2), Float.valueOf(f3), Float.valueOf(f4), Float.valueOf(f5), Float.valueOf(f6)));
        }
        if (!this.shouldRenderRect(f2, f3, f4, f5, null, basicStroke = new BasicStroke(f6, 0, 0, Math.max(1.0f, f6)))) {
            return;
        }
        new Composite(){

            @Override
            void doPaint(Graphics graphics) {
                graphics.setStroke(basicStroke);
                Paint paint = (Paint)WCGraphicsPrismContext.this.state.getStrokeNoClone().getPaint();
                if (paint == null) {
                    paint = WCGraphicsPrismContext.this.state.getPaintNoClone();
                }
                graphics.setPaint(paint);
                graphics.drawRect(f2, f3, f4, f5);
            }
        }.paint();
    }

    @Override
    public void strokePath(WCPath wCPath) {
        log.fine("strokePath");
        if (wCPath != null) {
            final BasicStroke basicStroke = this.state.getStrokeNoClone().getPlatformStroke();
            final DropShadow dropShadow = this.state.getShadowNoClone();
            final Path2D path2D = (Path2D)wCPath.getPlatformPath();
            if (basicStroke == null && dropShadow == null || !this.shouldRenderShape(path2D, dropShadow, basicStroke)) {
                return;
            }
            new Composite(){

                @Override
                void doPaint(Graphics graphics) {
                    if (dropShadow != null) {
                        NGPath nGPath = new NGPath();
                        nGPath.updateWithPath2d(path2D);
                        WCGraphicsPrismContext.this.render(graphics, dropShadow, null, basicStroke, nGPath);
                    } else if (basicStroke != null) {
                        Paint paint = (Paint)WCGraphicsPrismContext.this.state.getStrokeNoClone().getPaint();
                        if (paint == null) {
                            paint = WCGraphicsPrismContext.this.state.getPaintNoClone();
                        }
                        graphics.setPaint(paint);
                        graphics.setStroke(basicStroke);
                        graphics.draw(path2D);
                    }
                }
            }.paint();
        }
    }

    @Override
    public void fillPath(final WCPath wCPath) {
        log.fine("fillPath");
        if (wCPath != null) {
            if (!this.shouldRenderShape(((WCPathImpl)wCPath).getPlatformPath(), this.state.getShadowNoClone(), null)) {
                return;
            }
            new Composite(){

                @Override
                void doPaint(Graphics graphics) {
                    Path2D path2D = (Path2D)wCPath.getPlatformPath();
                    Paint paint = WCGraphicsPrismContext.this.state.getPaintNoClone();
                    DropShadow dropShadow = WCGraphicsPrismContext.this.state.getShadowNoClone();
                    if (dropShadow != null) {
                        NGPath nGPath = new NGPath();
                        nGPath.updateWithPath2d(path2D);
                        WCGraphicsPrismContext.this.render(graphics, dropShadow, paint, null, nGPath);
                    } else {
                        graphics.setPaint(paint);
                        graphics.fill(path2D);
                    }
                }
            }.paint();
        }
    }

    @Override
    public void setTransform(WCTransform wCTransform) {
        double[] arrd = wCTransform.getMatrix();
        Affine3D affine3D = new Affine3D(new Affine2D(arrd[0], arrd[1], arrd[2], arrd[3], arrd[4], arrd[5]));
        if (this.state.getLayerNoClone() == null) {
            affine3D.preConcatenate(this.baseTransform);
        }
        this.state.setTransform(affine3D);
        this.resetCachedGraphics();
    }

    @Override
    public WCTransform getTransform() {
        Affine3D affine3D = this.state.getTransformNoClone();
        return new WCTransform(affine3D.getMxx(), affine3D.getMyx(), affine3D.getMxy(), affine3D.getMyy(), affine3D.getMxt(), affine3D.getMyt());
    }

    @Override
    public void concatTransform(WCTransform wCTransform) {
        double[] arrd = wCTransform.getMatrix();
        Affine3D affine3D = new Affine3D(new Affine2D(arrd[0], arrd[1], arrd[2], arrd[3], arrd[4], arrd[5]));
        this.state.concatTransform(affine3D);
        this.resetCachedGraphics();
    }

    @Override
    public void flush() {
        this.flushAllLayers();
    }

    @Override
    public WCGradient createLinearGradient(WCPoint wCPoint, WCPoint wCPoint2) {
        return new WCLinearGradient(wCPoint, wCPoint2);
    }

    @Override
    public WCGradient createRadialGradient(WCPoint wCPoint, float f2, WCPoint wCPoint2, float f3) {
        return new WCRadialGradient(wCPoint, f2, wCPoint2, f3);
    }

    private static final class PassThrough
    extends Effect {
        private final PrDrawable img;
        private final int width;
        private final int height;

        private PassThrough(PrDrawable prDrawable, int n2, int n3) {
            this.img = prDrawable;
            this.width = n2;
            this.height = n3;
        }

        @Override
        public ImageData filter(FilterContext filterContext, BaseTransform baseTransform, Rectangle rectangle, Object object, Effect effect) {
            this.img.lock();
            ImageData imageData = new ImageData(filterContext, this.img, new Rectangle((int)baseTransform.getMxt(), (int)baseTransform.getMyt(), this.width, this.height));
            imageData.setReusable(true);
            return imageData;
        }

        @Override
        public RectBounds getBounds(BaseTransform baseTransform, Effect effect) {
            return null;
        }

        @Override
        public Effect.AccelType getAccelType(FilterContext filterContext) {
            return Effect.AccelType.INTRINSIC;
        }

        @Override
        public boolean reducesOpaquePixels() {
            return false;
        }

        @Override
        public DirtyRegionContainer getDirtyRegions(Effect effect, DirtyRegionPool dirtyRegionPool) {
            return null;
        }
    }

    private abstract class Composite {
        private Composite() {
        }

        abstract void doPaint(Graphics var1);

        void paint() {
            this.paint(WCGraphicsPrismContext.this.getGraphics(true));
        }

        void paint(Graphics graphics) {
            if (graphics != null) {
                CompositeMode compositeMode = graphics.getCompositeMode();
                switch (WCGraphicsPrismContext.this.state.getCompositeOperation()) {
                    case 1: {
                        graphics.setCompositeMode(CompositeMode.SRC);
                        this.doPaint(graphics);
                        graphics.setCompositeMode(compositeMode);
                        break;
                    }
                    case 2: {
                        graphics.setCompositeMode(CompositeMode.SRC_OVER);
                        this.doPaint(graphics);
                        graphics.setCompositeMode(compositeMode);
                        break;
                    }
                    default: {
                        this.blend(graphics);
                    }
                }
                WCGraphicsPrismContext.this.isRootLayerValid = false;
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        private void blend(Graphics graphics) {
            RTTexture rTTexture;
            ReadbackGraphics readbackGraphics;
            PrDrawable prDrawable;
            FilterContext filterContext;
            block11: {
                filterContext = WCGraphicsPrismContext.getFilterContext(graphics);
                prDrawable = null;
                PrDrawable prDrawable2 = null;
                readbackGraphics = null;
                rTTexture = null;
                Rectangle rectangle = WCGraphicsPrismContext.this.state.getClipNoClone();
                WCImage wCImage = WCGraphicsPrismContext.this.getImage();
                try {
                    Graphics graphics2;
                    if (wCImage != null && wCImage instanceof PrismImage) {
                        prDrawable = (PrDrawable)Effect.getCompatibleImage(filterContext, rectangle.width, rectangle.height);
                        graphics2 = prDrawable.createGraphics();
                        ((PrismImage)wCImage).draw(graphics2, 0, 0, rectangle.width, rectangle.height, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
                    } else {
                        readbackGraphics = (ReadbackGraphics)graphics;
                        rTTexture = readbackGraphics.readBack(rectangle);
                        prDrawable = PrDrawable.create(filterContext, rTTexture);
                    }
                    prDrawable2 = (PrDrawable)Effect.getCompatibleImage(filterContext, rectangle.width, rectangle.height);
                    graphics2 = prDrawable2.createGraphics();
                    WCGraphicsPrismContext.this.state.apply(graphics2);
                    this.doPaint(graphics2);
                    graphics.clear();
                    PrEffectHelper.render(this.createEffect(prDrawable, prDrawable2, rectangle.width, rectangle.height), graphics, 0.0f, 0.0f, null);
                    if (prDrawable2 == null) break block11;
                }
                catch (Throwable throwable) {
                    if (prDrawable2 != null) {
                        Effect.releaseCompatibleImage(filterContext, prDrawable2);
                    }
                    if (prDrawable != null) {
                        if (readbackGraphics != null && rTTexture != null) {
                            readbackGraphics.releaseReadBackBuffer(rTTexture);
                        } else {
                            Effect.releaseCompatibleImage(filterContext, prDrawable);
                        }
                    }
                    throw throwable;
                }
                Effect.releaseCompatibleImage(filterContext, prDrawable2);
            }
            if (prDrawable != null) {
                if (readbackGraphics != null && rTTexture != null) {
                    readbackGraphics.releaseReadBackBuffer(rTTexture);
                } else {
                    Effect.releaseCompatibleImage(filterContext, prDrawable);
                }
            }
        }

        private Effect createBlend(Blend.Mode mode, PrDrawable prDrawable, PrDrawable prDrawable2, int n2, int n3) {
            return new Blend(mode, new PassThrough(prDrawable, n2, n3), new PassThrough(prDrawable2, n2, n3));
        }

        private Effect createEffect(PrDrawable prDrawable, PrDrawable prDrawable2, int n2, int n3) {
            switch (WCGraphicsPrismContext.this.state.getCompositeOperation()) {
                case 0: 
                case 10: {
                    return new Blend(Blend.Mode.SRC_OVER, this.createBlend(Blend.Mode.SRC_OUT, prDrawable, prDrawable2, n2, n3), this.createBlend(Blend.Mode.SRC_OUT, prDrawable2, prDrawable, n2, n3));
                }
                case 3: {
                    return this.createBlend(Blend.Mode.SRC_IN, prDrawable, prDrawable2, n2, n3);
                }
                case 4: {
                    return this.createBlend(Blend.Mode.SRC_OUT, prDrawable, prDrawable2, n2, n3);
                }
                case 5: {
                    return this.createBlend(Blend.Mode.SRC_ATOP, prDrawable, prDrawable2, n2, n3);
                }
                case 6: {
                    return this.createBlend(Blend.Mode.SRC_OVER, prDrawable2, prDrawable, n2, n3);
                }
                case 7: {
                    return this.createBlend(Blend.Mode.SRC_IN, prDrawable2, prDrawable, n2, n3);
                }
                case 8: {
                    return this.createBlend(Blend.Mode.SRC_OUT, prDrawable2, prDrawable, n2, n3);
                }
                case 9: {
                    return this.createBlend(Blend.Mode.SRC_ATOP, prDrawable2, prDrawable, n2, n3);
                }
                case 12: {
                    return this.createBlend(Blend.Mode.ADD, prDrawable, prDrawable2, n2, n3);
                }
            }
            return this.createBlend(Blend.Mode.SRC_OVER, prDrawable, prDrawable2, n2, n3);
        }
    }

    private static final class ClipLayer
    extends Layer {
        private final WCPath normalizedToClipPath;
        private boolean srcover;

        private ClipLayer(Graphics graphics, Rectangle rectangle, WCPath wCPath, boolean bl) {
            super(graphics, rectangle, bl);
            this.normalizedToClipPath = wCPath;
            this.srcover = true;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        void init(Graphics graphics) {
            RTTexture rTTexture = null;
            ReadbackGraphics readbackGraphics = null;
            try {
                readbackGraphics = (ReadbackGraphics)graphics;
                rTTexture = readbackGraphics.readBack(this.bounds);
                this.getGraphics().drawTexture(rTTexture, 0.0f, 0.0f, this.bounds.width, this.bounds.height);
            }
            finally {
                if (readbackGraphics != null && rTTexture != null) {
                    readbackGraphics.releaseReadBackBuffer(rTTexture);
                }
            }
            this.srcover = false;
        }

        @Override
        void render(Graphics graphics) {
            Path2D path2D = ((WCPathImpl)this.normalizedToClipPath).getPlatformPath();
            PrDrawable prDrawable = (PrDrawable)Effect.getCompatibleImage(this.fctx, this.bounds.width, this.bounds.height);
            Graphics graphics2 = prDrawable.createGraphics();
            graphics2.setPaint(Color.BLACK);
            graphics2.fill(path2D);
            if (graphics instanceof MaskTextureGraphics && !(graphics instanceof PrinterGraphics)) {
                MaskTextureGraphics maskTextureGraphics = (MaskTextureGraphics)graphics;
                if (this.srcover) {
                    maskTextureGraphics.drawPixelsMasked((RTTexture)this.buffer.getTextureObject(), (RTTexture)prDrawable.getTextureObject(), this.bounds.x, this.bounds.y, this.bounds.width, this.bounds.height, 0, 0, 0, 0);
                } else {
                    maskTextureGraphics.maskInterpolatePixels((RTTexture)this.buffer.getTextureObject(), (RTTexture)prDrawable.getTextureObject(), this.bounds.x, this.bounds.y, this.bounds.width, this.bounds.height, 0, 0, 0, 0);
                }
            } else {
                Blend blend = new Blend(Blend.Mode.SRC_IN, new PassThrough(prDrawable, this.bounds.width, this.bounds.height), new PassThrough(this.buffer, this.bounds.width, this.bounds.height));
                Affine3D affine3D = new Affine3D(graphics.getTransformNoClone());
                graphics.setTransform(BaseTransform.IDENTITY_TRANSFORM);
                PrEffectHelper.render(blend, graphics, this.bounds.x, this.bounds.y, null);
                graphics.setTransform(affine3D);
            }
            Effect.releaseCompatibleImage(this.fctx, prDrawable);
        }

        public String toString() {
            return String.format("ClipLayer[%d,%d + %dx%d, path %s]", this.bounds.x, this.bounds.y, this.bounds.width, this.bounds.height, this.normalizedToClipPath);
        }
    }

    private final class TransparencyLayer
    extends Layer {
        private final float opacity;

        private TransparencyLayer(Graphics graphics, Rectangle rectangle, float f2) {
            super(graphics, rectangle, false);
            this.opacity = f2;
        }

        @Override
        void init(Graphics graphics) {
            WCGraphicsPrismContext.this.state.setCompositeOperation(2);
        }

        @Override
        void render(Graphics graphics) {
            new Composite(){

                @Override
                void doPaint(Graphics graphics) {
                    float f2 = graphics.getExtraAlpha();
                    graphics.setExtraAlpha(TransparencyLayer.this.opacity);
                    Affine3D affine3D = new Affine3D(graphics.getTransformNoClone());
                    graphics.setTransform(BaseTransform.IDENTITY_TRANSFORM);
                    graphics.drawTexture((Texture)TransparencyLayer.this.buffer.getTextureObject(), TransparencyLayer.this.bounds.x, TransparencyLayer.this.bounds.y, TransparencyLayer.this.bounds.width, TransparencyLayer.this.bounds.height);
                    graphics.setTransform(affine3D);
                    graphics.setExtraAlpha(f2);
                }
            }.paint(graphics);
        }

        public String toString() {
            return String.format("TransparencyLayer[%d,%d + %dx%d, opacity %.2f]", this.bounds.x, this.bounds.y, this.bounds.width, this.bounds.height, Float.valueOf(this.opacity));
        }
    }

    private static abstract class Layer {
        FilterContext fctx;
        PrDrawable buffer;
        Graphics graphics;
        final Rectangle bounds;
        boolean permanent;

        Layer(Graphics graphics, Rectangle rectangle, boolean bl) {
            this.bounds = new Rectangle(rectangle);
            this.permanent = bl;
            int n2 = Math.max(rectangle.width, 1);
            int n3 = Math.max(rectangle.height, 1);
            this.fctx = WCGraphicsPrismContext.getFilterContext(graphics);
            if (bl) {
                ResourceFactory resourceFactory = GraphicsPipeline.getDefaultResourceFactory();
                RTTexture rTTexture = resourceFactory.createRTTexture(n2, n3, Texture.WrapMode.CLAMP_NOT_NEEDED);
                rTTexture.makePermanent();
                this.buffer = ((PrRenderer)Renderer.getRenderer(this.fctx)).createDrawable(rTTexture);
            } else {
                this.buffer = (PrDrawable)Effect.getCompatibleImage(this.fctx, n2, n3);
            }
        }

        Graphics getGraphics() {
            if (this.graphics == null) {
                this.graphics = this.buffer.createGraphics();
            }
            return this.graphics;
        }

        abstract void init(Graphics var1);

        abstract void render(Graphics var1);

        private void dispose() {
            if (this.buffer != null) {
                if (this.permanent) {
                    this.buffer.flush();
                } else {
                    Effect.releaseCompatibleImage(this.fctx, this.buffer);
                }
                this.fctx = null;
                this.buffer = null;
            }
        }

        private double getX() {
            return this.bounds.x;
        }

        private double getY() {
            return this.bounds.y;
        }
    }

    private static final class ContextState {
        private final WCStrokeImpl stroke = new WCStrokeImpl();
        private Rectangle clip;
        private Paint paint;
        private float alpha;
        private boolean textFill = true;
        private boolean textStroke = false;
        private boolean textClip = false;
        private boolean restorePoint = false;
        private DropShadow shadow;
        private Affine3D xform;
        private Layer layer;
        private int compositeOperation;

        private ContextState() {
            this.clip = null;
            this.paint = Color.BLACK;
            this.stroke.setPaint(Color.BLACK);
            this.alpha = 1.0f;
            this.xform = new Affine3D();
            this.compositeOperation = 2;
        }

        private ContextState(ContextState contextState) {
            this.stroke.copyFrom(contextState.getStrokeNoClone());
            this.setPaint(contextState.getPaintNoClone());
            this.clip = contextState.getClipNoClone();
            if (this.clip != null) {
                this.clip = new Rectangle(this.clip);
            }
            this.xform = new Affine3D(contextState.getTransformNoClone());
            this.setShadow(contextState.getShadowNoClone());
            this.setLayer(contextState.getLayerNoClone());
            this.setAlpha(contextState.getAlpha());
            this.setTextMode(contextState.isTextFill(), contextState.isTextStroke(), contextState.isTextClip());
            this.setCompositeOperation(contextState.getCompositeOperation());
        }

        protected ContextState clone() {
            return new ContextState(this);
        }

        private void apply(Graphics graphics) {
            graphics.setTransform(this.getTransformNoClone());
            graphics.setClipRect(this.getClipNoClone());
            graphics.setExtraAlpha(this.getAlpha());
        }

        private int getCompositeOperation() {
            return this.compositeOperation;
        }

        private void setCompositeOperation(int n2) {
            this.compositeOperation = n2;
        }

        private WCStrokeImpl getStrokeNoClone() {
            return this.stroke;
        }

        private Paint getPaintNoClone() {
            return this.paint;
        }

        private void setPaint(Paint paint) {
            this.paint = paint;
        }

        private Rectangle getClipNoClone() {
            return this.clip;
        }

        private Layer getLayerNoClone() {
            return this.layer;
        }

        private void setLayer(Layer layer) {
            this.layer = layer;
        }

        private void setClip(Rectangle rectangle) {
            this.clip = rectangle;
        }

        private void clip(Rectangle rectangle) {
            if (null == this.clip) {
                this.clip = rectangle;
            } else {
                this.clip.intersectWith(rectangle);
            }
        }

        private void setAlpha(float f2) {
            this.alpha = f2;
        }

        private float getAlpha() {
            return this.alpha;
        }

        private void setTextMode(boolean bl, boolean bl2, boolean bl3) {
            this.textFill = bl;
            this.textStroke = bl2;
            this.textClip = bl3;
        }

        private boolean isTextFill() {
            return this.textFill;
        }

        private boolean isTextStroke() {
            return this.textStroke;
        }

        private boolean isTextClip() {
            return this.textClip;
        }

        private void markAsRestorePoint() {
            this.restorePoint = true;
        }

        private boolean isRestorePoint() {
            return this.restorePoint;
        }

        private void setShadow(DropShadow dropShadow) {
            this.shadow = dropShadow;
        }

        private DropShadow getShadowNoClone() {
            return this.shadow;
        }

        private Affine3D getTransformNoClone() {
            return this.xform;
        }

        private void setTransform(Affine3D affine3D) {
            this.xform.setTransform(affine3D);
        }

        private void concatTransform(Affine3D affine3D) {
            this.xform.concatenate(affine3D);
        }

        private void translate(double d2, double d3) {
            this.xform.translate(d2, d3);
        }

        private void scale(double d2, double d3) {
            this.xform.scale(d2, d3);
        }

        private void rotate(double d2) {
            this.xform.rotate(d2);
        }
    }

    public static enum Type {
        PRIMARY,
        DEDICATED;

    }
}

