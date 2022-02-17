/*
 * Decompiled with CFR 0.150.
 */
package javafx.fxml;

import com.sun.javafx.beans.IDProperty;
import com.sun.javafx.fxml.BeanAdapter;
import com.sun.javafx.fxml.LoadListener;
import com.sun.javafx.fxml.ParseTraceElement;
import com.sun.javafx.fxml.PropertyNotFoundException;
import com.sun.javafx.fxml.expression.Expression;
import com.sun.javafx.fxml.expression.ExpressionValue;
import com.sun.javafx.fxml.expression.KeyPath;
import com.sun.javafx.util.Logging;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.AccessController;
import java.security.AllPermission;
import java.security.PrivilegedAction;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import javafx.beans.DefaultProperty;
import javafx.beans.InvalidationListener;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.fxml.LoadException;
import javafx.util.Builder;
import javafx.util.BuilderFactory;
import javafx.util.Callback;
import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;
import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;
import sun.reflect.misc.ConstructorUtil;
import sun.reflect.misc.MethodUtil;
import sun.reflect.misc.ReflectUtil;

public class FXMLLoader {
    private static final RuntimePermission GET_CLASSLOADER_PERMISSION = new RuntimePermission("getClassLoader");
    private URL location;
    private ResourceBundle resources;
    private ObservableMap<String, Object> namespace = FXCollections.observableHashMap();
    private Object root = null;
    private Object controller = null;
    private BuilderFactory builderFactory;
    private Callback<Class<?>, Object> controllerFactory;
    private Charset charset;
    private final LinkedList<FXMLLoader> loaders;
    private ClassLoader classLoader = null;
    private boolean staticLoad = false;
    private LoadListener loadListener = null;
    private FXMLLoader parentLoader;
    private XMLStreamReader xmlStreamReader = null;
    private Element current = null;
    private ScriptEngine scriptEngine = null;
    private List<String> packages = new LinkedList<String>();
    private Map<String, Class<?>> classes = new HashMap();
    private ScriptEngineManager scriptEngineManager = null;
    private static ClassLoader defaultClassLoader = null;
    private static final Pattern extraneousWhitespacePattern = Pattern.compile("\\s+");
    private static BuilderFactory DEFAULT_BUILDER_FACTORY = new JavaFXBuilderFactory();
    public static final String DEFAULT_CHARSET_NAME = "UTF-8";
    public static final String LANGUAGE_PROCESSING_INSTRUCTION = "language";
    public static final String IMPORT_PROCESSING_INSTRUCTION = "import";
    public static final String FX_NAMESPACE_PREFIX = "fx";
    public static final String FX_CONTROLLER_ATTRIBUTE = "controller";
    public static final String FX_ID_ATTRIBUTE = "id";
    public static final String FX_VALUE_ATTRIBUTE = "value";
    public static final String FX_CONSTANT_ATTRIBUTE = "constant";
    public static final String FX_FACTORY_ATTRIBUTE = "factory";
    public static final String INCLUDE_TAG = "include";
    public static final String INCLUDE_SOURCE_ATTRIBUTE = "source";
    public static final String INCLUDE_RESOURCES_ATTRIBUTE = "resources";
    public static final String INCLUDE_CHARSET_ATTRIBUTE = "charset";
    public static final String SCRIPT_TAG = "script";
    public static final String SCRIPT_SOURCE_ATTRIBUTE = "source";
    public static final String SCRIPT_CHARSET_ATTRIBUTE = "charset";
    public static final String DEFINE_TAG = "define";
    public static final String REFERENCE_TAG = "reference";
    public static final String REFERENCE_SOURCE_ATTRIBUTE = "source";
    public static final String ROOT_TAG = "root";
    public static final String ROOT_TYPE_ATTRIBUTE = "type";
    public static final String COPY_TAG = "copy";
    public static final String COPY_SOURCE_ATTRIBUTE = "source";
    public static final String EVENT_HANDLER_PREFIX = "on";
    public static final String EVENT_KEY = "event";
    public static final String CHANGE_EVENT_HANDLER_SUFFIX = "Change";
    private static final String COLLECTION_HANDLER_NAME = "onChange";
    public static final String NULL_KEYWORD = "null";
    public static final String ESCAPE_PREFIX = "\\";
    public static final String RELATIVE_PATH_PREFIX = "@";
    public static final String RESOURCE_KEY_PREFIX = "%";
    public static final String EXPRESSION_PREFIX = "$";
    public static final String BINDING_EXPRESSION_PREFIX = "${";
    public static final String BINDING_EXPRESSION_SUFFIX = "}";
    public static final String BI_DIRECTIONAL_BINDING_PREFIX = "#{";
    public static final String BI_DIRECTIONAL_BINDING_SUFFIX = "}";
    public static final String ARRAY_COMPONENT_DELIMITER = ",";
    public static final String LOCATION_KEY = "location";
    public static final String RESOURCES_KEY = "resources";
    public static final String CONTROLLER_METHOD_PREFIX = "#";
    public static final String CONTROLLER_KEYWORD = "controller";
    public static final String CONTROLLER_SUFFIX = "Controller";
    public static final String INITIALIZE_METHOD_NAME = "initialize";
    public static final String JAVAFX_VERSION = AccessController.doPrivileged(new PrivilegedAction<String>(){

        @Override
        public String run() {
            return System.getProperty("javafx.version");
        }
    });
    public static final String FX_NAMESPACE_VERSION = "1";
    private Class<?> callerClass;
    private final ControllerAccessor controllerAccessor = new ControllerAccessor();

    private void injectFields(String string, Object object) throws LoadException {
        List<Field> list;
        if (this.controller != null && string != null && (list = this.controllerAccessor.getControllerFields().get(string)) != null) {
            try {
                for (Field field : list) {
                    field.set(this.controller, object);
                }
            }
            catch (IllegalAccessException illegalAccessException) {
                throw this.constructLoadException(illegalAccessException);
            }
        }
    }

    public FXMLLoader() {
        this((URL)null);
    }

    public FXMLLoader(URL uRL) {
        this(uRL, null);
    }

    public FXMLLoader(URL uRL, ResourceBundle resourceBundle) {
        this(uRL, resourceBundle, null);
    }

    public FXMLLoader(URL uRL, ResourceBundle resourceBundle, BuilderFactory builderFactory) {
        this(uRL, resourceBundle, builderFactory, null);
    }

    public FXMLLoader(URL uRL, ResourceBundle resourceBundle, BuilderFactory builderFactory, Callback<Class<?>, Object> callback) {
        this(uRL, resourceBundle, builderFactory, callback, Charset.forName(DEFAULT_CHARSET_NAME));
    }

    public FXMLLoader(Charset charset) {
        this(null, null, null, null, charset);
    }

    public FXMLLoader(URL uRL, ResourceBundle resourceBundle, BuilderFactory builderFactory, Callback<Class<?>, Object> callback, Charset charset) {
        this(uRL, resourceBundle, builderFactory, callback, charset, new LinkedList<FXMLLoader>());
    }

    public FXMLLoader(URL uRL, ResourceBundle resourceBundle, BuilderFactory builderFactory, Callback<Class<?>, Object> callback, Charset charset, LinkedList<FXMLLoader> linkedList) {
        this.setLocation(uRL);
        this.setResources(resourceBundle);
        this.setBuilderFactory(builderFactory);
        this.setControllerFactory(callback);
        this.setCharset(charset);
        this.loaders = new LinkedList<FXMLLoader>(linkedList);
    }

    public URL getLocation() {
        return this.location;
    }

    public void setLocation(URL uRL) {
        this.location = uRL;
    }

    public ResourceBundle getResources() {
        return this.resources;
    }

    public void setResources(ResourceBundle resourceBundle) {
        this.resources = resourceBundle;
    }

    public ObservableMap<String, Object> getNamespace() {
        return this.namespace;
    }

    public <T> T getRoot() {
        return (T)this.root;
    }

    public void setRoot(Object object) {
        this.root = object;
    }

    public boolean equals(Object object) {
        if (object instanceof FXMLLoader) {
            FXMLLoader fXMLLoader = (FXMLLoader)object;
            if (this.location == null || fXMLLoader.location == null) {
                return fXMLLoader.location == this.location;
            }
            return this.location.toExternalForm().equals(fXMLLoader.location.toExternalForm());
        }
        return false;
    }

    private boolean isCyclic(FXMLLoader fXMLLoader, FXMLLoader fXMLLoader2) {
        if (fXMLLoader == null) {
            return false;
        }
        if (fXMLLoader.equals(fXMLLoader2)) {
            return true;
        }
        return this.isCyclic(fXMLLoader.parentLoader, fXMLLoader2);
    }

    public <T> T getController() {
        return (T)this.controller;
    }

    public void setController(Object object) {
        this.controller = object;
        if (object == null) {
            this.namespace.remove("controller");
        } else {
            this.namespace.put("controller", object);
        }
        this.controllerAccessor.setController(object);
    }

    public BuilderFactory getBuilderFactory() {
        return this.builderFactory;
    }

    public void setBuilderFactory(BuilderFactory builderFactory) {
        this.builderFactory = builderFactory;
    }

    public Callback<Class<?>, Object> getControllerFactory() {
        return this.controllerFactory;
    }

    public void setControllerFactory(Callback<Class<?>, Object> callback) {
        this.controllerFactory = callback;
    }

    public Charset getCharset() {
        return this.charset;
    }

    public void setCharset(Charset charset) {
        if (charset == null) {
            throw new NullPointerException("charset is null.");
        }
        this.charset = charset;
    }

    @CallerSensitive
    public ClassLoader getClassLoader() {
        if (this.classLoader == null) {
            SecurityManager securityManager = System.getSecurityManager();
            Class<?> class_ = securityManager != null ? Reflection.getCallerClass() : null;
            return FXMLLoader.getDefaultClassLoader(class_);
        }
        return this.classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        if (classLoader == null) {
            throw new IllegalArgumentException();
        }
        this.classLoader = classLoader;
        this.clearImports();
    }

    public boolean impl_isStaticLoad() {
        return this.staticLoad;
    }

    public void impl_setStaticLoad(boolean bl) {
        this.staticLoad = bl;
    }

    public LoadListener impl_getLoadListener() {
        return this.loadListener;
    }

    public void impl_setLoadListener(LoadListener loadListener) {
        this.loadListener = loadListener;
    }

    @CallerSensitive
    public <T> T load() throws IOException {
        return this.loadImpl(System.getSecurityManager() != null ? Reflection.getCallerClass() : null);
    }

