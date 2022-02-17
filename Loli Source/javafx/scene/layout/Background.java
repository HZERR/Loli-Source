/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.layout;

import com.sun.javafx.UnmodifiableArrayList;
import com.sun.javafx.css.SubCssMetaData;
import com.sun.javafx.css.converters.InsetsConverter;
import com.sun.javafx.css.converters.PaintConverter;
import com.sun.javafx.css.converters.URLConverter;
import com.sun.javafx.scene.layout.region.CornerRadiiConverter;
import com.sun.javafx.scene.layout.region.LayeredBackgroundPositionConverter;
import com.sun.javafx.scene.layout.region.LayeredBackgroundSizeConverter;
import com.sun.javafx.scene.layout.region.RepeatStruct;
import com.sun.javafx.scene.layout.region.RepeatStructConverter;
import com.sun.javafx.tk.PlatformImage;
import com.sun.javafx.tk.Toolkit;
import com.sun.prism.Image;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javafx.beans.NamedArg;
import javafx.css.CssMetaData;
import javafx.css.StyleConverter;
import javafx.css.Styleable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public final class Background {
    static final CssMetaData<Node, Paint[]> BACKGROUND_COLOR = new SubCssMetaData<Paint[]>("-fx-background-color", (StyleConverter)PaintConverter.SequenceConverter.getInstance(), new Paint[]{Color.TRANSPARENT});
    static final CssMetaData<Node, CornerRadii[]> BACKGROUND_RADIUS = new SubCssMetaData<CornerRadii[]>("-fx-background-radius", (StyleConverter)CornerRadiiConverter.getInstance(), new CornerRadii[]{CornerRadii.EMPTY});
    static final CssMetaData<Node, Insets[]> BACKGROUND_INSETS = new SubCssMetaData<Insets[]>("-fx-background-insets", (StyleConverter)InsetsConverter.SequenceConverter.getInstance(), new Insets[]{Insets.EMPTY});
    static final CssMetaData<Node, javafx.scene.image.Image[]> BACKGROUND_IMAGE = new SubCssMetaData<javafx.scene.image.Image[]>("-fx-background-image", (StyleConverter)URLConverter.SequenceConverter.getInstance());
    static final CssMetaData<Node, RepeatStruct[]> BACKGROUND_REPEAT = new SubCssMetaData<RepeatStruct[]>("-fx-background-repeat", (StyleConverter)RepeatStructConverter.getInstance(), new RepeatStruct[]{new RepeatStruct(BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT)});
    static final CssMetaData<Node, BackgroundPosition[]> BACKGROUND_POSITION = new SubCssMetaData<BackgroundPosition[]>("-fx-background-position", (StyleConverter)LayeredBackgroundPositionConverter.getInstance(), new BackgroundPosition[]{BackgroundPosition.DEFAULT});
    static final CssMetaData<Node, BackgroundSize[]> BACKGROUND_SIZE = new SubCssMetaData<BackgroundSize[]>("-fx-background-size", (StyleConverter)LayeredBackgroundSizeConverter.getInstance(), new BackgroundSize[]{BackgroundSize.DEFAULT});
    private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES = Collections.unmodifiableList(Arrays.asList(BACKGROUND_COLOR, BACKGROUND_INSETS, BACKGROUND_RADIUS, BACKGROUND_IMAGE, BACKGROUND_REPEAT, BACKGROUND_POSITION, BACKGROUND_SIZE));
    public static final Background EMPTY = new Background((BackgroundFill[])null, null);
    final List<BackgroundFill> fills;
    final List<BackgroundImage> images;
    final Insets outsets;
    private final boolean hasOpaqueFill;
    private final double opaqueFillTop;
    private final double opaqueFillRight;
    private final double opaqueFillBottom;
    private final double opaqueFillLeft;
    final boolean hasPercentageBasedOpaqueFills;
    final boolean hasPercentageBasedFills;
    private final int hash;

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return STYLEABLES;
    }

    public final List<BackgroundFill> getFills() {
        return this.fills;
    }

    public final List<BackgroundImage> getImages() {
        return this.images;
    }

    public final Insets getOutsets() {
        return this.outsets;
    }

    public final boolean isEmpty() {
        return this.fills.isEmpty() && this.images.isEmpty();
    }

    public Background(BackgroundFill ... arrbackgroundFill) {
        this(arrbackgroundFill, (BackgroundImage[])null);
    }

    public Background(BackgroundImage ... arrbackgroundImage) {
        this((BackgroundFill[])null, arrbackgroundImage);
    }

    public Background(@NamedArg(value="fills") List<BackgroundFill> list, @NamedArg(value="images") List<BackgroundImage> list2) {
        this(list == null ? null : list.toArray(new BackgroundFill[list.size()]), list2 == null ? null : list2.toArray(new BackgroundImage[list2.size()]));
    }

    public Background(@NamedArg(value="fills") BackgroundFill[] arrbackgroundFill, @NamedArg(value="images") BackgroundImage[] arrbackgroundImage) {
        Object object;
        int n2;
        int n3;
        Object[] arrobject;
        double d2 = 0.0;
        double d3 = 0.0;
        double d4 = 0.0;
        double d5 = 0.0;
        boolean bl = false;
        boolean bl2 = false;
        boolean bl3 = false;
        if (arrbackgroundFill == null || arrbackgroundFill.length == 0) {
            this.fills = Collections.emptyList();
        } else {
            arrobject = new BackgroundFill[arrbackgroundFill.length];
            n3 = 0;
            for (n2 = 0; n2 < arrbackgroundFill.length; ++n2) {
                object = arrbackgroundFill[n2];
                if (object == null) continue;
                arrobject[n3++] = object;
                Insets insets = ((BackgroundFill)object).getInsets();
                double d6 = insets.getTop();
                double d7 = insets.getRight();
                double d8 = insets.getBottom();
                double d9 = insets.getLeft();
                d2 = d2 <= d6 ? d2 : d6;
                d3 = d3 <= d7 ? d3 : d7;
                d4 = d4 <= d8 ? d4 : d8;
                d5 = d5 <= d9 ? d5 : d9;
                boolean bl4 = object.getRadii().hasPercentBasedRadii;
                bl2 |= bl4;
                if (!((BackgroundFill)object).fill.isOpaque()) continue;
                bl3 = true;
                if (!bl4) continue;
                bl = true;
            }
            this.fills = new UnmodifiableArrayList<BackgroundFill>(arrobject, n3);
        }
        this.hasPercentageBasedFills = bl2;
        this.outsets = new Insets(Math.max(0.0, -d2), Math.max(0.0, -d3), Math.max(0.0, -d4), Math.max(0.0, -d5));
        if (arrbackgroundImage == null || arrbackgroundImage.length == 0) {
            this.images = Collections.emptyList();
        } else {
            arrobject = new BackgroundImage[arrbackgroundImage.length];
            n3 = 0;
            for (n2 = 0; n2 < arrbackgroundImage.length; ++n2) {
                object = arrbackgroundImage[n2];
                if (object == null) continue;
                arrobject[n3++] = object;
            }
            this.images = new UnmodifiableArrayList<Object>(arrobject, n3);
        }
        this.hasOpaqueFill = bl3;
        if (bl) {
            this.opaqueFillTop = Double.NaN;
            this.opaqueFillRight = Double.NaN;
            this.opaqueFillBottom = Double.NaN;
            this.opaqueFillLeft = Double.NaN;
        } else {
            arrobject = new double[4];
            this.computeOpaqueInsets(1.0, 1.0, true, (double[])arrobject);
            this.opaqueFillTop = (double)arrobject[0];
            this.opaqueFillRight = (double)arrobject[1];
            this.opaqueFillBottom = (double)arrobject[2];
            this.opaqueFillLeft = (double)arrobject[3];
        }
        this.hasPercentageBasedOpaqueFills = bl;
        int n4 = this.fills.hashCode();
        this.hash = n4 = 31 * n4 + this.images.hashCode();
    }

    public boolean isFillPercentageBased() {
        return this.hasPercentageBasedFills;
    }

    void computeOpaqueInsets(double d2, double d3, double[] arrd) {
        this.computeOpaqueInsets(d2, d3, false, arrd);
    }

    private void computeOpaqueInsets(double d2, double d3, boolean bl, double[] arrd) {
        double d4;
        double d5;
        double d6;
        double d7;
        double d8;
        double d9 = Double.NaN;
        double d10 = Double.NaN;
        double d11 = Double.NaN;
        double d12 = Double.NaN;
        if (this.hasOpaqueFill) {
            if (!bl && !this.hasPercentageBasedOpaqueFills) {
                d9 = this.opaqueFillTop;
                d10 = this.opaqueFillRight;
                d11 = this.opaqueFillBottom;
                d12 = this.opaqueFillLeft;
            } else {
                int n2 = this.fills.size();
                for (int i2 = 0; i2 < n2; ++i2) {
                    boolean bl2;
                    BackgroundFill object = this.fills.get(i2);
                    Insets bl8 = object.getInsets();
                    double d13 = bl8.getTop();
                    double d14 = bl8.getRight();
                    double d15 = bl8.getBottom();
                    double d16 = bl8.getLeft();
                    if (!object.fill.isOpaque()) continue;
                    CornerRadii cornerRadii = object.getRadii();
                    d8 = cornerRadii.isTopLeftHorizontalRadiusAsPercentage() ? d2 * cornerRadii.getTopLeftHorizontalRadius() : cornerRadii.getTopLeftHorizontalRadius();
                    d7 = cornerRadii.isTopLeftVerticalRadiusAsPercentage() ? d3 * cornerRadii.getTopLeftVerticalRadius() : cornerRadii.getTopLeftVerticalRadius();
                    d6 = cornerRadii.isTopRightVerticalRadiusAsPercentage() ? d3 * cornerRadii.getTopRightVerticalRadius() : cornerRadii.getTopRightVerticalRadius();
                    d5 = cornerRadii.isTopRightHorizontalRadiusAsPercentage() ? d2 * cornerRadii.getTopRightHorizontalRadius() : cornerRadii.getTopRightHorizontalRadius();
                    d4 = cornerRadii.isBottomRightHorizontalRadiusAsPercentage() ? d2 * cornerRadii.getBottomRightHorizontalRadius() : cornerRadii.getBottomRightHorizontalRadius();
                    double d17 = cornerRadii.isBottomRightVerticalRadiusAsPercentage() ? d3 * cornerRadii.getBottomRightVerticalRadius() : cornerRadii.getBottomRightVerticalRadius();
                    double d18 = cornerRadii.isBottomLeftVerticalRadiusAsPercentage() ? d3 * cornerRadii.getBottomLeftVerticalRadius() : cornerRadii.getBottomLeftVerticalRadius();
                    double d19 = cornerRadii.isBottomLeftHorizontalRadiusAsPercentage() ? d2 * cornerRadii.getBottomLeftHorizontalRadius() : cornerRadii.getBottomLeftHorizontalRadius();
                    double d20 = d13 + Math.max(d7, d6) / 2.0;
                    double d21 = d14 + Math.max(d5, d4) / 2.0;
                    double d22 = d15 + Math.max(d18, d17) / 2.0;
                    double d23 = d16 + Math.max(d8, d19) / 2.0;
                    if (Double.isNaN(d9)) {
                        d9 = d20;
                        d10 = d21;
                        d11 = d22;
                        d12 = d23;
                        continue;
                    }
                    boolean bl3 = d20 >= d9;
                    boolean bl4 = d21 >= d10;
                    boolean bl5 = d22 >= d11;
                    boolean bl6 = bl2 = d23 >= d12;
                    if (bl3 && bl4 && bl5 && bl2) continue;
                    if (!(bl3 || bl4 || bl5 || bl2)) {
                        d9 = d13;
                        d10 = d14;
                        d11 = d15;
                        d12 = d16;
                        continue;
                    }
                    if (d23 == d12 && d21 == d10) {
                        d9 = Math.min(d20, d9);
                        d11 = Math.min(d22, d11);
                        continue;
                    }
                    if (d20 != d9 || d22 != d11) continue;
                    d12 = Math.min(d23, d12);
                    d10 = Math.min(d21, d10);
                }
            }
        }
        Toolkit.ImageAccessor imageAccessor = Toolkit.getImageAccessor();
        for (BackgroundImage backgroundImage : this.images) {
            boolean bl7;
            if (backgroundImage.opaque == null) {
                PlatformImage platformImage = (PlatformImage)imageAccessor.getImageProperty(backgroundImage.image).get();
                if (platformImage == null || !(platformImage instanceof Image)) continue;
                backgroundImage.opaque = ((Image)platformImage).isOpaque();
            }
            if (!backgroundImage.opaque.booleanValue()) continue;
            if (backgroundImage.size.cover || backgroundImage.size.height == -1.0 && backgroundImage.size.width == -1.0 && backgroundImage.size.widthAsPercentage && backgroundImage.size.heightAsPercentage) {
                d9 = Double.isNaN(d9) ? 0.0 : Math.min(0.0, d9);
                d10 = Double.isNaN(d10) ? 0.0 : Math.min(0.0, d10);
                d11 = Double.isNaN(d11) ? 0.0 : Math.min(0.0, d11);
                d12 = Double.isNaN(d12) ? 0.0 : Math.min(0.0, d12);
                break;
            }
            if (backgroundImage.repeatX == BackgroundRepeat.SPACE || backgroundImage.repeatY == BackgroundRepeat.SPACE) {
                backgroundImage.opaque = false;
                continue;
            }
            boolean bl8 = backgroundImage.repeatX == BackgroundRepeat.REPEAT || backgroundImage.repeatX == BackgroundRepeat.ROUND;
            boolean bl9 = bl7 = backgroundImage.repeatY == BackgroundRepeat.REPEAT || backgroundImage.repeatY == BackgroundRepeat.ROUND;
            if (bl8 && bl7) {
                d9 = Double.isNaN(d9) ? 0.0 : Math.min(0.0, d9);
                d10 = Double.isNaN(d10) ? 0.0 : Math.min(0.0, d10);
                d11 = Double.isNaN(d11) ? 0.0 : Math.min(0.0, d11);
                d12 = Double.isNaN(d12) ? 0.0 : Math.min(0.0, d12);
                break;
            }
            double d24 = backgroundImage.size.widthAsPercentage ? backgroundImage.size.width * d2 : backgroundImage.size.width;
            double d25 = backgroundImage.size.heightAsPercentage ? backgroundImage.size.height * d3 : backgroundImage.size.height;
            double d26 = backgroundImage.image.getWidth();
            double d27 = backgroundImage.image.getHeight();
            if (backgroundImage.size.contain) {
                d6 = d2 / d26;
                d5 = d3 / d27;
                d4 = Math.min(d6, d5);
                d8 = Math.ceil(d4 * d26);
                d7 = Math.ceil(d4 * d27);
            } else if (backgroundImage.size.width >= 0.0 && backgroundImage.size.height >= 0.0) {
                d8 = d24;
                d7 = d25;
            } else if (d24 >= 0.0) {
                d8 = d24;
                d6 = d8 / d26;
                d7 = d27 * d6;
            } else if (d25 >= 0.0) {
                d7 = d25;
                d6 = d7 / d27;
                d8 = d26 * d6;
            } else {
                d8 = d26;
                d7 = d27;
            }
            d9 = Double.isNaN(d9) ? 0.0 : Math.min(0.0, d9);
            d10 = Double.isNaN(d10) ? d2 - d8 : Math.min(d2 - d8, d10);
            d11 = Double.isNaN(d11) ? d3 - d7 : Math.min(d3 - d7, d11);
            d12 = Double.isNaN(d12) ? 0.0 : Math.min(0.0, d12);
        }
        arrd[0] = d9;
        arrd[1] = d10;
        arrd[2] = d11;
        arrd[3] = d12;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        Background background = (Background)object;
        if (this.hash != background.hash) {
            return false;
        }
        if (!this.fills.equals(background.fills)) {
            return false;
        }
        return this.images.equals(background.images);
    }

    public int hashCode() {
        return this.hash;
    }
}

