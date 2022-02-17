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
import java.util.Set;
import javax.swing.UIDefaults;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.pushingpixels.lafwidget.ant.AugmentException;
import org.pushingpixels.lafwidget.ant.InfoClassVisitor;
import org.pushingpixels.lafwidget.ant.UiDelegateWriterEmptyCtr;
import org.pushingpixels.lafwidget.ant.UiDelegateWriterOneParamCtr;
import org.pushingpixels.lafwidget.ant.Utils;

public class LafMainClassAugmenter {
    protected static String METHOD_NAME = "initClassDefaults";
    private boolean isVerbose;
    private String[] delegatesToAdd;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    protected synchronized String augmentClass(String dir, String name) {
        if (this.isVerbose) {
            System.out.println("Working on LAF main class " + name);
        }
        resource = dir + File.separator + name.replace('.', '/') + ".class";
        origMethod = null;
        try {
            cl = new URLClassLoader(new URL[]{new File(dir).toURL()}, LafMainClassAugmenter.class.getClassLoader());
            clazz = cl.loadClass(name);
            origMethod = clazz.getDeclaredMethod(LafMainClassAugmenter.METHOD_NAME, new Class[]{UIDefaults.class});
        }
        catch (NoSuchMethodException nsme) {
            origMethod = null;
        }
        catch (Exception e2) {
            e2.printStackTrace();
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
        superClassName = null;
        ** try [egrp 5[TRYBLOCK] [8, 9 : 262->331)] { 
lbl30:
        // 1 sources

        ** GOTO lbl-1000
        catch (Exception e3) {
            try {
                e3.printStackTrace();
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
            cv = new AugmentClassAdapter(cw, existingMethods, origMethod);
            cr2.accept(cv, false);
            b2 = cw.toByteArray();
            superClassName = cv.getSuperClassName();
        }
        try {
            is.close();
        }
        catch (IOException ioe) {}
        fos = null;
        ** try [egrp 9[TRYBLOCK] [13, 15 : 380->429)] { 
lbl55:
        // 1 sources

        ** GOTO lbl-1000
lbl56:
        // 1 sources

        catch (Exception e4) {
            try {
                e4.printStackTrace();
                throw new AugmentException(name, e4);
            }
lbl60:
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
            if (fos == null) return superClassName;
        }
        try {
            fos.close();
            return superClassName;
        }
        catch (IOException ioe) {
            return superClassName;
        }
lbl77:
        // 1 sources

        catch (Exception e5) {
            if (fos == null) return superClassName;
            try {
                fos.close();
                return superClassName;
            }
            catch (IOException ioe) {
                return superClassName;
            }
lbl84:
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

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public void process(String toStrip, File file, String mainClassName) {
        if (file.isDirectory()) {
            children = file.listFiles();
            i2 = 0;
            while (i2 < children.length) {
                this.process(toStrip, children[i2], mainClassName);
                ++i2;
            }
            return;
        }
        className = file.getAbsolutePath().substring(toStrip.length() + 1);
        className = className.replace(File.separatorChar, '.');
        if (mainClassName.equals(className = className.substring(0, className.length() - 6)) == false) return;
        superClassName = this.augmentClass(toStrip, className);
        i3 = 0;
        while (i3 < this.delegatesToAdd.length) {
            uiKey = this.delegatesToAdd[i3];
            uiSuperClassName = Utils.getUtils().getUIDelegate(uiKey, superClassName);
            uiSuperCtrParams = null;
            try {
                uiSuperClazz = Class.forName(uiSuperClassName);
                uiSuperCtrs = uiSuperClazz.getDeclaredConstructors();
                if (uiSuperCtrs.length != 1) {
                    throw new AugmentException("Unsupported base UI class " + uiSuperClassName + " - not exactly one ctr");
                }
                uiSuperCtr = uiSuperCtrs[0];
                uiSuperCtrParams = uiSuperCtr.getParameterTypes();
                if (uiSuperCtrParams.length > 1) {
                    throw new AugmentException("Unsupported base UI class " + uiSuperClassName + " - " + uiSuperCtrParams.length + " parameters");
                }
            }
            catch (ClassNotFoundException cnfe) {
                throw new AugmentException("Failed locating base UI class", cnfe);
            }
            lastDotIndex = className.lastIndexOf(46);
            packageName = lastDotIndex >= 0 ? className.substring(0, lastDotIndex) : "";
            uiClassName = "__Forwarding__" + uiKey;
            resource = toStrip + File.separator + (packageName + File.separator + uiClassName).replace('.', File.separatorChar) + ".class";
            System.out.println("...Creating forwarding delegate");
            System.out.println("...... at '" + resource + "'");
            System.out.println("...... with class name '" + uiClassName + "'");
            System.out.println("...... package '" + packageName + "'");
            System.out.println("...... super impl '" + uiSuperClassName + "'");
            b2 = null;
            b2 = uiSuperCtrParams.length == 0 ? UiDelegateWriterEmptyCtr.createClass(packageName, uiClassName, uiSuperClassName) : UiDelegateWriterOneParamCtr.createClass(packageName, uiClassName, uiSuperClassName, Utils.getTypeDesc(uiSuperCtrParams[0]));
            fos = null;
            try {
                fos = new FileOutputStream(resource);
                fos.write(b2);
                if (this.isVerbose) {
                    System.out.println("...Created resource " + resource);
                }
                ** if (fos == null) goto lbl-1000
            }
            catch (Exception e2) {
                if (fos != null) {
                    try {
                        fos.close();
                    }
                    catch (IOException ioe) {}
                }
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
lbl-1000:
            // 1 sources

            {
                try {
                    fos.close();
                }
                catch (IOException ioe) {}
            }
lbl-1000:
            // 2 sources

            {
            }
            ++i3;
        }
    }

    public void setVerbose(boolean isVerbose) {
        this.isVerbose = isVerbose;
    }

    public void setDelegatesToAdd(String[] delegatesToAdd) {
        this.delegatesToAdd = delegatesToAdd;
    }

    public static void main(String[] args) throws AugmentException {
        if (args.length == 0) {
            System.out.println("Usage : java ... LafMainClassAugmenter [-verbose]");
            System.out.println("\t -main main_class_name -dir class_directory ");
            System.out.println("\t -delegates delegate_ui_ids");
            return;
        }
        LafMainClassAugmenter augmenter = new LafMainClassAugmenter();
        int argNum = 0;
        String mainLafClassName = null;
        String startDir = null;
        while (argNum < args.length) {
            String currArg = args[argNum];
            if ("-verbose".equals(currArg)) {
                augmenter.setVerbose(true);
                ++argNum;
                continue;
            }
            if ("-main".equals(currArg)) {
                mainLafClassName = args[++argNum];
                ++argNum;
                continue;
            }
            if ("-dir".equals(currArg)) {
                startDir = args[++argNum];
                ++argNum;
                continue;
            }
            if (!"-delegates".equals(currArg)) break;
            augmenter.setDelegatesToAdd(args[++argNum].split(";"));
            ++argNum;
        }
        File starter = new File(startDir);
        augmenter.process(starter.getAbsolutePath(), starter, mainLafClassName);
    }

    protected class AugmentClassAdapter
    extends ClassAdapter
    implements Opcodes {
        private Set<String> existingMethods;
        private String prefix;
        private String superClassName;

        public AugmentClassAdapter(ClassVisitor cv, Set<String> existingMethods, Method originalMethod) {
            super(cv);
            this.existingMethods = existingMethods;
        }

        public String getSuperClassName() {
            return this.superClassName;
        }

        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            this.superClassName = superName;
            this.prefix = "__" + name.replaceAll("/", "__") + "__";
            super.visit(version, access, name, signature, superName, interfaces);
            boolean hasOriginal = this.existingMethods.contains(METHOD_NAME);
            boolean hasDelegate = this.existingMethods.contains(this.prefix + METHOD_NAME);
            if (LafMainClassAugmenter.this.isVerbose) {
                System.out.println("..." + METHOD_NAME + " " + "(Ljavax/swing/UIDefaults;)V" + " : delegate - " + hasDelegate + ", 1 params");
            }
            if (!hasDelegate) {
                this.augmentInitClassDefaultsMethod(!hasOriginal, name, superName);
            }
        }

        public void augmentInitClassDefaultsMethod(boolean toSynthOriginal, String className, String superClassName) {
            MethodVisitor mv;
            if (toSynthOriginal) {
                if (LafMainClassAugmenter.this.isVerbose) {
                    System.out.println("... Creating empty 'initClassDefaults' forwarding to super '" + superClassName + "'");
                }
                mv = this.cv.visitMethod(4, this.prefix + "initClassDefaults", "(Ljavax/swing/UIDefaults;)V", null, null);
                mv.visitCode();
                mv.visitVarInsn(25, 0);
                mv.visitVarInsn(25, 1);
                mv.visitMethodInsn(183, superClassName, "initClassDefaults", "(Ljavax/swing/UIDefaults;)V");
                mv.visitInsn(177);
                mv.visitMaxs(2, 2);
                mv.visitEnd();
            }
            mv = this.cv.visitMethod(4, "initClassDefaults", "(Ljavax/swing/UIDefaults;)V", null, null);
            mv.visitCode();
            mv.visitVarInsn(25, 0);
            mv.visitVarInsn(25, 1);
            mv.visitMethodInsn(182, className, this.prefix + "initClassDefaults", "(Ljavax/swing/UIDefaults;)V");
            String packageName = className.replace('/', '.');
            int lastDotIndex = packageName.lastIndexOf(46);
            packageName = lastDotIndex >= 0 ? packageName.substring(0, lastDotIndex) : "";
            for (int i2 = 0; i2 < LafMainClassAugmenter.this.delegatesToAdd.length; ++i2) {
                mv.visitVarInsn(25, 1);
                String delegateKey = LafMainClassAugmenter.this.delegatesToAdd[i2];
                String delegateValue = packageName + ".__Forwarding__" + delegateKey;
                if (LafMainClassAugmenter.this.isVerbose) {
                    System.out.println("...Putting '" + delegateKey + "' -> '" + delegateValue + "'");
                }
                mv.visitLdcInsn(delegateKey);
                mv.visitLdcInsn(delegateValue);
                mv.visitMethodInsn(182, "javax/swing/UIDefaults", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
                mv.visitInsn(87);
            }
            mv.visitInsn(177);
            mv.visitMaxs(3, 2);
            mv.visitEnd();
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            if (METHOD_NAME.equals(name) && !this.existingMethods.contains(this.prefix + name)) {
                return this.cv.visitMethod(access, this.prefix + name, desc, signature, exceptions);
            }
            return this.cv.visitMethod(access, name, desc, signature, exceptions);
        }
    }
}

