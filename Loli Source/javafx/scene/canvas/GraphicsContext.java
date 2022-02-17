/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.canvas;

import com.sun.javafx.geom.Arc2D;
import com.sun.javafx.geom.IllegalPathStateException;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import com.sun.javafx.image.BytePixelGetter;
import com.sun.javafx.image.BytePixelSetter;
import com.sun.javafx.image.ByteToBytePixelConverter;
import com.sun.javafx.image.IntPixelGetter;
import com.sun.javafx.image.IntToBytePixelConverter;
import com.sun.javafx.image.PixelConverter;
import com.sun.javafx.image.PixelGetter;
import com.sun.javafx.image.PixelUtils;
import com.sun.javafx.image.impl.ByteBgraPre;
import com.sun.javafx.sg.prism.GrowableDataBuffer;
import com.sun.javafx.tk.Toolkit;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.LinkedList;
import javafx.geometry.NodeOrientation;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Affine;

public final class GraphicsContext {
    Canvas theCanvas;
    Path2D path;
    boolean pathDirty;
    State curState;
    LinkedList<State> stateStack;
    LinkedList<Path2D> clipStack;
    private float[] coords = new float[6];
    private static final byte[] pgtype = new byte[]{41, 42, 43, 44, 45};
    private static final int[] numsegs = new int[]{2, 2, 4, 6, 0};
    private float[] polybuf = new float[512];
    private boolean txdirty;
    private PixelWriter writer;

    GraphicsContext(Canvas canvas) {
        this.theCanvas = canvas;
        this.path = new Path2D();
        this.pathDirty = true;
        this.curState = new State();
        this.stateStack = new LinkedList();
        this.clipStack = new LinkedList();
    }

    private GrowableDataBuffer getBuffer() {
        return this.theCanvas.getBuffer();
    }

    private void markPathDirty() {
        this.pathDirty = true;
    }

    private void writePath(byte by) {
        this.updateTransform();
        GrowableDataBuffer growableDataBuffer = this.getBuffer();
        if (this.pathDirty) {
            growableDataBuffer.putByte((byte)40);
            PathIterator pathIterator = this.path.getPathIterator(null);
            while (!pathIterator.isDone()) {
                int n2 = pathIterator.currentSegment(this.coords);
                growableDataBuffer.putByte(pgtype[n2]);
                for (int i2 = 0; i2 < numsegs[n2]; ++i2) {
                    growableDataBuffer.putFloat(this.coords[i2]);
                }
                pathIterator.next();
            }
            growableDataBuffer.putByte((byte)46);
            this.pathDirty = false;
        }
        growableDataBuffer.putByte(by);
    }

    private void writePaint(Paint paint, byte by) {
        GrowableDataBuffer growableDataBuffer = this.getBuffer();
        growableDataBuffer.putByte(by);
        growableDataBuffer.putObject(Toolkit.getPaintAccessor().getPlatformPaint(paint));
    }

    private void writeArcType(ArcType arcType) {
        byte by;
        switch (arcType) {
            case OPEN: {
                by = 0;
                break;
            }
            case CHORD: {
                by = 1;
                break;
            }
            case ROUND: {
                by = 2;
                break;
            }
            default: {
                return;
            }
        }
        this.writeParam(by, (byte)15);
    }

    private void writeRectParams(GrowableDataBuffer growableDataBuffer, double d2, double d3, double d4, double d5, byte by) {
        growableDataBuffer.putByte(by);
        growableDataBuffer.putFloat((float)d2);
        growableDataBuffer.putFloat((float)d3);
        growableDataBuffer.putFloat((float)d4);
        growableDataBuffer.putFloat((float)d5);
    }

    private void writeOp4(double d2, double d3, double d4, double d5, byte by) {
        this.updateTransform();
        this.writeRectParams(this.getBuffer(), d2, d3, d4, d5, by);
    }

    private void writeOp6(double d2, double d3, double d4, double d5, double d6, double d7, byte by) {
        this.updateTransform();
        GrowableDataBuffer growableDataBuffer = this.getBuffer();
        growableDataBuffer.putByte(by);
        growableDataBuffer.putFloat((float)d2);
        growableDataBuffer.putFloat((float)d3);
        growableDataBuffer.putFloat((float)d4);
        growableDataBuffer.putFloat((float)d5);
        growableDataBuffer.putFloat((float)d6);
        growableDataBuffer.putFloat((float)d7);
    }

    private void flushPolyBuf(GrowableDataBuffer growableDataBuffer, float[] arrf, int n2, byte by) {
        this.curState.transform.transform(arrf, 0, arrf, 0, n2 / 2);
        for (int i2 = 0; i2 < n2; i2 += 2) {
            growableDataBuffer.putByte(by);
            growableDataBuffer.putFloat(arrf[i2]);
            growableDataBuffer.putFloat(arrf[i2 + 1]);
            by = (byte)42;
        }
    }

    private void writePoly(double[] arrd, double[] arrd2, int n2, boolean bl, byte by) {
        if (arrd == null || arrd2 == null) {
            return;
        }
        GrowableDataBuffer growableDataBuffer = this.getBuffer();
        growableDataBuffer.putByte((byte)40);
        int n3 = 0;
        int n4 = 41;
        for (int i2 = 0; i2 < n2; ++i2) {
            if (n3 >= this.polybuf.length) {
                this.flushPolyBuf(growableDataBuffer, this.polybuf, n3, (byte)n4);
                n3 = 0;
                n4 = 42;
            }
            this.polybuf[n3++] = (float)arrd[i2];
            this.polybuf[n3++] = (float)arrd2[i2];
        }
        this.flushPolyBuf(growableDataBuffer, this.polybuf, n3, (byte)n4);
        if (bl) {
            growableDataBuffer.putByte((byte)45);
        }
        growableDataBuffer.putByte((byte)46);
        this.updateTransform();
        growableDataBuffer.putByte(by);
        this.markPathDirty();
    }

