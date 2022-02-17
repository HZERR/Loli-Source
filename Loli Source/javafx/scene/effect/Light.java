/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.effect;

import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.util.Utils;
import com.sun.scenario.effect.light.DistantLight;
import com.sun.scenario.effect.light.PointLight;
import com.sun.scenario.effect.light.SpotLight;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.paint.Color;

public abstract class Light {
    private com.sun.scenario.effect.light.Light peer;
    private ObjectProperty<Color> color;
    private BooleanProperty effectDirty;

    protected Light() {
        this.impl_markDirty();
    }

    abstract com.sun.scenario.effect.light.Light impl_createImpl();

    com.sun.scenario.effect.light.Light impl_getImpl() {
        if (this.peer == null) {
            this.peer = this.impl_createImpl();
        }
        return this.peer;
    }

    public final void setColor(Color color) {
        this.colorProperty().set(color);
    }

    public final Color getColor() {
        return this.color == null ? Color.WHITE : (Color)this.color.get();
    }

    public final ObjectProperty<Color> colorProperty() {
        if (this.color == null) {
            this.color = new ObjectPropertyBase<Color>(Color.WHITE){

                @Override
                public void invalidated() {
                    Light.this.impl_markDirty();
                }

                @Override
                public Object getBean() {
                    return Light.this;
                }

                @Override
                public String getName() {
                    return "color";
                }
            };
        }
        return this.color;
    }

    void impl_sync() {
        if (this.impl_isEffectDirty()) {
            this.impl_update();
            this.impl_clearDirty();
        }
    }

    private Color getColorInternal() {
        Color color = this.getColor();
        return color == null ? Color.WHITE : color;
    }

    void impl_update() {
        this.impl_getImpl().setColor(Toolkit.getToolkit().toColor4f(this.getColorInternal()));
    }

    private void setEffectDirty(boolean bl) {
        this.effectDirtyProperty().set(bl);
    }

    final BooleanProperty effectDirtyProperty() {
        if (this.effectDirty == null) {
            this.effectDirty = new SimpleBooleanProperty(this, "effectDirty");
        }
        return this.effectDirty;
    }

    boolean impl_isEffectDirty() {
        return this.effectDirty == null ? false : this.effectDirty.get();
    }

    final void impl_markDirty() {
        this.setEffectDirty(true);
    }

    final void impl_clearDirty() {
        this.setEffectDirty(false);
    }

    public static class Spot
    extends Point {
        private DoubleProperty pointsAtX;
        private DoubleProperty pointsAtY;
        private DoubleProperty pointsAtZ;
        private DoubleProperty specularExponent;

        public Spot() {
        }

        public Spot(double d2, double d3, double d4, double d5, Color color) {
            this.setX(d2);
            this.setY(d3);
            this.setZ(d4);
            this.setSpecularExponent(d5);
            this.setColor(color);
        }

        @Override
        @Deprecated
        SpotLight impl_createImpl() {
            return new SpotLight();
        }

        public final void setPointsAtX(double d2) {
            this.pointsAtXProperty().set(d2);
        }

        public final double getPointsAtX() {
            return this.pointsAtX == null ? 0.0 : this.pointsAtX.get();
        }

        public final DoubleProperty pointsAtXProperty() {
            if (this.pointsAtX == null) {
                this.pointsAtX = new DoublePropertyBase(){

                    @Override
                    public void invalidated() {
                        this.impl_markDirty();
                    }

                    @Override
                    public Object getBean() {
                        return this;
                    }

                    @Override
                    public String getName() {
                        return "pointsAtX";
                    }
                };
            }
            return this.pointsAtX;
        }

        public final void setPointsAtY(double d2) {
            this.pointsAtYProperty().set(d2);
        }

        public final double getPointsAtY() {
            return this.pointsAtY == null ? 0.0 : this.pointsAtY.get();
        }

        public final DoubleProperty pointsAtYProperty() {
            if (this.pointsAtY == null) {
                this.pointsAtY = new DoublePropertyBase(){

                    @Override
                    public void invalidated() {
                        this.impl_markDirty();
                    }

                    @Override
                    public Object getBean() {
                        return this;
                    }

                    @Override
                    public String getName() {
                        return "pointsAtY";
                    }
                };
            }
            return this.pointsAtY;
        }

        public final void setPointsAtZ(double d2) {
            this.pointsAtZProperty().set(d2);
        }

        public final double getPointsAtZ() {
            return this.pointsAtZ == null ? 0.0 : this.pointsAtZ.get();
        }

        public final DoubleProperty pointsAtZProperty() {
            if (this.pointsAtZ == null) {
                this.pointsAtZ = new DoublePropertyBase(){

                    @Override
                    public void invalidated() {
                        this.impl_markDirty();
                    }

                    @Override
                    public Object getBean() {
                        return this;
                    }

                    @Override
                    public String getName() {
                        return "pointsAtZ";
                    }
                };
            }
            return this.pointsAtZ;
        }

        public final void setSpecularExponent(double d2) {
            this.specularExponentProperty().set(d2);
        }

        public final double getSpecularExponent() {
            return this.specularExponent == null ? 1.0 : this.specularExponent.get();
        }

        public final DoubleProperty specularExponentProperty() {
            if (this.specularExponent == null) {
                this.specularExponent = new DoublePropertyBase(1.0){

                    @Override
                    public void invalidated() {
                        this.impl_markDirty();
                    }

                    @Override
                    public Object getBean() {
                        return this;
                    }

                    @Override
                    public String getName() {
                        return "specularExponent";
                    }
                };
            }
            return this.specularExponent;
        }

        @Override
        void impl_update() {
            super.impl_update();
            SpotLight spotLight = (SpotLight)this.impl_getImpl();
            spotLight.setPointsAtX((float)this.getPointsAtX());
            spotLight.setPointsAtY((float)this.getPointsAtY());
            spotLight.setPointsAtZ((float)this.getPointsAtZ());
            spotLight.setSpecularExponent((float)Utils.clamp(0.0, this.getSpecularExponent(), 4.0));
        }
    }

