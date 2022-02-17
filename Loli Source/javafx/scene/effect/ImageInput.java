/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.effect;

import com.sun.javafx.beans.event.AbstractNotifyListener;
import com.sun.javafx.effect.EffectDirtyBits;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.BoundsAccessor;
import com.sun.javafx.tk.Toolkit;
import com.sun.scenario.effect.Identity;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.scene.Node;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;

public class ImageInput
extends Effect {
    private ObjectProperty<Image> source;
    private final AbstractNotifyListener platformImageChangeListener = new AbstractNotifyListener(){

        @Override
        public void invalidated(Observable observable) {
            ImageInput.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
            ImageInput.this.effectBoundsChanged();
        }
    };
    private Image oldImage;
    private DoubleProperty x;
    private DoubleProperty y;

    public ImageInput() {
    }

    public ImageInput(Image image) {
        this.setSource(image);
    }

    public ImageInput(Image image, double d2, double d3) {
        this.setSource(image);
        this.setX(d2);
        this.setY(d3);
    }

    @Override
    Identity impl_createImpl() {
        return new Identity(null);
    }

    public final void setSource(Image image) {
        this.sourceProperty().set(image);
    }

    public final Image getSource() {
        return this.source == null ? null : (Image)this.source.get();
    }

    public final ObjectProperty<Image> sourceProperty() {
        if (this.source == null) {
            this.source = new ObjectPropertyBase<Image>(){
                private boolean needsListeners = false;

                @Override
                public void invalidated() {
                    Image image = (Image)this.get();
                    Toolkit.ImageAccessor imageAccessor = Toolkit.getImageAccessor();
                    if (this.needsListeners) {
                        imageAccessor.getImageProperty(ImageInput.this.oldImage).removeListener(ImageInput.this.platformImageChangeListener.getWeakListener());
                    }
                    this.needsListeners = image != null && (imageAccessor.isAnimation(image) || image.getProgress() < 1.0);
                    ImageInput.this.oldImage = image;
                    if (this.needsListeners) {
                        imageAccessor.getImageProperty(image).addListener(ImageInput.this.platformImageChangeListener.getWeakListener());
                    }
                    ImageInput.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    ImageInput.this.effectBoundsChanged();
                }

                @Override
                public Object getBean() {
                    return ImageInput.this;
                }

                @Override
                public String getName() {
                    return "source";
                }
            };
        }
        return this.source;
    }

    public final void setX(double d2) {
        this.xProperty().set(d2);
    }

    public final double getX() {
        return this.x == null ? 0.0 : this.x.get();
    }

    public final DoubleProperty xProperty() {
        if (this.x == null) {
            this.x = new DoublePropertyBase(){

                @Override
                public void invalidated() {
                    ImageInput.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    ImageInput.this.effectBoundsChanged();
                }

                @Override
                public Object getBean() {
                    return ImageInput.this;
                }

                @Override
                public String getName() {
                    return "x";
                }
            };
        }
        return this.x;
    }

    public final void setY(double d2) {
        this.yProperty().set(d2);
    }

    public final double getY() {
        return this.y == null ? 0.0 : this.y.get();
    }

    public final DoubleProperty yProperty() {
        if (this.y == null) {
            this.y = new DoublePropertyBase(){

                @Override
                public void invalidated() {
                    ImageInput.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    ImageInput.this.effectBoundsChanged();
                }

                @Override
                public Object getBean() {
                    return ImageInput.this;
                }

                @Override
                public String getName() {
                    return "y";
                }
            };
        }
        return this.y;
    }

    @Override
    void impl_update() {
        Identity identity = (Identity)this.impl_getImpl();
        Image image = this.getSource();
        if (image != null && image.impl_getPlatformImage() != null) {
            identity.setSource(Toolkit.getToolkit().toFilterable(image));
        } else {
            identity.setSource(null);
        }
        identity.setLocation(new Point2D((float)this.getX(), (float)this.getY()));
    }

    @Override
    boolean impl_checkChainContains(Effect effect) {
        return false;
    }

    @Override
    @Deprecated
    public BaseBounds impl_getBounds(BaseBounds baseBounds, BaseTransform baseTransform, Node node, BoundsAccessor boundsAccessor) {
        Image image = this.getSource();
        if (image != null && image.impl_getPlatformImage() != null) {
            float f2 = (float)this.getX();
            float f3 = (float)this.getY();
            float f4 = (float)image.getWidth();
            float f5 = (float)image.getHeight();
            RectBounds rectBounds = new RectBounds(f2, f3, f2 + f4, f3 + f5);
            return ImageInput.transformBounds(baseTransform, rectBounds);
        }
        return new RectBounds();
    }

    @Override
    @Deprecated
    public Effect impl_copy() {
        return new ImageInput(this.getSource(), this.getX(), this.getY());
    }
}

