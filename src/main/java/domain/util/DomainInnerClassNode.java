package domain.util;

import java.util.ArrayList;
import java.util.List;

public class DomainInnerClassNode {

    String name;
    String localName;
    List<String> accessModifiers = new ArrayList<>();

    DomainInnerClassNode() {}

    public String getName() {
        return name;
    }

    public String getLocalName() {
        return localName;
    }

    public List<String> getAccessModifiers() {
        return List.copyOf(accessModifiers);
    }

}
