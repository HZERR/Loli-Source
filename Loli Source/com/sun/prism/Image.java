/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism;

import com.sun.javafx.iio.ImageFrame;
import com.sun.javafx.iio.ImageStorage;
import com.sun.javafx.image.BytePixelGetter;
import com.sun.javafx.image.BytePixelSetter;
import com.sun.javafx.image.ByteToBytePixelConverter;
import com.sun.javafx.image.ByteToIntPixelConverter;
import com.sun.javafx.image.IntPixelGetter;
import com.sun.javafx.image.IntPixelSetter;
import com.sun.javafx.image.IntToBytePixelConverter;
import com.sun.javafx.image.IntToIntPixelConverter;
import com.sun.javafx.image.PixelConverter;
import com.sun.javafx.image.PixelGetter;
import com.sun.javafx.image.PixelSetter;
import com.sun.javafx.image.PixelUtils;
import com.sun.javafx.image.impl.ByteBgra;
import com.sun.javafx.image.impl.ByteBgraPre;
import com.sun.javafx.image.impl.ByteGray;
import com.sun.javafx.image.impl.ByteGrayAlpha;
import com.sun.javafx.image.impl.ByteGrayAlphaPre;
import com.sun.javafx.image.impl.ByteRgb;
import com.sun.javafx.image.impl.ByteRgba;
import com.sun.javafx.tk.PlatformImage;
import com.sun.prism.PixelFormat;
import com.sun.prism.impl.BufferUtil;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritablePixelFormat;

