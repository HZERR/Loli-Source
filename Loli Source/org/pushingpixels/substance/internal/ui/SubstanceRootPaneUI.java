/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.ui;

import com.sun.awt.AWTUtilities;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.LayoutManager2;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.RoundRectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Arrays;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JRootPane;
import javax.swing.LookAndFeel;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicRootPaneUI;
import org.pushingpixels.lafwidget.LafWidget;
import org.pushingpixels.lafwidget.LafWidgetRepository;
import org.pushingpixels.lafwidget.utils.RenderingUtils;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.internal.animation.RootPaneDefaultButtonTracker;
import org.pushingpixels.substance.internal.painter.BackgroundPaintingUtils;
import org.pushingpixels.substance.internal.ui.SubstanceInternalFrameUI;
import org.pushingpixels.substance.internal.utils.MemoryAnalyzer;
import org.pushingpixels.substance.internal.utils.SubstanceColorUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceTitlePane;

public class SubstanceRootPaneUI
extends BasicRootPaneUI {
    protected Set lafWidgets;
    private static final int CORNER_DRAG_WIDTH = 16;
    private static final int BORDER_DRAG_THICKNESS = 5;
    private Window window;
    private JComponent titlePane;
    private MouseInputListener substanceMouseInputListener;
    private MouseInputListener substanceTitleMouseInputListener;
    private LayoutManager layoutManager;
    private LayoutManager savedOldLayout;
    protected JRootPane root;
    protected WindowListener substanceWindowListener;
    protected Window substanceCurrentWindow;
    protected HierarchyListener substanceHierarchyListener;
    protected ComponentListener substanceWindowComponentListener;
    protected GraphicsConfiguration currentRootPaneGC;
    protected PropertyChangeListener substancePropertyChangeListener;
    private Cursor lastCursor = Cursor.getPredefinedCursor(0);
    private static int rootPanesWithCustomSkin = 0;
    private static final int[] cursorMapping = new int[]{6, 6, 8, 7, 7, 6, 0, 0, 0, 7, 10, 0, 0, 0, 11, 4, 0, 0, 0, 5, 4, 4, 9, 5, 5};
    public static ComponentListener WINDOW_ROUNDER = new ComponentAdapter(){

        @Override
        public void componentResized(ComponentEvent e2) {
            if (e2.getComponent() instanceof Window) {
                JRootPane jrp;
                Window w2 = (Window)e2.getComponent();
                if (w2 instanceof Frame && !((Frame)w2).isUndecorated() || w2 instanceof Dialog && !((Dialog)w2).isUndecorated()) {
                    return;
                }
                if (w2 instanceof RootPaneContainer && ((jrp = ((RootPaneContainer)((Object)w2)).getRootPane()).getWindowDecorationStyle() == 0 || !SubstanceCoreUtilities.isRoundedCorners(jrp))) {
                    AWTUtilities.setWindowShape(w2, null);
                    return;
                }
                try {
                    if (SubstanceCoreUtilities.isRoundedCorners(w2) && w2.getWidth() * w2.getHeight() < 0x1000000) {
                        AWTUtilities.setWindowShape(w2, new RoundRectangle2D.Double(0.0, 0.0, w2.getWidth(), w2.getHeight(), 12.0, 12.0));
                    } else {
                        AWTUtilities.setWindowShape(w2, null);
                    }
                }
                catch (OutOfMemoryError oome) {
                    AWTUtilities.setWindowShape(w2, null);
                }
            }
        }
    };
    static HierarchyListener RESIZE_LOADER = new HierarchyListener(){

        @Override
        public void hierarchyChanged(HierarchyEvent e2) {
            if (!(e2.getChangedParent() instanceof Dialog) && !(e2.getChangedParent() instanceof Frame)) {
                e2.getChanged().removeHierarchyListener(this);
                return;
            }
            if (e2.getID() != 1400 || !(e2.getChanged() instanceof JRootPane)) {
                return;
            }
            Window w2 = (Window)e2.getChangedParent();
            if (w2 != null) {
                if (!Arrays.asList(w2.getComponentListeners()).contains(WINDOW_ROUNDER)) {
                    w2.addComponentListener(WINDOW_ROUNDER);
                }
                e2.getChanged().removeHierarchyListener(this);
            }
        }
    };

    protected void __org__pushingpixels__substance__internal__ui__SubstanceRootPaneUI__installComponents(JRootPane jRootPane) {
        super.installComponents(jRootPane);
    }

    @Override
    protected void installComponents(JRootPane jRootPane) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceRootPaneUI__installComponents(jRootPane);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installComponents();
        }
    }

    @Override
    public void installUI(JComponent jComponent) {
        this.lafWidgets = LafWidgetRepository.getRepository().getMatchingWidgets(jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceRootPaneUI__installUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installUI();
        }
    }

    @Override
    public void uninstallUI(JComponent jComponent) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceRootPaneUI__uninstallUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallUI();
        }
    }

    @Override
    protected void installListeners(JRootPane jRootPane) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceRootPaneUI__installListeners(jRootPane);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installListeners();
        }
    }

    @Override
    protected void installDefaults(JRootPane jRootPane) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceRootPaneUI__installDefaults(jRootPane);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installDefaults();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceRootPaneUI__uninstallComponents(JRootPane jRootPane) {
        super.uninstallComponents(jRootPane);
    }

    @Override
    protected void uninstallComponents(JRootPane jRootPane) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceRootPaneUI__uninstallComponents(jRootPane);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallComponents();
        }
    }

    @Override
    protected void uninstallListeners(JRootPane jRootPane) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceRootPaneUI__uninstallListeners(jRootPane);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallListeners();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceRootPaneUI__uninstallDefaults(JRootPane jRootPane) {
        super.uninstallDefaults(jRootPane);
    }

    @Override
    protected void uninstallDefaults(JRootPane jRootPane) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceRootPaneUI__uninstallDefaults(jRootPane);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallDefaults();
        }
    }

    @Override
    public void update(Graphics graphics, JComponent jComponent) {
        Graphics2D graphics2D = (Graphics2D)graphics.create();
        RenderingUtils.installDesktopHints(graphics2D, jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceRootPaneUI__update(graphics2D, jComponent);
        graphics2D.dispose();
    }

    public static ComponentUI createUI(JComponent comp) {
        SubstanceCoreUtilities.testComponentCreationThreadingViolation(comp);
        return new SubstanceRootPaneUI();
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceRootPaneUI__installUI(JComponent c2) {
        super.installUI(c2);
        if (SubstanceCoreUtilities.isRoundedCorners(c2)) {
            c2.addHierarchyListener(RESIZE_LOADER);
        }
        this.root = (JRootPane)c2;
        int style = this.root.getWindowDecorationStyle();
        if (style != 0) {
            this.installClientDecorations(this.root);
        }
        if (SubstanceCoreUtilities.isRootPaneModified(this.root)) {
            this.propagateModificationState();
        }
        if (this.root.getClientProperty("substancelaf.skin") instanceof SubstanceSkin) {
            ++rootPanesWithCustomSkin;
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceRootPaneUI__uninstallUI(JComponent c2) {
        super.uninstallUI(c2);
        this.uninstallClientDecorations(this.root);
        this.layoutManager = null;
        this.substanceMouseInputListener = null;
        if (this.root.getClientProperty("substancelaf.skin") instanceof SubstanceSkin) {
            --rootPanesWithCustomSkin;
        }
        this.root = null;
    }

    public void installBorder(JRootPane root) {
        int style = root.getWindowDecorationStyle();
        if (style == 0) {
            LookAndFeel.uninstallBorder(root);
        } else {
            LookAndFeel.installBorder(root, "RootPane.border");
        }
    }

    private void uninstallBorder(JRootPane root) {
        LookAndFeel.uninstallBorder(root);
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceRootPaneUI__installDefaults(JRootPane c2) {
        Color backgroundFillColor;
        super.installDefaults(c2);
        Color backgr = c2.getBackground();
        if ((backgr == null || backgr instanceof UIResource) && (backgroundFillColor = SubstanceColorUtilities.getBackgroundFillColor(c2)) != null) {
            c2.setBackground(new ColorUIResource(backgroundFillColor));
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceRootPaneUI__update(Graphics g2, JComponent c2) {
        if (!SubstanceLookAndFeel.isCurrentLookAndFeel()) {
            return;
        }
        if (SubstanceCoreUtilities.isOpaque(c2)) {
            BackgroundPaintingUtils.update(g2, c2, false);
        }
        super.paint(g2, c2);
    }

    private void installWindowListeners(JRootPane root, Component parent) {
        this.window = parent instanceof Window ? (Window)parent : SwingUtilities.getWindowAncestor(parent);
        if (this.window != null) {
            if (this.substanceMouseInputListener == null) {
                this.substanceMouseInputListener = this.createWindowMouseInputListener(root);
            }
            this.window.addMouseListener(this.substanceMouseInputListener);
            this.window.addMouseMotionListener(this.substanceMouseInputListener);
            if (this.titlePane != null) {
                if (this.substanceTitleMouseInputListener == null) {
                    this.substanceTitleMouseInputListener = new TitleMouseInputHandler();
                }
                this.titlePane.addMouseMotionListener(this.substanceTitleMouseInputListener);
                this.titlePane.addMouseListener(this.substanceTitleMouseInputListener);
            }
            this.setMaximized();
        }
    }

    private void uninstallWindowListeners(JRootPane root) {
        if (this.window != null) {
            this.window.removeMouseListener(this.substanceMouseInputListener);
            this.window.removeMouseMotionListener(this.substanceMouseInputListener);
        }
        if (this.titlePane != null) {
            this.titlePane.removeMouseListener(this.substanceTitleMouseInputListener);
            this.titlePane.removeMouseMotionListener(this.substanceTitleMouseInputListener);
        }
    }

    private void installLayout(JRootPane root) {
        if (this.layoutManager == null) {
            this.layoutManager = this.createLayoutManager();
        }
        this.savedOldLayout = root.getLayout();
        root.setLayout(this.layoutManager);
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceRootPaneUI__installListeners(final JRootPane root) {
        super.installListeners(root);
        this.substanceHierarchyListener = new HierarchyListener(){

            @Override
            public void hierarchyChanged(HierarchyEvent e2) {
                Container parent = root.getParent();
                if (parent == null) {
                    return;
                }
                if (MemoryAnalyzer.isRunning()) {
                    MemoryAnalyzer.enqueueUsage("Root pane @" + root.hashCode() + "\n" + SubstanceCoreUtilities.getHierarchy(parent));
                }
                if (parent.getClass().getName().startsWith("org.jdesktop.jdic.tray") || parent.getClass().getName().compareTo("javax.swing.Popup$HeavyWeightWindow") == 0) {
                    SwingUtilities.invokeLater(new Runnable(){

                        @Override
                        public void run() {
                            root.removeHierarchyListener(SubstanceRootPaneUI.this.substanceHierarchyListener);
                            SubstanceRootPaneUI.this.substanceHierarchyListener = null;
                        }
                    });
                }
                Window currWindow = parent instanceof Window ? (Window)parent : SwingUtilities.getWindowAncestor(parent);
                if (SubstanceRootPaneUI.this.substanceWindowListener != null) {
                    SubstanceRootPaneUI.this.substanceCurrentWindow.removeWindowListener(SubstanceRootPaneUI.this.substanceWindowListener);
                    SubstanceRootPaneUI.this.substanceWindowListener = null;
                }
                if (SubstanceRootPaneUI.this.substanceWindowComponentListener != null) {
                    SubstanceRootPaneUI.this.substanceCurrentWindow.removeComponentListener(SubstanceRootPaneUI.this.substanceWindowComponentListener);
                    SubstanceRootPaneUI.this.substanceWindowComponentListener = null;
                }
                if (currWindow != null) {
                    SubstanceRootPaneUI.this.substanceWindowListener = new WindowAdapter(){

                        @Override
                        public void windowClosed(WindowEvent e2) {
                            SubstanceCoreUtilities.testWindowCloseThreadingViolation(e2.getWindow());
                            SwingUtilities.invokeLater(new Runnable(){

                                @Override
                                public void run() {
                                    Frame[] frames;
                                    for (Frame frame : frames = Frame.getFrames()) {
                                        if (!frame.isDisplayable()) continue;
                                        return;
                                    }
                                    SubstanceCoreUtilities.stopThreads();
                                }
                            });
                        }
                    };
                    if (!(parent instanceof JInternalFrame)) {
                        currWindow.addWindowListener(SubstanceRootPaneUI.this.substanceWindowListener);
                    }
                    SubstanceRootPaneUI.this.substanceWindowComponentListener = new ComponentAdapter(){

                        @Override
                        public void componentMoved(ComponentEvent e2) {
                            this.processNewPosition();
                        }

                        @Override
                        public void componentResized(ComponentEvent e2) {
                            this.processNewPosition();
                        }

                        protected void processNewPosition() {
                            SwingUtilities.invokeLater(new Runnable(){

                                @Override
                                public void run() {
                                    if (SubstanceRootPaneUI.this.window == null) {
                                        return;
                                    }
                                    if (!SubstanceRootPaneUI.this.window.isShowing() || !SubstanceRootPaneUI.this.window.isDisplayable()) {
                                        SubstanceRootPaneUI.this.currentRootPaneGC = null;
                                        return;
                                    }
                                    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                                    GraphicsDevice[] gds = ge.getScreenDevices();
                                    if (gds.length == 1) {
                                        return;
                                    }
                                    Point midLoc = new Point(((SubstanceRootPaneUI)SubstanceRootPaneUI.this).window.getLocationOnScreen().x + SubstanceRootPaneUI.this.window.getWidth() / 2, ((SubstanceRootPaneUI)SubstanceRootPaneUI.this).window.getLocationOnScreen().y + SubstanceRootPaneUI.this.window.getHeight() / 2);
                                    int index = 0;
                                    for (GraphicsDevice gd : gds) {
                                        GraphicsConfiguration gc = gd.getDefaultConfiguration();
                                        Rectangle bounds = gc.getBounds();
                                        if (bounds.contains(midLoc)) {
                                            if (gc == SubstanceRootPaneUI.this.currentRootPaneGC) break;
                                            SubstanceRootPaneUI.this.currentRootPaneGC = gc;
                                            SubstanceRootPaneUI.this.setMaximized();
                                            break;
                                        }
                                        ++index;
                                    }
                                }
                            });
                        }
                    };
                    if (parent instanceof JFrame) {
                        currWindow.addComponentListener(SubstanceRootPaneUI.this.substanceWindowComponentListener);
                    }
                    SubstanceRootPaneUI.this.window = currWindow;
                }
                SubstanceRootPaneUI.this.substanceCurrentWindow = currWindow;
            }
        };
        root.addHierarchyListener(this.substanceHierarchyListener);
        JButton defaultButton = root.getDefaultButton();
        if (defaultButton != null) {
            RootPaneDefaultButtonTracker.update(defaultButton);
        }
        this.substancePropertyChangeListener = new PropertyChangeListener(){

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("defaultButton".equals(evt.getPropertyName())) {
                    JButton prev = (JButton)evt.getOldValue();
                    JButton next = (JButton)evt.getNewValue();
                    RootPaneDefaultButtonTracker.update(prev);
                    RootPaneDefaultButtonTracker.update(next);
                }
                if ("windowModified".equals(evt.getPropertyName())) {
                    SubstanceRootPaneUI.this.propagateModificationState();
                }
            }
        };
        root.addPropertyChangeListener(this.substancePropertyChangeListener);
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceRootPaneUI__uninstallListeners(JRootPane root) {
        if (this.window != null) {
            this.window.removeWindowListener(this.substanceWindowListener);
            this.substanceWindowListener = null;
            this.window.removeComponentListener(this.substanceWindowComponentListener);
            this.substanceWindowComponentListener = null;
        }
        root.removeHierarchyListener(this.substanceHierarchyListener);
        this.substanceHierarchyListener = null;
        root.removePropertyChangeListener(this.substancePropertyChangeListener);
        this.substancePropertyChangeListener = null;
        super.uninstallListeners(root);
    }

    private void uninstallLayout(JRootPane root) {
        if (this.savedOldLayout != null) {
            root.setLayout(this.savedOldLayout);
            this.savedOldLayout = null;
        }
    }

    private void installClientDecorations(JRootPane root) {
        this.installBorder(root);
        JComponent titlePane = this.createTitlePane(root);
        this.setTitlePane(root, titlePane);
        this.installWindowListeners(root, root.getParent());
        this.installLayout(root);
        if (this.window != null) {
            root.revalidate();
            root.repaint();
        }
    }

    private void uninstallClientDecorations(JRootPane root) {
        this.uninstallBorder(root);
        this.uninstallWindowListeners(root);
        this.setTitlePane(root, null);
        this.uninstallLayout(root);
        int style = root.getWindowDecorationStyle();
        if (style == 0) {
            root.repaint();
            root.revalidate();
        }
        if (this.window != null) {
            this.window.setCursor(Cursor.getPredefinedCursor(0));
        }
        this.window = null;
    }

    protected JComponent createTitlePane(JRootPane root) {
        return new SubstanceTitlePane(root, this);
    }

    private MouseInputListener createWindowMouseInputListener(JRootPane root) {
        return new MouseInputHandler();
    }

    protected LayoutManager createLayoutManager() {
        return new SubstanceRootLayout();
    }

    private void setTitlePane(JRootPane root, JComponent titlePane) {
        JLayeredPane layeredPane = root.getLayeredPane();
        JComponent oldTitlePane = this.getTitlePane();
        if (oldTitlePane != null) {
            if (oldTitlePane instanceof SubstanceTitlePane) {
                ((SubstanceTitlePane)oldTitlePane).uninstall();
            }
            layeredPane.remove(oldTitlePane);
        }
        if (titlePane != null) {
            layeredPane.add((Component)titlePane, JLayeredPane.FRAME_CONTENT_LAYER);
            titlePane.setVisible(true);
        }
        this.titlePane = titlePane;
    }

    public void setMaximized() {
        Container tla = this.root.getTopLevelAncestor();
        GraphicsConfiguration gc = this.currentRootPaneGC != null ? this.currentRootPaneGC : tla.getGraphicsConfiguration();
        Rectangle screenBounds = gc.getBounds();
        screenBounds.x = 0;
        screenBounds.y = 0;
        Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(gc);
        Rectangle maxBounds = new Rectangle(screenBounds.x + screenInsets.left, screenBounds.y + screenInsets.top, screenBounds.width - (screenInsets.left + screenInsets.right), screenBounds.height - (screenInsets.top + screenInsets.bottom));
        if (tla instanceof JFrame) {
            ((JFrame)tla).setMaximizedBounds(maxBounds);
        }
        if (MemoryAnalyzer.isRunning()) {
            MemoryAnalyzer.enqueueUsage("Frame set to bounds " + maxBounds);
        }
    }

    public JComponent getTitlePane() {
        return this.titlePane;
    }

    protected JRootPane getRootPane() {
        return this.root;
    }

    @Override
    public void propertyChange(PropertyChangeEvent e2) {
        super.propertyChange(e2);
        String propertyName = e2.getPropertyName();
        if (propertyName == null) {
            return;
        }
        if (propertyName.equals("windowDecorationStyle")) {
            JRootPane root = (JRootPane)e2.getSource();
            int style = root.getWindowDecorationStyle();
            this.uninstallClientDecorations(root);
            if (style != 0) {
                this.installClientDecorations(root);
            }
        }
        if (propertyName.equals("ancestor")) {
            this.uninstallWindowListeners(this.root);
            if (((JRootPane)e2.getSource()).getWindowDecorationStyle() != 0) {
                this.installWindowListeners(this.root, this.root.getParent());
            }
        }
        if (propertyName.equals("background")) {
            SubstanceLookAndFeel.getTitlePaneComponent(this.window).setBackground((Color)e2.getNewValue());
        }
        if (propertyName.equals("substancelaf.skin")) {
            SubstanceSkin oldValue = (SubstanceSkin)e2.getOldValue();
            SubstanceSkin newValue = (SubstanceSkin)e2.getNewValue();
            if (oldValue == null && newValue != null) {
                ++rootPanesWithCustomSkin;
            }
            if (oldValue != null && newValue == null) {
                --rootPanesWithCustomSkin;
            }
        }
    }

    private void propagateModificationState() {
        JComponent titlePane = this.getTitlePane();
        if (titlePane instanceof SubstanceTitlePane) {
            ((SubstanceTitlePane)titlePane).getCloseButton().putClientProperty("windowModified", this.root.getClientProperty("windowModified"));
            return;
        }
        JInternalFrame jif = (JInternalFrame)SwingUtilities.getAncestorOfClass(JInternalFrame.class, this.root);
        if (jif != null) {
            SubstanceInternalFrameUI internalFrameUI = (SubstanceInternalFrameUI)jif.getUI();
            internalFrameUI.setWindowModified(Boolean.TRUE.equals(this.root.getClientProperty("windowModified")));
        }
    }

    public static boolean hasCustomSkinOnAtLeastOneRootPane() {
        return rootPanesWithCustomSkin > 0;
    }

    private class TitleMouseInputHandler
    extends MouseInputAdapter {
        private Point dragOffset = new Point(0, 0);

        private TitleMouseInputHandler() {
        }

        @Override
        public void mousePressed(MouseEvent ev) {
            JRootPane rootPane = SubstanceRootPaneUI.this.getRootPane();
            if (rootPane.getWindowDecorationStyle() == 0) {
                return;
            }
            Point dragWindowOffset = ev.getPoint();
            Component source = (Component)ev.getSource();
            Point convertedDragWindowOffset = SwingUtilities.convertPoint(source, dragWindowOffset, SubstanceRootPaneUI.this.getTitlePane());
            dragWindowOffset = SwingUtilities.convertPoint(source, dragWindowOffset, SubstanceRootPaneUI.this.window);
            if (SubstanceRootPaneUI.this.getTitlePane() != null && SubstanceRootPaneUI.this.getTitlePane().contains(convertedDragWindowOffset) && SubstanceRootPaneUI.this.window != null) {
                SubstanceRootPaneUI.this.window.toFront();
                this.dragOffset = dragWindowOffset;
            }
        }

        @Override
        public void mouseDragged(MouseEvent ev) {
            Component source = (Component)ev.getSource();
            Point eventLocationOnScreen = ev.getLocationOnScreen();
            if (eventLocationOnScreen == null) {
                eventLocationOnScreen = new Point(ev.getX() + source.getLocationOnScreen().x, ev.getY() + source.getLocationOnScreen().y);
            }
            if (SubstanceRootPaneUI.this.window instanceof Frame) {
                int frameState;
                Frame f2 = (Frame)SubstanceRootPaneUI.this.window;
                int n2 = frameState = f2 != null ? f2.getExtendedState() : 0;
                if (f2 != null && (frameState & 6) == 0) {
                    SubstanceRootPaneUI.this.window.setLocation(eventLocationOnScreen.x - this.dragOffset.x, eventLocationOnScreen.y - this.dragOffset.y);
                }
            } else {
                SubstanceRootPaneUI.this.window.setLocation(eventLocationOnScreen.x - this.dragOffset.x, eventLocationOnScreen.y - this.dragOffset.y);
            }
        }

        @Override
        public void mouseClicked(MouseEvent ev) {
            if (!(SubstanceRootPaneUI.this.window instanceof Frame)) {
                return;
            }
            Frame f2 = (Frame)SubstanceRootPaneUI.this.window;
            Point convertedPoint = SwingUtilities.convertPoint(SubstanceRootPaneUI.this.window, ev.getPoint(), SubstanceRootPaneUI.this.getTitlePane());
            int state = f2.getExtendedState();
            if (SubstanceRootPaneUI.this.getTitlePane() != null && SubstanceRootPaneUI.this.getTitlePane().contains(convertedPoint) && ev.getClickCount() % 2 == 0 && (ev.getModifiers() & 0x10) != 0 && f2.isResizable()) {
                if ((state & 6) != 0) {
                    SubstanceRootPaneUI.this.setMaximized();
                    f2.setExtendedState(state & 0xFFFFFFF9);
                } else {
                    SubstanceRootPaneUI.this.setMaximized();
                    f2.setExtendedState(state | 6);
                }
            }
        }
    }

    private class MouseInputHandler
    implements MouseInputListener {
        private boolean isMovingWindow;
        private int dragCursor;
        private int dragOffsetX;
        private int dragOffsetY;
        private int dragWidth;
        private int dragHeight;
        private final PrivilegedExceptionAction getLocationAction = new PrivilegedExceptionAction(){

            public Object run() throws HeadlessException {
                PointerInfo pi = MouseInfo.getPointerInfo();
                return pi == null ? null : pi.getLocation();
            }
        };
        private CursorState cursorState = CursorState.NIL;

        private MouseInputHandler() {
        }

        @Override
        public void mousePressed(MouseEvent ev) {
            int frameState;
            JRootPane rootPane = SubstanceRootPaneUI.this.getRootPane();
            if (rootPane.getWindowDecorationStyle() == 0) {
                return;
            }
            Point dragWindowOffset = ev.getPoint();
            Window w2 = (Window)ev.getSource();
            if (w2 != null) {
                w2.toFront();
            }
            Point convertedDragWindowOffset = SwingUtilities.convertPoint(w2, dragWindowOffset, SubstanceRootPaneUI.this.getTitlePane());
            Frame f2 = null;
            Dialog d2 = null;
            if (w2 instanceof Frame) {
                f2 = (Frame)w2;
            } else if (w2 instanceof Dialog) {
                d2 = (Dialog)w2;
            }
            int n2 = frameState = f2 != null ? f2.getExtendedState() : 0;
            if (SubstanceRootPaneUI.this.getTitlePane() != null && SubstanceRootPaneUI.this.getTitlePane().contains(convertedDragWindowOffset)) {
                if ((f2 != null && (frameState & 6) == 0 || d2 != null) && dragWindowOffset.y >= 5 && dragWindowOffset.x >= 5 && dragWindowOffset.x < w2.getWidth() - 5) {
                    this.isMovingWindow = true;
                    this.dragOffsetX = dragWindowOffset.x;
                    this.dragOffsetY = dragWindowOffset.y;
                }
            } else if (f2 != null && f2.isResizable() && (frameState & 6) == 0 || d2 != null && d2.isResizable()) {
                this.dragOffsetX = dragWindowOffset.x;
                this.dragOffsetY = dragWindowOffset.y;
                this.dragWidth = w2.getWidth();
                this.dragHeight = w2.getHeight();
                this.dragCursor = this.getCursor(this.calculateCorner(w2, dragWindowOffset.x, dragWindowOffset.y));
            }
        }

        @Override
        public void mouseReleased(MouseEvent ev) {
            if (this.dragCursor != 0 && SubstanceRootPaneUI.this.window != null && !SubstanceRootPaneUI.this.window.isValid()) {
                SubstanceRootPaneUI.this.window.validate();
                SubstanceRootPaneUI.this.getRootPane().repaint();
            }
            this.isMovingWindow = false;
            this.dragCursor = 0;
        }

        @Override
        public void mouseMoved(MouseEvent ev) {
            JRootPane root = SubstanceRootPaneUI.this.getRootPane();
            if (root.getWindowDecorationStyle() == 0) {
                return;
            }
            Window w2 = (Window)ev.getSource();
            Frame f2 = null;
            Dialog d2 = null;
            if (w2 instanceof Frame) {
                f2 = (Frame)w2;
            } else if (w2 instanceof Dialog) {
                d2 = (Dialog)w2;
            }
            int cursor = this.getCursor(this.calculateCorner(w2, ev.getX(), ev.getY()));
            if (cursor != 0 && (f2 != null && f2.isResizable() && (f2.getExtendedState() & 6) == 0 || d2 != null && d2.isResizable())) {
                w2.setCursor(Cursor.getPredefinedCursor(cursor));
            } else {
                w2.setCursor(SubstanceRootPaneUI.this.lastCursor);
            }
        }

        private void adjust(Rectangle bounds, Dimension min, int deltaX, int deltaY, int deltaWidth, int deltaHeight) {
            bounds.x += deltaX;
            bounds.y += deltaY;
            bounds.width += deltaWidth;
            bounds.height += deltaHeight;
            if (min != null) {
                int correction;
                if (bounds.width < min.width) {
                    correction = min.width - bounds.width;
                    if (deltaX != 0) {
                        bounds.x -= correction;
                    }
                    bounds.width = min.width;
                }
                if (bounds.height < min.height) {
                    correction = min.height - bounds.height;
                    if (deltaY != 0) {
                        bounds.y -= correction;
                    }
                    bounds.height = min.height;
                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent ev) {
            Window w2 = (Window)ev.getSource();
            Point pt = ev.getPoint();
            if (this.isMovingWindow) {
                try {
                    Point windowPt = (Point)AccessController.doPrivileged(this.getLocationAction);
                    if (windowPt != null) {
                        windowPt.x -= this.dragOffsetX;
                        windowPt.y -= this.dragOffsetY;
                        w2.setLocation(windowPt);
                    }
                }
                catch (PrivilegedActionException ignored) {}
            } else if (this.dragCursor != 0) {
                Rectangle r2 = w2.getBounds();
                Rectangle startBounds = new Rectangle(r2);
                Dimension min = w2.getMinimumSize();
                switch (this.dragCursor) {
                    case 11: {
                        this.adjust(r2, min, 0, 0, pt.x + (this.dragWidth - this.dragOffsetX) - r2.width, 0);
                        break;
                    }
                    case 9: {
                        this.adjust(r2, min, 0, 0, 0, pt.y + (this.dragHeight - this.dragOffsetY) - r2.height);
                        break;
                    }
                    case 8: {
                        this.adjust(r2, min, 0, pt.y - this.dragOffsetY, 0, -(pt.y - this.dragOffsetY));
                        break;
                    }
                    case 10: {
                        this.adjust(r2, min, pt.x - this.dragOffsetX, 0, -(pt.x - this.dragOffsetX), 0);
                        break;
                    }
                    case 7: {
                        this.adjust(r2, min, 0, pt.y - this.dragOffsetY, pt.x + (this.dragWidth - this.dragOffsetX) - r2.width, -(pt.y - this.dragOffsetY));
                        break;
                    }
                    case 5: {
                        this.adjust(r2, min, 0, 0, pt.x + (this.dragWidth - this.dragOffsetX) - r2.width, pt.y + (this.dragHeight - this.dragOffsetY) - r2.height);
                        break;
                    }
                    case 6: {
                        this.adjust(r2, min, pt.x - this.dragOffsetX, pt.y - this.dragOffsetY, -(pt.x - this.dragOffsetX), -(pt.y - this.dragOffsetY));
                        break;
                    }
                    case 4: {
                        this.adjust(r2, min, pt.x - this.dragOffsetX, 0, -(pt.x - this.dragOffsetX), pt.y + (this.dragHeight - this.dragOffsetY) - r2.height);
                        break;
                    }
                }
                if (!r2.equals(startBounds)) {
                    w2.setBounds(r2);
                    if (Toolkit.getDefaultToolkit().isDynamicLayoutActive()) {
                        w2.validate();
                        SubstanceRootPaneUI.this.getRootPane().repaint();
                    }
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent ev) {
            Window w2 = (Window)ev.getSource();
            if (this.cursorState == CursorState.EXITED || this.cursorState == CursorState.NIL) {
                SubstanceRootPaneUI.this.lastCursor = w2.getCursor();
            }
            this.cursorState = CursorState.ENTERED;
            this.mouseMoved(ev);
        }

        @Override
        public void mouseExited(MouseEvent ev) {
            Window w2 = (Window)ev.getSource();
            w2.setCursor(SubstanceRootPaneUI.this.lastCursor);
            this.cursorState = CursorState.EXITED;
        }

        @Override
        public void mouseClicked(MouseEvent ev) {
            Window w2 = (Window)ev.getSource();
            if (!(w2 instanceof Frame)) {
                return;
            }
            Frame f2 = (Frame)w2;
            JComponent windowTitlePane = SubstanceRootPaneUI.this.getTitlePane();
            if (windowTitlePane == null) {
                return;
            }
            Point convertedPoint = SwingUtilities.convertPoint(w2, ev.getPoint(), windowTitlePane);
            int state = f2.getExtendedState();
            if (windowTitlePane != null && windowTitlePane.contains(convertedPoint) && ev.getClickCount() % 2 == 0 && (ev.getModifiers() & 0x10) != 0 && f2.isResizable()) {
                if ((state & 6) != 0) {
                    SubstanceRootPaneUI.this.setMaximized();
                    f2.setExtendedState(state & 0xFFFFFFF9);
                } else {
                    SubstanceRootPaneUI.this.setMaximized();
                    f2.setExtendedState(state | 6);
                }
            }
        }

        private int calculateCorner(Window w2, int x2, int y2) {
            Insets insets = w2.getInsets();
            int xPosition = this.calculatePosition(x2 - insets.left, w2.getWidth() - insets.left - insets.right);
            int yPosition = this.calculatePosition(y2 - insets.top, w2.getHeight() - insets.top - insets.bottom);
            if (xPosition == -1 || yPosition == -1) {
                return -1;
            }
            return yPosition * 5 + xPosition;
        }

        private int getCursor(int corner) {
            if (corner == -1) {
                return 0;
            }
            return cursorMapping[corner];
        }

        private int calculatePosition(int spot, int width) {
            if (spot < 5) {
                return 0;
            }
            if (spot < 16) {
                return 1;
            }
            if (spot >= width - 5) {
                return 4;
            }
            if (spot >= width - 16) {
                return 3;
            }
            return 2;
        }
    }

    protected class SubstanceRootLayout
    implements LayoutManager2 {
        protected SubstanceRootLayout() {
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            Dimension tpd;
            JComponent titlePane;
            Dimension mbd;
            int cpWidth = 0;
            int cpHeight = 0;
            int mbWidth = 0;
            int mbHeight = 0;
            int tpWidth = 0;
            int tpHeight = 0;
            Insets i2 = parent.getInsets();
            JRootPane root = (JRootPane)parent;
            Dimension cpd = root.getContentPane() != null ? root.getContentPane().getPreferredSize() : root.getSize();
            if (cpd != null) {
                cpWidth = cpd.width;
                cpHeight = cpd.height;
            }
            if (root.getJMenuBar() != null && (mbd = root.getJMenuBar().getPreferredSize()) != null) {
                mbWidth = mbd.width;
                mbHeight = mbd.height;
            }
            if (root.getWindowDecorationStyle() != 0 && root.getUI() instanceof SubstanceRootPaneUI && (titlePane = ((SubstanceRootPaneUI)root.getUI()).getTitlePane()) != null && (tpd = titlePane.getPreferredSize()) != null) {
                tpWidth = tpd.width;
                tpHeight = tpd.height;
            }
            return new Dimension(Math.max(Math.max(cpWidth, mbWidth), tpWidth) + i2.left + i2.right, cpHeight + mbHeight + tpHeight + i2.top + i2.bottom);
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            Dimension tpd;
            JComponent titlePane;
            Dimension mbd;
            int cpWidth = 0;
            int cpHeight = 0;
            int mbWidth = 0;
            int mbHeight = 0;
            int tpWidth = 0;
            int tpHeight = 0;
            Insets i2 = parent.getInsets();
            JRootPane root = (JRootPane)parent;
            Dimension cpd = root.getContentPane() != null ? root.getContentPane().getMinimumSize() : root.getSize();
            if (cpd != null) {
                cpWidth = cpd.width;
                cpHeight = cpd.height;
            }
            if (root.getJMenuBar() != null && (mbd = root.getJMenuBar().getMinimumSize()) != null) {
                mbWidth = mbd.width;
                mbHeight = mbd.height;
            }
            if (root.getWindowDecorationStyle() != 0 && root.getUI() instanceof SubstanceRootPaneUI && (titlePane = ((SubstanceRootPaneUI)root.getUI()).getTitlePane()) != null && (tpd = titlePane.getMinimumSize()) != null) {
                tpWidth = tpd.width;
                tpHeight = tpd.height;
            }
            return new Dimension(Math.max(Math.max(cpWidth, mbWidth), tpWidth) + i2.left + i2.right, cpHeight + mbHeight + tpHeight + i2.top + i2.bottom);
        }

        @Override
        public Dimension maximumLayoutSize(Container target) {
            int maxWidth;
            int maxHeight;
            Dimension tpd;
            JComponent titlePane;
            Dimension mbd;
            Dimension cpd;
            int cpWidth = Integer.MAX_VALUE;
            int cpHeight = Integer.MAX_VALUE;
            int mbWidth = Integer.MAX_VALUE;
            int mbHeight = Integer.MAX_VALUE;
            int tpWidth = Integer.MAX_VALUE;
            int tpHeight = Integer.MAX_VALUE;
            Insets i2 = target.getInsets();
            JRootPane root = (JRootPane)target;
            if (root.getContentPane() != null && (cpd = root.getContentPane().getMaximumSize()) != null) {
                cpWidth = cpd.width;
                cpHeight = cpd.height;
            }
            if (root.getJMenuBar() != null && (mbd = root.getJMenuBar().getMaximumSize()) != null) {
                mbWidth = mbd.width;
                mbHeight = mbd.height;
            }
            if (root.getWindowDecorationStyle() != 0 && root.getUI() instanceof SubstanceRootPaneUI && (titlePane = ((SubstanceRootPaneUI)root.getUI()).getTitlePane()) != null && (tpd = titlePane.getMaximumSize()) != null) {
                tpWidth = tpd.width;
                tpHeight = tpd.height;
            }
            if ((maxHeight = Math.max(Math.max(cpHeight, mbHeight), tpHeight)) != Integer.MAX_VALUE) {
                maxHeight = cpHeight + mbHeight + tpHeight + i2.top + i2.bottom;
            }
            if ((maxWidth = Math.max(Math.max(cpWidth, mbWidth), tpWidth)) != Integer.MAX_VALUE) {
                maxWidth += i2.left + i2.right;
            }
            return new Dimension(maxWidth, maxHeight);
        }

        @Override
        public void layoutContainer(Container parent) {
            Dimension tpd;
            JComponent titlePane;
            JRootPane root = (JRootPane)parent;
            Rectangle b2 = root.getBounds();
            Insets i2 = root.getInsets();
            int nextY = 0;
            int w2 = b2.width - i2.right - i2.left;
            int h2 = b2.height - i2.top - i2.bottom;
            if (root.getLayeredPane() != null) {
                root.getLayeredPane().setBounds(i2.left, i2.top, w2, h2);
            }
            if (root.getGlassPane() != null) {
                root.getGlassPane().setBounds(i2.left, i2.top, w2, h2);
            }
            if (root.getWindowDecorationStyle() != 0 && root.getUI() instanceof SubstanceRootPaneUI && (titlePane = ((SubstanceRootPaneUI)root.getUI()).getTitlePane()) != null && (tpd = titlePane.getPreferredSize()) != null) {
                int tpHeight = tpd.height;
                titlePane.setBounds(0, 0, w2, tpHeight);
                nextY += tpHeight;
            }
            if (root.getJMenuBar() != null) {
                Dimension mbd = root.getJMenuBar().getPreferredSize();
                root.getJMenuBar().setBounds(0, nextY, w2, mbd.height);
                nextY += mbd.height;
            }
            if (root.getContentPane() != null) {
                root.getContentPane().setBounds(0, nextY, w2, h2 < nextY ? 0 : h2 - nextY);
            }
        }

        @Override
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        @Override
        public void addLayoutComponent(Component comp, Object constraints) {
        }

        @Override
        public float getLayoutAlignmentX(Container target) {
            return 0.0f;
        }

        @Override
        public float getLayoutAlignmentY(Container target) {
            return 0.0f;
        }

        @Override
        public void invalidateLayout(Container target) {
        }
    }

    private static enum CursorState {
        EXITED,
        ENTERED,
        NIL;

    }
}

