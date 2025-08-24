package Control;
import Entity.Consultation;
import Entity.Doctor;
import Entity.Patient;
import adt.Heap; 
import adt.LinkedHashMap; 
import Entity.TreatmentAppointment; 
import Entity.Treatment; 
import Entity.Appointment; 
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 *
 * @author MeganYeohTzeXuan
 */

//how to make new appointment to get treatment 
//doctor go in system, open treatment appt page 
//    private final String appointmentId; -- system auto 
//    private String doctorId; -- need auto also, only choosable by time, linked with time -> need from appt 
//    private final String consultationId;  //need change to entity afterwards 
//    private final Treatment treatment; 
//    private final String room;
//    private LocalDateTime treatmentTime;  
//    private Severity severity; 
//    private String treatmentStatus; 
//doctor need select these fields 

/*
Treatment appointment new 
1. select which consultation 
2. select time 
3. check doctor availabiltiy by time -> select doctor 
4. select treatment 
5. select room 
*/
public class TreatmentApptManager {
    private Heap<Appointment> treatmentAppointment;
    private LinkedHashMap<String, TreatmentAppointment> history; 
    private final LocalTime WORK_START = LocalTime.of(8, 0);   // 08:00
    private final LocalTime WORK_END = LocalTime.of(17, 0);    // 17:00
    
    public TreatmentApptManager() {
        treatmentAppointment = new Heap<>(true);
        history = new LinkedHashMap<>(); 
    }
    
    //ON TREATMENT APPOINTMENT -----------------------------
    
    //check if chosen appt time is within working hours and after now 
    private boolean validDateTime(LocalDateTime time) {
        return (time.toLocalTime().isAfter(WORK_START) && time.toLocalTime().isBefore(WORK_END) && time.isAfter(LocalDateTime.now()));
    }
    
    //get list of treatment doctors -> send into this function to check which doctor is available, only can display 
    public Heap<Doctor> checkDoctorTime(Heap<Doctor> doctors, LocalDateTime treatmentTime) {
        return null; 
    }
    
    public boolean newTreatmentAppt(Doctor doctor, Consultation consult, Treatment treatment, String room, LocalDateTime treatmentTime) {
        if(!validDateTime(treatmentTime)) {
            throw new IllegalArgumentException("Invalid time entered.");
        }
        
        TreatmentAppointment appt = new TreatmentAppointment(doctor, consult, treatment, room, treatmentTime); 
        treatmentAppointment.insert(appt); 
        return true; 
    }
       
    public TreatmentAppointment nextAppointment() {
        return (TreatmentAppointment) treatmentAppointment.peekRoot();
    }
    
    public Heap<Appointment> upcomingAppointments() {
        return treatmentAppointment; 
    }
    
    public boolean completeTreatment(TreatmentAppointment appointment) {
        if(nextAppointment().equals(appointment)) {
            //something to set appointment status 
            TreatmentAppointment treatAppt = (TreatmentAppointment) treatmentAppointment.extractRoot();
            history.put(treatAppt.getAppointmentId(), treatAppt);
            return true; 
        }
        return false; 
    }
    
    //ON TREATMENT HISTORY -----------------------
    
    
}