    public static class Point
    extends Light {
        private DoubleProperty x;
        private DoubleProperty y;
        private DoubleProperty z;

        public Point() {
        }

        public Point(double d2, double d3, double d4, Color color) {
            this.setX(d2);
            this.setY(d3);
            this.setZ(d4);
            this.setColor(color);
        }

        @Override
        @Deprecated
        PointLight impl_createImpl() {
            return new PointLight();
        }

        public final void setX(double d2) {
            this.xProperty().set(d2);
        }

        public final double getX() {
            return this.x == null ? 0.0 : this.x.get();
        }

        public final DoubleProperty xProperty() {
            if (this.x == null) {
                this.x = new DoublePropertyBase(){

                    @Override
                    public void invalidated() {
                        this.impl_markDirty();
                    }

                    @Override
                    public Object getBean() {
                        return this;
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
            this.yProperty().set(d2);
        }

        public final double getY() {
            return this.y == null ? 0.0 : this.y.get();
        }

        public final DoubleProperty yProperty() {
            if (this.y == null) {
                this.y = new DoublePropertyBase(){

                    @Override
                    public void invalidated() {
                        this.impl_markDirty();
                    }

                    @Override
                    public Object getBean() {
                        return this;
                    }

                    @Override
                    public String getName() {
                        return "y";
                    }
                };
            }
            return this.y;
        }

        public final void setZ(double d2) {
            this.zProperty().set(d2);
        }

        public final double getZ() {
            return this.z == null ? 0.0 : this.z.get();
        }

        public final DoubleProperty zProperty() {
            if (this.z == null) {
                this.z = new DoublePropertyBase(){

                    @Override
                    public void invalidated() {
                        this.impl_markDirty();
                    }

                    @Override
                    public Object getBean() {
                        return this;
                    }

                    @Override
                    public String getName() {
                        return "z";
                    }
                };
            }
            return this.z;
        }

        @Override
        void impl_update() {
            super.impl_update();
            PointLight pointLight = (PointLight)this.impl_getImpl();
            pointLight.setX((float)this.getX());
            pointLight.setY((float)this.getY());
            pointLight.setZ((float)this.getZ());
        }
    }

    public static class Distant
    extends Light {
        private DoubleProperty azimuth;
        private DoubleProperty elevation;

        public Distant() {
        }

        public Distant(double d2, double d3, Color color) {
            this.setAzimuth(d2);
            this.setElevation(d3);
            this.setColor(color);
        }

        @Override
        DistantLight impl_createImpl() {
            return new DistantLight();
        }

        public final void setAzimuth(double d2) {
            this.azimuthProperty().set(d2);
        }

        public final double getAzimuth() {
            return this.azimuth == null ? 45.0 : this.azimuth.get();
        }

        public final DoubleProperty azimuthProperty() {
            if (this.azimuth == null) {
                this.azimuth = new DoublePropertyBase(45.0){

                    @Override
                    public void invalidated() {
                        this.impl_markDirty();
                    }

                    @Override
                    public Object getBean() {
                        return this;
                    }

                    @Override
                    public String getName() {
                        return "azimuth";
                    }
                };
            }
            return this.azimuth;
        }

        public final void setElevation(double d2) {
            this.elevationProperty().set(d2);
        }

        public final double getElevation() {
            return this.elevation == null ? 45.0 : this.elevation.get();
        }

        public final DoubleProperty elevationProperty() {
            if (this.elevation == null) {
                this.elevation = new DoublePropertyBase(45.0){

                    @Override
                    public void invalidated() {
                        this.impl_markDirty();
                    }

                    @Override
                    public Object getBean() {
                        return this;
                    }

                    @Override
                    public String getName() {
                        return "elevation";
                    }
                };
            }
            return this.elevation;
        }

        @Override
        void impl_update() {
            super.impl_update();
            DistantLight distantLight = (DistantLight)this.impl_getImpl();
            distantLight.setAzimuth((float)this.getAzimuth());
            distantLight.setElevation((float)this.getElevation());
        }
    }
}

