/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.image;

import com.sun.javafx.runtime.async.AsyncOperation;
import com.sun.javafx.runtime.async.AsyncOperationListener;
import com.sun.javafx.tk.ImageLoader;
import com.sun.javafx.tk.PlatformImage;
import com.sun.javafx.tk.Toolkit;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CancellationException;
import java.util.regex.Pattern;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.NamedArg;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoublePropertyBase;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectPropertyBase;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Image {
    private static final Pattern URL_QUICKMATCH;
    private final String url;
    private final InputStream impl_source;
    private ReadOnlyDoubleWrapper progress;
    private final double requestedWidth;
    private final double requestedHeight;
    private DoublePropertyImpl width;
    private DoublePropertyImpl height;
    private final boolean preserveRatio;
    private final boolean smooth;
    private final boolean backgroundLoading;
    private ReadOnlyBooleanWrapper error;
    private ReadOnlyObjectWrapper<Exception> exception;
    private ObjectPropertyImpl<PlatformImage> platformImage;
    private ImageTask backgroundTask;
    private Animation animation;
    private PlatformImage[] animFrames;
    private static final int MAX_RUNNING_TASKS = 4;
    private static int runningTasks;
    private static final Queue<ImageTask> pendingTasks;
    private PixelReader reader;

    @Deprecated
    public final String impl_getUrl() {
        return this.url;
    }

    final InputStream getImpl_source() {
        return this.impl_source;
    }

    final void setProgress(double d2) {
        this.progressPropertyImpl().set(d2);
    }

    public final double getProgress() {
        return this.progress == null ? 0.0 : this.progress.get();
    }

    public final ReadOnlyDoubleProperty progressProperty() {
        return this.progressPropertyImpl().getReadOnlyProperty();
    }

    private ReadOnlyDoubleWrapper progressPropertyImpl() {
        if (this.progress == null) {
            this.progress = new ReadOnlyDoubleWrapper(this, "progress");
        }
        return this.progress;
    }

    public final double getRequestedWidth() {
        return this.requestedWidth;
    }

    public final double getRequestedHeight() {
        return this.requestedHeight;
    }

    public final double getWidth() {
        return this.width == null ? 0.0 : this.width.get();
    }

    public final ReadOnlyDoubleProperty widthProperty() {
        return this.widthPropertyImpl();
    }

    private DoublePropertyImpl widthPropertyImpl() {
        if (this.width == null) {
            this.width = new DoublePropertyImpl("width");
        }
        return this.width;
    }

    public final double getHeight() {
        return this.height == null ? 0.0 : this.height.get();
    }

    public final ReadOnlyDoubleProperty heightProperty() {
        return this.heightPropertyImpl();
    }

    private DoublePropertyImpl heightPropertyImpl() {
        if (this.height == null) {
            this.height = new DoublePropertyImpl("height");
        }
        return this.height;
    }

    public final boolean isPreserveRatio() {
        return this.preserveRatio;
    }

    public final boolean isSmooth() {
        return this.smooth;
    }

    public final boolean isBackgroundLoading() {
        return this.backgroundLoading;
    }

    private void setError(boolean bl) {
        this.errorPropertyImpl().set(bl);
    }

    public final boolean isError() {
        return this.error == null ? false : this.error.get();
    }

    public final ReadOnlyBooleanProperty errorProperty() {
        return this.errorPropertyImpl().getReadOnlyProperty();
    }

    private ReadOnlyBooleanWrapper errorPropertyImpl() {
        if (this.error == null) {
            this.error = new ReadOnlyBooleanWrapper(this, "error");
        }
        return this.error;
    }

    private void setException(Exception exception) {
        this.exceptionPropertyImpl().set(exception);
    }

    public final Exception getException() {
        return this.exception == null ? null : (Exception)this.exception.get();
    }

    public final ReadOnlyObjectProperty<Exception> exceptionProperty() {
        return this.exceptionPropertyImpl().getReadOnlyProperty();
    }

    private ReadOnlyObjectWrapper<Exception> exceptionPropertyImpl() {
        if (this.exception == null) {
            this.exception = new ReadOnlyObjectWrapper(this, "exception");
        }
        return this.exception;
    }

    @Deprecated
    public final Object impl_getPlatformImage() {
        return this.platformImage == null ? null : this.platformImage.get();
    }

    final ReadOnlyObjectProperty<PlatformImage> acc_platformImageProperty() {
        return this.platformImagePropertyImpl();
    }

    private ObjectPropertyImpl<PlatformImage> platformImagePropertyImpl() {
        if (this.platformImage == null) {
            this.platformImage = new ObjectPropertyImpl("platformImage");
        }
        return this.platformImage;
    }

    void pixelsDirty() {
        this.platformImagePropertyImpl().fireValueChangedEvent();
    }

    public Image(@NamedArg(value="url") String string) {
        this(Image.validateUrl(string), null, 0.0, 0.0, false, false, false);
        this.initialize(null);
    }

    public Image(@NamedArg(value="url") String string, @NamedArg(value="backgroundLoading") boolean bl) {
        this(Image.validateUrl(string), null, 0.0, 0.0, false, false, bl);
        this.initialize(null);
    }

    public Image(@NamedArg(value="url") String string, @NamedArg(value="requestedWidth") double d2, @NamedArg(value="requestedHeight") double d3, @NamedArg(value="preserveRatio") boolean bl, @NamedArg(value="smooth") boolean bl2) {
        this(Image.validateUrl(string), null, d2, d3, bl, bl2, false);
        this.initialize(null);
    }

    public Image(@NamedArg(value="url", defaultValue="\"\"") String string, @NamedArg(value="requestedWidth") double d2, @NamedArg(value="requestedHeight") double d3, @NamedArg(value="preserveRatio") boolean bl, @NamedArg(value="smooth", defaultValue="true") boolean bl2, @NamedArg(value="backgroundLoading") boolean bl3) {
        this(Image.validateUrl(string), null, d2, d3, bl, bl2, bl3);
        this.initialize(null);
    }

    public Image(@NamedArg(value="is") InputStream inputStream) {
        this(null, Image.validateInputStream(inputStream), 0.0, 0.0, false, false, false);
        this.initialize(null);
    }

    public Image(@NamedArg(value="is") InputStream inputStream, @NamedArg(value="requestedWidth") double d2, @NamedArg(value="requestedHeight") double d3, @NamedArg(value="preserveRatio") boolean bl, @NamedArg(value="smooth") boolean bl2) {
        this(null, Image.validateInputStream(inputStream), d2, d3, bl, bl2, false);
        this.initialize(null);
    }

    Image(int n2, int n3) {
        this(null, null, n2, n3, false, false, false);
        if (n2 <= 0 || n3 <= 0) {
            throw new IllegalArgumentException("Image dimensions must be positive (w,h > 0)");
        }
        this.initialize(Toolkit.getToolkit().createPlatformImage(n2, n3));
    }

    private Image(Object object) {
        this(null, null, 0.0, 0.0, false, false, false);
        this.initialize(object);
    }

    private Image(String string, InputStream inputStream, double d2, double d3, boolean bl, boolean bl2, boolean bl3) {
        this.url = string;
        this.impl_source = inputStream;
        this.requestedWidth = d2;
        this.requestedHeight = d3;
        this.preserveRatio = bl;
        this.smooth = bl2;
        this.backgroundLoading = bl3;
    }

    public void cancel() {
        if (this.backgroundTask != null) {
            this.backgroundTask.cancel();
        }
    }

    void dispose() {
        this.cancel();
        if (this.animation != null) {
            this.animation.stop();
        }
    }

    private void initialize(Object object) {
        if (object != null) {
            ImageLoader imageLoader = Image.loadPlatformImage(object);
            this.finishImage(imageLoader);
        } else if (this.isBackgroundLoading() && this.impl_source == null) {
            this.loadInBackground();
        } else {
            ImageLoader imageLoader = this.impl_source != null ? Image.loadImage(this.impl_source, this.getRequestedWidth(), this.getRequestedHeight(), this.isPreserveRatio(), this.isSmooth()) : Image.loadImage(this.impl_getUrl(), this.getRequestedWidth(), this.getRequestedHeight(), this.isPreserveRatio(), this.isSmooth());
            this.finishImage(imageLoader);
        }
    }

    private void finishImage(ImageLoader imageLoader) {
        Exception exception = imageLoader.getException();
        if (exception != null) {
            this.finishImage(exception);
            return;
        }
        if (imageLoader.getFrameCount() > 1) {
            this.initializeAnimatedImage(imageLoader);
        } else {
            PlatformImage platformImage = imageLoader.getFrame(0);
            double d2 = (float)imageLoader.getWidth() / platformImage.getPixelScale();
            double d3 = (float)imageLoader.getHeight() / platformImage.getPixelScale();
            this.setPlatformImageWH(platformImage, d2, d3);
        }
        this.setProgress(1.0);
    }

    private void finishImage(Exception exception) {
        this.setException(exception);
        this.setError(true);
        this.setPlatformImageWH(null, 0.0, 0.0);
        this.setProgress(1.0);
    }

    private void initializeAnimatedImage(ImageLoader imageLoader) {
        int n2 = imageLoader.getFrameCount();
        this.animFrames = new PlatformImage[n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            this.animFrames[i2] = imageLoader.getFrame(i2);
        }
        PlatformImage platformImage = imageLoader.getFrame(0);
        double d2 = (float)imageLoader.getWidth() / platformImage.getPixelScale();
        double d3 = (float)imageLoader.getHeight() / platformImage.getPixelScale();
        this.setPlatformImageWH(platformImage, d2, d3);
        this.animation = new Animation(this, imageLoader);
        this.animation.start();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void cycleTasks() {
        Queue<ImageTask> queue = pendingTasks;
        synchronized (queue) {
            --runningTasks;
            ImageTask imageTask = pendingTasks.poll();
            if (imageTask != null) {
                ++runningTasks;
                imageTask.start();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void loadInBackground() {
        this.backgroundTask = new ImageTask();
        Queue<ImageTask> queue = pendingTasks;
        synchronized (queue) {
            if (runningTasks >= 4) {
                pendingTasks.offer(this.backgroundTask);
            } else {
                ++runningTasks;
                this.backgroundTask.start();
            }
        }
    }

    @Deprecated
    public static Image impl_fromPlatformImage(Object object) {
        return new Image(object);
    }

    private void setPlatformImageWH(PlatformImage platformImage, double d2, double d3) {
        if (this.impl_getPlatformImage() == platformImage && this.getWidth() == d2 && this.getHeight() == d3) {
            return;
        }
        Object object = this.impl_getPlatformImage();
        double d4 = this.getWidth();
        double d5 = this.getHeight();
        this.storePlatformImageWH(platformImage, d2, d3);
        if (object != platformImage) {
            this.platformImagePropertyImpl().fireValueChangedEvent();
        }
        if (d4 != d2) {
            this.widthPropertyImpl().fireValueChangedEvent();
        }
        if (d5 != d3) {
            this.heightPropertyImpl().fireValueChangedEvent();
        }
    }

    private void storePlatformImageWH(PlatformImage platformImage, double d2, double d3) {
        this.platformImagePropertyImpl().store(platformImage);
        this.widthPropertyImpl().store(d2);
        this.heightPropertyImpl().store(d3);
    }

    void setPlatformImage(PlatformImage platformImage) {
        this.platformImage.set(platformImage);
    }

    private static ImageLoader loadImage(String string, double d2, double d3, boolean bl, boolean bl2) {
        return Toolkit.getToolkit().loadImage(string, (int)d2, (int)d3, bl, bl2);
    }

    private static ImageLoader loadImage(InputStream inputStream, double d2, double d3, boolean bl, boolean bl2) {
        return Toolkit.getToolkit().loadImage(inputStream, (int)d2, (int)d3, bl, bl2);
    }

    private static AsyncOperation loadImageAsync(AsyncOperationListener<? extends ImageLoader> asyncOperationListener, String string, double d2, double d3, boolean bl, boolean bl2) {
        return Toolkit.getToolkit().loadImageAsync(asyncOperationListener, string, (int)d2, (int)d3, bl, bl2);
    }

    private static ImageLoader loadPlatformImage(Object object) {
        return Toolkit.getToolkit().loadPlatformImage(object);
    }

    private static String validateUrl(String string) {
        if (string == null) {
            throw new NullPointerException("URL must not be null");
        }
        if (string.trim().isEmpty()) {
            throw new IllegalArgumentException("URL must not be empty");
        }
        try {
            if (!URL_QUICKMATCH.matcher(string).matches()) {
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                URL uRL = string.charAt(0) == '/' ? classLoader.getResource(string.substring(1)) : classLoader.getResource(string);
                if (uRL == null) {
                    throw new IllegalArgumentException("Invalid URL or resource not found");
                }
                return uRL.toString();
            }
            return new URL(string).toString();
        }
        catch (IllegalArgumentException illegalArgumentException) {
            throw new IllegalArgumentException(Image.constructDetailedExceptionMessage("Invalid URL", illegalArgumentException), illegalArgumentException);
        }
        catch (MalformedURLException malformedURLException) {
            throw new IllegalArgumentException(Image.constructDetailedExceptionMessage("Invalid URL", malformedURLException), malformedURLException);
        }
    }

    private static InputStream validateInputStream(InputStream inputStream) {
        if (inputStream == null) {
            throw new NullPointerException("Input stream must not be null");
        }
        return inputStream;
    }

    private static String constructDetailedExceptionMessage(String string, Throwable throwable) {
        if (throwable == null) {
            return string;
        }
        String string2 = throwable.getMessage();
        return Image.constructDetailedExceptionMessage(string2 != null ? string + ": " + string2 : string, throwable.getCause());
    }

    boolean isAnimation() {
        return this.animation != null;
    }

    boolean pixelsReadable() {
        return this.getProgress() >= 1.0 && !this.isAnimation() && !this.isError();
    }

    public final PixelReader getPixelReader() {
        if (!this.pixelsReadable()) {
            return null;
        }
        if (this.reader == null) {
            this.reader = new PixelReader(){

                @Override
                public PixelFormat getPixelFormat() {
                    PlatformImage platformImage = (PlatformImage)Image.this.platformImage.get();
                    return platformImage.getPlatformPixelFormat();
                }

                @Override
                public int getArgb(int n2, int n3) {
                    PlatformImage platformImage = (PlatformImage)Image.this.platformImage.get();
                    return platformImage.getArgb(n2, n3);
                }

                @Override
                public Color getColor(int n2, int n3) {
                    int n4 = this.getArgb(n2, n3);
                    int n5 = n4 >>> 24;
                    int n6 = n4 >> 16 & 0xFF;
                    int n7 = n4 >> 8 & 0xFF;
                    int n8 = n4 & 0xFF;
                    return Color.rgb(n6, n7, n8, (double)n5 / 255.0);
                }

                @Override
                public <T extends Buffer> void getPixels(int n2, int n3, int n4, int n5, WritablePixelFormat<T> writablePixelFormat, T t2, int n6) {
                    PlatformImage platformImage = (PlatformImage)Image.this.platformImage.get();
                    platformImage.getPixels(n2, n3, n4, n5, writablePixelFormat, t2, n6);
                }

                @Override
                public void getPixels(int n2, int n3, int n4, int n5, WritablePixelFormat<ByteBuffer> writablePixelFormat, byte[] arrby, int n6, int n7) {
                    PlatformImage platformImage = (PlatformImage)Image.this.platformImage.get();
                    platformImage.getPixels(n2, n3, n4, n5, writablePixelFormat, arrby, n6, n7);
                }

                @Override
                public void getPixels(int n2, int n3, int n4, int n5, WritablePixelFormat<IntBuffer> writablePixelFormat, int[] arrn, int n6, int n7) {
                    PlatformImage platformImage = (PlatformImage)Image.this.platformImage.get();
                    platformImage.getPixels(n2, n3, n4, n5, writablePixelFormat, arrn, n6, n7);
                }
            };
        }
        return this.reader;
    }

    PlatformImage getWritablePlatformImage() {
        PlatformImage platformImage = this.platformImage.get();
        if (!platformImage.isWritable()) {
            platformImage = platformImage.promoteToWritableImage();
            this.platformImage.set(platformImage);
        }
        return platformImage;
    }

    static {
        Toolkit.setImageAccessor(new Toolkit.ImageAccessor(){

            @Override
            public boolean isAnimation(Image image) {
                return image.isAnimation();
            }

            @Override
            public ReadOnlyObjectProperty<PlatformImage> getImageProperty(Image image) {
                return image.acc_platformImageProperty();
            }

            @Override
            public int[] getPreColors(PixelFormat<ByteBuffer> pixelFormat) {
                return ((PixelFormat.IndexedPixelFormat)pixelFormat).getPreColors();
            }

            @Override
            public int[] getNonPreColors(PixelFormat<ByteBuffer> pixelFormat) {
                return ((PixelFormat.IndexedPixelFormat)pixelFormat).getNonPreColors();
            }
        });
        URL_QUICKMATCH = Pattern.compile("^\\p{Alpha}[\\p{Alnum}+.-]*:.*$");
        runningTasks = 0;
        pendingTasks = new LinkedList<ImageTask>();
    }

    private final class ImageTask
    implements AsyncOperationListener<ImageLoader> {
        private final AsyncOperation peer = this.constructPeer();

        @Override
        public void onCancel() {
            Image.this.finishImage(new CancellationException("Loading cancelled"));
            Image.this.cycleTasks();
        }

        @Override
        public void onException(Exception exception) {
            Image.this.finishImage(exception);
            Image.this.cycleTasks();
        }

        @Override
        public void onCompletion(ImageLoader imageLoader) {
            Image.this.finishImage(imageLoader);
            Image.this.cycleTasks();
        }

        @Override
        public void onProgress(int n2, int n3) {
            double d2;
            if (n3 > 0 && (d2 = (double)n2 / (double)n3) < 1.0 && d2 >= Image.this.getProgress() + 0.1) {
                Image.this.setProgress(d2);
            }
        }

        public void start() {
            this.peer.start();
        }

        public void cancel() {
            this.peer.cancel();
        }

        private AsyncOperation constructPeer() {
            return Image.loadImageAsync(this, Image.this.url, Image.this.requestedWidth, Image.this.requestedHeight, Image.this.preserveRatio, Image.this.smooth);
        }
    }

    private static final class Animation {
        final WeakReference<Image> imageRef;
        final Timeline timeline;
        final SimpleIntegerProperty frameIndex = new SimpleIntegerProperty(){

            @Override
            protected void invalidated() {
                this.updateImage(this.get());
            }
        };

        public Animation(Image image, ImageLoader imageLoader) {
            this.imageRef = new WeakReference<Image>(image);
            this.timeline = new Timeline();
            int n2 = imageLoader.getLoopCount();
            this.timeline.setCycleCount(n2 == 0 ? -1 : n2);
            int n3 = imageLoader.getFrameCount();
            int n4 = 0;
            for (int i2 = 0; i2 < n3; ++i2) {
                this.addKeyFrame(i2, n4);
                n4 += imageLoader.getFrameDelay(i2);
            }
            this.timeline.getKeyFrames().add(new KeyFrame(Duration.millis(n4), new KeyValue[0]));
        }

        public void start() {
            this.timeline.play();
        }

        public void stop() {
            this.timeline.stop();
        }

        private void updateImage(int n2) {
            Image image = (Image)this.imageRef.get();
            if (image != null) {
                image.platformImagePropertyImpl().set(image.animFrames[n2]);
            } else {
                this.timeline.stop();
            }
        }

        private void addKeyFrame(int n2, double d2) {
            this.timeline.getKeyFrames().add(new KeyFrame(Duration.millis(d2), new KeyValue(this.frameIndex, n2, Interpolator.DISCRETE)));
        }
    }

    private final class ObjectPropertyImpl<T>
    extends ReadOnlyObjectPropertyBase<T> {
        private final String name;
        private T value;
        private boolean valid = true;

        public ObjectPropertyImpl(String string) {
            this.name = string;
        }

        public void store(T t2) {
            this.value = t2;
        }

        public void set(T t2) {
            if (this.value != t2) {
                this.value = t2;
                this.markInvalid();
            }
        }

        @Override
        public void fireValueChangedEvent() {
            super.fireValueChangedEvent();
        }

        private void markInvalid() {
            if (this.valid) {
                this.valid = false;
                this.fireValueChangedEvent();
            }
        }

        @Override
        public T get() {
            this.valid = true;
            return this.value;
        }

        @Override
        public Object getBean() {
            return Image.this;
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
            return Image.this;
        }

        @Override
        public String getName() {
            return this.name;
        }
    }
}

