/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.prism.ps;

import com.sun.glass.ui.Screen;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.Image;
import com.sun.prism.PixelFormat;
import com.sun.prism.RTTexture;
import com.sun.prism.ResourceFactory;
import com.sun.prism.ResourceFactoryListener;
import com.sun.prism.Texture;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderFactory;
import com.sun.prism.ps.ShaderGraphics;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.Filterable;
import com.sun.scenario.effect.FloatMap;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.LockableResource;
import com.sun.scenario.effect.impl.EffectPeer;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.hw.ShaderSource;
import com.sun.scenario.effect.impl.prism.PrDrawable;
import com.sun.scenario.effect.impl.prism.PrFilterContext;
import com.sun.scenario.effect.impl.prism.PrImage;
import com.sun.scenario.effect.impl.prism.PrRenderer;
import com.sun.scenario.effect.impl.prism.PrTexture;
import com.sun.scenario.effect.impl.prism.ps.PPSDrawable;
import com.sun.scenario.effect.impl.prism.ps.PPStoPSWDisplacementMapPeer;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.nio.FloatBuffer;
import java.util.Map;

public class PPSRenderer
extends PrRenderer {
    private final ResourceFactory rf;
    private final ShaderSource shaderSource;
    private Renderer.RendererState state;
    private boolean needsSWDispMap;
    private final ResourceFactoryListener listener = new ResourceFactoryListener(){

        @Override
        public void factoryReset() {
            PPSRenderer.this.dispose();
        }

        @Override
        public void factoryReleased() {
            PPSRenderer.this.dispose();
        }
    };

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private PPSRenderer(Screen screen, ShaderSource shaderSource) {
        this.shaderSource = shaderSource;
        PPSRenderer pPSRenderer = this;
        synchronized (pPSRenderer) {
            this.state = Renderer.RendererState.OK;
        }
        this.rf = GraphicsPipeline.getPipeline().getResourceFactory(screen);
        this.rf.addFactoryListener(this.listener);
        this.needsSWDispMap = !this.rf.isFormatSupported(PixelFormat.FLOAT_XYZW);
    }

    @Override
    public PrDrawable createDrawable(RTTexture rTTexture) {
        return PPSDrawable.create(rTTexture);
    }

    @Override
    public Effect.AccelType getAccelType() {
        return this.shaderSource.getAccelType();
    }

    @Override
    public synchronized Renderer.RendererState getRendererState() {
        return this.state;
    }

    @Override
    protected Renderer getBackupRenderer() {
        return this;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void dispose() {
        for (EffectPeer effectPeer : this.getPeers()) {
            effectPeer.dispose();
        }
        PPSRenderer pPSRenderer = this;
        synchronized (pPSRenderer) {
            this.state = Renderer.RendererState.DISPOSED;
        }
        this.rf.removeFactoryListener(this.listener);
    }

    protected final synchronized void markLost() {
        if (this.state == Renderer.RendererState.OK) {
            this.state = Renderer.RendererState.LOST;
        }
    }

    @Override
    public int getCompatibleWidth(int n2) {
        return PPSDrawable.getCompatibleWidth(this.rf, n2);
    }

    @Override
    public int getCompatibleHeight(int n2) {
        return PPSDrawable.getCompatibleHeight(this.rf, n2);
    }

    @Override
    public final PPSDrawable createCompatibleImage(int n2, int n3) {
        return PPSDrawable.create(this.rf, n2, n3);
    }

    @Override
    public PPSDrawable getCompatibleImage(int n2, int n3) {
        PPSDrawable pPSDrawable = (PPSDrawable)super.getCompatibleImage(n2, n3);
        if (pPSDrawable == null) {
            this.markLost();
        }
        return pPSDrawable;
    }

    @Override
    public LockableResource createFloatTexture(int n2, int n3) {
        Texture texture = this.rf.createFloatTexture(n2, n3);
        return new PrTexture<Texture>(texture);
    }

    @Override
    public void updateFloatTexture(LockableResource lockableResource, FloatMap floatMap) {
        FloatBuffer floatBuffer = floatMap.getBuffer();
        int n2 = floatMap.getWidth();
        int n3 = floatMap.getHeight();
        Image image = Image.fromFloatMapData(floatBuffer, n2, n3);
        Object t2 = ((PrTexture)lockableResource).getTextureObject();
        t2.update(image);
    }

    public Shader createShader(String string, Map<String, Integer> map, Map<String, Integer> map2, boolean bl) {
        if (PrismSettings.verbose) {
            System.out.println("PPSRenderer: scenario.effect - createShader: " + string);
        }
        InputStream inputStream = this.shaderSource.loadSource(string);
        int n2 = map.keySet().size() - 1;
        ShaderFactory shaderFactory = (ShaderFactory)this.rf;
        return shaderFactory.createShader(inputStream, map, map2, n2, bl, false);
    }

    private EffectPeer createIntrinsicPeer(FilterContext filterContext, String string) {
        EffectPeer effectPeer;
        Class<?> class_ = null;
        try {
            class_ = Class.forName("com.sun.scenario.effect.impl.prism.Pr" + string + "Peer");
            Constructor<?> constructor = class_.getConstructor(FilterContext.class, Renderer.class, String.class);
            effectPeer = (EffectPeer)constructor.newInstance(filterContext, this, string);
        }
        catch (Exception exception) {
            return null;
        }
        return effectPeer;
    }

    private EffectPeer createPlatformPeer(FilterContext filterContext, String string, int n2) {
        EffectPeer effectPeer;
        String string2 = string;
        if (n2 > 0) {
            string2 = string2 + "_" + n2;
        }
        try {
            Class<?> class_ = Class.forName("com.sun.scenario.effect.impl.prism.ps.PPS" + string + "Peer");
            Constructor<?> constructor = class_.getConstructor(FilterContext.class, Renderer.class, String.class);
            effectPeer = (EffectPeer)constructor.newInstance(filterContext, this, string2);
        }
        catch (Exception exception) {
            System.err.println("Error: Prism peer not found for: " + string + " due to error: " + exception.getMessage());
            return null;
        }
        return effectPeer;
    }

    @Override
    protected EffectPeer createPeer(FilterContext filterContext, String string, int n2) {
        if (PrRenderer.isIntrinsicPeer(string)) {
            return this.createIntrinsicPeer(filterContext, string);
        }
        if (this.needsSWDispMap && string.equals("DisplacementMap")) {
            PrFilterContext prFilterContext = ((PrFilterContext)filterContext).getSoftwareInstance();
            return new PPStoPSWDisplacementMapPeer(prFilterContext, this, string);
        }
        return this.createPlatformPeer(filterContext, string, n2);
    }

    @Override
    public boolean isImageDataCompatible(ImageData imageData) {
        if (this.getRendererState() == Renderer.RendererState.OK) {
            Filterable filterable = imageData.getUntransformedImage();
            return filterable instanceof PrDrawable && !filterable.isLost();
        }
        return false;
    }

    @Override
    public void clearImage(Filterable filterable) {
        PPSDrawable pPSDrawable = (PPSDrawable)filterable;
        pPSDrawable.clear();
    }

    @Override
    public ImageData createImageData(FilterContext filterContext, Filterable filterable) {
        BaseTransform baseTransform;
        int n2;
        if (!(filterable instanceof PrImage)) {
            throw new IllegalArgumentException("Identity source must be PrImage");
        }
        Image image = ((PrImage)filterable).getImage();
        int n3 = image.getWidth();
        PPSDrawable pPSDrawable = this.createCompatibleImage(n3, n2 = image.getHeight());
        if (pPSDrawable == null) {
            return null;
        }
        ShaderGraphics shaderGraphics = pPSDrawable.createGraphics();
        ResourceFactory resourceFactory = shaderGraphics.getResourceFactory();
        Texture texture = resourceFactory.createTexture(image, Texture.Usage.DEFAULT, Texture.WrapMode.CLAMP_TO_EDGE);
        shaderGraphics.drawTexture(texture, 0.0f, 0.0f, n3, n2);
        shaderGraphics.sync();
        texture.dispose();
        float f2 = image.getPixelScale();
        if (f2 != 1.0f) {
            f2 = 1.0f / f2;
            baseTransform = BaseTransform.getScaleInstance(f2, f2);
        } else {
            baseTransform = BaseTransform.IDENTITY_TRANSFORM;
        }
        ImageData imageData = new ImageData(filterContext, pPSDrawable, new Rectangle(n3, n2), baseTransform);
        return imageData;
    }

    @Override
    public Filterable transform(FilterContext filterContext, Filterable filterable, BaseTransform baseTransform, Rectangle rectangle, Rectangle rectangle2) {
        PPSDrawable pPSDrawable = this.getCompatibleImage(rectangle2.width, rectangle2.height);
        if (pPSDrawable != null) {
            ShaderGraphics shaderGraphics = pPSDrawable.createGraphics();
            shaderGraphics.translate(-rectangle2.x, -rectangle2.y);
            shaderGraphics.transform(baseTransform);
            shaderGraphics.drawTexture((Texture)((PPSDrawable)filterable).getTextureObject(), rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        }
        return pPSDrawable;
    }

    @Override
    public ImageData transform(FilterContext filterContext, ImageData imageData, BaseTransform baseTransform, Rectangle rectangle, Rectangle rectangle2) {
        PPSDrawable pPSDrawable = this.getCompatibleImage(rectangle2.width, rectangle2.height);
        if (pPSDrawable != null) {
            PPSDrawable pPSDrawable2 = (PPSDrawable)imageData.getUntransformedImage();
            ShaderGraphics shaderGraphics = pPSDrawable.createGraphics();
            shaderGraphics.translate(-rectangle2.x, -rectangle2.y);
            shaderGraphics.transform(baseTransform);
            shaderGraphics.drawTexture((Texture)pPSDrawable2.getTextureObject(), rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        }
        imageData.unref();
        return new ImageData(filterContext, pPSDrawable, rectangle2);
    }

    private static ShaderSource createShaderSource(String string) {
        Class<?> class_ = null;
        try {
            class_ = Class.forName(string);
            return (ShaderSource)class_.newInstance();
        }
        catch (ClassNotFoundException classNotFoundException) {
            System.err.println(string + " class not found");
        }
        finally {
            return null;
        }
    }

    public static Renderer createRenderer(FilterContext filterContext) {
        Object object = filterContext.getReferent();
        GraphicsPipeline graphicsPipeline = GraphicsPipeline.getPipeline();
        if (graphicsPipeline == null || !(object instanceof Screen)) {
            return null;
        }
        Screen screen = (Screen)object;
        ShaderSource shaderSource = null;
        if (graphicsPipeline.supportsShader(GraphicsPipeline.ShaderType.HLSL, GraphicsPipeline.ShaderModel.SM3)) {
            shaderSource = PPSRenderer.createShaderSource("com.sun.scenario.effect.impl.hw.d3d.D3DShaderSource");
        } else if (graphicsPipeline.supportsShader(GraphicsPipeline.ShaderType.GLSL, GraphicsPipeline.ShaderModel.SM3)) {
            shaderSource = PPSRenderer.createShaderSource("com.sun.scenario.effect.impl.es2.ES2ShaderSource");
        } else {
            throw new InternalError("Unknown GraphicsPipeline");
        }
        if (shaderSource == null) {
            return null;
        }
        return new PPSRenderer(screen, shaderSource);
    }
}

