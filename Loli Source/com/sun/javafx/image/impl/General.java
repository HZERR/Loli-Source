/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.image.impl;

import com.sun.javafx.image.AlphaType;
import com.sun.javafx.image.BytePixelGetter;
import com.sun.javafx.image.BytePixelSetter;
import com.sun.javafx.image.ByteToBytePixelConverter;
import com.sun.javafx.image.ByteToIntPixelConverter;
import com.sun.javafx.image.IntPixelGetter;
import com.sun.javafx.image.IntPixelSetter;
import com.sun.javafx.image.IntToBytePixelConverter;
import com.sun.javafx.image.IntToIntPixelConverter;
import com.sun.javafx.image.impl.BaseByteToByteConverter;
import com.sun.javafx.image.impl.BaseByteToIntConverter;
import com.sun.javafx.image.impl.BaseIntToByteConverter;
import com.sun.javafx.image.impl.BaseIntToIntConverter;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class General {
    public static ByteToBytePixelConverter create(BytePixelGetter bytePixelGetter, BytePixelSetter bytePixelSetter) {
        return new ByteToByteGeneralConverter(bytePixelGetter, bytePixelSetter);
    }

    public static ByteToIntPixelConverter create(BytePixelGetter bytePixelGetter, IntPixelSetter intPixelSetter) {
        return new ByteToIntGeneralConverter(bytePixelGetter, intPixelSetter);
    }

    public static IntToBytePixelConverter create(IntPixelGetter intPixelGetter, BytePixelSetter bytePixelSetter) {
        return new IntToByteGeneralConverter(intPixelGetter, bytePixelSetter);
    }

    public static IntToIntPixelConverter create(IntPixelGetter intPixelGetter, IntPixelSetter intPixelSetter) {
        return new IntToIntGeneralConverter(intPixelGetter, intPixelSetter);
    }

    static class IntToIntGeneralConverter
    extends BaseIntToIntConverter {
        boolean usePremult;

        public IntToIntGeneralConverter(IntPixelGetter intPixelGetter, IntPixelSetter intPixelSetter) {
            super(intPixelGetter, intPixelSetter);
            this.usePremult = intPixelGetter.getAlphaType() != AlphaType.NONPREMULTIPLIED && intPixelSetter.getAlphaType() != AlphaType.NONPREMULTIPLIED;
        }

        @Override
        void doConvert(int[] arrn, int n2, int n3, int[] arrn2, int n4, int n5, int n6, int n7) {
            n3 -= n6;
            n5 -= n6;
            while (--n7 >= 0) {
                for (int i2 = 0; i2 < n6; ++i2) {
                    if (this.usePremult) {
                        this.setter.setArgbPre(arrn2, n4, this.getter.getArgbPre(arrn, n2));
                    } else {
                        this.setter.setArgb(arrn2, n4, this.getter.getArgb(arrn, n2));
                    }
                    ++n2;
                    ++n4;
                }
                n2 += n3;
                n4 += n5;
            }
        }

        @Override
        void doConvert(IntBuffer intBuffer, int n2, int n3, IntBuffer intBuffer2, int n4, int n5, int n6, int n7) {
            n3 -= n6;
            n5 -= n6;
            while (--n7 >= 0) {
                for (int i2 = 0; i2 < n6; ++i2) {
                    if (this.usePremult) {
                        this.setter.setArgbPre(intBuffer2, n4, this.getter.getArgbPre(intBuffer, n2));
                    } else {
                        this.setter.setArgb(intBuffer2, n4, this.getter.getArgb(intBuffer, n2));
                    }
                    ++n2;
                    ++n4;
                }
                n2 += n3;
                n4 += n5;
            }
        }
    }

    static class IntToByteGeneralConverter
    extends BaseIntToByteConverter {
        boolean usePremult;

        public IntToByteGeneralConverter(IntPixelGetter intPixelGetter, BytePixelSetter bytePixelSetter) {
            super(intPixelGetter, bytePixelSetter);
            this.usePremult = intPixelGetter.getAlphaType() != AlphaType.NONPREMULTIPLIED && bytePixelSetter.getAlphaType() != AlphaType.NONPREMULTIPLIED;
        }

        @Override
        void doConvert(int[] arrn, int n2, int n3, byte[] arrby, int n4, int n5, int n6, int n7) {
            n3 -= n6;
            n5 -= this.nDstElems * n6;
            while (--n7 >= 0) {
                for (int i2 = 0; i2 < n6; ++i2) {
                    if (this.usePremult) {
                        this.setter.setArgbPre(arrby, n4, this.getter.getArgbPre(arrn, n2));
                    } else {
                        this.setter.setArgb(arrby, n4, this.getter.getArgb(arrn, n2));
                    }
                    ++n2;
                    n4 += this.nDstElems;
                }
                n2 += n3;
                n4 += n5;
            }
        }

        @Override
        void doConvert(IntBuffer intBuffer, int n2, int n3, ByteBuffer byteBuffer, int n4, int n5, int n6, int n7) {
            n3 -= n6;
            n5 -= this.nDstElems * n6;
            while (--n7 >= 0) {
                for (int i2 = 0; i2 < n6; ++i2) {
                    if (this.usePremult) {
                        this.setter.setArgbPre(byteBuffer, n4, this.getter.getArgbPre(intBuffer, n2));
                    } else {
                        this.setter.setArgb(byteBuffer, n4, this.getter.getArgb(intBuffer, n2));
                    }
                    ++n2;
                    n4 += this.nDstElems;
                }
                n2 += n3;
                n4 += n5;
            }
        }
    }

    static class ByteToIntGeneralConverter
    extends BaseByteToIntConverter {
        boolean usePremult;

        ByteToIntGeneralConverter(BytePixelGetter bytePixelGetter, IntPixelSetter intPixelSetter) {
            super(bytePixelGetter, intPixelSetter);
            this.usePremult = bytePixelGetter.getAlphaType() != AlphaType.NONPREMULTIPLIED && intPixelSetter.getAlphaType() != AlphaType.NONPREMULTIPLIED;
        }

        @Override
        void doConvert(byte[] arrby, int n2, int n3, int[] arrn, int n4, int n5, int n6, int n7) {
            n3 -= this.nSrcElems * n6;
            n5 -= n6;
            while (--n7 >= 0) {
                for (int i2 = 0; i2 < n6; ++i2) {
                    if (this.usePremult) {
                        this.setter.setArgbPre(arrn, n4, this.getter.getArgbPre(arrby, n2));
                    } else {
                        this.setter.setArgb(arrn, n4, this.getter.getArgb(arrby, n2));
                    }
                    n2 += this.nSrcElems;
                    ++n4;
                }
                n2 += n3;
                n4 += n5;
            }
        }

        @Override
        void doConvert(ByteBuffer byteBuffer, int n2, int n3, IntBuffer intBuffer, int n4, int n5, int n6, int n7) {
            n3 -= this.nSrcElems * n6;
            n5 -= n6;
            while (--n7 >= 0) {
                for (int i2 = 0; i2 < n6; ++i2) {
                    if (this.usePremult) {
                        this.setter.setArgbPre(intBuffer, n4, this.getter.getArgbPre(byteBuffer, n2));
                    } else {
                        this.setter.setArgb(intBuffer, n4, this.getter.getArgb(byteBuffer, n2));
                    }
                    n2 += this.nSrcElems;
                    ++n4;
                }
                n2 += n3;
                n4 += n5;
            }
        }
    }

    static class ByteToByteGeneralConverter
    extends BaseByteToByteConverter {
        boolean usePremult;

        ByteToByteGeneralConverter(BytePixelGetter bytePixelGetter, BytePixelSetter bytePixelSetter) {
            super(bytePixelGetter, bytePixelSetter);
            this.usePremult = bytePixelGetter.getAlphaType() != AlphaType.NONPREMULTIPLIED && bytePixelSetter.getAlphaType() != AlphaType.NONPREMULTIPLIED;
        }

        @Override
        void doConvert(byte[] arrby, int n2, int n3, byte[] arrby2, int n4, int n5, int n6, int n7) {
            n3 -= this.nSrcElems * n6;
            n5 -= this.nDstElems * n6;
            while (--n7 >= 0) {
                for (int i2 = 0; i2 < n6; ++i2) {
                    if (this.usePremult) {
                        this.setter.setArgbPre(arrby2, n4, this.getter.getArgbPre(arrby, n2));
                    } else {
                        this.setter.setArgb(arrby2, n4, this.getter.getArgb(arrby, n2));
                    }
                    n2 += this.nSrcElems;
                    n4 += this.nDstElems;
                }
                n2 += n3;
                n4 += n5;
            }
        }

        @Override
        void doConvert(ByteBuffer byteBuffer, int n2, int n3, ByteBuffer byteBuffer2, int n4, int n5, int n6, int n7) {
            n3 -= this.nSrcElems * n6;
            n5 -= this.nDstElems * n6;
            while (--n7 >= 0) {
                for (int i2 = 0; i2 < n6; ++i2) {
                    if (this.usePremult) {
                        this.setter.setArgbPre(byteBuffer2, n4, this.getter.getArgbPre(byteBuffer, n2));
                    } else {
                        this.setter.setArgb(byteBuffer2, n4, this.getter.getArgb(byteBuffer, n2));
                    }
                    n2 += this.nSrcElems;
                    n4 += this.nDstElems;
                }
                n2 += n3;
                n4 += n5;
            }
        }
    }
}

