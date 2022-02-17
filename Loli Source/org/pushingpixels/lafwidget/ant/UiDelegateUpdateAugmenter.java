/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.ant;

import java.awt.Graphics;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.pushingpixels.lafwidget.ant.AugmentException;
import org.pushingpixels.lafwidget.ant.InfoClassVisitor;
import org.pushingpixels.lafwidget.ant.Utils;

public class UiDelegateUpdateAugmenter {
    private boolean isVerbose;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    protected synchronized void augmentClass(String dir, String name) {
        if (this.isVerbose) {
            System.out.println("Working on " + name);
        }
        resource = dir + File.separator + name.replace('.', '/') + ".class";
        updateMethod = null;
        try {
            cl = new URLClassLoader(new URL[]{new File(dir).toURL()}, UiDelegateUpdateAugmenter.class.getClassLoader());
            if (!ComponentUI.class.isAssignableFrom(clazz)) {
                if (this.isVerbose == false) return;
                System.out.println("Not augmenting resource, doesn't extend ComponentUI");
                return;
            }
            for (clazz = cl.loadClass(name); clazz != null; clazz = clazz.getSuperclass()) {
                try {
                    updateMethod = clazz.getDeclaredMethod("update", new Class[]{Graphics.class, JComponent.class});
                }
                catch (NoSuchMethodException nsme) {
                    // empty catch block
                }
                if (updateMethod == null) {
                    continue;
                }
                break;
            }
        }
        catch (Exception e2) {
            throw new AugmentException(name, e2);
        }
        existingMethods = null;
        is = null;
        is = new FileInputStream(resource);
        cr = new ClassReader(is);
        infoAdapter = new InfoClassVisitor();
        cr.accept(infoAdapter, false);
        existingMethods = infoAdapter.getMethods();
        try {
            is.close();
        }
        catch (IOException ioe) {}
        ** try [egrp 6[TRYBLOCK] [9, 10 : 301->363)] { 
lbl37:
        // 1 sources

        ** GOTO lbl-1000
        catch (Exception e3) {
            try {
                throw new AugmentException(name, e3);
            }
            catch (Throwable throwable) {
                try {
                    is.close();
                    throw throwable;
                }
                catch (IOException ioe) {
                    // empty catch block
                }
                throw throwable;
            }
        }
lbl-1000:
        // 1 sources

        {
            is = new FileInputStream(resource);
            cr2 = new ClassReader(is);
            cw = new ClassWriter(false);
            cv = new AugmentClassAdapter(cw, existingMethods, updateMethod);
            cr2.accept(cv, false);
            b2 = cw.toByteArray();
        }
        try {
            is.close();
        }
        catch (IOException ioe) {}
        fos = null;
        ** try [egrp 10[TRYBLOCK] [14, 16 : 407->456)] { 
lbl60:
        // 1 sources

        ** GOTO lbl-1000
lbl61:
        // 1 sources

        catch (Exception e4) {
            try {
                throw new AugmentException(name, e4);
            }
lbl64:
            // 2 sources

            catch (Throwable throwable) {
                try {
                    is.close();
                    throw throwable;
                }
                catch (IOException ioe) {
                    // empty catch block
                }
                throw throwable;
            }
        }
lbl-1000:
        // 1 sources

        {
            fos = new FileOutputStream(resource);
            fos.write(b2);
            if (this.isVerbose) {
                System.out.println("Updated resource " + resource);
            }
            if (fos == null) return;
        }
        try {
            fos.close();
            return;
        }
        catch (IOException ioe) {
            return;
        }
lbl81:
        // 1 sources

        catch (Exception e5) {
            if (fos == null) return;
            try {
                fos.close();
                return;
            }
            catch (IOException ioe) {
                return;
            }
lbl88:
            // 1 sources

            catch (Throwable throwable) {
                if (fos == null) throw throwable;
                try {
                    fos.close();
                    throw throwable;
                }
                catch (IOException ioe) {
                    // empty catch block
                }
                throw throwable;
            }
        }
    }

    public void process(String toStrip, File file, Pattern pattern) throws AugmentException {
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            for (int i2 = 0; i2 < children.length; ++i2) {
                this.process(toStrip, children[i2], pattern);
            }
        } else {
            Matcher m2 = pattern.matcher(file.getName());
            if (m2.matches()) {
                String className = file.getAbsolutePath().substring(toStrip.length() + 1);
                className = className.replace(File.separatorChar, '.');
                this.augmentClass(toStrip, className.substring(0, className.length() - 6));
            }
        }
    }

    public void setVerbose(boolean isVerbose) {
        this.isVerbose = isVerbose;
    }

    public static void main(String[] args) throws AugmentException {
        if (args.length == 0) {
            System.out.println("Usage : java ... UiDelegateUpdateAugmenter [-verbose] [-pattern class_pattern] file_resource");
            System.out.println("\tIf -verbose option is specified, the augmenter prints out its actions.");
            System.out.println("\tIf -pattern option is specified, its value is used as a wildcard for matching the classes for augmentation.");
            System.out.println("\tThe last parameter can point to either a file or a directory. The directory should be the root directory for classes.");
            return;
        }
        UiDelegateUpdateAugmenter uiDelegateAugmenter = new UiDelegateUpdateAugmenter();
        int argNum = 0;
        String pattern = ".*UI.class";
        while (true) {
            String currArg;
            if ("-verbose".equals(currArg = args[argNum])) {
                uiDelegateAugmenter.setVerbose(true);
                ++argNum;
                continue;
            }
            if (!"-pattern".equals(currArg)) break;
            pattern = args[++argNum];
            ++argNum;
        }
        Pattern p2 = Pattern.compile(pattern);
        File starter = new File(args[argNum]);
        uiDelegateAugmenter.process(starter.getAbsolutePath(), starter, p2);
    }

    protected class AugmentClassAdapter
    extends ClassAdapter
    implements Opcodes {
        private Set<String> existingMethods;
        private Method updateMethod;
        private String prefix;

        public AugmentClassAdapter(ClassVisitor cv, Set<String> existingMethods, Method updateMethod) {
            super(cv);
            this.existingMethods = existingMethods;
            this.updateMethod = updateMethod;
        }

        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            this.prefix = "__" + name.replaceAll("/", "__") + "__";
            super.visit(version, access, name, signature, superName, interfaces);
            boolean hasOriginal = this.existingMethods.contains("update");
            boolean hasDelegate = this.existingMethods.contains(this.prefix + "update");
            String methodSignature = Utils.getMethodDesc(this.updateMethod);
            int paramCount = this.updateMethod.getParameterTypes().length;
            if (UiDelegateUpdateAugmenter.this.isVerbose) {
                System.out.println("... Augmenting update " + methodSignature + " : original - " + hasOriginal + ", delegate - " + hasDelegate + ", " + paramCount + " params");
            }
            if (!hasDelegate) {
                this.augmentUpdateMethod(!hasOriginal, name, superName, methodSignature);
            }
        }

        public void augmentUpdateMethod(boolean toSynthOriginal, String className, String superClassName, String functionDesc) {
            MethodVisitor mv;
            if (toSynthOriginal) {
                mv = this.cv.visitMethod(1, this.prefix + "update", "(Ljava/awt/Graphics;Ljavax/swing/JComponent;)V", null, null);
                mv.visitCode();
                mv.visitVarInsn(25, 0);
                mv.visitVarInsn(25, 1);
                mv.visitVarInsn(25, 2);
                mv.visitMethodInsn(183, superClassName, "update", "(Ljava/awt/Graphics;Ljavax/swing/JComponent;)V");
                mv.visitInsn(177);
                mv.visitMaxs(3, 3);
                mv.visitEnd();
            }
            mv = this.cv.visitMethod(1, "update", "(Ljava/awt/Graphics;Ljavax/swing/JComponent;)V", null, null);
            mv.visitCode();
            mv.visitVarInsn(25, 1);
            mv.visitMethodInsn(182, "java/awt/Graphics", "create", "()Ljava/awt/Graphics;");
            mv.visitTypeInsn(192, "java/awt/Graphics2D");
            mv.visitVarInsn(58, 3);
            mv.visitVarInsn(25, 3);
            mv.visitVarInsn(25, 2);
            mv.visitMethodInsn(184, "org/pushingpixels/lafwidget/utils/RenderingUtils", "installDesktopHints", "(Ljava/awt/Graphics2D;Ljava/awt/Component;)V");
            mv.visitVarInsn(25, 0);
            mv.visitVarInsn(25, 3);
            mv.visitVarInsn(25, 2);
            mv.visitMethodInsn(182, className, this.prefix + "update", "(Ljava/awt/Graphics;Ljavax/swing/JComponent;)V");
            mv.visitVarInsn(25, 3);
            mv.visitMethodInsn(182, "java/awt/Graphics2D", "dispose", "()V");
            mv.visitInsn(177);
            mv.visitMaxs(3, 4);
            mv.visitEnd();
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            if ("update".equals(name) && !this.existingMethods.contains(this.prefix + name)) {
                if (UiDelegateUpdateAugmenter.this.isVerbose) {
                    System.out.println("... renaming '" + name + "(" + desc + ")' to '" + this.prefix + name + "'");
                }
                return this.cv.visitMethod(access, this.prefix + name, desc, signature, exceptions);
            }
            return this.cv.visitMethod(access, name, desc, signature, exceptions);
        }
    }
}

