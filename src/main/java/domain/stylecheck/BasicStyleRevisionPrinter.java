package domain.stylecheck;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class BasicStyleRevisionPrinter extends StyleRevisionPrinter {

    BasicStyleRevisionPrinter(OutputStream output) {
        super(output);
    }

    @Override
    void print(StyleRevision revision) throws IOException {

        print("\n");

        String originalName = revision.getOriginalName();
        String issueType = revision.getIssueType();
        String issueDetails = revision.getIssueDetails();
        String suggestedFix = revision.getSuggestedFix();

        if (!originalName.isEmpty()) {
            print("IDENTIFIER:\n");
            print(originalName);
            print("\n");
        }

        if (!issueType.isEmpty()) {
            print("ISSUE TYPE:\n");
            print(issueType);
            print("\n");
        }

        if (!issueDetails.isEmpty()) {
            print("ISSUE DETAILS:\n");
            print(issueDetails);
            print("\n");
        }

        if (!suggestedFix.isEmpty()) {
            print("SUGGESTED FIX:\n");
            print(suggestedFix);
            print("\n");
        }

    }

}