    @CallerSensitive
    public <T> T load(InputStream inputStream) throws IOException {
        return this.loadImpl(inputStream, System.getSecurityManager() != null ? Reflection.getCallerClass() : null);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private <T> T loadImpl(Class<?> class_) throws IOException {
        T t2;
        if (this.location == null) {
            throw new IllegalStateException("Location is not set.");
        }
        try (InputStream inputStream = null;){
            inputStream = this.location.openStream();
            t2 = this.loadImpl(inputStream, class_);
        }
        return t2;
    }

    private <T> T loadImpl(InputStream inputStream, Class<?> class_) throws IOException {
        block23: {
            if (inputStream == null) {
                throw new NullPointerException("inputStream is null.");
            }
            this.callerClass = class_;
            this.controllerAccessor.setCallerClass(class_);
            try {
                Object object;
                this.clearImports();
                this.namespace.put(LOCATION_KEY, this.location);
                this.namespace.put("resources", this.resources);
                this.scriptEngine = null;
                try {
                    XMLInputFactory xMLInputFactory = XMLInputFactory.newInstance();
                    xMLInputFactory.setProperty("javax.xml.stream.isCoalescing", true);
                    object = new InputStreamReader(inputStream, this.charset);
                    this.xmlStreamReader = new StreamReaderDelegate(xMLInputFactory.createXMLStreamReader((Reader)object)){

                        @Override
                        public String getPrefix() {
                            String string = super.getPrefix();
                            if (string != null && string.length() == 0) {
                                string = null;
                            }
                            return string;
                        }

                        @Override
                        public String getAttributePrefix(int n2) {
                            String string = super.getAttributePrefix(n2);
                            if (string != null && string.length() == 0) {
                                string = null;
                            }
                            return string;
                        }
                    };
                }
                catch (XMLStreamException xMLStreamException) {
                    throw this.constructLoadException(xMLStreamException);
                }
                this.loaders.push(this);
                try {
                    while (this.xmlStreamReader.hasNext()) {
                        int n2 = this.xmlStreamReader.next();
                        switch (n2) {
                            case 3: {
                                this.processProcessingInstruction();
                                break;
                            }
                            case 5: {
                                this.processComment();
                                break;
                            }
                            case 1: {
                                this.processStartElement();
                                break;
                            }
                            case 2: {
                                this.processEndElement();
                                break;
                            }
                            case 4: {
                                this.processCharacters();
                            }
                        }
                    }
                }
                catch (XMLStreamException xMLStreamException) {
                    throw this.constructLoadException(xMLStreamException);
                }
                if (this.controller == null) break block23;
                if (this.controller instanceof Initializable) {
                    ((Initializable)this.controller).initialize(this.location, this.resources);
                    break block23;
                }
                Map<String, List<Field>> map = this.controllerAccessor.getControllerFields();
                this.injectFields(LOCATION_KEY, this.location);
                this.injectFields("resources", this.resources);
                object = this.controllerAccessor.getControllerMethods().get((Object)SupportedType.PARAMETERLESS).get(INITIALIZE_METHOD_NAME);
                if (object == null) break block23;
                try {
                    MethodUtil.invoke((Method)object, this.controller, new Object[0]);
                }
                catch (IllegalAccessException illegalAccessException) {
                }
                catch (InvocationTargetException invocationTargetException) {
                    throw this.constructLoadException(invocationTargetException);
                }
            }
            catch (LoadException loadException) {
                throw loadException;
            }
            catch (Exception exception) {
                throw this.constructLoadException(exception);
            }
            finally {
                this.controllerAccessor.setCallerClass(null);
                this.controllerAccessor.reset();
                this.xmlStreamReader = null;
            }
        }
        return (T)this.root;
    }

    private void clearImports() {
        this.packages.clear();
        this.classes.clear();
    }

    private LoadException constructLoadException(String string) {
        return new LoadException(string + this.constructFXMLTrace());
    }

    private LoadException constructLoadException(Throwable throwable) {
        return new LoadException(this.constructFXMLTrace(), throwable);
    }

    private LoadException constructLoadException(String string, Throwable throwable) {
        return new LoadException(string + this.constructFXMLTrace(), throwable);
    }

    private String constructFXMLTrace() {
        StringBuilder stringBuilder = new StringBuilder("\n");
        for (FXMLLoader fXMLLoader : this.loaders) {
            stringBuilder.append(fXMLLoader.location != null ? fXMLLoader.location.getPath() : "unknown path");
            if (fXMLLoader.current != null) {
                stringBuilder.append(":");
                stringBuilder.append(fXMLLoader.impl_getLineNumber());
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    public int impl_getLineNumber() {
        return this.xmlStreamReader.getLocation().getLineNumber();
    }

    public ParseTraceElement[] impl_getParseTrace() {
        ParseTraceElement[] arrparseTraceElement = new ParseTraceElement[this.loaders.size()];
        int n2 = 0;
        for (FXMLLoader fXMLLoader : this.loaders) {
            arrparseTraceElement[n2++] = new ParseTraceElement(fXMLLoader.location, fXMLLoader.current != null ? fXMLLoader.impl_getLineNumber() : -1);
        }
        return arrparseTraceElement;
    }

    private void processProcessingInstruction() throws LoadException {
        String string = this.xmlStreamReader.getPITarget().trim();
        if (string.equals(LANGUAGE_PROCESSING_INSTRUCTION)) {
            this.processLanguage();
        } else if (string.equals(IMPORT_PROCESSING_INSTRUCTION)) {
            this.processImport();
        }
    }

    private void processLanguage() throws LoadException {
        if (this.scriptEngine != null) {
            throw this.constructLoadException("Page language already set.");
        }
        String string = this.xmlStreamReader.getPIData();
        if (this.loadListener != null) {
            this.loadListener.readLanguageProcessingInstruction(string);
        }
        if (!this.staticLoad) {
            ScriptEngineManager scriptEngineManager = this.getScriptEngineManager();
            this.scriptEngine = scriptEngineManager.getEngineByName(string);
        }
    }

    private void processImport() throws LoadException {
        String string = this.xmlStreamReader.getPIData().trim();
        if (this.loadListener != null) {
            this.loadListener.readImportProcessingInstruction(string);
        }
        if (string.endsWith(".*")) {
            this.importPackage(string.substring(0, string.length() - 2));
        } else {
            this.importClass(string);
        }
    }

    private void processComment() throws LoadException {
        if (this.loadListener != null) {
            this.loadListener.readComment(this.xmlStreamReader.getText());
        }
    }

    private void processStartElement() throws IOException {
        this.createElement();
        this.current.processStartElement();
        if (this.root == null) {
            this.root = this.current.value;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private void createElement() throws IOException {
        String string = this.xmlStreamReader.getPrefix();
        String string2 = this.xmlStreamReader.getLocalName();
        if (string == null) {
            int n2 = string2.lastIndexOf(46);
            if (Character.isLowerCase(string2.charAt(n2 + 1))) {
                String string3 = string2.substring(n2 + 1);
                if (n2 == -1) {
                    if (this.loadListener != null) {
                        this.loadListener.beginPropertyElement(string3, null);
                    }
                    this.current = new PropertyElement(string3, null);
                    return;
                } else {
                    Class<?> class_ = this.getType(string2.substring(0, n2));
                    if (class_ != null) {
                        if (this.loadListener != null) {
                            this.loadListener.beginPropertyElement(string3, class_);
                        }
                        this.current = new PropertyElement(string3, class_);
                        return;
                    } else {
                        if (!this.staticLoad) throw this.constructLoadException(string2 + " is not a valid property.");
                        if (this.loadListener != null) {
                            this.loadListener.beginUnknownStaticPropertyElement(string2);
                        }
                        this.current = new UnknownStaticPropertyElement();
                    }
                }
                return;
            } else {
                if (this.current == null && this.root != null) {
                    throw this.constructLoadException("Root value already specified.");
                }
                Class<?> class_ = this.getType(string2);
                if (class_ != null) {
                    if (this.loadListener != null) {
                        this.loadListener.beginInstanceDeclarationElement(class_);
                    }
                    this.current = new InstanceDeclarationElement(class_);
                    return;
                } else {
                    if (!this.staticLoad) throw this.constructLoadException(string2 + " is not a valid type.");
                    if (this.loadListener != null) {
                        this.loadListener.beginUnknownTypeElement(string2);
                    }
                    this.current = new UnknownTypeElement();
                }
            }
            return;
        } else {
            if (!string.equals(FX_NAMESPACE_PREFIX)) throw this.constructLoadException("Unexpected namespace prefix: " + string + ".");
            if (string2.equals(INCLUDE_TAG)) {
                if (this.loadListener != null) {
                    this.loadListener.beginIncludeElement();
                }
                this.current = new IncludeElement();
                return;
            } else if (string2.equals(REFERENCE_TAG)) {
                if (this.loadListener != null) {
                    this.loadListener.beginReferenceElement();
                }
                this.current = new ReferenceElement();
                return;
            } else if (string2.equals(COPY_TAG)) {
                if (this.loadListener != null) {
                    this.loadListener.beginCopyElement();
                }
                this.current = new CopyElement();
                return;
            } else if (string2.equals(ROOT_TAG)) {
                if (this.loadListener != null) {
                    this.loadListener.beginRootElement();
                }
                this.current = new RootElement();
                return;
            } else if (string2.equals(SCRIPT_TAG)) {
                if (this.loadListener != null) {
                    this.loadListener.beginScriptElement();
                }
                this.current = new ScriptElement();
                return;
            } else {
                if (!string2.equals(DEFINE_TAG)) throw this.constructLoadException(string + ":" + string2 + " is not a valid element.");
                if (this.loadListener != null) {
                    this.loadListener.beginDefineElement();
                }
                this.current = new DefineElement();
            }
        }
    }

    private void processEndElement() throws IOException {
        this.current.processEndElement();
        if (this.loadListener != null) {
            this.loadListener.endElement(this.current.value);
        }
        this.current = this.current.parent;
    }

    private void processCharacters() throws IOException {
        if (!this.xmlStreamReader.isWhiteSpace()) {
            this.current.processCharacters();
        }
    }

    private void importPackage(String string) throws LoadException {
        this.packages.add(string);
    }

    private void importClass(String string) throws LoadException {
        try {
            this.loadType(string, true);
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw this.constructLoadException(classNotFoundException);
        }
    }

    private Class<?> getType(String string) throws LoadException {
        Class<?> class_ = null;
        if (Character.isLowerCase(string.charAt(0))) {
            try {
                class_ = this.loadType(string, false);
            }
            catch (ClassNotFoundException classNotFoundException) {}
        } else {
            class_ = this.classes.get(string);
            if (class_ == null) {
                for (String string2 : this.packages) {
                    try {
                        class_ = this.loadTypeForPackage(string2, string);
                    }
                    catch (ClassNotFoundException classNotFoundException) {
                        // empty catch block
                    }
                    if (class_ == null) continue;
                    break;
                }
                if (class_ != null) {
                    this.classes.put(string, class_);
                }
            }
        }
        return class_;
    }

    private Class<?> loadType(String string, boolean bl) throws ClassNotFoundException {
        int n2 = string.indexOf(46);
        int n3 = string.length();
        while (n2 != -1 && n2 < n3 && Character.isLowerCase(string.charAt(n2 + 1))) {
            n2 = string.indexOf(46, n2 + 1);
        }
        if (n2 == -1 || n2 == n3) {
            throw new ClassNotFoundException();
        }
        String string2 = string.substring(0, n2);
        String string3 = string.substring(n2 + 1);
        Class<?> class_ = this.loadTypeForPackage(string2, string3);
        if (bl) {
            this.classes.put(string3, class_);
        }
        return class_;
    }

    private Class<?> loadTypeForPackage(String string, String string2) throws ClassNotFoundException {
        return this.getClassLoader().loadClass(string + "." + string2.replace('.', '$'));
    }

    private static SupportedType toSupportedType(Method method) {
        for (SupportedType supportedType : SupportedType.values()) {
            if (!supportedType.methodIsOfType(method)) continue;
            return supportedType;
        }
        return null;
    }

    private ScriptEngineManager getScriptEngineManager() {
        if (this.scriptEngineManager == null) {
            this.scriptEngineManager = new ScriptEngineManager();
            this.scriptEngineManager.setBindings(new SimpleBindings(this.namespace));
        }
        return this.scriptEngineManager;
    }

    public static Class<?> loadType(String string, String string2) throws ClassNotFoundException {
        return FXMLLoader.loadType(string + "." + string2.replace('.', '$'));
    }

    public static Class<?> loadType(String string) throws ClassNotFoundException {
        ReflectUtil.checkPackageAccess(string);
        return Class.forName(string, true, FXMLLoader.getDefaultClassLoader());
    }

    private static boolean needsClassLoaderPermissionCheck(ClassLoader classLoader, ClassLoader classLoader2) {
        if (classLoader == classLoader2) {
            return false;
        }
        if (classLoader == null) {
            return false;
        }
        if (classLoader2 == null) {
            return true;
        }
        ClassLoader classLoader3 = classLoader2;
        do {
            if (classLoader != (classLoader3 = classLoader3.getParent())) continue;
            return false;
        } while (classLoader3 != null);
        return true;
    }

    private static ClassLoader getDefaultClassLoader(Class class_) {
        if (defaultClassLoader == null) {
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                ClassLoader classLoader;
                ClassLoader classLoader2 = classLoader = class_ != null ? class_.getClassLoader() : null;
                if (FXMLLoader.needsClassLoaderPermissionCheck(classLoader, FXMLLoader.class.getClassLoader())) {
                    securityManager.checkPermission(GET_CLASSLOADER_PERMISSION);
                }
            }
            return Thread.currentThread().getContextClassLoader();
        }
        return defaultClassLoader;
    }

    @CallerSensitive
    public static ClassLoader getDefaultClassLoader() {
        SecurityManager securityManager = System.getSecurityManager();
        Class<?> class_ = securityManager != null ? Reflection.getCallerClass() : null;
        return FXMLLoader.getDefaultClassLoader(class_);
    }

    public static void setDefaultClassLoader(ClassLoader classLoader) {
        if (classLoader == null) {
            throw new NullPointerException();
        }
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new AllPermission());
        }
        defaultClassLoader = classLoader;
    }

    @CallerSensitive
    public static <T> T load(URL uRL) throws IOException {
        return FXMLLoader.loadImpl(uRL, System.getSecurityManager() != null ? Reflection.getCallerClass() : null);
    }

    private static <T> T loadImpl(URL uRL, Class<?> class_) throws IOException {
        return FXMLLoader.loadImpl(uRL, null, class_);
    }

    @CallerSensitive
    public static <T> T load(URL uRL, ResourceBundle resourceBundle) throws IOException {
        return FXMLLoader.loadImpl(uRL, resourceBundle, System.getSecurityManager() != null ? Reflection.getCallerClass() : null);
    }

    private static <T> T loadImpl(URL uRL, ResourceBundle resourceBundle, Class<?> class_) throws IOException {
        return FXMLLoader.loadImpl(uRL, resourceBundle, null, class_);
    }

    @CallerSensitive
    public static <T> T load(URL uRL, ResourceBundle resourceBundle, BuilderFactory builderFactory) throws IOException {
        return FXMLLoader.loadImpl(uRL, resourceBundle, builderFactory, System.getSecurityManager() != null ? Reflection.getCallerClass() : null);
    }

    private static <T> T loadImpl(URL uRL, ResourceBundle resourceBundle, BuilderFactory builderFactory, Class<?> class_) throws IOException {
        return FXMLLoader.loadImpl(uRL, resourceBundle, builderFactory, null, class_);
    }

    @CallerSensitive
    public static <T> T load(URL uRL, ResourceBundle resourceBundle, BuilderFactory builderFactory, Callback<Class<?>, Object> callback) throws IOException {
        return FXMLLoader.loadImpl(uRL, resourceBundle, builderFactory, callback, System.getSecurityManager() != null ? Reflection.getCallerClass() : null);
    }

    private static <T> T loadImpl(URL uRL, ResourceBundle resourceBundle, BuilderFactory builderFactory, Callback<Class<?>, Object> callback, Class<?> class_) throws IOException {
        return FXMLLoader.loadImpl(uRL, resourceBundle, builderFactory, callback, Charset.forName(DEFAULT_CHARSET_NAME), class_);
    }

    @CallerSensitive
    public static <T> T load(URL uRL, ResourceBundle resourceBundle, BuilderFactory builderFactory, Callback<Class<?>, Object> callback, Charset charset) throws IOException {
        return FXMLLoader.loadImpl(uRL, resourceBundle, builderFactory, callback, charset, System.getSecurityManager() != null ? Reflection.getCallerClass() : null);
    }

    private static <T> T loadImpl(URL uRL, ResourceBundle resourceBundle, BuilderFactory builderFactory, Callback<Class<?>, Object> callback, Charset charset, Class<?> class_) throws IOException {
        if (uRL == null) {
            throw new NullPointerException("Location is required.");
        }
        FXMLLoader fXMLLoader = new FXMLLoader(uRL, resourceBundle, builderFactory, callback, charset);
        return fXMLLoader.loadImpl(class_);
    }

    static int compareJFXVersions(String string, String string2) {
        int n2;
        int n3 = 0;
        if (string == null || "".equals(string) || string2 == null || "".equals(string2)) {
            return n3;
        }
        if (string.equals(string2)) {
            return n3;
        }
        int n4 = string.indexOf("-");
        if (n4 > 0) {
            string = string.substring(0, n4);
        }
        if ((n2 = string.indexOf("_")) > 0) {
            string = string.substring(0, n2);
        }
        if (!Pattern.matches("^(\\d+)(\\.\\d+)*$", string) || !Pattern.matches("^(\\d+)(\\.\\d+)*$", string2)) {
            return n3;
        }
        StringTokenizer stringTokenizer = new StringTokenizer(string2, ".");
        StringTokenizer stringTokenizer2 = new StringTokenizer(string, ".");
        int n5 = 0;
        int n6 = 0;
        boolean bl = false;
        while (stringTokenizer.hasMoreTokens() && n3 == 0) {
            n5 = Integer.parseInt(stringTokenizer.nextToken());
            if (stringTokenizer2.hasMoreTokens()) {
                n6 = Integer.parseInt(stringTokenizer2.nextToken());
                n3 = n6 - n5;
                continue;
            }
            bl = true;
            break;
        }
        if (stringTokenizer2.hasMoreTokens() && n3 == 0 && (n6 = Integer.parseInt(stringTokenizer2.nextToken())) > 0) {
            n3 = 1;
        }
        if (bl) {
            if (n5 > 0) {
                n3 = -1;
            } else {
                while (stringTokenizer.hasMoreTokens()) {
                    n5 = Integer.parseInt(stringTokenizer.nextToken());
                    if (n5 <= 0) continue;
                    n3 = -1;
                    break;
                }
            }
        }
        return n3;
    }

    private static void checkAllPermissions() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new AllPermission());
        }
    }

