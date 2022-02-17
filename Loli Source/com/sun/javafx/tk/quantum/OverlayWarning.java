/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.tk.quantum;

import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.tk.quantum.ViewScene;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class OverlayWarning
extends Group {
    private static final float PAD = 40.0f;
    private static final float RECTW = 600.0f;
    private static final float RECTH = 100.0f;
    private static final float ARC = 20.0f;
    private static final int FONTSIZE = 24;
    private ViewScene view;
    private SequentialTransition overlayTransition;
    private boolean warningTransition;
    private Text text = new Text();
    private Rectangle background;

    public OverlayWarning(ViewScene viewScene) {
        this.view = viewScene;
        this.createOverlayGroup();
        PauseTransition pauseTransition = new PauseTransition(Duration.millis(4000.0));
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000.0), this);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        this.overlayTransition = new SequentialTransition();
        this.overlayTransition.getChildren().add(pauseTransition);
        this.overlayTransition.getChildren().add(fadeTransition);
        this.overlayTransition.setOnFinished(actionEvent -> {
            this.warningTransition = false;
            this.view.getWindowStage().setWarning(null);
        });
    }

    protected ViewScene getView() {
        return this.view;
    }

    protected final void setView(ViewScene viewScene) {
        if (this.view != null) {
            this.view.getWindowStage().setWarning(null);
        }
        this.view = viewScene;
        this.view.entireSceneNeedsRepaint();
    }

    protected void warn(String string) {
        this.text.setText(string);
        this.warningTransition = true;
        this.overlayTransition.play();
    }

    protected void cancel() {
        if (this.overlayTransition != null && this.overlayTransition.getStatus() == Animation.Status.RUNNING) {
            this.overlayTransition.stop();
            this.warningTransition = false;
        }
        this.view.getWindowStage().setWarning(null);
    }

    protected boolean inWarningTransition() {
        return this.warningTransition;
    }

    private void createOverlayGroup() {
        Font font = new Font(Font.getDefault().getFamily(), 24.0);
        Rectangle2D rectangle2D = new Rectangle2D(0.0, 0.0, this.view.getSceneState().getScreenWidth(), this.view.getSceneState().getScreenHeight());
        String string = "-fx-effect: dropshadow(two-pass-box, rgba(0,0,0,0.75), 3, 0.0, 0, 2);";
        this.text.setStroke(Color.WHITE);
        this.text.setFill(Color.WHITE);
        this.text.setFont(font);
        this.text.setWrappingWidth(520.0);
        this.text.setStyle(string);
        this.text.setTextAlignment(TextAlignment.CENTER);
        this.background = this.createBackground(this.text, rectangle2D);
        this.getChildren().add(this.background);
        this.getChildren().add(this.text);
    }

    private Rectangle createBackground(Text text, Rectangle2D rectangle2D) {
        Rectangle rectangle = new Rectangle();
        double d2 = text.getLayoutBounds().getWidth();
        double d3 = text.getLayoutBounds().getHeight();
        double d4 = (rectangle2D.getWidth() - 600.0) / 2.0;
        double d5 = rectangle2D.getHeight() / 2.0;
        rectangle.setWidth(600.0);
        rectangle.setHeight(100.0);
        rectangle.setX(d4);
        rectangle.setY(d5 - 100.0);
        rectangle.setArcWidth(20.0);
        rectangle.setArcHeight(20.0);
        rectangle.setFill(Color.gray(0.0, 0.6));
        text.setX(d4 + (600.0 - d2) / 2.0);
        text.setY(d5 - 50.0 + (d3 - text.getBaselineOffset()) / 2.0);
        return rectangle;
    }

    @Override
    public void impl_updatePeer() {
        this.text.impl_updatePeer();
        this.background.impl_updatePeer();
        super.impl_updatePeer();
    }

    @Override
    protected void updateBounds() {
        super.updateBounds();
    }

    @Override
    protected void impl_markDirty(DirtyBits dirtyBits) {
        super.impl_markDirty(dirtyBits);
        this.view.synchroniseOverlayWarning();
    }
}

