/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.shader;

import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderFactory;
import java.io.InputStream;
import java.util.HashMap;

public class DrawEllipse_ImagePattern_AlphaTest_Loader {
    private DrawEllipse_ImagePattern_AlphaTest_Loader() {
    }

    public static Shader loadShader(ShaderFactory shaderFactory, InputStream inputStream) {
        HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
        hashMap.put("inputTex", 0);
        HashMap<String, Integer> hashMap2 = new HashMap<String, Integer>();
        hashMap2.put("perspVec", 3);
        hashMap2.put("xParams", 1);
        hashMap2.put("idim", 0);
        hashMap2.put("yParams", 2);
        hashMap2.put("content", 4);
        return shaderFactory.createShader(inputStream, hashMap, hashMap2, 1, true, true);
    }
}

