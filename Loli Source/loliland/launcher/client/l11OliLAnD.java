/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.gif4j.GifDecoder;
import com.gif4j.GifFrame;
import com.gif4j.GifImage;
import de.matthiasmann.twl.utils.PNGDecoder;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import loliland.launcher.client.l01O01Iiland;
import loliland.launcher.client.l1lIOlAND;
import loliland.launcher.client.lI10ilAnd;
import loliland.launcher.client.lI1il011LAnD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class l11OliLAnD {
    public static ExecutorService I1O1I1LaNd = Executors.newFixedThreadPool(2);
    static final Logger OOOIilanD = LogManager.getLogger();
    static final String lI00OlAND = "";
    private static int Oill1LAnD = -1;
    private int lIOILand;
    private int[] lil0liLand;
    private int[] iilIi1laND;
    String lli0OiIlAND;
    String li0iOILAND;
    protected File O1il1llOLANd;
    private volatile boolean lli011lLANd;
    private int l0illAND;
    private int IO11O0LANd;

    public l11OliLAnD(String string, String string2) {
        this.lli0OiIlAND = lI00OlAND + string;
        this.li0iOILAND = string2;
        this.O1il1llOLANd = l1lIOlAND.lI00OlAND(this.lli0OiIlAND, this.li0iOILAND);
    }

    private boolean Oill1LAnD() {
        return this.lli011lLANd;
    }

    public int OOOIilanD() {
        try {
            if (!this.Oill1LAnD()) {
                if (Oill1LAnD == -1) {
                    Oill1LAnD = lI1il011LAnD.I1O1I1LaNd();
                    try {
                        lI1il011LAnD.I1O1I1LaNd(Oill1LAnD, new BufferedImage(1, 1, 2));
                    }
                    catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
                return Oill1LAnD;
            }
            if (this.lil0liLand.length == 1) {
                return this.lil0liLand[0];
            }
            long l2 = System.currentTimeMillis() % (long)this.lIOILand;
            int n2 = 0;
            int n3 = 0;
            while ((long)n3 <= l2) {
                if (n2 >= this.lil0liLand.length) {
                    n2 = 1;
                    break;
                }
                n3 += this.iilIi1laND[n2];
                ++n2;
            }
            if (n2 - 1 >= this.lil0liLand.length) {
                return this.lil0liLand[this.lil0liLand.length - 1];
            }
            if (n2 - 1 < 0) {
                return this.lil0liLand[0];
            }
            return this.lil0liLand[n2 - 1];
        }
        catch (Throwable throwable) {
            return Oill1LAnD;
        }
    }

    public void lI00OlAND() {
        try {
            this.I1O1I1LaNd();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    void I1O1I1LaNd() throws Exception {
        FileInputStream fileInputStream = new FileInputStream(this.O1il1llOLANd);
        this.I1O1I1LaNd(fileInputStream, l01O01Iiland.I1O1I1LaNd(l1lIOlAND.OOOIilanD(this.O1il1llOLANd.getName())));
    }

    void I1O1I1LaNd(InputStream inputStream, l01O01Iiland l01O01Iiland2) {
        try {
            if (l01O01Iiland2 == null) {
                return;
            }
            switch (l01O01Iiland2) {
                case I1O1I1LaNd: {
                    GifImage gifImage = GifDecoder.decode(inputStream);
                    int n2 = gifImage.getNumberOfFrames();
                    this.lil0liLand = new int[n2];
                    this.iilIi1laND = new int[n2];
                    BufferedImage bufferedImage = gifImage.getFrame(0).getAsBufferedImage();
                    this.l0illAND = bufferedImage.getHeight();
                    this.IO11O0LANd = bufferedImage.getWidth();
                    this.lIOILand = 0;
                    BufferedImage bufferedImage2 = new BufferedImage(gifImage.getCurrentLogicWidth(), gifImage.getCurrentLogicHeight(), 2);
                    Graphics graphics = bufferedImage2.getGraphics();
                    graphics.drawImage(bufferedImage, 0, 0, null);
                    for (int i2 = 0; i2 < n2; ++i2) {
                        GifFrame gifFrame = gifImage.getFrame(i2);
                        graphics.drawImage(gifFrame.getAsBufferedImage(), 0, 0, null);
                        ColorModel colorModel = bufferedImage2.getColorModel();
                        boolean bl = colorModel.isAlphaPremultiplied();
                        WritableRaster writableRaster = bufferedImage2.copyData(null);
                        this.iilIi1laND[i2] = gifFrame.getDelay() * 10;
                        this.lIOILand += this.iilIi1laND[i2];
                        int n3 = i2;
                        BufferedImage bufferedImage3 = new BufferedImage(colorModel, writableRaster, bl, null);
                        int[] arrn = new int[bufferedImage3.getWidth() * bufferedImage3.getHeight()];
                        bufferedImage3.getRGB(0, 0, bufferedImage3.getWidth(), bufferedImage3.getHeight(), arrn, 0, bufferedImage3.getWidth());
                        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(bufferedImage3.getWidth() * bufferedImage3.getHeight() * 4);
                        for (int i3 = 0; i3 < bufferedImage3.getHeight(); ++i3) {
                            for (int i4 = 0; i4 < bufferedImage3.getWidth(); ++i4) {
                                int n4 = arrn[i3 * bufferedImage3.getWidth() + i4];
                                byteBuffer.put((byte)(n4 >> 16 & 0xFF));
                                byteBuffer.put((byte)(n4 >> 8 & 0xFF));
                                byteBuffer.put((byte)(n4 & 0xFF));
                                byteBuffer.put((byte)(n4 >> 24 & 0xFF));
                            }
                        }
                        byteBuffer.flip();
                        lI10ilAnd.I1O1I1LaNd().I1O1I1LaNd(() -> {
                            try {
                                this.lil0liLand[n2] = lI1il011LAnD.I1O1I1LaNd(byteBuffer, this.IO11O0LANd, this.l0illAND);
                            }
                            catch (Exception exception) {
                                exception.printStackTrace();
                            }
                        });
                    }
                    lI10ilAnd.I1O1I1LaNd().I1O1I1LaNd(() -> {
                        this.lli011lLANd = true;
                    });
                    break;
                }
                default: {
                    this.lIOILand = 0;
                    this.lil0liLand = new int[1];
                    PNGDecoder pNGDecoder = new PNGDecoder(inputStream);
                    this.l0illAND = pNGDecoder.getHeight();
                    this.IO11O0LANd = pNGDecoder.getWidth();
                    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * this.IO11O0LANd * this.l0illAND);
                    pNGDecoder.decode(byteBuffer, this.IO11O0LANd * 4, PNGDecoder.Format.RGBA);
                    byteBuffer.flip();
                    lI10ilAnd.I1O1I1LaNd().I1O1I1LaNd(() -> {
                        this.lil0liLand[0] = lI1il011LAnD.I1O1I1LaNd(byteBuffer, pNGDecoder.getWidth(), pNGDecoder.getHeight());
                        this.lli011lLANd = true;
                        System.gc();
                    });
                    break;
                }
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void lli0OiIlAND() {
        Oill1LAnD = -1;
    }

    public int li0iOILAND() {
        return this.l0illAND;
    }

    public int O1il1llOLANd() {
        return this.IO11O0LANd;
    }
}

