/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl.shape;

import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.openpisces.AlphaConsumer;
import com.sun.openpisces.Renderer;
import com.sun.prism.BasicStroke;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.shape.MaskData;
import com.sun.prism.impl.shape.OpenPiscesPrismUtils;
import com.sun.prism.impl.shape.ShapeRasterizer;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class OpenPiscesRasterizer
implements ShapeRasterizer {
    private static MaskData emptyData = MaskData.create(new byte[1], 0, 0, 1, 1);
    private static Consumer savedConsumer;

    @Override
    public MaskData getMaskData(Shape shape, BasicStroke basicStroke, RectBounds rectBounds, BaseTransform baseTransform, boolean bl, boolean bl2) {
        Rectangle rectangle;
        if (basicStroke != null && basicStroke.getType() != 0) {
            shape = basicStroke.createStrokedShape(shape);
            basicStroke = null;
        }
        if (rectBounds == null) {
            if (basicStroke != null) {
                shape = basicStroke.createStrokedShape(shape);
                basicStroke = null;
            }
            rectBounds = new RectBounds();
            rectBounds = (RectBounds)baseTransform.transform(shape.getBounds(), rectBounds);
        }
        if ((rectangle = new Rectangle(rectBounds)).isEmpty()) {
            return emptyData;
        }
        Renderer renderer = null;
        if (shape instanceof Path2D) {
            renderer = OpenPiscesPrismUtils.setupRenderer((Path2D)shape, basicStroke, baseTransform, rectangle, bl2);
        }
        if (renderer == null) {
            renderer = OpenPiscesPrismUtils.setupRenderer(shape, basicStroke, baseTransform, rectangle, bl2);
        }
        int n2 = renderer.getOutpixMinX();
        int n3 = renderer.getOutpixMinY();
        int n4 = renderer.getOutpixMaxX();
        int n5 = renderer.getOutpixMaxY();
        int n6 = n4 - n2;
        int n7 = n5 - n3;
        if (n6 <= 0 || n7 <= 0) {
            return emptyData;
        }
        Consumer consumer = savedConsumer;
        if (consumer == null || n6 * n7 > consumer.getAlphaLength()) {
            int n8 = n6 * n7 + 4095 & 0xFFFFF000;
            savedConsumer = consumer = new Consumer(n8);
            if (PrismSettings.verbose) {
                System.out.println("new alphas");
            }
        }
        consumer.setBoundsNoClone(n2, n3, n6, n7);
        renderer.produceAlphas(consumer);
        return consumer.getMaskData();
    }

    private static class Consumer
    implements AlphaConsumer {
        static byte[] savedAlphaMap;
        int x;
        int y;
        int width;
        int height;
        byte[] alphas;
        byte[] alphaMap;
        ByteBuffer alphabuffer;
        MaskData maskdata = new MaskData();

        public Consumer(int n2) {
            this.alphas = new byte[n2];
            this.alphabuffer = ByteBuffer.wrap(this.alphas);
        }

        public void setBoundsNoClone(int n2, int n3, int n4, int n5) {
            this.x = n2;
            this.y = n3;
            this.width = n4;
            this.height = n5;
            this.maskdata.update(this.alphabuffer, n2, n3, n4, n5);
        }

        @Override
        public int getOriginX() {
            return this.x;
        }

        @Override
        public int getOriginY() {
            return this.y;
        }

        @Override
        public int getWidth() {
            return this.width;
        }

        @Override
        public int getHeight() {
            return this.height;
        }

        public byte[] getAlphasNoClone() {
            return this.alphas;
        }

        public int getAlphaLength() {
            return this.alphas.length;
        }

        public MaskData getMaskData() {
            return this.maskdata;
        }

        @Override
        public void setMaxAlpha(int n2) {
            byte[] arrby = savedAlphaMap;
            if (arrby == null || arrby.length != n2 + 1) {
                arrby = new byte[n2 + 1];
                for (int i2 = 0; i2 <= n2; ++i2) {
                    arrby[i2] = (byte)((i2 * 255 + n2 / 2) / n2);
                }
                savedAlphaMap = arrby;
            }
            this.alphaMap = arrby;
        }

        @Override
        public void setAndClearRelativeAlphas(int[] arrn, int n2, int n3, int n4) {
            int n5 = this.width;
            int n6 = (n2 - this.y) * n5;
            byte[] arrby = this.alphas;
            byte[] arrby2 = this.alphaMap;
            int n7 = 0;
            for (int i2 = 0; i2 < n5; ++i2) {
                arrn[i2] = 0;
                arrby[n6 + i2] = arrby2[n7 += arrn[i2]];
            }
        }

        public void setAndClearRelativeAlphas2(int[] arrn, int n2, int n3, int n4) {
            if (n4 >= n3) {
                int n5;
                byte[] arrby = this.alphas;
                byte[] arrby2 = this.alphaMap;
                int n6 = n3 - this.x;
                int n7 = n4 - this.x;
                int n8 = this.width;
                int n9 = (n2 - this.y) * n8;
                for (n5 = 0; n5 < n6; ++n5) {
                    arrby[n9 + n5] = 0;
                }
                int n10 = 0;
                while (n5 <= n7) {
                    byte by;
                    arrn[n5] = 0;
                    arrby[n9 + n5] = by = arrby2[n10 += arrn[n5]];
                    ++n5;
                }
                arrn[n5] = 0;
                while (n5 < n8) {
                    arrby[n9 + n5] = 0;
                    ++n5;
                }
            } else {
                Arrays.fill(arrn, 0);
            }
        }
    }
}

