package domain.util;

import java.util.ArrayList;
import java.util.List;

public class DomainInnerClassNode {

    String name = null;
    List<String> accessModifiers = new ArrayList<>();

    DomainInnerClassNode() {}

    public String getName() {
        return name;
    }

    public List<String> getAccessModifiers() {
        return List.copyOf(accessModifiers);
    }

}
