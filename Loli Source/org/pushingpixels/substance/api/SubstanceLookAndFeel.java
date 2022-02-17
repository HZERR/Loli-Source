/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api;

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.KeyboardFocusManager;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.JTabbedPane;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.IconUIResource;
import javax.swing.plaf.basic.BasicLookAndFeel;
import org.pushingpixels.lafplugin.ComponentPluginManager;
import org.pushingpixels.lafplugin.PluginManager;
import org.pushingpixels.lafwidget.LafWidgetRepository;
import org.pushingpixels.lafwidget.animation.AnimationFacet;
import org.pushingpixels.lafwidget.utils.LookUtils;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceConstants;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.fonts.FontPolicy;
import org.pushingpixels.substance.api.fonts.FontSet;
import org.pushingpixels.substance.api.fonts.SubstanceFontUtilities;
import org.pushingpixels.substance.api.inputmaps.InputMapSet;
import org.pushingpixels.substance.api.inputmaps.SubstanceInputMapUtilities;
import org.pushingpixels.substance.api.skin.SkinChangeListener;
import org.pushingpixels.substance.api.skin.SkinInfo;
import org.pushingpixels.substance.api.tabbed.BaseTabCloseListener;
import org.pushingpixels.substance.internal.contrib.jgoodies.looks.common.ShadowPopupFactory;
import org.pushingpixels.substance.internal.painter.DecorationPainterUtils;
import org.pushingpixels.substance.internal.plugin.SubstanceSkinPlugin;
import org.pushingpixels.substance.internal.ui.SubstanceRootPaneUI;
import org.pushingpixels.substance.internal.utils.LazyResettableHashMap;
import org.pushingpixels.substance.internal.utils.LocaleChangeListener;
import org.pushingpixels.substance.internal.utils.MemoryAnalyzer;
import org.pushingpixels.substance.internal.utils.SubstanceColorSchemeUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceImageCreator;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;
import org.pushingpixels.substance.internal.utils.SubstanceTitlePane;
import org.pushingpixels.substance.internal.utils.SubstanceWidgetManager;
import org.pushingpixels.substance.internal.utils.SubstanceWidgetSupport;
import org.pushingpixels.substance.internal.utils.TabCloseListenerManager;

