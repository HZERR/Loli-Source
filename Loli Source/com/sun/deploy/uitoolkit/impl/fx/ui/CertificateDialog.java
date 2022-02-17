/*
 * Decompiled with CFR 0.150.
 */
package com.sun.deploy.uitoolkit.impl.fx.ui;

import com.sun.deploy.uitoolkit.impl.fx.ui.FXDialog;
import com.sun.deploy.uitoolkit.impl.fx.ui.resources.ResourceManager;
import java.security.MessageDigest;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.text.Format;
import java.text.MessageFormat;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import sun.misc.HexDumpEncoder;
import sun.security.x509.SerialNumber;

public class CertificateDialog {
    public static void showCertificates(Stage stage, Certificate[] arrcertificate, int n2, int n3) {
        final FXDialog fXDialog = new FXDialog(ResourceManager.getMessage("cert.dialog.caption"), stage, true);
        fXDialog.setWidth(800.0);
        fXDialog.setHeight(600.0);
        BorderPane borderPane = new BorderPane();
        fXDialog.setContentPane(borderPane);
        borderPane.setCenter(CertificateDialog.getComponents(stage, arrcertificate, n2, n3));
        FlowPane flowPane = new FlowPane();
        flowPane.getStyleClass().add("button-bar");
        Button button = new Button(ResourceManager.getMessage("cert.dialog.close"));
        button.setDefaultButton(true);
        button.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent actionEvent) {
                fXDialog.hide();
            }
        });
        flowPane.getChildren().add(button);
        borderPane.setBottom(flowPane);
        fXDialog.show();
    }

    private static Node getComponents(Stage stage, Certificate[] arrcertificate, int n2, int n3) {
        SplitPane splitPane = new SplitPane();
        if (arrcertificate.length > n2 && arrcertificate[n2] instanceof X509Certificate) {
            TreeView treeView = CertificateDialog.buildCertChainTree(arrcertificate, n2, n3);
            final TableView tableView = new TableView();
            final TextArea textArea = new TextArea();
            textArea.setEditable(false);
            final MultipleSelectionModel multipleSelectionModel = treeView.getSelectionModel();
            multipleSelectionModel.getSelectedItems().addListener(new ListChangeListener<TreeItem<CertificateInfo>>(){

                @Override
                public void onChanged(ListChangeListener.Change<? extends TreeItem<CertificateInfo>> change) {
                    ObservableList observableList = multipleSelectionModel.getSelectedItems();
                    if (observableList != null && observableList.size() == 1) {
                        TreeItem treeItem = (TreeItem)observableList.get(0);
                        CertificateInfo certificateInfo = (CertificateInfo)treeItem.getValue();
                        CertificateDialog.showCertificateInfo(certificateInfo.getCertificate(), tableView, textArea);
                    }
                }
            });
            TableColumn<Row, Object> tableColumn = new TableColumn<Row, Object>();
            tableColumn.setText(ResourceManager.getMessage("cert.dialog.field"));
            tableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Row, Object>, ObservableValue<Object>>(){

                @Override
                public ObservableValue<Object> call(TableColumn.CellDataFeatures<Row, Object> cellDataFeatures) {
                    return new ReadOnlyObjectWrapper<Object>(cellDataFeatures.getValue().field);
                }
            });
            TableColumn<Row, Object> tableColumn2 = new TableColumn<Row, Object>();
            tableColumn2.setText(ResourceManager.getMessage("cert.dialog.value"));
            tableColumn2.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Row, Object>, ObservableValue<Object>>(){

                @Override
                public ObservableValue<Object> call(TableColumn.CellDataFeatures<Row, Object> cellDataFeatures) {
                    return new ReadOnlyObjectWrapper<Object>(cellDataFeatures.getValue().value);
                }
            });
            tableView.getColumns().addAll(tableColumn, tableColumn2);
            final TableView.TableViewSelectionModel tableViewSelectionModel = tableView.getSelectionModel();
            tableViewSelectionModel.setSelectionMode(SelectionMode.SINGLE);
            tableViewSelectionModel.getSelectedItems().addListener(new ListChangeListener<String>(){

                @Override
                public void onChanged(ListChangeListener.Change<? extends String> change) {
                    ObservableList observableList = tableViewSelectionModel.getSelectedItems();
                    if (observableList != null && observableList.size() == 1) {
                        String string = ((Row)observableList.get((int)0)).value;
                        textArea.setText(string);
                    }
                }
            });
            treeView.setMinWidth(Double.NEGATIVE_INFINITY);
            treeView.setMinHeight(Double.NEGATIVE_INFINITY);
            ScrollPane scrollPane = CertificateDialog.makeScrollPane(treeView);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            splitPane.getItems().add(scrollPane);
            SplitPane splitPane2 = new SplitPane();
            splitPane2.setOrientation(Orientation.VERTICAL);
            splitPane2.getItems().add(tableView);
            textArea.setPrefWidth(320.0);
            textArea.setPrefHeight(120.0);
            splitPane2.getItems().add(textArea);
            splitPane2.setDividerPosition(0, 0.8);
            splitPane.getItems().add(splitPane2);
            splitPane.setDividerPosition(0, 0.4);
            multipleSelectionModel.select(0);
        }
        return splitPane;
    }

    private static ScrollPane makeScrollPane(Node node) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(node);
        if (node instanceof Label) {
            scrollPane.setFitToWidth(true);
        }
        return scrollPane;
    }

    private static TreeView buildCertChainTree(Certificate[] arrcertificate, int n2, int n3) {
        TreeItem<CertificateInfo> treeItem = null;
        TreeItem<CertificateInfo> treeItem2 = null;
        for (int i2 = n2; i2 < arrcertificate.length && i2 < n3; ++i2) {
            TreeItem<CertificateInfo> treeItem3 = new TreeItem<CertificateInfo>(new CertificateInfo((X509Certificate)arrcertificate[i2]));
            if (treeItem == null) {
                treeItem = treeItem3;
            } else {
                treeItem2.getChildren().add(treeItem3);
            }
            treeItem2 = treeItem3;
        }
        TreeView<CertificateInfo> treeView = new TreeView<CertificateInfo>();
        treeView.setShowRoot(true);
        treeView.setRoot(treeItem);
        treeView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        return treeView;
    }

    private static void showCertificateInfo(X509Certificate x509Certificate, TableView tableView, TextArea textArea) {
        Object object;
        String string = "V" + x509Certificate.getVersion();
        String string2 = "[xxxxx-xxxxx]";
        String string3 = null;
        String string4 = null;
        try {
            object = new SerialNumber(x509Certificate.getSerialNumber());
            string2 = "[" + ((SerialNumber)object).getNumber() + "]";
            string3 = CertificateDialog.getCertFingerPrint("MD5", x509Certificate);
            string4 = CertificateDialog.getCertFingerPrint("SHA1", x509Certificate);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        object = "[" + x509Certificate.getSigAlgName() + "]";
        String string5 = CertificateDialog.formatDNString(x509Certificate.getIssuerDN().toString());
        String string6 = "[From: " + x509Certificate.getNotBefore() + ",\n To: " + x509Certificate.getNotAfter() + "]";
        String string7 = CertificateDialog.formatDNString(x509Certificate.getSubjectDN().toString());
        HexDumpEncoder hexDumpEncoder = new HexDumpEncoder();
        String string8 = hexDumpEncoder.encodeBuffer(x509Certificate.getSignature());
        ObservableList<Row> observableList = FXCollections.observableArrayList(new Row(ResourceManager.getMessage("cert.dialog.field.Version"), string), new Row(ResourceManager.getMessage("cert.dialog.field.SerialNumber"), string2), new Row(ResourceManager.getMessage("cert.dialog.field.SignatureAlg"), (String)object), new Row(ResourceManager.getMessage("cert.dialog.field.Issuer"), string5), new Row(ResourceManager.getMessage("cert.dialog.field.Validity"), string6), new Row(ResourceManager.getMessage("cert.dialog.field.Subject"), string7), new Row(ResourceManager.getMessage("cert.dialog.field.Signature"), string8), new Row(ResourceManager.getMessage("cert.dialog.field.md5Fingerprint"), string3), new Row(ResourceManager.getMessage("cert.dialog.field.sha1Fingerprint"), string4));
        tableView.setItems(observableList);
        tableView.getSelectionModel().select(8, null);
    }

    public static String formatDNString(String string) {
        int n2 = string.length();
        boolean bl = false;
        boolean bl2 = false;
        StringBuffer stringBuffer = new StringBuffer();
        for (int i2 = 0; i2 < n2; ++i2) {
            char c2 = string.charAt(i2);
            if (c2 == '\"' || c2 == '\'') {
                boolean bl3 = bl2 = !bl2;
            }
            if (c2 == ',' && !bl2) {
                stringBuffer.append(",\n");
                continue;
            }
            stringBuffer.append(c2);
        }
        return stringBuffer.toString();
    }

    public static String getCertFingerPrint(String string, X509Certificate x509Certificate) throws Exception {
        byte[] arrby = x509Certificate.getEncoded();
        MessageDigest messageDigest = MessageDigest.getInstance(string);
        byte[] arrby2 = messageDigest.digest(arrby);
        return CertificateDialog.toHexString(arrby2);
    }

    private static void byte2hex(byte by, StringBuffer stringBuffer) {
        char[] arrc = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        int n2 = (by & 0xF0) >> 4;
        int n3 = by & 0xF;
        stringBuffer.append(arrc[n2]);
        stringBuffer.append(arrc[n3]);
    }

    private static String toHexString(byte[] arrby) {
        StringBuffer stringBuffer = new StringBuffer();
        int n2 = arrby.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            CertificateDialog.byte2hex(arrby[i2], stringBuffer);
            if (i2 >= n2 - 1) continue;
            stringBuffer.append(":");
        }
        return stringBuffer.toString();
    }

    public static class CertificateInfo {
        X509Certificate cert;

        public CertificateInfo(X509Certificate x509Certificate) {
            this.cert = x509Certificate;
        }

        public X509Certificate getCertificate() {
            return this.cert;
        }

        private String extractAliasName(X509Certificate x509Certificate) {
            Object[] arrobject;
            Object object;
            String string = ResourceManager.getMessage("security.dialog.unknown.subject");
            String string2 = ResourceManager.getMessage("security.dialog.unknown.issuer");
            try {
                object = x509Certificate.getSubjectDN();
                arrobject = x509Certificate.getIssuerDN();
                String string3 = object.getName();
                String string4 = arrobject.getName();
                string = this.extractFromQuote(string3, "CN=");
                if (string == null) {
                    string = this.extractFromQuote(string3, "O=");
                }
                if (string == null) {
                    string = ResourceManager.getMessage("security.dialog.unknown.subject");
                }
                if ((string2 = this.extractFromQuote(string4, "CN=")) == null) {
                    string2 = this.extractFromQuote(string4, "O=");
                }
                if (string2 == null) {
                    string2 = ResourceManager.getMessage("security.dialog.unknown.issuer");
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
            object = new MessageFormat(ResourceManager.getMessage("security.dialog.certShowName"));
            arrobject = new Object[]{string, string2};
            return ((Format)object).format(arrobject);
        }

        private String extractFromQuote(String string, String string2) {
            if (string == null) {
                return null;
            }
            int n2 = string.indexOf(string2);
            int n3 = 0;
            if (n2 >= 0) {
                if ((n3 = string.charAt(n2 += string2.length()) == '\"' ? string.indexOf(34, ++n2) : string.indexOf(44, n2)) < 0) {
                    return string.substring(n2);
                }
                return string.substring(n2, n3);
            }
            return null;
        }

        public String toString() {
            return this.extractAliasName(this.cert);
        }
    }

    private static class Row {
        public String field;
        public String value;

        Row(String string, String string2) {
            this.field = string;
            this.value = string2;
        }
    }
}