    private void writeImage(Image image, double d2, double d3, double d4, double d5) {
        if (image == null || image.getProgress() < 1.0) {
            return;
        }
        Object object = image.impl_getPlatformImage();
        if (object == null) {
            return;
        }
        this.updateTransform();
        GrowableDataBuffer growableDataBuffer = this.getBuffer();
        this.writeRectParams(growableDataBuffer, d2, d3, d4, d5, (byte)50);
        growableDataBuffer.putObject(object);
    }

    private void writeImage(Image image, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9) {
        if (image == null || image.getProgress() < 1.0) {
            return;
        }
        Object object = image.impl_getPlatformImage();
        if (object == null) {
            return;
        }
        this.updateTransform();
        GrowableDataBuffer growableDataBuffer = this.getBuffer();
        this.writeRectParams(growableDataBuffer, d2, d3, d4, d5, (byte)51);
        growableDataBuffer.putFloat((float)d6);
        growableDataBuffer.putFloat((float)d7);
        growableDataBuffer.putFloat((float)d8);
        growableDataBuffer.putFloat((float)d9);
        growableDataBuffer.putObject(object);
    }

    private void writeText(String string, double d2, double d3, double d4, byte by) {
        if (string == null) {
            return;
        }
        this.updateTransform();
        GrowableDataBuffer growableDataBuffer = this.getBuffer();
        growableDataBuffer.putByte(by);
        growableDataBuffer.putFloat((float)d2);
        growableDataBuffer.putFloat((float)d3);
        growableDataBuffer.putFloat((float)d4);
        growableDataBuffer.putBoolean(this.theCanvas.getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT);
        growableDataBuffer.putObject(string);
    }

    void writeParam(double d2, byte by) {
        GrowableDataBuffer growableDataBuffer = this.getBuffer();
        growableDataBuffer.putByte(by);
        growableDataBuffer.putFloat((float)d2);
    }

    private void writeParam(byte by, byte by2) {
        GrowableDataBuffer growableDataBuffer = this.getBuffer();
        growableDataBuffer.putByte(by2);
        growableDataBuffer.putByte(by);
    }

    private void updateTransform() {
        if (this.txdirty) {
            this.txdirty = false;
            GrowableDataBuffer growableDataBuffer = this.getBuffer();
            growableDataBuffer.putByte((byte)11);
            growableDataBuffer.putDouble(this.curState.transform.getMxx());
            growableDataBuffer.putDouble(this.curState.transform.getMxy());
            growableDataBuffer.putDouble(this.curState.transform.getMxt());
            growableDataBuffer.putDouble(this.curState.transform.getMyx());
            growableDataBuffer.putDouble(this.curState.transform.getMyy());
            growableDataBuffer.putDouble(this.curState.transform.getMyt());
        }
    }

    void updateDimensions() {
        GrowableDataBuffer growableDataBuffer = this.getBuffer();
        growableDataBuffer.putByte((byte)71);
        growableDataBuffer.putFloat((float)this.theCanvas.getWidth());
        growableDataBuffer.putFloat((float)this.theCanvas.getHeight());
    }

    private void reset() {
        GrowableDataBuffer growableDataBuffer = this.getBuffer();
        if (growableDataBuffer.writeValuePosition() > 1024 || this.theCanvas.isRendererFallingBehind()) {
            growableDataBuffer.reset();
            growableDataBuffer.putByte((byte)70);
            this.updateDimensions();
            this.txdirty = true;
            this.pathDirty = true;
            State state = this.curState;
            int n2 = this.curState.numClipPaths;
            this.curState = new State();
            for (int i2 = 0; i2 < n2; ++i2) {
                Path2D path2D = this.clipStack.get(i2);
                growableDataBuffer.putByte((byte)13);
                growableDataBuffer.putObject(path2D);
            }
            this.curState.numClipPaths = n2;
            state.restore(this);
        }
    }

    private void resetIfCovers(Paint paint, double d2, double d3, double d4, double d5) {
        Affine2D affine2D = this.curState.transform;
        if (affine2D.isTranslateOrIdentity()) {
            d3 += affine2D.getMyt();
            if ((d2 += affine2D.getMxt()) > 0.0 || d3 > 0.0 || d2 + d4 < this.theCanvas.getWidth() || d3 + d5 < this.theCanvas.getHeight()) {
                return;
            }
        } else {
            return;
        }
        if (paint != null) {
            if (this.curState.blendop != BlendMode.SRC_OVER) {
                return;
            }
            if (!paint.isOpaque() || this.curState.globalAlpha < 1.0) {
                return;
            }
        }
        if (this.curState.numClipPaths > 0) {
            return;
        }
        if (this.curState.effect != null) {
            return;
        }
        this.reset();
    }

    public Canvas getCanvas() {
        return this.theCanvas;
    }

    public void save() {
        this.stateStack.push(this.curState.copy());
    }

    public void restore() {
        if (!this.stateStack.isEmpty()) {
            State state = this.stateStack.pop();
            state.restore(this);
            this.txdirty = true;
        }
    }

    public void translate(double d2, double d3) {
        this.curState.transform.translate(d2, d3);
        this.txdirty = true;
    }

    public void scale(double d2, double d3) {
        this.curState.transform.scale(d2, d3);
        this.txdirty = true;
    }

    public void rotate(double d2) {
        this.curState.transform.rotate(Math.toRadians(d2));
        this.txdirty = true;
    }

    public void transform(double d2, double d3, double d4, double d5, double d6, double d7) {
        this.curState.transform.concatenate(d2, d4, d6, d3, d5, d7);
        this.txdirty = true;
    }

    public void transform(Affine affine) {
        if (affine == null) {
            return;
        }
        this.curState.transform.concatenate(affine.getMxx(), affine.getMxy(), affine.getTx(), affine.getMyx(), affine.getMyy(), affine.getTy());
        this.txdirty = true;
    }

    public void setTransform(double d2, double d3, double d4, double d5, double d6, double d7) {
        this.curState.transform.setTransform(d2, d3, d4, d5, d6, d7);
        this.txdirty = true;
    }

    public void setTransform(Affine affine) {
        this.curState.transform.setTransform(affine.getMxx(), affine.getMyx(), affine.getMxy(), affine.getMyy(), affine.getTx(), affine.getTy());
        this.txdirty = true;
    }

