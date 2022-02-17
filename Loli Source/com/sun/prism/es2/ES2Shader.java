/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.es2;

import com.sun.prism.es2.ES2Context;
import com.sun.prism.es2.GLContext;
import com.sun.prism.impl.BaseGraphicsResource;
import com.sun.prism.impl.Disposer;
import com.sun.prism.ps.Shader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ES2Shader
extends BaseGraphicsResource
implements Shader {
    private int programID;
    private final ES2Context context;
    private final Map<String, Uniform> uniforms = new HashMap<String, Uniform>();
    private final int maxTexCoordIndex;
    private final boolean isPixcoordUsed;
    private boolean valid;
    private float[] currentMatrix;

    private ES2Shader(ES2Context eS2Context, int n2, int n3, int[] arrn, Map<String, Integer> map, int n4, boolean bl) throws RuntimeException {
        super(new ES2ShaderDisposerRecord(eS2Context, n3, arrn, n2));
        this.context = eS2Context;
        this.programID = n2;
        this.maxTexCoordIndex = n4;
        this.isPixcoordUsed = bl;
        boolean bl2 = this.valid = n2 != 0;
        if (this.valid && map != null) {
            int n5 = eS2Context.getShaderProgram();
            eS2Context.setShaderProgram(n2);
            for (String string : map.keySet()) {
                this.setConstant(string, map.get(string));
            }
            eS2Context.setShaderProgram(n5);
        }
    }

    static ES2Shader createFromSource(ES2Context eS2Context, String string, String[] arrstring, Map<String, Integer> map, Map<String, Integer> map2, int n2, boolean bl) {
        GLContext gLContext = eS2Context.getGLContext();
        if (!gLContext.isShaderCompilerSupported()) {
            throw new RuntimeException("Shader compiler not available on this device");
        }
        if (string == null || arrstring == null || arrstring.length == 0) {
            throw new RuntimeException("Both vertexShaderSource and fragmentShaderSource must be specified");
        }
        int n3 = gLContext.compileShader(string, true);
        if (n3 == 0) {
            throw new RuntimeException("Error creating vertex shader");
        }
        int[] arrn = new int[arrstring.length];
        for (int i2 = 0; i2 < arrstring.length; ++i2) {
            arrn[i2] = gLContext.compileShader(arrstring[i2], false);
            if (arrn[i2] != 0) continue;
            gLContext.deleteShader(n3);
            throw new RuntimeException("Error creating fragment shader");
        }
        String[] arrstring2 = new String[map2.size()];
        int[] arrn2 = new int[arrstring2.length];
        int n4 = 0;
        Iterator<String> iterator = map2.keySet().iterator();
        while (iterator.hasNext()) {
            String string2;
            arrstring2[n4] = string2 = iterator.next();
            arrn2[n4] = map2.get(string2);
            ++n4;
        }
        int n5 = gLContext.createProgram(n3, arrn, arrstring2, arrn2);
        if (n5 == 0) {
            throw new RuntimeException("Error creating shader program");
        }
        return new ES2Shader(eS2Context, n5, n3, arrn, map, n2, bl);
    }

    static ES2Shader createFromSource(ES2Context eS2Context, String string, InputStream inputStream, Map<String, Integer> map, Map<String, Integer> map2, int n2, boolean bl) {
        String[] arrstring = new String[]{ES2Shader.readStreamIntoString(inputStream)};
        return ES2Shader.createFromSource(eS2Context, string, arrstring, map, map2, n2, bl);
    }

    static String readStreamIntoString(InputStream inputStream) {
        StringBuffer stringBuffer = new StringBuffer(1024);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            char[] arrc = new char[1024];
            int n2 = 0;
            while ((n2 = bufferedReader.read(arrc)) > -1) {
                stringBuffer.append(String.valueOf(arrc, 0, n2));
            }
        }
        catch (IOException iOException) {
            throw new RuntimeException("Error reading shader stream");
        }
        finally {
            try {
                bufferedReader.close();
            }
            catch (IOException iOException) {
                throw new RuntimeException("Error closing reader");
            }
        }
        return stringBuffer.toString();
    }

    public int getProgramObject() {
        return this.programID;
    }

    public int getMaxTexCoordIndex() {
        return this.maxTexCoordIndex;
    }

    public boolean isPixcoordUsed() {
        return this.isPixcoordUsed;
    }

    private Uniform getUniform(String string) {
        Uniform uniform = this.uniforms.get(string);
        if (uniform == null) {
            int n2 = this.context.getGLContext().getUniformLocation(this.programID, string);
            uniform = new Uniform();
            uniform.location = n2;
            this.uniforms.put(string, uniform);
        }
        return uniform;
    }

    @Override
    public void enable() throws RuntimeException {
        this.context.updateShaderProgram(this.programID);
    }

    @Override
    public void disable() throws RuntimeException {
        this.context.updateShaderProgram(0);
    }

    @Override
    public boolean isValid() {
        return this.valid;
    }

    @Override
    public void setConstant(String string, int n2) throws RuntimeException {
        int[] arrn;
        Uniform uniform = this.getUniform(string);
        if (uniform.location == -1) {
            return;
        }
        if (uniform.values == null) {
            uniform.values = new int[1];
        }
        if ((arrn = (int[])uniform.values)[0] != n2) {
            arrn[0] = n2;
            this.context.getGLContext().uniform1i(uniform.location, n2);
        }
    }

    @Override
    public void setConstant(String string, int n2, int n3) throws RuntimeException {
        int[] arrn;
        Uniform uniform = this.getUniform(string);
        if (uniform.location == -1) {
            return;
        }
        if (uniform.values == null) {
            uniform.values = new int[2];
        }
        if ((arrn = (int[])uniform.values)[0] != n2 || arrn[1] != n3) {
            arrn[0] = n2;
            arrn[1] = n3;
            this.context.getGLContext().uniform2i(uniform.location, n2, n3);
        }
    }

    @Override
    public void setConstant(String string, int n2, int n3, int n4) throws RuntimeException {
        int[] arrn;
        Uniform uniform = this.getUniform(string);
        if (uniform.location == -1) {
            return;
        }
        if (uniform.values == null) {
            uniform.values = new int[3];
        }
        if ((arrn = (int[])uniform.values)[0] != n2 || arrn[1] != n3 || arrn[2] != n4) {
            arrn[0] = n2;
            arrn[1] = n3;
            arrn[2] = n4;
            this.context.getGLContext().uniform3i(uniform.location, n2, n3, n4);
        }
    }

    @Override
    public void setConstant(String string, int n2, int n3, int n4, int n5) throws RuntimeException {
        int[] arrn;
        Uniform uniform = this.getUniform(string);
        if (uniform.location == -1) {
            return;
        }
        if (uniform.values == null) {
            uniform.values = new int[4];
        }
        if ((arrn = (int[])uniform.values)[0] != n2 || arrn[1] != n3 || arrn[2] != n4 || arrn[3] != n5) {
            arrn[0] = n2;
            arrn[1] = n3;
            arrn[2] = n4;
            arrn[3] = n5;
            this.context.getGLContext().uniform4i(uniform.location, n2, n3, n4, n5);
        }
    }

    @Override
    public void setConstant(String string, float f2) throws RuntimeException {
        float[] arrf;
        Uniform uniform = this.getUniform(string);
        if (uniform.location == -1) {
            return;
        }
        if (uniform.values == null) {
            uniform.values = new float[1];
        }
        if ((arrf = (float[])uniform.values)[0] != f2) {
            arrf[0] = f2;
            this.context.getGLContext().uniform1f(uniform.location, f2);
        }
    }

    @Override
    public void setConstant(String string, float f2, float f3) throws RuntimeException {
        float[] arrf;
        Uniform uniform = this.getUniform(string);
        if (uniform.location == -1) {
            return;
        }
        if (uniform.values == null) {
            uniform.values = new float[2];
        }
        if ((arrf = (float[])uniform.values)[0] != f2 || arrf[1] != f3) {
            arrf[0] = f2;
            arrf[1] = f3;
            this.context.getGLContext().uniform2f(uniform.location, f2, f3);
        }
    }

    @Override
    public void setConstant(String string, float f2, float f3, float f4) throws RuntimeException {
        float[] arrf;
        Uniform uniform = this.getUniform(string);
        if (uniform.location == -1) {
            return;
        }
        if (uniform.values == null) {
            uniform.values = new float[3];
        }
        if ((arrf = (float[])uniform.values)[0] != f2 || arrf[1] != f3 || arrf[2] != f4) {
            arrf[0] = f2;
            arrf[1] = f3;
            arrf[2] = f4;
            this.context.getGLContext().uniform3f(uniform.location, f2, f3, f4);
        }
    }

    @Override
    public void setConstant(String string, float f2, float f3, float f4, float f5) throws RuntimeException {
        float[] arrf;
        Uniform uniform = this.getUniform(string);
        if (uniform.location == -1) {
            return;
        }
        if (uniform.values == null) {
            uniform.values = new float[4];
        }
        if ((arrf = (float[])uniform.values)[0] != f2 || arrf[1] != f3 || arrf[2] != f4 || arrf[3] != f5) {
            arrf[0] = f2;
            arrf[1] = f3;
            arrf[2] = f4;
            arrf[3] = f5;
            this.context.getGLContext().uniform4f(uniform.location, f2, f3, f4, f5);
        }
    }

    @Override
    public void setConstants(String string, IntBuffer intBuffer, int n2, int n3) throws RuntimeException {
        int n4 = this.getUniform(string).location;
        if (n4 == -1) {
            return;
        }
        this.context.getGLContext().uniform4iv(n4, n3, intBuffer);
    }

    @Override
    public void setConstants(String string, FloatBuffer floatBuffer, int n2, int n3) throws RuntimeException {
        int n4 = this.getUniform(string).location;
        if (n4 == -1) {
            return;
        }
        this.context.getGLContext().uniform4fv(n4, n3, floatBuffer);
    }

    public void setMatrix(String string, float[] arrf) throws RuntimeException {
        int n2 = this.getUniform(string).location;
        if (n2 == -1) {
            return;
        }
        if (this.currentMatrix == null) {
            this.currentMatrix = new float[16];
        }
        if (!Arrays.equals(this.currentMatrix, arrf)) {
            this.context.getGLContext().uniformMatrix4fv(n2, false, arrf);
            System.arraycopy(arrf, 0, this.currentMatrix, 0, arrf.length);
        }
    }

    @Override
    public void dispose() throws RuntimeException {
        if (this.programID != 0) {
            this.disposerRecord.dispose();
            this.programID = 0;
        }
        this.valid = false;
    }

    private static class ES2ShaderDisposerRecord
    implements Disposer.Record {
        private final ES2Context context;
        private int vertexShaderID;
        private int[] fragmentShaderID;
        private int programID;

        private ES2ShaderDisposerRecord(ES2Context eS2Context, int n2, int[] arrn, int n3) {
            this.context = eS2Context;
            this.vertexShaderID = n2;
            this.fragmentShaderID = arrn;
            this.programID = n3;
        }

        @Override
        public void dispose() {
            if (this.programID != 0) {
                this.context.getGLContext().disposeShaders(this.programID, this.vertexShaderID, this.fragmentShaderID);
                this.vertexShaderID = 0;
                this.programID = 0;
                this.fragmentShaderID = null;
            }
        }
    }

    private static class Uniform {
        private int location;
        private Object values;

        private Uniform() {
        }
    }
}

