/*
 * Decompiled with CFR 0.150.
 */
package javafx.embed.swing;

import com.sun.javafx.application.PlatformImpl;
import java.awt.AlphaComposite;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.SecondaryLoop;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.nio.IntBuffer;
import java.security.AccessController;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.application.Platform;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javax.swing.SwingUtilities;
import sun.awt.AWTAccessor;
import sun.awt.FwDispatcher;
import sun.awt.image.IntegerComponentRaster;

public class SwingFXUtils {
    private static final Set<Object> eventLoopKeys = new HashSet<Object>();

    private SwingFXUtils() {
    }

    public static WritableImage toFXImage(BufferedImage object, WritableImage writableImage) {
        int[] arrn;
        Object object2;
        Object object3;
        int n2 = ((BufferedImage)object).getWidth();
        int n3 = ((BufferedImage)object).getHeight();
        switch (((BufferedImage)object).getType()) {
            case 2: 
            case 3: {
                break;
            }
            default: {
                object3 = new BufferedImage(n2, n3, 3);
                object2 = ((BufferedImage)object3).createGraphics();
                ((Graphics)object2).drawImage((Image)object, 0, 0, null);
                ((Graphics)object2).dispose();
                object = object3;
            }
        }
        if (writableImage != null) {
            int n4 = (int)writableImage.getWidth();
            int n5 = (int)writableImage.getHeight();
            if (n4 < n2 || n5 < n3) {
                writableImage = null;
            } else if (n2 < n4 || n3 < n5) {
                arrn = new int[n4];
                PixelWriter pixelWriter = writableImage.getPixelWriter();
                WritablePixelFormat<IntBuffer> writablePixelFormat = PixelFormat.getIntArgbPreInstance();
                if (n2 < n4) {
                    pixelWriter.setPixels(n2, 0, n4 - n2, n3, writablePixelFormat, arrn, 0, 0);
                }
                if (n3 < n5) {
                    pixelWriter.setPixels(0, n3, n4, n5 - n3, writablePixelFormat, arrn, 0, 0);
                }
            }
        }
        if (writableImage == null) {
            writableImage = new WritableImage(n2, n3);
        }
        object3 = writableImage.getPixelWriter();
        object2 = (IntegerComponentRaster)((BufferedImage)object).getRaster();
        arrn = ((IntegerComponentRaster)object2).getDataStorage();
        int n6 = ((IntegerComponentRaster)object2).getDataOffset(0);
        int n7 = ((IntegerComponentRaster)object2).getScanlineStride();
        WritablePixelFormat<IntBuffer> writablePixelFormat = ((BufferedImage)object).isAlphaPremultiplied() ? PixelFormat.getIntArgbPreInstance() : PixelFormat.getIntArgbInstance();
        object3.setPixels(0, 0, n2, n3, writablePixelFormat, arrn, n6, n7);
        return writableImage;
    }

    private static int getBestBufferedImageType(PixelFormat<?> pixelFormat, BufferedImage bufferedImage) {
        int n2;
        if (bufferedImage != null && ((n2 = bufferedImage.getType()) == 2 || n2 == 3)) {
            return n2;
        }
        switch (pixelFormat.getType()) {
            default: {
                return 3;
            }
            case BYTE_BGRA: 
            case INT_ARGB: {
                return 2;
            }
            case BYTE_RGB: {
                return 1;
            }
            case BYTE_INDEXED: 
        }
        return pixelFormat.isPremultiplied() ? 3 : 2;
    }

    private static WritablePixelFormat<IntBuffer> getAssociatedPixelFormat(BufferedImage bufferedImage) {
        switch (bufferedImage.getType()) {
            case 1: 
            case 3: {
                return PixelFormat.getIntArgbPreInstance();
            }
            case 2: {
                return PixelFormat.getIntArgbInstance();
            }
        }
        throw new InternalError("Failed to validate BufferedImage type");
    }

