/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene;

import com.sun.javafx.cursor.CursorFrame;
import com.sun.javafx.cursor.ImageCursorFrame;
import com.sun.javafx.tk.Toolkit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javafx.beans.InvalidationListener;
import javafx.beans.NamedArg;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoublePropertyBase;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectPropertyBase;
import javafx.geometry.Dimension2D;
import javafx.scene.Cursor;
import javafx.scene.image.Image;

public class ImageCursor
extends Cursor {
    private ObjectPropertyImpl<Image> image;
    private DoublePropertyImpl hotspotX;
    private DoublePropertyImpl hotspotY;
    private CursorFrame currentCursorFrame;
    private ImageCursorFrame firstCursorFrame;
    private Map<Object, ImageCursorFrame> otherCursorFrames;
    private int activeCounter;
    private InvalidationListener imageListener;

    public final Image getImage() {
        return this.image == null ? null : this.image.get();
    }

    public final ReadOnlyObjectProperty<Image> imageProperty() {
        return this.imagePropertyImpl();
    }

    private ObjectPropertyImpl<Image> imagePropertyImpl() {
        if (this.image == null) {
            this.image = new ObjectPropertyImpl("image");
        }
        return this.image;
    }

    public final double getHotspotX() {
        return this.hotspotX == null ? 0.0 : this.hotspotX.get();
    }

    public final ReadOnlyDoubleProperty hotspotXProperty() {
        return this.hotspotXPropertyImpl();
    }

    private DoublePropertyImpl hotspotXPropertyImpl() {
        if (this.hotspotX == null) {
            this.hotspotX = new DoublePropertyImpl("hotspotX");
        }
        return this.hotspotX;
    }

    public final double getHotspotY() {
        return this.hotspotY == null ? 0.0 : this.hotspotY.get();
    }

    public final ReadOnlyDoubleProperty hotspotYProperty() {
        return this.hotspotYPropertyImpl();
    }

    private DoublePropertyImpl hotspotYPropertyImpl() {
        if (this.hotspotY == null) {
            this.hotspotY = new DoublePropertyImpl("hotspotY");
        }
        return this.hotspotY;
    }

    public ImageCursor() {
    }

    public ImageCursor(@NamedArg(value="image") Image image) {
        this(image, 0.0, 0.0);
    }

    public ImageCursor(@NamedArg(value="image") Image image, @NamedArg(value="hotspotX") double d2, @NamedArg(value="hotspotY") double d3) {
        if (image != null && image.getProgress() < 1.0) {
            DelayedInitialization.applyTo(this, image, d2, d3);
        } else {
            this.initialize(image, d2, d3);
        }
    }

    public static Dimension2D getBestSize(double d2, double d3) {
        return Toolkit.getToolkit().getBestCursorSize((int)d2, (int)d3);
    }

    public static int getMaximumColors() {
        return Toolkit.getToolkit().getMaximumCursorColors();
    }

    public static ImageCursor chooseBestCursor(Image[] arrimage, double d2, double d3) {
        ImageCursor imageCursor = new ImageCursor();
        if (ImageCursor.needsDelayedInitialization(arrimage)) {
            DelayedInitialization.applyTo(imageCursor, arrimage, d2, d3);
        } else {
            imageCursor.initialize(arrimage, d2, d3);
        }
        return imageCursor;
    }

    @Override
    CursorFrame getCurrentFrame() {
        if (this.currentCursorFrame != null) {
            return this.currentCursorFrame;
        }
        Image image = this.getImage();
        if (image == null) {
            this.currentCursorFrame = Cursor.DEFAULT.getCurrentFrame();
            return this.currentCursorFrame;
        }
        Object object = image.impl_getPlatformImage();
        if (object == null) {
            this.currentCursorFrame = Cursor.DEFAULT.getCurrentFrame();
            return this.currentCursorFrame;
        }
        if (this.firstCursorFrame == null) {
            this.firstCursorFrame = new ImageCursorFrame(object, image.getWidth(), image.getHeight(), this.getHotspotX(), this.getHotspotY());
            this.currentCursorFrame = this.firstCursorFrame;
        } else if (this.firstCursorFrame.getPlatformImage() == object) {
            this.currentCursorFrame = this.firstCursorFrame;
        } else {
            if (this.otherCursorFrames == null) {
                this.otherCursorFrames = new HashMap<Object, ImageCursorFrame>();
            }
            this.currentCursorFrame = this.otherCursorFrames.get(object);
            if (this.currentCursorFrame == null) {
                ImageCursorFrame imageCursorFrame = new ImageCursorFrame(object, image.getWidth(), image.getHeight(), this.getHotspotX(), this.getHotspotY());
                this.otherCursorFrames.put(object, imageCursorFrame);
                this.currentCursorFrame = imageCursorFrame;
            }
        }
        return this.currentCursorFrame;
    }

    private void invalidateCurrentFrame() {
        this.currentCursorFrame = null;
    }

    @Override
    void activate() {
        if (++this.activeCounter == 1) {
            this.bindImage(this.getImage());
            this.invalidateCurrentFrame();
        }
    }

    @Override
    void deactivate() {
        if (--this.activeCounter == 0) {
            this.unbindImage(this.getImage());
        }
    }

    private void initialize(Image[] arrimage, double d2, double d3) {
        Dimension2D dimension2D = ImageCursor.getBestSize(1.0, 1.0);
        if (arrimage.length == 0 || dimension2D.getWidth() == 0.0 || dimension2D.getHeight() == 0.0) {
            return;
        }
        if (arrimage.length == 1) {
            this.initialize(arrimage[0], d2, d3);
            return;
        }
        Image image = ImageCursor.findBestImage(arrimage);
        double d4 = image.getWidth() / arrimage[0].getWidth();
        double d5 = image.getHeight() / arrimage[0].getHeight();
        this.initialize(image, d2 * d4, d3 * d5);
    }

    private void initialize(Image image, double d2, double d3) {
        Image image2 = this.getImage();
        double d4 = this.getHotspotX();
        double d5 = this.getHotspotY();
        if (image == null || image.getWidth() < 1.0 || image.getHeight() < 1.0) {
            d2 = 0.0;
            d3 = 0.0;
        } else {
            if (d2 < 0.0) {
                d2 = 0.0;
            }
            if (d2 > image.getWidth() - 1.0) {
                d2 = image.getWidth() - 1.0;
            }
            if (d3 < 0.0) {
                d3 = 0.0;
            }
            if (d3 > image.getHeight() - 1.0) {
                d3 = image.getHeight() - 1.0;
            }
        }
        this.imagePropertyImpl().store(image);
        this.hotspotXPropertyImpl().store(d2);
        this.hotspotYPropertyImpl().store(d3);
        if (image2 != image) {
            if (this.activeCounter > 0) {
                this.unbindImage(image2);
                this.bindImage(image);
            }
            this.invalidateCurrentFrame();
            this.image.fireValueChangedEvent();
        }
        if (d4 != d2) {
            this.hotspotX.fireValueChangedEvent();
        }
        if (d5 != d3) {
            this.hotspotY.fireValueChangedEvent();
        }
    }

    private InvalidationListener getImageListener() {
        if (this.imageListener == null) {
            this.imageListener = observable -> this.invalidateCurrentFrame();
        }
        return this.imageListener;
    }

    private void bindImage(Image image) {
        if (image == null) {
            return;
        }
        Toolkit.getImageAccessor().getImageProperty(image).addListener(this.getImageListener());
    }

    private void unbindImage(Image image) {
        if (image == null) {
            return;
        }
        Toolkit.getImageAccessor().getImageProperty(image).removeListener(this.getImageListener());
    }

    private static boolean needsDelayedInitialization(Image[] arrimage) {
        for (Image image : arrimage) {
            if (!(image.getProgress() < 1.0)) continue;
            return true;
        }
        return false;
    }

    /*
     * WARNING - void declaration
     */
    private static Image findBestImage(Image[] arrimage) {
        void var1_5;
        void var1_3;
        double d2;
        double d3;
        double d4;
        Dimension2D dimension2D;
        for (Image image : arrimage) {
            Dimension2D dimension2D2 = ImageCursor.getBestSize((int)image.getWidth(), (int)image.getHeight());
            if (dimension2D2.getWidth() != image.getWidth() || dimension2D2.getHeight() != image.getHeight()) continue;
            return image;
        }
        Object var1_2 = null;
        double d5 = Double.MAX_VALUE;
        for (Image image : arrimage) {
            if (!(image.getWidth() > 0.0) || !(image.getHeight() > 0.0)) continue;
            dimension2D = ImageCursor.getBestSize(image.getWidth(), image.getHeight());
            d4 = dimension2D.getWidth() / image.getWidth();
            d3 = dimension2D.getHeight() / image.getHeight();
            if (!(d4 >= 1.0) || !(d3 >= 1.0) || !((d2 = Math.max(d4, d3)) < d5)) continue;
            Image image2 = image;
            d5 = d2;
        }
        if (var1_3 != null) {
            return var1_3;
        }
        for (Image image : arrimage) {
            if (!(image.getWidth() > 0.0) || !(image.getHeight() > 0.0) || !((dimension2D = ImageCursor.getBestSize(image.getWidth(), image.getHeight())).getWidth() > 0.0) || !(dimension2D.getHeight() > 0.0)) continue;
            d4 = dimension2D.getWidth() / image.getWidth();
            if (d4 < 1.0) {
                d4 = 1.0 / d4;
            }
            if ((d3 = dimension2D.getHeight() / image.getHeight()) < 1.0) {
                d3 = 1.0 / d3;
            }
            if (!((d2 = Math.max(d4, d3)) < d5)) continue;
            Image image3 = image;
            d5 = d2;
        }
        if (var1_5 != null) {
            return var1_5;
        }
        return arrimage[0];
    }

    private static final class DelayedInitialization
    implements InvalidationListener {
        private final ImageCursor targetCursor;
        private final Image[] images;
        private final double hotspotX;
        private final double hotspotY;
        private final boolean initAsSingle;
        private int waitForImages;

        private DelayedInitialization(ImageCursor imageCursor, Image[] arrimage, double d2, double d3, boolean bl) {
            this.targetCursor = imageCursor;
            this.images = arrimage;
            this.hotspotX = d2;
            this.hotspotY = d3;
            this.initAsSingle = bl;
        }

        public static void applyTo(ImageCursor imageCursor, Image[] arrimage, double d2, double d3) {
            DelayedInitialization delayedInitialization = new DelayedInitialization(imageCursor, Arrays.copyOf(arrimage, arrimage.length), d2, d3, false);
            delayedInitialization.start();
        }

        public static void applyTo(ImageCursor imageCursor, Image image, double d2, double d3) {
            DelayedInitialization delayedInitialization = new DelayedInitialization(imageCursor, new Image[]{image}, d2, d3, true);
            delayedInitialization.start();
        }

        private void start() {
            for (Image image : this.images) {
                if (!(image.getProgress() < 1.0)) continue;
                ++this.waitForImages;
                image.progressProperty().addListener(this);
            }
        }

        private void cleanupAndFinishInitialization() {
            for (Image image : this.images) {
                image.progressProperty().removeListener(this);
            }
            if (this.initAsSingle) {
                this.targetCursor.initialize(this.images[0], this.hotspotX, this.hotspotY);
            } else {
                this.targetCursor.initialize(this.images, this.hotspotX, this.hotspotY);
            }
        }

        @Override
        public void invalidated(Observable observable) {
            if (((ReadOnlyDoubleProperty)observable).get() == 1.0 && --this.waitForImages == 0) {
                this.cleanupAndFinishInitialization();
            }
        }
    }

    private final class ObjectPropertyImpl<T>
    extends ReadOnlyObjectPropertyBase<T> {
        private final String name;
        private T value;

        public ObjectPropertyImpl(String string) {
            this.name = string;
        }

        public void store(T t2) {
            this.value = t2;
        }

        @Override
        public void fireValueChangedEvent() {
            super.fireValueChangedEvent();
        }

        @Override
        public T get() {
            return this.value;
        }

        @Override
        public Object getBean() {
            return ImageCursor.this;
        }

        @Override
        public String getName() {
            return this.name;
        }
    }

    private final class DoublePropertyImpl
    extends ReadOnlyDoublePropertyBase {
        private final String name;
        private double value;

        public DoublePropertyImpl(String string) {
            this.name = string;
        }

        public void store(double d2) {
            this.value = d2;
        }

        @Override
        public void fireValueChangedEvent() {
            super.fireValueChangedEvent();
        }

        @Override
        public double get() {
            return this.value;
        }

        @Override
        public Object getBean() {
            return ImageCursor.this;
        }

        @Override
        public String getName() {
            return this.name;
        }
    }
}

