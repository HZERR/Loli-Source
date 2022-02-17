/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.print;

import com.sun.javafx.print.PrinterImpl;
import javafx.print.PageLayout;
import javafx.scene.Node;
import javafx.stage.Window;

public interface PrinterJobImpl {
    public PrinterImpl getPrinterImpl();

    public void setPrinterImpl(PrinterImpl var1);

    public boolean showPrintDialog(Window var1);

    public boolean showPageDialog(Window var1);

    public PageLayout validatePageLayout(PageLayout var1);

    public boolean print(PageLayout var1, Node var2);

    public boolean endJob();

    public void cancelJob();
}

