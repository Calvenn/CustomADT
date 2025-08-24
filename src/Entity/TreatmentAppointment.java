package Entity;
import java.time.LocalDateTime; 
import java.time.format.DateTimeFormatter;

public class TreatmentAppointment extends Appointment{
    
    private final String appointmentId; 
    final private Consultation consult; 
    private final Treatment treatment; 
    private final String room;
    private final LocalDateTime createdAt; 
    
    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"); 
    private static int idNo = 0; 
    
    public TreatmentAppointment(Doctor doctor, Consultation consult, Treatment treatment, String room, LocalDateTime treatmentTime) {
        super(consult.getPatient(), doctor, treatmentTime);
        appointmentId = "T" + String.format("%04d", generateId()); 
        this.consult = consult; 
        this.treatment = treatment; 
        this.room = room; 
        this.createdAt = LocalDateTime.now(); 
    }
    
    public TreatmentAppointment(Doctor doctor, Consultation consult, Treatment treatment, String room, LocalDateTime treatmentTime, LocalDateTime createdAt) {
        super(consult.getPatient(), doctor, treatmentTime);
        appointmentId = "T" + String.format("%04d", generateId()); 
        this.consult = consult; 
        this.treatment = treatment; 
        this.room = room; 
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
     
    public String getRoom() {
        return room; 
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
            Created At: %s
            """, 
           this.appointmentId, getDoctor(), this.consult, this.treatment, this.room, getDateTime().format(dateFormat), this.createdAt.format(dateFormat)
        ); 
    }
    
}
