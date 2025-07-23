package Control;
import adt.ADTHeap; 
import Entity.TreatmentAppointment; 
import Entity.Treatment; 
import Entity.Severity; 

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


public class TreatmentApptManager {
    private ADTHeap<TreatmentAppointment> treatmentHistory; 
    
    public boolean newTreatmentAppt() {
        
    }
}
