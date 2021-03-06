/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.Locale;
import javax.swing.Icon;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.DimensionUIResource;
import javax.swing.plaf.IconUIResource;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.basic.BasicBorders;
import org.pushingpixels.substance.api.ColorSchemeAssociationKind;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.inputmaps.InputMapSet;
import org.pushingpixels.substance.api.inputmaps.SubstanceInputMapUtilities;
import org.pushingpixels.substance.api.renderers.SubstanceDefaultListCellRenderer;
import org.pushingpixels.substance.internal.utils.SubstanceColorUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceImageCreator;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;
import org.pushingpixels.substance.internal.utils.border.SubstanceBorder;
import org.pushingpixels.substance.internal.utils.border.SubstanceEtchedBorder;
import org.pushingpixels.substance.internal.utils.border.SubstancePaneBorder;
import org.pushingpixels.substance.internal.utils.border.SubstanceTextComponentBorder;
import org.pushingpixels.substance.internal.utils.border.SubstanceToolBarBorder;
import org.pushingpixels.substance.internal.utils.icon.CheckBoxMenuItemIcon;
import org.pushingpixels.substance.internal.utils.icon.MenuArrowIcon;
import org.pushingpixels.substance.internal.utils.icon.RadioButtonMenuItemIcon;
import org.pushingpixels.substance.internal.utils.icon.SubstanceIconFactory;
import org.pushingpixels.substance.internal.utils.scroll.SubstanceScrollPaneBorder;

