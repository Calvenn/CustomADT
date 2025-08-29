package Entity;
import java.time.LocalDateTime; 
import java.time.format.DateTimeFormatter;

/**
 *
 * @author MeganYeohTzeXuan
 */

public class TreatmentAppointment extends Appointment{
    
    private final String appointmentId; 
    final private Consultation consult; 
    private final Treatment treatment; 
    private final LocalDateTime createdAt; 
    
    private final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); 
    private static int idNo = 0; 
    
    public TreatmentAppointment(Doctor doctor, Consultation consult, Treatment treatment, LocalDateTime treatmentTime) {
        super(consult.getPatient(), doctor, treatmentTime);
        appointmentId = "R" + String.format("%04d", generateId()); 
        this.consult = consult; 
        this.treatment = treatment; 
        this.createdAt = LocalDateTime.now(); 
    }
    
    public TreatmentAppointment(Doctor doctor, Consultation consult, Treatment treatment, LocalDateTime treatmentTime, LocalDateTime createdAt) {
        super(consult.getPatient(), doctor, treatmentTime);
        appointmentId = "R" + String.format("%04d", generateId()); 
        this.consult = consult; 
        this.treatment = treatment; 
        this.createdAt = createdAt; 
    }
    
    private static int generateId() {
        idNo += 1; 
        return idNo; 
    }
    
    @Override
    public String getAppointmentType() {
        return "Treatment";
    }
    
    public String getAppointmentId() {
        return appointmentId; 
    }
     
    public Consultation getConsultation() {
        return consult; 
    }
    
    public Treatment getTreatment() {
        return treatment; 
    }
    

    @Override
    public String toString() {
        return String.format("""
            Appointment ID: %s
            Doctor: %s -- %s
            Consultation: %s -- %s
            Treatment: %s
            Treatment Time: %s
            Created At: %s
            """, 
           this.appointmentId, 
           super.getDoctor().getID(),super.getDoctor().getName(), 
           this.consult.getID(), super.getPatient().getPatientName(), 
           this.treatment.getName(), 
           super.getDateTime().format(DATE_FORMAT), 
           this.createdAt.format(DATE_FORMAT)
        ); 
    }
    
}