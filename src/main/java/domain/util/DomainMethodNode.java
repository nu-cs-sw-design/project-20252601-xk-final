package domain.util;

import java.util.ArrayList;
import java.util.List;

public class DomainMethodNode {

    String name;
    List<String> accessModifiers = new ArrayList<>();
    List<String> parameterNames = new ArrayList<>();
    List<String> parameterTypes = new ArrayList<>();
    List<String> localVariableNames = new ArrayList<>();
    List<String> localVariableTypes = new ArrayList<>();
    String returnType;

    DomainMethodNode() {}

    public String getName() {
        return name;
    }

    public List<String> getAccessModifiers() {
        return List.copyOf(accessModifiers);
    }

    public List<String> getParameterNames() {
        return List.copyOf(parameterNames);
    }

    public List<String> getParameterTypes() {
        return List.copyOf(parameterTypes);
    }

    public List<String> getLocalVariableNames() {
        return List.copyOf(localVariableNames);
    }

    public List<String> getLocalVariableTypes() {
        return List.copyOf(localVariableTypes);
    }

    public int getParameterCount() {
        return parameterTypes.size();
    }

    public String getReturnType() {
        return returnType;
    }

}
