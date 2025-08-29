
package Control;

import adt.Heap;
import Entity.Appointment;
import Entity.Consultation;
import Entity.Doctor;
import adt.LinkedHashMap;
import adt.List;
import adt.Queue;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author CalvenPhnuahKahHong
 */
public class AppointmentManager {
    private final Heap<Appointment> apptQueue;
    private final LinkedHashMap<String, Queue<Appointment>> missAppt;
    private final LinkedHashMap<String, List<Consultation>> consultLog;
    private final DoctorManager docManager;
    public boolean missedFlag;
    
    private final LocalTime WORK_START = LocalTime.of(8, 0);   // 08:00
    private final LocalTime WORK_END = LocalTime.of(17, 0);    // 17:00 

    
    public AppointmentManager(LinkedHashMap<String, Queue<Appointment>> missAppt, LinkedHashMap<String, List<Consultation>> consultLog, Heap<Appointment> apptQueue, DoctorManager docManager) {
        this.apptQueue = apptQueue;
        this.missAppt = missAppt;
        this.consultLog = consultLog;
        this.docManager = docManager;
        refreshHeapFromConsultations();
    }
    
    private void refreshHeapFromConsultations() {
        apptQueue.clear();

        // get all CONSULT doctors only
        List<Doctor> consultDoctors = docManager.getDoctorsByDept("CONSULT");

        if (consultDoctors == null) return;

        for (int i = 1; i <= consultDoctors.size(); i++) { 
            Doctor doctor = consultDoctors.get(i);
            List<Consultation> consultations = consultLog.get(doctor.getID());

            if (consultations == null) continue;

            for (int j = 1; j <= consultations.size(); j++) {
                Appointment consultAppt = consultations.get(j);

                if (consultAppt == null) continue;

                if (consultAppt.getDateTime() != null && !LocalDateTime.now().isAfter(consultAppt.getDateTime().plusMinutes(15)) && !apptQueueContains(consultAppt)) 
                {
                    apptQueue.insert(consultAppt);
                }
            }
        }
    }

    // Helper to check if apptQueue already has this appointment
    private boolean apptQueueContains(Appointment appt) {
        for (int i = 0; i < apptQueue.size(); i++) {
            Appointment existing = apptQueue.get(i);
            if (existing != null && existing.equals(appt)) { 
                return true; // Found duplicate
            }
        }
        return false;
    }

    //check whether the dateTime entry is within working hour
    public boolean isWithinWorkingHours(LocalDateTime time) {
        LocalTime appointmentTime = time.toLocalTime();
        return !appointmentTime.isBefore(WORK_START) && !appointmentTime.isAfter(WORK_END);
    }

    //find a available slot for doctor to book appt
    public LocalDateTime findNextAvailableSlot() {
        LocalDateTime now = LocalDateTime.now();

        // Dates to check: 1 week, 1 month, 3 months later
        LocalDate[] baseDates = new LocalDate[] {
            now.toLocalDate().plusWeeks(1),
            now.toLocalDate().plusMonths(1),
            now.toLocalDate().plusMonths(3)
        };

        for (LocalDate date : baseDates) {
            LocalDateTime currentSlot = LocalDateTime.of(date, WORK_START);

            while (!currentSlot.toLocalTime().isAfter(WORK_END)) {
                boolean conflict = false;

                // Check for time conflict with existing appointments
                for (int i = 0; i < apptQueue.size(); i++) {
                    Appointment appt = apptQueue.get(i); 
                    if (appt.getDateTime().equals(currentSlot)) {
                        conflict = true;
                        break;
                    }
                }

                if (!conflict) return currentSlot;
                currentSlot = currentSlot.plusMinutes(30); // try next 30-min slot
            }
        }

        return null; 
    }
    
    //find patient info from appointment record
    public Appointment findPatienInfo(String ic){
        for(int i=0; i< apptQueue.size();i++){
            Appointment appt = apptQueue.get(i);
            if (appt.getPatient().getPatientIC().equals(ic)){
                return appt;
            }
        }
        return null;
    }

    //book appointment
    public boolean bookAppointment(Appointment consultAppt, LocalDateTime dateTime, boolean isNew) {
        if (consultAppt instanceof Consultation) {
            Consultation newConsult = (Consultation) consultAppt;
            
            if(dateTime != null){
                if (!isWithinWorkingHours(dateTime)) {
                    return false;
                }
            
                for (int i = 0; i < apptQueue.size(); i++) {
                    Appointment appt = apptQueue.get(i);
                    if (appt.getDateTime().equals(dateTime)) {
                        return false;
                    }
                }
            }
            
            int index = findOldApptIndex(newConsult, isNew);  //it is new booking appt so find the record that with no appt dateTime
            String doctorId = newConsult.getDoctor().getID();
            List<Consultation> consultations = consultLog.get(doctorId);
            
            if(index != -1){
               Consultation consult = consultations.get(index);
               consult.setDateTime(dateTime);
            }
            
            refreshHeapFromConsultations();
            return true;
        }   
        return false;
    }
    
