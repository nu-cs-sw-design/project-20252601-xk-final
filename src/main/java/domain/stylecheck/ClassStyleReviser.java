package domain.stylecheck;

import java.util.ArrayList;
import java.util.List;

public abstract class ClassStyleReviser {

    private List<StyleRevision> revisions = new ArrayList<>();

    final boolean checkClassName(String name) {
        validateNameInput(name);
        StyleRevision.Builder revisionBuilder = new StyleRevision.Builder().withOriginalName("\"" + name + "\"");
        revisionBuilder.withIssueType("Class Name");
        return checkClassNameInternal(name, revisionBuilder);
    }

    final boolean checkVariableOrMethodName(String name) {
        validateNameInput(name);
        StyleRevision.Builder revisionBuilder = new StyleRevision.Builder().withOriginalName("\"" + name + "\"");
        revisionBuilder.withIssueType("Variable/Method Name");
        return checkVariableOrMethodNameInternal(name, revisionBuilder);
    }

    final boolean checkStaticConstantName(String name) {
        validateNameInput(name);
        StyleRevision.Builder revisionBuilder = new StyleRevision.Builder().withOriginalName("\"" + name + "\"");
        revisionBuilder.withIssueType("Static Constant Name");
        return checkStaticConstantNameInternal(name, revisionBuilder);
    }

    abstract boolean checkClassNameInternal(String name, StyleRevision.Builder revisionBuilder);

    abstract boolean checkVariableOrMethodNameInternal(String name, StyleRevision.Builder revisionBuilder);

    abstract boolean checkStaticConstantNameInternal(String name, StyleRevision.Builder revisionBuilder);

    final void saveRevision(StyleRevision revision) {
        revisions.add(revision);
    }

    final List<StyleRevision> getRevisions() {
        return List.copyOf(revisions);
    }

    private void validateNameInput(String name) throws IllegalArgumentException {
        if (name.isBlank()) {
            throw new IllegalArgumentException("Empty name");
        }
    }

}
