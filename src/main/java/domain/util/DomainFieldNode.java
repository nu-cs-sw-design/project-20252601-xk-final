package domain.util;

import java.util.ArrayList;
import java.util.List;

public class DomainFieldNode {

    String name;
    List<String> accessModifiers = new ArrayList<>();
    String type;

    DomainFieldNode() {}

    public String getName() {
        return name;
    }

    public List<String> getAccessModifiers() {
        return List.copyOf(accessModifiers);
    }

    public String getType () {
        return type;
    }

}