    public static BufferedImage fromFXImage(javafx.scene.image.Image image, BufferedImage bufferedImage) {
        int n2;
        PixelReader pixelReader = image.getPixelReader();
        if (pixelReader == null) {
            return null;
        }
        int n3 = (int)image.getWidth();
        int n4 = (int)image.getHeight();
        int n5 = SwingFXUtils.getBestBufferedImageType(pixelReader.getPixelFormat(), bufferedImage);
        if (bufferedImage != null) {
            int n6 = bufferedImage.getWidth();
            n2 = bufferedImage.getHeight();
            if (n6 < n3 || n2 < n4 || bufferedImage.getType() != n5) {
                bufferedImage = null;
            } else if (n3 < n6 || n4 < n2) {
                Graphics2D graphics2D = bufferedImage.createGraphics();
                graphics2D.setComposite(AlphaComposite.Clear);
                graphics2D.fillRect(0, 0, n6, n2);
                graphics2D.dispose();
            }
        }
        if (bufferedImage == null) {
            bufferedImage = new BufferedImage(n3, n4, n5);
        }
        IntegerComponentRaster integerComponentRaster = (IntegerComponentRaster)bufferedImage.getRaster();
        n2 = integerComponentRaster.getDataOffset(0);
        int n7 = integerComponentRaster.getScanlineStride();
        int[] arrn = integerComponentRaster.getDataStorage();
        WritablePixelFormat<IntBuffer> writablePixelFormat = SwingFXUtils.getAssociatedPixelFormat(bufferedImage);
        pixelReader.getPixels(0, 0, n3, n4, writablePixelFormat, arrn, n2, n7);
        return bufferedImage;
    }

    static void runOnFxThread(Runnable runnable) {
        if (Platform.isFxApplicationThread()) {
            runnable.run();
        } else {
            Platform.runLater(runnable);
        }
    }

    static void runOnEDT(Runnable runnable) {
        if (SwingUtilities.isEventDispatchThread()) {
            runnable.run();
        } else {
            SwingUtilities.invokeLater(runnable);
        }
    }

    static void runOnEDTAndWait(Object object, Runnable runnable) {
        com.sun.javafx.tk.Toolkit.getToolkit().checkFxUserThread();
        if (SwingUtilities.isEventDispatchThread()) {
            runnable.run();
        } else {
            eventLoopKeys.add(object);
            SwingUtilities.invokeLater(runnable);
            com.sun.javafx.tk.Toolkit.getToolkit().enterNestedEventLoop(object);
        }
    }

    static void leaveFXNestedLoop(Object object) {
        if (!eventLoopKeys.contains(object)) {
            return;
        }
        if (Platform.isFxApplicationThread()) {
            com.sun.javafx.tk.Toolkit.getToolkit().exitNestedEventLoop(object, null);
        } else {
            Platform.runLater(() -> com.sun.javafx.tk.Toolkit.getToolkit().exitNestedEventLoop(object, null));
        }
        eventLoopKeys.remove(object);
    }

    private static EventQueue getEventQueue() {
        return AccessController.doPrivileged(() -> Toolkit.getDefaultToolkit().getSystemEventQueue());
    }

    private static void installFwEventQueue() {
        AWTAccessor.getEventQueueAccessor().setFwDispatcher(SwingFXUtils.getEventQueue(), new FXDispatcher());
    }

    private static void removeFwEventQueue() {
        AWTAccessor.getEventQueueAccessor().setFwDispatcher(SwingFXUtils.getEventQueue(), null);
    }

    private static class FXDispatcher
    implements FwDispatcher {
        private FXDispatcher() {
        }

        @Override
        public boolean isDispatchThread() {
            return Platform.isFxApplicationThread();
        }

        @Override
        public void scheduleDispatch(Runnable runnable) {
            Platform.runLater(runnable);
        }

        @Override
        public SecondaryLoop createSecondaryLoop() {
            return new FwSecondaryLoop();
        }
    }

    private static class FwSecondaryLoop
    implements SecondaryLoop {
        private final AtomicBoolean isRunning = new AtomicBoolean(false);

        private FwSecondaryLoop() {
        }

        @Override
        public boolean enter() {
            if (this.isRunning.compareAndSet(false, true)) {
                PlatformImpl.runAndWait(() -> com.sun.javafx.tk.Toolkit.getToolkit().enterNestedEventLoop(this));
                return true;
            }
            return false;
        }

        @Override
        public boolean exit() {
            if (this.isRunning.compareAndSet(true, false)) {
                PlatformImpl.runAndWait(() -> com.sun.javafx.tk.Toolkit.getToolkit().exitNestedEventLoop(this, null));
                return true;
            }
            return false;
        }
    }
}

