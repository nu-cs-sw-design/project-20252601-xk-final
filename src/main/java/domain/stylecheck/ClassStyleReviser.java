package domain.stylecheck;

import java.util.ArrayList;
import java.util.List;

public abstract class ClassStyleReviser {

    private List<StyleRevision> revisions = new ArrayList<>();

    abstract boolean checkClassName(String name);

    abstract boolean checkVariableOrMethodName(String name);

    abstract boolean checkStaticConstantName(String name);

    final void saveRevision(StyleRevision revision) {
        revisions.add(revision);
    }

    final List<StyleRevision> getRevisions() {
        return List.copyOf(revisions);
    }

    final void validateNameInput(String name) throws IllegalArgumentException {
        if (name.isBlank()) {
            throw new IllegalArgumentException("Empty name");
        }
    }

}
