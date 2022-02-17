/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  lombok.eclipse.EclipseAugments
 *  org.eclipse.core.runtime.CoreException
 *  org.eclipse.jdt.core.IAnnotatable
 *  org.eclipse.jdt.core.IAnnotation
 *  org.eclipse.jdt.core.IField
 *  org.eclipse.jdt.core.IMethod
 *  org.eclipse.jdt.core.IType
 *  org.eclipse.jdt.core.JavaModelException
 *  org.eclipse.jdt.core.dom.ASTNode
 *  org.eclipse.jdt.core.dom.AbstractTypeDeclaration
 *  org.eclipse.jdt.core.dom.Annotation
 *  org.eclipse.jdt.core.dom.CompilationUnit
 *  org.eclipse.jdt.core.dom.MethodDeclaration
 *  org.eclipse.jdt.core.dom.Name
 *  org.eclipse.jdt.core.dom.NormalAnnotation
 *  org.eclipse.jdt.core.dom.QualifiedName
 *  org.eclipse.jdt.core.dom.SimpleName
 *  org.eclipse.jdt.core.dom.SingleMemberAnnotation
 *  org.eclipse.jdt.core.dom.rewrite.ListRewrite
 *  org.eclipse.jdt.core.search.SearchMatch
 *  org.eclipse.jdt.internal.compiler.ast.ASTNode
 *  org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration
 *  org.eclipse.jdt.internal.compiler.ast.Annotation
 *  org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration
 *  org.eclipse.jdt.internal.compiler.ast.Expression
 *  org.eclipse.jdt.internal.compiler.ast.FieldDeclaration
 *  org.eclipse.jdt.internal.compiler.ast.ForeachStatement
 *  org.eclipse.jdt.internal.compiler.ast.LocalDeclaration
 *  org.eclipse.jdt.internal.compiler.ast.MessageSend
 *  org.eclipse.jdt.internal.compiler.lookup.BlockScope
 *  org.eclipse.jdt.internal.compiler.lookup.MethodBinding
 *  org.eclipse.jdt.internal.compiler.lookup.Scope
 *  org.eclipse.jdt.internal.compiler.lookup.TypeBinding
 *  org.eclipse.jdt.internal.compiler.parser.Parser
 *  org.eclipse.jdt.internal.compiler.problem.ProblemReporter
 *  org.eclipse.jdt.internal.core.SourceField
 *  org.eclipse.jdt.internal.core.dom.rewrite.NodeRewriteEvent
 *  org.eclipse.jdt.internal.core.dom.rewrite.RewriteEvent
 *  org.eclipse.jdt.internal.core.dom.rewrite.TokenScanner
 *  org.eclipse.jdt.internal.corext.refactoring.SearchResultGroup
 *  org.eclipse.jdt.internal.corext.refactoring.structure.ASTNodeSearchUtil
 */
package lombok.launch;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import lombok.eclipse.EclipseAugments;
import lombok.launch.Main;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IAnnotatable;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.Annotation;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.ast.ForeachStatement;
import org.eclipse.jdt.internal.compiler.ast.LocalDeclaration;
import org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.Scope;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.eclipse.jdt.internal.compiler.parser.Parser;
import org.eclipse.jdt.internal.compiler.problem.ProblemReporter;
import org.eclipse.jdt.internal.core.SourceField;
import org.eclipse.jdt.internal.core.dom.rewrite.NodeRewriteEvent;
import org.eclipse.jdt.internal.core.dom.rewrite.RewriteEvent;
import org.eclipse.jdt.internal.core.dom.rewrite.TokenScanner;
import org.eclipse.jdt.internal.corext.refactoring.SearchResultGroup;
import org.eclipse.jdt.internal.corext.refactoring.structure.ASTNodeSearchUtil;

final class PatchFixesHider {
    PatchFixesHider() {
    }

    public static final class Delegate {
        private static final Method HANDLE_DELEGATE_FOR_TYPE;

        static {
            Class<?> shadowed = Util.shadowLoadClass("lombok.eclipse.agent.PatchDelegatePortal");
            HANDLE_DELEGATE_FOR_TYPE = Util.findMethod(shadowed, "handleDelegateForType", Object.class);
        }

        public static boolean handleDelegateForType(Object classScope) {
            return (Boolean)Util.invokeMethod(HANDLE_DELEGATE_FOR_TYPE, classScope);
        }
    }

