/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.scroll;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.MouseInputAdapter;
import org.pushingpixels.lafwidget.LafWidgetRepository;
import org.pushingpixels.lafwidget.LafWidgetUtilities2;
import org.pushingpixels.lafwidget.animation.AnimationConfigurationManager;
import org.pushingpixels.lafwidget.preview.PreviewPainter;
import org.pushingpixels.lafwidget.scroll.TweakedScrollPaneLayout;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.callback.UIThreadTimelineCallbackAdapter;

public class ScrollPaneSelector
extends JComponent {
    private static final double MAX_SIZE = 200.0;
    private static final String COMPONENT_ORIENTATION = "componentOrientation";
    private LayoutManager theFormerLayoutManager;
    private JScrollPane theScrollPane;
    private JComponent theComponent;
    private JPopupMenu thePopupMenu;
    private boolean toRestoreOriginal;
    private JButton theButton;
    private BufferedImage theImage;
    private Rectangle theStartRectangle;
    private Rectangle theRectangle;
    private Point theStartPoint;
    private Point thePrevPoint;
    private double theScale;
    private PropertyChangeListener propertyChangeListener;
    private ContainerAdapter theViewPortViewListener;

    ScrollPaneSelector() {
        this.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        this.theScrollPane = null;
        this.theImage = null;
        this.theStartRectangle = null;
        this.theRectangle = null;
        this.theStartPoint = null;
        this.theScale = 0.0;
        this.theButton = new JButton();
        LafWidgetRepository.getRepository().getLafSupport().markButtonAsFlat(this.theButton);
        this.theButton.setFocusable(false);
        this.theButton.setFocusPainted(false);
        MouseInputAdapter mil = new MouseInputAdapter(){

            @Override
            public void mousePressed(MouseEvent e2) {
                Point p2 = e2.getPoint();
                SwingUtilities.convertPointToScreen(p2, ScrollPaneSelector.this.theButton);
                ScrollPaneSelector.this.display(p2);
            }

            @Override
            public void mouseReleased(MouseEvent e2) {
                if (!ScrollPaneSelector.this.thePopupMenu.isVisible()) {
                    return;
                }
                ScrollPaneSelector.this.toRestoreOriginal = false;
                ScrollPaneSelector.this.thePopupMenu.setVisible(false);
                ScrollPaneSelector.this.theStartRectangle = ScrollPaneSelector.this.theRectangle;
            }

            @Override
            public void mouseDragged(MouseEvent e2) {
                if (ScrollPaneSelector.this.theStartPoint == null) {
                    return;
                }
                if (!ScrollPaneSelector.this.thePopupMenu.isShowing()) {
                    return;
                }
                Point newPoint = e2.getPoint();
                SwingUtilities.convertPointToScreen(newPoint, (Component)e2.getSource());
                Rectangle popupScreenRect = new Rectangle(ScrollPaneSelector.this.thePopupMenu.getLocationOnScreen(), ScrollPaneSelector.this.thePopupMenu.getSize());
                if (!popupScreenRect.contains(newPoint)) {
                    return;
                }
                int deltaX = (int)((double)(newPoint.x - ((ScrollPaneSelector)ScrollPaneSelector.this).thePrevPoint.x) / ScrollPaneSelector.this.theScale);
                int deltaY = (int)((double)(newPoint.y - ((ScrollPaneSelector)ScrollPaneSelector.this).thePrevPoint.y) / ScrollPaneSelector.this.theScale);
                ScrollPaneSelector.this.scroll(deltaX, deltaY, false);
                ScrollPaneSelector.this.thePrevPoint = newPoint;
            }
        };
        this.theButton.addMouseListener(mil);
        this.theButton.addMouseMotionListener(mil);
        this.setCursor(Cursor.getPredefinedCursor(13));
        this.thePopupMenu = new JPopupMenu();
        this.thePopupMenu.setLayout(new BorderLayout());
        this.thePopupMenu.add((Component)this, "Center");
        this.propertyChangeListener = new PropertyChangeListener(){

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (ScrollPaneSelector.this.theScrollPane == null) {
                    return;
                }
                if (ScrollPaneSelector.COMPONENT_ORIENTATION.equals(evt.getPropertyName())) {
                    ScrollPaneSelector.this.theScrollPane.setCorner("LOWER_LEADING_CORNER", null);
                    ScrollPaneSelector.this.theScrollPane.setCorner("LOWER_TRAILING_CORNER", ScrollPaneSelector.this.theButton);
                }
            }
        };
        this.theViewPortViewListener = new ContainerAdapter(){

            @Override
            public void componentAdded(ContainerEvent e2) {
                Component comp;
                if (ScrollPaneSelector.this.thePopupMenu.isVisible()) {
                    ScrollPaneSelector.this.thePopupMenu.setVisible(false);
                }
                ScrollPaneSelector.this.theComponent = (comp = ScrollPaneSelector.this.theScrollPane.getViewport().getView()) instanceof JComponent ? (JComponent)comp : null;
            }
        };
        this.thePopupMenu.addPropertyChangeListener(new PropertyChangeListener(){

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("visible".equals(evt.getPropertyName()) && !ScrollPaneSelector.this.thePopupMenu.isVisible()) {
                    ScrollPaneSelector.this.setCursor(Cursor.getPredefinedCursor(0));
                    if (ScrollPaneSelector.this.toRestoreOriginal) {
                        int deltaX = (int)((double)(((ScrollPaneSelector)ScrollPaneSelector.this).thePrevPoint.x - ((ScrollPaneSelector)ScrollPaneSelector.this).theStartPoint.x) / ScrollPaneSelector.this.theScale);
                        int deltaY = (int)((double)(((ScrollPaneSelector)ScrollPaneSelector.this).thePrevPoint.y - ((ScrollPaneSelector)ScrollPaneSelector.this).theStartPoint.y) / ScrollPaneSelector.this.theScale);
                        ScrollPaneSelector.this.scroll(-deltaX, -deltaY, true);
                    }
                }
            }
        });
    }

    @Override
    public Dimension getPreferredSize() {
        if (this.theImage == null || this.theRectangle == null) {
            return new Dimension();
        }
        Insets insets = this.getInsets();
        return new Dimension(this.theImage.getWidth(null) + insets.left + insets.right, this.theImage.getHeight(null) + insets.top + insets.bottom);
    }

    @Override
    protected void paintComponent(Graphics g1D) {
        if (this.theImage == null || this.theRectangle == null) {
            return;
        }
        Graphics2D g2 = (Graphics2D)g1D.create();
        Insets insets = this.getInsets();
        int xOffset = insets.left;
        int yOffset = insets.top;
        int availableWidth = this.getWidth() - insets.left - insets.right;
        int availableHeight = this.getHeight() - insets.top - insets.bottom;
        g2.drawImage((Image)this.theImage, xOffset, yOffset, null);
        Color tmpColor = g2.getColor();
        Area area = new Area(new Rectangle(xOffset, yOffset, availableWidth, availableHeight));
        area.subtract(new Area(this.theRectangle));
        g2.setColor(new Color(200, 200, 200, 128));
        g2.fill(area);
        g2.setColor(Color.BLACK);
        g2.draw(this.theRectangle);
        g2.setColor(tmpColor);
        g2.dispose();
    }

    void installOnScrollPane(JScrollPane aScrollPane) {
        if (this.theScrollPane != null) {
            this.uninstallFromScrollPane();
        }
        this.theScrollPane = aScrollPane;
        this.theFormerLayoutManager = this.theScrollPane.getLayout();
        this.theScrollPane.setLayout(new TweakedScrollPaneLayout());
        this.theScrollPane.firePropertyChange("layoutManager", false, true);
        this.theScrollPane.addPropertyChangeListener(COMPONENT_ORIENTATION, this.propertyChangeListener);
        this.theScrollPane.getViewport().addContainerListener(this.theViewPortViewListener);
        this.theScrollPane.setCorner("LOWER_TRAILING_CORNER", this.theButton);
        Component comp = this.theScrollPane.getViewport().getView();
        this.theComponent = comp instanceof JComponent ? (JComponent)comp : null;
        this.theButton.setIcon(LafWidgetRepository.getRepository().getLafSupport().getSearchIcon(UIManager.getInt("ScrollBar.width") - 3, this.theScrollPane.getComponentOrientation()));
        this.theScrollPane.doLayout();
    }

    void uninstallFromScrollPane() {
        if (this.theScrollPane == null) {
            return;
        }
        if (this.thePopupMenu.isVisible()) {
            this.thePopupMenu.setVisible(false);
        }
        this.theScrollPane.setCorner("LOWER_TRAILING_CORNER", null);
        this.theScrollPane.removePropertyChangeListener(COMPONENT_ORIENTATION, this.propertyChangeListener);
        this.theScrollPane.getViewport().removeContainerListener(this.theViewPortViewListener);
        this.theScrollPane.setLayout(this.theFormerLayoutManager);
        this.theScrollPane.firePropertyChange("layoutManager", true, false);
        this.theScrollPane = null;
    }

    private void display(Point aPointOnScreen) {
        if (this.theComponent == null) {
            return;
        }
        PreviewPainter previewPainter = LafWidgetUtilities2.getComponentPreviewPainter(this.theScrollPane);
        if (!previewPainter.hasPreview(this.theComponent.getParent(), this.theComponent, 0)) {
            return;
        }
        Dimension pDimension = previewPainter.getPreviewWindowDimension(this.theComponent.getParent(), this.theComponent, 0);
        double compWidth = this.theComponent.getWidth();
        double compHeight = this.theComponent.getHeight();
        double scaleX = pDimension.getWidth() / compWidth;
        double scaleY = pDimension.getHeight() / compHeight;
        this.theScale = Math.min(scaleX, scaleY);
        this.theImage = new BufferedImage((int)((double)this.theComponent.getWidth() * this.theScale), (int)((double)this.theComponent.getHeight() * this.theScale), 1);
        Graphics2D g2 = this.theImage.createGraphics();
        previewPainter.previewComponent(null, this.theComponent, 0, g2, 0, 0, this.theImage.getWidth(), this.theImage.getHeight());
        g2.dispose();
        this.theStartRectangle = this.theComponent.getVisibleRect();
        Insets insets = this.getInsets();
        this.theStartRectangle.x = (int)(this.theScale * (double)this.theStartRectangle.x + (double)insets.left);
        this.theStartRectangle.y = (int)(this.theScale * (double)this.theStartRectangle.y + (double)insets.right);
        this.theStartRectangle.width = (int)((double)this.theStartRectangle.width * this.theScale);
        this.theStartRectangle.height = (int)((double)this.theStartRectangle.height * this.theScale);
        this.theRectangle = this.theStartRectangle;
        Dimension pref = this.thePopupMenu.getPreferredSize();
        Point buttonLocation = this.theButton.getLocationOnScreen();
        Point popupLocation = new Point((this.theButton.getWidth() - pref.width) / 2, (this.theButton.getHeight() - pref.height) / 2);
        Point centerPoint = new Point(buttonLocation.x + popupLocation.x + this.theRectangle.x + this.theRectangle.width / 2, buttonLocation.y + popupLocation.y + this.theRectangle.y + this.theRectangle.height / 2);
        try {
            new Robot().mouseMove(centerPoint.x, centerPoint.y);
            this.theStartPoint = centerPoint;
        }
        catch (Exception e2) {
            this.theStartPoint = aPointOnScreen;
            popupLocation.x += this.theStartPoint.x - centerPoint.x;
            popupLocation.y += this.theStartPoint.y - centerPoint.y;
        }
        this.thePrevPoint = new Point(this.theStartPoint);
        this.toRestoreOriginal = true;
        this.thePopupMenu.show(this.theButton, popupLocation.x, popupLocation.y);
    }

    private void moveRectangle(int aDeltaX, int aDeltaY) {
        if (this.theStartRectangle == null) {
            return;
        }
        Insets insets = this.getInsets();
        Rectangle newRect = new Rectangle(this.theStartRectangle);
        newRect.x += aDeltaX;
        newRect.y += aDeltaY;
        newRect.x = Math.min(Math.max(newRect.x, insets.left), this.getWidth() - insets.right - newRect.width);
        newRect.y = Math.min(Math.max(newRect.y, insets.right), this.getHeight() - insets.bottom - newRect.height);
        Rectangle clip = new Rectangle();
        Rectangle.union(this.theRectangle, newRect, clip);
        clip.grow(2, 2);
        this.theRectangle = newRect;
        this.paintImmediately(clip);
    }

    private void syncRectangle() {
        JViewport viewport = this.theScrollPane.getViewport();
        Rectangle viewRect = viewport.getViewRect();
        Insets insets = this.getInsets();
        Rectangle newRect = new Rectangle();
        newRect.x = (int)(this.theScale * (double)viewRect.x + (double)insets.left);
        newRect.y = (int)(this.theScale * (double)viewRect.y + (double)insets.top);
        newRect.width = (int)((double)viewRect.width * this.theScale);
        newRect.height = (int)((double)viewRect.height * this.theScale);
        Rectangle clip = new Rectangle();
        Rectangle.union(this.theRectangle, newRect, clip);
        clip.grow(2, 2);
        this.theRectangle = newRect;
        this.paintImmediately(clip);
    }

    private void scroll(final int aDeltaX, final int aDeltaY, boolean toAnimate) {
        if (this.theComponent == null) {
            return;
        }
        final Rectangle oldRectangle = this.theComponent.getVisibleRect();
        final Rectangle newRectangle = new Rectangle(oldRectangle.x + aDeltaX, oldRectangle.y + aDeltaY, oldRectangle.width, oldRectangle.height);
        if (toAnimate) {
            Timeline scrollTimeline = new Timeline(this.theComponent);
            AnimationConfigurationManager.getInstance().configureTimeline(scrollTimeline);
            scrollTimeline.addCallback(new UIThreadTimelineCallbackAdapter(){

                @Override
                public void onTimelineStateChanged(Timeline.TimelineState oldState, Timeline.TimelineState newState, float durationFraction, float timelinePosition) {
                    if (oldState == Timeline.TimelineState.DONE && newState == Timeline.TimelineState.IDLE) {
                        ScrollPaneSelector.this.theComponent.scrollRectToVisible(newRectangle);
                        ScrollPaneSelector.this.syncRectangle();
                    }
                }

                @Override
                public void onTimelinePulse(float durationFraction, float timelinePosition) {
                    int x2 = (int)((float)oldRectangle.x + timelinePosition * (float)aDeltaX);
                    int y2 = (int)((float)oldRectangle.y + timelinePosition * (float)aDeltaY);
                    ScrollPaneSelector.this.theComponent.scrollRectToVisible(new Rectangle(x2, y2, oldRectangle.width, oldRectangle.height));
                    ScrollPaneSelector.this.syncRectangle();
                }
            });
            scrollTimeline.play();
        } else {
            this.theComponent.scrollRectToVisible(newRectangle);
            this.syncRectangle();
        }
    }
}

