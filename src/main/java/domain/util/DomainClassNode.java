package domain.util;

import java.util.ArrayList;
import java.util.List;

public class DomainClassNode {

    String name;
    String superName;
    List<String> interfaces = new ArrayList<>();
    List<String> accessModifiers = new ArrayList<>();
    List<DomainFieldNode> fields = new ArrayList<>();
    List<DomainMethodNode> methods = new ArrayList<>();
    List<DomainInnerClassNode> innerClasses = new ArrayList<>();

    DomainClassNode() {}

    public String getName() {
        return name;
    }

    public String getSuperName() {
        return superName;
    }

    public List<String> getInterfaces() {
        return interfaces;
    }

    public List<String> getAccessModifiers() {
        return List.copyOf(accessModifiers);
    }

    public List<DomainFieldNode> getFields() {
        return List.copyOf(fields);
    }

    public List<DomainMethodNode> getMethods() {
        return List.copyOf(methods);
    }

    public List<DomainInnerClassNode> getInnerClasses() {
        return List.copyOf(innerClasses);
    }

}
