/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom.transform;

import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.AffineBase;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;

public class Affine2D
extends AffineBase {
    private static final long BASE_HASH;

    private Affine2D(double d2, double d3, double d4, double d5, double d6, double d7, int n2) {
        this.mxx = d2;
        this.myx = d3;
        this.mxy = d4;
        this.myy = d5;
        this.mxt = d6;
        this.myt = d7;
        this.state = n2;
        this.type = -1;
    }

    public Affine2D() {
        this.myy = 1.0;
        this.mxx = 1.0;
    }

    public Affine2D(BaseTransform baseTransform) {
        this.setTransform(baseTransform);
    }

    public Affine2D(float f2, float f3, float f4, float f5, float f6, float f7) {
        this.mxx = f2;
        this.myx = f3;
        this.mxy = f4;
        this.myy = f5;
        this.mxt = f6;
        this.myt = f7;
        this.updateState2D();
    }

    public Affine2D(double d2, double d3, double d4, double d5, double d6, double d7) {
        this.mxx = d2;
        this.myx = d3;
        this.mxy = d4;
        this.myy = d5;
        this.mxt = d6;
        this.myt = d7;
        this.updateState2D();
    }

    @Override
    public BaseTransform.Degree getDegree() {
        return BaseTransform.Degree.AFFINE_2D;
    }

    @Override
    protected void reset3Delements() {
    }

    public void rotate(double d2, double d3, double d4) {
        this.translate(d3, d4);
        this.rotate(d2);
        this.translate(-d3, -d4);
    }

    public void rotate(double d2, double d3) {
        if (d3 == 0.0) {
            if (d2 < 0.0) {
                this.rotate180();
            }
        } else if (d2 == 0.0) {
            if (d3 > 0.0) {
                this.rotate90();
            } else {
                this.rotate270();
            }
        } else {
            double d4 = Math.sqrt(d2 * d2 + d3 * d3);
            double d5 = d3 / d4;
            double d6 = d2 / d4;
            double d7 = this.mxx;
            double d8 = this.mxy;
            this.mxx = d6 * d7 + d5 * d8;
            this.mxy = -d5 * d7 + d6 * d8;
            d7 = this.myx;
            d8 = this.myy;
            this.myx = d6 * d7 + d5 * d8;
            this.myy = -d5 * d7 + d6 * d8;
            this.updateState2D();
        }
    }

    public void rotate(double d2, double d3, double d4, double d5) {
        this.translate(d4, d5);
        this.rotate(d2, d3);
        this.translate(-d4, -d5);
    }

    public void quadrantRotate(int n2) {
        switch (n2 & 3) {
            case 0: {
                break;
            }
            case 1: {
                this.rotate90();
                break;
            }
            case 2: {
                this.rotate180();
                break;
            }
            case 3: {
                this.rotate270();
            }
        }
    }

    public void quadrantRotate(int n2, double d2, double d3) {
        switch (n2 & 3) {
            case 0: {
                return;
            }
            case 1: {
                this.mxt += d2 * (this.mxx - this.mxy) + d3 * (this.mxy + this.mxx);
                this.myt += d2 * (this.myx - this.myy) + d3 * (this.myy + this.myx);
                this.rotate90();
                break;
            }
            case 2: {
                this.mxt += d2 * (this.mxx + this.mxx) + d3 * (this.mxy + this.mxy);
                this.myt += d2 * (this.myx + this.myx) + d3 * (this.myy + this.myy);
                this.rotate180();
                break;
            }
            case 3: {
                this.mxt += d2 * (this.mxx + this.mxy) + d3 * (this.mxy - this.mxx);
                this.myt += d2 * (this.myx + this.myy) + d3 * (this.myy - this.myx);
                this.rotate270();
            }
        }
        if (this.mxt == 0.0 && this.myt == 0.0) {
            this.state &= 0xFFFFFFFE;
            if (this.type != -1) {
                this.type &= 0xFFFFFFFE;
            }
        } else {
            this.state |= 1;
            this.type |= 1;
        }
    }

    public void setToTranslation(double d2, double d3) {
        this.mxx = 1.0;
        this.myx = 0.0;
        this.mxy = 0.0;
        this.myy = 1.0;
        this.mxt = d2;
        this.myt = d3;
        if (d2 != 0.0 || d3 != 0.0) {
            this.state = 1;
            this.type = 1;
        } else {
            this.state = 0;
            this.type = 0;
        }
    }

    public void setToRotation(double d2) {
        double d3;
        double d4 = Math.sin(d2);
        if (d4 == 1.0 || d4 == -1.0) {
            d3 = 0.0;
            this.state = 4;
            this.type = 8;
        } else {
            d3 = Math.cos(d2);
            if (d3 == -1.0) {
                d4 = 0.0;
                this.state = 2;
                this.type = 8;
            } else if (d3 == 1.0) {
                d4 = 0.0;
                this.state = 0;
                this.type = 0;
            } else {
                this.state = 6;
                this.type = 16;
            }
        }
        this.mxx = d3;
        this.myx = d4;
        this.mxy = -d4;
        this.myy = d3;
        this.mxt = 0.0;
        this.myt = 0.0;
    }

    public void setToRotation(double d2, double d3, double d4) {
        this.setToRotation(d2);
        double d5 = this.myx;
        double d6 = 1.0 - this.mxx;
        this.mxt = d3 * d6 + d4 * d5;
        this.myt = d4 * d6 - d3 * d5;
        if (this.mxt != 0.0 || this.myt != 0.0) {
            this.state |= 1;
            this.type |= 1;
        }
    }

    public void setToRotation(double d2, double d3) {
        double d4;
        double d5;
        if (d3 == 0.0) {
            d5 = 0.0;
            if (d2 < 0.0) {
                d4 = -1.0;
                this.state = 2;
                this.type = 8;
            } else {
                d4 = 1.0;
                this.state = 0;
                this.type = 0;
            }
        } else if (d2 == 0.0) {
            d4 = 0.0;
            d5 = d3 > 0.0 ? 1.0 : -1.0;
            this.state = 4;
            this.type = 8;
        } else {
            double d6 = Math.sqrt(d2 * d2 + d3 * d3);
            d4 = d2 / d6;
            d5 = d3 / d6;
            this.state = 6;
            this.type = 16;
        }
        this.mxx = d4;
        this.myx = d5;
        this.mxy = -d5;
        this.myy = d4;
        this.mxt = 0.0;
        this.myt = 0.0;
    }

    public void setToRotation(double d2, double d3, double d4, double d5) {
        this.setToRotation(d2, d3);
        double d6 = this.myx;
        double d7 = 1.0 - this.mxx;
        this.mxt = d4 * d7 + d5 * d6;
        this.myt = d5 * d7 - d4 * d6;
        if (this.mxt != 0.0 || this.myt != 0.0) {
            this.state |= 1;
            this.type |= 1;
        }
    }

    public void setToQuadrantRotation(int n2) {
        switch (n2 & 3) {
            case 0: {
                this.mxx = 1.0;
                this.myx = 0.0;
                this.mxy = 0.0;
                this.myy = 1.0;
                this.mxt = 0.0;
                this.myt = 0.0;
                this.state = 0;
                this.type = 0;
                break;
            }
            case 1: {
                this.mxx = 0.0;
                this.myx = 1.0;
                this.mxy = -1.0;
                this.myy = 0.0;
                this.mxt = 0.0;
                this.myt = 0.0;
                this.state = 4;
                this.type = 8;
                break;
            }
            case 2: {
                this.mxx = -1.0;
                this.myx = 0.0;
                this.mxy = 0.0;
                this.myy = -1.0;
                this.mxt = 0.0;
                this.myt = 0.0;
                this.state = 2;
                this.type = 8;
                break;
            }
            case 3: {
                this.mxx = 0.0;
                this.myx = -1.0;
                this.mxy = 1.0;
                this.myy = 0.0;
                this.mxt = 0.0;
                this.myt = 0.0;
                this.state = 4;
                this.type = 8;
            }
        }
    }

    public void setToQuadrantRotation(int n2, double d2, double d3) {
        switch (n2 & 3) {
            case 0: {
                this.mxx = 1.0;
                this.myx = 0.0;
                this.mxy = 0.0;
                this.myy = 1.0;
                this.mxt = 0.0;
                this.myt = 0.0;
                this.state = 0;
                this.type = 0;
                break;
            }
            case 1: {
                this.mxx = 0.0;
                this.myx = 1.0;
                this.mxy = -1.0;
                this.myy = 0.0;
                this.mxt = d2 + d3;
                this.myt = d3 - d2;
                if (this.mxt == 0.0 && this.myt == 0.0) {
                    this.state = 4;
                    this.type = 8;
                    break;
                }
                this.state = 5;
                this.type = 9;
                break;
            }
            case 2: {
                this.mxx = -1.0;
                this.myx = 0.0;
                this.mxy = 0.0;
                this.myy = -1.0;
                this.mxt = d2 + d2;
                this.myt = d3 + d3;
                if (this.mxt == 0.0 && this.myt == 0.0) {
                    this.state = 2;
                    this.type = 8;
                    break;
                }
                this.state = 3;
                this.type = 9;
                break;
            }
            case 3: {
                this.mxx = 0.0;
                this.myx = -1.0;
                this.mxy = 1.0;
                this.myy = 0.0;
                this.mxt = d2 - d3;
                this.myt = d3 + d2;
                if (this.mxt == 0.0 && this.myt == 0.0) {
                    this.state = 4;
                    this.type = 8;
                    break;
                }
                this.state = 5;
                this.type = 9;
            }
        }
    }

    public void setToScale(double d2, double d3) {
        this.mxx = d2;
        this.myx = 0.0;
        this.mxy = 0.0;
        this.myy = d3;
        this.mxt = 0.0;
        this.myt = 0.0;
        if (d2 != 1.0 || d3 != 1.0) {
            this.state = 2;
            this.type = -1;
        } else {
            this.state = 0;
            this.type = 0;
        }
    }

    @Override
    public void setTransform(BaseTransform baseTransform) {
        switch (baseTransform.getDegree()) {
            case IDENTITY: {
                this.setToIdentity();
                break;
            }
            case TRANSLATE_2D: {
                this.setToTranslation(baseTransform.getMxt(), baseTransform.getMyt());
                break;
            }
            default: {
                if (baseTransform.getType() > 127) {
                    System.out.println(baseTransform + " is " + baseTransform.getType());
                    System.out.print("  " + baseTransform.getMxx());
                    System.out.print(", " + baseTransform.getMxy());
                    System.out.print(", " + baseTransform.getMxz());
                    System.out.print(", " + baseTransform.getMxt());
                    System.out.println();
                    System.out.print("  " + baseTransform.getMyx());
                    System.out.print(", " + baseTransform.getMyy());
                    System.out.print(", " + baseTransform.getMyz());
                    System.out.print(", " + baseTransform.getMyt());
                    System.out.println();
                    System.out.print("  " + baseTransform.getMzx());
                    System.out.print(", " + baseTransform.getMzy());
                    System.out.print(", " + baseTransform.getMzz());
                    System.out.print(", " + baseTransform.getMzt());
                    System.out.println();
                    Affine2D.degreeError(BaseTransform.Degree.AFFINE_2D);
                }
            }
            case AFFINE_2D: {
                this.mxx = baseTransform.getMxx();
                this.myx = baseTransform.getMyx();
                this.mxy = baseTransform.getMxy();
                this.myy = baseTransform.getMyy();
                this.mxt = baseTransform.getMxt();
                this.myt = baseTransform.getMyt();
                if (baseTransform instanceof AffineBase) {
                    this.state = ((AffineBase)baseTransform).state;
                    this.type = ((AffineBase)baseTransform).type;
                    break;
                }
                this.updateState2D();
            }
        }
    }

    public void preConcatenate(BaseTransform baseTransform) {
        switch (baseTransform.getDegree()) {
            case IDENTITY: {
                return;
            }
            case TRANSLATE_2D: {
                this.translate(baseTransform.getMxt(), baseTransform.getMyt());
                return;
            }
            case AFFINE_2D: {
                break;
            }
            default: {
                Affine2D.degreeError(BaseTransform.Degree.AFFINE_2D);
            }
        }
        int n2 = this.state;
        Affine2D affine2D = (Affine2D)baseTransform;
        int n3 = affine2D.state;
        switch (n3 << 4 | n2) {
            case 0: 
            case 1: 
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: 
            case 7: {
                return;
            }
            case 16: 
            case 18: 
            case 20: 
            case 22: {
                this.mxt = affine2D.mxt;
                this.myt = affine2D.myt;
                this.state = n2 | 1;
                this.type |= 1;
                return;
            }
            case 17: 
            case 19: 
            case 21: 
            case 23: {
                this.mxt += affine2D.mxt;
                this.myt += affine2D.myt;
                return;
            }
            case 32: 
            case 33: {
                this.state = n2 | 2;
            }
            case 34: 
            case 35: 
            case 36: 
            case 37: 
            case 38: 
            case 39: {
                double d2 = affine2D.mxx;
                double d3 = affine2D.myy;
                if ((n2 & 4) != 0) {
                    this.mxy *= d2;
                    this.myx *= d3;
                    if ((n2 & 2) != 0) {
                        this.mxx *= d2;
                        this.myy *= d3;
                    }
                } else {
                    this.mxx *= d2;
                    this.myy *= d3;
                }
                if ((n2 & 1) != 0) {
                    this.mxt *= d2;
                    this.myt *= d3;
                }
                this.type = -1;
                return;
            }
            case 68: 
            case 69: {
                n2 |= 2;
            }
            case 64: 
            case 65: 
            case 66: 
            case 67: {
                this.state = n2 ^ 4;
            }
            case 70: 
            case 71: {
                double d4 = affine2D.mxy;
                double d5 = affine2D.myx;
                double d6 = this.mxx;
                this.mxx = this.myx * d4;
                this.myx = d6 * d5;
                d6 = this.mxy;
                this.mxy = this.myy * d4;
                this.myy = d6 * d5;
                d6 = this.mxt;
                this.mxt = this.myt * d4;
                this.myt = d6 * d5;
                this.type = -1;
                return;
            }
        }
        double d7 = affine2D.mxx;
        double d8 = affine2D.mxy;
        double d9 = affine2D.mxt;
        double d10 = affine2D.myx;
        double d11 = affine2D.myy;
        double d12 = affine2D.myt;
        switch (n2) {
            default: {
                Affine2D.stateError();
            }
            case 7: {
                double d13 = this.mxt;
                double d14 = this.myt;
                d9 += d13 * d7 + d14 * d8;
                d12 += d13 * d10 + d14 * d11;
            }
            case 6: {
                this.mxt = d9;
                this.myt = d12;
                double d13 = this.mxx;
                double d14 = this.myx;
                this.mxx = d13 * d7 + d14 * d8;
                this.myx = d13 * d10 + d14 * d11;
                d13 = this.mxy;
                d14 = this.myy;
                this.mxy = d13 * d7 + d14 * d8;
                this.myy = d13 * d10 + d14 * d11;
                break;
            }
            case 5: {
                double d15 = this.mxt;
                double d16 = this.myt;
                d9 += d15 * d7 + d16 * d8;
                d12 += d15 * d10 + d16 * d11;
            }
            case 4: {
                this.mxt = d9;
                this.myt = d12;
                double d15 = this.myx;
                this.mxx = d15 * d8;
                this.myx = d15 * d11;
                d15 = this.mxy;
                this.mxy = d15 * d7;
                this.myy = d15 * d10;
                break;
            }
            case 3: {
                double d17 = this.mxt;
                double d18 = this.myt;
                d9 += d17 * d7 + d18 * d8;
                d12 += d17 * d10 + d18 * d11;
            }
            case 2: {
                this.mxt = d9;
                this.myt = d12;
                double d17 = this.mxx;
                this.mxx = d17 * d7;
                this.myx = d17 * d10;
                d17 = this.myy;
                this.mxy = d17 * d8;
                this.myy = d17 * d11;
                break;
            }
            case 1: {
                double d19 = this.mxt;
                double d20 = this.myt;
                d9 += d19 * d7 + d20 * d8;
                d12 += d19 * d10 + d20 * d11;
            }
            case 0: {
                this.mxt = d9;
                this.myt = d12;
                this.mxx = d7;
                this.myx = d10;
                this.mxy = d8;
                this.myy = d11;
                this.state = n2 | n3;
                this.type = -1;
                return;
            }
        }
        this.updateState2D();
    }

    @Override
    public Affine2D createInverse() throws NoninvertibleTransformException {
        switch (this.state) {
            default: {
                Affine2D.stateError();
            }
            case 7: {
                double d2 = this.mxx * this.myy - this.mxy * this.myx;
                if (d2 == 0.0 || Math.abs(d2) <= Double.MIN_VALUE) {
                    throw new NoninvertibleTransformException("Determinant is " + d2);
                }
                return new Affine2D(this.myy / d2, -this.myx / d2, -this.mxy / d2, this.mxx / d2, (this.mxy * this.myt - this.myy * this.mxt) / d2, (this.myx * this.mxt - this.mxx * this.myt) / d2, 7);
            }
            case 6: {
                double d3 = this.mxx * this.myy - this.mxy * this.myx;
                if (d3 == 0.0 || Math.abs(d3) <= Double.MIN_VALUE) {
                    throw new NoninvertibleTransformException("Determinant is " + d3);
                }
                return new Affine2D(this.myy / d3, -this.myx / d3, -this.mxy / d3, this.mxx / d3, 0.0, 0.0, 6);
            }
            case 5: {
                if (this.mxy == 0.0 || this.myx == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                return new Affine2D(0.0, 1.0 / this.mxy, 1.0 / this.myx, 0.0, -this.myt / this.myx, -this.mxt / this.mxy, 5);
            }
            case 4: {
                if (this.mxy == 0.0 || this.myx == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                return new Affine2D(0.0, 1.0 / this.mxy, 1.0 / this.myx, 0.0, 0.0, 0.0, 4);
            }
            case 3: {
                if (this.mxx == 0.0 || this.myy == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                return new Affine2D(1.0 / this.mxx, 0.0, 0.0, 1.0 / this.myy, -this.mxt / this.mxx, -this.myt / this.myy, 3);
            }
            case 2: {
                if (this.mxx == 0.0 || this.myy == 0.0) {
                    throw new NoninvertibleTransformException("Determinant is 0");
                }
                return new Affine2D(1.0 / this.mxx, 0.0, 0.0, 1.0 / this.myy, 0.0, 0.0, 2);
            }
            case 1: {
                return new Affine2D(1.0, 0.0, 0.0, 1.0, -this.mxt, -this.myt, 1);
            }
            case 0: 
        }
        return new Affine2D();
    }

    public void transform(Point2D[] arrpoint2D, int n2, Point2D[] arrpoint2D2, int n3, int n4) {
        int n5 = this.state;
        block10: while (--n4 >= 0) {
            Point2D point2D;
            Point2D point2D2 = arrpoint2D[n2++];
            double d2 = point2D2.x;
            double d3 = point2D2.y;
            if ((point2D = arrpoint2D2[n3++]) == null) {
                arrpoint2D2[n3 - 1] = point2D = new Point2D();
            }
            switch (n5) {
                default: {
                    Affine2D.stateError();
                }
                case 7: {
                    point2D.setLocation((float)(d2 * this.mxx + d3 * this.mxy + this.mxt), (float)(d2 * this.myx + d3 * this.myy + this.myt));
                    continue block10;
                }
                case 6: {
                    point2D.setLocation((float)(d2 * this.mxx + d3 * this.mxy), (float)(d2 * this.myx + d3 * this.myy));
                    continue block10;
                }
                case 5: {
                    point2D.setLocation((float)(d3 * this.mxy + this.mxt), (float)(d2 * this.myx + this.myt));
                    continue block10;
                }
                case 4: {
                    point2D.setLocation((float)(d3 * this.mxy), (float)(d2 * this.myx));
                    continue block10;
                }
                case 3: {
                    point2D.setLocation((float)(d2 * this.mxx + this.mxt), (float)(d3 * this.myy + this.myt));
                    continue block10;
                }
                case 2: {
                    point2D.setLocation((float)(d2 * this.mxx), (float)(d3 * this.myy));
                    continue block10;
                }
                case 1: {
                    point2D.setLocation((float)(d2 + this.mxt), (float)(d3 + this.myt));
                    continue block10;
                }
                case 0: 
            }
            point2D.setLocation((float)d2, (float)d3);
        }
    }

    public Point2D deltaTransform(Point2D point2D, Point2D point2D2) {
        if (point2D2 == null) {
            point2D2 = new Point2D();
        }
        double d2 = point2D.x;
        double d3 = point2D.y;
        switch (this.state) {
            default: {
                Affine2D.stateError();
            }
            case 6: 
            case 7: {
                point2D2.setLocation((float)(d2 * this.mxx + d3 * this.mxy), (float)(d2 * this.myx + d3 * this.myy));
                return point2D2;
            }
            case 4: 
            case 5: {
                point2D2.setLocation((float)(d3 * this.mxy), (float)(d2 * this.myx));
                return point2D2;
            }
            case 2: 
            case 3: {
                point2D2.setLocation((float)(d2 * this.mxx), (float)(d3 * this.myy));
                return point2D2;
            }
            case 0: 
            case 1: 
        }
        point2D2.setLocation((float)d2, (float)d3);
        return point2D2;
    }

    private static double _matround(double d2) {
        return Math.rint(d2 * 1.0E15) / 1.0E15;
    }

    @Override
    public String toString() {
        return "Affine2D[[" + Affine2D._matround(this.mxx) + ", " + Affine2D._matround(this.mxy) + ", " + Affine2D._matround(this.mxt) + "], [" + Affine2D._matround(this.myx) + ", " + Affine2D._matround(this.myy) + ", " + Affine2D._matround(this.myt) + "]]";
    }

    @Override
    public boolean is2D() {
        return true;
    }

    @Override
    public void restoreTransform(double d2, double d3, double d4, double d5, double d6, double d7) {
        this.setTransform(d2, d3, d4, d5, d6, d7);
    }

    @Override
    public void restoreTransform(double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12, double d13) {
        if (d4 != 0.0 || d8 != 0.0 || d10 != 0.0 || d11 != 0.0 || d12 != 1.0 || d13 != 0.0) {
            Affine2D.degreeError(BaseTransform.Degree.AFFINE_2D);
        }
        this.setTransform(d2, d6, d3, d7, d5, d9);
    }

    @Override
    public BaseTransform deriveWithTranslation(double d2, double d3) {
        this.translate(d2, d3);
        return this;
    }

    @Override
    public BaseTransform deriveWithTranslation(double d2, double d3, double d4) {
        if (d4 == 0.0) {
            this.translate(d2, d3);
            return this;
        }
        Affine3D affine3D = new Affine3D(this);
        affine3D.translate(d2, d3, d4);
        return affine3D;
    }

    @Override
    public BaseTransform deriveWithScale(double d2, double d3, double d4) {
        if (d4 == 1.0) {
            this.scale(d2, d3);
            return this;
        }
        Affine3D affine3D = new Affine3D(this);
        affine3D.scale(d2, d3, d4);
        return affine3D;
    }

    @Override
    public BaseTransform deriveWithRotation(double d2, double d3, double d4, double d5) {
        if (d2 == 0.0) {
            return this;
        }
        if (Affine2D.almostZero(d3) && Affine2D.almostZero(d4)) {
            if (d5 > 0.0) {
                this.rotate(d2);
            } else if (d5 < 0.0) {
                this.rotate(-d2);
            }
            return this;
        }
        Affine3D affine3D = new Affine3D(this);
        affine3D.rotate(d2, d3, d4, d5);
        return affine3D;
    }

    @Override
    public BaseTransform deriveWithPreTranslation(double d2, double d3) {
        this.mxt += d2;
        this.myt += d3;
        if (this.mxt != 0.0 || this.myt != 0.0) {
            this.state |= 1;
            this.type |= 1;
        } else {
            this.state &= 0xFFFFFFFE;
            if (this.type != -1) {
                this.type &= 0xFFFFFFFE;
            }
        }
        return this;
    }

    @Override
    public BaseTransform deriveWithConcatenation(double d2, double d3, double d4, double d5, double d6, double d7) {
        BaseTransform baseTransform = Affine2D.getInstance(d2, d3, d4, d5, d6, d7);
        this.concatenate(baseTransform);
        return this;
    }

    @Override
    public BaseTransform deriveWithConcatenation(double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12, double d13) {
        if (d4 == 0.0 && d8 == 0.0 && d10 == 0.0 && d11 == 0.0 && d12 == 1.0 && d13 == 0.0) {
            this.concatenate(d2, d3, d5, d6, d7, d9);
            return this;
        }
        Affine3D affine3D = new Affine3D(this);
        affine3D.concatenate(d2, d3, d4, d5, d6, d7, d8, d9, d10, d11, d12, d13);
        return affine3D;
    }

    @Override
    public BaseTransform deriveWithConcatenation(BaseTransform baseTransform) {
        if (baseTransform.is2D()) {
            this.concatenate(baseTransform);
            return this;
        }
        Affine3D affine3D = new Affine3D(this);
        affine3D.concatenate(baseTransform);
        return affine3D;
    }

    @Override
    public BaseTransform deriveWithPreConcatenation(BaseTransform baseTransform) {
        if (baseTransform.is2D()) {
            this.preConcatenate(baseTransform);
            return this;
        }
        Affine3D affine3D = new Affine3D(this);
        affine3D.preConcatenate(baseTransform);
        return affine3D;
    }

    @Override
    public BaseTransform deriveWithNewTransform(BaseTransform baseTransform) {
        if (baseTransform.is2D()) {
            this.setTransform(baseTransform);
            return this;
        }
        return Affine2D.getInstance(baseTransform);
    }

    @Override
    public BaseTransform copy() {
        return new Affine2D(this);
    }

    @Override
    public int hashCode() {
        if (this.isIdentity()) {
            return 0;
        }
        long l2 = BASE_HASH;
        l2 = l2 * 31L + Double.doubleToLongBits(this.getMyy());
        l2 = l2 * 31L + Double.doubleToLongBits(this.getMyx());
        l2 = l2 * 31L + Double.doubleToLongBits(this.getMxy());
        l2 = l2 * 31L + Double.doubleToLongBits(this.getMxx());
        l2 = l2 * 31L + Double.doubleToLongBits(0.0);
        l2 = l2 * 31L + Double.doubleToLongBits(this.getMyt());
        l2 = l2 * 31L + Double.doubleToLongBits(this.getMxt());
        return (int)l2 ^ (int)(l2 >> 32);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof BaseTransform) {
            BaseTransform baseTransform = (BaseTransform)object;
            return baseTransform.getType() <= 127 && baseTransform.getMxx() == this.mxx && baseTransform.getMxy() == this.mxy && baseTransform.getMxt() == this.mxt && baseTransform.getMyx() == this.myx && baseTransform.getMyy() == this.myy && baseTransform.getMyt() == this.myt;
        }
        return false;
    }

    static {
        long l2 = 0L;
        l2 = l2 * 31L + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMzz());
        l2 = l2 * 31L + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMzy());
        l2 = l2 * 31L + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMzx());
        l2 = l2 * 31L + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMyz());
        BASE_HASH = l2 = l2 * 31L + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMxz());
    }
}

