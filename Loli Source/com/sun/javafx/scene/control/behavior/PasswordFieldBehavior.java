/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.behavior.TextFieldBehavior;
import com.sun.javafx.scene.text.HitInfo;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class PasswordFieldBehavior
extends TextFieldBehavior {
    public PasswordFieldBehavior(PasswordField passwordField) {
        super(passwordField);
    }

    @Override
    protected void deletePreviousWord() {
    }

    @Override
    protected void deleteNextWord() {
    }

    @Override
    protected void selectPreviousWord() {
    }

    @Override
    protected void selectNextWord() {
    }

    @Override
    protected void previousWord() {
    }

    @Override
    protected void nextWord() {
    }

    @Override
    protected void selectWord() {
        ((TextField)this.getControl()).selectAll();
    }

    @Override
    protected void mouseDoubleClick(HitInfo hitInfo) {
        ((TextField)this.getControl()).selectAll();
    }
}

