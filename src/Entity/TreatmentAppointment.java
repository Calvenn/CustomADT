package Entity;
import java.time.LocalDateTime; 

public class TreatmentAppointment implements Comparable<TreatmentAppointment>{
    
    private final String appointmentId; 
    private Doctor doctor; 
    final private Consultation consult; 
    private final Treatment treatment; 
    private final String room;
    private LocalDateTime treatmentTime;  
    
    private static int idNo = 0; 
    
    public TreatmentAppointment(Doctor doctor, Consultation consult, Treatment treatment, String room, LocalDateTime treatmentTime) {
        appointmentId = "T" + String.format("%04d", generateId()); 
        this.doctor = doctor; 
        this.consult = consult; 
        this.treatment = treatment; 
        this.room = room; 
        this.treatmentTime = treatmentTime;  
    }
    
    private static int generateId() {
        idNo += 1; 
        return idNo; 
    }
    
    public String getAppointmentId() {
        return appointmentId; 
    }
     
    public Doctor getDoctor() {
        return doctor; 
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
     
    public void setDoctor(Doctor doctor) {  
        this.doctor = doctor; 
    }
    
    public void setTreatmentTime(LocalDateTime treatmentTime) {
        this.treatmentTime = treatmentTime; 
    }
    
    @Override 
    public int compareTo(TreatmentAppointment other) {
        //this earlier than other returns neg, this later than other returns pos, 0 if same time  
        return this.treatmentTime.compareTo(other.treatmentTime);  
    }
    
    @Override
    public String toString() {
        return String.format("""
            Appointment Id: %s
            Doctor: %s
            Consultation: %s
            Treatment: %s
            Room: %s
            Treatment Time: %s
            """, 
           this.appointmentId, this.doctor, this.consult, this.treatment, this.room, this.treatmentTime
        ); 
    }
    
}
