package domain.classdiagram;

import domain.util.DomainClassNode;
import domain.util.DomainFieldNode;
import domain.util.DomainMethodNode;
import domain.util.DomainTypeUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlantUMLGenerator {

    OutputStream output;
    boolean includeExternalDependencies;

    public PlantUMLGenerator(OutputStream output, boolean includeExternalDependencies) {
        this.output = output;
        this.includeExternalDependencies = includeExternalDependencies;
    }

    public void generateClassDiagram(List<DomainClassNode> classNodes) throws IOException {

        StringBuilder result = new StringBuilder();

        result.append("@startuml\n\n");

        for (DomainClassNode node : classNodes) {
            result.append(generateClass(node));
        }

        result.append(generateDependencies(classNodes));

        result.append("@enduml\n");

        output.write(result.toString().getBytes(StandardCharsets.UTF_8));
    }

    private String generateClass(DomainClassNode node) {

        StringBuilder result = new StringBuilder();

        String accessModifierTokens = getAccessModifierTokens(node.getAccessModifiers());

        result.append(accessModifierTokens);
        result.append(node.getName() + "\n");

        result.append("{\n");

        node.getFields().forEach(fieldNode -> result.append("    " + generateField(fieldNode)));
        result.append("\n");
        node.getMethods().forEach(methodNode -> result.append("    " + generateMethod(methodNode)));
        result.append("}\n");

        return result.toString();

    }

    private String generateField(DomainFieldNode field) {

        StringBuilder result = new StringBuilder();

        String accessModifierTokens = getAccessModifierTokens(field.getAccessModifiers());

        result.append(accessModifierTokens);

        result.append(field.getName()).append(" : ").append(field.getType());

        result.append("\n");

        return result.toString();

    }

    private String generateMethod(DomainMethodNode method) {

        StringBuilder result = new StringBuilder();

        String accessModifierTokens = getAccessModifierTokens(method.getAccessModifiers());

        result.append(accessModifierTokens);

        result.append(method.getName());

        result.append("(");

        List<String> parameterNames = method.getParameterNames();
        List<String> parameterTypes = method.getParameterTypes();
        boolean showNames = false;
        if (parameterTypes.size() == parameterNames.size())
            showNames = true;

        for (int i = 0; i < method.getParameterCount(); i++) {
            if (showNames) {
                result.append(parameterNames.get(i));
                result.append(" : ");
            }
            result.append(parameterTypes.get(i));
            if (i != method.getParameterCount() - 1)
                result.append(", ");
        }

        result.append(")");

        String returnType = method.getReturnType();
        if (!returnType.isEmpty()) {
            result.append(" : ");
            result.append(returnType);
        }

        result.append("\n");

        return result.toString();

    }

    private String generateDependencies(List<DomainClassNode> classNodes) {

        Set<String> classNames = new HashSet<>();
        classNodes.forEach(classNode -> classNames.add(classNode.getName()));

        StringBuilder result = new StringBuilder();

        result.append("\n");

        for (DomainClassNode classNode : classNodes) {

            // Extension
            if (includeExternalDependencies || classNames.contains(classNode.getSuperName())) {
                result.append(classNode.getName() + " --|> " + classNode.getSuperName());
                result.append("\n");
            }

            // Implementation
            for (var interfaceName : classNode.getInterfaces()) {
                if (includeExternalDependencies || classNames.contains(interfaceName)) {
                    result.append(classNode.getName() + " ..|> " + interfaceName);
                    result.append("\n");
                }
            }

            // Containment
            for (var field : classNode.getFields()) {
                List<String> involvedTypes = DomainTypeUtil.splitIntoSignificantTypes(field.getType());
                for (var typeName : involvedTypes) {
                    if (includeExternalDependencies || classNames.contains(typeName)) {
                        result.append(classNode.getName() + " --> " + typeName);
                        result.append("\n");
                    }
                }
            }

            // Association
            for (var method : classNode.getMethods()) {

                Set<String> involvedTypes = new HashSet<>();
                involvedTypes.addAll(DomainTypeUtil.splitIntoSignificantTypes(method.getReturnType()));
                for (var parameterType : method.getParameterTypes()) {
                    involvedTypes.addAll(DomainTypeUtil.splitIntoSignificantTypes(parameterType));
                }
                for (var localVariableType : method.getLocalVariableTypes()) {
                    involvedTypes.addAll(DomainTypeUtil.splitIntoSignificantTypes(localVariableType));
                }

                for (var typeName : involvedTypes) {
                    if ((includeExternalDependencies || classNames.contains(typeName)) && !classNode.getName().equals(typeName) && !typeName.isEmpty()) {
                        result.append(classNode.getName() + " ..> " + typeName);
                        result.append("\n");
                    }
                }

            }

        }

        result.append("\n");

        return result.toString();
    }

    // Helper method for creating PlantUML access modifier strings
    private String getAccessModifierTokens(List<String> accessModifiers) {
        return getAccessModifierTokens(accessModifiers, false);
    }
    private String getAccessModifierTokens(List<String> accessModifiers, boolean className) {

        String scopeToken;
        if (accessModifiers.contains("public")) {
            scopeToken = "+";
        }
        else if (accessModifiers.contains("private")) {
            scopeToken = "-";
        }
        else if (accessModifiers.contains("protected")) {
            scopeToken = "#";
        }
        else {
            scopeToken = "~";
        }

        StringBuilder accessModifierStringBuilder = new StringBuilder();

        accessModifierStringBuilder.append(scopeToken);

        if (accessModifiers.contains("static")) {
            accessModifierStringBuilder.append("{static} ");
        }

        if (accessModifiers.contains("final")) {
            accessModifierStringBuilder.append("{final} ");
        }
        else if (accessModifiers.contains("abstract")) {
            if (className)
                accessModifierStringBuilder.append("abstract ");
            else
                accessModifierStringBuilder.append("{abstract} ");
        }

        if (className) {
            if (accessModifiers.contains("interface"))
                accessModifierStringBuilder.append("interface ");
            else
                accessModifierStringBuilder.append("class ");
        }

        return accessModifierStringBuilder.toString();
    }


}
