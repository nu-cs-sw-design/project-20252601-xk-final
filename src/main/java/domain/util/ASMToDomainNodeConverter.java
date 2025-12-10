package domain.util;

import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InnerClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;
import java.util.List;

public class ASMToDomainNodeConverter {

    public DomainClassNode createDomainClassNode(ClassNode classNode) {

        DomainClassNode result = new DomainClassNode();

        result.name = Type.getObjectType(classNode.name).getClassName();
        result.superName = Type.getObjectType(classNode.superName).getClassName();
        result.interfaces = classNode.interfaces.stream().map(internalName -> Type.getObjectType(internalName).getClassName()).toList();
        result.accessModifiers = getAccessModifierStrings(classNode.access);
        result.fields = classNode.fields.stream().map(this::createDomainFieldNode).toList();
        result.methods = classNode.methods.stream().filter(methodNode -> !methodNode.name.contains("lambda$")).map(methodNode -> createDomainMethodNode(methodNode, getLocalClassName(classNode.name))).toList();
        result.innerClasses = classNode.innerClasses.stream().map(this::createDomainInnerClassNode).toList();

        return result;

    }

    public DomainFieldNode createDomainFieldNode(FieldNode fieldNode) {

        DomainFieldNode result = new DomainFieldNode();

        result.name = fieldNode.name;
        result.accessModifiers = getAccessModifierStrings(fieldNode.access);
        if (fieldNode.signature != null)
            result.type = getVariableType(fieldNode.signature);
        else
            result.type = getVariableType(fieldNode.desc);

        return result;

    }

    public DomainMethodNode createDomainMethodNode(MethodNode methodNode, String localClassName) {

        DomainMethodNode result = new DomainMethodNode();

        if (methodNode.name.equals("<init>")) {
            result.name = localClassName;
            result.returnType = "";
        }
        else {
            result.name = methodNode.name;
            if (methodNode.signature != null)
                result.returnType = getMethodReturnType(methodNode.signature);
            else
                result.returnType = getMethodReturnType(methodNode.desc);
        }

        result.accessModifiers = getAccessModifierStrings(methodNode.access);

        if (methodNode.parameters != null)
            result.parameterNames = methodNode.parameters.stream().map(parameterNode -> parameterNode.name).toList();
        if (methodNode.localVariables != null) {
            result.localVariableNames = methodNode.localVariables.stream().map(localVariableNode -> localVariableNode.name).toList();
            result.localVariableTypes = methodNode.localVariables.stream().map(
                    localVariableNode -> {
                        if (localVariableNode.signature != null)
                            return getVariableType(localVariableNode.signature);
                        else
                            return getVariableType(localVariableNode.desc);
                    }
            ).toList();
        }

        if (methodNode.signature != null)
            result.parameterTypes = getMethodArgumentTypes(methodNode.signature);
        else
            result.parameterTypes = getMethodArgumentTypes(methodNode.desc);

        return result;

    }

    public DomainInnerClassNode createDomainInnerClassNode(InnerClassNode innerClassNode) {

        DomainInnerClassNode result = new DomainInnerClassNode();

        result.name = getLocalClassName(innerClassNode.name);
        result.accessModifiers = getAccessModifierStrings(innerClassNode.access);

        return result;

    }

    // Helper method for turning ASM internal class name into raw local class name as seen in source code
    @Nullable
    private String getLocalClassName(String internalName) {

        if (internalName.isEmpty() || internalName.endsWith("/")) {
            return null;
        }

        int cutoff = Math.max(internalName.lastIndexOf('/'), internalName.lastIndexOf('$'));
        if (cutoff < 0) {
            return internalName;
        }

        return internalName.substring(cutoff + 1);
    }

    // Helper method to get a list of access modifier names from the asm access integer encoded with that info
    private List<String> getAccessModifierStrings(int access) {

        List<String> results = new ArrayList<>();

        // Scope
        if ((access & Opcodes.ACC_PUBLIC) != 0) {
            results.add("public");
        } else if ((access & Opcodes.ACC_PRIVATE) != 0) {
            results.add("private");
        } else if ((access & Opcodes.ACC_PROTECTED) != 0) {
            results.add("protected");
        }

        // Static
        if ((access & Opcodes.ACC_STATIC) != 0) {
            results.add("static");
        }

        // Final/abstract
        if ((access & Opcodes.ACC_FINAL) != 0) {
            results.add("final");
        } else if ((access & Opcodes.ACC_ABSTRACT) != 0) {
            results.add("abstract");
        } else if ((access & Opcodes.ACC_INTERFACE) != 0) {
            results.add("interface");
        }

        return results;

    }

    private String getVariableType(String signature) {

        List<String> results = new ArrayList<>();
        addNextType(signature, 0, results);
        return results.getFirst();

    }

    private List<String> getMethodArgumentTypes(String signature) {

        List<String> results = new ArrayList<>();

        int readIndex = 0;

        while (readIndex < signature.length()) {

            readIndex = addNextType(signature, readIndex, results);

        }

        return results;

    }

    private String getMethodReturnType(String signature) {

        List<String> results = new ArrayList<>();

        int readIndex = 0;

        while (readIndex < signature.length()) {

            readIndex = addNextType(signature, readIndex, results);

        }

        return results.getLast();

    }

    private int addNextType(String signature, int readIndex, List<String> results) {

        char[] signatureArray = signature.toCharArray();

        switch (signatureArray[readIndex]) {

            case 'Z':
                results.add("boolean");
                return readIndex + 1;
            case 'C':
                results.add("char");
                return readIndex + 1;
            case 'B':
                results.add("byte");
                return readIndex + 1;
            case 'S':
                results.add("short");
                return readIndex + 1;
            case 'I':
                results.add("int");
                return readIndex + 1;
            case 'F':
                results.add("float");
                return readIndex + 1;
            case 'J':
                results.add("long");
                return readIndex + 1;
            case 'D':
                results.add("double");
                return readIndex + 1;
            case '[':
                readIndex = addNextType(signature, readIndex + 1, results);
                String arrayElementType = results.getLast();
                results.set(results.size() - 1, arrayElementType + "[]");
                return readIndex;
            case 'L':
                int endMarkerIndex = signature.indexOf(';', readIndex + 1);
                int bracketIndex = signature.indexOf('<', readIndex + 1, endMarkerIndex);
                if (bracketIndex < 0) {
                    results.add(signature.substring(readIndex + 1, endMarkerIndex).replace('/', '.'));
                    readIndex = endMarkerIndex + 1;
                }
                else {
                    results.add(signature.substring(readIndex + 1, bracketIndex).replace('/', '.'));
                    readIndex = bracketIndex;
                    List<String> innerResults = new ArrayList<>();
                    while (signature.charAt(readIndex) != '>') {
                        readIndex = addNextType(signature, readIndex, innerResults);
                    }
                    StringBuilder s = new StringBuilder();
                    s.append('<');
                    int count = innerResults.size();
                    for (int i = 0; i < count; i++) {
                        s.append(innerResults.get(i));
                        if (i < count - 1) {
                            s.append(", ");
                        }
                    }
                    s.append('>');
                    results.set(results.size() - 1, results.getLast() + s.toString());
                    readIndex += 1;
                }
                return readIndex;

        }

        return readIndex + 1;

    }

}
