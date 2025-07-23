package Entity;

public class MedRecord {
    private String recordID;
    private String patientName;
    private String consultationType; // e.g., "General", "Emergency", "Follow-up" //optional
    private Medicine medRec;       // Link to Medicine table
    private int quantityTaken;
    private String timestamp;        // DateTime of when the medicine was issued

    public MedRecord(String recordID, String patientName, String consultationType,Medicine medRec, int quantityTaken, String timestamp) {
        this.recordID = recordID;
        this.patientName = patientName;
        this.consultationType = consultationType;
        this.medRec = medRec;
        this.quantityTaken = quantityTaken;
        this.timestamp = timestamp;
    }
    public MedRecord() {
        this.recordID = "";
        this.patientName = "";
        this.consultationType = "";
        this.quantityTaken = 0;
        this.timestamp = "";
    }

    
    public String getRecordID() { 
        return recordID; 
    }
    public String getPatientName() {
        return patientName; 
    }
    public String getConsultationType() {
        return consultationType; 
    }
    public Medicine getMedID() { 
        return medRec; 
    }
    public int getQuantityTaken() { 
        return quantityTaken; 
    }
    public String getTimestamp() {
        return timestamp; 
    }
}
