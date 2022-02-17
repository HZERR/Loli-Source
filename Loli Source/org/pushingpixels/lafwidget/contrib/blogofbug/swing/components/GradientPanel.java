/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.contrib.blogofbug.swing.components;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class GradientPanel
extends JPanel
implements ComponentListener {
    protected Color start;
    protected Color end;
    protected GradientPaint gp = null;
    protected BufferedImage cache = null;

    @Override
    public void setBackground(Color color) {
        this.start = color;
        this.end = color;
        super.setBackground(color);
    }

    public void setBackground(Color start, Color end) {
        this.start = start;
        this.end = end;
        this.makeGradient();
    }

    @Override
    public void paintComponent(Graphics graphics) {
        if (this.start == this.end) {
            super.paintComponent(graphics);
            return;
        }
        Graphics2D g2 = (Graphics2D)graphics;
        this.gp = new GradientPaint(this.getWidth() / 2, this.getY(), this.start, this.getWidth() / 2, this.getHeight(), this.end, false);
        g2.setPaint(this.gp);
        g2.fillRect(0, 0, this.getWidth(), this.getHeight());
        super.paintChildren(graphics);
    }

    private void makeGradient() {
        this.gp = new GradientPaint(this.getWidth() / 2, this.getY(), this.start, this.getWidth() / 2, this.getHeight(), this.end, false);
    }

    @Override
    public void componentResized(ComponentEvent componentEvent) {
        this.makeGradient();
    }

    @Override
    public void componentShown(ComponentEvent componentEvent) {
        this.makeGradient();
    }

    @Override
    public void componentMoved(ComponentEvent componentEvent) {
    }

    @Override
    public void componentHidden(ComponentEvent componentEvent) {
    }
}