    public static final class ExtensionMethod {
        private static final Method RESOLVE_TYPE;
        private static final Method ERROR_NO_METHOD_FOR;
        private static final Method INVALID_METHOD;
        private static final Method INVALID_METHOD2;

        static {
            Class<?> shadowed = Util.shadowLoadClass("lombok.eclipse.agent.PatchExtensionMethod");
            RESOLVE_TYPE = Util.findMethod(shadowed, "resolveType", TypeBinding.class, MessageSend.class, BlockScope.class);
            ERROR_NO_METHOD_FOR = Util.findMethod(shadowed, "errorNoMethodFor", ProblemReporter.class, MessageSend.class, TypeBinding.class, TypeBinding[].class);
            INVALID_METHOD = Util.findMethod(shadowed, "invalidMethod", ProblemReporter.class, MessageSend.class, MethodBinding.class);
            INVALID_METHOD2 = Util.findMethod(shadowed, "invalidMethod", ProblemReporter.class, MessageSend.class, MethodBinding.class, Scope.class);
        }

        public static TypeBinding resolveType(TypeBinding resolvedType, MessageSend methodCall, BlockScope scope) {
            return (TypeBinding)Util.invokeMethod(RESOLVE_TYPE, new Object[]{resolvedType, methodCall, scope});
        }

        public static void errorNoMethodFor(ProblemReporter problemReporter, MessageSend messageSend, TypeBinding recType, TypeBinding[] params) {
            Util.invokeMethod(ERROR_NO_METHOD_FOR, new Object[]{problemReporter, messageSend, recType, params});
        }

        public static void invalidMethod(ProblemReporter problemReporter, MessageSend messageSend, MethodBinding method) {
            Util.invokeMethod(INVALID_METHOD, new Object[]{problemReporter, messageSend, method});
        }

        public static void invalidMethod(ProblemReporter problemReporter, MessageSend messageSend, MethodBinding method, Scope scope) {
            Util.invokeMethod(INVALID_METHOD2, new Object[]{problemReporter, messageSend, method, scope});
        }
    }

    public static final class LombokDeps {
        public static final Method ADD_LOMBOK_NOTES;
        public static final Method POST_COMPILER_BYTES_STRING;
        public static final Method POST_COMPILER_OUTPUTSTREAM;
        public static final Method POST_COMPILER_BUFFEREDOUTPUTSTREAM_STRING_STRING;

        static {
            Class<?> shadowed = Util.shadowLoadClass("lombok.eclipse.agent.PatchFixesShadowLoaded");
            ADD_LOMBOK_NOTES = Util.findMethod(shadowed, "addLombokNotesToEclipseAboutDialog", String.class, String.class);
            POST_COMPILER_BYTES_STRING = Util.findMethod(shadowed, "runPostCompiler", byte[].class, String.class);
            POST_COMPILER_OUTPUTSTREAM = Util.findMethod(shadowed, "runPostCompiler", OutputStream.class);
            POST_COMPILER_BUFFEREDOUTPUTSTREAM_STRING_STRING = Util.findMethod(shadowed, "runPostCompiler", BufferedOutputStream.class, String.class, String.class);
        }

        public static String addLombokNotesToEclipseAboutDialog(String origReturnValue, String key) {
            try {
                return (String)Util.invokeMethod(ADD_LOMBOK_NOTES, origReturnValue, key);
            }
            catch (Throwable throwable) {
                return origReturnValue;
            }
        }

        public static byte[] runPostCompiler(byte[] bytes, String fileName) {
            return (byte[])Util.invokeMethod(POST_COMPILER_BYTES_STRING, bytes, fileName);
        }

        public static OutputStream runPostCompiler(OutputStream out) throws IOException {
            return (OutputStream)Util.invokeMethod(POST_COMPILER_OUTPUTSTREAM, out);
        }

