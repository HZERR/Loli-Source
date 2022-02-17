/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.contrib.blogofbug.swing.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import org.pushingpixels.lafwidget.contrib.blogofbug.swing.SwingBugUtilities;
import org.pushingpixels.lafwidget.contrib.blogofbug.swing.components.GradientPanel;
import org.pushingpixels.lafwidget.contrib.blogofbug.swing.components.ReflectedImageLabel;
import org.pushingpixels.lafwidget.contrib.blogofbug.swing.layout.CaroselLayout;
import org.pushingpixels.lafwidget.contrib.blogofbug.utility.ImageUtilities;

public class JCarosel
extends GradientPanel
implements MouseListener,
MouseWheelListener {
    private static final boolean MEASURE_PERFORMANCE = true;
    public static final String FRONT_COMPONENT_CHANGE = "frontComponentChanged";
    protected CaroselLayout layout = new CaroselLayout(this);
    protected Component lastWheeledTo = null;
    protected int DEFAULT_CONTENT_WIDTH = 64;
    protected int spinStartDelay = 200;

    public JCarosel() {
        this.setLayout(this.layout);
        this.addMouseWheelListener(this);
        this.setContentWidth(this.DEFAULT_CONTENT_WIDTH);
    }

    public JCarosel(int contentWidth) {
        this();
        this.setContentWidth(contentWidth);
    }

    public void setContentWidth(int contentWidth) {
        this.layout.setNeutralContentWidth(contentWidth);
    }

    public void setDepthBasedAlpha(boolean useDepthBased) {
        this.layout.setDepthBasedAlpha(useDepthBased);
    }

    public void setLayout(CaroselLayout layout) {
        this.layout = layout;
        super.setLayout(layout);
    }

    @Override
    public Component add(Component component) {
        this.add("", component);
        component.setForeground(Color.WHITE);
        component.setBackground(Color.BLACK);
        this.bringToFront(this.getComponent(0));
        this.validate();
        return component;
    }

    public Component add(Image image, String text) {
        ReflectedImageLabel component = new ReflectedImageLabel(image, text);
        component.addMouseListener(this);
        return this.add(component);
    }

    @Override
    public void remove(Component component) {
        super.remove(component);
        if (this.getComponentCount() > 0) {
            this.bringToFront(this.getComponent(0));
        }
        this.invalidate();
        this.validate();
    }

    public Component add(String imageURL, int width, int height) {
        ReflectedImageLabel component = new ReflectedImageLabel(imageURL, width, height);
        component.addMouseListener(this);
        return this.add(component);
    }

    public Component add(String imageURL, String text, int width, int height) {
        ReflectedImageLabel component = new ReflectedImageLabel(imageURL, text, width, height);
        component.addMouseListener(this);
        return this.add(component);
    }

    public void bringToFront(Component component) {
        this.firePropertyChange(FRONT_COMPONENT_CHANGE, this.getComponent(0), component);
        this.layout.setFrontMostComponent(component);
    }

    public Component getFrontmost() {
        return this.getComponent(0);
    }

    @Override
    public void mouseClicked(final MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 1) {
            SwingBugUtilities.invokeAfter(new Runnable(){

                @Override
                public void run() {
                    JCarosel.this.bringToFront((Component)mouseEvent.getSource());
                }
            }, this.spinStartDelay);
        }
    }

    public void setSpinStartDelay(int spinStartDelay) {
        this.spinStartDelay = spinStartDelay;
    }

    public int getSpinStartDelay() {
        return this.spinStartDelay;
    }

    public void insertComponentAt(int i2, Component comp) {
        this.add(comp);
        this.layout.moveComponentTo(i2, comp);
    }

    public Component insertAt(int i2, String imageURL, int width, int height) {
        Component comp = this.add(imageURL, width, height);
        this.layout.moveComponentTo(i2, comp);
        return comp;
    }

    public Component insertAt(int i2, String imageURL, String text, int width, int height) {
        Component comp = this.add(imageURL, text, width, height);
        this.layout.moveComponentTo(i2, comp);
        return comp;
    }

    public void finalizeLayoutImmediately() {
        this.layout.layoutContainer(this);
        this.layout.finalizeLayoutImmediately();
        this.repaint();
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
        if (mouseWheelEvent.getScrollType() == 0) {
            int frontMostPosition;
            int lastPosition;
            int amount = mouseWheelEvent.getWheelRotation();
            if (this.lastWheeledTo == null) {
                this.lastWheeledTo = this.getFrontmost();
            }
            if (Math.abs((lastPosition = this.layout.getComponentIndex(this.lastWheeledTo)) - (frontMostPosition = this.layout.getComponentIndex(this.getComponent(0)))) > this.layout.getComponentCount() / 4) {
                return;
            }
            this.lastWheeledTo = amount > 0 ? this.layout.getPreviousComponent(this.lastWheeledTo) : this.layout.getNextComponent(this.lastWheeledTo);
            this.bringToFront(this.lastWheeledTo);
        }
    }

    public Component add(String imageURL) {
        ReflectedImageLabel component = new ReflectedImageLabel(imageURL);
        component.addMouseListener(this);
        return this.add(component);
    }

    public Component add(String imageURL, String textLabel) {
        ReflectedImageLabel component = new ReflectedImageLabel(ImageUtilities.loadCompatibleImage(imageURL), textLabel);
        component.addMouseListener(this);
        return this.add(component);
    }
}