    private static final class ControllerAccessor {
        private static final int PUBLIC = 1;
        private static final int PROTECTED = 2;
        private static final int PACKAGE = 4;
        private static final int PRIVATE = 8;
        private static final int INITIAL_CLASS_ACCESS = 15;
        private static final int INITIAL_MEMBER_ACCESS = 15;
        private static final int METHODS = 0;
        private static final int FIELDS = 1;
        private Object controller;
        private ClassLoader callerClassLoader;
        private Map<String, List<Field>> controllerFields;
        private Map<SupportedType, Map<String, Method>> controllerMethods;

        private ControllerAccessor() {
        }

        void setController(Object object) {
            if (this.controller != object) {
                this.controller = object;
                this.reset();
            }
        }

        void setCallerClass(Class<?> class_) {
            ClassLoader classLoader;
            ClassLoader classLoader2 = classLoader = class_ != null ? class_.getClassLoader() : null;
            if (this.callerClassLoader != classLoader) {
                this.callerClassLoader = classLoader;
                this.reset();
            }
        }

        void reset() {
            this.controllerFields = null;
            this.controllerMethods = null;
        }

        Map<String, List<Field>> getControllerFields() {
            if (this.controllerFields == null) {
                this.controllerFields = new HashMap<String, List<Field>>();
                if (this.callerClassLoader == null) {
                    FXMLLoader.checkAllPermissions();
                }
                this.addAccessibleMembers(this.controller.getClass(), 15, 15, 1);
            }
            return this.controllerFields;
        }

        Map<SupportedType, Map<String, Method>> getControllerMethods() {
            if (this.controllerMethods == null) {
                this.controllerMethods = new EnumMap<SupportedType, Map<String, Method>>(SupportedType.class);
                for (SupportedType supportedType : SupportedType.values()) {
                    this.controllerMethods.put(supportedType, new HashMap());
                }
                if (this.callerClassLoader == null) {
                    FXMLLoader.checkAllPermissions();
                }
                this.addAccessibleMembers(this.controller.getClass(), 15, 15, 0);
            }
            return this.controllerMethods;
        }

        private void addAccessibleMembers(final Class<?> class_, int n2, int n3, final int n4) {
            int n5;
            if (class_ == Object.class) {
                return;
            }
            int n6 = n2;
            int n7 = n3;
            if (this.callerClassLoader != null && class_.getClassLoader() != this.callerClassLoader) {
                n6 &= 1;
                n7 &= 1;
            }
            if (((n5 = ControllerAccessor.getAccess(class_.getModifiers())) & n6) == 0) {
                return;
            }
            ReflectUtil.checkPackageAccess(class_);
            this.addAccessibleMembers(class_.getSuperclass(), n6, n7, n4);
            final int n8 = n7;
            AccessController.doPrivileged(new PrivilegedAction<Void>(){

                @Override
                public Void run() {
                    if (n4 == 1) {
                        this.addAccessibleFields(class_, n8);
                    } else {
                        this.addAccessibleMethods(class_, n8);
                    }
                    return null;
                }
            });
        }

        private void addAccessibleFields(Class<?> class_, int n2) {
            boolean bl = Modifier.isPublic(class_.getModifiers());
            Field[] arrfield = class_.getDeclaredFields();
            for (int i2 = 0; i2 < arrfield.length; ++i2) {
                List<Field> list;
                Field field = arrfield[i2];
                int n3 = field.getModifiers();
                if ((n3 & 0x18) != 0 || (ControllerAccessor.getAccess(n3) & n2) == 0) continue;
                if (!bl || !Modifier.isPublic(n3)) {
                    if (field.getAnnotation(FXML.class) == null) continue;
                    field.setAccessible(true);
                }
                if ((list = this.controllerFields.get(field.getName())) == null) {
                    list = new ArrayList<Field>(1);
                    this.controllerFields.put(field.getName(), list);
                }
                list.add(field);
            }
        }

