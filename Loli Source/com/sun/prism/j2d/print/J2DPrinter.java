/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.j2d.print;

import com.sun.javafx.print.PrintHelper;
import com.sun.javafx.print.PrinterImpl;
import com.sun.javafx.print.Units;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javafx.geometry.Rectangle2D;
import javafx.print.Collation;
import javafx.print.JobSettings;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.PageRange;
import javafx.print.Paper;
import javafx.print.PaperSource;
import javafx.print.PrintColor;
import javafx.print.PrintQuality;
import javafx.print.PrintResolution;
import javafx.print.PrintSides;
import javafx.print.Printer;
import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.SetOfIntegerSyntax;
import javax.print.attribute.standard.Chromaticity;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.CopiesSupported;
import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.MediaTray;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PageRanges;
import javax.print.attribute.standard.PrinterResolution;
import javax.print.attribute.standard.SheetCollate;
import javax.print.attribute.standard.Sides;

public class J2DPrinter
implements PrinterImpl {
    private PrintService service;
    private Printer fxPrinter;
    private int defaultCopies = 0;
    private int maxCopies = 0;
    private Collation defaultCollation;
    private Set<Collation> collateSet;
    private PrintColor defColor;
    private Set<PrintColor> colorSet;
    private PrintSides defSides;
    private Set<PrintSides> sidesSet;
    private PageOrientation defOrient;
    private Set<PageOrientation> orientSet;
    private PrintResolution defRes;
    private Set<PrintResolution> resSet;
    private PrintQuality defQuality;
    private Set<PrintQuality> qualitySet;
    private Paper defPaper;
    private Set<Paper> paperSet;
    private static Map<MediaTray, PaperSource> preDefinedTrayMap = null;
    private static Map<MediaSizeName, Paper> predefinedPaperMap = null;
    private PaperSource defPaperSource;
    private Set<PaperSource> paperSourceSet;
    private Map<PaperSource, MediaTray> sourceToTrayMap;
    private Map<MediaTray, PaperSource> trayToSourceMap;
    private Map<MediaSizeName, Paper> mediaToPaperMap;
    private Map<Paper, MediaSizeName> paperToMediaMap;
    private PageLayout defaultLayout;

    public J2DPrinter(PrintService printService) {
        this.service = printService;
    }

    public Printer getPrinter() {
        return this.fxPrinter;
    }

    @Override
    public void setPrinter(Printer printer) {
        this.fxPrinter = printer;
    }

    public PrintService getService() {
        return this.service;
    }

    @Override
    public String getName() {
        return this.service.getName();
    }

    @Override
    public JobSettings getDefaultJobSettings() {
        return PrintHelper.createJobSettings(this.fxPrinter);
    }

    @Override
    public int defaultCopies() {
        if (this.defaultCopies > 0) {
            return this.defaultCopies;
        }
        try {
            Copies copies = (Copies)this.service.getDefaultAttributeValue(Copies.class);
            this.defaultCopies = copies.getValue();
        }
        catch (Exception exception) {
            this.defaultCopies = 1;
        }
        return this.defaultCopies;
    }

    @Override
    public int maxCopies() {
        int[][] arrn;
        if (this.maxCopies > 0) {
            return this.maxCopies;
        }
        SetOfIntegerSyntax setOfIntegerSyntax = null;
        try {
            setOfIntegerSyntax = (CopiesSupported)this.service.getSupportedAttributeValues(CopiesSupported.class, null, null);
        }
        catch (Exception exception) {
            // empty catch block
        }
        if (setOfIntegerSyntax != null && (arrn = setOfIntegerSyntax.getMembers()) != null && arrn.length > 0 && arrn[0].length > 0) {
            this.maxCopies = arrn[0][1];
        }
        if (this.maxCopies == 0) {
            this.maxCopies = 999;
        }
        return this.maxCopies;
    }

    @Override
    public PageRange defaultPageRange() {
        try {
            PageRanges pageRanges = (PageRanges)this.service.getDefaultAttributeValue(PageRanges.class);
            if (pageRanges == null) {
                return null;
            }
            int n2 = pageRanges.getMembers()[0][0];
            int n3 = pageRanges.getMembers()[0][1];
            if (n2 == 1 && n3 == Integer.MAX_VALUE) {
                return null;
            }
            return new PageRange(n2, n3);
        }
        catch (Exception exception) {
            return null;
        }
    }

    @Override
    public boolean supportsPageRanges() {
        return true;
    }

    SheetCollate getDefaultSheetCollate() {
        SheetCollate sheetCollate = null;
        try {
            sheetCollate = (SheetCollate)this.service.getDefaultAttributeValue(SheetCollate.class);
        }
        catch (Exception exception) {
            sheetCollate = SheetCollate.UNCOLLATED;
        }
        return sheetCollate;
    }

    @Override
    public Collation defaultCollation() {
        if (this.defaultCollation != null) {
            return this.defaultCollation;
        }
        SheetCollate sheetCollate = this.getDefaultSheetCollate();
        this.defaultCollation = sheetCollate == SheetCollate.COLLATED ? Collation.COLLATED : Collation.UNCOLLATED;
        return this.defaultCollation;
    }

    @Override
    public Set<Collation> supportedCollations() {
        if (this.collateSet == null) {
            TreeSet<Collation> treeSet = new TreeSet<Collation>();
            SheetCollate[] arrsheetCollate = null;
            try {
                arrsheetCollate = (SheetCollate[])this.service.getSupportedAttributeValues(SheetCollate.class, null, null);
            }
            catch (Exception exception) {
                // empty catch block
            }
            if (arrsheetCollate != null) {
                for (int i2 = 0; i2 < arrsheetCollate.length; ++i2) {
                    if (arrsheetCollate[i2] == SheetCollate.UNCOLLATED) {
                        treeSet.add(Collation.UNCOLLATED);
                    }
                    if (arrsheetCollate[i2] != SheetCollate.COLLATED) continue;
                    treeSet.add(Collation.COLLATED);
                }
            }
            this.collateSet = Collections.unmodifiableSet(treeSet);
        }
        return this.collateSet;
    }

    Chromaticity getDefaultChromaticity() {
        Chromaticity chromaticity = null;
        try {
            chromaticity = (Chromaticity)this.service.getDefaultAttributeValue(Chromaticity.class);
        }
        catch (Exception exception) {
            chromaticity = Chromaticity.COLOR;
        }
        return chromaticity;
    }

    @Override
    public PrintColor defaultPrintColor() {
        if (this.defColor != null) {
            return this.defColor;
        }
        Chromaticity chromaticity = this.getDefaultChromaticity();
        this.defColor = chromaticity == Chromaticity.COLOR ? PrintColor.COLOR : PrintColor.MONOCHROME;
        return this.defColor;
    }

    @Override
    public Set<PrintColor> supportedPrintColor() {
        if (this.colorSet == null) {
            TreeSet<PrintColor> treeSet = new TreeSet<PrintColor>();
            Chromaticity[] arrchromaticity = null;
            try {
                arrchromaticity = (Chromaticity[])this.service.getSupportedAttributeValues(Chromaticity.class, null, null);
            }
            catch (Exception exception) {
                // empty catch block
            }
            if (arrchromaticity != null) {
                for (int i2 = 0; i2 < arrchromaticity.length; ++i2) {
                    if (arrchromaticity[i2] == Chromaticity.COLOR) {
                        treeSet.add(PrintColor.COLOR);
                    }
                    if (arrchromaticity[i2] != Chromaticity.MONOCHROME) continue;
                    treeSet.add(PrintColor.MONOCHROME);
                }
            }
            this.colorSet = Collections.unmodifiableSet(treeSet);
        }
        return this.colorSet;
    }

    @Override
    public PrintSides defaultSides() {
        if (this.defSides != null) {
            return this.defSides;
        }
        Sides sides = (Sides)this.service.getDefaultAttributeValue(Sides.class);
        this.defSides = sides == null || sides == Sides.ONE_SIDED ? PrintSides.ONE_SIDED : (sides == Sides.DUPLEX ? PrintSides.DUPLEX : PrintSides.TUMBLE);
        return this.defSides;
    }

    @Override
    public Set<PrintSides> supportedSides() {
        if (this.sidesSet == null) {
            TreeSet<PrintSides> treeSet = new TreeSet<PrintSides>();
            Sides[] arrsides = null;
            try {
                arrsides = (Sides[])this.service.getSupportedAttributeValues(Sides.class, null, null);
            }
            catch (Exception exception) {
                // empty catch block
            }
            if (arrsides != null) {
                for (int i2 = 0; i2 < arrsides.length; ++i2) {
                    if (arrsides[i2] == Sides.ONE_SIDED) {
                        treeSet.add(PrintSides.ONE_SIDED);
                    }
                    if (arrsides[i2] == Sides.DUPLEX) {
                        treeSet.add(PrintSides.DUPLEX);
                    }
                    if (arrsides[i2] != Sides.TUMBLE) continue;
                    treeSet.add(PrintSides.TUMBLE);
                }
            }
            this.sidesSet = Collections.unmodifiableSet(treeSet);
        }
        return this.sidesSet;
    }

    static int getOrientID(PageOrientation pageOrientation) {
        if (pageOrientation == PageOrientation.LANDSCAPE) {
            return 0;
        }
        if (pageOrientation == PageOrientation.REVERSE_LANDSCAPE) {
            return 2;
        }
        return 1;
    }

    static OrientationRequested mapOrientation(PageOrientation pageOrientation) {
        if (pageOrientation == PageOrientation.REVERSE_PORTRAIT) {
            return OrientationRequested.REVERSE_PORTRAIT;
        }
        if (pageOrientation == PageOrientation.LANDSCAPE) {
            return OrientationRequested.LANDSCAPE;
        }
        if (pageOrientation == PageOrientation.REVERSE_LANDSCAPE) {
            return OrientationRequested.REVERSE_LANDSCAPE;
        }
        return OrientationRequested.PORTRAIT;
    }

    static PageOrientation reverseMapOrientation(OrientationRequested orientationRequested) {
        if (orientationRequested == OrientationRequested.REVERSE_PORTRAIT) {
            return PageOrientation.REVERSE_PORTRAIT;
        }
        if (orientationRequested == OrientationRequested.LANDSCAPE) {
            return PageOrientation.LANDSCAPE;
        }
        if (orientationRequested == OrientationRequested.REVERSE_LANDSCAPE) {
            return PageOrientation.REVERSE_LANDSCAPE;
        }
        return PageOrientation.PORTRAIT;
    }

    @Override
    public PageOrientation defaultOrientation() {
        if (this.defOrient == null) {
            OrientationRequested orientationRequested = (OrientationRequested)this.service.getDefaultAttributeValue(OrientationRequested.class);
            this.defOrient = J2DPrinter.reverseMapOrientation(orientationRequested);
        }
        return this.defOrient;
    }

    @Override
    public Set<PageOrientation> supportedOrientation() {
        if (this.orientSet != null) {
            return this.orientSet;
        }
        TreeSet<PageOrientation> treeSet = new TreeSet<PageOrientation>();
        OrientationRequested[] arrorientationRequested = null;
        try {
            arrorientationRequested = (OrientationRequested[])this.service.getSupportedAttributeValues(OrientationRequested.class, null, null);
        }
        catch (Exception exception) {
            // empty catch block
        }
        if (arrorientationRequested == null || arrorientationRequested.length == 0) {
            treeSet.add(this.defaultOrientation());
        } else {
            for (int i2 = 0; i2 < arrorientationRequested.length; ++i2) {
                if (arrorientationRequested[i2] == OrientationRequested.PORTRAIT) {
                    treeSet.add(PageOrientation.PORTRAIT);
                    continue;
                }
                if (arrorientationRequested[i2] == OrientationRequested.REVERSE_PORTRAIT) {
                    treeSet.add(PageOrientation.REVERSE_PORTRAIT);
                    continue;
                }
                if (arrorientationRequested[i2] == OrientationRequested.LANDSCAPE) {
                    treeSet.add(PageOrientation.LANDSCAPE);
                    continue;
                }
                treeSet.add(PageOrientation.REVERSE_LANDSCAPE);
            }
        }
        this.orientSet = Collections.unmodifiableSet(treeSet);
        return this.orientSet;
    }

    PrinterResolution getDefaultPrinterResolution() {
        PrinterResolution printerResolution = (PrinterResolution)this.service.getDefaultAttributeValue(PrinterResolution.class);
        if (printerResolution == null) {
            printerResolution = new PrinterResolution(300, 300, 100);
        }
        return printerResolution;
    }

    @Override
    public PrintResolution defaultPrintResolution() {
        if (this.defRes != null) {
            return this.defRes;
        }
        PrinterResolution printerResolution = this.getDefaultPrinterResolution();
        int n2 = printerResolution.getCrossFeedResolution(100);
        int n3 = printerResolution.getFeedResolution(100);
        this.defRes = PrintHelper.createPrintResolution(n2, n3);
        return this.defRes;
    }

    @Override
    public Set<PrintResolution> supportedPrintResolution() {
        if (this.resSet != null) {
            return this.resSet;
        }
        TreeSet<PrintResolution> treeSet = new TreeSet<PrintResolution>(PrintResolutionComparator.theComparator);
        PrinterResolution[] arrprinterResolution = null;
        try {
            arrprinterResolution = (PrinterResolution[])this.service.getSupportedAttributeValues(PrinterResolution.class, null, null);
        }
        catch (Exception exception) {
            // empty catch block
        }
        if (arrprinterResolution == null || arrprinterResolution.length == 0) {
            treeSet.add(this.defaultPrintResolution());
        } else {
            for (int i2 = 0; i2 < arrprinterResolution.length; ++i2) {
                int n2 = arrprinterResolution[i2].getCrossFeedResolution(100);
                int n3 = arrprinterResolution[i2].getFeedResolution(100);
                treeSet.add(PrintHelper.createPrintResolution(n2, n3));
            }
        }
        this.resSet = Collections.unmodifiableSet(treeSet);
        return this.resSet;
    }

    javax.print.attribute.standard.PrintQuality getDefaultPrintQuality() {
        javax.print.attribute.standard.PrintQuality printQuality = null;
        try {
            printQuality = (javax.print.attribute.standard.PrintQuality)this.service.getDefaultAttributeValue(javax.print.attribute.standard.PrintQuality.class);
        }
        catch (Exception exception) {
            printQuality = javax.print.attribute.standard.PrintQuality.NORMAL;
        }
        return printQuality;
    }

    @Override
    public PrintQuality defaultPrintQuality() {
        if (this.defQuality != null) {
            return this.defQuality;
        }
        javax.print.attribute.standard.PrintQuality printQuality = this.getDefaultPrintQuality();
        this.defQuality = printQuality == javax.print.attribute.standard.PrintQuality.DRAFT ? PrintQuality.DRAFT : (printQuality == javax.print.attribute.standard.PrintQuality.HIGH ? PrintQuality.HIGH : PrintQuality.NORMAL);
        return this.defQuality;
    }

    @Override
    public Set<PrintQuality> supportedPrintQuality() {
        if (this.qualitySet == null) {
            TreeSet<PrintQuality> treeSet = new TreeSet<PrintQuality>();
            javax.print.attribute.standard.PrintQuality[] arrprintQuality = null;
            try {
                arrprintQuality = (javax.print.attribute.standard.PrintQuality[])this.service.getSupportedAttributeValues(javax.print.attribute.standard.PrintQuality.class, null, null);
            }
            catch (Exception exception) {
                // empty catch block
            }
            if (arrprintQuality == null || arrprintQuality.length == 0) {
                treeSet.add(PrintQuality.NORMAL);
            } else {
                for (int i2 = 0; i2 < arrprintQuality.length; ++i2) {
                    if (arrprintQuality[i2] == javax.print.attribute.standard.PrintQuality.NORMAL) {
                        treeSet.add(PrintQuality.NORMAL);
                    }
                    if (arrprintQuality[i2] == javax.print.attribute.standard.PrintQuality.DRAFT) {
                        treeSet.add(PrintQuality.DRAFT);
                    }
                    if (arrprintQuality[i2] != javax.print.attribute.standard.PrintQuality.HIGH) continue;
                    treeSet.add(PrintQuality.HIGH);
                }
            }
            this.qualitySet = Collections.unmodifiableSet(treeSet);
        }
        return this.qualitySet;
    }

    Paper getPaperForMedia(Media media) {
        this.populateMedia();
        if (media == null || !(media instanceof MediaSizeName)) {
            return this.defaultPaper();
        }
        return this.getPaper((MediaSizeName)media);
    }

    @Override
    public Paper defaultPaper() {
        if (this.defPaper != null) {
            return this.defPaper;
        }
        Media media = (Media)this.service.getDefaultAttributeValue(Media.class);
        this.defPaper = media == null || !(media instanceof MediaSizeName) ? Paper.NA_LETTER : this.getPaper((MediaSizeName)media);
        return this.defPaper;
    }

    @Override
    public Set<Paper> supportedPapers() {
        if (this.paperSet == null) {
            this.populateMedia();
        }
        return this.paperSet;
    }

    private static void initPrefinedMediaMaps() {
        HashMap<MediaTray, PaperSource> hashMap;
        if (predefinedPaperMap == null) {
            hashMap = new HashMap<MediaTray, PaperSource>();
            hashMap.put((MediaTray)((Object)MediaSizeName.NA_LETTER), (PaperSource)((Object)Paper.NA_LETTER));
            hashMap.put((MediaTray)((Object)MediaSizeName.TABLOID), (PaperSource)((Object)Paper.TABLOID));
            hashMap.put((MediaTray)((Object)MediaSizeName.NA_LEGAL), (PaperSource)((Object)Paper.LEGAL));
            hashMap.put((MediaTray)((Object)MediaSizeName.EXECUTIVE), (PaperSource)((Object)Paper.EXECUTIVE));
            hashMap.put((MediaTray)((Object)MediaSizeName.NA_8X10), (PaperSource)((Object)Paper.NA_8X10));
            hashMap.put((MediaTray)((Object)MediaSizeName.MONARCH_ENVELOPE), (PaperSource)((Object)Paper.MONARCH_ENVELOPE));
            hashMap.put((MediaTray)((Object)MediaSizeName.NA_NUMBER_10_ENVELOPE), (PaperSource)((Object)Paper.NA_NUMBER_10_ENVELOPE));
            hashMap.put((MediaTray)((Object)MediaSizeName.ISO_A0), (PaperSource)((Object)Paper.A0));
            hashMap.put((MediaTray)((Object)MediaSizeName.ISO_A1), (PaperSource)((Object)Paper.A1));
            hashMap.put((MediaTray)((Object)MediaSizeName.ISO_A2), (PaperSource)((Object)Paper.A2));
            hashMap.put((MediaTray)((Object)MediaSizeName.ISO_A3), (PaperSource)((Object)Paper.A3));
            hashMap.put((MediaTray)((Object)MediaSizeName.ISO_A4), (PaperSource)((Object)Paper.A4));
            hashMap.put((MediaTray)((Object)MediaSizeName.ISO_A5), (PaperSource)((Object)Paper.A5));
            hashMap.put((MediaTray)((Object)MediaSizeName.ISO_A6), (PaperSource)((Object)Paper.A6));
            hashMap.put((MediaTray)((Object)MediaSizeName.C), (PaperSource)((Object)Paper.C));
            hashMap.put((MediaTray)((Object)MediaSizeName.ISO_DESIGNATED_LONG), (PaperSource)((Object)Paper.DESIGNATED_LONG));
            hashMap.put((MediaTray)((Object)MediaSizeName.JIS_B4), (PaperSource)((Object)Paper.JIS_B4));
            hashMap.put((MediaTray)((Object)MediaSizeName.JIS_B5), (PaperSource)((Object)Paper.JIS_B5));
            hashMap.put((MediaTray)((Object)MediaSizeName.JIS_B6), (PaperSource)((Object)Paper.JIS_B6));
            hashMap.put((MediaTray)((Object)MediaSizeName.JAPANESE_POSTCARD), (PaperSource)((Object)Paper.JAPANESE_POSTCARD));
            predefinedPaperMap = hashMap;
        }
        if (preDefinedTrayMap == null) {
            hashMap = new HashMap();
            hashMap.put(MediaTray.MAIN, PaperSource.MAIN);
            hashMap.put(MediaTray.MANUAL, PaperSource.MANUAL);
            hashMap.put(MediaTray.BOTTOM, PaperSource.BOTTOM);
            hashMap.put(MediaTray.MIDDLE, PaperSource.MIDDLE);
            hashMap.put(MediaTray.TOP, PaperSource.TOP);
            hashMap.put(MediaTray.SIDE, PaperSource.SIDE);
            hashMap.put(MediaTray.ENVELOPE, PaperSource.ENVELOPE);
            hashMap.put(MediaTray.LARGE_CAPACITY, PaperSource.LARGE_CAPACITY);
            preDefinedTrayMap = hashMap;
        }
    }

    private void populateMedia() {
        J2DPrinter.initPrefinedMediaMaps();
        if (this.paperSet != null) {
            return;
        }
        Media[] arrmedia = (Media[])this.service.getSupportedAttributeValues(Media.class, null, null);
        TreeSet<Paper> treeSet = new TreeSet<Paper>(PaperComparator.theComparator);
        TreeSet<PaperSource> treeSet2 = new TreeSet<PaperSource>(PaperSourceComparator.theComparator);
        if (arrmedia != null) {
            for (int i2 = 0; i2 < arrmedia.length; ++i2) {
                Media media = arrmedia[i2];
                if (media instanceof MediaSizeName) {
                    treeSet.add(this.addPaper((MediaSizeName)media));
                    continue;
                }
                if (!(media instanceof MediaTray)) continue;
                treeSet2.add(this.addPaperSource((MediaTray)media));
            }
        }
        this.paperSet = Collections.unmodifiableSet(treeSet);
        this.paperSourceSet = Collections.unmodifiableSet(treeSet2);
    }

    @Override
    public PaperSource defaultPaperSource() {
        if (this.defPaperSource != null) {
            return this.defPaperSource;
        }
        this.defPaperSource = PaperSource.AUTOMATIC;
        return this.defPaperSource;
    }

    @Override
    public Set<PaperSource> supportedPaperSources() {
        if (this.paperSourceSet == null) {
            this.populateMedia();
        }
        return this.paperSourceSet;
    }

    final synchronized PaperSource getPaperSource(MediaTray mediaTray) {
        PaperSource paperSource;
        if (this.paperSourceSet == null) {
            this.populateMedia();
        }
        if ((paperSource = this.trayToSourceMap.get(mediaTray)) != null) {
            return paperSource;
        }
        return this.addPaperSource(mediaTray);
    }

    MediaTray getTrayForPaperSource(PaperSource paperSource) {
        if (this.paperSourceSet == null) {
            this.populateMedia();
        }
        return this.sourceToTrayMap.get(paperSource);
    }

    private final synchronized PaperSource addPaperSource(MediaTray mediaTray) {
        PaperSource paperSource = preDefinedTrayMap.get(mediaTray);
        if (paperSource == null) {
            paperSource = PrintHelper.createPaperSource(mediaTray.toString());
        }
        if (this.trayToSourceMap == null) {
            this.trayToSourceMap = new HashMap<MediaTray, PaperSource>();
        }
        this.trayToSourceMap.put(mediaTray, paperSource);
        if (this.sourceToTrayMap == null) {
            this.sourceToTrayMap = new HashMap<PaperSource, MediaTray>();
        }
        this.sourceToTrayMap.put(paperSource, mediaTray);
        return paperSource;
    }

    private final synchronized Paper addPaper(MediaSizeName mediaSizeName) {
        MediaSize mediaSize;
        Paper paper;
        if (this.mediaToPaperMap == null) {
            this.mediaToPaperMap = new HashMap<MediaSizeName, Paper>();
            this.paperToMediaMap = new HashMap<Paper, MediaSizeName>();
        }
        if ((paper = predefinedPaperMap.get(mediaSizeName)) == null && (mediaSize = MediaSize.getMediaSizeForName(mediaSizeName)) != null) {
            double d2 = (double)mediaSize.getX(1) / 1000.0;
            double d3 = (double)mediaSize.getY(1) / 1000.0;
            paper = PrintHelper.createPaper(mediaSizeName.toString(), d2, d3, Units.MM);
        }
        if (paper == null) {
            paper = Paper.NA_LETTER;
        }
        this.paperToMediaMap.put(paper, mediaSizeName);
        this.mediaToPaperMap.put(mediaSizeName, paper);
        return paper;
    }

    private Paper getPaper(MediaSizeName mediaSizeName) {
        this.populateMedia();
        Paper paper = this.mediaToPaperMap.get(mediaSizeName);
        if (paper == null) {
            paper = Paper.NA_LETTER;
        }
        return paper;
    }

    private MediaSizeName getMediaSizeName(Paper paper) {
        this.populateMedia();
        MediaSizeName mediaSizeName = this.paperToMediaMap.get(paper);
        if (mediaSizeName == null) {
            mediaSizeName = MediaSize.findMedia((float)paper.getWidth(), (float)paper.getHeight(), 352);
        }
        return mediaSizeName;
    }

    @Override
    public Rectangle2D printableArea(Paper paper) {
        Rectangle2D rectangle2D = null;
        MediaSizeName mediaSizeName = this.getMediaSizeName(paper);
        if (mediaSizeName != null) {
            HashPrintRequestAttributeSet hashPrintRequestAttributeSet = new HashPrintRequestAttributeSet();
            hashPrintRequestAttributeSet.add(mediaSizeName);
            MediaPrintableArea[] arrmediaPrintableArea = (MediaPrintableArea[])this.service.getSupportedAttributeValues(MediaPrintableArea.class, null, hashPrintRequestAttributeSet);
            if (arrmediaPrintableArea != null && arrmediaPrintableArea.length > 0 && arrmediaPrintableArea[0] != null) {
                int n2 = 25400;
                rectangle2D = new Rectangle2D(arrmediaPrintableArea[0].getX(n2), arrmediaPrintableArea[0].getY(n2), arrmediaPrintableArea[0].getWidth(n2), arrmediaPrintableArea[0].getHeight(n2));
            }
        }
        if (rectangle2D == null) {
            double d2 = paper.getWidth() / 72.0;
            double d3 = paper.getHeight() / 72.0;
            double d4 = d2 < 3.0 ? 0.75 * d2 : d2 - 1.5;
            double d5 = d3 < 3.0 ? 0.75 * d3 : d3 - 1.5;
            double d6 = (d2 - d4) / 2.0;
            double d7 = (d3 - d5) / 2.0;
            rectangle2D = new Rectangle2D(d6, d7, d4, d5);
        }
        return rectangle2D;
    }

    PageLayout defaultPageLayout() {
        if (this.defaultLayout == null) {
            Paper paper = this.defaultPaper();
            PageOrientation pageOrientation = this.defaultOrientation();
            this.defaultLayout = this.fxPrinter.createPageLayout(paper, pageOrientation, Printer.MarginType.DEFAULT);
        }
        return this.defaultLayout;
    }

    private static class PaperSourceComparator
    implements Comparator<PaperSource> {
        static final PaperSourceComparator theComparator = new PaperSourceComparator();

        private PaperSourceComparator() {
        }

        @Override
        public int compare(PaperSource paperSource, PaperSource paperSource2) {
            return paperSource.getName().compareTo(paperSource2.getName());
        }
    }

    private static class PaperComparator
    implements Comparator<Paper> {
        static final PaperComparator theComparator = new PaperComparator();

        private PaperComparator() {
        }

        @Override
        public int compare(Paper paper, Paper paper2) {
            return paper.getName().compareTo(paper2.getName());
        }
    }

    private static class PrintResolutionComparator
    implements Comparator<PrintResolution> {
        static final PrintResolutionComparator theComparator = new PrintResolutionComparator();

        private PrintResolutionComparator() {
        }

        @Override
        public int compare(PrintResolution printResolution, PrintResolution printResolution2) {
            long l2;
            long l3 = printResolution.getCrossFeedResolution() * printResolution.getFeedResolution();
            if (l3 == (l2 = (long)(printResolution2.getCrossFeedResolution() * printResolution2.getFeedResolution()))) {
                return 0;
            }
            if (l3 < l2) {
                return -1;
            }
            return 1;
        }
    }
}

