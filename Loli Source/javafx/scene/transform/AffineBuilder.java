/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.transform;

import javafx.scene.transform.Affine;
import javafx.util.Builder;

@Deprecated
public class AffineBuilder<B extends AffineBuilder<B>>
implements Builder<Affine> {
    private int __set;
    private double mxx;
    private double mxy;
    private double mxz;
    private double myx;
    private double myy;
    private double myz;
    private double mzx;
    private double mzy;
    private double mzz;
    private double tx;
    private double ty;
    private double tz;

    protected AffineBuilder() {
    }

    public static AffineBuilder<?> create() {
        return new AffineBuilder();
    }

    private void __set(int n2) {
        this.__set |= 1 << n2;
    }

    public void applyTo(Affine affine) {
        int n2 = this.__set;
        while (n2 != 0) {
            int n3 = Integer.numberOfTrailingZeros(n2);
            n2 &= ~(1 << n3);
            switch (n3) {
                case 0: {
                    affine.setMxx(this.mxx);
                    break;
                }
                case 1: {
                    affine.setMxy(this.mxy);
                    break;
                }
                case 2: {
                    affine.setMxz(this.mxz);
                    break;
                }
                case 3: {
                    affine.setMyx(this.myx);
                    break;
                }
                case 4: {
                    affine.setMyy(this.myy);
                    break;
                }
                case 5: {
                    affine.setMyz(this.myz);
                    break;
                }
                case 6: {
                    affine.setMzx(this.mzx);
                    break;
                }
                case 7: {
                    affine.setMzy(this.mzy);
                    break;
                }
                case 8: {
                    affine.setMzz(this.mzz);
                    break;
                }
                case 9: {
                    affine.setTx(this.tx);
                    break;
                }
                case 10: {
                    affine.setTy(this.ty);
                    break;
                }
                case 11: {
                    affine.setTz(this.tz);
                }
            }
        }
    }

    public B mxx(double d2) {
        this.mxx = d2;
        this.__set(0);
        return (B)this;
    }

    public B mxy(double d2) {
        this.mxy = d2;
        this.__set(1);
        return (B)this;
    }

    public B mxz(double d2) {
        this.mxz = d2;
        this.__set(2);
        return (B)this;
    }

    public B myx(double d2) {
        this.myx = d2;
        this.__set(3);
        return (B)this;
    }

    public B myy(double d2) {
        this.myy = d2;
        this.__set(4);
        return (B)this;
    }

    public B myz(double d2) {
        this.myz = d2;
        this.__set(5);
        return (B)this;
    }

    public B mzx(double d2) {
        this.mzx = d2;
        this.__set(6);
        return (B)this;
    }

    public B mzy(double d2) {
        this.mzy = d2;
        this.__set(7);
        return (B)this;
    }

    public B mzz(double d2) {
        this.mzz = d2;
        this.__set(8);
        return (B)this;
    }

    public B tx(double d2) {
        this.tx = d2;
        this.__set(9);
        return (B)this;
    }

    public B ty(double d2) {
        this.ty = d2;
        this.__set(10);
        return (B)this;
    }

    public B tz(double d2) {
        this.tz = d2;
        this.__set(11);
        return (B)this;
    }

    @Override
    public Affine build() {
        Affine affine = new Affine();
        this.applyTo(affine);
        return affine;
    }
}

