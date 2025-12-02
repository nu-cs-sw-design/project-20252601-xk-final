package domain.stylecheck;

public class ReadableNamingStyleReviser extends ClassStyleReviser {
    
    @Override
    boolean checkClassNameInternal(String name, StyleRevision.Builder revisionBuilder) {
        return checkReadability(name, revisionBuilder);
    }

    @Override
    boolean checkVariableOrMethodNameInternal(String name, StyleRevision.Builder revisionBuilder) {
        return checkReadability(name, revisionBuilder);
    }

    @Override
    boolean checkStaticConstantNameInternal(String name, StyleRevision.Builder revisionBuilder) {
        return checkReadability(name, revisionBuilder);
    }
    
    private boolean checkReadability(String name, StyleRevision.Builder revisionBuilder) {

        // Analyze name
        boolean readable = isReadable(name);

        // Stop if no issues were found
        if (readable) {
            return true;
        }

        // Save revision
        revisionBuilder.withIssueType("Identifier Readability");
        revisionBuilder.withIssueDetails("Identifier name may not be very readable.");
        saveRevision(revisionBuilder.build());
        return false;

    }

    private boolean isReadable(String name) {

        char[] nameCharArray = name.toCharArray();
        int length = nameCharArray.length;
        boolean readable = true;

        for (int i = 0; i < length; i++) {

            // Check for a series of characters that are the same
            if (i >= 2) {
                char c0 = nameCharArray[i - 2];
                char c1 = nameCharArray[i - 1];
                char c2 = nameCharArray[i];
                if (c0 == c1 && c0 == c2) {
                    readable = false;
                }
            }

        }

        return readable;

    }

}
