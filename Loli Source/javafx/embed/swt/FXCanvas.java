/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.SWT
 *  org.eclipse.swt.dnd.DragSource
 *  org.eclipse.swt.dnd.DragSourceEvent
 *  org.eclipse.swt.dnd.DragSourceListener
 *  org.eclipse.swt.dnd.DropTarget
 *  org.eclipse.swt.dnd.DropTargetEvent
 *  org.eclipse.swt.dnd.DropTargetListener
 *  org.eclipse.swt.dnd.FileTransfer
 *  org.eclipse.swt.dnd.HTMLTransfer
 *  org.eclipse.swt.dnd.ImageTransfer
 *  org.eclipse.swt.dnd.RTFTransfer
 *  org.eclipse.swt.dnd.TextTransfer
 *  org.eclipse.swt.dnd.Transfer
 *  org.eclipse.swt.dnd.TransferData
 *  org.eclipse.swt.dnd.URLTransfer
 *  org.eclipse.swt.events.ControlEvent
 *  org.eclipse.swt.events.ControlListener
 *  org.eclipse.swt.events.DisposeEvent
 *  org.eclipse.swt.events.DisposeListener
 *  org.eclipse.swt.events.FocusEvent
 *  org.eclipse.swt.events.FocusListener
 *  org.eclipse.swt.events.KeyEvent
 *  org.eclipse.swt.events.KeyListener
 *  org.eclipse.swt.events.MenuDetectEvent
 *  org.eclipse.swt.events.MouseEvent
 *  org.eclipse.swt.events.MouseListener
 *  org.eclipse.swt.events.MouseTrackListener
 *  org.eclipse.swt.events.PaintEvent
 *  org.eclipse.swt.graphics.Cursor
 *  org.eclipse.swt.graphics.Device
 *  org.eclipse.swt.graphics.Image
 *  org.eclipse.swt.graphics.ImageData
 *  org.eclipse.swt.graphics.PaletteData
 *  org.eclipse.swt.graphics.Point
 *  org.eclipse.swt.graphics.RGB
 *  org.eclipse.swt.graphics.Rectangle
 *  org.eclipse.swt.widgets.Canvas
 *  org.eclipse.swt.widgets.Composite
 *  org.eclipse.swt.widgets.Control
 *  org.eclipse.swt.widgets.Display
 *  org.eclipse.swt.widgets.Listener
 *  org.eclipse.swt.widgets.Shell
 */
