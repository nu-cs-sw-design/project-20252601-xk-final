package domain.stylecheck;

public class NamingConventionStyleReviser extends ClassStyleReviser {

    @Override
    public boolean checkClassName(String name) {

        validateNameInput(name);

        StyleRevision.Builder revisionBuilder = new StyleRevision.Builder();
        revisionBuilder.withIssueType("Class Name");
        revisionBuilder.withIssueDetails("Class name should be PascalCase.");

        char[] nameCharArray = name.toCharArray();
        StringBuilder fixedNameBuilder = new StringBuilder();
        int length = nameCharArray.length;

        // Copy the name into the new list with some checks
        boolean foundLetter = false;
        for (int i = 0; i < length; i++) {

            char c = nameCharArray[i];

            // Skip bad characters
            if (c == '_' || c == '$')
                continue;

            // Check if character is letter and record that
            if (Character.isLetter(c)) {
                foundLetter = true;
            }

            // Add characters to list
            fixedNameBuilder.append(c);

        }

        // Stop if there were no letters at all in the class name
        if (!foundLetter) {
            revisionBuilder.withSuggestedFix("Choose a class name with alphabetic characters.");
            saveRevision(revisionBuilder.build());
            return false;
        }

        // Ensure first letter is uppercase
        fixedNameBuilder.setCharAt(0, Character.toUpperCase(fixedNameBuilder.charAt(0)));

        // Build revised string
        String fixedName = fixedNameBuilder.toString();

        // Finish if the revised string is the same as the original
        if (fixedName.equals(name)) {
            return true;
        }

        revisionBuilder.withSuggestedFix("Potentially change name to " + fixedName);
        saveRevision(revisionBuilder.build());
        return false;

    }

    @Override
    public boolean checkVariableOrMethodName(String name) {

        validateNameInput(name);

        StyleRevision.Builder revisionBuilder = new StyleRevision.Builder();
        revisionBuilder.withIssueType("Variable/Method Name");
        revisionBuilder.withIssueDetails("Variable/method name should be camelCase.");

        char[] nameCharArray = name.toCharArray();
        StringBuilder fixedNameBuilder = new StringBuilder();
        int length = nameCharArray.length;

        // Copy the name into the new list with some checks
        boolean foundLetter = false;
        for (int i = 0; i < length; i++) {

            char c = nameCharArray[i];

            // Skip bad characters
            if (c == '_' || c == '$')
                continue;

            // Check if character is letter and record that
            if (Character.isLetter(c)) {
                foundLetter = true;
            }

            // Add characters to list
            fixedNameBuilder.append(c);

        }

        // Stop if there were no letters at all in the class name
        if (!foundLetter) {
            revisionBuilder.withSuggestedFix("Choose a name containing alphabetic characters.");
            saveRevision(revisionBuilder.build());
            return false;
        }

        // Ensure first letter is uppercase
        fixedNameBuilder.setCharAt(0, Character.toLowerCase(fixedNameBuilder.charAt(0)));

        // Build revised string
        String fixedName = fixedNameBuilder.toString();

        // Finish if the revised string is the same as the original
        if (fixedName.equals(name)) {
            return true;
        }

        revisionBuilder.withSuggestedFix("Potentially change name to " + fixedName);
        saveRevision(revisionBuilder.build());
        return false;

    }

    @Override
    public boolean checkStaticConstantName(String name) {

        validateNameInput(name);

        StyleRevision.Builder revisionBuilder = new StyleRevision.Builder();
        revisionBuilder.withIssueType("Static Constant Name");
        revisionBuilder.withIssueDetails("Constant name should be UPPER_SNAKE_CASE.");

        char[] nameCharArray = name.toCharArray();
        StringBuilder fixedNameBuilder = new StringBuilder();
        int length = nameCharArray.length;

        // Copy the name into the new list with some checks
        boolean foundLetter = false;
        for (int i = 0; i < length; i++) {

            char c = nameCharArray[i];

            // Turn $ into _
            if (c == '$') {
                fixedNameBuilder.append('_');
                continue;
            }

            // Check if character is letter and record that
            // Make letters uppercase
            if (Character.isLetter(c)) {
                c = Character.toUpperCase(c);
                foundLetter = true;
            }

            // Add characters to list
            fixedNameBuilder.append(c);

        }

        // Stop if there were no letters at all in the class name
        if (!foundLetter) {
            revisionBuilder.withSuggestedFix("Choose a name containing alphabetic characters.");
            saveRevision(revisionBuilder.build());
            return false;
        }

        // Delete instances of multiple underscores right after one another
        while (fixedNameBuilder.indexOf("__") != -1) {
            fixedNameBuilder.deleteCharAt(fixedNameBuilder.indexOf("__"));
        }

        // Delete leading and trailing underscores
        if (fixedNameBuilder.charAt(0) == '_') {
            fixedNameBuilder.deleteCharAt(0);
        }
        if (fixedNameBuilder.charAt(length - 1) == '_') {
            fixedNameBuilder.deleteCharAt(length - 1);
        }

        // Build revised string
        String fixedName = fixedNameBuilder.toString();

        // Finish if the revised string is the same as the original
        if (fixedName.equals(name)) {
            return true;
        }

        revisionBuilder.withSuggestedFix("Potentially change name to " + fixedName);
        saveRevision(revisionBuilder.build());
        return false;

    }

}
