package domain.stylecheck;

import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InnerClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
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

    public void revise(ClassNode classNode) {

        reviseClassName(classNode.name);
        classNode.fields.forEach(this::reviseField);
        classNode.methods.forEach(this::reviseMethod);
        classNode.innerClasses.forEach(this::reviseInnerClass);

        printRevisions();

    }

    private void reviseClassName(String name) {
        revisers.forEach(reviser -> reviser.checkClassName(getLocalClassName(name)));
    }

    private void reviseField(FieldNode field) {

        // Define list of access constants that should use the static constant check
        List<Integer> constantFieldOpcodes = List.of(
                Opcodes.ACC_STATIC + Opcodes.ACC_FINAL,
                Opcodes.ACC_STATIC + Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL,
                Opcodes.ACC_STATIC + Opcodes.ACC_PRIVATE + Opcodes.ACC_FINAL,
                Opcodes.ACC_STATIC + Opcodes.ACC_PROTECTED + Opcodes.ACC_FINAL
        );

        // Do appropriate style check
        if (constantFieldOpcodes.contains(field.access)) {
            revisers.forEach(reviser -> reviser.checkStaticConstantName(field.name));
        }
        else {
            revisers.forEach(reviser -> reviser.checkVariableOrMethodName(field.name));
        }

    }

    private void reviseMethod(MethodNode method) {

        revisers.forEach(reviser -> reviser.checkVariableOrMethodName(method.name));

        if (method.parameters != null)
            method.parameters.forEach(parameterNode -> revisers.forEach(reviser -> reviser.checkVariableOrMethodName(parameterNode.name)));

        if (method.localVariables != null)
            method.localVariables.forEach(localVariableNode -> revisers.forEach(reviser -> reviser.checkVariableOrMethodName(localVariableNode.name)));

    }

    private void reviseInnerClass(InnerClassNode innerClass) {

        reviseClassName(innerClass.name);

    }

    private void printRevisions() {
        for (ClassStyleReviser reviser : revisers) {
            List<StyleRevision> revisions = reviser.getRevisions();
            revisions.forEach(revision -> {
                try {
                    revisionPrinter.print(revision);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    // Helper method for turning ASM internal class name into raw local class name as seen in source code
    @Nullable
    private String getLocalClassName(String internalName) {

        if (internalName.isEmpty() || internalName.endsWith("/")) {
            return null;
        }

        int cutoff = Math.max(internalName.lastIndexOf('/'), internalName.lastIndexOf('$'));
        if (cutoff < 0) {
            return internalName;
        }

        return internalName.substring(cutoff + 1);
    }

}
