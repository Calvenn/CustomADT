package Control;
import Entity.Doctor;
import adt.Heap;
import adt.LinkedHashMap;

// 1. Use getMinWorkDoctor(), which is doctor with least patient
// 2. (A)IF THE DOCTOR IS APPOINTED NEW TASK then use updateDoctorInc(Doctor minWorkDoctor), to update the doctor back into heap
//    (B)IF THE DOCTOR FINISH A TASK then use updateDoctorDec(Doctor minWorkDoctor), to update the doctor back into heap
/**
 * 
 * 
 * @author tanjixian
 */
public class DoctorManager{
    private LinkedHashMap<String, Doctor> doctorLookup;
    private Heap<Doctor> doctorHeap;

    // Constructor
    public DoctorManager(){
        doctorLookup = new LinkedHashMap();
        doctorHeap = new Heap<>(false);
    }
    
  // Functions
    public boolean addNewDoctor(Doctor newDoctor){
        doctorLookup.put(newDoctor.getID(), newDoctor);
        doctorHeap.insert(newDoctor);
        return true;
    }
    
    // Peek All Doctors
    public String[] peekAllDoctorID() {
        String[] allDocIDs = new String[doctorHeap.size()];
        for (int i = 0; i < doctorHeap.size(); i++) {
            allDocIDs[i] = doctorHeap.get(i).getID();
        }
        return allDocIDs;
    }
    
    // Find Doctor 
    public Doctor findDoctor(String id){
        return doctorLookup.get(id);
    }
    
    // Extract doctor from min-heap root (least workload), used in "bookAppointment"
    public Doctor getMinWorkDoctor(){
        Doctor minWorkDoctor = doctorHeap.extractRoot();
        return minWorkDoctor;
    }
    
    // Peek lowest patientCount doctor
    public Doctor peekRootDoctor(){
        return doctorHeap.peekRoot();
    } 
    
    // Update doctor workload/patientCount, used after !APPOINTED NEW TASK!
    public void updateDoctorInc(Doctor minWorkDoctor){
        int currCount = minWorkDoctor.getPatientCount();
        minWorkDoctor.setPatientCount(++currCount);
        doctorHeap.insert(minWorkDoctor);
    }

    // Update doctor workload/patientCount, used after !DOCTOR FINISH ITS TASK!
    public void updateDoctorDec(Doctor minWorkDoctor){
        int currCount = minWorkDoctor.getPatientCount();
        if (currCount > 0) minWorkDoctor.setPatientCount(--currCount);
        doctorHeap.insert(minWorkDoctor);
    }
    
    // Get size of heap for doctorHeap
    public int sizeOfDoctHeap(){
        return doctorHeap.size();
    }
    
    // Remove Doctor (from doctorHeap, staffList, staffLookup)
    public void removeDoctor(String id){
        Doctor d = doctorLookup.remove(id);
        doctorHeap.remove(d);
    }
}