    public Affine getTransform(Affine affine) {
        if (affine == null) {
            affine = new Affine();
        }
        affine.setMxx(this.curState.transform.getMxx());
        affine.setMxy(this.curState.transform.getMxy());
        affine.setMxz(0.0);
        affine.setTx(this.curState.transform.getMxt());
        affine.setMyx(this.curState.transform.getMyx());
        affine.setMyy(this.curState.transform.getMyy());
        affine.setMyz(0.0);
        affine.setTy(this.curState.transform.getMyt());
        affine.setMzx(0.0);
        affine.setMzy(0.0);
        affine.setMzz(1.0);
        affine.setTz(0.0);
        return affine;
    }

    public Affine getTransform() {
        return this.getTransform(null);
    }

    public void setGlobalAlpha(double d2) {
        if (this.curState.globalAlpha != d2) {
            this.curState.globalAlpha = d2;
            d2 = d2 > 1.0 ? 1.0 : (d2 < 0.0 ? 0.0 : d2);
            this.writeParam(d2, (byte)0);
        }
    }

    public double getGlobalAlpha() {
        return this.curState.globalAlpha;
    }

    public void setGlobalBlendMode(BlendMode blendMode) {
        if (blendMode != null && blendMode != this.curState.blendop) {
            GrowableDataBuffer growableDataBuffer = this.getBuffer();
            this.curState.blendop = blendMode;
            growableDataBuffer.putByte((byte)1);
            growableDataBuffer.putObject((Object)Blend.impl_getToolkitMode(blendMode));
        }
    }

    public BlendMode getGlobalBlendMode() {
        return this.curState.blendop;
    }

    public void setFill(Paint paint) {
        if (paint != null && this.curState.fill != paint) {
            this.curState.fill = paint;
            this.writePaint(paint, (byte)2);
        }
    }

    public Paint getFill() {
        return this.curState.fill;
    }

    public void setStroke(Paint paint) {
        if (paint != null && this.curState.stroke != paint) {
            this.curState.stroke = paint;
            this.writePaint(paint, (byte)3);
        }
    }

    public Paint getStroke() {
        return this.curState.stroke;
    }

    public void setLineWidth(double d2) {
        if (d2 > 0.0 && d2 < Double.POSITIVE_INFINITY && this.curState.linewidth != d2) {
            this.curState.linewidth = d2;
            this.writeParam(d2, (byte)4);
        }
    }

    public double getLineWidth() {
        return this.curState.linewidth;
    }

    public void setLineCap(StrokeLineCap strokeLineCap) {
        if (strokeLineCap != null && this.curState.linecap != strokeLineCap) {
            byte by;
            switch (strokeLineCap) {
                case BUTT: {
                    by = 0;
                    break;
                }
                case ROUND: {
                    by = 1;
                    break;
                }
                case SQUARE: {
                    by = 2;
                    break;
                }
                default: {
                    return;
                }
            }
            this.curState.linecap = strokeLineCap;
            this.writeParam(by, (byte)5);
        }
    }

    public StrokeLineCap getLineCap() {
        return this.curState.linecap;
    }

    public void setLineJoin(StrokeLineJoin strokeLineJoin) {
        if (strokeLineJoin != null && this.curState.linejoin != strokeLineJoin) {
            byte by;
            switch (strokeLineJoin) {
                case MITER: {
                    by = 0;
                    break;
                }
                case BEVEL: {
                    by = 2;
                    break;
                }
                case ROUND: {
                    by = 1;
                    break;
                }
                default: {
                    return;
                }
            }
            this.curState.linejoin = strokeLineJoin;
            this.writeParam(by, (byte)6);
        }
    }

    public StrokeLineJoin getLineJoin() {
        return this.curState.linejoin;
    }

    public void setMiterLimit(double d2) {
        if (d2 > 0.0 && d2 < Double.POSITIVE_INFINITY && this.curState.miterlimit != d2) {
            this.curState.miterlimit = d2;
            this.writeParam(d2, (byte)7);
        }
    }

    public double getMiterLimit() {
        return this.curState.miterlimit;
    }

    public void setLineDashes(double ... arrd) {
        if (arrd == null || arrd.length == 0) {
            if (this.curState.dashes == null) {
                return;
            }
            this.curState.dashes = null;
        } else {
            int n2;
            boolean bl = true;
            for (n2 = 0; n2 < arrd.length; ++n2) {
                double d2 = arrd[n2];
                if (d2 >= 0.0 && d2 < Double.POSITIVE_INFINITY) {
                    if (!(d2 > 0.0)) continue;
                    bl = false;
                    continue;
                }
                return;
            }
            if (bl) {
                if (this.curState.dashes == null) {
                    return;
                }
                this.curState.dashes = null;
            } else {
                n2 = arrd.length;
                if ((n2 & 1) == 0) {
                    this.curState.dashes = Arrays.copyOf(arrd, n2);
                } else {
                    this.curState.dashes = Arrays.copyOf(arrd, n2 * 2);
                    System.arraycopy(arrd, 0, this.curState.dashes, n2, n2);
                }
            }
        }
        GrowableDataBuffer growableDataBuffer = this.getBuffer();
        growableDataBuffer.putByte((byte)17);
        growableDataBuffer.putObject(this.curState.dashes);
    }

    public double[] getLineDashes() {
        if (this.curState.dashes == null) {
            return null;
        }
        return Arrays.copyOf(this.curState.dashes, this.curState.dashes.length);
    }

    public void setLineDashOffset(double d2) {
        if (d2 > Double.NEGATIVE_INFINITY && d2 < Double.POSITIVE_INFINITY) {
            this.curState.dashOffset = d2;
            this.writeParam(d2, (byte)18);
        }
    }

    public double getLineDashOffset() {
        return this.curState.dashOffset;
    }

    public void setFont(Font font) {
        if (font != null && this.curState.font != font) {
            this.curState.font = font;
            GrowableDataBuffer growableDataBuffer = this.getBuffer();
            growableDataBuffer.putByte((byte)8);
            growableDataBuffer.putObject(font.impl_getNativeFont());
        }
    }