        private void addAccessibleMethods(Class<?> class_, int n2) {
            boolean bl = Modifier.isPublic(class_.getModifiers());
            Method[] arrmethod = class_.getDeclaredMethods();
            for (int i2 = 0; i2 < arrmethod.length; ++i2) {
                Method method = arrmethod[i2];
                int n3 = method.getModifiers();
                if ((n3 & 0x108) != 0 || (ControllerAccessor.getAccess(n3) & n2) == 0) continue;
                if (!bl || !Modifier.isPublic(n3)) {
                    if (method.getAnnotation(FXML.class) == null) continue;
                    method.setAccessible(true);
                }
                String string = method.getName();
                SupportedType supportedType = FXMLLoader.toSupportedType(method);
                if (supportedType == null) continue;
                this.controllerMethods.get((Object)supportedType).put(string, method);
            }
        }

        private static int getAccess(int n2) {
            int n3 = n2 & 7;
            switch (n3) {
                case 1: {
                    return 1;
                }
                case 4: {
                    return 2;
                }
                case 2: {
                    return 8;
                }
            }
            return 4;
        }
    }

    private static enum SupportedType {
        PARAMETERLESS{

            @Override
            protected boolean methodIsOfType(Method method) {
                return method.getParameterTypes().length == 0;
            }
        }
        ,
        EVENT{

            @Override
            protected boolean methodIsOfType(Method method) {
                return method.getParameterTypes().length == 1 && Event.class.isAssignableFrom(method.getParameterTypes()[0]);
            }
        }
        ,
        LIST_CHANGE_LISTENER{

            @Override
            protected boolean methodIsOfType(Method method) {
                return method.getParameterTypes().length == 1 && method.getParameterTypes()[0].equals(ListChangeListener.Change.class);
            }
        }
        ,
        MAP_CHANGE_LISTENER{

            @Override
            protected boolean methodIsOfType(Method method) {
                return method.getParameterTypes().length == 1 && method.getParameterTypes()[0].equals(MapChangeListener.Change.class);
            }
        }
        ,
        SET_CHANGE_LISTENER{

            @Override
            protected boolean methodIsOfType(Method method) {
                return method.getParameterTypes().length == 1 && method.getParameterTypes()[0].equals(SetChangeListener.Change.class);
            }
        }
        ,
        PROPERTY_CHANGE_LISTENER{

            @Override
            protected boolean methodIsOfType(Method method) {
                return method.getParameterTypes().length == 3 && ObservableValue.class.isAssignableFrom(method.getParameterTypes()[0]) && method.getParameterTypes()[1].equals(method.getParameterTypes()[2]);
            }
        };


        protected abstract boolean methodIsOfType(Method var1);
    }

    private static class MethodHandler {
        private final Object controller;
        private final Method method;
        private final SupportedType type;

        private MethodHandler(Object object, Method method, SupportedType supportedType) {
            this.method = method;
            this.controller = object;
            this.type = supportedType;
        }

        public void invoke(Object ... arrobject) {
            try {
                if (this.type != SupportedType.PARAMETERLESS) {
                    MethodUtil.invoke(this.method, this.controller, arrobject);
                } else {
                    MethodUtil.invoke(this.method, this.controller, new Object[0]);
                }
            }
            catch (InvocationTargetException invocationTargetException) {
                throw new RuntimeException(invocationTargetException);
            }
            catch (IllegalAccessException illegalAccessException) {
                throw new RuntimeException(illegalAccessException);
            }
        }
    }

    private static class PropertyChangeAdapter
    implements ChangeListener<Object> {
        public final MethodHandler handler;

        public PropertyChangeAdapter(MethodHandler methodHandler) {
            this.handler = methodHandler;
        }

        @Override
        public void changed(ObservableValue<? extends Object> observableValue, Object object, Object object2) {
            this.handler.invoke(observableValue, object, object2);
        }
    }

    private static class ObservableSetChangeAdapter
    implements SetChangeListener {
        public final MethodHandler handler;

        public ObservableSetChangeAdapter(MethodHandler methodHandler) {
            this.handler = methodHandler;
        }

        public void onChanged(SetChangeListener.Change change) {
            if (this.handler != null) {
                this.handler.invoke(change);
            }
        }
    }

    private static class ObservableMapChangeAdapter
    implements MapChangeListener {
        public final MethodHandler handler;

        public ObservableMapChangeAdapter(MethodHandler methodHandler) {
            this.handler = methodHandler;
        }

        public void onChanged(MapChangeListener.Change change) {
            if (this.handler != null) {
                this.handler.invoke(change);
            }
        }
    }

    private static class ObservableListChangeAdapter
    implements ListChangeListener {
        private final MethodHandler handler;

        public ObservableListChangeAdapter(MethodHandler methodHandler) {
            this.handler = methodHandler;
        }

        public void onChanged(ListChangeListener.Change change) {
            if (this.handler != null) {
                this.handler.invoke(change);
            }
        }
    }

    private static class ScriptEventHandler
    implements EventHandler<Event> {
        public final String script;
        public final ScriptEngine scriptEngine;

        public ScriptEventHandler(String string, ScriptEngine scriptEngine) {
            this.script = string;
            this.scriptEngine = scriptEngine;
        }

        @Override
        public void handle(Event event) {
            Bindings bindings = this.scriptEngine.getBindings(100);
            Bindings bindings2 = this.scriptEngine.createBindings();
            bindings2.put(FXMLLoader.EVENT_KEY, (Object)event);
            bindings2.putAll(bindings);
            this.scriptEngine.setBindings(bindings2, 100);
            try {
                this.scriptEngine.eval(this.script);
            }
            catch (ScriptException scriptException) {
                throw new RuntimeException(scriptException);
            }
            this.scriptEngine.setBindings(bindings, 100);
        }
    }

    private static class ControllerMethodEventHandler<T extends Event>
    implements EventHandler<T> {
        private final MethodHandler handler;

        public ControllerMethodEventHandler(MethodHandler methodHandler) {
            this.handler = methodHandler;
        }

        @Override
        public void handle(T t2) {
            this.handler.invoke(t2);
        }
    }

    private static class Attribute {
        public final String name;
        public final Class<?> sourceType;
        public final String value;

        public Attribute(String string, Class<?> class_, String string2) {
            this.name = string;
            this.sourceType = class_;
            this.value = string2;
        }
    }

    private class DefineElement
    extends Element {
        private DefineElement() {
        }

        @Override
        public boolean isCollection() {
            return true;
        }

        @Override
        public void add(Object object) {
        }

        @Override
        public void processAttribute(String string, String string2, String string3) throws LoadException {
            throw FXMLLoader.this.constructLoadException("Element does not support attributes.");
        }
    }

