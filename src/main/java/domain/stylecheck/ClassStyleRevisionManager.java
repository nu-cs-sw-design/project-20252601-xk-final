package domain.stylecheck;

import domain.util.DomainClassNode;
import domain.util.DomainFieldNode;
import domain.util.DomainInnerClassNode;
import domain.util.DomainMethodNode;

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

    public void generateReport(List<DomainClassNode> classNodes) {
        classNodes.forEach(this::revise);

        try {
            printRevisions();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void revise(DomainClassNode classNode) {

        reviseClassName(classNode);
        classNode.getFields().forEach(this::reviseField);
        classNode.getMethods().forEach(this::reviseMethod);
        classNode.getInnerClasses().forEach(this::reviseInnerClass);

    }

    private void reviseClassName(DomainClassNode classNode) {
        revisers.forEach(reviser -> reviser.checkClassName(classNode.getLocalName()));
    }

    private void reviseClassName(DomainInnerClassNode innerClassNode) {
        revisers.forEach(reviser -> reviser.checkClassName(innerClassNode.getLocalName()));
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

        reviseClassName(innerClass);

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
            revisionPrinter.print("There are no revisions!\n");
        }
    }

}
