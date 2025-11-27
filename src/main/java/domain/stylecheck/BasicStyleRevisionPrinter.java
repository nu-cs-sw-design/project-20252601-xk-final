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

        output.write("\n".getBytes(StandardCharsets.UTF_8));

        String issueType = revision.getIssueType();
        String issueDetails = revision.getIssueDetails();
        String suggestedFix = revision.getSuggestedFix();

        if (!issueType.isEmpty()) {
            output.write("ISSUE TYPE:\n".getBytes(StandardCharsets.UTF_8));
            output.write(issueType.getBytes(StandardCharsets.UTF_8));
            output.write("\n".getBytes(StandardCharsets.UTF_8));
        }

        if (!issueDetails.isEmpty()) {
            output.write("ISSUE DETAILS:\n".getBytes(StandardCharsets.UTF_8));
            output.write(issueDetails.getBytes(StandardCharsets.UTF_8));
            output.write("\n".getBytes(StandardCharsets.UTF_8));
        }

        if (!suggestedFix.isEmpty()) {
            output.write("SUGGESTED FIX:\n".getBytes(StandardCharsets.UTF_8));
            output.write(suggestedFix.getBytes(StandardCharsets.UTF_8));
            output.write("\n".getBytes(StandardCharsets.UTF_8));
        }

    }

}