        public static BufferedOutputStream runPostCompiler(BufferedOutputStream out, String path, String name) throws IOException {
            return (BufferedOutputStream)Util.invokeMethod(POST_COMPILER_BUFFEREDOUTPUTSTREAM_STRING_STRING, out, path, name);
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static final class PatchFixes {
        public static final int ALREADY_PROCESSED_FLAG = 0x800000;

        public static boolean isGenerated(ASTNode node) {
            boolean result = false;
            try {
                result = (Boolean)node.getClass().getField("$isGenerated").get((Object)node);
                if (!result && node.getParent() != null && node.getParent() instanceof QualifiedName) {
                    result = PatchFixes.isGenerated(node.getParent());
                }
            }
            catch (Exception exception) {}
            return result;
        }

        public static boolean isListRewriteOnGeneratedNode(ListRewrite rewrite) {
            return PatchFixes.isGenerated(rewrite.getParent());
        }

        public static boolean returnFalse(Object object) {
            return false;
        }

        public static boolean returnTrue(Object object) {
            return true;
        }

        public static List removeGeneratedNodes(List list) {
            try {
                ArrayList realNodes = new ArrayList(list.size());
                for (Object node : list) {
                    if (PatchFixes.isGenerated((ASTNode)node)) continue;
                    realNodes.add(node);
                }
                return realNodes;
            }
            catch (Exception exception) {
                return list;
            }
        }

        public static String getRealMethodDeclarationSource(String original, Object processor, MethodDeclaration declaration) throws Exception {
            if (!PatchFixes.isGenerated((ASTNode)declaration)) {
                return original;
            }
            ArrayList<org.eclipse.jdt.core.dom.Annotation> annotations = new ArrayList<org.eclipse.jdt.core.dom.Annotation>();
            for (Object modifier : declaration.modifiers()) {
                org.eclipse.jdt.core.dom.Annotation annotation;
                String qualifiedAnnotationName;
                if (!(modifier instanceof org.eclipse.jdt.core.dom.Annotation) || "java.lang.Override".equals(qualifiedAnnotationName = (annotation = (org.eclipse.jdt.core.dom.Annotation)modifier).resolveTypeBinding().getQualifiedName()) || "java.lang.SuppressWarnings".equals(qualifiedAnnotationName)) continue;
                annotations.add(annotation);
            }
            StringBuilder signature = new StringBuilder();
            PatchFixes.addAnnotations(annotations, signature);
            if (((Boolean)processor.getClass().getDeclaredField("fPublic").get(processor)).booleanValue()) {
                signature.append("public ");
            }
            if (((Boolean)processor.getClass().getDeclaredField("fAbstract").get(processor)).booleanValue()) {
                signature.append("abstract ");
            }
            signature.append(declaration.getReturnType2().toString()).append(" ").append(declaration.getName().getFullyQualifiedName()).append("(");
            boolean first = true;
            for (Object parameter : declaration.parameters()) {
                if (!first) {
                    signature.append(", ");
                }
                first = false;
                signature.append(parameter);
            }
            signature.append(");");
            return signature.toString();
        }

        public static void addAnnotations(List<org.eclipse.jdt.core.dom.Annotation> annotations, StringBuilder signature) {
            for (org.eclipse.jdt.core.dom.Annotation annotation : annotations) {
                ArrayList<String> values = new ArrayList<String>();
                if (annotation.isSingleMemberAnnotation()) {
                    SingleMemberAnnotation smAnn = (SingleMemberAnnotation)annotation;
                    values.add(smAnn.getValue().toString());
                } else if (annotation.isNormalAnnotation()) {
                    NormalAnnotation normalAnn = (NormalAnnotation)annotation;
                    for (Object value : normalAnn.values()) {
                        values.add(value.toString());
                    }
                }
                signature.append("@").append(annotation.resolveTypeBinding().getQualifiedName());
                if (!values.isEmpty()) {
                    signature.append("(");
                    boolean first = true;
                    for (String string : values) {
                        if (!first) {
                            signature.append(", ");
                        }
                        first = false;
                        signature.append('\"').append(string).append('\"');
                    }
                    signature.append(")");
                }
                signature.append(" ");
            }
        }

        public static MethodDeclaration getRealMethodDeclarationNode(IMethod sourceMethod, CompilationUnit cuUnit) throws JavaModelException {
            MethodDeclaration methodDeclarationNode = ASTNodeSearchUtil.getMethodDeclarationNode((IMethod)sourceMethod, (CompilationUnit)cuUnit);
            if (PatchFixes.isGenerated((ASTNode)methodDeclarationNode)) {
                Stack<IType> typeStack = new Stack<IType>();
                for (IType declaringType = sourceMethod.getDeclaringType(); declaringType != null; declaringType = declaringType.getDeclaringType()) {
                    typeStack.push(declaringType);
                }
                IType rootType = (IType)typeStack.pop();
                AbstractTypeDeclaration typeDeclaration = PatchFixes.findTypeDeclaration(rootType, cuUnit.types());
                while (!typeStack.isEmpty() && typeDeclaration != null) {
                    typeDeclaration = PatchFixes.findTypeDeclaration((IType)typeStack.pop(), typeDeclaration.bodyDeclarations());
                }
                if (typeStack.isEmpty() && typeDeclaration != null) {
                    String methodName = sourceMethod.getElementName();
                    for (Object declaration : typeDeclaration.bodyDeclarations()) {
                        MethodDeclaration methodDeclaration;
                        if (!(declaration instanceof MethodDeclaration) || !(methodDeclaration = (MethodDeclaration)declaration).getName().toString().equals(methodName)) continue;
                        return methodDeclaration;
                    }
                }
            }
            return methodDeclarationNode;
        }

        public static AbstractTypeDeclaration findTypeDeclaration(IType searchType, List<?> nodes) {
            for (Object object : nodes) {
                AbstractTypeDeclaration typeDeclaration;
                if (!(object instanceof AbstractTypeDeclaration) || !(typeDeclaration = (AbstractTypeDeclaration)object).getName().toString().equals(searchType.getElementName())) continue;
                return typeDeclaration;
            }
            return null;
        }

        public static int getSourceEndFixed(int sourceEnd, org.eclipse.jdt.internal.compiler.ast.ASTNode node) throws Exception {
            org.eclipse.jdt.internal.compiler.ast.ASTNode object;
            if (sourceEnd == -1 && (object = (org.eclipse.jdt.internal.compiler.ast.ASTNode)node.getClass().getField("$generatedBy").get((Object)node)) != null) {
                return object.sourceEnd;
            }
            return sourceEnd;
        }

        public static int fixRetrieveStartingCatchPosition(int original, int start) {
            return original == -1 ? start : original;
        }

        public static int fixRetrieveIdentifierEndPosition(int original, int start, int end) {
            if (original == -1) {
                return end;
            }
            if (original < start) {
                return end;
            }
            return original;
        }

        public static int fixRetrieveEllipsisStartPosition(int original, int end) {
            return original == -1 ? end : original;
        }

        public static int fixRetrieveRightBraceOrSemiColonPosition(int original, int end) {
            return original == -1 ? end : original;
        }

        public static int fixRetrieveRightBraceOrSemiColonPosition(int retVal, AbstractMethodDeclaration amd) {
            boolean isGenerated;
            if (retVal != -1 || amd == null) {
                return retVal;
            }
            boolean bl = isGenerated = EclipseAugments.ASTNode_generatedBy.get((Object)amd) != null;
            if (isGenerated) {
                return amd.declarationSourceEnd;
            }
            return -1;
        }

        public static int fixRetrieveRightBraceOrSemiColonPosition(int retVal, FieldDeclaration fd) {
            boolean isGenerated;
            if (retVal != -1 || fd == null) {
                return retVal;
            }
            boolean bl = isGenerated = EclipseAugments.ASTNode_generatedBy.get((Object)fd) != null;
            if (isGenerated) {
                return fd.declarationSourceEnd;
            }
            return -1;
        }

        public static boolean checkBit24(Object node) throws Exception {
            int bits = (Integer)node.getClass().getField("bits").get(node);
            return (bits & 0x800000) != 0;
        }

        public static boolean skipRewritingGeneratedNodes(ASTNode node) throws Exception {
            return (Boolean)node.getClass().getField("$isGenerated").get((Object)node);
        }

        public static void setIsGeneratedFlag(ASTNode domNode, org.eclipse.jdt.internal.compiler.ast.ASTNode internalNode) throws Exception {
            boolean isGenerated;
            if (internalNode == null || domNode == null) {
                return;
            }
            boolean bl = isGenerated = EclipseAugments.ASTNode_generatedBy.get((Object)internalNode) != null;
            if (isGenerated) {
                domNode.getClass().getField("$isGenerated").set((Object)domNode, true);
            }
        }

        public static void setIsGeneratedFlagForName(Name name, Object internalNode) throws Exception {
            if (internalNode instanceof org.eclipse.jdt.internal.compiler.ast.ASTNode) {
                boolean isGenerated;
                boolean bl = isGenerated = EclipseAugments.ASTNode_generatedBy.get((Object)((org.eclipse.jdt.internal.compiler.ast.ASTNode)internalNode)) != null;
                if (isGenerated) {
                    name.getClass().getField("$isGenerated").set((Object)name, true);
                }
            }
        }

        public static RewriteEvent[] listRewriteHandleGeneratedMethods(RewriteEvent parent) {
            RewriteEvent[] children = parent.getChildren();
            ArrayList<Object> newChildren = new ArrayList<Object>();
            ArrayList<NodeRewriteEvent> modifiedChildren = new ArrayList<NodeRewriteEvent>();
            for (int i2 = 0; i2 < children.length; ++i2) {
                RewriteEvent child = children[i2];
                boolean isGenerated = PatchFixes.isGenerated((ASTNode)child.getOriginalValue());
                if (isGenerated) {
                    boolean isReplacedOrRemoved = child.getChangeKind() == 4 || child.getChangeKind() == 2;
                    boolean convertingFromMethod = child.getOriginalValue() instanceof MethodDeclaration;
                    if (!isReplacedOrRemoved || !convertingFromMethod || child.getNewValue() == null) continue;
                    modifiedChildren.add(new NodeRewriteEvent(null, child.getNewValue()));
                    continue;
                }
                newChildren.add((Object)child);
            }
            newChildren.addAll(modifiedChildren);
            return newChildren.toArray((T[])new RewriteEvent[0]);
        }

        public static int getTokenEndOffsetFixed(TokenScanner scanner, int token, int startOffset, Object domNode) throws CoreException {
            boolean isGenerated = false;
            try {
                isGenerated = (Boolean)domNode.getClass().getField("$isGenerated").get(domNode);
            }
            catch (Exception exception) {}
            if (isGenerated) {
                return -1;
            }
            return scanner.getTokenEndOffset(token, startOffset);
        }

        public static IMethod[] removeGeneratedMethods(IMethod[] methods) throws Exception {
            ArrayList<IMethod> result = new ArrayList<IMethod>();
            IMethod[] arriMethod = methods;
            int n2 = methods.length;
            for (int i2 = 0; i2 < n2; ++i2) {
                IMethod m2 = arriMethod[i2];
                if (m2.getNameRange().getLength() <= 0 || m2.getNameRange().equals((Object)m2.getSourceRange())) continue;
                result.add(m2);
            }
            return result.size() == methods.length ? methods : result.toArray((T[])new IMethod[0]);
        }

        public static SearchMatch[] removeGenerated(SearchMatch[] returnValue) {
            ArrayList<SearchMatch> result = new ArrayList<SearchMatch>();
            for (int j2 = 0; j2 < returnValue.length; ++j2) {
                IField field;
                IAnnotation annotation;
                SearchMatch searchResult = returnValue[j2];
                if (searchResult.getElement() instanceof IField && (annotation = (field = (IField)searchResult.getElement()).getAnnotation("Generated")) != null) continue;
                result.add(searchResult);
            }
            return result.toArray((T[])new SearchMatch[0]);
        }

        public static SearchResultGroup[] createFakeSearchResult(SearchResultGroup[] returnValue, Object processor) throws Exception {
            Field declaredField;
            if ((returnValue == null || returnValue.length == 0) && (declaredField = processor.getClass().getDeclaredField("fField")) != null) {
                declaredField.setAccessible(true);
                SourceField fField = (SourceField)declaredField.get(processor);
                IAnnotation dataAnnotation = fField.getDeclaringType().getAnnotation("Data");
                if (dataAnnotation != null) {
                    return new SearchResultGroup[]{new SearchResultGroup(null, new SearchMatch[1])};
                }
            }
            return returnValue;
        }

        public static SimpleName[] removeGeneratedSimpleNames(SimpleName[] in) throws Exception {
            Field f2 = SimpleName.class.getField("$isGenerated");
            int count = 0;
            for (int i2 = 0; i2 < in.length; ++i2) {
                if (in[i2] != null && ((Boolean)f2.get((Object)in[i2])).booleanValue()) continue;
                ++count;
            }
            if (count == in.length) {
                return in;
            }
            SimpleName[] newSimpleNames = new SimpleName[count];
            count = 0;
            for (int i3 = 0; i3 < in.length; ++i3) {
                if (in[i3] != null && ((Boolean)f2.get((Object)in[i3])).booleanValue()) continue;
                newSimpleNames[count++] = in[i3];
            }
            return newSimpleNames;
        }

        public static Annotation[] convertAnnotations(Annotation[] out, IAnnotatable annotatable) {
            IAnnotation[] in;
            try {
                in = annotatable.getAnnotations();
            }
            catch (Exception exception) {
                return out;
            }
            if (out == null) {
                return null;
            }
            int toWrite = 0;
            for (int idx = 0; idx < out.length; ++idx) {
                String oName = new String(out[idx].type.getLastToken());
                boolean found = false;
                IAnnotation[] arriAnnotation = in;
                int n2 = in.length;
                for (int i2 = 0; i2 < n2; ++i2) {
                    IAnnotation i3 = arriAnnotation[i2];
                    String name = i3.getElementName();
                    int li = name.lastIndexOf(46);
                    if (li > -1) {
                        name = name.substring(li + 1);
                    }
                    if (!name.equals(oName)) continue;
                    found = true;
                    break;
                }
                if (!found) {
                    out[idx] = null;
                    continue;
                }
                ++toWrite;
            }
            Annotation[] replace = out;
            if (toWrite < out.length) {
                replace = new Annotation[toWrite];
                int idx = 0;
                for (int i4 = 0; i4 < out.length; ++i4) {
                    if (out[i4] == null) continue;
                    replace[idx++] = out[i4];
                }
            }
            return replace;
        }
    }

    public static final class Transform {
        private static final Method TRANSFORM;
        private static final Method TRANSFORM_SWAPPED;

        static {
            Class<?> shadowed = Util.shadowLoadClass("lombok.eclipse.TransformEclipseAST");
            TRANSFORM = Util.findMethod(shadowed, "transform", Parser.class, CompilationUnitDeclaration.class);
            TRANSFORM_SWAPPED = Util.findMethod(shadowed, "transform_swapped", CompilationUnitDeclaration.class, Parser.class);
        }

        public static void transform(Parser parser, CompilationUnitDeclaration ast) throws IOException {
            Util.invokeMethod(TRANSFORM, new Object[]{parser, ast});
        }

        public static void transform_swapped(CompilationUnitDeclaration ast, Parser parser) throws IOException {
            Util.invokeMethod(TRANSFORM_SWAPPED, new Object[]{ast, parser});
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static final class Util {
        private static ClassLoader shadowLoader;

        public static Class<?> shadowLoadClass(String name) {
            try {
                if (shadowLoader == null) {
                    try {
                        Class.forName("lombok.core.LombokNode");
                        shadowLoader = Util.class.getClassLoader();
                    }
                    catch (ClassNotFoundException classNotFoundException) {
                        shadowLoader = Main.getShadowClassLoader();
                    }
                }
                return Class.forName(name, true, shadowLoader);
            }
            catch (ClassNotFoundException e2) {
                throw Util.sneakyThrow(e2);
            }
        }

        public static Method findMethod(Class<?> type, String name, Class<?> ... parameterTypes) {
            try {
                return type.getDeclaredMethod(name, parameterTypes);
            }
            catch (NoSuchMethodException e2) {
                throw Util.sneakyThrow(e2);
            }
        }

        public static Object invokeMethod(Method method, Object ... args) {
            try {
                return method.invoke(null, args);
            }
            catch (IllegalAccessException e2) {
                throw Util.sneakyThrow(e2);
            }
            catch (InvocationTargetException e3) {
                throw Util.sneakyThrow(e3.getCause());
            }
        }

        private static RuntimeException sneakyThrow(Throwable t2) {
            if (t2 == null) {
                throw new NullPointerException("t");
            }
            Util.sneakyThrow0(t2);
            return null;
        }

        private static <T extends Throwable> void sneakyThrow0(Throwable t2) throws T {
            throw t2;
        }
    }

    public static final class Val {
        private static final Method SKIP_RESOLVE_INITIALIZER_IF_ALREADY_CALLED;
        private static final Method SKIP_RESOLVE_INITIALIZER_IF_ALREADY_CALLED2;
        private static final Method HANDLE_VAL_FOR_LOCAL_DECLARATION;
        private static final Method HANDLE_VAL_FOR_FOR_EACH;

        static {
            Class<?> shadowed = Util.shadowLoadClass("lombok.eclipse.agent.PatchVal");
            SKIP_RESOLVE_INITIALIZER_IF_ALREADY_CALLED = Util.findMethod(shadowed, "skipResolveInitializerIfAlreadyCalled", Expression.class, BlockScope.class);
            SKIP_RESOLVE_INITIALIZER_IF_ALREADY_CALLED2 = Util.findMethod(shadowed, "skipResolveInitializerIfAlreadyCalled2", Expression.class, BlockScope.class, LocalDeclaration.class);
            HANDLE_VAL_FOR_LOCAL_DECLARATION = Util.findMethod(shadowed, "handleValForLocalDeclaration", LocalDeclaration.class, BlockScope.class);
            HANDLE_VAL_FOR_FOR_EACH = Util.findMethod(shadowed, "handleValForForEach", ForeachStatement.class, BlockScope.class);
        }

        public static TypeBinding skipResolveInitializerIfAlreadyCalled(Expression expr, BlockScope scope) {
            return (TypeBinding)Util.invokeMethod(SKIP_RESOLVE_INITIALIZER_IF_ALREADY_CALLED, new Object[]{expr, scope});
        }

        public static TypeBinding skipResolveInitializerIfAlreadyCalled2(Expression expr, BlockScope scope, LocalDeclaration decl) {
            return (TypeBinding)Util.invokeMethod(SKIP_RESOLVE_INITIALIZER_IF_ALREADY_CALLED2, new Object[]{expr, scope, decl});
        }

        public static boolean handleValForLocalDeclaration(LocalDeclaration local, BlockScope scope) {
            return (Boolean)Util.invokeMethod(HANDLE_VAL_FOR_LOCAL_DECLARATION, new Object[]{local, scope});
        }

        public static boolean handleValForForEach(ForeachStatement forEach, BlockScope scope) {
            return (Boolean)Util.invokeMethod(HANDLE_VAL_FOR_FOR_EACH, new Object[]{forEach, scope});
        }
    }

    public static final class ValPortal {
        private static final Method COPY_INITIALIZATION_OF_FOR_EACH_ITERABLE;
        private static final Method COPY_INITIALIZATION_OF_LOCAL_DECLARATION;
        private static final Method ADD_FINAL_AND_VAL_ANNOTATION_TO_VARIABLE_DECLARATION_STATEMENT;
        private static final Method ADD_FINAL_AND_VAL_ANNOTATION_TO_SINGLE_VARIABLE_DECLARATION;

        static {
            Class<?> shadowed = Util.shadowLoadClass("lombok.eclipse.agent.PatchValEclipsePortal");
            COPY_INITIALIZATION_OF_FOR_EACH_ITERABLE = Util.findMethod(shadowed, "copyInitializationOfForEachIterable", Object.class);
            COPY_INITIALIZATION_OF_LOCAL_DECLARATION = Util.findMethod(shadowed, "copyInitializationOfLocalDeclaration", Object.class);
            ADD_FINAL_AND_VAL_ANNOTATION_TO_VARIABLE_DECLARATION_STATEMENT = Util.findMethod(shadowed, "addFinalAndValAnnotationToVariableDeclarationStatement", Object.class, Object.class, Object.class);
            ADD_FINAL_AND_VAL_ANNOTATION_TO_SINGLE_VARIABLE_DECLARATION = Util.findMethod(shadowed, "addFinalAndValAnnotationToSingleVariableDeclaration", Object.class, Object.class, Object.class);
        }

        public static void copyInitializationOfForEachIterable(Object parser) {
            Util.invokeMethod(COPY_INITIALIZATION_OF_FOR_EACH_ITERABLE, parser);
        }

        public static void copyInitializationOfLocalDeclaration(Object parser) {
            Util.invokeMethod(COPY_INITIALIZATION_OF_LOCAL_DECLARATION, parser);
        }

        public static void addFinalAndValAnnotationToVariableDeclarationStatement(Object converter, Object out, Object in) {
            Util.invokeMethod(ADD_FINAL_AND_VAL_ANNOTATION_TO_VARIABLE_DECLARATION_STATEMENT, converter, out, in);
        }

        public static void addFinalAndValAnnotationToSingleVariableDeclaration(Object converter, Object out, Object in) {
            Util.invokeMethod(ADD_FINAL_AND_VAL_ANNOTATION_TO_SINGLE_VARIABLE_DECLARATION, converter, out, in);
        }
    }
}

