/*
 * Decompiled with CFR 0.150.
 */
package com.sun.jna.platform;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.DesktopWindow;
import com.sun.jna.platform.RasterRangesUtils;
import com.sun.jna.platform.unix.X11;
import com.sun.jna.platform.win32.GDI32;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Kernel32Util;
import com.sun.jna.platform.win32.PsapiUtil;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinGDI;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import java.awt.AWTEvent;
import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.AWTEventListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;

public class WindowUtils {
    private static final Logger LOG = Logger.getLogger(WindowUtils.class.getName());
    private static final String TRANSPARENT_OLD_BG = "transparent-old-bg";
    private static final String TRANSPARENT_OLD_OPAQUE = "transparent-old-opaque";
    private static final String TRANSPARENT_ALPHA = "transparent-alpha";
    public static final Shape MASK_NONE = null;

    private static NativeWindowUtils getInstance() {
        return Holder.INSTANCE;
    }

    public static void setWindowMask(Window w2, Shape mask) {
        WindowUtils.getInstance().setWindowMask((Component)w2, mask);
    }

    public static void setComponentMask(Component c2, Shape mask) {
        WindowUtils.getInstance().setWindowMask(c2, mask);
    }

    public static void setWindowMask(Window w2, Icon mask) {
        WindowUtils.getInstance().setWindowMask((Component)w2, mask);
    }

    public static boolean isWindowAlphaSupported() {
        return WindowUtils.getInstance().isWindowAlphaSupported();
    }

    public static GraphicsConfiguration getAlphaCompatibleGraphicsConfiguration() {
        return WindowUtils.getInstance().getAlphaCompatibleGraphicsConfiguration();
    }

    public static void setWindowAlpha(Window w2, float alpha) {
        WindowUtils.getInstance().setWindowAlpha(w2, Math.max(0.0f, Math.min(alpha, 1.0f)));
    }

    public static void setWindowTransparent(Window w2, boolean transparent) {
        WindowUtils.getInstance().setWindowTransparent(w2, transparent);
    }

    public static BufferedImage getWindowIcon(WinDef.HWND hwnd) {
        return WindowUtils.getInstance().getWindowIcon(hwnd);
    }

    public static Dimension getIconSize(WinDef.HICON hIcon) {
        return WindowUtils.getInstance().getIconSize(hIcon);
    }

    public static List<DesktopWindow> getAllWindows(boolean onlyVisibleWindows) {
        return WindowUtils.getInstance().getAllWindows(onlyVisibleWindows);
    }

    public static String getWindowTitle(WinDef.HWND hwnd) {
        return WindowUtils.getInstance().getWindowTitle(hwnd);
    }

    public static String getProcessFilePath(WinDef.HWND hwnd) {
        return WindowUtils.getInstance().getProcessFilePath(hwnd);
    }

    public static Rectangle getWindowLocationAndSize(WinDef.HWND hwnd) {
        return WindowUtils.getInstance().getWindowLocationAndSize(hwnd);
    }

