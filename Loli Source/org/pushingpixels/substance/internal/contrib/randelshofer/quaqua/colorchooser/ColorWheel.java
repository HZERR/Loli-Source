/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser.ColorWheelImageProducer;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser.HSBColorSliderModel;

public class ColorWheel
extends JPanel {
    private Image colorWheelImage;
    private ColorWheelImageProducer colorWheelProducer;
    private HSBColorSliderModel model = new HSBColorSliderModel();
    private MouseHandler mouseHandler;
    private ModelHandler modelHandler;

    public ColorWheel() {
        this.initComponents();
        this.colorWheelProducer = new ColorWheelImageProducer(0, 0);
        this.mouseHandler = new MouseHandler();
        this.modelHandler = new ModelHandler();
        this.addMouseListener(this.mouseHandler);
        this.addMouseMotionListener(this.mouseHandler);
        this.setOpaque(false);
    }

    public void setModel(HSBColorSliderModel m2) {
        if (this.model != null) {
            this.model.removeChangeListener(this.modelHandler);
        }
        this.model = m2;
        if (this.model != null) {
            this.model.addChangeListener(this.modelHandler);
            this.repaint();
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(100, 100);
    }

    public HSBColorSliderModel getModel() {
        return this.model;
    }

    @Override
    public void paintComponent(Graphics g2) {
        int w2 = this.getWidth();
        int h2 = this.getHeight();
        if (this.colorWheelImage == null || this.colorWheelImage.getWidth(this) != w2 || this.colorWheelImage.getHeight(this) != h2) {
            if (this.colorWheelImage != null) {
                this.colorWheelImage.flush();
            }
            this.colorWheelProducer = new ColorWheelImageProducer(w2, h2);
            this.colorWheelImage = this.createImage(this.colorWheelProducer);
        }
        this.colorWheelProducer.setBrightness((float)this.model.getValue(2) / 100.0f);
        this.colorWheelProducer.regenerateColorWheel();
        g2.drawImage(this.colorWheelImage, 0, 0, this);
        int x2 = w2 / 2 + (int)((double)(this.colorWheelProducer.getRadius() * this.model.getValue(1)) / 100.0 * Math.cos((double)this.model.getValue(0) * Math.PI / 180.0));
        int y2 = h2 / 2 - (int)((double)(this.colorWheelProducer.getRadius() * this.model.getValue(1)) / 100.0 * Math.sin((double)this.model.getValue(0) * Math.PI / 180.0));
        g2.setColor(Color.white);
        g2.fillRect(x2 - 1, y2 - 1, 2, 2);
        g2.setColor(Color.black);
        g2.drawRect(x2 - 2, y2 - 2, 3, 3);
    }

    private void initComponents() {
        this.setLayout(new BorderLayout());
    }

    private class ModelHandler
    implements ChangeListener {
        private ModelHandler() {
        }

        @Override
        public void stateChanged(ChangeEvent e2) {
            ColorWheel.this.repaint();
        }
    }

    private class MouseHandler
    implements MouseListener,
    MouseMotionListener {
        private MouseHandler() {
        }

        @Override
        public void mouseClicked(MouseEvent e2) {
        }

        @Override
        public void mouseDragged(MouseEvent e2) {
            this.update(e2);
        }

        @Override
        public void mouseEntered(MouseEvent e2) {
        }

        @Override
        public void mouseExited(MouseEvent e2) {
        }

        @Override
        public void mouseMoved(MouseEvent e2) {
        }

        @Override
        public void mousePressed(MouseEvent e2) {
        }

        @Override
        public void mouseReleased(MouseEvent e2) {
            this.update(e2);
        }

        private void update(MouseEvent e2) {
            int x2 = e2.getX() - ColorWheel.this.getWidth() / 2;
            int y2 = e2.getY() - ColorWheel.this.getHeight() / 2;
            float r2 = (float)Math.sqrt(x2 * x2 + y2 * y2);
            float theta = (float)Math.atan2(y2, -x2);
            ColorWheel.this.model.setValue(0, 180 + (int)((double)theta / Math.PI * 180.0));
            ColorWheel.this.model.setValue(1, (int)(Math.min(1.0f, r2 / (float)ColorWheel.this.colorWheelProducer.getRadius()) * 100.0f));
            ColorWheel.this.repaint();
        }
    }
}

