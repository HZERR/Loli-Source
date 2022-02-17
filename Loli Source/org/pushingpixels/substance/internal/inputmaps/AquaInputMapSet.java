/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.inputmaps;

import org.pushingpixels.substance.api.inputmaps.SubstanceInputMap;
import org.pushingpixels.substance.internal.inputmaps.BaseInputMapSet;

public class AquaInputMapSet
extends BaseInputMapSet {
    @Override
    public SubstanceInputMap getComboBoxAncestorInputMap() {
        return super.getComboBoxAncestorInputMap();
    }

    protected SubstanceInputMap getSingleLineTextComponentFocusInputMap() {
        SubstanceInputMap result = new SubstanceInputMap();
        result.put("meta C", "copy-to-clipboard");
        result.put("meta V", "paste-from-clipboard");
        result.put("meta X", "cut-to-clipboard");
        result.put("COPY", "copy-to-clipboard");
        result.put("PASTE", "paste-from-clipboard");
        result.put("CUT", "cut-to-clipboard");
        result.put("shift LEFT", "selection-backward");
        result.put("shift KP_LEFT", "selection-backward");
        result.put("shift RIGHT", "selection-forward");
        result.put("shift KP_RIGHT", "selection-forward");
        result.put("meta LEFT", "caret-begin-line");
        result.put("meta KP_LEFT", "caret-begin-line");
        result.put("ctrl A", "caret-begin-line");
        result.put("UP", "caret-begin-line");
        result.put("KP_UP", "caret-begin-line");
        result.put("meta RIGHT", "caret-end-line");
        result.put("meta KP_RIGHT", "caret-end-line");
        result.put("ctrl E", "caret-end-line");
        result.put("DOWN", "caret-end-line");
        result.put("KP_DOWN", "caret-end-line");
        result.put("meta shift LEFT", "selection-begin-line");
        result.put("meta shift KP_LEFT", "selection-begin-line");
        result.put("shift UP", "selection-begin-line");
        result.put("shift KP_UP", "selection-begin-line");
        result.put("meta shift RIGHT", "selection-end-line");
        result.put("meta shift KP_RIGHT", "selection-end-line");
        result.put("shift DOWN", "selection-end-line");
        result.put("shift KP_DOWN", "selection-end-line");
        result.put("meta A", "select-all");
        result.put("HOME", "caret-begin");
        result.put("ctrl P", "caret-begin");
        result.put("meta UP", "caret-begin");
        result.put("meta KP_UP", "caret-begin");
        result.put("END", "caret-end");
        result.put("ctrl N", "caret-end");
        result.put("ctrl V", "caret-end");
        result.put("meta DOWN", "caret-end");
        result.put("meta KP_DOWN", "caret-end");
        result.put("shift meta KP_UP", "selection-begin");
        result.put("shift UP", "selection-begin");
        result.put("shift HOME", "selection-begin");
        result.put("shift meta DOWN", "selection-end");
        result.put("shift meta KP_DOWN", "selection-end");
        result.put("shift END", "selection-end");
        result.put("BACK_SPACE", "delete-previous");
        result.put("ctrl H", "delete-previous");
        result.put("DELETE", "delete-next");
        result.put("ctrl D", "delete-next");
        result.put("alt BACK_SPACE", "delete-previous-word");
        result.put("ctrl W", "delete-previous-word");
        result.put("alt DELETE", "delete-next-word");
        result.put("RIGHT", "caret-forward");
        result.put("KP_RIGHT", "caret-forward");
        result.put("ctrl F", "caret-forward");
        result.put("LEFT", "caret-backward");
        result.put("KP_LEFT", "caret-backward");
        result.put("ctrl B", "caret-backward");
        result.put("alt RIGHT", "caret-next-word");
        result.put("alt KP_RIGHT", "caret-next-word");
        result.put("alt LEFT", "caret-previous-word");
        result.put("alt KP_LEFT", "caret-previous-word");
        result.put("shift alt RIGHT", "selection-next-word");
        result.put("shift alt KP_RIGHT", "selection-next-word");
        result.put("shift alt LEFT", "selection-previous-word");
        result.put("shift alt KP_LEFT", "selection-previous-word");
        result.put("shift meta PAGE_UP", "selection-page-left");
        result.put("shift meta PAGE_DOWN", "selection-page-right");
        result.put("shift meta PAGE_UP", "selection-page-up");
        result.put("shift meta PAGE_DOWN", "selection-page-down");
        result.put("ENTER", "notify-field-accept");
        result.put("meta BACK_SLASH", "unselect");
        result.put("control shift O", "toggle-componentOrientation");
        result.put("PAGE_DOWN", "page-down");
        result.put("PAGE_UP", "page-up");
        return result;
    }

    @Override
    protected SubstanceInputMap getMultilineTextComponentFocusInputMap() {
        SubstanceInputMap result = new SubstanceInputMap();
        result.put("meta C", "copy-to-clipboard");
        result.put("meta V", "paste-from-clipboard");
        result.put("meta X", "cut-to-clipboard");
        result.put("COPY", "copy-to-clipboard");
        result.put("PASTE", "paste-from-clipboard");
        result.put("CUT", "cut-to-clipboard");
        result.put("shift LEFT", "selection-backward");
        result.put("shift KP_LEFT", "selection-backward");
        result.put("shift RIGHT", "selection-forward");
        result.put("shift KP_RIGHT", "selection-forward");
        result.put("alt LEFT", "caret-previous-word");
        result.put("alt KP_LEFT", "caret-previous-word");
        result.put("alt RIGHT", "caret-next-word");
        result.put("alt KP_RIGHT", "caret-next-word");
        result.put("alt shift LEFT", "selection-previous-word");
        result.put("alt shift KP_LEFT", "selection-previous-word");
        result.put("alt shift RIGHT", "selection-next-word");
        result.put("alt shift KP_RIGHT", "selection-next-word");
        result.put("meta A", "select-all");
        result.put("ctrl A", "caret-begin-line");
        result.put("meta KP_LEFT", "caret-begin-line");
        result.put("meta LEFT", "caret-begin-line");
        result.put("ctrl E", "caret-end-line");
        result.put("meta KP_RIGHT", "caret-end-line");
        result.put("meta RIGHT", "caret-end-line");
        result.put("shift meta KP_LEFT", "selection-begin-line");
        result.put("shift meta LEFT", "selection-begin-line");
        result.put("shift meta KP_RIGHT", "selection-end-line");
        result.put("shift meta RIGHT", "selection-end-line");
        result.put("ctrl P", "caret-up");
        result.put("ctrl N", "caret-down");
        result.put("UP", "caret-up");
        result.put("KP_UP", "caret-up");
        result.put("DOWN", "caret-down");
        result.put("KP_DOWN", "caret-down");
        result.put("PAGE_UP", "page-up");
        result.put("PAGE_DOWN", "page-down");
        result.put("ctrl V", "page-down");
        result.put("shift PAGE_UP", "selection-page-up");
        result.put("shift PAGE_DOWN", "selection-page-down");
        result.put("meta shift PAGE_UP", "selection-page-left");
        result.put("meta shift PAGE_DOWN", "selection-page-right");
        result.put("shift UP", "selection-up");
        result.put("shift KP_UP", "selection-up");
        result.put("shift DOWN", "selection-down");
        result.put("shift KP_DOWN", "selection-down");
        result.put("meta shift KP_UP", "selection-begin");
        result.put("meta shift UP", "selection-begin");
        result.put("shift HOME", "selection-begin");
        result.put("meta shift KP_DOWN", "selection-end");
        result.put("meta shift DOWN", "selection-end");
        result.put("shift END", "selection-end");
        result.put("shift alt KP_UP", "selection-begin-paragraph");
        result.put("shift alt UP", "selection-begin-paragraph");
        result.put("shift alt KP_DOWN", "selection-end-paragraph");
        result.put("shift alt DOWN", "selection-end-paragraph");
        result.put("ENTER", "insert-break");
        result.put("BACK_SPACE", "delete-previous");
        result.put("ctrl H", "delete-previous");
        result.put("DELETE", "delete-next");
        result.put("ctrl D", "delete-next");
        result.put("alt DELETE", "delete-next-word");
        result.put("alt BACK_SPACE", "delete-previous-word");
        result.put("ctrl W", "delete-previous-word");
        result.put("RIGHT", "caret-forward");
        result.put("KP_RIGHT", "caret-forward");
        result.put("ctrl F", "caret-forward");
        result.put("LEFT", "caret-backward");
        result.put("KP_LEFT", "caret-backward");
        result.put("ctrl B", "caret-backward");
        result.put("TAB", "insert-tab");
        result.put("meta BACK_SLASH", "unselect");
        result.put("meta KP_UP", "caret-begin");
        result.put("meta UP", "caret-begin");
        result.put("HOME", "caret-begin");
        result.put("meta KP_DOWN", "caret-end");
        result.put("meta DOWN", "caret-end");
        result.put("END", "caret-end");
        result.put("meta T", "next-link-action");
        result.put("meta shift T", "previous-link-action");
        result.put("meta SPACE", "activate-link-action");
        result.put("control shift O", "toggle-componentOrientation");
        return result;
    }

    @Override
    public SubstanceInputMap getFileChooserAncestorInputMap() {
        SubstanceInputMap result = new SubstanceInputMap();
        result.put("ESCAPE", "cancelSelection");
        result.put("F5", "refresh");
        return result;
    }

    @Override
    public SubstanceInputMap getListFocusInputMap() {
        SubstanceInputMap result = new SubstanceInputMap();
        result.put("meta C", COPY);
        result.put("meta V", PASTE);
        result.put("meta X", CUT);
        result.put("COPY", COPY);
        result.put("PASTE", PASTE);
        result.put("CUT", CUT);
        result.put("UP", "selectPreviousRow");
        result.put("KP_UP", "selectPreviousRow");
        result.put("shift UP", "selectPreviousRowExtendSelection");
        result.put("shift KP_UP", "selectPreviousRowExtendSelection");
        result.put("DOWN", "selectNextRow");
        result.put("KP_DOWN", "selectNextRow");
        result.put("shift DOWN", "selectNextRowExtendSelection");
        result.put("shift KP_DOWN", "selectNextRowExtendSelection");
        result.put("LEFT", "selectPreviousColumn");
        result.put("KP_LEFT", "selectPreviousColumn");
        result.put("shift LEFT", "selectPreviousColumnExtendSelection");
        result.put("shift KP_LEFT", "selectPreviousColumnExtendSelection");
        result.put("RIGHT", "selectNextColumn");
        result.put("KP_RIGHT", "selectNextColumn");
        result.put("shift RIGHT", "selectNextColumnExtendSelection");
        result.put("shift KP_RIGHT", "selectNextColumnExtendSelection");
        result.put("shift HOME", "selectFirstRowExtendSelection");
        result.put("shift END", "selectLastRowExtendSelection");
        result.put("shift PAGE_UP", "scrollUpExtendSelection");
        result.put("shift PAGE_DOWN", "scrollDownExtendSelection");
        result.put("meta A", "selectAll");
        return result;
    }

    @Override
    public SubstanceInputMap getPasswordFieldFocusInputMap() {
        return this.getSingleLineTextComponentFocusInputMap();
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
        result.put("HOME", "scrollHome");
        result.put("END", "scrollEnd");
        return result;
    }

    @Override
    public SubstanceInputMap getSliderFocusInputMap() {
        SubstanceInputMap result = super.getSliderFocusInputMap();
        result.remove("ctrl PAGE_DOWN");
        result.remove("ctrl PAGE_UP");
        return result;
    }

    @Override
    public SubstanceInputMap getTableAncestorInputMap() {
        SubstanceInputMap result = new SubstanceInputMap();
        result.put("meta C", COPY);
        result.put("meta V", PASTE);
        result.put("meta X", CUT);
        result.put("COPY", COPY);
        result.put("PASTE", PASTE);
        result.put("CUT", CUT);
        result.put("RIGHT", "selectNextColumn");
        result.put("KP_RIGHT", "selectNextColumn");
        result.put("shift RIGHT", "selectNextColumnExtendSelection");
        result.put("shift KP_RIGHT", "selectNextColumnExtendSelection");
        result.put("LEFT", "selectPreviousColumn");
        result.put("KP_LEFT", "selectPreviousColumn");
        result.put("shift LEFT", "selectPreviousColumnExtendSelection");
        result.put("shift KP_LEFT", "selectPreviousColumnExtendSelection");
        result.put("DOWN", "selectNextRow");
        result.put("KP_DOWN", "selectNextRow");
        result.put("shift DOWN", "selectNextRowExtendSelection");
        result.put("shift KP_DOWN", "selectNextRowExtendSelection");
        result.put("UP", "selectPreviousRow");
        result.put("KP_UP", "selectPreviousRow");
        result.put("shift UP", "selectPreviousRowExtendSelection");
        result.put("shift KP_UP", "selectPreviousRowExtendSelection");
        result.put("HOME", "selectFirstColumn");
        result.put("shift HOME", "selectFirstColumnExtendSelection");
        result.put("END", "selectLastColumn");
        result.put("shift END", "selectLastColumnExtendSelection");
        result.put("PAGE_UP", "scrollUpChangeSelection");
        result.put("shift PAGE_UP", "scrollUpExtendSelection");
        result.put("PAGE_DOWN", "scrollDownChangeSelection");
        result.put("shift PAGE_DOWN", "scrollDownExtendSelection");
        result.put("TAB", "selectNextColumnCell");
        result.put("shift TAB", "selectPreviousColumnCell");
        result.put("ENTER", "selectNextRowCell");
        result.put("shift ENTER", "selectPreviousRowCell");
        result.put("meta A", "selectAll");
        result.put("alt TAB", "focusHeader");
        result.put("shift alt TAB", "focusHeader");
        return result;
    }

    @Override
    public SubstanceInputMap getTextFieldFocusInputMap() {
        return this.getSingleLineTextComponentFocusInputMap();
    }

    @Override
    public SubstanceInputMap getTreeFocusInputMap() {
        SubstanceInputMap result = new SubstanceInputMap();
        result.put("meta C", COPY);
        result.put("meta V", PASTE);
        result.put("meta X", CUT);
        result.put("COPY", COPY);
        result.put("PASTE", PASTE);
        result.put("CUT", CUT);
        result.put("UP", "selectPrevious");
        result.put("KP_UP", "selectPrevious");
        result.put("shift UP", "selectPreviousExtendSelection");
        result.put("shift KP_UP", "selectPreviousExtendSelection");
        result.put("DOWN", "selectNext");
        result.put("KP_DOWN", "selectNext");
        result.put("shift DOWN", "selectNextExtendSelection");
        result.put("shift KP_DOWN", "selectNextExtendSelection");
        result.put("ctrl A", "selectAll");
        result.put("RIGHT", "expand");
        result.put("KP_RIGHT", "expand");
        result.put("ctrl RIGHT", "expand");
        result.put("ctrl KP_RIGHT", "expand");
        result.put("shift RIGHT", "expand");
        result.put("shift KP_RIGHT", "expand");
        result.put("LEFT", "collapse");
        result.put("KP_LEFT", "collapse");
        result.put("ctrl LEFT", "collapse");
        result.put("ctrl KP_LEFT", "collapse");
        result.put("shift LEFT", "collapse");
        result.put("shift KP_LEFT", "collapse");
        return result;
    }
}

