package Control;
import adt.LinkedHashMap;
import Entity.Treatment; 
import exception.InvalidInputException;
import java.time.Duration; 

//input data format validation should be in boundary, business logic validation in control 

public class TreatmentManager {
//    private Heap<Treatment> providedTreatments;
    private LinkedHashMap<String, Treatment> providedTreatments; 
    
    public TreatmentManager() {
//        providedTreatments = new Heap<>(true); 
        providedTreatments = new LinkedHashMap<>();
    }
    
    public TreatmentManager(LinkedHashMap<String, Treatment> providedTreatments){
        this.providedTreatments = providedTreatments;
    }
    
    //if treatment names same, return true. else, return false
    public boolean treatmentExist(String treatmentName) {
        return providedTreatments.containsKey(treatmentName.toLowerCase());
    }
    
    public boolean changeDescription(Treatment treatment, String description) {
        if(treatment.getDescription().equalsIgnoreCase(description)) {
            return false; 
        }
        treatment.setDescription(description); 
        return true; 
    }
    
    public boolean changeDuration(Treatment treatment, Duration duration) {
        if(treatment.getDuration().equals(duration)) {
            return false; 
        }
        treatment.setDuration(duration); 
        return true; 
    }
    
    public boolean checkIDFormat(String id) {
        return id.matches("^T\\d{4}$");
    }
    
    public Treatment findTreatmentName(String treatmentName) {
        return providedTreatments.get(treatmentName.toLowerCase());
    }
    
//        if(isNull(treatmentName) || treatmentName.trim().isEmpty()) throw new IllegalArgumentException("Treatment Name cannot be empty.");
//        if(isNull(description)) throw new IllegalArgumentException("Description cannot be null.");
//        if(isNull(duration)) throw new IllegalArgumentException("Duration cannot be null.");
//        if(duration.isNegative() || duration.isZero()) throw new IllegalArgumentException("Duration cannot be negative or zero."); 

//    public boolean isNull(Object obj) {
//        return obj == null; 
//    }
    
    //trigger to add new treatment to heap list 
    public boolean newTreatment(String treatmentName, String description, Duration duration) {
        treatmentName = treatmentName.substring(0, 1).toUpperCase() + treatmentName.substring(1); 
        Treatment newTreatment = new Treatment(treatmentName.trim(), description, duration); 
        providedTreatments.put(treatmentName.toLowerCase(), newTreatment); 
        return true; 
    }
    
    public String[] getTreatmentNames() {
        return (String[]) providedTreatments.getKeys();
    }
    
    //show all available treatments 
    public void displayAllTreatments() {
        providedTreatments.display(); 
    }
    
    //return total amount of treatments provided 
    public int totalTreatments() {
        return providedTreatments.size(); 
    }
    
}