    //update appt
    public boolean updateAppointment(Appointment oldAppt, LocalDateTime newDateTime) {
        int index = findOldApptIndex(oldAppt, false); //it is old booking appt so find the record that with appt dateTime
        List<Consultation> consultations = findOldAppt(oldAppt);
        String oldID = consultations.get(index).getID();
            if(oldAppt instanceof Consultation oldConsult && index != -1 && oldAppt.getDateTime() != null){
                Consultation newAppt = new Consultation(oldID,oldConsult.getSeverity(), oldConsult.getPatient(), oldConsult.getDisease(), oldConsult.getNotes(), oldConsult.getDoctor(), oldConsult.getConsultTime(), newDateTime, oldConsult.getCreatedAt()); 
                consultations.replace(index, newAppt);
                apptQueue.remove(oldAppt); // for refresh purpose
                refreshHeapFromConsultations();
                return true;
            }
        return false;
    }    
    
    //cancel appt
    public boolean cancelAppointment(Appointment appt){
        int index = findOldApptIndex(appt, false); //it is old booking appt so find the record that with appt dateTime
        List<Consultation> consultations = findOldAppt(appt);
        String oldID = consultations.get(index).getID();
            if(appt instanceof Consultation oldConsult && index != -1){
                Consultation newAppt = new Consultation(oldID,oldConsult.getSeverity(), oldConsult.getPatient(), oldConsult.getDisease(), oldConsult.getNotes(), oldConsult.getDoctor(), oldConsult.getConsultTime(), null, oldConsult.getCreatedAt()); 
                consultations.replace(index, newAppt); // set date time as null, so system won't assign this record as a appt
                apptQueue.remove(appt);
                refreshHeapFromConsultations();
                return true;
            }
        return false;
    }
    
    //find the appt data
    private List<Consultation> findOldAppt(Appointment oldAppt){
        String doctorId = oldAppt.getDoctor().getID();
        List<Consultation> consultations = consultLog.get(doctorId);
        return consultations;
    }
   
    //find the appt data based on the info given
    private int findOldApptIndex(Appointment oldAppt, boolean isNew) {
        int index = -1;
        List<Consultation> consultations = findOldAppt(oldAppt);

        if (consultations == null) return -1;

        for (int i = 1; i <= consultations.size(); i++) { 
            Consultation consult = consultations.get(i);
            if (consult == null) {
                continue;
            }
            
            Consultation appt = (Consultation) oldAppt;
            boolean sameID = consult.getID().equals(appt.getID());
            boolean samePatient = consult.getPatient().getPatientIC().equals(oldAppt.getPatient().getPatientIC());
            boolean sameDoctor = consult.getDoctor().getID().equals(oldAppt.getDoctor().getID());
            boolean matchDateTime = (isNew && consult.getDateTime() == null) || (!isNew && consult.getDateTime() != null);

            if (sameID && samePatient && sameDoctor && matchDateTime) {
                index = i;
                break;
            }
        }
        return index;
    }

    //display appt record
    public void displayAllAppointments() {
        refreshHeapFromConsultations();
        apptQueue.display();
    }
    
    //display appt record by logged in doctor
    public List<Appointment> getAllAppointmentsByDoctor(String docId) {
        refreshHeapFromConsultations();
        List<Appointment> doctorAppointments = new List<>();

        for (int i = 0; i < apptQueue.size(); i++) {
            Appointment appt = apptQueue.get(i);
            if (appt.getDoctor().getID().equalsIgnoreCase(docId)) {
                doctorAppointments.add(appt);
            }
        }

        return doctorAppointments;
    }
    
    public Heap<Appointment> getAppointmentHeap() {
        refreshHeapFromConsultations();
        return apptQueue;
    }

    //get the next incoming appointment for a doctor
    public Appointment getIncomingAppointment(String docId) {
        refreshHeapFromConsultations(); // refresh heap
        Appointment nextAppt = null;

        for (int i = 0; i < apptQueue.size(); i++) {
            Appointment appt = apptQueue.get(i);

            if (!appt.getDoctor().getID().equalsIgnoreCase(docId)) continue;

            // If the appointment is in the valid range or still pending
            if (!LocalDateTime.now().isAfter(appt.getDateTime().plusMinutes(15))) {
                nextAppt = appt;
                break; // return the earliest valid one
            }
        }
        return nextAppt;
    }

