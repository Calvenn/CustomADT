package Entity;

public class MedRecord {
    private String recordID;
    private String patientName;
    private String consultationType; // e.g., "General", "Emergency", "Follow-up"
    private String medID;       // Link to Medicine table
    private int quantityTaken;
    private String timestamp;        // DateTime of when the medicine was issued

    public MedRecord(String recordID, String patientName, String consultationType,String medID, int quantityTaken, String timestamp) {
        this.recordID = recordID;
        this.patientName = patientName;
        this.consultationType = consultationType;
        this.medID = medID;
        this.quantityTaken = quantityTaken;
        this.timestamp = timestamp;
    }
    public MedRecord() {
        this.recordID = "";
        this.patientName = "";
        this.consultationType = "";
        this.medID = "";
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
    public String getMedID() { 
        return medID; 
    }
    public int getQuantityTaken() { 
        return quantityTaken; 
    }
    public String getTimestamp() {
        return timestamp; 
    }
}
