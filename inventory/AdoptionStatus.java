package inventory;

public enum AdoptionStatus {
    AVAILABLE("Available"),
    PENDING_REVIEW("Pending Review"),
    RESERVED("Reserved"),
    ADOPTED("Adopted"),
    RETURN_PENDING("Pending Return"),
    RETURNED("Returned");

    private final String label;

    AdoptionStatus(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
