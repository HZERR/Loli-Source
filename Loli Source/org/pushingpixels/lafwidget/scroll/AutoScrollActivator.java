/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.scroll;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.Border;

public class AutoScrollActivator {
    protected JScrollPane scrollPane;
    protected AutoScrollProperties autoScrollProperties;
    protected static final ImageIcon H_IMAGE_ICON = new ImageIcon(AutoScrollActivator.class.getResource("resource/autoscroll_h.png"));
    protected static final ImageIcon V_IMAGE_ICON = new ImageIcon(AutoScrollActivator.class.getResource("resource/autoscroll_v.png"));
    protected static final ImageIcon HV_IMAGE_ICON = new ImageIcon(AutoScrollActivator.class.getResource("resource/autoscroll_all.png"));

    public AutoScrollActivator(JScrollPane scrollPane) {
        this.scrollPane = scrollPane;
        this.configureScrollPane();
    }

    protected void deactivateAutoScroll() {
        if (this.autoScrollProperties == null) {
            return;
        }
        this.autoScrollProperties.timer.stop();
        Toolkit.getDefaultToolkit().removeAWTEventListener(this.autoScrollProperties.toolkitListener);
        this.autoScrollProperties.iconPopupMenu.setVisible(false);
        this.autoScrollProperties = null;
    }

