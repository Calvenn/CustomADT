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
        String[] urgentKeywords = {
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
            "chest pain",
            "shortness of breath", "difficulty breathing",
            "unconscious", "unresponsive",
            "fracture", 
            "dengue", 
            "heart disease",
            "stroke", "cancer",
            "seizure", "convulsion", "epilepsy",
            "allergic reaction",
            "anaphylaxis",
            "dehydration",
            "accident", "injury",
            "influenza", "covid",
            "pregnancy checkup", 
            "muscle tears", "sprains",
            "open wounds", "deep cuts", "fall down", "scratches",
            "fever", "asthma",
            "diabetes", "hypertension",
            "abdominal pain", "severe abdominal pain", "stomach pain",
            "dizziness", "fainting", "blackout",
            "sports injuries", "swelling", "inflammation", "warts", "moles",
            "eye pain", "redness", "blurred vision", "stye", 
            "high blood pressure", "high cholesterol", 
        };
        
        for (String keyword : urgentKeywords) {
            if (symptoms.toLowerCase().contains(keyword.toLowerCase())) {
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
            "light",
            "low fever",
            "cough",
            "sore throat",
            "runny nose",
            "cold",
            "allergy",
            "itchy",
            "diarrhea",
            "constipation",
            "body checkup",
            "vaccination required", "requires vaccination", "just come clinic"
        };
        
        for (String keyword : mildKeywords) {
            if (symptoms.toLowerCase().contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    } 
}