package Control;
import adt.LinkedHashMap;
import adt.Heap; 
import adt.List; 
import Entity.Treatment; 
import java.time.Duration; 

/**
 *
 * @author MeganYeohTzeXuan
 */

public class TreatmentManager {
    private final LinkedHashMap<String, Treatment> providedTreatments; 
    
    public TreatmentManager() {
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
    
    public boolean changePrice(Treatment treatment, double price) {
        if(treatment.getPrice() == price) {
            return false; 
        }
        treatment.setPrice(price); 
        return true; 
    }
    
    public Treatment findTreatmentName(String treatmentName) {
        return providedTreatments.get(treatmentName.toLowerCase());
    }
    
    //trigger to add new treatment 
    public boolean newTreatment(String treatmentName, String description, Duration duration, double price) {
        treatmentName = treatmentName.substring(0, 1).toUpperCase() + treatmentName.substring(1); 
        Treatment newTreatment = new Treatment(treatmentName.trim(), description, duration, price); 
        providedTreatments.put(treatmentName.toLowerCase(), newTreatment); 
        return true; 
    }
    
    public String[] getTreatmentNames() {
        return (String[]) providedTreatments.getKeys();
    }
    
    //show all available treatments 
    public Treatment[] displayAllTreatments() {
        Object[] objects = providedTreatments.getValues();
        Treatment[] treatments = new Treatment[objects.length];
        for (int i = 0; i < objects.length; i++) {
            treatments[i] = (Treatment) objects[i];
        }
        return treatments;
    }
    
    public boolean deleteTreatment(Treatment treatment) {
        String treatmentName = treatment.getName();
        if(treatmentExist(treatmentName)) {
            providedTreatments.remove(treatmentName.toLowerCase());
            return true; 
        }
        return false; 
    }
    
    //return total amount of treatments provided 
    public int totalTreatments() {
        return providedTreatments.size(); 
    }
    
    public boolean emptyTreatment() {
        return providedTreatments.isEmpty(); 
    }
    
    //functions for reports 
    public Heap<Treatment> getFrequencyReport() {
        Heap<Treatment> treatmentHeap = new Heap(true); 
        
        if(emptyTreatment()) {
            return null;
        }
        
        for(Object obj : providedTreatments.getValues()) {
            if(obj instanceof Treatment treatment) { 
                treatmentHeap.insert(treatment);
            }
        }
        
        return treatmentHeap; 
    
    }
    
    public List<Treatment> getTimeAllocationReport() {
        List<Treatment> treatmentList = new List(); 
        
        if(emptyTreatment()) {
            return null;
        }
        
        for(Object obj : providedTreatments.getValues()) {
            if(obj instanceof Treatment treatment) {
                treatmentList.add(treatment); 
            }
        }
        
        int listLength = treatmentList.size(); 

        //bubble sort
        for(int i = 1; i < listLength; i++) {
            for(int j = 1; j < listLength - i + 1; j++) {
                Treatment current = treatmentList.get(j); 
                Treatment next = treatmentList.get(j+1);

                //swap
                if(current.getTimeAllocation().compareTo(next.getTimeAllocation()) < 0) {
                    treatmentList.replace(j, next); 
                    treatmentList.replace(j+1, current); 
                }
            }
        }
        return treatmentList; 
        
    }
    
    public List<Treatment> getTreatmentRevenueReport() {
        List<Treatment> treatmentList = new List(); 
        
        if(emptyTreatment()) {
            return null;
        }
        
        for(Object obj : providedTreatments.getValues()) {
            if(obj instanceof Treatment treatment) {
                treatmentList.add(treatment); 
            }
        }
        
        int listLength = treatmentList.size(); 

        //bubble sort
        for(int i = 1; i < listLength; i++) {
            for(int j = 1; j < listLength - i + 1; j++) {
                Treatment current = treatmentList.get(j); 
                Treatment next = treatmentList.get(j+1);

                //swap
                if(current.getEarned() < next.getEarned()) {
                    treatmentList.replace(j, next); 
                    treatmentList.replace(j+1, current); 
                }
            }
        }
        return treatmentList; 
        
    }
    
}