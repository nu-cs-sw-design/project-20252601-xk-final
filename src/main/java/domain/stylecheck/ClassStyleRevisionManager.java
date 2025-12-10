package domain.stylecheck;

import domain.util.DomainClassNode;
import domain.util.DomainFieldNode;
import domain.util.DomainInnerClassNode;
import domain.util.DomainMethodNode;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ClassStyleRevisionManager {

    private List<ClassStyleReviser> revisers = new ArrayList<>();
    private final StyleRevisionPrinter revisionPrinter;

    public ClassStyleRevisionManager(OutputStream output) {
        this.revisionPrinter = new BasicStyleRevisionPrinter(output);
    }

    public void addStyleReviser(ClassStyleReviser reviser) {
        revisers.add(reviser);
    }

    public void removeStyleReviser(ClassStyleReviser reviser) {
        revisers.remove(reviser);
    }

    public void revise(DomainClassNode classNode) {

        reviseClassName(classNode.getName());
        classNode.getFields().forEach(this::reviseField);
        classNode.getMethods().forEach(this::reviseMethod);
        classNode.getInnerClasses().forEach(this::reviseInnerClass);

        try {
            printRevisions();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void revise(List<DomainClassNode> classNodes) {
        classNodes.forEach(this::revise);
    }

    private void reviseClassName(String name) {
        revisers.forEach(reviser -> reviser.checkClassName(getLocalClassName(name)));
    }

    private void reviseField(DomainFieldNode field) {

        // Do appropriate style check
        if (new HashSet<>(field.getAccessModifiers()).containsAll(List.of("static", "final"))) {
            revisers.forEach(reviser -> reviser.checkStaticConstantName(field.getName()));
        }
        else {
            revisers.forEach(reviser -> reviser.checkVariableOrMethodName(field.getName()));
        }

    }

    private void reviseMethod(DomainMethodNode method) {

        revisers.forEach(reviser -> reviser.checkVariableOrMethodName(method.getName()));

        method.getParameterNames().forEach(parameterName -> revisers.forEach(reviser -> reviser.checkVariableOrMethodName(parameterName)));

        method.getLocalVariableNames().forEach(localVariableName -> revisers.forEach(reviser -> reviser.checkVariableOrMethodName(localVariableName)));

    }

    private void reviseInnerClass(DomainInnerClassNode innerClass) {

        reviseClassName(innerClass.getName());

    }

    private void printRevisions() throws IOException {
        int revisionCount = 0;
        for (ClassStyleReviser reviser : revisers) {
            List<StyleRevision> revisions = reviser.getRevisions();
            for (StyleRevision revision : revisions) {
                revisionCount++;
                revisionPrinter.print(revision);
            }
        }
        if (revisionCount == 0) {
            revisionPrinter.print("There are no revisions!");
        }
    }

    // Helper method for turning ASM internal class name into raw local class name as seen in source code
    @Nullable
    private String getLocalClassName(String fullName) {

        if (fullName.isEmpty()) {
            return null;
        }

        int cutoff = fullName.lastIndexOf('.');
        if (cutoff < 0) {
            return fullName;
        }

        return fullName.substring(cutoff + 1);
    }

}
