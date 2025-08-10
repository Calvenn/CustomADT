package Entity;

public enum Severity{
    EMERGENCY(3),
    URGENT(2),
    MILD(1),;

    private final int severity;
    
    Severity(int severity) {
        this.severity = severity;
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

    @Override
    public String toString() {
        return this.name();
    }
}