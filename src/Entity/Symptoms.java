package Entity;

public class Symptoms {
    
    public static Severity assessSeverity(String symptoms) {
        String lowerSymptoms = symptoms.toLowerCase();
        
        // Check for emergency conditions
        if (containsEmergencySymptoms(lowerSymptoms)) {
            return Severity.EMERGENCY;
        }
        
        // Check for urgent conditions
        if (containsUrgentSymptoms(lowerSymptoms)) {
            return Severity.URGENT;
        }
        
        // All other conditions
        return Severity.MILD;
    }
    
    private static boolean containsEmergencySymptoms(String symptoms) {
        String[] emergencyKeywords = {
            "chest pain", 
            "difficulty breathing", 
            "unconscious",
            "severe bleeding",
            "heart attack",
            "stroke",
            "choking",
            "seizure"
        };
        
        for (String keyword : emergencyKeywords) {
            if (symptoms.contains(keyword)) {
                return true;
            }
        }
        return false;
    }
    
    private static boolean containsUrgentSymptoms(String symptoms) {
        String[] urgentKeywords = {
            "fever", 
            "fracture", 
            "pain",
            "vomiting",
            "diarrhea",
            "headache",
            "infection",
            "allergic reaction"
        };
        
        for (String keyword : urgentKeywords) {
            if (symptoms.contains(keyword)) {
                return true;
            }
        }
        return false;
    }
    
    public static String getPriorityDescription(Severity severity) {
        switch (severity) {
            case EMERGENCY:
                return "Highest Priority (Level 3) - Immediate attention required";
            case URGENT:
                return "Medium Priority (Level 2) - Requires prompt medical care";
            case MILD:
                return "Regular Priority (Level 1) - Routine medical consultation";
            default:
                return "Unknown Priority";
        }
    }
    
    public static String[] getEmergencySymptoms() {
        return new String[]{
            "chest pain", "difficulty breathing", "unconscious",
            "severe bleeding", "heart attack", "stroke", "choking", "seizure"
        };
    }
    
    public static String[] getUrgentSymptoms() {
        return new String[]{
            "fever", "fracture", "pain", "vomiting", "diarrhea", 
            "headache", "infection", "allergic reaction"
        };
    }
}