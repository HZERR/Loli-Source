/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileView;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicFileChooserUI;
import javax.swing.plaf.metal.MetalFileChooserUI;
import org.pushingpixels.lafwidget.LafWidget;
import org.pushingpixels.lafwidget.LafWidgetRepository;
import org.pushingpixels.lafwidget.utils.RenderingUtils;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;

public class SubstanceFileChooserUI
extends MetalFileChooserUI {
    protected Set lafWidgets;
    private final SubstanceFileView fileView = new SubstanceFileView();

    public void __org__pushingpixels__substance__internal__ui__SubstanceFileChooserUI__installComponents(JFileChooser jFileChooser) {
        super.installComponents(jFileChooser);
    }

    @Override
    public void installComponents(JFileChooser jFileChooser) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceFileChooserUI__installComponents(jFileChooser);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installComponents();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceFileChooserUI__installUI(JComponent jComponent) {
        super.installUI(jComponent);
    }

    @Override
    public void installUI(JComponent jComponent) {
        this.lafWidgets = LafWidgetRepository.getRepository().getMatchingWidgets(jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceFileChooserUI__installUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installUI();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceFileChooserUI__uninstallUI(JComponent jComponent) {
        super.uninstallUI(jComponent);
    }

    @Override
    public void uninstallUI(JComponent jComponent) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceFileChooserUI__uninstallUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallUI();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceFileChooserUI__installListeners(JFileChooser jFileChooser) {
        super.installListeners(jFileChooser);
    }

    @Override
    protected void installListeners(JFileChooser jFileChooser) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceFileChooserUI__installListeners(jFileChooser);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installListeners();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceFileChooserUI__installDefaults(JFileChooser jFileChooser) {
        super.installDefaults(jFileChooser);
    }

    @Override
    protected void installDefaults(JFileChooser jFileChooser) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceFileChooserUI__installDefaults(jFileChooser);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installDefaults();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceFileChooserUI__uninstallComponents(JFileChooser jFileChooser) {
        super.uninstallComponents(jFileChooser);
    }

    @Override
    public void uninstallComponents(JFileChooser jFileChooser) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceFileChooserUI__uninstallComponents(jFileChooser);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallComponents();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceFileChooserUI__uninstallListeners(JFileChooser jFileChooser) {
        super.uninstallListeners(jFileChooser);
    }

    @Override
    protected void uninstallListeners(JFileChooser jFileChooser) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceFileChooserUI__uninstallListeners(jFileChooser);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallListeners();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceFileChooserUI__uninstallDefaults(JFileChooser jFileChooser) {
        super.uninstallDefaults(jFileChooser);
    }

    @Override
    protected void uninstallDefaults(JFileChooser jFileChooser) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceFileChooserUI__uninstallDefaults(jFileChooser);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallDefaults();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceFileChooserUI__update(Graphics graphics, JComponent jComponent) {
        super.update(graphics, jComponent);
    }

    @Override
    public void update(Graphics graphics, JComponent jComponent) {
        Graphics2D graphics2D = (Graphics2D)graphics.create();
        RenderingUtils.installDesktopHints(graphics2D, jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceFileChooserUI__update(graphics2D, jComponent);
        graphics2D.dispose();
    }

    public static ComponentUI createUI(JComponent comp) {
        SubstanceCoreUtilities.testComponentCreationThreadingViolation(comp);
        return new SubstanceFileChooserUI((JFileChooser)comp);
    }

    public SubstanceFileChooserUI(JFileChooser filechooser) {
        super(filechooser);
    }

    @Override
    public FileView getFileView(JFileChooser fc) {
        return this.fileView;
    }

    private class SubstanceFileView
    extends BasicFileChooserUI.BasicFileView {
        private final Map<String, Icon> pathIconCache;

        private SubstanceFileView() {
            super(SubstanceFileChooserUI.this);
            this.pathIconCache = new HashMap<String, Icon>();
        }

        @Override
        public Icon getCachedIcon(File f2) {
            return this.pathIconCache.get(f2.getPath());
        }

        @Override
        public Icon getIcon(File f2) {
            Icon icon = this.getCachedIcon(f2);
            if (icon != null) {
                return icon;
            }
            icon = this.getDefaultIcon(f2);
            if (icon == null && (icon = super.getIcon(f2)) == null) {
                icon = new ImageIcon(SubstanceCoreUtilities.getBlankImage(8, 8));
            }
            this.cacheIcon(f2, icon);
            return icon;
        }

        @Override
        public void cacheIcon(File f2, Icon icon) {
            this.pathIconCache.put(f2.getPath(), icon);
        }

        @Override
        public void clearIconCache() {
            this.pathIconCache.clear();
        }

        public Icon getDefaultIcon(File f2) {
            JFileChooser fileChooser = SubstanceFileChooserUI.this.getFileChooser();
            Icon icon = fileChooser.getFileSystemView().getSystemIcon(f2);
            if (SubstanceCoreUtilities.useThemedDefaultIcon(null)) {
                icon = SubstanceCoreUtilities.getThemedIcon(fileChooser, icon);
            }
            return icon;
        }
    }
}

