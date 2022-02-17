/*
 * Decompiled with CFR 0.150.
 */
package javafx.fxml;

import com.sun.javafx.fxml.builder.JavaFXFontBuilder;
import com.sun.javafx.fxml.builder.JavaFXImageBuilder;
import com.sun.javafx.fxml.builder.JavaFXSceneBuilder;
import com.sun.javafx.fxml.builder.ProxyBuilder;
import com.sun.javafx.fxml.builder.TriangleMeshBuilder;
import com.sun.javafx.fxml.builder.URLBuilder;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilder;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.text.Font;
import javafx.util.Builder;
import javafx.util.BuilderFactory;
import sun.reflect.misc.ConstructorUtil;

public final class JavaFXBuilderFactory
implements BuilderFactory {
    private final JavaFXBuilder NO_BUILDER = new JavaFXBuilder();
    private final Map<Class<?>, JavaFXBuilder> builders = new HashMap();
    private final ClassLoader classLoader;
    private final boolean alwaysUseBuilders;
    private final boolean webSupported;

    public JavaFXBuilderFactory() {
        this(FXMLLoader.getDefaultClassLoader(), false);
    }

    public JavaFXBuilderFactory(boolean bl) {
        this(FXMLLoader.getDefaultClassLoader(), bl);
    }

    public JavaFXBuilderFactory(ClassLoader classLoader) {
        this(classLoader, false);
    }

    public JavaFXBuilderFactory(ClassLoader classLoader, boolean bl) {
        if (classLoader == null) {
            throw new NullPointerException();
        }
        this.classLoader = classLoader;
        this.alwaysUseBuilders = bl;
        this.webSupported = Platform.isSupported(ConditionalFeature.WEB);
    }

    @Override
    public Builder<?> getBuilder(Class<?> class_) {
        Builder<Scene> builder;
        if (class_ == Scene.class) {
            builder = new JavaFXSceneBuilder();
        } else if (class_ == Font.class) {
            builder = new JavaFXFontBuilder();
        } else if (class_ == Image.class) {
            builder = new JavaFXImageBuilder();
        } else if (class_ == URL.class) {
            builder = new URLBuilder(this.classLoader);
        } else if (class_ == TriangleMesh.class) {
            builder = new TriangleMeshBuilder();
        } else if (this.scanForConstructorAnnotations(class_)) {
            builder = new ProxyBuilder(class_);
        } else {
            Builder<Object> builder2 = null;
            JavaFXBuilder javaFXBuilder = this.builders.get(class_);
            if (javaFXBuilder != this.NO_BUILDER) {
                if (javaFXBuilder == null) {
                    boolean bl;
                    try {
                        ConstructorUtil.getConstructor(class_, new Class[0]);
                        if (this.alwaysUseBuilders) {
                            throw new Exception();
                        }
                        bl = true;
                    }
                    catch (Exception exception) {
                        bl = false;
                    }
                    if (!bl || this.webSupported && class_.getName().equals("javafx.scene.web.WebView")) {
                        try {
                            javaFXBuilder = this.createTypeBuilder(class_);
                        }
                        catch (ClassNotFoundException classNotFoundException) {
                            // empty catch block
                        }
                    }
                    this.builders.put(class_, javaFXBuilder == null ? this.NO_BUILDER : javaFXBuilder);
                }
                if (javaFXBuilder != null) {
                    builder2 = javaFXBuilder.createBuilder();
                }
            }
            builder = builder2;
        }
        return builder;
    }

    JavaFXBuilder createTypeBuilder(Class<?> class_) throws ClassNotFoundException {
        JavaFXBuilder javaFXBuilder = null;
        Class<?> class_2 = this.classLoader.loadClass(class_.getName() + "Builder");
        try {
            javaFXBuilder = new JavaFXBuilder(class_2);
        }
        catch (Exception exception) {
            Logger.getLogger(JavaFXBuilderFactory.class.getName()).log(Level.WARNING, "Failed to instantiate JavaFXBuilder for " + class_2, exception);
        }
        if (!this.alwaysUseBuilders) {
            Logger.getLogger(JavaFXBuilderFactory.class.getName()).log(Level.FINER, "class {0} requires a builder.", class_);
        }
        return javaFXBuilder;
    }

    private boolean scanForConstructorAnnotations(Class<?> class_) {
        Constructor<?>[] arrconstructor;
        for (Constructor<?> constructor : arrconstructor = ConstructorUtil.getConstructors(class_)) {
            Annotation[][] arrannotation = constructor.getParameterAnnotations();
            for (int i2 = 0; i2 < constructor.getParameterTypes().length; ++i2) {
                for (Annotation annotation : arrannotation[i2]) {
                    if (!(annotation instanceof NamedArg)) continue;
                    return true;
                }
            }
        }
        return false;
    }
}

