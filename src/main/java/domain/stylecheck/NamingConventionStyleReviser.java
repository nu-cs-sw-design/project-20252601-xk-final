package domain.stylecheck;

public class NamingConventionStyleReviser extends ClassStyleReviser {

    @Override
    boolean checkClassNameInternal(String name, StyleRevision.Builder revisionBuilder) {

        revisionBuilder.withIssueDetails("Class name should be PascalCase.");

        char[] nameCharArray = name.toCharArray();
        StringBuilder fixedNameBuilder = new StringBuilder();

        // Copy the name into the new list with some checks
        boolean foundLetter = false;
        for (char c : nameCharArray) {

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

        revisionBuilder.withSuggestedFix("Potentially change name to \"" + fixedName + "\".");
        saveRevision(revisionBuilder.build());
        return false;

    }

    @Override
    boolean checkVariableOrMethodNameInternal(String name, StyleRevision.Builder revisionBuilder) {

        revisionBuilder.withIssueDetails("Variable/method name should be camelCase.");

        char[] nameCharArray = name.toCharArray();
        StringBuilder fixedNameBuilder = new StringBuilder();

        // Copy the name into the new list with some checks
        boolean foundLetter = false;
        for (char c : nameCharArray) {

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

        revisionBuilder.withSuggestedFix("Potentially change name to \"" + fixedName + "\".");
        saveRevision(revisionBuilder.build());
        return false;

    }

    @Override
    boolean checkStaticConstantNameInternal(String name, StyleRevision.Builder revisionBuilder) {

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
            // Insert '_' if letter is already uppercase and previous letter is lowercase
            if (Character.isLetter(c)) {
                if (i > 0 && Character.isUpperCase(c) && Character.isLowerCase(nameCharArray[i - 1])) {
                    fixedNameBuilder.append('_');
                }
                else {
                    c = Character.toUpperCase(c);
                }
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

        revisionBuilder.withSuggestedFix("Potentially change name to \"" + fixedName + "\".");
        saveRevision(revisionBuilder.build());
        return false;

    }

}