    private class ScriptElement
    extends Element {
        public String source;
        public Charset charset;

        private ScriptElement() {
            this.source = null;
            this.charset = FXMLLoader.this.charset;
        }

        @Override
        public boolean isCollection() {
            return false;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void processStartElement() throws IOException {
            super.processStartElement();
            if (this.source != null && !FXMLLoader.this.staticLoad) {
                Object object;
                Object object2;
                ScriptEngine scriptEngine;
                int n2 = this.source.lastIndexOf(".");
                if (n2 == -1) {
                    throw FXMLLoader.this.constructLoadException("Cannot determine type of script \"" + this.source + "\".");
                }
                String string = this.source.substring(n2 + 1);
                ClassLoader classLoader = FXMLLoader.this.getClassLoader();
                if (FXMLLoader.this.scriptEngine != null && FXMLLoader.this.scriptEngine.getFactory().getExtensions().contains(string)) {
                    scriptEngine = FXMLLoader.this.scriptEngine;
                } else {
                    object2 = Thread.currentThread().getContextClassLoader();
                    try {
                        Thread.currentThread().setContextClassLoader(classLoader);
                        object = FXMLLoader.this.getScriptEngineManager();
                        scriptEngine = ((ScriptEngineManager)object).getEngineByExtension(string);
                    }
                    finally {
                        Thread.currentThread().setContextClassLoader((ClassLoader)object2);
                    }
                }
                if (scriptEngine == null) {
                    throw FXMLLoader.this.constructLoadException("Unable to locate scripting engine for extension " + string + ".");
                }
                try {
                    if (this.source.charAt(0) == '/') {
                        object2 = classLoader.getResource(this.source.substring(1));
                    } else {
                        if (FXMLLoader.this.location == null) {
                            throw FXMLLoader.this.constructLoadException("Base location is undefined.");
                        }
                        object2 = new URL(FXMLLoader.this.location, this.source);
                    }
                    object = null;
                    try {
                        object = new InputStreamReader(((URL)object2).openStream(), this.charset);
                        scriptEngine.eval((Reader)object);
                    }
                    catch (ScriptException scriptException) {
                        scriptException.printStackTrace();
                    }
                    finally {
                        if (object != null) {
                            ((InputStreamReader)object).close();
                        }
                    }
                }
                catch (IOException iOException) {
                    throw FXMLLoader.this.constructLoadException(iOException);
                }
            }
        }

        @Override
        public void processEndElement() throws IOException {
            super.processEndElement();
            if (this.value != null && !FXMLLoader.this.staticLoad) {
                try {
                    FXMLLoader.this.scriptEngine.eval((String)this.value);
                }
                catch (ScriptException scriptException) {
                    System.err.println(scriptException.getMessage());
                }
            }
        }

        @Override
        public void processCharacters() throws LoadException {
            if (this.source != null) {
                throw FXMLLoader.this.constructLoadException("Script source already specified.");
            }
            if (FXMLLoader.this.scriptEngine == null && !FXMLLoader.this.staticLoad) {
                throw FXMLLoader.this.constructLoadException("Page language not specified.");
            }
            this.updateValue(FXMLLoader.this.xmlStreamReader.getText());
        }

        @Override
        public void processAttribute(String string, String string2, String string3) throws IOException {
            if (string == null && string2.equals("source")) {
                if (FXMLLoader.this.loadListener != null) {
                    FXMLLoader.this.loadListener.readInternalAttribute(string2, string3);
                }
                this.source = string3;
            } else if (string2.equals("charset")) {
                if (FXMLLoader.this.loadListener != null) {
                    FXMLLoader.this.loadListener.readInternalAttribute(string2, string3);
                }
                this.charset = Charset.forName(string3);
            } else {
                throw FXMLLoader.this.constructLoadException(string == null ? string2 : string + ":" + string2 + " is not a valid attribute.");
            }
        }
    }

    private class UnknownStaticPropertyElement
    extends Element {
        public UnknownStaticPropertyElement() throws LoadException {
            if (this.parent == null) {
                throw FXMLLoader.this.constructLoadException("Invalid root element.");
            }
            if (this.parent.value == null) {
                throw FXMLLoader.this.constructLoadException("Parent element does not support property elements.");
            }
        }

        @Override
        public boolean isCollection() {
            return false;
        }

        @Override
        public void set(Object object) {
            this.updateValue(object);
        }

        @Override
        public void processCharacters() throws IOException {
            String string = FXMLLoader.this.xmlStreamReader.getText();
            string = extraneousWhitespacePattern.matcher(string).replaceAll(" ");
            this.updateValue(string.trim());
        }
    }

    private class PropertyElement
    extends Element {
        public final String name;
        public final Class<?> sourceType;
        public final boolean readOnly;

        public PropertyElement(String string, Class<?> class_) throws LoadException {
            if (this.parent == null) {
                throw FXMLLoader.this.constructLoadException("Invalid root element.");
            }
            if (this.parent.value == null) {
                throw FXMLLoader.this.constructLoadException("Parent element does not support property elements.");
            }
            this.name = string;
            this.sourceType = class_;
            if (class_ == null) {
                if (string.startsWith(FXMLLoader.EVENT_HANDLER_PREFIX)) {
                    throw FXMLLoader.this.constructLoadException("\"" + string + "\" is not a valid element name.");
                }
                Map<String, Object> map = this.parent.getProperties();
                this.readOnly = this.parent.isTyped() ? this.parent.getValueAdapter().isReadOnly(string) : map.containsKey(string);
                if (this.readOnly) {
                    Object object = map.get(string);
                    if (object == null) {
                        throw FXMLLoader.this.constructLoadException("Invalid property.");
                    }
                    this.updateValue(object);
                }
            } else {
                this.readOnly = false;
            }
        }

        @Override
        public boolean isCollection() {
            return this.readOnly ? super.isCollection() : false;
        }

        @Override
        public void add(Object object) throws LoadException {
            if (this.parent.isTyped()) {
                Type type = this.parent.getValueAdapter().getGenericType(this.name);
                object = BeanAdapter.coerce(object, BeanAdapter.getListItemType(type));
            }
            super.add(object);
        }

        @Override
        public void set(Object object) throws LoadException {
            this.updateValue(object);
            if (this.sourceType == null) {
                this.parent.getProperties().put(this.name, object);
            } else if (this.parent.value instanceof Builder) {
                this.parent.staticPropertyElements.add(this);
            } else {
                BeanAdapter.put(this.parent.value, this.sourceType, this.name, object);
            }
        }

        @Override
        public void processAttribute(String string, String string2, String string3) throws IOException {
            if (!this.readOnly) {
                throw FXMLLoader.this.constructLoadException("Attributes are not supported for writable property elements.");
            }
            super.processAttribute(string, string2, string3);
        }

        @Override
        public void processEndElement() throws IOException {
            super.processEndElement();
            if (this.readOnly) {
                this.processInstancePropertyAttributes();
                this.processEventHandlerAttributes();
            }
        }

        @Override
        public void processCharacters() throws IOException {
            String string = FXMLLoader.this.xmlStreamReader.getText();
            string = extraneousWhitespacePattern.matcher(string).replaceAll(" ").trim();
            if (this.readOnly) {
                if (this.isCollection()) {
                    this.add(string);
                } else {
                    super.processCharacters();
                }
            } else {
                this.set(string);
            }
        }
    }

    private class RootElement
    extends ValueElement {
        public String type;

        private RootElement() {
            this.type = null;
        }

        @Override
        public void processAttribute(String string, String string2, String string3) throws IOException {
            if (string == null) {
                if (string2.equals(FXMLLoader.ROOT_TYPE_ATTRIBUTE)) {
                    if (FXMLLoader.this.loadListener != null) {
                        FXMLLoader.this.loadListener.readInternalAttribute(string2, string3);
                    }
                    this.type = string3;
                } else {
                    super.processAttribute(string, string2, string3);
                }
            } else {
                super.processAttribute(string, string2, string3);
            }
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        public Object constructValue() throws LoadException {
            Object object;
            if (this.type == null) {
                throw FXMLLoader.this.constructLoadException("type is required.");
            }
            Class class_ = FXMLLoader.this.getType(this.type);
            if (class_ == null) {
                throw FXMLLoader.this.constructLoadException(this.type + " is not a valid type.");
            }
            if (FXMLLoader.this.root == null) {
                if (!FXMLLoader.this.staticLoad) throw FXMLLoader.this.constructLoadException("Root hasn't been set. Use method setRoot() before load.");
                Object object2 = object = FXMLLoader.this.builderFactory == null ? null : FXMLLoader.this.builderFactory.getBuilder(class_);
                if (object == null) {
                    object = DEFAULT_BUILDER_FACTORY.getBuilder(class_);
                }
                if (object == null) {
                    try {
                        object = ReflectUtil.newInstance(class_);
                    }
                    catch (InstantiationException instantiationException) {
                        throw FXMLLoader.this.constructLoadException(instantiationException);
                    }
                    catch (IllegalAccessException illegalAccessException) {
                        throw FXMLLoader.this.constructLoadException(illegalAccessException);
                    }
                }
            } else {
                if (class_.isAssignableFrom(FXMLLoader.this.root.getClass())) return FXMLLoader.this.root;
                throw FXMLLoader.this.constructLoadException("Root is not an instance of " + class_.getName() + ".");
            }
            FXMLLoader.this.root = object;
            return object;
        }
    }

    private class CopyElement
    extends ValueElement {
        public String source;

        private CopyElement() {
            this.source = null;
        }

        @Override
        public void processAttribute(String string, String string2, String string3) throws IOException {
            if (string == null) {
                if (string2.equals("source")) {
                    if (FXMLLoader.this.loadListener != null) {
                        FXMLLoader.this.loadListener.readInternalAttribute(string2, string3);
                    }
                    this.source = string3;
                } else {
                    super.processAttribute(string, string2, string3);
                }
            } else {
                super.processAttribute(string, string2, string3);
            }
        }

        @Override
        public Object constructValue() throws LoadException {
            Object obj;
            if (this.source == null) {
                throw FXMLLoader.this.constructLoadException("source is required.");
            }
            KeyPath keyPath = KeyPath.parse(this.source);
            if (!Expression.isDefined((Object)FXMLLoader.this.namespace, keyPath)) {
                throw FXMLLoader.this.constructLoadException("Value \"" + this.source + "\" does not exist.");
            }
            Object t2 = Expression.get((Object)FXMLLoader.this.namespace, keyPath);
            Class<?> class_ = t2.getClass();
            Constructor<?> constructor = null;
            try {
                constructor = ConstructorUtil.getConstructor(class_, new Class[]{class_});
            }
            catch (NoSuchMethodException noSuchMethodException) {
                // empty catch block
            }
            if (constructor != null) {
                try {
                    ReflectUtil.checkPackageAccess(class_);
                    obj = constructor.newInstance(t2);
                }
                catch (InstantiationException instantiationException) {
                    throw FXMLLoader.this.constructLoadException(instantiationException);
                }
                catch (IllegalAccessException illegalAccessException) {
                    throw FXMLLoader.this.constructLoadException(illegalAccessException);
                }
                catch (InvocationTargetException invocationTargetException) {
                    throw FXMLLoader.this.constructLoadException(invocationTargetException);
                }
            } else {
                throw FXMLLoader.this.constructLoadException("Can't copy value " + t2 + ".");
            }
            return obj;
        }
    }

    private class ReferenceElement
    extends ValueElement {
        public String source;

        private ReferenceElement() {
            this.source = null;
        }

        @Override
        public void processAttribute(String string, String string2, String string3) throws IOException {
            if (string == null) {
                if (string2.equals("source")) {
                    if (FXMLLoader.this.loadListener != null) {
                        FXMLLoader.this.loadListener.readInternalAttribute(string2, string3);
                    }
                    this.source = string3;
                } else {
                    super.processAttribute(string, string2, string3);
                }
            } else {
                super.processAttribute(string, string2, string3);
            }
        }

        @Override
        public Object constructValue() throws LoadException {
            if (this.source == null) {
                throw FXMLLoader.this.constructLoadException("source is required.");
            }
            KeyPath keyPath = KeyPath.parse(this.source);
            if (!Expression.isDefined((Object)FXMLLoader.this.namespace, keyPath)) {
                throw FXMLLoader.this.constructLoadException("Value \"" + this.source + "\" does not exist.");
            }
            return Expression.get((Object)FXMLLoader.this.namespace, keyPath);
        }
    }

    private class IncludeElement
    extends ValueElement {
        public String source;
        public ResourceBundle resources;
        public Charset charset;

        private IncludeElement() {
            this.source = null;
            this.resources = FXMLLoader.this.resources;
            this.charset = FXMLLoader.this.charset;
        }

        @Override
        public void processAttribute(String string, String string2, String string3) throws IOException {
            if (string == null) {
                if (string2.equals("source")) {
                    if (FXMLLoader.this.loadListener != null) {
                        FXMLLoader.this.loadListener.readInternalAttribute(string2, string3);
                    }
                    this.source = string3;
                } else if (string2.equals("resources")) {
                    if (FXMLLoader.this.loadListener != null) {
                        FXMLLoader.this.loadListener.readInternalAttribute(string2, string3);
                    }
                    this.resources = ResourceBundle.getBundle(string3, Locale.getDefault(), FXMLLoader.this.resources.getClass().getClassLoader());
                } else if (string2.equals("charset")) {
                    if (FXMLLoader.this.loadListener != null) {
                        FXMLLoader.this.loadListener.readInternalAttribute(string2, string3);
                    }
                    this.charset = Charset.forName(string3);
                } else {
                    super.processAttribute(string, string2, string3);
                }
            } else {
                super.processAttribute(string, string2, string3);
            }
        }

        @Override
        public Object constructValue() throws IOException {
            URL uRL;
            if (this.source == null) {
                throw FXMLLoader.this.constructLoadException("source is required.");
            }
            ClassLoader classLoader = FXMLLoader.this.getClassLoader();
            if (this.source.charAt(0) == '/') {
                uRL = classLoader.getResource(this.source.substring(1));
                if (uRL == null) {
                    throw FXMLLoader.this.constructLoadException("Cannot resolve path: " + this.source);
                }
            } else {
                if (FXMLLoader.this.location == null) {
                    throw FXMLLoader.this.constructLoadException("Base location is undefined.");
                }
                uRL = new URL(FXMLLoader.this.location, this.source);
            }
            FXMLLoader fXMLLoader = new FXMLLoader(uRL, this.resources, FXMLLoader.this.builderFactory, FXMLLoader.this.controllerFactory, this.charset, FXMLLoader.this.loaders);
            fXMLLoader.parentLoader = FXMLLoader.this;
            if (FXMLLoader.this.isCyclic(FXMLLoader.this, fXMLLoader)) {
                throw new IOException(String.format("Including \"%s\" in \"%s\" created cyclic reference.", fXMLLoader.location.toExternalForm(), FXMLLoader.this.location.toExternalForm()));
            }
            fXMLLoader.setClassLoader(classLoader);
            fXMLLoader.impl_setStaticLoad(FXMLLoader.this.staticLoad);
            Object object = fXMLLoader.loadImpl(FXMLLoader.this.callerClass);
            if (this.fx_id != null) {
                String string = this.fx_id + FXMLLoader.CONTROLLER_SUFFIX;
                Object t2 = fXMLLoader.getController();
                FXMLLoader.this.namespace.put(string, t2);
                FXMLLoader.this.injectFields(string, t2);
            }
            return object;
        }
    }

    private class UnknownTypeElement
    extends ValueElement {
        private UnknownTypeElement() {
        }

        @Override
        public void processEndElement() throws IOException {
        }

        @Override
        public Object constructValue() throws LoadException {
            return new UnknownValueMap();
        }

        @DefaultProperty(value="items")
        public class UnknownValueMap
        extends AbstractMap<String, Object> {
            private ArrayList<?> items = new ArrayList();
            private HashMap<String, Object> values = new HashMap();

            @Override
            public Object get(Object object) {
                if (object == null) {
                    throw new NullPointerException();
                }
                return object.equals(this.getClass().getAnnotation(DefaultProperty.class).value()) ? this.items : this.values.get(object);
            }

            @Override
            public Object put(String string, Object object) {
                if (string == null) {
                    throw new NullPointerException();
                }
                if (string.equals(this.getClass().getAnnotation(DefaultProperty.class).value())) {
                    throw new IllegalArgumentException();
                }
                return this.values.put(string, object);
            }

            @Override
            public Set<Map.Entry<String, Object>> entrySet() {
                return Collections.emptySet();
            }
        }
    }

    private class InstanceDeclarationElement
    extends ValueElement {
        public Class<?> type;
        public String constant;
        public String factory;

        public InstanceDeclarationElement(Class<?> class_) throws LoadException {
            this.constant = null;
            this.factory = null;
            this.type = class_;
        }

        @Override
        public void processAttribute(String string, String string2, String string3) throws IOException {
            if (string != null && string.equals(FXMLLoader.FX_NAMESPACE_PREFIX)) {
                if (string2.equals(FXMLLoader.FX_VALUE_ATTRIBUTE)) {
                    this.value = string3;
                } else if (string2.equals(FXMLLoader.FX_CONSTANT_ATTRIBUTE)) {
                    this.constant = string3;
                } else if (string2.equals(FXMLLoader.FX_FACTORY_ATTRIBUTE)) {
                    this.factory = string3;
                } else {
                    super.processAttribute(string, string2, string3);
                }
            } else {
                super.processAttribute(string, string2, string3);
            }
        }

        @Override
        public Object constructValue() throws IOException {
            Object object;
            if (this.value != null) {
                object = BeanAdapter.coerce(this.value, this.type);
            } else if (this.constant != null) {
                object = BeanAdapter.getConstantValue(this.type, this.constant);
            } else {
                if (this.factory != null) {
                    Method method;
                    try {
                        method = MethodUtil.getMethod(this.type, this.factory, new Class[0]);
                    }
                    catch (NoSuchMethodException noSuchMethodException) {
                        throw FXMLLoader.this.constructLoadException(noSuchMethodException);
                    }
                    try {
                        object = MethodUtil.invoke(method, null, new Object[0]);
                    }
                    catch (IllegalAccessException illegalAccessException) {
                        throw FXMLLoader.this.constructLoadException(illegalAccessException);
                    }
                    catch (InvocationTargetException invocationTargetException) {
                        throw FXMLLoader.this.constructLoadException(invocationTargetException);
                    }
                }
                Object obj = object = FXMLLoader.this.builderFactory == null ? null : FXMLLoader.this.builderFactory.getBuilder(this.type);
                if (object == null) {
                    object = DEFAULT_BUILDER_FACTORY.getBuilder(this.type);
                }
                if (object == null) {
                    try {
                        object = ReflectUtil.newInstance(this.type);
                    }
                    catch (InstantiationException instantiationException) {
                        throw FXMLLoader.this.constructLoadException(instantiationException);
                    }
                    catch (IllegalAccessException illegalAccessException) {
                        throw FXMLLoader.this.constructLoadException(illegalAccessException);
                    }
                }
            }
            return object;
        }
    }

    private abstract class ValueElement
    extends Element {
        public String fx_id;

        private ValueElement() {
            this.fx_id = null;
        }

        @Override
        public void processStartElement() throws IOException {
            super.processStartElement();
            this.updateValue(this.constructValue());
            if (this.value instanceof Builder) {
                this.processInstancePropertyAttributes();
            } else {
                this.processValue();
            }
        }

        @Override
        public void processEndElement() throws IOException {
            super.processEndElement();
            if (this.value instanceof Builder) {
                Iterator iterator = (Builder)this.value;
                this.updateValue(iterator.build());
                this.processValue();
            } else {
                this.processInstancePropertyAttributes();
            }
            this.processEventHandlerAttributes();
            if (this.staticPropertyAttributes.size() > 0) {
                for (Object object : this.staticPropertyAttributes) {
                    this.processPropertyAttribute((Attribute)object);
                }
            }
            if (this.staticPropertyElements.size() > 0) {
                for (Object object : this.staticPropertyElements) {
                    BeanAdapter.put(this.value, ((PropertyElement)object).sourceType, ((PropertyElement)object).name, ((PropertyElement)object).value);
                }
            }
            if (this.parent != null) {
                if (this.parent.isCollection()) {
                    this.parent.add(this.value);
                } else {
                    this.parent.set(this.value);
                }
            }
        }

        private Object getListValue(Element element, String string, Object object) {
            Type type;
            if (element.isTyped() && (type = element.getValueAdapter().getGenericType(string)) != null) {
                Type type2 = BeanAdapter.getGenericListItemType(type);
                if (type2 instanceof ParameterizedType) {
                    type2 = ((ParameterizedType)type2).getRawType();
                }
                object = BeanAdapter.coerce(object, (Class)type2);
            }
            return object;
        }

        private void processValue() throws LoadException {
            Object object;
            Object object2;
            if (this.parent == null) {
                String string;
                FXMLLoader.this.root = this.value;
                object2 = FXMLLoader.this.xmlStreamReader.getNamespaceContext().getNamespaceURI(FXMLLoader.FX_NAMESPACE_PREFIX);
                if (object2 != null && FXMLLoader.compareJFXVersions(FXMLLoader.FX_NAMESPACE_VERSION, (String)(object = ((String)object2).substring(((String)object2).lastIndexOf("/") + 1))) < 0) {
                    throw FXMLLoader.this.constructLoadException("Loading FXML document of version " + (String)object + " by JavaFX runtime supporting version " + FXMLLoader.FX_NAMESPACE_VERSION);
                }
                object = FXMLLoader.this.xmlStreamReader.getNamespaceContext().getNamespaceURI("");
                if (object != null && FXMLLoader.compareJFXVersions(JAVAFX_VERSION, string = ((String)object).substring(((String)object).lastIndexOf("/") + 1)) < 0) {
                    Logging.getJavaFXLogger().warning("Loading FXML document with JavaFX API of version " + string + " by JavaFX runtime of version " + JAVAFX_VERSION);
                }
            }
            if (this.fx_id != null) {
                FXMLLoader.this.namespace.put(this.fx_id, this.value);
                object2 = this.value.getClass().getAnnotation(IDProperty.class);
                if (object2 != null && (object = this.getProperties()).get(object2.value()) == null) {
                    object.put(object2.value(), this.fx_id);
                }
                FXMLLoader.this.injectFields(this.fx_id, this.value);
            }
        }

        @Override
        public void processCharacters() throws LoadException {
            Class<?> class_ = this.value.getClass();
            DefaultProperty defaultProperty = class_.getAnnotation(DefaultProperty.class);
            if (defaultProperty != null) {
                String string = FXMLLoader.this.xmlStreamReader.getText();
                string = extraneousWhitespacePattern.matcher(string).replaceAll(" ");
                String string2 = defaultProperty.value();
                BeanAdapter beanAdapter = this.getValueAdapter();
                if (beanAdapter.isReadOnly(string2) && List.class.isAssignableFrom(beanAdapter.getType(string2))) {
                    List list = (List)beanAdapter.get((Object)string2);
                    list.add(this.getListValue(this, string2, string));
                } else {
                    beanAdapter.put(string2, (Object)string.trim());
                }
            } else {
                throw FXMLLoader.this.constructLoadException(class_.getName() + " does not have a default property.");
            }
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        public void processAttribute(String string, String string2, String string3) throws IOException {
            if (string != null && string.equals(FXMLLoader.FX_NAMESPACE_PREFIX)) {
                Class<?> class_;
                if (string2.equals(FXMLLoader.FX_ID_ATTRIBUTE)) {
                    if (string3.equals(FXMLLoader.NULL_KEYWORD)) {
                        throw FXMLLoader.this.constructLoadException("Invalid identifier.");
                    }
                    int n2 = string3.length();
                    for (int i2 = 0; i2 < n2; ++i2) {
                        if (Character.isJavaIdentifierPart(string3.charAt(i2))) continue;
                        throw FXMLLoader.this.constructLoadException("Invalid identifier.");
                    }
                    this.fx_id = string3;
                    return;
                }
                if (!string2.equals("controller")) throw FXMLLoader.this.constructLoadException("Invalid attribute.");
                if (((FXMLLoader)FXMLLoader.this).current.parent != null) {
                    throw FXMLLoader.this.constructLoadException("fx:controller can only be applied to root element.");
                }
                if (FXMLLoader.this.controller != null) {
                    throw FXMLLoader.this.constructLoadException("Controller value already specified.");
                }
                if (FXMLLoader.this.staticLoad) return;
                try {
                    class_ = FXMLLoader.this.getClassLoader().loadClass(string3);
                }
                catch (ClassNotFoundException classNotFoundException) {
                    throw FXMLLoader.this.constructLoadException(classNotFoundException);
                }
                try {
                    if (FXMLLoader.this.controllerFactory == null) {
                        FXMLLoader.this.setController(ReflectUtil.newInstance(class_));
                        return;
                    }
                    FXMLLoader.this.setController(FXMLLoader.this.controllerFactory.call(class_));
                    return;
                }
                catch (InstantiationException instantiationException) {
                    throw FXMLLoader.this.constructLoadException(instantiationException);
                }
                catch (IllegalAccessException illegalAccessException) {
                    throw FXMLLoader.this.constructLoadException(illegalAccessException);
                }
            }
            super.processAttribute(string, string2, string3);
        }

        public abstract Object constructValue() throws IOException;
    }

    private abstract class Element {
        public final Element parent;
        public Object value = null;
        private BeanAdapter valueAdapter = null;
        public final LinkedList<Attribute> eventHandlerAttributes = new LinkedList();
        public final LinkedList<Attribute> instancePropertyAttributes = new LinkedList();
        public final LinkedList<Attribute> staticPropertyAttributes = new LinkedList();
        public final LinkedList<PropertyElement> staticPropertyElements = new LinkedList();

        public Element() {
            this.parent = FXMLLoader.this.current;
        }

        public boolean isCollection() {
            Class<?> class_;
            DefaultProperty defaultProperty;
            boolean bl = this.value instanceof List ? true : ((defaultProperty = (class_ = this.value.getClass()).getAnnotation(DefaultProperty.class)) != null ? this.getProperties().get(defaultProperty.value()) instanceof List : false);
            return bl;
        }

        public void add(Object object) throws LoadException {
            List list;
            if (this.value instanceof List) {
                list = (List)this.value;
            } else {
                Class<?> class_ = this.value.getClass();
                DefaultProperty defaultProperty = class_.getAnnotation(DefaultProperty.class);
                String string = defaultProperty.value();
                list = (List)this.getProperties().get(string);
                if (!Map.class.isAssignableFrom(class_)) {
                    Type type = this.getValueAdapter().getGenericType(string);
                    object = BeanAdapter.coerce(object, BeanAdapter.getListItemType(type));
                }
            }
            list.add(object);
        }

        public void set(Object object) throws LoadException {
            if (this.value == null) {
                throw FXMLLoader.this.constructLoadException("Cannot set value on this element.");
            }
            Class<?> class_ = this.value.getClass();
            DefaultProperty defaultProperty = class_.getAnnotation(DefaultProperty.class);
            if (defaultProperty == null) {
                throw FXMLLoader.this.constructLoadException("Element does not define a default property.");
            }
            this.getProperties().put(defaultProperty.value(), object);
        }

        public void updateValue(Object object) {
            this.value = object;
            this.valueAdapter = null;
        }

        public boolean isTyped() {
            return !(this.value instanceof Map);
        }

        public BeanAdapter getValueAdapter() {
            if (this.valueAdapter == null) {
                this.valueAdapter = new BeanAdapter(this.value);
            }
            return this.valueAdapter;
        }

        public Map<String, Object> getProperties() {
            return this.isTyped() ? this.getValueAdapter() : (Map)this.value;
        }

        public void processStartElement() throws IOException {
            int n2 = FXMLLoader.this.xmlStreamReader.getAttributeCount();
            for (int i2 = 0; i2 < n2; ++i2) {
                String string = FXMLLoader.this.xmlStreamReader.getAttributePrefix(i2);
                String string2 = FXMLLoader.this.xmlStreamReader.getAttributeLocalName(i2);
                String string3 = FXMLLoader.this.xmlStreamReader.getAttributeValue(i2);
                if (FXMLLoader.this.loadListener != null && string != null && string.equals(FXMLLoader.FX_NAMESPACE_PREFIX)) {
                    FXMLLoader.this.loadListener.readInternalAttribute(string + ":" + string2, string3);
                }
                this.processAttribute(string, string2, string3);
            }
        }

        public void processEndElement() throws IOException {
        }

        public void processCharacters() throws IOException {
            throw FXMLLoader.this.constructLoadException("Unexpected characters in input stream.");
        }

        public void processInstancePropertyAttributes() throws IOException {
            if (this.instancePropertyAttributes.size() > 0) {
                for (Attribute attribute : this.instancePropertyAttributes) {
                    this.processPropertyAttribute(attribute);
                }
            }
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        public void processAttribute(String string, String string2, String string3) throws IOException {
            if (string != null) throw FXMLLoader.this.constructLoadException(string + ":" + string2 + " is not a valid attribute.");
            if (string2.startsWith(FXMLLoader.EVENT_HANDLER_PREFIX)) {
                if (FXMLLoader.this.loadListener != null) {
                    FXMLLoader.this.loadListener.readEventHandlerAttribute(string2, string3);
                }
                this.eventHandlerAttributes.add(new Attribute(string2, null, string3));
                return;
            } else {
                int n2 = string2.lastIndexOf(46);
                if (n2 == -1) {
                    if (FXMLLoader.this.loadListener != null) {
                        FXMLLoader.this.loadListener.readPropertyAttribute(string2, null, string3);
                    }
                    this.instancePropertyAttributes.add(new Attribute(string2, null, string3));
                    return;
                } else {
                    String string4 = string2.substring(n2 + 1);
                    Class class_ = FXMLLoader.this.getType(string2.substring(0, n2));
                    if (class_ != null) {
                        if (FXMLLoader.this.loadListener != null) {
                            FXMLLoader.this.loadListener.readPropertyAttribute(string4, class_, string3);
                        }
                        this.staticPropertyAttributes.add(new Attribute(string4, class_, string3));
                        return;
                    } else {
                        if (!FXMLLoader.this.staticLoad) throw FXMLLoader.this.constructLoadException(string2 + " is not a valid attribute.");
                        if (FXMLLoader.this.loadListener == null) return;
                        FXMLLoader.this.loadListener.readUnknownStaticPropertyAttribute(string2, string3);
                    }
                }
            }
        }

        public void processPropertyAttribute(Attribute attribute) throws IOException {
            String string = attribute.value;
            if (this.isBindingExpression(string)) {
                if (attribute.sourceType != null) {
                    throw FXMLLoader.this.constructLoadException("Cannot bind to static property.");
                }
                if (!this.isTyped()) {
                    throw FXMLLoader.this.constructLoadException("Cannot bind to untyped object.");
                }
                if (this.value instanceof Builder) {
                    throw FXMLLoader.this.constructLoadException("Cannot bind to builder property.");
                }
                if (!FXMLLoader.this.impl_isStaticLoad()) {
                    string = string.substring(FXMLLoader.BINDING_EXPRESSION_PREFIX.length(), string.length() - 1);
                    Expression expression = Expression.valueOf(string);
                    BeanAdapter beanAdapter = new BeanAdapter(this.value);
                    ObservableValue observableValue = beanAdapter.getPropertyModel(attribute.name);
                    Class<?> class_ = beanAdapter.getType(attribute.name);
                    if (observableValue instanceof Property) {
                        ((Property)observableValue).bind(new ExpressionValue(FXMLLoader.this.namespace, expression, class_));
                    }
                }
            } else {
                if (this.isBidirectionalBindingExpression(string)) {
                    throw FXMLLoader.this.constructLoadException(new UnsupportedOperationException("This feature is not currently enabled."));
                }
                this.processValue(attribute.sourceType, attribute.name, string);
            }
        }

        private boolean isBindingExpression(String string) {
            return string.startsWith(FXMLLoader.BINDING_EXPRESSION_PREFIX) && string.endsWith("}");
        }

        private boolean isBidirectionalBindingExpression(String string) {
            return string.startsWith(FXMLLoader.BI_DIRECTIONAL_BINDING_PREFIX);
        }

        private boolean processValue(Class class_, String string, String string2) throws LoadException {
            boolean bl = false;
            if (class_ == null && this.isTyped()) {
                BeanAdapter beanAdapter = this.getValueAdapter();
                Class<?> class_2 = beanAdapter.getType(string);
                if (class_2 == null) {
                    throw new PropertyNotFoundException("Property \"" + string + "\" does not exist" + " or is read-only.");
                }
                if (List.class.isAssignableFrom(class_2) && beanAdapter.isReadOnly(string)) {
                    this.populateListFromString(beanAdapter, string, string2);
                    bl = true;
                } else if (class_2.isArray()) {
                    this.applyProperty(string, class_, this.populateArrayFromString(class_2, string2));
                    bl = true;
                }
            }
            if (!bl) {
                this.applyProperty(string, class_, this.resolvePrefixedValue(string2));
                bl = true;
            }
            return bl;
        }

        private Object resolvePrefixedValue(String string) throws LoadException {
            if (string.startsWith(FXMLLoader.ESCAPE_PREFIX)) {
                if (!((string = string.substring(FXMLLoader.ESCAPE_PREFIX.length())).length() != 0 && (string.startsWith(FXMLLoader.ESCAPE_PREFIX) || string.startsWith(FXMLLoader.RELATIVE_PATH_PREFIX) || string.startsWith(FXMLLoader.RESOURCE_KEY_PREFIX) || string.startsWith(FXMLLoader.EXPRESSION_PREFIX) || string.startsWith(FXMLLoader.BI_DIRECTIONAL_BINDING_PREFIX)))) {
                    throw FXMLLoader.this.constructLoadException("Invalid escape sequence.");
                }
                return string;
            }
            if (string.startsWith(FXMLLoader.RELATIVE_PATH_PREFIX)) {
                if ((string = string.substring(FXMLLoader.RELATIVE_PATH_PREFIX.length())).length() == 0) {
                    throw FXMLLoader.this.constructLoadException("Missing relative path.");
                }
                if (string.startsWith(FXMLLoader.RELATIVE_PATH_PREFIX)) {
                    this.warnDeprecatedEscapeSequence(FXMLLoader.RELATIVE_PATH_PREFIX);
                    return string;
                }
                if (string.charAt(0) == '/') {
                    URL uRL = FXMLLoader.this.getClassLoader().getResource(string.substring(1));
                    if (uRL == null) {
                        throw FXMLLoader.this.constructLoadException("Invalid resource: " + string + " not found on the classpath");
                    }
                    return uRL.toString();
                }
                try {
                    return new URL(FXMLLoader.this.location, string).toString();
                }
                catch (MalformedURLException malformedURLException) {
                    System.err.println(FXMLLoader.this.location + "/" + string);
                }
            } else {
                if (string.startsWith(FXMLLoader.RESOURCE_KEY_PREFIX)) {
                    if ((string = string.substring(FXMLLoader.RESOURCE_KEY_PREFIX.length())).length() == 0) {
                        throw FXMLLoader.this.constructLoadException("Missing resource key.");
                    }
                    if (string.startsWith(FXMLLoader.RESOURCE_KEY_PREFIX)) {
                        this.warnDeprecatedEscapeSequence(FXMLLoader.RESOURCE_KEY_PREFIX);
                        return string;
                    }
                    if (FXMLLoader.this.resources == null) {
                        throw FXMLLoader.this.constructLoadException("No resources specified.");
                    }
                    if (!FXMLLoader.this.resources.containsKey(string)) {
                        throw FXMLLoader.this.constructLoadException("Resource \"" + string + "\" not found.");
                    }
                    return FXMLLoader.this.resources.getString(string);
                }
                if (string.startsWith(FXMLLoader.EXPRESSION_PREFIX)) {
                    if ((string = string.substring(FXMLLoader.EXPRESSION_PREFIX.length())).length() == 0) {
                        throw FXMLLoader.this.constructLoadException("Missing expression.");
                    }
                    if (string.startsWith(FXMLLoader.EXPRESSION_PREFIX)) {
                        this.warnDeprecatedEscapeSequence(FXMLLoader.EXPRESSION_PREFIX);
                        return string;
                    }
                    if (string.equals(FXMLLoader.NULL_KEYWORD)) {
                        return null;
                    }
                    return Expression.get((Object)FXMLLoader.this.namespace, KeyPath.parse(string));
                }
            }
            return string;
        }

        private Object populateArrayFromString(Class<?> class_, String string) throws LoadException {
            Object object = null;
            Class<?> class_2 = class_.getComponentType();
            if (string.length() > 0) {
                String[] arrstring = string.split(FXMLLoader.ARRAY_COMPONENT_DELIMITER);
                object = Array.newInstance(class_2, arrstring.length);
                for (int i2 = 0; i2 < arrstring.length; ++i2) {
                    Array.set(object, i2, BeanAdapter.coerce(this.resolvePrefixedValue(arrstring[i2].trim()), class_.getComponentType()));
                }
            } else {
                object = Array.newInstance(class_2, 0);
            }
            return object;
        }

        private void populateListFromString(BeanAdapter beanAdapter, String string, String string2) throws LoadException {
            List list = (List)beanAdapter.get((Object)string);
            Type type = beanAdapter.getGenericType(string);
            Type type2 = (Class)BeanAdapter.getGenericListItemType(type);
            if (type2 instanceof ParameterizedType) {
                type2 = ((ParameterizedType)type2).getRawType();
            }
            if (string2.length() > 0) {
                String[] arrstring;
                for (String string3 : arrstring = string2.split(FXMLLoader.ARRAY_COMPONENT_DELIMITER)) {
                    string3 = string3.trim();
                    list.add(BeanAdapter.coerce(this.resolvePrefixedValue(string3), type2));
                }
            }
        }

        public void warnDeprecatedEscapeSequence(String string) {
            System.err.println(string + string + " is a deprecated escape sequence. " + "Please use \\" + string + " instead.");
        }

        public void applyProperty(String string, Class<?> class_, Object object) {
            if (class_ == null) {
                this.getProperties().put(string, object);
            } else {
                BeanAdapter.put(this.value, class_, string, object);
            }
        }

        private Object getExpressionObject(String string) throws LoadException {
            if (string.startsWith(FXMLLoader.EXPRESSION_PREFIX)) {
                if ((string = string.substring(FXMLLoader.EXPRESSION_PREFIX.length())).length() == 0) {
                    throw FXMLLoader.this.constructLoadException("Missing expression reference.");
                }
                Object t2 = Expression.get((Object)FXMLLoader.this.namespace, KeyPath.parse(string));
                if (t2 == null) {
                    throw FXMLLoader.this.constructLoadException("Unable to resolve expression : $" + string);
                }
                return t2;
            }
            return null;
        }

        private <T> T getExpressionObjectOfType(String string, Class<T> class_) throws LoadException {
            Object object = this.getExpressionObject(string);
            if (object != null) {
                if (class_.isInstance(object)) {
                    return (T)object;
                }
                throw FXMLLoader.this.constructLoadException("Error resolving \"" + string + "\" expression." + "Does not point to a " + class_.getName());
            }
            return null;
        }

        private MethodHandler getControllerMethodHandle(String string, SupportedType ... arrsupportedType) throws LoadException {
            if (string.startsWith(FXMLLoader.CONTROLLER_METHOD_PREFIX) && !(string = string.substring(FXMLLoader.CONTROLLER_METHOD_PREFIX.length())).startsWith(FXMLLoader.CONTROLLER_METHOD_PREFIX)) {
                if (string.length() == 0) {
                    throw FXMLLoader.this.constructLoadException("Missing controller method.");
                }
                if (FXMLLoader.this.controller == null) {
                    throw FXMLLoader.this.constructLoadException("No controller specified.");
                }
                for (SupportedType supportedType : arrsupportedType) {
                    Method method = FXMLLoader.this.controllerAccessor.getControllerMethods().get((Object)supportedType).get(string);
                    if (method == null) continue;
                    return new MethodHandler(FXMLLoader.this.controller, method, supportedType);
                }
                Method method = FXMLLoader.this.controllerAccessor.getControllerMethods().get((Object)SupportedType.PARAMETERLESS).get(string);
                if (method != null) {
                    return new MethodHandler(FXMLLoader.this.controller, method, SupportedType.PARAMETERLESS);
                }
                return null;
            }
            return null;
        }

        public void processEventHandlerAttributes() throws LoadException {
            if (this.eventHandlerAttributes.size() > 0 && !FXMLLoader.this.staticLoad) {
                for (Attribute attribute : this.eventHandlerAttributes) {
                    String string = attribute.value;
                    if (this.value instanceof ObservableList && attribute.name.equals(FXMLLoader.COLLECTION_HANDLER_NAME)) {
                        this.processObservableListHandler(string);
                        continue;
                    }
                    if (this.value instanceof ObservableMap && attribute.name.equals(FXMLLoader.COLLECTION_HANDLER_NAME)) {
                        this.processObservableMapHandler(string);
                        continue;
                    }
                    if (this.value instanceof ObservableSet && attribute.name.equals(FXMLLoader.COLLECTION_HANDLER_NAME)) {
                        this.processObservableSetHandler(string);
                        continue;
                    }
                    if (attribute.name.endsWith(FXMLLoader.CHANGE_EVENT_HANDLER_SUFFIX)) {
                        this.processPropertyHandler(attribute.name, string);
                        continue;
                    }
                    EventHandler eventHandler = null;
                    MethodHandler methodHandler = this.getControllerMethodHandle(string, SupportedType.EVENT);
                    if (methodHandler != null) {
                        eventHandler = new ControllerMethodEventHandler(methodHandler);
                    }
                    if (eventHandler == null) {
                        eventHandler = this.getExpressionObjectOfType(string, EventHandler.class);
                    }
                    if (eventHandler == null) {
                        if (string.length() == 0 || FXMLLoader.this.scriptEngine == null) {
                            throw FXMLLoader.this.constructLoadException("Error resolving " + attribute.name + "='" + attribute.value + "', either the event handler is not in the Namespace or there is an error in the script.");
                        }
                        eventHandler = new ScriptEventHandler(string, FXMLLoader.this.scriptEngine);
                    }
                    this.getValueAdapter().put(attribute.name, (Object)eventHandler);
                }
            }
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        private void processObservableListHandler(String string) throws LoadException {
            ObservableList observableList = (ObservableList)this.value;
            if (string.startsWith(FXMLLoader.CONTROLLER_METHOD_PREFIX)) {
                MethodHandler methodHandler = this.getControllerMethodHandle(string, SupportedType.LIST_CHANGE_LISTENER);
                if (methodHandler == null) throw FXMLLoader.this.constructLoadException("Controller method \"" + string + "\" not found.");
                observableList.addListener(new ObservableListChangeAdapter(methodHandler));
                return;
            } else {
                if (!string.startsWith(FXMLLoader.EXPRESSION_PREFIX)) return;
                Object object = this.getExpressionObject(string);
                if (object instanceof ListChangeListener) {
                    observableList.addListener((ListChangeListener)object);
                    return;
                } else {
                    if (!(object instanceof InvalidationListener)) throw FXMLLoader.this.constructLoadException("Error resolving \"" + string + "\" expression." + "Must be either ListChangeListener or InvalidationListener");
                    observableList.addListener((InvalidationListener)object);
                }
            }
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        private void processObservableMapHandler(String string) throws LoadException {
            ObservableMap observableMap = (ObservableMap)this.value;
            if (string.startsWith(FXMLLoader.CONTROLLER_METHOD_PREFIX)) {
                MethodHandler methodHandler = this.getControllerMethodHandle(string, SupportedType.MAP_CHANGE_LISTENER);
                if (methodHandler == null) throw FXMLLoader.this.constructLoadException("Controller method \"" + string + "\" not found.");
                observableMap.addListener(new ObservableMapChangeAdapter(methodHandler));
                return;
            } else {
                if (!string.startsWith(FXMLLoader.EXPRESSION_PREFIX)) return;
                Object object = this.getExpressionObject(string);
                if (object instanceof MapChangeListener) {
                    observableMap.addListener((MapChangeListener)object);
                    return;
                } else {
                    if (!(object instanceof InvalidationListener)) throw FXMLLoader.this.constructLoadException("Error resolving \"" + string + "\" expression." + "Must be either MapChangeListener or InvalidationListener");
                    observableMap.addListener((InvalidationListener)object);
                }
            }
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        private void processObservableSetHandler(String string) throws LoadException {
            ObservableSet observableSet = (ObservableSet)this.value;
            if (string.startsWith(FXMLLoader.CONTROLLER_METHOD_PREFIX)) {
                MethodHandler methodHandler = this.getControllerMethodHandle(string, SupportedType.SET_CHANGE_LISTENER);
                if (methodHandler == null) throw FXMLLoader.this.constructLoadException("Controller method \"" + string + "\" not found.");
                observableSet.addListener(new ObservableSetChangeAdapter(methodHandler));
                return;
            } else {
                if (!string.startsWith(FXMLLoader.EXPRESSION_PREFIX)) return;
                Object object = this.getExpressionObject(string);
                if (object instanceof SetChangeListener) {
                    observableSet.addListener((SetChangeListener)object);
                    return;
                } else {
                    if (!(object instanceof InvalidationListener)) throw FXMLLoader.this.constructLoadException("Error resolving \"" + string + "\" expression." + "Must be either SetChangeListener or InvalidationListener");
                    observableSet.addListener((InvalidationListener)object);
                }
            }
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        private void processPropertyHandler(String string, String string2) throws LoadException {
            int n2;
            int n3 = FXMLLoader.EVENT_HANDLER_PREFIX.length();
            if (n3 == (n2 = string.length() - FXMLLoader.CHANGE_EVENT_HANDLER_SUFFIX.length())) return;
            String string3 = Character.toLowerCase(string.charAt(n3)) + string.substring(n3 + 1, n2);
            ObservableValue<Object> observableValue = this.getValueAdapter().getPropertyModel(string3);
            if (observableValue == null) {
                throw FXMLLoader.this.constructLoadException(this.value.getClass().getName() + " does not define" + " a property model for \"" + string3 + "\".");
            }
            if (string2.startsWith(FXMLLoader.CONTROLLER_METHOD_PREFIX)) {
                final MethodHandler methodHandler = this.getControllerMethodHandle(string2, SupportedType.PROPERTY_CHANGE_LISTENER, SupportedType.EVENT);
                if (methodHandler == null) throw FXMLLoader.this.constructLoadException("Controller method \"" + string2 + "\" not found.");
                if (methodHandler.type == SupportedType.EVENT) {
                    observableValue.addListener(new ChangeListener<Object>(){

                        @Override
                        public void changed(ObservableValue<?> observableValue, Object object, Object object2) {
                            methodHandler.invoke(new Event(Element.this.value, null, Event.ANY));
                        }
                    });
                    return;
                } else {
                    observableValue.addListener(new PropertyChangeAdapter(methodHandler));
                }
                return;
            } else {
                if (!string2.startsWith(FXMLLoader.EXPRESSION_PREFIX)) return;
                Object object = this.getExpressionObject(string2);
                if (object instanceof ChangeListener) {
                    observableValue.addListener((ChangeListener)object);
                    return;
                } else {
                    if (!(object instanceof InvalidationListener)) throw FXMLLoader.this.constructLoadException("Error resolving \"" + string2 + "\" expression." + "Must be either ChangeListener or InvalidationListener");
                    observableValue.addListener((InvalidationListener)object);
                }
            }
        }
    }
}

