/*
 * Decompiled with CFR 0.150.
 */
package com.sun.deploy.uitoolkit.impl.fx.ui;

import com.sun.deploy.uitoolkit.impl.fx.FXPreloader;
import com.sun.deploy.uitoolkit.impl.fx.ui.resources.ResourceManager;
import com.sun.javafx.css.StyleManager;
import com.sun.javafx.css.Stylesheet;
import com.sun.javafx.css.converters.StringConverter;
import com.sun.javafx.css.parser.CSSParser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.animation.FadeTransition;
import javafx.application.HostServices;
import javafx.application.Preloader;
import javafx.beans.property.StringProperty;
import javafx.css.CssMetaData;
import javafx.css.StyleOrigin;
import javafx.css.StyleableProperty;
import javafx.css.StyleableStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class FXDefaultPreloader
extends Preloader {
    static final double FADE_DURATION = 2000.0;
    Stage stage;
    FXPreloaderPane pane;
    double currentWidth = 0.0;
    double currentHeight = 0.0;
    boolean isEmbedded;

    @Override
    public void init() {
        this.pane = new FXPreloaderPane();
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        this.stage.setTitle("JavaFX Application Preview");
        this.isEmbedded = stage.getWidth() >= 0.0 && stage.getHeight() >= 0.0;
    }

    private void showIfNeeded() {
        if (!this.stage.isShowing()) {
            this.stage.setScene(new FXPreloaderScene(this.pane));
            this.stage.show();
            ((FXPreloaderScene)this.stage.getScene()).loadStylesheets();
            this.stage.toFront();
            FXPreloader.hideSplash();
        }
    }

    @Override
    public void stop() {
    }

    @Override
    public void handleStateChangeNotification(Preloader.StateChangeNotification stateChangeNotification) {
        switch (stateChangeNotification.getType()) {
            case BEFORE_START: {
                if (this.stage.isShowing()) {
                    if (this.isEmbedded) {
                        FadeTransition fadeTransition = new FadeTransition(Duration.millis(2000.0), this.pane);
                        fadeTransition.setFromValue(1.0);
                        fadeTransition.setToValue(0.0);
                        fadeTransition.setOnFinished(new FadeOutFinisher(this.stage));
                        fadeTransition.play();
                        break;
                    }
                    this.stage.hide();
                    break;
                }
                FXPreloader.hideSplash();
                break;
            }
        }
    }

    @Override
    public boolean handleErrorNotification(Preloader.ErrorNotification errorNotification) {
        if (this.stage != null && this.stage.isShowing() && !this.isEmbedded) {
            this.stage.hide();
        } else if (this.isEmbedded) {
            FXPreloader.hideSplash();
        }
        return false;
    }

    @Override
    public void handleProgressNotification(Preloader.ProgressNotification progressNotification) {
        if (progressNotification.getProgress() != 1.0) {
            this.showIfNeeded();
        }
        if (this.stage.isShowing()) {
            this.pane.setProgress(progressNotification.getProgress());
        }
    }

    public List<CssMetaData> impl_CSS_STYLEABLES() {
        return CSSProperties.STYLEABLES;
    }

    private static class CSSProperties {
        private static CssMetaData<FXPreloaderPane, String> PRELOADER_TEXT = new CssMetaData<FXPreloaderPane, String>("-fx-preloader-text", StringConverter.getInstance()){

            @Override
            public boolean isSettable(FXPreloaderPane fXPreloaderPane) {
                return true;
            }

            @Override
            public StyleableProperty<String> getStyleableProperty(FXPreloaderPane fXPreloaderPane) {
                return (StyleableProperty)((Object)fXPreloaderPane.preloaderText);
            }
        };
        private static CssMetaData<FXPreloaderPane, String> PRELOADER_GRAPHIC = new CssMetaData<FXPreloaderPane, String>("-fx-preloader-graphic", StringConverter.getInstance()){

            @Override
            public boolean isSettable(FXPreloaderPane fXPreloaderPane) {
                return true;
            }

            @Override
            public StyleableProperty<String> getStyleableProperty(FXPreloaderPane fXPreloaderPane) {
                return (StyleableProperty)((Object)fXPreloaderPane.preloaderGraphicUrl);
            }
        };
        private static final List<CssMetaData> STYLEABLES;

        private CSSProperties() {
        }

        static {
            ArrayList arrayList = new ArrayList();
            Collections.addAll(arrayList, PRELOADER_TEXT, PRELOADER_GRAPHIC);
            STYLEABLES = Collections.unmodifiableList(arrayList);
        }
    }

    class FXPreloaderPane
    extends Pane {
        ProgressBar progressBar;
        Label status;
        Label percent;
        Label graphic;
        private StringProperty preloaderText = new StyleableStringProperty(){

            @Override
            public Object getBean() {
                return FXPreloaderPane.this;
            }

            @Override
            public String getName() {
                return "preloaderText";
            }

            @Override
            public CssMetaData getCssMetaData() {
                return CSSProperties.PRELOADER_TEXT;
            }
        };
        private StringProperty preloaderGraphicUrl = new StyleableStringProperty(){

            @Override
            protected void invalidated() {
                if (this.getValue() != null) {
                    if (FXPreloaderPane.this.graphic == null) {
                        FXPreloaderPane.this.graphic = new Label();
                        FXPreloaderPane.this.getChildren().add(FXPreloaderPane.this.graphic);
                    }
                    FXPreloaderPane.this.graphic.setGraphic(new ImageView(new Image(this.getValue())));
                }
            }

            @Override
            public Object getBean() {
                return FXPreloaderPane.this;
            }

            @Override
            public String getName() {
                return "preloaderGraphicUrl";
            }

            @Override
            public CssMetaData getCssMetaData() {
                return CSSProperties.PRELOADER_GRAPHIC;
            }
        };

        FXPreloaderPane() {
            this.getStyleClass().setAll("default-preloader");
            String string = FXDefaultPreloader.this.getParameters().getNamed().get("javafx.default.preloader.style");
            if (string != null) {
                this.setStyle(string);
            }
            this.progressBar = new ProgressBar();
            this.status = new Label(ResourceManager.getMessage("preloader.loading"));
            this.status.setId("preloader-status-label");
            this.percent = new Label("");
            this.percent.setId("preloader-percent-label");
            this.getChildren().addAll(this.progressBar, this.status, this.percent);
        }

        @Override
        public void layoutChildren() {
            if (FXDefaultPreloader.this.currentWidth == this.getWidth() && FXDefaultPreloader.this.currentHeight == this.getHeight()) {
                return;
            }
            FXDefaultPreloader.this.currentWidth = this.getWidth();
            FXDefaultPreloader.this.currentHeight = this.getHeight();
            this.setPrefHeight(FXDefaultPreloader.this.currentHeight);
            this.setPrefWidth(FXDefaultPreloader.this.currentWidth);
            if (FXDefaultPreloader.this.currentWidth > 40.0 && FXDefaultPreloader.this.currentHeight > 21.0) {
                this.percent.autosize();
                if (FXDefaultPreloader.this.currentWidth < 100.0 || FXDefaultPreloader.this.currentHeight < 100.0) {
                    this.graphic.setVisible(false);
                    this.progressBar.setVisible(false);
                    this.status.setVisible(false);
                    this.percent.relocate((FXDefaultPreloader.this.currentWidth - this.percent.getWidth()) / 2.0, (FXDefaultPreloader.this.currentHeight - this.percent.getHeight()) / 2.0);
                } else {
                    if (this.graphic != null) {
                        this.graphic.setVisible(true);
                        this.graphic.autosize();
                        this.graphic.relocate((FXDefaultPreloader.this.currentWidth - this.graphic.getWidth()) / 2.0, (FXDefaultPreloader.this.currentHeight / 2.0 - this.graphic.getHeight()) / 2.0);
                    }
                    this.status.setVisible(true);
                    this.status.autosize();
                    float f2 = FXDefaultPreloader.this.currentWidth < 240.0 ? 0.75f : 0.65f;
                    this.progressBar.setVisible(true);
                    this.progressBar.setPrefWidth(FXDefaultPreloader.this.currentWidth * (double)f2);
                    this.progressBar.resize(this.progressBar.prefWidth(-1.0), this.progressBar.prefHeight(-1.0));
                    this.progressBar.relocate((FXDefaultPreloader.this.currentWidth - this.progressBar.getWidth()) / 2.0, FXDefaultPreloader.this.currentHeight / 2.0 - this.progressBar.getHeight());
                    this.status.relocate(this.progressBar.getLayoutX(), this.progressBar.getLayoutY() + this.progressBar.getHeight() + 4.0);
                    this.percent.relocate(this.progressBar.getLayoutX() + this.progressBar.getWidth() - this.percent.getWidth(), this.progressBar.getLayoutY() - this.percent.getHeight() - 4.0);
                }
            }
        }

        void setProgress(double d2) {
            this.progressBar.setProgress(d2);
            this.percent.setText(String.format("%.0f%%", d2 * 100.0));
            this.percent.autosize();
            this.percent.setLayoutX(this.progressBar.getLayoutX() + this.progressBar.getPrefWidth() - this.percent.getWidth());
        }
    }

    private class FXPreloaderScene
    extends Scene {
        FXPreloaderScene(Parent parent) {
            super(parent, 600.0, 400.0);
            Scene scene = new Scene(new Pane(), 0.0, 0.0);
            this.setRoot(parent);
            this.getStylesheets().addAll(FXDefaultPreloader.class.getResource("deploydialogs.css").toExternalForm());
        }

        private void loadStylesheets() {
            String string = FXDefaultPreloader.this.getParameters().getNamed().get("javafx.default.preloader.stylesheet");
            if (string != null) {
                HostServices hostServices = FXDefaultPreloader.this.getHostServices();
                String string2 = hostServices.getDocumentBase();
                if (string.matches(".*\\.[bc]ss$")) {
                    String string3 = string.startsWith("jar:") ? "jar:" + hostServices.resolveURI(string2, string.substring(4)) : hostServices.resolveURI(string2, string);
                    this.getStylesheets().add(string3);
                } else {
                    try {
                        Stylesheet stylesheet = CSSParser.getInstance().parse(string2, string);
                        StyleManager.getInstance().addUserAgentStylesheet((Scene)this, stylesheet);
                        stylesheet.setOrigin(StyleOrigin.AUTHOR);
                    }
                    catch (IOException iOException) {
                        iOException.printStackTrace();
                    }
                }
            }
        }
    }

    private class FadeOutFinisher
    implements EventHandler<ActionEvent> {
        Stage stage;

        FadeOutFinisher(Stage stage) {
            this.stage = stage;
        }

        @Override
        public void handle(ActionEvent actionEvent) {
            if (this.stage.isShowing()) {
                this.stage.hide();
            }
        }
    }
}

