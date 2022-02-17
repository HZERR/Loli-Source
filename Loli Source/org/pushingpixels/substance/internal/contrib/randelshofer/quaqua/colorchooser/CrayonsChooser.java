/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Icon;
import javax.swing.UIManager;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.plaf.UIResource;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser.Crayons;

public class CrayonsChooser
extends AbstractColorChooserPanel
implements UIResource {
    private Crayons crayons;

    public CrayonsChooser() {
        this.initComponents();
        this.crayons = new Crayons();
        this.add(this.crayons);
        this.crayons.addPropertyChangeListener(new PropertyChangeListener(){

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("Color")) {
                    CrayonsChooser.this.setColorToModel(CrayonsChooser.this.crayons.getColor());
                }
            }
        });
    }

    private void initComponents() {
    }

    @Override
    protected void buildChooser() {
    }

    @Override
    public String getDisplayName() {
        return UIManager.getString("ColorChooser.crayons");
    }

    @Override
    public Icon getLargeDisplayIcon() {
        return UIManager.getIcon("ColorChooser.crayonsIcon");
    }

    @Override
    public Icon getSmallDisplayIcon() {
        return this.getLargeDisplayIcon();
    }

    @Override
    public void updateChooser() {
        this.crayons.setColor(this.getColorFromModel());
    }

    public void setColorToModel(Color color) {
        this.getColorSelectionModel().setSelectedColor(color);
    }
}

