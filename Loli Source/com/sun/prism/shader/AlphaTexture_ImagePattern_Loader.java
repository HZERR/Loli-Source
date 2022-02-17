/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.shader;

import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderFactory;
import java.io.InputStream;
import java.util.HashMap;

public class AlphaTexture_ImagePattern_Loader {
    private AlphaTexture_ImagePattern_Loader() {
    }

    public static Shader loadShader(ShaderFactory shaderFactory, InputStream inputStream) {
        HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
        hashMap.put("inputTex", 1);
        hashMap.put("maskInput", 0);
        HashMap<String, Integer> hashMap2 = new HashMap<String, Integer>();
        hashMap2.put("content", 0);
        return shaderFactory.createShader(inputStream, hashMap, hashMap2, 1, false, true);
    }
}

