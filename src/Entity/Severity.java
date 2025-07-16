package Entity;

public enum Severity{
    EMERGENCY(3, "Emergency: Immediate attention required"),
    URGENT(2, "Serious: Urgent attention required"),
    MILD(1, "Mild: Standard attention required"),;

    private final int severity;
    private final String description;
    
    Severity(int severity, String description) {
        this.severity = severity;
        this.description = description;
    }

    public int getSeverity() {
        return severity;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return this.name() + " (" + description + ")";
    }
}