    public Font getFont() {
        return this.curState.font;
    }

    public void setFontSmoothingType(FontSmoothingType fontSmoothingType) {
        if (fontSmoothingType != null && fontSmoothingType != this.curState.fontsmoothing) {
            this.curState.fontsmoothing = fontSmoothingType;
            this.writeParam((byte)fontSmoothingType.ordinal(), (byte)19);
        }
    }

    public FontSmoothingType getFontSmoothingType() {
        return this.curState.fontsmoothing;
    }

    public void setTextAlign(TextAlignment textAlignment) {
        if (textAlignment != null && this.curState.textalign != textAlignment) {
            byte by;
            switch (textAlignment) {
                case LEFT: {
                    by = 0;
                    break;
                }
                case CENTER: {
                    by = 1;
                    break;
                }
                case RIGHT: {
                    by = 2;
                    break;
                }
                case JUSTIFY: {
                    by = 3;
                    break;
                }
                default: {
                    return;
                }
            }
            this.curState.textalign = textAlignment;
            this.writeParam(by, (byte)9);
        }
    }

    public TextAlignment getTextAlign() {
        return this.curState.textalign;
    }

    public void setTextBaseline(VPos vPos) {
        if (vPos != null && this.curState.textbaseline != vPos) {
            byte by;
            switch (vPos) {
                case TOP: {
                    by = 0;
                    break;
                }
                case CENTER: {
                    by = 1;
                    break;
                }
                case BASELINE: {
                    by = 2;
                    break;
                }
                case BOTTOM: {
                    by = 3;
                    break;
                }
                default: {
                    return;
                }
            }
            this.curState.textbaseline = vPos;
            this.writeParam(by, (byte)10);
        }
    }

    public VPos getTextBaseline() {
        return this.curState.textbaseline;
    }

    public void fillText(String string, double d2, double d3) {
        this.writeText(string, d2, d3, 0.0, (byte)30);
    }

    public void strokeText(String string, double d2, double d3) {
        this.writeText(string, d2, d3, 0.0, (byte)31);
    }

    public void fillText(String string, double d2, double d3, double d4) {
        if (d4 <= 0.0) {
            return;
        }
        this.writeText(string, d2, d3, d4, (byte)30);
    }

    public void strokeText(String string, double d2, double d3, double d4) {
        if (d4 <= 0.0) {
            return;
        }
        this.writeText(string, d2, d3, d4, (byte)31);
    }

    public void setFillRule(FillRule fillRule) {
        if (fillRule != null && this.curState.fillRule != fillRule) {
            byte by = fillRule == FillRule.EVEN_ODD ? (byte)1 : 0;
            this.curState.fillRule = fillRule;
            this.writeParam(by, (byte)16);
        }
    }

    public FillRule getFillRule() {
        return this.curState.fillRule;
    }

    public void beginPath() {
        this.path.reset();
        this.markPathDirty();
    }

    public void moveTo(double d2, double d3) {
        this.coords[0] = (float)d2;
        this.coords[1] = (float)d3;
        this.curState.transform.transform(this.coords, 0, this.coords, 0, 1);
        this.path.moveTo(this.coords[0], this.coords[1]);
        this.markPathDirty();
    }

    public void lineTo(double d2, double d3) {
        this.coords[0] = (float)d2;
        this.coords[1] = (float)d3;
        this.curState.transform.transform(this.coords, 0, this.coords, 0, 1);
        if (this.path.getNumCommands() == 0) {
            this.path.moveTo(this.coords[0], this.coords[1]);
        }
        this.path.lineTo(this.coords[0], this.coords[1]);
        this.markPathDirty();
    }

    public void quadraticCurveTo(double d2, double d3, double d4, double d5) {
        this.coords[0] = (float)d2;
        this.coords[1] = (float)d3;
        this.coords[2] = (float)d4;
        this.coords[3] = (float)d5;
        this.curState.transform.transform(this.coords, 0, this.coords, 0, 2);
        if (this.path.getNumCommands() == 0) {
            this.path.moveTo(this.coords[0], this.coords[1]);
        }
        this.path.quadTo(this.coords[0], this.coords[1], this.coords[2], this.coords[3]);
        this.markPathDirty();
    }

    public void bezierCurveTo(double d2, double d3, double d4, double d5, double d6, double d7) {
        this.coords[0] = (float)d2;
        this.coords[1] = (float)d3;
        this.coords[2] = (float)d4;
        this.coords[3] = (float)d5;
        this.coords[4] = (float)d6;
        this.coords[5] = (float)d7;
        this.curState.transform.transform(this.coords, 0, this.coords, 0, 3);
        if (this.path.getNumCommands() == 0) {
            this.path.moveTo(this.coords[0], this.coords[1]);
        }
        this.path.curveTo(this.coords[0], this.coords[1], this.coords[2], this.coords[3], this.coords[4], this.coords[5]);
        this.markPathDirty();
    }

    public void arcTo(double d2, double d3, double d4, double d5, double d6) {
        if (this.path.getNumCommands() == 0) {
            this.moveTo(d2, d3);
            this.lineTo(d2, d3);
        } else if (!this.tryArcTo((float)d2, (float)d3, (float)d4, (float)d5, (float)d6)) {
            this.lineTo(d2, d3);
        }
    }

    private static double lenSq(double d2, double d3, double d4, double d5) {
        return (d4 -= d2) * d4 + (d5 -= d3) * d5;
    }

