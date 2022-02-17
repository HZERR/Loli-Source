/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.deploy.trace.Trace
 *  com.sun.deploy.ui.AppInfo
 */
package com.sun.deploy.uitoolkit.impl.fx.ui;

import com.sun.deploy.trace.Trace;
import com.sun.deploy.ui.AppInfo;
import com.sun.deploy.uitoolkit.impl.fx.ui.CertificateDialog;
import com.sun.deploy.uitoolkit.impl.fx.ui.FXDialog;
import com.sun.deploy.uitoolkit.impl.fx.ui.MoreInfoDialog;
import com.sun.deploy.uitoolkit.impl.fx.ui.UITextArea;
import com.sun.deploy.uitoolkit.impl.fx.ui.resources.ResourceManager;
import com.sun.javafx.applet.HostServicesImpl;
import com.sun.javafx.application.HostServicesDelegate;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.TreeMap;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DialogTemplate {
    int theAnswer = -1;
    final Object responseLock = new Object();
    private static final int RISK_LABEL_WIDTH = 52;
    private static final int RISK_TEXT_WIDTH = 490;
    private EventHandler<ActionEvent> okHandler = new EventHandler<ActionEvent>(){

        @Override
        public void handle(ActionEvent actionEvent) {
            DialogTemplate.this.userAnswer = 0;
            if (DialogTemplate.this.always != null && DialogTemplate.this.always.isSelected()) {
                DialogTemplate.this.userAnswer = 2;
            }
            if (DialogTemplate.this.stayAliveOnOk) {
                return;
            }
            if (DialogTemplate.this.password != null) {
                DialogTemplate.access$402(DialogTemplate.this, DialogTemplate.this.password.getText().toCharArray());
            }
            if (DialogTemplate.this.pwdName != null) {
                DialogTemplate.this.userName = DialogTemplate.this.pwdName.getText();
            }
            if (DialogTemplate.this.pwdDomain != null) {
                DialogTemplate.this.domain = DialogTemplate.this.pwdDomain.getText();
            }
            if (DialogTemplate.this.scrollList != null) {
                DialogTemplate.this.userAnswer = DialogTemplate.this.scrollList.getSelectionModel().getSelectedIndex();
            }
            DialogTemplate.this.setVisible(false);
        }
    };
    private EventHandler<ActionEvent> cancelHandler = new EventHandler<ActionEvent>(){

        @Override
        public void handle(ActionEvent actionEvent) {
            if (DialogTemplate.this.throwable != null || DialogTemplate.this.detailPanel != null) {
                DialogTemplate.this.showMoreInfo();
                return;
            }
            DialogTemplate.this.userAnswer = 1;
            if (DialogTemplate.this.scrollList != null) {
                DialogTemplate.this.userAnswer = -1;
            }
            DialogTemplate.this.setVisible(false);
        }
    };
    EventHandler<ActionEvent> acceptRiskHandler = new EventHandler<ActionEvent>(){

        @Override
        public void handle(ActionEvent actionEvent) {
            boolean bl = DialogTemplate.this.acceptRisk.isSelected();
            DialogTemplate.this.okBtn.setDisable(!bl);
            DialogTemplate.this.okBtn.setDefaultButton(bl);
            DialogTemplate.this.cancelBtn.setDefaultButton(!bl);
            if (DialogTemplate.this.always != null) {
                DialogTemplate.this.always.setSelected(false);
                DialogTemplate.this.always.setDisable(!bl);
            }
        }
    };
    EventHandler<ActionEvent> expandHandler = new EventHandler<ActionEvent>(){

        @Override
        public void handle(ActionEvent actionEvent) {
            if (actionEvent.getSource() == DialogTemplate.this.expandBtn) {
                DialogTemplate.this.expandPanel.setTop(DialogTemplate.this.collapseBtn);
                DialogTemplate.this.expandPanel.setBottom(DialogTemplate.this.always);
                if (DialogTemplate.this.acceptRisk != null) {
                    DialogTemplate.this.always.setDisable(!DialogTemplate.this.acceptRisk.isSelected());
                }
            } else if (actionEvent.getSource() == DialogTemplate.this.collapseBtn) {
                DialogTemplate.this.expandPanel.setTop(DialogTemplate.this.expandBtn);
                DialogTemplate.this.expandPanel.setBottom(null);
            }
            DialogTemplate.this.dialog.sizeToScene();
        }
    };
    EventHandler moreInfoHandler = new EventHandler(){

        public void handle(Event event) {
            DialogTemplate.this.showMoreInfo();
        }
    };
    EventHandler closeHandler = new EventHandler(){

        public void handle(Event event) {
            DialogTemplate.this.dialog.hide();
        }
    };
    private FXDialog dialog = null;
    private VBox contentPane = null;
    private AppInfo ainfo = null;
    private String topText = null;
    private boolean useErrorIcon = false;
    private boolean useWarningIcon = false;
    private boolean useInfoIcon = false;
    private boolean useBlockedIcon = false;
    private boolean useMixcodeIcon = false;
    private Label progressStatusLabel = null;
    private BorderPane topPanel;
    private Pane centerPanel;
    private BorderPane expandPanel;
    private ImageView topIcon;
    private ImageView securityIcon;
    private Label nameInfo;
    private Label publisherInfo;
    private Label urlInfo;
    private Label mainJNLPInfo;
    private Label documentBaseInfo;
    private Button okBtn;
    private Button cancelBtn;
    private Button expandBtn;
    private Button collapseBtn;
    private CheckBox always;
    private CheckBox acceptRisk;
    private Label mixedCodeLabel;
    private UITextArea masthead1 = null;
    private UITextArea masthead2 = null;
    private static final int ICON_SIZE = 48;
    private int userAnswer = -1;
    static final int DIALOG_WIDTH = 540;
    private final int MAX_LARGE_SCROLL_WIDTH = 600;
    private final String SECURITY_ALERT_HIGH = "security.alert.high.image";
    private final String SECURITY_ALERT_LOW = "security.alert.low.image";
    private static int MAIN_TEXT_WIDTH = 400;
    private final String OK_ACTION = "OK";
    private final int MAX_BUTTONS = 2;
    private int start;
    private int end;
    private Certificate[] certs;
    private String[] alertStrs;
    private String[] infoStrs;
    private int securityInfoCount;
    private Color originalColor;
    private Cursor originalCursor = null;
    protected ProgressBar progressBar = null;
    private boolean stayAliveOnOk = false;
    private String contentString = null;
    private String reason;
    private String cacheUpgradeContentString = null;
    private String contentLabel = null;
    private String alwaysString = null;
    private String mixedCodeString = null;
    private boolean contentScroll = false;
    private boolean includeMasthead = true;
    private boolean includeAppInfo = true;
    private boolean largeScroll = false;
    private Throwable throwable = null;
    private Pane detailPanel = null;
    private char[] pwd = new char[0];
    private String userName;
    private String domain;
    private TextField pwdName;
    private TextField pwdDomain;
    private PasswordField password;
    private ListView scrollList;
    private boolean showDetails = false;
    TreeMap clientAuthCertsMap;
    private boolean majorWarning = false;
    private String okBtnStr;
    private String cancelBtnStr;
    private boolean sandboxApp = false;
    private boolean checkAlways = false;
    private boolean selfSigned = false;
    private boolean isBlockedDialog;

    DialogTemplate(AppInfo appInfo, Stage stage, String string, String string2) {
        Stage stage2 = stage;
        this.dialog = new FXDialog(string, stage2, false);
        this.contentPane = new VBox(){

            @Override
            protected double computePrefHeight(double d2) {
                double d3 = super.computePrefHeight(d2);
                return d3;
            }
        };
        this.dialog.setContentPane(this.contentPane);
        this.ainfo = appInfo;
        this.topText = string2;
    }

    void setNewSecurityContent(boolean bl, boolean bl2, String string, String string2, String[] arrstring, String[] arrstring2, int n2, boolean bl3, Certificate[] arrcertificate, int n3, int n4, boolean bl4, boolean bl5, boolean bl6) {
        this.certs = arrcertificate;
        this.start = n3;
        this.end = n4;
        this.alertStrs = arrstring;
        this.infoStrs = arrstring2;
        this.securityInfoCount = n2;
        this.majorWarning = bl4;
        this.okBtnStr = string;
        this.cancelBtnStr = string2;
        this.sandboxApp = bl5;
        this.checkAlways = bl2;
        this.selfSigned = bl6;
        if (arrstring != null && arrstring.length > 0) {
            this.useWarningIcon = true;
        }
        try {
            this.contentPane.setId("security-content-panel");
            this.dialog.initModality(Modality.APPLICATION_MODAL);
            this.contentPane.getChildren().add(this.createSecurityTopPanel());
            this.contentPane.getChildren().add(this.createSecurityCenterPanel());
            if (!bl6) {
                this.contentPane.getChildren().add(this.createSecurityBottomPanel());
            }
            this.dialog.setResizable(false);
            this.dialog.setIconifiable(false);
            if (this.alertStrs == null) {
                this.dialog.hideWindowTitle();
            }
        }
        catch (Throwable throwable) {
            Trace.ignored((Throwable)throwable);
        }
    }

    private Pane createSecurityTopPanel() {
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(this.createSecurityTopMastheadPanel());
        borderPane.setBottom(this.createSecurityTopIconLabelsPanel());
        return borderPane;
    }

    private Pane createSecurityTopMastheadPanel() {
        BorderPane borderPane = new BorderPane();
        this.masthead1 = new UITextArea(MAIN_TEXT_WIDTH);
        this.masthead1.setId("security-masthead-label");
        this.masthead1.setText(this.topText.trim());
        borderPane.setLeft(this.masthead1);
        if (this.alertStrs == null) {
            Button button = FXDialog.createCloseButton();
            button.setOnAction(this.closeHandler);
            borderPane.setRight(button);
        }
        return borderPane;
    }

    private Pane createSecurityTopIconLabelsPanel() {
        BorderPane borderPane = new BorderPane();
        this.topIcon = this.alertStrs != null ? ResourceManager.getIcon("warning48s.image") : ResourceManager.getIcon("java48s.image");
        Label label = new Label(null, this.topIcon);
        label.setId("security-top-icon-label");
        borderPane.setLeft(label);
        GridPane gridPane = new GridPane();
        gridPane.setId("security-top-labels-grid");
        String string = ResourceManager.getMessage("dialog.template.name");
        Label label2 = new Label(string);
        label2.setId("security-name-label");
        String string2 = ResourceManager.getMessage("dialog.template.publisher");
        Label label3 = new Label(string2);
        label3.setId("security-publisher-label");
        String string3 = ResourceManager.getMessage("deployment.ssv.location");
        Label label4 = new Label(string3);
        label4.setId("security-from-label");
        this.nameInfo = new Label();
        this.nameInfo.setId("security-name-value");
        this.publisherInfo = new Label();
        this.urlInfo = new Label();
        if (this.ainfo.getTitle() != null) {
            GridPane.setConstraints(label2, 0, 0);
            GridPane.setHalignment(label2, HPos.LEFT);
            gridPane.getChildren().add(label2);
            GridPane.setConstraints(this.nameInfo, 1, 0);
            gridPane.getChildren().add(this.nameInfo);
        }
        if (this.ainfo.getVendor() != null) {
            GridPane.setConstraints(label3, 0, 1);
            GridPane.setHalignment(label3, HPos.LEFT);
            gridPane.getChildren().add(label3);
            GridPane.setConstraints(this.publisherInfo, 1, 1);
            gridPane.getChildren().add(this.publisherInfo);
        }
        if (this.ainfo.getFrom() != null) {
            GridPane.setConstraints(label4, 0, 2);
            GridPane.setHalignment(label4, HPos.LEFT);
            gridPane.getChildren().add(label4);
            GridPane.setConstraints(this.urlInfo, 1, 2);
            gridPane.getChildren().add(this.urlInfo);
            int n2 = 3;
            if (this.ainfo.shouldDisplayMainJNLP()) {
                this.mainJNLPInfo = new Label();
                this.mainJNLPInfo.setId("dialog-from-value");
                GridPane.setConstraints(this.mainJNLPInfo, 1, n2++);
                gridPane.getChildren().add(this.mainJNLPInfo);
            }
            if (this.ainfo.shouldDisplayDocumentBase()) {
                this.documentBaseInfo = new Label();
                this.documentBaseInfo.setId("dialog-from-value");
                GridPane.setConstraints(this.documentBaseInfo, 1, n2);
                gridPane.getChildren().add(this.documentBaseInfo);
            }
        }
        this.setInfo(this.ainfo);
        borderPane.setCenter(gridPane);
        return borderPane;
    }

    private Pane createSecurityCenterPanel() {
        BorderPane borderPane = new BorderPane();
        if (this.majorWarning) {
            BorderPane borderPane2 = new BorderPane();
            String string = null;
            string = this.selfSigned ? ResourceManager.getMessage("dialog.selfsigned.security.risk.warning") : ResourceManager.getMessage("dialog.security.risk.warning");
            String string2 = ResourceManager.getMessage("security.dialog.notverified.subject");
            Text text = new Text(string.replaceAll(string2, string2.toUpperCase()));
            text.setWrappingWidth(MAIN_TEXT_WIDTH + 120);
            text.setFill(Color.web("0xCC0000"));
            text.setFont(Font.font("System", FontWeight.BOLD, 15.0));
            borderPane2.setLeft(text);
            borderPane2.setPadding(new Insets(8.0, 0.0, 0.0, 0.0));
            borderPane.setTop(borderPane2);
            borderPane.setCenter(this.createSecurityRiskPanel());
            borderPane.setBottom(this.createSecurityAcceptRiskPanel());
        } else {
            borderPane.setTop(this.createSecurityRiskPanel());
            borderPane.setBottom(this.createSecurityAlwaysPanel());
        }
        return borderPane;
    }

    private Pane createSecurityRiskPanel() {
        BorderPane borderPane = new BorderPane();
        borderPane.setId("security-risk-panel");
        if (this.majorWarning) {
            String string = this.alertStrs[0];
            int n2 = string.indexOf(" ");
            String string2 = string.substring(0, n2);
            String string3 = string.substring(n2 + 1);
            UITextArea uITextArea = new UITextArea(52.0);
            uITextArea.setText(string2);
            uITextArea.setId("security-risk-label");
            borderPane.setLeft(uITextArea);
            BorderPane borderPane2 = new BorderPane();
            UITextArea uITextArea2 = new UITextArea(490.0);
            uITextArea2.setId("security-risk-value");
            uITextArea2.setText(string3);
            borderPane2.setTop(uITextArea2);
            borderPane2.setBottom(this.createMoreInfoHyperlink());
            borderPane.setRight(borderPane2);
        } else {
            String string = ResourceManager.getMessage(this.sandboxApp ? "sandbox.security.dialog.valid.signed.risk" : "security.dialog.valid.signed.risk");
            UITextArea uITextArea = new UITextArea(450.0);
            uITextArea.setId("security-risk-value");
            uITextArea.setText(string);
            borderPane.setLeft(uITextArea);
        }
        return borderPane;
    }

    private Hyperlink createMoreInfoHyperlink() {
        String string = ResourceManager.getMessage("dialog.template.more.info2");
        Hyperlink hyperlink = new Hyperlink(string);
        hyperlink.setMnemonicParsing(true);
        hyperlink.setId("security-more-info-link");
        hyperlink.setOnAction(this.moreInfoHandler);
        return hyperlink;
    }

    private Pane createSecurityAcceptRiskPanel() {
        BorderPane borderPane = new BorderPane();
        String string = ResourceManager.getMessage("security.dialog.accept.title");
        String string2 = ResourceManager.getMessage("security.dialog.accept.text");
        UITextArea uITextArea = new UITextArea(542.0);
        uITextArea.setId("security-risk-value");
        uITextArea.setText(string);
        borderPane.setTop(uITextArea);
        BorderPane borderPane2 = new BorderPane();
        borderPane2.setId("security-accept-risk-panel");
        this.acceptRisk = new CheckBox(string2);
        this.acceptRisk.setSelected(false);
        this.acceptRisk.setOnAction(this.acceptRiskHandler);
        borderPane2.setLeft(this.acceptRisk);
        borderPane2.setRight(this.createSecurityOkCancelButtons());
        borderPane.setBottom(borderPane2);
        return borderPane;
    }

    private Pane createSecurityOkCancelButtons() {
        HBox hBox = new HBox();
        hBox.getStyleClass().add("security-button-bar");
        this.okBtn = new Button(ResourceManager.getMessage(this.okBtnStr));
        this.okBtn.setMnemonicParsing(true);
        this.okBtn.setOnAction(this.okHandler);
        hBox.getChildren().add(this.okBtn);
        this.cancelBtn = new Button(this.cancelBtnStr);
        this.cancelBtn.setOnAction(this.cancelHandler);
        this.cancelBtn.setCancelButton(true);
        hBox.getChildren().add(this.cancelBtn);
        if (this.majorWarning) {
            this.okBtn.setDisable(true);
            this.cancelBtn.setDefaultButton(true);
        } else {
            this.okBtn.setDefaultButton(true);
        }
        return hBox;
    }

    private Pane createSecurityAlwaysPanel() {
        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(this.createSecurityAlwaysCheckbox());
        return borderPane;
    }

    private CheckBox createSecurityAlwaysCheckbox() {
        this.alwaysString = ResourceManager.getMessage(this.sandboxApp ? "sandbox.security.dialog.always" : "security.dialog.always");
        this.always = new CheckBox(this.alwaysString);
        if (this.majorWarning) {
            this.always.setId("security-always-trust-checkbox");
        }
        this.always.setSelected(this.sandboxApp && this.checkAlways);
        this.always.setVisible(true);
        return this.always;
    }

    private Pane createSecurityBottomPanel() {
        if (this.majorWarning) {
            return this.createSecurityBottomExpandPanel();
        }
        return this.createSecurityBottomMoreInfoPanel();
    }

    private Pane createSecurityBottomExpandPanel() {
        this.expandPanel = new BorderPane();
        this.expandPanel.setId("security-bottom-panel");
        this.always = this.createSecurityAlwaysCheckbox();
        ImageView imageView = ResourceManager.getIcon("toggle_down2.image");
        String string = ResourceManager.getMessage("security.dialog.show.options");
        this.expandBtn = new Button(string, imageView);
        this.expandBtn.setId("security-expand-button");
        this.expandBtn.setOnAction(this.expandHandler);
        ImageView imageView2 = ResourceManager.getIcon("toggle_up2.image");
        String string2 = ResourceManager.getMessage("security.dialog.hide.options");
        this.collapseBtn = new Button(string2, imageView2);
        this.collapseBtn.setId("security-expand-button");
        this.collapseBtn.setOnAction(this.expandHandler);
        this.expandPanel.setTop(this.expandBtn);
        return this.expandPanel;
    }

    private Pane createSecurityBottomMoreInfoPanel() {
        BorderPane borderPane = new BorderPane();
        borderPane.setId("security-bottom-panel");
        HBox hBox = new HBox();
        this.securityIcon = this.alertStrs != null ? ResourceManager.getIcon("yellowShield.image") : ResourceManager.getIcon("blueShield.image");
        hBox.getChildren().add(this.securityIcon);
        BorderPane borderPane2 = new BorderPane();
        borderPane2.setId("security-more-info-panel");
        borderPane2.setCenter(this.createMoreInfoHyperlink());
        hBox.getChildren().add(borderPane2);
        borderPane.setRight(this.createSecurityOkCancelButtons());
        borderPane.setLeft(hBox);
        return borderPane;
    }

    void setSecurityContent(boolean bl, boolean bl2, String string, String string2, String[] arrstring, String[] arrstring2, int n2, boolean bl3, Certificate[] arrcertificate, int n3, int n4, boolean bl4) {
        this.certs = arrcertificate;
        this.start = n3;
        this.end = n4;
        this.alertStrs = arrstring;
        this.infoStrs = arrstring2;
        this.securityInfoCount = n2;
        this.majorWarning = bl4;
        if (arrstring != null && arrstring.length > 0) {
            this.useWarningIcon = true;
        }
        try {
            this.contentPane.getChildren().add(this.createTopPanel(false));
            this.dialog.initModality(Modality.APPLICATION_MODAL);
            if (bl) {
                this.alwaysString = ResourceManager.getMessage("security.dialog.always");
            }
            this.contentPane.getChildren().add(this.createCenterPanel(bl2, string, string2, -1));
            this.contentPane.getChildren().add(this.createBottomPanel(bl3));
            this.dialog.setResizable(false);
            this.dialog.setIconifiable(false);
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    void setSSVContent(String string, String string2, URL uRL, String string3, String string4, String string5, String string6, String string7) {
        try {
            BorderPane borderPane = new BorderPane();
            borderPane.setId("ssv-content-panel");
            this.dialog.initModality(Modality.APPLICATION_MODAL);
            this.contentPane.getChildren().add(borderPane);
            borderPane.setTop(this.createSSVTopPanel(this.topText, this.ainfo.getTitle(), this.ainfo.getDisplayFrom()));
            BorderPane borderPane2 = this.createSSVRiskPanel(string, string2, uRL);
            final SSVChoicePanel sSVChoicePanel = new SSVChoicePanel(string3, string4, string5);
            BorderPane borderPane3 = new BorderPane();
            borderPane3.setTop(borderPane2);
            borderPane3.setBottom(sSVChoicePanel);
            borderPane.setCenter(borderPane3);
            FlowPane flowPane = new FlowPane(6.0, 0.0);
            flowPane.getStyleClass().add("button-bar");
            this.okBtn = new Button(string6);
            this.okBtn.setOnAction(new EventHandler<ActionEvent>(){

                @Override
                public void handle(ActionEvent actionEvent) {
                    if (sSVChoicePanel.getSelection() == 0) {
                        DialogTemplate.this.setUserAnswer(2);
                    } else {
                        DialogTemplate.this.setUserAnswer(0);
                    }
                    DialogTemplate.this.setVisible(false);
                }
            });
            flowPane.getChildren().add(this.okBtn);
            this.cancelBtn = new Button(string7);
            this.cancelBtn.setOnAction(new EventHandler<ActionEvent>(){

                @Override
                public void handle(ActionEvent actionEvent) {
                    DialogTemplate.this.cancelAction();
                }
            });
            this.cancelBtn.setCancelButton(true);
            flowPane.getChildren().add(this.cancelBtn);
            this.okBtn.setDefaultButton(true);
            borderPane.setBottom(flowPane);
            this.dialog.setResizable(false);
            this.dialog.setIconifiable(false);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    void setSimpleContent(String string, boolean bl, String string2, String string3, String string4, boolean bl2, boolean bl3) {
        this.contentString = string;
        this.contentScroll = bl;
        this.includeMasthead = bl2;
        this.includeAppInfo = bl2;
        this.largeScroll = !bl2;
        this.useWarningIcon = bl3;
        if (string2 != null) {
            String[] arrstring = new String[]{string2};
            if (bl3) {
                this.alertStrs = arrstring;
            } else {
                this.infoStrs = arrstring;
            }
        }
        try {
            this.contentPane.getChildren().add(this.createTopPanel(false));
            this.contentPane.getChildren().add(this.createCenterPanel(false, string3, string4, -1));
            this.contentPane.getChildren().add(this.createBottomPanel(false));
            this.dialog.setResizable(bl);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    void setMixedCodeContent(String string, boolean bl, String string2, String string3, String string4, String string5, boolean bl2, boolean bl3) {
        this.contentString = string;
        this.contentScroll = bl;
        this.includeMasthead = bl2;
        this.includeAppInfo = bl2;
        this.largeScroll = !bl2;
        this.useMixcodeIcon = true;
        this.alertStrs = new String[1];
        String[] arrstring = new String[]{string3};
        this.alertStrs = arrstring;
        this.infoStrs = new String[3];
        String string6 = ResourceManager.getString("security.dialog.mixcode.info1");
        String string7 = ResourceManager.getString("security.dialog.mixcode.info2");
        String string8 = ResourceManager.getString("security.dialog.mixcode.info3");
        String[] arrstring2 = new String[]{string6, string7, string8};
        this.infoStrs = arrstring2;
        try {
            this.contentPane.getChildren().add(this.createTopPanel(false));
            this.mixedCodeString = string2;
            this.contentPane.getChildren().add(this.createCenterPanel(false, string4, string5, -1));
            this.contentPane.getChildren().add(this.createBottomPanel(false));
            this.okBtn.requestFocus();
            boolean bl4 = bl;
            this.dialog.setResizable(bl4);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    void setListContent(String string, ListView listView, boolean bl, String string2, String string3, TreeMap treeMap) {
        this.useWarningIcon = true;
        this.includeAppInfo = false;
        this.clientAuthCertsMap = treeMap;
        this.contentLabel = string;
        this.contentScroll = true;
        this.scrollList = listView;
        this.showDetails = bl;
        try {
            this.contentPane.getChildren().add(this.createTopPanel(false));
            this.contentPane.getChildren().add(this.createCenterPanel(false, string2, string3, -1));
            this.contentPane.getChildren().add(this.createBottomPanel(false));
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    void setApiContent(String string, String string2, String string3, boolean bl, String string4, String string5) {
        this.contentString = string;
        this.contentLabel = string2;
        this.contentScroll = string != null;
        this.alwaysString = string3;
        if (string2 == null && string != null) {
            this.infoStrs = new String[1];
            this.infoStrs[0] = string;
            this.contentString = null;
        }
        this.includeMasthead = true;
        this.includeAppInfo = this.contentString == null;
        this.largeScroll = false;
        try {
            this.contentPane.getChildren().add(this.createTopPanel(false));
            this.contentPane.getChildren().add(this.createCenterPanel(false, string4, string5, -1));
            this.contentPane.getChildren().add(this.createBottomPanel(false));
            this.dialog.setResizable(this.contentScroll);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    void setErrorContent(String string, String string2, String string3, Throwable throwable, Object object, Certificate[] arrcertificate, boolean bl) {
        Pane pane = (Pane)object;
        this.contentString = string;
        this.throwable = throwable;
        this.detailPanel = pane;
        this.certs = arrcertificate;
        if (bl) {
            this.includeAppInfo = false;
        }
        this.useErrorIcon = true;
        try {
            this.contentPane.getChildren().add(this.createTopPanel(false));
            this.contentPane.getChildren().add(this.createCenterPanel(false, string2, string3, -1));
            Pane pane2 = this.createBottomPanel(false);
            if (pane2.getChildren().size() > 0) {
                this.contentPane.getChildren().add(pane2);
            }
            this.dialog.setResizable(false);
        }
        catch (Throwable throwable2) {
            // empty catch block
        }
    }

    void setMultiButtonErrorContent(String string, String string2, String string3, String string4) {
        this.useErrorIcon = true;
        try {
            this.contentPane.getChildren().add(this.createTopPanel(false));
            BorderPane borderPane = new BorderPane();
            borderPane.setId("error-panel");
            borderPane.setTop(this.createInfoPanel(string));
            borderPane.setBottom(this.createThreeButtonsPanel(string2, string3, string4, false));
            this.contentPane.getChildren().add(borderPane);
            this.dialog.setResizable(false);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    void setInfoContent(String string, String string2) {
        this.useInfoIcon = true;
        this.contentString = string;
        try {
            this.contentPane.getChildren().add(this.createTopPanel(false));
            this.contentPane.getChildren().add(this.createCenterPanel(false, string2, null, -1));
            this.dialog.setResizable(false);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    void setPasswordContent(String string, boolean bl, boolean bl2, String string2, String string3, boolean bl3, char[] arrc, String string4, String string5) {
        try {
            this.contentPane.getChildren().add(this.createPasswordPanel(string, bl, bl2, string2, string3, bl3, arrc, string4, string5));
            this.dialog.initModality(Modality.APPLICATION_MODAL);
            this.dialog.setResizable(false);
            this.dialog.setIconifiable(false);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    void setUpdateCheckContent(String string, String string2, String string3, String string4) {
        try {
            this.contentPane.getChildren().add(this.createTopPanel(false));
            this.contentPane.getChildren().add(this.createInfoPanel(string));
            this.contentPane.getChildren().add(this.createThreeButtonsPanel(string2, string3, string4, true));
            this.dialog.setResizable(false);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    void setProgressContent(String string, String string2, String string3, boolean bl, int n2) {
        try {
            this.cacheUpgradeContentString = string3;
            this.contentPane.getChildren().add(this.createTopPanel(false));
            this.contentPane.getChildren().add(this.createCenterPanel(false, string, string2, n2));
            if (this.cacheUpgradeContentString == null) {
                this.contentPane.getChildren().add(this.createBottomPanel(false));
            }
            this.dialog.setResizable(false);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    private Pane createInfoPanel(String string) {
        StackPane stackPane = new StackPane();
        stackPane.setId("info-panel");
        UITextArea uITextArea = new UITextArea(508.0);
        uITextArea.setId("info-panel-text");
        uITextArea.setText(string);
        stackPane.getChildren().add(uITextArea);
        return stackPane;
    }

    private Pane createThreeButtonsPanel(String string, String string2, String string3, boolean bl) {
        FlowPane flowPane = new FlowPane(6.0, 0.0);
        flowPane.getStyleClass().add("button-bar");
        Button button = new Button(ResourceManager.getMessage(string));
        button.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent actionEvent) {
                DialogTemplate.this.setUserAnswer(0);
                DialogTemplate.this.setVisible(false);
            }
        });
        flowPane.getChildren().add(button);
        Button button2 = new Button(ResourceManager.getMessage(string2));
        button2.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent actionEvent) {
                DialogTemplate.this.setVisible(false);
                DialogTemplate.this.setUserAnswer(1);
            }
        });
        flowPane.getChildren().add(button2);
        Button button3 = null;
        if (string3 != null) {
            button3 = new Button(ResourceManager.getMessage(string3));
            button3.setOnAction(new EventHandler<ActionEvent>(){

                @Override
                public void handle(ActionEvent actionEvent) {
                    DialogTemplate.this.setVisible(false);
                    DialogTemplate.this.setUserAnswer(3);
                }
            });
            flowPane.getChildren().add(button3);
        }
        if (bl) {
            button3.setTooltip(new Tooltip(ResourceManager.getMessage("autoupdatecheck.masthead")));
        }
        if (button3 != null) {
            DialogTemplate.resizeButtons(button, button2, button3);
        } else {
            DialogTemplate.resizeButtons(button, button2);
        }
        return flowPane;
    }

    private Pane createTopPanel(boolean bl) {
        this.topPanel = new BorderPane();
        this.topPanel.setId("top-panel");
        if (this.includeMasthead) {
            this.masthead1 = new UITextArea(MAIN_TEXT_WIDTH);
            this.masthead1.setId("masthead-label-1");
            String string = this.topText;
            String string2 = null;
            for (String string3 : new String[]{"security.dialog.caption.run.question", "security.dialog.caption.continue.question"}) {
                String string4 = ResourceManager.getMessage(string3);
                if (string4 == null || !string.endsWith(string4)) continue;
                string2 = string.substring(0, string.indexOf(string4)).trim();
                string = string4;
                break;
            }
            VBox vBox = new VBox();
            vBox.setAlignment(Pos.CENTER_LEFT);
            this.masthead1.setText(string);
            this.masthead1.setAlignment(Pos.CENTER_LEFT);
            vBox.getChildren().add(this.masthead1);
            if (string2 != null) {
                this.masthead2 = new UITextArea(MAIN_TEXT_WIDTH);
                this.masthead2.setId("masthead-label-2");
                this.masthead2.setText(string2);
                this.masthead2.setAlignment(Pos.CENTER_LEFT);
                vBox.getChildren().add(this.masthead2);
            }
            this.topPanel.setLeft(vBox);
            BorderPane.setAlignment(vBox, Pos.CENTER_LEFT);
            if (bl) {
                ImageView imageView = ResourceManager.getIcon("progress.background.image");
            } else {
                this.topIcon = ResourceManager.getIcon("java48.image");
                if (this.useErrorIcon) {
                    this.topIcon = ResourceManager.getIcon("error48.image");
                }
                if (this.useInfoIcon) {
                    this.topIcon = ResourceManager.getIcon("info48.image");
                }
                if (this.useMixcodeIcon) {
                    this.topIcon = ResourceManager.getIcon("mixcode.image");
                }
                if (this.useBlockedIcon) {
                    this.topIcon = ResourceManager.getIcon("cert_error48.image");
                }
                if (this.useWarningIcon) {
                    this.topIcon = this.majorWarning ? ResourceManager.getIcon("major-warning48.image") : ResourceManager.getIcon("warning48.image");
                } else if (this.ainfo.getIconRef() != null) {
                    this.topIcon = ResourceManager.getIcon(this.ainfo.getIconRef());
                }
                this.topPanel.setRight(this.topIcon);
            }
        }
        return this.topPanel;
    }

    private Pane createCenterPanel(boolean bl, String string, String string2, int n2) {
        Region region;
        boolean bl2;
        Region region2;
        Object object;
        this.centerPanel = new VBox();
        this.centerPanel.setId("center-panel");
        GridPane gridPane = new GridPane();
        gridPane.setId("center-panel-grid");
        Label label = new Label(ResourceManager.getMessage("dialog.template.name"));
        label.setId("dialog-name-label");
        Label label2 = new Label(ResourceManager.getMessage("dialog.template.publisher"));
        label2.setId("dialog-publisher-label");
        Label label3 = new Label(ResourceManager.getMessage("deployment.ssv.location"));
        label3.setId("dialog-from-label");
        Label label4 = new Label(ResourceManager.getMessage("deployment.ssv.reason"));
        label4.setId("dialog-reason-label");
        this.nameInfo = new Label();
        this.nameInfo.setId("dialog-name-value");
        this.publisherInfo = new Label();
        this.publisherInfo.setId("dialog-publisher-value");
        this.urlInfo = new Label();
        this.urlInfo.setId("dialog-from-value");
        Label label5 = new Label();
        label5.setWrapText(true);
        label5.setId("dialog-reason-value");
        int n3 = 0;
        if (this.ainfo.getTitle() != null) {
            GridPane.setConstraints(label, 0, n3);
            GridPane.setHalignment(label, HPos.RIGHT);
            gridPane.getChildren().add(label);
            GridPane.setConstraints(this.nameInfo, 1, n3++);
            gridPane.getChildren().add(this.nameInfo);
        }
        if (this.ainfo.getVendor() != null) {
            GridPane.setConstraints(label2, 0, n3);
            GridPane.setHalignment(label2, HPos.RIGHT);
            gridPane.getChildren().add(label2);
            GridPane.setConstraints(this.publisherInfo, 1, n3++);
            gridPane.getChildren().add(this.publisherInfo);
        }
        if (this.ainfo.getFrom() != null) {
            GridPane.setConstraints(label3, 0, n3);
            GridPane.setHalignment(label3, HPos.RIGHT);
            gridPane.getChildren().add(label3);
            GridPane.setConstraints(this.urlInfo, 1, n3++);
            gridPane.getChildren().add(this.urlInfo);
            if (this.ainfo.shouldDisplayMainJNLP()) {
                this.mainJNLPInfo = new Label();
                this.mainJNLPInfo.setId("dialog-from-value");
                GridPane.setConstraints(this.mainJNLPInfo, 1, n3++);
                gridPane.getChildren().add(this.mainJNLPInfo);
            }
            if (this.ainfo.shouldDisplayDocumentBase()) {
                this.documentBaseInfo = new Label();
                this.documentBaseInfo.setId("dialog-from-value");
                GridPane.setConstraints(this.documentBaseInfo, 1, n3++);
                gridPane.getChildren().add(this.documentBaseInfo);
            }
        }
        if (this.reason != null) {
            GridPane.setConstraints(label4, 0, n3);
            GridPane.setHalignment(label4, HPos.RIGHT);
            gridPane.getChildren().add(label4);
            label5.setText(this.reason);
            GridPane.setConstraints(label5, 1, n3++);
            gridPane.getChildren().add(label5);
        }
        this.setInfo(this.ainfo);
        FlowPane flowPane = new FlowPane();
        flowPane.setId("center-checkbox-panel");
        BorderPane borderPane = new BorderPane();
        borderPane.setId("mixed-code-panel");
        BorderPane borderPane2 = new BorderPane();
        borderPane2.setId("center-content-panel");
        VBox.setVgrow(borderPane2, Priority.ALWAYS);
        if (this.alwaysString != null) {
            object = "security.dialog.always";
            this.always = new CheckBox(this.alwaysString);
            this.always.setSelected(bl);
            flowPane.getChildren().add(this.always);
        }
        if (this.mixedCodeString != null) {
            this.mixedCodeLabel = new Label(this.mixedCodeString);
            object = new BorderPane();
            ((Node)object).setId("center-more-info-panel");
            region2 = new Hyperlink(ResourceManager.getMessage("dialog.template.more.info"));
            ((Labeled)region2).setMnemonicParsing(true);
            ((ButtonBase)region2).setOnAction(new EventHandler<ActionEvent>(){

                @Override
                public void handle(ActionEvent actionEvent) {
                    DialogTemplate.this.showMixedcodeMoreInfo();
                }
            });
            ((BorderPane)object).setLeft(region2);
            borderPane.setTop(this.mixedCodeLabel);
            borderPane.setBottom((Node)object);
        }
        boolean bl3 = bl2 = n2 >= 0;
        if (bl2) {
            this.progressBar = new ProgressBar();
            this.progressBar.setVisible(n2 <= 100);
        }
        if (this.contentString != null) {
            if (this.contentLabel != null) {
                region2 = new BorderPane();
                ((BorderPane)region2).setLeft(new Label(this.contentLabel));
                borderPane2.setTop(region2);
            }
            if (this.contentScroll) {
                boolean bl4 = this.largeScroll;
                if (this.largeScroll) {
                    region2 = new Label(this.contentString);
                    region2.setPrefWidth(640.0);
                    region2.setPrefHeight(240.0);
                } else {
                    region2 = new Label(this.contentString);
                    region2.setPrefWidth(320.0);
                    region2.setPrefHeight(48.0);
                }
                region = new ScrollPane();
                ((ScrollPane)region).setContent(region2);
                ((ScrollPane)region).setFitToWidth(true);
                VBox.setVgrow(region, Priority.ALWAYS);
                if (bl4) {
                    region.setMaxWidth(600.0);
                }
                borderPane2.setCenter(region);
            } else if (this.isBlockedDialog) {
                region2 = new VBox();
                UITextArea uITextArea = new UITextArea(this.contentString);
                uITextArea.setId("center-content-area");
                uITextArea.setAlignment(Pos.TOP_CENTER);
                uITextArea.setPrefWidth(540.0);
                region = new Hyperlink(ResourceManager.getMessage("deployment.blocked.moreinfo.hyperlink.text"));
                final String string3 = ResourceManager.getMessage("deployment.blocked.moreinfo.hyperlink.url");
                ((Labeled)region).setMnemonicParsing(true);
                ((ButtonBase)region).setOnAction(new EventHandler<ActionEvent>(){

                    @Override
                    public void handle(ActionEvent actionEvent) {
                        HostServicesDelegate hostServicesDelegate = HostServicesImpl.getInstance(null);
                        if (hostServicesDelegate != null && string3 != null) {
                            hostServicesDelegate.showDocument(string3);
                        }
                    }
                });
                ((Pane)region2).getChildren().add(uITextArea);
                ((Pane)region2).getChildren().add(region);
                borderPane2.setCenter(region2);
            } else {
                region2 = new UITextArea(this.contentString);
                region2.setId("center-content-area");
                ((Labeled)region2).setAlignment(Pos.TOP_LEFT);
                borderPane2.setCenter(region2);
            }
            borderPane2.setPadding(new Insets(0.0, 0.0, 12.0, 0.0));
        }
        if (this.scrollList != null) {
            if (this.contentLabel != null) {
                region2 = new BorderPane();
                ((BorderPane)region2).setLeft(new Label(this.contentLabel));
                borderPane2.setTop(region2);
            }
            if (this.contentScroll) {
                region2 = new ScrollPane();
                ((ScrollPane)region2).setContent(this.scrollList);
                VBox.setVgrow(region2, Priority.ALWAYS);
                borderPane2.setCenter(region2);
            }
            if (this.showDetails) {
                region2 = new Hyperlink(ResourceManager.getMessage("security.more.info.details"));
                ((Labeled)region2).setMnemonicParsing(true);
                ((ButtonBase)region2).setOnAction(new EventHandler<ActionEvent>(){

                    @Override
                    public void handle(ActionEvent actionEvent) {
                        DialogTemplate.this.showCertificateDetails();
                    }
                });
                FlowPane flowPane2 = new FlowPane();
                flowPane2.setPadding(new Insets(12.0, 0.0, 12.0, 0.0));
                flowPane2.setAlignment(Pos.TOP_LEFT);
                flowPane2.getChildren().add(region2);
                borderPane2.setBottom(flowPane2);
            }
        }
        region2 = new FlowPane(6.0, 0.0);
        region2.getStyleClass().add("button-bar");
        region2.setId("center-bottom-button-bar");
        this.okBtn = new Button(string == null ? "" : ResourceManager.getMessage(string));
        this.okBtn.setOnAction(this.okHandler);
        ((Pane)region2).getChildren().add(this.okBtn);
        this.okBtn.setVisible(string != null);
        this.cancelBtn = new Button(string2 == null ? "" : ResourceManager.getMessage(string2));
        this.cancelBtn.setOnAction(this.cancelHandler);
        ((Pane)region2).getChildren().add(this.cancelBtn);
        this.cancelBtn.setVisible(string2 != null);
        if (this.okBtn.isVisible()) {
            this.okBtn.setDefaultButton(true);
        } else {
            this.cancelBtn.setCancelButton(true);
        }
        DialogTemplate.resizeButtons(this.okBtn, this.cancelBtn);
        if (this.isBlockedDialog && borderPane2.getChildren().size() > 0) {
            this.centerPanel.getChildren().add(borderPane2);
        }
        if (this.cacheUpgradeContentString != null) {
            UITextArea uITextArea = new UITextArea(this.cacheUpgradeContentString);
            uITextArea.setId("cache-upgrade-content");
            borderPane2.setTop(uITextArea);
        } else {
            if (this.includeAppInfo) {
                this.centerPanel.getChildren().add(gridPane);
            }
            if (this.alwaysString != null) {
                this.centerPanel.getChildren().add(flowPane);
            }
            if (this.mixedCodeString != null) {
                this.centerPanel.getChildren().add(borderPane);
            }
        }
        if (!this.isBlockedDialog && borderPane2.getChildren().size() > 0) {
            this.centerPanel.getChildren().add(borderPane2);
        }
        BorderPane borderPane3 = new BorderPane();
        borderPane3.setId("center-bottom-panel");
        if (bl2) {
            this.progressStatusLabel = new Label(" ");
            this.progressStatusLabel.getStyleClass().add("progress-label");
            region = new BorderPane();
            region.setId("center-progress-status-panel");
            this.centerPanel.getChildren().add(region);
            ((BorderPane)region).setLeft(this.progressStatusLabel);
            region.setPadding(new Insets(2.0, 0.0, 2.0, 0.0));
            borderPane3.setCenter(this.progressBar);
        }
        borderPane3.setRight(region2);
        this.centerPanel.getChildren().add(borderPane3);
        return this.centerPanel;
    }

    private Pane createBottomPanel(boolean bl) {
        HBox hBox = new HBox();
        hBox.setId("bottom-panel");
        if (this.alertStrs != null || this.infoStrs != null) {
            String string = "security.alert.high.image";
            if (this.alertStrs == null || this.alertStrs.length == 0) {
                string = "security.alert.low.image";
                if (this.always != null) {
                    this.always.setSelected(true);
                }
            } else if (this.mixedCodeString == null) {
                this.okBtn.setDefaultButton(false);
                this.cancelBtn.setCancelButton(true);
            }
            this.securityIcon = ResourceManager.getIcon(string);
            hBox.getChildren().add(this.securityIcon);
            boolean bl2 = false;
            Hyperlink hyperlink = null;
            if (bl) {
                hyperlink = new Hyperlink(ResourceManager.getMessage("dialog.template.more.info"));
                hyperlink.setMnemonicParsing(true);
                hyperlink.setId("bottom-more-info-link");
                hyperlink.setOnAction(new EventHandler<ActionEvent>(){

                    @Override
                    public void handle(ActionEvent actionEvent) {
                        DialogTemplate.this.showMoreInfo();
                    }
                });
            }
            int n2 = 333;
            UITextArea uITextArea = new UITextArea(n2);
            uITextArea.setId("bottom-text");
            if ((this.alertStrs == null || this.alertStrs.length == 0) && this.infoStrs != null && this.infoStrs.length != 0) {
                uITextArea.setText(this.infoStrs[0] != null ? this.infoStrs[0] : " ");
            } else if (this.alertStrs != null && this.alertStrs.length != 0) {
                uITextArea.setText(this.alertStrs[0] != null ? this.alertStrs[0] : " ");
            }
            hBox.getChildren().add(uITextArea);
            if (hyperlink != null) {
                hBox.getChildren().add(hyperlink);
            }
        }
        return hBox;
    }

    private BorderPane createSSVTopPanel(String string, String string2, String string3) {
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(16.0, 0.0, 16.0, 16.0));
        Label label = new Label(string);
        label.getStyleClass().add("ssv-big-bold-label");
        borderPane.setTop(label);
        Label label2 = new Label(ResourceManager.getMessage("dialog.template.name"));
        label2.getStyleClass().add("ssv-small-bold-label");
        label2.setId("ssv-top-panel-name-label");
        Label label3 = new Label(ResourceManager.getMessage("deployment.ssv.location"));
        label3.getStyleClass().add("ssv-small-bold-label");
        label3.setId("ssv-top-panel-from-label");
        this.nameInfo = new Label(string2);
        this.nameInfo.getStyleClass().add("ssv-big-bold-label");
        Label label4 = new Label(string3);
        label4.getStyleClass().add("ssv-small-label");
        BorderPane[] arrborderPane = new BorderPane[4];
        for (int i2 = 0; i2 < 4; ++i2) {
            arrborderPane[i2] = new BorderPane();
        }
        ImageView imageView = ResourceManager.getIcon("warning48.image");
        arrborderPane[2].setTop(label2);
        arrborderPane[2].setBottom(label3);
        arrborderPane[2].setPadding(new Insets(2.0, 0.0, 0.0, 0.0));
        arrborderPane[3].setTop(this.nameInfo);
        arrborderPane[3].setBottom(label4);
        arrborderPane[1].setLeft(arrborderPane[2]);
        arrborderPane[1].setCenter(arrborderPane[3]);
        arrborderPane[1].setPadding(new Insets(12.0, 0.0, 12.0, 0.0));
        arrborderPane[0].setLeft(imageView);
        arrborderPane[0].setRight(arrborderPane[1]);
        arrborderPane[0].setPadding(new Insets(8.0, 0.0, 0.0, 32.0));
        borderPane.setBottom(arrborderPane[0]);
        return borderPane;
    }

    private BorderPane createSSVRiskPanel(String string, String string2, final URL uRL) {
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(8.0, 8.0, 0.0, 8.0));
        int n2 = string.indexOf(" ");
        if (n2 < string.length() - 2) {
            String string3 = string.substring(0, n2);
            String string4 = string.substring(n2 + 1);
            BorderPane borderPane2 = new BorderPane();
            Label label = new Label(string3);
            label.getStyleClass().add("ssv-small-bold-label");
            borderPane2.setTop(label);
            borderPane2.setPadding(new Insets(0.0, 8.0, 0.0, 8.0));
            BorderPane borderPane3 = new BorderPane();
            Label label2 = new Label(string4);
            borderPane3.setLeft(label2);
            label2.getStyleClass().add("ssv-small-label");
            Hyperlink hyperlink = new Hyperlink(string2);
            hyperlink.setOnAction(new EventHandler<ActionEvent>(){

                @Override
                public void handle(ActionEvent actionEvent) {
                    HostServicesDelegate hostServicesDelegate = HostServicesImpl.getInstance(null);
                    if (hostServicesDelegate != null && uRL != null) {
                        hostServicesDelegate.showDocument(uRL.toExternalForm());
                    }
                }
            });
            borderPane3.setRight(hyperlink);
            borderPane.setLeft(borderPane2);
            borderPane.setCenter(borderPane3);
        }
        return borderPane;
    }

    private Pane createPasswordPanel(String string, boolean bl, boolean bl2, String string2, String string3, boolean bl3, char[] arrc, String string4, String string5) {
        Region region;
        Object object;
        String string6;
        Label label = new Label();
        Label label2 = new Label();
        ImageView imageView = ResourceManager.getIcon("pwd-masthead.png");
        if (bl) {
            string6 = "password.dialog.username";
            label.setText(ResourceManager.getMessage(string6));
            label.setMnemonicParsing(true);
            this.pwdName = new TextField();
            this.pwdName.setId("user-name-field");
            this.pwdName.setText(string2);
            label.setLabelFor(this.pwdName);
            label.setId("user-name-label");
        }
        string6 = "password.dialog.password";
        Label label3 = new Label(ResourceManager.getMessage(string6));
        this.password = new PasswordField();
        this.password.setText(String.valueOf(arrc));
        label3.setLabelFor(this.password);
        label3.setMnemonicParsing(true);
        label3.setId("password-label");
        if (bl2) {
            object = "password.dialog.domain";
            label2.setText(ResourceManager.getMessage((String)object));
            this.pwdDomain = new TextField();
            this.pwdDomain.setText(string3);
            label2.setLabelFor(this.pwdDomain);
            label2.setMnemonicParsing(true);
            label2.setId("password-domain-label");
        }
        object = new VBox();
        ((Region)object).setMaxWidth(imageView.getImage().getWidth());
        ((Pane)object).getChildren().add(imageView);
        VBox vBox = new VBox(10.0);
        vBox.setId("password-panel");
        Label label4 = new Label();
        label4.setId("password-details");
        label4.setWrapText(true);
        label4.setText(string);
        vBox.getChildren().add(label4);
        GridPane gridPane = new GridPane();
        gridPane.setId("password-panel-grid");
        int n2 = 0;
        if (bl) {
            GridPane.setConstraints(label, 0, n2);
            GridPane.setHalignment(label, HPos.RIGHT);
            gridPane.getChildren().add(label);
            GridPane.setConstraints(this.pwdName, 1, n2++);
            gridPane.getChildren().add(this.pwdName);
        }
        GridPane.setConstraints(label3, 0, n2);
        GridPane.setHalignment(label3, HPos.RIGHT);
        gridPane.getChildren().add(label3);
        GridPane.setConstraints(this.password, 1, n2++);
        gridPane.getChildren().add(this.password);
        if (bl2) {
            GridPane.setConstraints(label2, 0, n2);
            GridPane.setHalignment(label2, HPos.RIGHT);
            gridPane.getChildren().add(label2);
            GridPane.setConstraints(this.pwdDomain, 1, n2++);
            gridPane.getChildren().add(this.pwdDomain);
        }
        if (bl3) {
            this.always = new CheckBox(ResourceManager.getMessage("password.dialog.save"));
            this.always.setId("password-always-checkbox");
            this.always.setSelected(arrc.length > 0);
            GridPane.setConstraints(this.always, 1, n2++);
            gridPane.getChildren().add(this.always);
        }
        vBox.getChildren().add(gridPane);
        if (string5 != null) {
            region = new Label();
            region.setId("password-warning");
            ((Labeled)region).setWrapText(true);
            ((Labeled)region).setText(string5);
            vBox.getChildren().add(region);
        }
        region = new FlowPane(6.0, 0.0);
        ((FlowPane)region).setPrefWrapLength(300.0);
        region.getStyleClass().add("button-bar");
        region.setId("password-button-bar");
        this.okBtn = new Button(ResourceManager.getMessage("common.ok_btn"));
        this.okBtn.setOnAction(this.okHandler);
        this.okBtn.setDefaultButton(true);
        this.cancelBtn = new Button(ResourceManager.getMessage("common.cancel_btn"));
        this.cancelBtn.setOnAction(this.cancelHandler);
        DialogTemplate.resizeButtons(this.okBtn, this.cancelBtn);
        ((Pane)region).getChildren().addAll(this.okBtn, this.cancelBtn);
        vBox.getChildren().add(region);
        if (string4 != null) {
            MessageFormat messageFormat = new MessageFormat(ResourceManager.getMessage("password.dialog.scheme"));
            Object[] arrobject = new Object[]{string4};
            Label label5 = new Label(messageFormat.format(arrobject));
            vBox.getChildren().add(label5);
        }
        ((Pane)object).getChildren().add(vBox);
        return object;
    }

    void showMoreInfo() {
        MoreInfoDialog moreInfoDialog = this.throwable == null && this.detailPanel == null ? new MoreInfoDialog(this.dialog, this.alertStrs, this.infoStrs, this.securityInfoCount, this.certs, this.start, this.end, this.sandboxApp) : new MoreInfoDialog(this.dialog, this.detailPanel, this.throwable, this.certs);
        moreInfoDialog.show();
    }

    void showMixedcodeMoreInfo() {
        MoreInfoDialog moreInfoDialog = new MoreInfoDialog(this.dialog, null, this.infoStrs, 0, null, 0, 0, this.sandboxApp);
        moreInfoDialog.show();
    }

    void showCertificateDetails() {
        Certificate[] arrcertificate = null;
        Iterator iterator = this.clientAuthCertsMap.values().iterator();
        for (long i2 = (long)this.scrollList.getSelectionModel().getSelectedIndex(); i2 >= 0L && iterator.hasNext(); --i2) {
            arrcertificate = (X509Certificate[])iterator.next();
        }
        if (arrcertificate != null) {
            CertificateDialog.showCertificates(this.dialog, arrcertificate, 0, arrcertificate.length);
        }
    }

    public void setVisible(boolean bl) {
        if (bl) {
            final FXDialog fXDialog = this.dialog;
            Runnable runnable = new Runnable(){

                @Override
                public void run() {
                    fXDialog.showAndWait();
                }
            };
            runnable.run();
        } else {
            this.dialog.hide();
        }
    }

    public static void resizeButtons(Button ... arrbutton) {
        int n2 = arrbutton.length;
        double d2 = 50.0;
        for (int i2 = 0; i2 < n2; ++i2) {
            if (!(arrbutton[i2].prefWidth(-1.0) > d2)) continue;
            d2 = arrbutton[i2].prefWidth(-1.0);
        }
    }

    public void cancelAction() {
        this.userAnswer = 1;
        this.setVisible(false);
    }

    int getUserAnswer() {
        return this.userAnswer;
    }

    void setUserAnswer(int n2) {
        this.userAnswer = n2;
    }

    char[] getPassword() {
        return this.pwd;
    }

    String getUserName() {
        return this.userName;
    }

    String getDomain() {
        return this.domain;
    }

    public boolean isPasswordSaved() {
        return this.always != null && this.always.isSelected();
    }

    public void progress(int n2) {
        if (this.progressBar != null) {
            if (n2 <= 100) {
                this.progressBar.setProgress((double)n2 / 100.0);
                this.progressBar.setVisible(true);
            } else {
                this.progressBar.setVisible(false);
            }
        }
    }

    public FXDialog getDialog() {
        return this.dialog;
    }

    static String getDisplayMainJNLPTooltip(AppInfo appInfo) {
        try {
            Class<AppInfo> class_ = AppInfo.class;
            Method method = class_.getMethod("getDisplayMainJNLPTooltip", null);
            return (String)method.invoke((Object)appInfo, new Object[0]);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return "";
        }
    }

    public void setInfo(AppInfo appInfo) {
        URL uRL;
        this.ainfo = appInfo;
        if (this.nameInfo != null) {
            this.nameInfo.setText(appInfo.getTitle());
        }
        if (this.publisherInfo != null) {
            this.publisherInfo.setText(appInfo.getVendor());
        }
        if (this.urlInfo != null) {
            this.urlInfo.setText(appInfo.getDisplayFrom());
            uRL = appInfo.getFrom();
            this.urlInfo.setTooltip(new Tooltip(uRL == null ? "" : uRL.toString()));
        }
        if (this.mainJNLPInfo != null) {
            this.mainJNLPInfo.setText(appInfo.getDisplayMainJNLP());
            this.mainJNLPInfo.setTooltip(new Tooltip(DialogTemplate.getDisplayMainJNLPTooltip(appInfo)));
        }
        if (this.documentBaseInfo != null) {
            this.documentBaseInfo.setText(appInfo.getDisplayDocumentBase());
            uRL = appInfo.getDocumentBase();
            this.documentBaseInfo.setTooltip(new Tooltip(uRL == null ? "" : uRL.toString()));
        }
    }

    void showOk(boolean bl) {
        DialogTemplate.resizeButtons(this.okBtn, this.cancelBtn);
        this.okBtn.setVisible(bl);
    }

    void stayAlive() {
        this.stayAliveOnOk = true;
    }

    public void setProgressStatusText(String string) {
        if (this.progressStatusLabel != null) {
            if (string == null || string.length() == 0) {
                string = " ";
            }
            this.progressStatusLabel.setText(string);
        }
    }

    void setPublisherInfo(String string, String string2, String string3, Object object, boolean bl) {
        this.detailPanel = (Pane)object;
        this.contentString = string;
        this.useInfoIcon = true;
        if (object == null) {
            string3 = null;
        }
        if (bl) {
            this.includeAppInfo = false;
        }
        this.okBtnStr = string2;
        this.cancelBtnStr = string3;
        try {
            this.contentPane.getChildren().add(this.createTopPanel(true));
            this.contentPane.getChildren().add(this.createCenterPanel(false, string2, string3, -1));
            this.dialog.setResizable(false);
            this.dialog.setIconifiable(false);
        }
        catch (Throwable throwable) {
            Trace.ignored((Throwable)throwable);
        }
    }

    void setBlockedDialogInfo(String string, String string2, String string3, String string4, Object object, boolean bl) {
        this.detailPanel = (Pane)object;
        this.contentString = string2;
        this.reason = string;
        this.useBlockedIcon = true;
        this.isBlockedDialog = true;
        if (object == null) {
            string4 = null;
        }
        if (bl) {
            this.includeAppInfo = false;
        }
        this.okBtnStr = string3;
        this.cancelBtnStr = string4;
        try {
            this.contentPane.getChildren().add(this.createTopPanel(true));
            this.contentPane.getChildren().add(this.createCenterPanel(false, string3, string4, -1));
            this.dialog.setResizable(false);
            this.dialog.setIconifiable(false);
        }
        catch (Throwable throwable) {
            Trace.ignored((Throwable)throwable);
        }
    }

    static void showDocument(String string) {
        try {
            Class<?> class_ = Class.forName("com.sun.deploy.config.Platform");
            Method method = class_.getMethod("get", new Class[0]);
            Object object = method.invoke(null, new Object[0]);
            Method method2 = object.getClass().getMethod("showDocument", String.class);
            method2.invoke(object, string);
        }
        catch (Exception exception) {
            Trace.ignored((Throwable)exception);
        }
    }

    static /* synthetic */ char[] access$402(DialogTemplate dialogTemplate, char[] arrc) {
        dialogTemplate.pwd = arrc;
        return arrc;
    }

    private class SSVChoicePanel
    extends BorderPane {
        ToggleGroup group;
        RadioButton button1;
        RadioButton button2;

        public SSVChoicePanel(String string, String string2, String string3) {
            this.setPadding(new Insets(8.0, 16.0, 0.0, 16.0));
            BorderPane borderPane = new BorderPane();
            VBox vBox = new VBox();
            vBox.setSpacing(4.0);
            Label label = new Label(string);
            label.getStyleClass().add("ssv-small-bold-label");
            borderPane.setCenter(label);
            this.button1 = new RadioButton(string2);
            this.button1.getStyleClass().add("ssv-small-label");
            this.button2 = new RadioButton(string3);
            this.button2.getStyleClass().add("ssv-small-label");
            this.group = new ToggleGroup();
            this.button1.setToggleGroup(this.group);
            this.button2.setToggleGroup(this.group);
            this.button1.setSelected(true);
            vBox.getChildren().addAll(this.button1, this.button2);
            vBox.setPadding(new Insets(0.0, 16.0, 0.0, 32.0));
            this.setTop(borderPane);
            this.setBottom(vBox);
            this.button1.requestFocus();
        }

        public int getSelection() {
            if (this.button2.isSelected()) {
                return 1;
            }
            return 0;
        }
    }
}

