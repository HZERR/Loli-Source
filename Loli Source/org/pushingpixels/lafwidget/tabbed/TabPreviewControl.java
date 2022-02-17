/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.tabbed;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.image.BufferedImage;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import org.pushingpixels.lafwidget.animation.AnimationConfigurationManager;
import org.pushingpixels.lafwidget.utils.ShadowPopupBorder;
import org.pushingpixels.trident.Timeline;

public class TabPreviewControl
extends JPanel {
    protected JLabel iconLabel;
    protected JLabel titleLabel;
    protected JPanel previewImagePanel;
    protected BufferedImage previewImage;
    protected JTabbedPane tabPane;
    private float alpha;
    private float zoom;

    public TabPreviewControl(JTabbedPane tabPane, int tabIndex) {
        this.tabPane = tabPane;
        this.setLayout(new TabPreviewControlLayout());
        this.iconLabel = new JLabel(tabPane.getIconAt(tabIndex));
        this.titleLabel = new JLabel(tabPane.getTitleAt(tabIndex));
        this.titleLabel.setFont(this.titleLabel.getFont().deriveFont(1));
        this.previewImagePanel = new JPanel(){

            @Override
            public void paintComponent(Graphics g2) {
                super.paintComponent(g2);
                TabPreviewControl.this.paintTabThumbnail(g2);
            }
        };
        this.add(this.iconLabel);
        this.add(this.titleLabel);
        this.add(this.previewImagePanel);
        boolean isSelected = tabPane.getSelectedIndex() == tabIndex;
        LineBorder innerBorder = isSelected ? new LineBorder(Color.black, 2) : new LineBorder(Color.black, 1);
        this.setBorder(new CompoundBorder(new ShadowPopupBorder(), innerBorder));
        this.alpha = 0.0f;
        this.zoom = 1.0f;
    }

    public synchronized void paintTabThumbnail(Graphics g2) {
        if (this.previewImage != null) {
            int pw = this.previewImage.getWidth();
            int ph = this.previewImage.getHeight();
            int w2 = this.previewImagePanel.getWidth();
            int h2 = this.previewImagePanel.getHeight();
            Graphics2D g22 = (Graphics2D)g2.create();
            g22.setComposite(AlphaComposite.SrcOver.derive(this.alpha));
            int dx = (w2 - pw) / 2;
            int dy = (h2 - ph) / 2;
            g22.drawImage((Image)this.previewImage, dx, dy, null);
            g22.dispose();
        }
    }

    public void setTabIndex(int tabIndex) {
        this.iconLabel.setIcon(this.tabPane.getIconAt(tabIndex));
        this.titleLabel.setText(this.tabPane.getTitleAt(tabIndex));
        boolean isSelected = this.tabPane.getSelectedIndex() == tabIndex;
        LineBorder innerBorder = isSelected ? new LineBorder(Color.black, 2) : new LineBorder(Color.black, 1);
        this.setBorder(new CompoundBorder(new ShadowPopupBorder(), innerBorder));
    }

    public void setPreviewImage(BufferedImage previewImage, boolean toAnimate) {
        this.previewImage = previewImage;
        if (toAnimate) {
            Timeline fadeTimeline = new Timeline(this);
            AnimationConfigurationManager.getInstance().configureTimeline(fadeTimeline);
            fadeTimeline.addPropertyToInterpolate("alpha", Float.valueOf(0.0f), Float.valueOf(1.0f));
            fadeTimeline.play();
        }
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
        this.repaint();
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    public float getZoom() {
        return this.zoom;
    }

    protected class TabPreviewControlLayout
    implements LayoutManager {
        protected TabPreviewControlLayout() {
        }

        @Override
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        @Override
        public void layoutContainer(Container parent) {
            int width = parent.getWidth();
            int height = parent.getHeight();
            Insets insets = TabPreviewControl.this.getInsets();
            TabPreviewControl.this.iconLabel.setBounds(insets.left + 1, insets.top + 1, 16, 16);
            TabPreviewControl.this.titleLabel.setBounds(insets.left + 18, insets.top + 1, width - 18 - insets.left - insets.right, 16);
            TabPreviewControl.this.previewImagePanel.setBounds(insets.left + 1, insets.top + 17, width - insets.left - insets.right - 2, height - 17 - insets.top - insets.bottom);
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            return parent.getSize();
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            return this.minimumLayoutSize(parent);
        }
    }
}

