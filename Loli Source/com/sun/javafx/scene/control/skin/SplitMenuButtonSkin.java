/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.SplitMenuButtonBehavior;
import com.sun.javafx.scene.control.skin.MenuButtonSkinBase;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.input.MouseEvent;

public class SplitMenuButtonSkin
extends MenuButtonSkinBase<SplitMenuButton, SplitMenuButtonBehavior> {
    public SplitMenuButtonSkin(SplitMenuButton splitMenuButton) {
        super(splitMenuButton, new SplitMenuButtonBehavior(splitMenuButton));
        this.behaveLikeButton = true;
        this.arrowButton.addEventHandler(MouseEvent.ANY, mouseEvent -> mouseEvent.consume());
        this.arrowButton.setOnMousePressed(mouseEvent -> {
            ((SplitMenuButtonBehavior)this.getBehavior()).mousePressed((MouseEvent)mouseEvent, false);
            mouseEvent.consume();
        });
        this.arrowButton.setOnMouseReleased(mouseEvent -> {
            ((SplitMenuButtonBehavior)this.getBehavior()).mouseReleased((MouseEvent)mouseEvent, false);
            mouseEvent.consume();
        });
        this.label.setLabelFor(splitMenuButton);
    }
}

