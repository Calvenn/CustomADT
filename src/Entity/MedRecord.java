package Entity;

import java.time.LocalDateTime;

public class MedRecord {
    private static int idNo = 0; 
    private String recordID;
    private Patient patient;
    private Doctor doc;
    private Medicine med;       // Link to Medicine table
    private int quantityTaken;  
    private LocalDateTime timestamp;        // DateTime of when the medicine was issued
    private boolean toSave;

    public MedRecord(Patient patient, Doctor doc, Medicine med, int quantityTaken, LocalDateTime timestamp, boolean toSave) {
        this.recordID = "MR" + String.format("%04d", generateId(toSave)); 
        this.patient = patient;
        this.doc = doc;
        this.med = med;
        this.quantityTaken = quantityTaken;
        this.timestamp = timestamp;
    }
    
    public MedRecord() {
        this.recordID = "";
        this.patient = null;
        this.doc = null;
        this.quantityTaken = 0;
        this.timestamp = LocalDateTime.now();
    }
    
    private static int generateId(boolean toSave) {
        if(toSave)
        idNo += 1; 
        return idNo; 
    }
    
    public String getRecordID() { 
        return recordID; 
    }
    public Patient getPatient() {
        return patient; 
    }
    public Doctor getDoctor() {
        return doc; 
    }
    public Medicine getMed() { 
        return med; 
    }
    public int getQuantityTaken() { 
        return quantityTaken; 
    }
    public LocalDateTime getTimestamp() {
        return timestamp; 
    }
}
