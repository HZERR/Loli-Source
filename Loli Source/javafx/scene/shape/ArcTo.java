/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.shape;

import com.sun.javafx.geom.Arc2D;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.sg.prism.NGPath;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.scene.shape.PathElement;

public class ArcTo
extends PathElement {
    private DoubleProperty radiusX = new DoublePropertyBase(){

        @Override
        public void invalidated() {
            ArcTo.this.u();
        }

        @Override
        public Object getBean() {
            return ArcTo.this;
        }

        @Override
        public String getName() {
            return "radiusX";
        }
    };
    private DoubleProperty radiusY = new DoublePropertyBase(){

        @Override
        public void invalidated() {
            ArcTo.this.u();
        }

        @Override
        public Object getBean() {
            return ArcTo.this;
        }

        @Override
        public String getName() {
            return "radiusY";
        }
    };
    private DoubleProperty xAxisRotation;
    private BooleanProperty largeArcFlag;
    private BooleanProperty sweepFlag;
    private DoubleProperty x;
    private DoubleProperty y;

    public ArcTo() {
    }

    public ArcTo(double d2, double d3, double d4, double d5, double d6, boolean bl, boolean bl2) {
        this.setRadiusX(d2);
        this.setRadiusY(d3);
        this.setXAxisRotation(d4);
        this.setX(d5);
        this.setY(d6);
        this.setLargeArcFlag(bl);
        this.setSweepFlag(bl2);
    }

    public final void setRadiusX(double d2) {
        this.radiusX.set(d2);
    }

    public final double getRadiusX() {
        return this.radiusX.get();
    }

    public final DoubleProperty radiusXProperty() {
        return this.radiusX;
    }

    public final void setRadiusY(double d2) {
        this.radiusY.set(d2);
    }

    public final double getRadiusY() {
        return this.radiusY.get();
    }

    public final DoubleProperty radiusYProperty() {
        return this.radiusY;
    }

    public final void setXAxisRotation(double d2) {
        if (this.xAxisRotation != null || d2 != 0.0) {
            this.XAxisRotationProperty().set(d2);
        }
    }

    public final double getXAxisRotation() {
        return this.xAxisRotation == null ? 0.0 : this.xAxisRotation.get();
    }

    public final DoubleProperty XAxisRotationProperty() {
        if (this.xAxisRotation == null) {
            this.xAxisRotation = new DoublePropertyBase(){

                @Override
                public void invalidated() {
                    ArcTo.this.u();
                }

                @Override
                public Object getBean() {
                    return ArcTo.this;
                }

                @Override
                public String getName() {
                    return "XAxisRotation";
                }
            };
        }
        return this.xAxisRotation;
    }

    public final void setLargeArcFlag(boolean bl) {
        if (this.largeArcFlag != null || bl) {
            this.largeArcFlagProperty().set(bl);
        }
    }

    public final boolean isLargeArcFlag() {
        return this.largeArcFlag == null ? false : this.largeArcFlag.get();
    }

    public final BooleanProperty largeArcFlagProperty() {
        if (this.largeArcFlag == null) {
            this.largeArcFlag = new BooleanPropertyBase(){

                @Override
                public void invalidated() {
                    ArcTo.this.u();
                }

                @Override
                public Object getBean() {
                    return ArcTo.this;
                }

                @Override
                public String getName() {
                    return "largeArcFlag";
                }
            };
        }
        return this.largeArcFlag;
    }

    public final void setSweepFlag(boolean bl) {
        if (this.sweepFlag != null || bl) {
            this.sweepFlagProperty().set(bl);
        }
    }

    public final boolean isSweepFlag() {
        return this.sweepFlag == null ? false : this.sweepFlag.get();
    }

    public final BooleanProperty sweepFlagProperty() {
        if (this.sweepFlag == null) {
            this.sweepFlag = new BooleanPropertyBase(){

                @Override
                public void invalidated() {
                    ArcTo.this.u();
                }

                @Override
                public Object getBean() {
                    return ArcTo.this;
                }

                @Override
                public String getName() {
                    return "sweepFlag";
                }
            };
        }
        return this.sweepFlag;
    }

    public final void setX(double d2) {
        if (this.x != null || d2 != 0.0) {
            this.xProperty().set(d2);
        }
    }

    public final double getX() {
        return this.x == null ? 0.0 : this.x.get();
    }

    public final DoubleProperty xProperty() {
        if (this.x == null) {
            this.x = new DoublePropertyBase(){

                @Override
                public void invalidated() {
                    ArcTo.this.u();
                }

                @Override
                public Object getBean() {
                    return ArcTo.this;
                }

                @Override
                public String getName() {
                    return "x";
                }
            };
        }
        return this.x;
    }

    public final void setY(double d2) {
        if (this.y != null || d2 != 0.0) {
            this.yProperty().set(d2);
        }
    }

    public final double getY() {
        return this.y == null ? 0.0 : this.y.get();
    }

    public final DoubleProperty yProperty() {
        if (this.y == null) {
            this.y = new DoublePropertyBase(){

                @Override
                public void invalidated() {
                    ArcTo.this.u();
                }

                @Override
                public Object getBean() {
                    return ArcTo.this;
                }

                @Override
                public String getName() {
                    return "y";
                }
            };
        }
        return this.y;
    }

    @Override
    void addTo(NGPath nGPath) {
        this.addArcTo(nGPath, null, nGPath.getCurrentX(), nGPath.getCurrentY());
    }

    @Override
    @Deprecated
    public void impl_addTo(Path2D path2D) {
        this.addArcTo(null, path2D, path2D.getCurrentX(), path2D.getCurrentY());
    }

    private void addArcTo(NGPath nGPath, Path2D path2D, double d2, double d3) {
        double d4;
        double d5;
        double d6;
        double d7 = this.getX();
        double d8 = this.getY();
        boolean bl = this.isSweepFlag();
        boolean bl2 = this.isLargeArcFlag();
        double d9 = this.isAbsolute() ? d7 : d7 + d2;
        double d10 = this.isAbsolute() ? d8 : d8 + d3;
        double d11 = (d2 - d9) / 2.0;
        double d12 = (d3 - d10) / 2.0;
        double d13 = Math.toRadians(this.getXAxisRotation());
        double d14 = Math.cos(d13);
        double d15 = Math.sin(d13);
        double d16 = d14 * d11 + d15 * d12;
        double d17 = -d15 * d11 + d14 * d12;
        double d18 = d16 * d16;
        double d19 = Math.abs(this.getRadiusX());
        double d20 = d19 * d19;
        double d21 = d18 / d20 + (d6 = d17 * d17) / (d5 = (d4 = Math.abs(this.getRadiusY())) * d4);
        if (d21 > 1.0) {
            d19 = Math.sqrt(d21) * d19;
            d4 = Math.sqrt(d21) * d4;
            if (d19 != d19 || d4 != d4) {
                if (nGPath == null) {
                    path2D.lineTo((float)d9, (float)d10);
                } else {
                    nGPath.addLineTo((float)d9, (float)d10);
                }
                return;
            }
            d20 = d19 * d19;
            d5 = d4 * d4;
        }
        double d22 = bl2 == bl ? -1.0 : 1.0;
        double d23 = (d20 * d5 - d20 * d6 - d5 * d18) / (d20 * d6 + d5 * d18);
        d23 = d23 < 0.0 ? 0.0 : d23;
        double d24 = d22 * Math.sqrt(d23);
        double d25 = d24 * (d19 * d17 / d4);
        double d26 = d24 * -(d4 * d16 / d19);
        double d27 = (d2 + d9) / 2.0;
        double d28 = (d3 + d10) / 2.0;
        double d29 = d27 + (d14 * d25 - d15 * d26);
        double d30 = d28 + (d15 * d25 + d14 * d26);
        double d31 = (d16 - d25) / d19;
        double d32 = (d17 - d26) / d4;
        double d33 = (-d16 - d25) / d19;
        double d34 = (-d17 - d26) / d4;
        double d35 = Math.sqrt(d31 * d31 + d32 * d32);
        double d36 = d31;
        d22 = d32 < 0.0 ? -1.0 : 1.0;
        double d37 = Math.toDegrees(d22 * Math.acos(d36 / d35));
        d35 = Math.sqrt((d31 * d31 + d32 * d32) * (d33 * d33 + d34 * d34));
        d36 = d31 * d33 + d32 * d34;
        d22 = d31 * d34 - d32 * d33 < 0.0 ? -1.0 : 1.0;
        double d38 = Math.toDegrees(d22 * Math.acos(d36 / d35));
        if (!bl && d38 > 0.0) {
            d38 -= 360.0;
        } else if (bl && d38 < 0.0) {
            d38 += 360.0;
        }
        float f2 = (float)(d29 - d19);
        float f3 = (float)(d30 - d4);
        float f4 = (float)(d19 * 2.0);
        float f5 = (float)(d4 * 2.0);
        float f6 = (float)(-(d37 %= 360.0));
        float f7 = (float)(-(d38 %= 360.0));
        if (nGPath == null) {
            Arc2D arc2D = new Arc2D(f2, f3, f4, f5, f6, f7, 0);
            BaseTransform baseTransform = d13 == 0.0 ? null : BaseTransform.getRotateInstance(d13, d29, d30);
            PathIterator pathIterator = arc2D.getPathIterator(baseTransform);
            pathIterator.next();
            path2D.append(pathIterator, true);
        } else {
            nGPath.addArcTo(f2, f3, f4, f5, f6, f7, (float)d13);
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("ArcTo[");
        stringBuilder.append("x=").append(this.getX());
        stringBuilder.append(", y=").append(this.getY());
        stringBuilder.append(", radiusX=").append(this.getRadiusX());
        stringBuilder.append(", radiusY=").append(this.getRadiusY());
        stringBuilder.append(", xAxisRotation=").append(this.getXAxisRotation());
        if (this.isLargeArcFlag()) {
            stringBuilder.append(", lartArcFlag");
        }
        if (this.isSweepFlag()) {
            stringBuilder.append(", sweepFlag");
        }
        return stringBuilder.append("]").toString();
    }
}

