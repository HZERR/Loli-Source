/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.sg.prism;

import com.sun.prism.Image;
import com.sun.prism.Material;
import com.sun.prism.PhongMaterial;
import com.sun.prism.ResourceFactory;
import com.sun.prism.TextureMap;
import com.sun.prism.paint.Color;

public class NGPhongMaterial {
    private static final Image WHITE_1X1 = Image.fromIntArgbPreData(new int[]{-1}, 1, 1);
    private PhongMaterial material;
    private Color diffuseColor;
    private boolean diffuseColorDirty = true;
    private TextureMap diffuseMap = new TextureMap(PhongMaterial.MapType.DIFFUSE);
    private Color specularColor;
    private boolean specularColorDirty = true;
    private float specularPower;
    private boolean specularPowerDirty = true;
    private TextureMap specularMap = new TextureMap(PhongMaterial.MapType.SPECULAR);
    private TextureMap bumpMap = new TextureMap(PhongMaterial.MapType.BUMP);
    private TextureMap selfIllumMap = new TextureMap(PhongMaterial.MapType.SELF_ILLUM);

    Material createMaterial(ResourceFactory resourceFactory) {
        if (this.material == null) {
            this.material = resourceFactory.createPhongMaterial();
        }
        this.validate(resourceFactory);
        return this.material;
    }

    private void validate(ResourceFactory resourceFactory) {
        if (this.diffuseColorDirty) {
            if (this.diffuseColor != null) {
                this.material.setDiffuseColor(this.diffuseColor.getRed(), this.diffuseColor.getGreen(), this.diffuseColor.getBlue(), this.diffuseColor.getAlpha());
            } else {
                this.material.setDiffuseColor(0.0f, 0.0f, 0.0f, 0.0f);
            }
            this.diffuseColorDirty = false;
        }
        if (this.diffuseMap.isDirty()) {
            if (this.diffuseMap.getImage() == null) {
                this.diffuseMap.setImage(WHITE_1X1);
            }
            this.material.setTextureMap(this.diffuseMap);
        }
        if (this.bumpMap.isDirty()) {
            this.material.setTextureMap(this.bumpMap);
        }
        if (this.selfIllumMap.isDirty()) {
            this.material.setTextureMap(this.selfIllumMap);
        }
        if (this.specularMap.isDirty()) {
            this.material.setTextureMap(this.specularMap);
        }
        if (this.specularColorDirty || this.specularPowerDirty) {
            if (this.specularColor != null) {
                float f2 = this.specularColor.getRed();
                float f3 = this.specularColor.getGreen();
                float f4 = this.specularColor.getBlue();
                this.material.setSpecularColor(true, f2, f3, f4, this.specularPower);
            } else {
                this.material.setSpecularColor(false, 1.0f, 1.0f, 1.0f, this.specularPower);
            }
            this.specularColorDirty = false;
            this.specularPowerDirty = false;
        }
    }

    public void setDiffuseColor(Object object) {
        this.diffuseColor = (Color)object;
        this.diffuseColorDirty = true;
    }

    public void setSpecularColor(Object object) {
        this.specularColor = (Color)object;
        this.specularColorDirty = true;
    }

    public void setSpecularPower(float f2) {
        if (f2 < 0.001f) {
            f2 = 0.001f;
        }
        this.specularPower = f2;
        this.specularPowerDirty = true;
    }

    public void setDiffuseMap(Object object) {
        this.diffuseMap.setImage((Image)object);
        this.diffuseMap.setDirty(true);
    }

    public void setSpecularMap(Object object) {
        this.specularMap.setImage((Image)object);
        this.specularMap.setDirty(true);
    }

    public void setBumpMap(Object object) {
        this.bumpMap.setImage((Image)object);
        this.bumpMap.setDirty(true);
    }

    public void setSelfIllumMap(Object object) {
        this.selfIllumMap.setImage((Image)object);
        this.selfIllumMap.setDirty(true);
    }

    Color test_getDiffuseColor() {
        return this.diffuseColor;
    }
}

