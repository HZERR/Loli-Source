/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget;

import javax.swing.JComponent;

public interface LafWidget<T extends JComponent> {
    public static final String HAS_LOCK_ICON = "lafwidgets.hasLockIcon";
    public static final String TABBED_PANE_PREVIEW_PAINTER = "lafwidgets.tabbedpanePreviewPainter";
    public static final String COMPONENT_PREVIEW_PAINTER = "lafwidgets.componentPreviewPainter";
    public static final String PASSWORD_STRENGTH_CHECKER = "lafwidgets.passwordStrengthChecker";
    public static final String TEXT_SELECT_ON_FOCUS = "lafwidgets.textSelectAllOnFocus";
    public static final String TEXT_FLIP_SELECT_ON_ESCAPE = "lafwidgets.textFlipSelectOnEscape";
    public static final String TEXT_EDIT_CONTEXT_MENU = "lafwidgets.textEditContextMenu";
    public static final String TREE_AUTO_DND_SUPPORT = "lafwidgets.treeAutoDnDSupport";
    public static final String AUTO_SCROLL = "lafwidget.scroll.auto";
    public static final String IGNORE_GLOBAL_LOCALE = "lafwidgets.ignoreGlobalLocale";

    public void setComponent(T var1);

    public boolean requiresCustomLafSupport();

    public void installUI();

    public void installDefaults();

    public void installListeners();

    public void installComponents();

    public void uninstallUI();

    public void uninstallDefaults();

    public void uninstallListeners();

    public void uninstallComponents();
}