package javafx.embed.swt;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Pixels;
import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.cursor.CursorFrame;
import com.sun.javafx.cursor.CursorType;
import com.sun.javafx.embed.EmbeddedSceneDSInterface;
import com.sun.javafx.embed.EmbeddedSceneDTInterface;
import com.sun.javafx.embed.EmbeddedSceneInterface;
import com.sun.javafx.embed.EmbeddedStageInterface;
import com.sun.javafx.embed.HostInterface;
import com.sun.javafx.stage.EmbeddedWindow;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.embed.swt.CustomTransfer;
import javafx.embed.swt.SWTCursors;
import javafx.embed.swt.SWTEvents;
import javafx.scene.Scene;
import javafx.scene.input.TransferMode;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.HTMLTransfer;
import org.eclipse.swt.dnd.ImageTransfer;
import org.eclipse.swt.dnd.RTFTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.dnd.URLTransfer;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class FXCanvas
extends Canvas {
    private HostContainer hostContainer;
    private volatile EmbeddedWindow stage;
    private volatile Scene scene;
    private EmbeddedStageInterface stagePeer;
    private EmbeddedSceneInterface scenePeer;
    private int pWidth = 0;
    private int pHeight = 0;
    private volatile int pPreferredWidth = -1;
    private volatile int pPreferredHeight = -1;
    private IntBuffer pixelsBuf = null;
    Listener moveFilter = event -> {
        for (FXCanvas fXCanvas = this; fXCanvas != null; fXCanvas = fXCanvas.getParent()) {
            if (fXCanvas != event.widget) continue;
            this.sendMoveEventToFX();
            break;
        }
    };
    private DropTarget dropTarget;
    static Transfer[] StandardTransfers = new Transfer[]{TextTransfer.getInstance(), RTFTransfer.getInstance(), HTMLTransfer.getInstance(), URLTransfer.getInstance(), ImageTransfer.getInstance(), FileTransfer.getInstance()};
    static Transfer[] CustomTransfers = new Transfer[0];
    private static Field windowField;
    private static Method windowMethod;
    private static Method screenMethod;
    private static Method backingScaleFactorMethod;
    static ArrayList<DropTarget> targets;
    double lastScaleFactor = 1.0;
    int lastWidth;
    int lastHeight;
    IntBuffer lastPixelsBuf = null;

    private double getScaleFactor() {
        if (SWT.getPlatform().equals("cocoa")) {
            if (windowField == null || screenMethod == null || backingScaleFactorMethod == null) {
                return 1.0;
            }
            try {
                Object object = windowField.get((Object)this.getShell());
                Object object2 = screenMethod.invoke(object, new Object[0]);
                Object object3 = backingScaleFactorMethod.invoke(object2, new Object[0]);
                return (Double)object3;
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        return 1.0;
    }

    static Transfer[] getAllTransfers() {
        Transfer[] arrtransfer = new Transfer[StandardTransfers.length + CustomTransfers.length];
        System.arraycopy(StandardTransfers, 0, arrtransfer, 0, StandardTransfers.length);
        System.arraycopy(CustomTransfers, 0, arrtransfer, StandardTransfers.length, CustomTransfers.length);
        return arrtransfer;
    }

    static Transfer getCustomTransfer(String string) {
        for (int i2 = 0; i2 < CustomTransfers.length; ++i2) {
            if (!((CustomTransfer)CustomTransfers[i2]).getMime().equals(string)) continue;
            return CustomTransfers[i2];
        }
        CustomTransfer customTransfer = new CustomTransfer(string, string);
        Transfer[] arrtransfer = new Transfer[CustomTransfers.length + 1];
        System.arraycopy(CustomTransfers, 0, arrtransfer, 0, CustomTransfers.length);
        arrtransfer[FXCanvas.CustomTransfers.length] = customTransfer;
        CustomTransfers = arrtransfer;
        return customTransfer;
    }

    public FXCanvas(@NamedArg(value="parent") Composite composite, @NamedArg(value="style") int n2) {
        super(composite, n2 | 0x40000);
        FXCanvas.initFx();
        this.hostContainer = new HostContainer();
        this.registerEventListeners();
        Display display = composite.getDisplay();
        display.addFilter(10, this.moveFilter);
    }

    private static void initFx() {
        AccessController.doPrivileged(() -> {
            System.setProperty("javafx.embed.isEventThread", "true");
            System.setProperty("glass.win.uiScale", "100%");
            System.setProperty("glass.win.renderScale", "100%");
            return null;
        });
        HashMap<String, Long> hashMap = Application.getDeviceDetails();
        if (hashMap == null) {
            hashMap = new HashMap<String, Long>();
            Application.setDeviceDetails(hashMap);
        }
        if (hashMap.get("javafx.embed.eventProc") == null) {
            long l2 = 0L;
            try {
                Field field = Display.class.getDeclaredField("eventProc");
                field.setAccessible(true);
                if (field.getType() == Integer.TYPE) {
                    l2 = field.getLong((Object)Display.getDefault());
                } else if (field.getType() == Long.TYPE) {
                    l2 = field.getLong((Object)Display.getDefault());
                }
            }
            catch (Throwable throwable) {
                // empty catch block
            }
            hashMap.put("javafx.embed.eventProc", l2);
        }
        PlatformImpl.startup(() -> Application.GetApplication().setName(Display.getAppName()));
    }

    DropTarget getDropTarget() {
        return this.dropTarget;
    }

    void setDropTarget(DropTarget dropTarget) {
        if (this.dropTarget != null) {
            targets.remove((Object)this.dropTarget);
            this.dropTarget.dispose();
        }
        this.dropTarget = dropTarget;
        if (this.dropTarget != null) {
            targets.add(this.dropTarget);
        }
    }

    static void updateDropTarget() {
        for (DropTarget dropTarget : targets) {
            dropTarget.setTransfer(FXCanvas.getAllTransfers());
        }
    }

    public Point computeSize(int n2, int n3, boolean bl) {
        this.checkWidget();
        if (n2 == -1 && n3 == -1 && this.pPreferredWidth != -1 && this.pPreferredHeight != -1) {
            return new Point(this.pPreferredWidth, this.pPreferredHeight);
        }
        return super.computeSize(n2, n3, bl);
    }

    public Scene getScene() {
        this.checkWidget();
        return this.scene;
    }

    public void setScene(Scene scene) {
        this.checkWidget();
        if (this.stage == null && scene != null) {
            this.stage = new EmbeddedWindow(this.hostContainer);
            this.stage.show();
        }
        this.scene = scene;
        if (this.stage != null) {
            this.stage.setScene(scene);
        }
        if (this.stage != null && scene == null) {
            this.stage.hide();
            this.stage = null;
        }
    }

    private void registerEventListeners() {
        this.addDisposeListener(new DisposeListener(){

            public void widgetDisposed(DisposeEvent disposeEvent) {
                Display display = FXCanvas.this.getDisplay();
                display.removeFilter(10, FXCanvas.this.moveFilter);
                FXCanvas.this.widgetDisposed(disposeEvent);
            }
        });
        this.addPaintListener(paintEvent -> this.paintControl(paintEvent));
        this.addMouseListener(new MouseListener(){

            public void mouseDoubleClick(MouseEvent mouseEvent) {
            }

            public void mouseDown(MouseEvent mouseEvent) {
                if (mouseEvent.button > 3) {
                    return;
                }
                FXCanvas.this.sendMouseEventToFX(mouseEvent, 0);
            }

            public void mouseUp(MouseEvent mouseEvent) {
                if (mouseEvent.button > 3) {
                    return;
                }
                FXCanvas.this.sendMouseEventToFX(mouseEvent, 1);
            }
        });
        this.addMouseMoveListener(mouseEvent -> {
            if ((mouseEvent.stateMask & SWT.BUTTON_MASK) != 0) {
                if ((mouseEvent.stateMask & 0x380000) != 0) {
                    this.sendMouseEventToFX(mouseEvent, 6);
                } else {
                    this.sendMouseEventToFX(mouseEvent, 5);
                }
            } else {
                this.sendMouseEventToFX(mouseEvent, 5);
            }
        });
        this.addMouseWheelListener(mouseEvent -> this.sendMouseEventToFX(mouseEvent, 7));
        this.addMouseTrackListener(new MouseTrackListener(){

            public void mouseEnter(MouseEvent mouseEvent) {
                FXCanvas.this.sendMouseEventToFX(mouseEvent, 3);
            }

            public void mouseExit(MouseEvent mouseEvent) {
                FXCanvas.this.sendMouseEventToFX(mouseEvent, 4);
            }

            public void mouseHover(MouseEvent mouseEvent) {
            }
        });
        this.addControlListener(new ControlListener(){

            public void controlMoved(ControlEvent controlEvent) {
                FXCanvas.this.sendMoveEventToFX();
            }

            public void controlResized(ControlEvent controlEvent) {
                FXCanvas.this.sendResizeEventToFX();
            }
        });
        this.addFocusListener(new FocusListener(){

            public void focusGained(FocusEvent focusEvent) {
                FXCanvas.this.sendFocusEventToFX(focusEvent, true);
            }

            public void focusLost(FocusEvent focusEvent) {
                FXCanvas.this.sendFocusEventToFX(focusEvent, false);
            }
        });
        this.addKeyListener(new KeyListener(){

            public void keyPressed(KeyEvent keyEvent) {
                FXCanvas.this.sendKeyEventToFX(keyEvent, 1);
            }

            public void keyReleased(KeyEvent keyEvent) {
                FXCanvas.this.sendKeyEventToFX(keyEvent, 2);
            }
        });
        this.addMenuDetectListener(menuDetectEvent -> {
            Runnable runnable = () -> {
                if (this.isDisposed()) {
                    return;
                }
                this.sendMenuEventToFX(menuDetectEvent);
            };
            if ("cocoa".equals(SWT.getPlatform())) {
                this.getDisplay().asyncExec(runnable);
            } else {
                runnable.run();
            }
        });
    }

    private void widgetDisposed(DisposeEvent disposeEvent) {
        this.setDropTarget(null);
        if (this.stage != null) {
            this.stage.hide();
        }
    }

    private void paintControl(PaintEvent paintEvent) {
        PaletteData paletteData;
        if (this.scenePeer == null || this.pixelsBuf == null) {
            return;
        }
        double d2 = this.getScaleFactor();
        if (this.lastScaleFactor != d2) {
            this.resizePixelBuffer(d2);
            this.lastScaleFactor = d2;
            this.scenePeer.setPixelScaleFactor((float)d2);
        }
        IntBuffer intBuffer = this.pixelsBuf;
        int n2 = this.pWidth;
        int n3 = this.pHeight;
        if (this.scenePeer.getPixels(this.pixelsBuf, this.pWidth, this.pHeight)) {
            n2 = this.lastWidth = this.pWidth;
            n3 = this.lastHeight = this.pHeight;
            intBuffer = this.lastPixelsBuf = this.pixelsBuf;
        } else {
            if (this.lastPixelsBuf == null) {
                return;
            }
            n2 = this.lastWidth;
            n3 = this.lastHeight;
            intBuffer = this.lastPixelsBuf;
        }
        n2 = (int)Math.round((double)n2 * d2);
        n3 = (int)Math.round((double)n3 * d2);
        ImageData imageData = null;
        if ("win32".equals(SWT.getPlatform())) {
            paletteData = new PaletteData(65280, 0xFF0000, -16777216);
            int n4 = n2 * 4;
            byte[] arrby = new byte[n4 * n3];
            int[] arrn = intBuffer.array();
            int n5 = 0;
            int n6 = 0;
            for (int i2 = 0; i2 < n3; ++i2) {
                for (int i3 = 0; i3 < n2; ++i3) {
                    int n7 = arrn[n6++];
                    arrby[n5++] = (byte)(n7 & 0xFF);
                    arrby[n5++] = (byte)(n7 >> 8 & 0xFF);
                    arrby[n5++] = (byte)(n7 >> 16 & 0xFF);
                    arrby[n5++] = 0;
                }
            }
            imageData = new ImageData(n2, n3, 32, paletteData, 4, arrby);
        } else {
            if (n2 * n3 > intBuffer.array().length) {
                System.err.println("FXCanvas.paintControl: scale mismatch!");
                return;
            }
            paletteData = new PaletteData(0xFF0000, 65280, 255);
            imageData = new ImageData(n2, n3, 32, paletteData);
            imageData.setPixels(0, 0, n2 * n3, intBuffer.array(), 0);
        }
        paletteData = new Image((Device)Display.getDefault(), imageData);
        paintEvent.gc.drawImage((Image)paletteData, 0, 0, n2, n3, 0, 0, this.pWidth, this.pHeight);
        paletteData.dispose();
    }

    private void sendMoveEventToFX() {
        if (this.stagePeer == null) {
            return;
        }
        Rectangle rectangle = this.getClientArea();
        Point point = this.toDisplay(rectangle.x, rectangle.y);
        this.stagePeer.setLocation(point.x, point.y);
    }

    private void sendMouseEventToFX(MouseEvent mouseEvent, int n2) {
        if (this.scenePeer == null) {
            return;
        }
        Point point = this.toDisplay(mouseEvent.x, mouseEvent.y);
        boolean bl = (mouseEvent.stateMask & 0x80000) != 0;
        boolean bl2 = (mouseEvent.stateMask & 0x100000) != 0;
        boolean bl3 = (mouseEvent.stateMask & 0x200000) != 0;
        boolean bl4 = (mouseEvent.stateMask & 0x20000) != 0;
        boolean bl5 = (mouseEvent.stateMask & 0x40000) != 0;
        boolean bl6 = (mouseEvent.stateMask & 0x10000) != 0;
        boolean bl7 = (mouseEvent.stateMask & 0x400000) != 0;
        int n3 = mouseEvent.button;
        switch (n2) {
            case 0: {
                bl |= mouseEvent.button == 1;
                bl2 |= mouseEvent.button == 2;
                bl3 |= mouseEvent.button == 3;
                break;
            }
            case 1: {
                bl &= mouseEvent.button != 1;
                bl2 &= mouseEvent.button != 2;
                bl3 &= mouseEvent.button != 3;
                break;
            }
            case 2: {
                return;
            }
            case 3: 
            case 4: 
            case 5: 
            case 6: {
                if (n3 != 0) break;
                if ((mouseEvent.stateMask & 0x80000) != 0) {
                    n3 = 1;
                    break;
                }
                if ((mouseEvent.stateMask & 0x100000) != 0) {
                    n3 = 2;
                    break;
                }
                if ((mouseEvent.stateMask & 0x200000) == 0) break;
                n3 = 3;
                break;
            }
        }
        this.scenePeer.mouseEvent(n2, SWTEvents.mouseButtonToEmbedMouseButton(n3, mouseEvent.stateMask), bl, bl2, bl3, mouseEvent.x, mouseEvent.y, point.x, point.y, bl4, bl5, bl6, bl7, SWTEvents.getWheelRotation(mouseEvent, n2), false);
    }

    private void sendKeyEventToFX(KeyEvent keyEvent, int n2) {
        if (this.scenePeer == null) {
            return;
        }
        int n3 = keyEvent.stateMask;
        if (n2 == 1) {
            if (keyEvent.keyCode == 131072) {
                n3 |= 0x20000;
            }
            if (keyEvent.keyCode == 262144) {
                n3 |= 0x40000;
            }
            if (keyEvent.keyCode == 65536) {
                n3 |= 0x10000;
            }
            if (keyEvent.keyCode == 0x400000) {
                n3 |= 0x400000;
            }
        } else {
            if (keyEvent.keyCode == 131072) {
                n3 &= 0xFFFDFFFF;
            }
            if (keyEvent.keyCode == 262144) {
                n3 &= 0xFFFBFFFF;
            }
            if (keyEvent.keyCode == 65536) {
                n3 &= 0xFFFEFFFF;
            }
            if (keyEvent.keyCode == 0x400000) {
                n3 &= 0xFFBFFFFF;
            }
        }
        int n4 = SWTEvents.keyCodeToEmbedKeyCode(keyEvent.keyCode);
        this.scenePeer.keyEvent(SWTEvents.keyIDToEmbedKeyType(n2), n4, new char[0], SWTEvents.keyModifiersToEmbedKeyModifiers(n3));
        if (keyEvent.character != '\u0000' && n2 == 1) {
            char[] arrc = new char[]{keyEvent.character};
            this.scenePeer.keyEvent(2, keyEvent.keyCode, arrc, SWTEvents.keyModifiersToEmbedKeyModifiers(n3));
        }
    }

    private void sendMenuEventToFX(MenuDetectEvent menuDetectEvent) {
        if (this.scenePeer == null) {
            return;
        }
        Point point = this.toControl(menuDetectEvent.x, menuDetectEvent.y);
        this.scenePeer.menuEvent(point.x, point.y, menuDetectEvent.x, menuDetectEvent.y, false);
    }

    private void sendResizeEventToFX() {
        this.redraw();
        this.update();
        this.pWidth = this.getClientArea().width;
        this.pHeight = this.getClientArea().height;
        this.resizePixelBuffer(this.lastScaleFactor);
        if (this.scenePeer == null) {
            return;
        }
        this.stagePeer.setSize(this.pWidth, this.pHeight);
        this.scenePeer.setSize(this.pWidth, this.pHeight);
    }

    private void resizePixelBuffer(double d2) {
        this.lastPixelsBuf = null;
        if (this.pWidth <= 0 || this.pHeight <= 0) {
            this.pixelsBuf = null;
        } else {
            this.pixelsBuf = IntBuffer.allocate((int)Math.round((double)this.pWidth * d2) * (int)Math.round((double)this.pHeight * d2));
            RGB rGB = this.getBackground().getRGB();
            Arrays.fill(this.pixelsBuf.array(), rGB.red << 16 | rGB.green << 8 | rGB.blue);
        }
    }

    private void sendFocusEventToFX(FocusEvent focusEvent, boolean bl) {
        if (this.stage == null || this.stagePeer == null) {
            return;
        }
        int n2 = bl ? 0 : 3;
        this.stagePeer.setFocused(bl, n2);
    }

    static {
        if (SWT.getPlatform().equals("cocoa")) {
            try {
                windowField = Shell.class.getDeclaredField("window");
                windowField.setAccessible(true);
                Class<?> class_ = Class.forName("org.eclipse.swt.internal.cocoa.NSView");
                windowMethod = class_.getDeclaredMethod("window", new Class[0]);
                windowMethod.setAccessible(true);
                Class<?> class_2 = Class.forName("org.eclipse.swt.internal.cocoa.NSWindow");
                screenMethod = class_2.getDeclaredMethod("screen", new Class[0]);
                screenMethod.setAccessible(true);
                Class<?> class_3 = Class.forName("org.eclipse.swt.internal.cocoa.NSScreen");
                backingScaleFactorMethod = class_3.getDeclaredMethod("backingScaleFactor", new Class[0]);
                backingScaleFactorMethod.setAccessible(true);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        targets = new ArrayList();
    }

    private class HostContainer
    implements HostInterface {
        Object lock = new Object();
        boolean queued = false;

        private HostContainer() {
        }

        @Override
        public void setEmbeddedStage(EmbeddedStageInterface embeddedStageInterface) {
            FXCanvas.this.stagePeer = embeddedStageInterface;
            if (FXCanvas.this.stagePeer == null) {
                return;
            }
            if (FXCanvas.this.pWidth > 0 && FXCanvas.this.pHeight > 0) {
                FXCanvas.this.stagePeer.setSize(FXCanvas.this.pWidth, FXCanvas.this.pHeight);
            }
            if (FXCanvas.this.isFocusControl()) {
                FXCanvas.this.stagePeer.setFocused(true, 0);
            }
            FXCanvas.this.sendMoveEventToFX();
            FXCanvas.this.sendResizeEventToFX();
        }

        TransferMode getTransferMode(int n2) {
            switch (n2) {
                case 1: {
                    return TransferMode.COPY;
                }
                case 2: 
                case 8: {
                    return TransferMode.MOVE;
                }
                case 4: {
                    return TransferMode.LINK;
                }
            }
            return null;
        }

        Set<TransferMode> getTransferModes(int n2) {
            HashSet<TransferMode> hashSet = new HashSet<TransferMode>();
            if ((n2 & 1) != 0) {
                hashSet.add(TransferMode.COPY);
            }
            if ((n2 & 2) != 0) {
                hashSet.add(TransferMode.MOVE);
            }
            if ((n2 & 8) != 0) {
                hashSet.add(TransferMode.MOVE);
            }
            if ((n2 & 4) != 0) {
                hashSet.add(TransferMode.LINK);
            }
            return hashSet;
        }

        ImageData createImageData(Pixels pixels) {
            int n2;
            Buffer buffer;
            if (pixels == null) {
                return null;
            }
            int n3 = pixels.getWidth();
            int n4 = pixels.getHeight();
            int n5 = n3 * 4;
            int n6 = n5 * n4;
            byte[] arrby = new byte[n6];
            byte[] arrby2 = new byte[n3 * n4];
            if (pixels.getBytesPerComponent() == 1) {
                buffer = (ByteBuffer)pixels.getPixels();
                int n7 = 0;
                int n8 = 0;
                for (n2 = 0; n2 < n4; ++n2) {
                    int n9 = 0;
                    while (n9 < n3) {
                        byte by = ((ByteBuffer)buffer).get();
                        byte by2 = ((ByteBuffer)buffer).get();
                        byte by3 = ((ByteBuffer)buffer).get();
                        byte by4 = ((ByteBuffer)buffer).get();
                        arrby2[n8++] = by4;
                        arrby[n7] = by;
                        arrby[n7 + 1] = by2;
                        arrby[n7 + 2] = by3;
                        arrby[n7 + 3] = 0;
                        ++n9;
                        n7 += 4;
                    }
                }
            } else if (pixels.getBytesPerComponent() == 4) {
                buffer = (IntBuffer)pixels.getPixels();
                int n10 = 0;
                int n11 = 0;
                for (n2 = 0; n2 < n4; ++n2) {
                    int n12 = 0;
                    while (n12 < n3) {
                        int n13 = ((IntBuffer)buffer).get();
                        byte by = (byte)(n13 & 0xFF);
                        byte by5 = (byte)(n13 >> 8 & 0xFF);
                        byte by6 = (byte)(n13 >> 16 & 0xFF);
                        byte by7 = (byte)(n13 >> 24 & 0xFF);
                        arrby2[n11++] = by7;
                        arrby[n10] = by;
                        arrby[n10 + 1] = by5;
                        arrby[n10 + 2] = by6;
                        arrby[n10 + 3] = 0;
                        ++n12;
                        n10 += 4;
                    }
                }
            } else {
                return null;
            }
            buffer = new PaletteData(65280, 0xFF0000, -16777216);
            ImageData imageData = new ImageData(n3, n4, 32, (PaletteData)buffer, 4, arrby);
            imageData.alphaData = arrby2;
            return imageData;
        }

        private DragSource createDragSource(final EmbeddedSceneDSInterface embeddedSceneDSInterface, TransferMode transferMode) {
            Transfer[] arrtransfer = this.getTransferTypes(embeddedSceneDSInterface.getMimeTypes());
            if (arrtransfer.length == 0) {
                return null;
            }
            int n2 = this.getDragActions(embeddedSceneDSInterface.getSupportedActions());
            final DragSource dragSource = new DragSource((Control)FXCanvas.this, n2);
            dragSource.setTransfer(arrtransfer);
            dragSource.addDragListener(new DragSourceListener(){

                public void dragFinished(DragSourceEvent dragSourceEvent) {
                    dragSource.dispose();
                    embeddedSceneDSInterface.dragDropEnd(HostContainer.this.getTransferMode(dragSourceEvent.detail));
                }

                public void dragSetData(DragSourceEvent dragSourceEvent) {
                    Transfer[] arrtransfer = dragSource.getTransfer();
                    for (int i2 = 0; i2 < arrtransfer.length; ++i2) {
                        String string;
                        if (arrtransfer[i2].isSupportedType(dragSourceEvent.dataType) && (string = HostContainer.this.getMime(arrtransfer[i2])) != null) {
                            dragSourceEvent.doit = true;
                            dragSourceEvent.data = embeddedSceneDSInterface.getData(string);
                            if (dragSourceEvent.data instanceof Pixels) {
                                dragSourceEvent.data = HostContainer.this.createImageData((Pixels)dragSourceEvent.data);
                            }
                            return;
                        }
                        dragSourceEvent.doit = false;
                    }
                }

                public void dragStart(DragSourceEvent dragSourceEvent) {
                }
            });
            return dragSource;
        }

        int getDragAction(TransferMode transferMode) {
            if (transferMode == null) {
                return 0;
            }
            switch (transferMode) {
                case COPY: {
                    return 1;
                }
                case MOVE: {
                    return 2;
                }
                case LINK: {
                    return 4;
                }
            }
            throw new IllegalArgumentException("Invalid transfer mode");
        }

        int getDragActions(Set<TransferMode> set) {
            int n2 = 0;
            for (TransferMode transferMode : set) {
                n2 |= this.getDragAction(transferMode);
            }
            return n2;
        }

        Transfer getTransferType(String string) {
            if (string.equals("text/plain")) {
                return TextTransfer.getInstance();
            }
            if (string.equals("text/rtf")) {
                return RTFTransfer.getInstance();
            }
            if (string.equals("text/html")) {
                return HTMLTransfer.getInstance();
            }
            if (string.equals("text/uri-list")) {
                return URLTransfer.getInstance();
            }
            if (string.equals("application/x-java-rawimage")) {
                return ImageTransfer.getInstance();
            }
            if (string.equals("application/x-java-file-list") || string.equals("java.file-list")) {
                return FileTransfer.getInstance();
            }
            return FXCanvas.getCustomTransfer(string);
        }

        Transfer[] getTransferTypes(String[] arrstring) {
            int n2 = 0;
            Transfer[] arrtransfer = new Transfer[arrstring.length];
            for (int i2 = 0; i2 < arrstring.length; ++i2) {
                Transfer transfer = this.getTransferType(arrstring[i2]);
                if (transfer == null) continue;
                arrtransfer[n2++] = transfer;
            }
            if (n2 != arrstring.length) {
                Transfer[] arrtransfer2 = new Transfer[n2];
                System.arraycopy(arrtransfer, 0, arrtransfer2, 0, n2);
                arrtransfer = arrtransfer2;
            }
            return arrtransfer;
        }

        String getMime(Transfer transfer) {
            if (transfer.equals((Object)TextTransfer.getInstance())) {
                return "text/plain";
            }
            if (transfer.equals((Object)RTFTransfer.getInstance())) {
                return "text/rtf";
            }
            if (transfer.equals((Object)HTMLTransfer.getInstance())) {
                return "text/html";
            }
            if (transfer.equals((Object)URLTransfer.getInstance())) {
                return "text/uri-list";
            }
            if (transfer.equals((Object)ImageTransfer.getInstance())) {
                return "application/x-java-rawimage";
            }
            if (transfer.equals((Object)FileTransfer.getInstance())) {
                return "application/x-java-file-list";
            }
            if (transfer instanceof CustomTransfer) {
                return ((CustomTransfer)transfer).getMime();
            }
            return null;
        }

        String[] getMimes(Transfer[] arrtransfer, TransferData transferData) {
            int n2 = 0;
            String[] arrstring = new String[arrtransfer.length];
            for (int i2 = 0; i2 < arrtransfer.length; ++i2) {
                if (!arrtransfer[i2].isSupportedType(transferData)) continue;
                arrstring[n2++] = this.getMime(arrtransfer[i2]);
            }
            if (n2 != arrstring.length) {
                String[] arrstring2 = new String[n2];
                System.arraycopy(arrstring, 0, arrstring2, 0, n2);
                arrstring = arrstring2;
            }
            return arrstring;
        }

        DropTarget createDropTarget(EmbeddedSceneInterface embeddedSceneInterface) {
            final DropTarget dropTarget = new DropTarget((Control)FXCanvas.this, 7);
            final EmbeddedSceneDTInterface embeddedSceneDTInterface = embeddedSceneInterface.createDropTarget();
            dropTarget.setTransfer(FXCanvas.getAllTransfers());
            dropTarget.addDropListener(new DropTargetListener(){
                Object data;
                TransferData currentTransferData;
                boolean ignoreLeave;
                int detail = 0;
                int operations = 0;
                EmbeddedSceneDSInterface fxDragSource = new EmbeddedSceneDSInterface(){

                    @Override
                    public Set<TransferMode> getSupportedActions() {
                        return HostContainer.this.getTransferModes(operations);
                    }

                    @Override
                    public Object getData(String string) {
                        return data;
                    }

                    @Override
                    public String[] getMimeTypes() {
                        if (currentTransferData == null) {
                            return new String[0];
                        }
                        return HostContainer.this.getMimes(FXCanvas.getAllTransfers(), currentTransferData);
                    }

                    @Override
                    public boolean isMimeTypeAvailable(String string) {
                        String[] arrstring = this.getMimeTypes();
                        for (int i2 = 0; i2 < arrstring.length; ++i2) {
                            if (!arrstring[i2].equals(string)) continue;
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public void dragDropEnd(TransferMode transferMode) {
                        data = null;
                        currentTransferData = null;
                    }
                };

                public void dragEnter(DropTargetEvent dropTargetEvent) {
                    this.ignoreLeave = false;
                    dropTarget.setTransfer(FXCanvas.getAllTransfers());
                    this.detail = dropTargetEvent.detail;
                    this.operations = dropTargetEvent.operations;
                    this.dragOver(dropTargetEvent, true, this.detail);
                }

                public void dragLeave(DropTargetEvent dropTargetEvent) {
                    this.operations = 0;
                    this.detail = 0;
                    this.data = null;
                    this.currentTransferData = null;
                    FXCanvas.this.getDisplay().asyncExec(() -> {
                        if (this.ignoreLeave) {
                            return;
                        }
                        embeddedSceneDTInterface.handleDragLeave();
                    });
                }

                public void dragOperationChanged(DropTargetEvent dropTargetEvent) {
                    this.detail = dropTargetEvent.detail;
                    this.operations = dropTargetEvent.operations;
                    this.dragOver(dropTargetEvent, false, this.detail);
                }

                public void dragOver(DropTargetEvent dropTargetEvent) {
                    this.operations = dropTargetEvent.operations;
                    this.dragOver(dropTargetEvent, false, this.detail);
                }

                public void dragOver(DropTargetEvent dropTargetEvent, boolean bl, int n2) {
                    this.currentTransferData = dropTargetEvent.currentDataType;
                    Point point = FXCanvas.this.toControl(dropTargetEvent.x, dropTargetEvent.y);
                    if (n2 == 0) {
                        n2 = 1;
                    }
                    TransferMode transferMode = HostContainer.this.getTransferMode(n2);
                    TransferMode transferMode2 = bl ? embeddedSceneDTInterface.handleDragEnter(point.x, point.y, dropTargetEvent.x, dropTargetEvent.y, transferMode, this.fxDragSource) : embeddedSceneDTInterface.handleDragOver(point.x, point.y, dropTargetEvent.x, dropTargetEvent.y, transferMode);
                    dropTargetEvent.detail = HostContainer.this.getDragAction(transferMode2);
                }

                public void drop(DropTargetEvent dropTargetEvent) {
                    this.detail = dropTargetEvent.detail;
                    this.operations = dropTargetEvent.operations;
                    this.data = dropTargetEvent.data;
                    this.currentTransferData = dropTargetEvent.currentDataType;
                    Point point = FXCanvas.this.toControl(dropTargetEvent.x, dropTargetEvent.y);
                    TransferMode transferMode = HostContainer.this.getTransferMode(dropTargetEvent.detail);
                    TransferMode transferMode2 = embeddedSceneDTInterface.handleDragDrop(point.x, point.y, dropTargetEvent.x, dropTargetEvent.y, transferMode);
                    dropTargetEvent.detail = HostContainer.this.getDragAction(transferMode2);
                    this.data = null;
                    this.currentTransferData = null;
                }

                public void dropAccept(DropTargetEvent dropTargetEvent) {
                    this.ignoreLeave = true;
                }
            });
            return dropTarget;
        }

        @Override
        public void setEmbeddedScene(EmbeddedSceneInterface embeddedSceneInterface) {
            FXCanvas.this.scenePeer = embeddedSceneInterface;
            if (FXCanvas.this.scenePeer == null) {
                return;
            }
            if (FXCanvas.this.pWidth > 0 && FXCanvas.this.pHeight > 0) {
                FXCanvas.this.scenePeer.setSize(FXCanvas.this.pWidth, FXCanvas.this.pHeight);
            }
            double d2 = FXCanvas.this.getScaleFactor();
            FXCanvas.this.resizePixelBuffer(d2);
            FXCanvas.this.lastScaleFactor = d2;
            FXCanvas.this.scenePeer.setPixelScaleFactor((float)d2);
            FXCanvas.this.scenePeer.setDragStartListener((embeddedSceneDSInterface, transferMode) -> Platform.runLater(() -> {
                DragSource dragSource = this.createDragSource(embeddedSceneDSInterface, transferMode);
                if (dragSource == null) {
                    embeddedSceneDSInterface.dragDropEnd(null);
                } else {
                    FXCanvas.updateDropTarget();
                    FXCanvas.this.notifyListeners(29, null);
                }
            }));
            FXCanvas.this.setDropTarget(null);
            FXCanvas.this.setDropTarget(this.createDropTarget(embeddedSceneInterface));
        }

        @Override
        public boolean requestFocus() {
            Display.getDefault().asyncExec(() -> {
                if (FXCanvas.this.isDisposed()) {
                    return;
                }
                FXCanvas.this.forceFocus();
            });
            return true;
        }

        @Override
        public boolean traverseFocusOut(boolean bl) {
            return true;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void repaint() {
            Object object = this.lock;
            synchronized (object) {
                if (this.queued) {
                    return;
                }
                this.queued = true;
                Display.getDefault().asyncExec(() -> {
                    try {
                        if (FXCanvas.this.isDisposed()) {
                            return;
                        }
                        FXCanvas.this.redraw();
                    }
                    finally {
                        Object object = this.lock;
                        synchronized (object) {
                            this.queued = false;
                        }
                    }
                });
            }
        }

        @Override
        public void setPreferredSize(int n2, int n3) {
            FXCanvas.this.pPreferredWidth = n2;
            FXCanvas.this.pPreferredHeight = n3;
        }

        @Override
        public void setEnabled(boolean bl) {
            FXCanvas.this.setEnabled(bl);
        }

        @Override
        public void setCursor(CursorFrame cursorFrame) {
            FXCanvas.this.setCursor(this.getPlatformCursor(cursorFrame));
        }

        private Cursor getPlatformCursor(CursorFrame cursorFrame) {
            if (cursorFrame.getCursorType() == CursorType.DEFAULT) {
                return null;
            }
            Cursor cursor = cursorFrame.getPlatformCursor(Cursor.class);
            if (cursor != null) {
                return cursor;
            }
            Cursor cursor2 = SWTCursors.embedCursorToCursor(cursorFrame);
            cursorFrame.setPlatforCursor(Cursor.class, cursor2);
            return cursor2;
        }

        @Override
        public boolean grabFocus() {
            return true;
        }

        @Override
        public void ungrabFocus() {
        }
    }
}

