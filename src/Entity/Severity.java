package Entity;

public enum Severity{
    EMERGENCY(3, "Immediate attention required"),
    URGENT(2, "Urgent attention required"),
    MILD(1, "Standard attention required"),;

    private final int severity;
    private final String description;
    
    Severity(int severity, String description) {
        this.severity = severity;
        this.description = description;
    }
    
    public static Severity fromValue(int value) {
        for (Severity s : Severity.values()) {
            if (s.getSeverity() == value) {
                return s;
            }
        }
        return null;
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