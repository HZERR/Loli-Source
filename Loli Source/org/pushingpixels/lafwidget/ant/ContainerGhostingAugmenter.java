/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.ant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.pushingpixels.lafwidget.ant.AugmentException;
import org.pushingpixels.lafwidget.ant.ContainerGhostingType;
import org.pushingpixels.lafwidget.ant.InfoClassVisitor;
import org.pushingpixels.lafwidget.ant.Utils;

public class ContainerGhostingAugmenter {
    private boolean isVerbose;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    protected synchronized void augmentClass(String dir, String name, boolean toInjectAfterOriginal) {
        block33: {
            block34: {
                if (this.isVerbose) {
                    System.out.println("Working on " + name + ".update() [" + (toInjectAfterOriginal != false ? "after]" : "before]"));
                }
                resource = dir + File.separator + name.replace('.', '/') + ".class";
                methodToAugment = null;
                try {
                    cl = new URLClassLoader(new URL[]{new File(dir).toURL()}, ContainerGhostingAugmenter.class.getClassLoader());
                    block28: for (clazz = cl.loadClass(name); clazz != null && methodToAugment == null; clazz = clazz.getSuperclass()) {
                        methods = clazz.getDeclaredMethods();
                        for (i2 = 0; i2 < methods.length; ++i2) {
                            method = methods[i2];
                            if (!"update".equals(method.getName())) {
                                continue;
                            }
                            methodToAugment = method;
                            continue block28;
                        }
                    }
                }
                catch (Exception e2) {
                    throw new AugmentException(name, e2);
                }
                existingMethods = null;
                existingFields = null;
                is = null;
                is = new FileInputStream(resource);
                cr = new ClassReader(is);
                infoAdapter = new InfoClassVisitor();
                cr.accept(infoAdapter, false);
                existingMethods = infoAdapter.getMethods();
                existingFields = infoAdapter.getFields();
                try {
                    is.close();
                }
                catch (IOException ioe) {}
                if (!existingFields.contains("containerGhostingMarker")) break block33;
                break block34;
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
            }
            if (this.isVerbose == false) return;
            System.out.println("Not augmenting resource, field 'containerGhostingMarker' is present");
            return;
        }
        is = new FileInputStream(resource);
        cr = new ClassReader(is);
        cw = new ClassWriter(false);
        cv = new AugmentClassAdapter(cw, existingMethods, existingFields, methodToAugment, toInjectAfterOriginal);
        cr.accept(cv, false);
        b2 = cw.toByteArray();
        try {
            is.close();
        }
        catch (IOException ioe) {}
        fos = null;
        ** try [egrp 9[TRYBLOCK] [12, 14 : 467->518)] { 
lbl63:
        // 1 sources

        ** GOTO lbl-1000
        catch (Exception e4) {
            try {
                throw new AugmentException(name, e4);
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
lbl84:
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
lbl91:
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

    public void process(String toStrip, File file, List<ContainerGhostingType> ids) throws AugmentException {
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            for (int i2 = 0; i2 < children.length; ++i2) {
                this.process(toStrip, children[i2], ids);
            }
        } else {
            String currClassName = file.getAbsolutePath().substring(toStrip.length() + 1);
            currClassName = currClassName.replace(File.separatorChar, '.');
            currClassName = currClassName.substring(0, currClassName.length() - 6);
            for (ContainerGhostingType igt : ids) {
                if (!currClassName.equals(igt.getClassName())) continue;
                this.augmentClass(toStrip, igt.getClassName(), igt.isToInjectAfterOriginal());
            }
        }
    }

    public void setVerbose(boolean isVerbose) {
        this.isVerbose = isVerbose;
    }

    public static void main(String[] args) throws AugmentException {
        if (args.length == 0) {
            System.out.println("Usage : java ... IconGhostingDelegateAugmenter [-verbose] [-pattern class_pattern] file_resource");
            System.out.println("\tIf -verbose option is specified, the augmenter prints out its actions.");
            System.out.println("\tIf -class option if specified, its value is used as class name to augment.");
            System.out.println("\tIf -before option if specified, the code is injected before the original code.");
            System.out.println("\tThe last parameter can point to either a file or a directory. The directory should be the root directory for classes.");
            return;
        }
        ContainerGhostingAugmenter uiDelegateAugmenter = new ContainerGhostingAugmenter();
        int argNum = 0;
        String className = null;
        boolean isAfter = true;
        while (true) {
            String currArg;
            if ("-verbose".equals(currArg = args[argNum])) {
                uiDelegateAugmenter.setVerbose(true);
                ++argNum;
                continue;
            }
            if ("-class".equals(currArg)) {
                className = args[++argNum];
                ++argNum;
                continue;
            }
            if (!"-before".equals(currArg)) break;
            ++argNum;
            isAfter = false;
        }
        File starter = new File(args[argNum]);
        ContainerGhostingType igt = new ContainerGhostingType();
        igt.setClassName(className);
        igt.setToInjectAfterOriginal(isAfter);
        uiDelegateAugmenter.process(starter.getAbsolutePath(), starter, Arrays.asList(new ContainerGhostingType[]{igt}));
    }

    protected class AugmentClassAdapter
    extends ClassAdapter
    implements Opcodes {
        private Set<String> existingMethods;
        private Set<String> existingFields;
        private Method methodToAugment;
        private boolean toInjectAfterOriginal;
        private String prefix;

        public AugmentClassAdapter(ClassVisitor cv, Set<String> existingMethods, Set<String> existingFields, Method methodToAugment, boolean toInjectAfterOriginal) {
            super(cv);
            this.existingMethods = existingMethods;
            this.existingFields = existingFields;
            this.methodToAugment = methodToAugment;
            this.toInjectAfterOriginal = toInjectAfterOriginal;
        }

        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            this.prefix = "__" + name.replaceAll("/", "__") + "__container__";
            super.visit(version, access, name, signature, superName, interfaces);
            if (!this.existingFields.contains("containerGhostingMarker")) {
                FieldVisitor fv = this.visitField(4, "containerGhostingMarker", "Z", null, null);
                fv.visitEnd();
            }
            String methodName = this.methodToAugment.getName();
            boolean hasOriginal = this.existingMethods.contains(methodName);
            boolean hasDelegate = this.existingMethods.contains(this.prefix + methodName);
            String methodSignature = Utils.getMethodDesc(this.methodToAugment);
            int paramCount = this.methodToAugment.getParameterTypes().length;
            if (ContainerGhostingAugmenter.this.isVerbose) {
                System.out.println("... Augmenting " + methodName + " " + methodSignature + " : original - " + hasOriginal + ", delegate - " + hasDelegate + ", " + paramCount + " params");
            }
            if (!hasDelegate) {
                if (this.toInjectAfterOriginal) {
                    this.augmentUpdateMethodAfter(!hasOriginal, name, superName, methodSignature);
                } else {
                    this.augmentUpdateMethodBefore(!hasOriginal, name, superName, methodSignature);
                }
            }
        }

        public void augmentUpdateMethodBefore(boolean toSynthOriginal, String className, String superClassName, String methodDesc) {
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
            mv = this.cv.visitMethod(4, "update", methodDesc, null, null);
            mv.visitCode();
            mv.visitVarInsn(25, 2);
            mv.visitVarInsn(25, 1);
            mv.visitMethodInsn(184, "org/pushingpixels/lafwidget/animation/effects/GhostPaintingUtils", "paintGhostImages", "(Ljava/awt/Component;Ljava/awt/Graphics;)V");
            mv.visitVarInsn(25, 0);
            mv.visitVarInsn(25, 1);
            mv.visitVarInsn(25, 2);
            mv.visitMethodInsn(182, className, this.prefix + "update", "(Ljava/awt/Graphics;Ljavax/swing/JComponent;)V");
            mv.visitInsn(177);
            mv.visitMaxs(3, 3);
            mv.visitEnd();
        }

        public void augmentUpdateMethodAfter(boolean toSynthOriginal, String className, String superClassName, String methodDesc) {
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
            mv = this.cv.visitMethod(4, "update", methodDesc, null, null);
            mv.visitCode();
            mv.visitVarInsn(25, 0);
            mv.visitVarInsn(25, 1);
            mv.visitVarInsn(25, 2);
            mv.visitMethodInsn(182, className, this.prefix + "update", "(Ljava/awt/Graphics;Ljavax/swing/JComponent;)V");
            mv.visitVarInsn(25, 2);
            mv.visitVarInsn(25, 1);
            mv.visitMethodInsn(184, "org/pushingpixels/lafwidget/animation/effects/GhostPaintingUtils", "paintGhostImages", "(Ljava/awt/Component;Ljava/awt/Graphics;)V");
            mv.visitInsn(177);
            mv.visitMaxs(3, 3);
            mv.visitEnd();
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            if (this.methodToAugment.getName().equals(name) && !this.existingMethods.contains(this.prefix + name)) {
                if (ContainerGhostingAugmenter.this.isVerbose) {
                    System.out.println("... renaming '" + name + "(" + desc + ")' to '" + this.prefix + name + "'");
                }
                return this.cv.visitMethod(access, this.prefix + name, desc, signature, exceptions);
            }
            return this.cv.visitMethod(access, name, desc, signature, exceptions);
        }
    }
}