public class Image
implements PlatformImage {
    static final WritablePixelFormat<ByteBuffer> FX_ByteBgraPre_FORMAT = javafx.scene.image.PixelFormat.getByteBgraPreInstance();
    static final WritablePixelFormat<IntBuffer> FX_IntArgbPre_FORMAT = javafx.scene.image.PixelFormat.getIntArgbPreInstance();
    static final javafx.scene.image.PixelFormat<ByteBuffer> FX_ByteRgb_FORMAT = javafx.scene.image.PixelFormat.getByteRgbInstance();
    private final Buffer pixelBuffer;
    private final int minX;
    private final int minY;
    private final int width;
    private final int height;
    private final int scanlineStride;
    private final PixelFormat pixelFormat;
    private final float pixelScale;
    int[] serial = new int[1];
    private Accessor<?> pixelaccessor;
    static javafx.scene.image.PixelFormat<ByteBuffer> FX_ByteGray_FORMAT;

    public static Image fromIntArgbPreData(int[] arrn, int n2, int n3) {
        return new Image(PixelFormat.INT_ARGB_PRE, arrn, n2, n3);
    }

    public static Image fromIntArgbPreData(IntBuffer intBuffer, int n2, int n3) {
        return new Image(PixelFormat.INT_ARGB_PRE, intBuffer, n2, n3);
    }

    public static Image fromIntArgbPreData(IntBuffer intBuffer, int n2, int n3, int n4) {
        return new Image(PixelFormat.INT_ARGB_PRE, intBuffer, n2, n3, 0, 0, n4);
    }

    public static Image fromIntArgbPreData(IntBuffer intBuffer, int n2, int n3, int n4, float f2) {
        return new Image(PixelFormat.INT_ARGB_PRE, intBuffer, n2, n3, 0, 0, n4, f2);
    }

    public static Image fromByteBgraPreData(byte[] arrby, int n2, int n3) {
        return new Image(PixelFormat.BYTE_BGRA_PRE, arrby, n2, n3);
    }

    public static Image fromByteBgraPreData(byte[] arrby, int n2, int n3, float f2) {
        return new Image(PixelFormat.BYTE_BGRA_PRE, ByteBuffer.wrap(arrby), n2, n3, 0, 0, 0, f2);
    }

    public static Image fromByteBgraPreData(ByteBuffer byteBuffer, int n2, int n3) {
        return new Image(PixelFormat.BYTE_BGRA_PRE, byteBuffer, n2, n3);
    }

    public static Image fromByteBgraPreData(ByteBuffer byteBuffer, int n2, int n3, int n4) {
        return new Image(PixelFormat.BYTE_BGRA_PRE, byteBuffer, n2, n3, 0, 0, n4);
    }

    public static Image fromByteBgraPreData(ByteBuffer byteBuffer, int n2, int n3, int n4, float f2) {
        return new Image(PixelFormat.BYTE_BGRA_PRE, byteBuffer, n2, n3, 0, 0, n4, f2);
    }

    public static Image fromByteRgbData(byte[] arrby, int n2, int n3) {
        return new Image(PixelFormat.BYTE_RGB, arrby, n2, n3);
    }

    public static Image fromByteRgbData(ByteBuffer byteBuffer, int n2, int n3) {
        return new Image(PixelFormat.BYTE_RGB, byteBuffer, n2, n3);
    }

    public static Image fromByteRgbData(ByteBuffer byteBuffer, int n2, int n3, int n4) {
        return new Image(PixelFormat.BYTE_RGB, byteBuffer, n2, n3, 0, 0, n4);
    }

    public static Image fromByteRgbData(ByteBuffer byteBuffer, int n2, int n3, int n4, float f2) {
        return new Image(PixelFormat.BYTE_RGB, byteBuffer, n2, n3, 0, 0, n4, f2);
    }

    public static Image fromByteGrayData(byte[] arrby, int n2, int n3) {
        return new Image(PixelFormat.BYTE_GRAY, arrby, n2, n3);
    }

    public static Image fromByteGrayData(ByteBuffer byteBuffer, int n2, int n3) {
        return new Image(PixelFormat.BYTE_GRAY, byteBuffer, n2, n3);
    }

    public static Image fromByteGrayData(ByteBuffer byteBuffer, int n2, int n3, int n4) {
        return new Image(PixelFormat.BYTE_GRAY, byteBuffer, n2, n3, 0, 0, n4);
    }

    public static Image fromByteGrayData(ByteBuffer byteBuffer, int n2, int n3, int n4, float f2) {
        return new Image(PixelFormat.BYTE_GRAY, byteBuffer, n2, n3, 0, 0, n4, f2);
    }

    public static Image fromByteAlphaData(byte[] arrby, int n2, int n3) {
        return new Image(PixelFormat.BYTE_ALPHA, arrby, n2, n3);
    }

    public static Image fromByteAlphaData(ByteBuffer byteBuffer, int n2, int n3) {
        return new Image(PixelFormat.BYTE_ALPHA, byteBuffer, n2, n3);
    }

    public static Image fromByteAlphaData(ByteBuffer byteBuffer, int n2, int n3, int n4) {
        return new Image(PixelFormat.BYTE_ALPHA, byteBuffer, n2, n3, 0, 0, n4);
    }

    public static Image fromByteApple422Data(byte[] arrby, int n2, int n3) {
        return new Image(PixelFormat.BYTE_APPLE_422, arrby, n2, n3);
    }

    public static Image fromByteApple422Data(ByteBuffer byteBuffer, int n2, int n3) {
        return new Image(PixelFormat.BYTE_APPLE_422, byteBuffer, n2, n3);
    }

    public static Image fromByteApple422Data(ByteBuffer byteBuffer, int n2, int n3, int n4) {
        return new Image(PixelFormat.BYTE_APPLE_422, byteBuffer, n2, n3, 0, 0, n4);
    }

    public static Image fromFloatMapData(FloatBuffer floatBuffer, int n2, int n3) {
        return new Image(PixelFormat.FLOAT_XYZW, floatBuffer, n2, n3);
    }

    public static Image convertImageFrame(ImageFrame imageFrame) {
        ByteBuffer byteBuffer = (ByteBuffer)imageFrame.getImageData();
        ImageStorage.ImageType imageType = imageFrame.getImageType();
        int n2 = imageFrame.getWidth();
        int n3 = imageFrame.getHeight();
        int n4 = imageFrame.getStride();
        float f2 = imageFrame.getPixelScale();
        switch (imageType) {
            case GRAY: {
                return Image.fromByteGrayData(byteBuffer, n2, n3, n4, f2);
            }
            case RGB: {
                return Image.fromByteRgbData(byteBuffer, n2, n3, n4, f2);
            }
            case RGBA: {
                ByteBgra.ToByteBgraPreConverter().convert(byteBuffer, 0, n4, byteBuffer, 0, n4, n2, n3);
            }
            case RGBA_PRE: {
                ByteRgba.ToByteBgraConverter().convert(byteBuffer, 0, n4, byteBuffer, 0, n4, n2, n3);
                return Image.fromByteBgraPreData(byteBuffer, n2, n3, n4, f2);
            }
            case GRAY_ALPHA: {
                ByteGrayAlpha.ToByteGrayAlphaPreConverter().convert(byteBuffer, 0, n4, byteBuffer, 0, n4, n2, n3);
            }
            case GRAY_ALPHA_PRE: {
                if (n4 != n2 * 2) {
                    throw new AssertionError((Object)"Bad stride for GRAY_ALPHA");
                }
                byte[] arrby = new byte[n2 * n3 * 4];
                ByteGrayAlphaPre.ToByteBgraPreConverter().convert(byteBuffer, 0, n4, arrby, 0, n2 * 4, n2, n3);
                return Image.fromByteBgraPreData(arrby, n2, n3, f2);
            }
        }
        throw new RuntimeException("Unknown image type: " + (Object)((Object)imageType));
    }

    private Image(PixelFormat pixelFormat, int[] arrn, int n2, int n3) {
        this(pixelFormat, IntBuffer.wrap(arrn), n2, n3, 0, 0, 0, 1.0f);
    }

    private Image(PixelFormat pixelFormat, byte[] arrby, int n2, int n3) {
        this(pixelFormat, ByteBuffer.wrap(arrby), n2, n3, 0, 0, 0, 1.0f);
    }

    private Image(PixelFormat pixelFormat, Buffer buffer, int n2, int n3) {
        this(pixelFormat, buffer, n2, n3, 0, 0, 0, 1.0f);
    }

    private Image(PixelFormat pixelFormat, Buffer buffer, int n2, int n3, int n4, int n5, int n6) {
        this(pixelFormat, buffer, n2, n3, n4, n5, n6, 1.0f);
    }

    private Image(PixelFormat pixelFormat, Buffer buffer, int n2, int n3, int n4, int n5, int n6, float f2) {
        if (pixelFormat == PixelFormat.MULTI_YCbCr_420) {
            throw new IllegalArgumentException("Format not supported " + pixelFormat.name());
        }
        if (n6 == 0) {
            n6 = n2 * pixelFormat.getBytesPerPixelUnit();
        }
        if (buffer == null) {
            throw new IllegalArgumentException("Pixel buffer must be non-null");
        }
        if (n2 <= 0 || n3 <= 0) {
            throw new IllegalArgumentException("Image dimensions must be > 0");
        }
        if (n4 < 0 || n5 < 0) {
            throw new IllegalArgumentException("Image minX and minY must be >= 0");
        }
        if ((n4 + n2) * pixelFormat.getBytesPerPixelUnit() > n6) {
            throw new IllegalArgumentException("Image scanlineStride is too small");
        }
        if (n6 % pixelFormat.getBytesPerPixelUnit() != 0) {
            throw new IllegalArgumentException("Image scanlineStride must be a multiple of the pixel stride");
        }
        this.pixelFormat = pixelFormat;
        this.pixelBuffer = buffer;
        this.width = n2;
        this.height = n3;
        this.minX = n4;
        this.minY = n5;
        this.scanlineStride = n6;
        this.pixelScale = f2;
    }

    public PixelFormat getPixelFormat() {
        return this.pixelFormat;
    }

    public PixelFormat.DataType getDataType() {
        return this.pixelFormat.getDataType();
    }

    public int getBytesPerPixelUnit() {
        return this.pixelFormat.getBytesPerPixelUnit();
    }

    public Buffer getPixelBuffer() {
        return this.pixelBuffer;
    }

    public int getMinX() {
        return this.minX;
    }

    public int getMinY() {
        return this.minY;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getScanlineStride() {
        return this.scanlineStride;
    }

    @Override
    public float getPixelScale() {
        return this.pixelScale;
    }

    public int getRowLength() {
        return this.scanlineStride / this.pixelFormat.getBytesPerPixelUnit();
    }

    public boolean isTightlyPacked() {
        return this.minX == 0 && this.minY == 0 && this.width == this.getRowLength();
    }

    public Image createSubImage(int n2, int n3, int n4, int n5) {
        if (n4 <= 0 || n5 <= 0) {
            throw new IllegalArgumentException("Subimage dimensions must be > 0");
        }
        if (n2 < 0 || n3 < 0) {
            throw new IllegalArgumentException("Subimage minX and minY must be >= 0");
        }
        if (n2 + n4 > this.width) {
            throw new IllegalArgumentException("Subimage minX+width must be <= width of parent image");
        }
        if (n3 + n5 > this.height) {
            throw new IllegalArgumentException("Subimage minY+height must be <= height of parent image");
        }
        Image image = new Image(this.pixelFormat, this.pixelBuffer, n4, n5, this.minX + n2, this.minY + n3, this.scanlineStride);
        image.serial = this.serial;
        return image;
    }

    public Image createPackedCopy() {
        int n2 = this.width * this.pixelFormat.getBytesPerPixelUnit();
        Buffer buffer = Image.createPackedBuffer(this.pixelBuffer, this.pixelFormat, this.minX, this.minY, this.width, this.height, this.scanlineStride);
        return new Image(this.pixelFormat, buffer, this.width, this.height, 0, 0, n2);
    }

    public Image createPackedCopyIfNeeded() {
        int n2 = this.width * this.pixelFormat.getBytesPerPixelUnit();
        if (n2 == this.scanlineStride && this.minX == 0 && this.minY == 0) {
            return this;
        }
        return this.createPackedCopy();
    }

    public static Buffer createPackedBuffer(Buffer buffer, PixelFormat pixelFormat, int n2, int n3, int n4, int n5, int n6) {
        Buffer buffer2;
        if (n6 % pixelFormat.getBytesPerPixelUnit() != 0) {
            throw new IllegalArgumentException("Image scanlineStride must be a multiple of the pixel stride");
        }
        if (pixelFormat == PixelFormat.MULTI_YCbCr_420) {
            throw new IllegalArgumentException("Format unsupported " + (Object)((Object)pixelFormat));
        }
        int n7 = pixelFormat.getElemsPerPixelUnit();
        int n8 = n6 / pixelFormat.getBytesPerPixelUnit();
        int n9 = n8 * n7;
        int n10 = n4 * n7;
        int n11 = n10 * n5;
        int n12 = n2 * n7 + n3 * n9;
        int n13 = 0;
        switch (pixelFormat.getDataType()) {
            case BYTE: {
                ByteBuffer byteBuffer = (ByteBuffer)buffer;
                ByteBuffer byteBuffer2 = BufferUtil.newByteBuffer(n11);
                for (int i2 = 0; i2 < n5; ++i2) {
                    byteBuffer.limit(n12 + n10);
                    byteBuffer.position(n12);
                    byteBuffer2.limit(n13 + n10);
                    byteBuffer2.position(n13);
                    byteBuffer2.put(byteBuffer);
                    n12 += n9;
                    n13 += n10;
                }
                buffer2 = byteBuffer2;
                break;
            }
            case INT: {
                IntBuffer intBuffer = (IntBuffer)buffer;
                IntBuffer intBuffer2 = BufferUtil.newIntBuffer(n11);
                for (int i3 = 0; i3 < n5; ++i3) {
                    intBuffer.limit(n12 + n10);
                    intBuffer.position(n12);
                    intBuffer2.limit(n13 + n10);
                    intBuffer2.position(n13);
                    intBuffer2.put(intBuffer);
                    n12 += n9;
                    n13 += n10;
                }
                buffer2 = intBuffer2;
                break;
            }
            case FLOAT: {
                FloatBuffer floatBuffer = (FloatBuffer)buffer;
                FloatBuffer floatBuffer2 = BufferUtil.newFloatBuffer(n11);
                for (int i4 = 0; i4 < n5; ++i4) {
                    floatBuffer.limit(n12 + n10);
                    floatBuffer.position(n12);
                    floatBuffer2.limit(n13 + n10);
                    floatBuffer2.position(n13);
                    floatBuffer2.put(floatBuffer);
                    n12 += n9;
                    n13 += n10;
                }
                buffer2 = floatBuffer2;
                break;
            }
            default: {
                throw new InternalError("Unknown data type");
            }
        }
        buffer.limit(buffer.capacity());
        buffer.rewind();
        buffer2.limit(buffer2.capacity());
        buffer2.rewind();
        return buffer2;
    }

    public Image iconify(ByteBuffer byteBuffer, int n2, int n3) {
        if (this.pixelFormat == PixelFormat.MULTI_YCbCr_420) {
            throw new IllegalArgumentException("Format not supported " + (Object)((Object)this.pixelFormat));
        }
        int n4 = this.getBytesPerPixelUnit();
        int n5 = n2 * n4;
        ByteToIntPixelConverter byteToIntPixelConverter = n4 == 1 ? ByteGray.ToIntArgbPreConverter() : (this.pixelFormat == PixelFormat.BYTE_BGRA_PRE ? ByteBgraPre.ToIntArgbPreConverter() : ByteRgb.ToIntArgbPreConverter());
        int[] arrn = new int[n2 * n3];
        byteToIntPixelConverter.convert(byteBuffer, 0, n5, arrn, 0, n2, n2, n3);
        return new Image(PixelFormat.INT_ARGB_PRE, arrn, n2, n3);
    }

    public String toString() {
        return super.toString() + " [format=" + (Object)((Object)this.pixelFormat) + " width=" + this.width + " height=" + this.height + " scanlineStride=" + this.scanlineStride + " minX=" + this.minX + " minY=" + this.minY + " pixelBuffer=" + this.pixelBuffer + " bpp=" + this.getBytesPerPixelUnit() + "]";
    }

    public int getSerial() {
        return this.serial[0];
    }

    public Image promoteByteRgbToByteBgra() {
        ByteBuffer byteBuffer = (ByteBuffer)this.pixelBuffer;
        ByteBuffer byteBuffer2 = ByteBuffer.allocate(this.width * this.height * 4);
        int n2 = this.minY * this.scanlineStride + this.minX * 3;
        ByteRgb.ToByteBgraPreConverter().convert(byteBuffer, n2, this.scanlineStride, byteBuffer2, 0, this.width * 4, this.width, this.height);
        return new Image(PixelFormat.BYTE_BGRA_PRE, byteBuffer2, this.width, this.height, 0, 0, this.width * 4, this.getPixelScale());
    }

    private Accessor<?> getPixelAccessor() {
        if (this.pixelaccessor == null) {
            switch (this.getPixelFormat()) {
                default: {
                    this.pixelaccessor = new UnsupportedAccess();
                    break;
                }
                case BYTE_GRAY: {
                    this.pixelaccessor = new ByteAccess(Image.getGrayFXPixelFormat(), ByteGray.getter, null, (ByteBuffer)this.pixelBuffer, 1);
                    break;
                }
                case BYTE_RGB: {
                    this.pixelaccessor = new ByteRgbAccess((ByteBuffer)this.pixelBuffer);
                    break;
                }
                case BYTE_BGRA_PRE: {
                    this.pixelaccessor = new ByteAccess(FX_ByteBgraPre_FORMAT, (ByteBuffer)this.pixelBuffer, 4);
                    break;
                }
                case INT_ARGB_PRE: {
                    this.pixelaccessor = new IntAccess(FX_IntArgbPre_FORMAT, (IntBuffer)this.pixelBuffer);
                }
            }
        }
        if (this.pixelaccessor != null && this.pixelScale != 1.0f) {
            this.pixelaccessor = new ScaledAccessor(this.pixelaccessor, this.pixelScale);
        }
        return this.pixelaccessor;
    }

    @Override
    public javafx.scene.image.PixelFormat<?> getPlatformPixelFormat() {
        return this.getPixelAccessor().getPlatformPixelFormat();
    }

    @Override
    public boolean isWritable() {
        return this.getPixelAccessor().isWritable();
    }

    @Override
    public PlatformImage promoteToWritableImage() {
        return this.getPixelAccessor().promoteToWritableImage();
    }

    @Override
    public int getArgb(int n2, int n3) {
        return this.getPixelAccessor().getArgb(n2, n3);
    }

    @Override
    public void setArgb(int n2, int n3, int n4) {
        this.getPixelAccessor().setArgb(n2, n3, n4);
        this.serial[0] = this.serial[0] + 1;
    }

    @Override
    public <T extends Buffer> void getPixels(int n2, int n3, int n4, int n5, WritablePixelFormat<T> writablePixelFormat, T t2, int n6) {
        this.getPixelAccessor().getPixels(n2, n3, n4, n5, writablePixelFormat, t2, n6);
    }

    @Override
    public void getPixels(int n2, int n3, int n4, int n5, WritablePixelFormat<ByteBuffer> writablePixelFormat, byte[] arrby, int n6, int n7) {
        this.getPixelAccessor().getPixels(n2, n3, n4, n5, writablePixelFormat, arrby, n6, n7);
    }

    @Override
    public void getPixels(int n2, int n3, int n4, int n5, WritablePixelFormat<IntBuffer> writablePixelFormat, int[] arrn, int n6, int n7) {
        this.getPixelAccessor().getPixels(n2, n3, n4, n5, writablePixelFormat, arrn, n6, n7);
    }

    @Override
    public <T extends Buffer> void setPixels(int n2, int n3, int n4, int n5, javafx.scene.image.PixelFormat<T> pixelFormat, T t2, int n6) {
        this.getPixelAccessor().setPixels(n2, n3, n4, n5, pixelFormat, t2, n6);
        this.serial[0] = this.serial[0] + 1;
    }

    @Override
    public void setPixels(int n2, int n3, int n4, int n5, javafx.scene.image.PixelFormat<ByteBuffer> pixelFormat, byte[] arrby, int n6, int n7) {
        this.getPixelAccessor().setPixels(n2, n3, n4, n5, pixelFormat, arrby, n6, n7);
        this.serial[0] = this.serial[0] + 1;
    }

    @Override
    public void setPixels(int n2, int n3, int n4, int n5, javafx.scene.image.PixelFormat<IntBuffer> pixelFormat, int[] arrn, int n6, int n7) {
        this.getPixelAccessor().setPixels(n2, n3, n4, n5, pixelFormat, arrn, n6, n7);
        this.serial[0] = this.serial[0] + 1;
    }

    @Override
    public void setPixels(int n2, int n3, int n4, int n5, PixelReader pixelReader, int n6, int n7) {
        this.getPixelAccessor().setPixels(n2, n3, n4, n5, pixelReader, n6, n7);
        this.serial[0] = this.serial[0] + 1;
    }

    public boolean isOpaque() {
        return this.pixelFormat.isOpaque();
    }

    static <I extends Buffer> PixelSetter<I> getSetterIfWritable(javafx.scene.image.PixelFormat<I> pixelFormat) {
        if (pixelFormat instanceof WritablePixelFormat) {
            return PixelUtils.getSetter((WritablePixelFormat)pixelFormat);
        }
        return null;
    }

    static javafx.scene.image.PixelFormat<ByteBuffer> getGrayFXPixelFormat() {
        if (FX_ByteGray_FORMAT == null) {
            int[] arrn = new int[256];
            int n2 = -16777216;
            for (int i2 = 0; i2 < 256; ++i2) {
                arrn[i2] = n2;
                n2 += 65793;
            }
            FX_ByteGray_FORMAT = javafx.scene.image.PixelFormat.createByteIndexedPremultipliedInstance(arrn);
        }
        return FX_ByteGray_FORMAT;
    }

    class ByteRgbAccess
    extends ByteAccess {
        public ByteRgbAccess(ByteBuffer byteBuffer) {
            super(FX_ByteRgb_FORMAT, byteBuffer, 3);
        }

        @Override
        public PlatformImage promoteToWritableImage() {
            return Image.this.promoteByteRgbToByteBgra();
        }
    }

    class UnsupportedAccess
    extends ByteAccess {
        private UnsupportedAccess() {
            super(null, null, null, null, 0);
        }
    }

    class IntAccess
    extends BaseAccessor<IntBuffer> {
        IntAccess(javafx.scene.image.PixelFormat<IntBuffer> pixelFormat, IntBuffer intBuffer) {
            super(Image.this, pixelFormat, (Buffer)intBuffer, 1);
        }

        @Override
        public void getPixels(int n2, int n3, int n4, int n5, WritablePixelFormat<ByteBuffer> writablePixelFormat, byte[] arrby, int n6, int n7) {
            BytePixelSetter bytePixelSetter = PixelUtils.getByteSetter(writablePixelFormat);
            IntToBytePixelConverter intToBytePixelConverter = PixelUtils.getI2BConverter(this.getGetter(), bytePixelSetter);
            intToBytePixelConverter.convert((IntBuffer)this.getBuffer(), this.getIndex(n2, n3), this.scanlineElems, arrby, n6, n7, n4, n5);
        }

        @Override
        public void getPixels(int n2, int n3, int n4, int n5, WritablePixelFormat<IntBuffer> writablePixelFormat, int[] arrn, int n6, int n7) {
            IntPixelSetter intPixelSetter = PixelUtils.getIntSetter(writablePixelFormat);
            IntToIntPixelConverter intToIntPixelConverter = PixelUtils.getI2IConverter(this.getGetter(), intPixelSetter);
            intToIntPixelConverter.convert((IntBuffer)this.getBuffer(), this.getIndex(n2, n3), this.scanlineElems, arrn, n6, n7, n4, n5);
        }

        @Override
        public void setPixels(int n2, int n3, int n4, int n5, javafx.scene.image.PixelFormat<ByteBuffer> pixelFormat, byte[] arrby, int n6, int n7) {
            BytePixelGetter bytePixelGetter = PixelUtils.getByteGetter(pixelFormat);
            ByteToIntPixelConverter byteToIntPixelConverter = PixelUtils.getB2IConverter(bytePixelGetter, this.getSetter());
            byteToIntPixelConverter.convert(arrby, n6, n7, (IntBuffer)this.getBuffer(), this.getIndex(n2, n3), this.scanlineElems, n4, n5);
        }

        @Override
        public void setPixels(int n2, int n3, int n4, int n5, javafx.scene.image.PixelFormat<IntBuffer> pixelFormat, int[] arrn, int n6, int n7) {
            IntPixelGetter intPixelGetter = PixelUtils.getIntGetter(pixelFormat);
            IntToIntPixelConverter intToIntPixelConverter = PixelUtils.getI2IConverter(intPixelGetter, this.getSetter());
            intToIntPixelConverter.convert(arrn, n6, n7, (IntBuffer)this.getBuffer(), this.getIndex(n2, n3), this.scanlineElems, n4, n5);
        }

        @Override
        public void setPixels(int n2, int n3, int n4, int n5, PixelReader pixelReader, int n6, int n7) {
            IntBuffer intBuffer = ((IntBuffer)this.theBuffer).duplicate();
            intBuffer.position(intBuffer.position() + this.getIndex(n2, n3));
            pixelReader.getPixels(n6, n7, n4, n5, (WritablePixelFormat)this.theFormat, intBuffer, this.scanlineElems);
        }
    }

    class ByteAccess
    extends BaseAccessor<ByteBuffer> {
        ByteAccess(javafx.scene.image.PixelFormat<ByteBuffer> pixelFormat, PixelGetter<ByteBuffer> pixelGetter, PixelSetter<ByteBuffer> pixelSetter, ByteBuffer byteBuffer, int n2) {
            super(Image.this, pixelFormat, pixelGetter, pixelSetter, (Buffer)byteBuffer, n2);
        }

        ByteAccess(javafx.scene.image.PixelFormat<ByteBuffer> pixelFormat, ByteBuffer byteBuffer, int n2) {
            super(Image.this, pixelFormat, (Buffer)byteBuffer, n2);
        }

        @Override
        public void getPixels(int n2, int n3, int n4, int n5, WritablePixelFormat<ByteBuffer> writablePixelFormat, byte[] arrby, int n6, int n7) {
            BytePixelSetter bytePixelSetter = PixelUtils.getByteSetter(writablePixelFormat);
            ByteToBytePixelConverter byteToBytePixelConverter = PixelUtils.getB2BConverter(this.getGetter(), bytePixelSetter);
            byteToBytePixelConverter.convert((ByteBuffer)this.getBuffer(), this.getIndex(n2, n3), this.scanlineElems, arrby, n6, n7, n4, n5);
        }

        @Override
        public void getPixels(int n2, int n3, int n4, int n5, WritablePixelFormat<IntBuffer> writablePixelFormat, int[] arrn, int n6, int n7) {
            IntPixelSetter intPixelSetter = PixelUtils.getIntSetter(writablePixelFormat);
            ByteToIntPixelConverter byteToIntPixelConverter = PixelUtils.getB2IConverter(this.getGetter(), intPixelSetter);
            byteToIntPixelConverter.convert((ByteBuffer)this.getBuffer(), this.getIndex(n2, n3), this.scanlineElems, arrn, n6, n7, n4, n5);
        }

        @Override
        public void setPixels(int n2, int n3, int n4, int n5, javafx.scene.image.PixelFormat<ByteBuffer> pixelFormat, byte[] arrby, int n6, int n7) {
            BytePixelGetter bytePixelGetter = PixelUtils.getByteGetter(pixelFormat);
            ByteToBytePixelConverter byteToBytePixelConverter = PixelUtils.getB2BConverter(bytePixelGetter, this.getSetter());
            byteToBytePixelConverter.convert(arrby, n6, n7, (ByteBuffer)this.getBuffer(), this.getIndex(n2, n3), this.scanlineElems, n4, n5);
        }

        @Override
        public void setPixels(int n2, int n3, int n4, int n5, javafx.scene.image.PixelFormat<IntBuffer> pixelFormat, int[] arrn, int n6, int n7) {
            IntPixelGetter intPixelGetter = PixelUtils.getIntGetter(pixelFormat);
            IntToBytePixelConverter intToBytePixelConverter = PixelUtils.getI2BConverter(intPixelGetter, this.getSetter());
            intToBytePixelConverter.convert(arrn, n6, n7, (ByteBuffer)this.getBuffer(), this.getIndex(n2, n3), this.scanlineElems, n4, n5);
        }

        @Override
        public void setPixels(int n2, int n3, int n4, int n5, PixelReader pixelReader, int n6, int n7) {
            ByteBuffer byteBuffer = ((ByteBuffer)this.theBuffer).duplicate();
            byteBuffer.position(byteBuffer.position() + this.getIndex(n2, n3));
            pixelReader.getPixels(n6, n7, n4, n5, (WritablePixelFormat)this.theFormat, byteBuffer, this.scanlineElems);
        }
    }

    abstract class BaseAccessor<I extends Buffer>
    extends Accessor<I> {
        javafx.scene.image.PixelFormat<I> theFormat;
        PixelGetter<I> theGetter;
        PixelSetter<I> theSetter;
        I theBuffer;
        int pixelElems;
        int scanlineElems;
        int offsetElems;
        final /* synthetic */ Image this$0;

        /*
         * WARNING - Possible parameter corruption
         */
        BaseAccessor(javafx.scene.image.PixelFormat<I> pixelFormat, I i2, int n3) {
            this((Image)n2, pixelFormat, PixelUtils.getGetter(pixelFormat), Image.getSetterIfWritable(pixelFormat), (Buffer)i2, n3);
        }

        /*
         * WARNING - Possible parameter corruption
         */
        BaseAccessor(javafx.scene.image.PixelFormat<I> pixelFormat, PixelGetter<I> pixelGetter, PixelSetter<I> pixelSetter, I i2, int n3) {
            this.this$0 = (Image)n2;
            this.theFormat = pixelFormat;
            this.theGetter = pixelGetter;
            this.theSetter = pixelSetter;
            this.theBuffer = i2;
            this.pixelElems = n3;
            this.scanlineElems = ((Image)n2).scanlineStride / ((Image)n2).pixelFormat.getDataType().getSizeInBytes();
            this.offsetElems = ((Image)n2).minY * this.scanlineElems + ((Image)n2).minX * n3;
        }

        public int getIndex(int n2, int n3) {
            if (n2 < 0 || n3 < 0 || n2 >= this.this$0.width || n3 >= this.this$0.height) {
                throw new IndexOutOfBoundsException(n2 + ", " + n3);
            }
            return this.offsetElems + n3 * this.scanlineElems + n2 * this.pixelElems;
        }

        public I getBuffer() {
            return this.theBuffer;
        }

        public PixelGetter<I> getGetter() {
            if (this.theGetter == null) {
                throw new UnsupportedOperationException("Unsupported Image type");
            }
            return this.theGetter;
        }

        public PixelSetter<I> getSetter() {
            if (this.theSetter == null) {
                throw new UnsupportedOperationException("Unsupported Image type");
            }
            return this.theSetter;
        }

        @Override
        public javafx.scene.image.PixelFormat<I> getPlatformPixelFormat() {
            return this.theFormat;
        }

        @Override
        public boolean isWritable() {
            return this.theSetter != null;
        }

        @Override
        public PlatformImage promoteToWritableImage() {
            return this.this$0;
        }

        @Override
        public int getArgb(int n2, int n3) {
            return this.getGetter().getArgb(this.getBuffer(), this.getIndex(n2, n3));
        }

        @Override
        public void setArgb(int n2, int n3, int n4) {
            this.getSetter().setArgb(this.getBuffer(), this.getIndex(n2, n3), n4);
        }

        @Override
        public <T extends Buffer> void getPixels(int n2, int n3, int n4, int n5, WritablePixelFormat<T> writablePixelFormat, T t2, int n6) {
            PixelSetter<T> pixelSetter = PixelUtils.getSetter(writablePixelFormat);
            PixelConverter<I, I> pixelConverter = PixelUtils.getConverter(this.getGetter(), pixelSetter);
            int n7 = t2.position();
            pixelConverter.convert(this.getBuffer(), this.getIndex(n2, n3), this.scanlineElems, t2, n7, n6, n4, n5);
        }

        @Override
        public <T extends Buffer> void setPixels(int n2, int n3, int n4, int n5, javafx.scene.image.PixelFormat<T> pixelFormat, T t2, int n6) {
            PixelGetter<T> pixelGetter = PixelUtils.getGetter(pixelFormat);
            PixelConverter<T, I> pixelConverter = PixelUtils.getConverter(pixelGetter, this.getSetter());
            int n7 = t2.position();
            pixelConverter.convert(t2, n7, n6, this.getBuffer(), this.getIndex(n2, n3), this.scanlineElems, n4, n5);
        }
    }

    class ScaledAccessor<I extends Buffer>
    extends Accessor<I> {
        Accessor<I> theDelegate;
        float pixelScale;

        ScaledAccessor(Accessor<I> accessor, float f2) {
            this.theDelegate = accessor;
            this.pixelScale = f2;
        }

        private int scale(int n2) {
            return (int)(((float)n2 + 0.5f) * this.pixelScale);
        }

        @Override
        public int getArgb(int n2, int n3) {
            return this.theDelegate.getArgb(this.scale(n2), this.scale(n3));
        }

        @Override
        public void setArgb(int n2, int n3, int n4) {
            throw new UnsupportedOperationException("Pixel setting for scaled images not supported yet");
        }

        @Override
        public javafx.scene.image.PixelFormat<I> getPlatformPixelFormat() {
            return this.theDelegate.getPlatformPixelFormat();
        }

        @Override
        public boolean isWritable() {
            return this.theDelegate.isWritable();
        }

        @Override
        public PlatformImage promoteToWritableImage() {
            throw new UnsupportedOperationException("Pixel setting for scaled images not supported yet");
        }

        @Override
        public <T extends Buffer> void getPixels(int n2, int n3, int n4, int n5, WritablePixelFormat<T> writablePixelFormat, T t2, int n6) {
            PixelSetter<T> pixelSetter = PixelUtils.getSetter(writablePixelFormat);
            int n7 = t2.position();
            int n8 = pixelSetter.getNumElements();
            for (int i2 = 0; i2 < n5; ++i2) {
                int n9 = this.scale(n3 + i2);
                int n10 = n7;
                for (int i3 = 0; i3 < n4; ++i3) {
                    int n11 = this.scale(n2 + i3);
                    pixelSetter.setArgb(t2, n10, this.theDelegate.getArgb(n11, n9));
                    n10 += n8;
                }
                n7 += n6;
            }
        }

        @Override
        public void getPixels(int n2, int n3, int n4, int n5, WritablePixelFormat<ByteBuffer> writablePixelFormat, byte[] arrby, int n6, int n7) {
            ByteBuffer byteBuffer = ByteBuffer.wrap(arrby);
            byteBuffer.position(n6);
            this.getPixels(n2, n3, n4, n5, writablePixelFormat, byteBuffer, n7);
        }

        @Override
        public void getPixels(int n2, int n3, int n4, int n5, WritablePixelFormat<IntBuffer> writablePixelFormat, int[] arrn, int n6, int n7) {
            IntBuffer intBuffer = IntBuffer.wrap(arrn);
            intBuffer.position(n6);
            this.getPixels(n2, n3, n4, n5, writablePixelFormat, intBuffer, n7);
        }

        @Override
        public <T extends Buffer> void setPixels(int n2, int n3, int n4, int n5, javafx.scene.image.PixelFormat<T> pixelFormat, T t2, int n6) {
            throw new UnsupportedOperationException("Pixel setting for scaled images not supported yet");
        }

        @Override
        public void setPixels(int n2, int n3, int n4, int n5, javafx.scene.image.PixelFormat<ByteBuffer> pixelFormat, byte[] arrby, int n6, int n7) {
            throw new UnsupportedOperationException("Pixel setting for scaled images not supported yet");
        }

        @Override
        public void setPixels(int n2, int n3, int n4, int n5, javafx.scene.image.PixelFormat<IntBuffer> pixelFormat, int[] arrn, int n6, int n7) {
            throw new UnsupportedOperationException("Pixel setting for scaled images not supported yet");
        }

        @Override
        public void setPixels(int n2, int n3, int n4, int n5, PixelReader pixelReader, int n6, int n7) {
            throw new UnsupportedOperationException("Pixel setting for scaled images not supported yet");
        }
    }

    abstract class Accessor<I extends Buffer> {
        Accessor() {
        }

        public abstract int getArgb(int var1, int var2);

        public abstract void setArgb(int var1, int var2, int var3);

        public abstract javafx.scene.image.PixelFormat<I> getPlatformPixelFormat();

        public abstract boolean isWritable();

        public abstract PlatformImage promoteToWritableImage();

        public abstract <T extends Buffer> void getPixels(int var1, int var2, int var3, int var4, WritablePixelFormat<T> var5, T var6, int var7);

        public abstract void getPixels(int var1, int var2, int var3, int var4, WritablePixelFormat<ByteBuffer> var5, byte[] var6, int var7, int var8);

        public abstract void getPixels(int var1, int var2, int var3, int var4, WritablePixelFormat<IntBuffer> var5, int[] var6, int var7, int var8);

        public abstract <T extends Buffer> void setPixels(int var1, int var2, int var3, int var4, javafx.scene.image.PixelFormat<T> var5, T var6, int var7);

        public abstract void setPixels(int var1, int var2, int var3, int var4, javafx.scene.image.PixelFormat<ByteBuffer> var5, byte[] var6, int var7, int var8);

        public abstract void setPixels(int var1, int var2, int var3, int var4, javafx.scene.image.PixelFormat<IntBuffer> var5, int[] var6, int var7, int var8);

        public abstract void setPixels(int var1, int var2, int var3, int var4, PixelReader var5, int var6, int var7);
    }
}

