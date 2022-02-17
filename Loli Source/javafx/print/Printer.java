/*
 * Decompiled with CFR 0.150.
 */
package javafx.print;

import com.sun.javafx.print.PrintHelper;
import com.sun.javafx.print.PrinterImpl;
import com.sun.javafx.print.Units;
import com.sun.javafx.tk.PrintPipeline;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableSet;
import javafx.geometry.Rectangle2D;
import javafx.print.JobSettings;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.PaperSource;
import javafx.print.PrintResolution;
import javafx.print.PrinterAttributes;

public final class Printer {
    private static ReadOnlyObjectWrapper<Printer> defaultPrinter;
    private PrinterImpl impl;
    private PrinterAttributes attributes;
    private PageLayout defPageLayout;

    public static ObservableSet<Printer> getAllPrinters() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPrintJobAccess();
        }
        return PrintPipeline.getPrintPipeline().getAllPrinters();
    }

    private static ReadOnlyObjectWrapper<Printer> defaultPrinterImpl() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPrintJobAccess();
        }
        if (defaultPrinter == null) {
            Printer printer = PrintPipeline.getPrintPipeline().getDefaultPrinter();
            defaultPrinter = new ReadOnlyObjectWrapper<Printer>(null, "defaultPrinter", printer);
        }
        return defaultPrinter;
    }

    public static ReadOnlyObjectProperty<Printer> defaultPrinterProperty() {
        return Printer.defaultPrinterImpl().getReadOnlyProperty();
    }

    public static Printer getDefaultPrinter() {
        return (Printer)Printer.defaultPrinterProperty().get();
    }

    Printer(PrinterImpl printerImpl) {
        this.impl = printerImpl;
        printerImpl.setPrinter(this);
    }

    PrinterImpl getPrinterImpl() {
        return this.impl;
    }

    public String getName() {
        return this.impl.getName();
    }

    public PrinterAttributes getPrinterAttributes() {
        if (this.attributes == null) {
            this.attributes = new PrinterAttributes(this.impl);
        }
        return this.attributes;
    }

    JobSettings getDefaultJobSettings() {
        return this.impl.getDefaultJobSettings();
    }

    public PageLayout getDefaultPageLayout() {
        if (this.defPageLayout == null) {
            PrinterAttributes printerAttributes = this.getPrinterAttributes();
            this.defPageLayout = this.createPageLayout(printerAttributes.getDefaultPaper(), printerAttributes.getDefaultPageOrientation(), MarginType.DEFAULT);
        }
        return this.defPageLayout;
    }

    public PageLayout createPageLayout(Paper paper, PageOrientation pageOrientation, MarginType marginType) {
        double d2;
        double d3;
        double d4;
        if (paper == null || pageOrientation == null || marginType == null) {
            throw new NullPointerException("Parameters cannot be null");
        }
        Rectangle2D rectangle2D = this.impl.printableArea(paper);
        double d5 = paper.getWidth() / 72.0;
        double d6 = paper.getHeight() / 72.0;
        double d7 = rectangle2D.getMinX();
        double d8 = rectangle2D.getMinY();
        double d9 = d5 - rectangle2D.getMaxX();
        double d10 = d6 - rectangle2D.getMaxY();
        if (Math.abs(d7) < 0.01) {
            d7 = 0.0;
        }
        if (Math.abs(d9) < 0.01) {
            d9 = 0.0;
        }
        if (Math.abs(d8) < 0.01) {
            d8 = 0.0;
        }
        if (Math.abs(d10) < 0.01) {
            d10 = 0.0;
        }
        switch (marginType) {
            case DEFAULT: {
                d7 = d7 <= 0.75 ? 0.75 : d7;
                d9 = d9 <= 0.75 ? 0.75 : d9;
                d8 = d8 <= 0.75 ? 0.75 : d8;
                d10 = d10 <= 0.75 ? 0.75 : d10;
                break;
            }
            case EQUAL: {
                d4 = Math.max(d7, d9);
                d3 = Math.max(d8, d10);
                d8 = d10 = (d2 = Math.max(d4, d3));
                d9 = d10;
                d7 = d10;
                break;
            }
            case EQUAL_OPPOSITES: {
                d4 = Math.max(d7, d9);
                d3 = Math.max(d8, d10);
                d7 = d9 = d4;
                d8 = d10 = d3;
                break;
            }
        }
        switch (pageOrientation) {
            case LANDSCAPE: {
                d4 = d10;
                d3 = d8;
                d2 = d7;
                double d11 = d9;
                break;
            }
            case REVERSE_LANDSCAPE: {
                d4 = d8;
                d3 = d10;
                d2 = d9;
                double d11 = d7;
                break;
            }
            case REVERSE_PORTRAIT: {
                d4 = d9;
                d3 = d7;
                d2 = d10;
                double d11 = d8;
                break;
            }
            default: {
                d4 = d7;
                d3 = d9;
                d2 = d8;
                double d11 = d10;
            }
        }
        return new PageLayout(paper, pageOrientation, d4 *= 72.0, d3 *= 72.0, d2 *= 72.0, d11 *= 72.0);
    }

    public PageLayout createPageLayout(Paper paper, PageOrientation pageOrientation, double d2, double d3, double d4, double d5) {
        double d6;
        double d7;
        double d8;
        double d9;
        if (paper == null || pageOrientation == null) {
            throw new NullPointerException("Parameters cannot be null");
        }
        if (d2 < 0.0 || d3 < 0.0 || d4 < 0.0 || d5 < 0.0) {
            throw new IllegalArgumentException("Margins must be >= 0");
        }
        Rectangle2D rectangle2D = this.impl.printableArea(paper);
        double d10 = paper.getWidth() / 72.0;
        double d11 = paper.getHeight() / 72.0;
        double d12 = rectangle2D.getMinX();
        double d13 = rectangle2D.getMinY();
        double d14 = d10 - rectangle2D.getMaxX();
        double d15 = d11 - rectangle2D.getMaxY();
        d2 /= 72.0;
        d3 /= 72.0;
        d4 /= 72.0;
        d5 /= 72.0;
        boolean bl = false;
        if (pageOrientation == PageOrientation.PORTRAIT || pageOrientation == PageOrientation.REVERSE_PORTRAIT) {
            if (d2 + d3 > d10 || d4 + d5 > d11) {
                bl = true;
            }
        } else if (d2 + d3 > d11 || d4 + d5 > d10) {
            bl = true;
        }
        if (bl) {
            return this.createPageLayout(paper, pageOrientation, MarginType.DEFAULT);
        }
        switch (pageOrientation) {
            case LANDSCAPE: {
                d9 = d15;
                d8 = d13;
                d7 = d12;
                d6 = d14;
                break;
            }
            case REVERSE_LANDSCAPE: {
                d9 = d13;
                d8 = d15;
                d7 = d14;
                d6 = d12;
                break;
            }
            case REVERSE_PORTRAIT: {
                d9 = d14;
                d8 = d12;
                d7 = d15;
                d6 = d13;
                break;
            }
            default: {
                d9 = d12;
                d8 = d14;
                d7 = d13;
                d6 = d15;
            }
        }
        d9 = d2 >= d9 ? d2 : d9;
        d8 = d3 >= d8 ? d3 : d8;
        d7 = d4 >= d7 ? d4 : d7;
        d6 = d5 >= d6 ? d5 : d6;
        return new PageLayout(paper, pageOrientation, d9 *= 72.0, d8 *= 72.0, d7 *= 72.0, d6 *= 72.0);
    }

    public String toString() {
        return "Printer " + this.getName();
    }

    static {
        PrintHelper.setPrintAccessor(new PrintHelper.PrintAccessor(){

            @Override
            public PrintResolution createPrintResolution(int n2, int n3) {
                return new PrintResolution(n2, n3);
            }

            @Override
            public Paper createPaper(String string, double d2, double d3, Units units) {
                return new Paper(string, d2, d3, units);
            }

            @Override
            public PaperSource createPaperSource(String string) {
                return new PaperSource(string);
            }

            @Override
            public JobSettings createJobSettings(Printer printer) {
                return new JobSettings(printer);
            }

            @Override
            public Printer createPrinter(PrinterImpl printerImpl) {
                return new Printer(printerImpl);
            }

            @Override
            public PrinterImpl getPrinterImpl(Printer printer) {
                return printer.getPrinterImpl();
            }
        });
    }

    public static enum MarginType {
        DEFAULT,
        HARDWARE_MINIMUM,
        EQUAL,
        EQUAL_OPPOSITES;

    }
}