    private boolean tryArcTo(float f2, float f3, float f4, float f5, float f6) {
        boolean bl;
        double d2;
        double d3;
        double d4;
        double d5;
        float f7;
        float f8;
        if (this.curState.transform.isTranslateOrIdentity()) {
            f8 = (float)((double)this.path.getCurrentX() - this.curState.transform.getMxt());
            f7 = (float)((double)this.path.getCurrentY() - this.curState.transform.getMyt());
        } else {
            this.coords[0] = this.path.getCurrentX();
            this.coords[1] = this.path.getCurrentY();
            try {
                this.curState.transform.inverseTransform(this.coords, 0, this.coords, 0, 1);
            }
            catch (NoninvertibleTransformException noninvertibleTransformException) {
                return false;
            }
            f8 = this.coords[0];
            f7 = this.coords[1];
        }
        double d6 = GraphicsContext.lenSq(f8, f7, f2, f3);
        double d7 = GraphicsContext.lenSq(f2, f3, f4, f5);
        double d8 = GraphicsContext.lenSq(f8, f7, f4, f5);
        double d9 = Math.sqrt(d6);
        double d10 = Math.sqrt(d7);
        double d11 = d6 + d7 - d8;
        double d12 = 2.0 * d9 * d10;
        if (d12 == 0.0 || f6 <= 0.0f) {
            return false;
        }
        double d13 = d11 / d12;
        double d14 = 1.0 + d13;
        if (d14 == 0.0) {
            return false;
        }
        double d15 = (1.0 - d13) / d14;
        double d16 = (double)f6 / Math.sqrt(d15);
        double d17 = (double)f2 + d16 / d9 * (double)(f8 - f2);
        double d18 = (d17 + (d5 = (double)f2 + d16 / d10 * (double)(f4 - f2))) / 2.0;
        double d19 = GraphicsContext.lenSq(d18, d4 = ((d3 = (double)f3 + d16 / d9 * (double)(f7 - f3)) + (d2 = (double)f3 + d16 / d10 * (double)(f5 - f3))) / 2.0, f2, f3);
        if (d19 == 0.0) {
            return false;
        }
        double d20 = GraphicsContext.lenSq(d18, d4, d17, d3) / d19;
        double d21 = d18 + (d18 - (double)f2) * d20;
        double d22 = d4 + (d4 - (double)f3) * d20;
        if (d21 != d21 || d22 != d22) {
            return false;
        }
        if (d17 != (double)f8 || d3 != (double)f7) {
            this.lineTo(d17, d3);
        }
        double d23 = Math.sqrt((1.0 - d13) / 2.0);
        boolean bl2 = bl = (d3 - d22) * (d5 - d21) > (d2 - d22) * (d17 - d21);
        if (d13 <= 0.0) {
            double d24 = Math.sqrt((1.0 + d13) / 2.0);
            double d25 = 1.3333333333333333 * d24 / (1.0 + d23);
            if (bl) {
                d25 = -d25;
            }
            double d26 = d17 - d25 * (d3 - d22);
            double d27 = d3 + d25 * (d17 - d21);
            double d28 = d5 + d25 * (d2 - d22);
            double d29 = d2 - d25 * (d5 - d21);
            this.bezierCurveTo(d26, d27, d28, d29, d5, d2);
        } else {
            double d30 = Math.sqrt((1.0 - d23) / 2.0);
            double d31 = Math.sqrt((1.0 + d23) / 2.0);
            double d32 = 1.3333333333333333 * d30 / (1.0 + d31);
            if (bl) {
                d32 = -d32;
            }
            double d33 = (double)f6 / Math.sqrt(d19);
            double d34 = d21 + ((double)f2 - d18) * d33;
            double d35 = d22 + ((double)f3 - d4) * d33;
            double d36 = d17 - d32 * (d3 - d22);
            double d37 = d3 + d32 * (d17 - d21);
            double d38 = d34 + d32 * (d35 - d22);
            double d39 = d35 - d32 * (d34 - d21);
            this.bezierCurveTo(d36, d37, d38, d39, d34, d35);
            d36 = d34 - d32 * (d35 - d22);
            d37 = d35 + d32 * (d34 - d21);
            d38 = d5 + d32 * (d2 - d22);
            d39 = d2 - d32 * (d5 - d21);
            this.bezierCurveTo(d36, d37, d38, d39, d5, d2);
        }
        return true;
    }

    public void arc(double d2, double d3, double d4, double d5, double d6, double d7) {
        Arc2D arc2D = new Arc2D((float)(d2 - d4), (float)(d3 - d5), (float)(d4 * 2.0), (float)(d5 * 2.0), (float)d6, (float)d7, 0);
        this.path.append(arc2D.getPathIterator(this.curState.transform), true);
        this.markPathDirty();
    }

    public void rect(double d2, double d3, double d4, double d5) {
        this.coords[0] = (float)d2;
        this.coords[1] = (float)d3;
        this.coords[2] = (float)d4;
        this.coords[3] = 0.0f;
        this.coords[4] = 0.0f;
        this.coords[5] = (float)d5;
        this.curState.transform.deltaTransform(this.coords, 0, this.coords, 0, 3);
        float f2 = this.coords[0] + (float)this.curState.transform.getMxt();
        float f3 = this.coords[1] + (float)this.curState.transform.getMyt();
        float f4 = this.coords[2];
        float f5 = this.coords[3];
        float f6 = this.coords[4];
        float f7 = this.coords[5];
        this.path.moveTo(f2, f3);
        this.path.lineTo(f2 + f4, f3 + f5);
        this.path.lineTo(f2 + f4 + f6, f3 + f5 + f7);
        this.path.lineTo(f2 + f6, f3 + f7);
        this.path.closePath();
        this.markPathDirty();
    }

    public void appendSVGPath(String string) {
        boolean bl;
        boolean bl2;
        block17: {
            if (string == null) {
                return;
            }
            bl2 = true;
            bl = true;
            block9: for (int i2 = 0; i2 < string.length(); ++i2) {
                switch (string.charAt(i2)) {
                    case '\t': 
                    case '\n': 
                    case '\r': 
                    case ' ': {
                        continue block9;
                    }
                    case 'M': {
                        bl = false;
                        bl2 = false;
                        break block17;
                    }
                    case 'm': {
                        if (this.path.getNumCommands() == 0) {
                            bl2 = false;
                        }
                        bl = false;
                    }
                }
            }
        }
        Path2D path2D = new Path2D();
        if (bl2 && this.path.getNumCommands() > 0) {
            float f2;
            float f3;
            if (this.curState.transform.isTranslateOrIdentity()) {
                f3 = (float)((double)this.path.getCurrentX() - this.curState.transform.getMxt());
                f2 = (float)((double)this.path.getCurrentY() - this.curState.transform.getMyt());
            } else {
                this.coords[0] = this.path.getCurrentX();
                this.coords[1] = this.path.getCurrentY();
                try {
                    this.curState.transform.inverseTransform(this.coords, 0, this.coords, 0, 1);
                }
                catch (NoninvertibleTransformException noninvertibleTransformException) {
                    // empty catch block
                }
                f3 = this.coords[0];
                f2 = this.coords[1];
            }
            path2D.moveTo(f3, f2);
        } else {
            bl = false;
        }
        try {
            path2D.appendSVGPath(string);
            PathIterator pathIterator = path2D.getPathIterator(this.curState.transform);
            if (bl) {
                pathIterator.next();
            }
            this.path.append(pathIterator, false);
        }
        catch (IllegalPathStateException | IllegalArgumentException runtimeException) {
            // empty catch block
        }
    }