    private static class X11WindowUtils
    extends NativeWindowUtils {
        private boolean didCheck;
        private long[] alphaVisualIDs = new long[0];
        private static final long OPAQUE = 0xFFFFFFFFL;
        private static final String OPACITY = "_NET_WM_WINDOW_OPACITY";

        private X11WindowUtils() {
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        private static X11.Pixmap createBitmap(X11.Display dpy, X11.Window win, Raster raster) {
            X11 x11 = X11.INSTANCE;
            Rectangle bounds = raster.getBounds();
            int width = bounds.x + bounds.width;
            int height = bounds.y + bounds.height;
            X11.Pixmap pm = x11.XCreatePixmap(dpy, win, width, height, 1);
            X11.GC gc = x11.XCreateGC(dpy, pm, new NativeLong(0L), null);
            if (gc == null) {
                return null;
            }
            x11.XSetForeground(dpy, gc, new NativeLong(0L));
            x11.XFillRectangle(dpy, pm, gc, 0, 0, width, height);
            final ArrayList rlist = new ArrayList();
            try {
                RasterRangesUtils.outputOccupiedRanges(raster, new RasterRangesUtils.RangesOutput(){

                    @Override
                    public boolean outputRange(int x2, int y2, int w2, int h2) {
                        rlist.add(new Rectangle(x2, y2, w2, h2));
                        return true;
                    }
                });
                X11.XRectangle[] rects = (X11.XRectangle[])new X11.XRectangle().toArray(rlist.size());
                for (int i2 = 0; i2 < rects.length; ++i2) {
                    Rectangle r2 = (Rectangle)rlist.get(i2);
                    rects[i2].x = (short)r2.x;
                    rects[i2].y = (short)r2.y;
                    rects[i2].width = (short)r2.width;
                    rects[i2].height = (short)r2.height;
                    Pointer p2 = rects[i2].getPointer();
                    p2.setShort(0L, (short)r2.x);
                    p2.setShort(2L, (short)r2.y);
                    p2.setShort(4L, (short)r2.width);
                    p2.setShort(6L, (short)r2.height);
                    rects[i2].setAutoSynch(false);
                }
                boolean UNMASKED = true;
                x11.XSetForeground(dpy, gc, new NativeLong(1L));
                x11.XFillRectangles(dpy, pm, gc, rects, rects.length);
            }
            finally {
                x11.XFreeGC(dpy, gc);
            }
            return pm;
        }

        @Override
        public boolean isWindowAlphaSupported() {
            return this.getAlphaVisualIDs().length > 0;
        }

        private static long getVisualID(GraphicsConfiguration config) {
            try {
                Object o2 = config.getClass().getMethod("getVisual", null).invoke(config, null);
                return ((Number)o2).longValue();
            }
            catch (Exception e2) {
                e2.printStackTrace();
                return -1L;
            }
        }

        @Override
        public GraphicsConfiguration getAlphaCompatibleGraphicsConfiguration() {
            if (this.isWindowAlphaSupported()) {
                GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
                GraphicsDevice[] devices = env.getScreenDevices();
                for (int i2 = 0; i2 < devices.length; ++i2) {
                    GraphicsConfiguration[] configs = devices[i2].getConfigurations();
                    for (int j2 = 0; j2 < configs.length; ++j2) {
                        long visualID = X11WindowUtils.getVisualID(configs[j2]);
                        long[] ids = this.getAlphaVisualIDs();
                        for (int k2 = 0; k2 < ids.length; ++k2) {
                            if (visualID != ids[k2]) continue;
                            return configs[j2];
                        }
                    }
                }
            }
            return super.getAlphaCompatibleGraphicsConfiguration();
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        private synchronized long[] getAlphaVisualIDs() {
            if (this.didCheck) {
                return this.alphaVisualIDs;
            }
            this.didCheck = true;
            X11 x11 = X11.INSTANCE;
            X11.Display dpy = x11.XOpenDisplay(null);
            if (dpy == null) {
                return this.alphaVisualIDs;
            }
            Structure info = null;
            try {
                int screen = x11.XDefaultScreen(dpy);
                X11.XVisualInfo template = new X11.XVisualInfo();
                template.screen = screen;
                template.depth = 32;
                template.c_class = 4;
                NativeLong mask = new NativeLong(14L);
                IntByReference pcount = new IntByReference();
                info = x11.XGetVisualInfo(dpy, mask, template, pcount);
                if (info != null) {
                    int i2;
                    ArrayList<X11.VisualID> list = new ArrayList<X11.VisualID>();
                    X11.XVisualInfo[] infos = (X11.XVisualInfo[])info.toArray(pcount.getValue());
                    for (i2 = 0; i2 < infos.length; ++i2) {
                        X11.Xrender.XRenderPictFormat format = X11.Xrender.INSTANCE.XRenderFindVisualFormat(dpy, infos[i2].visual);
                        if (format.type != 1 || format.direct.alphaMask == 0) continue;
                        list.add(infos[i2].visualid);
                    }
                    this.alphaVisualIDs = new long[list.size()];
                    for (i2 = 0; i2 < this.alphaVisualIDs.length; ++i2) {
                        this.alphaVisualIDs[i2] = ((Number)list.get(i2)).longValue();
                    }
                    long[] arrl = this.alphaVisualIDs;
                    return arrl;
                }
            }
            finally {
                if (info != null) {
                    x11.XFree(info.getPointer());
                }
                x11.XCloseDisplay(dpy);
            }
            return this.alphaVisualIDs;
        }

        private static X11.Window getContentWindow(Window w2, X11.Display dpy, X11.Window win, Point offset) {
            if (w2 instanceof Frame && !((Frame)w2).isUndecorated() || w2 instanceof Dialog && !((Dialog)w2).isUndecorated()) {
                int[] ids;
                X11 x11 = X11.INSTANCE;
                X11.WindowByReference rootp = new X11.WindowByReference();
                X11.WindowByReference parentp = new X11.WindowByReference();
                PointerByReference childrenp = new PointerByReference();
                IntByReference countp = new IntByReference();
                x11.XQueryTree(dpy, win, rootp, parentp, childrenp, countp);
                Pointer p2 = childrenp.getValue();
                int[] arrn = ids = p2.getIntArray(0L, countp.getValue());
                int n2 = arrn.length;
                int n3 = 0;
                if (n3 < n2) {
                    int id = arrn[n3];
                    X11.Window child = new X11.Window((long)id);
                    X11.XWindowAttributes xwa = new X11.XWindowAttributes();
                    x11.XGetWindowAttributes(dpy, child, xwa);
                    offset.x = -xwa.x;
                    offset.y = -xwa.y;
                    win = child;
                }
                if (p2 != null) {
                    x11.XFree(p2);
                }
            }
            return win;
        }

        private static X11.Window getDrawable(Component w2) {
            int id = (int)Native.getComponentID(w2);
            if (id == 0) {
                return null;
            }
            return new X11.Window((long)id);
        }

        @Override
        public void setWindowAlpha(final Window w2, final float alpha) {
            if (!this.isWindowAlphaSupported()) {
                throw new UnsupportedOperationException("This X11 display does not provide a 32-bit visual");
            }
            Runnable action = new Runnable(){

                /*
                 * WARNING - Removed try catching itself - possible behaviour change.
                 */
                @Override
                public void run() {
                    X11 x11 = X11.INSTANCE;
                    X11.Display dpy = x11.XOpenDisplay(null);
                    if (dpy == null) {
                        return;
                    }
                    try {
                        X11.Window win = X11WindowUtils.getDrawable(w2);
                        if (alpha == 1.0f) {
                            x11.XDeleteProperty(dpy, win, x11.XInternAtom(dpy, X11WindowUtils.OPACITY, false));
                        } else {
                            int opacity = (int)((long)(alpha * 4.2949673E9f) & 0xFFFFFFFFFFFFFFFFL);
                            IntByReference patom = new IntByReference(opacity);
                            x11.XChangeProperty(dpy, win, x11.XInternAtom(dpy, X11WindowUtils.OPACITY, false), X11.XA_CARDINAL, 32, 0, patom.getPointer(), 1);
                        }
                    }
                    finally {
                        x11.XCloseDisplay(dpy);
                    }
                }
            };
            this.whenDisplayable(w2, action);
        }

        @Override
        public void setWindowTransparent(final Window w2, final boolean transparent) {
            boolean isTransparent;
            if (!(w2 instanceof RootPaneContainer)) {
                throw new IllegalArgumentException("Window must be a RootPaneContainer");
            }
            if (!this.isWindowAlphaSupported()) {
                throw new UnsupportedOperationException("This X11 display does not provide a 32-bit visual");
            }
            if (!w2.getGraphicsConfiguration().equals(this.getAlphaCompatibleGraphicsConfiguration())) {
                throw new IllegalArgumentException("Window GraphicsConfiguration '" + w2.getGraphicsConfiguration() + "' does not support transparency");
            }
            boolean bl = isTransparent = w2.getBackground() != null && w2.getBackground().getAlpha() == 0;
            if (transparent == isTransparent) {
                return;
            }
            this.whenDisplayable(w2, new Runnable(){

                @Override
                public void run() {
                    JRootPane root = ((RootPaneContainer)((Object)w2)).getRootPane();
                    JLayeredPane lp = root.getLayeredPane();
                    Container content = root.getContentPane();
                    if (content instanceof X11TransparentContentPane) {
                        ((X11TransparentContentPane)content).setTransparent(transparent);
                    } else if (transparent) {
                        X11TransparentContentPane x11content = new X11TransparentContentPane(content);
                        root.setContentPane(x11content);
                        lp.add((Component)new RepaintTrigger(x11content), JLayeredPane.DRAG_LAYER);
                    }
                    X11WindowUtils.this.setLayersTransparent(w2, transparent);
                    X11WindowUtils.this.setForceHeavyweightPopups(w2, transparent);
                    X11WindowUtils.this.setDoubleBuffered(w2, !transparent);
                }
            });
        }

        private void setWindowShape(final Window w2, final PixmapSource src) {
            Runnable action = new Runnable(){

                /*
                 * WARNING - Removed try catching itself - possible behaviour change.
                 */
                @Override
                public void run() {
                    X11.Pixmap pm;
                    X11.Display dpy;
                    X11 x11;
                    block4: {
                        x11 = X11.INSTANCE;
                        dpy = x11.XOpenDisplay(null);
                        if (dpy == null) {
                            return;
                        }
                        pm = null;
                        try {
                            X11.Window win = X11WindowUtils.getDrawable(w2);
                            pm = src.getPixmap(dpy, win);
                            X11.Xext ext = X11.Xext.INSTANCE;
                            ext.XShapeCombineMask(dpy, win, 0, 0, 0, pm == null ? X11.Pixmap.None : pm, 0);
                            if (pm == null) break block4;
                            x11.XFreePixmap(dpy, pm);
                        }
                        catch (Throwable throwable) {
                            if (pm != null) {
                                x11.XFreePixmap(dpy, pm);
                            }
                            x11.XCloseDisplay(dpy);
                            throw throwable;
                        }
                    }
                    x11.XCloseDisplay(dpy);
                    X11WindowUtils.this.setForceHeavyweightPopups(X11WindowUtils.this.getWindow(w2), pm != null);
                }
            };
            this.whenDisplayable(w2, action);
        }

        @Override
        protected void setMask(Component w2, final Raster raster) {
            this.setWindowShape(this.getWindow(w2), new PixmapSource(){

                @Override
                public X11.Pixmap getPixmap(X11.Display dpy, X11.Window win) {
                    return raster != null ? X11WindowUtils.createBitmap(dpy, win, raster) : null;
                }
            });
        }

        private static interface PixmapSource {
            public X11.Pixmap getPixmap(X11.Display var1, X11.Window var2);
        }

        private class X11TransparentContentPane
        extends NativeWindowUtils.TransparentContentPane {
            private static final long serialVersionUID = 1L;
            private Memory buffer;
            private int[] pixels;
            private final int[] pixel;

            public X11TransparentContentPane(Container oldContent) {
                super(oldContent);
                this.pixel = new int[4];
            }

            @Override
            protected void paintDirect(BufferedImage buf, Rectangle bounds) {
                Window window = SwingUtilities.getWindowAncestor(this);
                X11 x11 = X11.INSTANCE;
                X11.Display dpy = x11.XOpenDisplay(null);
                X11.Window win = X11WindowUtils.getDrawable(window);
                Point offset = new Point();
                win = X11WindowUtils.getContentWindow(window, dpy, win, offset);
                X11.GC gc = x11.XCreateGC(dpy, win, new NativeLong(0L), null);
                Raster raster = buf.getData();
                int w2 = bounds.width;
                int h2 = bounds.height;
                if (this.buffer == null || this.buffer.size() != (long)(w2 * h2 * 4)) {
                    this.buffer = new Memory(w2 * h2 * 4);
                    this.pixels = new int[w2 * h2];
                }
                for (int y2 = 0; y2 < h2; ++y2) {
                    for (int x2 = 0; x2 < w2; ++x2) {
                        raster.getPixel(x2, y2, this.pixel);
                        int alpha = this.pixel[3] & 0xFF;
                        int red = this.pixel[2] & 0xFF;
                        int green = this.pixel[1] & 0xFF;
                        int blue = this.pixel[0] & 0xFF;
                        this.pixels[y2 * w2 + x2] = alpha << 24 | blue << 16 | green << 8 | red;
                    }
                }
                X11.XWindowAttributes xwa = new X11.XWindowAttributes();
                x11.XGetWindowAttributes(dpy, win, xwa);
                X11.XImage image = x11.XCreateImage(dpy, xwa.visual, 32, 2, 0, this.buffer, w2, h2, 32, w2 * 4);
                this.buffer.write(0L, this.pixels, 0, this.pixels.length);
                offset.x += bounds.x;
                offset.y += bounds.y;
                x11.XPutImage(dpy, win, gc, image, 0, 0, offset.x, offset.y, w2, h2);
                x11.XFree(image.getPointer());
                x11.XFreeGC(dpy, gc);
                x11.XCloseDisplay(dpy);
            }
        }
    }

    private static class MacWindowUtils
    extends NativeWindowUtils {
        private static final String WDRAG = "apple.awt.draggableWindowBackground";

        private MacWindowUtils() {
        }

        @Override
        public boolean isWindowAlphaSupported() {
            return true;
        }

        private OSXMaskingContentPane installMaskingPane(Window w2) {
            OSXMaskingContentPane content;
            if (w2 instanceof RootPaneContainer) {
                RootPaneContainer rpc = (RootPaneContainer)((Object)w2);
                Container oldContent = rpc.getContentPane();
                if (oldContent instanceof OSXMaskingContentPane) {
                    content = (OSXMaskingContentPane)oldContent;
                } else {
                    content = new OSXMaskingContentPane(oldContent);
                    rpc.setContentPane(content);
                }
            } else {
                Component oldContent;
                Component component = oldContent = w2.getComponentCount() > 0 ? w2.getComponent(0) : null;
                if (oldContent instanceof OSXMaskingContentPane) {
                    content = (OSXMaskingContentPane)oldContent;
                } else {
                    content = new OSXMaskingContentPane(oldContent);
                    w2.add(content);
                }
            }
            return content;
        }

        @Override
        public void setWindowTransparent(Window w2, boolean transparent) {
            boolean isTransparent;
            boolean bl = isTransparent = w2.getBackground() != null && w2.getBackground().getAlpha() == 0;
            if (transparent != isTransparent) {
                this.setBackgroundTransparent(w2, transparent, "setWindowTransparent");
            }
        }

        private void fixWindowDragging(Window w2, String context) {
            JRootPane p2;
            Boolean oldDraggable;
            if (w2 instanceof RootPaneContainer && (oldDraggable = (Boolean)(p2 = ((RootPaneContainer)((Object)w2)).getRootPane()).getClientProperty(WDRAG)) == null) {
                p2.putClientProperty(WDRAG, Boolean.FALSE);
                if (w2.isDisplayable()) {
                    LOG.log(Level.WARNING, "{0}(): To avoid content dragging, {1}() must be called before the window is realized, or apple.awt.draggableWindowBackground must be set to Boolean.FALSE before the window is realized.  If you really want content dragging, set apple.awt.draggableWindowBackground on the window''s root pane to Boolean.TRUE before calling {2}() to hide this message.", new Object[]{context, context, context});
                }
            }
        }

        @Override
        public void setWindowAlpha(final Window w2, final float alpha) {
            if (w2 instanceof RootPaneContainer) {
                JRootPane p2 = ((RootPaneContainer)((Object)w2)).getRootPane();
                p2.putClientProperty("Window.alpha", Float.valueOf(alpha));
                this.fixWindowDragging(w2, "setWindowAlpha");
            }
            this.whenDisplayable(w2, new Runnable(){

                @Override
                public void run() {
                    try {
                        Method getPeer = w2.getClass().getMethod("getPeer", new Class[0]);
                        Object peer = getPeer.invoke(w2, new Object[0]);
                        Method setAlpha = peer.getClass().getMethod("setAlpha", Float.TYPE);
                        setAlpha.invoke(peer, Float.valueOf(alpha));
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                }
            });
        }

        @Override
        protected void setWindowMask(Component w2, Raster raster) {
            if (raster != null) {
                this.setWindowMask(w2, this.toShape(raster));
            } else {
                this.setWindowMask(w2, new Rectangle(0, 0, w2.getWidth(), w2.getHeight()));
            }
        }

        @Override
        public void setWindowMask(Component c2, Shape shape) {
            if (c2 instanceof Window) {
                Window w2 = (Window)c2;
                OSXMaskingContentPane content = this.installMaskingPane(w2);
                content.setMask(shape);
                this.setBackgroundTransparent(w2, shape != MASK_NONE, "setWindowMask");
            }
        }

        private void setBackgroundTransparent(Window w2, boolean transparent, String context) {
            JRootPane rp;
            JRootPane jRootPane = rp = w2 instanceof RootPaneContainer ? ((RootPaneContainer)((Object)w2)).getRootPane() : null;
            if (transparent) {
                if (rp != null) {
                    rp.putClientProperty(WindowUtils.TRANSPARENT_OLD_BG, w2.getBackground());
                }
                w2.setBackground(new Color(0, 0, 0, 0));
            } else if (rp != null) {
                Color bg = (Color)rp.getClientProperty(WindowUtils.TRANSPARENT_OLD_BG);
                if (bg != null) {
                    bg = new Color(bg.getRed(), bg.getGreen(), bg.getBlue(), bg.getAlpha());
                }
                w2.setBackground(bg);
                rp.putClientProperty(WindowUtils.TRANSPARENT_OLD_BG, null);
            } else {
                w2.setBackground(null);
            }
            this.fixWindowDragging(w2, context);
        }

        private static class OSXMaskingContentPane
        extends JPanel {
            private static final long serialVersionUID = 1L;
            private Shape shape;

            public OSXMaskingContentPane(Component oldContent) {
                super(new BorderLayout());
                if (oldContent != null) {
                    this.add(oldContent, "Center");
                }
            }

            public void setMask(Shape shape) {
                this.shape = shape;
                this.repaint();
            }

            @Override
            public void paint(Graphics graphics) {
                Graphics2D g2 = (Graphics2D)graphics.create();
                g2.setComposite(AlphaComposite.Clear);
                g2.fillRect(0, 0, this.getWidth(), this.getHeight());
                g2.dispose();
                if (this.shape != null) {
                    g2 = (Graphics2D)graphics.create();
                    g2.setClip(this.shape);
                    super.paint(g2);
                    g2.dispose();
                } else {
                    super.paint(graphics);
                }
            }
        }
    }

    private static class W32WindowUtils
    extends NativeWindowUtils {
        private W32WindowUtils() {
        }

        private WinDef.HWND getHWnd(Component w2) {
            WinDef.HWND hwnd = new WinDef.HWND();
            hwnd.setPointer(Native.getComponentPointer(w2));
            return hwnd;
        }

        @Override
        public boolean isWindowAlphaSupported() {
            return Boolean.getBoolean("sun.java2d.noddraw");
        }

        private boolean usingUpdateLayeredWindow(Window w2) {
            if (w2 instanceof RootPaneContainer) {
                JRootPane root = ((RootPaneContainer)((Object)w2)).getRootPane();
                return root.getClientProperty(WindowUtils.TRANSPARENT_OLD_BG) != null;
            }
            return false;
        }

        private void storeAlpha(Window w2, byte alpha) {
            if (w2 instanceof RootPaneContainer) {
                JRootPane root = ((RootPaneContainer)((Object)w2)).getRootPane();
                Byte b2 = alpha == -1 ? null : Byte.valueOf(alpha);
                root.putClientProperty(WindowUtils.TRANSPARENT_ALPHA, b2);
            }
        }

        private byte getAlpha(Window w2) {
            JRootPane root;
            Byte b2;
            if (w2 instanceof RootPaneContainer && (b2 = (Byte)(root = ((RootPaneContainer)((Object)w2)).getRootPane()).getClientProperty(WindowUtils.TRANSPARENT_ALPHA)) != null) {
                return b2;
            }
            return -1;
        }

        @Override
        public void setWindowAlpha(final Window w2, final float alpha) {
            if (!this.isWindowAlphaSupported()) {
                throw new UnsupportedOperationException("Set sun.java2d.noddraw=true to enable transparent windows");
            }
            this.whenDisplayable(w2, new Runnable(){

                @Override
                public void run() {
                    WinDef.HWND hWnd = W32WindowUtils.this.getHWnd(w2);
                    User32 user = User32.INSTANCE;
                    int flags = user.GetWindowLong(hWnd, -20);
                    byte level = (byte)((int)(255.0f * alpha) & 0xFF);
                    if (W32WindowUtils.this.usingUpdateLayeredWindow(w2)) {
                        WinUser.BLENDFUNCTION blend = new WinUser.BLENDFUNCTION();
                        blend.SourceConstantAlpha = level;
                        blend.AlphaFormat = 1;
                        user.UpdateLayeredWindow(hWnd, null, null, null, null, null, 0, blend, 2);
                    } else if (alpha == 1.0f) {
                        user.SetWindowLong(hWnd, -20, flags &= 0xFFF7FFFF);
                    } else {
                        user.SetWindowLong(hWnd, -20, flags |= 0x80000);
                        user.SetLayeredWindowAttributes(hWnd, 0, level, 2);
                    }
                    W32WindowUtils.this.setForceHeavyweightPopups(w2, alpha != 1.0f);
                    W32WindowUtils.this.storeAlpha(w2, level);
                }
            });
        }

        @Override
        public void setWindowTransparent(final Window w2, final boolean transparent) {
            boolean isTransparent;
            if (!(w2 instanceof RootPaneContainer)) {
                throw new IllegalArgumentException("Window must be a RootPaneContainer");
            }
            if (!this.isWindowAlphaSupported()) {
                throw new UnsupportedOperationException("Set sun.java2d.noddraw=true to enable transparent windows");
            }
            boolean bl = isTransparent = w2.getBackground() != null && w2.getBackground().getAlpha() == 0;
            if (transparent == isTransparent) {
                return;
            }
            this.whenDisplayable(w2, new Runnable(){

                @Override
                public void run() {
                    User32 user = User32.INSTANCE;
                    WinDef.HWND hWnd = W32WindowUtils.this.getHWnd(w2);
                    int flags = user.GetWindowLong(hWnd, -20);
                    JRootPane root = ((RootPaneContainer)((Object)w2)).getRootPane();
                    JLayeredPane lp = root.getLayeredPane();
                    Container content = root.getContentPane();
                    if (content instanceof W32TransparentContentPane) {
                        ((W32TransparentContentPane)content).setTransparent(transparent);
                    } else if (transparent) {
                        W32TransparentContentPane w32content = new W32TransparentContentPane(content);
                        root.setContentPane(w32content);
                        lp.add((Component)new RepaintTrigger(w32content), JLayeredPane.DRAG_LAYER);
                    }
                    if (transparent && !W32WindowUtils.this.usingUpdateLayeredWindow(w2)) {
                        user.SetWindowLong(hWnd, -20, flags |= 0x80000);
                    } else if (!transparent && W32WindowUtils.this.usingUpdateLayeredWindow(w2)) {
                        user.SetWindowLong(hWnd, -20, flags &= 0xFFF7FFFF);
                    }
                    W32WindowUtils.this.setLayersTransparent(w2, transparent);
                    W32WindowUtils.this.setForceHeavyweightPopups(w2, transparent);
                    W32WindowUtils.this.setDoubleBuffered(w2, !transparent);
                }
            });
        }

        @Override
        public void setWindowMask(Component w2, Shape mask) {
            if (mask instanceof Area && ((Area)mask).isPolygonal()) {
                this.setMask(w2, (Area)mask);
            } else {
                super.setWindowMask(w2, mask);
            }
        }

        private void setWindowRegion(final Component w2, final WinDef.HRGN hrgn) {
            this.whenDisplayable(w2, new Runnable(){

                /*
                 * WARNING - Removed try catching itself - possible behaviour change.
                 */
                @Override
                public void run() {
                    GDI32 gdi = GDI32.INSTANCE;
                    User32 user = User32.INSTANCE;
                    WinDef.HWND hWnd = W32WindowUtils.this.getHWnd(w2);
                    try {
                        user.SetWindowRgn(hWnd, hrgn, true);
                        W32WindowUtils.this.setForceHeavyweightPopups(W32WindowUtils.this.getWindow(w2), hrgn != null);
                    }
                    finally {
                        gdi.DeleteObject(hrgn);
                    }
                }
            });
        }

        private void setMask(Component w2, Area area) {
            GDI32 gdi = GDI32.INSTANCE;
            PathIterator pi = area.getPathIterator(null);
            int mode = pi.getWindingRule() == 1 ? 2 : 1;
            float[] coords = new float[6];
            ArrayList<WinDef.POINT> points = new ArrayList<WinDef.POINT>();
            int size = 0;
            ArrayList<Integer> sizes = new ArrayList<Integer>();
            while (!pi.isDone()) {
                int type = pi.currentSegment(coords);
                if (type == 0) {
                    size = 1;
                    points.add(new WinDef.POINT((int)coords[0], (int)coords[1]));
                } else if (type == 1) {
                    ++size;
                    points.add(new WinDef.POINT((int)coords[0], (int)coords[1]));
                } else if (type == 4) {
                    sizes.add(size);
                } else {
                    throw new RuntimeException("Area is not polygonal: " + area);
                }
                pi.next();
            }
            WinDef.POINT[] lppt = (WinDef.POINT[])new WinDef.POINT().toArray(points.size());
            WinDef.POINT[] pts = points.toArray(new WinDef.POINT[points.size()]);
            for (int i2 = 0; i2 < lppt.length; ++i2) {
                lppt[i2].x = pts[i2].x;
                lppt[i2].y = pts[i2].y;
            }
            int[] counts = new int[sizes.size()];
            for (int i3 = 0; i3 < counts.length; ++i3) {
                counts[i3] = (Integer)sizes.get(i3);
            }
            WinDef.HRGN hrgn = gdi.CreatePolyPolygonRgn(lppt, counts, counts.length, mode);
            this.setWindowRegion(w2, hrgn);
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        protected void setMask(Component w2, Raster raster) {
            WinDef.HRGN region;
            GDI32 gdi = GDI32.INSTANCE;
            WinDef.HRGN hRGN = region = raster != null ? gdi.CreateRectRgn(0, 0, 0, 0) : null;
            if (region != null) {
                final WinDef.HRGN tempRgn = gdi.CreateRectRgn(0, 0, 0, 0);
                try {
                    RasterRangesUtils.outputOccupiedRanges(raster, new RasterRangesUtils.RangesOutput(){

                        @Override
                        public boolean outputRange(int x2, int y2, int w2, int h2) {
                            GDI32 gdi = GDI32.INSTANCE;
                            gdi.SetRectRgn(tempRgn, x2, y2, x2 + w2, y2 + h2);
                            return gdi.CombineRgn(region, region, tempRgn, 2) != 0;
                        }
                    });
                }
                finally {
                    gdi.DeleteObject(tempRgn);
                }
            }
            this.setWindowRegion(w2, region);
        }

        @Override
        public BufferedImage getWindowIcon(WinDef.HWND hwnd) {
            WinGDI.BITMAPINFOHEADER hdr;
            WinDef.DWORDByReference hIconNumber = new WinDef.DWORDByReference();
            WinDef.LRESULT result = User32.INSTANCE.SendMessageTimeout(hwnd, 127, new WinDef.WPARAM(1L), new WinDef.LPARAM(0L), 2, 500, hIconNumber);
            if (result.intValue() == 0) {
                result = User32.INSTANCE.SendMessageTimeout(hwnd, 127, new WinDef.WPARAM(0L), new WinDef.LPARAM(0L), 2, 500, hIconNumber);
            }
            if (result.intValue() == 0) {
                result = User32.INSTANCE.SendMessageTimeout(hwnd, 127, new WinDef.WPARAM(2L), new WinDef.LPARAM(0L), 2, 500, hIconNumber);
            }
            if (result.intValue() == 0) {
                result = new WinDef.LRESULT((long)User32.INSTANCE.GetClassLongPtr(hwnd, -14).intValue());
                hIconNumber.getValue().setValue(result.intValue());
            }
            if (result.intValue() == 0) {
                result = new WinDef.LRESULT((long)User32.INSTANCE.GetClassLongPtr(hwnd, -34).intValue());
                hIconNumber.getValue().setValue(result.intValue());
            }
            if (result.intValue() == 0) {
                return null;
            }
            WinDef.HICON hIcon = new WinDef.HICON(new Pointer(hIconNumber.getValue().longValue()));
            Dimension iconSize = this.getIconSize(hIcon);
            if (iconSize.width == 0 || iconSize.height == 0) {
                return null;
            }
            int width = iconSize.width;
            int height = iconSize.height;
            int depth = 24;
            byte[] lpBitsColor = new byte[width * height * 24 / 8];
            Memory lpBitsColorPtr = new Memory(lpBitsColor.length);
            byte[] lpBitsMask = new byte[width * height * 24 / 8];
            Memory lpBitsMaskPtr = new Memory(lpBitsMask.length);
            WinGDI.BITMAPINFO bitmapInfo = new WinGDI.BITMAPINFO();
            bitmapInfo.bmiHeader = hdr = new WinGDI.BITMAPINFOHEADER();
            hdr.biWidth = width;
            hdr.biHeight = height;
            hdr.biPlanes = 1;
            hdr.biBitCount = (short)24;
            hdr.biCompression = 0;
            hdr.write();
            bitmapInfo.write();
            WinDef.HDC hDC = User32.INSTANCE.GetDC(null);
            WinGDI.ICONINFO iconInfo = new WinGDI.ICONINFO();
            User32.INSTANCE.GetIconInfo(hIcon, iconInfo);
            iconInfo.read();
            GDI32.INSTANCE.GetDIBits(hDC, iconInfo.hbmColor, 0, height, lpBitsColorPtr, bitmapInfo, 0);
            ((Pointer)lpBitsColorPtr).read(0L, lpBitsColor, 0, lpBitsColor.length);
            GDI32.INSTANCE.GetDIBits(hDC, iconInfo.hbmMask, 0, height, lpBitsMaskPtr, bitmapInfo, 0);
            ((Pointer)lpBitsMaskPtr).read(0L, lpBitsMask, 0, lpBitsMask.length);
            BufferedImage image = new BufferedImage(width, height, 2);
            int x2 = 0;
            int y2 = height - 1;
            for (int i2 = 0; i2 < lpBitsColor.length; i2 += 3) {
                int b2 = lpBitsColor[i2] & 0xFF;
                int g2 = lpBitsColor[i2 + 1] & 0xFF;
                int r2 = lpBitsColor[i2 + 2] & 0xFF;
                int a2 = 255 - lpBitsMask[i2] & 0xFF;
                int argb = a2 << 24 | r2 << 16 | g2 << 8 | b2;
                image.setRGB(x2, y2, argb);
                x2 = (x2 + 1) % width;
                if (x2 != 0) continue;
                --y2;
            }
            User32.INSTANCE.ReleaseDC(null, hDC);
            return image;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Dimension getIconSize(WinDef.HICON hIcon) {
            WinGDI.ICONINFO iconInfo = new WinGDI.ICONINFO();
            try {
                if (!User32.INSTANCE.GetIconInfo(hIcon, iconInfo)) {
                    Dimension dimension = new Dimension();
                    return dimension;
                }
                iconInfo.read();
                WinGDI.BITMAP bmp = new WinGDI.BITMAP();
                if (iconInfo.hbmColor != null && iconInfo.hbmColor.getPointer() != Pointer.NULL) {
                    int nWrittenBytes = GDI32.INSTANCE.GetObject(iconInfo.hbmColor, bmp.size(), bmp.getPointer());
                    bmp.read();
                    if (nWrittenBytes > 0) {
                        Dimension dimension = new Dimension(bmp.bmWidth.intValue(), bmp.bmHeight.intValue());
                        return dimension;
                    }
                } else if (iconInfo.hbmMask != null && iconInfo.hbmMask.getPointer() != Pointer.NULL) {
                    int nWrittenBytes = GDI32.INSTANCE.GetObject(iconInfo.hbmMask, bmp.size(), bmp.getPointer());
                    bmp.read();
                    if (nWrittenBytes > 0) {
                        Dimension dimension = new Dimension(bmp.bmWidth.intValue(), bmp.bmHeight.intValue() / 2);
                        return dimension;
                    }
                }
            }
            finally {
                if (iconInfo.hbmColor != null && iconInfo.hbmColor.getPointer() != Pointer.NULL) {
                    GDI32.INSTANCE.DeleteObject(iconInfo.hbmColor);
                }
                if (iconInfo.hbmMask != null && iconInfo.hbmMask.getPointer() != Pointer.NULL) {
                    GDI32.INSTANCE.DeleteObject(iconInfo.hbmMask);
                }
            }
            return new Dimension();
        }

        @Override
        public List<DesktopWindow> getAllWindows(final boolean onlyVisibleWindows) {
            final LinkedList<DesktopWindow> result = new LinkedList<DesktopWindow>();
            WinUser.WNDENUMPROC lpEnumFunc = new WinUser.WNDENUMPROC(){

                @Override
                public boolean callback(WinDef.HWND hwnd, Pointer arg1) {
                    try {
                        boolean visible;
                        boolean bl = visible = !onlyVisibleWindows || User32.INSTANCE.IsWindowVisible(hwnd);
                        if (visible) {
                            String title = W32WindowUtils.this.getWindowTitle(hwnd);
                            String filePath = W32WindowUtils.this.getProcessFilePath(hwnd);
                            Rectangle locAndSize = W32WindowUtils.this.getWindowLocationAndSize(hwnd);
                            result.add(new DesktopWindow(hwnd, title, filePath, locAndSize));
                        }
                    }
                    catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    return true;
                }
            };
            if (!User32.INSTANCE.EnumWindows(lpEnumFunc, null)) {
                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
            return result;
        }

        @Override
        public String getWindowTitle(WinDef.HWND hwnd) {
            int requiredLength = User32.INSTANCE.GetWindowTextLength(hwnd) + 1;
            char[] title = new char[requiredLength];
            int length = User32.INSTANCE.GetWindowText(hwnd, title, title.length);
            return Native.toString(Arrays.copyOfRange(title, 0, length));
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public String getProcessFilePath(WinDef.HWND hwnd) {
            IntByReference pid = new IntByReference();
            User32.INSTANCE.GetWindowThreadProcessId(hwnd, pid);
            WinNT.HANDLE process = Kernel32.INSTANCE.OpenProcess(1024, false, pid.getValue());
            if (process == null) {
                if (Kernel32.INSTANCE.GetLastError() != 5) {
                    throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
                }
                process = Kernel32.INSTANCE.OpenProcess(4096, false, pid.getValue());
                if (process == null) {
                    if (Kernel32.INSTANCE.GetLastError() != 5) {
                        throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
                    }
                    return "";
                }
            }
            try {
                String processImagePath = PsapiUtil.GetProcessImageFileName(process);
                if (processImagePath.startsWith("\\Device\\Mup\\")) {
                    String string = "\\" + processImagePath.substring(11);
                    return string;
                }
                char[] volumeUUID = new char[50];
                WinNT.HANDLE h2 = Kernel32.INSTANCE.FindFirstVolume(volumeUUID, 50);
                if (h2 == null || h2.equals(WinBase.INVALID_HANDLE_VALUE)) {
                    throw new Win32Exception(Native.getLastError());
                }
                try {
                    do {
                        String volumePath = Native.toString(volumeUUID);
                        for (String s2 : Kernel32Util.getVolumePathNamesForVolumeName(volumePath)) {
                            if (!s2.matches("[a-zA-Z]:\\\\")) continue;
                            for (String path : Kernel32Util.queryDosDevice(s2.substring(0, 2), 1024)) {
                                if (!processImagePath.startsWith(path)) continue;
                                String string = s2 + processImagePath.substring(path.length() + 1);
                                return string;
                            }
                        }
                    } while (Kernel32.INSTANCE.FindNextVolume(h2, volumeUUID, 50));
                    if (Native.getLastError() != 18) {
                        throw new Win32Exception(Native.getLastError());
                    }
                }
                finally {
                    Kernel32.INSTANCE.FindVolumeClose(h2);
                }
                String string = processImagePath;
                return string;
            }
            finally {
                Kernel32.INSTANCE.CloseHandle(process);
            }
        }

        @Override
        public Rectangle getWindowLocationAndSize(WinDef.HWND hwnd) {
            WinDef.RECT lpRect = new WinDef.RECT();
            if (!User32.INSTANCE.GetWindowRect(hwnd, lpRect)) {
                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
            return new Rectangle(lpRect.left, lpRect.top, Math.abs(lpRect.right - lpRect.left), Math.abs(lpRect.bottom - lpRect.top));
        }

        private class W32TransparentContentPane
        extends NativeWindowUtils.TransparentContentPane {
            private static final long serialVersionUID = 1L;
            private WinDef.HDC memDC;
            private WinDef.HBITMAP hBitmap;
            private Pointer pbits;
            private Dimension bitmapSize;

            public W32TransparentContentPane(Container content) {
                super(content);
            }

            private void disposeBackingStore() {
                GDI32 gdi = GDI32.INSTANCE;
                if (this.hBitmap != null) {
                    gdi.DeleteObject(this.hBitmap);
                    this.hBitmap = null;
                }
                if (this.memDC != null) {
                    gdi.DeleteDC(this.memDC);
                    this.memDC = null;
                }
            }

            @Override
            public void removeNotify() {
                super.removeNotify();
                this.disposeBackingStore();
            }

            @Override
            public void setTransparent(boolean transparent) {
                super.setTransparent(transparent);
                if (!transparent) {
                    this.disposeBackingStore();
                }
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            protected void paintDirect(BufferedImage buf, Rectangle bounds) {
                Window win = SwingUtilities.getWindowAncestor(this);
                GDI32 gdi = GDI32.INSTANCE;
                User32 user = User32.INSTANCE;
                int x2 = bounds.x;
                int y2 = bounds.y;
                Point origin = SwingUtilities.convertPoint(this, x2, y2, win);
                int w2 = bounds.width;
                int h2 = bounds.height;
                int ww = win.getWidth();
                int wh = win.getHeight();
                WinDef.HDC screenDC = user.GetDC(null);
                WinNT.HANDLE oldBitmap = null;
                try {
                    if (this.memDC == null) {
                        this.memDC = gdi.CreateCompatibleDC(screenDC);
                    }
                    if (this.hBitmap == null || !win.getSize().equals(this.bitmapSize)) {
                        if (this.hBitmap != null) {
                            gdi.DeleteObject(this.hBitmap);
                            this.hBitmap = null;
                        }
                        WinGDI.BITMAPINFO bmi = new WinGDI.BITMAPINFO();
                        bmi.bmiHeader.biWidth = ww;
                        bmi.bmiHeader.biHeight = wh;
                        bmi.bmiHeader.biPlanes = 1;
                        bmi.bmiHeader.biBitCount = (short)32;
                        bmi.bmiHeader.biCompression = 0;
                        bmi.bmiHeader.biSizeImage = ww * wh * 4;
                        PointerByReference ppbits = new PointerByReference();
                        this.hBitmap = gdi.CreateDIBSection(this.memDC, bmi, 0, ppbits, null, 0);
                        this.pbits = ppbits.getValue();
                        this.bitmapSize = new Dimension(ww, wh);
                    }
                    oldBitmap = gdi.SelectObject(this.memDC, this.hBitmap);
                    Raster raster = buf.getData();
                    int[] pixel = new int[4];
                    int[] bits = new int[w2];
                    for (int row = 0; row < h2; ++row) {
                        for (int col = 0; col < w2; ++col) {
                            raster.getPixel(col, row, pixel);
                            int alpha = (pixel[3] & 0xFF) << 24;
                            int red = pixel[2] & 0xFF;
                            int green = (pixel[1] & 0xFF) << 8;
                            int blue = (pixel[0] & 0xFF) << 16;
                            bits[col] = alpha | red | green | blue;
                        }
                        int v2 = wh - (origin.y + row) - 1;
                        this.pbits.write((long)((v2 * ww + origin.x) * 4), bits, 0, bits.length);
                    }
                    WinUser.SIZE winSize = new WinUser.SIZE();
                    winSize.cx = win.getWidth();
                    winSize.cy = win.getHeight();
                    WinDef.POINT winLoc = new WinDef.POINT();
                    winLoc.x = win.getX();
                    winLoc.y = win.getY();
                    WinDef.POINT srcLoc = new WinDef.POINT();
                    WinUser.BLENDFUNCTION blend = new WinUser.BLENDFUNCTION();
                    WinDef.HWND hWnd = W32WindowUtils.this.getHWnd(win);
                    ByteByReference bref = new ByteByReference();
                    IntByReference iref = new IntByReference();
                    byte level = W32WindowUtils.this.getAlpha(win);
                    try {
                        if (user.GetLayeredWindowAttributes(hWnd, null, bref, iref) && (iref.getValue() & 2) != 0) {
                            level = bref.getValue();
                        }
                    }
                    catch (UnsatisfiedLinkError unsatisfiedLinkError) {
                        // empty catch block
                    }
                    blend.SourceConstantAlpha = level;
                    blend.AlphaFormat = 1;
                    user.UpdateLayeredWindow(hWnd, screenDC, winLoc, winSize, this.memDC, srcLoc, 0, blend, 2);
                    user.ReleaseDC(null, screenDC);
                }
                catch (Throwable throwable) {
                    user.ReleaseDC(null, screenDC);
                    if (this.memDC != null && oldBitmap != null) {
                        gdi.SelectObject(this.memDC, oldBitmap);
                    }
                    throw throwable;
                }
                if (this.memDC != null && oldBitmap != null) {
                    gdi.SelectObject(this.memDC, oldBitmap);
                }
            }
        }
    }

    private static class Holder {
        public static boolean requiresVisible;
        public static final NativeWindowUtils INSTANCE;

        private Holder() {
        }

        static {
            if (Platform.isWindows()) {
                INSTANCE = new W32WindowUtils();
            } else if (Platform.isMac()) {
                INSTANCE = new MacWindowUtils();
            } else if (Platform.isX11()) {
                INSTANCE = new X11WindowUtils();
                requiresVisible = System.getProperty("java.version").matches("^1\\.4\\..*");
            } else {
                String os = System.getProperty("os.name");
                throw new UnsupportedOperationException("No support for " + os);
            }
        }
    }

    public static abstract class NativeWindowUtils {
        protected Window getWindow(Component c2) {
            return c2 instanceof Window ? (Window)c2 : SwingUtilities.getWindowAncestor(c2);
        }

        protected void whenDisplayable(Component w2, final Runnable action) {
            if (w2.isDisplayable() && (!Holder.requiresVisible || w2.isVisible())) {
                action.run();
            } else if (Holder.requiresVisible) {
                this.getWindow(w2).addWindowListener(new WindowAdapter(){

                    @Override
                    public void windowOpened(WindowEvent e2) {
                        e2.getWindow().removeWindowListener(this);
                        action.run();
                    }

                    @Override
                    public void windowClosed(WindowEvent e2) {
                        e2.getWindow().removeWindowListener(this);
                    }
                });
            } else {
                w2.addHierarchyListener(new HierarchyListener(){

                    @Override
                    public void hierarchyChanged(HierarchyEvent e2) {
                        if ((e2.getChangeFlags() & 2L) != 0L && e2.getComponent().isDisplayable()) {
                            e2.getComponent().removeHierarchyListener(this);
                            action.run();
                        }
                    }
                });
            }
        }

        protected Raster toRaster(Shape mask) {
            WritableRaster raster = null;
            if (mask != MASK_NONE) {
                Rectangle bounds = mask.getBounds();
                if (bounds.width > 0 && bounds.height > 0) {
                    BufferedImage clip = new BufferedImage(bounds.x + bounds.width, bounds.y + bounds.height, 12);
                    Graphics2D g2 = clip.createGraphics();
                    g2.setColor(Color.black);
                    g2.fillRect(0, 0, bounds.x + bounds.width, bounds.y + bounds.height);
                    g2.setColor(Color.white);
                    g2.fill(mask);
                    raster = clip.getRaster();
                }
            }
            return raster;
        }

        protected Raster toRaster(Component c2, Icon mask) {
            WritableRaster raster = null;
            if (mask != null) {
                Rectangle bounds = new Rectangle(0, 0, mask.getIconWidth(), mask.getIconHeight());
                BufferedImage clip = new BufferedImage(bounds.width, bounds.height, 2);
                Graphics2D g2 = clip.createGraphics();
                g2.setComposite(AlphaComposite.Clear);
                g2.fillRect(0, 0, bounds.width, bounds.height);
                g2.setComposite(AlphaComposite.SrcOver);
                mask.paintIcon(c2, g2, 0, 0);
                raster = clip.getAlphaRaster();
            }
            return raster;
        }

        protected Shape toShape(Raster raster) {
            final Area area = new Area(new Rectangle(0, 0, 0, 0));
            RasterRangesUtils.outputOccupiedRanges(raster, new RasterRangesUtils.RangesOutput(){

                @Override
                public boolean outputRange(int x2, int y2, int w2, int h2) {
                    area.add(new Area(new Rectangle(x2, y2, w2, h2)));
                    return true;
                }
            });
            return area;
        }

        public void setWindowAlpha(Window w2, float alpha) {
        }

        public boolean isWindowAlphaSupported() {
            return false;
        }

        public GraphicsConfiguration getAlphaCompatibleGraphicsConfiguration() {
            GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice dev = env.getDefaultScreenDevice();
            return dev.getDefaultConfiguration();
        }

        public void setWindowTransparent(Window w2, boolean transparent) {
        }

        protected void setDoubleBuffered(Component root, boolean buffered) {
            if (root instanceof JComponent) {
                ((JComponent)root).setDoubleBuffered(buffered);
            }
            if (root instanceof JRootPane && buffered) {
                ((JRootPane)root).setDoubleBuffered(true);
            } else if (root instanceof Container) {
                Component[] kids = ((Container)root).getComponents();
                for (int i2 = 0; i2 < kids.length; ++i2) {
                    this.setDoubleBuffered(kids[i2], buffered);
                }
            }
        }

        protected void setLayersTransparent(Window w2, boolean transparent) {
            Color bg;
            Color color = bg = transparent ? new Color(0, 0, 0, 0) : null;
            if (w2 instanceof RootPaneContainer) {
                JComponent content;
                RootPaneContainer rpc = (RootPaneContainer)((Object)w2);
                JRootPane root = rpc.getRootPane();
                JLayeredPane lp = root.getLayeredPane();
                Container c2 = root.getContentPane();
                JComponent jComponent = content = c2 instanceof JComponent ? (JComponent)c2 : null;
                if (transparent) {
                    lp.putClientProperty(WindowUtils.TRANSPARENT_OLD_OPAQUE, lp.isOpaque());
                    lp.setOpaque(false);
                    root.putClientProperty(WindowUtils.TRANSPARENT_OLD_OPAQUE, root.isOpaque());
                    root.setOpaque(false);
                    if (content != null) {
                        content.putClientProperty(WindowUtils.TRANSPARENT_OLD_OPAQUE, content.isOpaque());
                        content.setOpaque(false);
                    }
                    root.putClientProperty(WindowUtils.TRANSPARENT_OLD_BG, root.getParent().getBackground());
                } else {
                    lp.setOpaque(Boolean.TRUE.equals(lp.getClientProperty(WindowUtils.TRANSPARENT_OLD_OPAQUE)));
                    lp.putClientProperty(WindowUtils.TRANSPARENT_OLD_OPAQUE, null);
                    root.setOpaque(Boolean.TRUE.equals(root.getClientProperty(WindowUtils.TRANSPARENT_OLD_OPAQUE)));
                    root.putClientProperty(WindowUtils.TRANSPARENT_OLD_OPAQUE, null);
                    if (content != null) {
                        content.setOpaque(Boolean.TRUE.equals(content.getClientProperty(WindowUtils.TRANSPARENT_OLD_OPAQUE)));
                        content.putClientProperty(WindowUtils.TRANSPARENT_OLD_OPAQUE, null);
                    }
                    bg = (Color)root.getClientProperty(WindowUtils.TRANSPARENT_OLD_BG);
                    root.putClientProperty(WindowUtils.TRANSPARENT_OLD_BG, null);
                }
            }
            w2.setBackground(bg);
        }

        protected void setMask(Component c2, Raster raster) {
            throw new UnsupportedOperationException("Window masking is not available");
        }

        protected void setWindowMask(Component w2, Raster raster) {
            if (w2.isLightweight()) {
                throw new IllegalArgumentException("Component must be heavyweight: " + w2);
            }
            this.setMask(w2, raster);
        }

        public void setWindowMask(Component w2, Shape mask) {
            this.setWindowMask(w2, this.toRaster(mask));
        }

        public void setWindowMask(Component w2, Icon mask) {
            this.setWindowMask(w2, this.toRaster(w2, mask));
        }

        protected void setForceHeavyweightPopups(Window w2, boolean force) {
            if (!(w2 instanceof HeavyweightForcer)) {
                Window[] owned = w2.getOwnedWindows();
                for (int i2 = 0; i2 < owned.length; ++i2) {
                    if (!(owned[i2] instanceof HeavyweightForcer)) continue;
                    if (force) {
                        return;
                    }
                    owned[i2].dispose();
                }
                Boolean b2 = Boolean.valueOf(System.getProperty("jna.force_hw_popups", "true"));
                if (force && b2.booleanValue()) {
                    new HeavyweightForcer(w2);
                }
            }
        }

        protected BufferedImage getWindowIcon(WinDef.HWND hwnd) {
            throw new UnsupportedOperationException("This platform is not supported, yet.");
        }

        protected Dimension getIconSize(WinDef.HICON hIcon) {
            throw new UnsupportedOperationException("This platform is not supported, yet.");
        }

        protected List<DesktopWindow> getAllWindows(boolean onlyVisibleWindows) {
            throw new UnsupportedOperationException("This platform is not supported, yet.");
        }

        protected String getWindowTitle(WinDef.HWND hwnd) {
            throw new UnsupportedOperationException("This platform is not supported, yet.");
        }

        protected String getProcessFilePath(WinDef.HWND hwnd) {
            throw new UnsupportedOperationException("This platform is not supported, yet.");
        }

        protected Rectangle getWindowLocationAndSize(WinDef.HWND hwnd) {
            throw new UnsupportedOperationException("This platform is not supported, yet.");
        }

        protected abstract class TransparentContentPane
        extends JPanel
        implements AWTEventListener {
            private static final long serialVersionUID = 1L;
            private boolean transparent;

            public TransparentContentPane(Container oldContent) {
                super(new BorderLayout());
                this.add((Component)oldContent, "Center");
                this.setTransparent(true);
                if (oldContent instanceof JPanel) {
                    ((JComponent)oldContent).setOpaque(false);
                }
            }

            @Override
            public void addNotify() {
                super.addNotify();
                Toolkit.getDefaultToolkit().addAWTEventListener(this, 2L);
            }

            @Override
            public void removeNotify() {
                Toolkit.getDefaultToolkit().removeAWTEventListener(this);
                super.removeNotify();
            }

            public void setTransparent(boolean transparent) {
                this.transparent = transparent;
                this.setOpaque(!transparent);
                this.setDoubleBuffered(!transparent);
                this.repaint();
            }

            @Override
            public void eventDispatched(AWTEvent e2) {
                if (e2.getID() == 300 && SwingUtilities.isDescendingFrom(((ContainerEvent)e2).getChild(), this)) {
                    Component child = ((ContainerEvent)e2).getChild();
                    NativeWindowUtils.this.setDoubleBuffered(child, false);
                }
            }

            @Override
            public void paint(Graphics gr) {
                if (this.transparent) {
                    Rectangle r2 = gr.getClipBounds();
                    int w2 = r2.width;
                    int h2 = r2.height;
                    if (this.getWidth() > 0 && this.getHeight() > 0) {
                        BufferedImage buf = new BufferedImage(w2, h2, 3);
                        Graphics2D g2 = buf.createGraphics();
                        g2.setComposite(AlphaComposite.Clear);
                        g2.fillRect(0, 0, w2, h2);
                        g2.dispose();
                        g2 = buf.createGraphics();
                        g2.translate(-r2.x, -r2.y);
                        super.paint(g2);
                        g2.dispose();
                        this.paintDirect(buf, r2);
                    }
                } else {
                    super.paint(gr);
                }
            }

            protected abstract void paintDirect(BufferedImage var1, Rectangle var2);
        }
    }

    protected static class RepaintTrigger
    extends JComponent {
        private static final long serialVersionUID = 1L;
        private final Listener listener = this.createListener();
        private final JComponent content;
        private Rectangle dirty;

        public RepaintTrigger(JComponent content) {
            this.content = content;
        }

        @Override
        public void addNotify() {
            super.addNotify();
            Window w2 = SwingUtilities.getWindowAncestor(this);
            this.setSize(this.getParent().getSize());
            w2.addComponentListener(this.listener);
            w2.addWindowListener(this.listener);
            Toolkit.getDefaultToolkit().addAWTEventListener(this.listener, 48L);
        }

        @Override
        public void removeNotify() {
            Toolkit.getDefaultToolkit().removeAWTEventListener(this.listener);
            Window w2 = SwingUtilities.getWindowAncestor(this);
            w2.removeComponentListener(this.listener);
            w2.removeWindowListener(this.listener);
            super.removeNotify();
        }

        @Override
        protected void paintComponent(Graphics g2) {
            Rectangle bounds = g2.getClipBounds();
            if (this.dirty == null || !this.dirty.contains(bounds)) {
                this.dirty = this.dirty == null ? bounds : this.dirty.union(bounds);
                this.content.repaint(this.dirty);
            } else {
                this.dirty = null;
            }
        }

        protected Listener createListener() {
            return new Listener();
        }

        protected class Listener
        extends WindowAdapter
        implements ComponentListener,
        HierarchyListener,
        AWTEventListener {
            protected Listener() {
            }

            @Override
            public void windowOpened(WindowEvent e2) {
                RepaintTrigger.this.repaint();
            }

            @Override
            public void componentHidden(ComponentEvent e2) {
            }

            @Override
            public void componentMoved(ComponentEvent e2) {
            }

            @Override
            public void componentResized(ComponentEvent e2) {
                RepaintTrigger.this.setSize(RepaintTrigger.this.getParent().getSize());
                RepaintTrigger.this.repaint();
            }

            @Override
            public void componentShown(ComponentEvent e2) {
                RepaintTrigger.this.repaint();
            }

            @Override
            public void hierarchyChanged(HierarchyEvent e2) {
                RepaintTrigger.this.repaint();
            }

            @Override
            public void eventDispatched(AWTEvent e2) {
                Component src;
                if (e2 instanceof MouseEvent && (src = ((MouseEvent)e2).getComponent()) != null && SwingUtilities.isDescendingFrom(src, RepaintTrigger.this.content)) {
                    MouseEvent me = SwingUtilities.convertMouseEvent(src, (MouseEvent)e2, RepaintTrigger.this.content);
                    Component c2 = SwingUtilities.getDeepestComponentAt(RepaintTrigger.this.content, me.getX(), me.getY());
                    if (c2 != null) {
                        RepaintTrigger.this.setCursor(c2.getCursor());
                    }
                }
            }
        }
    }

    private static class HeavyweightForcer
    extends Window {
        private static final long serialVersionUID = 1L;
        private final boolean packed;

        public HeavyweightForcer(Window parent) {
            super(parent);
            this.pack();
            this.packed = true;
        }

        @Override
        public boolean isVisible() {
            return this.packed;
        }

        @Override
        public Rectangle getBounds() {
            return this.getOwner().getBounds();
        }
    }
}

