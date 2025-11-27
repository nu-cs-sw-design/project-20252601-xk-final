package domain.stylecheck;

import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static org.objectweb.asm.Opcodes.ASM7;

public class StyleRevisionClassVisitor extends ClassVisitor {

    private List<ClassStyleReviser> revisers = new ArrayList<>();
    private final StyleRevisionPrinter revisionPrinter;

    public StyleRevisionClassVisitor(OutputStream output) {
        super(ASM7);
        revisionPrinter = new BasicStyleRevisionPrinter(output);
    }

    public void addStyleReviser(ClassStyleReviser reviser) {
        revisers.add(reviser);
    }

    public void removeStyleReviser(ClassStyleReviser reviser) {
        revisers.remove(reviser);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        revisers.forEach(reviser -> reviser.checkClassName(getLocalClassName(name)));
    }

    @Override
    @Nullable
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        // TODO : distinguish static vs object field
        revisers.forEach(reviser -> reviser.checkVariableOrMethodName(name));
        return null;
    }

    @Override
    @Nullable
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        revisers.forEach(reviser -> reviser.checkVariableOrMethodName(name));
        return null;
    }

    @Override
    public void visitEnd() {
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

    @Nullable
    private String getLocalClassName(String internalName) {
        if (internalName.isEmpty() || internalName.endsWith("/")) {
            return null;
        }
        int cutoff = internalName.lastIndexOf('/');
        if (cutoff < 0) {
            return internalName;
        }
        return internalName.substring(cutoff + 1);
    }

}
