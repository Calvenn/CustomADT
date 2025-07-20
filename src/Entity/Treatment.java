package Entity; 
import java.time.Duration; 

public class Treatment implements Comparable<Treatment> {
    private final String treatmentId; 
    private String name; 
    private String description; 
    private Duration duration; 
    private int frequency; 
    
    private static int count = 0; 
    
//    private static String[] treatments = {
//        "X-ray", 
//        "Blood test", 
//        "Urine test", 
//        "Vaccination",
//        "Physical therapy", 
//        "Ultrasound", 
//        "Allertic test", 
//        "Wound care", 
//        "IV Fluid therapy", 
//        "Nebuliser", 
//        "Cryotherapy", 
//    }; 
    
    public Treatment(String name, String description, Duration duration) {
        this.treatmentId = String.format("T%04d", generateId());
        this.name = name; 
        this.description = description; 
        this.duration = duration; 
        this.frequency = 0; 
    }
    
    private static int generateId() {
        return count += 1; 
    }
    
    public String getTreatmentId() {
        return treatmentId; 
    }
    
    public String getName() {
        return name; 
    }
    
    public String getDescription() {
        return description; 
    }
    
    public Duration getDuration() {
        return duration; 
    }
    
    public int getFrequency() {
        return frequency; 
    }
    
    public void setName(String name) {
        this.name = name; 
    }
    
    public void setDescription(String description) {
        this.description = description; 
    }
    
    public void setDuration(Duration duration) {
        this.duration = duration; 
    }
    
    public void doneTreatment() {
        this.frequency++; 
    }
    
    @Override
    public int compareTo(Treatment other) {
        return Integer.compare(this.frequency, other.frequency); 
    }
}