package Entity;
import java.time.LocalDateTime; 

public class TreatmentAppointment implements Comparable<TreatmentAppointment>{
    
    private final String appointmentId; 
    private Doctor doc; //need change to entity afterwards //done
    private Consultation consult;  //need change to entity afterwards //done
    private final Treatment treatment; 
    private final String room;
    private LocalDateTime treatmentTime;  
    private Severity severity; 
    private String treatmentStatus; 
    
    private final static String[] status = {
        "Scheduled", 
        "Completed", 
        "Cancelled", 
        "Rescheduled"
    }; 
    
    private static int idNo = 0; 
    
    public TreatmentAppointment(Doctor doc, Consultation consult, Treatment treatment, String room, LocalDateTime treatmentTime, Severity severity) {
        appointmentId = "T" + String.format("%04d", generateId()); 
        this.doc = doc; 
        this.consult = consult; 
        this.treatment = treatment; 
        this.room = room; 
        this.treatmentTime = treatmentTime; 
        this.severity = severity; 
        treatmentStatus = ""; 
    }
    
    private static int generateId() {
        idNo += 1; 
        return idNo; 
    }
    
    public static String[] getAllStatus() {
        return status; 
    }
    
    public String getAppointmentId() {
        return appointmentId; 
    }
     
    public Doctor getDoctor() {
        return doc; 
    }
     
    public Consultation getConsultation() {
        return consult; 
    }
    
    public Treatment getTreatment() {
        return treatment; 
    }
     
    public String getRoom() {
        return room; 
    }
     
    public LocalDateTime getTreatmentTime() {
        return treatmentTime; 
    }
    
    public Severity getSeverity() {
        return severity; 
    }
     
    public String getTreatmentStatus() {
        return treatmentStatus;  
    }
     
    public void setDoctor(Doctor doctor) {  
        this.doc = doctor; 
    }
     
    public void setTreatmentStatus(String treatmentStatus) {
        this.treatmentStatus = treatmentStatus; 
    }
    
    public void setTreatmentTime(LocalDateTime treatmentTime) {
        this.treatmentTime = treatmentTime; 
    }
    
    public void setSeverity(Severity severity) {
        this.severity = severity; 
    }
    
    @Override 
    public int compareTo(TreatmentAppointment other) {
        //this earlier than other returns neg, this later than other returns pos, 0 if same time  
        int comparism = this.treatmentTime.compareTo(other.treatmentTime);  
        if(comparism == 0) {
            comparism = this.severity.compareTo(other.severity); 
        } 
        return comparism; 
    }
    
    @Override
    public String toString() {
        return String.format("""
                Appointment Id: %s
                Doctor Id: %s
                Consultation Id: %s
                Treatment: %s
                Room: %s
                Treatment Time: %s
                Treatment Status: %s
            """, 
           this.appointmentId, this.doc, this.consult, this.treatment, this.room, this.treatmentTime, this.treatmentStatus
        ); 
    }
    
}