public abstract class SubstanceLookAndFeel
extends BasicLookAndFeel {
    public static final String PLUGIN_XML = "META-INF/substance-plugin.xml";
    private static ComponentPluginManager componentPlugins;
    private static PluginManager skinPlugins;
    protected static final Set<SkinChangeListener> skinChangeListeners;
    protected static final Set<LocaleChangeListener> localeChangeListeners;
    private static boolean toUseConstantThemesOnDialogs;
    protected PropertyChangeListener focusOwnerChangeListener;
    protected KeyboardFocusManager currentKeyboardFocusManager;
    public static final AnimationFacet TREE_SMART_SCROLL_ANIMATION_KIND;
    public static final String WATERMARK_VISIBLE = "substancelaf.watermark.visible";
    public static final String BUTTON_NO_MIN_SIZE_PROPERTY = "substancelaf.buttonnominsize";
    public static final String BUTTON_PAINT_NEVER_PROPERTY = "substancelaf.buttonpaintnever";
    public static final String BUTTON_SIDE_PROPERTY = "substancelaf.buttonside";
    public static final String BUTTON_OPEN_SIDE_PROPERTY = "substancelaf.buttonopenSide";
    public static final String CORNER_RADIUS = "substancelaf.cornerRadius";
    public static final String FLAT_PROPERTY = "substancelaf.componentFlat";
    public static final String HEAP_STATUS_TRACE_FILE = "substancelaf.heapStatusTraceFile";
    public static final String WINDOW_MODIFIED = "windowModified";
    public static final String WINDOW_AUTO_DEACTIVATE = "windowAutoDeactivate";
    public static final String WINDOW_ROUNDED_CORNERS_PROPERTY = "substancelaf.windowRoundedCorners";
    public static final String WINDOW_ROUNDED_CORNERS = "windowRoundedCorners";
    public static final String TABBED_PANE_CLOSE_BUTTONS_PROPERTY = "substancelaf.tabbedpanehasclosebuttons";
    public static final String TABBED_PANE_CLOSE_BUTTONS_MODIFIED_ANIMATION = "substancelaf.tabbedpaneclosebuttonsmodifiedanimation";
    public static final String TABBED_PANE_CLOSE_CALLBACK = "substancelaf.tabbedpanecloseCallback";
    public static final String TABBED_PANE_CONTENT_BORDER_KIND = "substancelaf.tabbedPaneContentBorderKind";
    public static final String TABBED_PANE_ROTATE_SIDE_TABS = "substancelaf.rotate";
    public static final String TABLE_LEADING_VERTICAL_LINE = "substancelaf.tableLeadingVerticalLine";
    public static final String TABLE_TRAILING_VERTICAL_LINE = "substancelaf.tableTrailingVerticalLine";
    public static final String COMBO_BOX_POPUP_FLYOUT_ORIENTATION = "substancelaf.comboboxpopupFlyoutOrientation";
    public static final String SCROLL_PANE_BUTTONS_POLICY = "substancelaf.scrollPaneButtonsPolicy";
    public static final String SHOW_EXTRA_WIDGETS = "substancelaf.addWidgets";
    public static final String MENU_GUTTER_FILL_KIND = "substancelaf.menuGutterFillKind";
    public static final String FOCUS_KIND = "substancelaf.focusKind";
    public static final String COMBO_POPUP_PROTOTYPE = "substancelaf.comboPopupPrototype";
    public static final String TRACE_FILE = "substancelaf.traceFile";
    public static final String PASSWORD_ECHO_PER_CHAR = "substancelaf.passwordEchoPerChar";
    public static final String USE_THEMED_DEFAULT_ICONS = "substancelaf.useThemedDefaultIcons";
    public static final String COLORIZATION_FACTOR = "substancelaf.colorizationFactor";
    protected static final String SUBSTANCE_FONT_POLICY_KEY = "substancelaf.fontPolicyKey";
    protected static final String SUBSTANCE_INPUT_MAP_SET_KEY = "substancelaf.inputMapSetKey";
    public static final String BUTTON_SHAPER_PROPERTY = "substancelaf.buttonShaper";
    public static final String SKIN_PROPERTY = "substancelaf.skin";
    private static ResourceBundle LABEL_BUNDLE;
    private static ClassLoader labelBundleClassLoader;
    protected SubstanceSkin skin;
    protected String name;
    private static SubstanceSkin currentSkin;

    protected SubstanceLookAndFeel(SubstanceSkin skin) {
        this.skin = skin;
        this.name = "Substance " + skin.getDisplayName();
        SubstanceLookAndFeel.initPluginsIfNecessary();
    }

    protected static void initPluginsIfNecessary() {
        if (skinPlugins != null) {
            return;
        }
        skinPlugins = new PluginManager(PLUGIN_XML, "laf-plugin", "skin-plugin-class");
        componentPlugins = new ComponentPluginManager(PLUGIN_XML);
    }

    public static synchronized ResourceBundle getLabelBundle() {
        if (LABEL_BUNDLE == null) {
            LABEL_BUNDLE = labelBundleClassLoader == null ? ResourceBundle.getBundle("org.pushingpixels.substance.internal.resources.Labels", Locale.getDefault()) : ResourceBundle.getBundle("org.pushingpixels.substance.internal.resources.Labels", Locale.getDefault(), labelBundleClassLoader);
            for (LocaleChangeListener lcl : localeChangeListeners) {
                lcl.localeChanged();
            }
        }
        return LABEL_BUNDLE;
    }

    public static synchronized ResourceBundle getLabelBundle(Locale locale) {
        if (labelBundleClassLoader == null) {
            return ResourceBundle.getBundle("org.pushingpixels.substance.internal.resources.Labels", locale);
        }
        return ResourceBundle.getBundle("org.pushingpixels.substance.internal.resources.Labels", locale, labelBundleClassLoader);
    }

    public static synchronized void resetLabelBundle() {
        LABEL_BUNDLE = null;
        LafWidgetRepository.resetLabelBundle();
    }

    public static SubstanceSkin getCurrentSkin() {
        LookAndFeel current = UIManager.getLookAndFeel();
        if (current instanceof SubstanceLookAndFeel) {
            return currentSkin;
        }
        return null;
    }

    public static SubstanceSkin getCurrentSkin(Component c2) {
        return SubstanceCoreUtilities.getSkin(c2);
    }

    @Override
    public String getDescription() {
        return "Substance Look and Feel by Kirill Grouchnikov";
    }

    @Override
    public String getID() {
        return this.name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean isNativeLookAndFeel() {
        return false;
    }

    @Override
    public boolean isSupportedLookAndFeel() {
        return true;
    }

    @Override
    protected void initClassDefaults(UIDefaults table) {
        super.initClassDefaults(table);
        String UI_CLASSNAME_PREFIX = "org.pushingpixels.substance.internal.ui.Substance";
        Object[] uiDefaults = new Object[]{"ButtonUI", UI_CLASSNAME_PREFIX + "ButtonUI", "CheckBoxUI", UI_CLASSNAME_PREFIX + "CheckBoxUI", "ComboBoxUI", UI_CLASSNAME_PREFIX + "ComboBoxUI", "CheckBoxMenuItemUI", UI_CLASSNAME_PREFIX + "CheckBoxMenuItemUI", "DesktopIconUI", UI_CLASSNAME_PREFIX + "DesktopIconUI", "DesktopPaneUI", UI_CLASSNAME_PREFIX + "DesktopPaneUI", "EditorPaneUI", UI_CLASSNAME_PREFIX + "EditorPaneUI", "FileChooserUI", UI_CLASSNAME_PREFIX + "FileChooserUI", "FormattedTextFieldUI", UI_CLASSNAME_PREFIX + "FormattedTextFieldUI", "InternalFrameUI", UI_CLASSNAME_PREFIX + "InternalFrameUI", "LabelUI", UI_CLASSNAME_PREFIX + "LabelUI", "ListUI", UI_CLASSNAME_PREFIX + "ListUI", "MenuUI", UI_CLASSNAME_PREFIX + "MenuUI", "MenuBarUI", UI_CLASSNAME_PREFIX + "MenuBarUI", "MenuItemUI", UI_CLASSNAME_PREFIX + "MenuItemUI", "OptionPaneUI", UI_CLASSNAME_PREFIX + "OptionPaneUI", "PanelUI", UI_CLASSNAME_PREFIX + "PanelUI", "PasswordFieldUI", UI_CLASSNAME_PREFIX + "PasswordFieldUI", "PopupMenuUI", UI_CLASSNAME_PREFIX + "PopupMenuUI", "PopupMenuSeparatorUI", UI_CLASSNAME_PREFIX + "PopupMenuSeparatorUI", "ProgressBarUI", UI_CLASSNAME_PREFIX + "ProgressBarUI", "RadioButtonUI", UI_CLASSNAME_PREFIX + "RadioButtonUI", "RadioButtonMenuItemUI", UI_CLASSNAME_PREFIX + "RadioButtonMenuItemUI", "RootPaneUI", UI_CLASSNAME_PREFIX + "RootPaneUI", "ScrollBarUI", UI_CLASSNAME_PREFIX + "ScrollBarUI", "ScrollPaneUI", UI_CLASSNAME_PREFIX + "ScrollPaneUI", "SeparatorUI", UI_CLASSNAME_PREFIX + "SeparatorUI", "SliderUI", UI_CLASSNAME_PREFIX + "SliderUI", "SpinnerUI", UI_CLASSNAME_PREFIX + "SpinnerUI", "SplitPaneUI", UI_CLASSNAME_PREFIX + "SplitPaneUI", "TabbedPaneUI", UI_CLASSNAME_PREFIX + "TabbedPaneUI", "TableUI", UI_CLASSNAME_PREFIX + "TableUI", "TableHeaderUI", UI_CLASSNAME_PREFIX + "TableHeaderUI", "TextAreaUI", UI_CLASSNAME_PREFIX + "TextAreaUI", "TextFieldUI", UI_CLASSNAME_PREFIX + "TextFieldUI", "TextPaneUI", UI_CLASSNAME_PREFIX + "TextPaneUI", "ToggleButtonUI", UI_CLASSNAME_PREFIX + "ToggleButtonUI", "ToolBarUI", UI_CLASSNAME_PREFIX + "ToolBarUI", "ToolBarSeparatorUI", UI_CLASSNAME_PREFIX + "ToolBarSeparatorUI", "ToolTipUI", UI_CLASSNAME_PREFIX + "ToolTipUI", "TreeUI", UI_CLASSNAME_PREFIX + "TreeUI", "ViewportUI", UI_CLASSNAME_PREFIX + "ViewportUI"};
        table.putDefaults(uiDefaults);
    }

    @Override
    protected void initComponentDefaults(UIDefaults table) {
        super.initComponentDefaults(table);
        this.initFontDefaults(table);
        this.skin.addCustomEntriesToTable(table);
    }

    public static void setFontPolicy(FontPolicy fontPolicy) {
        UIManager.put(SUBSTANCE_FONT_POLICY_KEY, fontPolicy);
        SubstanceSizeUtils.setControlFontSize(-1);
        SubstanceSizeUtils.resetPointsToPixelsRatio(fontPolicy);
        SubstanceLookAndFeel.setSkin(SubstanceLookAndFeel.getCurrentSkin());
    }

    public static FontPolicy getFontPolicy() {
        FontPolicy policy = (FontPolicy)UIManager.get(SUBSTANCE_FONT_POLICY_KEY);
        if (policy != null) {
            return policy;
        }
        return SubstanceFontUtilities.getDefaultFontPolicy();
    }

    public static void setInputMapSet(InputMapSet inputMapSet) {
        UIManager.put(SUBSTANCE_INPUT_MAP_SET_KEY, inputMapSet);
        SubstanceLookAndFeel.setSkin(SubstanceLookAndFeel.getCurrentSkin());
    }

    public static InputMapSet getInputMapSet() {
        InputMapSet inputMapSet = (InputMapSet)UIManager.get(SUBSTANCE_INPUT_MAP_SET_KEY);
        if (inputMapSet != null) {
            return inputMapSet;
        }
        return SubstanceInputMapUtilities.getSystemInputMapSet();
    }

    private void initFontDefaults(UIDefaults table) {
        FontSet substanceFontSet = SubstanceLookAndFeel.getFontPolicy().getFontSet("Substance", null);
        SubstanceLookAndFeel.initFontDefaults(table, substanceFontSet);
    }

    private static void initFontDefaults(UIDefaults table, FontSet fontSet) {
        FontUIResource controlFont = fontSet.getControlFont();
        FontUIResource menuFont = fontSet.getMenuFont();
        FontUIResource messageFont = fontSet.getMessageFont();
        FontUIResource toolTipFont = fontSet.getSmallFont();
        FontUIResource titleFont = fontSet.getTitleFont();
        FontUIResource windowFont = fontSet.getWindowTitleFont();
        Object[] defaults = new Object[]{"Button.font", controlFont, "CheckBox.font", controlFont, "ColorChooser.font", controlFont, "ComboBox.font", controlFont, "EditorPane.font", controlFont, "FormattedTextField.font", controlFont, "Label.font", controlFont, "List.font", controlFont, "Panel.font", controlFont, "PasswordField.font", controlFont, "ProgressBar.font", controlFont, "RadioButton.font", controlFont, "ScrollPane.font", controlFont, "Spinner.font", controlFont, "TabbedPane.font", controlFont, "Table.font", controlFont, "TableHeader.font", controlFont, "TextArea.font", controlFont, "TextField.font", controlFont, "TextPane.font", controlFont, "ToolBar.font", controlFont, "ToggleButton.font", controlFont, "Tree.font", controlFont, "Viewport.font", controlFont, "InternalFrame.titleFont", windowFont, "DesktopIcon.titleFont", windowFont, "OptionPane.font", messageFont, "OptionPane.messageFont", messageFont, "OptionPane.buttonFont", messageFont, "TitledBorder.font", titleFont, "ToolTip.font", toolTipFont, "CheckBoxMenuItem.font", menuFont, "CheckBoxMenuItem.acceleratorFont", menuFont, "Menu.font", menuFont, "Menu.acceleratorFont", menuFont, "MenuBar.font", menuFont, "MenuItem.font", menuFont, "MenuItem.acceleratorFont", menuFont, "PopupMenu.font", menuFont, "RadioButtonMenuItem.font", menuFont, "RadioButtonMenuItem.acceleratorFont", menuFont};
        table.putDefaults(defaults);
    }

    @Override
    public UIDefaults getDefaults() {
        UIDefaults table = super.getDefaults();
        componentPlugins.processAllDefaultsEntries(table, this.skin);
        return table;
    }

    @Override
    public void initialize() {
        super.initialize();
        ShadowPopupFactory.install();
        SubstanceLookAndFeel.setSkin(this.skin, false);
        String paramTraceFile = SubstanceCoreUtilities.getVmParameter(TRACE_FILE);
        if (paramTraceFile != null) {
            MemoryAnalyzer.commence(1000L, paramTraceFile);
            for (Object plugin : componentPlugins.getAvailablePlugins(true)) {
                MemoryAnalyzer.enqueueUsage("Has plugin '" + plugin.getClass().getName() + "'");
            }
        }
        String heapStatusPanelParam = SubstanceCoreUtilities.getVmParameter(HEAP_STATUS_TRACE_FILE);
        SubstanceTitlePane.setHeapStatusLogfileName(heapStatusPanelParam);
        componentPlugins.initializeAll();
        LafWidgetRepository.getRepository().setLafSupport(new SubstanceWidgetSupport());
        this.focusOwnerChangeListener = new PropertyChangeListener(){

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                Component newFocusOwner;
                if ("focusOwner".equals(evt.getPropertyName()) && (newFocusOwner = (Component)evt.getNewValue()) != null) {
                    JRootPane rootPane = SwingUtilities.getRootPane(newFocusOwner);
                    if (rootPane == null) {
                        return;
                    }
                    JButton defaultButton = rootPane.getDefaultButton();
                    if (defaultButton == null) {
                        return;
                    }
                    defaultButton.repaint();
                }
                if ("managingFocus".equals(evt.getPropertyName()) && Boolean.FALSE.equals(evt.getNewValue())) {
                    SubstanceLookAndFeel.this.currentKeyboardFocusManager.removePropertyChangeListener(SubstanceLookAndFeel.this.focusOwnerChangeListener);
                    SubstanceLookAndFeel.this.currentKeyboardFocusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
                    SubstanceLookAndFeel.this.currentKeyboardFocusManager.addPropertyChangeListener(SubstanceLookAndFeel.this.focusOwnerChangeListener);
                }
            }
        };
        this.currentKeyboardFocusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        this.currentKeyboardFocusManager.addPropertyChangeListener(this.focusOwnerChangeListener);
        if (!LookUtils.IS_OS_WINDOWS || System.getProperty("java.version").compareTo("1.6.0_10") < 0) {
            UIManager.put(WINDOW_ROUNDED_CORNERS, Boolean.FALSE);
        }
    }

    @Override
    public void uninitialize() {
        super.uninitialize();
        currentSkin = null;
        ShadowPopupFactory.uninstall();
        SubstanceCoreUtilities.stopThreads();
        if (this.skin.getWatermark() != null) {
            this.skin.getWatermark().dispose();
        }
        componentPlugins.uninitializeAll();
        LafWidgetRepository.getRepository().unsetLafSupport();
        LazyResettableHashMap.reset();
        this.currentKeyboardFocusManager.removePropertyChangeListener(this.focusOwnerChangeListener);
        this.focusOwnerChangeListener = null;
        this.currentKeyboardFocusManager = null;
    }

    public static void registerSkinChangeListener(SkinChangeListener skinChangeListener) {
        skinChangeListeners.add(skinChangeListener);
    }

    public static void unregisterSkinChangeListener(SkinChangeListener skinChangeListener) {
        skinChangeListeners.remove(skinChangeListener);
    }

    public static void registerTabCloseChangeListener(BaseTabCloseListener tabCloseListener) {
        TabCloseListenerManager.getInstance().registerListener(tabCloseListener);
    }

    public static void registerTabCloseChangeListener(JTabbedPane tabbedPane, BaseTabCloseListener tabCloseListener) {
        TabCloseListenerManager.getInstance().registerListener(tabbedPane, tabCloseListener);
    }

    public static void unregisterTabCloseChangeListener(BaseTabCloseListener tabCloseListener) {
        TabCloseListenerManager.getInstance().unregisterListener(tabCloseListener);
    }

    public static void unregisterTabCloseChangeListener(JTabbedPane tabbedPane, BaseTabCloseListener tabCloseListener) {
        TabCloseListenerManager.getInstance().unregisterListener(tabbedPane, tabCloseListener);
    }

    public static Set<BaseTabCloseListener> getAllTabCloseListeners() {
        return TabCloseListenerManager.getInstance().getListeners();
    }

    public static Set<BaseTabCloseListener> getAllTabCloseListeners(JTabbedPane tabbedPane) {
        return TabCloseListenerManager.getInstance().getListeners(tabbedPane);
    }

    public static void registerLocaleChangeListener(LocaleChangeListener localeListener) {
        localeChangeListeners.add(localeListener);
    }

    public static void unregisterLocaleChangeListener(LocaleChangeListener localeListener) {
        localeChangeListeners.remove(localeListener);
    }

    public static Set<LocaleChangeListener> getLocaleListeners() {
        return Collections.unmodifiableSet(localeChangeListeners);
    }

    public static void setWidgetVisible(JRootPane rootPane, boolean visible, SubstanceConstants.SubstanceWidgetType ... substanceWidgets) {
        SubstanceWidgetManager.getInstance().register(rootPane, visible, substanceWidgets);
        if (rootPane != null) {
            SwingUtilities.updateComponentTreeUI(rootPane);
        } else {
            for (Window window : Window.getWindows()) {
                JRootPane root = SwingUtilities.getRootPane(window);
                SwingUtilities.updateComponentTreeUI(root);
            }
        }
    }

    public static boolean isToUseConstantThemesOnDialogs() {
        return toUseConstantThemesOnDialogs;
    }

    public static void setToUseConstantThemesOnDialogs(boolean toUseConstantThemesOnDialogs) {
        SubstanceLookAndFeel.toUseConstantThemesOnDialogs = toUseConstantThemesOnDialogs;
        SwingUtilities.invokeLater(new Runnable(){

            @Override
            public void run() {
                for (Window window : Window.getWindows()) {
                    SwingUtilities.updateComponentTreeUI(window);
                }
            }
        });
    }

    private static boolean setSkin(final SubstanceSkin newSkin, final boolean toUpdateWindows) {
        if (!SwingUtilities.isEventDispatchThread()) {
            final boolean[] returnValue = new boolean[1];
            try {
                SwingUtilities.invokeAndWait(new Runnable(){

                    @Override
                    public void run() {
                        returnValue[0] = SubstanceLookAndFeel.setSkin(newSkin, toUpdateWindows);
                    }
                });
            }
            catch (InvocationTargetException ite) {
                if (ite.getCause() instanceof RuntimeException) {
                    throw (RuntimeException)ite.getCause();
                }
                if (ite.getCause() instanceof Error) {
                    throw (Error)ite.getCause();
                }
            }
            catch (InterruptedException ignore) {
                // empty catch block
            }
            return returnValue[0];
        }
        if (!newSkin.isValid()) {
            return false;
        }
        boolean isSubstance = UIManager.getLookAndFeel() instanceof SubstanceLookAndFeel;
        if (!isSubstance) {
            class SkinDerivedLookAndFeel
            extends SubstanceLookAndFeel {
                public SkinDerivedLookAndFeel(SubstanceSkin newSkin) {
                    super(newSkin);
                }
            }
            SkinDerivedLookAndFeel derived = new SkinDerivedLookAndFeel(newSkin);
            try {
                UIManager.setLookAndFeel(derived);
            }
            catch (UnsupportedLookAndFeelException ulafe) {
                return false;
            }
            if (!(UIManager.getLookAndFeel() instanceof SubstanceLookAndFeel)) {
                return false;
            }
            for (Window window : Window.getWindows()) {
                SwingUtilities.updateComponentTreeUI(window);
            }
            return true;
        }
        try {
            if (!newSkin.isValid()) {
                return false;
            }
            if (currentSkin != null && currentSkin.getWatermark() != null) {
                currentSkin.getWatermark().dispose();
            }
            if (newSkin.getWatermark() != null && !newSkin.getWatermark().updateWatermarkImage(newSkin)) {
                return false;
            }
            UIDefaults lafDefaults = UIManager.getLookAndFeelDefaults();
            if (lafDefaults != null) {
                SubstanceLookAndFeel.initFontDefaults(lafDefaults, SubstanceLookAndFeel.getFontPolicy().getFontSet("Substance", null));
                newSkin.addCustomEntriesToTable(lafDefaults);
                componentPlugins.processAllDefaultsEntries(lafDefaults, newSkin);
            }
            for (ResourceBundle bundle : new ResourceBundle[]{ResourceBundle.getBundle("com.sun.swing.internal.plaf.metal.resources.metal"), SubstanceLookAndFeel.getLabelBundle()}) {
                Enumeration<String> keyEn = bundle.getKeys();
                while (keyEn.hasMoreElements()) {
                    String key = keyEn.nextElement();
                    if (!key.contains("FileChooser")) continue;
                    String value = bundle.getString(key);
                    UIManager.put(key, value);
                }
            }
            if (isSubstance) {
                LazyResettableHashMap.reset();
            }
            currentSkin = newSkin;
            if (toUpdateWindows) {
                for (Window window : Window.getWindows()) {
                    SwingUtilities.updateComponentTreeUI(window);
                }
            }
            for (SkinChangeListener skinChangeListener : skinChangeListeners) {
                skinChangeListener.skinChanged();
            }
            return true;
        }
        catch (NoClassDefFoundError ncdfe) {
            return false;
        }
        catch (Exception e2) {
            return false;
        }
    }

    public static boolean setSkin(SubstanceSkin newSkin) {
        return SubstanceLookAndFeel.setSkin(newSkin, true);
    }

    public static boolean setSkin(String skinClassName) {
        try {
            Class<?> skinClass = Class.forName(skinClassName);
            if (skinClass == null) {
                return false;
            }
            Object obj = skinClass.newInstance();
            if (obj == null) {
                return false;
            }
            if (!(obj instanceof SubstanceSkin)) {
                return false;
            }
            return SubstanceLookAndFeel.setSkin((SubstanceSkin)obj);
        }
        catch (Exception exc) {
            exc.printStackTrace();
            return false;
        }
    }

    public static Map<String, SkinInfo> getAllSkins() {
        SubstanceLookAndFeel.initPluginsIfNecessary();
        TreeMap<String, SkinInfo> result = new TreeMap<String, SkinInfo>();
        for (Object skinPlugin : skinPlugins.getAvailablePlugins(true)) {
            for (SkinInfo skinInfo : ((SubstanceSkinPlugin)skinPlugin).getSkins()) {
                result.put(skinInfo.getDisplayName(), skinInfo);
            }
        }
        return result;
    }

    @Override
    public boolean getSupportsWindowDecorations() {
        return true;
    }

    public static void setLabelBundleClassLoader(ClassLoader labelBundleClassLoader) {
        SubstanceLookAndFeel.labelBundleClassLoader = labelBundleClassLoader;
        LafWidgetRepository.setLabelBundleClassLoader(labelBundleClassLoader);
    }

    public static JComponent getTitlePaneComponent(Window window) {
        JRootPane rootPane = null;
        if (window instanceof JFrame) {
            JFrame f2 = (JFrame)window;
            rootPane = f2.getRootPane();
        }
        if (window instanceof JDialog) {
            JDialog d2 = (JDialog)window;
            rootPane = d2.getRootPane();
        }
        if (rootPane != null) {
            SubstanceRootPaneUI ui = (SubstanceRootPaneUI)rootPane.getUI();
            return ui.getTitlePane();
        }
        return null;
    }

    public static void setDecorationType(JComponent comp, DecorationAreaType type) {
        DecorationPainterUtils.setDecorationType(comp, type);
    }

    public static DecorationAreaType getDecorationType(Component comp) {
        return DecorationPainterUtils.getDecorationType(comp);
    }

    public static DecorationAreaType getImmediateDecorationType(Component comp) {
        return DecorationPainterUtils.getImmediateDecorationType(comp);
    }

    public static boolean isCurrentLookAndFeel() {
        return UIManager.getLookAndFeel() instanceof SubstanceLookAndFeel && currentSkin != null;
    }

    @Override
    public Icon getDisabledIcon(JComponent component, Icon icon) {
        if (icon == null) {
            return null;
        }
        SubstanceColorScheme colorScheme = SubstanceColorSchemeUtilities.getColorScheme(component, ComponentState.DISABLED_UNSELECTED);
        BufferedImage result = SubstanceImageCreator.getColorSchemeImage(component, icon, colorScheme, 0.5f);
        float alpha = SubstanceColorSchemeUtilities.getAlpha(component, ComponentState.DISABLED_UNSELECTED);
        if (alpha < 1.0f) {
            BufferedImage intermediate = SubstanceCoreUtilities.getBlankImage(result.getWidth(), result.getHeight());
            Graphics2D g2d = intermediate.createGraphics();
            g2d.setComposite(AlphaComposite.SrcOver.derive(alpha));
            g2d.drawImage((Image)result, 0, 0, null);
            g2d.dispose();
            result = intermediate;
        }
        return new IconUIResource(new ImageIcon(result));
    }

    static {
        skinChangeListeners = new HashSet<SkinChangeListener>();
        localeChangeListeners = new HashSet<LocaleChangeListener>();
        toUseConstantThemesOnDialogs = true;
        TREE_SMART_SCROLL_ANIMATION_KIND = new AnimationFacet("substancelaf.treeSmartScrollAnimationKind", false);
        LABEL_BUNDLE = null;
        currentSkin = null;
    }
}

