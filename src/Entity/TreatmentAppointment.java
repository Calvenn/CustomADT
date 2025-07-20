package Entity;
import java.time.LocalDateTime; 

public class TreatmentAppointment implements Comparable<TreatmentAppointment>{
    
    private final String appointmentId; 
    private String doctorId; //need change to entity afterwards 
    private final String consultationId;  //need change to entity afterwards 
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
    
    protected TreatmentAppointment(String doctorId, String consultationId, Treatment treatment, String room, LocalDateTime treatmentTime, Severity severity) {
        appointmentId = "T" + String.format("%04d", generateId()); 
        this.doctorId = doctorId; 
        this.consultationId = consultationId; 
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
     
    public String getDoctorId() {
        return doctorId; 
    }
     
    public String getConsultationId() {
        return consultationId; 
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
     
    public void setDoctorId(String doctorId) {  //need change to entity afterwards 
        this.doctorId = doctorId; 
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
           this.appointmentId, this.doctorId, this.consultationId, this.treatment, this.room, this.treatmentTime, this.treatmentStatus
        ); 
    }
    
}
