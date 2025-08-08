package Entity;

import java.time.LocalDateTime;

public class MedRecord {
    private static int idNo = 0; 
    private String recordID;
    private Patient patient;
    private Doctor doc;
    private Medicine medRec;       // Link to Medicine table
    private int quantityTaken;  
    private LocalDateTime timestamp;        // DateTime of when the medicine was issued

    public MedRecord(Patient patient, Doctor doc, Medicine medRec, int quantityTaken, LocalDateTime timestamp) {
        this.recordID = "MR" + String.format("%04d", generateId()); 
        this.patient = patient;
        this.doc = doc;
        this.medRec = medRec;
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
    
    private static int generateId() {
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
    public Medicine getMedID() { 
        return medRec; 
    }
    public int getQuantityTaken() { 
        return quantityTaken; 
    }
    public LocalDateTime getTimestamp() {
        return timestamp; 
    }
}
