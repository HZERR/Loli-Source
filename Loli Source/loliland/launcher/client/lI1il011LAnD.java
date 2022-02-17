/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class lI1il011LAnD {
    public static int I1O1I1LaNd() {
        return GL11.glGenTextures();
    }

    public static void I1O1I1LaNd(int n2, BufferedImage bufferedImage) {
        int[] arrn = new int[bufferedImage.getWidth() * bufferedImage.getHeight()];
        bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), arrn, 0, bufferedImage.getWidth());
        ByteBuffer byteBuffer = BufferUtils.createByteBuffer(bufferedImage.getWidth() * bufferedImage.getHeight() * 4);
        for (int i2 = 0; i2 < bufferedImage.getHeight(); ++i2) {
            for (int i3 = 0; i3 < bufferedImage.getWidth(); ++i3) {
                int n3 = arrn[i2 * bufferedImage.getWidth() + i3];
                byteBuffer.put((byte)(n3 >> 16 & 0xFF));
                byteBuffer.put((byte)(n3 >> 8 & 0xFF));
                byteBuffer.put((byte)(n3 & 0xFF));
                byteBuffer.put((byte)(n3 >> 24 & 0xFF));
            }
        }
        byteBuffer.flip();
        GL11.glBindTexture(3553, n2);
        GL11.glPixelStorei(3312, 0);
        GL11.glPixelStorei(3313, 0);
        GL11.glPixelStorei(3314, 0);
        GL11.glPixelStorei(3315, 0);
        GL11.glPixelStorei(3316, 0);
        GL11.glPixelStorei(3317, 4);
        GL11.glTexImage2D(3553, 0, 6408, bufferedImage.getWidth(), bufferedImage.getHeight(), 0, 6408, 5121, byteBuffer);
        GL11.glTexParameteri(3553, 10241, 9729);
        GL11.glTexParameteri(3553, 10240, 9729);
        GL11.glTexParameteri(3553, 10242, 10496);
        GL11.glTexParameteri(3553, 10243, 10496);
        GL11.glTexParameteri(3553, 10242, 33071);
        GL11.glTexParameteri(3553, 10243, 33071);
        GL11.glTexParameterf(3553, 34046, 1.0f);
        byteBuffer.clear();
    }

    public static int I1O1I1LaNd(ByteBuffer byteBuffer, int n2, int n3) {
        int n4 = lI1il011LAnD.I1O1I1LaNd();
        GL11.glBindTexture(3553, n4);
        GL11.glPixelStorei(3317, 1);
        GL11.glTexImage2D(3553, 0, 6408, n2, n3, 0, 6408, 5121, byteBuffer);
        GL11.glTexParameteri(3553, 10241, 9729);
        GL11.glTexParameteri(3553, 10240, 9729);
        GL11.glTexParameteri(3553, 10242, 10496);
        GL11.glTexParameteri(3553, 10243, 10496);
        GL11.glTexParameteri(3553, 10242, 33071);
        GL11.glTexParameteri(3553, 10243, 33071);
        GL11.glTexParameterf(3553, 34046, 1.0f);
        byteBuffer.clear();
        return n4;
    }
}