    public void closePath() {
        if (this.path.getNumCommands() > 0) {
            this.path.closePath();
            this.markPathDirty();
        }
    }

    public void fill() {
        this.writePath((byte)47);
    }

    public void stroke() {
        this.writePath((byte)48);
    }

    public void clip() {
        Path2D path2D = new Path2D(this.path);
        this.clipStack.addLast(path2D);
        ++this.curState.numClipPaths;
        GrowableDataBuffer growableDataBuffer = this.getBuffer();
        growableDataBuffer.putByte((byte)13);
        growableDataBuffer.putObject(path2D);
    }

    public boolean isPointInPath(double d2, double d3) {
        return this.path.contains((float)d2, (float)d3);
    }

    public void clearRect(double d2, double d3, double d4, double d5) {
        if (d4 != 0.0 && d5 != 0.0) {
            this.resetIfCovers(null, d2, d3, d4, d5);
            this.writeOp4(d2, d3, d4, d5, (byte)22);
        }
    }

    public void fillRect(double d2, double d3, double d4, double d5) {
        if (d4 != 0.0 && d5 != 0.0) {
            this.resetIfCovers(this.curState.fill, d2, d3, d4, d5);
            this.writeOp4(d2, d3, d4, d5, (byte)20);
        }
    }

    public void strokeRect(double d2, double d3, double d4, double d5) {
        if (d4 != 0.0 || d5 != 0.0) {
            this.writeOp4(d2, d3, d4, d5, (byte)21);
        }
    }

    public void fillOval(double d2, double d3, double d4, double d5) {
        if (d4 != 0.0 && d5 != 0.0) {
            this.writeOp4(d2, d3, d4, d5, (byte)24);
        }
    }

    public void strokeOval(double d2, double d3, double d4, double d5) {
        if (d4 != 0.0 || d5 != 0.0) {
            this.writeOp4(d2, d3, d4, d5, (byte)25);
        }
    }

    public void fillArc(double d2, double d3, double d4, double d5, double d6, double d7, ArcType arcType) {
        if (d4 != 0.0 && d5 != 0.0 && arcType != null) {
            this.writeArcType(arcType);
            this.writeOp6(d2, d3, d4, d5, d6, d7, (byte)28);
        }
    }

    public void strokeArc(double d2, double d3, double d4, double d5, double d6, double d7, ArcType arcType) {
        if (d4 != 0.0 && d5 != 0.0 && arcType != null) {
            this.writeArcType(arcType);
            this.writeOp6(d2, d3, d4, d5, d6, d7, (byte)29);
        }
    }

    public void fillRoundRect(double d2, double d3, double d4, double d5, double d6, double d7) {
        if (d4 != 0.0 && d5 != 0.0) {
            this.writeOp6(d2, d3, d4, d5, d6, d7, (byte)26);
        }
    }

    public void strokeRoundRect(double d2, double d3, double d4, double d5, double d6, double d7) {
        if (d4 != 0.0 && d5 != 0.0) {
            this.writeOp6(d2, d3, d4, d5, d6, d7, (byte)27);
        }
    }

    public void strokeLine(double d2, double d3, double d4, double d5) {
        this.writeOp4(d2, d3, d4, d5, (byte)23);
    }

    public void fillPolygon(double[] arrd, double[] arrd2, int n2) {
        if (n2 >= 3) {
            this.writePoly(arrd, arrd2, n2, true, (byte)47);
        }
    }

    public void strokePolygon(double[] arrd, double[] arrd2, int n2) {
        if (n2 >= 2) {
            this.writePoly(arrd, arrd2, n2, true, (byte)48);
        }
    }

    public void strokePolyline(double[] arrd, double[] arrd2, int n2) {
        if (n2 >= 2) {
            this.writePoly(arrd, arrd2, n2, false, (byte)48);
        }
    }

    public void drawImage(Image image, double d2, double d3) {
        if (image == null) {
            return;
        }
        double d4 = image.getWidth();
        double d5 = image.getHeight();
        this.writeImage(image, d2, d3, d4, d5);
    }

    public void drawImage(Image image, double d2, double d3, double d4, double d5) {
        this.writeImage(image, d2, d3, d4, d5);
    }

    public void drawImage(Image image, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9) {
        this.writeImage(image, d6, d7, d8, d9, d2, d3, d4, d5);
    }

