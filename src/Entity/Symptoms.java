package Entity;

public class Symptoms {
    
    public static Severity assessSeverity(String symptoms, boolean isLifeThreatening) {
        String lowerSymptoms = symptoms.toLowerCase();
        
        // If user indicated life-threatening, directly EMERGENCY=
        if (isLifeThreatening) {
            return Severity.EMERGENCY;
        }
        
        // Check for emergency symptoms first
        if (containsUrgentSymptoms(lowerSymptoms)) {
            return Severity.URGENT;
        }
        
        // If not emergency, check if mild or default to mild
        if (containsMildKeywords(lowerSymptoms)) {
            return Severity.MILD;
        }
        
        // Default case
        return Severity.MILD;
    }
    
    private static boolean containsUrgentSymptoms(String symptoms) {
        String[] emergencyKeywords = {
            "severe", 
            "persistent", 
            "high fever",
            "39",
            "vomiting",
            "bleeding",
            "cannot eat",
            "worsening",
            "getting worse",
            "continuous",
            "heavy",
            "chest pain"
        };
        
        for (String keyword : emergencyKeywords) {
            if (symptoms.contains(keyword)) {
                return true;
            }
        }
        return false;
    }
    
    private static boolean containsMildKeywords(String symptoms) {
        String[] mildKeywords = {
            "mild",
            "light headache",
            "slight pain",
            "slight",
            "occasional",
            "sometimes",
            "minor",
            "little",
            "small",
            "light"
        };
        
        for (String keyword : mildKeywords) {
            if (symptoms.contains(keyword)) {
                return true;
            }
        }
        return false;
    }  
}