/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.menu;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.ResourceBundle;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.SwingUtilities;
import org.pushingpixels.lafwidget.LafWidgetAdapter;
import org.pushingpixels.lafwidget.LafWidgetRepository;
import org.pushingpixels.lafwidget.LafWidgetSupport;
import org.pushingpixels.lafwidget.LafWidgetUtilities;
import org.pushingpixels.lafwidget.Resettable;

public class MenuSearchWidget
extends LafWidgetAdapter<JMenuBar>
implements Resettable {
    private boolean inEvent = false;
    protected PropertyChangeListener propertyListener;
    private SearchPanel searchPanel;

    private static int getMenuItemCount(JMenuItem menuItem) {
        int result = 1;
        if (menuItem instanceof JMenu) {
            JMenu menu = (JMenu)menuItem;
            for (int i2 = 0; i2 < menu.getMenuComponentCount(); ++i2) {
                Component child = menu.getMenuComponent(i2);
                if (!(child instanceof JMenuItem)) continue;
                result += MenuSearchWidget.getMenuItemCount((JMenuItem)child);
            }
        }
        return result;
    }

    public static int getMenuItemCount(JMenuBar menuBar) {
        int result = 0;
        for (int i2 = 0; i2 < menuBar.getMenuCount(); ++i2) {
            JMenu menu = menuBar.getMenu(i2);
            if (menu == null) continue;
            result += MenuSearchWidget.getMenuItemCount(menu);
        }
        return result;
    }

    @Override
    public void installUI() {
        final LafWidgetSupport lafSupport = LafWidgetRepository.getRepository().getLafSupport();
        this.searchPanel = new SearchPanel((JMenuBar)this.jcomp);
        ((JMenuBar)this.jcomp).add((Component)this.searchPanel, ((JMenuBar)this.jcomp).getComponentCount());
        this.searchPanel.setVisible(lafSupport.toInstallMenuSearch((JMenuBar)this.jcomp));
        ((JMenuBar)this.jcomp).addContainerListener(new ContainerAdapter(){

            @Override
            public void componentAdded(ContainerEvent e2) {
                if (!(e2.getChild() instanceof JMenu)) {
                    return;
                }
                if (!MenuSearchWidget.this.inEvent) {
                    MenuSearchWidget.this.inEvent = true;
                    Component removed = null;
                    for (int i2 = 0; i2 < ((JMenuBar)MenuSearchWidget.this.jcomp).getComponentCount(); ++i2) {
                        if (!(((JMenuBar)MenuSearchWidget.this.jcomp).getComponent(i2) instanceof SearchPanel)) continue;
                        removed = ((JMenuBar)MenuSearchWidget.this.jcomp).getComponent(i2);
                        break;
                    }
                    if (removed != null) {
                        ((JMenuBar)MenuSearchWidget.this.jcomp).remove(removed);
                        ((JMenuBar)MenuSearchWidget.this.jcomp).add(removed, ((JMenuBar)MenuSearchWidget.this.jcomp).getComponentCount());
                        if (lafSupport.toInstallMenuSearch((JMenuBar)MenuSearchWidget.this.jcomp)) {
                            removed.setVisible(true);
                        } else {
                            removed.setVisible(false);
                        }
                    }
                    MenuSearchWidget.this.inEvent = false;
                }
            }
        });
        this.searchPanel.applyComponentOrientation(((JMenuBar)this.jcomp).getComponentOrientation());
    }

    @Override
    public void uninstallUI() {
        ((JMenuBar)this.jcomp).remove(this.searchPanel);
        super.uninstallUI();
    }

    @Override
    public void installListeners() {
        super.installListeners();
        this.propertyListener = new PropertyChangeListener(){

            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                if ("componentOrientation".equals(evt.getPropertyName())) {
                    SwingUtilities.invokeLater(new Runnable(){

                        @Override
                        public void run() {
                            if (MenuSearchWidget.this.searchPanel != null) {
                                MenuSearchWidget.this.searchPanel.applyComponentOrientation((ComponentOrientation)evt.getNewValue());
                            }
                            MenuSearchWidget.this.reset();
                        }
                    });
                }
                if ("locale".equals(evt.getPropertyName())) {
                    SwingUtilities.invokeLater(new Runnable(){

                        @Override
                        public void run() {
                            MenuSearchWidget.this.reset();
                        }
                    });
                }
            }
        };
        ((JMenuBar)this.jcomp).addPropertyChangeListener(this.propertyListener);
    }

    @Override
    public void uninstallListeners() {
        ((JMenuBar)this.jcomp).removePropertyChangeListener(this.propertyListener);
        this.propertyListener = null;
    }

    @Override
    public void reset() {
        LafWidgetSupport support = LafWidgetRepository.getRepository().getLafSupport();
        if (this.searchPanel == null) {
            return;
        }
        for (Map.Entry entry : this.searchPanel.resultButtons.entrySet()) {
            int index = (Integer)entry.getKey();
            JButton button = (JButton)entry.getValue();
            Icon markerIcon = support == null ? LafWidgetUtilities.getHexaMarker(index) : support.getNumberIcon(index);
            button.setIcon(markerIcon);
        }
        int iconDim = support.getLookupIconSize();
        Icon searchIcon = support == null ? LafWidgetUtilities.getSearchIcon(iconDim, this.searchPanel.getComponentOrientation().isLeftToRight()) : support.getSearchIcon(iconDim, this.searchPanel.getComponentOrientation());
        this.searchPanel.searchButton.setIcon(searchIcon);
        ResourceBundle bundle = LafWidgetUtilities.getResourceBundle(this.jcomp);
        this.searchPanel.searchButton.setToolTipText(bundle.getString("Tooltip.menuSearchButton"));
        this.searchPanel.searchStringField.setToolTipText(bundle.getString("Tooltip.menuSearchField"));
    }

    @Override
    public boolean requiresCustomLafSupport() {
        return false;
    }

    private class SearchResultsLayout
    implements LayoutManager {
        private SearchPanel searchPanel;

        public SearchResultsLayout(SearchPanel searchPanel) {
            this.searchPanel = searchPanel;
        }

        @Override
        public void addLayoutComponent(String name, Component c2) {
        }

        @Override
        public void removeLayoutComponent(Component c2) {
        }

        @Override
        public Dimension preferredLayoutSize(Container c2) {
            if (this.searchPanel.searchButton.isSelected()) {
                return c2.getSize();
            }
            int buttonSize = LafWidgetRepository.getRepository().getLafSupport().getLookupButtonSize();
            return new Dimension(buttonSize, buttonSize);
        }

        @Override
        public Dimension minimumLayoutSize(Container c2) {
            int buttonSize = LafWidgetRepository.getRepository().getLafSupport().getLookupButtonSize();
            return new Dimension(buttonSize, buttonSize);
        }

        @Override
        public void layoutContainer(Container c2) {
            block5: {
                int width;
                int height;
                block4: {
                    height = c2.getHeight();
                    width = c2.getWidth();
                    if (!this.searchPanel.searchButton.isVisible()) {
                        return;
                    }
                    boolean leftToRight = ((JMenuBar)MenuSearchWidget.this.jcomp).getComponentOrientation().isLeftToRight();
                    if (!leftToRight) break block4;
                    int x2 = 2;
                    int sbWidth = ((SearchPanel)this.searchPanel).searchButton.getPreferredSize().width;
                    int sbHeight = ((SearchPanel)this.searchPanel).searchButton.getPreferredSize().height;
                    this.searchPanel.searchButton.setBounds(x2, (height - sbHeight) / 2, sbWidth, sbHeight);
                    x2 += sbWidth + 4;
                    if (!this.searchPanel.isVisible()) break block5;
                    int tbWidth = ((SearchPanel)this.searchPanel).searchStringField.getPreferredSize().width;
                    int tbHeight = ((SearchPanel)this.searchPanel).searchStringField.getPreferredSize().height;
                    tbHeight = Math.min(tbHeight, height - 2);
                    this.searchPanel.searchStringField.setBounds(x2, (height - tbHeight) / 2, tbWidth, tbHeight);
                    x2 += tbWidth + 2;
                    int buttonCount = this.searchPanel.resultButtons.size();
                    for (int i2 = 1; i2 <= buttonCount; ++i2) {
                        JButton button = (JButton)this.searchPanel.resultButtons.get(i2);
                        int bw = button.getPreferredSize().width;
                        int bh = button.getPreferredSize().height;
                        button.setBounds(x2, (height - bh) / 2, bw, bh);
                        x2 += bw + 1;
                    }
                    break block5;
                }
                int x3 = width - 2;
                int sbWidth = ((SearchPanel)this.searchPanel).searchButton.getPreferredSize().width;
                int sbHeight = ((SearchPanel)this.searchPanel).searchButton.getPreferredSize().height;
                this.searchPanel.searchButton.setBounds(x3 - sbWidth, (height - sbHeight) / 2, sbWidth, sbHeight);
                x3 -= sbWidth + 4;
                if (this.searchPanel.isVisible()) {
                    int tbWidth = ((SearchPanel)this.searchPanel).searchStringField.getPreferredSize().width;
                    int tbHeight = ((SearchPanel)this.searchPanel).searchStringField.getPreferredSize().height;
                    tbHeight = Math.min(tbHeight, height - 2);
                    this.searchPanel.searchStringField.setBounds(x3 - tbWidth, (height - tbHeight) / 2, tbWidth, tbHeight);
                    x3 -= tbWidth + 2;
                    int buttonCount = this.searchPanel.resultButtons.size();
                    for (int i3 = 1; i3 <= buttonCount; ++i3) {
                        JButton button = (JButton)this.searchPanel.resultButtons.get(i3);
                        int bw = button.getPreferredSize().width;
                        int bh = button.getPreferredSize().height;
                        button.setBounds(x3 - bw, (height - bh) / 2, bw, bh);
                        x3 -= bw + 1;
                    }
                }
            }
        }
    }

    private static class SearchResult {
        private MenuElement[] menuElements;

        public SearchResult(JMenuBar menuBar, LinkedList<JMenu> menuPath, JMenuItem menuLeaf) {
            int count = 1;
            if (menuPath != null) {
                count += 2 * menuPath.size();
            }
            if (menuLeaf != null) {
                ++count;
            }
            this.menuElements = new MenuElement[count];
            count = 0;
            this.menuElements[count++] = menuBar;
            if (menuPath != null) {
                for (JMenu menu : menuPath) {
                    this.menuElements[count++] = menu;
                    this.menuElements[count++] = menu.getPopupMenu();
                }
            }
            if (menuLeaf != null) {
                this.menuElements[count] = menuLeaf;
            }
        }

        public MenuElement[] getMenuElements() {
            return this.menuElements;
        }

        public String toString() {
            StringBuffer sb = new StringBuffer();
            if (this.menuElements != null) {
                String sep = "";
                for (int i2 = 0; i2 < this.menuElements.length; ++i2) {
                    MenuElement menuElem = this.menuElements[i2];
                    if (!(menuElem instanceof JMenuItem)) continue;
                    sb.append(sep);
                    sep = " -> ";
                    sb.append(((JMenuItem)menuElem).getText());
                }
            }
            return sb.toString();
        }

        public boolean isEnabled() {
            for (int i2 = 0; i2 < this.menuElements.length; ++i2) {
                JMenuItem menuItem;
                MenuElement menuElem = this.menuElements[i2];
                if (!(menuElem instanceof JMenuItem) || (menuItem = (JMenuItem)menuElem).isEnabled()) continue;
                return false;
            }
            return true;
        }
    }

    private static class SearchResultListener
    implements ActionListener {
        private SearchResult searchResult;

        public SearchResultListener(SearchResult searchResult) {
            this.searchResult = searchResult;
        }

        @Override
        public void actionPerformed(ActionEvent e2) {
            MenuElement[] menuElements = this.searchResult.menuElements;
            MenuSelectionManager.defaultManager().setSelectedPath(menuElements);
        }
    }

    private class SearchPanel
    extends JPanel {
        private JToggleButton searchButton;
        private JTextField searchStringField;
        private Map<Integer, JButton> resultButtons;

        public SearchPanel(final JMenuBar menuBar) {
            this.setLayout(new SearchResultsLayout(this));
            LafWidgetSupport support = LafWidgetRepository.getRepository().getLafSupport();
            int iconDim = support.getLookupIconSize();
            int buttonDim = support.getLookupButtonSize();
            Icon searchIcon = support == null ? LafWidgetUtilities.getSearchIcon(iconDim, ((JMenuBar)MenuSearchWidget.this.jcomp).getComponentOrientation().isLeftToRight()) : support.getSearchIcon(iconDim, ((JMenuBar)MenuSearchWidget.this.jcomp).getComponentOrientation());
            this.searchButton = new JToggleButton(searchIcon);
            this.searchButton.setPreferredSize(new Dimension(buttonDim, buttonDim));
            ResourceBundle bundle = LafWidgetUtilities.getResourceBundle(menuBar);
            this.searchButton.setToolTipText(bundle.getString("Tooltip.menuSearchButton"));
            this.searchButton.setFocusable(false);
            if (support != null) {
                support.markButtonAsFlat(this.searchButton);
            }
            this.add(this.searchButton);
            this.searchButton.addActionListener(new ActionListener(){

                @Override
                public void actionPerformed(ActionEvent e2) {
                    SwingUtilities.invokeLater(new Runnable(){

                        @Override
                        public void run() {
                            boolean toShow = SearchPanel.this.searchButton.isSelected();
                            SearchPanel.this.searchStringField.setVisible(toShow);
                            SearchPanel.this.searchStringField.requestFocus();
                            for (JButton resultButton : SearchPanel.this.resultButtons.values()) {
                                resultButton.setVisible(toShow);
                            }
                            SearchPanel.this.repaint();
                            SearchPanel.this.revalidate();
                        }
                    });
                }
            });
            this.searchButton.addMouseListener(new MouseAdapter(){

                @Override
                public void mousePressed(MouseEvent e2) {
                    if ((e2.getModifiers() & 2) != 0) {
                        SwingUtilities.invokeLater(new Runnable(){

                            @Override
                            public void run() {
                                SearchPanel.this.removeAll();
                                SearchPanel.this.repaint();
                                ((JMenuBar)MenuSearchWidget.this.jcomp).revalidate();
                            }
                        });
                    }
                }
            });
            this.searchStringField = new JTextField();
            this.searchStringField.setColumns(10);
            this.add(this.searchStringField);
            this.searchStringField.setVisible(false);
            this.searchStringField.setToolTipText(bundle.getString("Tooltip.menuSearchField"));
            this.resultButtons = new HashMap<Integer, JButton>();
            this.searchStringField.addActionListener(new ActionListener(){

                @Override
                public void actionPerformed(ActionEvent e2) {
                    String searchString = SearchPanel.this.searchStringField.getText().toLowerCase();
                    if (searchString.trim().length() == 0) {
                        return;
                    }
                    for (JButton toRemove : SearchPanel.this.resultButtons.values()) {
                        SearchPanel.this.remove(toRemove);
                    }
                    SearchPanel.this.resultButtons.clear();
                    LinkedList searchResults = SearchPanel.this.findOccurences(searchString);
                    int count = 0;
                    for (SearchResult searchResult : searchResults) {
                        if (count == 16) break;
                        LafWidgetSupport support = LafWidgetRepository.getRepository().getLafSupport();
                        Icon markerIcon = support.getNumberIcon(count + 1);
                        JButton resultButton = new JButton(markerIcon);
                        resultButton.addActionListener(new SearchResultListener(searchResult));
                        resultButton.setEnabled(searchResult.isEnabled());
                        SearchPanel.this.add(resultButton);
                        SearchPanel.this.resultButtons.put(count + 1, resultButton);
                        resultButton.setToolTipText("<html><body><b>" + searchResult.toString() + "</b><br>" + LafWidgetUtilities.getResourceBundle(menuBar).getString("Tooltip.menuSearchTooltip") + "</html>");
                        if (support != null) {
                            support.markButtonAsFlat(resultButton);
                        }
                        ++count;
                    }
                    SearchPanel.this.repaint();
                    ((JMenuBar)MenuSearchWidget.this.jcomp).revalidate();
                }
            });
        }

        private LinkedList<SearchResult> findOccurences(String searchPattern) {
            LinkedList<SearchResult> result = new LinkedList<SearchResult>();
            LinkedList<JMenu> currentPath = new LinkedList<JMenu>();
            for (int i2 = 0; i2 < ((JMenuBar)MenuSearchWidget.this.jcomp).getComponentCount(); ++i2) {
                Component component = ((JMenuBar)MenuSearchWidget.this.jcomp).getComponent(i2);
                if (!(component instanceof JMenu)) continue;
                JMenu menu = (JMenu)component;
                this.checkMenu(currentPath, menu, searchPattern, result);
            }
            return result;
        }

        private void checkMenu(LinkedList<JMenu> currentPath, JMenuItem menuItem, String searchPattern, LinkedList<SearchResult> matchingResults) {
            String menuItemText = menuItem.getText();
            if (menuItemText.toLowerCase().indexOf(searchPattern) >= 0) {
                matchingResults.addLast(new SearchResult((JMenuBar)MenuSearchWidget.this.jcomp, currentPath, menuItem));
            }
            if (menuItem instanceof JMenu) {
                JMenu menu = (JMenu)menuItem;
                currentPath.addLast(menu);
                for (int i2 = 0; i2 < menu.getMenuComponentCount(); ++i2) {
                    Component menuComponent = menu.getMenuComponent(i2);
                    if (!(menuComponent instanceof JMenuItem)) continue;
                    JMenuItem childItem = (JMenuItem)menuComponent;
                    this.checkMenu(currentPath, childItem, searchPattern, matchingResults);
                }
                currentPath.removeLast();
            }
        }

        @Override
        public void setVisible(boolean aFlag) {
            super.setVisible(aFlag);
            if (aFlag) {
                this.searchStringField.requestFocus();
            }
        }
    }
}

