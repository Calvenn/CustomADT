package Control;
import adt.Heap; 
import Entity.Treatment; 
import java.time.Duration; 

//input data format validation should be in boundary, business logic validation in control 

public class TreatmentManager {
    private Heap<Treatment> providedTreatments;
    
    public TreatmentManager(Heap<Treatment> providedTreatments){
        this.providedTreatments = providedTreatments;
    }
    
    //if treatment names same, return true. else, return false
    public boolean treatmentExist(String treatmentName) {
        for(int i = 0; i < providedTreatments.size(); i++) {
            if(providedTreatments.get(i).getName().trim().equalsIgnoreCase(treatmentName)) {
                return true; 
            }
        }
        return false; 
    }
    
    public Treatment findTreatment(String treatmentName) {
        for(int i = 0; i < providedTreatments.size(); i++ ){
            if(providedTreatments.get(i).getName().equalsIgnoreCase(treatmentName)) {
                return providedTreatments.get(i);
            }  
        } return null;
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
        treatmentName = treatmentName.trim(); 
        if(treatmentExist(treatmentName)) throw new IllegalArgumentException("Treatment already exist."); 
        
        Treatment newTreatment = new Treatment(treatmentName, description, duration); 
        providedTreatments.insert(newTreatment); 
        return true; 
    }
    
    //get the most frequently done treatment 
    public Treatment getMostFrequentTreatment() {
        return providedTreatments.peekRoot(); 
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
