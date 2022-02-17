/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.MessageFormat;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.UIResource;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser.DefaultPalettes;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser.PaletteEntry;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser.PaletteEntryCellRenderer;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser.PaletteListModel;

public class ColorPalettesChooser
extends AbstractColorChooserPanel
implements UIResource {
    private static int lastSelectedPalette = 0;
    private JComboBox paletteCombo;
    private JLabel paletteLabel;
    private JList paletteList;
    private JScrollPane paletteScrollPane;

    public ColorPalettesChooser() {
        this.initComponents();
        Font font = UIManager.getFont("ColorChooser.font");
        this.paletteLabel.setFont(font);
        this.paletteCombo.setFont(font);
        this.paletteScrollPane.setFont(font);
        this.paletteList.setFont(font);
        this.paletteList.setCellRenderer(new PaletteEntryCellRenderer());
        DefaultComboBoxModel cbm = new DefaultComboBoxModel(this.loadPalettes());
        this.paletteCombo.setModel(cbm);
        this.paletteList.getSelectionModel().addListSelectionListener(new ListSelectionListener(){

            @Override
            public void valueChanged(ListSelectionEvent e2) {
                PaletteEntry entry = (PaletteEntry)ColorPalettesChooser.this.paletteList.getSelectedValue();
                if (entry != null) {
                    PaletteListModel lm = (PaletteListModel)ColorPalettesChooser.this.paletteList.getModel();
                    lm.setClosestIndex(-1);
                    ColorPalettesChooser.this.setColorToModel(entry.getColor());
                }
            }
        });
        this.paletteCombo.setSelectedIndex(lastSelectedPalette);
        this.loadPalettes();
        this.updatePaletteList();
    }

    protected Vector loadPalettes() {
        int i2;
        Vector<PaletteListModel> palettes = new Vector<PaletteListModel>();
        Color[] colors = DefaultPalettes.APPLE_COLORS;
        PaletteEntry[] entries = new PaletteEntry[colors.length];
        for (i2 = 0; i2 < colors.length; ++i2) {
            entries[i2] = new PaletteEntry(UIManager.getString("ColorChooser.apple." + Integer.toHexString(0xFF000000 | colors[i2].getRGB()).substring(2)), colors[i2]);
        }
        palettes.add(new PaletteListModel(UIManager.getString("ColorChooser.appleColors"), MessageFormat.format(UIManager.getString("ColorChooser.profileContainsNColors"), UIManager.getString("ColorChooser.appleColors"), entries.length), entries));
        colors = DefaultPalettes.CRAYONS;
        entries = new PaletteEntry[colors.length];
        for (i2 = 0; i2 < colors.length; ++i2) {
            entries[i2 % 8 + colors.length - i2 / 8 * 8 - 8] = new PaletteEntry(UIManager.getString("ColorChooser.crayon." + Integer.toHexString(0xFF000000 | colors[i2].getRGB()).substring(2)), colors[i2]);
        }
        palettes.add(new PaletteListModel(UIManager.getString("ColorChooser.crayons"), MessageFormat.format(UIManager.getString("ColorChooser.profileContainsNColors"), UIManager.getString("ColorChooser.crayons"), entries.length), entries));
        colors = DefaultPalettes.WEB_SAFE_COLORS;
        entries = new PaletteEntry[colors.length];
        for (i2 = 0; i2 < colors.length; ++i2) {
            entries[i2] = new PaletteEntry(Integer.toHexString(0xFF000000 | colors[i2].getRGB()).substring(2).toUpperCase(), colors[i2]);
        }
        palettes.add(new PaletteListModel(UIManager.getString("ColorChooser.webSafeColors"), MessageFormat.format(UIManager.getString("ColorChooser.profileContainsNColors"), UIManager.getString("ColorChooser.webSafeColors"), entries.length), entries));
        return palettes;
    }

    private void updatePaletteList() {
        PaletteListModel palette = (PaletteListModel)this.paletteCombo.getSelectedItem();
        this.paletteList.setModel(palette);
        this.paletteCombo.setToolTipText(palette.getInfo());
        this.updateChooser();
    }

    @Override
    protected void buildChooser() {
    }

    @Override
    public String getDisplayName() {
        return UIManager.getString("ColorChooser.colorPalettes");
    }

    @Override
    public Icon getLargeDisplayIcon() {
        return UIManager.getIcon("ColorChooser.colorPalettesIcon");
    }

    @Override
    public Icon getSmallDisplayIcon() {
        return this.getLargeDisplayIcon();
    }

    @Override
    public void updateChooser() {
        int i2;
        Color color;
        try {
            color = this.getColorFromModel();
            if (color == null) {
                return;
            }
        }
        catch (NullPointerException e2) {
            return;
        }
        int rgb = color.getRGB() & 0xFFFFFF;
        PaletteEntry entry = (PaletteEntry)this.paletteList.getSelectedValue();
        if (entry != null && (entry.getColor().getRGB() & 0xFFFFFF) == rgb) {
            return;
        }
        PaletteListModel lm = (PaletteListModel)this.paletteList.getModel();
        int n2 = lm.getSize();
        for (i2 = 0; i2 < n2 && ((entry = (PaletteEntry)lm.getElementAt(i2)).getColor().getRGB() & 0xFFFFFF) != rgb; ++i2) {
        }
        if (i2 < n2) {
            lm.setClosestIndex(-1);
            this.paletteList.setSelectedIndex(i2);
            this.paletteList.scrollRectToVisible(this.paletteList.getCellBounds(i2, i2));
        } else {
            this.paletteList.clearSelection();
            int closest = lm.computeClosestIndex(color);
            lm.setClosestIndex(closest);
            if (closest != -1) {
                this.paletteList.scrollRectToVisible(this.paletteList.getCellBounds(closest, closest));
            }
        }
    }

    public void setColorToModel(Color color) {
        this.getColorSelectionModel().setSelectedColor(color);
    }

    private void initComponents() {
        this.paletteLabel = new JLabel();
        this.paletteCombo = new JComboBox();
        this.paletteScrollPane = new JScrollPane();
        this.paletteList = new JList();
        this.setLayout(new GridBagLayout());
        this.paletteLabel.setLabelFor(this.paletteCombo);
        this.paletteLabel.setText(UIManager.getString("ColorChooser.list"));
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridy = 0;
        this.add((Component)this.paletteLabel, gridBagConstraints);
        this.paletteCombo.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent evt) {
                ColorPalettesChooser.this.paletteChanged(evt);
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = 2;
        gridBagConstraints.anchor = 17;
        gridBagConstraints.weightx = 1.0;
        this.add((Component)this.paletteCombo, gridBagConstraints);
        this.paletteScrollPane.setHorizontalScrollBarPolicy(31);
        this.paletteScrollPane.setVerticalScrollBarPolicy(22);
        this.paletteList.setSelectionMode(0);
        this.paletteScrollPane.setViewportView(this.paletteList);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 0;
        gridBagConstraints.fill = 1;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        this.add((Component)this.paletteScrollPane, gridBagConstraints);
    }

    private void paletteChanged(ItemEvent evt) {
        this.updatePaletteList();
        lastSelectedPalette = this.paletteCombo.getSelectedIndex();
    }
}