    //get total appointments for a doctor (on time or 15min late)
    public int totalAppointments(String docId) {
        refreshHeapFromConsultations();
        int totalAppt = 0;

        for (int i = 0; i < apptQueue.size(); i++) {
            Appointment appt = apptQueue.get(i);

            if (!appt.getDoctor().getID().equalsIgnoreCase(docId)) continue;

            //count appointments that are upcoming or within the 15 min
            if (!LocalDateTime.now().isAfter(appt.getDateTime().plusMinutes(15))) {
                totalAppt++;
            }
        }
        return totalAppt;
    }
    
    //to refresh the flag
    public void refreshMissApptFlag(Doctor currentDoc){
        if(getNumMissedAppt(currentDoc.getID()) != 0){
            missedFlag = true;
        } else {
            missedFlag = false;
        }
    }
    
    //to refresh the collection
    public void checkMissedAppt(String docId) {
        if (!missAppt.containsKey(docId)) {
            missAppt.put(docId, new Queue<>());
        }

        Queue<Appointment> docQueue = missAppt.get(docId);

        // iterate backwards (heap index starts from 1, not 0)
        for (int i = apptQueue.size() - 1; i >= 0; i--) {
            Appointment appt = apptQueue.get(i);

            if (appt instanceof Consultation) {
                Consultation consultAppt = (Consultation) appt;

                if (consultAppt.getDoctor().getID().equalsIgnoreCase(docId) &&
                    LocalDateTime.now().isAfter(consultAppt.getDateTime().plusMinutes(15))) {

                    // enqueue missed appt
                    boolean exists = false;
                    for (int k = 0; k < docQueue.size(); k++) {
                        Appointment existing = docQueue.get(k);
                        if (existing instanceof Consultation) {
                            Consultation existingC = (Consultation) existing;
                            if (existingC.getID().equals(consultAppt.getID())) {
                                exists = true;
                                break;
                            }
                        }
                    }

                    if (!exists) {
                        docQueue.enqueue(consultAppt);
                    }
                    apptQueue.remove(consultAppt);
                }
            }
        }
    }

    //get the number of missed appt
    public int getNumMissedAppt(String docId) {
        if (missAppt.containsKey(docId)) {
            return missAppt.get(docId).size();
        }
        return 0;
    }
    
    //get all data of missed appt
    public List<Appointment> getAllMissedAppt(Doctor doc) {
        List<Appointment> result = new List<>();

        if (missAppt.containsKey(doc.getID())) {
            Queue<Appointment> delayAppt = missAppt.get(doc.getID());
            for (int i = 0; i < delayAppt.size(); i++) {
                Appointment appt = delayAppt.get(i); 
                result.add(appt);
            }
        }
        return result;
    }
    
    //get the data of missed appt based no patient ic
    public Appointment getMissedAppt(Doctor doc, String IC) {
        if (missAppt.containsKey(doc.getID())) {
            Queue<Appointment> delayAppt = missAppt.get(doc.getID());
            for (int i = 0; i < delayAppt.size(); i++) {
                Appointment appt = delayAppt.get(i);
                if (appt.getPatient().getPatientIC().equals(IC)) {
                    return appt; // return first match
                }
            }
        }
        return null; // no match found
    }

    //remove missed appt after reschedule 
    public boolean removeMissedAppt(Doctor doc, String patientIC) {
        if (!missAppt.containsKey(doc.getID())) return false;

        Queue<Appointment> originalQueue = missAppt.get(doc.getID());
        Queue<Appointment> tempQueue = new Queue<>();
        boolean removed = false;

        while (!originalQueue.isEmpty()) {
            Appointment appt = originalQueue.dequeue();
            if (!removed && appt.getPatient().getPatientIC().equals(patientIC)) {
                removed = true; 
            } else {
                tempQueue.enqueue(appt);
            }
        }

        missAppt.put(doc.getID(), tempQueue);
        return removed;
    }
    
    //Sorting
     public void sortByDate(List<Appointment> list, boolean ascending) {
        if (list.isEmpty()) return;

        // Bubble sort using ADT 
        for (int i = 1; i <= list.size(); i++) {
            for (int j = 1; j <= list.size() - i; j++) {
                Appointment a1 = list.get(j);
                Appointment a2 = list.get(j + 1);

                boolean needSwap = false;
                if (ascending && a1.getDateTime().isAfter(a2.getDateTime())) {
                    needSwap = true;
                } else if (!ascending && a1.getDateTime().isBefore(a2.getDateTime())) {
                    needSwap = true;
                }

                if (needSwap) {
                    list.replace(j, a2);
                    list.replace(j + 1, a1);
                }
            }
        }
    }
}