public class SkinUtilities {
    public static void addCustomEntriesToTable(UIDefaults table, SubstanceSkin skin) {
        ColorUIResource disabledForegroundColor;
        UIDefaults.LazyValue menuArrowIcon = new UIDefaults.LazyValue(){

            @Override
            public Object createValue(UIDefaults table) {
                return new MenuArrowIcon(null);
            }
        };
        UIDefaults.ActiveValue listCellRendererActiveValue = new UIDefaults.ActiveValue(){

            @Override
            public Object createValue(UIDefaults table) {
                return new SubstanceDefaultListCellRenderer.SubstanceUIResource();
            }
        };
        SubstanceColorScheme mainActiveScheme = skin.getActiveColorScheme(DecorationAreaType.NONE);
        SubstanceColorScheme mainEnabledScheme = skin.getEnabledColorScheme(DecorationAreaType.NONE);
        SubstanceColorScheme mainDisabledScheme = skin.getDisabledColorScheme(DecorationAreaType.NONE);
        ColorUIResource control = new ColorUIResource(mainActiveScheme.getLightColor());
        ColorUIResource foregroundColor = SubstanceColorUtilities.getForegroundColor(mainEnabledScheme);
        ColorUIResource backgroundActiveColor = new ColorUIResource(mainActiveScheme.getBackgroundFillColor());
        ColorUIResource backgroundDefaultColor = new ColorUIResource(mainEnabledScheme.getBackgroundFillColor());
        ColorUIResource textBackgroundColor = new ColorUIResource(mainActiveScheme.getTextBackgroundFillColor());
        ColorUIResource disabledTextComponentForegroundColor = disabledForegroundColor = SubstanceColorUtilities.getForegroundColor(mainDisabledScheme);
        float alpha = skin.getAlpha(null, ComponentState.DISABLED_UNSELECTED);
        if (alpha < 1.0f) {
            ColorUIResource defaultTextBackgroundColor = SubstanceColorUtilities.getDefaultBackgroundColor(true, skin, false);
            disabledTextComponentForegroundColor = new ColorUIResource(SubstanceColorUtilities.getInterpolatedColor(disabledTextComponentForegroundColor, defaultTextBackgroundColor, alpha));
        }
        ColorUIResource lineColor = new ColorUIResource(mainActiveScheme.getLineColor());
        ColorUIResource lineColorDefault = new ColorUIResource(mainEnabledScheme.getLineColor());
        int lcb = SubstanceColorUtilities.getColorBrightness(lineColor.getRGB());
        ColorUIResource lineBwColor = new ColorUIResource(new Color(lcb, lcb, lcb));
        SubstanceColorScheme textHighlightColorScheme = skin.getColorScheme((Component)null, ColorSchemeAssociationKind.TEXT_HIGHLIGHT, ComponentState.SELECTED);
        if (textHighlightColorScheme == null) {
            textHighlightColorScheme = skin.getColorScheme(null, ComponentState.ROLLOVER_SELECTED);
        }
        ColorUIResource selectionTextBackgroundColor = new ColorUIResource(textHighlightColorScheme.getSelectionBackgroundColor());
        ColorUIResource selectionTextForegroundColor = new ColorUIResource(textHighlightColorScheme.getSelectionForegroundColor());
        ColorUIResource selectionCellForegroundColor = new ColorUIResource(textHighlightColorScheme.getForegroundColor());
        ColorUIResource selectionCellBackgroundColor = new ColorUIResource(textHighlightColorScheme.getBackgroundFillColor());
        UIDefaults.LazyValue regularMarginBorder = new UIDefaults.LazyValue(){

            @Override
            public Object createValue(UIDefaults table) {
                return new BorderUIResource.CompoundBorderUIResource(new SubstanceBorder(), new BasicBorders.MarginBorder());
            }
        };
        UIDefaults.LazyValue textBorder = new UIDefaults.LazyValue(){

            @Override
            public Object createValue(UIDefaults table) {
                return new BorderUIResource.CompoundBorderUIResource(new SubstanceTextComponentBorder(SubstanceSizeUtils.getTextBorderInsets(SubstanceSizeUtils.getControlFontSize())), new BasicBorders.MarginBorder());
            }
        };
        UIDefaults.LazyValue textMarginBorder = new UIDefaults.LazyValue(){

            @Override
            public Object createValue(UIDefaults table) {
                return new BasicBorders.MarginBorder();
            }
        };
        UIDefaults.LazyValue tooltipBorder = new UIDefaults.LazyValue(){

            @Override
            public Object createValue(UIDefaults table) {
                return new SubstanceBorder(SubstanceSizeUtils.getToolTipBorderInsets(SubstanceSizeUtils.getControlFontSize()));
            }
        };
        UIDefaults.LazyValue comboBorder = new UIDefaults.LazyValue(){

            @Override
            public Object createValue(UIDefaults table) {
                return new SubstanceBorder(SubstanceSizeUtils.getComboBorderInsets(SubstanceSizeUtils.getControlFontSize()));
            }
        };
        UIDefaults.LazyValue spinnerBorder = new UIDefaults.LazyValue(){

            @Override
            public Object createValue(UIDefaults table) {
                return new BorderUIResource.CompoundBorderUIResource(new SubstanceTextComponentBorder(SubstanceSizeUtils.getSpinnerBorderInsets(SubstanceSizeUtils.getControlFontSize())), new BasicBorders.MarginBorder());
            }
        };
        final SubstanceColorScheme titlePaneScheme = skin.getBackgroundColorScheme(DecorationAreaType.PRIMARY_TITLE_PANE);
        UIDefaults.LazyValue menuItemInsets = new UIDefaults.LazyValue(){

            @Override
            public Object createValue(UIDefaults table) {
                int menuItemMargin = SubstanceSizeUtils.getMenuItemMargin(SubstanceSizeUtils.getComponentFontSize(null));
                return new InsetsUIResource(menuItemMargin, menuItemMargin, menuItemMargin, menuItemMargin);
            }
        };
        UIDefaults.LazyValue emptyIcon = new UIDefaults.LazyValue(){

            @Override
            public Object createValue(UIDefaults table) {
                return new IconUIResource(new Icon(){

                    @Override
                    public int getIconHeight() {
                        return 16;
                    }

                    @Override
                    public int getIconWidth() {
                        return 2;
                    }

                    @Override
                    public void paintIcon(Component c2, Graphics g2, int x2, int y2) {
                    }
                });
            }
        };
        Object[] defaults = new Object[]{"activeCaption", new ColorUIResource(skin.getBackgroundColorScheme(DecorationAreaType.PRIMARY_TITLE_PANE).getBackgroundFillColor()), "activeCaptionBorder", new ColorUIResource(skin.getColorScheme(DecorationAreaType.PRIMARY_TITLE_PANE, ColorSchemeAssociationKind.BORDER, ComponentState.ENABLED).getBackgroundFillColor()), "activeCaptionText", SubstanceColorUtilities.getForegroundColor(skin.getBackgroundColorScheme(DecorationAreaType.PRIMARY_TITLE_PANE)), "control", new ColorUIResource(mainEnabledScheme.getBackgroundFillColor()), "controlDkShadow", new ColorUIResource(mainEnabledScheme.getUltraDarkColor()), "controlHighlight", new ColorUIResource(mainEnabledScheme.getLightColor()), "controlLtHighlight", new ColorUIResource(mainEnabledScheme.getExtraLightColor()), "controlShadow", new ColorUIResource(mainEnabledScheme.getDarkColor()), "controlText", new ColorUIResource(mainEnabledScheme.getForegroundColor()), "desktop", SubstanceColorUtilities.getDefaultBackgroundColor(false, skin, false), "inactiveCaption", new ColorUIResource(skin.getBackgroundColorScheme(UIManager.getBoolean("windowAutoDeactivate") ? DecorationAreaType.PRIMARY_TITLE_PANE : DecorationAreaType.PRIMARY_TITLE_PANE_INACTIVE).getBackgroundFillColor()), "inactiveCaptionBorder", new ColorUIResource(skin.getColorScheme(UIManager.getBoolean("windowAutoDeactivate") ? DecorationAreaType.PRIMARY_TITLE_PANE : DecorationAreaType.PRIMARY_TITLE_PANE_INACTIVE, ColorSchemeAssociationKind.BORDER, ComponentState.ENABLED).getBackgroundFillColor()), "inactiveCaptionText", SubstanceColorUtilities.getForegroundColor(skin.getBackgroundColorScheme(UIManager.getBoolean("windowAutoDeactivate") ? DecorationAreaType.PRIMARY_TITLE_PANE : DecorationAreaType.PRIMARY_TITLE_PANE_INACTIVE)), "info", SubstanceColorUtilities.getDefaultBackgroundColor(false, skin, false), "infoText", foregroundColor, "menu", SubstanceColorUtilities.getDefaultBackgroundColor(false, skin, false), "menuText", foregroundColor, "scrollbar", SubstanceColorUtilities.getDefaultBackgroundColor(false, skin, false), "text", SubstanceColorUtilities.getDefaultBackgroundColor(true, skin, false), "textHighlight", selectionTextBackgroundColor, "textHighlightText", selectionTextForegroundColor, "textInactiveText", disabledTextComponentForegroundColor, "textText", foregroundColor, "window", SubstanceColorUtilities.getDefaultBackgroundColor(false, skin, false), "windowBorder", control, "windowText", foregroundColor, "Button.defaultButtonFollowsFocus", Boolean.FALSE, "Button.disabledText", disabledForegroundColor, "Button.foreground", foregroundColor, "Button.margin", new InsetsUIResource(0, 0, 0, 0), "CheckBox.background", SubstanceColorUtilities.getDefaultBackgroundColor(false, skin, false), "CheckBox.border", new BorderUIResource.CompoundBorderUIResource(SubstanceSizeUtils.getCheckBoxBorder(SubstanceSizeUtils.getControlFontSize(), ComponentOrientation.getOrientation(Locale.getDefault()).isLeftToRight()), new BasicBorders.MarginBorder()), "CheckBox.disabledText", disabledForegroundColor, "CheckBox.foreground", foregroundColor, "CheckBoxMenuItem.acceleratorForeground", foregroundColor, "CheckBoxMenuItem.acceleratorSelectionForeground", foregroundColor, "CheckBoxMenuItem.background", SubstanceColorUtilities.getDefaultBackgroundColor(false, skin, false), "CheckBoxMenuItem.borderPainted", Boolean.FALSE, "CheckBoxMenuItem.checkIcon", new CheckBoxMenuItemIcon(null, 1 + SubstanceSizeUtils.getMenuCheckMarkSize(SubstanceSizeUtils.getControlFontSize())), "CheckBoxMenuItem.disabledForeground", disabledForegroundColor, "CheckBoxMenuItem.foreground", foregroundColor, "CheckBoxMenuItem.margin", menuItemInsets, "CheckBoxMenuItem.selectionForeground", selectionCellForegroundColor, "ColorChooser.background", SubstanceColorUtilities.getDefaultBackgroundColor(false, skin, false), "ColorChooser.foreground", foregroundColor, "ComboBox.background", SubstanceColorUtilities.getDefaultBackgroundColor(false, skin, false), "ComboBox.border", comboBorder, "ComboBox.disabledBackground", textBackgroundColor, "ComboBox.disabledForeground", disabledForegroundColor, "ComboBox.foreground", foregroundColor, "ComboBox.selectionBackground", selectionCellBackgroundColor, "ComboBox.selectionForeground", selectionCellForegroundColor, "DesktopIcon.border", regularMarginBorder, "DesktopIcon.width", 140, "Desktop.background", new ColorUIResource(new Color(0, true)), "Desktop.foreground", foregroundColor, "Dialog.background", SubstanceColorUtilities.getDefaultBackgroundColor(false, skin, false), "EditorPane.background", SubstanceColorUtilities.getDefaultBackgroundColor(true, skin, false), "EditorPane.border", textMarginBorder, "EditorPane.foreground", foregroundColor, "EditorPane.caretForeground", foregroundColor, "EditorPane.disabledBackground", SubstanceColorUtilities.getDefaultBackgroundColor(true, skin, true), "EditorPane.inactiveBackground", SubstanceColorUtilities.getDefaultBackgroundColor(true, skin, true), "EditorPane.inactiveForeground", disabledTextComponentForegroundColor, "EditorPane.selectionBackground", selectionTextBackgroundColor, "EditorPane.selectionForeground", selectionTextForegroundColor, "FileChooser.upFolderIcon", new UIDefaults.LazyValue(){

            @Override
            public Object createValue(UIDefaults table) {
                return SubstanceCoreUtilities.getIcon("resource/go-up.png");
            }
        }, "FileChooser.newFolderIcon", new UIDefaults.LazyValue(){

            @Override
            public Object createValue(UIDefaults table) {
                return SubstanceCoreUtilities.getIcon("resource/folder-new.png");
            }
        }, "FileChooser.homeFolderIcon", new UIDefaults.LazyValue(){

            @Override
            public Object createValue(UIDefaults table) {
                return SubstanceCoreUtilities.getIcon("resource/user-home.png");
            }
        }, "FileChooser.listViewIcon", new UIDefaults.LazyValue(){

            @Override
            public Object createValue(UIDefaults table) {
                return SubstanceCoreUtilities.getIcon("resource/application_view_list.png");
            }
        }, "FileChooser.detailsViewIcon", new UIDefaults.LazyValue(){

            @Override
            public Object createValue(UIDefaults table) {
                return SubstanceCoreUtilities.getIcon("resource/application_view_detail.png");
            }
        }, "FileChooser.usesSingleFilePane", Boolean.TRUE, "FileView.computerIcon", new UIDefaults.LazyValue(){

            @Override
            public Object createValue(UIDefaults table) {
                return SubstanceCoreUtilities.getIcon("resource/computer.png");
            }
        }, "FileView.directoryIcon", new UIDefaults.LazyValue(){

            @Override
            public Object createValue(UIDefaults table) {
                return SubstanceCoreUtilities.getIcon("resource/folder.png");
            }
        }, "FileView.fileIcon", new UIDefaults.LazyValue(){

            @Override
            public Object createValue(UIDefaults table) {
                return SubstanceCoreUtilities.getIcon("resource/text-x-generic.png");
            }
        }, "FileView.floppyDriveIcon", new UIDefaults.LazyValue(){

            @Override
            public Object createValue(UIDefaults table) {
                return SubstanceCoreUtilities.getIcon("resource/media-floppy.png");
            }
        }, "FileView.hardDriveIcon", new UIDefaults.LazyValue(){

            @Override
            public Object createValue(UIDefaults table) {
                return SubstanceCoreUtilities.getIcon("resource/drive-harddisk.png");
            }
        }, "FormattedTextField.background", SubstanceColorUtilities.getDefaultBackgroundColor(true, skin, false), "FormattedTextField.border", textBorder, "FormattedTextField.caretForeground", foregroundColor, "FormattedTextField.disabledBackground", SubstanceColorUtilities.getDefaultBackgroundColor(true, skin, true), "FormattedTextField.foreground", foregroundColor, "FormattedTextField.inactiveBackground", SubstanceColorUtilities.getDefaultBackgroundColor(true, skin, true), "FormattedTextField.inactiveForeground", disabledTextComponentForegroundColor, "FormattedTextField.selectionBackground", selectionTextBackgroundColor, "FormattedTextField.selectionForeground", selectionTextForegroundColor, "InternalFrame.activeTitleBackground", new ColorUIResource(skin.getBackgroundColorScheme(DecorationAreaType.PRIMARY_TITLE_PANE).getBackgroundFillColor()), "InternalFrame.activeTitleForeground", SubstanceColorUtilities.getForegroundColor(skin.getBackgroundColorScheme(DecorationAreaType.PRIMARY_TITLE_PANE)), "InternalFrame.inactiveTitleBackground", new ColorUIResource(skin.getBackgroundColorScheme(DecorationAreaType.PRIMARY_TITLE_PANE_INACTIVE).getBackgroundFillColor()), "InternalFrame.inactiveTitleForeground", SubstanceColorUtilities.getForegroundColor(skin.getBackgroundColorScheme(DecorationAreaType.PRIMARY_TITLE_PANE_INACTIVE)), "InternalFrame.border", new BorderUIResource(new SubstancePaneBorder()), "InternalFrame.closeIcon", new UIDefaults.LazyValue(){

            @Override
            public Object createValue(UIDefaults table) {
                return SubstanceImageCreator.getCloseIcon(titlePaneScheme, titlePaneScheme);
            }
        }, "InternalFrame.iconifyIcon", new UIDefaults.LazyValue(){

            @Override
            public Object createValue(UIDefaults table) {
                return SubstanceImageCreator.getMinimizeIcon(titlePaneScheme, titlePaneScheme);
            }
        }, "InternalFrame.maximizeIcon", new UIDefaults.LazyValue(){

            @Override
            public Object createValue(UIDefaults table) {
                return SubstanceImageCreator.getMaximizeIcon(titlePaneScheme, titlePaneScheme);
            }
        }, "InternalFrame.minimizeIcon", new UIDefaults.LazyValue(){

            @Override
            public Object createValue(UIDefaults table) {
                return SubstanceImageCreator.getRestoreIcon(titlePaneScheme, titlePaneScheme);
            }
        }, "InternalFrame.paletteCloseIcon", new UIDefaults.LazyValue(){

            @Override
            public Object createValue(UIDefaults table) {
                return SubstanceImageCreator.getCloseIcon(titlePaneScheme, titlePaneScheme);
            }
        }, "Label.background", SubstanceColorUtilities.getDefaultBackgroundColor(false, skin, false), "Label.foreground", foregroundColor, "Label.disabledText", disabledForegroundColor, "Label.disabledForeground", disabledForegroundColor, "List.background", SubstanceColorUtilities.getDefaultBackgroundColor(false, skin, false), "List.cellRenderer", listCellRendererActiveValue, "List.focusCellHighlightBorder", new SubstanceBorder(new Insets(1, 1, 1, 1)), "List.focusSelectedCellHighlightBorder", new BorderUIResource.EmptyBorderUIResource(1, 1, 1, 1), "List.foreground", foregroundColor, "List.selectionBackground", selectionCellBackgroundColor, "List.selectionForeground", selectionCellForegroundColor, "Menu.arrowIcon", menuArrowIcon, "Menu.background", SubstanceColorUtilities.getDefaultBackgroundColor(false, skin, false), "Menu.borderPainted", Boolean.FALSE, "Menu.checkIcon", null, "Menu.disabledForeground", disabledForegroundColor, "Menu.foreground", foregroundColor, "Menu.margin", menuItemInsets, "Menu.selectionForeground", selectionCellForegroundColor, "MenuBar.background", skin.isRegisteredAsDecorationArea(DecorationAreaType.HEADER) ? new ColorUIResource(skin.getActiveColorScheme(DecorationAreaType.HEADER).getMidColor()) : SubstanceColorUtilities.getDefaultBackgroundColor(false, skin, false), "MenuBar.foreground", new ColorUIResource(skin.getActiveColorScheme(DecorationAreaType.HEADER).getForegroundColor()), "MenuBar.border", null, "MenuItem.acceleratorForeground", foregroundColor, "MenuItem.acceleratorSelectionForeground", foregroundColor, "MenuItem.background", SubstanceColorUtilities.getDefaultBackgroundColor(false, skin, false), "MenuItem.borderPainted", Boolean.FALSE, "MenuItem.checkIcon", null, "MenuItem.disabledForeground", disabledForegroundColor, "MenuItem.foreground", foregroundColor, "MenuItem.margin", menuItemInsets, "MenuItem.selectionForeground", selectionCellForegroundColor, "OptionPane.background", SubstanceColorUtilities.getDefaultBackgroundColor(false, skin, false), "OptionPane.errorIcon", new UIDefaults.LazyValue(){

            @Override
            public Object createValue(UIDefaults table) {
                return SubstanceCoreUtilities.getIcon("resource/32/dialog-error.png");
            }
        }, "OptionPane.foreground", foregroundColor, "OptionPane.informationIcon", new UIDefaults.LazyValue(){

            @Override
            public Object createValue(UIDefaults table) {
                return SubstanceCoreUtilities.getIcon("resource/32/dialog-information.png");
            }
        }, "OptionPane.messageForeground", foregroundColor, "OptionPane.questionIcon", new UIDefaults.LazyValue(){

            @Override
            public Object createValue(UIDefaults table) {
                return SubstanceCoreUtilities.getIcon("resource/32/help-browser.png");
            }
        }, "OptionPane.warningIcon", new UIDefaults.LazyValue(){

            @Override
            public Object createValue(UIDefaults table) {
                return SubstanceCoreUtilities.getIcon("resource/32/dialog-warning.png");
            }
        }, "Panel.background", SubstanceColorUtilities.getDefaultBackgroundColor(false, skin, false), "Panel.foreground", foregroundColor, "PasswordField.background", SubstanceColorUtilities.getDefaultBackgroundColor(true, skin, false), "PasswordField.border", textBorder, "PasswordField.caretForeground", foregroundColor, "PasswordField.disabledBackground", SubstanceColorUtilities.getDefaultBackgroundColor(true, skin, true), "PasswordField.foreground", foregroundColor, "PasswordField.inactiveBackground", SubstanceColorUtilities.getDefaultBackgroundColor(true, skin, true), "PasswordField.inactiveForeground", disabledTextComponentForegroundColor, "PasswordField.selectionBackground", selectionTextBackgroundColor, "PasswordField.selectionForeground", selectionTextForegroundColor, "PopupMenu.background", SubstanceColorUtilities.getDefaultBackgroundColor(false, skin, false), "PopupMenu.border", regularMarginBorder, "ProgressBar.border", new BorderUIResource(new SubstanceBorder()), "ProgressBar.cycleTime", 1000, "ProgressBar.repaintInterval", 50, "ProgressBar.horizontalSize", new DimensionUIResource(146, SubstanceSizeUtils.getControlFontSize()), "ProgressBar.verticalSize", new DimensionUIResource(SubstanceSizeUtils.getControlFontSize(), 146), "ProgressBar.selectionBackground", foregroundColor, "ProgressBar.selectionForeground", foregroundColor, "RadioButton.background", SubstanceColorUtilities.getDefaultBackgroundColor(false, skin, false), "RadioButton.border", new BorderUIResource.CompoundBorderUIResource(SubstanceSizeUtils.getRadioButtonBorder(SubstanceSizeUtils.getControlFontSize(), ComponentOrientation.getOrientation(Locale.getDefault()).isLeftToRight()), new BasicBorders.MarginBorder()), "RadioButton.foreground", foregroundColor, "RadioButton.disabledText", disabledForegroundColor, "RadioButtonMenuItem.acceleratorForeground", foregroundColor, "RadioButtonMenuItem.acceleratorSelectionForeground", foregroundColor, "RadioButtonMenuItem.background", SubstanceColorUtilities.getDefaultBackgroundColor(false, skin, false), "RadioButtonMenuItem.borderPainted", Boolean.FALSE, "RadioButtonMenuItem.checkIcon", new RadioButtonMenuItemIcon(null, SubstanceSizeUtils.getMenuCheckMarkSize(SubstanceSizeUtils.getControlFontSize())), "RadioButtonMenuItem.disabledForeground", disabledForegroundColor, "RadioButtonMenuItem.foreground", foregroundColor, "RadioButtonMenuItem.margin", menuItemInsets, "RadioButtonMenuItem.selectionForeground", selectionCellForegroundColor, "RootPane.background", SubstanceColorUtilities.getDefaultBackgroundColor(false, skin, false), "RootPane.border", new SubstancePaneBorder(), "ScrollBar.background", SubstanceColorUtilities.getDefaultBackgroundColor(false, skin, false), "ScrollBar.width", SubstanceSizeUtils.getScrollBarWidth(SubstanceSizeUtils.getControlFontSize()), "ScrollBar.minimumThumbSize", new DimensionUIResource(SubstanceSizeUtils.getScrollBarWidth(SubstanceSizeUtils.getControlFontSize()) - 2, SubstanceSizeUtils.getScrollBarWidth(SubstanceSizeUtils.getControlFontSize()) - 2), "ScrollPane.background", SubstanceColorUtilities.getDefaultBackgroundColor(false, skin, false), "ScrollPane.foreground", foregroundColor, "ScrollPane.border", new SubstanceScrollPaneBorder(), "Separator.background", backgroundDefaultColor, "Separator.foreground", lineBwColor, "Slider.altTrackColor", lineColor, "Slider.background", SubstanceColorUtilities.getDefaultBackgroundColor(false, skin, false), "Slider.darkShadow", lineColor, "Slider.focus", lineColor, "Slider.focusInsets", new InsetsUIResource(2, 2, 0, 2), "Slider.foreground", lineColor, "Slider.highlight", textBackgroundColor, "Slider.shadow", lineColor, "Slider.tickColor", foregroundColor, "Spinner.arrowButtonInsets", SubstanceSizeUtils.getSpinnerArrowButtonInsets(SubstanceSizeUtils.getControlFontSize()), "Spinner.background", SubstanceColorUtilities.getDefaultBackgroundColor(true, skin, false), "Spinner.border", spinnerBorder, "Spinner.disableOnBoundaryValues", Boolean.TRUE, "Spinner.foreground", foregroundColor, "Spinner.editorBorderPainted", Boolean.TRUE, "SplitPane.background", SubstanceColorUtilities.getDefaultBackgroundColor(false, skin, false), "SplitPane.foreground", foregroundColor, "SplitPane.dividerFocusColor", backgroundDefaultColor, "SplitPaneDivider.draggingColor", backgroundActiveColor, "SplitPane.border", new BorderUIResource(new EmptyBorder(0, 0, 0, 0)), "SplitPane.dividerSize", (int)(SubstanceSizeUtils.getArrowIconWidth(SubstanceSizeUtils.getControlFontSize()) + (float)SubstanceSizeUtils.getAdjustedSize(SubstanceSizeUtils.getControlFontSize(), -1, 6, -1, true)), "SplitPaneDivider.border", new BorderUIResource(new EmptyBorder(1, 1, 1, 1)), "TabbedPane.tabAreaBackground", backgroundDefaultColor, "TabbedPane.unselectedBackground", backgroundDefaultColor, "TabbedPane.background", SubstanceColorUtilities.getDefaultBackgroundColor(false, skin, false), "TabbedPane.borderHightlightColor", new ColorUIResource(mainActiveScheme.getMidColor()), "TabbedPane.contentAreaColor", null, "TabbedPane.contentBorderInsets", new InsetsUIResource(4, 4, 4, 4), "TabbedPane.contentOpaque", Boolean.FALSE, "TabbedPane.darkShadow", new ColorUIResource(skin.getColorScheme((Component)null, ColorSchemeAssociationKind.BORDER, ComponentState.SELECTED).getLineColor()), "TabbedPane.focus", foregroundColor, "TabbedPane.foreground", foregroundColor, "TabbedPane.highlight", new ColorUIResource(mainActiveScheme.getLightColor()), "TabbedPane.light", mainEnabledScheme.isDark() ? new ColorUIResource(SubstanceColorUtilities.getAlphaColor(mainEnabledScheme.getUltraDarkColor(), 100)) : new ColorUIResource(mainEnabledScheme.getLightColor()), "TabbedPane.selected", new ColorUIResource(mainActiveScheme.getExtraLightColor()), "TabbedPane.selectedForeground", foregroundColor, "TabbedPane.selectHighlight", new ColorUIResource(mainActiveScheme.getMidColor()), "TabbedPane.shadow", new ColorUIResource(SubstanceColorUtilities.getInterpolatedColor(mainEnabledScheme.getExtraLightColor(), mainEnabledScheme.getLightColor(), 0.5)), "TabbedPane.tabRunOverlay", 0, "Table.background", SubstanceColorUtilities.getDefaultBackgroundColor(false, skin, false), "Table.cellNoFocusBorder", new BorderUIResource.EmptyBorderUIResource(SubstanceSizeUtils.getDefaultBorderInsets(SubstanceSizeUtils.getComponentFontSize(null))), "Table.focusCellBackground", backgroundActiveColor, "Table.focusCellForeground", foregroundColor, "Table.focusCellHighlightBorder", new SubstanceBorder(), "Table.foreground", foregroundColor, "Table.gridColor", lineColorDefault, "Table.scrollPaneBorder", new SubstanceScrollPaneBorder(), "Table.selectionBackground", selectionCellBackgroundColor, "Table.selectionForeground", selectionCellForegroundColor, "TableHeader.cellBorder", null, "TableHeader.foreground", foregroundColor, "TableHeader.background", SubstanceColorUtilities.getDefaultBackgroundColor(false, skin, false), "TextArea.background", SubstanceColorUtilities.getDefaultBackgroundColor(true, skin, false), "TextArea.border", textMarginBorder, "TextArea.caretForeground", foregroundColor, "TextArea.disabledBackground", SubstanceColorUtilities.getDefaultBackgroundColor(true, skin, true), "TextArea.foreground", foregroundColor, "TextArea.inactiveBackground", SubstanceColorUtilities.getDefaultBackgroundColor(true, skin, true), "TextArea.inactiveForeground", disabledTextComponentForegroundColor, "TextArea.selectionBackground", selectionTextBackgroundColor, "TextArea.selectionForeground", selectionTextForegroundColor, "TextField.background", SubstanceColorUtilities.getDefaultBackgroundColor(true, skin, false), "TextField.border", textBorder, "TextField.caretForeground", foregroundColor, "TextField.disabledBackground", SubstanceColorUtilities.getDefaultBackgroundColor(true, skin, true), "TextField.foreground", foregroundColor, "TextField.inactiveBackground", SubstanceColorUtilities.getDefaultBackgroundColor(true, skin, true), "TextField.inactiveForeground", disabledTextComponentForegroundColor, "TextField.selectionBackground", selectionTextBackgroundColor, "TextField.selectionForeground", selectionTextForegroundColor, "TextPane.background", SubstanceColorUtilities.getDefaultBackgroundColor(true, skin, false), "TextPane.border", textMarginBorder, "TextPane.disabledBackground", SubstanceColorUtilities.getDefaultBackgroundColor(true, skin, true), "TextPane.foreground", foregroundColor, "TextPane.caretForeground", foregroundColor, "TextPane.inactiveBackground", SubstanceColorUtilities.getDefaultBackgroundColor(true, skin, true), "TextPane.inactiveForeground", disabledTextComponentForegroundColor, "TextPane.selectionBackground", selectionTextBackgroundColor, "TextPane.selectionForeground", selectionTextForegroundColor, "TitledBorder.titleColor", foregroundColor, "TitledBorder.border", new SubstanceEtchedBorder(), "ToggleButton.foreground", foregroundColor, "ToggleButton.disabledText", disabledForegroundColor, "ToggleButton.margin", new InsetsUIResource(0, 0, 0, 0), "ToolBar.background", SubstanceColorUtilities.getDefaultBackgroundColor(false, skin, false), "ToolBar.border", new BorderUIResource(new SubstanceToolBarBorder()), "ToolBar.isRollover", Boolean.TRUE, "ToolBar.foreground", foregroundColor, "ToolBarSeparator.background", SubstanceColorUtilities.getDefaultBackgroundColor(false, skin, false), "ToolBarSeparator.foreground", lineBwColor, "ToolBar.separatorSize", null, "ToolTip.border", tooltipBorder, "ToolTip.borderInactive", tooltipBorder, "ToolTip.background", SubstanceColorUtilities.getDefaultBackgroundColor(false, skin, false), "ToolTip.backgroundInactive", SubstanceColorUtilities.getDefaultBackgroundColor(false, skin, true), "ToolTip.foreground", foregroundColor, "ToolTip.foregroundInactive", disabledForegroundColor, "Tree.closedIcon", emptyIcon, "Tree.collapsedIcon", new UIDefaults.LazyValue(){

            @Override
            public Object createValue(UIDefaults table) {
                return new IconUIResource(SubstanceIconFactory.getTreeIcon(null, true));
            }
        }, "Tree.expandedIcon", new UIDefaults.LazyValue(){

            @Override
            public Object createValue(UIDefaults table) {
                return new IconUIResource(SubstanceIconFactory.getTreeIcon(null, false));
            }
        }, "Tree.leafIcon", emptyIcon, "Tree.openIcon", emptyIcon, "Tree.background", SubstanceColorUtilities.getDefaultBackgroundColor(false, skin, false), "Tree.selectionBackground", selectionCellBackgroundColor, "Tree.foreground", foregroundColor, "Tree.hash", lineColorDefault, "Tree.rowHeight", 0, "Tree.selectionBorderColor", lineColor, "Tree.selectionForeground", selectionCellForegroundColor, "Tree.textBackground", backgroundDefaultColor, "Tree.textForeground", foregroundColor, "Viewport.background", backgroundDefaultColor, "Viewport.foreground", foregroundColor, "windowAutoDeactivate", skin.isRegisteredAsDecorationArea(DecorationAreaType.PRIMARY_TITLE_PANE_INACTIVE)};
        table.putDefaults(defaults);
        InputMapSet inputMapSet = SubstanceInputMapUtilities.getSystemInputMapSet();
        if (inputMapSet == null) {
            throw new IllegalStateException("Input map set is null!");
        }
        table.put("Button.focusInputMap", inputMapSet.getButtonFocusInputMap().getUiMap());
        table.put("CheckBox.focusInputMap", inputMapSet.getCheckBoxFocusInputMap().getUiMap());
        table.put("ComboBox.ancestorInputMap", inputMapSet.getComboBoxAncestorInputMap().getUiMap());
        table.put("Desktop.ancestorInputMap", inputMapSet.getDesktopAncestorInputMap().getUiMap());
        table.put("EditorPane.focusInputMap", inputMapSet.getEditorPaneFocusInputMap().getUiMap());
        table.put("FileChooser.ancestorInputMap", inputMapSet.getFileChooserAncestorInputMap().getUiMap());
        table.put("FormattedTextField.focusInputMap", inputMapSet.getFormattedTextFieldFocusInputMap().getUiMap());
        table.put("List.focusInputMap", inputMapSet.getListFocusInputMap().getUiMap());
        table.put("PasswordField.focusInputMap", inputMapSet.getPasswordFieldFocusInputMap().getUiMap());
        table.put("RadioButton.focusInputMap", inputMapSet.getRadioButtonFocusInputMap().getUiMap());
        table.put("RootPane.ancestorInputMap", inputMapSet.getRootPaneAncestorInputMap().getUiMap());
        table.put("ScrollBar.ancestorInputMap", inputMapSet.getScrollBarAncestorInputMap().getUiMap());
        table.put("ScrollPane.ancestorInputMap", inputMapSet.getScrollPaneAncestorInputMap().getUiMap());
        table.put("Slider.focusInputMap", inputMapSet.getSliderFocusInputMap().getUiMap());
        table.put("Spinner.ancestorInputMap", inputMapSet.getSpinnerAncestorInputMap().getUiMap());
        table.put("SplitPane.ancestorInputMap", inputMapSet.getSplitPaneAncestorInputMap().getUiMap());
        table.put("TabbedPane.ancestorInputMap", inputMapSet.getTabbedPaneAncestorInputMap().getUiMap());
        table.put("TabbedPane.focusInputMap", inputMapSet.getTabbedPaneFocusInputMap().getUiMap());
        table.put("Table.ancestorInputMap", inputMapSet.getTableAncestorInputMap().getUiMap());
        table.put("TableHeader.ancestorInputMap", inputMapSet.getTableHeaderAncestorInputMap().getUiMap());
        table.put("TextArea.focusInputMap", inputMapSet.getTextAreaFocusInputMap().getUiMap());
        table.put("TextField.focusInputMap", inputMapSet.getTextFieldFocusInputMap().getUiMap());
        table.put("TextPane.focusInputMap", inputMapSet.getTextPaneFocusInputMap().getUiMap());
        table.put("ToggleButton.focusInputMap", inputMapSet.getToggleButtonFocusInputMap().getUiMap());
        table.put("ToolBar.ancestorInputMap", inputMapSet.getToolBarAncestorInputMap().getUiMap());
        table.put("Tree.ancestorInputMap", inputMapSet.getTreeAncestorInputMap().getUiMap());
        table.put("Tree.focusInputMap", inputMapSet.getTreeFocusInputMap().getUiMap());
    }
}

