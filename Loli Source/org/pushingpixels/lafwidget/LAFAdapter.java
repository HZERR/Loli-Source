/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget;

import java.awt.EventQueue;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeListenerProxy;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import org.pushingpixels.lafwidget.LafWidget;
import org.pushingpixels.lafwidget.LafWidgetRepository;
import org.pushingpixels.lafwidget.LafWidgetSupport;

public class LAFAdapter {
    private static ReinitListener reinitListener;
    private static InternalUIListener internalListener;
    private static boolean initialised;
    private static UIDefaults uiDelegates;
    private static final String[] UI_CLASSNAMES;
    private static final String LAF_PROPERTY = "Widgeted_LAFS";

    public static ComponentUI createUI(JComponent c2) {
        if (c2 == null || !initialised) {
            return null;
        }
        ComponentUI uiObject = uiDelegates.getUI(c2);
        LAFAdapter.uninstallLafWidgets(c2);
        if (!LAFAdapter.isPropertyListening(c2)) {
            c2.addPropertyChangeListener("UI", internalListener);
        }
        return uiObject;
    }

    private static boolean isPropertyListening(JComponent c2) {
        PropertyChangeListener[] pc = c2.getPropertyChangeListeners();
        if (pc.length == 0) {
            return false;
        }
        int ilen = pc.length;
        for (int i2 = 0; i2 < ilen; ++i2) {
            if (pc[i2] instanceof PropertyChangeListenerProxy && ((PropertyChangeListenerProxy)pc[i2]).getListener() == internalListener) {
                return true;
            }
            if (pc[i2] != internalListener) continue;
            return true;
        }
        return false;
    }

    private static void installLafWidgets(JComponent c2) {
        Set<LafWidget> lafWidgets;
        if (LafWidgetRepository.getRepository().getLafSupport().getClass().equals(LafWidgetSupport.class) && (lafWidgets = LafWidgetRepository.getRepository().getMatchingWidgets(c2)).size() > 0) {
            for (LafWidget lw : lafWidgets) {
                lw.installUI();
                lw.installComponents();
                lw.installDefaults();
                lw.installListeners();
            }
            c2.putClientProperty(LAF_PROPERTY, lafWidgets);
        }
    }

    private static void uninstallLafWidgets(JComponent c2) {
        Set lafWidgets = (Set)c2.getClientProperty(LAF_PROPERTY);
        if (lafWidgets != null) {
            for (LafWidget lw : lafWidgets) {
                lw.uninstallListeners();
                lw.uninstallDefaults();
                lw.uninstallComponents();
                lw.uninstallUI();
            }
            c2.putClientProperty(LAF_PROPERTY, null);
        }
    }

    public static void startWidget() {
        LAFAdapter.widget(true);
    }

    public static void stopWidget() {
        LAFAdapter.widget(false);
    }

    private static void widget(boolean enable) {
        Init init = new Init(enable);
        if (EventQueue.isDispatchThread()) {
            init.run();
        } else {
            try {
                EventQueue.invokeAndWait(init);
            }
            catch (Exception e2) {
                throw new RuntimeException(e2);
            }
        }
    }

    static {
        initialised = false;
        UI_CLASSNAMES = new String[]{"ButtonUI", "CheckBoxUI", "CheckBoxMenuItemUI", "ColorChooserUI", "ComboBoxUI", "DesktopIconUI", "DesktopPaneUI", "EditorPaneUI", "FormattedTextFieldUI", "InternalFrameUI", "LabelUI", "ListUI", "MenuUI", "MenuBarUI", "MenuItemUI", "OptionPaneUI", "PanelUI", "PasswordFieldUI", "PopupMenuUI", "PopupMenuSeparatorUI", "ProgressBarUI", "RadioButtonUI", "RadioButtonMenuItemUI", "RootPaneUI", "ScrollBarUI", "ScrollPaneUI", "SeparatorUI", "SliderUI", "SpinnerUI", "SplitPaneUI", "TabbedPaneUI", "TableUI", "TableHeaderUI", "TextAreaUI", "TextFieldUI", "TextPaneUI", "ToggleButtonUI", "ToolBarUI", "ToolBarSeparatorUI", "ToolTipUI", "TreeUI", "ViewportUI"};
    }

    private static class ReinitListener
    implements PropertyChangeListener {
        private ReinitListener() {
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if ("lookAndFeel".equals(evt.getPropertyName())) {
                UIManager.removePropertyChangeListener(reinitListener);
                initialised = false;
                uiDelegates = null;
                reinitListener = null;
                internalListener = null;
                LAFAdapter.startWidget();
            }
        }
    }

    private static class Init
    implements Runnable {
        private final boolean enable;

        private Init(boolean enable) {
            this.enable = enable;
        }

        @Override
        public void run() {
            if (!EventQueue.isDispatchThread()) {
                throw new IllegalStateException("This must be run on the EDT");
            }
            try {
                if (this.enable) {
                    Init.setup();
                } else {
                    Init.tearDown();
                }
            }
            catch (Exception e2) {
                initialised = false;
                uiDelegates = null;
                internalListener = null;
                reinitListener = null;
                throw new RuntimeException(e2);
            }
        }

        public static void setup() throws Exception {
            if (initialised) {
                return;
            }
            reinitListener = new ReinitListener();
            internalListener = new InternalUIListener();
            uiDelegates = new UIDefaults();
            UIDefaults defaults = UIManager.getLookAndFeelDefaults();
            for (String uiClassID : UI_CLASSNAMES) {
                uiDelegates.put(uiClassID, defaults.getString(uiClassID));
                defaults.put(uiClassID, LAFAdapter.class.getName());
            }
            UIManager.addPropertyChangeListener(reinitListener);
            initialised = true;
        }

        public static void tearDown() throws Exception {
            if (!initialised) {
                return;
            }
            UIManager.removePropertyChangeListener(reinitListener);
            UIManager.setLookAndFeel(UIManager.getLookAndFeel());
            uiDelegates = null;
            reinitListener = null;
            internalListener = null;
            initialised = false;
        }
    }

    private static class InternalUIListener
    implements PropertyChangeListener {
        private InternalUIListener() {
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            JComponent c2 = (JComponent)evt.getSource();
            c2.removePropertyChangeListener("UI", internalListener);
            LAFAdapter.installLafWidgets(c2);
        }
    }
}

