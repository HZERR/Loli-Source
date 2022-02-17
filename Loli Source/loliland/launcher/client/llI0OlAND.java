/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;
import loliland.launcher.client.lI0il11LaND;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class llI0OlAND {
    public static final int I1O1I1LaNd = 1027;
    public static final int OOOIilanD = 659;
    private static final int lI00OlAND = 40;
    private static ByteBuffer[] lli0OiIlAND;

    public static void I1O1I1LaNd() {
        try {
            System.out.println(llI0OlAND.class.getSimpleName() + ": CREATE DISPLAY!!!");
            Display.setDisplayMode(new DisplayMode(1027, 659));
            if (lli0OiIlAND == null) {
                lli0OiIlAND = new ByteBuffer[2];
                try {
                    llI0OlAND.lli0OiIlAND[0] = llI0OlAND.I1O1I1LaNd(llI0OlAND.class.getResourceAsStream("/assets/loliland/launcher/icon16.png"));
                    llI0OlAND.lli0OiIlAND[1] = llI0OlAND.I1O1I1LaNd(llI0OlAND.class.getResourceAsStream("/assets/loliland/launcher/icon32.png"));
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
            Display.setIcon(lli0OiIlAND);
            Display.setTitle("LoliLand: Launcher");
            Display.create(new PixelFormat());
            GL11.glViewport(0, 0, 1027, 659);
            GL11.glMatrixMode(5889);
            GL11.glOrtho(0.0, 1027.0, 659.0, 0.0, 1.0, -1.0);
            GL11.glMatrixMode(5888);
        }
        catch (LWJGLException lWJGLException) {
            lWJGLException.printStackTrace();
        }
    }

    public static void OOOIilanD() {
        Display.sync(40);
        Display.update();
    }

    public static void lI00OlAND() {
        lI0il11LaND.I1O1I1LaNd();
        Display.destroy();
    }

    private static ByteBuffer I1O1I1LaNd(InputStream inputStream) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        int[] arrn = bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null, 0, bufferedImage.getWidth());
        ByteBuffer byteBuffer = ByteBuffer.allocate(4 * arrn.length);
        for (int n2 : arrn) {
            byteBuffer.putInt(n2 << 8 | n2 >> 24 & 0xFF);
        }
        byteBuffer.flip();
        return byteBuffer;
    }

    public static boolean lli0OiIlAND() {
        return Display.isCreated();
    }
}

