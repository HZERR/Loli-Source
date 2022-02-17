/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.contrib.blogofbug.swing.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.pushingpixels.lafwidget.contrib.blogofbug.swing.borders.ImageBorder;
import org.pushingpixels.lafwidget.contrib.blogofbug.swing.components.GradientPanel;
import org.pushingpixels.lafwidget.contrib.blogofbug.swing.components.JCarosel;
import org.pushingpixels.lafwidget.contrib.blogofbug.swing.layout.OffsetCaroselLayout;

public class JCarouselMenu
extends GradientPanel
implements ListSelectionListener,
MouseListener,
KeyListener,
ChangeListener,
MouseWheelListener {
    private JCarosel carousel;
    private JList menu;
    private JScrollPane menuScroll;
    private DefaultListModel menuModel = new DefaultListModel();
    private LinkedList<MenuItem> menuItems = new LinkedList();
    private Map<Component, MenuItem> menuMap = new HashMap<Component, MenuItem>();
    private int lastSelection = -1;
    private UpDownButton upButton = new UpDownButton("Up");
    private UpDownButton downButton = new UpDownButton("Down");

    public JCarouselMenu(ImageBorder border) {
        this.carousel = new JCarosel();
        this.carousel.setLayout(new OffsetCaroselLayout(this.carousel));
        this.carousel.setBackground(null);
        this.carousel.setOpaque(false);
        this.carousel.setContentWidth(256);
        super.setLayout(new GridLayout(1, 2));
        super.add(this.carousel);
        this.upButton.setForeground(Color.WHITE);
        this.downButton.setForeground(Color.WHITE);
        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(null);
        menuPanel.setOpaque(false);
        menuPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        this.menu = new JList();
        this.menuScroll = new JScrollPane(this.menu, 21, 31);
        this.menuScroll.getViewport().setOpaque(false);
        this.menuScroll.setBorder(null);
        this.menuScroll.getViewport().addChangeListener(this);
        this.menu.setModel(this.menuModel);
        this.menu.setCellRenderer(new CarouselListCellRenderer(border));
        this.menu.setBackground(null);
        this.menu.setOpaque(false);
        this.menu.addListSelectionListener(this);
        this.menuScroll.setOpaque(true);
        this.menuScroll.setBackground(Color.BLACK);
        this.menuScroll.setBorder(BorderFactory.createEmptyBorder());
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.gridy = 0;
        gbc.fill = 2;
        menuPanel.add((Component)this.upButton, gbc);
        gbc.weighty = 1.0;
        gbc.weightx = 1.0;
        ++gbc.gridy;
        gbc.fill = 1;
        menuPanel.add((Component)this.menuScroll, gbc);
        gbc.weighty = 0.0;
        gbc.weightx = 0.0;
        ++gbc.gridy;
        gbc.fill = 2;
        menuPanel.add((Component)this.downButton, gbc);
        this.menu.addMouseListener(this);
        this.menu.addKeyListener(this);
        this.carousel.removeMouseWheelListener(this.carousel);
        this.carousel.addMouseWheelListener(this);
        this.menu.addMouseWheelListener(this);
        this.menuScroll.addMouseWheelListener(this);
        menuPanel.addMouseWheelListener(this);
        super.add(menuPanel);
    }

    public JCarouselMenu() {
        this(new ImageBorder(JCarouselMenu.class.getResource("/com/blogofbug/swing/borders/images/menu_highlight.png"), new Insets(10, 12, 16, 12)));
    }

    public void setUpDownColor(Color color) {
        this.upButton.setForeground(color);
        this.downButton.setForeground(color);
    }

    public JList getList() {
        return this.menu;
    }

    public void setSelectedIndex(int i2) {
        this.menu.setSelectedIndex(i2);
    }

    public Component add(Component component, String label) {
        this.carousel.add(label, component);
        MenuItem item = new MenuItem(component, label, null);
        this.menuItems.addLast(item);
        this.menuModel.addElement(item);
        this.menuMap.put(component, item);
        component.removeMouseListener(this.carousel);
        return component;
    }

    @Override
    public void remove(Component component) {
        this.carousel.remove(component);
        MenuItem menuItem = this.menuMap.remove(component);
        if (menuItem != null) {
            this.menuItems.remove(menuItem);
            this.menuModel.removeElement(menuItem);
        }
    }

    public Component add(Image image, String label, int width, int height) {
        Component comp = this.carousel.add(image, null);
        MenuItem item = new MenuItem(comp, label, null);
        this.menuItems.addLast(item);
        this.menuModel.addElement(item);
        comp.removeMouseListener(this.carousel);
        this.menuMap.put(comp, item);
        return comp;
    }

    public Component add(Image image, String label) {
        Component comp = this.carousel.add(image, null);
        MenuItem item = new MenuItem(comp, label, null);
        this.menuItems.addLast(item);
        this.menuModel.addElement(item);
        comp.removeMouseListener(this.carousel);
        this.menuMap.put(comp, item);
        return comp;
    }

    public Component add(Action action, int width, int height) {
        URL url = (URL)action.getValue("actionImageURL");
        if (url == null) {
            throw new InvalidParameterException("Supplied action does not have Image URL key (AbstractCarouselMenuAction.ACTION_IMAGE_URL)");
        }
        Component comp = this.carousel.add(url.toString());
        MenuItem item = new MenuItem(comp, (String)action.getValue("ShortDescription"), action);
        this.menuItems.addLast(item);
        this.menuMap.put(comp, item);
        this.menuModel.addElement(item);
        comp.removeMouseListener(this.carousel);
        return comp;
    }

    public Component add(Action action) {
        URL url = (URL)action.getValue("actionImageURL");
        if (url == null) {
            throw new InvalidParameterException("Supplied action does not have Image URL key (AbstractCarouselMenuAction.ACTION_IMAGE_URL)");
        }
        Component comp = this.carousel.add(url.toString());
        MenuItem item = new MenuItem(comp, (String)action.getValue("ShortDescription"), action);
        this.menuItems.addLast(item);
        this.menuMap.put(comp, item);
        this.menuModel.addElement(item);
        comp.removeMouseListener(this.carousel);
        return comp;
    }

    public Component add(String imageURL, String label, int width, int height) {
        Component comp = this.carousel.add(imageURL);
        MenuItem item = new MenuItem(comp, label, null);
        this.menuMap.put(comp, item);
        this.menuItems.addLast(item);
        this.menuModel.addElement(item);
        comp.removeMouseListener(this.carousel);
        return comp;
    }

    public Component add(String imageURL, String label) {
        Component comp = this.carousel.add(imageURL);
        MenuItem item = new MenuItem(comp, label, null);
        this.menuMap.put(comp, item);
        this.menuItems.addLast(item);
        this.menuModel.addElement(item);
        comp.removeMouseListener(this.carousel);
        return comp;
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        size.width /= 2;
        return size;
    }

    @Override
    public void stateChanged(ChangeEvent e2) {
        JViewport viewport = this.menuScroll.getViewport();
        int yPos = (int)viewport.getViewPosition().getY();
        this.upButton.setDoPaint(yPos > 0);
        this.downButton.setDoPaint((double)yPos + viewport.getExtentSize().getHeight() != (double)this.menu.getHeight());
    }

    @Override
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        MenuItem item = (MenuItem)this.menu.getSelectedValue();
        if (item == null) {
            return;
        }
        this.carousel.bringToFront(item.carouselComponent);
    }

    protected void processAction() {
        MenuItem item = (MenuItem)this.menu.getSelectedValue();
        if (item == null) {
            return;
        }
        if (item.action == null) {
            return;
        }
        item.action.actionPerformed(new ActionEvent(this, 1001, item.label));
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            this.processAction();
        }
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
    public void keyTyped(KeyEvent keyEvent) {
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()) {
            case 10: {
                this.processAction();
                break;
            }
            case 38: {
                if (this.menu.getSelectedIndex() == 0) {
                    this.lastSelection = this.menuModel.size() - 1;
                    break;
                }
                this.lastSelection = -1;
                break;
            }
            case 40: {
                this.lastSelection = this.menu.getSelectedIndex() == this.menuModel.size() - 1 ? 0 : -1;
            }
        }
    }

    public void setCellImageBorder(ImageBorder imageBorder) {
        CarouselListCellRenderer renderer = (CarouselListCellRenderer)this.menu.getCellRenderer();
        renderer.setImageBorder(imageBorder);
    }

    public void setCellRenderer(ListCellRenderer cellRenderer) {
        this.menu.setCellRenderer(cellRenderer);
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        if (this.lastSelection != -1) {
            this.menu.setSelectedIndex(this.lastSelection);
            this.menu.ensureIndexIsVisible(this.lastSelection);
            this.lastSelection = -1;
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
        if (mouseWheelEvent.getScrollType() == 0) {
            int amount = mouseWheelEvent.getWheelRotation();
            int lastSelection = amount < 0 ? (this.menu.getSelectedIndex() == 0 ? this.menuModel.size() - 1 : this.menu.getSelectedIndex() - 1) : (this.menu.getSelectedIndex() == this.menuModel.size() - 1 ? 0 : this.menu.getSelectedIndex() + 1);
            final int indexToSelect = lastSelection;
            SwingUtilities.invokeLater(new Runnable(){

                @Override
                public void run() {
                    JCarouselMenu.this.menu.setSelectedIndex(indexToSelect);
                    JCarouselMenu.this.menu.ensureIndexIsVisible(indexToSelect);
                    JCarouselMenu.this.menu.repaint();
                }
            });
        }
    }

    public void setUpDownIcons(Icon upIcon, Icon downIcon) {
        this.upButton.setIcon(upIcon);
        this.downButton.setIcon(downIcon);
    }

    public void setMenuScrollColor(Color color) {
        this.menuScroll.setBackground(color);
    }

    private class UpDownButton
    extends JLabel
    implements MouseListener {
        private boolean doPaint;

        public UpDownButton(String text) {
            super(text);
            this.doPaint = true;
            this.addMouseListener(this);
            this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        }

        public void setDoPaint(boolean shouldPaint) {
            this.doPaint = shouldPaint;
            this.repaint();
        }

        @Override
        public void paintComponent(Graphics g2) {
            if (this.doPaint) {
                Icon icon = this.getIcon();
                if (icon != null) {
                    int centerX = this.getWidth() - (this.getInsets().left + this.getInsets().right);
                    centerX = this.getInsets().left + centerX / 2;
                    int centerY = this.getHeight() - (this.getInsets().top + this.getInsets().bottom);
                    centerY = this.getInsets().top + centerY / 2;
                    icon.paintIcon(this, g2, centerX - icon.getIconWidth() / 2, centerY - icon.getIconHeight() / 2);
                } else {
                    Graphics2D g22 = (Graphics2D)g2;
                    g22.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(this.getForeground());
                    int centerX = this.getWidth() - (this.getInsets().left + this.getInsets().right);
                    centerX = this.getInsets().left + centerX / 2;
                    int height = this.getHeight() - (this.getInsets().top + this.getInsets().bottom);
                    int width = height * 2;
                    if ("Up".equals(this.getText())) {
                        g2.fillPolygon(new int[]{centerX - width, centerX, centerX + width}, new int[]{height, this.getInsets().top, height}, 3);
                    } else {
                        g2.fillPolygon(new int[]{centerX - width, centerX, centerX + width}, new int[]{this.getInsets().top, height, this.getInsets().top}, 3);
                    }
                }
            }
        }

        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            if (!this.doPaint) {
                return;
            }
            if (mouseEvent.getClickCount() == 1) {
                int height = ((JCarouselMenu)JCarouselMenu.this).menu.getCellBounds((int)((JCarouselMenu)JCarouselMenu.this).menu.getSelectedIndex(), (int)((JCarouselMenu)JCarouselMenu.this).menu.getSelectedIndex()).height;
                if (this.getText().equals("Up")) {
                    JCarouselMenu.this.setSelectedIndex(JCarouselMenu.this.menu.getSelectedIndex() - 1);
                    Point pos = JCarouselMenu.this.menuScroll.getViewport().getViewPosition();
                    pos.y -= height;
                    JCarouselMenu.this.menuScroll.getViewport().setViewPosition(pos);
                } else if (this.getText().equals("Down")) {
                    JCarouselMenu.this.setSelectedIndex(JCarouselMenu.this.menu.getSelectedIndex() + 1);
                    Point pos = JCarouselMenu.this.menuScroll.getViewport().getViewPosition();
                    pos.y += height;
                    JCarouselMenu.this.menuScroll.getViewport().setViewPosition(pos);
                }
            }
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
    }

    public class MenuItem {
        protected Component carouselComponent;
        protected String label;
        protected Action action;

        public MenuItem(Component component, String label, Action action) {
            this.label = label;
            this.carouselComponent = component;
            this.action = action;
        }

        public String getLabel() {
            return this.label;
        }

        public Action getAction() {
            return this.action;
        }

        public Component getCarouselComponent() {
            return this.carouselComponent;
        }
    }

    protected class CarouselListCellRenderer
    extends JLabel
    implements ListCellRenderer {
        ImageBorder imageBorder;

        public CarouselListCellRenderer(ImageBorder border) {
            this.imageBorder = border;
            this.setBorder(this.imageBorder);
        }

        public void setImageBorder(ImageBorder border) {
            this.imageBorder = border;
            this.setBorder(this.imageBorder);
        }

        public Component getListCellRendererComponent(JList jList, Object object, int i2, boolean isSelected, boolean cellHasFocus) {
            MenuItem item = (MenuItem)object;
            this.setText(item.label);
            if (!isSelected) {
                this.setBackground(null);
                this.imageBorder.setPaintBorder(false);
                this.setOpaque(false);
            } else {
                this.imageBorder.setPaintBorder(false);
                this.setOpaque(false);
            }
            this.setForeground(Color.WHITE);
            return this;
        }

        @Override
        public void paintComponent(Graphics g2) {
            this.imageBorder.paintCenter((Graphics2D)g2, this);
            super.paintComponent(g2);
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension d2 = super.getPreferredSize();
            d2.width += 20;
            return d2;
        }
    }
}