    protected void activateAutoScroll(MouseEvent e2) {
        this.autoScrollProperties = new AutoScrollProperties();
        this.autoScrollProperties.isDragMode = false;
        JViewport viewport = this.scrollPane.getViewport();
        PointerInfo pi = MouseInfo.getPointerInfo();
        if (pi == null) {
            return;
        }
        this.autoScrollProperties.currentLocation = pi.getLocation();
        SwingUtilities.convertPointFromScreen(this.autoScrollProperties.currentLocation, viewport);
        this.autoScrollProperties.startLocation = this.autoScrollProperties.currentLocation;
        final JPopupMenu iconPopupMenu = new JPopupMenu(){

            @Override
            public void setBorder(Border border) {
            }
        };
        iconPopupMenu.setFocusable(false);
        iconPopupMenu.setOpaque(false);
        JLabel iconLabel = new JLabel(this.getAutoScrollIcon());
        iconLabel.addMouseWheelListener(new MouseWheelListener(){

            @Override
            public void mouseWheelMoved(MouseWheelEvent e2) {
                AutoScrollActivator.this.deactivateAutoScroll();
            }
        });
        iconPopupMenu.add(iconLabel);
        iconPopupMenu.addMouseListener(new MouseAdapter(){

            @Override
            public void mousePressed(MouseEvent e2) {
                iconPopupMenu.setVisible(false);
            }
        });
        this.autoScrollProperties.iconPopupMenu = iconPopupMenu;
        Dimension iconPopupMenuSize = iconPopupMenu.getPreferredSize();
        iconPopupMenu.show(viewport, this.autoScrollProperties.startLocation.x - iconPopupMenuSize.width / 2, this.autoScrollProperties.startLocation.y - iconPopupMenuSize.height / 2);
        Container parent = iconPopupMenu.getParent();
        if (parent instanceof JComponent) {
            ((JComponent)parent).setBorder(null);
        }
        ActionListener actionListener = new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e2) {
                JViewport viewport = AutoScrollActivator.this.scrollPane.getViewport();
                Component view = viewport.getView();
                if (view == null) {
                    return;
                }
                Point viewPosition = viewport.getViewPosition();
                int offsetX = AutoScrollActivator.this.autoScrollProperties.currentLocation.x - AutoScrollActivator.this.autoScrollProperties.startLocation.x;
                int offsetY = AutoScrollActivator.this.autoScrollProperties.currentLocation.y - AutoScrollActivator.this.autoScrollProperties.startLocation.y;
                offsetX = offsetX > 0 ? Math.max(0, offsetX - 4) : Math.min(0, offsetX + 4);
                offsetY = offsetY > 0 ? Math.max(0, offsetY - 4) : Math.min(0, offsetY + 4);
                viewPosition = new Point(viewPosition.x + offsetX, viewPosition.y + offsetY);
                Dimension extentSize = viewport.getExtentSize();
                Dimension viewSize = view.getSize();
                if (viewSize.width - viewPosition.x < extentSize.width) {
                    viewPosition.x = viewSize.width - extentSize.width;
                }
                if (viewSize.height - viewPosition.y < extentSize.height) {
                    viewPosition.y = viewSize.height - extentSize.height;
                }
                if (viewPosition.x < 0) {
                    viewPosition.x = 0;
                }
                if (viewPosition.y < 0) {
                    viewPosition.y = 0;
                }
                viewport.setViewPosition(viewPosition);
            }
        };
        this.autoScrollProperties.timer = new Timer(50, actionListener);
        this.autoScrollProperties.timer.start();
        this.autoScrollProperties.toolkitListener = new AWTEventListener(){

            @Override
            public void eventDispatched(AWTEvent e2) {
                int eventID = e2.getID();
                switch (eventID) {
                    case 503: 
                    case 506: {
                        JViewport viewport = AutoScrollActivator.this.scrollPane.getViewport();
                        PointerInfo pi = MouseInfo.getPointerInfo();
                        if (pi == null) break;
                        AutoScrollActivator.this.autoScrollProperties.currentLocation = pi.getLocation();
                        SwingUtilities.convertPointFromScreen(AutoScrollActivator.this.autoScrollProperties.currentLocation, viewport);
                        if (AutoScrollActivator.this.autoScrollProperties.isDragMode || eventID != 506) break;
                        Dimension size = new Dimension(Math.abs(AutoScrollActivator.this.autoScrollProperties.currentLocation.x - AutoScrollActivator.this.autoScrollProperties.startLocation.x), Math.abs(AutoScrollActivator.this.autoScrollProperties.currentLocation.y - AutoScrollActivator.this.autoScrollProperties.startLocation.y));
                        AutoScrollActivator.this.autoScrollProperties.isDragMode = size.width > HV_IMAGE_ICON.getIconWidth() / 2 || size.height > HV_IMAGE_ICON.getIconHeight() / 2;
                        break;
                    }
                    case 501: 
                    case 507: {
                        AutoScrollActivator.this.deactivateAutoScroll();
                        break;
                    }
                    case 502: {
                        if (!AutoScrollActivator.this.autoScrollProperties.isDragMode || ((MouseEvent)e2).getButton() != 2) break;
                        AutoScrollActivator.this.deactivateAutoScroll();
                        break;
                    }
                    case 208: {
                        AutoScrollActivator.this.deactivateAutoScroll();
                    }
                }
            }
        };
        Toolkit.getDefaultToolkit().addAWTEventListener(this.autoScrollProperties.toolkitListener, 655408L);
    }

    protected void configureScrollPane() {
        for (MouseListener mouseListener : this.scrollPane.getMouseListeners()) {
            if (!(mouseListener instanceof AutoScrollMouseListener)) continue;
            return;
        }
        this.scrollPane.addMouseListener(new AutoScrollMouseListener(this));
    }

    protected ImageIcon getAutoScrollIcon() {
        ImageIcon icon = this.scrollPane.getHorizontalScrollBar().isVisible() ? (this.scrollPane.getVerticalScrollBar().isVisible() ? HV_IMAGE_ICON : H_IMAGE_ICON) : (this.scrollPane.getVerticalScrollBar().isVisible() ? V_IMAGE_ICON : HV_IMAGE_ICON);
        return icon;
    }

    public static void setAutoScrollEnabled(JScrollPane scrollPane, boolean isEnabled) {
        if (isEnabled) {
            new AutoScrollActivator(scrollPane);
        } else {
            for (MouseListener mouseListener : scrollPane.getMouseListeners()) {
                if (!(mouseListener instanceof AutoScrollMouseListener)) continue;
                scrollPane.removeMouseListener(mouseListener);
                return;
            }
        }
    }

    protected static class AutoScrollMouseListener
    extends MouseAdapter {
        protected AutoScrollActivator autoScrollActivator;

        public AutoScrollMouseListener(AutoScrollActivator autoScrollActivator) {
            this.autoScrollActivator = autoScrollActivator;
        }

        @Override
        public void mousePressed(MouseEvent e2) {
            if (e2.getButton() != 2) {
                return;
            }
            this.autoScrollActivator.activateAutoScroll(e2);
        }
    }

    protected static class AutoScrollProperties {
        public Point startLocation;
        public Point currentLocation;
        public Timer timer;
        public AWTEventListener toolkitListener;
        public boolean isDragMode;
        public JPopupMenu iconPopupMenu;

        protected AutoScrollProperties() {
        }
    }
}

