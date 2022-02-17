/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Accordion;
import javafx.scene.control.FocusModel;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

public class AccordionBehavior
extends BehaviorBase<Accordion> {
    private AccordionFocusModel focusModel;
    private static final String HOME = "Home";
    private static final String END = "End";
    private static final String PAGE_UP = "Page_Up";
    private static final String PAGE_DOWN = "Page_Down";
    private static final String CTRL_PAGE_UP = "Ctrl_Page_Up";
    private static final String CTRL_PAGE_DOWN = "Ctrl_Page_Down";
    private static final String CTRL_TAB = "Ctrl_Tab";
    private static final String CTRL_SHIFT_TAB = "Ctrl_Shift_Tab";
    protected static final List<KeyBinding> ACCORDION_BINDINGS = new ArrayList<KeyBinding>();

    public AccordionBehavior(Accordion accordion) {
        super(accordion, ACCORDION_BINDINGS);
        this.focusModel = new AccordionFocusModel(accordion);
    }

    @Override
    public void dispose() {
        this.focusModel.dispose();
        super.dispose();
    }

    @Override
    protected void callAction(String string) {
        boolean bl;
        Accordion accordion = (Accordion)this.getControl();
        boolean bl2 = bl = accordion.getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT;
        if ("TraverseLeft".equals(string) && !bl || "TraverseRight".equals(string) && bl || "TraverseUp".equals(string) || PAGE_UP.equals(string)) {
            if (this.focusModel.getFocusedIndex() != -1 && ((TitledPane)accordion.getPanes().get(this.focusModel.getFocusedIndex())).isFocused()) {
                this.focusModel.focusPrevious();
                int n2 = this.focusModel.getFocusedIndex();
                ((TitledPane)accordion.getPanes().get(n2)).requestFocus();
                if (PAGE_UP.equals(string)) {
                    ((TitledPane)accordion.getPanes().get(n2)).setExpanded(true);
                }
            }
        } else if ("TraverseRight".equals(string) && !bl || "TraverseLeft".equals(string) && bl || "TraverseDown".equals(string) || PAGE_DOWN.equals(string)) {
            if (this.focusModel.getFocusedIndex() != -1 && ((TitledPane)accordion.getPanes().get(this.focusModel.getFocusedIndex())).isFocused()) {
                this.focusModel.focusNext();
                int n3 = this.focusModel.getFocusedIndex();
                ((TitledPane)accordion.getPanes().get(n3)).requestFocus();
                if (PAGE_DOWN.equals(string)) {
                    ((TitledPane)accordion.getPanes().get(n3)).setExpanded(true);
                }
            }
        } else if (CTRL_TAB.equals(string) || CTRL_PAGE_DOWN.equals(string)) {
            this.focusModel.focusNext();
            if (this.focusModel.getFocusedIndex() != -1) {
                int n4 = this.focusModel.getFocusedIndex();
                ((TitledPane)accordion.getPanes().get(n4)).requestFocus();
                ((TitledPane)accordion.getPanes().get(n4)).setExpanded(true);
            }
        } else if (CTRL_SHIFT_TAB.equals(string) || CTRL_PAGE_UP.equals(string)) {
            this.focusModel.focusPrevious();
            if (this.focusModel.getFocusedIndex() != -1) {
                int n5 = this.focusModel.getFocusedIndex();
                ((TitledPane)accordion.getPanes().get(n5)).requestFocus();
                ((TitledPane)accordion.getPanes().get(n5)).setExpanded(true);
            }
        } else if (HOME.equals(string)) {
            if (this.focusModel.getFocusedIndex() != -1 && ((TitledPane)accordion.getPanes().get(this.focusModel.getFocusedIndex())).isFocused()) {
                TitledPane titledPane = (TitledPane)accordion.getPanes().get(0);
                titledPane.requestFocus();
                titledPane.setExpanded(!titledPane.isExpanded());
            }
        } else if (END.equals(string)) {
            if (this.focusModel.getFocusedIndex() != -1 && ((TitledPane)accordion.getPanes().get(this.focusModel.getFocusedIndex())).isFocused()) {
                TitledPane titledPane = (TitledPane)accordion.getPanes().get(accordion.getPanes().size() - 1);
                titledPane.requestFocus();
                titledPane.setExpanded(!titledPane.isExpanded());
            }
        } else {
            super.callAction(string);
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        Accordion accordion = (Accordion)this.getControl();
        if (accordion.getPanes().size() > 0) {
            TitledPane titledPane = (TitledPane)accordion.getPanes().get(accordion.getPanes().size() - 1);
            titledPane.requestFocus();
        } else {
            accordion.requestFocus();
        }
    }

    static {
        ACCORDION_BINDINGS.add(new KeyBinding(KeyCode.UP, "TraverseUp"));
        ACCORDION_BINDINGS.add(new KeyBinding(KeyCode.DOWN, "TraverseDown"));
        ACCORDION_BINDINGS.add(new KeyBinding(KeyCode.LEFT, "TraverseLeft"));
        ACCORDION_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, "TraverseRight"));
        ACCORDION_BINDINGS.add(new KeyBinding(KeyCode.HOME, HOME));
        ACCORDION_BINDINGS.add(new KeyBinding(KeyCode.END, END));
        ACCORDION_BINDINGS.add(new KeyBinding(KeyCode.PAGE_UP, PAGE_UP));
        ACCORDION_BINDINGS.add(new KeyBinding(KeyCode.PAGE_DOWN, PAGE_DOWN));
        ACCORDION_BINDINGS.add(new KeyBinding(KeyCode.PAGE_UP, CTRL_PAGE_UP).ctrl());
        ACCORDION_BINDINGS.add(new KeyBinding(KeyCode.PAGE_DOWN, CTRL_PAGE_DOWN).ctrl());
        ACCORDION_BINDINGS.add(new KeyBinding(KeyCode.TAB, CTRL_TAB).ctrl());
        ACCORDION_BINDINGS.add(new KeyBinding(KeyCode.TAB, CTRL_SHIFT_TAB).shift().ctrl());
    }

    static class AccordionFocusModel
    extends FocusModel<TitledPane> {
        private final Accordion accordion;
        private final ChangeListener<Boolean> focusListener = new ChangeListener<Boolean>(){

            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean bl, Boolean bl2) {
                if (bl2.booleanValue()) {
                    if (accordion.getExpandedPane() != null) {
                        accordion.getExpandedPane().requestFocus();
                    } else if (!accordion.getPanes().isEmpty()) {
                        ((TitledPane)accordion.getPanes().get(0)).requestFocus();
                    }
                }
            }
        };
        private final ChangeListener<Boolean> paneFocusListener = new ChangeListener<Boolean>(){

            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean bl, Boolean bl2) {
                if (bl2.booleanValue()) {
                    ReadOnlyBooleanProperty readOnlyBooleanProperty = (ReadOnlyBooleanProperty)observableValue;
                    TitledPane titledPane = (TitledPane)readOnlyBooleanProperty.getBean();
                    this.focus(accordion.getPanes().indexOf(titledPane));
                }
            }
        };
        private final ListChangeListener<TitledPane> panesListener = change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (TitledPane titledPane : change.getAddedSubList()) {
                        titledPane.focusedProperty().addListener(this.paneFocusListener);
                    }
                    continue;
                }
                if (!change.wasRemoved()) continue;
                for (TitledPane titledPane : change.getAddedSubList()) {
                    titledPane.focusedProperty().removeListener(this.paneFocusListener);
                }
            }
        };

        public AccordionFocusModel(Accordion accordion) {
            if (accordion == null) {
                throw new IllegalArgumentException("Accordion can not be null");
            }
            this.accordion = accordion;
            this.accordion.focusedProperty().addListener(this.focusListener);
            this.accordion.getPanes().addListener(this.panesListener);
            for (TitledPane titledPane : this.accordion.getPanes()) {
                titledPane.focusedProperty().addListener(this.paneFocusListener);
            }
        }

        void dispose() {
            this.accordion.focusedProperty().removeListener(this.focusListener);
            this.accordion.getPanes().removeListener(this.panesListener);
            for (TitledPane titledPane : this.accordion.getPanes()) {
                titledPane.focusedProperty().removeListener(this.paneFocusListener);
            }
        }

        @Override
        protected int getItemCount() {
            ObservableList<TitledPane> observableList = this.accordion.getPanes();
            return observableList == null ? 0 : observableList.size();
        }

        @Override
        protected TitledPane getModelItem(int n2) {
            ObservableList<TitledPane> observableList = this.accordion.getPanes();
            if (observableList == null) {
                return null;
            }
            if (n2 < 0) {
                return null;
            }
            return (TitledPane)observableList.get(n2 % observableList.size());
        }

        @Override
        public void focusPrevious() {
            if (this.getFocusedIndex() <= 0) {
                this.focus(this.accordion.getPanes().size() - 1);
            } else {
                this.focus((this.getFocusedIndex() - 1) % this.accordion.getPanes().size());
            }
        }

        @Override
        public void focusNext() {
            if (this.getFocusedIndex() == -1) {
                this.focus(0);
            } else {
                this.focus((this.getFocusedIndex() + 1) % this.accordion.getPanes().size());
            }
        }
    }
}