    public PixelWriter getPixelWriter() {
        if (this.writer == null) {
            this.writer = new PixelWriter(){

                @Override
                public PixelFormat<ByteBuffer> getPixelFormat() {
                    return PixelFormat.getByteBgraPreInstance();
                }

                private BytePixelSetter getSetter() {
                    return ByteBgraPre.setter;
                }

                @Override
                public void setArgb(int n2, int n3, int n4) {
                    GrowableDataBuffer growableDataBuffer = GraphicsContext.this.getBuffer();
                    growableDataBuffer.putByte((byte)52);
                    growableDataBuffer.putInt(n2);
                    growableDataBuffer.putInt(n3);
                    growableDataBuffer.putInt(n4);
                }

                @Override
                public void setColor(int n2, int n3, Color color) {
                    if (color == null) {
                        throw new NullPointerException("Color cannot be null");
                    }
                    int n4 = (int)Math.round(color.getOpacity() * 255.0);
                    int n5 = (int)Math.round(color.getRed() * 255.0);
                    int n6 = (int)Math.round(color.getGreen() * 255.0);
                    int n7 = (int)Math.round(color.getBlue() * 255.0);
                    this.setArgb(n2, n3, n4 << 24 | n5 << 16 | n6 << 8 | n7);
                }

                private void writePixelBuffer(int n2, int n3, int n4, int n5, byte[] arrby) {
                    GrowableDataBuffer growableDataBuffer = GraphicsContext.this.getBuffer();
                    growableDataBuffer.putByte((byte)53);
                    growableDataBuffer.putInt(n2);
                    growableDataBuffer.putInt(n3);
                    growableDataBuffer.putInt(n4);
                    growableDataBuffer.putInt(n5);
                    growableDataBuffer.putObject(arrby);
                }

                private int[] checkBounds(int n2, int n3, int n4, int n5, PixelFormat<? extends Buffer> pixelFormat, int n6) {
                    int n7 = (int)Math.ceil(GraphicsContext.this.theCanvas.getWidth());
                    int n8 = (int)Math.ceil(GraphicsContext.this.theCanvas.getHeight());
                    if (n2 >= 0 && n3 >= 0 && n2 + n4 <= n7 && n3 + n5 <= n8) {
                        return null;
                    }
                    int n9 = 0;
                    if (n2 < 0) {
                        if ((n4 += n2) < 0) {
                            return null;
                        }
                        if (pixelFormat != null) {
                            switch (pixelFormat.getType()) {
                                case BYTE_BGRA: 
                                case BYTE_BGRA_PRE: {
                                    n9 -= n2 * 4;
                                    break;
                                }
                                case BYTE_RGB: {
                                    n9 -= n2 * 3;
                                    break;
                                }
                                case BYTE_INDEXED: 
                                case INT_ARGB: 
                                case INT_ARGB_PRE: {
                                    n9 -= n2;
                                    break;
                                }
                                default: {
                                    throw new InternalError("unknown Pixel Format");
                                }
                            }
                        }
                        n2 = 0;
                    }
                    if (n3 < 0) {
                        if ((n5 += n3) < 0) {
                            return null;
                        }
                        n9 -= n3 * n6;
                        n3 = 0;
                    }
                    if (n2 + n4 > n7 && (n4 = n7 - n2) < 0) {
                        return null;
                    }
                    if (n3 + n5 > n8 && (n5 = n8 - n3) < 0) {
                        return null;
                    }
                    return new int[]{n2, n3, n4, n5, n9};
                }

                @Override
                public <T extends Buffer> void setPixels(int n2, int n3, int n4, int n5, PixelFormat<T> pixelFormat, T t2, int n6) {
                    if (pixelFormat == null) {
                        throw new NullPointerException("PixelFormat cannot be null");
                    }
                    if (t2 == null) {
                        throw new NullPointerException("Buffer cannot be null");
                    }
                    if (n4 <= 0 || n5 <= 0) {
                        return;
                    }
                    int n7 = t2.position();
                    int[] arrn = this.checkBounds(n2, n3, n4, n5, pixelFormat, n6);
                    if (arrn != null) {
                        n2 = arrn[0];
                        n3 = arrn[1];
                        n4 = arrn[2];
                        n5 = arrn[3];
                        n7 += arrn[4];
                    }
                    byte[] arrby = new byte[n4 * n5 * 4];
                    ByteBuffer byteBuffer = ByteBuffer.wrap(arrby);
                    PixelGetter<T> pixelGetter = PixelUtils.getGetter(pixelFormat);
                    PixelConverter<T, ByteBuffer> pixelConverter = PixelUtils.getConverter(pixelGetter, this.getSetter());
                    pixelConverter.convert(t2, n7, n6, byteBuffer, 0, n4 * 4, n4, n5);
                    this.writePixelBuffer(n2, n3, n4, n5, arrby);
                }

                @Override
                public void setPixels(int n2, int n3, int n4, int n5, PixelFormat<ByteBuffer> pixelFormat, byte[] arrby, int n6, int n7) {
                    if (pixelFormat == null) {
                        throw new NullPointerException("PixelFormat cannot be null");
                    }
                    if (arrby == null) {
                        throw new NullPointerException("Buffer cannot be null");
                    }
                    if (n4 <= 0 || n5 <= 0) {
                        return;
                    }
                    int[] arrn = this.checkBounds(n2, n3, n4, n5, pixelFormat, n7);
                    if (arrn != null) {
                        n2 = arrn[0];
                        n3 = arrn[1];
                        n4 = arrn[2];
                        n5 = arrn[3];
                        n6 += arrn[4];
                    }
                    byte[] arrby2 = new byte[n4 * n5 * 4];
                    BytePixelGetter bytePixelGetter = PixelUtils.getByteGetter(pixelFormat);
                    ByteToBytePixelConverter byteToBytePixelConverter = PixelUtils.getB2BConverter(bytePixelGetter, this.getSetter());
                    byteToBytePixelConverter.convert(arrby, n6, n7, arrby2, 0, n4 * 4, n4, n5);
                    this.writePixelBuffer(n2, n3, n4, n5, arrby2);
                }

                @Override
                public void setPixels(int n2, int n3, int n4, int n5, PixelFormat<IntBuffer> pixelFormat, int[] arrn, int n6, int n7) {
                    if (pixelFormat == null) {
                        throw new NullPointerException("PixelFormat cannot be null");
                    }
                    if (arrn == null) {
                        throw new NullPointerException("Buffer cannot be null");
                    }
                    if (n4 <= 0 || n5 <= 0) {
                        return;
                    }
                    int[] arrn2 = this.checkBounds(n2, n3, n4, n5, pixelFormat, n7);
                    if (arrn2 != null) {
                        n2 = arrn2[0];
                        n3 = arrn2[1];
                        n4 = arrn2[2];
                        n5 = arrn2[3];
                        n6 += arrn2[4];
                    }
                    byte[] arrby = new byte[n4 * n5 * 4];
                    IntPixelGetter intPixelGetter = PixelUtils.getIntGetter(pixelFormat);
                    IntToBytePixelConverter intToBytePixelConverter = PixelUtils.getI2BConverter(intPixelGetter, this.getSetter());
                    intToBytePixelConverter.convert(arrn, n6, n7, arrby, 0, n4 * 4, n4, n5);
                    this.writePixelBuffer(n2, n3, n4, n5, arrby);
                }

                @Override
                public void setPixels(int n2, int n3, int n4, int n5, PixelReader pixelReader, int n6, int n7) {
                    if (pixelReader == null) {
                        throw new NullPointerException("Reader cannot be null");
                    }
                    if (n4 <= 0 || n5 <= 0) {
                        return;
                    }
                    int[] arrn = this.checkBounds(n2, n3, n4, n5, null, 0);
                    if (arrn != null) {
                        int n8 = arrn[0];
                        int n9 = arrn[1];
                        n6 += n8 - n2;
                        n7 += n9 - n3;
                        n2 = n8;
                        n3 = n9;
                        n4 = arrn[2];
                        n5 = arrn[3];
                    }
                    byte[] arrby = new byte[n4 * n5 * 4];
                    pixelReader.getPixels(n6, n7, n4, n5, PixelFormat.getByteBgraPreInstance(), arrby, 0, n4 * 4);
                    this.writePixelBuffer(n2, n3, n4, n5, arrby);
                }
            };
        }
        return this.writer;
    }

