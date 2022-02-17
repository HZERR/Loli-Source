/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.ant;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JComponent;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.pushingpixels.lafwidget.ant.AugmentException;
import org.pushingpixels.lafwidget.ant.IconGhostingType;
import org.pushingpixels.lafwidget.ant.InfoClassVisitor;
import org.pushingpixels.lafwidget.ant.Utils;

public class IconGhostingAugmenter {
    private boolean isVerbose;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    protected synchronized void augmentClass(String dir, String name, String paintIconMethodName) {
        block36: {
            block37: {
                if (this.isVerbose) {
                    System.out.println("Working on " + name + "." + paintIconMethodName);
                }
                resource = dir + File.separator + name.replace('.', '/') + ".class";
                methodToAugment = null;
                methodInstallListeners = null;
                methodUninstallListeners = null;
                try {
                    cl = new URLClassLoader(new URL[]{new File(dir).toURL()}, IconGhostingAugmenter.class.getClassLoader());
                    for (clazz = cl.loadClass(name); clazz != null; clazz = clazz.getSuperclass()) {
                        methods = clazz.getDeclaredMethods();
                        for (i2 = 0; i2 < methods.length; ++i2) {
                            method = methods[i2];
                            if (methodInstallListeners == null && method.getName().equals("installListeners")) {
                                methodInstallListeners = method;
                                continue;
                            }
                            if (methodUninstallListeners == null && method.getName().equals("uninstallListeners")) {
                                methodUninstallListeners = method;
                                continue;
                            }
                            if (!paintIconMethodName.equals(method.getName())) continue;
                            params = method.getParameterTypes();
                            v0 = paramsOk = params.length == 3;
                            if (paramsOk) {
                                paramsOk = paramsOk != false && params[0] == Graphics.class;
                                paramsOk = paramsOk != false && JComponent.class.isAssignableFrom(params[1]) != false;
                                v1 = paramsOk = paramsOk != false && params[2] == Rectangle.class;
                                if (this.isVerbose) {
                                    System.out.println("Method params are " + params[0].getName() + ":" + params[1].getName() + ":" + params[2].getName() + " - " + paramsOk);
                                }
                            }
                            if (!paramsOk || methodToAugment != null) continue;
                            methodToAugment = method;
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
                if (!existingFields.contains("iconGhostingMarker")) break block36;
                break block37;
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
            System.out.println("Not augmenting resource, field 'iconGhostingMarker' is present");
            return;
        }
        is = new FileInputStream(resource);
        cr = new ClassReader(is);
        cw = new ClassWriter(false);
        cv = new AugmentClassAdapter(cw, existingMethods, existingFields, methodToAugment);
        cr.accept(cv, false);
        b2 = cw.toByteArray();
        try {
            is.close();
        }
        catch (IOException ioe) {}
        fos = null;
        ** try [egrp 9[TRYBLOCK] [12, 14 : 686->737)] { 
lbl79:
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
lbl100:
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
lbl107:
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

    public void process(String toStrip, File file, List<IconGhostingType> ids) throws AugmentException {
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            for (int i2 = 0; i2 < children.length; ++i2) {
                this.process(toStrip, children[i2], ids);
            }
        } else {
            String currClassName = file.getAbsolutePath().substring(toStrip.length() + 1);
            currClassName = currClassName.replace(File.separatorChar, '.');
            currClassName = currClassName.substring(0, currClassName.length() - 6);
            for (IconGhostingType igt : ids) {
                if (!currClassName.equals(igt.getClassName())) continue;
                this.augmentClass(toStrip, igt.getClassName(), igt.getMethodName());
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
            System.out.println("\tIf -class option is specified, its value is used as class name to augment.");
            System.out.println("\tIf -method option is specified, its value is used as method name to augment.");
            System.out.println("\tThe last parameter can point to either a file or a directory. The directory should be the root directory for classes.");
            return;
        }
        IconGhostingAugmenter uiDelegateAugmenter = new IconGhostingAugmenter();
        int argNum = 0;
        String className = null;
        String methodName = null;
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
            if (!"-method".equals(currArg)) break;
            methodName = args[++argNum];
            ++argNum;
        }
        File starter = new File(args[argNum]);
        IconGhostingType igt = new IconGhostingType();
        igt.setClassName(className);
        igt.setMethodName(methodName);
        uiDelegateAugmenter.process(starter.getAbsolutePath(), starter, Arrays.asList(new IconGhostingType[]{igt}));
    }

    protected class AugmentClassAdapter
    extends ClassAdapter
    implements Opcodes {
        private Set<String> existingMethods;
        private Set<String> existingFields;
        private Method methodToAugment;
        private String prefix;

        public AugmentClassAdapter(ClassVisitor cv, Set<String> existingMethods, Set<String> existingFields, Method methodToAugment) {
            super(cv);
            this.existingMethods = existingMethods;
            this.existingFields = existingFields;
            this.methodToAugment = methodToAugment;
        }

        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            Method[] toAugment;
            this.prefix = "__" + name.replaceAll("/", "__") + "__icon__";
            super.visit(version, access, name, signature, superName, interfaces);
            if (!this.existingFields.contains("iconGhostingMarker")) {
                FieldVisitor fv = this.visitField(4, "iconGhostingMarker", "Z", null, null);
                fv.visitEnd();
            }
            for (Method currMethodToAugment : toAugment = new Method[]{this.methodToAugment}) {
                String methodName = currMethodToAugment.getName();
                boolean hasOriginal = this.existingMethods.contains(methodName);
                boolean hasDelegate = this.existingMethods.contains(this.prefix + methodName);
                String methodSignature = Utils.getMethodDesc(currMethodToAugment);
                int paramCount = currMethodToAugment.getParameterTypes().length;
                if (IconGhostingAugmenter.this.isVerbose) {
                    System.out.println("... Augmenting " + methodName + " " + methodSignature + " : original - " + hasOriginal + ", delegate - " + hasDelegate + ", " + paramCount + " params");
                }
                if (hasDelegate) continue;
                this.augmentPaintIconMethod(!hasOriginal, name, superName, methodName, methodSignature);
            }
        }

        public void augmentPaintIconMethod(boolean toSynthOriginal, String className, String superClassName, String methodName, String methodDesc) {
            MethodVisitor mv;
            if (toSynthOriginal) {
                mv = this.cv.visitMethod(1, this.prefix + methodName, methodDesc, null, null);
                mv.visitCode();
                mv.visitVarInsn(25, 0);
                mv.visitVarInsn(25, 1);
                mv.visitVarInsn(25, 2);
                mv.visitVarInsn(25, 3);
                mv.visitMethodInsn(183, superClassName, methodName, methodDesc);
                mv.visitInsn(177);
                mv.visitMaxs(4, 4);
                mv.visitEnd();
            }
            mv = this.cv.visitMethod(4, methodName, methodDesc, null, null);
            mv.visitCode();
            mv.visitVarInsn(25, 2);
            mv.visitLdcInsn("icon.bounds");
            mv.visitTypeInsn(187, "java/awt/Rectangle");
            mv.visitInsn(89);
            mv.visitVarInsn(25, 3);
            mv.visitMethodInsn(183, "java/awt/Rectangle", "<init>", "(Ljava/awt/Rectangle;)V");
            mv.visitMethodInsn(182, "javax/swing/JComponent", "putClientProperty", "(Ljava/lang/Object;Ljava/lang/Object;)V");
            mv.visitVarInsn(25, 1);
            mv.visitMethodInsn(182, "java/awt/Graphics", "create", "()Ljava/awt/Graphics;");
            mv.visitTypeInsn(192, "java/awt/Graphics2D");
            mv.visitVarInsn(58, 4);
            mv.visitVarInsn(25, 4);
            mv.visitVarInsn(25, 2);
            mv.visitTypeInsn(192, "javax/swing/AbstractButton");
            mv.visitVarInsn(25, 3);
            mv.visitMethodInsn(184, "org/pushingpixels/lafwidget/animation/effects/GhostPaintingUtils", "paintGhostIcon", "(Ljava/awt/Graphics2D;Ljavax/swing/AbstractButton;Ljava/awt/Rectangle;)V");
            mv.visitVarInsn(25, 4);
            mv.visitMethodInsn(182, "java/awt/Graphics2D", "dispose", "()V");
            mv.visitVarInsn(25, 0);
            mv.visitVarInsn(25, 1);
            mv.visitVarInsn(25, 2);
            mv.visitVarInsn(25, 3);
            mv.visitMethodInsn(182, className, this.prefix + methodName, methodDesc);
            mv.visitInsn(177);
            mv.visitMaxs(5, 5);
            mv.visitEnd();
        }

        public void augmentInstallListeners(boolean toSynthOriginal, String className, String superClassName, String methodName, String functionDesc) {
            MethodVisitor mv;
            if (toSynthOriginal) {
                if (IconGhostingAugmenter.this.isVerbose) {
                    System.out.println("... Creating empty '" + methodName + functionDesc + "' forwarding to super '" + superClassName + "'");
                }
                mv = this.cv.visitMethod(1, this.prefix + methodName, functionDesc, null, null);
                mv.visitCode();
                mv.visitVarInsn(25, 0);
                mv.visitVarInsn(25, 1);
                mv.visitMethodInsn(183, superClassName, "installListeners", "(Ljavax/swing/AbstractButton;)V");
                mv.visitInsn(177);
                mv.visitMaxs(2, 2);
                mv.visitEnd();
            }
            if (IconGhostingAugmenter.this.isVerbose) {
                System.out.println("... Augmenting '" + methodName + functionDesc + "'");
            }
            mv = this.cv.visitMethod(1, methodName, functionDesc, null, null);
            mv.visitCode();
            mv.visitVarInsn(25, 0);
            mv.visitVarInsn(25, 1);
            mv.visitMethodInsn(182, className, this.prefix + "installListeners", functionDesc);
            mv.visitVarInsn(25, 0);
            mv.visitTypeInsn(187, className + "$1");
            mv.visitInsn(89);
            mv.visitVarInsn(25, 0);
            mv.visitVarInsn(25, 1);
            mv.visitMethodInsn(183, className + "$1", "<init>", "(L" + className + ";Ljavax/swing/AbstractButton;)V");
            mv.visitFieldInsn(181, className, "ghostPropertyListener", "Ljava/beans/PropertyChangeListener;");
            mv.visitVarInsn(25, 1);
            mv.visitVarInsn(25, 0);
            mv.visitFieldInsn(180, className, "ghostPropertyListener", "Ljava/beans/PropertyChangeListener;");
            mv.visitMethodInsn(182, "javax/swing/AbstractButton", "addPropertyChangeListener", "(Ljava/beans/PropertyChangeListener;)V");
            mv.visitVarInsn(25, 0);
            mv.visitTypeInsn(187, "org/pushingpixels/lafwidget/utils/GhostingListener");
            mv.visitInsn(89);
            mv.visitVarInsn(25, 1);
            mv.visitVarInsn(25, 1);
            mv.visitMethodInsn(182, "javax/swing/AbstractButton", "getModel", "()Ljavax/swing/ButtonModel;");
            mv.visitMethodInsn(183, "org/pushingpixels/lafwidget/utils/GhostingListener", "<init>", "(Ljava/awt/Component;Ljavax/swing/ButtonModel;)V");
            mv.visitFieldInsn(181, className, "ghostModelChangeListener", "Lorg/pushingpixels/lafwidget/utils/GhostingListener;");
            mv.visitVarInsn(25, 0);
            mv.visitFieldInsn(180, className, "ghostModelChangeListener", "Lorg/pushingpixels/lafwidget/utils/GhostingListener;");
            mv.visitMethodInsn(182, "org/pushingpixels/lafwidget/utils/GhostingListener", "registerListeners", "()V");
            mv.visitInsn(177);
            mv.visitMaxs(5, 2);
            mv.visitEnd();
        }

        public void augmentUninstallListeners(boolean toSynthOriginal, String className, String superClassName, String methodName, String functionDesc) {
            MethodVisitor mv;
            if (toSynthOriginal) {
                if (IconGhostingAugmenter.this.isVerbose) {
                    System.out.println("... Creating empty '" + methodName + functionDesc + "' forwarding to super '" + superClassName + "'");
                }
                mv = this.cv.visitMethod(1, this.prefix + methodName, functionDesc, null, null);
                mv.visitCode();
                mv.visitVarInsn(25, 0);
                mv.visitVarInsn(25, 1);
                mv.visitMethodInsn(183, superClassName, "uninstallListeners", "(Ljavax/swing/AbstractButton;)V");
                mv.visitInsn(177);
                mv.visitMaxs(2, 2);
                mv.visitEnd();
            }
            if (IconGhostingAugmenter.this.isVerbose) {
                System.out.println("... Augmenting '" + methodName + functionDesc + "'");
            }
            mv = this.cv.visitMethod(1, methodName, functionDesc, null, null);
            mv.visitCode();
            mv.visitVarInsn(25, 1);
            mv.visitVarInsn(25, 0);
            mv.visitFieldInsn(180, className, "ghostPropertyListener", "Ljava/beans/PropertyChangeListener;");
            mv.visitMethodInsn(182, "javax/swing/AbstractButton", "removePropertyChangeListener", "(Ljava/beans/PropertyChangeListener;)V");
            mv.visitVarInsn(25, 0);
            mv.visitInsn(1);
            mv.visitFieldInsn(181, className, "ghostPropertyListener", "Ljava/beans/PropertyChangeListener;");
            mv.visitVarInsn(25, 0);
            mv.visitFieldInsn(180, className, "ghostModelChangeListener", "Lorg/pushingpixels/lafwidget/utils/GhostingListener;");
            mv.visitMethodInsn(182, "org/pushingpixels/lafwidget/utils/GhostingListener", "unregisterListeners", "()V");
            mv.visitVarInsn(25, 0);
            mv.visitInsn(1);
            mv.visitFieldInsn(181, className, "ghostModelChangeListener", "Lorg/pushingpixels/lafwidget/utils/GhostingListener;");
            mv.visitVarInsn(25, 0);
            mv.visitVarInsn(25, 1);
            mv.visitMethodInsn(182, className, this.prefix + "uninstallListeners", "(Ljavax/swing/AbstractButton;)V");
            mv.visitInsn(177);
            mv.visitMaxs(2, 2);
            mv.visitEnd();
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            HashSet<String> toAugment = new HashSet<String>();
            toAugment.add(this.methodToAugment.getName());
            if (toAugment.contains(name) && !this.existingMethods.contains(this.prefix + name)) {
                if (IconGhostingAugmenter.this.isVerbose) {
                    System.out.println("... renaming '" + name + "(" + desc + ")' to '" + this.prefix + name + "'");
                }
                return this.cv.visitMethod(access, this.prefix + name, desc, signature, exceptions);
            }
            return this.cv.visitMethod(access, name, desc, signature, exceptions);
        }
    }
}

