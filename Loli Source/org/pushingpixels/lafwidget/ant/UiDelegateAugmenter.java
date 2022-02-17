/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.ant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.pushingpixels.lafwidget.ant.AugmentException;
import org.pushingpixels.lafwidget.ant.InfoClassVisitor;
import org.pushingpixels.lafwidget.ant.Utils;

public class UiDelegateAugmenter {
    private Set<String> methodsToChange = new HashSet<String>();
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
        block34: {
            block35: {
                if (this.isVerbose) {
                    System.out.println("Working on " + name);
                }
                resource = dir + File.separator + name.replace('.', '/') + ".class";
                methodsToAugment = new HashMap<String, Method>();
                try {
                    cl = new URLClassLoader(new URL[]{new File(dir).toURL()}, UiDelegateAugmenter.class.getClassLoader());
                    if (!ComponentUI.class.isAssignableFrom(clazz)) {
                        if (this.isVerbose == false) return;
                        System.out.println("Not augmenting resource, doesn't extend ComponentUI");
                        return;
                    }
                    for (clazz = cl.loadClass(name); clazz != null; clazz = clazz.getSuperclass()) {
                        methods = clazz.getDeclaredMethods();
                        for (i2 = 0; i2 < methods.length; ++i2) {
                            method = methods[i2];
                            if (methodsToAugment.containsKey(method.getName()) || !this.methodsToChange.contains(method.getName())) continue;
                            isSupportedRetType = Void.TYPE == method.getReturnType();
                            paramTypes = method.getParameterTypes();
                            isSupportedParamList = paramTypes.length == 0 || paramTypes.length == 1 && JComponent.class.isAssignableFrom(paramTypes[0]) != false;
                            if (isSupportedRetType == false) throw new AugmentException("Method '" + method.getName() + "' in class '" + name + "' has unsupported signature");
                            if (isSupportedParamList == false) throw new AugmentException("Method '" + method.getName() + "' in class '" + name + "' has unsupported signature");
                            if (Modifier.isProtected(method.getModifiers()) || Modifier.isPublic(method.getModifiers())) {
                                methodsToAugment.put(method.getName(), method);
                                continue;
                            }
                            if (!this.isVerbose) continue;
                            System.out.println("Not augmenting private " + name + "." + method.getName());
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
                if (!existingFields.contains("lafWidgets")) break block34;
                break block35;
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
            System.out.println("Not augmenting resource, field 'lafWidgets' is present");
            return;
        }
        is = new FileInputStream(resource);
        cr = new ClassReader(is);
        cw = new ClassWriter(false);
        cv = new AugmentClassAdapter(cw, existingMethods, existingFields, methodsToAugment);
        cr.accept(cv, false);
        b2 = cw.toByteArray();
        try {
            is.close();
        }
        catch (IOException ioe) {}
        fos = null;
        ** try [egrp 9[TRYBLOCK] [13, 15 : 682->731)] { 
lbl76:
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
lbl97:
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
lbl104:
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

    public UiDelegateAugmenter() {
        this.methodsToChange.add("installUI");
        this.methodsToChange.add("installDefaults");
        this.methodsToChange.add("installComponents");
        this.methodsToChange.add("installListeners");
        this.methodsToChange.add("uninstallUI");
        this.methodsToChange.add("uninstallDefaults");
        this.methodsToChange.add("uninstallComponents");
        this.methodsToChange.add("uninstallListeners");
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
            System.out.println("Usage : java ... UiDelegateAugmenter [-verbose] [-pattern class_pattern] file_resource");
            System.out.println("\tIf -verbose option is specified, the augmenter prints out its actions.");
            System.out.println("\tIf -pattern option is specified, its value is used as a wildcard for matching the classes for augmentation.");
            System.out.println("\tThe last parameter can point to either a file or a directory. The directory should be the root directory for classes.");
            return;
        }
        UiDelegateAugmenter uiDelegateAugmenter = new UiDelegateAugmenter();
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
        private Set<String> existingFields;
        private Map<String, Method> methodsToAugment;
        private String prefix;

        public AugmentClassAdapter(ClassVisitor cv, Set<String> existingMethods, Set<String> existingFields, Map<String, Method> methodsToAugment) {
            super(cv);
            this.existingMethods = existingMethods;
            this.existingFields = existingFields;
            this.methodsToAugment = methodsToAugment;
        }

        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            this.prefix = "__" + name.replaceAll("/", "__") + "__";
            super.visit(version, access, name, signature, superName, interfaces);
            if (!this.existingFields.contains("lafWidgets")) {
                FieldVisitor fv = this.visitField(4, "lafWidgets", "Ljava/util/Set;", null, null);
                fv.visitEnd();
            }
            for (String methodName : UiDelegateAugmenter.this.methodsToChange) {
                if (!this.methodsToAugment.containsKey(methodName)) continue;
                boolean hasOriginal = this.existingMethods.contains(methodName);
                boolean hasDelegate = this.existingMethods.contains(this.prefix + methodName);
                Method method = this.methodsToAugment.get(methodName);
                String methodSignature = Utils.getMethodDesc(method);
                int paramCount = method.getParameterTypes().length;
                if (UiDelegateAugmenter.this.isVerbose) {
                    System.out.println("... Augmenting " + methodName + " " + methodSignature + " : original - " + hasOriginal + ", delegate - " + hasDelegate + ", " + paramCount + " params");
                }
                if (hasDelegate) continue;
                if (methodName.equals("installUI")) {
                    this.augmentInstallUIMethod(!hasOriginal, name, superName, methodSignature);
                    continue;
                }
                if (method.getParameterTypes().length == 0) {
                    this.augmentVoidMethod(!hasOriginal, name, superName, methodName, method.getModifiers());
                    continue;
                }
                this.augmentSingleParameterMethod(!hasOriginal, name, superName, methodName, method.getModifiers(), methodSignature);
            }
        }

        public void augmentVoidMethod(boolean toSynthOriginal, String className, String superClassName, String methodName, int methodModifiers) {
            MethodVisitor mv;
            int modifierOpcode = 1;
            if (Modifier.isProtected(methodModifiers)) {
                modifierOpcode = 4;
            }
            if (toSynthOriginal) {
                mv = this.cv.visitMethod(modifierOpcode, this.prefix + methodName, "()V", null, null);
                mv.visitCode();
                mv.visitVarInsn(25, 0);
                mv.visitMethodInsn(183, superClassName, methodName, "()V");
                mv.visitInsn(177);
                mv.visitMaxs(1, 1);
                mv.visitEnd();
            }
            mv = this.cv.visitMethod(modifierOpcode, methodName, "()V", null, null);
            mv.visitCode();
            mv.visitVarInsn(25, 0);
            mv.visitMethodInsn(182, className, this.prefix + methodName, "()V");
            mv.visitVarInsn(25, 0);
            mv.visitFieldInsn(180, className, "lafWidgets", "Ljava/util/Set;");
            mv.visitMethodInsn(185, "java/util/Set", "iterator", "()Ljava/util/Iterator;");
            mv.visitVarInsn(58, 1);
            Label l0 = new Label();
            mv.visitJumpInsn(167, l0);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitVarInsn(25, 1);
            mv.visitMethodInsn(185, "java/util/Iterator", "next", "()Ljava/lang/Object;");
            mv.visitTypeInsn(192, "org/pushingpixels/lafwidget/LafWidget");
            mv.visitVarInsn(58, 2);
            mv.visitVarInsn(25, 2);
            mv.visitMethodInsn(185, "org/pushingpixels/lafwidget/LafWidget", methodName, "()V");
            mv.visitLabel(l0);
            mv.visitVarInsn(25, 1);
            mv.visitMethodInsn(185, "java/util/Iterator", "hasNext", "()Z");
            mv.visitJumpInsn(154, l1);
            mv.visitInsn(177);
            mv.visitMaxs(1, 3);
            mv.visitEnd();
        }

        public void augmentSingleParameterMethod(boolean toSynthOriginal, String className, String superClassName, String methodName, int methodModifiers, String functionDesc) {
            MethodVisitor mv;
            int modifierOpcode = 1;
            if (Modifier.isProtected(methodModifiers)) {
                modifierOpcode = 4;
            }
            if (toSynthOriginal) {
                if (UiDelegateAugmenter.this.isVerbose) {
                    System.out.println("... Creating empty '" + methodName + functionDesc + "' forwarding to super '" + superClassName + "'");
                }
                mv = this.cv.visitMethod(modifierOpcode, this.prefix + methodName, functionDesc, null, null);
                mv.visitCode();
                mv.visitVarInsn(25, 0);
                mv.visitVarInsn(25, 1);
                mv.visitMethodInsn(183, superClassName, methodName, functionDesc);
                mv.visitInsn(177);
                mv.visitMaxs(2, 2);
                mv.visitEnd();
            }
            if (UiDelegateAugmenter.this.isVerbose) {
                System.out.println("... Augmenting '" + methodName + functionDesc + "'");
            }
            mv = this.cv.visitMethod(modifierOpcode, methodName, functionDesc, null, null);
            mv.visitCode();
            mv.visitVarInsn(25, 0);
            mv.visitVarInsn(25, 1);
            mv.visitMethodInsn(182, className, this.prefix + methodName, functionDesc);
            mv.visitVarInsn(25, 0);
            mv.visitFieldInsn(180, className, "lafWidgets", "Ljava/util/Set;");
            mv.visitMethodInsn(185, "java/util/Set", "iterator", "()Ljava/util/Iterator;");
            mv.visitVarInsn(58, 2);
            Label l0 = new Label();
            mv.visitJumpInsn(167, l0);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitVarInsn(25, 2);
            mv.visitMethodInsn(185, "java/util/Iterator", "next", "()Ljava/lang/Object;");
            mv.visitTypeInsn(192, "org/pushingpixels/lafwidget/LafWidget");
            mv.visitVarInsn(58, 3);
            mv.visitVarInsn(25, 3);
            mv.visitMethodInsn(185, "org/pushingpixels/lafwidget/LafWidget", methodName, "()V");
            mv.visitLabel(l0);
            mv.visitVarInsn(25, 2);
            mv.visitMethodInsn(185, "java/util/Iterator", "hasNext", "()Z");
            mv.visitJumpInsn(154, l1);
            mv.visitInsn(177);
            mv.visitMaxs(2, 4);
            mv.visitEnd();
        }

        public void augmentInstallUIMethod(boolean toSynthOriginal, String className, String superClassName, String functionDesc) {
            MethodVisitor mv;
            if (toSynthOriginal) {
                mv = this.cv.visitMethod(1, this.prefix + "installUI", "(Ljavax/swing/JComponent;)V", null, null);
                mv.visitCode();
                mv.visitVarInsn(25, 0);
                mv.visitVarInsn(25, 1);
                mv.visitMethodInsn(183, superClassName, "installUI", "(Ljavax/swing/JComponent;)V");
                mv.visitInsn(177);
                mv.visitMaxs(2, 2);
                mv.visitEnd();
            }
            mv = this.cv.visitMethod(1, "installUI", "(Ljavax/swing/JComponent;)V", null, null);
            mv.visitCode();
            mv.visitVarInsn(25, 0);
            mv.visitMethodInsn(184, "org/pushingpixels/lafwidget/LafWidgetRepository", "getRepository", "()Lorg/pushingpixels/lafwidget/LafWidgetRepository;");
            mv.visitVarInsn(25, 1);
            mv.visitMethodInsn(182, "org/pushingpixels/lafwidget/LafWidgetRepository", "getMatchingWidgets", "(Ljavax/swing/JComponent;)Ljava/util/Set;");
            mv.visitFieldInsn(181, className, "lafWidgets", "Ljava/util/Set;");
            mv.visitVarInsn(25, 0);
            mv.visitVarInsn(25, 1);
            mv.visitMethodInsn(182, className, this.prefix + "installUI", "(Ljavax/swing/JComponent;)V");
            mv.visitVarInsn(25, 0);
            mv.visitFieldInsn(180, className, "lafWidgets", "Ljava/util/Set;");
            mv.visitMethodInsn(185, "java/util/Set", "iterator", "()Ljava/util/Iterator;");
            mv.visitVarInsn(58, 2);
            Label l0 = new Label();
            mv.visitJumpInsn(167, l0);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitVarInsn(25, 2);
            mv.visitMethodInsn(185, "java/util/Iterator", "next", "()Ljava/lang/Object;");
            mv.visitTypeInsn(192, "org/pushingpixels/lafwidget/LafWidget");
            mv.visitVarInsn(58, 3);
            mv.visitVarInsn(25, 3);
            mv.visitMethodInsn(185, "org/pushingpixels/lafwidget/LafWidget", "installUI", "()V");
            mv.visitLabel(l0);
            mv.visitVarInsn(25, 2);
            mv.visitMethodInsn(185, "java/util/Iterator", "hasNext", "()Z");
            mv.visitJumpInsn(154, l1);
            mv.visitInsn(177);
            mv.visitMaxs(3, 4);
            mv.visitEnd();
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            if (UiDelegateAugmenter.this.methodsToChange.contains(name) && !this.existingMethods.contains(this.prefix + name)) {
                if (UiDelegateAugmenter.this.isVerbose) {
                    System.out.println("... renaming '" + name + "(" + desc + ")' to '" + this.prefix + name + "'");
                }
                return this.cv.visitMethod(access, this.prefix + name, desc, signature, exceptions);
            }
            return this.cv.visitMethod(access, name, desc, signature, exceptions);
        }
    }
}

