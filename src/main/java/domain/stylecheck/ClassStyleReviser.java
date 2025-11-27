package domain.stylecheck;

import java.util.ArrayList;
import java.util.List;

public abstract class ClassStyleReviser implements ClassStyleChecker {

    private List<StyleRevision> revisions = new ArrayList<>();

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
