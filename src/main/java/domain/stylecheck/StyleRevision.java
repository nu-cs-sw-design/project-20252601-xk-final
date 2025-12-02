package domain.stylecheck;

class StyleRevision {

    private String originalName = "";
    private String issueType = "";
    private String issueDetails = "";
    private String suggestedFix = "";

    String getOriginalName() {
        return originalName;
    }

    String getIssueType() {
        return issueType;
    }

    String getIssueDetails() {
        return issueDetails;
    }

    String getSuggestedFix() {
        return suggestedFix;
    }

    static class Builder {

        private final StyleRevision revision = new StyleRevision();

        Builder withOriginalName(String originalName) {
            revision.originalName = originalName;
            return this;
        }

        Builder withIssueType(String issueType) {
            revision.issueType = issueType;
            return this;
        }

        Builder withIssueDetails(String issueDetails) {
            revision.issueDetails = issueDetails;
            return this;
        }

        Builder withSuggestedFix(String suggestedFix) {
            revision.suggestedFix = suggestedFix;
            return this;
        }

        StyleRevision build() {
            return revision;
        }

    }

}
