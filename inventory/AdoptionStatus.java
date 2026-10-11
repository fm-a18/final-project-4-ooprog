package inventory;

public enum AdoptionStatus {
    AVAILABLE("Available"),
    ADOPTION_REVIEW("Pending Adoption"),
    RESERVED("Reserved"),
    CANCELLATION_REVIEW("Pending Cancellation"),
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
