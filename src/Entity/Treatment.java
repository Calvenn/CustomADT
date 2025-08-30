package Entity; 
import java.time.Duration; 

/**
 *
 * @author MeganYeohTzeXuan
 */

public class Treatment implements Comparable<Treatment> {
    private final String treatmentId; 
    private String name; 
    private String description; 
    private Duration duration; 
    private double price;
    private int frequency; 
    
    private static int count = 0; 
    
    public Treatment(String name, String description, Duration duration, double price) {
        this.treatmentId = String.format("T%04d", generateId());
        this.name = name; 
        this.description = description; 
        this.duration = duration; 
        this.price = price; 
        this.frequency = 0; 
    }
    
    public Treatment(String name, String description, Duration duration, double price, int frequency) {
        this.treatmentId = String.format("T%04d", generateId());
        this.name = name; 
        this.description = description; 
        this.duration = duration; 
        this.price = price;
        this.frequency = frequency; 
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
    
    public double getPrice() {
        return price; 
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
    
    public void setPrice(Double price) {
        this.price = price; 
    }
    
    public void doneTreatment() {
        this.frequency++; 
    }
    
    public Duration getTimeAllocation() {
        return duration.multipliedBy(frequency);
    }
    
    public double getEarned() {
        return frequency * price; 
    }
    
    @Override
    public int compareTo(Treatment other) {
        return Integer.compare(this.frequency, other.frequency); 
    }
    
    //id, name, description, duration, frequency 
    @Override
    public String toString() {
        return String.format("""
            %-16s: %s
            %-16s: %s
            %-16s: %s
            %-16s: %s
            %-16s: %s
            %-16s: %s
            """, 
                "Treatment ID", treatmentId, 
                "Treatment Name", name, 
                "Description", description, 
                "Duration", duration.toMinutes() + " minutes", 
                "Price", String.format("RM %,.02f", price), 
                "Frequency", frequency); 
    }
}