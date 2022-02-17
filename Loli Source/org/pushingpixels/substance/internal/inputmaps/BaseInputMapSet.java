/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.inputmaps;

import javax.swing.TransferHandler;
import org.pushingpixels.substance.api.inputmaps.InputMapSet;
import org.pushingpixels.substance.api.inputmaps.SubstanceInputMap;

public class BaseInputMapSet
implements InputMapSet {
    public static final String PRESSED = "pressed";
    public static final String RELEASED = "released";
    public static final String COPY = (String)TransferHandler.getCopyAction().getValue("Name");
    public static final String PASTE = (String)TransferHandler.getPasteAction().getValue("Name");
    public static final String CUT = (String)TransferHandler.getCutAction().getValue("Name");

    protected SubstanceInputMap getActionControlFocusInputMap() {
        SubstanceInputMap result = new SubstanceInputMap();
        result.put("SPACE", PRESSED);
        result.put("released SPACE", RELEASED);
        return result;
    }

    @Override
    public SubstanceInputMap getButtonFocusInputMap() {
        return this.getActionControlFocusInputMap();
    }

    @Override
    public SubstanceInputMap getCheckBoxFocusInputMap() {
        return this.getActionControlFocusInputMap();
    }

    @Override
    public SubstanceInputMap getComboBoxAncestorInputMap() {
        SubstanceInputMap result = new SubstanceInputMap();
        result.put("ESCAPE", "hidePopup");
        result.put("PAGE_UP", "pageUpPassThrough");
        result.put("PAGE_DOWN", "pageDownPassThrough");
        result.put("HOME", "homePassThrough");
        result.put("END", "endPassThrough");
        result.put("DOWN", "selectNext");
        result.put("KP_DOWN", "selectNext");
        result.put("alt DOWN", "togglePopup");
        result.put("alt KP_DOWN", "togglePopup");
        result.put("alt UP", "togglePopup");
        result.put("alt KP_UP", "togglePopup");
        result.put("SPACE", "spacePopup");
        result.put("ENTER", "enterPressed");
        result.put("UP", "selectPrevious");
        result.put("KP_UP", "selectPrevious");
        return result;
    }

    @Override
    public SubstanceInputMap getDesktopAncestorInputMap() {
        SubstanceInputMap result = new SubstanceInputMap();
        result.put("ctrl F5", DesktopPaneActions.RESTORE);
        result.put("ctrl F4", DesktopPaneActions.CLOSE);
        result.put("ctrl F7", DesktopPaneActions.MOVE);
        result.put("ctrl F8", DesktopPaneActions.RESIZE);
        result.put("RIGHT", DesktopPaneActions.RIGHT);
        result.put("KP_RIGHT", DesktopPaneActions.RIGHT);
        result.put("shift RIGHT", DesktopPaneActions.SHRINK_RIGHT);
        result.put("shift KP_RIGHT", DesktopPaneActions.SHRINK_RIGHT);
        result.put("LEFT", DesktopPaneActions.LEFT);
        result.put("KP_LEFT", DesktopPaneActions.LEFT);
        result.put("shift LEFT", DesktopPaneActions.SHRINK_LEFT);
        result.put("shift KP_LEFT", DesktopPaneActions.SHRINK_LEFT);
        result.put("UP", DesktopPaneActions.UP);
        result.put("KP_UP", DesktopPaneActions.UP);
        result.put("shift UP", DesktopPaneActions.SHRINK_UP);
        result.put("shift KP_UP", DesktopPaneActions.SHRINK_UP);
        result.put("DOWN", DesktopPaneActions.DOWN);
        result.put("KP_DOWN", DesktopPaneActions.DOWN);
        result.put("shift DOWN", DesktopPaneActions.SHRINK_DOWN);
        result.put("shift KP_DOWN", DesktopPaneActions.SHRINK_DOWN);
        result.put("ESCAPE", DesktopPaneActions.ESCAPE);
        result.put("ctrl F9", DesktopPaneActions.MINIMIZE);
        result.put("ctrl F10", DesktopPaneActions.MAXIMIZE);
        result.put("ctrl F6", DesktopPaneActions.NEXT_FRAME);
        result.put("ctrl TAB", DesktopPaneActions.NEXT_FRAME);
        result.put("ctrl alt F6", DesktopPaneActions.NEXT_FRAME);
        result.put("shift ctrl alt F6", DesktopPaneActions.PREVIOUS_FRAME);
        result.put("ctrl F12", DesktopPaneActions.NAVIGATE_NEXT);
        result.put("shift ctrl F12", DesktopPaneActions.NAVIGATE_PREVIOUS);
        return result;
    }

    protected SubstanceInputMap getMultilineTextComponentFocusInputMap() {
        SubstanceInputMap result = new SubstanceInputMap();
        result.put("ctrl C", "copy-to-clipboard");
        result.put("ctrl V", "paste-from-clipboard");
        result.put("ctrl X", "cut-to-clipboard");
        result.put("COPY", "copy-to-clipboard");
        result.put("PASTE", "paste-from-clipboard");
        result.put("CUT", "cut-to-clipboard");
        result.put("control INSERT", "copy-to-clipboard");
        result.put("shift INSERT", "paste-from-clipboard");
        result.put("shift DELETE", "cut-to-clipboard");
        result.put("shift LEFT", "selection-backward");
        result.put("shift KP_LEFT", "selection-backward");
        result.put("shift RIGHT", "selection-forward");
        result.put("shift KP_RIGHT", "selection-forward");
        result.put("ctrl LEFT", "caret-previous-word");
        result.put("ctrl KP_LEFT", "caret-previous-word");
        result.put("ctrl RIGHT", "caret-next-word");
        result.put("ctrl KP_RIGHT", "caret-next-word");
        result.put("ctrl shift LEFT", "selection-previous-word");
        result.put("ctrl shift KP_LEFT", "selection-previous-word");
        result.put("ctrl shift RIGHT", "selection-next-word");
        result.put("ctrl shift KP_RIGHT", "selection-next-word");
        result.put("ctrl A", "select-all");
        result.put("HOME", "caret-begin-line");
        result.put("END", "caret-end-line");
        result.put("shift HOME", "selection-begin-line");
        result.put("shift END", "selection-end-line");
        result.put("UP", "caret-up");
        result.put("KP_UP", "caret-up");
        result.put("DOWN", "caret-down");
        result.put("KP_DOWN", "caret-down");
        result.put("PAGE_UP", "page-up");
        result.put("PAGE_DOWN", "page-down");
        result.put("shift PAGE_UP", "selection-page-up");
        result.put("shift PAGE_DOWN", "selection-page-down");
        result.put("ctrl shift PAGE_UP", "selection-page-left");
        result.put("ctrl shift PAGE_DOWN", "selection-page-right");
        result.put("shift UP", "selection-up");
        result.put("shift KP_UP", "selection-up");
        result.put("shift DOWN", "selection-down");
        result.put("shift KP_DOWN", "selection-down");
        result.put("ctrl shift HOME", "selection-begin");
        result.put("ctrl shift END", "selection-end");
        result.put("ENTER", "insert-break");
        result.put("BACK_SPACE", "delete-previous");
        result.put("shift BACK_SPACE", "delete-previous");
        result.put("ctrl H", "delete-previous");
        result.put("DELETE", "delete-next");
        result.put("ctrl DELETE", "delete-next-word");
        result.put("ctrl BACK_SPACE", "delete-previous-word");
        result.put("RIGHT", "caret-forward");
        result.put("KP_RIGHT", "caret-forward");
        result.put("LEFT", "caret-backward");
        result.put("KP_LEFT", "caret-backward");
        result.put("TAB", "insert-tab");
        result.put("ctrl BACK_SLASH", "unselect");
        result.put("ctrl HOME", "caret-begin");
        result.put("ctrl END", "caret-end");
        result.put("ctrl T", "next-link-action");
        result.put("ctrl shift T", "previous-link-action");
        result.put("ctrl SPACE", "activate-link-action");
        result.put("control shift O", "toggle-componentOrientation");
        return result;
    }

    @Override
    public SubstanceInputMap getEditorPaneFocusInputMap() {
        return this.getMultilineTextComponentFocusInputMap();
    }

    @Override
    public SubstanceInputMap getFileChooserAncestorInputMap() {
        SubstanceInputMap result = new SubstanceInputMap();
        result.put("ESCAPE", "cancelSelection");
        result.put("F2", "editFileName");
        result.put("F5", "refresh");
        result.put("BACK_SPACE", "Go Up");
        result.put("ENTER", "approveSelection");
        result.put("ctrl ENTER", "approveSelection");
        return result;
    }

    @Override
    public SubstanceInputMap getFormattedTextFieldFocusInputMap() {
        SubstanceInputMap result = new SubstanceInputMap();
        result.put("ctrl C", "copy-to-clipboard");
        result.put("ctrl V", "paste-from-clipboard");
        result.put("ctrl X", "cut-to-clipboard");
        result.put("COPY", "copy-to-clipboard");
        result.put("PASTE", "paste-from-clipboard");
        result.put("CUT", "cut-to-clipboard");
        result.put("control INSERT", "copy-to-clipboard");
        result.put("shift INSERT", "paste-from-clipboard");
        result.put("shift DELETE", "cut-to-clipboard");
        result.put("shift LEFT", "selection-backward");
        result.put("shift KP_LEFT", "selection-backward");
        result.put("shift RIGHT", "selection-forward");
        result.put("shift KP_RIGHT", "selection-forward");
        result.put("ctrl LEFT", "caret-previous-word");
        result.put("ctrl KP_LEFT", "caret-previous-word");
        result.put("ctrl RIGHT", "caret-next-word");
        result.put("ctrl KP_RIGHT", "caret-next-word");
        result.put("ctrl shift LEFT", "selection-previous-word");
        result.put("ctrl shift KP_LEFT", "selection-previous-word");
        result.put("ctrl shift RIGHT", "selection-next-word");
        result.put("ctrl shift KP_RIGHT", "selection-next-word");
        result.put("ctrl A", "select-all");
        result.put("HOME", "caret-begin-line");
        result.put("END", "caret-end-line");
        result.put("shift HOME", "selection-begin-line");
        result.put("shift END", "selection-end-line");
        result.put("BACK_SPACE", "delete-previous");
        result.put("shift BACK_SPACE", "delete-previous");
        result.put("ctrl H", "delete-previous");
        result.put("DELETE", "delete-next");
        result.put("ctrl DELETE", "delete-next-word");
        result.put("ctrl BACK_SPACE", "delete-previous-word");
        result.put("RIGHT", "caret-forward");
        result.put("LEFT", "caret-backward");
        result.put("KP_RIGHT", "caret-forward");
        result.put("KP_LEFT", "caret-backward");
        result.put("ENTER", "notify-field-accept");
        result.put("ctrl BACK_SLASH", "unselect");
        result.put("control shift O", "toggle-componentOrientation");
        result.put("ESCAPE", "reset-field-edit");
        result.put("UP", "increment");
        result.put("KP_UP", "increment");
        result.put("DOWN", "decrement");
        result.put("KP_DOWN", "decrement");
        return result;
    }

    @Override
    public SubstanceInputMap getListFocusInputMap() {
        SubstanceInputMap result = new SubstanceInputMap();
        result.put("ctrl C", COPY);
        result.put("ctrl V", PASTE);
        result.put("ctrl X", CUT);
        result.put("COPY", COPY);
        result.put("PASTE", PASTE);
        result.put("CUT", CUT);
        result.put("control INSERT", COPY);
        result.put("shift INSERT", PASTE);
        result.put("shift DELETE", CUT);
        result.put("UP", "selectPreviousRow");
        result.put("KP_UP", "selectPreviousRow");
        result.put("shift UP", "selectPreviousRowExtendSelection");
        result.put("shift KP_UP", "selectPreviousRowExtendSelection");
        result.put("ctrl shift UP", "selectPreviousRowExtendSelection");
        result.put("ctrl shift KP_UP", "selectPreviousRowExtendSelection");
        result.put("ctrl UP", "selectPreviousRowChangeLead");
        result.put("ctrl KP_UP", "selectPreviousRowChangeLead");
        result.put("DOWN", "selectNextRow");
        result.put("KP_DOWN", "selectNextRow");
        result.put("shift DOWN", "selectNextRowExtendSelection");
        result.put("shift KP_DOWN", "selectNextRowExtendSelection");
        result.put("ctrl shift DOWN", "selectNextRowExtendSelection");
        result.put("ctrl shift KP_DOWN", "selectNextRowExtendSelection");
        result.put("ctrl DOWN", "selectNextRowChangeLead");
        result.put("ctrl KP_DOWN", "selectNextRowChangeLead");
        result.put("LEFT", "selectPreviousColumn");
        result.put("KP_LEFT", "selectPreviousColumn");
        result.put("shift LEFT", "selectPreviousColumnExtendSelection");
        result.put("shift KP_LEFT", "selectPreviousColumnExtendSelection");
        result.put("ctrl shift LEFT", "selectPreviousColumnExtendSelection");
        result.put("ctrl shift KP_LEFT", "selectPreviousColumnExtendSelection");
        result.put("ctrl LEFT", "selectPreviousColumnChangeLead");
        result.put("ctrl KP_LEFT", "selectPreviousColumnChangeLead");
        result.put("RIGHT", "selectNextColumn");
        result.put("KP_RIGHT", "selectNextColumn");
        result.put("shift RIGHT", "selectNextColumnExtendSelection");
        result.put("shift KP_RIGHT", "selectNextColumnExtendSelection");
        result.put("ctrl shift RIGHT", "selectNextColumnExtendSelection");
        result.put("ctrl shift KP_RIGHT", "selectNextColumnExtendSelection");
        result.put("ctrl RIGHT", "selectNextColumnChangeLead");
        result.put("ctrl KP_RIGHT", "selectNextColumnChangeLead");
        result.put("HOME", "selectFirstRow");
        result.put("shift HOME", "selectFirstRowExtendSelection");
        result.put("ctrl shift HOME", "selectFirstRowExtendSelection");
        result.put("ctrl HOME", "selectFirstRowChangeLead");
        result.put("END", "selectLastRow");
        result.put("shift END", "selectLastRowExtendSelection");
        result.put("ctrl shift END", "selectLastRowExtendSelection");
        result.put("ctrl END", "selectLastRowChangeLead");
        result.put("PAGE_UP", "scrollUp");
        result.put("shift PAGE_UP", "scrollUpExtendSelection");
        result.put("ctrl shift PAGE_UP", "scrollUpExtendSelection");
        result.put("ctrl PAGE_UP", "scrollUpChangeLead");
        result.put("PAGE_DOWN", "scrollDown");
        result.put("shift PAGE_DOWN", "scrollDownExtendSelection");
        result.put("ctrl shift PAGE_DOWN", "scrollDownExtendSelection");
        result.put("ctrl PAGE_DOWN", "scrollDownChangeLead");
        result.put("ctrl A", "selectAll");
        result.put("ctrl SLASH", "selectAll");
        result.put("ctrl BACK_SLASH", "clearSelection");
        result.put("SPACE", "addToSelection");
        result.put("ctrl SPACE", "toggleAndAnchor");
        result.put("shift SPACE", "extendTo");
        result.put("ctrl shift SPACE", "moveSelectionTo");
        return result;
    }

    @Override
    public SubstanceInputMap getPasswordFieldFocusInputMap() {
        SubstanceInputMap result = new SubstanceInputMap();
        result.put("ctrl C", "copy-to-clipboard");
        result.put("ctrl V", "paste-from-clipboard");
        result.put("ctrl X", "cut-to-clipboard");
        result.put("COPY", "copy-to-clipboard");
        result.put("PASTE", "paste-from-clipboard");
        result.put("CUT", "cut-to-clipboard");
        result.put("control INSERT", "copy-to-clipboard");
        result.put("shift INSERT", "paste-from-clipboard");
        result.put("shift DELETE", "cut-to-clipboard");
        result.put("shift LEFT", "selection-backward");
        result.put("shift KP_LEFT", "selection-backward");
        result.put("shift RIGHT", "selection-forward");
        result.put("shift KP_RIGHT", "selection-forward");
        result.put("ctrl LEFT", "caret-begin-line");
        result.put("ctrl KP_LEFT", "caret-begin-line");
        result.put("ctrl RIGHT", "caret-end-line");
        result.put("ctrl KP_RIGHT", "caret-end-line");
        result.put("ctrl shift LEFT", "selection-begin-line");
        result.put("ctrl shift KP_LEFT", "selection-begin-line");
        result.put("ctrl shift RIGHT", "selection-end-line");
        result.put("ctrl shift KP_RIGHT", "selection-end-line");
        result.put("ctrl A", "select-all");
        result.put("HOME", "caret-begin-line");
        result.put("END", "caret-end-line");
        result.put("shift HOME", "selection-begin-line");
        result.put("shift END", "selection-end-line");
        result.put("BACK_SPACE", "delete-previous");
        result.put("shift BACK_SPACE", "delete-previous");
        result.put("ctrl H", "delete-previous");
        result.put("DELETE", "delete-next");
        result.put("RIGHT", "caret-forward");
        result.put("LEFT", "caret-backward");
        result.put("KP_RIGHT", "caret-forward");
        result.put("KP_LEFT", "caret-backward");
        result.put("ENTER", "notify-field-accept");
        result.put("ctrl BACK_SLASH", "unselect");
        result.put("control shift O", "toggle-componentOrientation");
        return result;
    }

    @Override
    public SubstanceInputMap getRadioButtonFocusInputMap() {
        return this.getActionControlFocusInputMap();
    }

    @Override
    public SubstanceInputMap getRootPaneAncestorInputMap() {
        SubstanceInputMap result = new SubstanceInputMap();
        result.put("shift F10", "postPopup");
        result.put("CONTEXT_MENU", "postPopup");
        return result;
    }

    @Override
    public SubstanceInputMap getScrollBarAncestorInputMap() {
        SubstanceInputMap result = new SubstanceInputMap();
        result.put("RIGHT", "positiveUnitIncrement");
        result.put("KP_RIGHT", "positiveUnitIncrement");
        result.put("DOWN", "positiveUnitIncrement");
        result.put("KP_DOWN", "positiveUnitIncrement");
        result.put("PAGE_DOWN", "positiveBlockIncrement");
        result.put("LEFT", "negativeUnitIncrement");
        result.put("KP_LEFT", "negativeUnitIncrement");
        result.put("UP", "negativeUnitIncrement");
        result.put("KP_UP", "negativeUnitIncrement");
        result.put("PAGE_UP", "negativeBlockIncrement");
        result.put("HOME", "minScroll");
        result.put("END", "maxScroll");
        return result;
    }

    @Override
    public SubstanceInputMap getScrollPaneAncestorInputMap() {
        SubstanceInputMap result = new SubstanceInputMap();
        result.put("RIGHT", "unitScrollRight");
        result.put("KP_RIGHT", "unitScrollRight");
        result.put("DOWN", "unitScrollDown");
        result.put("KP_DOWN", "unitScrollDown");
        result.put("LEFT", "unitScrollLeft");
        result.put("KP_LEFT", "unitScrollLeft");
        result.put("UP", "unitScrollUp");
        result.put("KP_UP", "unitScrollUp");
        result.put("PAGE_UP", "scrollUp");
        result.put("PAGE_DOWN", "scrollDown");
        result.put("ctrl PAGE_UP", "scrollLeft");
        result.put("ctrl PAGE_DOWN", "scrollRight");
        result.put("ctrl HOME", "scrollHome");
        result.put("ctrl END", "scrollEnd");
        return result;
    }

    @Override
    public SubstanceInputMap getSliderFocusInputMap() {
        SubstanceInputMap result = new SubstanceInputMap();
        result.put("RIGHT", "positiveUnitIncrement");
        result.put("KP_RIGHT", "positiveUnitIncrement");
        result.put("DOWN", "negativeUnitIncrement");
        result.put("KP_DOWN", "negativeUnitIncrement");
        result.put("LEFT", "negativeUnitIncrement");
        result.put("KP_LEFT", "negativeUnitIncrement");
        result.put("UP", "positiveUnitIncrement");
        result.put("KP_UP", "positiveUnitIncrement");
        result.put("PAGE_DOWN", "negativeBlockIncrement");
        result.put("ctrl PAGE_DOWN", "negativeBlockIncrement");
        result.put("PAGE_UP", "positiveBlockIncrement");
        result.put("ctrl PAGE_UP", "positiveBlockIncrement");
        result.put("HOME", "minScroll");
        result.put("END", "maxScroll");
        return result;
    }

    @Override
    public SubstanceInputMap getSpinnerAncestorInputMap() {
        SubstanceInputMap result = new SubstanceInputMap();
        result.put("UP", "increment");
        result.put("KP_UP", "increment");
        result.put("DOWN", "decrement");
        result.put("KP_DOWN", "decrement");
        return result;
    }

    @Override
    public SubstanceInputMap getSplitPaneAncestorInputMap() {
        SubstanceInputMap result = new SubstanceInputMap();
        result.put("UP", "negativeIncrement");
        result.put("DOWN", "positiveIncrement");
        result.put("LEFT", "negativeIncrement");
        result.put("RIGHT", "positiveIncrement");
        result.put("KP_UP", "negativeIncrement");
        result.put("KP_DOWN", "positiveIncrement");
        result.put("KP_LEFT", "negativeIncrement");
        result.put("KP_RIGHT", "positiveIncrement");
        result.put("HOME", "selectMin");
        result.put("END", "selectMax");
        result.put("F8", "startResize");
        result.put("F6", "toggleFocus");
        result.put("ctrl TAB", "focusOutForward");
        result.put("ctrl shift TAB", "focusOutBackward");
        return result;
    }

    @Override
    public SubstanceInputMap getTabbedPaneAncestorInputMap() {
        SubstanceInputMap result = new SubstanceInputMap();
        result.put("ctrl PAGE_DOWN", "navigatePageDown");
        result.put("ctrl PAGE_UP", "navigatePageUp");
        result.put("ctrl UP", "requestFocus");
        result.put("ctrl KP_UP", "requestFocus");
        return result;
    }

    @Override
    public SubstanceInputMap getTabbedPaneFocusInputMap() {
        SubstanceInputMap result = new SubstanceInputMap();
        result.put("RIGHT", "navigateRight");
        result.put("KP_RIGHT", "navigateRight");
        result.put("LEFT", "navigateLeft");
        result.put("KP_LEFT", "navigateLeft");
        result.put("UP", "navigateUp");
        result.put("KP_UP", "navigateUp");
        result.put("DOWN", "navigateDown");
        result.put("KP_DOWN", "navigateDown");
        result.put("ctrl DOWN", "requestFocusForVisibleComponent");
        result.put("ctrl KP_DOWN", "requestFocusForVisibleComponent");
        return result;
    }

    @Override
    public SubstanceInputMap getTableAncestorInputMap() {
        SubstanceInputMap result = new SubstanceInputMap();
        result.put("ctrl C", COPY);
        result.put("ctrl V", PASTE);
        result.put("ctrl X", CUT);
        result.put("COPY", COPY);
        result.put("PASTE", PASTE);
        result.put("CUT", CUT);
        result.put("control INSERT", COPY);
        result.put("shift INSERT", PASTE);
        result.put("shift DELETE", CUT);
        result.put("RIGHT", "selectNextColumn");
        result.put("KP_RIGHT", "selectNextColumn");
        result.put("shift RIGHT", "selectNextColumnExtendSelection");
        result.put("shift KP_RIGHT", "selectNextColumnExtendSelection");
        result.put("ctrl shift RIGHT", "selectNextColumnExtendSelection");
        result.put("ctrl shift KP_RIGHT", "selectNextColumnExtendSelection");
        result.put("ctrl RIGHT", "selectNextColumnChangeLead");
        result.put("ctrl KP_RIGHT", "selectNextColumnChangeLead");
        result.put("LEFT", "selectPreviousColumn");
        result.put("KP_LEFT", "selectPreviousColumn");
        result.put("shift LEFT", "selectPreviousColumnExtendSelection");
        result.put("shift KP_LEFT", "selectPreviousColumnExtendSelection");
        result.put("ctrl shift LEFT", "selectPreviousColumnExtendSelection");
        result.put("ctrl shift KP_LEFT", "selectPreviousColumnExtendSelection");
        result.put("ctrl LEFT", "selectPreviousColumnChangeLead");
        result.put("ctrl KP_LEFT", "selectPreviousColumnChangeLead");
        result.put("DOWN", "selectNextRow");
        result.put("KP_DOWN", "selectNextRow");
        result.put("shift DOWN", "selectNextRowExtendSelection");
        result.put("shift KP_DOWN", "selectNextRowExtendSelection");
        result.put("ctrl shift DOWN", "selectNextRowExtendSelection");
        result.put("ctrl shift KP_DOWN", "selectNextRowExtendSelection");
        result.put("ctrl DOWN", "selectNextRowChangeLead");
        result.put("ctrl KP_DOWN", "selectNextRowChangeLead");
        result.put("UP", "selectPreviousRow");
        result.put("KP_UP", "selectPreviousRow");
        result.put("shift UP", "selectPreviousRowExtendSelection");
        result.put("shift KP_UP", "selectPreviousRowExtendSelection");
        result.put("ctrl shift UP", "selectPreviousRowExtendSelection");
        result.put("ctrl shift KP_UP", "selectPreviousRowExtendSelection");
        result.put("ctrl UP", "selectPreviousRowChangeLead");
        result.put("ctrl KP_UP", "selectPreviousRowChangeLead");
        result.put("HOME", "selectFirstColumn");
        result.put("shift HOME", "selectFirstColumnExtendSelection");
        result.put("ctrl shift HOME", "selectFirstRowExtendSelection");
        result.put("ctrl HOME", "selectFirstRow");
        result.put("END", "selectLastColumn");
        result.put("shift END", "selectLastColumnExtendSelection");
        result.put("ctrl shift END", "selectLastRowExtendSelection");
        result.put("ctrl END", "selectLastRow");
        result.put("PAGE_UP", "scrollUpChangeSelection");
        result.put("shift PAGE_UP", "scrollUpExtendSelection");
        result.put("ctrl shift PAGE_UP", "scrollLeftExtendSelection");
        result.put("ctrl PAGE_UP", "scrollLeftChangeSelection");
        result.put("PAGE_DOWN", "scrollDownChangeSelection");
        result.put("shift PAGE_DOWN", "scrollDownExtendSelection");
        result.put("ctrl shift PAGE_DOWN", "scrollRightExtendSelection");
        result.put("ctrl PAGE_DOWN", "scrollRightChangeSelection");
        result.put("TAB", "selectNextColumnCell");
        result.put("shift TAB", "selectPreviousColumnCell");
        result.put("ENTER", "selectNextRowCell");
        result.put("shift ENTER", "selectPreviousRowCell");
        result.put("ctrl A", "selectAll");
        result.put("ctrl SLASH", "selectAll");
        result.put("ctrl BACK_SLASH", "clearSelection");
        result.put("ESCAPE", "cancel");
        result.put("F2", "startEditing");
        result.put("SPACE", "addToSelection");
        result.put("ctrl SPACE", "toggleAndAnchor");
        result.put("shift SPACE", "extendTo");
        result.put("ctrl shift SPACE", "moveSelectionTo");
        result.put("F8", "focusHeader");
        return result;
    }

    @Override
    public SubstanceInputMap getTableHeaderAncestorInputMap() {
        SubstanceInputMap result = new SubstanceInputMap();
        result.put("SPACE", "toggleSortOrder");
        result.put("LEFT", "selectColumnToLeft");
        result.put("KP_LEFT", "selectColumnToLeft");
        result.put("RIGHT", "selectColumnToRight");
        result.put("KP_RIGHT", "selectColumnToRight");
        result.put("alt LEFT", "moveColumnLeft");
        result.put("alt KP_LEFT", "moveColumnLeft");
        result.put("alt RIGHT", "moveColumnRight");
        result.put("alt KP_RIGHT", "moveColumnRight");
        result.put("alt shift LEFT", "resizeLeft");
        result.put("alt shift KP_LEFT", "resizeLeft");
        result.put("alt shift RIGHT", "resizeRight");
        result.put("alt shift KP_RIGHT", "resizeRight");
        result.put("ESCAPE", "focusTable");
        return result;
    }

    @Override
    public SubstanceInputMap getTextAreaFocusInputMap() {
        return this.getMultilineTextComponentFocusInputMap();
    }

    @Override
    public SubstanceInputMap getTextFieldFocusInputMap() {
        SubstanceInputMap result = new SubstanceInputMap();
        result.put("ctrl C", "copy-to-clipboard");
        result.put("ctrl V", "paste-from-clipboard");
        result.put("ctrl X", "cut-to-clipboard");
        result.put("COPY", "copy-to-clipboard");
        result.put("PASTE", "paste-from-clipboard");
        result.put("CUT", "cut-to-clipboard");
        result.put("control INSERT", "copy-to-clipboard");
        result.put("shift INSERT", "paste-from-clipboard");
        result.put("shift DELETE", "cut-to-clipboard");
        result.put("shift LEFT", "selection-backward");
        result.put("shift KP_LEFT", "selection-backward");
        result.put("shift RIGHT", "selection-forward");
        result.put("shift KP_RIGHT", "selection-forward");
        result.put("ctrl LEFT", "caret-previous-word");
        result.put("ctrl KP_LEFT", "caret-previous-word");
        result.put("ctrl RIGHT", "caret-next-word");
        result.put("ctrl KP_RIGHT", "caret-next-word");
        result.put("ctrl shift LEFT", "selection-previous-word");
        result.put("ctrl shift KP_LEFT", "selection-previous-word");
        result.put("ctrl shift RIGHT", "selection-next-word");
        result.put("ctrl shift KP_RIGHT", "selection-next-word");
        result.put("ctrl A", "select-all");
        result.put("HOME", "caret-begin-line");
        result.put("END", "caret-end-line");
        result.put("shift HOME", "selection-begin-line");
        result.put("shift END", "selection-end-line");
        result.put("BACK_SPACE", "delete-previous");
        result.put("shift BACK_SPACE", "delete-previous");
        result.put("ctrl H", "delete-previous");
        result.put("DELETE", "delete-next");
        result.put("ctrl DELETE", "delete-next-word");
        result.put("ctrl BACK_SPACE", "delete-previous-word");
        result.put("RIGHT", "caret-forward");
        result.put("LEFT", "caret-backward");
        result.put("KP_RIGHT", "caret-forward");
        result.put("KP_LEFT", "caret-backward");
        result.put("ENTER", "notify-field-accept");
        result.put("ctrl BACK_SLASH", "unselect");
        result.put("control shift O", "toggle-componentOrientation");
        return result;
    }

    @Override
    public SubstanceInputMap getTextPaneFocusInputMap() {
        return this.getMultilineTextComponentFocusInputMap();
    }

    @Override
    public SubstanceInputMap getToggleButtonFocusInputMap() {
        return this.getActionControlFocusInputMap();
    }

    @Override
    public SubstanceInputMap getToolBarAncestorInputMap() {
        SubstanceInputMap result = new SubstanceInputMap();
        result.put("UP", "navigateUp");
        result.put("KP_UP", "navigateUp");
        result.put("DOWN", "navigateDown");
        result.put("KP_DOWN", "navigateDown");
        result.put("LEFT", "navigateLeft");
        result.put("KP_LEFT", "navigateLeft");
        result.put("RIGHT", "navigateRight");
        result.put("KP_RIGHT", "navigateRight");
        return result;
    }

    @Override
    public SubstanceInputMap getTreeAncestorInputMap() {
        SubstanceInputMap result = new SubstanceInputMap();
        result.put("ESCAPE", "cancel");
        return result;
    }

    @Override
    public SubstanceInputMap getTreeFocusInputMap() {
        SubstanceInputMap result = new SubstanceInputMap();
        result.put("ADD", "expand");
        result.put("SUBTRACT", "collapse");
        result.put("ctrl C", COPY);
        result.put("ctrl V", PASTE);
        result.put("ctrl X", CUT);
        result.put("COPY", COPY);
        result.put("PASTE", PASTE);
        result.put("CUT", CUT);
        result.put("control INSERT", COPY);
        result.put("shift INSERT", PASTE);
        result.put("shift DELETE", CUT);
        result.put("UP", "selectPrevious");
        result.put("KP_UP", "selectPrevious");
        result.put("shift UP", "selectPreviousExtendSelection");
        result.put("shift KP_UP", "selectPreviousExtendSelection");
        result.put("ctrl shift UP", "selectPreviousExtendSelection");
        result.put("ctrl shift KP_UP", "selectPreviousExtendSelection");
        result.put("ctrl UP", "selectPreviousChangeLead");
        result.put("ctrl KP_UP", "selectPreviousChangeLead");
        result.put("DOWN", "selectNext");
        result.put("KP_DOWN", "selectNext");
        result.put("shift DOWN", "selectNextExtendSelection");
        result.put("shift KP_DOWN", "selectNextExtendSelection");
        result.put("ctrl shift DOWN", "selectNextExtendSelection");
        result.put("ctrl shift KP_DOWN", "selectNextExtendSelection");
        result.put("ctrl DOWN", "selectNextChangeLead");
        result.put("ctrl KP_DOWN", "selectNextChangeLead");
        result.put("RIGHT", "selectChild");
        result.put("KP_RIGHT", "selectChild");
        result.put("LEFT", "selectParent");
        result.put("KP_LEFT", "selectParent");
        result.put("PAGE_UP", "scrollUpChangeSelection");
        result.put("shift PAGE_UP", "scrollUpExtendSelection");
        result.put("ctrl shift PAGE_UP", "scrollUpExtendSelection");
        result.put("ctrl PAGE_UP", "scrollUpChangeLead");
        result.put("PAGE_DOWN", "scrollDownChangeSelection");
        result.put("shift PAGE_DOWN", "scrollDownExtendSelection");
        result.put("ctrl shift PAGE_DOWN", "scrollDownExtendSelection");
        result.put("ctrl PAGE_DOWN", "scrollDownChangeLead");
        result.put("HOME", "selectFirst");
        result.put("shift HOME", "selectFirstExtendSelection");
        result.put("ctrl shift HOME", "selectFirstExtendSelection");
        result.put("ctrl HOME", "selectFirstChangeLead");
        result.put("END", "selectLast");
        result.put("shift END", "selectLastExtendSelection");
        result.put("ctrl shift END", "selectLastExtendSelection");
        result.put("ctrl END", "selectLastChangeLead");
        result.put("F2", "startEditing");
        result.put("ctrl A", "selectAll");
        result.put("ctrl SLASH", "selectAll");
        result.put("ctrl BACK_SLASH", "clearSelection");
        result.put("ctrl LEFT", "scrollLeft");
        result.put("ctrl KP_LEFT", "scrollLeft");
        result.put("ctrl RIGHT", "scrollRight");
        result.put("ctrl KP_RIGHT", "scrollRight");
        result.put("SPACE", "addToSelection");
        result.put("ctrl SPACE", "toggleAndAnchor");
        result.put("shift SPACE", "extendTo");
        result.put("ctrl shift SPACE", "moveSelectionTo");
        return result;
    }

    protected static class TreeActions {
        public static final String SELECT_PREVIOUS = "selectPrevious";
        public static final String SELECT_PREVIOUS_CHANGE_LEAD = "selectPreviousChangeLead";
        public static final String SELECT_PREVIOUS_EXTEND_SELECTION = "selectPreviousExtendSelection";
        public static final String SELECT_NEXT = "selectNext";
        public static final String SELECT_NEXT_CHANGE_LEAD = "selectNextChangeLead";
        public static final String SELECT_NEXT_EXTEND_SELECTION = "selectNextExtendSelection";
        public static final String SELECT_CHILD = "selectChild";
        public static final String SELECT_CHILD_CHANGE_LEAD = "selectChildChangeLead";
        public static final String SELECT_PARENT = "selectParent";
        public static final String SELECT_PARENT_CHANGE_LEAD = "selectParentChangeLead";
        public static final String SCROLL_UP_CHANGE_SELECTION = "scrollUpChangeSelection";
        public static final String SCROLL_UP_CHANGE_LEAD = "scrollUpChangeLead";
        public static final String SCROLL_UP_EXTEND_SELECTION = "scrollUpExtendSelection";
        public static final String SCROLL_DOWN_CHANGE_SELECTION = "scrollDownChangeSelection";
        public static final String SCROLL_DOWN_EXTEND_SELECTION = "scrollDownExtendSelection";
        public static final String SCROLL_DOWN_CHANGE_LEAD = "scrollDownChangeLead";
        public static final String SELECT_FIRST = "selectFirst";
        public static final String SELECT_FIRST_CHANGE_LEAD = "selectFirstChangeLead";
        public static final String SELECT_FIRST_EXTEND_SELECTION = "selectFirstExtendSelection";
        public static final String SELECT_LAST = "selectLast";
        public static final String SELECT_LAST_CHANGE_LEAD = "selectLastChangeLead";
        public static final String SELECT_LAST_EXTEND_SELECTION = "selectLastExtendSelection";
        public static final String TOGGLE = "toggle";
        public static final String CANCEL_EDITING = "cancel";
        public static final String START_EDITING = "startEditing";
        public static final String SELECT_ALL = "selectAll";
        public static final String CLEAR_SELECTION = "clearSelection";
        public static final String SCROLL_LEFT = "scrollLeft";
        public static final String SCROLL_RIGHT = "scrollRight";
        public static final String SCROLL_LEFT_EXTEND_SELECTION = "scrollLeftExtendSelection";
        public static final String SCROLL_RIGHT_EXTEND_SELECTION = "scrollRightExtendSelection";
        public static final String SCROLL_RIGHT_CHANGE_LEAD = "scrollRightChangeLead";
        public static final String SCROLL_LEFT_CHANGE_LEAD = "scrollLeftChangeLead";
        public static final String EXPAND = "expand";
        public static final String COLLAPSE = "collapse";
        public static final String MOVE_SELECTION_TO_PARENT = "moveSelectionToParent";
        public static final String ADD_TO_SELECTION = "addToSelection";
        public static final String TOGGLE_AND_ANCHOR = "toggleAndAnchor";
        public static final String EXTEND_TO = "extendTo";
        public static final String MOVE_SELECTION_TO = "moveSelectionTo";

        protected TreeActions() {
        }
    }

    protected static class ToolBarActions {
        public static final String NAVIGATE_RIGHT = "navigateRight";
        public static final String NAVIGATE_LEFT = "navigateLeft";
        public static final String NAVIGATE_UP = "navigateUp";
        public static final String NAVIGATE_DOWN = "navigateDown";

        protected ToolBarActions() {
        }
    }

    protected static class TableHeaderActions {
        public static final String TOGGLE_SORT_ORDER = "toggleSortOrder";
        public static final String SELECT_COLUMN_TO_LEFT = "selectColumnToLeft";
        public static final String SELECT_COLUMN_TO_RIGHT = "selectColumnToRight";
        public static final String MOVE_COLUMN_LEFT = "moveColumnLeft";
        public static final String MOVE_COLUMN_RIGHT = "moveColumnRight";
        public static final String RESIZE_LEFT = "resizeLeft";
        public static final String RESIZE_RIGHT = "resizeRight";
        public static final String FOCUS_TABLE = "focusTable";

        protected TableHeaderActions() {
        }
    }

    protected static class TableActions {
        public static final String CANCEL_EDITING = "cancel";
        public static final String SELECT_ALL = "selectAll";
        public static final String CLEAR_SELECTION = "clearSelection";
        public static final String START_EDITING = "startEditing";
        public static final String NEXT_ROW = "selectNextRow";
        public static final String NEXT_ROW_CELL = "selectNextRowCell";
        public static final String NEXT_ROW_EXTEND_SELECTION = "selectNextRowExtendSelection";
        public static final String NEXT_ROW_CHANGE_LEAD = "selectNextRowChangeLead";
        public static final String PREVIOUS_ROW = "selectPreviousRow";
        public static final String PREVIOUS_ROW_CELL = "selectPreviousRowCell";
        public static final String PREVIOUS_ROW_EXTEND_SELECTION = "selectPreviousRowExtendSelection";
        public static final String PREVIOUS_ROW_CHANGE_LEAD = "selectPreviousRowChangeLead";
        public static final String NEXT_COLUMN = "selectNextColumn";
        public static final String NEXT_COLUMN_CELL = "selectNextColumnCell";
        public static final String NEXT_COLUMN_EXTEND_SELECTION = "selectNextColumnExtendSelection";
        public static final String NEXT_COLUMN_CHANGE_LEAD = "selectNextColumnChangeLead";
        public static final String PREVIOUS_COLUMN = "selectPreviousColumn";
        public static final String PREVIOUS_COLUMN_CELL = "selectPreviousColumnCell";
        public static final String PREVIOUS_COLUMN_EXTEND_SELECTION = "selectPreviousColumnExtendSelection";
        public static final String PREVIOUS_COLUMN_CHANGE_LEAD = "selectPreviousColumnChangeLead";
        public static final String SCROLL_LEFT_CHANGE_SELECTION = "scrollLeftChangeSelection";
        public static final String SCROLL_LEFT_EXTEND_SELECTION = "scrollLeftExtendSelection";
        public static final String SCROLL_RIGHT_CHANGE_SELECTION = "scrollRightChangeSelection";
        public static final String SCROLL_RIGHT_EXTEND_SELECTION = "scrollRightExtendSelection";
        public static final String SCROLL_UP_CHANGE_SELECTION = "scrollUpChangeSelection";
        public static final String SCROLL_UP_EXTEND_SELECTION = "scrollUpExtendSelection";
        public static final String SCROLL_DOWN_CHANGE_SELECTION = "scrollDownChangeSelection";
        public static final String SCROLL_DOWN_EXTEND_SELECTION = "scrollDownExtendSelection";
        public static final String FIRST_COLUMN = "selectFirstColumn";
        public static final String FIRST_COLUMN_EXTEND_SELECTION = "selectFirstColumnExtendSelection";
        public static final String LAST_COLUMN = "selectLastColumn";
        public static final String LAST_COLUMN_EXTEND_SELECTION = "selectLastColumnExtendSelection";
        public static final String FIRST_ROW = "selectFirstRow";
        public static final String FIRST_ROW_EXTEND_SELECTION = "selectFirstRowExtendSelection";
        public static final String LAST_ROW = "selectLastRow";
        public static final String LAST_ROW_EXTEND_SELECTION = "selectLastRowExtendSelection";
        public static final String ADD_TO_SELECTION = "addToSelection";
        public static final String TOGGLE_AND_ANCHOR = "toggleAndAnchor";
        public static final String EXTEND_TO = "extendTo";
        public static final String MOVE_SELECTION_TO = "moveSelectionTo";
        public static final String FOCUS_HEADER = "focusHeader";

        protected TableActions() {
        }
    }

    protected static class TabbedPaneActions {
        public static final String NEXT = "navigateNext";
        public static final String PREVIOUS = "navigatePrevious";
        public static final String RIGHT = "navigateRight";
        public static final String LEFT = "navigateLeft";
        public static final String UP = "navigateUp";
        public static final String DOWN = "navigateDown";
        public static final String PAGE_UP = "navigatePageUp";
        public static final String PAGE_DOWN = "navigatePageDown";
        public static final String REQUEST_FOCUS = "requestFocus";
        public static final String REQUEST_FOCUS_FOR_VISIBLE = "requestFocusForVisibleComponent";
        public static final String SET_SELECTED = "setSelectedIndex";
        public static final String SELECT_FOCUSED = "selectTabWithFocus";
        public static final String SCROLL_FORWARD = "scrollTabsForwardAction";
        public static final String SCROLL_BACKWARD = "scrollTabsBackwardAction";

        protected TabbedPaneActions() {
        }
    }

    protected static class SplitPaneActions {
        public static final String NEGATIVE_INCREMENT = "negativeIncrement";
        public static final String POSITIVE_INCREMENT = "positiveIncrement";
        public static final String SELECT_MIN = "selectMin";
        public static final String SELECT_MAX = "selectMax";
        public static final String START_RESIZE = "startResize";
        public static final String TOGGLE_FOCUS = "toggleFocus";
        public static final String FOCUS_OUT_FORWARD = "focusOutForward";
        public static final String FOCUS_OUT_BACKWARD = "focusOutBackward";

        protected SplitPaneActions() {
        }
    }

    protected static class SliderActions {
        public static final String POSITIVE_UNIT_INCREMENT = "positiveUnitIncrement";
        public static final String POSITIVE_BLOCK_INCREMENT = "positiveBlockIncrement";
        public static final String NEGATIVE_UNIT_INCREMENT = "negativeUnitIncrement";
        public static final String NEGATIVE_BLOCK_INCREMENT = "negativeBlockIncrement";
        public static final String MIN_SCROLL_INCREMENT = "minScroll";
        public static final String MAX_SCROLL_INCREMENT = "maxScroll";

        protected SliderActions() {
        }
    }

    protected static class ScrollPaneActions {
        public static final String SCROLL_UP = "scrollUp";
        public static final String SCROLL_DOWN = "scrollDown";
        public static final String SCROLL_HOME = "scrollHome";
        public static final String SCROLL_END = "scrollEnd";
        public static final String UNIT_SCROLL_UP = "unitScrollUp";
        public static final String UNIT_SCROLL_DOWN = "unitScrollDown";
        public static final String SCROLL_LEFT = "scrollLeft";
        public static final String SCROLL_RIGHT = "scrollRight";
        public static final String UNIT_SCROLL_LEFT = "unitScrollLeft";
        public static final String UNIT_SCROLL_RIGHT = "unitScrollRight";

        protected ScrollPaneActions() {
        }
    }

    protected static class ScrollBarActions {
        public static final String POSITIVE_UNIT_INCREMENT = "positiveUnitIncrement";
        public static final String POSITIVE_BLOCK_INCREMENT = "positiveBlockIncrement";
        public static final String NEGATIVE_UNIT_INCREMENT = "negativeUnitIncrement";
        public static final String NEGATIVE_BLOCK_INCREMENT = "negativeBlockIncrement";
        public static final String MIN_SCROLL = "minScroll";
        public static final String MAX_SCROLL = "maxScroll";

        protected ScrollBarActions() {
        }
    }

    protected static class RootPaneActions {
        public static final String POST_POPUP = "postPopup";

        protected RootPaneActions() {
        }
    }

    protected static class ListActions {
        public static final String SELECT_PREVIOUS_COLUMN = "selectPreviousColumn";
        public static final String SELECT_PREVIOUS_COLUMN_EXTEND = "selectPreviousColumnExtendSelection";
        public static final String SELECT_PREVIOUS_COLUMN_CHANGE_LEAD = "selectPreviousColumnChangeLead";
        public static final String SELECT_NEXT_COLUMN = "selectNextColumn";
        public static final String SELECT_NEXT_COLUMN_EXTEND = "selectNextColumnExtendSelection";
        public static final String SELECT_NEXT_COLUMN_CHANGE_LEAD = "selectNextColumnChangeLead";
        public static final String SELECT_PREVIOUS_ROW = "selectPreviousRow";
        public static final String SELECT_PREVIOUS_ROW_EXTEND = "selectPreviousRowExtendSelection";
        public static final String SELECT_PREVIOUS_ROW_CHANGE_LEAD = "selectPreviousRowChangeLead";
        public static final String SELECT_NEXT_ROW = "selectNextRow";
        public static final String SELECT_NEXT_ROW_EXTEND = "selectNextRowExtendSelection";
        public static final String SELECT_NEXT_ROW_CHANGE_LEAD = "selectNextRowChangeLead";
        public static final String SELECT_FIRST_ROW = "selectFirstRow";
        public static final String SELECT_FIRST_ROW_EXTEND = "selectFirstRowExtendSelection";
        public static final String SELECT_FIRST_ROW_CHANGE_LEAD = "selectFirstRowChangeLead";
        public static final String SELECT_LAST_ROW = "selectLastRow";
        public static final String SELECT_LAST_ROW_EXTEND = "selectLastRowExtendSelection";
        public static final String SELECT_LAST_ROW_CHANGE_LEAD = "selectLastRowChangeLead";
        public static final String SCROLL_UP = "scrollUp";
        public static final String SCROLL_UP_EXTEND = "scrollUpExtendSelection";
        public static final String SCROLL_UP_CHANGE_LEAD = "scrollUpChangeLead";
        public static final String SCROLL_DOWN = "scrollDown";
        public static final String SCROLL_DOWN_EXTEND = "scrollDownExtendSelection";
        public static final String SCROLL_DOWN_CHANGE_LEAD = "scrollDownChangeLead";
        public static final String SELECT_ALL = "selectAll";
        public static final String CLEAR_SELECTION = "clearSelection";
        public static final String ADD_TO_SELECTION = "addToSelection";
        public static final String TOGGLE_AND_ANCHOR = "toggleAndAnchor";
        public static final String EXTEND_TO = "extendTo";
        public static final String MOVE_SELECTION_TO = "moveSelectionTo";

        protected ListActions() {
        }
    }

    protected static class FileChooserActions {
        public static final String APPROVE_SELECTION = "approveSelection";
        public static final String CANCEL_SELECTION = "cancelSelection";
        public static final String EDIT_FILE_NAME = "editFileName";
        public static final String REFRESH = "refresh";
        public static final String GO_UP = "Go Up";

        protected FileChooserActions() {
        }
    }

    protected static class TextComponentActions {
        public static final String SELECTION_PAGE_UP = "selection-page-up";
        public static final String SELECTION_PAGE_DOWN = "selection-page-down";
        public static final String SELECTION_PAGE_LEFT = "selection-page-left";
        public static final String SELECTION_PAGE_RIGHT = "selection-page-right";
        public static final String UNSELECT = "unselect";
        public static final String TOGGLE_COMPONENT_ORIENTATION = "toggle-componentOrientation";
        public static final String NEXT_LINK = "next-link-action";
        public static final String PREVIOUS_LINK = "previous-link-action";
        public static final String ACTIVATE_LINK = "activate-link-action";
        public static final String RESET_FIELD_EDIT = "reset-field-edit";
        public static final String INCREMENT = "increment";
        public static final String DECREMENT = "decrement";

        protected TextComponentActions() {
        }
    }

    protected static class DesktopPaneActions {
        public static String CLOSE = "close";
        public static String ESCAPE = "escape";
        public static String MAXIMIZE = "maximize";
        public static String MINIMIZE = "minimize";
        public static String MOVE = "move";
        public static String RESIZE = "resize";
        public static String RESTORE = "restore";
        public static String LEFT = "left";
        public static String RIGHT = "right";
        public static String UP = "up";
        public static String DOWN = "down";
        public static String SHRINK_LEFT = "shrinkLeft";
        public static String SHRINK_RIGHT = "shrinkRight";
        public static String SHRINK_UP = "shrinkUp";
        public static String SHRINK_DOWN = "shrinkDown";
        public static String NEXT_FRAME = "selectNextFrame";
        public static String PREVIOUS_FRAME = "selectPreviousFrame";
        public static String NAVIGATE_NEXT = "navigateNext";
        public static String NAVIGATE_PREVIOUS = "navigatePrevious";

        protected DesktopPaneActions() {
        }
    }

    protected static class ComboActions {
        public static final String HIDE = "hidePopup";
        public static final String DOWN = "selectNext";
        public static final String DOWN_2 = "selectNext2";
        public static final String TOGGLE = "togglePopup";
        public static final String TOGGLE_2 = "spacePopup";
        public static final String UP = "selectPrevious";
        public static final String UP_2 = "selectPrevious2";
        public static final String ENTER = "enterPressed";
        public static final String PAGE_DOWN = "pageDownPassThrough";
        public static final String PAGE_UP = "pageUpPassThrough";
        public static final String HOME = "homePassThrough";
        public static final String END = "endPassThrough";

        protected ComboActions() {
        }
    }
}

