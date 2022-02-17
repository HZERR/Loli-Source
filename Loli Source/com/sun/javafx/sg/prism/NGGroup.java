/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.GeneralTransform3D;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NodeEffectInput;
import com.sun.javafx.sg.prism.NodePath;
import com.sun.prism.Graphics;
import com.sun.prism.Texture;
import com.sun.scenario.effect.Blend;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.prism.PrDrawable;
import com.sun.scenario.effect.impl.prism.PrEffectHelper;
import com.sun.scenario.effect.impl.prism.PrTexture;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NGGroup
extends NGNode {
    private Blend.Mode blendMode = Blend.Mode.SRC_OVER;
    private List<NGNode> children = new ArrayList<NGNode>(1);
    private List<NGNode> unmod = Collections.unmodifiableList(this.children);
    private List<NGNode> removed;
    private static final int REGION_INTERSECTS_MASK = 0x15555555;

    public List<NGNode> getChildren() {
        return this.unmod;
    }

    public void add(int n2, NGNode nGNode) {
        if (n2 < -1 || n2 > this.children.size()) {
            throw new IndexOutOfBoundsException("invalid index");
        }
        NGNode nGNode2 = nGNode;
        nGNode2.setParent(this);
        this.childDirty = true;
        if (n2 == -1) {
            this.children.add(nGNode);
        } else {
            this.children.add(n2, nGNode);
        }
        nGNode2.markDirty();
        this.markTreeDirtyNoIncrement();
        this.geometryChanged();
    }

    public void clearFrom(int n2) {
        if (n2 < this.children.size()) {
            this.children.subList(n2, this.children.size()).clear();
            this.geometryChanged();
            this.childDirty = true;
            this.markTreeDirtyNoIncrement();
        }
    }

    public List<NGNode> getRemovedChildren() {
        return this.removed;
    }

    public void addToRemoved(NGNode nGNode) {
        if (this.removed == null) {
            this.removed = new ArrayList<NGNode>();
        }
        if (this.dirtyChildrenAccumulated > 12) {
            return;
        }
        this.removed.add(nGNode);
        ++this.dirtyChildrenAccumulated;
        if (this.dirtyChildrenAccumulated > 12) {
            this.removed.clear();
        }
    }

    @Override
    protected void clearDirty() {
        super.clearDirty();
        if (this.removed != null) {
            this.removed.clear();
        }
    }

    public void remove(NGNode nGNode) {
        this.children.remove(nGNode);
        this.geometryChanged();
        this.childDirty = true;
        this.markTreeDirtyNoIncrement();
    }

    public void remove(int n2) {
        this.children.remove(n2);
        this.geometryChanged();
        this.childDirty = true;
        this.markTreeDirtyNoIncrement();
    }

    public void clear() {
        this.children.clear();
        this.childDirty = false;
        this.geometryChanged();
        this.markTreeDirtyNoIncrement();
    }

    public void setBlendMode(Object object) {
        if (object == null) {
            throw new IllegalArgumentException("Mode must be non-null");
        }
        if (this.blendMode != object) {
            this.blendMode = (Blend.Mode)((Object)object);
            this.visualsChanged();
        }
    }

    @Override
    public void renderForcedContent(Graphics graphics) {
        if (this.children == null) {
            return;
        }
        for (int i2 = 0; i2 < this.children.size(); ++i2) {
            this.children.get(i2).renderForcedContent(graphics);
        }
    }

    @Override
    protected void renderContent(Graphics graphics) {
        int n2;
        if (this.children == null) {
            return;
        }
        NodePath nodePath = graphics.getRenderRoot();
        int n3 = 0;
        if (nodePath != null) {
            if (nodePath.hasNext()) {
                nodePath.next();
                n3 = this.children.indexOf(nodePath.getCurrentNode());
                for (n2 = 0; n2 < n3; ++n2) {
                    this.children.get(n2).clearDirtyTree();
                }
            } else {
                graphics.setRenderRoot(null);
            }
        }
        if (this.blendMode == Blend.Mode.SRC_OVER || this.children.size() < 2) {
            for (n2 = n3; n2 < this.children.size(); ++n2) {
                NGNode nGNode;
                try {
                    nGNode = this.children.get(n2);
                }
                catch (Exception exception) {
                    nGNode = null;
                }
                if (nGNode == null) continue;
                nGNode.render(graphics);
            }
            return;
        }
        Blend blend = new Blend(this.blendMode, null, null);
        FilterContext filterContext = NGGroup.getFilterContext(graphics);
        ImageData imageData = null;
        boolean bl = true;
        do {
            Object object;
            BaseTransform baseTransform = graphics.getTransformNoClone().copy();
            if (imageData != null) {
                imageData.unref();
                imageData = null;
            }
            Rectangle rectangle = PrEffectHelper.getGraphicsClipNoClone(graphics);
            for (int i2 = n3; i2 < this.children.size(); ++i2) {
                object = this.children.get(i2);
                ImageData imageData2 = NodeEffectInput.getImageDataForNode(filterContext, (NGNode)object, false, baseTransform, rectangle);
                if (imageData == null) {
                    imageData = imageData2;
                    continue;
                }
                ImageData imageData3 = blend.filterImageDatas(filterContext, baseTransform, rectangle, null, new ImageData[]{imageData, imageData2});
                imageData.unref();
                imageData2.unref();
                imageData = imageData3;
            }
            if (imageData == null || !(bl = imageData.validate(filterContext))) continue;
            Rectangle rectangle2 = imageData.getUntransformedBounds();
            object = (PrDrawable)imageData.getUntransformedImage();
            graphics.setTransform(imageData.getTransform());
            graphics.drawTexture((Texture)((PrTexture)object).getTextureObject(), rectangle2.x, rectangle2.y, rectangle2.width, rectangle2.height);
        } while (imageData == null || !bl);
        if (imageData != null) {
            imageData.unref();
        }
    }

    @Override
    protected boolean hasOverlappingContents() {
        int n2;
        if (this.blendMode != Blend.Mode.SRC_OVER) {
            return false;
        }
        int n3 = n2 = this.children == null ? 0 : this.children.size();
        if (n2 == 1) {
            return this.children.get(0).hasOverlappingContents();
        }
        return n2 != 0;
    }

    public boolean isEmpty() {
        return this.children == null || this.children.isEmpty();
    }

    @Override
    protected boolean hasVisuals() {
        return false;
    }

    @Override
    protected boolean needsBlending() {
        Blend.Mode mode = this.getNodeBlendMode();
        return mode != null;
    }

    @Override
    protected NGNode.RenderRootResult computeRenderRoot(NodePath nodePath, RectBounds rectBounds, int n2, BaseTransform baseTransform, GeneralTransform3D generalTransform3D) {
        if (n2 != -1) {
            int n3 = this.cullingBits >> n2 * 2;
            if ((n3 & 3) == 0) {
                return NGNode.RenderRootResult.NO_RENDER_ROOT;
            }
            if ((n3 & 2) != 0) {
                n2 = -1;
            }
        }
        if (!this.isVisible()) {
            return NGNode.RenderRootResult.NO_RENDER_ROOT;
        }
        if ((double)this.getOpacity() != 1.0 || this.getEffect() != null && this.getEffect().reducesOpaquePixels() || this.needsBlending()) {
            return NGNode.RenderRootResult.NO_RENDER_ROOT;
        }
        if (this.getClipNode() != null) {
            NGNode nGNode = this.getClipNode();
            RectBounds rectBounds2 = nGNode.getOpaqueRegion();
            if (rectBounds2 == null) {
                return NGNode.RenderRootResult.NO_RENDER_ROOT;
            }
            TEMP_TRANSFORM.deriveWithNewTransform(baseTransform).deriveWithConcatenation(this.getTransform()).deriveWithConcatenation(nGNode.getTransform());
            if (!NGGroup.checkBoundsInQuad(rectBounds2, rectBounds, TEMP_TRANSFORM, generalTransform3D)) {
                return NGNode.RenderRootResult.NO_RENDER_ROOT;
            }
        }
        double d2 = baseTransform.getMxx();
        double d3 = baseTransform.getMxy();
        double d4 = baseTransform.getMxz();
        double d5 = baseTransform.getMxt();
        double d6 = baseTransform.getMyx();
        double d7 = baseTransform.getMyy();
        double d8 = baseTransform.getMyz();
        double d9 = baseTransform.getMyt();
        double d10 = baseTransform.getMzx();
        double d11 = baseTransform.getMzy();
        double d12 = baseTransform.getMzz();
        double d13 = baseTransform.getMzt();
        BaseTransform baseTransform2 = baseTransform.deriveWithConcatenation(this.getTransform());
        NGNode.RenderRootResult renderRootResult = NGNode.RenderRootResult.NO_RENDER_ROOT;
        boolean bl = true;
        for (int i2 = this.children.size() - 1; i2 >= 0; --i2) {
            NGNode nGNode = this.children.get(i2);
            renderRootResult = nGNode.computeRenderRoot(nodePath, rectBounds, n2, baseTransform2, generalTransform3D);
            bl &= nGNode.isClean();
            if (renderRootResult == NGNode.RenderRootResult.HAS_RENDER_ROOT) {
                nodePath.add(this);
                break;
            }
            if (renderRootResult != NGNode.RenderRootResult.HAS_RENDER_ROOT_AND_IS_CLEAN) continue;
            nodePath.add(this);
            if (bl) break;
            renderRootResult = NGNode.RenderRootResult.HAS_RENDER_ROOT;
            break;
        }
        baseTransform.restoreTransform(d2, d3, d4, d5, d6, d7, d8, d9, d10, d11, d12, d13);
        return renderRootResult;
    }

    @Override
    protected void markCullRegions(DirtyRegionContainer dirtyRegionContainer, int n2, BaseTransform baseTransform, GeneralTransform3D generalTransform3D) {
        super.markCullRegions(dirtyRegionContainer, n2, baseTransform, generalTransform3D);
        if (this.cullingBits == -1 || this.cullingBits != 0 && (this.cullingBits & 0x15555555) != 0) {
            double d2 = baseTransform.getMxx();
            double d3 = baseTransform.getMxy();
            double d4 = baseTransform.getMxz();
            double d5 = baseTransform.getMxt();
            double d6 = baseTransform.getMyx();
            double d7 = baseTransform.getMyy();
            double d8 = baseTransform.getMyz();
            double d9 = baseTransform.getMyt();
            double d10 = baseTransform.getMzx();
            double d11 = baseTransform.getMzy();
            double d12 = baseTransform.getMzz();
            double d13 = baseTransform.getMzt();
            BaseTransform baseTransform2 = baseTransform.deriveWithConcatenation(this.getTransform());
            for (int i2 = 0; i2 < this.children.size(); ++i2) {
                NGNode nGNode = this.children.get(i2);
                nGNode.markCullRegions(dirtyRegionContainer, this.cullingBits, baseTransform2, generalTransform3D);
            }
            baseTransform.restoreTransform(d2, d3, d4, d5, d6, d7, d8, d9, d10, d11, d12, d13);
        }
    }

    @Override
    public void drawDirtyOpts(BaseTransform baseTransform, GeneralTransform3D generalTransform3D, Rectangle rectangle, int[] arrn, int n2) {
        super.drawDirtyOpts(baseTransform, generalTransform3D, rectangle, arrn, n2);
        BaseTransform baseTransform2 = baseTransform.copy();
        baseTransform2 = baseTransform2.deriveWithConcatenation(this.getTransform());
        for (int i2 = 0; i2 < this.children.size(); ++i2) {
            NGNode nGNode = this.children.get(i2);
            nGNode.drawDirtyOpts(baseTransform2, generalTransform3D, rectangle, arrn, n2);
        }
    }
}