    public void setEffect(Effect effect) {
        GrowableDataBuffer growableDataBuffer = this.getBuffer();
        growableDataBuffer.putByte((byte)12);
        if (effect == null) {
            this.curState.effect = null;
            growableDataBuffer.putObject(null);
        } else {
            this.curState.effect = effect.impl_copy();
            this.curState.effect.impl_sync();
            growableDataBuffer.putObject(this.curState.effect.impl_getImpl());
        }
    }

    public Effect getEffect(Effect effect) {
        return this.curState.effect == null ? null : this.curState.effect.impl_copy();
    }

    public void applyEffect(Effect effect) {
        if (effect == null) {
            return;
        }
        GrowableDataBuffer growableDataBuffer = this.getBuffer();
        growableDataBuffer.putByte((byte)60);
        Effect effect2 = effect.impl_copy();
        effect2.impl_sync();
        growableDataBuffer.putObject(effect2.impl_getImpl());
    }

    static class State {
        double globalAlpha;
        BlendMode blendop;
        Affine2D transform;
        Paint fill;
        Paint stroke;
        double linewidth;
        StrokeLineCap linecap;
        StrokeLineJoin linejoin;
        double miterlimit;
        double[] dashes;
        double dashOffset;
        int numClipPaths;
        Font font;
        FontSmoothingType fontsmoothing;
        TextAlignment textalign;
        VPos textbaseline;
        Effect effect;
        FillRule fillRule;

        State() {
            this.init();
        }

        final void init() {
            this.set(1.0, BlendMode.SRC_OVER, new Affine2D(), Color.BLACK, Color.BLACK, 1.0, StrokeLineCap.SQUARE, StrokeLineJoin.MITER, 10.0, null, 0.0, 0, Font.getDefault(), FontSmoothingType.GRAY, TextAlignment.LEFT, VPos.BASELINE, null, FillRule.NON_ZERO);
        }

        State(State state) {
            this.set(state.globalAlpha, state.blendop, new Affine2D(state.transform), state.fill, state.stroke, state.linewidth, state.linecap, state.linejoin, state.miterlimit, state.dashes, state.dashOffset, state.numClipPaths, state.font, state.fontsmoothing, state.textalign, state.textbaseline, state.effect, state.fillRule);
        }

        final void set(double d2, BlendMode blendMode, Affine2D affine2D, Paint paint, Paint paint2, double d3, StrokeLineCap strokeLineCap, StrokeLineJoin strokeLineJoin, double d4, double[] arrd, double d5, int n2, Font font, FontSmoothingType fontSmoothingType, TextAlignment textAlignment, VPos vPos, Effect effect, FillRule fillRule) {
            this.globalAlpha = d2;
            this.blendop = blendMode;
            this.transform = affine2D;
            this.fill = paint;
            this.stroke = paint2;
            this.linewidth = d3;
            this.linecap = strokeLineCap;
            this.linejoin = strokeLineJoin;
            this.miterlimit = d4;
            this.dashes = arrd;
            this.dashOffset = d5;
            this.numClipPaths = n2;
            this.font = font;
            this.fontsmoothing = fontSmoothingType;
            this.textalign = textAlignment;
            this.textbaseline = vPos;
            this.effect = effect;
            this.fillRule = fillRule;
        }

        State copy() {
            return new State(this);
        }

        void restore(GraphicsContext graphicsContext) {
            graphicsContext.setGlobalAlpha(this.globalAlpha);
            graphicsContext.setGlobalBlendMode(this.blendop);
            graphicsContext.setTransform(this.transform.getMxx(), this.transform.getMyx(), this.transform.getMxy(), this.transform.getMyy(), this.transform.getMxt(), this.transform.getMyt());
            graphicsContext.setFill(this.fill);
            graphicsContext.setStroke(this.stroke);
            graphicsContext.setLineWidth(this.linewidth);
            graphicsContext.setLineCap(this.linecap);
            graphicsContext.setLineJoin(this.linejoin);
            graphicsContext.setMiterLimit(this.miterlimit);
            graphicsContext.setLineDashes(this.dashes);
            graphicsContext.setLineDashOffset(this.dashOffset);
            GrowableDataBuffer growableDataBuffer = graphicsContext.getBuffer();
            while (graphicsContext.curState.numClipPaths > this.numClipPaths) {
                --graphicsContext.curState.numClipPaths;
                graphicsContext.clipStack.removeLast();
                growableDataBuffer.putByte((byte)14);
            }
            graphicsContext.setFillRule(this.fillRule);
            graphicsContext.setFont(this.font);
            graphicsContext.setFontSmoothingType(this.fontsmoothing);
            graphicsContext.setTextAlign(this.textalign);
            graphicsContext.setTextBaseline(this.textbaseline);
            graphicsContext.setEffect(this.effect);
        }
    }
}

