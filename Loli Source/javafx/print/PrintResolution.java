/*
 * Decompiled with CFR 0.150.
 */
package javafx.print;

public final class PrintResolution {
    private int cfRes;
    private int fRes;

    PrintResolution(int n2, int n3) throws IllegalArgumentException {
        if (n2 <= 0 || n3 <= 0) {
            throw new IllegalArgumentException("Values must be positive");
        }
        this.cfRes = n2;
        this.fRes = n3;
    }

    public int getCrossFeedResolution() {
        return this.cfRes;
    }

    public int getFeedResolution() {
        return this.fRes;
    }

    public boolean equals(Object object) {
        try {
            PrintResolution printResolution = (PrintResolution)object;
            return this.cfRes == printResolution.cfRes && this.fRes == printResolution.fRes;
        }
        catch (Exception exception) {
            return false;
        }
    }

    public int hashCode() {
        return this.cfRes << 16 | this.fRes;
    }

    public String toString() {
        return "Feed res=" + this.fRes + "dpi. Cross Feed res=" + this.cfRes + "dpi.";
    }
}

