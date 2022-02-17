/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.contrib.blogofbug.swing.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ConcurrentModificationException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.Timer;
import org.pushingpixels.lafwidget.contrib.blogofbug.swing.components.RichComponent;

public class CaroselLayout
implements LayoutManager,
ActionListener {
    protected int numberOfItems = 0;
    protected LinkedList<Component> components = new LinkedList();
    protected Hashtable additionalData = new Hashtable();
    protected double rotationalOffset = 0.0;
    protected double targetOffset = 0.0;
    private Timer animationTimer = new Timer(0, this);
    private Container container = null;
    private boolean depthBasedAlpha = true;
    private int neutralContentWidth = 64;

    public CaroselLayout(Container forContainer) {
        this.container = forContainer;
    }

    public void setNeutralContentWidth(int neutralContentWidth) {
        this.neutralContentWidth = neutralContentWidth;
    }

    public void moveComponentTo(int i2, Component comp) {
        this.components.remove(comp);
        this.components.add(i2, comp);
        this.recalculateCarosel();
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
        this.components.addLast(comp);
        this.recalculateCarosel();
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        this.components.remove(comp);
        this.recalculateCarosel();
    }

    protected CaroselPosition getPosition(Component comp) {
        CaroselPosition cpos = (CaroselPosition)this.additionalData.get(comp);
        if (cpos == null) {
            cpos = new CaroselPosition(comp);
            this.additionalData.put(comp, cpos);
        }
        return cpos;
    }

    protected int recalculateVisibleItems() {
        int visibleItems = 0;
        try {
            for (Component comp : this.components) {
                if (!comp.isVisible()) continue;
                ++visibleItems;
            }
        }
        catch (ConcurrentModificationException ex) {
            return this.recalculateVisibleItems();
        }
        return visibleItems;
    }

    protected void recalculateCarosel() {
        this.numberOfItems = this.recalculateVisibleItems();
        try {
            boolean animate = false;
            double itemCount = 0.0;
            for (Component comp : this.components) {
                CaroselPosition position = this.getPosition(comp);
                if (comp.isVisible()) {
                    double localAngle = itemCount * (Math.PI * 2 / (double)this.numberOfItems);
                    position.setAngle(localAngle);
                }
                if (position.isAnimating()) {
                    animate = true;
                }
                itemCount += 1.0;
            }
            if (animate) {
                this.animationTimer.start();
            }
        }
        catch (ConcurrentModificationException ex) {
            this.recalculateCarosel();
            return;
        }
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return this.preferredLayoutSize(parent);
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        Dimension dim = new Dimension(0, 0);
        int widestWidth = 0;
        int highestHeight = 0;
        for (Component comp : this.components) {
            if (!comp.isVisible()) continue;
            widestWidth = Math.max(widestWidth, comp.getPreferredSize().width);
            highestHeight = Math.max(highestHeight, comp.getPreferredSize().height);
        }
        dim.width = widestWidth * 3;
        dim.height = highestHeight * 2;
        Insets insets = parent.getInsets();
        dim.width += insets.left + insets.right;
        dim.height += insets.top + insets.bottom;
        return dim;
    }

    protected Point calculateCenter(Insets insets, int width, int height, int widest) {
        return new Point(insets.left + widest / 2 + width / 2, insets.top + height / 2);
    }

    public void setDepthBasedAlpha(boolean depthBasedAlpha) {
        this.depthBasedAlpha = depthBasedAlpha;
    }

    protected boolean shouldHide(Component comp, double angle, double s2) {
        if (this.depthBasedAlpha && comp instanceof RichComponent) {
            s2 = Math.min(1.0, Math.max(0.0, s2 / (double)1.2f));
            ((RichComponent)((Object)comp)).setAlpha((float)s2);
        }
        return false;
    }

    protected Dimension getCarouselRadius(Container target, Insets insets, int width, int height, int widestComponent) {
        return null;
    }

    protected double getScale(double angle, double x2, double y2, double carouselX, double carouselY) {
        return y2 / carouselY;
    }

    @Override
    public void layoutContainer(Container target) {
        LinkedList components = (LinkedList)this.components.clone();
        int numberOfItems = this.numberOfItems;
        this.recalculateCarosel();
        Insets insets = target.getInsets();
        int width = target.getSize().width - (insets.left + insets.right);
        int height = target.getSize().height - (insets.top + insets.bottom);
        int widestWidth = this.neutralContentWidth;
        boolean highestHeight = false;
        int radiusX = (width -= widestWidth) / 2;
        int radiusY = radiusX / 3;
        Dimension radius = this.getCarouselRadius(target, insets, width, height, widestWidth);
        if (radius != null) {
            radiusX = radius.width;
            radiusY = radius.height;
        }
        Point center = this.calculateCenter(insets, width, height, widestWidth);
        int centerX = center.x;
        int centerY = center.y;
        Iterator i2 = components.iterator();
        int p2 = 0;
        CaroselPosition[] z_order = new CaroselPosition[numberOfItems];
        while (i2.hasNext()) {
            Component comp = (Component)i2.next();
            CaroselPosition position = this.getPosition(comp);
            double finalAngle = position.getAngle() + this.rotationalOffset;
            double x2 = Math.sin(finalAngle) * (double)radiusX + (double)centerX;
            double y2 = Math.cos(finalAngle) * (double)radiusY + (double)centerY;
            double initialWidth = comp.getPreferredSize().width;
            double initialHeight = (double)comp.getPreferredSize().height * (initialWidth / (double)comp.getPreferredSize().width);
            double s2 = this.getScale(finalAngle, x2, y2, centerX, centerY);
            double boundsWidth = initialWidth * s2;
            double boundsHeight = initialHeight * s2;
            if (!this.shouldHide(comp, finalAngle, s2)) {
                int finalWidth = (int)boundsWidth / 1;
                int finalHeight = (int)boundsHeight / 1;
                comp.setBounds((int)x2 - (int)boundsWidth / 2, (int)y2 - (int)boundsHeight / 2, finalWidth &= 0xFFFFFFFE, finalHeight &= 0xFFFFFFFE);
            } else {
                comp.setBounds(-100, -100, 32, 32);
            }
            position.setZ(s2);
            z_order[p2++] = position;
        }
        boolean swaps = true;
        int limit = numberOfItems - 1;
        while (swaps) {
            swaps = false;
            for (int j2 = 0; j2 < limit; ++j2) {
                if (!(z_order[j2].getZ() < z_order[j2 + 1].getZ())) continue;
                CaroselPosition temp = z_order[j2 + 1];
                z_order[j2 + 1] = z_order[j2];
                z_order[j2] = temp;
                swaps = true;
            }
            if (--limit != 0) continue;
            swaps = false;
        }
        for (int j3 = 0; j3 < numberOfItems; ++j3) {
            if (target.getComponentZOrder(z_order[j3].getComponent()) == j3) continue;
            target.setComponentZOrder(z_order[j3].getComponent(), j3);
        }
    }

    public double getAngle() {
        return this.rotationalOffset;
    }

    public void setAngle(double d2) {
        this.rotationalOffset = d2;
    }

    protected boolean isAnimating() {
        if (!this.animationTimer.isRunning()) {
            return false;
        }
        try {
            for (Component comp : this.components) {
                CaroselPosition cpos = this.getPosition(comp);
                if (!cpos.isAnimating()) continue;
                return true;
            }
        }
        catch (ConcurrentModificationException ex) {
            return this.isAnimating();
        }
        return !(Math.abs(this.rotationalOffset - this.targetOffset) < 0.001);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (this.animationTimer == null) {
            return;
        }
        if (!this.animationTimer.isRunning()) {
            return;
        }
        if (!this.isAnimating()) {
            this.animationTimer.stop();
            return;
        }
        try {
            for (Component comp : this.components) {
                CaroselPosition cpos = this.getPosition(comp);
                if (!cpos.isAnimating()) continue;
                cpos.updateAngle();
            }
        }
        catch (ConcurrentModificationException cMe) {
            this.actionPerformed(actionEvent);
        }
        this.rotationalOffset += (this.targetOffset - this.rotationalOffset) / 6.0;
        if (this.container != null) {
            this.layoutContainer(this.container);
            if (this.container instanceof Component) {
                this.container.repaint();
            }
        }
    }

    public void finalizeLayoutImmediately() {
        for (Component comp : this.components) {
            CaroselPosition cpos = this.getPosition(comp);
            cpos.angle = cpos.targetAngle;
        }
        this.rotationalOffset = this.targetOffset;
        this.recalculateCarosel();
        this.container.validate();
    }

    protected final void setTarget(double target) {
        while (Math.abs(target - this.rotationalOffset) > Math.PI) {
            if (target < this.rotationalOffset) {
                target += Math.PI * 2;
                continue;
            }
            target -= Math.PI * 2;
        }
        this.targetOffset = target;
        if (!this.animationTimer.isRunning()) {
            this.animationTimer.setCoalesce(true);
            this.animationTimer.setRepeats(true);
            this.animationTimer.setDelay(20);
            this.animationTimer.start();
        }
    }

    public void setFrontMostComponent(Component component) {
        this.setTarget(-this.getPosition(component).getTargetAngle());
    }

    public Component getPreviousComponent(Component component) {
        int i2 = this.components.indexOf(component) - 1;
        if (i2 < 0) {
            return this.components.get(this.components.size() - 1);
        }
        return this.components.get(i2);
    }

    public Component getNextComponent(Component component) {
        int i2 = this.components.indexOf(component) + 1;
        if (i2 >= this.components.size()) {
            return this.components.get(0);
        }
        return this.components.get(i2);
    }

    public int getComponentCount() {
        return this.components.size();
    }

    public int getComponentIndex(Component comp) {
        return this.components.indexOf(comp);
    }

    public int getNeutralContentWidth() {
        return this.neutralContentWidth;
    }

    class CaroselPosition {
        protected double angle = 0.0;
        protected double scale = 0.0;
        protected double z = 0.0;
        protected Component component;
        protected boolean firstSet = false;
        protected double targetAngle = 0.0;

        public CaroselPosition(Component component) {
            this.component = component;
        }

        public Component getComponent() {
            return this.component;
        }

        public double getZ() {
            return this.z;
        }

        public void setZ(double z) {
            this.z = z;
        }

        public double getTargetAngle() {
            return this.targetAngle;
        }

        public double getAngle() {
            return this.angle;
        }

        public double getScale() {
            return this.scale;
        }

        public boolean isAnimating() {
            return !(Math.abs(this.angle - this.targetAngle) < 0.001);
        }

        public void moveToTarget() {
            this.angle = this.targetAngle;
        }

        public void updateAngle() {
            this.angle = Math.abs(this.angle - this.targetAngle) < 0.001 ? this.targetAngle : (this.angle += Math.min((this.targetAngle - this.angle) / 6.0, 0.1));
        }

        public void setAngle(double angle) {
            if (this.firstSet) {
                this.angle = angle;
                this.targetAngle = angle;
                this.firstSet = false;
            } else {
                this.targetAngle = angle;
            }
        }

        public void setScale(double scale) {
            this.scale = scale;
        }
    }
}

