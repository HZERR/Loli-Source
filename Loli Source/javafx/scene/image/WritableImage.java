/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.image;

import com.sun.javafx.tk.ImageLoader;
import com.sun.javafx.tk.PlatformImage;
import com.sun.javafx.tk.Toolkit;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import javafx.beans.NamedArg;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class WritableImage
extends Image {
    private ImageLoader tkImageLoader;
    private PixelWriter writer;

    public WritableImage(@NamedArg(value="width") int n2, @NamedArg(value="height") int n3) {
        super(n2, n3);
    }

    public WritableImage(@NamedArg(value="reader") PixelReader pixelReader, @NamedArg(value="width") int n2, @NamedArg(value="height") int n3) {
        super(n2, n3);
        this.getPixelWriter().setPixels(0, 0, n2, n3, pixelReader, 0, 0);
    }

    public WritableImage(@NamedArg(value="reader") PixelReader pixelReader, @NamedArg(value="x") int n2, @NamedArg(value="y") int n3, @NamedArg(value="width") int n4, @NamedArg(value="height") int n5) {
        super(n4, n5);
        this.getPixelWriter().setPixels(0, 0, n4, n5, pixelReader, n2, n3);
    }

    @Override
    boolean isAnimation() {
        return true;
    }

    @Override
    boolean pixelsReadable() {
        return true;
    }

    public final PixelWriter getPixelWriter() {
        if (this.getProgress() < 1.0 || this.isError()) {
            return null;
        }
        if (this.writer == null) {
            this.writer = new PixelWriter(){
                ReadOnlyObjectProperty<PlatformImage> pimgprop;
                {
                    this.pimgprop = WritableImage.this.acc_platformImageProperty();
                }

                @Override
                public PixelFormat getPixelFormat() {
                    PlatformImage platformImage = WritableImage.this.getWritablePlatformImage();
                    return platformImage.getPlatformPixelFormat();
                }

                @Override
                public void setArgb(int n2, int n3, int n4) {
                    WritableImage.this.getWritablePlatformImage().setArgb(n2, n3, n4);
                    WritableImage.this.pixelsDirty();
                }

                @Override
                public void setColor(int n2, int n3, Color color) {
                    if (color == null) {
                        throw new NullPointerException("Color cannot be null");
                    }
                    int n4 = (int)Math.round(color.getOpacity() * 255.0);
                    int n5 = (int)Math.round(color.getRed() * 255.0);
                    int n6 = (int)Math.round(color.getGreen() * 255.0);
                    int n7 = (int)Math.round(color.getBlue() * 255.0);
                    this.setArgb(n2, n3, n4 << 24 | n5 << 16 | n6 << 8 | n7);
                }

                @Override
                public <T extends Buffer> void setPixels(int n2, int n3, int n4, int n5, PixelFormat<T> pixelFormat, T t2, int n6) {
                    if (pixelFormat == null) {
                        throw new NullPointerException("PixelFormat cannot be null");
                    }
                    if (t2 == null) {
                        throw new NullPointerException("Buffer cannot be null");
                    }
                    PlatformImage platformImage = WritableImage.this.getWritablePlatformImage();
                    platformImage.setPixels(n2, n3, n4, n5, pixelFormat, t2, n6);
                    WritableImage.this.pixelsDirty();
                }

                @Override
                public void setPixels(int n2, int n3, int n4, int n5, PixelFormat<ByteBuffer> pixelFormat, byte[] arrby, int n6, int n7) {
                    if (pixelFormat == null) {
                        throw new NullPointerException("PixelFormat cannot be null");
                    }
                    if (arrby == null) {
                        throw new NullPointerException("Buffer cannot be null");
                    }
                    PlatformImage platformImage = WritableImage.this.getWritablePlatformImage();
                    platformImage.setPixels(n2, n3, n4, n5, pixelFormat, arrby, n6, n7);
                    WritableImage.this.pixelsDirty();
                }

                @Override
                public void setPixels(int n2, int n3, int n4, int n5, PixelFormat<IntBuffer> pixelFormat, int[] arrn, int n6, int n7) {
                    if (pixelFormat == null) {
                        throw new NullPointerException("PixelFormat cannot be null");
                    }
                    if (arrn == null) {
                        throw new NullPointerException("Buffer cannot be null");
                    }
                    PlatformImage platformImage = WritableImage.this.getWritablePlatformImage();
                    platformImage.setPixels(n2, n3, n4, n5, pixelFormat, arrn, n6, n7);
                    WritableImage.this.pixelsDirty();
                }

                @Override
                public void setPixels(int n2, int n3, int n4, int n5, PixelReader pixelReader, int n6, int n7) {
                    if (pixelReader == null) {
                        throw new NullPointerException("Reader cannot be null");
                    }
                    PlatformImage platformImage = WritableImage.this.getWritablePlatformImage();
                    platformImage.setPixels(n2, n3, n4, n5, pixelReader, n6, n7);
                    WritableImage.this.pixelsDirty();
                }
            };
        }
        return this.writer;
    }

    private void loadTkImage(Object object) {
        if (!(object instanceof ImageLoader)) {
            throw new IllegalArgumentException("Unrecognized image loader: " + object);
        }
        ImageLoader imageLoader = (ImageLoader)object;
        if (imageLoader.getWidth() != (int)this.getWidth() || imageLoader.getHeight() != (int)this.getHeight()) {
            throw new IllegalArgumentException("Size of loader does not match size of image");
        }
        super.setPlatformImage(imageLoader.getFrame(0));
        this.tkImageLoader = imageLoader;
    }

    private Object getTkImageLoader() {
        return this.tkImageLoader;
    }

    static {
        Toolkit.setWritableImageAccessor(new Toolkit.WritableImageAccessor(){

            @Override
            public void loadTkImage(WritableImage writableImage, Object object) {
                writableImage.loadTkImage(object);
            }

            @Override
            public Object getTkImageLoader(WritableImage writableImage) {
                return writableImage.getTkImageLoader();
            }
        });
    }
}

