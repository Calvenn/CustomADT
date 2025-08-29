package Control;
import Entity.Consultation;
import Entity.Doctor;
import adt.List;
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

public class TreatmentApptManager {
    private final LinkedHashMap<String, Heap<Appointment>> incomingAppointment; 
    private final LinkedHashMap<String, List<TreatmentAppointment>> history;
    private final LocalTime WORK_START = LocalTime.of(8, 0);   // 08:00
    private final LocalTime WORK_END = LocalTime.of(17, 0);    // 17:00
    
    public TreatmentApptManager(LinkedHashMap<String, List<TreatmentAppointment>> history) {
        incomingAppointment = new LinkedHashMap<>();
        this.history = history;
    }
    
    //ON TREATMENT APPOINTMENT -----------------------------
    
    //check if chosen appt time is within working hours and after now 
    public boolean validDateTime(LocalDateTime time) {
        return (time.isAfter(LocalDateTime.now()) && (time.toLocalTime().isAfter(WORK_START) && time.toLocalTime().isBefore(WORK_END)));
    }
    
    public boolean newTreatmentToHeap(Doctor doctor, Consultation consult, Treatment treatment, LocalDateTime treatmentTime) {
        if(!searchDoctor(doctor)) {
            addNewDoctorKey(doctor); 
        }
        TreatmentAppointment appt = new TreatmentAppointment(doctor, consult, treatment, treatmentTime); 
        incomingAppointment.get(doctor.getID().toUpperCase()).insert(appt);
        return true; 
    }
    
    public TreatmentAppointment searchAppt(String doctorID, String trtApptID) {
        Heap<Appointment> apptHeap = incomingAppointment.get(doctorID.toUpperCase());
        if(apptHeap.isEmpty()) return null; 
        for(int i = 0; i < apptHeap.size(); i++) {
            TreatmentAppointment appt = (TreatmentAppointment) apptHeap.get(i); 
            if(appt.getAppointmentId().equalsIgnoreCase(trtApptID)) {
                return appt; 
            }
        }
        return null; //if nothing found
    }

    //called when new doctor
    private void addNewDoctorKey(Doctor doctor) {
        String doctorID = doctor.getID();
        incomingAppointment.put(doctorID, new Heap<>(false));
        history.put(doctorID, new List<>());
    }
    
    //search if doctor already exist as key 
    private boolean searchDoctor(Doctor doctor) {
        String doctorID = doctor.getID(); 
        return incomingAppointment.containsKey(doctorID) && history.containsKey(doctorID);
    }
    
    public boolean startEndTimeConflict(LocalDateTime start, LocalDateTime end, LocalDateTime time) {
        return (!time.isBefore(start) && time.isBefore(end));
    }
    
    public List<String> checkDoctorAvailability(LocalDateTime time) {
        List<String> availableDoctors = new List<>(); 
        
        //for each doctor in available doctors 
        for(Object doctor : incomingAppointment.getKeys()) {
            //will get their incoming appointment heap 
            Heap<Appointment> appt = incomingAppointment.get((String)doctor); 
            boolean available = true; 
            
            //loop through each appt in appointment heap 
            for(int i = 0; i < appt.size(); i++) {
                //need check whether each appointments have time clashed with passed in time 
                //if time is within range of booked time & duration 
                TreatmentAppointment trtAppt = (TreatmentAppointment) appt.get(i);
                LocalDateTime start = trtAppt.getDateTime();
                LocalDateTime end = start.plusMinutes(trtAppt.getTreatment().getDuration().toMinutes());
                
                //to check if time is = start/end and within start-end range 
                //time is not before start, so not at start and not earlier than start 
                //time is before end, so can be at end but cannot anytime before end 
                if(startEndTimeConflict(start, end, time)) {
                    available = false; 
                    break;
                } 
            }
            if(available == true) availableDoctors.add((String) doctor); 
        }

        return availableDoctors; 
    }
    
    public Heap<Appointment> getIncomingAppt(String doctorID) {
        Heap<Appointment> incoming = incomingAppointment.get(doctorID);
        if(incoming.isEmpty()) {
            return null;
        } else {
            return incoming; 
        }
    }
    
    //viw next appointment
    public TreatmentAppointment nextAppt(String doctorID) {
        if(incomingAppointment.get(doctorID).isEmpty()) return null; 
        Appointment next = incomingAppointment.get(doctorID).peekRoot();
        return (TreatmentAppointment) next; 
    }
    
    public void cancelTreatmentAppt(TreatmentAppointment appt) {
        incomingAppointment.get(appt.getDoctor().getID().toUpperCase()).remove((Appointment) appt); 
    }
    
    //ON TREATMENT HISTORY -----------------------
    
    //treatment already done -> will pop out of queue and go into history list 
    public boolean completeAppt(String doctorID) {
        TreatmentAppointment next = nextAppt(doctorID);
        if(next.getDateTime().isAfter(LocalDateTime.now())) {
            return false; //UI will show error, say appointment time havent reached 
        }
        incomingAppointment.get(doctorID).extractRoot();
        history.get(doctorID).add(next);
        next.getTreatment().doneTreatment();
        return true; 
    }
    
    //to receive history records from csv 
    public boolean newTreatmentApptHist(Doctor doctor, Consultation consult, Treatment treatment, LocalDateTime treatmentTime, LocalDateTime createdAt) {
        TreatmentAppointment appt = new TreatmentAppointment(doctor, consult, treatment, treatmentTime, createdAt); 
        
        String doctorID = doctor.getID().toUpperCase(); 
        if(!searchDoctor(doctor)) {
            addNewDoctorKey(doctor);
        }
        
        history.get(doctorID).add(appt);
        appt.getTreatment().doneTreatment();
        return true; 
    }
    
    public List<TreatmentAppointment> getHistoryList(String doctorID) {
        return history.get(doctorID); 
    }
    
}