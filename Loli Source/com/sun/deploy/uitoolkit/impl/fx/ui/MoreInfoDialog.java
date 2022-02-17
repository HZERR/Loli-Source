/*
 * Decompiled with CFR 0.150.
 */
package com.sun.deploy.uitoolkit.impl.fx.ui;

import com.sun.deploy.uitoolkit.impl.fx.ui.CertificateDialog;
import com.sun.deploy.uitoolkit.impl.fx.ui.FXDialog;
import com.sun.deploy.uitoolkit.impl.fx.ui.UITextArea;
import com.sun.deploy.uitoolkit.impl.fx.ui.resources.ResourceManager;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.cert.Certificate;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

class MoreInfoDialog
extends FXDialog {
    private Hyperlink details;
    private String[] alerts;
    private String[] infos;
    private int securityInfoCount;
    private Certificate[] certs;
    private int start;
    private int end;
    private boolean sandboxApp = false;
    private final String WARNING_ICON = "warning16.image";
    private final String INFO_ICON = "info16.image";
    private final int VERTICAL_STRUT = 18;
    private final int HORIZONTAL_STRUT = 12;
    private final int TEXT_WIDTH = 326;

    MoreInfoDialog(Stage stage, String[] arrstring, String[] arrstring2, int n2, Certificate[] arrcertificate, int n3, int n4, boolean bl) {
        super(ResourceManager.getMessage("security.more.info.title"), stage, true);
        this.alerts = arrstring;
        this.infos = arrstring2;
        this.securityInfoCount = n2;
        this.certs = arrcertificate;
        this.start = n3;
        this.end = n4;
        this.sandboxApp = bl;
        this.initComponents(null, null);
        this.setResizable(false);
    }

    MoreInfoDialog(Stage stage, Pane pane, Throwable throwable, Certificate[] arrcertificate) {
        super(ResourceManager.getMessage("security.more.info.title"));
        this.certs = arrcertificate;
        this.start = 0;
        this.end = arrcertificate == null ? 0 : arrcertificate.length;
        this.initComponents(pane, throwable);
    }

    private void initComponents(Pane pane, Throwable throwable) {
        VBox vBox = new VBox();
        vBox.setId("more-info-dialog");
        if (pane != null) {
            VBox.setVgrow(pane, Priority.ALWAYS);
            vBox.getChildren().add(pane);
        } else if (throwable != null) {
            BorderPane borderPane = new BorderPane();
            Label label = new Label(ResourceManager.getString("exception.details.label"));
            borderPane.setLeft(label);
            vBox.getChildren().add(borderPane);
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            throwable.printStackTrace(printWriter);
            TextArea textArea = new TextArea(stringWriter.toString());
            textArea.setEditable(false);
            textArea.setWrapText(true);
            textArea.setPrefWidth(480.0);
            textArea.setPrefHeight(240.0);
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setContent(textArea);
            scrollPane.setFitToWidth(true);
            VBox.setVgrow(scrollPane, Priority.ALWAYS);
            vBox.getChildren().add(scrollPane);
            if (this.certs != null) {
                vBox.getChildren().add(this.getLinkPanel());
            }
        } else {
            Pane pane2 = this.getSecurityPanel();
            if (pane2.getChildren().size() > 0) {
                VBox.setVgrow(pane2, Priority.ALWAYS);
                vBox.getChildren().add(pane2);
            }
            vBox.getChildren().add(this.getIntegrationPanel());
        }
        vBox.getChildren().add(this.getBtnPanel());
        this.setContentPane(vBox);
    }

    private Pane getSecurityPanel() {
        int n2;
        VBox vBox = new VBox();
        boolean bl = this.certs == null;
        int n3 = bl || this.alerts == null ? 0 : 1;
        int n4 = n2 = this.alerts == null ? 0 : this.alerts.length;
        if (n2 > n3) {
            vBox.getChildren().add(this.blockPanel("warning16.image", this.alerts, n3, n2));
        }
        if ((n2 = this.securityInfoCount) > n3) {
            vBox.getChildren().add(this.blockPanel("info16.image", this.infos, n3, n2));
        }
        if (this.certs != null) {
            vBox.getChildren().add(this.getLinkPanel());
        }
        return vBox;
    }

    private Pane getLinkPanel() {
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(8.0, 0.0, 0.0, 0.0));
        hBox.setAlignment(Pos.TOP_RIGHT);
        String string = this.sandboxApp ? "sandbox.security.more.info.details" : "security.more.info.details";
        this.details = new Hyperlink(ResourceManager.getMessage(string));
        this.details.setMnemonicParsing(true);
        this.details.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent actionEvent) {
                MoreInfoDialog.this.showCertDetails();
            }
        });
        hBox.getChildren().add(this.details);
        return hBox;
    }

    private Pane getIntegrationPanel() {
        int n2 = this.securityInfoCount;
        int n3 = this.infos == null ? 0 : this.infos.length;
        return this.blockPanel("info16.image", this.infos, n2, n3);
    }

    private Pane getBtnPanel() {
        HBox hBox = new HBox();
        hBox.setId("more-info-dialog-button-panel");
        Button button = new Button(ResourceManager.getMessage("common.close_btn"));
        button.setCancelButton(true);
        button.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent actionEvent) {
                MoreInfoDialog.this.dismissAction();
            }
        });
        button.setDefaultButton(true);
        hBox.getChildren().add(button);
        return hBox;
    }

    private Pane blockPanel(String string, String[] arrstring, int n2, int n3) {
        VBox vBox = new VBox(5.0);
        if (arrstring != null) {
            for (int i2 = n2; i2 < n3; ++i2) {
                HBox hBox = new HBox(12.0);
                hBox.setAlignment(Pos.TOP_LEFT);
                ImageView imageView = ResourceManager.getIcon(string);
                UITextArea uITextArea = new UITextArea(326.0);
                uITextArea.setWrapText(true);
                uITextArea.setId("more-info-text-block");
                uITextArea.setText(arrstring[i2]);
                if (i2 > n2) {
                    imageView.setVisible(false);
                }
                hBox.getChildren().add(imageView);
                hBox.getChildren().add(uITextArea);
                vBox.getChildren().add(hBox);
                if (i2 >= n3 - 1) continue;
                vBox.getChildren().add(new Separator());
            }
        }
        return vBox;
    }

    private void showCertDetails() {
        CertificateDialog.showCertificates(this, this.certs, this.start, this.end);
    }

    private void dismissAction() {
        this.hide();
    }
}

